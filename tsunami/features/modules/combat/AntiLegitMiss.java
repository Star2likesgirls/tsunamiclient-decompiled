package tsunami.features.modules.combat;

import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_3966;
import tsunami.events.impl.EventAttack;
import tsunami.events.impl.EventHandleBlockBreaking;
import tsunami.features.modules.Module;

public class AntiLegitMiss extends Module {
   public AntiLegitMiss() {
      super("AntiLegitMiss", Module.Category.NONE);
   }

   @EventHandler
   public void onAttack(EventAttack e) {
      if (!(mc.field_1765 instanceof class_3966) && e.isPre()) {
         e.cancel();
      }

   }

   @EventHandler
   public void onBlockBreaking(EventHandleBlockBreaking e) {
      if (!(mc.field_1765 instanceof class_3966)) {
         e.cancel();
      }

   }
}
