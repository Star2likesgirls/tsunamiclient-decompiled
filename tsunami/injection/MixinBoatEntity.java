package tsunami.injection;

import net.minecraft.class_1297;
import net.minecraft.class_1690;
import net.minecraft.class_1297.class_4738;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tsunami.core.manager.client.ModuleManager;

@Mixin({class_1690.class})
public class MixinBoatEntity {
   @Unique
   private float prevYaw;
   @Unique
   private float prevHeadYaw;

   @Inject(
      method = {"updatePassengerPosition"},
      at = {@At("HEAD")}
   )
   protected void updatePassengerPositionHookPre(class_1297 passenger, class_4738 positionUpdater, CallbackInfo ci) {
      if (ModuleManager.boatFly.isEnabled()) {
         this.prevYaw = passenger.method_36454();
         this.prevHeadYaw = passenger.method_5791();
      }

   }

   @Inject(
      method = {"updatePassengerPosition"},
      at = {@At("RETURN")}
   )
   protected void updatePassengerPositionHookPost(class_1297 passenger, class_4738 positionUpdater, CallbackInfo ci) {
      if (ModuleManager.boatFly.isEnabled()) {
         passenger.method_36456(this.prevYaw);
         passenger.method_5847(this.prevHeadYaw);
      }

   }
}
