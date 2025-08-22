package tsunami.injection;

import net.minecraft.class_1299;
import net.minecraft.class_1429;
import net.minecraft.class_1496;
import net.minecraft.class_1937;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tsunami.core.manager.client.ModuleManager;

@Mixin({class_1496.class})
public abstract class MixinAbstractHorseEntity extends class_1429 {
   protected MixinAbstractHorseEntity(class_1299<? extends class_1429> entityType, class_1937 world) {
      super(entityType, world);
   }

   @Inject(
      method = {"isSaddled"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void onIsSaddled(CallbackInfoReturnable<Boolean> cir) {
      if (ModuleManager.entityControl.isEnabled()) {
         cir.setReturnValue(true);
      }

   }
}
