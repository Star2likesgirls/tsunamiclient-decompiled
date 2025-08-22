package tsunami.utility.render;

import com.mojang.blaze3d.platform.GlStateManager.class_4534;
import com.mojang.blaze3d.platform.GlStateManager.class_4535;
import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Stack;
import javax.imageio.ImageIO;
import net.minecraft.class_1011;
import net.minecraft.class_1043;
import net.minecraft.class_286;
import net.minecraft.class_287;
import net.minecraft.class_289;
import net.minecraft.class_290;
import net.minecraft.class_3532;
import net.minecraft.class_4587;
import net.minecraft.class_757;
import net.minecraft.class_7833;
import net.minecraft.class_9801;
import net.minecraft.class_293.class_5596;
import org.apache.commons.lang3.RandomStringUtils;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import tsunami.features.modules.Module;
import tsunami.features.modules.client.HudEditor;
import tsunami.gui.font.Texture;
import tsunami.setting.impl.ColorSetting;
import tsunami.utility.math.MathUtility;
import tsunami.utility.render.shaders.ArcShader;
import tsunami.utility.render.shaders.BlurProgram;
import tsunami.utility.render.shaders.HudShader;
import tsunami.utility.render.shaders.MainMenuProgram;
import tsunami.utility.render.shaders.RectangleShader;
import tsunami.utility.render.shaders.TextureColorProgram;

public class Render2DEngine {
   public static TextureColorProgram TEXTURE_COLOR_PROGRAM;
   public static HudShader HUD_SHADER;
   public static RectangleShader RECTANGLE_SHADER;
   public static MainMenuProgram MAIN_MENU_PROGRAM;
   public static ArcShader ARC_PROGRAM;
   public static BlurProgram BLUR_PROGRAM;
   public static HashMap<Integer, Render2DEngine.BlurredShadow> shadowCache = new HashMap();
   public static HashMap<Integer, Render2DEngine.BlurredShadow> shadowCache1 = new HashMap();
   static final Stack<Render2DEngine.Rectangle> clipStack = new Stack();

   public static void addWindow(class_4587 stack, Render2DEngine.Rectangle r1) {
      Matrix4f matrix = stack.method_23760().method_23761();
      Vector4f coord = new Vector4f(r1.x, r1.y, 0.0F, 1.0F);
      Vector4f end = new Vector4f(r1.x1, r1.y1, 0.0F, 1.0F);
      coord.mulTranspose(matrix);
      end.mulTranspose(matrix);
      float x = coord.x();
      float y = coord.y();
      float endX = end.x();
      float endY = end.y();
      Render2DEngine.Rectangle r = new Render2DEngine.Rectangle(x, y, endX, endY);
      if (clipStack.empty()) {
         clipStack.push(r);
         beginScissor((double)r.x, (double)r.y, (double)r.x1, (double)r.y1);
      } else {
         Render2DEngine.Rectangle lastClip = (Render2DEngine.Rectangle)clipStack.peek();
         float lsx = lastClip.x;
         float lsy = lastClip.y;
         float lstx = lastClip.x1;
         float lsty = lastClip.y1;
         float nsx = class_3532.method_15363(r.x, lsx, lstx);
         float nsy = class_3532.method_15363(r.y, lsy, lsty);
         float nstx = class_3532.method_15363(r.x1, nsx, lstx);
         float nsty = class_3532.method_15363(r.y1, nsy, lsty);
         clipStack.push(new Render2DEngine.Rectangle(nsx, nsy, nstx, nsty));
         beginScissor((double)nsx, (double)nsy, (double)nstx, (double)nsty);
      }

   }

   public static void popWindow() {
      clipStack.pop();
      if (clipStack.empty()) {
         endScissor();
      } else {
         Render2DEngine.Rectangle r = (Render2DEngine.Rectangle)clipStack.peek();
         beginScissor((double)r.x, (double)r.y, (double)r.x1, (double)r.y1);
      }

   }

   public static void beginScissor(double x, double y, double endX, double endY) {
      double width = endX - x;
      double height = endY - y;
      width = Math.max(0.0D, width);
      height = Math.max(0.0D, height);
      float d = (float)Render3DEngine.getScaleFactor();
      int ay = (int)(((double)Module.mc.method_22683().method_4502() - (y + height)) * (double)d);
      RenderSystem.enableScissor((int)(x * (double)d), ay, (int)(width * (double)d), (int)(height * (double)d));
   }

   public static void endScissor() {
      RenderSystem.disableScissor();
   }

   public static void addWindow(class_4587 stack, float x, float y, float x1, float y1, double animation_factor) {
      float h = y + y1;
      float h2 = (float)((double)h * (1.0D - MathUtility.clamp(animation_factor, 0.0D, 1.002500057220459D)));
      float y3 = y + h2;
      float x4 = x1;
      float y4 = y1 - h2;
      if (x1 < x) {
         x4 = x;
      }

      if (y4 < y3) {
         y4 = y3;
      }

      addWindow(stack, new Render2DEngine.Rectangle(x, y3, x4, y4));
   }

   public static void horizontalGradient(class_4587 matrices, float x1, float y1, float x2, float y2, Color startColor, Color endColor) {
      Matrix4f matrix = matrices.method_23760().method_23761();
      setupRender();
      RenderSystem.setShader(class_757::method_34540);
      class_287 buffer = class_289.method_1348().method_60827(class_5596.field_27382, class_290.field_1576);
      buffer.method_22918(matrix, x1, y1, 0.0F).method_39415(startColor.getRGB());
      buffer.method_22918(matrix, x1, y2, 0.0F).method_39415(startColor.getRGB());
      buffer.method_22918(matrix, x2, y2, 0.0F).method_39415(endColor.getRGB());
      buffer.method_22918(matrix, x2, y1, 0.0F).method_39415(endColor.getRGB());
      class_286.method_43433(buffer.method_60800());
      endRender();
   }

   public static void verticalGradient(class_4587 matrices, float left, float top, float right, float bottom, Color startColor, Color endColor) {
      Matrix4f matrix = matrices.method_23760().method_23761();
      setupRender();
      RenderSystem.setShader(class_757::method_34540);
      class_287 buffer = class_289.method_1348().method_60827(class_5596.field_27382, class_290.field_1576);
      buffer.method_22918(matrix, left, top, 0.0F).method_39415(startColor.getRGB());
      buffer.method_22918(matrix, left, bottom, 0.0F).method_39415(endColor.getRGB());
      buffer.method_22918(matrix, right, bottom, 0.0F).method_39415(endColor.getRGB());
      buffer.method_22918(matrix, right, top, 0.0F).method_39415(startColor.getRGB());
      class_286.method_43433(buffer.method_60800());
      endRender();
   }

   public static void drawRect(class_4587 matrices, float x, float y, float width, float height, Color c) {
      Matrix4f matrix = matrices.method_23760().method_23761();
      setupRender();
      RenderSystem.setShader(class_757::method_34540);
      class_287 buffer = class_289.method_1348().method_60827(class_5596.field_27382, class_290.field_1576);
      buffer.method_22918(matrix, x, y + height, 0.0F).method_39415(c.getRGB());
      buffer.method_22918(matrix, x + width, y + height, 0.0F).method_39415(c.getRGB());
      buffer.method_22918(matrix, x + width, y, 0.0F).method_39415(c.getRGB());
      buffer.method_22918(matrix, x, y, 0.0F).method_39415(c.getRGB());
      class_286.method_43433(buffer.method_60800());
      endRender();
   }

   public static void drawRectWithOutline(class_4587 matrices, float x, float y, float width, float height, Color c, Color c2) {
      Matrix4f matrix = matrices.method_23760().method_23761();
      setupRender();
      RenderSystem.setShader(class_757::method_34540);
      class_287 buffer = class_289.method_1348().method_60827(class_5596.field_27382, class_290.field_1576);
      buffer.method_22918(matrix, x, y + height, 0.0F).method_39415(c.getRGB());
      buffer.method_22918(matrix, x + width, y + height, 0.0F).method_39415(c.getRGB());
      buffer.method_22918(matrix, x + width, y, 0.0F).method_39415(c.getRGB());
      buffer.method_22918(matrix, x, y, 0.0F).method_39415(c.getRGB());
      class_286.method_43433(buffer.method_60800());
      buffer = class_289.method_1348().method_60827(class_5596.field_29345, class_290.field_1576);
      buffer.method_22918(matrix, x, y + height, 0.0F).method_39415(c2.getRGB());
      buffer.method_22918(matrix, x + width, y + height, 0.0F).method_39415(c2.getRGB());
      buffer.method_22918(matrix, x + width, y, 0.0F).method_39415(c2.getRGB());
      buffer.method_22918(matrix, x, y, 0.0F).method_39415(c2.getRGB());
      buffer.method_22918(matrix, x, y + height, 0.0F).method_39415(c2.getRGB());
      class_286.method_43433(buffer.method_60800());
      endRender();
   }

   public static void drawRectDumbWay(class_4587 matrices, float x, float y, float x1, float y1, Color c1) {
      Matrix4f matrix = matrices.method_23760().method_23761();
      setupRender();
      RenderSystem.setShader(class_757::method_34540);
      class_287 buffer = class_289.method_1348().method_60827(class_5596.field_27382, class_290.field_1576);
      buffer.method_22918(matrix, x, y1, 0.0F).method_39415(c1.getRGB());
      buffer.method_22918(matrix, x1, y1, 0.0F).method_39415(c1.getRGB());
      buffer.method_22918(matrix, x1, y, 0.0F).method_39415(c1.getRGB());
      buffer.method_22918(matrix, x, y, 0.0F).method_39415(c1.getRGB());
      class_286.method_43433(buffer.method_60800());
      endRender();
   }

   public static void setRectPoints(class_287 bufferBuilder, Matrix4f matrix, float x, float y, float x1, float y1, Color c1, Color c2, Color c3, Color c4) {
      bufferBuilder.method_22918(matrix, x, y1, 0.0F).method_39415(c1.getRGB());
      bufferBuilder.method_22918(matrix, x1, y1, 0.0F).method_39415(c2.getRGB());
      bufferBuilder.method_22918(matrix, x1, y, 0.0F).method_39415(c3.getRGB());
      bufferBuilder.method_22918(matrix, x, y, 0.0F).method_39415(c4.getRGB());
   }

   public static boolean isHovered(double mouseX, double mouseY, double x, double y, double width, double height) {
      return mouseX >= x && mouseX - width <= x && mouseY >= y && mouseY - height <= y;
   }

   public static void drawBlurredShadow(class_4587 matrices, float x, float y, float width, float height, int blurRadius, Color color) {
      if ((Boolean)HudEditor.glow.getValue()) {
         width += (float)(blurRadius * 2);
         height += (float)(blurRadius * 2);
         x -= (float)blurRadius;
         y -= (float)blurRadius;
         int identifier = (int)(width * height + width * (float)blurRadius);
         if (shadowCache.containsKey(identifier)) {
            ((Render2DEngine.BlurredShadow)shadowCache.get(identifier)).bind();
            setupRender();
            RenderSystem.setShaderColor((float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F, (float)color.getAlpha() / 255.0F);
            renderTexture(matrices, (double)x, (double)y, (double)width, (double)height, 0.0F, 0.0F, (double)width, (double)height, (double)width, (double)height);
            endRender();
         } else {
            BufferedImage original = new BufferedImage((int)width, (int)height, 2);
            Graphics g = original.getGraphics();
            g.setColor(new Color(-1));
            g.fillRect(blurRadius, blurRadius, (int)(width - (float)(blurRadius * 2)), (int)(height - (float)(blurRadius * 2)));
            g.dispose();
            GaussianFilter op = new GaussianFilter((float)blurRadius);
            BufferedImage blurred = op.filter(original, (BufferedImage)null);
            shadowCache.put(identifier, new Render2DEngine.BlurredShadow(blurred));
         }
      }
   }

   public static void drawGradientBlurredShadow(class_4587 matrices, float x, float y, float width, float height, int blurRadius, Color color1, Color color2, Color color3, Color color4) {
      if ((Boolean)HudEditor.glow.getValue()) {
         width += (float)(blurRadius * 2);
         height += (float)(blurRadius * 2);
         x -= (float)blurRadius;
         y -= (float)blurRadius;
         int identifier = (int)(width * height + width * (float)blurRadius);
         if (shadowCache.containsKey(identifier)) {
            ((Render2DEngine.BlurredShadow)shadowCache.get(identifier)).bind();
            setupRender();
            renderGradientTexture(matrices, (double)x, (double)y, (double)width, (double)height, 0.0F, 0.0F, (double)width, (double)height, (double)width, (double)height, color1, color2, color3, color4);
            endRender();
         } else {
            BufferedImage original = new BufferedImage((int)width, (int)height, 2);
            Graphics g = original.getGraphics();
            g.setColor(new Color(-1));
            g.fillRect(blurRadius, blurRadius, (int)(width - (float)(blurRadius * 2)), (int)(height - (float)(blurRadius * 2)));
            g.dispose();
            GaussianFilter op = new GaussianFilter((float)blurRadius);
            BufferedImage blurred = op.filter(original, (BufferedImage)null);
            shadowCache.put(identifier, new Render2DEngine.BlurredShadow(blurred));
         }
      }
   }

   public static void drawGradientBlurredShadow1(class_4587 matrices, float x, float y, float width, float height, int blurRadius, Color color1, Color color2, Color color3, Color color4) {
      if ((Boolean)HudEditor.glow.getValue()) {
         width += (float)(blurRadius * 2);
         height += (float)(blurRadius * 2);
         x -= (float)blurRadius;
         y -= (float)blurRadius;
         int identifier = (int)(width * height + width * (float)blurRadius);
         if (shadowCache1.containsKey(identifier)) {
            ((Render2DEngine.BlurredShadow)shadowCache1.get(identifier)).bind();
            setupRender();
            RenderSystem.blendFunc(class_4535.SRC_ALPHA, class_4534.ONE);
            renderGradientTexture(matrices, (double)x, (double)y, (double)width, (double)height, 0.0F, 0.0F, (double)width, (double)height, (double)width, (double)height, color1, color2, color3, color4);
            endRender();
         } else {
            BufferedImage original = new BufferedImage((int)width, (int)height, 2);
            Graphics g = original.getGraphics();
            g.setColor(new Color(-1));
            g.fillRect(blurRadius, blurRadius, (int)(width - (float)(blurRadius * 2)), (int)(height - (float)(blurRadius * 2)));
            g.dispose();
            BufferedImage blurred = (new GaussianFilter((float)blurRadius)).filter(original, (BufferedImage)null);
            BufferedImage black = new BufferedImage((int)width + blurRadius * 2, (int)height + blurRadius * 2, 2);
            Graphics g2 = black.getGraphics();
            g2.setColor(new Color(0));
            g2.fillRect(0, 0, (int)width + blurRadius * 2, (int)height + blurRadius * 2);
            g2.dispose();
            BufferedImage combined = new BufferedImage((int)width, (int)height, 2);
            Graphics g1 = combined.getGraphics();
            g1.drawImage(black, -blurRadius, -blurRadius, (ImageObserver)null);
            g1.drawImage(blurred, 0, 0, (ImageObserver)null);
            g1.dispose();
            shadowCache1.put(identifier, new Render2DEngine.BlurredShadow(combined));
         }
      }
   }

   public static void registerBufferedImageTexture(Texture i, BufferedImage bi) {
      try {
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         ImageIO.write(bi, "png", baos);
         byte[] bytes = baos.toByteArray();
         registerTexture(i, bytes);
      } catch (Exception var4) {
      }

   }

   private static void registerTexture(Texture i, byte[] content) {
      try {
         ByteBuffer data = BufferUtils.createByteBuffer(content.length).put(content);
         data.flip();
         class_1043 tex = new class_1043(class_1011.method_4324(data));
         Module.mc.execute(() -> {
            Module.mc.method_1531().method_4616(i.getId(), tex);
         });
      } catch (Exception var4) {
      }

   }

   public static void renderTexture(class_4587 matrices, double x0, double y0, double width, double height, float u, float v, double regionWidth, double regionHeight, double textureWidth, double textureHeight) {
      double x1 = x0 + width;
      double y1 = y0 + height;
      double z = 0.0D;
      Matrix4f matrix = matrices.method_23760().method_23761();
      RenderSystem.setShader(class_757::method_34542);
      class_287 buffer = class_289.method_1348().method_60827(class_5596.field_27382, class_290.field_1585);
      buffer.method_22918(matrix, (float)x0, (float)y1, (float)z).method_22913(u / (float)textureWidth, (v + (float)regionHeight) / (float)textureHeight);
      buffer.method_22918(matrix, (float)x1, (float)y1, (float)z).method_22913((u + (float)regionWidth) / (float)textureWidth, (v + (float)regionHeight) / (float)textureHeight);
      buffer.method_22918(matrix, (float)x1, (float)y0, (float)z).method_22913((u + (float)regionWidth) / (float)textureWidth, v / (float)textureHeight);
      buffer.method_22918(matrix, (float)x0, (float)y0, (float)z).method_22913(u / (float)textureWidth, (v + 0.0F) / (float)textureHeight);
      class_286.method_43433(buffer.method_60800());
   }

   public static void renderGradientTexture(class_4587 matrices, double x0, double y0, double width, double height, float u, float v, double regionWidth, double regionHeight, double textureWidth, double textureHeight, Color c1, Color c2, Color c3, Color c4) {
      RenderSystem.setShader(class_757::method_34543);
      class_287 buffer = class_289.method_1348().method_60827(class_5596.field_27382, class_290.field_1575);
      renderGradientTextureInternal(buffer, matrices, x0, y0, width, height, u, v, regionWidth, regionHeight, textureWidth, textureHeight, c1, c2, c3, c4);
      class_286.method_43433(buffer.method_60800());
   }

   public static void renderGradientTextureInternal(class_287 buff, class_4587 matrices, double x0, double y0, double width, double height, float u, float v, double regionWidth, double regionHeight, double textureWidth, double textureHeight, Color c1, Color c2, Color c3, Color c4) {
      double x1 = x0 + width;
      double y1 = y0 + height;
      double z = 0.0D;
      Matrix4f matrix = matrices.method_23760().method_23761();
      buff.method_22918(matrix, (float)x0, (float)y1, (float)z).method_22913(u / (float)textureWidth, (v + (float)regionHeight) / (float)textureHeight).method_39415(c1.getRGB());
      buff.method_22918(matrix, (float)x1, (float)y1, (float)z).method_22913((u + (float)regionWidth) / (float)textureWidth, (v + (float)regionHeight) / (float)textureHeight).method_39415(c2.getRGB());
      buff.method_22918(matrix, (float)x1, (float)y0, (float)z).method_22913((u + (float)regionWidth) / (float)textureWidth, v / (float)textureHeight).method_39415(c3.getRGB());
      buff.method_22918(matrix, (float)x0, (float)y0, (float)z).method_22913(u / (float)textureWidth, (v + 0.0F) / (float)textureHeight).method_39415(c4.getRGB());
   }

   public static void renderRoundedGradientRect(class_4587 matrices, Color color1, Color color2, Color color3, Color color4, float x, float y, float width, float height, float Radius) {
      Matrix4f matrix = matrices.method_23760().method_23761();
      RenderSystem.colorMask(false, false, false, true);
      RenderSystem.clearColor(0.0F, 0.0F, 0.0F, 0.0F);
      RenderSystem.clear(16384, false);
      RenderSystem.colorMask(true, true, true, true);
      drawRound(matrices, x, y, width, height, Radius, color1);
      setupRender();
      RenderSystem.blendFunc(772, 773);
      class_287 bufferBuilder = class_289.method_1348().method_60827(class_5596.field_27381, class_290.field_1576);
      bufferBuilder.method_22918(matrix, x, y + height, 0.0F).method_39415(color1.getRGB());
      bufferBuilder.method_22918(matrix, x + width, y + height, 0.0F).method_39415(color2.getRGB());
      bufferBuilder.method_22918(matrix, x + width, y, 0.0F).method_39415(color3.getRGB());
      bufferBuilder.method_22918(matrix, x, y, 0.0F).method_39415(color4.getRGB());
      class_286.method_43433(bufferBuilder.method_60800());
      endRender();
   }

   public static void drawRound(class_4587 matrices, float x, float y, float width, float height, float radius, Color color) {
      renderRoundedQuad(matrices, color, (double)x, (double)y, (double)(width + x), (double)(height + y), (double)radius, 4.0D);
   }

   public static void renderRoundedQuad(class_4587 matrices, Color c, double fromX, double fromY, double toX, double toY, double radius, double samples) {
      setupRender();
      RenderSystem.setShader(class_757::method_34540);
      renderRoundedQuadInternal(matrices.method_23760().method_23761(), (float)c.getRed() / 255.0F, (float)c.getGreen() / 255.0F, (float)c.getBlue() / 255.0F, (float)c.getAlpha() / 255.0F, fromX, fromY, toX, toY, radius, samples);
      endRender();
   }

   public static void renderRoundedQuad2(class_4587 matrices, Color c, Color c2, Color c3, Color c4, double fromX, double fromY, double toX, double toY, double radius) {
      setupRender();
      RenderSystem.setShader(class_757::method_34540);
      renderRoundedQuadInternal2(matrices.method_23760().method_23761(), (float)c.getRed() / 255.0F, (float)c.getGreen() / 255.0F, (float)c.getBlue() / 255.0F, (float)c.getAlpha() / 255.0F, (float)c2.getRed() / 255.0F, (float)c2.getGreen() / 255.0F, (float)c2.getBlue() / 255.0F, (float)c2.getAlpha() / 255.0F, (float)c3.getRed() / 255.0F, (float)c3.getGreen() / 255.0F, (float)c3.getBlue() / 255.0F, (float)c3.getAlpha() / 255.0F, (float)c4.getRed() / 255.0F, (float)c4.getGreen() / 255.0F, (float)c4.getBlue() / 255.0F, (float)c4.getAlpha() / 255.0F, fromX, fromY, toX, toY, radius);
      endRender();
   }

   public static void renderRoundedQuadInternal(Matrix4f matrix, float cr, float cg, float cb, float ca, double fromX, double fromY, double toX, double toY, double radius, double samples) {
      class_287 bufferBuilder = class_289.method_1348().method_60827(class_5596.field_27381, class_290.field_1576);
      double[][] map = new double[][]{{toX - radius, toY - radius, radius}, {toX - radius, fromY + radius, radius}, {fromX + radius, fromY + radius, radius}, {fromX + radius, toY - radius, radius}};

      for(int i = 0; i < 4; ++i) {
         double[] current = map[i];
         double rad = current[2];

         float cos;
         for(double r = (double)i * 90.0D; r < 90.0D + (double)i * 90.0D; r += 90.0D / samples) {
            cos = (float)Math.toRadians(r);
            float sin = (float)(Math.sin((double)cos) * rad);
            float cos = (float)(Math.cos((double)cos) * rad);
            bufferBuilder.method_22918(matrix, (float)current[0] + sin, (float)current[1] + cos, 0.0F).method_22915(cr, cg, cb, ca);
         }

         float rad1 = (float)Math.toRadians(90.0D + (double)i * 90.0D);
         float sin = (float)(Math.sin((double)rad1) * rad);
         cos = (float)(Math.cos((double)rad1) * rad);
         bufferBuilder.method_22918(matrix, (float)current[0] + sin, (float)current[1] + cos, 0.0F).method_22915(cr, cg, cb, ca);
      }

      class_286.method_43433(bufferBuilder.method_60800());
   }

   public static void renderRoundedQuadInternal2(Matrix4f matrix, float cr, float cg, float cb, float ca, float cr1, float cg1, float cb1, float ca1, float cr2, float cg2, float cb2, float ca2, float cr3, float cg3, float cb3, float ca3, double fromX, double fromY, double toX, double toY, double radC1) {
      class_287 bufferBuilder = class_289.method_1348().method_60827(class_5596.field_27381, class_290.field_1576);
      double[][] map = new double[][]{{toX - radC1, toY - radC1, radC1}, {toX - radC1, fromY + radC1, radC1}, {fromX + radC1, fromY + radC1, radC1}, {fromX + radC1, toY - radC1, radC1}};

      for(int i = 0; i < 4; ++i) {
         double[] current = map[i];
         double rad = current[2];

         for(double r = (double)(i * 90); r < (double)(90 + i * 90); r += 10.0D) {
            float rad1 = (float)Math.toRadians(r);
            float sin = (float)(Math.sin((double)rad1) * rad);
            float cos = (float)(Math.cos((double)rad1) * rad);
            switch(i) {
            case 0:
               bufferBuilder.method_22918(matrix, (float)current[0] + sin, (float)current[1] + cos, 0.0F).method_22915(cr1, cg1, cb1, ca1);
               break;
            case 1:
               bufferBuilder.method_22918(matrix, (float)current[0] + sin, (float)current[1] + cos, 0.0F).method_22915(cr, cg, cb, ca);
               break;
            case 2:
               bufferBuilder.method_22918(matrix, (float)current[0] + sin, (float)current[1] + cos, 0.0F).method_22915(cr2, cg2, cb2, ca2);
               break;
            default:
               bufferBuilder.method_22918(matrix, (float)current[0] + sin, (float)current[1] + cos, 0.0F).method_22915(cr3, cg3, cb3, ca3);
            }
         }
      }

      class_286.method_43433(bufferBuilder.method_60800());
   }

   public static void draw2DGradientRect(class_4587 matrices, float left, float top, float right, float bottom, Color leftBottomColor, Color leftTopColor, Color rightBottomColor, Color rightTopColor) {
      Matrix4f matrix = matrices.method_23760().method_23761();
      setupRender();
      RenderSystem.setShader(class_757::method_34540);
      class_287 bufferBuilder = class_289.method_1348().method_60827(class_5596.field_27382, class_290.field_1576);
      bufferBuilder.method_22918(matrix, right, top, 0.0F).method_39415(rightTopColor.getRGB());
      bufferBuilder.method_22918(matrix, left, top, 0.0F).method_39415(leftTopColor.getRGB());
      bufferBuilder.method_22918(matrix, left, bottom, 0.0F).method_39415(leftBottomColor.getRGB());
      bufferBuilder.method_22918(matrix, right, bottom, 0.0F).method_39415(rightBottomColor.getRGB());
      class_286.method_43433(bufferBuilder.method_60800());
      endRender();
   }

   public static void setupRender() {
      RenderSystem.enableBlend();
      RenderSystem.defaultBlendFunc();
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
   }

   public static void drawTracerPointer(class_4587 matrices, float x, float y, float size, float tracerWidth, float downHeight, boolean down, boolean glow, int color) {
      switch((HudEditor.ArrowsStyle)HudEditor.arrowsStyle.getValue()) {
      case Default:
         drawDefaultArrow(matrices, x, y, size, tracerWidth, downHeight, down, glow, color);
         break;
      case New:
         drawNewArrow(matrices, x, y, size + 8.0F, new Color(color));
      }

   }

   public static void drawNewArrow(class_4587 matrices, float x, float y, float size, Color color) {
      RenderSystem.setShaderTexture(0, TextureStorage.arrow);
      setupRender();
      RenderSystem.setShaderColor((float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F, (float)color.getAlpha() / 255.0F);
      RenderSystem.disableDepthTest();
      RenderSystem.blendFunc(class_4535.SRC_ALPHA, class_4534.ONE);
      Matrix4f matrix = matrices.method_23760().method_23761();
      RenderSystem.setShader(class_757::method_34542);
      class_287 bufferBuilder = class_289.method_1348().method_60827(class_5596.field_27382, class_290.field_1585);
      bufferBuilder.method_22918(matrix, x - size / 2.0F, y + size, 0.0F).method_22913(0.0F, 1.0F);
      bufferBuilder.method_22918(matrix, x + size / 2.0F, y + size, 0.0F).method_22913(1.0F, 1.0F);
      bufferBuilder.method_22918(matrix, x + size / 2.0F, y, 0.0F).method_22913(1.0F, 0.0F);
      bufferBuilder.method_22918(matrix, x - size / 2.0F, y, 0.0F).method_22913(0.0F, 0.0F);
      class_286.method_43433(bufferBuilder.method_60800());
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      RenderSystem.defaultBlendFunc();
      RenderSystem.enableDepthTest();
      endRender();
   }

   public static void drawDefaultArrow(class_4587 matrices, float x, float y, float size, float tracerWidth, float downHeight, boolean down, boolean glow, int color) {
      if (glow) {
         drawBlurredShadow(matrices, x - size * tracerWidth, y, x + size * tracerWidth - (x - size * tracerWidth), size, 10, injectAlpha(new Color(color), 140));
      }

      matrices.method_22903();
      setupRender();
      Matrix4f matrix = matrices.method_23760().method_23761();
      RenderSystem.setShader(class_757::method_34540);
      class_287 bufferBuilder = class_289.method_1348().method_60827(class_5596.field_27382, class_290.field_1576);
      bufferBuilder.method_22918(matrix, x, y, 0.0F).method_39415(color);
      bufferBuilder.method_22918(matrix, x - size * tracerWidth, y + size, 0.0F).method_39415(color);
      bufferBuilder.method_22918(matrix, x, y + size - downHeight, 0.0F).method_39415(color);
      bufferBuilder.method_22918(matrix, x, y, 0.0F).method_39415(color);
      color = darker(new Color(color), 0.8F).getRGB();
      bufferBuilder.method_22918(matrix, x, y, 0.0F).method_39415(color);
      bufferBuilder.method_22918(matrix, x, y + size - downHeight, 0.0F).method_39415(color);
      bufferBuilder.method_22918(matrix, x + size * tracerWidth, y + size, 0.0F).method_39415(color);
      bufferBuilder.method_22918(matrix, x, y, 0.0F).method_39415(color);
      if (down) {
         color = darker(new Color(color), 0.6F).getRGB();
         bufferBuilder.method_22918(matrix, x - size * tracerWidth, y + size, 0.0F).method_39415(color);
         bufferBuilder.method_22918(matrix, x + size * tracerWidth, y + size, 0.0F).method_39415(color);
         bufferBuilder.method_22918(matrix, x, y + size - downHeight, 0.0F).method_39415(color);
         bufferBuilder.method_22918(matrix, x - size * tracerWidth, y + size, 0.0F).method_39415(color);
      }

      class_286.method_43433(bufferBuilder.method_60800());
      endRender();
      matrices.method_22909();
   }

   public static void endRender() {
      RenderSystem.defaultBlendFunc();
      RenderSystem.disableBlend();
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
   }

   public static void drawGradientRound(class_4587 ms, float v, float v1, float i, float i1, float v2, Color darker, Color darker1, Color darker2, Color darker3) {
      renderRoundedQuad2(ms, darker, darker1, darker2, darker3, (double)v, (double)v1, (double)(v + i), (double)(v1 + i1), (double)v2);
   }

   public static float scrollAnimate(float endPoint, float current, float speed) {
      boolean shouldContinueAnimation = endPoint > current;
      if (speed < 0.0F) {
         speed = 0.0F;
      } else if (speed > 1.0F) {
         speed = 1.0F;
      }

      float dif = Math.max(endPoint, current) - Math.min(endPoint, current);
      float factor = dif * speed;
      return current + (shouldContinueAnimation ? factor : -factor);
   }

   public static Color injectAlpha(Color color, int alpha) {
      return new Color(color.getRed(), color.getGreen(), color.getBlue(), class_3532.method_15340(alpha, 0, 255));
   }

   public static Color TwoColoreffect(Color cl1, Color cl2, double speed, double count) {
      int angle = (int)(((double)System.currentTimeMillis() / speed + count) % 360.0D);
      angle = (angle >= 180 ? 360 - angle : angle) * 2;
      return interpolateColorC(cl1, cl2, (float)angle / 360.0F);
   }

   public static Color astolfo(boolean clickgui, int yOffset) {
      float speed = clickgui ? 3500.0F : 3000.0F;
      float hue = (float)(System.currentTimeMillis() % (long)((int)speed) + (long)yOffset);
      if (hue > speed) {
         hue -= speed;
      }

      hue /= speed;
      if (hue > 0.5F) {
         hue = 0.5F - (hue - 0.5F);
      }

      hue += 0.5F;
      return Color.getHSBColor(hue, 0.4F, 1.0F);
   }

   public static Color rainbow(int delay, float saturation, float brightness) {
      double rainbow = Math.ceil((double)((float)(System.currentTimeMillis() + (long)delay) / 16.0F));
      rainbow %= 360.0D;
      return Color.getHSBColor((float)(rainbow / 360.0D), saturation, brightness);
   }

   public static Color skyRainbow(int speed, int index) {
      int angle = (int)((System.currentTimeMillis() / (long)speed + (long)index) % 360L);
      return Color.getHSBColor((double)((float)((double)(angle %= 360) / 360.0D)) < 0.5D ? -((float)((double)angle / 360.0D)) : (float)((double)angle / 360.0D), 0.5F, 1.0F);
   }

   public static Color fade(int speed, int index, Color color, float alpha) {
      float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), (float[])null);
      int angle = (int)((System.currentTimeMillis() / (long)speed + (long)index) % 360L);
      angle = (angle > 180 ? 360 - angle : angle) + 180;
      Color colorHSB = new Color(Color.HSBtoRGB(hsb[0], hsb[1], (float)angle / 360.0F));
      return new Color(colorHSB.getRed(), colorHSB.getGreen(), colorHSB.getBlue(), Math.max(0, Math.min(255, (int)(alpha * 255.0F))));
   }

   public static Color getAnalogousColor(Color color) {
      float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), (float[])null);
      float degree = 0.84F;
      float newHueSubtracted = hsb[0] - degree;
      return new Color(Color.HSBtoRGB(newHueSubtracted, hsb[1], hsb[2]));
   }

   public static Color applyOpacity(Color color, float opacity) {
      opacity = Math.min(1.0F, Math.max(0.0F, opacity));
      return new Color(color.getRed(), color.getGreen(), color.getBlue(), (int)((float)color.getAlpha() * opacity));
   }

   public static int applyOpacity(int color_int, float opacity) {
      opacity = Math.min(1.0F, Math.max(0.0F, opacity));
      Color color = new Color(color_int);
      return (new Color(color.getRed(), color.getGreen(), color.getBlue(), (int)((float)color.getAlpha() * opacity))).getRGB();
   }

   public static Color darker(Color color, float factor) {
      return new Color(Math.max((int)((float)color.getRed() * factor), 0), Math.max((int)((float)color.getGreen() * factor), 0), Math.max((int)((float)color.getBlue() * factor), 0), color.getAlpha());
   }

   public static Color rainbow(int speed, int index, float saturation, float brightness, float opacity) {
      int angle = (int)((System.currentTimeMillis() / (long)speed + (long)index) % 360L);
      float hue = (float)angle / 360.0F;
      Color color = new Color(Color.HSBtoRGB(hue, saturation, brightness));
      return new Color(color.getRed(), color.getGreen(), color.getBlue(), Math.max(0, Math.min(255, (int)(opacity * 255.0F))));
   }

   public static Color interpolateColorsBackAndForth(int speed, int index, Color start, Color end, boolean trueColor) {
      int angle = (int)((System.currentTimeMillis() / (long)speed + (long)index) % 360L);
      angle = (angle >= 180 ? 360 - angle : angle) * 2;
      return trueColor ? interpolateColorHue(start, end, (float)angle / 360.0F) : interpolateColorC(start, end, (float)angle / 360.0F);
   }

   public static Color interpolateColorC(Color color1, Color color2, float amount) {
      amount = Math.min(1.0F, Math.max(0.0F, amount));
      return new Color(interpolateInt(color1.getRed(), color2.getRed(), (double)amount), interpolateInt(color1.getGreen(), color2.getGreen(), (double)amount), interpolateInt(color1.getBlue(), color2.getBlue(), (double)amount), interpolateInt(color1.getAlpha(), color2.getAlpha(), (double)amount));
   }

   public static Color interpolateColorHue(Color color1, Color color2, float amount) {
      amount = Math.min(1.0F, Math.max(0.0F, amount));
      float[] color1HSB = Color.RGBtoHSB(color1.getRed(), color1.getGreen(), color1.getBlue(), (float[])null);
      float[] color2HSB = Color.RGBtoHSB(color2.getRed(), color2.getGreen(), color2.getBlue(), (float[])null);
      Color resultColor = Color.getHSBColor(interpolateFloat(color1HSB[0], color2HSB[0], (double)amount), interpolateFloat(color1HSB[1], color2HSB[1], (double)amount), interpolateFloat(color1HSB[2], color2HSB[2], (double)amount));
      return new Color(resultColor.getRed(), resultColor.getGreen(), resultColor.getBlue(), interpolateInt(color1.getAlpha(), color2.getAlpha(), (double)amount));
   }

   public static double interpolate(double oldValue, double newValue, double interpolationValue) {
      return oldValue + (newValue - oldValue) * interpolationValue;
   }

   public static float interpolateFloat(float oldValue, float newValue, double interpolationValue) {
      return (float)interpolate((double)oldValue, (double)newValue, (double)((float)interpolationValue));
   }

   public static int interpolateInt(int oldValue, int newValue, double interpolationValue) {
      return (int)interpolate((double)oldValue, (double)newValue, (double)((float)interpolationValue));
   }

   public static void drawArc(class_4587 matrices, float x, float y, float width, float height, float radius, float thickness, float start, float end, Color c1, Color c2) {
      class_287 bb = preShaderDraw(matrices, x - width / 2.0F, y - height / 2.0F, x + width / 2.0F, y + height / 2.0F);
      ARC_PROGRAM.setParameters(x, y, width, height, radius, thickness, start, end, c1, c2);
      ARC_PROGRAM.use();
      class_286.method_43433(bb.method_60800());
      endRender();
   }

   public static void drawRect(class_4587 matrices, float x, float y, float width, float height, float radius, float alpha) {
      class_287 bb = preShaderDraw(matrices, x - 10.0F, y - 10.0F, width + 20.0F, height + 20.0F);
      RECTANGLE_SHADER.setParameters(x, y, width, height, radius, alpha);
      RECTANGLE_SHADER.use();
      class_286.method_43433(bb.method_60800());
      endRender();
   }

   public static void drawRect(class_4587 matrices, float x, float y, float width, float height, float radius, float alpha, Color c1, Color c2, Color c3, Color c4) {
      class_287 bb = preShaderDraw(matrices, x - 10.0F, y - 10.0F, width + 20.0F, height + 20.0F);
      RECTANGLE_SHADER.setParameters(x, y, width, height, radius, alpha, c1, c2, c3, c4);
      RECTANGLE_SHADER.use();
      class_286.method_43433(bb.method_60800());
      endRender();
   }

   public static void drawHudBase(class_4587 matrices, float x, float y, float width, float height, float radius) {
      if (HudEditor.hudStyle.is(HudEditor.HudStyle.Blurry)) {
         drawRoundedBlur(matrices, x, y, width, height, radius, ((ColorSetting)HudEditor.blurColor.getValue()).getColorObject());
      } else {
         class_287 bb = preShaderDraw(matrices, x - 10.0F, y - 10.0F, width + 20.0F, height + 20.0F);
         HUD_SHADER.setParameters(x, y, width, height, radius, (Float)HudEditor.alpha.getValue(), (Float)HudEditor.alpha.getValue());
         HUD_SHADER.use();
         class_286.method_43433(bb.method_60800());
         endRender();
      }

   }

   public static void drawHudBase2(class_4587 matrices, float x, float y, float width, float height, float radius, float blurStrenth, float blurOpacity, float animationFactor) {
      if (HudEditor.hudStyle.is(HudEditor.HudStyle.Blurry)) {
         blurStrenth *= animationFactor;
         blurOpacity = (float)interpolate(1.0D, (double)blurOpacity, (double)animationFactor);
         Color c = interpolateColorC(Color.BLACK, ((ColorSetting)HudEditor.blurColor.getValue()).getColorObject(), animationFactor);
         drawRoundedBlur(matrices, x, y, width, height, radius, c, blurStrenth, blurOpacity);
      } else {
         class_287 bb = preShaderDraw(matrices, x - 10.0F, y - 10.0F, width + 20.0F, height + 20.0F);
         HUD_SHADER.setParameters(x, y, width, height, radius, (Float)HudEditor.alpha.getValue(), (Float)HudEditor.alpha.getValue());
         HUD_SHADER.use();
         class_286.method_43433(bb.method_60800());
         endRender();
      }

   }

   public static void drawHudBase(class_4587 matrices, float x, float y, float width, float height, float radius, boolean hud) {
      class_287 bb = preShaderDraw(matrices, x - 10.0F, y - 10.0F, width + 20.0F, height + 20.0F);
      HUD_SHADER.setParameters(x, y, width, height, radius, (Float)HudEditor.alpha.getValue(), (Float)HudEditor.alpha.getValue());
      HUD_SHADER.use();
      class_286.method_43433(bb.method_60800());
      endRender();
   }

   public static void drawRoundedBlur(class_4587 matrices, float x, float y, float width, float height, float radius, Color c1) {
      drawRoundedBlur(matrices, x, y, width, height, radius, c1, (Float)HudEditor.blurStrength.getValue(), (Float)HudEditor.blurOpacity.getValue());
   }

   public static void drawRoundedBlur(class_4587 matrices, float x, float y, float width, float height, float radius, Color c1, float blurStrenth, float blurOpacity) {
      class_287 bb = preShaderDraw(matrices, x - 10.0F, y - 10.0F, width + 20.0F, height + 20.0F);
      BLUR_PROGRAM.setParameters(x, y, width, height, radius, c1, blurStrenth, blurOpacity);
      BLUR_PROGRAM.use();
      class_286.method_43433(bb.method_60800());
      endRender();
   }

   public static void drawHudBase(class_4587 matrices, float x, float y, float width, float height, float radius, float alpha) {
      class_287 bb = preShaderDraw(matrices, x - 10.0F, y - 10.0F, width + 20.0F, height + 20.0F);
      HUD_SHADER.setParameters(x, y, width, height, radius, alpha, (Float)HudEditor.alpha.getValue());
      HUD_SHADER.use();
      class_286.method_43433(bb.method_60800());
      endRender();
   }

   public static void drawGuiBase(class_4587 matrices, float x, float y, float width, float height, float radius, float opacity) {
      class_287 bb = preShaderDraw(matrices, x - 10.0F, y - 10.0F, width + 20.0F, height + 20.0F);
      HUD_SHADER.setParameters(x, y, width, height, radius, 1.0F, opacity);
      HUD_SHADER.use();
      class_286.method_43433(bb.method_60800());
      endRender();
   }

   public static void drawMainMenuShader(class_4587 matrices, float x, float y, float width, float height) {
      class_287 bb = preShaderDraw(matrices, x, y, width, height);
      MAIN_MENU_PROGRAM.setParameters(x, y, width, height);
      MAIN_MENU_PROGRAM.use();
      class_286.method_43433(bb.method_60800());
      endRender();
   }

   public static class_287 preShaderDraw(class_4587 matrices, float x, float y, float width, float height) {
      setupRender();
      Matrix4f matrix = matrices.method_23760().method_23761();
      class_287 buffer = class_289.method_1348().method_60827(class_5596.field_27382, class_290.field_1592);
      setRectanglePoints(buffer, matrix, x, y, x + width, y + height);
      return buffer;
   }

   public static void setRectanglePoints(class_287 buffer, Matrix4f matrix, float x, float y, float x1, float y1) {
      buffer.method_22918(matrix, x, y, 0.0F);
      buffer.method_22918(matrix, x, y1, 0.0F);
      buffer.method_22918(matrix, x1, y1, 0.0F);
      buffer.method_22918(matrix, x1, y, 0.0F);
   }

   public static void drawOrbiz(class_4587 matrices, float z, double r, Color c) {
      Matrix4f matrix = matrices.method_23760().method_23761();
      setupRender();
      RenderSystem.setShader(class_757::method_34540);
      class_287 bufferBuilder = class_289.method_1348().method_60827(class_5596.field_27381, class_290.field_1576);

      for(int i = 0; i <= 20; ++i) {
         float x2 = (float)(Math.sin((double)((float)i * 56.548656F / 180.0F)) * r);
         float y2 = (float)(Math.cos((double)((float)i * 56.548656F / 180.0F)) * r);
         bufferBuilder.method_22918(matrix, x2, y2, z).method_22915((float)c.getRed() / 255.0F, (float)c.getGreen() / 255.0F, (float)c.getBlue() / 255.0F, 0.4F);
      }

      class_286.method_43433(bufferBuilder.method_60800());
      endRender();
   }

   public static void drawStar(class_4587 matrices, Color c, float scale) {
      setupRender();
      RenderSystem.blendFunc(class_4535.SRC_ALPHA, class_4534.ONE);
      RenderSystem.setShaderTexture(0, TextureStorage.star);
      RenderSystem.setShaderColor((float)c.getRed() / 255.0F, (float)c.getGreen() / 255.0F, (float)c.getBlue() / 255.0F, (float)c.getAlpha() / 255.0F);
      renderGradientTexture(matrices, 0.0D, 0.0D, (double)scale, (double)scale, 0.0F, 0.0F, 128.0D, 128.0D, 128.0D, 128.0D, c, c, c, c);
      endRender();
   }

   public static void drawHeart(class_4587 matrices, Color c, float scale) {
      setupRender();
      RenderSystem.blendFunc(class_4535.SRC_ALPHA, class_4534.ONE);
      RenderSystem.setShaderTexture(0, TextureStorage.heart);
      RenderSystem.setShaderColor((float)c.getRed() / 255.0F, (float)c.getGreen() / 255.0F, (float)c.getBlue() / 255.0F, (float)c.getAlpha() / 255.0F);
      renderGradientTexture(matrices, 0.0D, 0.0D, (double)scale, (double)scale, 0.0F, 0.0F, 128.0D, 128.0D, 128.0D, 128.0D, c, c, c, c);
      endRender();
   }

   public static void drawBloom(class_4587 matrices, Color c, float scale) {
      setupRender();
      RenderSystem.blendFunc(class_4535.SRC_ALPHA, class_4534.ONE);
      RenderSystem.setShaderTexture(0, TextureStorage.firefly);
      RenderSystem.setShaderColor((float)c.getRed() / 255.0F, (float)c.getGreen() / 255.0F, (float)c.getBlue() / 255.0F, (float)c.getAlpha() / 255.0F);
      renderGradientTexture(matrices, 0.0D, 0.0D, (double)scale, (double)scale, 0.0F, 0.0F, 128.0D, 128.0D, 128.0D, 128.0D, c, c, c, c);
      endRender();
   }

   public static void drawBubble(class_4587 matrices, float angle, float factor) {
      setupRender();
      RenderSystem.blendFunc(class_4535.SRC_ALPHA, class_4534.ONE);
      RenderSystem.setShaderTexture(0, TextureStorage.bubble);
      matrices.method_22907(class_7833.field_40718.rotationDegrees(angle));
      float scale = factor * 2.0F;
      renderGradientTexture(matrices, (double)(-scale / 2.0F), (double)(-scale / 2.0F), (double)scale, (double)scale, 0.0F, 0.0F, 128.0D, 128.0D, 128.0D, 128.0D, applyOpacity(HudEditor.getColor(270), 1.0F - factor), applyOpacity(HudEditor.getColor(0), 1.0F - factor), applyOpacity(HudEditor.getColor(180), 1.0F - factor), applyOpacity(HudEditor.getColor(90), 1.0F - factor));
      endRender();
   }

   public static void drawLine(float x, float y, float x1, float y1, int color) {
      RenderSystem.setShader(class_757::method_34540);
      class_287 bufferBuilder = class_289.method_1348().method_60827(class_5596.field_29345, class_290.field_1576);
      bufferBuilder.method_22912(x, y, 0.0F).method_39415(color);
      bufferBuilder.method_22912(x1, y1, 0.0F).method_39415(color);
      class_286.method_43433(bufferBuilder.method_60800());
   }

   public static boolean isDark(Color color) {
      return isDark((float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F);
   }

   public static boolean isDark(float r, float g, float b) {
      return colorDistance(r, g, b, 0.0F, 0.0F, 0.0F) < colorDistance(r, g, b, 1.0F, 1.0F, 1.0F);
   }

   public static float colorDistance(float r1, float g1, float b1, float r2, float g2, float b2) {
      float a = r2 - r1;
      float b = g2 - g1;
      float c = b2 - b1;
      return (float)Math.sqrt((double)(a * a + b * b + c * c));
   }

   public static void initShaders() {
      HUD_SHADER = new HudShader();
      MAIN_MENU_PROGRAM = new MainMenuProgram();
      TEXTURE_COLOR_PROGRAM = new TextureColorProgram();
      ARC_PROGRAM = new ArcShader();
      RECTANGLE_SHADER = new RectangleShader();
      BLUR_PROGRAM = new BlurProgram();
   }

   @NotNull
   public static Color getColor(@NotNull Color start, @NotNull Color end, float progress, boolean smooth) {
      if (!smooth) {
         return (double)progress >= 0.95D ? end : start;
      } else {
         int rDiff = end.getRed() - start.getRed();
         int gDiff = end.getGreen() - start.getGreen();
         int bDiff = end.getBlue() - start.getBlue();
         int aDiff = end.getAlpha() - start.getAlpha();
         return new Color(fixColorValue(start.getRed() + (int)((float)rDiff * progress)), fixColorValue(start.getGreen() + (int)((float)gDiff * progress)), fixColorValue(start.getBlue() + (int)((float)bDiff * progress)), fixColorValue(start.getAlpha() + (int)((float)aDiff * progress)));
      }
   }

   private static int fixColorValue(int colorVal) {
      return colorVal > 255 ? 255 : Math.max(colorVal, 0);
   }

   public static void endBuilding(class_287 bb) {
      class_9801 builtBuffer = bb.method_60794();
      if (builtBuffer != null) {
         class_286.method_43433(builtBuffer);
      }

   }

   public static record Rectangle(float x, float y, float x1, float y1) {
      public Rectangle(float x, float y, float x1, float y1) {
         this.x = x;
         this.y = y;
         this.x1 = x1;
         this.y1 = y1;
      }

      public boolean contains(double x, double y) {
         return x >= (double)this.x && x <= (double)this.x1 && y >= (double)this.y && y <= (double)this.y1;
      }

      public float x() {
         return this.x;
      }

      public float y() {
         return this.y;
      }

      public float x1() {
         return this.x1;
      }

      public float y1() {
         return this.y1;
      }
   }

   public static class BlurredShadow {
      Texture id = new Texture("texture/remote/" + RandomStringUtils.randomAlphanumeric(16));

      public BlurredShadow(BufferedImage bufferedImage) {
         Render2DEngine.registerBufferedImageTexture(this.id, bufferedImage);
      }

      public void bind() {
         RenderSystem.setShaderTexture(0, this.id.getId());
      }
   }
}
