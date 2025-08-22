package tsunami.injection;

import net.minecraft.class_1297;
import net.minecraft.class_1937;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_2560;
import net.minecraft.class_2680;
import net.minecraft.class_2846;
import net.minecraft.class_2846.class_2847;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tsunami.core.manager.IManager;
import tsunami.core.manager.client.ModuleManager;
import tsunami.features.modules.movement.AntiWeb;
import tsunami.utility.player.InteractionUtility;

@Mixin({class_2560.class})
public class MixinCobwebBlock {
   @Inject(
      method = {"onEntityCollision"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void onEntityCollisionHook(class_2680 state, class_1937 world, class_2338 pos, class_1297 entity, CallbackInfo ci) {
      if (ModuleManager.antiWeb.isEnabled() && AntiWeb.mode.getValue() == AntiWeb.Mode.Ignore && entity == IManager.mc.field_1724) {
         ci.cancel();
         if ((Boolean)AntiWeb.grim.getValue()) {
            InteractionUtility.sendSequencedPacket((id) -> {
               return new class_2846(class_2847.field_12973, pos, class_2350.field_11036, id);
            });
         }
      }

   }
}
