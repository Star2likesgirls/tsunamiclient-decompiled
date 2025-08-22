package tsunami.features.modules.player;

import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_2815;
import tsunami.events.impl.PacketEvent;
import tsunami.features.modules.Module;

public class XCarry extends Module {
   public XCarry() {
      super("XCarry", Module.Category.NONE);
   }

   @EventHandler
   public void onPacketSend(PacketEvent.Send e) {
      if (e.getPacket() instanceof class_2815) {
         e.cancel();
      }

   }
}
