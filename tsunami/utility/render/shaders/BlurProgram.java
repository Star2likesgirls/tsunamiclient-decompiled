package tsunami.utility.render.shaders;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.util.Objects;
import net.minecraft.class_276;
import net.minecraft.class_290;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_6367;
import org.lwjgl.opengl.GL30;
import tsunami.features.modules.Module;
import tsunami.utility.render.WindowResizeCallback;
import tsunami.utility.render.shaders.satin.api.managed.ManagedCoreShader;
import tsunami.utility.render.shaders.satin.api.managed.ShaderEffectManager;
import tsunami.utility.render.shaders.satin.api.managed.uniform.SamplerUniform;
import tsunami.utility.render.shaders.satin.api.managed.uniform.Uniform1f;
import tsunami.utility.render.shaders.satin.api.managed.uniform.Uniform2f;
import tsunami.utility.render.shaders.satin.api.managed.uniform.Uniform4f;

public class BlurProgram {
   private Uniform2f uSize;
   private Uniform2f uLocation;
   private Uniform1f radius;
   private Uniform2f inputResolution;
   private Uniform1f brightness;
   private Uniform1f quality;
   private Uniform4f color1;
   private SamplerUniform sampler;
   private class_276 input;
   public static final ManagedCoreShader BLUR;

   public BlurProgram() {
      this.setup();
   }

   public void setParameters(float x, float y, float width, float height, float r, Color c1, float blurStrenth, float blurOpacity) {
      if (this.input == null) {
         this.input = new class_6367(Module.mc.method_22683().method_4486(), Module.mc.method_22683().method_4502(), false, class_310.field_1703);
      }

      float i = (float)Module.mc.method_22683().method_4495();
      this.radius.set(r * i);
      this.uLocation.set(x * i, -y * i + (float)Module.mc.method_22683().method_4502() * i - height * i);
      this.uSize.set(width * i, height * i);
      this.brightness.set(blurOpacity);
      this.quality.set(blurStrenth);
      this.color1.set((float)c1.getRed() / 255.0F, (float)c1.getGreen() / 255.0F, (float)c1.getBlue() / 255.0F, 1.0F);
      this.sampler.set(this.input.method_30277());
   }

   public void use() {
      class_276 buffer = class_310.method_1551().method_1522();
      this.input.method_1235(false);
      GL30.glBindFramebuffer(36008, buffer.field_1476);
      GL30.glBlitFramebuffer(0, 0, buffer.field_1482, buffer.field_1481, 0, 0, buffer.field_1482, buffer.field_1481, 16384, 9729);
      buffer.method_1235(false);
      if (this.input != null && (this.input.field_1482 != Module.mc.method_22683().method_4489() || this.input.field_1481 != Module.mc.method_22683().method_4506())) {
         this.input.method_1234(Module.mc.method_22683().method_4489(), Module.mc.method_22683().method_4506(), class_310.field_1703);
      }

      this.inputResolution.set((float)buffer.field_1482, (float)buffer.field_1481);
      this.sampler.set(this.input.method_30277());
      ManagedCoreShader var10000 = BLUR;
      Objects.requireNonNull(var10000);
      RenderSystem.setShader(var10000::getProgram);
   }

   protected void setup() {
      this.inputResolution = BLUR.findUniform2f("InputResolution");
      this.brightness = BLUR.findUniform1f("Brightness");
      this.quality = BLUR.findUniform1f("Quality");
      this.color1 = BLUR.findUniform4f("color1");
      this.uSize = BLUR.findUniform2f("uSize");
      this.uLocation = BLUR.findUniform2f("uLocation");
      this.radius = BLUR.findUniform1f("radius");
      this.sampler = BLUR.findSampler("InputSampler");
      WindowResizeCallback.EVENT.register((client, window) -> {
         if (this.input != null) {
            this.input.method_1234(window.method_4489(), window.method_4506(), class_310.field_1703);
         }

      });
   }

   static {
      BLUR = ShaderEffectManager.getInstance().manageCoreShader(class_2960.method_60655("thunderhack", "blur"), class_290.field_1592);
   }
}
