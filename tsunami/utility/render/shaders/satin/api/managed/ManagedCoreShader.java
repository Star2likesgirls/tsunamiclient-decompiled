package tsunami.utility.render.shaders.satin.api.managed;

import net.minecraft.class_5944;
import tsunami.utility.render.shaders.satin.api.managed.uniform.UniformFinder;

public interface ManagedCoreShader extends UniformFinder {
   class_5944 getProgram();

   void release();
}
