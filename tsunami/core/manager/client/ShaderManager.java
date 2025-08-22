package tsunami.core.manager.client;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.GlStateManager.class_4534;
import com.mojang.blaze3d.platform.GlStateManager.class_4535;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_276;
import net.minecraft.class_279;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import org.jetbrains.annotations.NotNull;
import tsunami.core.manager.IManager;
import tsunami.features.modules.render.Shaders;
import tsunami.setting.impl.ColorSetting;
import tsunami.utility.interfaces.IShaderEffect;
import tsunami.utility.render.Render3DEngine;
import tsunami.utility.render.shaders.satin.api.managed.ManagedShaderEffect;
import tsunami.utility.render.shaders.satin.api.managed.ShaderEffectManager;

public class ShaderManager implements IManager {
   private static final List<ShaderManager.RenderTask> tasks = new ArrayList();
   private ShaderManager.ThunderHackFramebuffer shaderBuffer;
   public float time = 0.0F;
   public static ManagedShaderEffect DEFAULT_OUTLINE;
   public static ManagedShaderEffect SMOKE_OUTLINE;
   public static ManagedShaderEffect GRADIENT_OUTLINE;
   public static ManagedShaderEffect SNOW_OUTLINE;
   public static ManagedShaderEffect FADE_OUTLINE;
   public static ManagedShaderEffect DEFAULT;
   public static ManagedShaderEffect SMOKE;
   public static ManagedShaderEffect GRADIENT;
   public static ManagedShaderEffect SNOW;
   public static ManagedShaderEffect FADE;

   public void renderShader(Runnable runnable, ShaderManager.Shader mode) {
      tasks.add(new ShaderManager.RenderTask(runnable, mode));
   }

   public void renderShaders() {
      if (DEFAULT == null) {
         this.shaderBuffer = new ShaderManager.ThunderHackFramebuffer(mc.method_1522().field_1482, mc.method_1522().field_1481);
         this.reloadShaders();
      }

      if (this.shaderBuffer != null) {
         tasks.forEach((t) -> {
            this.applyShader(t.task(), t.shader());
         });
         tasks.clear();
      }
   }

   public void applyShader(Runnable runnable, ShaderManager.Shader mode) {
      class_276 MCBuffer = class_310.method_1551().method_1522();
      RenderSystem.assertOnRenderThreadOrInit();
      if (this.shaderBuffer.field_1482 != MCBuffer.field_1482 || this.shaderBuffer.field_1481 != MCBuffer.field_1481) {
         this.shaderBuffer.method_1234(MCBuffer.field_1482, MCBuffer.field_1481, false);
      }

      GlStateManager._glBindFramebuffer(36009, this.shaderBuffer.field_1476);
      this.shaderBuffer.method_1235(true);
      runnable.run();
      this.shaderBuffer.method_1240();
      GlStateManager._glBindFramebuffer(36009, MCBuffer.field_1476);
      MCBuffer.method_1235(false);
      ManagedShaderEffect shader = this.getShader(mode);
      class_276 mainBuffer = class_310.method_1551().method_1522();
      class_279 effect = shader.getShaderEffect();
      if (effect != null) {
         ((IShaderEffect)effect).addFakeTargetHook("bufIn", this.shaderBuffer);
      }

      class_276 outBuffer = shader.getShaderEffect().method_1264("bufOut");
      this.setupShader(mode, shader);
      this.shaderBuffer.method_1230(false);
      mainBuffer.method_1235(false);
      RenderSystem.enableBlend();
      RenderSystem.blendFuncSeparate(class_4535.SRC_ALPHA, class_4534.ONE_MINUS_SRC_ALPHA, class_4535.ZERO, class_4534.ONE);
      RenderSystem.backupProjectionMatrix();
      outBuffer.method_22594(outBuffer.field_1482, outBuffer.field_1481, false);
      RenderSystem.restoreProjectionMatrix();
      RenderSystem.defaultBlendFunc();
      RenderSystem.disableBlend();
   }

   public ManagedShaderEffect getShader(@NotNull ShaderManager.Shader mode) {
      ManagedShaderEffect var10000;
      switch(mode.ordinal()) {
      case 1:
         var10000 = SMOKE;
         break;
      case 2:
         var10000 = GRADIENT;
         break;
      case 3:
         var10000 = SNOW;
         break;
      case 4:
         var10000 = FADE;
         break;
      default:
         var10000 = DEFAULT;
      }

      return var10000;
   }

   public ManagedShaderEffect getShaderOutline(@NotNull ShaderManager.Shader mode) {
      ManagedShaderEffect var10000;
      switch(mode.ordinal()) {
      case 1:
         var10000 = SMOKE_OUTLINE;
         break;
      case 2:
         var10000 = GRADIENT_OUTLINE;
         break;
      case 3:
         var10000 = SNOW_OUTLINE;
         break;
      case 4:
         var10000 = FADE_OUTLINE;
         break;
      default:
         var10000 = DEFAULT_OUTLINE;
      }

      return var10000;
   }

   public void setupShader(ShaderManager.Shader shader, ManagedShaderEffect effect) {
      Shaders shaders = ModuleManager.shaders;
      if (shader == ShaderManager.Shader.Gradient) {
         effect.setUniformValue("alpha0", (Boolean)shaders.glow.getValue() ? -1.0F : (float)((ColorSetting)shaders.outlineColor.getValue()).getAlpha() / 255.0F);
         effect.setUniformValue("alpha1", (float)(Integer)shaders.fillAlpha.getValue() / 255.0F);
         effect.setUniformValue("alpha2", (float)(Integer)shaders.alpha2.getValue() / 255.0F);
         effect.setUniformValue("lineWidth", (Integer)shaders.lineWidth.getValue());
         effect.setUniformValue("oct", (Integer)shaders.octaves.getValue());
         effect.setUniformValue("quality", (Integer)shaders.quality.getValue());
         effect.setUniformValue("factor", (Float)shaders.factor.getValue());
         effect.setUniformValue("moreGradient", (Float)shaders.gradient.getValue());
         effect.setUniformValue("resolution", (float)mc.method_22683().method_4486(), (float)mc.method_22683().method_4502());
         effect.setUniformValue("time", this.time);
         effect.render(Render3DEngine.getTickDelta());
         this.time += 0.008F;
      } else if (shader == ShaderManager.Shader.Smoke) {
         effect.setUniformValue("alpha0", (Boolean)shaders.glow.getValue() ? -1.0F : (float)((ColorSetting)shaders.outlineColor.getValue()).getAlpha() / 255.0F);
         effect.setUniformValue("alpha1", (float)(Integer)shaders.fillAlpha.getValue() / 255.0F);
         effect.setUniformValue("lineWidth", (Integer)shaders.lineWidth.getValue());
         effect.setUniformValue("quality", (Integer)shaders.quality.getValue());
         effect.setUniformValue("first", ((ColorSetting)shaders.outlineColor.getValue()).getGlRed(), ((ColorSetting)shaders.outlineColor.getValue()).getGlGreen(), ((ColorSetting)shaders.outlineColor.getValue()).getGlBlue(), ((ColorSetting)shaders.outlineColor.getValue()).getGlAlpha());
         effect.setUniformValue("second", ((ColorSetting)shaders.outlineColor1.getValue()).getGlRed(), ((ColorSetting)shaders.outlineColor1.getValue()).getGlGreen(), ((ColorSetting)shaders.outlineColor1.getValue()).getGlBlue());
         effect.setUniformValue("third", ((ColorSetting)shaders.outlineColor2.getValue()).getGlRed(), ((ColorSetting)shaders.outlineColor2.getValue()).getGlGreen(), ((ColorSetting)shaders.outlineColor2.getValue()).getGlBlue());
         effect.setUniformValue("ffirst", ((ColorSetting)shaders.fillColor1.getValue()).getGlRed(), ((ColorSetting)shaders.fillColor1.getValue()).getGlGreen(), ((ColorSetting)shaders.fillColor1.getValue()).getGlBlue(), ((ColorSetting)shaders.fillColor1.getValue()).getGlAlpha());
         effect.setUniformValue("fsecond", ((ColorSetting)shaders.fillColor2.getValue()).getGlRed(), ((ColorSetting)shaders.fillColor2.getValue()).getGlGreen(), ((ColorSetting)shaders.fillColor2.getValue()).getGlBlue());
         effect.setUniformValue("fthird", ((ColorSetting)shaders.fillColor3.getValue()).getGlRed(), ((ColorSetting)shaders.fillColor3.getValue()).getGlGreen(), ((ColorSetting)shaders.fillColor3.getValue()).getGlBlue());
         effect.setUniformValue("oct", (Integer)shaders.octaves.getValue());
         effect.setUniformValue("resolution", (float)mc.method_22683().method_4486(), (float)mc.method_22683().method_4502());
         effect.setUniformValue("time", this.time);
         effect.render(Render3DEngine.getTickDelta());
         this.time += 0.008F;
      } else if (shader == ShaderManager.Shader.Default) {
         effect.setUniformValue("alpha0", (Boolean)shaders.glow.getValue() ? -1.0F : (float)((ColorSetting)shaders.outlineColor.getValue()).getAlpha() / 255.0F);
         effect.setUniformValue("lineWidth", (Integer)shaders.lineWidth.getValue());
         effect.setUniformValue("quality", (Integer)shaders.quality.getValue());
         effect.setUniformValue("color", ((ColorSetting)shaders.fillColor1.getValue()).getGlRed(), ((ColorSetting)shaders.fillColor1.getValue()).getGlGreen(), ((ColorSetting)shaders.fillColor1.getValue()).getGlBlue(), ((ColorSetting)shaders.fillColor1.getValue()).getGlAlpha());
         effect.setUniformValue("outlinecolor", ((ColorSetting)shaders.outlineColor.getValue()).getGlRed(), ((ColorSetting)shaders.outlineColor.getValue()).getGlGreen(), ((ColorSetting)shaders.outlineColor.getValue()).getGlBlue(), ((ColorSetting)shaders.outlineColor.getValue()).getGlAlpha());
         effect.render(Render3DEngine.getTickDelta());
      } else if (shader == ShaderManager.Shader.Snow) {
         effect.setUniformValue("color", ((ColorSetting)shaders.fillColor1.getValue()).getGlRed(), ((ColorSetting)shaders.fillColor1.getValue()).getGlGreen(), ((ColorSetting)shaders.fillColor1.getValue()).getGlBlue(), ((ColorSetting)shaders.fillColor1.getValue()).getGlAlpha());
         effect.setUniformValue("quality", (Integer)shaders.quality.getValue());
         effect.setUniformValue("resolution", (float)mc.method_22683().method_4486(), (float)mc.method_22683().method_4502());
         effect.setUniformValue("time", this.time);
         effect.render(Render3DEngine.getTickDelta());
         this.time += 0.008F;
      } else if (shader == ShaderManager.Shader.Fade) {
         effect.setUniformValue("alpha0", (Boolean)shaders.glow.getValue() ? -1.0F : (float)((ColorSetting)shaders.outlineColor.getValue()).getAlpha() / 255.0F);
         effect.setUniformValue("fillAlpha", (float)(Integer)shaders.fillAlpha.getValue() / 255.0F);
         effect.setUniformValue("lineWidth", (Integer)shaders.lineWidth.getValue());
         effect.setUniformValue("quality", (Integer)shaders.quality.getValue());
         effect.setUniformValue("outlinecolor", ((ColorSetting)shaders.outlineColor.getValue()).getGlRed(), ((ColorSetting)shaders.outlineColor.getValue()).getGlGreen(), ((ColorSetting)shaders.outlineColor.getValue()).getGlBlue(), ((ColorSetting)shaders.outlineColor.getValue()).getGlAlpha());
         effect.setUniformValue("primaryColor", ((ColorSetting)shaders.fillColor1.getValue()).getGlRed(), ((ColorSetting)shaders.fillColor1.getValue()).getGlGreen(), ((ColorSetting)shaders.fillColor1.getValue()).getGlBlue(), (float)((ColorSetting)shaders.fillColor1.getValue()).getAlpha());
         effect.setUniformValue("secondaryColor", ((ColorSetting)shaders.fillColor2.getValue()).getGlRed(), ((ColorSetting)shaders.fillColor2.getValue()).getGlGreen(), ((ColorSetting)shaders.fillColor2.getValue()).getGlBlue(), (float)((ColorSetting)shaders.fillColor1.getValue()).getAlpha());
         effect.setUniformValue("time", (float)(System.currentTimeMillis() % 100000L) / 1000.0F);
         effect.render(Render3DEngine.getTickDelta());
      }

   }

   public void reloadShaders() {
      DEFAULT = ShaderEffectManager.getInstance().manage(class_2960.method_60655("thunderhack", "shaders/post/outline.json"));
      SMOKE = ShaderEffectManager.getInstance().manage(class_2960.method_60655("thunderhack", "shaders/post/smoke.json"));
      GRADIENT = ShaderEffectManager.getInstance().manage(class_2960.method_60655("thunderhack", "shaders/post/gradient.json"));
      SNOW = ShaderEffectManager.getInstance().manage(class_2960.method_60655("thunderhack", "shaders/post/snow.json"));
      FADE = ShaderEffectManager.getInstance().manage(class_2960.method_60655("thunderhack", "shaders/post/fade.json"));
      FADE_OUTLINE = ShaderEffectManager.getInstance().manage(class_2960.method_60655("thunderhack", "shaders/post/fade.json"), (managedShaderEffect) -> {
         class_279 effect = managedShaderEffect.getShaderEffect();
         if (effect != null) {
            ((IShaderEffect)effect).addFakeTargetHook("bufIn", mc.field_1769.method_22990());
            ((IShaderEffect)effect).addFakeTargetHook("bufOut", mc.field_1769.method_22990());
         }
      });
      DEFAULT_OUTLINE = ShaderEffectManager.getInstance().manage(class_2960.method_60655("thunderhack", "shaders/post/outline.json"), (managedShaderEffect) -> {
         class_279 effect = managedShaderEffect.getShaderEffect();
         if (effect != null) {
            ((IShaderEffect)effect).addFakeTargetHook("bufIn", mc.field_1769.method_22990());
            ((IShaderEffect)effect).addFakeTargetHook("bufOut", mc.field_1769.method_22990());
         }
      });
      SMOKE_OUTLINE = ShaderEffectManager.getInstance().manage(class_2960.method_60655("thunderhack", "shaders/post/smoke.json"), (managedShaderEffect) -> {
         class_279 effect = managedShaderEffect.getShaderEffect();
         if (effect != null) {
            ((IShaderEffect)effect).addFakeTargetHook("bufIn", mc.field_1769.method_22990());
            ((IShaderEffect)effect).addFakeTargetHook("bufOut", mc.field_1769.method_22990());
         }
      });
      GRADIENT_OUTLINE = ShaderEffectManager.getInstance().manage(class_2960.method_60655("thunderhack", "shaders/post/gradient.json"), (managedShaderEffect) -> {
         class_279 effect = managedShaderEffect.getShaderEffect();
         if (effect != null) {
            ((IShaderEffect)effect).addFakeTargetHook("bufIn", mc.field_1769.method_22990());
            ((IShaderEffect)effect).addFakeTargetHook("bufOut", mc.field_1769.method_22990());
         }
      });
      SNOW_OUTLINE = ShaderEffectManager.getInstance().manage(class_2960.method_60655("thunderhack", "shaders/post/snow.json"), (managedShaderEffect) -> {
         class_279 effect = managedShaderEffect.getShaderEffect();
         if (effect != null) {
            ((IShaderEffect)effect).addFakeTargetHook("bufIn", mc.field_1769.method_22990());
            ((IShaderEffect)effect).addFakeTargetHook("bufOut", mc.field_1769.method_22990());
         }
      });
   }

   public boolean fullNullCheck() {
      if (GRADIENT != null && SMOKE != null && DEFAULT != null) {
         return false;
      } else {
         this.shaderBuffer = new ShaderManager.ThunderHackFramebuffer(mc.method_1522().field_1482, mc.method_1522().field_1481);
         this.reloadShaders();
         return true;
      }
   }

   public static record RenderTask(Runnable task, ShaderManager.Shader shader) {
      public RenderTask(Runnable task, ShaderManager.Shader shader) {
         this.task = task;
         this.shader = shader;
      }

      public Runnable task() {
         return this.task;
      }

      public ShaderManager.Shader shader() {
         return this.shader;
      }
   }

   public static enum Shader {
      Default,
      Smoke,
      Gradient,
      Snow,
      Fade;

      // $FF: synthetic method
      private static ShaderManager.Shader[] $values() {
         return new ShaderManager.Shader[]{Default, Smoke, Gradient, Snow, Fade};
      }
   }

   public static class ThunderHackFramebuffer extends class_276 {
      public ThunderHackFramebuffer(int width, int height) {
         super(false);
         RenderSystem.assertOnRenderThreadOrInit();
         this.method_1234(width, height, true);
         this.method_1236(0.0F, 0.0F, 0.0F, 0.0F);
      }
   }
}
