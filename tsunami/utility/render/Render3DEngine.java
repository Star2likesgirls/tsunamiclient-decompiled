package tsunami.utility.render;

import com.mojang.blaze3d.platform.GlStateManager.class_4534;
import com.mojang.blaze3d.platform.GlStateManager.class_4535;
import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_1297;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_286;
import net.minecraft.class_287;
import net.minecraft.class_289;
import net.minecraft.class_290;
import net.minecraft.class_310;
import net.minecraft.class_3532;
import net.minecraft.class_4184;
import net.minecraft.class_4587;
import net.minecraft.class_4588;
import net.minecraft.class_757;
import net.minecraft.class_7833;
import net.minecraft.class_293.class_5596;
import net.minecraft.class_4587.class_4665;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;
import tsunami.TsunamiClient;
import tsunami.features.modules.Module;
import tsunami.features.modules.client.ClientSettings;
import tsunami.features.modules.client.HudEditor;
import tsunami.gui.font.FontRenderers;

public class Render3DEngine {
   public static List<Render3DEngine.FillAction> FILLED_QUEUE = new ArrayList();
   public static List<Render3DEngine.OutlineAction> OUTLINE_QUEUE = new ArrayList();
   public static List<Render3DEngine.FadeAction> FADE_QUEUE = new ArrayList();
   public static List<Render3DEngine.FillSideAction> FILLED_SIDE_QUEUE = new ArrayList();
   public static List<Render3DEngine.OutlineSideAction> OUTLINE_SIDE_QUEUE = new ArrayList();
   public static List<Render3DEngine.DebugLineAction> DEBUG_LINE_QUEUE = new ArrayList();
   public static List<Render3DEngine.LineAction> LINE_QUEUE = new ArrayList();
   public static final Matrix4f lastProjMat = new Matrix4f();
   public static final Matrix4f lastModMat = new Matrix4f();
   public static final Matrix4f lastWorldSpaceMatrix = new Matrix4f();
   private static float prevCircleStep;
   private static float circleStep;

   public static void onRender3D(class_4587 stack) {
      class_289 tessellator;
      class_287 buffer;
      if (!FILLED_QUEUE.isEmpty() || !FADE_QUEUE.isEmpty() || !FILLED_SIDE_QUEUE.isEmpty()) {
         tessellator = class_289.method_1348();
         buffer = tessellator.method_60827(class_5596.field_27382, class_290.field_1576);
         RenderSystem.disableDepthTest();
         setupRender();
         RenderSystem.setShader(class_757::method_34540);
         FILLED_QUEUE.forEach((action) -> {
            setFilledBoxVertexes(buffer, stack.method_23760().method_23761(), action.box(), action.color());
         });
         FADE_QUEUE.forEach((action) -> {
            setFilledFadePoints(action.box(), buffer, stack.method_23760().method_23761(), action.color(), action.color2());
         });
         FILLED_SIDE_QUEUE.forEach((action) -> {
            setFilledSidePoints(buffer, stack.method_23760().method_23761(), action.box, action.color(), action.side());
         });
         Render2DEngine.endBuilding(buffer);
         endRender();
         RenderSystem.enableDepthTest();
         FADE_QUEUE.clear();
         FILLED_SIDE_QUEUE.clear();
         FILLED_QUEUE.clear();
      }

      if (!OUTLINE_QUEUE.isEmpty() || !OUTLINE_SIDE_QUEUE.isEmpty()) {
         setupRender();
         tessellator = class_289.method_1348();
         buffer = tessellator.method_60827(class_5596.field_27377, class_290.field_29337);
         RenderSystem.disableCull();
         RenderSystem.disableDepthTest();
         RenderSystem.setShader(class_757::method_34535);
         RenderSystem.lineWidth(2.0F);
         OUTLINE_QUEUE.forEach((action) -> {
            setOutlinePoints(action.box(), matrixFrom(action.box().field_1323, action.box().field_1322, action.box().field_1321), buffer, action.color());
         });
         OUTLINE_SIDE_QUEUE.forEach((action) -> {
            setSideOutlinePoints(action.box, matrixFrom(action.box().field_1323, action.box().field_1322, action.box().field_1321), buffer, action.color(), action.side());
         });
         Render2DEngine.endBuilding(buffer);
         RenderSystem.enableCull();
         RenderSystem.enableDepthTest();
         endRender();
         OUTLINE_QUEUE.clear();
         OUTLINE_SIDE_QUEUE.clear();
      }

      if (!DEBUG_LINE_QUEUE.isEmpty()) {
         setupRender();
         RenderSystem.disableDepthTest();
         tessellator = class_289.method_1348();
         buffer = tessellator.method_60827(class_5596.field_29344, class_290.field_29337);
         RenderSystem.disableCull();
         RenderSystem.setShader(class_757::method_34535);
         DEBUG_LINE_QUEUE.forEach((action) -> {
            class_4587 matrices = matrixFrom(action.start.method_10216(), action.start.method_10214(), action.start.method_10215());
            vertexLine(matrices, buffer, 0.0F, 0.0F, 0.0F, (float)(action.end.method_10216() - action.start.method_10216()), (float)(action.end.method_10214() - action.start.method_10214()), (float)(action.end.method_10215() - action.start.method_10215()), action.color);
         });
         Render2DEngine.endBuilding(buffer);
         RenderSystem.enableCull();
         RenderSystem.enableDepthTest();
         endRender();
         DEBUG_LINE_QUEUE.clear();
      }

      if (!LINE_QUEUE.isEmpty()) {
         setupRender();
         tessellator = class_289.method_1348();
         RenderSystem.disableCull();
         RenderSystem.setShader(class_757::method_34535);
         RenderSystem.lineWidth(2.0F);
         RenderSystem.disableDepthTest();
         buffer = tessellator.method_60827(class_5596.field_27377, class_290.field_29337);
         LINE_QUEUE.forEach((action) -> {
            class_4587 matrices = matrixFrom(action.start.method_10216(), action.start.method_10214(), action.start.method_10215());
            vertexLine(matrices, buffer, 0.0F, 0.0F, 0.0F, (float)(action.end.method_10216() - action.start.method_10216()), (float)(action.end.method_10214() - action.start.method_10214()), (float)(action.end.method_10215() - action.start.method_10215()), action.color);
         });
         Render2DEngine.endBuilding(buffer);
         RenderSystem.enableCull();
         RenderSystem.lineWidth(1.0F);
         RenderSystem.enableDepthTest();
         endRender();
         LINE_QUEUE.clear();
      }

   }

   /** @deprecated */
   @Deprecated
   public static void drawFilledBox(class_4587 stack, class_238 box, Color c) {
      FILLED_QUEUE.add(new Render3DEngine.FillAction(box, c));
   }

   public static void setFilledBoxVertexes(@NotNull class_287 bufferBuilder, Matrix4f m, @NotNull class_238 box, @NotNull Color c) {
      float minX = (float)(box.field_1323 - Module.mc.method_1561().field_4686.method_19326().method_10216());
      float minY = (float)(box.field_1322 - Module.mc.method_1561().field_4686.method_19326().method_10214());
      float minZ = (float)(box.field_1321 - Module.mc.method_1561().field_4686.method_19326().method_10215());
      float maxX = (float)(box.field_1320 - Module.mc.method_1561().field_4686.method_19326().method_10216());
      float maxY = (float)(box.field_1325 - Module.mc.method_1561().field_4686.method_19326().method_10214());
      float maxZ = (float)(box.field_1324 - Module.mc.method_1561().field_4686.method_19326().method_10215());
      bufferBuilder.method_22918(m, minX, minY, minZ).method_39415(c.getRGB());
      bufferBuilder.method_22918(m, maxX, minY, minZ).method_39415(c.getRGB());
      bufferBuilder.method_22918(m, maxX, minY, maxZ).method_39415(c.getRGB());
      bufferBuilder.method_22918(m, minX, minY, maxZ).method_39415(c.getRGB());
      bufferBuilder.method_22918(m, minX, minY, minZ).method_39415(c.getRGB());
      bufferBuilder.method_22918(m, minX, maxY, minZ).method_39415(c.getRGB());
      bufferBuilder.method_22918(m, maxX, maxY, minZ).method_39415(c.getRGB());
      bufferBuilder.method_22918(m, maxX, minY, minZ).method_39415(c.getRGB());
      bufferBuilder.method_22918(m, maxX, minY, minZ).method_39415(c.getRGB());
      bufferBuilder.method_22918(m, maxX, maxY, minZ).method_39415(c.getRGB());
      bufferBuilder.method_22918(m, maxX, maxY, maxZ).method_39415(c.getRGB());
      bufferBuilder.method_22918(m, maxX, minY, maxZ).method_39415(c.getRGB());
      bufferBuilder.method_22918(m, minX, minY, maxZ).method_39415(c.getRGB());
      bufferBuilder.method_22918(m, maxX, minY, maxZ).method_39415(c.getRGB());
      bufferBuilder.method_22918(m, maxX, maxY, maxZ).method_39415(c.getRGB());
      bufferBuilder.method_22918(m, minX, maxY, maxZ).method_39415(c.getRGB());
      bufferBuilder.method_22918(m, minX, minY, minZ).method_39415(c.getRGB());
      bufferBuilder.method_22918(m, minX, minY, maxZ).method_39415(c.getRGB());
      bufferBuilder.method_22918(m, minX, maxY, maxZ).method_39415(c.getRGB());
      bufferBuilder.method_22918(m, minX, maxY, minZ).method_39415(c.getRGB());
      bufferBuilder.method_22918(m, minX, maxY, minZ).method_39415(c.getRGB());
      bufferBuilder.method_22918(m, minX, maxY, maxZ).method_39415(c.getRGB());
      bufferBuilder.method_22918(m, maxX, maxY, maxZ).method_39415(c.getRGB());
      bufferBuilder.method_22918(m, maxX, maxY, minZ).method_39415(c.getRGB());
   }

   @NotNull
   public static class_238 interpolateBox(@NotNull class_238 from, @NotNull class_238 to, float delta) {
      double X = Render2DEngine.interpolate(from.field_1320, to.field_1320, (double)delta);
      double Y = Render2DEngine.interpolate(from.field_1325, to.field_1325, (double)delta);
      double Z = Render2DEngine.interpolate(from.field_1324, to.field_1324, (double)delta);
      double X1 = Render2DEngine.interpolate(from.field_1323, to.field_1323, (double)delta);
      double Y1 = Render2DEngine.interpolate(from.field_1322, to.field_1322, (double)delta);
      double Z1 = Render2DEngine.interpolate(from.field_1321, to.field_1321, (double)delta);
      return new class_238(X1, Y1, Z1, X, Y, Z);
   }

   /** @deprecated */
   @Deprecated
   public static void drawFilledSide(class_4587 stack, @NotNull class_238 box, Color c, class_2350 dir) {
      FILLED_SIDE_QUEUE.add(new Render3DEngine.FillSideAction(box, c, dir));
   }

   public static void setFilledSidePoints(class_287 buffer, Matrix4f matrix, class_238 box, Color c, class_2350 dir) {
      float minX = (float)(box.field_1323 - Module.mc.method_1561().field_4686.method_19326().method_10216());
      float minY = (float)(box.field_1322 - Module.mc.method_1561().field_4686.method_19326().method_10214());
      float minZ = (float)(box.field_1321 - Module.mc.method_1561().field_4686.method_19326().method_10215());
      float maxX = (float)(box.field_1320 - Module.mc.method_1561().field_4686.method_19326().method_10216());
      float maxY = (float)(box.field_1325 - Module.mc.method_1561().field_4686.method_19326().method_10214());
      float maxZ = (float)(box.field_1324 - Module.mc.method_1561().field_4686.method_19326().method_10215());
      if (dir == class_2350.field_11033) {
         buffer.method_22918(matrix, minX, minY, minZ).method_39415(c.getRGB());
         buffer.method_22918(matrix, maxX, minY, minZ).method_39415(c.getRGB());
         buffer.method_22918(matrix, maxX, minY, maxZ).method_39415(c.getRGB());
         buffer.method_22918(matrix, minX, minY, maxZ).method_39415(c.getRGB());
      }

      if (dir == class_2350.field_11043) {
         buffer.method_22918(matrix, minX, minY, minZ).method_39415(c.getRGB());
         buffer.method_22918(matrix, minX, maxY, minZ).method_39415(c.getRGB());
         buffer.method_22918(matrix, maxX, maxY, minZ).method_39415(c.getRGB());
         buffer.method_22918(matrix, maxX, minY, minZ).method_39415(c.getRGB());
      }

      if (dir == class_2350.field_11034) {
         buffer.method_22918(matrix, maxX, minY, minZ).method_39415(c.getRGB());
         buffer.method_22918(matrix, maxX, maxY, minZ).method_39415(c.getRGB());
         buffer.method_22918(matrix, maxX, maxY, maxZ).method_39415(c.getRGB());
         buffer.method_22918(matrix, maxX, minY, maxZ).method_39415(c.getRGB());
      }

      if (dir == class_2350.field_11035) {
         buffer.method_22918(matrix, minX, minY, maxZ).method_39415(c.getRGB());
         buffer.method_22918(matrix, maxX, minY, maxZ).method_39415(c.getRGB());
         buffer.method_22918(matrix, maxX, maxY, maxZ).method_39415(c.getRGB());
         buffer.method_22918(matrix, minX, maxY, maxZ).method_39415(c.getRGB());
      }

      if (dir == class_2350.field_11039) {
         buffer.method_22918(matrix, minX, minY, minZ).method_39415(c.getRGB());
         buffer.method_22918(matrix, minX, minY, maxZ).method_39415(c.getRGB());
         buffer.method_22918(matrix, minX, maxY, maxZ).method_39415(c.getRGB());
         buffer.method_22918(matrix, minX, maxY, minZ).method_39415(c.getRGB());
      }

      if (dir == class_2350.field_11036) {
         buffer.method_22918(matrix, minX, maxY, minZ).method_39415(c.getRGB());
         buffer.method_22918(matrix, minX, maxY, maxZ).method_39415(c.getRGB());
         buffer.method_22918(matrix, maxX, maxY, maxZ).method_39415(c.getRGB());
         buffer.method_22918(matrix, maxX, maxY, minZ).method_39415(c.getRGB());
      }

   }

   public static void drawTextIn3D(String text, @NotNull class_243 pos, double offX, double offY, double textOffset, @NotNull Color color) {
      class_4587 matrices = new class_4587();
      class_4184 camera = Module.mc.field_1773.method_19418();
      RenderSystem.disableDepthTest();
      RenderSystem.disableCull();
      matrices.method_22907(class_7833.field_40714.rotationDegrees(camera.method_19329()));
      matrices.method_22907(class_7833.field_40716.rotationDegrees(camera.method_19330() + 180.0F));
      matrices.method_22904(pos.method_10216() - camera.method_19326().field_1352, pos.method_10214() - camera.method_19326().field_1351, pos.method_10215() - camera.method_19326().field_1350);
      matrices.method_22907(class_7833.field_40716.rotationDegrees(-camera.method_19330()));
      matrices.method_22907(class_7833.field_40714.rotationDegrees(camera.method_19329()));
      setupRender();
      matrices.method_22904(offX, offY - 0.1D, -0.01D);
      matrices.method_22905(-0.025F, -0.025F, 0.0F);
      FontRenderers.sf_medium.drawCenteredString(matrices, text, textOffset, 0.0D, color.getRGB());
      RenderSystem.enableCull();
      RenderSystem.enableDepthTest();
      endRender();
   }

   @NotNull
   public static class_243 worldSpaceToScreenSpace(@NotNull class_243 pos) {
      class_4184 camera = Module.mc.method_1561().field_4686;
      int displayHeight = Module.mc.method_22683().method_4507();
      int[] viewport = new int[4];
      GL11.glGetIntegerv(2978, viewport);
      Vector3f target = new Vector3f();
      double deltaX = pos.field_1352 - camera.method_19326().field_1352;
      double deltaY = pos.field_1351 - camera.method_19326().field_1351;
      double deltaZ = pos.field_1350 - camera.method_19326().field_1350;
      Vector4f transformedCoordinates = (new Vector4f((float)deltaX, (float)deltaY, (float)deltaZ, 1.0F)).mul(lastWorldSpaceMatrix);
      Matrix4f matrixProj = new Matrix4f(lastProjMat);
      Matrix4f matrixModel = new Matrix4f(lastModMat);
      matrixProj.mul(matrixModel).project(transformedCoordinates.x(), transformedCoordinates.y(), transformedCoordinates.z(), viewport, target);
      return new class_243((double)target.x / getScaleFactor(), (double)((float)displayHeight - target.y) / getScaleFactor(), (double)target.z);
   }

   public static double getScaleFactor() {
      return (Boolean)ClientSettings.scaleFactorFix.getValue() ? (double)(Float)ClientSettings.scaleFactorFixValue.getValue() : Module.mc.method_22683().method_4495();
   }

   /** @deprecated */
   @Deprecated
   public static void drawFilledFadeBox(@NotNull class_4587 stack, @NotNull class_238 box, @NotNull Color c, @NotNull Color c1) {
      FADE_QUEUE.add(new Render3DEngine.FadeAction(box, c, c1));
   }

   public static void setFilledFadePoints(class_238 box, class_287 buffer, Matrix4f posMatrix, Color c, Color c1) {
      float minX = (float)(box.field_1323 - Module.mc.method_1561().field_4686.method_19326().method_10216());
      float minY = (float)(box.field_1322 - Module.mc.method_1561().field_4686.method_19326().method_10214());
      float minZ = (float)(box.field_1321 - Module.mc.method_1561().field_4686.method_19326().method_10215());
      float maxX = (float)(box.field_1320 - Module.mc.method_1561().field_4686.method_19326().method_10216());
      float maxY = (float)(box.field_1325 - Module.mc.method_1561().field_4686.method_19326().method_10214());
      float maxZ = (float)(box.field_1324 - Module.mc.method_1561().field_4686.method_19326().method_10215());
      RenderSystem.enableCull();
      buffer.method_22918(posMatrix, minX, minY, minZ).method_39415(c.getRGB());
      buffer.method_22918(posMatrix, minX, maxY, minZ).method_39415(c1.getRGB());
      buffer.method_22918(posMatrix, maxX, maxY, minZ).method_39415(c1.getRGB());
      buffer.method_22918(posMatrix, maxX, minY, minZ).method_39415(c.getRGB());
      buffer.method_22918(posMatrix, maxX, minY, minZ).method_39415(c.getRGB());
      buffer.method_22918(posMatrix, maxX, maxY, minZ).method_39415(c1.getRGB());
      buffer.method_22918(posMatrix, maxX, maxY, maxZ).method_39415(c1.getRGB());
      buffer.method_22918(posMatrix, maxX, minY, maxZ).method_39415(c.getRGB());
      buffer.method_22918(posMatrix, minX, minY, maxZ).method_39415(c.getRGB());
      buffer.method_22918(posMatrix, maxX, minY, maxZ).method_39415(c.getRGB());
      buffer.method_22918(posMatrix, maxX, maxY, maxZ).method_39415(c1.getRGB());
      buffer.method_22918(posMatrix, minX, maxY, maxZ).method_39415(c1.getRGB());
      buffer.method_22918(posMatrix, minX, minY, minZ).method_39415(c.getRGB());
      buffer.method_22918(posMatrix, minX, minY, maxZ).method_39415(c.getRGB());
      buffer.method_22918(posMatrix, minX, maxY, maxZ).method_39415(c1.getRGB());
      buffer.method_22918(posMatrix, minX, maxY, minZ).method_39415(c1.getRGB());
      buffer.method_22918(posMatrix, minX, maxY, minZ).method_39415(c1.getRGB());
      buffer.method_22918(posMatrix, minX, maxY, maxZ).method_39415(c1.getRGB());
      buffer.method_22918(posMatrix, maxX, maxY, maxZ).method_39415(c1.getRGB());
      buffer.method_22918(posMatrix, maxX, maxY, minZ).method_39415(c1.getRGB());
      RenderSystem.disableCull();
   }

   public static void drawLine(@NotNull class_243 start, @NotNull class_243 end, @NotNull Color color) {
      LINE_QUEUE.add(new Render3DEngine.LineAction(start, end, color));
   }

   /** @deprecated */
   @Deprecated
   public static void drawBoxOutline(@NotNull class_238 box, Color color, float lineWidth) {
      OUTLINE_QUEUE.add(new Render3DEngine.OutlineAction(box, color, lineWidth));
   }

   public static void setOutlinePoints(class_238 box, class_4587 matrices, class_287 buffer, Color color) {
      box = box.method_997((new class_243(box.field_1323, box.field_1322, box.field_1321)).method_22882());
      float x1 = (float)box.field_1323;
      float y1 = (float)box.field_1322;
      float z1 = (float)box.field_1321;
      float x2 = (float)box.field_1320;
      float y2 = (float)box.field_1325;
      float z2 = (float)box.field_1324;
      vertexLine(matrices, buffer, x1, y1, z1, x2, y1, z1, color);
      vertexLine(matrices, buffer, x2, y1, z1, x2, y1, z2, color);
      vertexLine(matrices, buffer, x2, y1, z2, x1, y1, z2, color);
      vertexLine(matrices, buffer, x1, y1, z2, x1, y1, z1, color);
      vertexLine(matrices, buffer, x1, y1, z2, x1, y2, z2, color);
      vertexLine(matrices, buffer, x1, y1, z1, x1, y2, z1, color);
      vertexLine(matrices, buffer, x2, y1, z2, x2, y2, z2, color);
      vertexLine(matrices, buffer, x2, y1, z1, x2, y2, z1, color);
      vertexLine(matrices, buffer, x1, y2, z1, x2, y2, z1, color);
      vertexLine(matrices, buffer, x2, y2, z1, x2, y2, z2, color);
      vertexLine(matrices, buffer, x2, y2, z2, x1, y2, z2, color);
      vertexLine(matrices, buffer, x1, y2, z2, x1, y2, z1, color);
   }

   /** @deprecated */
   @Deprecated
   public static void drawSideOutline(@NotNull class_238 box, Color color, float lineWidth, class_2350 dir) {
      OUTLINE_SIDE_QUEUE.add(new Render3DEngine.OutlineSideAction(box, color, lineWidth, dir));
   }

   public static void setSideOutlinePoints(class_238 box, class_4587 matrices, class_287 buffer, Color color, class_2350 dir) {
      box = box.method_997((new class_243(box.field_1323, box.field_1322, box.field_1321)).method_22882());
      float x1 = (float)box.field_1323;
      float y1 = (float)box.field_1322;
      float z1 = (float)box.field_1321;
      float x2 = (float)box.field_1320;
      float y2 = (float)box.field_1325;
      float z2 = (float)box.field_1324;
      switch(dir) {
      case field_11036:
         vertexLine(matrices, buffer, x1, y2, z1, x2, y2, z1, color);
         vertexLine(matrices, buffer, x2, y2, z1, x2, y2, z2, color);
         vertexLine(matrices, buffer, x2, y2, z2, x1, y2, z2, color);
         vertexLine(matrices, buffer, x1, y2, z2, x1, y2, z1, color);
         break;
      case field_11033:
         vertexLine(matrices, buffer, x1, y1, z1, x2, y1, z1, color);
         vertexLine(matrices, buffer, x2, y1, z1, x2, y1, z2, color);
         vertexLine(matrices, buffer, x2, y1, z2, x1, y1, z2, color);
         vertexLine(matrices, buffer, x1, y1, z2, x1, y1, z1, color);
         break;
      case field_11034:
         vertexLine(matrices, buffer, x2, y1, z1, x2, y2, z1, color);
         vertexLine(matrices, buffer, x2, y1, z2, x2, y2, z2, color);
         vertexLine(matrices, buffer, x2, y2, z2, x2, y2, z1, color);
         vertexLine(matrices, buffer, x2, y1, z2, x2, y1, z1, color);
         break;
      case field_11039:
         vertexLine(matrices, buffer, x1, y1, z1, x1, y2, z1, color);
         vertexLine(matrices, buffer, x1, y1, z2, x1, y2, z2, color);
         vertexLine(matrices, buffer, x1, y2, z2, x1, y2, z1, color);
         vertexLine(matrices, buffer, x1, y1, z2, x1, y1, z1, color);
         break;
      case field_11043:
         vertexLine(matrices, buffer, x2, y1, z1, x2, y2, z1, color);
         vertexLine(matrices, buffer, x1, y1, z1, x1, y2, z1, color);
         vertexLine(matrices, buffer, x2, y1, z1, x1, y1, z1, color);
         vertexLine(matrices, buffer, x2, y2, z1, x1, y2, z1, color);
         break;
      case field_11035:
         vertexLine(matrices, buffer, x1, y1, z2, x1, y2, z2, color);
         vertexLine(matrices, buffer, x2, y1, z2, x2, y2, z2, color);
         vertexLine(matrices, buffer, x1, y1, z2, x2, y1, z2, color);
         vertexLine(matrices, buffer, x1, y2, z2, x2, y2, z2, color);
      }

   }

   public static void drawHoleOutline(@NotNull class_238 box, Color color, float lineWidth) {
      setupRender();
      class_4587 matrices = matrixFrom(box.field_1323, box.field_1322, box.field_1321);
      class_289 tessellator = class_289.method_1348();
      class_287 buffer = tessellator.method_60827(class_5596.field_27377, class_290.field_29337);
      RenderSystem.disableCull();
      RenderSystem.setShader(class_757::method_34535);
      RenderSystem.lineWidth(lineWidth);
      box = box.method_997((new class_243(box.field_1323, box.field_1322, box.field_1321)).method_22882());
      float x1 = (float)box.field_1323;
      float y1 = (float)box.field_1322;
      float y2 = (float)box.field_1325;
      float z1 = (float)box.field_1321;
      float x2 = (float)box.field_1320;
      float z2 = (float)box.field_1324;
      vertexLine(matrices, buffer, x1, y1, z1, x2, y1, z1, color);
      vertexLine(matrices, buffer, x2, y1, z1, x2, y1, z2, color);
      vertexLine(matrices, buffer, x2, y1, z2, x1, y1, z2, color);
      vertexLine(matrices, buffer, x1, y1, z2, x1, y1, z1, color);
      vertexLine(matrices, buffer, x1, y1, z1, x1, y2, z1, color);
      vertexLine(matrices, buffer, x2, y1, z2, x2, y2, z2, color);
      vertexLine(matrices, buffer, x1, y1, z2, x1, y2, z2, color);
      vertexLine(matrices, buffer, x2, y1, z1, x2, y2, z1, color);
      Render2DEngine.endBuilding(buffer);
      RenderSystem.enableCull();
      endRender();
   }

   public static void vertexLine(@NotNull class_4587 matrices, @NotNull class_4588 buffer, float x1, float y1, float z1, float x2, float y2, float z2, @NotNull Color lineColor) {
      Matrix4f model = matrices.method_23760().method_23761();
      class_4665 entry = matrices.method_23760();
      Vector3f normalVec = getNormal(x1, y1, z1, x2, y2, z2);
      buffer.method_22918(model, x1, y1, z1).method_1336(lineColor.getRed(), lineColor.getGreen(), lineColor.getBlue(), lineColor.getAlpha()).method_60831(entry, normalVec.x(), normalVec.y(), normalVec.z());
      buffer.method_22918(model, x2, y2, z2).method_1336(lineColor.getRed(), lineColor.getGreen(), lineColor.getBlue(), lineColor.getAlpha()).method_60831(entry, normalVec.x(), normalVec.y(), normalVec.z());
   }

   @NotNull
   public static Vector3f getNormal(float x1, float y1, float z1, float x2, float y2, float z2) {
      float xNormal = x2 - x1;
      float yNormal = y2 - y1;
      float zNormal = z2 - z1;
      float normalSqrt = class_3532.method_15355(xNormal * xNormal + yNormal * yNormal + zNormal * zNormal);
      return new Vector3f(xNormal / normalSqrt, yNormal / normalSqrt, zNormal / normalSqrt);
   }

   @NotNull
   public static class_4587 matrixFrom(double x, double y, double z) {
      class_4587 matrices = new class_4587();
      class_4184 camera = class_310.method_1551().field_1773.method_19418();
      matrices.method_22907(class_7833.field_40714.rotationDegrees(camera.method_19329()));
      matrices.method_22907(class_7833.field_40716.rotationDegrees(camera.method_19330() + 180.0F));
      matrices.method_22904(x - camera.method_19326().field_1352, y - camera.method_19326().field_1351, z - camera.method_19326().field_1350);
      return matrices;
   }

   public static void setupRender() {
      RenderSystem.enableBlend();
      RenderSystem.defaultBlendFunc();
   }

   public static void endRender() {
      RenderSystem.disableBlend();
   }

   public static void drawTargetEsp(class_4587 stack, @NotNull class_1297 target) {
      ArrayList<class_243> vecs = new ArrayList();
      ArrayList<class_243> vecs1 = new ArrayList();
      ArrayList<class_243> vecs2 = new ArrayList();
      double x = target.field_6014 + (target.method_23317() - target.field_6014) * (double)getTickDelta() - Module.mc.method_1561().field_4686.method_19326().method_10216();
      double y = target.field_6036 + (target.method_23318() - target.field_6036) * (double)getTickDelta() - Module.mc.method_1561().field_4686.method_19326().method_10214();
      double z = target.field_5969 + (target.method_23321() - target.field_5969) * (double)getTickDelta() - Module.mc.method_1561().field_4686.method_19326().method_10215();
      double height = (double)target.method_17682();

      for(int i = 0; i <= 361; ++i) {
         double v = Math.sin(Math.toRadians((double)i));
         double u = Math.cos(Math.toRadians((double)i));
         class_243 vec = new class_243((double)((float)(u * 0.5D)), height, (double)((float)(v * 0.5D)));
         vecs.add(vec);
         double v1 = Math.sin(Math.toRadians((double)((i + 120) % 360)));
         double u1 = Math.cos(Math.toRadians((double)(i + 120)) % 360.0D);
         class_243 vec1 = new class_243((double)((float)(u1 * 0.5D)), height, (double)((float)(v1 * 0.5D)));
         vecs1.add(vec1);
         double v2 = Math.sin(Math.toRadians((double)((i + 240) % 360)));
         double u2 = Math.cos(Math.toRadians((double)((i + 240) % 360)));
         class_243 vec2 = new class_243((double)((float)(u2 * 0.5D)), height, (double)((float)(v2 * 0.5D)));
         vecs2.add(vec2);
         height -= 0.004000000189989805D;
      }

      stack.method_22903();
      stack.method_22904(x, y, z);
      setupRender();
      RenderSystem.disableCull();
      RenderSystem.disableDepthTest();
      RenderSystem.setShader(class_757::method_34540);
      class_287 bufferBuilder = class_289.method_1348().method_60827(class_5596.field_27380, class_290.field_1576);
      Matrix4f matrix = stack.method_23760().method_23761();

      int j;
      float alpha;
      for(j = 0; j < vecs.size() - 1; ++j) {
         alpha = 1.0F - ((float)j + (float)(System.currentTimeMillis() - TsunamiClient.initTime) / 5.0F) % 360.0F / 60.0F;
         bufferBuilder.method_22918(matrix, (float)((class_243)vecs.get(j)).field_1352, (float)((class_243)vecs.get(j)).field_1351, (float)((class_243)vecs.get(j)).field_1350).method_39415(Render2DEngine.injectAlpha(HudEditor.getColor((int)((float)j / 20.0F)), (int)(alpha * 255.0F)).getRGB());
         bufferBuilder.method_22918(matrix, (float)((class_243)vecs.get(j + 1)).field_1352, (float)((class_243)vecs.get(j + 1)).field_1351 + 0.1F, (float)((class_243)vecs.get(j + 1)).field_1350).method_39415(Render2DEngine.injectAlpha(HudEditor.getColor((int)((float)j / 20.0F)), (int)(alpha * 255.0F)).getRGB());
      }

      Render2DEngine.endBuilding(bufferBuilder);
      RenderSystem.setShader(class_757::method_34540);
      bufferBuilder = class_289.method_1348().method_60827(class_5596.field_27380, class_290.field_1576);

      for(j = 0; j < vecs1.size() - 1; ++j) {
         alpha = 1.0F - ((float)j + (float)(System.currentTimeMillis() - TsunamiClient.initTime) / 5.0F) % 360.0F / 60.0F;
         bufferBuilder.method_22918(matrix, (float)((class_243)vecs1.get(j)).field_1352, (float)((class_243)vecs1.get(j)).field_1351, (float)((class_243)vecs1.get(j)).field_1350).method_39415(Render2DEngine.injectAlpha(HudEditor.getColor((int)((float)j / 20.0F)), (int)(alpha * 255.0F)).getRGB());
         bufferBuilder.method_22918(matrix, (float)((class_243)vecs1.get(j + 1)).field_1352, (float)((class_243)vecs1.get(j + 1)).field_1351 + 0.1F, (float)((class_243)vecs1.get(j + 1)).field_1350).method_39415(Render2DEngine.injectAlpha(HudEditor.getColor((int)((float)j / 20.0F)), (int)(alpha * 255.0F)).getRGB());
      }

      Render2DEngine.endBuilding(bufferBuilder);
      RenderSystem.setShader(class_757::method_34540);
      bufferBuilder = class_289.method_1348().method_60827(class_5596.field_27380, class_290.field_1576);

      for(j = 0; j < vecs2.size() - 1; ++j) {
         alpha = 1.0F - ((float)j + (float)(System.currentTimeMillis() - TsunamiClient.initTime) / 5.0F) % 360.0F / 60.0F;
         bufferBuilder.method_22918(matrix, (float)((class_243)vecs2.get(j)).field_1352, (float)((class_243)vecs2.get(j)).field_1351, (float)((class_243)vecs2.get(j)).field_1350).method_39415(Render2DEngine.injectAlpha(HudEditor.getColor((int)((float)j / 20.0F)), (int)(alpha * 255.0F)).getRGB());
         bufferBuilder.method_22918(matrix, (float)((class_243)vecs2.get(j + 1)).field_1352, (float)((class_243)vecs2.get(j + 1)).field_1351 + 0.1F, (float)((class_243)vecs2.get(j + 1)).field_1350).method_39415(Render2DEngine.injectAlpha(HudEditor.getColor((int)((float)j / 20.0F)), (int)(alpha * 255.0F)).getRGB());
      }

      Render2DEngine.endBuilding(bufferBuilder);
      RenderSystem.enableCull();
      stack.method_22904(-x, -y, -z);
      endRender();
      RenderSystem.enableDepthTest();
      stack.method_22909();
   }

   public static void renderCrosses(@NotNull class_238 box, Color color, float lineWidth) {
      setupRender();
      class_4587 matrices = matrixFrom(box.field_1323, box.field_1322, box.field_1321);
      RenderSystem.disableCull();
      RenderSystem.setShader(class_757::method_34535);
      RenderSystem.lineWidth(lineWidth);
      class_287 buffer = class_289.method_1348().method_60827(class_5596.field_27377, class_290.field_29337);
      box = box.method_997((new class_243(box.field_1323, box.field_1322, box.field_1321)).method_22882());
      vertexLine(matrices, buffer, (float)box.field_1320, (float)box.field_1322, (float)box.field_1321, (float)box.field_1323, (float)box.field_1322, (float)box.field_1324, color);
      vertexLine(matrices, buffer, (float)box.field_1323, (float)box.field_1322, (float)box.field_1321, (float)box.field_1320, (float)box.field_1322, (float)box.field_1324, color);
      Render2DEngine.endBuilding(buffer);
      RenderSystem.enableCull();
      endRender();
   }

   public static void drawSphere(class_4587 matrix, float radius, int slices, int stacks, int color) {
      float drho = 3.1415927F / (float)stacks;
      float dtheta = 6.2831855F / ((float)slices - 1.0F);
      setupRender();

      float rho;
      float theta;
      float x;
      float y;
      float z;
      int i;
      int j;
      class_287 buffer;
      for(i = 1; i < stacks; ++i) {
         rho = (float)i * drho;
         buffer = class_289.method_1348().method_60827(class_5596.field_29345, class_290.field_1576);
         RenderSystem.setShader(class_757::method_34540);

         for(j = 0; j < slices; ++j) {
            theta = (float)j * dtheta;
            x = (float)(Math.cos((double)theta) * Math.sin((double)rho));
            y = (float)(Math.sin((double)theta) * Math.sin((double)rho));
            z = (float)Math.cos((double)rho);
            buffer.method_22918(matrix.method_23760().method_23761(), x * radius, y * radius, z * radius).method_39415(color);
         }

         Render2DEngine.endBuilding(buffer);
      }

      for(j = 0; j < slices; ++j) {
         theta = (float)j * dtheta;
         buffer = class_289.method_1348().method_60827(class_5596.field_29345, class_290.field_1576);
         RenderSystem.setShader(class_757::method_34540);

         for(i = 0; i <= stacks; ++i) {
            rho = (float)i * drho;
            x = (float)(Math.cos((double)theta) * Math.sin((double)rho));
            y = (float)(Math.sin((double)theta) * Math.sin((double)rho));
            z = (float)Math.cos((double)rho);
            buffer.method_22918(matrix.method_23760().method_23761(), x * radius, y * radius, z * radius).method_39415(color);
         }

         class_286.method_43433(buffer.method_60800());
      }

      endRender();
   }

   public static void drawCylinder(class_4587 stack, float radius, float height, int slices, int stacks, int color) {
      float da = (float)(6.283185307179586D / (double)slices);
      float dz = height / (float)stacks;
      class_287 buffer = class_289.method_1348().method_60827(class_5596.field_29345, class_290.field_1576);
      RenderSystem.setShader(class_757::method_34540);
      float y = 0.0F;

      int i;
      float z;
      for(i = 0; i <= stacks; ++i) {
         for(int i = 0; i <= slices; ++i) {
            z = (float)Math.cos((double)((float)i * da));
            float z = (float)Math.sin((double)((float)i * da));
            buffer.method_22918(stack.method_23760().method_23761(), z * radius, y, z * radius).method_39415(color);
         }

         y += dz;
      }

      class_286.method_43433(buffer.method_60800());
      buffer = class_289.method_1348().method_60827(class_5596.field_29345, class_290.field_1576);
      RenderSystem.setShader(class_757::method_34540);

      for(i = 0; i <= slices; ++i) {
         float x = (float)Math.cos((double)((float)i * da));
         z = (float)Math.sin((double)((float)i * da));
         buffer.method_22918(stack.method_23760().method_23761(), x * radius, 0.0F, z * radius).method_39415(color);
         buffer.method_22918(stack.method_23760().method_23761(), x * radius, height, z * radius).method_39415(color);
      }

      class_286.method_43433(buffer.method_60800());
   }

   public static void drawCircle3D(class_4587 stack, class_1297 ent, float radius, int color, int points, boolean hudColor, int colorOffset) {
      setupRender();
      RenderSystem.setShader(class_757::method_34540);
      class_287 bufferBuilder = class_289.method_1348().method_60827(class_5596.field_29345, class_290.field_1576);
      double x = ent.field_6014 + (ent.method_23317() - ent.field_6014) * (double)getTickDelta() - Module.mc.method_1561().field_4686.method_19326().method_10216();
      double y = ent.field_6036 + (ent.method_23318() - ent.field_6036) * (double)getTickDelta() - Module.mc.method_1561().field_4686.method_19326().method_10214();
      double z = ent.field_5969 + (ent.method_23321() - ent.field_5969) * (double)getTickDelta() - Module.mc.method_1561().field_4686.method_19326().method_10215();
      stack.method_22903();
      stack.method_22904(x, y, z);
      Matrix4f matrix = stack.method_23760().method_23761();

      for(int i = 0; i <= points; ++i) {
         if (hudColor) {
            color = HudEditor.getColor(i * colorOffset).getRGB();
         }

         bufferBuilder.method_22918(matrix, (float)((double)radius * Math.cos((double)i * 6.28D / (double)points)), 0.0F, (float)((double)radius * Math.sin((double)i * 6.28D / (double)points))).method_39415(color);
      }

      class_286.method_43433(bufferBuilder.method_60800());
      endRender();
      stack.method_22904(-x, -y, -z);
      stack.method_22909();
   }

   public static void drawOldTargetEsp(class_4587 stack, class_1297 target) {
      double cs = (double)(prevCircleStep + (circleStep - prevCircleStep) * getTickDelta());
      double prevSinAnim = absSinAnimation(cs - 0.44999998807907104D);
      double sinAnim = absSinAnimation(cs);
      double x = target.field_6014 + (target.method_23317() - target.field_6014) * (double)getTickDelta() - Module.mc.method_1561().field_4686.method_19326().method_10216();
      double y = target.field_6036 + (target.method_23318() - target.field_6036) * (double)getTickDelta() - Module.mc.method_1561().field_4686.method_19326().method_10214() + prevSinAnim * (double)target.method_17682();
      double z = target.field_5969 + (target.method_23321() - target.field_5969) * (double)getTickDelta() - Module.mc.method_1561().field_4686.method_19326().method_10215();
      double nextY = target.field_6036 + (target.method_23318() - target.field_6036) * (double)getTickDelta() - Module.mc.method_1561().field_4686.method_19326().method_10214() + sinAnim * (double)target.method_17682();
      stack.method_22903();
      setupRender();
      RenderSystem.disableCull();
      RenderSystem.disableDepthTest();
      class_287 bufferBuilder = class_289.method_1348().method_60827(class_5596.field_27380, class_290.field_1576);
      RenderSystem.setShader(class_757::method_34540);

      for(int i = 0; i <= 30; ++i) {
         float cos = (float)(x + Math.cos((double)i * 6.28D / 30.0D) * (double)target.method_17681() * 0.8D);
         float sin = (float)(z + Math.sin((double)i * 6.28D / 30.0D) * (double)target.method_17681() * 0.8D);
         bufferBuilder.method_22918(stack.method_23760().method_23761(), cos, (float)nextY, sin).method_39415(Render2DEngine.injectAlpha(HudEditor.getColor(i), 170).getRGB());
         bufferBuilder.method_22918(stack.method_23760().method_23761(), cos, (float)y, sin).method_39415(Render2DEngine.injectAlpha(HudEditor.getColor(i), 0).getRGB());
      }

      Render2DEngine.endBuilding(bufferBuilder);
      RenderSystem.enableCull();
      endRender();
      RenderSystem.enableDepthTest();
      stack.method_22909();
   }

   public static void renderGhosts(int espLength, int factor, float shaking, float amplitude, class_1297 target) {
      class_4184 camera = Module.mc.field_1773.method_19418();
      double tPosX = Render2DEngine.interpolate(target.field_6014, target.method_23317(), (double)getTickDelta()) - camera.method_19326().field_1352;
      double tPosY = Render2DEngine.interpolate(target.field_6036, target.method_23318(), (double)getTickDelta()) - camera.method_19326().field_1351;
      double tPosZ = Render2DEngine.interpolate(target.field_5969, target.method_23321(), (double)getTickDelta()) - camera.method_19326().field_1350;
      float iAge = (float)Render2DEngine.interpolate((double)(target.field_6012 - 1), (double)target.field_6012, (double)getTickDelta());
      RenderSystem.enableBlend();
      RenderSystem.blendFunc(class_4535.SRC_ALPHA, class_4534.ONE);
      RenderSystem.setShaderTexture(0, TextureStorage.firefly);
      RenderSystem.setShader(class_757::method_34543);
      class_287 buffer = class_289.method_1348().method_60827(class_5596.field_27382, class_290.field_1575);
      boolean canSee = Module.mc.field_1724.method_6057(target);
      if (canSee) {
         RenderSystem.enableDepthTest();
         RenderSystem.depthMask(false);
      } else {
         RenderSystem.disableDepthTest();
      }

      for(int j = 0; j < 3; ++j) {
         for(int i = 0; i <= espLength; ++i) {
            double radians = Math.toRadians((double)((((float)i / 1.5F + iAge) * (float)factor + (float)(j * 120)) % (float)(factor * 360)));
            double sinQuad = Math.sin(Math.toRadians((double)(iAge * 2.5F + (float)(i * (j + 1)))) * (double)amplitude) / (double)shaking;
            float offset = (float)i / (float)espLength;
            class_4587 matrices = new class_4587();
            matrices.method_22907(class_7833.field_40714.rotationDegrees(camera.method_19329()));
            matrices.method_22907(class_7833.field_40716.rotationDegrees(camera.method_19330() + 180.0F));
            matrices.method_22904(tPosX + Math.cos(radians) * (double)target.method_17681(), tPosY + 1.0D + sinQuad, tPosZ + Math.sin(radians) * (double)target.method_17681());
            matrices.method_22907(class_7833.field_40716.rotationDegrees(-camera.method_19330()));
            matrices.method_22907(class_7833.field_40714.rotationDegrees(camera.method_19329()));
            Matrix4f matrix = matrices.method_23760().method_23761();
            int color = Render2DEngine.applyOpacity(HudEditor.getColor((int)(180.0F * offset)), offset).getRGB();
            float scale = Math.max(0.24F * offset, 0.2F);
            buffer.method_22918(matrix, -scale, scale, 0.0F).method_22913(0.0F, 1.0F).method_39415(color);
            buffer.method_22918(matrix, scale, scale, 0.0F).method_22913(1.0F, 1.0F).method_39415(color);
            buffer.method_22918(matrix, scale, -scale, 0.0F).method_22913(1.0F, 0.0F).method_39415(color);
            buffer.method_22918(matrix, -scale, -scale, 0.0F).method_22913(0.0F, 0.0F).method_39415(color);
         }
      }

      class_286.method_43433(buffer.method_60800());
      if (canSee) {
         RenderSystem.depthMask(true);
         RenderSystem.disableDepthTest();
      } else {
         RenderSystem.enableDepthTest();
      }

      RenderSystem.disableBlend();
   }

   public static void updateTargetESP() {
      prevCircleStep = circleStep;
      circleStep += 0.15F;
   }

   public static double absSinAnimation(double input) {
      return Math.abs(1.0D + Math.sin(input)) / 2.0D;
   }

   public static class_243 interpolatePos(float prevposX, float prevposY, float prevposZ, float posX, float posY, float posZ) {
      double x = (double)(prevposX + (posX - prevposX) * getTickDelta()) - Module.mc.method_1561().field_4686.method_19326().method_10216();
      double y = (double)(prevposY + (posY - prevposY) * getTickDelta()) - Module.mc.method_1561().field_4686.method_19326().method_10214();
      double z = (double)(prevposZ + (posZ - prevposZ) * getTickDelta()) - Module.mc.method_1561().field_4686.method_19326().method_10215();
      return new class_243(x, y, z);
   }

   public static void drawLineDebug(class_243 start, class_243 end, Color color) {
      DEBUG_LINE_QUEUE.add(new Render3DEngine.DebugLineAction(start, end, color));
   }

   public static float getTickDelta() {
      return Module.mc.method_60646().method_60637(true);
   }

   public static record FillAction(class_238 box, Color color) {
      public FillAction(class_238 box, Color color) {
         this.box = box;
         this.color = color;
      }

      public class_238 box() {
         return this.box;
      }

      public Color color() {
         return this.color;
      }
   }

   public static record FillSideAction(class_238 box, Color color, class_2350 side) {
      public FillSideAction(class_238 box, Color color, class_2350 side) {
         this.box = box;
         this.color = color;
         this.side = side;
      }

      public class_238 box() {
         return this.box;
      }

      public Color color() {
         return this.color;
      }

      public class_2350 side() {
         return this.side;
      }
   }

   public static record FadeAction(class_238 box, Color color, Color color2) {
      public FadeAction(class_238 box, Color color, Color color2) {
         this.box = box;
         this.color = color;
         this.color2 = color2;
      }

      public class_238 box() {
         return this.box;
      }

      public Color color() {
         return this.color;
      }

      public Color color2() {
         return this.color2;
      }
   }

   public static record LineAction(class_243 start, class_243 end, Color color) {
      public LineAction(class_243 start, class_243 end, Color color) {
         this.start = start;
         this.end = end;
         this.color = color;
      }

      public class_243 start() {
         return this.start;
      }

      public class_243 end() {
         return this.end;
      }

      public Color color() {
         return this.color;
      }
   }

   public static record OutlineAction(class_238 box, Color color, float lineWidth) {
      public OutlineAction(class_238 box, Color color, float lineWidth) {
         this.box = box;
         this.color = color;
         this.lineWidth = lineWidth;
      }

      public class_238 box() {
         return this.box;
      }

      public Color color() {
         return this.color;
      }

      public float lineWidth() {
         return this.lineWidth;
      }
   }

   public static record OutlineSideAction(class_238 box, Color color, float lineWidth, class_2350 side) {
      public OutlineSideAction(class_238 box, Color color, float lineWidth, class_2350 side) {
         this.box = box;
         this.color = color;
         this.lineWidth = lineWidth;
         this.side = side;
      }

      public class_238 box() {
         return this.box;
      }

      public Color color() {
         return this.color;
      }

      public float lineWidth() {
         return this.lineWidth;
      }

      public class_2350 side() {
         return this.side;
      }
   }

   public static record DebugLineAction(class_243 start, class_243 end, Color color) {
      public DebugLineAction(class_243 start, class_243 end, Color color) {
         this.start = start;
         this.end = end;
         this.color = color;
      }

      public class_243 start() {
         return this.start;
      }

      public class_243 end() {
         return this.end;
      }

      public Color color() {
         return this.color;
      }
   }
}
