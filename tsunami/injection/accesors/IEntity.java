package tsunami.injection.accesors;

import net.minecraft.class_1297;
import net.minecraft.class_2338;
import net.minecraft.class_243;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({class_1297.class})
public interface IEntity {
   @Mutable
   @Accessor("pos")
   void setPos(class_243 var1);

   @Mutable
   @Accessor("blockPos")
   void setBlockPos(class_2338 var1);
}
