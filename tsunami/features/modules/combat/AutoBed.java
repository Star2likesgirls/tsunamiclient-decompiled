package tsunami.features.modules.combat;

import java.awt.Color;
import java.util.Iterator;
import java.util.Objects;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_1268;
import net.minecraft.class_1657;
import net.minecraft.class_1703;
import net.minecraft.class_1713;
import net.minecraft.class_1714;
import net.minecraft.class_1748;
import net.minecraft.class_2244;
import net.minecraft.class_2304;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_2680;
import net.minecraft.class_2815;
import net.minecraft.class_2885;
import net.minecraft.class_3959;
import net.minecraft.class_3965;
import net.minecraft.class_4587;
import net.minecraft.class_479;
import net.minecraft.class_516;
import net.minecraft.class_8786;
import net.minecraft.class_239.class_240;
import net.minecraft.class_2828.class_2831;
import net.minecraft.class_3959.class_242;
import net.minecraft.class_3959.class_3960;
import org.jetbrains.annotations.NotNull;
import tsunami.core.Managers;
import tsunami.core.manager.client.ModuleManager;
import tsunami.events.impl.EventPostSync;
import tsunami.events.impl.EventSync;
import tsunami.events.impl.PlayerUpdateEvent;
import tsunami.features.modules.Module;
import tsunami.features.modules.client.ClientSettings;
import tsunami.features.modules.client.HudEditor;
import tsunami.injection.accesors.IClientPlayerEntity;
import tsunami.setting.Setting;
import tsunami.setting.impl.ColorSetting;
import tsunami.setting.impl.SettingGroup;
import tsunami.utility.Timer;
import tsunami.utility.math.MathUtility;
import tsunami.utility.player.InteractionUtility;
import tsunami.utility.player.InventoryUtility;
import tsunami.utility.player.PlayerUtility;
import tsunami.utility.player.SearchInvResult;
import tsunami.utility.render.Render2DEngine;
import tsunami.utility.render.Render3DEngine;
import tsunami.utility.world.ExplosionUtility;

public final class AutoBed extends Module {
   private final Setting<InteractionUtility.Interact> interactMode;
   public static final Setting<Float> range = new Setting("Range", 4.0F, 2.0F, 6.0F);
   public static final Setting<Float> wallRange = new Setting("WallRange", 4.0F, 0.0F, 6.0F);
   public static final Setting<Integer> placeDelay = new Setting("PlaceDelay", 100, 0, 1000);
   public static final Setting<Integer> explodeDelay = new Setting("ExplodeDelay", 100, 0, 1000);
   public static final Setting<Float> minDamage = new Setting("MinDamage", 8.0F, 0.0F, 25.0F);
   public static final Setting<Float> maxSelfDamage = new Setting("MaxSelfDamage", 4.0F, 0.0F, 25.0F);
   private final Setting<Boolean> dimCheck;
   public final Setting<Boolean> switchToHotbar;
   public final Setting<Boolean> oldPlace;
   public final Setting<Boolean> autoSwap;
   public final Setting<Boolean> autoCraft;
   public static final Setting<Integer> minBeds = new Setting("MinBeds", 4, 0, 10);
   public static final Setting<Integer> bedsPerCraft = new Setting("BedsPerCraft", 8, 1, 27);
   private final Setting<SettingGroup> renderCategory;
   private final Setting<Boolean> render;
   private final Setting<Boolean> rselfDamage;
   private final Setting<Boolean> drawDamage;
   private final Setting<ColorSetting> fillColor;
   private final Setting<ColorSetting> lineColor;
   private final Setting<ColorSetting> textColor;
   private class_1657 target;
   private AutoBed.BedData bestBed;
   private AutoBed.BedData bestPos;
   private float rotationYaw;
   private float rotationPitch;
   private final Timer placeTimer;
   private final Timer explodeTimer;

   public AutoBed() {
      super("AutoBed", Module.Category.NONE);
      this.interactMode = new Setting("InteractMode", InteractionUtility.Interact.Vanilla);
      this.dimCheck = new Setting("DimensionCheck", false);
      this.switchToHotbar = new Setting("SwitchToHotbar", true);
      this.oldPlace = new Setting("1.12 Place", false);
      this.autoSwap = new Setting("AutoSwap", true);
      this.autoCraft = new Setting("AutoCraft", true);
      this.renderCategory = new Setting("Render", new SettingGroup(false, 0));
      this.render = (new Setting("Render", true)).addToGroup(this.renderCategory);
      this.rselfDamage = (new Setting("SelfDamage", true)).addToGroup(this.renderCategory);
      this.drawDamage = (new Setting("RenderDamage", true)).addToGroup(this.renderCategory);
      this.fillColor = (new Setting("Fill", new ColorSetting(Render2DEngine.injectAlpha(HudEditor.getColor(0), 150)))).addToGroup(this.renderCategory);
      this.lineColor = (new Setting("Line", new ColorSetting(HudEditor.getColor(0)))).addToGroup(this.renderCategory);
      this.textColor = (new Setting("Text", new ColorSetting(Color.WHITE))).addToGroup(this.renderCategory);
      this.placeTimer = new Timer();
      this.explodeTimer = new Timer();
   }

   @EventHandler
   public void onSync(EventSync e) {
      if (this.bestBed != null || this.bestPos != null) {
         mc.field_1724.method_36456(this.rotationYaw);
         mc.field_1724.method_36457(this.rotationPitch);
      }

   }

   @EventHandler
   public void onPlayerUpdate(PlayerUpdateEvent e) {
      this.target = this.findTarget();
      if (mc.field_1687.method_8597().comp_648() && (Boolean)this.dimCheck.getValue()) {
         this.disable(ClientSettings.isRu() ? "Кровати не взрываются в этом измерении!" : "Beds don't explode in this dimension!");
      } else if (this.target != null && (this.target.method_29504() || this.target.method_6032() < 0.0F)) {
         this.target = null;
      } else {
         this.bestBed = this.findBedToExplode();
         this.bestPos = this.findBlockToPlace();
         if (this.bestBed != null || this.bestPos != null) {
            float[] angle = InteractionUtility.calculateAngle(((AutoBed.BedData)Objects.requireNonNullElseGet(this.bestPos, () -> {
               return this.bestBed;
            })).hitResult().method_17784());
            this.rotationYaw = angle[0];
            this.rotationPitch = angle[1];
            ModuleManager.rotations.fixRotation = this.rotationYaw;
         }

         if ((Boolean)this.autoCraft.getValue()) {
            if (InventoryUtility.getBedsCount() <= (Integer)minBeds.getValue()) {
               this.craftBed();
               return;
            }

            if (mc.field_1724.field_7512 instanceof class_1714) {
               this.sendPacket(new class_2815(mc.field_1724.field_7512.field_7763));
               mc.field_1724.method_3137();
            }
         }

      }
   }

   @EventHandler
   public void onPostSync(EventPostSync e) {
      if (!(mc.field_1724.method_6047().method_7909() instanceof class_1748) && (Boolean)this.autoSwap.getValue() && this.bestPos != null) {
         SearchInvResult hotBarResult = InventoryUtility.findBedInHotBar();
         if (hotBarResult.found()) {
            hotBarResult.switchTo();
         } else if ((Boolean)this.switchToHotbar.getValue()) {
            SearchInvResult invResult = InventoryUtility.findBed();
            if (invResult.found() && !(mc.field_1755 instanceof class_479)) {
               mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, invResult.slot(), mc.field_1724.method_31548().field_7545, class_1713.field_7791, mc.field_1724);
               this.sendPacket(new class_2815(mc.field_1724.field_7512.field_7763));
            }
         }
      }

      if (this.bestBed != null && this.explodeTimer.passedMs((long)(Integer)explodeDelay.getValue())) {
         this.sendSequencedPacket((id) -> {
            return new class_2885(class_1268.field_5808, this.bestBed.hitResult(), id);
         });
         mc.field_1724.method_6104(class_1268.field_5808);
         this.explodeTimer.reset();
      }

      if (mc.field_1724.method_6047().method_7909() instanceof class_1748) {
         if (this.bestPos != null && this.placeTimer.passedMs((long)(Integer)placeDelay.getValue()) && !(mc.field_1687.method_8320(this.bestPos.hitResult().method_17777().method_10084()).method_26204() instanceof class_2244)) {
            float angle2 = InteractionUtility.calculateAngle(this.bestPos.hitResult.method_17777().method_46558(), this.bestPos.hitResult.method_17777().method_10093(this.bestPos.dir).method_46558())[0];
            this.sendPacket(new class_2831(angle2, 0.0F, mc.field_1724.method_24828()));
            float prevYaw = mc.field_1724.method_36454();
            mc.field_1724.method_36456(angle2);
            mc.field_1724.field_5982 = angle2;
            ((IClientPlayerEntity)mc.field_1724).setLastYaw(angle2);
            this.sendSequencedPacket((id) -> {
               return new class_2885(class_1268.field_5808, this.bestPos.hitResult(), id);
            });
            mc.field_1724.method_6104(class_1268.field_5808);
            this.placeTimer.reset();
            mc.field_1724.method_36456(prevYaw);
         }

      }
   }

   public void onRender3D(class_4587 stack) {
      if (this.bestPos != null && (Boolean)this.render.getValue()) {
         class_238 box = new class_238(this.bestPos.hitResult.method_17777().method_10084());
         class_238 box2 = new class_238(this.bestPos.hitResult.method_17777().method_10084().method_10093(this.bestPos.dir));
         class_238 finalBox = box.method_991(box2).method_35578(box.field_1325 - 0.44999998807907104D);
         float var10000 = MathUtility.round2((double)this.bestPos.damage());
         String dmg = var10000 + ((Boolean)this.rselfDamage.getValue() ? " / " + MathUtility.round2((double)this.bestPos.selfDamage()) : "");
         Render3DEngine.OUTLINE_QUEUE.add(new Render3DEngine.OutlineAction(finalBox, ((ColorSetting)this.lineColor.getValue()).getColorObject(), 2.0F));
         Render3DEngine.FILLED_QUEUE.add(new Render3DEngine.FillAction(finalBox, ((ColorSetting)this.fillColor.getValue()).getColorObject()));
         if ((Boolean)this.drawDamage.getValue()) {
            Render3DEngine.drawTextIn3D(dmg, finalBox.method_1005(), 0.0D, 0.1D, 0.0D, ((ColorSetting)this.textColor.getValue()).getColorObject());
         }
      }

   }

   private class_1657 findTarget() {
      return Managers.COMBAT.getNearestTarget(12.0F);
   }

   private AutoBed.BedData findBedToExplode() {
      int intRange = (int)(Math.floor((double)(Float)range.getValue()) + 1.0D);
      Iterable<class_2338> blocks_ = class_2338.method_25996(new class_2338(class_2338.method_49638(mc.field_1724.method_19538()).method_10084()), intRange, intRange, intRange);
      AutoBed.BedData bestData = null;
      Iterator var4 = blocks_.iterator();

      while(true) {
         class_3965 bhr;
         float damage;
         float selfDamage;
         do {
            do {
               do {
                  do {
                     class_2338 b;
                     class_2680 state;
                     do {
                        do {
                           if (!var4.hasNext()) {
                              return bestData;
                           }

                           b = (class_2338)var4.next();
                           state = mc.field_1687.method_8320(b);
                        } while(!(PlayerUtility.squaredDistanceFromEyes(b.method_46558()) <= range.getPow2Value()));
                     } while(!(state.method_26204() instanceof class_2244));

                     bhr = this.getInteractResult(b);
                     mc.field_1687.method_8650(b, false);
                     damage = ExplosionUtility.getExplosionDamage(b.method_46558().method_1031(0.0D, -0.5D, 0.0D), this.target, false);
                     selfDamage = ExplosionUtility.getExplosionDamage(b.method_46558().method_1031(0.0D, -0.5D, 0.0D), mc.field_1724, false);
                     mc.field_1687.method_8501(b, state);
                  } while(damage < (Float)minDamage.getValue());
               } while(selfDamage > (Float)maxSelfDamage.getValue());
            } while(selfDamage > mc.field_1724.method_6032() + mc.field_1724.method_6067() + 2.0F);
         } while(bestData != null && bestData.damage > damage);

         if (bhr != null) {
            bestData = new AutoBed.BedData(bhr, damage, selfDamage, bhr.method_17780());
         }
      }
   }

   private AutoBed.BedData findBlockToPlace() {
      int intRange = (int)(Math.floor((double)(Float)range.getValue()) + 1.0D);
      Iterable<class_2338> blocks_ = class_2338.method_25996(new class_2338(class_2338.method_49638(mc.field_1724.method_19538()).method_10084()), intRange, intRange, intRange);
      AutoBed.BedData bestData = null;
      Iterator var4 = blocks_.iterator();

      while(true) {
         class_2338 b;
         class_3965 bhr;
         float damage;
         float selfDamage;
         do {
            do {
               do {
                  do {
                     class_3965 wallCheck;
                     do {
                        do {
                           class_2680 state;
                           do {
                              class_2680 state2;
                              do {
                                 if (!var4.hasNext()) {
                                    return bestData;
                                 }

                                 b = (class_2338)var4.next();
                                 state = mc.field_1687.method_8320(b);
                                 state2 = mc.field_1687.method_8320(b.method_10084());
                              } while(!(PlayerUtility.squaredDistanceFromEyes(b.method_46558()) <= range.getPow2Value()));

                              if (state2.method_26204() instanceof class_2244 && !this.placeTimer.passedMs(1500L) && this.bestPos != null) {
                                 return this.bestPos;
                              }
                           } while(state.method_45474());

                           bhr = InteractionUtility.getPlaceResult(b.method_10084(), (InteractionUtility.Interact)this.interactMode.getValue(), false);
                        } while(bhr == null);

                        wallCheck = mc.field_1687.method_17742(new class_3959(InteractionUtility.getEyesPos(mc.field_1724), bhr.method_17784(), class_3960.field_17558, class_242.field_1348, mc.field_1724));
                     } while(wallCheck != null && wallCheck.method_17783() == class_240.field_1332 && wallCheck.method_17777() != b);

                     damage = ExplosionUtility.getExplosionDamage(b.method_10084().method_46558().method_1031(0.0D, -0.5D, 0.0D), this.target, false);
                     selfDamage = ExplosionUtility.getExplosionDamage(b.method_10084().method_46558().method_1031(0.0D, -0.5D, 0.0D), mc.field_1724, false);
                  } while(damage < (Float)minDamage.getValue());
               } while(selfDamage > (Float)maxSelfDamage.getValue());
            } while(selfDamage > mc.field_1724.method_6032() + mc.field_1724.method_6067() + 2.0F);
         } while(bestData != null && bestData.damage > damage);

         float bestDirdmg = 0.0F;
         class_2350 bestDir = null;
         class_2350[] var14 = class_2350.values();
         int var15 = var14.length;

         for(int var16 = 0; var16 < var15; ++var16) {
            class_2350 dir = var14[var16];
            if (dir != class_2350.field_11033 && dir != class_2350.field_11036) {
               class_2338 offset = b.method_10084().method_10093(dir);
               if (mc.field_1687.method_8320(offset).method_45474() && (!(Boolean)this.oldPlace.getValue() || !mc.field_1687.method_8320(b.method_10093(dir)).method_45474())) {
                  float dirdamage = ExplosionUtility.getExplosionDamage(offset.method_46558().method_1031(0.0D, -0.5D, 0.0D), this.target, false);
                  float dirSelfDamage = ExplosionUtility.getExplosionDamage(offset.method_46558().method_1031(0.0D, -0.5D, 0.0D), mc.field_1724, false);
                  if (dirdamage > bestDirdmg && dirSelfDamage <= (Float)maxSelfDamage.getValue()) {
                     bestDir = dir;
                     bestDirdmg = dirdamage;
                  }
               }
            }
         }

         bestData = bestDir == null ? null : new AutoBed.BedData(bhr, damage, selfDamage, bestDir);
      }
   }

   public void craftBed() {
      int intRange = (int)(Math.floor((double)(Float)range.getValue()) + 1.0D);
      Iterable<class_2338> blocks_ = class_2338.method_25996(new class_2338(class_2338.method_49638(mc.field_1724.method_19538()).method_10084()), intRange, intRange, intRange);
      Iterator var3 = blocks_.iterator();

      while(true) {
         label37:
         while(true) {
            class_3965 result;
            do {
               class_2338 b;
               class_2680 state;
               do {
                  if (!var3.hasNext()) {
                     return;
                  }

                  b = (class_2338)var3.next();
                  state = mc.field_1687.method_8320(b);
               } while(!(state.method_26204() instanceof class_2304));

               result = this.getInteractResult(b);
            } while(result == null);

            class_1703 var8 = mc.field_1724.field_7512;
            if (var8 instanceof class_1714) {
               class_1714 craft = (class_1714)var8;
               mc.field_1724.method_3130().method_14884(craft.method_30264(), true);
               Iterator var14 = mc.field_1724.method_3130().method_1393().iterator();

               while(true) {
                  while(true) {
                     if (!var14.hasNext()) {
                        continue label37;
                     }

                     class_516 results = (class_516)var14.next();
                     Iterator var10 = results.method_2648(true).iterator();

                     while(var10.hasNext()) {
                        class_8786<?> recipe = (class_8786)var10.next();
                        if (recipe.comp_1933().method_8110(results.method_48479()).method_7909() instanceof class_1748) {
                           for(int i = 0; i < (Integer)bedsPerCraft.getValue(); ++i) {
                              mc.field_1761.method_2912(mc.field_1724.field_7512.field_7763, recipe, false);
                           }

                           mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, 0, 0, class_1713.field_7794, mc.field_1724);
                           break;
                        }
                     }
                  }
               }
            } else {
               float[] angle = InteractionUtility.calculateAngle(result.method_17784());
               mc.field_1724.method_36456(angle[0]);
               mc.field_1724.method_36457(angle[1]);
               this.sendSequencedPacket((id) -> {
                  return new class_2885(class_1268.field_5808, result, id);
               });
            }
         }
      }
   }

   public class_3965 getInteractResult(@NotNull class_2338 bp) {
      float bestDistance = 999.0F;
      class_3965 bestResult = null;

      float bestDistance2;
      for(bestDistance2 = 0.0F; bestDistance2 < 1.0F; bestDistance2 += 0.25F) {
         for(float y = 0.0F; y < 0.5F; y += 0.125F) {
            for(float z = 0.0F; z < 1.0F; z += 0.25F) {
               class_243 point = new class_243((double)((float)bp.method_10263() + bestDistance2), (double)((float)bp.method_10264() + y), (double)((float)bp.method_10260() + z));
               float distance = PlayerUtility.squaredDistanceFromEyes(point);
               class_3965 wallCheck = mc.field_1687.method_17742(new class_3959(InteractionUtility.getEyesPos(mc.field_1724), point, class_3960.field_17558, class_242.field_1348, mc.field_1724));
               if (wallCheck == null || wallCheck.method_17783() != class_240.field_1332 || wallCheck.method_17777() == bp || !(distance > wallRange.getPow2Value())) {
                  class_3965 result = ExplosionUtility.rayCastBlock(new class_3959(InteractionUtility.getEyesPos(mc.field_1724), point, class_3960.field_17558, class_242.field_1348, mc.field_1724), bp);
                  if (!(distance > range.getPow2Value()) && distance < bestDistance && result != null && result.method_17783() == class_240.field_1332) {
                     bestResult = result;
                     bestDistance = distance;
                  }
               }
            }
         }
      }

      bestDistance2 = 999.0F;
      class_2350 bestDirection = null;
      if (mc.field_1724.method_33571().method_10214() > (double)bp.method_10084().method_10264()) {
         bestDirection = class_2350.field_11036;
      } else if (mc.field_1724.method_33571().method_10214() < (double)bp.method_10264()) {
         bestDirection = class_2350.field_11033;
      } else {
         class_2350[] var13 = class_2350.values();
         int var14 = var13.length;

         for(int var15 = 0; var15 < var14; ++var15) {
            class_2350 dir = var13[var15];
            class_243 directionVec = new class_243((double)bp.method_10263() + 0.5D + (double)dir.method_10163().method_10263() * 0.5D, (double)bp.method_10264() + 0.5D + (double)dir.method_10163().method_10264() * 0.5D, (double)bp.method_10260() + 0.5D + (double)dir.method_10163().method_10260() * 0.5D);
            float distance = PlayerUtility.squaredDistanceFromEyes(directionVec);
            if (bestDistance2 > distance) {
               bestDirection = dir;
               bestDistance2 = distance;
            }
         }
      }

      if (bestResult == null) {
         return null;
      } else {
         return new class_3965(bestResult.method_17784(), bestDirection, bestResult.method_17777(), false);
      }
   }

   private static record BedData(class_3965 hitResult, float damage, float selfDamage, class_2350 dir) {
      private BedData(class_3965 hitResult, float damage, float selfDamage, class_2350 dir) {
         this.hitResult = hitResult;
         this.damage = damage;
         this.selfDamage = selfDamage;
         this.dir = dir;
      }

      public class_3965 hitResult() {
         return this.hitResult;
      }

      public float damage() {
         return this.damage;
      }

      public float selfDamage() {
         return this.selfDamage;
      }

      public class_2350 dir() {
         return this.dir;
      }
   }
}
