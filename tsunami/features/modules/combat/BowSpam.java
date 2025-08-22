package tsunami.features.modules.combat;

import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_1268;
import net.minecraft.class_1802;
import net.minecraft.class_2338;
import net.minecraft.class_2846;
import net.minecraft.class_2886;
import net.minecraft.class_2846.class_2847;
import tsunami.events.impl.EventSync;
import tsunami.features.modules.Module;
import tsunami.setting.Setting;

public class BowSpam extends Module {
   private final Setting<Integer> ticks = new Setting("Delay", 3, 0, 20);

   public BowSpam() {
      super("BowSpam", Module.Category.NONE);
   }

   @EventHandler
   public void onSync(EventSync event) {
      if ((mc.field_1724.method_6079().method_7909() == class_1802.field_8102 || mc.field_1724.method_6047().method_7909() == class_1802.field_8102) && mc.field_1724.method_6115() && mc.field_1724.method_6048() >= (Integer)this.ticks.getValue()) {
         this.sendPacket(new class_2846(class_2847.field_12974, class_2338.field_10980, mc.field_1724.method_5735()));
         this.sendSequencedPacket((id) -> {
            return new class_2886(mc.field_1724.method_6079().method_7909() == class_1802.field_8102 ? class_1268.field_5810 : class_1268.field_5808, id, mc.field_1724.method_36454(), mc.field_1724.method_36455());
         });
         mc.field_1724.method_6075();
      }

   }
}
