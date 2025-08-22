package tsunami.features.modules.player;

import net.minecraft.class_1766;
import net.minecraft.class_1799;
import tsunami.features.modules.Module;
import tsunami.features.modules.client.ClientSettings;
import tsunami.features.modules.combat.AutoTotem;
import tsunami.setting.Setting;

public class ToolSaver extends Module {
   private final Setting<Integer> savePercent = new Setting("Save %", 10, 1, 50);

   public ToolSaver() {
      super("ToolSaver", Module.Category.NONE);
   }

   public void onUpdate() {
      class_1799 tool = mc.field_1724.method_6047();
      if (tool.method_7909() instanceof class_1766) {
         float durability = (float)(tool.method_7936() - tool.method_7919());
         int percent = (int)(durability / (float)tool.method_7936() * 100.0F);
         if (percent <= (Integer)this.savePercent.getValue()) {
            mc.field_1724.method_31548().field_7545 = AutoTotem.findNearestCurrentItem();
            this.sendMessage(ClientSettings.isRu() ? "Твой инструмент почти сломался!" : "Your tool is almost broken!");
         }

      }
   }
}
