package tsunami.injection;

import net.minecraft.class_1937;
import net.minecraft.class_2338;
import net.minecraft.class_2680;
import net.minecraft.class_2818;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tsunami.TsunamiClient;
import tsunami.events.impl.EventSetBlockState;

@Mixin({class_2818.class})
public class MixinWorldChunk {
   @Shadow
   @Final
   class_1937 field_12858;

   @Inject(
      method = {"setBlockState"},
      at = {@At("RETURN")}
   )
   private void setBlockStateHook(class_2338 pos, class_2680 state, boolean moved, CallbackInfoReturnable<class_2680> cir) {
      if (this.field_12858.field_9236) {
         TsunamiClient.EVENT_BUS.post((Object)(new EventSetBlockState(pos, (class_2680)cir.getReturnValue(), state)));
      }

   }
}
