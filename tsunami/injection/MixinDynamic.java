package tsunami.injection;

import net.minecraft.class_9779.class_9781;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tsunami.TsunamiClient;

@Mixin({class_9781.class})
public class MixinDynamic {
   @Shadow
   private float field_51958;
   @Shadow
   private float field_51959;
   @Shadow
   private long field_51962;
   @Final
   @Shadow
   private float field_51964;

   @Inject(
      method = {"Lnet/minecraft/client/render/RenderTickCounter$Dynamic;beginRenderTick(J)I"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void beginRenderTickHook(long timeMillis, CallbackInfoReturnable<Integer> cir) {
      if (TsunamiClient.TICK_TIMER != 1.0F) {
         this.field_51958 = (float)(timeMillis - this.field_51962) / this.field_51964 * TsunamiClient.TICK_TIMER;
         this.field_51962 = timeMillis;
         this.field_51959 += this.field_51958;
         int i = (int)this.field_51959;
         this.field_51959 -= (float)i;
         cir.setReturnValue(i);
      }
   }
}
