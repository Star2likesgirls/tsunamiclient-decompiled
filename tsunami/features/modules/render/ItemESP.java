package tsunami.features.modules.render;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.util.Iterator;
import net.minecraft.class_1297;
import net.minecraft.class_1542;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_287;
import net.minecraft.class_289;
import net.minecraft.class_290;
import net.minecraft.class_332;
import net.minecraft.class_4587;
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

public class ItemESP extends Module {
   private final Setting<Boolean> shadow = new Setting("Shadow", true);
   private final Setting<ColorSetting> scolor = new Setting("ShadowColor", new ColorSetting((new Color(0)).getRGB()));
   private final Setting<ColorSetting> tcolor = new Setting("TextColor", new ColorSetting((new Color(-1)).getRGB()));
   private final Setting<ItemESP.ESPMode> espMode;
   private final Setting<Float> radius;
   private final Setting<Boolean> useHudColor;
   private final Setting<Integer> cOffset;
   private final Setting<ColorSetting> circleColor;
   private final Setting<Integer> cPoints;

   public ItemESP() {
      super("ItemESP", Module.Category.RENDER);
      this.espMode = new Setting("Mode", ItemESP.ESPMode.Rect);
      this.radius = new Setting("Radius", 1.0F, 0.1F, 5.0F, (v) -> {
         return this.espMode.getValue() == ItemESP.ESPMode.Circle;
      });
      this.useHudColor = new Setting("UseHudColor", true, (v) -> {
         return this.espMode.getValue() == ItemESP.ESPMode.Circle;
      });
      this.cOffset = new Setting("ColorOffset", 2, 1, 50, (v) -> {
         return this.espMode.getValue() == ItemESP.ESPMode.Circle && (Boolean)this.useHudColor.getValue();
      });
      this.circleColor = new Setting("CircleColor", new ColorSetting((new Color(-1)).getRGB()), (v) -> {
         return this.espMode.getValue() == ItemESP.ESPMode.Circle && !(Boolean)this.useHudColor.getValue();
      });
      this.cPoints = new Setting("CirclePoints", 12, 3, 32, (v) -> {
         return this.espMode.getValue() == ItemESP.ESPMode.Circle;
      });
   }

   public void onRender2D(class_332 context) {
      Iterator var2 = mc.field_1687.method_18112().iterator();

      while(true) {
         class_1297 ent;
         int var8;
         float posY;
         float endPosX;
         float posX;
         do {
            if (!var2.hasNext()) {
               if (this.espMode.getValue() == ItemESP.ESPMode.Rect) {
                  boolean any = false;
                  Iterator var14 = mc.field_1687.method_18112().iterator();

                  while(true) {
                     class_1297 ent;
                     class_243[] vectors;
                     do {
                        if (!var14.hasNext()) {
                           if (!any) {
                              return;
                           }

                           Matrix4f matrix = context.method_51448().method_23760().method_23761();
                           Render2DEngine.setupRender();
                           RenderSystem.setShader(class_757::method_34540);
                           class_287 bufferBuilder = class_289.method_1348().method_60827(class_5596.field_27382, class_290.field_1576);
                           Iterator var19 = mc.field_1687.method_18112().iterator();

                           while(true) {
                              class_1297 ent;
                              do {
                                 if (!var19.hasNext()) {
                                    Render2DEngine.endBuilding(bufferBuilder);
                                    Render2DEngine.endRender();
                                    return;
                                 }

                                 ent = (class_1297)var19.next();
                              } while(!(ent instanceof class_1542));

                              vectors = getPoints(ent);
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
                                 posY = (float)position.y;
                                 endPosX = (float)position.z;
                                 float endPosY = (float)position.w;
                                 this.drawRect(bufferBuilder, matrix, posX, posY, endPosX, endPosY);
                              }
                           }
                        }

                        ent = (class_1297)var14.next();
                     } while(!(ent instanceof class_1542));

                     class_243[] vectors = getPoints(ent);
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
               }

               return;
            }

            ent = (class_1297)var2.next();
         } while(!(ent instanceof class_1542));

         class_243[] vectors = getPoints(ent);
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
            posY = FontRenderers.sf_bold_mini.getStringWidth(ent.method_5476().getString()) * 1.0F;
            endPosX = (posX + posX - posY / 2.0F) * 1.0F;
            if ((Boolean)this.shadow.getValue()) {
               Render2DEngine.drawBlurredShadow(context.method_51448(), endPosX - 2.0F, posY - 13.0F, FontRenderers.sf_bold_mini.getStringWidth(ent.method_5476().getString()) + 4.0F, 10.0F, 14, ((ColorSetting)this.scolor.getValue()).getColorObject());
            }

            FontRenderers.sf_bold_mini.drawString(context.method_51448(), ent.method_5476().getString(), (double)endPosX, (double)(posY - 10.0F), ((ColorSetting)this.tcolor.getValue()).getColor());
         }
      }
   }

   public void onRender3D(class_4587 stack) {
      if (this.espMode.getValue() == ItemESP.ESPMode.Circle) {
         Iterator var2 = mc.field_1687.method_18112().iterator();

         while(var2.hasNext()) {
            class_1297 ent = (class_1297)var2.next();
            if (ent instanceof class_1542) {
               Render3DEngine.drawCircle3D(stack, ent, (Float)this.radius.getValue(), ((ColorSetting)this.circleColor.getValue()).getColor(), (Integer)this.cPoints.getValue(), (Boolean)this.useHudColor.getValue(), (Integer)this.cOffset.getValue());
            }
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
   private static class_243[] getPoints(class_1297 ent) {
      class_238 axisAlignedBB = getBox(ent);
      class_243[] vectors = new class_243[]{new class_243(axisAlignedBB.field_1323, axisAlignedBB.field_1322, axisAlignedBB.field_1321), new class_243(axisAlignedBB.field_1323, axisAlignedBB.field_1325, axisAlignedBB.field_1321), new class_243(axisAlignedBB.field_1320, axisAlignedBB.field_1322, axisAlignedBB.field_1321), new class_243(axisAlignedBB.field_1320, axisAlignedBB.field_1325, axisAlignedBB.field_1321), new class_243(axisAlignedBB.field_1323, axisAlignedBB.field_1322, axisAlignedBB.field_1324), new class_243(axisAlignedBB.field_1323, axisAlignedBB.field_1325, axisAlignedBB.field_1324), new class_243(axisAlignedBB.field_1320, axisAlignedBB.field_1322, axisAlignedBB.field_1324), new class_243(axisAlignedBB.field_1320, axisAlignedBB.field_1325, axisAlignedBB.field_1324)};
      return vectors;
   }

   @NotNull
   private static class_238 getBox(class_1297 ent) {
      double x = ent.field_6014 + (ent.method_23317() - ent.field_6014) * (double)Render3DEngine.getTickDelta();
      double y = ent.field_6036 + (ent.method_23318() - ent.field_6036) * (double)Render3DEngine.getTickDelta();
      double z = ent.field_5969 + (ent.method_23321() - ent.field_5969) * (double)Render3DEngine.getTickDelta();
      class_238 axisAlignedBB2 = ent.method_5829();
      class_238 axisAlignedBB = new class_238(axisAlignedBB2.field_1323 - ent.method_23317() + x - 0.05D, axisAlignedBB2.field_1322 - ent.method_23318() + y, axisAlignedBB2.field_1321 - ent.method_23321() + z - 0.05D, axisAlignedBB2.field_1320 - ent.method_23317() + x + 0.05D, axisAlignedBB2.field_1325 - ent.method_23318() + y + 0.15D, axisAlignedBB2.field_1324 - ent.method_23321() + z + 0.05D);
      return axisAlignedBB;
   }

   private static enum ESPMode {
      Rect,
      Circle,
      None;

      // $FF: synthetic method
      private static ItemESP.ESPMode[] $values() {
         return new ItemESP.ESPMode[]{Rect, Circle, None};
      }
   }
}
