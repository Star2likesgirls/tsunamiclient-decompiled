package tsunami.utility.render.shaders.satin.impl;

import net.minecraft.class_1044;
import net.minecraft.class_276;
import net.minecraft.class_5944;

public final class ManagedSamplerUniformV1 extends ManagedSamplerUniformBase {
   public ManagedSamplerUniformV1(String name) {
      super(name);
   }

   public void set(class_1044 texture) {
      this.set((Object)texture);
   }

   public void set(class_276 textureFbo) {
      this.set((Object)textureFbo);
   }

   public void set(int textureName) {
      this.set((Object)textureName);
   }

   protected void set(Object value) {
      SamplerAccess[] targets = this.targets;
      if (targets.length > 0 && this.cachedValue != value) {
         SamplerAccess[] var3 = targets;
         int var4 = targets.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            SamplerAccess target = var3[var5];
            ((class_5944)target).method_34583(this.name, value);
         }

         this.cachedValue = value;
      }

   }
}
