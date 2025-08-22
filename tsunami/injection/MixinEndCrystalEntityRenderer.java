package tsunami.injection;

import net.minecraft.class_1511;
import net.minecraft.class_4587;
import net.minecraft.class_4597;
import net.minecraft.class_630;
import net.minecraft.class_892;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tsunami.core.manager.client.ModuleManager;

@Mixin({class_892.class})
public class MixinEndCrystalEntityRenderer {
   @Shadow
   @Final
   private static float field_21002;
   @Shadow
   @Final
   private class_630 field_21003;
   @Shadow
   @Final
   private class_630 field_21004;

   @Inject(
      method = {"render(Lnet/minecraft/entity/decoration/EndCrystalEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void render(class_1511 endCrystalEntity, float f, float g, class_4587 matrixStack, class_4597 vertexConsumerProvider, int i, CallbackInfo ci) {
      if (ModuleManager.chams.isEnabled() && (Boolean)ModuleManager.chams.crystals.getValue()) {
         ci.cancel();
         ModuleManager.chams.renderCrystal(endCrystalEntity, f, g, matrixStack, i, this.field_21003, this.field_21004);
      }

   }
}
