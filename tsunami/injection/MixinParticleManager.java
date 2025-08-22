package tsunami.injection;

import net.minecraft.class_3937;
import net.minecraft.class_691;
import net.minecraft.class_700;
import net.minecraft.class_702;
import net.minecraft.class_703;
import net.minecraft.class_727;
import net.minecraft.class_677.class_678;
import net.minecraft.class_677.class_681;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tsunami.core.manager.client.ModuleManager;
import tsunami.features.modules.render.NoRender;

@Mixin({class_702.class})
public class MixinParticleManager {
   @Inject(
      at = {@At("HEAD")},
      method = {"addParticle(Lnet/minecraft/client/particle/Particle;)V"},
      cancellable = true
   )
   public void addParticleHook(class_703 p, CallbackInfo e) {
      NoRender nR = ModuleManager.noRender;
      if (nR.isEnabled()) {
         if ((Boolean)nR.elderGuardian.getValue() && p instanceof class_700) {
            e.cancel();
         }

         if ((Boolean)nR.explosions.getValue() && p instanceof class_691) {
            e.cancel();
         }

         if ((Boolean)nR.campFire.getValue() && p instanceof class_3937) {
            e.cancel();
         }

         if ((Boolean)nR.breakParticles.getValue() && p instanceof class_727) {
            e.cancel();
         }

         if ((Boolean)nR.fireworks.getValue() && (p instanceof class_681 || p instanceof class_678)) {
            e.cancel();
         }

      }
   }
}
