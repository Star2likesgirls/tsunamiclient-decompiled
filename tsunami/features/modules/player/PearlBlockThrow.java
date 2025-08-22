package tsunami.features.modules.player;

import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_1268;
import net.minecraft.class_1802;
import net.minecraft.class_2596;
import net.minecraft.class_2885;
import tsunami.events.impl.PacketEvent;
import tsunami.features.modules.Module;
import tsunami.injection.accesors.IPlayerInteractBlockC2SPacket;

public class PearlBlockThrow extends Module {
   public PearlBlockThrow() {
      super("PearlBlockThrow", Module.Category.NONE);
   }

   @EventHandler
   public void onPackerSend(PacketEvent.Send event) {
      class_2596 var3 = event.getPacket();
      if (var3 instanceof class_2885) {
         class_2885 p = (class_2885)var3;
         if (mc.field_1724.method_6047().method_7909() == class_1802.field_8634) {
            ((IPlayerInteractBlockC2SPacket)p).setHand(class_1268.field_5810);
         }
      }

   }
}
