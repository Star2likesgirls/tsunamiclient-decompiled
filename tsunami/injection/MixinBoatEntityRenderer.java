package tsunami.injection;

import net.minecraft.class_1690;
import net.minecraft.class_4587;
import net.minecraft.class_4597;
import net.minecraft.class_881;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tsunami.core.manager.client.ModuleManager;

@Mixin({class_881.class})
public class MixinBoatEntityRenderer {
   @Inject(
      method = {"render(Lnet/minecraft/entity/vehicle/BoatEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/client/util/math/MatrixStack;scale(FFF)V",
   shift = Shift.AFTER
)}
   )
   public void render(class_1690 boatEntity, float f, float g, class_4587 matrixStack, class_4597 vertexConsumerProvider, int i, CallbackInfo ci) {
      if (ModuleManager.boatFly.isEnabled() && (Boolean)ModuleManager.boatFly.hideBoat.getValue()) {
         matrixStack.method_22905(0.3F, 0.3F, 0.3F);
      }

   }
}
