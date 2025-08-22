package tsunami.injection;

import net.minecraft.class_1937;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_2680;
import net.minecraft.class_2818;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tsunami.features.modules.Module;
import tsunami.utility.world.ExplosionUtility;

@Mixin({class_1937.class})
public abstract class MixinWorld {
   @Inject(
      method = {"getBlockState"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void blockStateHook(class_2338 pos, CallbackInfoReturnable<class_2680> cir) {
      if (ExplosionUtility.terrainIgnore && Module.mc.field_1687 != null && !Module.mc.field_1687.method_24794(pos)) {
         class_2818 worldChunk = Module.mc.field_1687.method_8497(pos.method_10263() >> 4, pos.method_10260() >> 4);
         class_2680 tempState = worldChunk.method_8320(pos);
         if (tempState.method_26204() == class_2246.field_10540 || tempState.method_26204() == class_2246.field_9987 || tempState.method_26204() == class_2246.field_10443 || tempState.method_26204() == class_2246.field_23152) {
            return;
         }

         cir.setReturnValue(class_2246.field_10124.method_9564());
      }

   }
}
