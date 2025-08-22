package tsunami.features.modules.render;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.util.Iterator;
import net.minecraft.class_1297;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_287;
import net.minecraft.class_289;
import net.minecraft.class_290;
import net.minecraft.class_332;
import net.minecraft.class_3986;
import net.minecraft.class_3989;
import net.minecraft.class_4587;
import net.minecraft.class_757;
import net.minecraft.class_293.class_5596;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Vector4d;
import tsunami.features.modules.Module;
import tsunami.features.modules.client.HudEditor;
import tsunami.setting.Setting;
import tsunami.setting.impl.ColorSetting;
import tsunami.utility.render.Render2DEngine;
import tsunami.utility.render.Render3DEngine;

public class WanderingESP extends Module {
   private final Setting<Boolean> wanderingTraders = new Setting("WanderingTraders", true);
   private final Setting<Boolean> wanderingLlamas = new Setting("WanderingLlamas", true);
   private final Setting<Boolean> outline = new Setting("Outline", true);
   private final Setting<WanderingESP.Colors> colorMode;
   private final Setting<ColorSetting> traderColor;
   private final Setting<ColorSetting> llamaColor;
   private final Setting<Boolean> tracers;
   private final Setting<Float> tracerHeight;
   private final Setting<ColorSetting> tracerTraderColor;
   private final Setting<ColorSetting> tracerLlamaColor;

   public WanderingESP() {
      super("WanderingESP", Module.Category.DONUT);
      this.colorMode = new Setting("ColorMode", WanderingESP.Colors.Custom);
      this.traderColor = new Setting("TraderColor", new ColorSetting(new Color(65280)));
      this.llamaColor = new Setting("LlamaColor", new ColorSetting(new Color(16755200)));
      this.tracers = new Setting("Tracers", true);
      this.tracerHeight = new Setting("TracerHeight", 0.0F, 0.0F, 2.0F, (v) -> {
         return (Boolean)this.tracers.getValue();
      });
      this.tracerTraderColor = new Setting("TracerTraderColor", new ColorSetting(new Color(-1828651264, true)), (v) -> {
         return (Boolean)this.tracers.getValue();
      });
      this.tracerLlamaColor = new Setting("TracerLlamaColor", new ColorSetting(new Color(-1811961344, true)), (v) -> {
         return (Boolean)this.tracers.getValue();
      });
   }

   public void onRender3D(class_4587 stack) {
      if (!mc.field_1690.field_1842) {
         if ((Boolean)this.tracers.getValue()) {
            this.renderTracers();
         }

      }
   }

   public void onRender2D(class_332 context) {
      if (!mc.field_1690.field_1842) {
         Matrix4f matrix = context.method_51448().method_23760().method_23761();
         Render2DEngine.setupRender();
         RenderSystem.setShader(class_757::method_34540);
         class_287 bufferBuilder = class_289.method_1348().method_60827(class_5596.field_27382, class_290.field_1576);
         Iterator var4 = mc.field_1687.method_18112().iterator();

         while(var4.hasNext()) {
            class_1297 ent = (class_1297)var4.next();
            if (this.shouldRender(ent)) {
               this.drawBox(bufferBuilder, ent, matrix);
            }
         }

         Render2DEngine.endBuilding(bufferBuilder);
         Render2DEngine.endRender();
      }
   }

   private void renderTracers() {
      Iterator var1 = mc.field_1687.method_18112().iterator();

      while(var1.hasNext()) {
         class_1297 entity = (class_1297)var1.next();
         if (this.shouldRenderTracer(entity)) {
            Color tracerColor = this.getTracerColor(entity);
            double x1 = mc.field_1724.field_6014 + (mc.field_1724.method_23317() - mc.field_1724.field_6014) * (double)Render3DEngine.getTickDelta();
            double y1 = (double)mc.field_1724.method_18381(mc.field_1724.method_18376()) + mc.field_1724.field_6036 + (mc.field_1724.method_23318() - mc.field_1724.field_6036) * (double)Render3DEngine.getTickDelta();
            double z1 = mc.field_1724.field_5969 + (mc.field_1724.method_23321() - mc.field_1724.field_5969) * (double)Render3DEngine.getTickDelta();
            class_243 vec2 = (new class_243(0.0D, 0.0D, 75.0D)).method_1037(-((float)Math.toRadians((double)mc.field_1773.method_19418().method_19329()))).method_1024(-((float)Math.toRadians((double)mc.field_1773.method_19418().method_19330()))).method_1031(x1, y1, z1);
            double x = entity.field_6014 + (entity.method_23317() - entity.field_6014) * (double)Render3DEngine.getTickDelta();
            double y = entity.field_6036 + (entity.method_23318() - entity.field_6036) * (double)Render3DEngine.getTickDelta();
            double z = entity.field_5969 + (entity.method_23321() - entity.field_5969) * (double)Render3DEngine.getTickDelta();
            Render3DEngine.drawLineDebug(vec2, new class_243(x, y + (double)(Float)this.tracerHeight.getValue(), z), tracerColor);
         }
      }

   }

   public boolean shouldRender(class_1297 entity) {
      if (entity != null && mc.field_1724 != null) {
         if (entity instanceof class_3989) {
            return (Boolean)this.wanderingTraders.getValue();
         } else {
            return entity instanceof class_3986 ? (Boolean)this.wanderingLlamas.getValue() : false;
         }
      } else {
         return false;
      }
   }

   public boolean shouldRenderTracer(class_1297 entity) {
      if ((Boolean)this.tracers.getValue() && entity != null && mc.field_1724 != null) {
         if (entity instanceof class_3989) {
            return (Boolean)this.wanderingTraders.getValue();
         } else {
            return entity instanceof class_3986 ? (Boolean)this.wanderingLlamas.getValue() : false;
         }
      } else {
         return false;
      }
   }

   public Color getEntityColor(class_1297 entity) {
      if (entity == null) {
         return new Color(-1);
      } else if (entity instanceof class_3989) {
         return ((ColorSetting)this.traderColor.getValue()).getColorObject();
      } else {
         return entity instanceof class_3986 ? ((ColorSetting)this.llamaColor.getValue()).getColorObject() : new Color(-1);
      }
   }

   public Color getTracerColor(class_1297 entity) {
      if (entity == null) {
         return new Color(-1);
      } else if (entity instanceof class_3989) {
         return ((ColorSetting)this.tracerTraderColor.getValue()).getColorObject();
      } else {
         return entity instanceof class_3986 ? ((ColorSetting)this.tracerLlamaColor.getValue()).getColorObject() : new Color(-1);
      }
   }

   public void drawBox(class_287 bufferBuilder, @NotNull class_1297 ent, Matrix4f matrix) {
      class_243[] vectors = getVectors(ent);
      Color col = this.getEntityColor(ent);
      Vector4d position = null;
      class_243[] var7 = vectors;
      int var8 = vectors.length;

      for(int var9 = 0; var9 < var8; ++var9) {
         class_243 vector = var7[var9];
         vector = Render3DEngine.worldSpaceToScreenSpace(new class_243(vector.field_1352, vector.field_1351, vector.field_1350));
         if (vector.field_1350 > 0.0D && vector.field_1350 < 1.0D) {
            if (position == null) {
               position = new Vector4d(vector.field_1352, vector.field_1351, vector.field_1350, 0.0D);
            }

            position.x = Math.min(vector.field_1352, position.x);
            position.y = Math.min(vector.field_1351, position.y);
            position.z = Math.max(vector.field_1352, position.z);
            position.w = Math.max(vector.field_1351, position.w);
         }
      }

      if (position != null) {
         double posX = position.x;
         double posY = position.y;
         double endPosX = position.z;
         double endPosY = position.w;
         if ((Boolean)this.outline.getValue()) {
            Render2DEngine.setRectPoints(bufferBuilder, matrix, (float)(posX - 1.0D), (float)posY, (float)(posX + 0.5D), (float)(endPosY + 0.5D), Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK);
            Render2DEngine.setRectPoints(bufferBuilder, matrix, (float)(posX - 1.0D), (float)(posY - 0.5D), (float)(endPosX + 0.5D), (float)(posY + 0.5D + 0.5D), Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK);
            Render2DEngine.setRectPoints(bufferBuilder, matrix, (float)(endPosX - 0.5D - 0.5D), (float)posY, (float)(endPosX + 0.5D), (float)(endPosY + 0.5D), Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK);
            Render2DEngine.setRectPoints(bufferBuilder, matrix, (float)(posX - 1.0D), (float)(endPosY - 0.5D - 0.5D), (float)(endPosX + 0.5D), (float)(endPosY + 0.5D), Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK);
         }

         switch(((WanderingESP.Colors)this.colorMode.getValue()).ordinal()) {
         case 0:
            Render2DEngine.setRectPoints(bufferBuilder, matrix, (float)(posX - 0.5D), (float)posY, (float)(posX + 0.5D - 0.5D), (float)endPosY, HudEditor.getColor(270), HudEditor.getColor(0), HudEditor.getColor(0), HudEditor.getColor(270));
            Render2DEngine.setRectPoints(bufferBuilder, matrix, (float)posX, (float)(endPosY - 0.5D), (float)endPosX, (float)endPosY, HudEditor.getColor(0), HudEditor.getColor(180), HudEditor.getColor(180), HudEditor.getColor(0));
            Render2DEngine.setRectPoints(bufferBuilder, matrix, (float)(posX - 0.5D), (float)posY, (float)endPosX, (float)(posY + 0.5D), HudEditor.getColor(180), HudEditor.getColor(90), HudEditor.getColor(90), HudEditor.getColor(180));
            Render2DEngine.setRectPoints(bufferBuilder, matrix, (float)(endPosX - 0.5D), (float)posY, (float)endPosX, (float)endPosY, HudEditor.getColor(90), HudEditor.getColor(270), HudEditor.getColor(270), HudEditor.getColor(90));
            break;
         case 1:
            Render2DEngine.setRectPoints(bufferBuilder, matrix, (float)(posX - 0.5D), (float)posY, (float)(posX + 0.5D - 0.5D), (float)endPosY, col, col, col, col);
            Render2DEngine.setRectPoints(bufferBuilder, matrix, (float)posX, (float)(endPosY - 0.5D), (float)endPosX, (float)endPosY, col, col, col, col);
            Render2DEngine.setRectPoints(bufferBuilder, matrix, (float)(posX - 0.5D), (float)posY, (float)endPosX, (float)(posY + 0.5D), col, col, col, col);
            Render2DEngine.setRectPoints(bufferBuilder, matrix, (float)(endPosX - 0.5D), (float)posY, (float)endPosX, (float)endPosY, col, col, col, col);
         }
      }

   }

   @NotNull
   private static class_243[] getVectors(@NotNull class_1297 ent) {
      double x = ent.field_6014 + (ent.method_23317() - ent.field_6014) * (double)Render3DEngine.getTickDelta();
      double y = ent.field_6036 + (ent.method_23318() - ent.field_6036) * (double)Render3DEngine.getTickDelta();
      double z = ent.field_5969 + (ent.method_23321() - ent.field_5969) * (double)Render3DEngine.getTickDelta();
      class_238 axisAlignedBB2 = ent.method_5829();
      class_238 axisAlignedBB = new class_238(axisAlignedBB2.field_1323 - ent.method_23317() + x - 0.05D, axisAlignedBB2.field_1322 - ent.method_23318() + y, axisAlignedBB2.field_1321 - ent.method_23321() + z - 0.05D, axisAlignedBB2.field_1320 - ent.method_23317() + x + 0.05D, axisAlignedBB2.field_1325 - ent.method_23318() + y + 0.15D, axisAlignedBB2.field_1324 - ent.method_23321() + z + 0.05D);
      return new class_243[]{new class_243(axisAlignedBB.field_1323, axisAlignedBB.field_1322, axisAlignedBB.field_1321), new class_243(axisAlignedBB.field_1323, axisAlignedBB.field_1325, axisAlignedBB.field_1321), new class_243(axisAlignedBB.field_1320, axisAlignedBB.field_1322, axisAlignedBB.field_1321), new class_243(axisAlignedBB.field_1320, axisAlignedBB.field_1325, axisAlignedBB.field_1321), new class_243(axisAlignedBB.field_1323, axisAlignedBB.field_1322, axisAlignedBB.field_1324), new class_243(axisAlignedBB.field_1323, axisAlignedBB.field_1325, axisAlignedBB.field_1324), new class_243(axisAlignedBB.field_1320, axisAlignedBB.field_1322, axisAlignedBB.field_1324), new class_243(axisAlignedBB.field_1320, axisAlignedBB.field_1325, axisAlignedBB.field_1324)};
   }

   public static enum Colors {
      SyncColor,
      Custom;

      // $FF: synthetic method
      private static WanderingESP.Colors[] $values() {
         return new WanderingESP.Colors[]{SyncColor, Custom};
      }
   }
}
