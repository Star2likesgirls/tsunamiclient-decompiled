package tsunami.features.modules.player;

import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_2596;
import net.minecraft.class_2708;
import tsunami.events.impl.PacketEvent;
import tsunami.features.modules.Module;
import tsunami.injection.accesors.IPlayerPositionLookS2CPacket;

public class NoServerRotate extends Module {
   public NoServerRotate() {
      super("NoServerRotate", Module.Category.NONE);
   }

   @EventHandler
   public void onPacketReceive(PacketEvent.Receive e) {
      if (!fullNullCheck()) {
         class_2596 var3 = e.getPacket();
         if (var3 instanceof class_2708) {
            class_2708 pac = (class_2708)var3;
            ((IPlayerPositionLookS2CPacket)pac).setYaw(mc.field_1724.method_36454());
            ((IPlayerPositionLookS2CPacket)pac).setPitch(mc.field_1724.method_36455());
         }

      }
   }
}
