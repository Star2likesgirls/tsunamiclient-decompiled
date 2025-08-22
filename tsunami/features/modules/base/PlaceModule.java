package tsunami.features.modules.base;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_1268;
import net.minecraft.class_1294;
import net.minecraft.class_1511;
import net.minecraft.class_1747;
import net.minecraft.class_1799;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2338;
import net.minecraft.class_238;
import net.minecraft.class_2824;
import net.minecraft.class_2879;
import net.minecraft.class_1297.class_5529;
import org.jetbrains.annotations.NotNull;
import tsunami.features.modules.Module;
import tsunami.features.modules.client.HudEditor;
import tsunami.setting.Setting;
import tsunami.setting.impl.BooleanSettingGroup;
import tsunami.setting.impl.ColorSetting;
import tsunami.setting.impl.SettingGroup;
import tsunami.utility.Timer;
import tsunami.utility.player.InteractionUtility;
import tsunami.utility.player.InventoryUtility;
import tsunami.utility.player.PlayerUtility;
import tsunami.utility.player.SearchInvResult;
import tsunami.utility.render.BlockAnimationUtility;

public abstract class PlaceModule extends Module {
   protected final Setting<Float> range = new Setting("Range", 5.0F, 0.0F, 7.0F);
   protected final Setting<InteractionUtility.Interact> interact;
   protected final Setting<PlaceModule.InteractMode> placeMode;
   protected final Setting<InteractionUtility.Rotate> rotate;
   protected final Setting<Boolean> swing;
   protected final Setting<BooleanSettingGroup> crystalBreaker;
   protected final Setting<Integer> crystalAge;
   protected final Setting<Integer> breakDelay;
   protected final Setting<Boolean> remove;
   protected final Setting<PlaceModule.InteractMode> breakCrystalMode;
   protected final Setting<Boolean> antiWeakness;
   protected final Setting<SettingGroup> blocks;
   protected final Setting<Boolean> obsidian;
   protected final Setting<Boolean> anchor;
   protected final Setting<Boolean> enderChest;
   protected final Setting<Boolean> netherite;
   protected final Setting<Boolean> cryingObsidian;
   protected final Setting<Boolean> dirt;
   protected final Setting<Boolean> oakPlanks;
   protected final Setting<SettingGroup> pause;
   protected final Setting<Boolean> eatPause;
   protected final Setting<Boolean> breakPause;
   protected final Setting<BooleanSettingGroup> render;
   protected final Setting<BlockAnimationUtility.BlockRenderMode> renderMode;
   protected final Setting<BlockAnimationUtility.BlockAnimationMode> animationMode;
   protected final Setting<ColorSetting> renderFillColor;
   protected final Setting<ColorSetting> renderLineColor;
   protected final Setting<Integer> renderLineWidth;
   public final Timer inactivityTimer;
   public final Timer pauseTimer;
   protected final Timer attackTimer;

   public PlaceModule(@NotNull String name, @NotNull Module.Category category) {
      super(name, category);
      this.interact = new Setting("Interact", InteractionUtility.Interact.Strict);
      this.placeMode = new Setting("Place Mode", PlaceModule.InteractMode.Normal);
      this.rotate = new Setting("Rotate", InteractionUtility.Rotate.None);
      this.swing = new Setting("Swing", false);
      this.crystalBreaker = new Setting("Crystal Breaker", new BooleanSettingGroup(false));
      this.crystalAge = (new Setting("CrystalAge", 0, 0, 20)).addToGroup(this.crystalBreaker);
      this.breakDelay = (new Setting("Break Delay", 100, 1, 1000)).addToGroup(this.crystalBreaker);
      this.remove = (new Setting("Remove", false)).addToGroup(this.crystalBreaker);
      this.breakCrystalMode = (new Setting("Break Mode", PlaceModule.InteractMode.Normal)).addToGroup(this.crystalBreaker);
      this.antiWeakness = (new Setting("Anti Weakness", false)).addToGroup(this.crystalBreaker);
      this.blocks = new Setting("Blocks", new SettingGroup(false, 0));
      this.obsidian = (new Setting("Obsidian", true)).addToGroup(this.blocks);
      this.anchor = (new Setting("Anchor", false)).addToGroup(this.blocks);
      this.enderChest = (new Setting("EnderChest", true)).addToGroup(this.blocks);
      this.netherite = (new Setting("Netherite", false)).addToGroup(this.blocks);
      this.cryingObsidian = (new Setting("Crying Obsidian", true)).addToGroup(this.blocks);
      this.dirt = (new Setting("Dirt", false)).addToGroup(this.blocks);
      this.oakPlanks = (new Setting("OakPlanks", false)).addToGroup(this.blocks);
      this.pause = new Setting("Pause", new SettingGroup(false, 0));
      this.eatPause = (new Setting("On Eat", false)).addToGroup(this.pause);
      this.breakPause = (new Setting("On Break", false)).addToGroup(this.pause);
      this.render = new Setting("Render", new BooleanSettingGroup(true));
      this.renderMode = (new Setting("Render Mode", BlockAnimationUtility.BlockRenderMode.All)).addToGroup(this.render);
      this.animationMode = (new Setting("Animation Mode", BlockAnimationUtility.BlockAnimationMode.Fade)).addToGroup(this.render);
      this.renderFillColor = (new Setting("Fill Color", new ColorSetting(HudEditor.getColor(0)))).addToGroup(this.render);
      this.renderLineColor = (new Setting("Line Color", new ColorSetting(HudEditor.getColor(0)))).addToGroup(this.render);
      this.renderLineWidth = (new Setting("Line Width", 2, 1, 5)).addToGroup(this.render);
      this.inactivityTimer = new Timer();
      this.pauseTimer = new Timer();
      this.attackTimer = new Timer();
   }

   protected boolean shouldPause() {
      return (Boolean)this.eatPause.getValue() && PlayerUtility.isEating() || (Boolean)this.breakPause.getValue() && PlayerUtility.isMining() || !this.pauseTimer.passedMs(350L);
   }

   protected boolean placeBlock(class_2338 pos) {
      return this.placeBlock(pos, ((BooleanSettingGroup)this.crystalBreaker.getValue()).isEnabled(), (PlaceModule.InteractMode)this.placeMode.getValue(), (InteractionUtility.Rotate)this.rotate.getValue());
   }

   protected boolean placeBlock(class_2338 pos, boolean ignoreEntities) {
      return this.placeBlock(pos, ignoreEntities, (PlaceModule.InteractMode)this.placeMode.getValue(), (InteractionUtility.Rotate)this.rotate.getValue());
   }

   protected boolean placeBlock(class_2338 pos, InteractionUtility.Rotate rotate) {
      return this.placeBlock(pos, ((BooleanSettingGroup)this.crystalBreaker.getValue()).isEnabled(), (PlaceModule.InteractMode)this.placeMode.getValue(), rotate);
   }

   protected boolean placeBlock(class_2338 pos, PlaceModule.InteractMode mode) {
      return this.placeBlock(pos, ((BooleanSettingGroup)this.crystalBreaker.getValue()).isEnabled(), mode, (InteractionUtility.Rotate)this.rotate.getValue());
   }

   protected boolean placeBlock(class_2338 pos, boolean ignoreEntities, PlaceModule.InteractMode mode, InteractionUtility.Rotate rotate) {
      if (this.shouldPause()) {
         return false;
      } else {
         boolean validInteraction = false;
         SearchInvResult result = this.getBlockResult();
         if (!result.found()) {
            return false;
         } else {
            if (((BooleanSettingGroup)this.crystalBreaker.getValue()).isEnabled() && mc.field_1687 != null && this.attackTimer.passedMs((long)(Integer)this.breakDelay.getValue())) {
               mc.field_1687.method_18467(class_1511.class, new class_238(pos)).stream().findFirst().ifPresent(this::breakCrystal);
            }

            if (mode == PlaceModule.InteractMode.Packet) {
               validInteraction = InteractionUtility.placeBlock(pos, rotate, (InteractionUtility.Interact)this.interact.getValue(), InteractionUtility.PlaceMode.Packet, result.slot(), true, ignoreEntities);
            }

            if (mode == PlaceModule.InteractMode.Normal) {
               validInteraction = InteractionUtility.placeBlock(pos, rotate, (InteractionUtility.Interact)this.interact.getValue(), InteractionUtility.PlaceMode.Normal, result.slot(), true, ignoreEntities);
            }

            if (validInteraction && mc.field_1724 != null) {
               if (((BooleanSettingGroup)this.render.getValue()).isEnabled()) {
                  this.renderBlock(pos);
               }

               if ((Boolean)this.swing.getValue()) {
                  mc.field_1724.method_6104(class_1268.field_5808);
               }
            }

            return validInteraction;
         }
      }
   }

   protected void breakCrystal(class_1511 entity) {
      if (mc.field_1724 != null && mc.field_1687 != null && mc.field_1761 != null && !this.shouldPause() && this.attackTimer.passedMs((long)(Integer)this.breakDelay.getValue()) && !(mc.field_1724.method_5858(entity) > (double)this.range.getPow2Value()) && entity.field_6012 >= (Integer)this.crystalAge.getValue()) {
         int preSlot = mc.field_1724.method_31548().field_7545;
         if ((Boolean)this.antiWeakness.getValue() && mc.field_1724.method_6059(class_1294.field_5911)) {
            SearchInvResult result = InventoryUtility.getAntiWeaknessItem();
            if (!result.found()) {
               return;
            }

            result.switchTo();
         }

         if (this.breakCrystalMode.getValue() == PlaceModule.InteractMode.Packet) {
            this.sendPacket(class_2824.method_34206(entity, mc.field_1724.method_5715()));
         }

         if (this.breakCrystalMode.getValue() == PlaceModule.InteractMode.Normal) {
            mc.field_1761.method_2918(mc.field_1724, entity);
         }

         this.sendPacket(new class_2879(class_1268.field_5808));
         this.attackTimer.reset();
         if ((Boolean)this.remove.getValue()) {
            entity.method_5768();
            entity.method_31745(class_5529.field_26998);
            entity.method_36209();
         }

         if ((Boolean)this.antiWeakness.getValue() && mc.field_1724.method_6059(class_1294.field_5911)) {
            InventoryUtility.switchTo(preSlot);
         }

      }
   }

   protected boolean canPlaceBlock(class_2338 pos, boolean ignoreEntities) {
      return InteractionUtility.canPlaceBlock(pos, (InteractionUtility.Interact)this.interact.getValue(), ignoreEntities);
   }

   protected void renderBlock(class_2338 pos) {
      BlockAnimationUtility.renderBlock(pos, ((ColorSetting)this.renderLineColor.getValue()).getColorObject(), (Integer)this.renderLineWidth.getValue(), ((ColorSetting)this.renderFillColor.getValue()).getColorObject(), (BlockAnimationUtility.BlockAnimationMode)this.animationMode.getValue(), (BlockAnimationUtility.BlockRenderMode)this.renderMode.getValue());
   }

   protected SearchInvResult getBlockResult() {
      List<class_2248> canUseBlocks = new ArrayList();
      if (mc.field_1724 == null) {
         return SearchInvResult.notFound();
      } else {
         if ((Boolean)this.obsidian.getValue()) {
            canUseBlocks.add(class_2246.field_10540);
         }

         if ((Boolean)this.enderChest.getValue()) {
            canUseBlocks.add(class_2246.field_10443);
         }

         if ((Boolean)this.cryingObsidian.getValue()) {
            canUseBlocks.add(class_2246.field_22423);
         }

         if ((Boolean)this.netherite.getValue()) {
            canUseBlocks.add(class_2246.field_22108);
         }

         if ((Boolean)this.anchor.getValue()) {
            canUseBlocks.add(class_2246.field_23152);
         }

         if ((Boolean)this.dirt.getValue()) {
            canUseBlocks.addAll(List.of(class_2246.field_10566, class_2246.field_10219, class_2246.field_10520));
         }

         if ((Boolean)this.oakPlanks.getValue()) {
            canUseBlocks.addAll(List.of(class_2246.field_10161, class_2246.field_10148, class_2246.field_10075));
         }

         class_1799 mainHandStack = mc.field_1724.method_6047();
         if (mainHandStack != class_1799.field_8037 && mainHandStack.method_7909() instanceof class_1747) {
            class_2248 blockFromMainHandItem = ((class_1747)mainHandStack.method_7909()).method_7711();
            if (canUseBlocks.contains(blockFromMainHandItem)) {
               return new SearchInvResult(mc.field_1724.method_31548().field_7545, true, mainHandStack);
            }
         }

         return InventoryUtility.findBlockInHotBar((List)canUseBlocks);
      }
   }

   public void pause() {
      this.pauseTimer.reset();
   }

   protected static enum InteractMode {
      Packet,
      Normal;

      // $FF: synthetic method
      private static PlaceModule.InteractMode[] $values() {
         return new PlaceModule.InteractMode[]{Packet, Normal};
      }
   }
}
