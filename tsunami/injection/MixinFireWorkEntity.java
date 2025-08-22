package tsunami.injection;

import net.minecraft.class_1309;
import net.minecraft.class_1671;
import net.minecraft.class_243;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import tsunami.core.Managers;
import tsunami.core.manager.IManager;
import tsunami.core.manager.client.ModuleManager;
import tsunami.features.modules.combat.Aura;

@Mixin({class_1671.class})
public class MixinFireWorkEntity {
   @Shadow
   private class_1309 field_7616;

   @Redirect(
      method = {"tick"},
      at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/entity/LivingEntity;getRotationVector()Lnet/minecraft/util/math/Vec3d;"
)
   )
   private class_243 tickHook(class_1309 instance) {
      if (ModuleManager.aura.isEnabled() && ModuleManager.aura.rotationMode.not(Aura.Mode.None)) {
         Aura var10000 = ModuleManager.aura;
         if (Aura.target != null && this.field_7616 == IManager.mc.field_1724 && (Boolean)ModuleManager.aura.elytraTarget.getValue()) {
            return Managers.PLAYER.getRotationVector(ModuleManager.aura.rotationPitch, ModuleManager.aura.rotationYaw);
         }
      }

      return this.field_7616.method_5720();
   }
}
