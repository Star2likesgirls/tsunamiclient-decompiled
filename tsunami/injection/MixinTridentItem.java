package tsunami.injection;

import net.minecraft.class_1268;
import net.minecraft.class_1271;
import net.minecraft.class_1309;
import net.minecraft.class_1657;
import net.minecraft.class_1799;
import net.minecraft.class_1835;
import net.minecraft.class_1890;
import net.minecraft.class_1937;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tsunami.TsunamiClient;
import tsunami.core.manager.client.ModuleManager;
import tsunami.events.impl.UseTridentEvent;

@Mixin({class_1835.class})
public abstract class MixinTridentItem {
   @Inject(
      method = {"onStoppedUsing"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void onStoppedUsingHook(class_1799 stack, class_1937 world, class_1309 user, int remainingUseTicks, CallbackInfo ci) {
      if (user == TsunamiClient.mc.field_1724 && class_1890.method_60123(stack, TsunamiClient.mc.field_1724) > 0.0F) {
         UseTridentEvent e = new UseTridentEvent();
         TsunamiClient.EVENT_BUS.post((Object)e);
         if (e.isCancelled()) {
            ci.cancel();
         }
      }

   }

   @Inject(
      method = {"use"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void useHook(class_1937 world, class_1657 user, class_1268 hand, CallbackInfoReturnable<class_1271<class_1799>> cir) {
      class_1799 itemStack = user.method_5998(hand);
      if (class_1890.method_60123(itemStack, user) > 0.0F && !user.method_5721() && ModuleManager.tridentBoost.isEnabled() && (Boolean)ModuleManager.tridentBoost.anyWeather.getValue()) {
         user.method_6019(hand);
         cir.setReturnValue(class_1271.method_22428(itemStack));
      }

   }
}
