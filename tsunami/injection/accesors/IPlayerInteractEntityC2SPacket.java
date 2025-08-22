package tsunami.injection.accesors;

import net.minecraft.class_2540;
import net.minecraft.class_2824;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin({class_2824.class})
public interface IPlayerInteractEntityC2SPacket {
   @Invoker("write")
   void write(class_2540 var1);
}
