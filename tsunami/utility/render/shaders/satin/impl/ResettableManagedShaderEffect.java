package tsunami.utility.render.shaders.satin.impl;

import com.google.common.base.Preconditions;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.logging.LogUtils;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Consumer;
import net.minecraft.class_279;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_5912;
import tsunami.injection.accesors.AccessiblePassesShaderEffect;
import tsunami.utility.render.shaders.satin.api.managed.ManagedFramebuffer;
import tsunami.utility.render.shaders.satin.api.managed.ManagedShaderEffect;
import tsunami.utility.render.shaders.satin.api.managed.uniform.SamplerUniformV2;

public final class ResettableManagedShaderEffect extends ResettableManagedShaderBase<class_279> implements ManagedShaderEffect {
   private final Consumer<ManagedShaderEffect> initCallback;
   private final Map<String, FramebufferWrapper> managedTargets;
   private final Map<String, ManagedSamplerUniformV2> managedSamplers = new HashMap();

   public ResettableManagedShaderEffect(class_2960 location, Consumer<ManagedShaderEffect> initCallback) {
      super(location);
      this.initCallback = initCallback;
      this.managedTargets = new HashMap();
   }

   public class_279 getShaderEffect() {
      return this.getShaderOrLog();
   }

   protected class_279 parseShader(class_5912 resourceFactory, class_310 mc, class_2960 location) throws IOException {
      return new class_279(mc.method_1531(), mc.method_1478(), mc.method_1522(), location);
   }

   public void setup(int windowWidth, int windowHeight) {
      Preconditions.checkNotNull((class_279)this.shader);
      ((class_279)this.shader).method_1259(windowWidth, windowHeight);
      Iterator var3 = this.getManagedUniforms().iterator();

      while(var3.hasNext()) {
         ManagedUniformBase uniform = (ManagedUniformBase)var3.next();
         this.setupUniform(uniform, (class_279)this.shader);
      }

      var3 = this.managedTargets.values().iterator();

      while(var3.hasNext()) {
         FramebufferWrapper buf = (FramebufferWrapper)var3.next();
         buf.findTarget((class_279)this.shader);
      }

      this.initCallback.accept(this);
   }

   public void render(float tickDelta) {
      class_279 sg = this.getShaderEffect();
      if (sg != null) {
         RenderSystem.disableBlend();
         RenderSystem.disableDepthTest();
         RenderSystem.resetTextureMatrix();
         sg.method_1258(tickDelta);
         class_310.method_1551().method_1522().method_1235(true);
         RenderSystem.disableBlend();
         RenderSystem.blendFunc(770, 771);
         RenderSystem.enableDepthTest();
      }

   }

   public ManagedFramebuffer getTarget(String name) {
      return (ManagedFramebuffer)this.managedTargets.computeIfAbsent(name, (n) -> {
         FramebufferWrapper ret = new FramebufferWrapper(n);
         if (this.shader != null) {
            ret.findTarget((class_279)this.shader);
         }

         return ret;
      });
   }

   public void setUniformValue(String uniformName, int value) {
      this.findUniform1i(uniformName).set(value);
   }

   public void setUniformValue(String uniformName, float value) {
      this.findUniform1f(uniformName).set(value);
   }

   public void setUniformValue(String uniformName, float value0, float value1) {
      this.findUniform2f(uniformName).set(value0, value1);
   }

   public void setUniformValue(String uniformName, float value0, float value1, float value2) {
      this.findUniform3f(uniformName).set(value0, value1, value2);
   }

   public void setUniformValue(String uniformName, float value0, float value1, float value2, float value3) {
      this.findUniform4f(uniformName).set(value0, value1, value2, value3);
   }

   public SamplerUniformV2 findSampler(String samplerName) {
      return (SamplerUniformV2)this.manageUniform(this.managedSamplers, ManagedSamplerUniformV2::new, samplerName, "sampler");
   }

   protected boolean setupUniform(ManagedUniformBase uniform, class_279 shader) {
      return uniform.findUniformTargets(((AccessiblePassesShaderEffect)shader).getPasses());
   }

   protected void logInitError(IOException e) {
      LogUtils.getLogger().error("Could not create screen shader {}", this.getLocation(), e);
   }

   private class_279 getShaderOrLog() {
      if (!this.isInitialized() && !this.isErrored()) {
         this.initializeOrLog(class_310.method_1551().method_1478());
      }

      return (class_279)this.shader;
   }
}
