package tsunami.features.modules.movement;

import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_1294;
import tsunami.events.impl.EventMove;
import tsunami.features.modules.Module;
import tsunami.setting.Setting;

public class LevitationControl extends Module {
   private final Setting<Integer> upAmplifier = new Setting("Up Speed", 1, 1, 5);
   private final Setting<Integer> downAmplifier = new Setting("Down Speed", 1, 1, 5);

   public LevitationControl() {
      super("LevitCtrl", Module.Category.NONE);
   }

   @EventHandler
   public void onMove(EventMove e) {
      if (mc.field_1724.method_6059(class_1294.field_5902)) {
         int amplifier = mc.field_1724.method_6112(class_1294.field_5902).method_5578();
         if (mc.field_1690.field_1903.method_1434()) {
            e.setY((0.05D * (double)(amplifier + 1) - e.getY()) * 0.2D * (double)(Integer)this.upAmplifier.getValue() * 100.0D);
         } else if (mc.field_1690.field_1832.method_1434()) {
            e.setY(-((0.05D * (double)(amplifier + 1) - e.getY()) * 0.2D * (double)(Integer)this.downAmplifier.getValue() * 100.0D));
         } else {
            e.setY(0.0D);
         }

         e.cancel();
      }

   }
}
