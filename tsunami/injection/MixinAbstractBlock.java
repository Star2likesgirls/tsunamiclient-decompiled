package tsunami.injection;

import net.minecraft.class_1657;
import net.minecraft.class_1799;
import net.minecraft.class_1890;
import net.minecraft.class_1893;
import net.minecraft.class_1922;
import net.minecraft.class_2338;
import net.minecraft.class_2680;
import net.minecraft.class_4970;
import net.minecraft.class_6880;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tsunami.core.manager.IManager;
import tsunami.core.manager.client.ModuleManager;
import tsunami.features.modules.player.AutoTool;

@Mixin({class_4970.class})
public abstract class MixinAbstractBlock {
   @Inject(
      method = {"calcBlockBreakingDelta"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void calcBlockBreakingDeltaHook(class_2680 state, class_1657 player, class_1922 world, class_2338 pos, CallbackInfoReturnable<Float> ci) {
      if (ModuleManager.autoTool.isEnabled() && (Boolean)AutoTool.silent.getValue()) {
         float f = state.method_26214(world, pos);
         if (f < 0.0F) {
            ci.setReturnValue(0.0F);
         } else {
            float dig_speed = this.getDigSpeed(state, player.method_31548().method_5438(AutoTool.itemIndex)) / f;
            ci.setReturnValue(player.method_31548().method_5438(AutoTool.itemIndex).method_7951(state) ? dig_speed / 30.0F : dig_speed / 100.0F);
         }
      }

   }

   public float getDigSpeed(class_2680 state, class_1799 stack) {
      double str = (double)stack.method_7924(state);
      int effect = class_1890.method_8225((class_6880)IManager.mc.field_1687.method_30349().method_30530(class_1893.field_9131.method_58273()).method_40264(class_1893.field_9131).get(), stack);
      return (float)Math.max(str + (str > 1.0D ? (double)(effect * effect) + 1.0D : 0.0D), 0.0D);
   }
}
