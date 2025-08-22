package tsunami.features.modules.player;

import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_2828.class_2830;
import tsunami.events.impl.EventSync;
import tsunami.features.modules.Module;
import tsunami.setting.Setting;

public class Regen extends Module {
   private final Setting<Integer> health = new Setting("Health", 10, 0, 20);
   private final Setting<Integer> packetsPerTick = new Setting("Packets/Tick", 20, 2, 120);

   public Regen() {
      super("Regen", Module.Category.NONE);
   }

   @EventHandler
   public void onSync(EventSync e) {
      if (mc.field_1724.method_6032() + mc.field_1724.method_6067() <= (float)(Integer)this.health.getValue()) {
         for(int i = 0; i < (Integer)this.packetsPerTick.getValue(); ++i) {
            this.sendPacket(new class_2830(mc.field_1724.method_23317(), mc.field_1724.method_23318(), mc.field_1724.method_23321(), mc.field_1724.method_36454(), mc.field_1724.method_36455(), mc.field_1724.method_24828()));
         }
      }

   }
}
