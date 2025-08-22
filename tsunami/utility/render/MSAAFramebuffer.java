package tsunami.utility.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.class_276;
import net.minecraft.class_310;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL30;

public class MSAAFramebuffer extends class_276 {
   public static final int MAX_SAMPLES = GL30.glGetInteger(36183);
   private static final Map<Integer, MSAAFramebuffer> INSTANCES = new HashMap();
   private final int samples;
   private int rboColor;
   private int rboDepth;

   public MSAAFramebuffer(int samples) {
      super(true);
      this.samples = samples;
      this.method_1236(1.0F, 1.0F, 1.0F, 0.0F);
   }

   public static MSAAFramebuffer getInstance(int samples) {
      return (MSAAFramebuffer)INSTANCES.computeIfAbsent(samples, (x) -> {
         return new MSAAFramebuffer(samples);
      });
   }

   public static void use(boolean fancy, Runnable drawAction) {
      use(Math.min(fancy ? 16 : 4, MAX_SAMPLES), class_310.method_1551().method_1522(), drawAction);
   }

   public static void use(int samples, @NotNull class_276 mainBuffer, @NotNull Runnable drawAction) {
      RenderSystem.assertOnRenderThreadOrInit();
      MSAAFramebuffer msaaBuffer = getInstance(samples);
      msaaBuffer.method_1234(mainBuffer.field_1482, mainBuffer.field_1481, false);
      GlStateManager._glBindFramebuffer(36008, mainBuffer.field_1476);
      GlStateManager._glBindFramebuffer(36009, msaaBuffer.field_1476);
      GlStateManager._glBlitFrameBuffer(0, 0, msaaBuffer.field_1482, msaaBuffer.field_1481, 0, 0, msaaBuffer.field_1482, msaaBuffer.field_1481, 16384, 9729);
      msaaBuffer.method_1235(true);
      drawAction.run();
      msaaBuffer.method_1240();
      GlStateManager._glBindFramebuffer(36008, msaaBuffer.field_1476);
      GlStateManager._glBindFramebuffer(36009, mainBuffer.field_1476);
      GlStateManager._glBlitFrameBuffer(0, 0, msaaBuffer.field_1482, msaaBuffer.field_1481, 0, 0, msaaBuffer.field_1482, msaaBuffer.field_1481, 16384, 9729);
      msaaBuffer.method_1230(false);
      mainBuffer.method_1235(false);
   }

   public void method_1234(int width, int height, boolean getError) {
      if (this.field_1482 != width || this.field_1481 != height) {
         super.method_1234(width, height, getError);
      }

   }

   public void method_1231(int width, int height, boolean getError) {
      RenderSystem.assertOnRenderThreadOrInit();
      this.field_1480 = width;
      this.field_1477 = height;
      this.field_1482 = width;
      this.field_1481 = height;
      this.field_1476 = GlStateManager.glGenFramebuffers();
      GlStateManager._glBindFramebuffer(36160, this.field_1476);
      this.rboColor = GlStateManager.glGenRenderbuffers();
      GlStateManager._glBindRenderbuffer(36161, this.rboColor);
      GL30.glRenderbufferStorageMultisample(36161, this.samples, 32856, width, height);
      GlStateManager._glBindRenderbuffer(36161, 0);
      this.rboDepth = GlStateManager.glGenRenderbuffers();
      GlStateManager._glBindRenderbuffer(36161, this.rboDepth);
      GL30.glRenderbufferStorageMultisample(36161, this.samples, 6402, width, height);
      GlStateManager._glBindRenderbuffer(36161, 0);
      GL30.glFramebufferRenderbuffer(36160, 36064, 36161, this.rboColor);
      GL30.glFramebufferRenderbuffer(36160, 36096, 36161, this.rboDepth);
      this.field_1475 = class_310.method_1551().method_1522().method_30277();
      this.field_1474 = class_310.method_1551().method_1522().method_30278();
      this.method_1239();
      this.method_1230(getError);
      this.method_1242();
   }

   public void method_1238() {
      RenderSystem.assertOnRenderThreadOrInit();
      this.method_1242();
      this.method_1240();
      if (this.field_1476 > -1) {
         GlStateManager._glBindFramebuffer(36160, 0);
         GlStateManager._glDeleteFramebuffers(this.field_1476);
         this.field_1476 = -1;
      }

      if (this.rboColor > -1) {
         GlStateManager._glDeleteRenderbuffers(this.rboColor);
         this.rboColor = -1;
      }

      if (this.rboDepth > -1) {
         GlStateManager._glDeleteRenderbuffers(this.rboDepth);
         this.rboDepth = -1;
      }

      this.field_1475 = -1;
      this.field_1474 = -1;
      this.field_1482 = -1;
      this.field_1481 = -1;
   }
}
