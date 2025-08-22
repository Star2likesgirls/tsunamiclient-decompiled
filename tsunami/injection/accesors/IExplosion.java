package tsunami.injection.accesors;

import net.minecraft.class_1282;
import net.minecraft.class_1297;
import net.minecraft.class_1927;
import net.minecraft.class_1937;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({class_1927.class})
public interface IExplosion {
   @Mutable
   @Accessor("x")
   void setX(double var1);

   @Mutable
   @Accessor("y")
   void setY(double var1);

   @Mutable
   @Accessor("z")
   void setZ(double var1);

   @Mutable
   @Accessor("entity")
   void setEntity(class_1297 var1);

   @Mutable
   @Accessor("world")
   void setWorld(class_1937 var1);

   @Mutable
   @Accessor("world")
   class_1937 getWorld();

   @Mutable
   @Accessor("damageSource")
   class_1282 getDamageSource();
}
