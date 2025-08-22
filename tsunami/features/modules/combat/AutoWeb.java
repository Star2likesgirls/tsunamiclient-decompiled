package tsunami.features.modules.combat;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_1657;
import net.minecraft.class_1747;
import net.minecraft.class_1799;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_3959;
import net.minecraft.class_3965;
import net.minecraft.class_4587;
import net.minecraft.class_239.class_240;
import net.minecraft.class_3959.class_242;
import net.minecraft.class_3959.class_3960;
import tsunami.core.Managers;
import tsunami.events.impl.EventTick;
import tsunami.features.modules.Module;
import tsunami.features.modules.client.HudEditor;
import tsunami.setting.Setting;
import tsunami.setting.impl.ColorSetting;
import tsunami.setting.impl.SettingGroup;
import tsunami.utility.Timer;
import tsunami.utility.player.InteractionUtility;
import tsunami.utility.player.InventoryUtility;
import tsunami.utility.render.Render2DEngine;
import tsunami.utility.render.Render3DEngine;

public final class AutoWeb extends Module {
   private final Setting<Integer> range = new Setting("Range", 5, 1, 7);
   private final Setting<Integer> placeWallRange = new Setting("WallRange", 5, 1, 7);
   private final Setting<AutoWeb.PlaceTiming> placeTiming;
   private final Setting<Integer> blocksPerTick;
   private final Setting<Integer> placeDelay;
   private final Setting<InteractionUtility.Interact> interact;
   private final Setting<InteractionUtility.PlaceMode> placeMode;
   private final Setting<InteractionUtility.Rotate> rotate;
   private final Setting<SettingGroup> selection;
   private final Setting<Boolean> head;
   private final Setting<Boolean> leggs;
   private final Setting<Boolean> surround;
   private final Setting<Boolean> upperSurround;
   private final Setting<SettingGroup> renderCategory;
   private final Setting<AutoWeb.RenderMode> renderMode;
   private final Setting<ColorSetting> renderFillColor;
   private final Setting<ColorSetting> renderLineColor;
   private final Setting<Integer> renderLineWidth;
   private final Setting<Integer> effectDurationMs;
   private final ArrayList<class_2338> sequentialBlocks;
   public static Timer inactivityTimer = new Timer();
   private final Map<class_2338, Long> renderPoses;
   private int delay;

   public AutoWeb() {
      super("AutoWeb", Module.Category.NONE);
      this.placeTiming = new Setting("PlaceTiming", AutoWeb.PlaceTiming.Default);
      this.blocksPerTick = new Setting("Block/Tick", 8, 1, 12, (v) -> {
         return this.placeTiming.getValue() == AutoWeb.PlaceTiming.Default;
      });
      this.placeDelay = new Setting("Delay/Place", 3, 0, 10);
      this.interact = new Setting("Interact", InteractionUtility.Interact.Strict);
      this.placeMode = new Setting("PlaceMode", InteractionUtility.PlaceMode.Normal);
      this.rotate = new Setting("Rotate", InteractionUtility.Rotate.None);
      this.selection = new Setting("Selection", new SettingGroup(false, 0));
      this.head = (new Setting("Head", true)).addToGroup(this.selection);
      this.leggs = (new Setting("Leggs", true)).addToGroup(this.selection);
      this.surround = (new Setting("Surround", true)).addToGroup(this.selection);
      this.upperSurround = (new Setting("UpperSurround", false)).addToGroup(this.selection);
      this.renderCategory = new Setting("Render", new SettingGroup(false, 0));
      this.renderMode = (new Setting("Render Mode", AutoWeb.RenderMode.Fade)).addToGroup(this.renderCategory);
      this.renderFillColor = (new Setting("Render Fill Color", new ColorSetting(HudEditor.getColor(0)))).addToGroup(this.renderCategory);
      this.renderLineColor = (new Setting("Render Line Color", new ColorSetting(HudEditor.getColor(0)))).addToGroup(this.renderCategory);
      this.renderLineWidth = (new Setting("Render Line Width", 2, 1, 5)).addToGroup(this.renderCategory);
      this.effectDurationMs = (new Setting("Effect Duration (MS)", 500, 0, 10000)).addToGroup(this.renderCategory);
      this.sequentialBlocks = new ArrayList();
      this.renderPoses = new ConcurrentHashMap();
      this.delay = 0;
   }

   public void onRender3D(class_4587 stack) {
      this.renderPoses.forEach((pos, time) -> {
         if (System.currentTimeMillis() - time > (long)(Integer)this.effectDurationMs.getValue()) {
            this.renderPoses.remove(pos);
         } else {
            switch(((AutoWeb.RenderMode)this.renderMode.getValue()).ordinal()) {
            case 0:
               Render3DEngine.drawFilledBox(stack, new class_238(pos), Render2DEngine.injectAlpha(((ColorSetting)this.renderFillColor.getValue()).getColorObject(), (int)(100.0F * (1.0F - (float)(System.currentTimeMillis() - time) / 500.0F))));
               Render3DEngine.drawBoxOutline(new class_238(pos), Render2DEngine.injectAlpha(((ColorSetting)this.renderLineColor.getValue()).getColorObject(), (int)(100.0F * (1.0F - (float)(System.currentTimeMillis() - time) / 500.0F))), (float)(Integer)this.renderLineWidth.getValue());
               break;
            case 1:
               float scale = 1.0F - (float)(System.currentTimeMillis() - time) / 500.0F;
               class_238 box = new class_238((double)pos.method_10263(), (double)pos.method_10264(), (double)pos.method_10260(), (double)pos.method_10263(), (double)pos.method_10264(), (double)pos.method_10260());
               Render3DEngine.drawFilledBox(stack, box.method_1002((double)scale, (double)scale, (double)scale).method_989(0.5D + (double)scale * 0.5D, 0.5D + (double)scale * 0.5D, 0.5D + (double)scale * 0.5D), Render2DEngine.injectAlpha(((ColorSetting)this.renderFillColor.getValue()).getColorObject(), (int)(100.0F * (1.0F - (float)(System.currentTimeMillis() - time) / 500.0F))));
               Render3DEngine.drawBoxOutline(box.method_1002((double)scale, (double)scale, (double)scale).method_989(0.5D + (double)scale * 0.5D, 0.5D + (double)scale * 0.5D, 0.5D + (double)scale * 0.5D), ((ColorSetting)this.renderLineColor.getValue()).getColorObject(), (float)(Integer)this.renderLineWidth.getValue());
            }
         }

      });
   }

   public void onEnable() {
      this.sequentialBlocks.clear();
      this.renderPoses.clear();
   }

   @EventHandler
   public void onTick(EventTick e) {
      class_2338 targetBlock1 = this.getSequentialPos();
      if (targetBlock1 != null) {
         if (this.delay > 0) {
            --this.delay;
         } else {
            InventoryUtility.saveSlot();
            if (this.placeTiming.getValue() == AutoWeb.PlaceTiming.Default) {
               int placed = 0;

               while(placed < (Integer)this.blocksPerTick.getValue()) {
                  class_2338 targetBlock = this.getSequentialPos();
                  if (targetBlock == null || !InteractionUtility.placeBlock(targetBlock, (InteractionUtility.Rotate)this.rotate.getValue(), (InteractionUtility.Interact)this.interact.getValue(), (InteractionUtility.PlaceMode)this.placeMode.getValue(), this.getSlot(), false, true)) {
                     break;
                  }

                  ++placed;
                  this.renderPoses.put(targetBlock, System.currentTimeMillis());
                  this.delay = (Integer)this.placeDelay.getValue();
                  inactivityTimer.reset();
               }
            } else if (this.placeTiming.getValue() == AutoWeb.PlaceTiming.Vanilla) {
               class_2338 targetBlock = this.getSequentialPos();
               if (targetBlock == null) {
                  return;
               }

               if (InteractionUtility.placeBlock(targetBlock, (InteractionUtility.Rotate)this.rotate.getValue(), (InteractionUtility.Interact)this.interact.getValue(), (InteractionUtility.PlaceMode)this.placeMode.getValue(), this.getSlot(), false, true)) {
                  this.sequentialBlocks.add(targetBlock);
                  this.renderPoses.put(targetBlock, System.currentTimeMillis());
                  this.delay = (Integer)this.placeDelay.getValue();
                  inactivityTimer.reset();
               }
            }

            InventoryUtility.returnSlot();
         }
      }
   }

   private class_2338 getSequentialPos() {
      class_1657 target = Managers.COMBAT.getNearestTarget((float)(Integer)this.range.getValue());
      if (target != null) {
         class_2338 targetBp = class_2338.method_49638(target.method_19538());
         ArrayList<class_2338> positions = new ArrayList();
         if ((Boolean)this.leggs.getValue()) {
            positions.add(targetBp);
         }

         if ((Boolean)this.head.getValue()) {
            positions.add(targetBp.method_10084());
         }

         if ((Boolean)this.surround.getValue()) {
            positions.add(targetBp.method_10078());
            positions.add(targetBp.method_10067());
            positions.add(targetBp.method_10072());
            positions.add(targetBp.method_10095());
         }

         if ((Boolean)this.upperSurround.getValue()) {
            positions.add(targetBp.method_10078().method_10084());
            positions.add(targetBp.method_10067().method_10084());
            positions.add(targetBp.method_10072().method_10084());
            positions.add(targetBp.method_10095().method_10084());
         }

         Iterator var4 = positions.iterator();

         class_2338 bp;
         class_3965 wallCheck;
         do {
            do {
               if (!var4.hasNext()) {
                  return null;
               }

               bp = (class_2338)var4.next();
               wallCheck = mc.field_1687.method_17742(new class_3959(InteractionUtility.getEyesPos(mc.field_1724), bp.method_46558().method_43206(class_2350.field_11036, 0.5D), class_3960.field_17558, class_242.field_1348, mc.field_1724));
            } while(wallCheck != null && wallCheck.method_17783() == class_240.field_1332 && wallCheck.method_17777() != bp && InteractionUtility.squaredDistanceFromEyes(bp.method_46558()) > this.placeWallRange.getPow2Value());
         } while(!InteractionUtility.canPlaceBlock(bp, (InteractionUtility.Interact)this.interact.getValue(), true) || !mc.field_1687.method_8320(bp).method_45474());

         return bp;
      } else {
         return null;
      }
   }

   private int getSlot() {
      List<class_2248> canUseBlocks = new ArrayList();
      canUseBlocks.add(class_2246.field_10343);
      int slot = -1;
      class_1799 mainhandStack = mc.field_1724.method_6047();
      if (mainhandStack != class_1799.field_8037 && mainhandStack.method_7909() instanceof class_1747) {
         class_2248 blockFromMainhandItem = ((class_1747)mainhandStack.method_7909()).method_7711();
         if (canUseBlocks.contains(blockFromMainhandItem)) {
            slot = mc.field_1724.method_31548().field_7545;
         }
      }

      if (slot == -1) {
         for(int i = 0; i < 9; ++i) {
            class_1799 stack = mc.field_1724.method_31548().method_5438(i);
            if (stack != class_1799.field_8037 && stack.method_7909() instanceof class_1747) {
               class_2248 blockFromItem = ((class_1747)stack.method_7909()).method_7711();
               if (canUseBlocks.contains(blockFromItem)) {
                  slot = i;
                  break;
               }
            }
         }
      }

      return slot;
   }

   private static enum PlaceTiming {
      Default,
      Vanilla;

      // $FF: synthetic method
      private static AutoWeb.PlaceTiming[] $values() {
         return new AutoWeb.PlaceTiming[]{Default, Vanilla};
      }
   }

   private static enum RenderMode {
      Fade,
      Decrease;

      // $FF: synthetic method
      private static AutoWeb.RenderMode[] $values() {
         return new AutoWeb.RenderMode[]{Fade, Decrease};
      }
   }
}
