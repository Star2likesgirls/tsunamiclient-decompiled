package tsunami.injection;

import net.minecraft.class_1297;
import net.minecraft.class_1937;
import net.minecraft.class_2338;
import net.minecraft.class_2490;
import net.minecraft.class_2680;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tsunami.core.manager.client.ModuleManager;

@Mixin({class_2490.class})
public class MixinSlimeBlock {
   @Inject(
      method = {"onSteppedOn"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void onSteppedOnHook(class_1937 world, class_2338 pos, class_2680 state, class_1297 entity, CallbackInfo ci) {
      if (ModuleManager.noSlow.isEnabled() && (Boolean)ModuleManager.noSlow.slime.getValue()) {
         ci.cancel();
      }

   }
}
