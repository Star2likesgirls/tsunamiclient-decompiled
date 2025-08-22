package tsunami.features.modules.misc;

import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_1713;
import net.minecraft.class_1735;
import net.minecraft.class_1792;
import tsunami.events.impl.EventClickSlot;
import tsunami.features.modules.Module;
import tsunami.setting.Setting;

public class ItemScroller extends Module {
   public Setting<Integer> delay = new Setting("Delay", 80, 0, 500);
   private boolean pauseListening = false;

   public ItemScroller() {
      super("ItemScroller", Module.Category.MISC);
   }

   @EventHandler
   public void onClick(EventClickSlot e) {
      if ((this.isKeyPressed(340) || this.isKeyPressed(344)) && (this.isKeyPressed(341) || this.isKeyPressed(345)) && e.getSlotActionType() == class_1713.field_7795 && !this.pauseListening) {
         class_1792 copy = ((class_1735)mc.field_1724.field_7512.field_7761.get(e.getSlot())).method_7677().method_7909();
         this.pauseListening = true;

         for(int i2 = 0; i2 < mc.field_1724.field_7512.field_7761.size(); ++i2) {
            if (((class_1735)mc.field_1724.field_7512.field_7761.get(i2)).method_7677().method_7909() == copy) {
               mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, i2, 1, class_1713.field_7795, mc.field_1724);
            }
         }

         this.pauseListening = false;
      }

   }
}
