package tsunami.injection.accesors;

import net.minecraft.class_2664;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({class_2664.class})
public interface IExplosionS2CPacket {
   @Mutable
   @Accessor("playerVelocityX")
   void setMotionX(float var1);

   @Mutable
   @Accessor("playerVelocityY")
   void setMotionY(float var1);

   @Mutable
   @Accessor("playerVelocityZ")
   void setMotionZ(float var1);

   @Accessor("playerVelocityX")
   float getMotionX();

   @Accessor("playerVelocityY")
   float getMotionY();

   @Accessor("playerVelocityZ")
   float getMotionZ();
}
