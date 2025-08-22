package tsunami.utility.render.shaders.satin.impl;

import java.util.Objects;
import java.util.function.IntSupplier;
import net.minecraft.class_1044;
import net.minecraft.class_276;
import net.minecraft.class_280;
import tsunami.utility.render.shaders.satin.api.managed.uniform.SamplerUniformV2;

public final class ManagedSamplerUniformV2 extends ManagedSamplerUniformBase implements SamplerUniformV2 {
   public ManagedSamplerUniformV2(String name) {
      super(name);
   }

   public void set(class_1044 texture) {
      Objects.requireNonNull(texture);
      this.set(texture::method_4624);
   }

   public void set(class_276 textureFbo) {
      Objects.requireNonNull(textureFbo);
      this.set(textureFbo::method_30277);
   }

   public void set(int textureName) {
      this.set(() -> {
         return textureName;
      });
   }

   protected void set(Object value) {
      this.set((IntSupplier)value);
   }

   public void set(IntSupplier value) {
      SamplerAccess[] targets = this.targets;
      if (targets.length > 0 && this.cachedValue != value) {
         SamplerAccess[] var3 = targets;
         int var4 = targets.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            SamplerAccess target = var3[var5];
            ((class_280)target).method_1269(this.name, value);
         }

         this.cachedValue = value;
      }

   }
}
