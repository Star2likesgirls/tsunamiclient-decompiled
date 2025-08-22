package tsunami.injection;

import net.minecraft.class_312;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tsunami.TsunamiClient;
import tsunami.core.Managers;
import tsunami.events.impl.EventMouse;
import tsunami.features.modules.Module;

@Mixin({class_312.class})
public class MixinMouse {
   @Inject(
      method = {"onMouseButton"},
      at = {@At("HEAD")}
   )
   public void onMouseButtonHook(long window, int button, int action, int mods, CallbackInfo ci) {
      if (window == Module.mc.method_22683().method_4490()) {
         if (action == 0) {
            Managers.MODULE.onMoseKeyReleased(button);
         }

         if (action == 1) {
            Managers.MODULE.onMoseKeyPressed(button);
         }

         TsunamiClient.EVENT_BUS.post((Object)(new EventMouse(button, action)));
      }

   }

   @Inject(
      method = {"onMouseScroll"},
      at = {@At("HEAD")}
   )
   private void onMouseScrollHook(long window, double horizontal, double vertical, CallbackInfo ci) {
      if (window == Module.mc.method_22683().method_4490()) {
         TsunamiClient.EVENT_BUS.post((Object)(new EventMouse((int)vertical, 2)));
      }

   }
}
