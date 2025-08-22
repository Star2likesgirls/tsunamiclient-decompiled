package tsunami.features.modules.player;

import java.util.Comparator;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_1684;
import net.minecraft.class_2828.class_2829;
import tsunami.events.impl.EventEntitySpawn;
import tsunami.features.modules.Module;

public class PearlBait extends Module {
   public PearlBait() {
      super("PearlBait", Module.Category.NONE);
   }

   @EventHandler
   public void onEntitySpawn(EventEntitySpawn e) {
      if (e.getEntity() instanceof class_1684) {
         mc.field_1687.method_18456().stream().min(Comparator.comparingDouble((p) -> {
            return p.method_5707(e.getEntity().method_19538());
         })).ifPresent((player) -> {
            if (player.equals(mc.field_1724) && mc.field_1724.method_24828()) {
               mc.field_1724.method_18800(0.0D, 0.0D, 0.0D);
               mc.field_1724.field_3913.field_3905 = 0.0F;
               mc.field_1724.field_3913.field_3907 = 0.0F;
               mc.field_1724.field_3944.method_52787(new class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318() + 1.0D, mc.field_1724.method_23321(), false));
            }

         });
      }

   }
}
