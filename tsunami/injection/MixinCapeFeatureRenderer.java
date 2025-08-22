package tsunami.injection;

import net.minecraft.class_3532;
import net.minecraft.class_4587;
import net.minecraft.class_4597;
import net.minecraft.class_742;
import net.minecraft.class_972;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin({class_972.class})
public class MixinCapeFeatureRenderer {
   @ModifyVariable(
      method = {"render"},
      at = @At("STORE"),
      ordinal = 6
   )
   private float renderHook(float n, class_4587 matrixStack, class_4597 vertexConsumerProvider, int i, class_742 abstractClientPlayerEntity, float f, float g, float h, float j, float k, float l) {
      return class_3532.method_16439(h, abstractClientPlayerEntity.field_6220, abstractClientPlayerEntity.field_6283);
   }
}
