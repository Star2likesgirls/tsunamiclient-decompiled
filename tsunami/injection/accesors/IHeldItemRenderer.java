package tsunami.injection.accesors;

import net.minecraft.class_1799;
import net.minecraft.class_759;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({class_759.class})
public interface IHeldItemRenderer {
   @Accessor("equipProgressMainHand")
   void setEquippedProgressMainHand(float var1);

   @Accessor("equipProgressOffHand")
   void setEquippedProgressOffHand(float var1);

   @Accessor("equipProgressMainHand")
   float getEquippedProgressMainHand();

   @Accessor("equipProgressOffHand")
   float getEquippedProgressOffHand();

   @Accessor("mainHand")
   void setItemStackMainHand(class_1799 var1);

   @Accessor("offHand")
   void setItemStackOffHand(class_1799 var1);
}
