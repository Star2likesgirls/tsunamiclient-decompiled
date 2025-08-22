package tsunami.injection;

import net.minecraft.class_279;
import net.minecraft.class_761;
import net.minecraft.class_765;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tsunami.core.Managers;
import tsunami.core.manager.client.ModuleManager;
import tsunami.core.manager.client.ShaderManager;
import tsunami.features.modules.Module;

@Mixin({class_761.class})
public abstract class MixinWorldRenderer {
   @ModifyArg(
      method = {"render"},
      at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/client/render/WorldRenderer;setupTerrain(Lnet/minecraft/client/render/Camera;Lnet/minecraft/client/render/Frustum;ZZ)V"
),
      index = 3
   )
   private boolean renderSetupTerrainModifyArg(boolean spectator) {
      return ModuleManager.freeCam.isEnabled() || spectator;
   }

   @Redirect(
      method = {"render"},
      at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/client/gl/PostEffectProcessor;render(F)V",
   ordinal = 0
)
   )
   private void replaceShaderHook(class_279 instance, float tickDelta) {
      ShaderManager.Shader shaders = (ShaderManager.Shader)ModuleManager.shaders.mode.getValue();
      if (ModuleManager.shaders.isEnabled() && Module.mc.field_1687 != null) {
         if (Managers.SHADER.fullNullCheck()) {
            return;
         }

         Managers.SHADER.setupShader(shaders, Managers.SHADER.getShaderOutline(shaders));
      } else {
         instance.method_1258(tickDelta);
      }

   }

   @Inject(
      method = {"renderWeather"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void renderWeatherHook(class_765 manager, float tickDelta, double cameraX, double cameraY, double cameraZ, CallbackInfo ci) {
      if (ModuleManager.noRender.isEnabled() && (Boolean)ModuleManager.noRender.noWeather.getValue()) {
         ci.cancel();
      }

   }
}
