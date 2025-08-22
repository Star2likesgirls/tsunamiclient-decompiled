package tsunami.features.modules.misc;

import net.minecraft.class_1268;
import net.minecraft.class_1802;
import net.minecraft.class_2886;
import tsunami.features.modules.Module;
import tsunami.setting.Setting;
import tsunami.utility.player.InventoryUtility;
import tsunami.utility.player.SearchInvResult;

public class AutoSoup extends Module {
   private final Setting<Float> health = new Setting("TriggerHealth", 7.0F, 1.0F, 20.0F);

   public AutoSoup() {
      super("AutoSoup", Module.Category.NONE);
   }

   public void onUpdate() {
      if (mc.field_1724.method_6032() <= (Float)this.health.getValue()) {
         SearchInvResult result = InventoryUtility.findItemInHotBar(class_1802.field_8208);
         int prevSlot = mc.field_1724.method_31548().field_7545;
         if (result.found()) {
            result.switchTo();
            this.sendSequencedPacket((id) -> {
               return new class_2886(class_1268.field_5808, id, mc.field_1724.method_36454(), mc.field_1724.method_36455());
            });
            InventoryUtility.switchTo(prevSlot);
         }
      }

   }
}
