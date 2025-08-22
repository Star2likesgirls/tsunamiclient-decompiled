package tsunami.features.modules.misc;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_1268;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_1667;
import net.minecraft.class_1684;
import net.minecraft.class_1802;
import net.minecraft.class_2338;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_2868;
import net.minecraft.class_2879;
import net.minecraft.class_2886;
import net.minecraft.class_3532;
import net.minecraft.class_3959;
import net.minecraft.class_3965;
import net.minecraft.class_239.class_240;
import net.minecraft.class_3959.class_242;
import net.minecraft.class_3959.class_3960;
import org.jetbrains.annotations.NotNull;
import tsunami.core.manager.client.ModuleManager;
import tsunami.events.impl.EventEntitySpawn;
import tsunami.events.impl.EventPostSync;
import tsunami.events.impl.EventSync;
import tsunami.features.modules.Module;
import tsunami.features.modules.client.ClientSettings;
import tsunami.features.modules.combat.Aura;
import tsunami.features.modules.combat.AutoCrystal;
import tsunami.setting.Setting;
import tsunami.setting.impl.BooleanSettingGroup;
import tsunami.utility.Timer;
import tsunami.utility.math.MathUtility;
import tsunami.utility.player.MovementUtility;

public class PearlChaser extends Module {
   private final Setting<BooleanSettingGroup> stopMotion = new Setting("StopMotion", new BooleanSettingGroup(false));
   private final Setting<Boolean> legitStop;
   private final Setting<Boolean> pauseAura;
   private final Setting<Boolean> onlyOnGround;
   private final Setting<Boolean> noMove;
   private final Setting<Boolean> onlyTarget;
   private Runnable postSyncAction;
   private final Timer delayTimer;
   private class_2338 targetBlock;
   private int lastPearlId;
   private int lastOurPearlId;
   private HashMap<class_1657, Long> targets;

   public PearlChaser() {
      super("PearlChaser", Module.Category.NONE);
      this.legitStop = (new Setting("LegitStop", false)).addToGroup(this.stopMotion);
      this.pauseAura = new Setting("PauseAura", false);
      this.onlyOnGround = new Setting("OnlyOnGround", false);
      this.noMove = new Setting("NoMove", false);
      this.onlyTarget = new Setting("OnlyTarget", false);
      this.delayTimer = new Timer();
      this.targets = new HashMap();
   }

   @EventHandler
   public void onEntitySpawn(EventEntitySpawn e) {
      if (e.getEntity() instanceof class_1684) {
         mc.field_1687.method_18456().stream().min(Comparator.comparingDouble((p) -> {
            return p.method_5707(e.getEntity().method_19538());
         })).ifPresent((player) -> {
            if (player.equals(mc.field_1724)) {
               this.lastOurPearlId = e.getEntity().method_5628();
            }

         });
      }

   }

   @EventHandler(
      priority = -100
   )
   public void onSync(EventSync event) {
      class_1297 ent;
      if ((Boolean)this.onlyTarget.getValue()) {
         if (Aura.target != null && ModuleManager.aura.isEnabled()) {
            ent = Aura.target;
            if (ent instanceof class_1657) {
               class_1657 pl = (class_1657)ent;
               if (!this.targets.containsKey(pl)) {
                  this.targets.put(pl, System.currentTimeMillis());
               }
            }
         }

         if (AutoCrystal.target != null && ModuleManager.autoCrystal.isEnabled()) {
            class_1657 var9 = AutoCrystal.target;
            if (var9 instanceof class_1657 && !this.targets.containsKey(var9)) {
               this.targets.put(var9, System.currentTimeMillis());
            }
         }

         (new HashMap(this.targets)).forEach((k, v) -> {
            if (System.currentTimeMillis() - v > 10000L) {
               this.targets.remove(k);
            }

         });
      }

      if (!(mc.field_1724.method_6032() < 5.0F)) {
         if (this.delayTimer.passedMs(1000L)) {
            Iterator var7 = mc.field_1687.method_18112().iterator();

            while(var7.hasNext()) {
               ent = (class_1297)var7.next();
               if (ent instanceof class_1684 && ent.method_5628() != this.lastPearlId && ent.method_5628() != this.lastOurPearlId) {
                  mc.field_1687.method_18456().stream().filter((e) -> {
                     return this.targets.containsKey(e) || !(Boolean)this.onlyTarget.getValue();
                  }).min(Comparator.comparingDouble((p) -> {
                     return p.method_5707(ent.method_19538());
                  })).ifPresent((player) -> {
                     if (!player.equals(mc.field_1724)) {
                        this.targetBlock = this.calcTrajectory(ent);
                        this.lastPearlId = ent.method_5628();
                     }

                  });
               }
            }

            if (this.targetBlock != null) {
               if (!(mc.field_1724.method_5707(this.targetBlock.method_46558()) < 49.0D)) {
                  float rotationPitch = (float)(-Math.toDegrees((double)this.calcTrajectory(this.targetBlock)));
                  float rotationYaw = (float)Math.toDegrees(Math.atan2((double)((float)this.targetBlock.method_10260() + 0.5F) - mc.field_1724.method_23321(), (double)((float)this.targetBlock.method_10263() + 0.5F) - mc.field_1724.method_23317())) - 90.0F;
                  class_2338 tracedBP = this.checkTrajectory(rotationYaw, rotationPitch);
                  if (tracedBP != null && !(this.targetBlock.method_19770(tracedBP.method_46558()) > 36.0D)) {
                     if ((Boolean)this.pauseAura.getValue() && ModuleManager.aura.isEnabled()) {
                        ModuleManager.aura.pause();
                     }

                     if (!(Boolean)this.onlyOnGround.getValue() || mc.field_1724.method_24828()) {
                        if (!(Boolean)this.noMove.getValue() || !MovementUtility.isMoving()) {
                           if (((BooleanSettingGroup)this.stopMotion.getValue()).isEnabled()) {
                              if (!(Boolean)this.legitStop.getValue()) {
                                 mc.field_1724.method_18800(0.0D, 0.0D, 0.0D);
                              }

                              mc.field_1690.field_1894.method_23481(false);
                              mc.field_1690.field_1881.method_23481(false);
                              mc.field_1690.field_1913.method_23481(false);
                              mc.field_1690.field_1849.method_23481(false);
                              mc.field_1724.field_3913.field_3905 = 0.0F;
                              mc.field_1724.field_3913.field_3907 = 0.0F;
                           } else {
                              this.sendMessage(ClientSettings.isRu() ? "Догоняем перл! Позиция X:" + tracedBP.method_10263() + " Y:" + tracedBP.method_10264() + " Z:" + tracedBP.method_10260() + " Углы Y:" + rotationYaw + " P:" + rotationPitch : "Chasing pearl on X:" + tracedBP.method_10263() + " Y:" + tracedBP.method_10264() + " Z:" + tracedBP.method_10260() + " Angle Y:" + rotationYaw + " P:" + rotationPitch);
                              mc.field_1724.method_36456(rotationYaw);
                              mc.field_1724.method_36457(MathUtility.clamp(rotationPitch, -89.0F, 89.0F));
                              float yaw = mc.field_1724.method_36454();
                              float pitch = mc.field_1724.method_36455();
                              this.postSyncAction = () -> {
                                 int epSlot = this.findEPSlot();
                                 int originalSlot = mc.field_1724.method_31548().field_7545;
                                 if (epSlot != -1) {
                                    mc.field_1724.method_31548().field_7545 = epSlot;
                                    this.sendPacket(new class_2868(epSlot));
                                    this.sendSequencedPacket((id) -> {
                                       return new class_2886(class_1268.field_5808, id, yaw, pitch);
                                    });
                                    this.sendPacket(new class_2879(class_1268.field_5808));
                                    mc.field_1724.method_31548().field_7545 = originalSlot;
                                    this.sendPacket(new class_2868(originalSlot));
                                 }

                              };
                              this.targetBlock = null;
                              this.delayTimer.reset();
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   @EventHandler
   public void onPostSync(EventPostSync event) {
      if (this.postSyncAction != null) {
         this.postSyncAction.run();
         this.postSyncAction = null;
      }

   }

   private int findEPSlot() {
      int epSlot = -1;
      if (mc.field_1724.method_6047().method_7909() == class_1802.field_8634) {
         epSlot = mc.field_1724.method_31548().field_7545;
      }

      if (epSlot == -1) {
         for(int l = 0; l < 9; ++l) {
            if (mc.field_1724.method_31548().method_5438(l).method_7909() == class_1802.field_8634) {
               epSlot = l;
               break;
            }
         }
      }

      return epSlot;
   }

   private float calcTrajectory(@NotNull class_2338 bp) {
      double a = Math.hypot((double)((float)bp.method_10263() + 0.5F) - mc.field_1724.method_23317(), (double)((float)bp.method_10260() + 0.5F) - mc.field_1724.method_23321());
      double y = 6.125D * ((double)((float)bp.method_10264() + 1.0F) - (mc.field_1724.method_23318() + (double)mc.field_1724.method_18381(mc.field_1724.method_18376())));
      y = 0.05000000074505806D * (0.05000000074505806D * a * a + y);
      y = Math.sqrt(9.37890625D - y);
      double d = 3.0625D - y;
      y = Math.atan2(d * d + y, 0.05000000074505806D * a);
      d = Math.atan2(d, 0.05000000074505806D * a);
      return (float)Math.min(y, d);
   }

   private class_2338 calcTrajectory(class_1297 e) {
      return this.traceTrajectory(e.method_23317(), e.method_23318(), e.method_23321(), e.method_18798().field_1352, e.method_18798().field_1351, e.method_18798().field_1350);
   }

   private class_2338 checkTrajectory(float yaw, float pitch) {
      if (Float.isNaN(pitch)) {
         return null;
      } else {
         float yawRad = yaw / 180.0F * 3.1415927F;
         float pitchRad = pitch / 180.0F * 3.1415927F;
         double x = mc.field_1724.method_23317() - (double)(class_3532.method_15362(yawRad) * 0.16F);
         double y = mc.field_1724.method_23318() + (double)mc.field_1724.method_18381(mc.field_1724.method_18376()) - 0.1000000014901161D;
         double z = mc.field_1724.method_23321() - (double)(class_3532.method_15374(yawRad) * 0.16F);
         double motionX = (double)(-class_3532.method_15374(yawRad) * class_3532.method_15362(pitchRad) * 0.4F);
         double motionY = (double)(-class_3532.method_15374(pitchRad) * 0.4F);
         double motionZ = (double)(class_3532.method_15362(yawRad) * class_3532.method_15362(pitchRad) * 0.4F);
         float distance = class_3532.method_15355((float)(motionX * motionX + motionY * motionY + motionZ * motionZ));
         motionX /= (double)distance;
         motionY /= (double)distance;
         motionZ /= (double)distance;
         motionX *= 1.5D;
         motionY *= 1.5D;
         motionZ *= 1.5D;
         if (!mc.field_1724.method_24828()) {
            motionY += mc.field_1724.method_18798().method_10214();
         }

         return this.traceTrajectory(x, y, z, motionX, motionY, motionZ);
      }
   }

   private class_2338 traceTrajectory(double x, double y, double z, double mx, double my, double mz) {
      for(int i = 0; i < 300; ++i) {
         class_243 lastPos = new class_243(x, y, z);
         x += mx;
         y += my;
         z += mz;
         mx *= 0.99D;
         my *= 0.99D;
         mz *= 0.99D;
         my -= 0.029999999329447746D;
         class_243 pos = new class_243(x, y, z);
         class_3965 bhr = mc.field_1687.method_17742(new class_3959(lastPos, pos, class_3960.field_17559, class_242.field_1348, mc.field_1724));
         if (bhr != null && bhr.method_17783() == class_240.field_1332) {
            return bhr.method_17777();
         }

         Iterator var17 = mc.field_1687.method_18112().iterator();

         while(var17.hasNext()) {
            class_1297 ent = (class_1297)var17.next();
            if (!(ent instanceof class_1667) && ent != mc.field_1724 && !(ent instanceof class_1684) && ent.method_5829().method_994(new class_238(x - 0.3D, y - 0.3D, z - 0.3D, x + 0.3D, y + 0.3D, z + 0.2D))) {
               return null;
            }
         }

         if (y <= -65.0D) {
            break;
         }
      }

      return null;
   }
}
