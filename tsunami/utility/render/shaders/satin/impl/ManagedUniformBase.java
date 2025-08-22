package tsunami.utility.render.shaders.satin.impl;

import java.util.List;
import net.minecraft.class_283;
import net.minecraft.class_5944;

public abstract class ManagedUniformBase {
   protected final String name;

   public ManagedUniformBase(String name) {
      this.name = name;
   }

   public abstract boolean findUniformTargets(List<class_283> var1);

   public abstract boolean findUniformTarget(class_5944 var1);

   public String getName() {
      return this.name;
   }
}
