package tsunami.features.modules.render;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_238;
import net.minecraft.class_2393;
import net.minecraft.class_243;
import net.minecraft.class_2680;
import net.minecraft.class_287;
import net.minecraft.class_289;
import net.minecraft.class_290;
import net.minecraft.class_332;
import net.minecraft.class_757;
import net.minecraft.class_293.class_5596;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Vector4d;
import tsunami.features.modules.Module;
import tsunami.features.modules.client.HudEditor;
import tsunami.gui.font.FontRenderers;
import tsunami.setting.Setting;
import tsunami.setting.impl.ColorSetting;
import tsunami.utility.render.Render2DEngine;
import tsunami.utility.render.Render3DEngine;

public class KelpEsp extends Module {
   private final Setting<Boolean> shadow = new Setting("Shadow", true);
   private final Setting<ColorSetting> scolor = new Setting("ShadowColor", new ColorSetting((new Color(0)).getRGB()));
   private final Setting<ColorSetting> tcolor = new Setting("TextColor", new ColorSetting((new Color(-1)).getRGB()));
   private final List<class_2338> kelpPositions = new ArrayList();
   private int lastUpdateTick = 0;

   public KelpEsp() {
      super("KelpESP", Module.Category.DONUT);
   }

   public void onRender2D(class_332 context) {
      if (mc.field_1724.field_6012 - this.lastUpdateTick > 20) {
         this.updateKelpPositions();
         this.lastUpdateTick = mc.field_1724.field_6012;
      }

      Iterator var2 = this.kelpPositions.iterator();

      int var8;
      float endPosX;
      float endPosY;
      float posX;
      while(var2.hasNext()) {
         class_2338 pos = (class_2338)var2.next();
         class_243[] vectors = getPoints(pos);
         Vector4d position = null;
         class_243[] var6 = vectors;
         int var7 = vectors.length;

         for(var8 = 0; var8 < var7; ++var8) {
            class_243 vector = var6[var8];
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
            float posX = (float)position.x;
            float posY = (float)position.y;
            float endPosX = (float)position.z;
            posX = (endPosX - posX) / 2.0F;
            String displayText = String.format("%.1fm", Math.sqrt(mc.field_1724.method_5707(class_243.method_24954(pos))));
            endPosX = FontRenderers.sf_bold.getStringWidth(displayText) * 1.0F;
            endPosY = (posX + posX - endPosX / 2.0F) * 1.0F;
            if ((Boolean)this.shadow.getValue()) {
               Render2DEngine.drawBlurredShadow(context.method_51448(), endPosY - 2.0F, posY - 13.0F, FontRenderers.sf_bold.getStringWidth(displayText) + 4.0F, 10.0F, 14, ((ColorSetting)this.scolor.getValue()).getColorObject());
            }

            FontRenderers.sf_bold.drawString(context.method_51448(), displayText, (double)endPosY, (double)(posY - 10.0F), ((ColorSetting)this.tcolor.getValue()).getColor());
         }
      }

      boolean any = false;
      Iterator var14 = this.kelpPositions.iterator();

      class_243[] vectors;
      while(var14.hasNext()) {
         class_2338 pos = (class_2338)var14.next();
         class_243[] vectors = getPoints(pos);
         Vector4d position = null;
         vectors = vectors;
         var8 = vectors.length;

         for(int var28 = 0; var28 < var8; ++var28) {
            class_243 vector = vectors[var28];
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
            any = true;
         }
      }

      if (any) {
         Matrix4f matrix = context.method_51448().method_23760().method_23761();
         Render2DEngine.setupRender();
         RenderSystem.setShader(class_757::method_34540);
         class_287 bufferBuilder = class_289.method_1348().method_60827(class_5596.field_27382, class_290.field_1576);
         Iterator var19 = this.kelpPositions.iterator();

         while(var19.hasNext()) {
            class_2338 pos = (class_2338)var19.next();
            vectors = getPoints(pos);
            Vector4d position = null;
            class_243[] var30 = vectors;
            int var31 = vectors.length;

            for(int var32 = 0; var32 < var31; ++var32) {
               class_243 vector = var30[var32];
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
               posX = (float)position.x;
               float posY = (float)position.y;
               endPosX = (float)position.z;
               endPosY = (float)position.w;
               this.drawRect(bufferBuilder, matrix, posX, posY, endPosX, endPosY);
            }
         }

         Render2DEngine.endBuilding(bufferBuilder);
         Render2DEngine.endRender();
      }
   }

   private void updateKelpPositions() {
      this.kelpPositions.clear();
      if (mc.field_1687 != null && mc.field_1724 != null) {
         int renderDistance = (Integer)mc.field_1690.method_42503().method_41753() * 16;
         class_2338 playerPos = mc.field_1724.method_24515();

         for(int x = playerPos.method_10263() - renderDistance; x <= playerPos.method_10263() + renderDistance; ++x) {
            for(int z = playerPos.method_10260() - renderDistance; z <= playerPos.method_10260() + renderDistance; ++z) {
               for(int y = Math.min(80, mc.field_1687.method_31600()); y >= Math.max(30, mc.field_1687.method_31607()); --y) {
                  class_2338 pos = new class_2338(x, y, z);
                  if (this.isKelpTouchingWaterSurface(pos)) {
                     this.kelpPositions.add(pos);
                     break;
                  }
               }
            }
         }

      }
   }

   private boolean isKelpTouchingWaterSurface(class_2338 pos) {
      class_2680 state = mc.field_1687.method_8320(pos);
      if (!(state.method_26204() instanceof class_2393)) {
         return false;
      } else {
         class_2338 above = pos.method_10084();
         class_2680 aboveState = mc.field_1687.method_8320(above);
         if (aboveState.method_27852(class_2246.field_10124)) {
            return true;
         } else if (aboveState.method_27852(class_2246.field_10382)) {
            class_2338 twoAbove = above.method_10084();
            class_2680 twoAboveState = mc.field_1687.method_8320(twoAbove);
            return twoAboveState.method_27852(class_2246.field_10124);
         } else {
            return false;
         }
      }
   }

   private void drawRect(class_287 bufferBuilder, Matrix4f stack, float posX, float posY, float endPosX, float endPosY) {
      Color black = Color.BLACK;
      Render2DEngine.setRectPoints(bufferBuilder, stack, posX - 1.0F, posY, posX + 0.5F, endPosY + 0.5F, black, black, black, black);
      Render2DEngine.setRectPoints(bufferBuilder, stack, posX - 1.0F, posY - 0.5F, endPosX + 0.5F, posY + 1.0F, black, black, black, black);
      Render2DEngine.setRectPoints(bufferBuilder, stack, endPosX - 1.0F, posY, endPosX + 0.5F, endPosY + 0.5F, black, black, black, black);
      Render2DEngine.setRectPoints(bufferBuilder, stack, posX - 1.0F, endPosY - 1.0F, endPosX + 0.5F, endPosY + 0.5F, black, black, black, black);
      Render2DEngine.setRectPoints(bufferBuilder, stack, posX - 0.5F, posY, posX, endPosY, HudEditor.getColor(270), HudEditor.getColor(0), HudEditor.getColor(0), HudEditor.getColor(270));
      Render2DEngine.setRectPoints(bufferBuilder, stack, posX, endPosY - 0.5F, endPosX, endPosY, HudEditor.getColor(0), HudEditor.getColor(180), HudEditor.getColor(180), HudEditor.getColor(0));
      Render2DEngine.setRectPoints(bufferBuilder, stack, posX - 0.5F, posY, endPosX, posY + 0.5F, HudEditor.getColor(180), HudEditor.getColor(90), HudEditor.getColor(90), HudEditor.getColor(180));
      Render2DEngine.setRectPoints(bufferBuilder, stack, endPosX - 0.5F, posY, endPosX, endPosY, HudEditor.getColor(90), HudEditor.getColor(270), HudEditor.getColor(270), HudEditor.getColor(90));
   }

   @NotNull
   private static class_243[] getPoints(class_2338 pos) {
      class_238 axisAlignedBB = getBox(pos);
      class_243[] vectors = new class_243[]{new class_243(axisAlignedBB.field_1323, axisAlignedBB.field_1322, axisAlignedBB.field_1321), new class_243(axisAlignedBB.field_1323, axisAlignedBB.field_1325, axisAlignedBB.field_1321), new class_243(axisAlignedBB.field_1320, axisAlignedBB.field_1322, axisAlignedBB.field_1321), new class_243(axisAlignedBB.field_1320, axisAlignedBB.field_1325, axisAlignedBB.field_1321), new class_243(axisAlignedBB.field_1323, axisAlignedBB.field_1322, axisAlignedBB.field_1324), new class_243(axisAlignedBB.field_1323, axisAlignedBB.field_1325, axisAlignedBB.field_1324), new class_243(axisAlignedBB.field_1320, axisAlignedBB.field_1322, axisAlignedBB.field_1324), new class_243(axisAlignedBB.field_1320, axisAlignedBB.field_1325, axisAlignedBB.field_1324)};
      return vectors;
   }

   @NotNull
   private static class_238 getBox(class_2338 pos) {
      return new class_238(pos);
   }
}
