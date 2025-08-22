package tsunami.injection;

import net.minecraft.class_1922;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_2358;
import net.minecraft.class_2680;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tsunami.core.manager.IManager;
import tsunami.core.manager.client.ModuleManager;
import tsunami.features.modules.render.XRay;

@Mixin({class_2248.class})
public abstract class MixinBlock {
   @Inject(
      method = {"shouldDrawSide"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private static void shouldDrawSideHook(class_2680 state, class_1922 world, class_2338 pos, class_2350 side, class_2338 blockPos, CallbackInfoReturnable<Boolean> cir) {
      if (ModuleManager.xray.isEnabled() && (Boolean)ModuleManager.xray.wallHack.getValue()) {
         cir.setReturnValue(XRay.isCheckableOre(state.method_26204()));
      }

      if (ModuleManager.autoAnchor.isEnabled() && state.method_26204() instanceof class_2358) {
         cir.setReturnValue(false);
      }

   }

   @Inject(
      method = {"getVelocityMultiplier"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void getVelocityMultiplierHook(CallbackInfoReturnable<Float> cir) {
      if (ModuleManager.noSlow.isEnabled()) {
         if ((Boolean)ModuleManager.noSlow.soulSand.getValue() && this == class_2246.field_10114) {
            cir.setReturnValue(class_2246.field_10566.method_23349());
         }

         if ((Boolean)ModuleManager.noSlow.honey.getValue() && this == class_2246.field_21211) {
            cir.setReturnValue(class_2246.field_10566.method_23349());
         }
      }

   }

   @Inject(
      method = {"getSlipperiness"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void getSlipperinessHook(CallbackInfoReturnable<Float> cir) {
      if (ModuleManager.noSlow.isEnabled()) {
         if ((Boolean)ModuleManager.noSlow.slime.getValue() && this == class_2246.field_10030) {
            cir.setReturnValue(class_2246.field_10566.method_9499());
         }

         if ((Boolean)ModuleManager.noSlow.ice.getValue() && (this == class_2246.field_10295 || this == class_2246.field_10225 || this == class_2246.field_10384 || this == class_2246.field_10110) && !IManager.mc.field_1690.field_1903.method_1434()) {
            cir.setReturnValue(class_2246.field_10566.method_9499());
         }
      }

   }
}
