package tsunami.injection;

import net.minecraft.class_2874;
import net.minecraft.class_3532;
import net.minecraft.class_765;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tsunami.core.manager.client.ModuleManager;
import tsunami.features.modules.render.Fullbright;

@Mixin({class_765.class})
public class MixinLightmapTextureManager {
   @Inject(
      method = {"getDarknessFactor(F)F"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void getDarknessFactor(float tickDelta, CallbackInfoReturnable<Float> info) {
      if (ModuleManager.noRender.isEnabled() && (Boolean)ModuleManager.noRender.darkness.getValue()) {
         info.setReturnValue(0.0F);
      }

   }

   @Inject(
      method = {"getBrightness"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private static void getBrightnessHook(class_2874 type, int lightLevel, CallbackInfoReturnable<Float> cir) {
      if (ModuleManager.fullbright.isEnabled()) {
         float f = (float)lightLevel / 15.0F;
         float g = f / (4.0F - 3.0F * f);
         cir.setReturnValue(Math.max(class_3532.method_16439(type.comp_656(), g, 1.0F), (Float)Fullbright.minBright.getValue()));
      }

   }
}
