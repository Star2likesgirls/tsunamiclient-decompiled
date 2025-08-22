package tsunami.features.modules.misc;

import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_2828;
import net.minecraft.class_437;
import tsunami.events.impl.PacketEvent;
import tsunami.features.modules.Module;
import tsunami.features.modules.client.ClientSettings;

public class Ghost extends Module {
   private boolean bypass = false;

   public Ghost() {
      super("Ghost", Module.Category.NONE);
   }

   public void onEnable() {
      this.bypass = false;
   }

   public void onDisable() {
      if (mc.field_1724 != null) {
         mc.field_1724.method_7331();
      }

      this.bypass = false;
   }

   public void onUpdate() {
      if (mc.field_1724 != null && mc.field_1687 != null) {
         if (mc.field_1724.method_6032() == 0.0F) {
            mc.field_1724.method_6033(20.0F);
            this.bypass = true;
            mc.method_1507((class_437)null);
            mc.field_1724.method_5814(mc.field_1724.method_23317(), mc.field_1724.method_23318(), mc.field_1724.method_23321());
            this.sendMessage(ClientSettings.isRu() ? "Для возрождения выключи модуль!" : "To revive, turn off the module!");
         }

      }
   }

   @EventHandler
   public void onPacketSend(PacketEvent.Send event) {
      if (this.bypass && event.getPacket() instanceof class_2828) {
         event.cancel();
      }

   }
}
