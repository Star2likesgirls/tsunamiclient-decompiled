package tsunami.injection.accesors;

import net.minecraft.class_2561;
import net.minecraft.class_7439;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({class_7439.class})
public interface IGameMessageS2CPacket {
   @Mutable
   @Accessor("content")
   void setContent(class_2561 var1);
}
