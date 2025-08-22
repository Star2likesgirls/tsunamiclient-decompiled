package tsunami.injection.accesors;

import net.minecraft.class_636;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin({class_636.class})
public interface IInteractionManager {
   @Accessor("currentBreakingProgress")
   float getCurBlockDamageMP();

   @Accessor("currentBreakingProgress")
   void setCurBlockDamageMP(float var1);

   @Invoker("syncSelectedSlot")
   void syncSlot();
}
