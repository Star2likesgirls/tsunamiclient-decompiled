package tsunami.features.cmd.impl;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import java.util.Iterator;
import net.minecraft.class_124;
import net.minecraft.class_2172;
import net.minecraft.class_2248;
import net.minecraft.class_7923;
import org.jetbrains.annotations.NotNull;
import tsunami.core.manager.client.ModuleManager;
import tsunami.features.cmd.Command;
import tsunami.features.cmd.args.SearchArgumentType;
import tsunami.features.modules.client.ClientSettings;
import tsunami.setting.impl.ItemSelectSetting;

public class BlockESPCommand extends Command {
   public BlockESPCommand() {
      super("blockesp");
   }

   public void executeBuild(@NotNull LiteralArgumentBuilder<class_2172> builder) {
      builder.then(literal("reset").executes((context) -> {
         ((ItemSelectSetting)ModuleManager.blockESP.selectedBlocks.getValue()).clear();
         sendMessage(ClientSettings.isRu() ? "BlockESP был очищен!" : "BlockESP got reset.");
         mc.field_1769.method_3279();
         return 1;
      }));
      builder.then(literal("add").then(arg("block", SearchArgumentType.create()).executes((context) -> {
         String blockName = (String)context.getArgument("block", String.class);
         class_2248 result = getRegisteredBlock(blockName);
         String var10000;
         if (result != null) {
            ((ItemSelectSetting)ModuleManager.blockESP.selectedBlocks.getValue()).add(result);
            var10000 = String.valueOf(class_124.field_1060);
            sendMessage(var10000 + blockName + (ClientSettings.isRu() ? " добавлен в BlockESP" : " added to BlockESP"));
         } else {
            var10000 = String.valueOf(class_124.field_1061);
            sendMessage(var10000 + (ClientSettings.isRu() ? "Такого блока нет!" : "There is no such block!"));
         }

         mc.field_1769.method_3279();
         return 1;
      })));
      builder.then(literal("del").then(arg("block", SearchArgumentType.create()).executes((context) -> {
         String blockName = (String)context.getArgument("block", String.class);
         class_2248 result = getRegisteredBlock(blockName);
         String var10000;
         if (result != null) {
            ((ItemSelectSetting)ModuleManager.blockESP.selectedBlocks.getValue()).remove(result);
            var10000 = String.valueOf(class_124.field_1060);
            sendMessage(var10000 + blockName + (ClientSettings.isRu() ? " удален из BlockESP" : " removed from BlockESP"));
         } else {
            var10000 = String.valueOf(class_124.field_1061);
            sendMessage(var10000 + (ClientSettings.isRu() ? "Такого блока нет!" : "There is no such block!"));
         }

         mc.field_1769.method_3279();
         return 1;
      })));
      builder.executes((context) -> {
         if (((ItemSelectSetting)ModuleManager.blockESP.selectedBlocks.getValue()).getItemsById().isEmpty()) {
            sendMessage("BlockESP list empty");
         } else {
            StringBuilder f = new StringBuilder("BlockESP list: ");
            Iterator var2 = ((ItemSelectSetting)ModuleManager.blockESP.selectedBlocks.getValue()).getItemsById().iterator();

            while(var2.hasNext()) {
               String name = (String)var2.next();

               try {
                  f.append(name).append(", ");
               } catch (Exception var5) {
               }
            }

            sendMessage(f.toString());
         }

         return 1;
      });
   }

   public static class_2248 getRegisteredBlock(String blockName) {
      Iterator var1 = class_7923.field_41175.iterator();

      class_2248 block;
      do {
         if (!var1.hasNext()) {
            return null;
         }

         block = (class_2248)var1.next();
      } while(!block.method_9539().replace("block.minecraft.", "").equalsIgnoreCase(blockName.replace("block.minecraft.", "")));

      return block;
   }
}
