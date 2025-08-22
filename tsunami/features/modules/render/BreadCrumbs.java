package tsunami.features.modules.render;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_243;
import net.minecraft.class_287;
import net.minecraft.class_289;
import net.minecraft.class_290;
import net.minecraft.class_4587;
import net.minecraft.class_757;
import net.minecraft.class_293.class_5596;
import tsunami.events.impl.EventPostSync;
import tsunami.features.modules.Module;
import tsunami.features.modules.client.HudEditor;
import tsunami.setting.Setting;
import tsunami.setting.impl.ColorSetting;
import tsunami.utility.render.Render2DEngine;
import tsunami.utility.render.Render3DEngine;

public class BreadCrumbs extends Module {
   private final Setting<Boolean> throughWalls = new Setting("ThroughWalls", true);
   private final Setting<Integer> limit = new Setting("ListLimit", 1000, 10, 99999);
   private final List<class_243> positions = new CopyOnWriteArrayList();
   private final Setting<BreadCrumbs.Mode> lmode;
   private final Setting<ColorSetting> color;

   public BreadCrumbs() {
      super("BreadCrumbs", Module.Category.NONE);
      this.lmode = new Setting("ColorMode", BreadCrumbs.Mode.Sync);
      this.color = new Setting("Color", new ColorSetting(3649978), (v) -> {
         return this.lmode.getValue() == BreadCrumbs.Mode.Custom;
      });
   }

   public void onRender3D(class_4587 stack) {
      Render3DEngine.setupRender();
      if ((Boolean)this.throughWalls.getValue()) {
         RenderSystem.disableDepthTest();
      }

      RenderSystem.disableCull();
      RenderSystem.lineWidth(1.0F);
      RenderSystem.setShader(class_757::method_34535);
      class_287 buffer = class_289.method_1348().method_60827(class_5596.field_27377, class_290.field_29337);

      for(int i = 0; i < this.positions.size(); ++i) {
         class_243 vec1 = null;

         try {
            vec1 = (class_243)this.positions.get(i - 1);
         } catch (Exception var8) {
         }

         class_243 vec2 = (class_243)this.positions.get(i);
         if (vec1 != null && vec2 != null) {
            Color c = this.lmode.getValue() == BreadCrumbs.Mode.Sync ? HudEditor.getColor(i) : ((ColorSetting)this.color.getValue()).getColorObject();
            if (i < 10) {
               c = Render2DEngine.injectAlpha(c, (int)((float)c.getAlpha() * ((float)i / 10.0F)));
            }

            class_4587 matrices = Render3DEngine.matrixFrom(vec1.method_10216(), vec1.method_10214(), vec1.method_10215());
            Render3DEngine.vertexLine(matrices, buffer, 0.0F, 0.0F, 0.0F, (float)(vec2.method_10216() - vec1.method_10216()), (float)(vec2.method_10214() - vec1.method_10214()), (float)(vec2.method_10215() - vec1.method_10215()), c);
         }
      }

      Render2DEngine.endBuilding(buffer);
      RenderSystem.enableCull();
      if ((Boolean)this.throughWalls.getValue()) {
         RenderSystem.enableDepthTest();
      }

      Render3DEngine.endRender();
   }

   @EventHandler
   public void postSync(EventPostSync e) {
      if (this.positions.size() > (Integer)this.limit.getValue()) {
         this.positions.remove(0);
      }

      this.positions.add(new class_243(mc.field_1724.method_23317(), mc.field_1724.method_5829().field_1322, mc.field_1724.method_23321()));
   }

   public void onDisable() {
      this.positions.clear();
   }

   private static enum Mode {
      Custom,
      Sync;

      // $FF: synthetic method
      private static BreadCrumbs.Mode[] $values() {
         return new BreadCrumbs.Mode[]{Custom, Sync};
      }
   }
}
