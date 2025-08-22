package tsunami.injection;

import net.minecraft.class_1297;
import net.minecraft.class_1937;
import net.minecraft.class_2338;
import net.minecraft.class_2680;
import net.minecraft.class_3830;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tsunami.core.manager.client.ModuleManager;

@Mixin({class_3830.class})
public class MixinSweetBerryBushBlock {
   @Inject(
      method = {"onEntityCollision"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void onEntityCollisionHook(class_2680 state, class_1937 world, class_2338 pos, class_1297 entity, CallbackInfo ci) {
      if (ModuleManager.noSlow.isEnabled() && (Boolean)ModuleManager.noSlow.sweetBerryBush.getValue()) {
         ci.cancel();
      }

   }
}
