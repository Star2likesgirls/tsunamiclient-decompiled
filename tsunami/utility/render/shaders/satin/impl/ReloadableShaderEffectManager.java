package tsunami.utility.render.shaders.satin.impl;

import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Consumer;
import net.minecraft.class_290;
import net.minecraft.class_293;
import net.minecraft.class_2960;
import net.minecraft.class_5912;
import tsunami.utility.render.WindowResizeCallback;
import tsunami.utility.render.shaders.satin.api.managed.ManagedCoreShader;
import tsunami.utility.render.shaders.satin.api.managed.ManagedShaderEffect;
import tsunami.utility.render.shaders.satin.api.managed.ShaderEffectManager;

public final class ReloadableShaderEffectManager implements ShaderEffectManager {
   public static final ReloadableShaderEffectManager INSTANCE = new ReloadableShaderEffectManager();
   private final Set<ResettableManagedShaderBase<?>> managedShaders = new ReferenceOpenHashSet();

   public ReloadableShaderEffectManager() {
      WindowResizeCallback.EVENT.register((client, window) -> {
         this.onResolutionChanged(window.method_4489(), window.method_4506());
      });
   }

   public ManagedShaderEffect manage(class_2960 location) {
      return this.manage(location, (s) -> {
      });
   }

   public ManagedShaderEffect manage(class_2960 location, Consumer<ManagedShaderEffect> initCallback) {
      ResettableManagedShaderEffect ret = new ResettableManagedShaderEffect(location, initCallback);
      this.managedShaders.add(ret);
      return ret;
   }

   public ManagedCoreShader manageCoreShader(class_2960 location) {
      return this.manageCoreShader(location, class_290.field_1580);
   }

   public ManagedCoreShader manageCoreShader(class_2960 location, class_293 vertexFormat) {
      return this.manageCoreShader(location, vertexFormat, (s) -> {
      });
   }

   public ManagedCoreShader manageCoreShader(class_2960 location, class_293 vertexFormat, Consumer<ManagedCoreShader> initCallback) {
      ResettableManagedCoreShader ret = new ResettableManagedCoreShader(location, vertexFormat, initCallback);
      this.managedShaders.add(ret);
      return ret;
   }

   public void reload(class_5912 shaderResources) {
      Iterator var2 = this.managedShaders.iterator();

      while(var2.hasNext()) {
         ResettableManagedShaderBase<?> ss = (ResettableManagedShaderBase)var2.next();
         ss.initializeOrLog(shaderResources);
      }

   }

   public void onResolutionChanged(int newWidth, int newHeight) {
      this.runShaderSetup(newWidth, newHeight);
   }

   private void runShaderSetup(int newWidth, int newHeight) {
      if (!this.managedShaders.isEmpty()) {
         Iterator var3 = this.managedShaders.iterator();

         while(var3.hasNext()) {
            ResettableManagedShaderBase<?> ss = (ResettableManagedShaderBase)var3.next();
            if (ss.isInitialized()) {
               ss.setup(newWidth, newHeight);
            }
         }
      }

   }
}
