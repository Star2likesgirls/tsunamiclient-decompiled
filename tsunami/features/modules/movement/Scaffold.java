package tsunami.features.modules.movement;

import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_1268;
import net.minecraft.class_1713;
import net.minecraft.class_1747;
import net.minecraft.class_1792;
import net.minecraft.class_2338;
import net.minecraft.class_243;
import net.minecraft.class_2815;
import net.minecraft.class_2848;
import net.minecraft.class_2879;
import net.minecraft.class_2885;
import net.minecraft.class_3965;
import net.minecraft.class_2828.class_2830;
import net.minecraft.class_2848.class_2849;
import tsunami.events.impl.EventMove;
import tsunami.events.impl.EventPostSync;
import tsunami.events.impl.EventSync;
import tsunami.events.impl.EventTick;
import tsunami.features.modules.Module;
import tsunami.features.modules.client.HudEditor;
import tsunami.setting.Setting;
import tsunami.setting.impl.ColorSetting;
import tsunami.setting.impl.SettingGroup;
import tsunami.utility.player.InteractionUtility;
import tsunami.utility.player.InventoryUtility;
import tsunami.utility.player.MovementUtility;
import tsunami.utility.player.SearchInvResult;
import tsunami.utility.render.BlockAnimationUtility;

public class Scaffold extends Module {
   private final Setting<Scaffold.Mode> mode;
   private final Setting<InteractionUtility.PlaceMode> placeMode;
   private final Setting<Scaffold.Switch> autoSwitch;
   private final Setting<Boolean> rotate;
   private final Setting<Boolean> lockY;
   private final Setting<Boolean> onlyNotHoldingSpace;
   private final Setting<Boolean> autoJump;
   private final Setting<Boolean> allowShift;
   private final Setting<Boolean> tower;
   private final Setting<Boolean> safewalk;
   private final Setting<Boolean> echestholding;
   private final Setting<SettingGroup> renderCategory;
   private final Setting<Boolean> render;
   private final Setting<BlockAnimationUtility.BlockRenderMode> renderMode;
   private final Setting<BlockAnimationUtility.BlockAnimationMode> animationMode;
   private final Setting<ColorSetting> renderFillColor;
   private final Setting<ColorSetting> renderLineColor;
   private final Setting<Integer> renderLineWidth;
   private final tsunami.utility.Timer timer;
   private InteractionUtility.BlockPosWithFacing currentblock;
   private int prevY;

   public Scaffold() {
      super("Scaffold", Module.Category.NONE);
      this.mode = new Setting("Mode", Scaffold.Mode.NCP);
      this.placeMode = new Setting("PlaceMode", InteractionUtility.PlaceMode.Normal, (v) -> {
         return !this.mode.is(Scaffold.Mode.Grim);
      });
      this.autoSwitch = new Setting("Switch", Scaffold.Switch.Silent);
      this.rotate = new Setting("Rotate", true);
      this.lockY = new Setting("LockY", false);
      this.onlyNotHoldingSpace = new Setting("OnlyNotHoldingSpace", false, (v) -> {
         return (Boolean)this.lockY.getValue();
      });
      this.autoJump = new Setting("AutoJump", false);
      this.allowShift = new Setting("WorkWhileSneaking", false);
      this.tower = new Setting("Tower", true, (v) -> {
         return !this.mode.is(Scaffold.Mode.Grim);
      });
      this.safewalk = new Setting("SafeWalk", true, (v) -> {
         return !this.mode.is(Scaffold.Mode.Grim);
      });
      this.echestholding = new Setting("EchestHolding", false);
      this.renderCategory = new Setting("Render", new SettingGroup(false, 0));
      this.render = (new Setting("Render", true)).addToGroup(this.renderCategory);
      this.renderMode = (new Setting("RenderMode", BlockAnimationUtility.BlockRenderMode.All)).addToGroup(this.renderCategory);
      this.animationMode = (new Setting("AnimationMode", BlockAnimationUtility.BlockAnimationMode.Fade)).addToGroup(this.renderCategory);
      this.renderFillColor = (new Setting("RenderFillColor", new ColorSetting(HudEditor.getColor(0)))).addToGroup(this.renderCategory);
      this.renderLineColor = (new Setting("RenderLineColor", new ColorSetting(HudEditor.getColor(0)))).addToGroup(this.renderCategory);
      this.renderLineWidth = (new Setting("RenderLineWidth", 2, 1, 5)).addToGroup(this.renderCategory);
      this.timer = new tsunami.utility.Timer();
   }

   public void onEnable() {
      this.prevY = -999;
   }

   @EventHandler
   public void onMove(EventMove event) {
      if (!fullNullCheck()) {
         if ((Boolean)this.safewalk.getValue() && !this.mode.is(Scaffold.Mode.Grim)) {
            double x = event.getX();
            double y = event.getY();
            double z = event.getZ();
            if (mc.field_1724.method_24828() && !mc.field_1724.field_5960) {
               double increment = 0.05D;

               while(x != 0.0D && this.isOffsetBBEmpty(x, 0.0D)) {
                  if (x < increment && x >= -increment) {
                     x = 0.0D;
                  } else if (x > 0.0D) {
                     x -= increment;
                  } else {
                     x += increment;
                  }
               }

               while(z != 0.0D && this.isOffsetBBEmpty(0.0D, z)) {
                  if (z < increment && z >= -increment) {
                     z = 0.0D;
                  } else if (z > 0.0D) {
                     z -= increment;
                  } else {
                     z += increment;
                  }
               }

               label71:
               while(true) {
                  while(true) {
                     if (x == 0.0D || z == 0.0D || !this.isOffsetBBEmpty(x, z)) {
                        break label71;
                     }

                     if (x < increment && x >= -increment) {
                        x = 0.0D;
                     } else if (x > 0.0D) {
                        x -= increment;
                     } else {
                        x += increment;
                     }

                     if (z < increment && z >= -increment) {
                        z = 0.0D;
                     } else if (z > 0.0D) {
                        z -= increment;
                     } else {
                        z += increment;
                     }
                  }
               }
            }

            event.setX(x);
            event.setY(y);
            event.setZ(z);
            event.cancel();
         }

      }
   }

   @EventHandler
   public void onTick(EventTick e) {
      if (this.mode.is(Scaffold.Mode.Grim)) {
         this.preAction();
         this.postAction();
      }

   }

   @EventHandler
   public void onPre(EventSync e) {
      if (!this.mode.is(Scaffold.Mode.Grim)) {
         this.preAction();
      }

   }

   public void preAction() {
      this.currentblock = null;
      if (!mc.field_1724.method_5715() || (Boolean)this.allowShift.getValue()) {
         if (this.prePlace(false) != -1) {
            if (mc.field_1690.field_1903.method_1434() && !MovementUtility.isMoving()) {
               this.prevY = (int)Math.floor(mc.field_1724.method_23318() - 1.0D);
            }

            if (MovementUtility.isMoving() && (Boolean)this.autoJump.getValue()) {
               if (mc.field_1690.field_1903.method_1434()) {
                  if ((Boolean)this.onlyNotHoldingSpace.getValue()) {
                     this.prevY = (int)Math.floor(mc.field_1724.method_23318() - 1.0D);
                  }
               } else if (mc.field_1724.method_24828()) {
                  mc.field_1724.method_6043();
               }
            }

            class_2338 blockPos2 = (Boolean)this.lockY.getValue() && this.prevY != -999 ? class_2338.method_49637(mc.field_1724.method_23317(), (double)this.prevY, mc.field_1724.method_23321()) : new class_2338((int)Math.floor(mc.field_1724.method_23317()), (int)Math.floor(mc.field_1724.method_23318() - 1.0D), (int)Math.floor(mc.field_1724.method_23321()));
            if (mc.field_1687.method_8320(blockPos2).method_45474()) {
               this.currentblock = this.checkNearBlocksExtended(blockPos2);
               if (this.currentblock != null && (Boolean)this.rotate.getValue() && !this.mode.is(Scaffold.Mode.Grim)) {
                  class_243 hitVec = (new class_243((double)this.currentblock.position().method_10263() + 0.5D, (double)this.currentblock.position().method_10264() + 0.5D, (double)this.currentblock.position().method_10260() + 0.5D)).method_1019((new class_243(this.currentblock.facing().method_23955())).method_1021(0.5D));
                  float[] rotations = InteractionUtility.calculateAngle(hitVec);
                  mc.field_1724.method_36456(rotations[0]);
                  mc.field_1724.method_36457(rotations[1]);
               }

            }
         }
      }
   }

   @EventHandler
   public void onPost(EventPostSync e) {
      if (!this.mode.is(Scaffold.Mode.Grim)) {
         this.postAction();
      }

   }

   public void postAction() {
      float offset = this.mode.is(Scaffold.Mode.Grim) ? 0.3F : 0.2F;
      if (!mc.field_1687.method_20812(mc.field_1724, mc.field_1724.method_5829().method_1009((double)(-offset), 0.0D, (double)(-offset)).method_989(0.0D, -0.5D, 0.0D)).iterator().hasNext()) {
         if (this.currentblock != null) {
            int prevItem = this.prePlace(true);
            if (prevItem != -1) {
               if (mc.field_1724.field_3913.field_3904 && !MovementUtility.isMoving() && (Boolean)this.tower.getValue() && !this.mode.is(Scaffold.Mode.Grim)) {
                  mc.field_1724.method_18800(0.0D, 0.42D, 0.0D);
                  if (this.timer.passedMs(1500L)) {
                     mc.field_1724.method_18800(mc.field_1724.method_18798().field_1352, -0.28D, mc.field_1724.method_18798().field_1350);
                     this.timer.reset();
                  }
               } else {
                  this.timer.reset();
               }

               class_3965 bhr;
               if (this.mode.is(Scaffold.Mode.StrictNCP)) {
                  bhr = new class_3965((new class_243((double)this.currentblock.position().method_10263() + 0.5D, (double)this.currentblock.position().method_10264() + 0.5D, (double)this.currentblock.position().method_10260() + 0.5D)).method_1019((new class_243(this.currentblock.facing().method_23955())).method_1021(0.5D)), this.currentblock.facing(), this.currentblock.position(), false);
               } else {
                  bhr = new class_3965(new class_243((double)this.currentblock.position().method_10263() + Math.random(), (double)((float)this.currentblock.position().method_10264() + 0.99F), (double)this.currentblock.position().method_10260() + Math.random()), this.currentblock.facing(), this.currentblock.position(), false);
               }

               float[] rotations = InteractionUtility.calculateAngle(bhr.method_17784());
               boolean sneak = InteractionUtility.needSneak(mc.field_1687.method_8320(bhr.method_17777()).method_26204()) && !mc.field_1724.method_5715();
               if (sneak) {
                  mc.field_1724.field_3944.method_52787(new class_2848(mc.field_1724, class_2849.field_12979));
               }

               if (this.mode.is(Scaffold.Mode.Grim)) {
                  this.sendPacket(new class_2830(mc.field_1724.method_23317(), mc.field_1724.method_23318(), mc.field_1724.method_23321(), rotations[0], rotations[1], mc.field_1724.method_24828()));
               }

               if (this.placeMode.getValue() == InteractionUtility.PlaceMode.Packet && !this.mode.is(Scaffold.Mode.Grim)) {
                  boolean finalIsOffhand = prevItem == -2;
                  this.sendSequencedPacket((id) -> {
                     return new class_2885(finalIsOffhand ? class_1268.field_5810 : class_1268.field_5808, bhr, id);
                  });
               } else {
                  mc.field_1761.method_2896(mc.field_1724, prevItem == -2 ? class_1268.field_5810 : class_1268.field_5808, bhr);
               }

               mc.field_1724.field_3944.method_52787(new class_2879(prevItem == -2 ? class_1268.field_5810 : class_1268.field_5808));
               this.prevY = this.currentblock.position().method_10264();
               if (sneak) {
                  mc.field_1724.field_3944.method_52787(new class_2848(mc.field_1724, class_2849.field_12984));
               }

               if (this.mode.is(Scaffold.Mode.Grim)) {
                  this.sendPacket(new class_2830(mc.field_1724.method_23317(), mc.field_1724.method_23318(), mc.field_1724.method_23321(), mc.field_1724.method_36454(), mc.field_1724.method_36455(), mc.field_1724.method_24828()));
               }

               if ((Boolean)this.render.getValue()) {
                  BlockAnimationUtility.renderBlock(this.currentblock.position(), ((ColorSetting)this.renderLineColor.getValue()).getColorObject(), (Integer)this.renderLineWidth.getValue(), ((ColorSetting)this.renderFillColor.getValue()).getColorObject(), (BlockAnimationUtility.BlockAnimationMode)this.animationMode.getValue(), (BlockAnimationUtility.BlockRenderMode)this.renderMode.getValue());
               }

               this.postPlace(prevItem);
            }

         }
      }
   }

   private InteractionUtility.BlockPosWithFacing checkNearBlocksExtended(class_2338 blockPos) {
      InteractionUtility.BlockPosWithFacing ret = null;
      ret = InteractionUtility.checkNearBlocks(blockPos);
      if (ret != null) {
         return ret;
      } else {
         ret = InteractionUtility.checkNearBlocks(blockPos.method_10069(-1, 0, 0));
         if (ret != null) {
            return ret;
         } else {
            ret = InteractionUtility.checkNearBlocks(blockPos.method_10069(1, 0, 0));
            if (ret != null) {
               return ret;
            } else {
               ret = InteractionUtility.checkNearBlocks(blockPos.method_10069(0, 0, 1));
               if (ret != null) {
                  return ret;
               } else {
                  ret = InteractionUtility.checkNearBlocks(blockPos.method_10069(0, 0, -1));
                  if (ret != null) {
                     return ret;
                  } else {
                     ret = InteractionUtility.checkNearBlocks(blockPos.method_10069(-2, 0, 0));
                     if (ret != null) {
                        return ret;
                     } else {
                        ret = InteractionUtility.checkNearBlocks(blockPos.method_10069(2, 0, 0));
                        if (ret != null) {
                           return ret;
                        } else {
                           ret = InteractionUtility.checkNearBlocks(blockPos.method_10069(0, 0, 2));
                           if (ret != null) {
                              return ret;
                           } else {
                              ret = InteractionUtility.checkNearBlocks(blockPos.method_10069(0, 0, -2));
                              if (ret != null) {
                                 return ret;
                              } else {
                                 ret = InteractionUtility.checkNearBlocks(blockPos.method_10069(0, -1, 0));
                                 if (ret != null) {
                                    return ret;
                                 } else {
                                    ret = InteractionUtility.checkNearBlocks(blockPos.method_10069(1, -1, 0));
                                    if (ret != null) {
                                       return ret;
                                    } else {
                                       ret = InteractionUtility.checkNearBlocks(blockPos.method_10069(-1, -1, 0));
                                       if (ret != null) {
                                          return ret;
                                       } else {
                                          ret = InteractionUtility.checkNearBlocks(blockPos.method_10069(0, -1, 1));
                                          return ret != null ? ret : InteractionUtility.checkNearBlocks(blockPos.method_10069(0, -1, -1));
                                       }
                                    }
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private int prePlace(boolean swap) {
      if (mc.field_1724 != null && mc.field_1687 != null && mc.field_1761 != null) {
         class_1792 var3 = mc.field_1724.method_6079().method_7909();
         class_1747 bi;
         if (var3 instanceof class_1747) {
            bi = (class_1747)var3;
            if (!bi.method_7711().method_9564().method_45474()) {
               return -2;
            }
         }

         var3 = mc.field_1724.method_6047().method_7909();
         if (var3 instanceof class_1747) {
            bi = (class_1747)var3;
            if (!bi.method_7711().method_9564().method_45474()) {
               return mc.field_1724.method_31548().field_7545;
            }
         }

         int prevSlot = mc.field_1724.method_31548().field_7545;
         SearchInvResult hotbarResult = InventoryUtility.findInHotBar((i) -> {
            class_1792 patt0$temp = i.method_7909();
            boolean var10000;
            if (patt0$temp instanceof class_1747) {
               class_1747 bi = (class_1747)patt0$temp;
               if (!bi.method_7711().method_9564().method_45474()) {
                  var10000 = true;
                  return var10000;
               }
            }

            var10000 = false;
            return var10000;
         });
         SearchInvResult invResult = InventoryUtility.findInInventory((i) -> {
            class_1792 patt0$temp = i.method_7909();
            boolean var10000;
            if (patt0$temp instanceof class_1747) {
               class_1747 bi = (class_1747)patt0$temp;
               if (!bi.method_7711().method_9564().method_45474()) {
                  var10000 = true;
                  return var10000;
               }
            }

            var10000 = false;
            return var10000;
         });
         if (swap) {
            switch(((Scaffold.Switch)this.autoSwitch.getValue()).ordinal()) {
            case 0:
            case 1:
               hotbarResult.switchTo();
               break;
            case 2:
               if (invResult.found()) {
                  prevSlot = invResult.slot();
                  mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, prevSlot, mc.field_1724.method_31548().field_7545, class_1713.field_7791, mc.field_1724);
                  this.sendPacket(new class_2815(mc.field_1724.field_7512.field_7763));
               }
            }
         }

         return prevSlot;
      } else {
         return -1;
      }
   }

   private void postPlace(int prevSlot) {
      if (prevSlot != -1 && prevSlot != -2) {
         switch(((Scaffold.Switch)this.autoSwitch.getValue()).ordinal()) {
         case 1:
            InventoryUtility.switchTo(prevSlot);
            break;
         case 2:
            mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, prevSlot, mc.field_1724.method_31548().field_7545, class_1713.field_7791, mc.field_1724);
            this.sendPacket(new class_2815(mc.field_1724.field_7512.field_7763));
         }

      }
   }

   private boolean isOffsetBBEmpty(double x, double z) {
      return !mc.field_1687.method_20812(mc.field_1724, mc.field_1724.method_5829().method_1009(-0.1D, 0.0D, -0.1D).method_989(x, -2.0D, z)).iterator().hasNext();
   }

   private static enum Mode {
      NCP,
      StrictNCP,
      Grim;

      // $FF: synthetic method
      private static Scaffold.Mode[] $values() {
         return new Scaffold.Mode[]{NCP, StrictNCP, Grim};
      }
   }

   private static enum Switch {
      Normal,
      Silent,
      Inventory,
      None;

      // $FF: synthetic method
      private static Scaffold.Switch[] $values() {
         return new Scaffold.Switch[]{Normal, Silent, Inventory, None};
      }
   }
}
