package tsunami.features.modules.player;

import net.minecraft.class_1713;
import net.minecraft.class_1738;
import net.minecraft.class_1792;
import net.minecraft.class_1802;
import net.minecraft.class_1738.class_8051;
import tsunami.features.modules.Module;
import tsunami.utility.Timer;

public class MouseElytraFix extends Module {
   private final Timer delay = new Timer();

   public MouseElytraFix() {
      super("MouseElytraFix", Module.Category.NONE);
   }

   public void onUpdate() {
      class_1792 var2 = mc.field_1724.field_7512.method_34255().method_7909();
      if (var2 instanceof class_1738) {
         class_1738 armor = (class_1738)var2;
         if (!ElytraSwap.swapping && this.delay.every(300L) && armor.method_48398() == class_8051.field_41935 && mc.field_1724.method_31548().method_7372(2).method_7909() == class_1802.field_8833) {
            mc.field_1761.method_2906(0, 6, 1, class_1713.field_7790, mc.field_1724);
            int empty = findEmptySlot();
            boolean needDrop = empty == 999;
            if (needDrop) {
               empty = 9;
            }

            mc.field_1761.method_2906(0, empty, 1, class_1713.field_7790, mc.field_1724);
            if (needDrop) {
               mc.field_1761.method_2906(0, -999, 1, class_1713.field_7790, mc.field_1724);
            }
         }
      }

   }

   public static int findEmptySlot() {
      for(int i = 0; i < 36; ++i) {
         if (mc.field_1724.method_31548().method_5438(i).method_7960()) {
            return i < 9 ? i + 36 : i;
         }
      }

      return 999;
   }
}
