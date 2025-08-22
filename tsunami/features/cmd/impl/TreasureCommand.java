package tsunami.features.cmd.impl;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.class_1844;
import net.minecraft.class_2172;
import net.minecraft.class_9334;
import tsunami.features.cmd.Command;
import tsunami.features.modules.client.ClientSettings;

public class TreasureCommand extends Command {
   public TreasureCommand() {
      super("gettreasure", "treasure");
   }

   public void executeBuild(LiteralArgumentBuilder<class_2172> builder) {
      builder.executes((context) -> {
         if (mc.field_1724.method_6047().method_7909().toString().equals("filled_map")) {
            Record nbt = (Record)mc.field_1724.method_6047().method_57825(class_9334.field_49647, class_1844.field_49274);
            if (nbt == null) {
               return 1;
            }

            StringBuilder result = new StringBuilder();
            String rawNbt = nbt.toString();

            for(int i = rawNbt.indexOf("x="); i < rawNbt.indexOf(", rotation") - 2; ++i) {
               result.append(rawNbt.charAt(i));
            }

            sendMessage(ClientSettings.isRu() ? "Нашел! Координаты: " + String.valueOf(result) : "Found! Coords: " + String.valueOf(result));
         } else {
            sendMessage(ClientSettings.isRu() ? "Возьми карту в руки!" : "Take a map into your hand!");
         }

         return 1;
      });
   }
}
