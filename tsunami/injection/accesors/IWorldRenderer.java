package tsunami.injection.accesors;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.class_3191;
import net.minecraft.class_761;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({class_761.class})
public interface IWorldRenderer {
   @Accessor("blockBreakingInfos")
   Int2ObjectMap<class_3191> getBlockBreakingInfos();
}
