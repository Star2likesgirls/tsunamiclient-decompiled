package tsunami.injection.accesors;

import net.minecraft.class_276;
import net.minecraft.class_283;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({class_283.class})
public interface IPostProcessShader {
   @Mutable
   @Accessor("input")
   void setInput(class_276 var1);

   @Mutable
   @Accessor("output")
   void setOutput(class_276 var1);
}
