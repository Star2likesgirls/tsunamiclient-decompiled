package tsunami.features.modules.player;

import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_2735;
import net.minecraft.class_2868;
import tsunami.events.impl.PacketEvent;
import tsunami.features.modules.Module;

public class NoServerSlot extends Module {
   public NoServerSlot() {
      super("NoServerSlot", Module.Category.NONE);
   }

   @EventHandler
   public void onPacketReceive(PacketEvent.Receive event) {
      if (event.getPacket() instanceof class_2735) {
         event.cancel();
         this.sendPacket(new class_2868(mc.field_1724.method_31548().field_7545));
      }

   }
}
