package tsunami.injection.accesors;

import net.minecraft.class_2708;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({class_2708.class})
public interface ISPacketPlayerPosLook {
   @Accessor("yaw")
   void setYaw(float var1);

   @Accessor("pitch")
   void setPitch(float var1);
}
