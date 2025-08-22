package tsunami.utility.render.shaders.satin.impl;

import com.google.common.base.Preconditions;
import com.mojang.logging.LogUtils;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Consumer;
import net.fabricmc.fabric.impl.client.rendering.FabricShaderProgram;
import net.minecraft.class_293;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_5912;
import net.minecraft.class_5944;
import tsunami.utility.render.shaders.satin.api.managed.ManagedCoreShader;
import tsunami.utility.render.shaders.satin.api.managed.uniform.SamplerUniform;

public final class ResettableManagedCoreShader extends ResettableManagedShaderBase<class_5944> implements ManagedCoreShader {
   private final Consumer<ManagedCoreShader> initCallback;
   private final class_293 vertexFormat;
   private final Map<String, ManagedSamplerUniformV1> managedSamplers = new HashMap();

   public ResettableManagedCoreShader(class_2960 location, class_293 vertexFormat, Consumer<ManagedCoreShader> initCallback) {
      super(location);
      this.vertexFormat = vertexFormat;
      this.initCallback = initCallback;
   }

   protected class_5944 parseShader(class_5912 resourceManager, class_310 mc, class_2960 location) throws IOException {
      return new FabricShaderProgram(resourceManager, this.getLocation(), this.vertexFormat);
   }

   public void setup(int newWidth, int newHeight) {
      Preconditions.checkNotNull((class_5944)this.shader);
      Iterator var3 = this.getManagedUniforms().iterator();

      while(var3.hasNext()) {
         ManagedUniformBase uniform = (ManagedUniformBase)var3.next();
         this.setupUniform(uniform, (class_5944)this.shader);
      }

      this.initCallback.accept(this);
   }

   public class_5944 getProgram() {
      return (class_5944)this.shader;
   }

   protected boolean setupUniform(ManagedUniformBase uniform, class_5944 shader) {
      return uniform.findUniformTarget(shader);
   }

   public SamplerUniform findSampler(String samplerName) {
      return (SamplerUniform)this.manageUniform(this.managedSamplers, ManagedSamplerUniformV1::new, samplerName, "sampler");
   }

   protected void logInitError(IOException e) {
      LogUtils.getLogger().error("Could not create shader program {}", this.getLocation(), e);
   }
}
