package tsunami.features.modules.misc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import net.minecraft.class_124;
import net.minecraft.class_1703;
import net.minecraft.class_1735;
import net.minecraft.class_1799;
import net.minecraft.class_1812;
import net.minecraft.class_1844;
import net.minecraft.class_9334;
import tsunami.features.cmd.impl.KitCommand;
import tsunami.features.modules.Module;
import tsunami.features.modules.client.ClientSettings;
import tsunami.setting.Setting;

public class AutoGear extends Module {
   public Setting<Integer> actionDelay = new Setting("ActionDelay", 50, 0, 20);
   public Setting<Integer> clicksPerAction = new Setting("Click/Action", 1, 1, 108);
   private HashMap<Integer, String> expectedInv = new HashMap();
   private int delay = 0;

   public AutoGear() {
      super("AutoGear", Module.Category.NONE);
   }

   public void onEnable() {
      this.setup();
   }

   public void setup() {
      String selectedKit = KitCommand.getSelectedKit();
      if (selectedKit.isEmpty()) {
         this.disable(ClientSettings.isRu() ? "Не выбран кит! Воспользуйся командой kit" : "No kit is selected! Use the kit command");
      } else {
         this.sendMessage(ClientSettings.isRu() ? "Выбран кит => " + String.valueOf(class_124.field_1075) + selectedKit : "Selected kit -> " + String.valueOf(class_124.field_1075) + selectedKit);
         String kitItems = KitCommand.getKitItems(selectedKit);
         if (!kitItems.isEmpty() && kitItems.split(" ").length == 36) {
            String[] items = kitItems.split(" ");
            this.expectedInv = new HashMap();

            for(int i = 0; i < 36; ++i) {
               if (!items[i].equals("block.minecraft.air")) {
                  this.expectedInv.put(i, items[i]);
               }
            }

         } else {
            this.disable(ClientSettings.isRu() ? "Произошла ошибка в конфигурации кита! Создай кит снова" : "There was an error in the kit configuration! Create the kit again");
         }
      }
   }

   public void onUpdate() {
      if (this.delay > 0) {
         --this.delay;
      } else if (this.expectedInv.isEmpty()) {
         this.setup();
      } else {
         int actions = 0;
         class_1703 handler = mc.field_1724.field_7512;
         if (handler.field_7761.size() == 63 || handler.field_7761.size() == 90) {
            ArrayList<Integer> clickSequence = this.buildClickSequence(handler);
            Iterator var4 = clickSequence.iterator();

            while(var4.hasNext()) {
               int s = (Integer)var4.next();
               clickSlot(s);
               ++actions;
               if (actions >= (Integer)this.clicksPerAction.getValue()) {
                  break;
               }
            }

            this.delay = (Integer)this.actionDelay.getValue();
         }
      }
   }

   private int searchInContainer(String name, boolean lower, class_1703 handler) {
      class_1799 cursorStack = handler.method_34255();
      if ((cursorStack.method_7909() instanceof class_1812 ? cursorStack.method_7909().method_7876() + ((class_1844)cursorStack.method_7909().method_57347().method_57829(class_9334.field_49651)).method_8064() : cursorStack.method_7909().method_7876()).equals(name)) {
         return -2;
      } else {
         for(int i = 0; i < (lower ? 26 : 53); ++i) {
            class_1799 stack = handler.method_7611(i).method_7677();
            if ((stack.method_7909() instanceof class_1812 ? stack.method_7909().method_7876() + ((class_1844)stack.method_7909().method_57347().method_57829(class_9334.field_49651)).method_8064() : stack.method_7909().method_7876()).equals(name)) {
               return i;
            }
         }

         return -1;
      }
   }

   private ArrayList<Integer> buildClickSequence(class_1703 handler) {
      ArrayList<Integer> clicks = new ArrayList();
      Iterator var3 = this.expectedInv.keySet().iterator();

      while(var3.hasNext()) {
         int s = (Integer)var3.next();
         int lower = s < 9 ? s + 54 : s + 18;
         int upper = s < 9 ? s + 81 : s + 45;
         class_1799 itemInslot = ((class_1735)handler.field_7761.get(handler.field_7761.size() == 63 ? lower : upper)).method_7677();
         if (!(itemInslot.method_7909() instanceof class_1812 ? itemInslot.method_7909().method_7876() + ((class_1844)itemInslot.method_7909().method_57347().method_57829(class_9334.field_49651)).method_8064() : itemInslot.method_7909().method_7876()).equals(this.expectedInv.get(s))) {
            int slot = this.searchInContainer((String)this.expectedInv.get(s), handler.field_7761.size() == 63, handler);
            if (slot == -2) {
               clicks.add(handler.field_7761.size() == 63 ? lower : upper);
            } else if (slot != -1) {
               clicks.add(slot);
               clicks.add(handler.field_7761.size() == 63 ? lower : upper);
               clicks.add(slot);
            }
         }
      }

      return clicks;
   }
}
