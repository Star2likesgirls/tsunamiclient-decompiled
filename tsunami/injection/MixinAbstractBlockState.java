package tsunami.injection;

import net.minecraft.class_4970.class_4971;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tsunami.core.manager.client.ModuleManager;

@Mixin({class_4971.class})
public class MixinAbstractBlockState {
   @Inject(
      method = {"getLuminance"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void getLuminanceHook(CallbackInfoReturnable<Integer> cir) {
      if (ModuleManager.xray.isEnabled()) {
         cir.setReturnValue(15);
      }

   }
}
