package tsunami.injection;

import net.minecraft.class_743;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tsunami.TsunamiClient;
import tsunami.events.impl.EventKeyboardInput;
import tsunami.features.modules.Module;

@Mixin({class_743.class})
public class MixinKeyboardInput {
   @Inject(
      method = {"tick"},
      at = {@At(
   value = "FIELD",
   target = "Lnet/minecraft/client/input/KeyboardInput;sneaking:Z",
   shift = Shift.BEFORE
)},
      cancellable = true
   )
   private void onSneak(boolean slowDown, float slowDownFactor, CallbackInfo ci) {
      if (!Module.fullNullCheck()) {
         EventKeyboardInput event = new EventKeyboardInput();
         TsunamiClient.EVENT_BUS.post((Object)event);
         if (event.isCancelled()) {
            ci.cancel();
         }

      }
   }
}
