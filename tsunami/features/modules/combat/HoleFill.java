package tsunami.features.modules.combat;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_1657;
import net.minecraft.class_1747;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_2382;
import net.minecraft.class_3959;
import net.minecraft.class_3965;
import net.minecraft.class_239.class_240;
import net.minecraft.class_3959.class_242;
import net.minecraft.class_3959.class_3960;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import tsunami.core.Managers;
import tsunami.core.manager.client.ModuleManager;
import tsunami.events.impl.EventTick;
import tsunami.features.modules.Module;
import tsunami.features.modules.client.ClientSettings;
import tsunami.features.modules.client.HudEditor;
import tsunami.setting.Setting;
import tsunami.setting.impl.ColorSetting;
import tsunami.setting.impl.SettingGroup;
import tsunami.utility.Timer;
import tsunami.utility.math.PredictUtility;
import tsunami.utility.player.InteractionUtility;
import tsunami.utility.render.BlockAnimationUtility;
import tsunami.utility.world.HoleUtility;

public final class HoleFill extends Module {
   private final Setting<InteractionUtility.Rotate> rotate;
   private final Setting<InteractionUtility.Interact> interactMode;
   private final Setting<Float> placeRange;
   private final Setting<Float> placeWallRange;
   private final Setting<Integer> actionShift;
   private final Setting<Integer> actionInterval;
   private final Setting<Boolean> jumpDisable;
   private final Setting<HoleFill.FillBlocks> blocks;
   private final Setting<SettingGroup> fill;
   private final Setting<Boolean> selfFill;
   private final Setting<HoleFill.SelfFillMode> selfFillMode;
   private final Setting<Boolean> fillSingle;
   private final Setting<Boolean> fillDouble;
   private final Setting<Boolean> fillQuad;
   private final Setting<HoleFill.Mode> mode;
   private final Setting<Float> rangeToTarget;
   private final Setting<Boolean> autoDisable;
   private final Setting<InteractionUtility.PlaceMode> placeMode;
   private final Setting<SettingGroup> renderCategory;
   private final Setting<BlockAnimationUtility.BlockRenderMode> renderMode;
   private final Setting<BlockAnimationUtility.BlockAnimationMode> animationMode;
   private final Setting<ColorSetting> renderFillColor;
   private final Setting<ColorSetting> renderLineColor;
   private final Setting<Integer> renderLineWidth;
   private static final class_2382[] HOLE_VECTORS = new class_2382[]{new class_2382(-1, 0, -1), new class_2382(1, 0, -1), new class_2382(-1, 0, 1), new class_2382(1, 0, 1)};
   private boolean burrowWasEnabled;
   public static final Timer inactivityTimer = new Timer();
   private int tickCounter;
   private boolean selfFillNeed;

   public HoleFill() {
      super("HoleFill", Module.Category.NONE);
      this.rotate = new Setting("Rotate", InteractionUtility.Rotate.None);
      this.interactMode = new Setting("Interact Mode", InteractionUtility.Interact.Vanilla);
      this.placeRange = new Setting("Range", 5.0F, 1.0F, 6.0F);
      this.placeWallRange = new Setting("WallRange", 5.0F, 1.0F, 6.0F);
      this.actionShift = new Setting("BLock Per Tick", 1, 1, 4);
      this.actionInterval = new Setting("Delay", 0, 0, 5);
      this.jumpDisable = new Setting("Jump Disable", false);
      this.blocks = new Setting("Blocks", HoleFill.FillBlocks.All);
      this.fill = new Setting("Fill Holes", new SettingGroup(true, 0));
      this.selfFill = (new Setting("Self Fill", false)).addToGroup(this.fill);
      this.selfFillMode = (new Setting("Self Fill Mode", HoleFill.SelfFillMode.Burrow)).addToGroup(this.fill);
      this.fillSingle = (new Setting("Single", true)).addToGroup(this.fill);
      this.fillDouble = (new Setting("Double", false)).addToGroup(this.fill);
      this.fillQuad = (new Setting("Quad", false)).addToGroup(this.fill);
      this.mode = new Setting("Mode", HoleFill.Mode.Always);
      this.rangeToTarget = new Setting("Range To Target", 2.0F, 1.0F, 5.0F, (v) -> {
         return this.mode.getValue() == HoleFill.Mode.Target;
      });
      this.autoDisable = new Setting("Auto Disable", false);
      this.placeMode = new Setting("Place Mode", InteractionUtility.PlaceMode.Packet);
      this.renderCategory = new Setting("Render", new SettingGroup(false, 0));
      this.renderMode = (new Setting("Render Mode", BlockAnimationUtility.BlockRenderMode.All)).addToGroup(this.renderCategory);
      this.animationMode = (new Setting("Animation Mode", BlockAnimationUtility.BlockAnimationMode.Fade)).addToGroup(this.renderCategory);
      this.renderFillColor = (new Setting("Render Fill Color", new ColorSetting(HudEditor.getColor(0)))).addToGroup(this.renderCategory);
      this.renderLineColor = (new Setting("Render Line Color", new ColorSetting(HudEditor.getColor(0)))).addToGroup(this.renderCategory);
      this.renderLineWidth = (new Setting("Render Line Width", 2, 1, 5)).addToGroup(this.renderCategory);
      this.burrowWasEnabled = false;
      this.tickCounter = 0;
      this.selfFillNeed = false;
   }

   public void onEnable() {
      this.burrowWasEnabled = false;
      this.selfFillNeed = false;
   }

   @EventHandler
   public void onTick(EventTick event) {
      if (!fullNullCheck()) {
         if ((Boolean)this.jumpDisable.getValue() && mc.field_1724.field_6036 < mc.field_1724.method_23318()) {
            this.disable(ClientSettings.isRu() ? "Вы прыгнули! Выключаю..." : "You jumped! Disabling...");
         }

         if (this.tickCounter < (Integer)this.actionInterval.getValue()) {
            ++this.tickCounter;
         } else {
            if (HoleUtility.isHole(mc.field_1724.method_24515()) && mc.field_1687.method_8320(mc.field_1724.method_24515()).method_26215()) {
               this.burrowWasEnabled = false;
            }

            int slot = this.getBlockSlot();
            if (slot != -1) {
               List<class_2338> holes = this.findHoles();
               class_1657 target = (class_1657)Managers.COMBAT.getTargets((Float)this.placeRange.getValue()).stream().min(Comparator.comparing((e) -> {
                  return mc.field_1724.method_5858(e);
               })).orElse((Object)null);
               if (this.mode.getValue() != HoleFill.Mode.Target || target != null) {
                  class_1657 predicted = PredictUtility.predictPlayer(target, 3);
                  int blocksPlaced = 0;

                  while(blocksPlaced < (Integer)this.actionShift.getValue()) {
                     class_2338 pos;
                     if (this.mode.getValue() == HoleFill.Mode.Target) {
                        pos = (class_2338)holes.stream().filter(this::isHole).filter((p) -> {
                           return mc.field_1724.method_19538().method_1022(p.method_46558()) <= (double)(Float)this.placeRange.getValue();
                        }).filter((p) -> {
                           return predicted.method_19538().method_1022(p.method_46558()) <= (double)(Float)this.rangeToTarget.getValue();
                        }).filter((p) -> {
                           if (p.equals(mc.field_1724.method_24515()) && (Boolean)this.selfFill.getValue()) {
                              this.selfFillNeed = true;
                              return true;
                           } else {
                              return InteractionUtility.canPlaceBlock(p, (InteractionUtility.Interact)this.interactMode.getValue(), false);
                           }
                        }).min(Comparator.comparing((p) -> {
                           return mc.field_1724.method_19538().method_1022(p.method_46558());
                        })).orElse((Object)null);
                     } else {
                        pos = (class_2338)holes.stream().filter(this::isHole).filter((p) -> {
                           return mc.field_1724.method_19538().method_1022(p.method_46558()) <= (double)(Float)this.placeRange.getValue();
                        }).filter((p) -> {
                           if (p.equals(mc.field_1724.method_24515()) && (Boolean)this.selfFill.getValue()) {
                              this.selfFillNeed = true;
                              return true;
                           } else {
                              return InteractionUtility.canPlaceBlock(p, (InteractionUtility.Interact)this.interactMode.getValue(), false);
                           }
                        }).min(Comparator.comparing((p) -> {
                           return mc.field_1724.method_19538().method_1022(p.method_46558());
                        })).orElse((Object)null);
                     }

                     if (pos == null) {
                        if ((Boolean)this.autoDisable.getValue()) {
                           this.disable(ClientSettings.isRu() ? "Все холки заполнены!" : "All holes are filled!");
                        }
                        break;
                     }

                     List<class_2338> poses = this.getHolePoses(pos).stream().filter((blockPosx) -> {
                        return mc.field_1724.method_19538().method_1022(blockPosx.method_46558()) <= (double)(Float)this.placeRange.getValue();
                     }).toList();
                     boolean broke = false;
                     if (this.selfFillNeed && HoleUtility.isHole(mc.field_1724.method_24515())) {
                        switch(((HoleFill.SelfFillMode)this.selfFillMode.getValue()).ordinal()) {
                        case 0:
                           if (!ModuleManager.burrow.isEnabled() && !this.burrowWasEnabled) {
                              ModuleManager.burrow.enable();
                              this.selfFillNeed = false;
                              return;
                           }

                           return;
                        case 1:
                           class_2338 headPos = class_2338.method_49638(mc.field_1724.method_19538()).method_10086(2);
                           if (mc.field_1687.method_8320(headPos).method_45474() && InteractionUtility.canPlaceBlock(headPos, (InteractionUtility.Interact)this.interactMode.getValue(), false)) {
                              this.selfFillNeed = false;
                              InteractionUtility.placeBlock(headPos, (InteractionUtility.Rotate)this.rotate.getValue(), (InteractionUtility.Interact)this.interactMode.getValue(), (InteractionUtility.PlaceMode)this.placeMode.getValue(), slot, true, false);
                              BlockAnimationUtility.renderBlock(headPos, ((ColorSetting)this.renderLineColor.getValue()).getColorObject(), (Integer)this.renderLineWidth.getValue(), ((ColorSetting)this.renderFillColor.getValue()).getColorObject(), (BlockAnimationUtility.BlockAnimationMode)this.animationMode.getValue(), (BlockAnimationUtility.BlockRenderMode)this.renderMode.getValue());
                              this.tickCounter = 0;
                              inactivityTimer.reset();
                              return;
                           }

                           boolean placed = false;

                           for(int i = 0; i < 3; ++i) {
                              class_2382[] var13 = HoleUtility.VECTOR_PATTERN;
                              int var14 = var13.length;

                              for(int var15 = 0; var15 < var14; ++var15) {
                                 class_2382 vecAdd = var13[var15];
                                 class_2338 checkPos = headPos.method_10081(vecAdd).method_10087(i);
                                 if (mc.field_1687.method_8320(checkPos).method_45474() && InteractionUtility.canPlaceBlock(checkPos, (InteractionUtility.Interact)this.interactMode.getValue(), false)) {
                                    InteractionUtility.placeBlock(checkPos, (InteractionUtility.Rotate)this.rotate.getValue(), (InteractionUtility.Interact)this.interactMode.getValue(), (InteractionUtility.PlaceMode)this.placeMode.getValue(), slot, true, false);
                                    ++blocksPlaced;
                                    this.tickCounter = 0;
                                    inactivityTimer.reset();
                                    placed = true;
                                    break;
                                 }
                              }

                              if (placed) {
                                 break;
                              }
                           }

                           if (placed) {
                              continue;
                           }
                        }
                     }

                     Iterator var18 = poses.iterator();

                     while(var18.hasNext()) {
                        class_2338 blockPos = (class_2338)var18.next();
                        if (!InteractionUtility.placeBlock(blockPos, (InteractionUtility.Rotate)this.rotate.getValue(), (InteractionUtility.Interact)this.interactMode.getValue(), (InteractionUtility.PlaceMode)this.placeMode.getValue(), slot, true, false)) {
                           broke = true;
                           break;
                        }

                        ++blocksPlaced;
                        this.tickCounter = 0;
                        BlockAnimationUtility.renderBlock(blockPos, ((ColorSetting)this.renderLineColor.getValue()).getColorObject(), (Integer)this.renderLineWidth.getValue(), ((ColorSetting)this.renderFillColor.getValue()).getColorObject(), (BlockAnimationUtility.BlockAnimationMode)this.animationMode.getValue(), (BlockAnimationUtility.BlockRenderMode)this.renderMode.getValue());
                        if (!mc.field_1724.method_24828()) {
                           return;
                        }

                        inactivityTimer.reset();
                     }

                     if (broke) {
                        break;
                     }
                  }

               }
            }
         }
      }
   }

   @NotNull
   @Unmodifiable
   private List<class_2338> getHolePoses(class_2338 fromPos) {
      class_2382[] var2;
      int var3;
      int var4;
      class_2382 vec;
      if (HoleUtility.validQuadBedrock(fromPos) || HoleUtility.validQuadIndestructible(fromPos)) {
         var2 = HOLE_VECTORS;
         var3 = var2.length;

         for(var4 = 0; var4 < var3; ++var4) {
            vec = var2[var4];
            if (mc.field_1687.method_8320(fromPos.method_10081(vec)).method_45474() && mc.field_1687.method_8320(fromPos.method_10069(vec.method_10263(), 0, 0)).method_45474() && mc.field_1687.method_8320(fromPos.method_10069(0, 0, vec.method_10260())).method_45474()) {
               return List.of(fromPos, fromPos.method_10081(vec), fromPos.method_10069(vec.method_10263(), 0, 0), fromPos.method_10069(0, 0, vec.method_10260()));
            }
         }
      }

      if (HoleUtility.validTwoBlockBedrock(fromPos) || HoleUtility.validTwoBlockIndestructible(fromPos)) {
         var2 = HoleUtility.VECTOR_PATTERN;
         var3 = var2.length;

         for(var4 = 0; var4 < var3; ++var4) {
            vec = var2[var4];
            if (mc.field_1687.method_8320(fromPos).method_45474() && mc.field_1687.method_8320(fromPos.method_10081(vec)).method_45474()) {
               return List.of(fromPos, fromPos.method_10081(vec));
            }
         }
      }

      return List.of(fromPos);
   }

   @NotNull
   private List<class_2338> findHoles() {
      List<class_2338> positions = new ArrayList();
      class_2338 centerPos = mc.field_1724.method_24515();
      int r = (int)Math.ceil((double)(Float)this.placeRange.getValue()) + 1;
      int h = ((Float)this.placeRange.getValue()).intValue();

      for(int i = centerPos.method_10263() - r; i < centerPos.method_10263() + r; ++i) {
         for(int j = centerPos.method_10264() - h; j < centerPos.method_10264() + h; ++j) {
            for(int k = centerPos.method_10260() - r; k < centerPos.method_10260() + r; ++k) {
               class_2338 pos = new class_2338(i, j, k);
               boolean foundEntity = false;
               if (this.isHole(pos) && !this.isFillingNow(pos)) {
                  Iterator var10 = Managers.ASYNC.getAsyncPlayers().iterator();

                  while(var10.hasNext()) {
                     class_1657 pe = (class_1657)var10.next();
                     if ((new class_238(pos)).method_994(pe.method_5829())) {
                        foundEntity = true;
                        break;
                     }
                  }

                  if (!foundEntity) {
                     class_3965 wallCheck = mc.field_1687.method_17742(new class_3959(InteractionUtility.getEyesPos(mc.field_1724), pos.method_46558().method_43206(class_2350.field_11036, 0.5D), class_3960.field_17558, class_242.field_1348, mc.field_1724));
                     if (wallCheck == null || wallCheck.method_17783() != class_240.field_1332 || wallCheck.method_17777() == pos || !(InteractionUtility.squaredDistanceFromEyes(pos.method_46558()) > this.placeWallRange.getPow2Value())) {
                        positions.add(pos);
                     }
                  }
               }
            }
         }
      }

      return positions;
   }

   private int getBlockSlot() {
      class_1799 stack = mc.field_1724.method_6047();
      if (!stack.method_7960() && this.isValidItem(stack.method_7909())) {
         return mc.field_1724.method_31548().field_7545;
      } else {
         for(int i = 0; i < 9; ++i) {
            stack = mc.field_1724.method_31548().method_5438(i);
            if (!stack.method_7960() && this.isValidItem(stack.method_7909())) {
               return i;
            }
         }

         return -1;
      }
   }

   private boolean isFillingNow(class_2338 pos) {
      return BlockAnimationUtility.isRendering(pos);
   }

   private boolean isValidItem(class_1792 item) {
      if (!(item instanceof class_1747)) {
         return false;
      } else {
         class_2248 block = ((class_1747)item).method_7711();
         boolean isCorrectBlock;
         switch(((HoleFill.FillBlocks)this.blocks.getValue()).ordinal()) {
         case 1:
            isCorrectBlock = block == class_2246.field_10343;
            break;
         case 2:
            isCorrectBlock = block == class_2246.field_10540;
            break;
         case 3:
            isCorrectBlock = block == class_2246.field_10540 || block == class_2246.field_22423 || block == class_2246.field_22108 || block == class_2246.field_23152;
            break;
         default:
            isCorrectBlock = true;
         }

         return isCorrectBlock;
      }
   }

   private boolean isHole(class_2338 pos) {
      return (HoleUtility.validTwoBlockIndestructible(pos) || HoleUtility.validTwoBlockBedrock(pos)) && (Boolean)this.fillDouble.getValue() || (HoleUtility.validQuadBedrock(pos) || HoleUtility.validQuadIndestructible(pos)) && (Boolean)this.fillQuad.getValue() || (HoleUtility.validBedrock(pos) || HoleUtility.validIndestructible(pos)) && (Boolean)this.fillSingle.getValue();
   }

   private static enum FillBlocks {
      All,
      Webs,
      Obsidian,
      Indestructible;

      // $FF: synthetic method
      private static HoleFill.FillBlocks[] $values() {
         return new HoleFill.FillBlocks[]{All, Webs, Obsidian, Indestructible};
      }
   }

   private static enum SelfFillMode {
      Burrow,
      Trap;

      // $FF: synthetic method
      private static HoleFill.SelfFillMode[] $values() {
         return new HoleFill.SelfFillMode[]{Burrow, Trap};
      }
   }

   private static enum Mode {
      Always,
      Target;

      // $FF: synthetic method
      private static HoleFill.Mode[] $values() {
         return new HoleFill.Mode[]{Always, Target};
      }
   }
}
