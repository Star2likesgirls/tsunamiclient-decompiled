package tsunami.features.modules.movement;

import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_2338;
import net.minecraft.class_243;
import tsunami.events.impl.PlayerUpdateEvent;
import tsunami.features.modules.Module;
import tsunami.setting.Setting;
import tsunami.utility.world.HoleUtility;

public class HoleAnchor extends Module {
   private final Setting<Integer> pitch = new Setting("Pitch", 60, 0, 90);
   private final Setting<Boolean> pull = new Setting("Pull", true);

   public HoleAnchor() {
      super("HoleAnchor", Module.Category.NONE);
   }

   @EventHandler
   public void onPlayerUpdate(PlayerUpdateEvent e) {
      if (mc.field_1724.method_36455() > (float)(Integer)this.pitch.getValue() && (HoleUtility.validIndestructible(class_2338.method_49638(mc.field_1724.method_19538()).method_10087(1)) || HoleUtility.validIndestructible(class_2338.method_49638(mc.field_1724.method_19538()).method_10087(2)) || HoleUtility.validIndestructible(class_2338.method_49638(mc.field_1724.method_19538()).method_10087(3)) || HoleUtility.validTwoBlockIndestructible(class_2338.method_49638(mc.field_1724.method_19538()).method_10087(1)) || HoleUtility.validTwoBlockIndestructible(class_2338.method_49638(mc.field_1724.method_19538()).method_10087(2)) || HoleUtility.validTwoBlockIndestructible(class_2338.method_49638(mc.field_1724.method_19538()).method_10087(3)))) {
         if (!(Boolean)this.pull.getValue()) {
            mc.field_1724.method_18800(0.0D, mc.field_1724.method_18798().method_10214(), 0.0D);
         } else {
            class_243 center = new class_243(Math.floor(mc.field_1724.method_23317()) + 0.5D, Math.floor(mc.field_1724.method_23318()), Math.floor(mc.field_1724.method_23321()) + 0.5D);
            if (Math.abs(center.field_1352 - mc.field_1724.method_23317()) > 0.1D || Math.abs(center.field_1350 - mc.field_1724.method_23321()) > 0.1D) {
               double d3 = center.field_1352 - mc.field_1724.method_23317();
               double d4 = center.field_1350 - mc.field_1724.method_23321();
               mc.field_1724.method_18800(Math.min(d3 / 2.0D, 0.2D), mc.field_1724.method_18798().method_10214(), Math.min(d4 / 2.0D, 0.2D));
            }
         }
      }

   }
}
