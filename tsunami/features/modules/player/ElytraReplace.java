package tsunami.features.modules.player;

import net.minecraft.class_1304;
import net.minecraft.class_1770;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_2815;
import tsunami.core.Managers;
import tsunami.features.modules.Module;
import tsunami.features.modules.client.ClientSettings;
import tsunami.gui.notification.Notification;
import tsunami.setting.Setting;
import tsunami.utility.player.InventoryUtility;
import tsunami.utility.player.SearchInvResult;

public class ElytraReplace extends Module {
   private final Setting<Integer> durability = new Setting("Durability", 5, 0, 100);

   public ElytraReplace() {
      super("ElytraReplace", Module.Category.NONE);
   }

   public void onUpdate() {
      class_1799 is = mc.field_1724.method_6118(class_1304.field_6174);
      if (is.method_31574(class_1802.field_8833) && 100.0F - (float)is.method_7919() / (float)is.method_7936() * 100.0F <= (float)(Integer)this.durability.getValue()) {
         SearchInvResult result = InventoryUtility.findInInventory((stack) -> {
            if (stack.method_7909() instanceof class_1770) {
               return 100.0F - (float)stack.method_7919() / (float)stack.method_7936() * 100.0F > (float)(Integer)this.durability.getValue();
            } else {
               return false;
            }
         });
         if (result.found()) {
            clickSlot(result.slot());
            clickSlot(6);
            clickSlot(result.slot());
            this.sendPacket(new class_2815(mc.field_1724.field_7512.field_7763));
            Managers.NOTIFICATION.publicity("ElytraReplace", ClientSettings.isRu() ? "Меняем элитру на новую!" : "Swapping the old elytra for a new one!", 2, Notification.Type.SUCCESS);
            this.sendMessage(ClientSettings.isRu() ? "Меняем элитру на новую!" : "Swapping the old elytra for a new one!");
         }
      }

   }
}
