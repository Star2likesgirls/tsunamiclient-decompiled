package tsunami.features.modules.movement;

import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_1297;
import net.minecraft.class_1536;
import net.minecraft.class_2596;
import net.minecraft.class_2663;
import tsunami.events.impl.PacketEvent;
import tsunami.features.modules.Module;
import tsunami.setting.Setting;

public class NoPush extends Module {
   public Setting<Boolean> blocks = new Setting("Blocks", true);
   public Setting<Boolean> players = new Setting("Players", true);
   public Setting<Boolean> water = new Setting("Liquids", true);
   public Setting<Boolean> fishingHook = new Setting("FishingHook", true);

   public NoPush() {
      super("NoPush", Module.Category.NONE);
   }

   @EventHandler
   public void onPacketReceive(PacketEvent.Receive e) {
      class_2596 var4 = e.getPacket();
      if (var4 instanceof class_2663) {
         class_2663 pac = (class_2663)var4;
         if (pac.method_11470() == 31) {
            class_1297 var5 = pac.method_11469(mc.field_1687);
            if (var5 instanceof class_1536) {
               class_1536 hook = (class_1536)var5;
               if ((Boolean)this.fishingHook.getValue() && hook.method_26957() == mc.field_1724) {
                  e.cancel();
               }
            }
         }
      }

   }
}
