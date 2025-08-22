package tsunami.injection.accesors;

import net.minecraft.class_4604;
import org.joml.FrustumIntersection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({class_4604.class})
public interface IFrustum {
   @Accessor
   FrustumIntersection getFrustumIntersection();

   @Accessor("x")
   double getX();

   @Accessor("x")
   void setX(double var1);

   @Accessor("y")
   double getY();

   @Accessor("y")
   void setY(double var1);

   @Accessor("z")
   double getZ();

   @Accessor("z")
   void setZ(double var1);
}
