package tsunami.injection.accesors;

import net.minecraft.class_2743;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({class_2743.class})
public interface ISPacketEntityVelocity {
   @Mutable
   @Accessor("velocityX")
   void setMotionX(int var1);

   @Mutable
   @Accessor("velocityY")
   void setMotionY(int var1);

   @Mutable
   @Accessor("velocityZ")
   void setMotionZ(int var1);
}
