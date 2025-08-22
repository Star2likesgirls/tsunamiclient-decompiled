package tsunami.injection;

import java.util.List;
import net.minecraft.class_1799;
import net.minecraft.class_1836;
import net.minecraft.class_2480;
import net.minecraft.class_2561;
import net.minecraft.class_1792.class_9635;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tsunami.core.manager.client.ModuleManager;
import tsunami.features.modules.render.Tooltips;

@Mixin({class_2480.class})
public class MixinShulkerBoxBlock {
   @Inject(
      method = {"appendTooltip"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onAppendTooltip(class_1799 stack, class_9635 context, List<class_2561> tooltip, class_1836 options, CallbackInfo ci) {
      if (ModuleManager.tooltips != null) {
         if ((Boolean)Tooltips.storage.getValue()) {
            ci.cancel();
         }

      }
   }
}
