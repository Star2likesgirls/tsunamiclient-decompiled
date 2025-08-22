package tsunami.features.modules.player;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_1799;
import net.minecraft.class_1890;
import net.minecraft.class_1893;
import net.minecraft.class_2189;
import net.minecraft.class_2336;
import net.minecraft.class_2338;
import net.minecraft.class_2868;
import net.minecraft.class_3965;
import net.minecraft.class_6880;
import tsunami.features.modules.Module;
import tsunami.setting.Setting;

public class AutoTool extends Module {
   public static Setting<Boolean> swapBack = new Setting("SwapBack", true);
   public static Setting<Boolean> saveItem = new Setting("SaveItem", true);
   public static Setting<Boolean> silent = new Setting("Silent", false);
   public static Setting<Boolean> echestSilk = new Setting("EchestSilk", true);
   public static int itemIndex;
   private boolean swap;
   private long swapDelay;
   private final List<Integer> lastItem = new ArrayList();

   public AutoTool() {
      super("AutoTool", Module.Category.PLAYER);
   }

   public void onUpdate() {
      if (mc.field_1765 instanceof class_3965) {
         class_3965 result = (class_3965)mc.field_1765;
         class_2338 pos = result.method_17777();
         if (!mc.field_1687.method_8320(pos).method_26215()) {
            if (getTool(pos) != -1 && mc.field_1690.field_1886.method_1434()) {
               this.lastItem.add(mc.field_1724.method_31548().field_7545);
               if ((Boolean)silent.getValue()) {
                  mc.field_1724.field_3944.method_52787(new class_2868(getTool(pos)));
               } else {
                  mc.field_1724.method_31548().field_7545 = getTool(pos);
               }

               itemIndex = getTool(pos);
               this.swap = true;
               this.swapDelay = System.currentTimeMillis();
            } else if (this.swap && !this.lastItem.isEmpty() && System.currentTimeMillis() >= this.swapDelay + 300L && (Boolean)swapBack.getValue()) {
               if ((Boolean)silent.getValue()) {
                  mc.field_1724.field_3944.method_52787(new class_2868((Integer)this.lastItem.get(0)));
               } else {
                  mc.field_1724.method_31548().field_7545 = (Integer)this.lastItem.get(0);
               }

               itemIndex = (Integer)this.lastItem.get(0);
               this.lastItem.clear();
               this.swap = false;
            }

         }
      }
   }

   public static int getTool(class_2338 pos) {
      int index = -1;
      float CurrentFastest = 1.0F;

      for(int i = 0; i < 9; ++i) {
         class_1799 stack = mc.field_1724.method_31548().method_5438(i);
         if (stack != class_1799.field_8037 && (mc.field_1724.method_31548().method_5438(i).method_7936() - mc.field_1724.method_31548().method_5438(i).method_7919() > 10 || !(Boolean)saveItem.getValue())) {
            float digSpeed = (float)class_1890.method_8225((class_6880)mc.field_1687.method_30349().method_30530(class_1893.field_9131.method_58273()).method_40264(class_1893.field_9131).get(), stack);
            float destroySpeed = stack.method_7924(mc.field_1687.method_8320(pos));
            if (mc.field_1687.method_8320(pos).method_26204() instanceof class_2189) {
               return -1;
            }

            if (mc.field_1687.method_8320(pos).method_26204() instanceof class_2336 && (Boolean)echestSilk.getValue()) {
               if (class_1890.method_8225((class_6880)mc.field_1687.method_30349().method_30530(class_1893.field_9099.method_58273()).method_40264(class_1893.field_9099).get(), stack) > 0 && digSpeed + destroySpeed > CurrentFastest) {
                  CurrentFastest = digSpeed + destroySpeed;
                  index = i;
               }
            } else if (digSpeed + destroySpeed > CurrentFastest) {
               CurrentFastest = digSpeed + destroySpeed;
               index = i;
            }
         }
      }

      return index;
   }
}
