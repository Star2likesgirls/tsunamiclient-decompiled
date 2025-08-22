package tsunami.injection;

import net.minecraft.class_1922;
import net.minecraft.class_2338;
import net.minecraft.class_2680;
import net.minecraft.class_5329;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import tsunami.TsunamiClient;
import tsunami.core.manager.client.ModuleManager;
import tsunami.events.impl.EventCollision;

@Mixin(
   value = {class_5329.class},
   priority = 800
)
public abstract class MixinBlockCollisionSpliterator {
   @Redirect(
      method = {"computeNext"},
      at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/world/BlockView;getBlockState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/BlockState;"
)
   )
   private class_2680 computeNextHook(class_1922 instance, class_2338 blockPos) {
      if (!ModuleManager.antiWeb.isEnabled() && !ModuleManager.phase.isEnabled() && !ModuleManager.jesus.isEnabled()) {
         return instance.method_8320(blockPos);
      } else {
         EventCollision event = new EventCollision(instance.method_8320(blockPos), blockPos);
         TsunamiClient.EVENT_BUS.post((Object)event);
         return event.getState();
      }
   }
}
