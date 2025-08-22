package tsunami.features.modules.player;

import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_2793;
import tsunami.events.impl.PacketEvent;
import tsunami.features.modules.Module;
import tsunami.utility.Timer;

public class PortalGodMode extends Module {
   private Timer confirmTimer = new Timer();
   private boolean teleported;

   public PortalGodMode() {
      super("PortalGodMode", Module.Category.NONE);
      this.confirmTimer.setMs(99999L);
   }

   @EventHandler
   public void onPacketSend(PacketEvent.Send e) {
      if (e.getPacket() instanceof class_2793 && this.confirmTimer.getPassedTimeMs() < 5000L) {
         this.teleported = true;
         e.cancel();
      }

   }

   public void onDisable() {
      this.teleported = false;
   }

   public void onUpdate() {
      for(int x = (int)(mc.field_1724.method_23317() - 2.0D); (double)x < mc.field_1724.method_23317() + 2.0D; ++x) {
         for(int z = (int)(mc.field_1724.method_23321() - 2.0D); (double)z < mc.field_1724.method_23321() + 2.0D; ++z) {
            for(int y = (int)(mc.field_1724.method_23318() - 2.0D); (double)y < mc.field_1724.method_23318() + 2.0D; ++y) {
               if (mc.field_1687.method_8320(class_2338.method_49637((double)x, (double)y, (double)z)).method_26204() == class_2246.field_10316) {
                  this.confirmTimer.reset();
               }
            }
         }
      }

   }

   public String getDisplayInfo() {
      return this.teleported ? "God" : (this.confirmTimer.getPassedTimeMs() < 5000L ? "Ready" : "Waiting");
   }
}
