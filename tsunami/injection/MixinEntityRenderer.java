package tsunami.injection;

import net.minecraft.class_1297;
import net.minecraft.class_1531;
import net.minecraft.class_1657;
import net.minecraft.class_2561;
import net.minecraft.class_4587;
import net.minecraft.class_4597;
import net.minecraft.class_897;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tsunami.core.manager.client.ModuleManager;

@Mixin({class_897.class})
public abstract class MixinEntityRenderer<T extends class_1297> {
   @Inject(
      method = {"renderLabelIfPresent"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void renderLabelIfPresent(T entity, class_2561 text, class_4587 matrices, class_4597 vertexConsumers, int light, float tickDelta, CallbackInfo info) {
      if (entity instanceof class_1531 && ModuleManager.noRender.isEnabled() && (Boolean)ModuleManager.noRender.noArmorStands.getValue()) {
         info.cancel();
      }

      if (entity instanceof class_1657 && ModuleManager.nameTags.isEnabled()) {
         info.cancel();
      }

   }
}
