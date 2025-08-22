package tsunami.injection;

import net.minecraft.class_1511;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tsunami.utility.interfaces.ICrystal;

@Mixin({class_1511.class})
public class MixinEndCrystal implements ICrystal {
   @Unique
   int attacks;
   @Unique
   int cooldown;

   public boolean canAttack() {
      return this.cooldown == 0;
   }

   public void attack() {
      if (this.attacks++ >= 5) {
         this.cooldown = 20;
      }

   }

   @Inject(
      method = {"tick"},
      at = {@At("HEAD")}
   )
   public void tickHook(CallbackInfo ci) {
      if (this.cooldown > 0) {
         --this.cooldown;
      }

   }
}
