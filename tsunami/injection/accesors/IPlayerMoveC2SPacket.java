package tsunami.injection.accesors;

import net.minecraft.class_2828;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({class_2828.class})
public interface IPlayerMoveC2SPacket {
   @Mutable
   @Accessor("onGround")
   void setOnGround(boolean var1);

   @Mutable
   @Accessor("y")
   void setY(double var1);
}
