package tsunami.injection;

import net.minecraft.class_2960;
import net.minecraft.class_8685;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tsunami.core.manager.client.ModuleManager;
import tsunami.features.modules.client.Media;
import tsunami.utility.render.TextureStorage;

@Mixin({class_8685.class})
public class MixinSkinTextures {
   @Inject(
      method = {"texture"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void getSkinTextureHook(CallbackInfoReturnable<class_2960> cir) {
      if (ModuleManager.media.isEnabled() && (Boolean)Media.skinProtect.getValue()) {
         cir.setReturnValue(TextureStorage.sunRiseSkin);
      }

   }
}
