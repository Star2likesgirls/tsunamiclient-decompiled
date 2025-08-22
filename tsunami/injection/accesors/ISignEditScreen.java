package tsunami.injection.accesors;

import net.minecraft.class_2625;
import net.minecraft.class_7743;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({class_7743.class})
public interface ISignEditScreen {
   @Accessor("blockEntity")
   class_2625 getBlockEntity();

   @Accessor("front")
   boolean isFront();
}
