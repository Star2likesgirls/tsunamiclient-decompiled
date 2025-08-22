package tsunami.features.modules.combat;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_1268;
import net.minecraft.class_1657;
import net.minecraft.class_1802;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_2530;
import net.minecraft.class_2885;
import net.minecraft.class_3965;
import net.minecraft.class_4587;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tsunami.core.Managers;
import tsunami.events.impl.EventTick;
import tsunami.features.modules.Module;
import tsunami.features.modules.client.ClientSettings;
import tsunami.features.modules.client.HudEditor;
import tsunami.setting.Setting;
import tsunami.setting.impl.ColorSetting;
import tsunami.setting.impl.SettingGroup;
import tsunami.utility.Timer;
import tsunami.utility.player.InteractionUtility;
import tsunami.utility.player.InventoryUtility;
import tsunami.utility.player.PlayerUtility;
import tsunami.utility.render.Render2DEngine;
import tsunami.utility.render.Render3DEngine;

public class TNTAura extends Module {
   private final Setting<Float> range = new Setting("Range", 5.0F, 2.0F, 7.0F);
   private final Setting<Integer> blocksPerTick = new Setting("Block/Tick", 8, 1, 12);
   private final Setting<Integer> placeDelay = new Setting("Delay/Place", 3, 0, 10);
   private final Setting<InteractionUtility.PlaceMode> placeMode;
   private final Setting<InteractionUtility.Rotate> rotate;
   private final Setting<SettingGroup> renderCategory;
   private final Setting<TNTAura.RenderMode> renderMode;
   private final Setting<ColorSetting> renderFillColor;
   private final Setting<ColorSetting> renderLineColor;
   private final Setting<Integer> renderLineWidth;
   private final Map<class_2338, Long> renderPoses;
   public static Timer inactivityTimer = new Timer();
   private int delay;

   public TNTAura() {
      super("TNTAura", Module.Category.NONE);
      this.placeMode = new Setting("PlaceMode", InteractionUtility.PlaceMode.Normal);
      this.rotate = new Setting("Rotate", InteractionUtility.Rotate.None);
      this.renderCategory = new Setting("Render", new SettingGroup(false, 0));
      this.renderMode = (new Setting("RenderMode", TNTAura.RenderMode.Fade)).addToGroup(this.renderCategory);
      this.renderFillColor = (new Setting("Fill", new ColorSetting(HudEditor.getColor(0)))).addToGroup(this.renderCategory);
      this.renderLineColor = (new Setting("Line", new ColorSetting(HudEditor.getColor(0)))).addToGroup(this.renderCategory);
      this.renderLineWidth = (new Setting("LineWidth", 2, 1, 5)).addToGroup(this.renderCategory);
      this.renderPoses = new ConcurrentHashMap();
   }

   public void onRender3D(class_4587 stack) {
      this.renderPoses.forEach((pos, time) -> {
         if (System.currentTimeMillis() - time > 500L) {
            this.renderPoses.remove(pos);
         } else {
            switch(((TNTAura.RenderMode)this.renderMode.getValue()).ordinal()) {
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

   @EventHandler
   public void onTick(EventTick e) {
      if (this.getTntSlot() == -1) {
         this.disable(ClientSettings.isRu() ? "Нет динамита!" : "No TNT!");
      } else if (this.getFlintSlot() == -1) {
         this.disable(ClientSettings.isRu() ? "Нет зажигалки!" : "No flint and steel!");
      } else if (this.getObbySlot() == -1) {
         this.disable(ClientSettings.isRu() ? "Нет обсидиана!" : "No obsidian!");
      } else {
         class_1657 targetedPlayer = Managers.COMBAT.getNearestTarget((Float)this.range.getValue());
         List<class_2338> blocks = this.getBlocks(targetedPlayer);
         if (!blocks.isEmpty()) {
            if (this.delay > 0) {
               --this.delay;
               return;
            }

            InventoryUtility.saveSlot();
            int placed = 0;

            while(placed < (Integer)this.blocksPerTick.getValue()) {
               class_2338 targetBlock = this.getSequentialPos(targetedPlayer);
               if (targetBlock == null || !InteractionUtility.placeBlock(targetBlock, (InteractionUtility.Rotate)this.rotate.getValue(), InteractionUtility.Interact.Vanilla, (InteractionUtility.PlaceMode)this.placeMode.getValue(), this.getObbySlot(), false, false)) {
                  break;
               }

               ++placed;
               this.delay = (Integer)this.placeDelay.getValue();
               inactivityTimer.reset();
               this.renderPoses.put(targetBlock, System.currentTimeMillis());
            }

            InventoryUtility.returnSlot();
         }

         if (targetedPlayer != null) {
            class_2338 headBlock = class_2338.method_49638(targetedPlayer.method_19538()).method_10086(2);
            InventoryUtility.saveSlot();
            InteractionUtility.placeBlock(headBlock, (InteractionUtility.Rotate)this.rotate.getValue(), InteractionUtility.Interact.Vanilla, (InteractionUtility.PlaceMode)this.placeMode.getValue(), this.getTntSlot(), false, false);
            class_3965 igniteResult = this.getIgniteResult(headBlock);
            InventoryUtility.switchTo(this.getFlintSlot());
            if (mc.field_1687.method_8320(headBlock).method_26204() instanceof class_2530 && igniteResult != null) {
               this.sendSequencedPacket((id) -> {
                  return new class_2885(class_1268.field_5808, igniteResult, id);
               });
               mc.field_1724.method_6104(class_1268.field_5808);
            }

            this.renderPoses.put(headBlock, System.currentTimeMillis());
            InventoryUtility.returnSlot();
         }

      }
   }

   private class_2338 getSequentialPos(class_1657 pl) {
      List<class_2338> list = this.getBlocks(pl);
      if (list.isEmpty()) {
         return null;
      } else {
         Iterator var3 = this.getBlocks(pl).iterator();

         class_2338 bp;
         do {
            if (!var3.hasNext()) {
               return null;
            }

            bp = (class_2338)var3.next();
         } while(!InteractionUtility.canPlaceBlock(bp, InteractionUtility.Interact.Vanilla, false) || !mc.field_1687.method_22347(bp));

         return bp;
      }
   }

   private List<class_2338> getBlocks(class_1657 pl) {
      if (pl == null) {
         return new ArrayList();
      } else {
         List<class_2338> blocks = new ArrayList();
         Iterator var3 = this.getAffectedBlocks(pl).iterator();

         while(var3.hasNext()) {
            class_2338 bp = (class_2338)var3.next();
            class_2350[] var5 = class_2350.values();
            int var6 = var5.length;

            for(int var7 = 0; var7 < var6; ++var7) {
               class_2350 dir = var5[var7];
               if (dir != class_2350.field_11036 && dir != class_2350.field_11033) {
                  blocks.add(bp.method_10093(dir));
                  blocks.add(bp.method_10093(dir).method_10084());
                  if (!(new class_238(bp.method_10093(dir).method_10086(1))).method_994(pl.method_5829())) {
                     blocks.add(bp.method_10093(dir).method_10086(2));
                  }

                  blocks.add(bp.method_10093(dir).method_10074());
               }
            }

            blocks.add(bp.method_10074());
            blocks.add(bp.method_10086(3));
            if (!InteractionUtility.canPlaceBlock(bp.method_10086(3), InteractionUtility.Interact.Vanilla, false)) {
               class_2350 dir = mc.field_1724.method_5735();
               if (dir != null) {
                  blocks.add(bp.method_10086(3).method_10079(dir, 1));
               }
            }
         }

         return blocks.stream().sorted(Comparator.comparing((b) -> {
            return mc.field_1724.method_5707(b.method_46558()) * -1.0D;
         })).toList();
      }
   }

   private int getObbySlot() {
      if (mc.field_1724.method_6047().method_7909() == class_1802.field_8281) {
         return mc.field_1724.method_31548().field_7545;
      } else {
         int slot = -1;

         for(int i = 0; i < 9; ++i) {
            if (mc.field_1724.method_31548().method_5438(i).method_7909() == class_1802.field_8281) {
               slot = i;
               break;
            }
         }

         return slot;
      }
   }

   private int getTntSlot() {
      if (mc.field_1724.method_6047().method_7909() == class_1802.field_8626) {
         return mc.field_1724.method_31548().field_7545;
      } else {
         int slot = -1;

         for(int i = 0; i < 9; ++i) {
            if (mc.field_1724.method_31548().method_5438(i).method_7909() == class_1802.field_8626) {
               slot = i;
               break;
            }
         }

         return slot;
      }
   }

   private int getFlintSlot() {
      if (mc.field_1724.method_6047().method_7909() == class_1802.field_8884) {
         return mc.field_1724.method_31548().field_7545;
      } else {
         int slot = -1;

         for(int i = 0; i < 9; ++i) {
            if (mc.field_1724.method_31548().method_5438(i).method_7909() == class_1802.field_8884) {
               slot = i;
               break;
            }
         }

         return slot;
      }
   }

   @Nullable
   private class_3965 getIgniteResult(class_2338 bp) {
      if (mc.field_1724 != null && mc.field_1687 != null) {
         return PlayerUtility.squaredDistanceFromEyes(bp.method_46558()) > this.range.getPow2Value() ? null : new class_3965(bp.method_46558().method_1031(0.0D, -0.5D, 0.0D), class_2350.field_11033, bp, false);
      } else {
         return null;
      }
   }

   private List<class_2338> getAffectedBlocks(class_1657 pl) {
      List<class_2338> tempPos = new ArrayList();
      List<class_2338> finalPos = new ArrayList();
      List<class_238> boxes = new ArrayList();
      Iterator var5 = mc.field_1687.method_18456().iterator();

      while(var5.hasNext()) {
         class_1657 player = (class_1657)var5.next();
         if (player.method_5858(pl) < 9.0D && player != pl) {
            boxes.add(player.method_5829());
         }
      }

      boxes.add(pl.method_5829());
      class_2338 center = this.getPlayerPos(pl);
      tempPos.add(center);
      tempPos.add(center.method_10095());
      tempPos.add(center.method_10095().method_10078());
      tempPos.add(center.method_10067());
      tempPos.add(center.method_10067().method_10095());
      tempPos.add(center.method_10072());
      tempPos.add(center.method_10072().method_10067());
      tempPos.add(center.method_10078());
      tempPos.add(center.method_10078().method_10072());
      Iterator var11 = tempPos.iterator();

      class_2338 bp;
      while(var11.hasNext()) {
         bp = (class_2338)var11.next();
         if ((new class_238(bp)).method_994(pl.method_5829())) {
            finalPos.add(bp);
         }
      }

      var11 = Lists.newArrayList(finalPos).iterator();

      while(var11.hasNext()) {
         bp = (class_2338)var11.next();
         Iterator var8 = boxes.iterator();

         while(var8.hasNext()) {
            class_238 box = (class_238)var8.next();
            if ((new class_238(bp)).method_994(box)) {
               finalPos.add(class_2338.method_49638(box.method_1005()));
            }
         }
      }

      return finalPos;
   }

   private class_2338 getPlayerPos(@NotNull class_1657 pl) {
      return class_2338.method_49637(pl.method_23317(), pl.method_23318() - Math.floor(pl.method_23318()) > 0.8D ? Math.floor(pl.method_23318()) + 1.0D : Math.floor(pl.method_23318()), pl.method_23321());
   }

   private static enum RenderMode {
      Fade,
      Decrease;

      // $FF: synthetic method
      private static TNTAura.RenderMode[] $values() {
         return new TNTAura.RenderMode[]{Fade, Decrease};
      }
   }
}
