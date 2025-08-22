package tsunami.utility.render.shaders.satin.api.managed;

import net.minecraft.class_279;
import tsunami.utility.render.shaders.satin.api.managed.uniform.SamplerUniformV2;
import tsunami.utility.render.shaders.satin.api.managed.uniform.UniformFinder;

public interface ManagedShaderEffect extends UniformFinder {
   class_279 getShaderEffect();

   void release();

   void render(float var1);

   ManagedFramebuffer getTarget(String var1);

   void setUniformValue(String var1, int var2);

   void setUniformValue(String var1, float var2);

   void setUniformValue(String var1, float var2, float var3);

   void setUniformValue(String var1, float var2, float var3, float var4);

   void setUniformValue(String var1, float var2, float var3, float var4, float var5);

   SamplerUniformV2 findSampler(String var1);
}
