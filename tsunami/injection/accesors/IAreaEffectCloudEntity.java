package tsunami.injection.accesors;

import net.minecraft.class_1295;
import net.minecraft.class_1844;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({class_1295.class})
public interface IAreaEffectCloudEntity {
   @Accessor("potionContentsComponent")
   class_1844 getPotionContentsComponent();
}
