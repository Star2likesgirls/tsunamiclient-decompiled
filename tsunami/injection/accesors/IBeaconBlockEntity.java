package tsunami.injection.accesors;

import net.minecraft.class_2580;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({class_2580.class})
public interface IBeaconBlockEntity {
   @Accessor("level")
   int getLevel();
}
