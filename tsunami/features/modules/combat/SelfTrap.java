package tsunami.features.modules.combat;

import net.minecraft.class_1657;
import org.jetbrains.annotations.Nullable;
import tsunami.features.modules.Module;
import tsunami.features.modules.base.TrapModule;

public final class SelfTrap extends TrapModule {
   public SelfTrap() {
      super("SelfTrap", Module.Category.NONE);
   }

   protected boolean needNewTarget() {
      return this.target == null;
   }

   @Nullable
   protected class_1657 getTarget() {
      return mc.field_1724;
   }
}
