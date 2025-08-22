package tsunami.utility.render.shaders.satin.api.managed;

import java.util.function.Consumer;
import net.minecraft.class_293;
import net.minecraft.class_2960;
import tsunami.utility.render.shaders.satin.impl.ReloadableShaderEffectManager;

public interface ShaderEffectManager {
   static ShaderEffectManager getInstance() {
      return ReloadableShaderEffectManager.INSTANCE;
   }

   ManagedShaderEffect manage(class_2960 var1);

   ManagedShaderEffect manage(class_2960 var1, Consumer<ManagedShaderEffect> var2);

   ManagedCoreShader manageCoreShader(class_2960 var1);

   ManagedCoreShader manageCoreShader(class_2960 var1, class_293 var2);

   ManagedCoreShader manageCoreShader(class_2960 var1, class_293 var2, Consumer<ManagedCoreShader> var3);
}
