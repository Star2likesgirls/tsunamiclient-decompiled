package tsunami.features.modules.combat;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_1268;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_1531;
import net.minecraft.class_1657;
import net.minecraft.class_1753;
import net.minecraft.class_243;
import net.minecraft.class_2886;
import net.minecraft.class_3532;
import net.minecraft.class_4587;
import net.minecraft.class_239.class_240;
import org.jetbrains.annotations.NotNull;
import tsunami.core.Managers;
import tsunami.core.manager.client.ModuleManager;
import tsunami.events.impl.EventSync;
import tsunami.events.impl.PlayerUpdateEvent;
import tsunami.features.modules.Module;
import tsunami.setting.Setting;
import tsunami.utility.Timer;
import tsunami.utility.math.MathUtility;
import tsunami.utility.math.PredictUtility;
import tsunami.utility.render.Render2DEngine;
import tsunami.utility.render.Render3DEngine;

public final class AimBot extends Module {
   private final Setting<AimBot.Mode> mode;
   private final Setting<AimBot.Rotation> rotation;
   private final Setting<Float> aimRange;
   private final Setting<Integer> aimStrength;
   private final Setting<Integer> aimSmooth;
   private final Setting<Integer> aimtime;
   private final Setting<Boolean> ignoreWalls;
   private final Setting<Boolean> ignoreTeam;
   private final Setting<Integer> reactionTime;
   private final Setting<Boolean> ignoreInvisible;
   private final Setting<Float> rotYawRandom;
   private final Setting<Float> rotPitchRandom;
   private final Setting<Float> predict;
   private final Setting<Integer> delay;
   private final Setting<Integer> fov;
   private final Setting<Integer> predictTicks;
   private final Setting<AimBot.Bone> part;
   private class_1297 target;
   private float rotationYaw;
   private float rotationPitch;
   private float assistAcceleration;
   private int aimTicks;
   private Timer visibleTime;

   public AimBot() {
      super("AimBot", Module.Category.NONE);
      this.mode = new Setting("Mode", AimBot.Mode.BowAim);
      this.rotation = new Setting("Rotation", AimBot.Rotation.Silent, (v) -> {
         return this.mode.getValue() != AimBot.Mode.AimAssist;
      });
      this.aimRange = new Setting("Range", 20.0F, 1.0F, 30.0F, (v) -> {
         return this.mode.getValue() != AimBot.Mode.AimAssist;
      });
      this.aimStrength = new Setting("AimStrength", 30, 1, 100, (v) -> {
         return this.mode.getValue() == AimBot.Mode.AimAssist;
      });
      this.aimSmooth = new Setting("AimSmooth", 45, 1, 180, (v) -> {
         return this.mode.getValue() == AimBot.Mode.AimAssist;
      });
      this.aimtime = new Setting("AimTime", 2, 1, 10, (v) -> {
         return this.mode.getValue() == AimBot.Mode.AimAssist;
      });
      this.ignoreWalls = new Setting("IgnoreWalls", true, (v) -> {
         return this.mode.getValue() == AimBot.Mode.CSAim || this.mode.is(AimBot.Mode.AimAssist);
      });
      this.ignoreTeam = new Setting("IgnoreTeam", true, (v) -> {
         return this.mode.getValue() == AimBot.Mode.CSAim || this.mode.is(AimBot.Mode.AimAssist);
      });
      this.reactionTime = new Setting("ReactionTime", 80, 1, 500, (v) -> {
         return this.mode.getValue() == AimBot.Mode.AimAssist && !(Boolean)this.ignoreWalls.getValue();
      });
      this.ignoreInvisible = new Setting("IgnoreInvis", false, (v) -> {
         return this.mode.is(AimBot.Mode.AimAssist);
      });
      this.rotYawRandom = new Setting("YawRandom", 0.0F, 0.0F, 3.0F, (v) -> {
         return this.mode.getValue() == AimBot.Mode.CSAim;
      });
      this.rotPitchRandom = new Setting("PitchRandom", 0.0F, 0.0F, 3.0F, (v) -> {
         return this.mode.getValue() == AimBot.Mode.CSAim;
      });
      this.predict = new Setting("AimPredict", 0.5F, 0.5F, 8.0F, (v) -> {
         return this.mode.getValue() == AimBot.Mode.CSAim;
      });
      this.delay = new Setting("Shoot delay", 5, 0, 10, (v) -> {
         return this.mode.getValue() == AimBot.Mode.CSAim;
      });
      this.fov = new Setting("FOV", 65, 10, 360, (v) -> {
         return this.mode.getValue() == AimBot.Mode.CSAim;
      });
      this.predictTicks = new Setting("PredictTicks", 2, 0, 20, (v) -> {
         return this.mode.getValue() == AimBot.Mode.BowAim;
      });
      this.part = new Setting("Bone", AimBot.Bone.Head, (v) -> {
         return this.mode.getValue() == AimBot.Mode.CSAim;
      });
      this.aimTicks = 0;
      this.visibleTime = new Timer();
   }

   @EventHandler
   public void onPlayerUpdate(PlayerUpdateEvent event) {
      class_1657 nearestTarget;
      float delta_yaw;
      float deltaYaw;
      double gcdFix;
      if (this.mode.getValue() == AimBot.Mode.BowAim) {
         if (!(mc.field_1724.method_6030().method_7909() instanceof class_1753)) {
            return;
         }

         nearestTarget = Managers.COMBAT.getTargetByFOV(128.0F);
         if (nearestTarget == null) {
            return;
         }

         delta_yaw = (float)(mc.field_1724.method_6030().method_7935(mc.field_1724) - mc.field_1724.method_6048()) / 20.0F;
         delta_yaw = (delta_yaw * delta_yaw + delta_yaw * 2.0F) / 3.0F;
         if (delta_yaw >= 1.0F) {
            delta_yaw = 1.0F;
         }

         deltaYaw = (float)(-Math.toDegrees((double)this.calculateArc(nearestTarget, (double)(delta_yaw * 3.0F))));
         if (Float.isNaN(deltaYaw)) {
            return;
         }

         class_1657 predictedEntity = PredictUtility.predictPlayer(nearestTarget, (Integer)this.predictTicks.getValue());
         gcdFix = predictedEntity.method_23317() - predictedEntity.field_6014;
         double iZ = predictedEntity.method_23321() - predictedEntity.field_5969;
         double distance = (double)mc.field_1724.method_5739(predictedEntity);
         distance -= distance % 2.0D;
         gcdFix = distance / 2.0D * gcdFix * (mc.field_1724.method_5624() ? 1.3D : 1.1D);
         iZ = distance / 2.0D * iZ * (mc.field_1724.method_5624() ? 1.3D : 1.1D);
         this.rotationYaw = (float)Math.toDegrees(Math.atan2(predictedEntity.method_23321() + iZ - mc.field_1724.method_23321(), predictedEntity.method_23317() + gcdFix - mc.field_1724.method_23317())) - 90.0F;
         this.rotationPitch = deltaYaw;
      } else if (this.mode.getValue() == AimBot.Mode.CSAim) {
         this.calcThread();
      } else {
         if (mc.field_1765.method_17783() == class_240.field_1331) {
            ++this.aimTicks;
         } else {
            this.aimTicks = 0;
         }

         if (this.aimTicks >= (Integer)this.aimtime.getValue()) {
            this.assistAcceleration = 0.0F;
            return;
         }

         nearestTarget = Managers.COMBAT.getNearestTarget(5.0F);
         this.assistAcceleration += (float)(Integer)this.aimStrength.getValue() / 10000.0F;
         if (nearestTarget != null) {
            if (!mc.field_1724.method_6057(nearestTarget) && !(Boolean)this.ignoreWalls.getValue()) {
               this.visibleTime.reset();
            }

            if (!this.visibleTime.passedMs((long)(Integer)this.reactionTime.getValue())) {
               this.rotationYaw = Float.NaN;
               return;
            }

            if (Float.isNaN(this.rotationYaw)) {
               this.rotationYaw = mc.field_1724.method_36454();
            }

            delta_yaw = class_3532.method_15393((float)class_3532.method_15338(Math.toDegrees(Math.atan2(nearestTarget.method_33571().field_1350 - mc.field_1724.method_23321(), nearestTarget.method_33571().field_1352 - mc.field_1724.method_23317())) - 90.0D) - this.rotationYaw);
            if (delta_yaw > 180.0F) {
               delta_yaw -= 180.0F;
            }

            deltaYaw = class_3532.method_15363(class_3532.method_15379(delta_yaw), (float)(-(Integer)this.aimSmooth.getValue()), (float)(Integer)this.aimSmooth.getValue());
            float newYaw = this.rotationYaw + (delta_yaw > 0.0F ? deltaYaw : -deltaYaw);
            gcdFix = Math.pow((Double)mc.field_1690.method_42495().method_41753() * 0.6D + 0.2D, 3.0D) * 1.2D;
            this.rotationYaw = (float)((double)newYaw - (double)(newYaw - this.rotationYaw) % gcdFix);
         } else {
            this.rotationYaw = Float.NaN;
         }
      }

      if (!Float.isNaN(this.rotationYaw)) {
         ModuleManager.rotations.fixRotation = this.rotationYaw;
      }

   }

   @EventHandler
   public void onSync(EventSync event) {
      if (!this.mode.is(AimBot.Mode.AimAssist)) {
         if (this.mode.is(AimBot.Mode.CSAim)) {
            if (this.target == null || !mc.field_1724.method_6057(this.target) && !(Boolean)this.ignoreWalls.getValue()) {
               this.rotationYaw = mc.field_1724.method_36454();
               this.rotationPitch = mc.field_1724.method_36455();
            } else if (mc.field_1724.field_6012 % (Integer)this.delay.getValue() == 0) {
               event.addPostAction(() -> {
                  this.sendSequencedPacket((id) -> {
                     return new class_2886(class_1268.field_5808, id, mc.field_1724.method_36454(), mc.field_1724.method_36455());
                  });
               });
            }
         }

         if ((this.target != null || this.mode.getValue() == AimBot.Mode.BowAim && mc.field_1724.method_6030().method_7909() instanceof class_1753) && this.rotation.getValue() == AimBot.Rotation.Silent) {
            mc.field_1724.method_36456(this.rotationYaw);
            mc.field_1724.method_36457(this.rotationPitch);
         }

      }
   }

   public void onEnable() {
      this.target = null;
      this.rotationYaw = mc.field_1724.method_36454();
      this.rotationPitch = mc.field_1724.method_36455();
   }

   public void onRender3D(class_4587 stack) {
      if (this.mode.getValue() == AimBot.Mode.AimAssist) {
         if (!Float.isNaN(this.rotationYaw)) {
            mc.field_1724.method_36456((float)Render2DEngine.interpolate((double)mc.field_1724.method_36454(), (double)this.rotationYaw, (double)this.assistAcceleration));
         }
      } else {
         if (this.target != null && (mc.field_1724.method_6057(this.target) || (Boolean)this.ignoreWalls.getValue())) {
            if (this.rotation.getValue() == AimBot.Rotation.Client) {
               mc.field_1724.method_36456((float)Render2DEngine.interpolate((double)mc.field_1724.field_5982, (double)this.rotationYaw, (double)Render3DEngine.getTickDelta()));
               mc.field_1724.method_36457((float)Render2DEngine.interpolate((double)mc.field_1724.field_6004, (double)this.rotationPitch, (double)Render3DEngine.getTickDelta()));
            }
         } else if (this.mode.getValue() == AimBot.Mode.CSAim) {
            this.rotationYaw = mc.field_1724.method_36454();
            this.rotationPitch = mc.field_1724.method_36455();
         }

         if (this.rotation.getValue() == AimBot.Rotation.Client && this.mode.getValue() == AimBot.Mode.BowAim && mc.field_1724.method_6030().method_7909() instanceof class_1753) {
            mc.field_1724.method_36456((float)Render2DEngine.interpolate((double)mc.field_1724.field_5982, (double)this.rotationYaw, (double)Render3DEngine.getTickDelta()));
            mc.field_1724.method_36457((float)Render2DEngine.interpolate((double)mc.field_1724.field_6004, (double)this.rotationPitch, (double)Render3DEngine.getTickDelta()));
         }

      }
   }

   private float calculateArc(@NotNull class_1657 target, double duration) {
      double yArc = target.method_23318() + (double)target.method_18381(target.method_18376()) - (mc.field_1724.method_23318() + (double)mc.field_1724.method_18381(mc.field_1724.method_18376()));
      double dX = target.method_23317() - mc.field_1724.method_23317();
      double dZ = target.method_23321() - mc.field_1724.method_23321();
      double dirRoot = Math.sqrt(dX * dX + dZ * dZ);
      return this.calculateArc(duration, dirRoot, yArc);
   }

   private float calculateArc(double d, double dr, double y) {
      y = 2.0D * y * d * d;
      y = 0.05000000074505806D * (0.05000000074505806D * dr * dr + y);
      y = Math.sqrt(d * d * d * d - y);
      d = d * d - y;
      y = Math.atan2(d * d + y, 0.05000000074505806D * dr);
      d = Math.atan2(d, 0.05000000074505806D * dr);
      return (float)Math.min(y, d);
   }

   private void calcThread() {
      if (this.target == null) {
         this.findTarget();
      } else if (this.skipEntity(this.target)) {
         this.target = null;
      } else {
         class_243 targetVec = this.getResolvedPos(this.target).method_1031(0.0D, (double)((AimBot.Bone)this.part.getValue()).getH(), 0.0D);
         if (targetVec != null) {
            float delta_yaw = class_3532.method_15393((float)class_3532.method_15338(Math.toDegrees(Math.atan2(targetVec.field_1350 - mc.field_1724.method_23321(), targetVec.field_1352 - mc.field_1724.method_23317())) - 90.0D) - this.rotationYaw);
            float delta_pitch = (float)(-Math.toDegrees(Math.atan2(targetVec.field_1351 - (mc.field_1724.method_19538().field_1351 + (double)mc.field_1724.method_18381(mc.field_1724.method_18376())), Math.sqrt(Math.pow(targetVec.field_1352 - mc.field_1724.method_23317(), 2.0D) + Math.pow(targetVec.field_1350 - mc.field_1724.method_23321(), 2.0D))))) - this.rotationPitch;
            if (delta_yaw > 180.0F) {
               delta_yaw -= 180.0F;
            }

            float deltaYaw = class_3532.method_15363(class_3532.method_15379(delta_yaw), MathUtility.random(-40.0F, -60.0F), MathUtility.random(40.0F, 60.0F));
            float newYaw = this.rotationYaw + (delta_yaw > 0.0F ? deltaYaw : -deltaYaw) + MathUtility.random(-(Float)this.rotYawRandom.getValue(), (Float)this.rotYawRandom.getValue());
            float newPitch = class_3532.method_15363(this.rotationPitch + class_3532.method_15363(delta_pitch, MathUtility.random(-10.0F, -20.0F), MathUtility.random(10.0F, 20.0F)), -90.0F, 90.0F) + MathUtility.random(-(Float)this.rotPitchRandom.getValue(), (Float)this.rotPitchRandom.getValue());
            double gcdFix = Math.pow((Double)mc.field_1690.method_42495().method_41753() * 0.6D + 0.2D, 3.0D) * 8.0D * 0.15000000596046448D;
            this.rotationYaw = (float)((double)newYaw - (double)(newYaw - this.rotationYaw) % gcdFix);
            this.rotationPitch = (float)((double)newPitch - (double)(newPitch - this.rotationPitch) % gcdFix);
         }
      }
   }

   public void findTarget() {
      List<class_1297> first_stage = new CopyOnWriteArrayList();
      Iterator var2 = mc.field_1687.method_18112().iterator();

      class_1297 best_entity;
      while(var2.hasNext()) {
         best_entity = (class_1297)var2.next();
         if (!this.skipEntity(best_entity)) {
            first_stage.add(best_entity);
         }
      }

      float best_fov = (float)(Integer)this.fov.getValue();
      best_entity = null;
      Iterator var4 = first_stage.iterator();

      while(var4.hasNext()) {
         class_1297 ent = (class_1297)var4.next();
         float temp_fov = Math.abs((float)class_3532.method_15338(Math.toDegrees(Math.atan2(ent.method_23321() - mc.field_1724.method_23321(), ent.method_23317() - mc.field_1724.method_23317())) - 90.0D) - class_3532.method_15393(mc.field_1724.method_36454()));
         if (temp_fov < best_fov) {
            best_entity = ent;
            best_fov = temp_fov;
         }
      }

      this.target = best_entity;
   }

   private boolean skipEntity(class_1297 entity) {
      if (entity instanceof class_1309) {
         class_1309 ent = (class_1309)entity;
         if (ent.method_29504()) {
            return true;
         } else if (!entity.method_5805()) {
            return true;
         } else if (entity instanceof class_1531) {
            return true;
         } else if (ModuleManager.antiBot.isEnabled() && AntiBot.bots.contains(entity)) {
            return true;
         } else if (entity instanceof class_1657) {
            class_1657 pl = (class_1657)entity;
            if (entity == mc.field_1724) {
               return true;
            } else if (entity.method_5767() && (Boolean)this.ignoreInvisible.getValue()) {
               return true;
            } else if (Managers.FRIEND.isFriend(pl)) {
               return true;
            } else if (Math.abs(this.getYawToEntityNew(entity)) > (float)(Integer)this.fov.getValue()) {
               return true;
            } else if (pl.method_22861() == mc.field_1724.method_22861() && (Boolean)this.ignoreTeam.getValue() && mc.field_1724.method_22861() != 16777215) {
               return true;
            } else {
               return mc.field_1724.method_5707(this.getResolvedPos(entity)) > (double)this.aimRange.getPow2Value();
            }
         } else {
            return true;
         }
      } else {
         return true;
      }
   }

   public float getYawToEntityNew(@NotNull class_1297 entity) {
      return this.getYawBetween(mc.field_1724.method_36454(), mc.field_1724.method_23317(), mc.field_1724.method_23321(), entity.method_23317(), entity.method_23321());
   }

   public float getYawBetween(float yaw, double srcX, double srcZ, double destX, double destZ) {
      double xDist = destX - srcX;
      double zDist = destZ - srcZ;
      float yaw1 = (float)(StrictMath.atan2(zDist, xDist) * 180.0D / 3.141592653589793D) - 90.0F;
      return yaw + class_3532.method_15393(yaw1 - yaw);
   }

   private class_243 getResolvedPos(@NotNull class_1297 pl) {
      return new class_243(pl.method_23317() + (pl.method_23317() - pl.field_6014) * (double)(Float)this.predict.getValue(), pl.method_23318(), pl.method_23321() + (pl.method_23321() - pl.field_5969) * (double)(Float)this.predict.getValue());
   }

   private static enum Mode {
      CSAim,
      AimAssist,
      BowAim;

      // $FF: synthetic method
      private static AimBot.Mode[] $values() {
         return new AimBot.Mode[]{CSAim, AimAssist, BowAim};
      }
   }

   private static enum Rotation {
      Client,
      Silent;

      // $FF: synthetic method
      private static AimBot.Rotation[] $values() {
         return new AimBot.Rotation[]{Client, Silent};
      }
   }

   private static enum Bone {
      Head(1.7F),
      Neck(1.5F),
      Torso(1.0F),
      Tights(0.8F),
      Feet(0.25F);

      private final float h;

      private Bone(float h) {
         this.h = h;
      }

      public float getH() {
         return this.h;
      }

      // $FF: synthetic method
      private static AimBot.Bone[] $values() {
         return new AimBot.Bone[]{Head, Neck, Torso, Tights, Feet};
      }
   }
}
