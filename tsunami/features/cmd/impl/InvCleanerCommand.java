package tsunami.features.cmd.impl;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import java.util.Iterator;
import net.minecraft.class_124;
import net.minecraft.class_1792;
import net.minecraft.class_2172;
import net.minecraft.class_2248;
import net.minecraft.class_7923;
import org.jetbrains.annotations.NotNull;
import tsunami.core.manager.client.ModuleManager;
import tsunami.features.cmd.Command;
import tsunami.features.cmd.args.ChestStealerArgumentType;
import tsunami.features.modules.client.ClientSettings;
import tsunami.setting.impl.ItemSelectSetting;

public class InvCleanerCommand extends Command {
   public InvCleanerCommand() {
      super("invcleaner", "cleaner");
   }

   public void executeBuild(@NotNull LiteralArgumentBuilder<class_2172> builder) {
      builder.then(literal("reset").executes((context) -> {
         ((ItemSelectSetting)ModuleManager.inventoryCleaner.items.getValue()).clear();
         sendMessage("InvCleaner got reset.");
         return 1;
      }));
      builder.then(literal("add").then(arg("item", ChestStealerArgumentType.create()).executes((context) -> {
         String blockName = (String)context.getArgument("item", String.class);
         String result = getRegistered(blockName);
         String var10000;
         if (result != null) {
            ((ItemSelectSetting)ModuleManager.inventoryCleaner.items.getValue()).add(result);
            var10000 = String.valueOf(class_124.field_1060);
            sendMessage(var10000 + blockName + (ClientSettings.isRu() ? " добавлен в InvCleaner" : " added to InvCleaner"));
         } else {
            var10000 = String.valueOf(class_124.field_1061);
            sendMessage(var10000 + (ClientSettings.isRu() ? "Такого предмета нет!" : "There is no such item!"));
         }

         return 1;
      })));
      builder.then(literal("del").then(arg("item", ChestStealerArgumentType.create()).executes((context) -> {
         String blockName = (String)context.getArgument("item", String.class);
         String result = getRegistered(blockName);
         String var10000;
         if (result != null) {
            ((ItemSelectSetting)ModuleManager.inventoryCleaner.items.getValue()).remove(result);
            var10000 = String.valueOf(class_124.field_1060);
            sendMessage(var10000 + blockName + (ClientSettings.isRu() ? " удален из InvCleaner" : " removed from InvCleaner"));
         } else {
            var10000 = String.valueOf(class_124.field_1061);
            sendMessage(var10000 + (ClientSettings.isRu() ? "Такого предмета нет!" : "There is no such item!"));
         }

         return 1;
      })));
      builder.executes((context) -> {
         if (((ItemSelectSetting)ModuleManager.inventoryCleaner.items.getValue()).getItemsById().isEmpty()) {
            sendMessage("InvCleaner list empty");
         } else {
            StringBuilder f = new StringBuilder("InvCleaner list: ");
            Iterator var2 = ((ItemSelectSetting)ModuleManager.inventoryCleaner.items.getValue()).getItemsById().iterator();

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

   public static String getRegistered(String Name) {
      Iterator var1 = class_7923.field_41175.iterator();

      class_2248 block;
      do {
         if (!var1.hasNext()) {
            var1 = class_7923.field_41178.iterator();

            class_1792 item;
            do {
               if (!var1.hasNext()) {
                  return null;
               }

               item = (class_1792)var1.next();
            } while(!item.method_7876().replace("item.minecraft.", "").equalsIgnoreCase(Name));

            return item.method_7876().replace("item.minecraft.", "");
         }

         block = (class_2248)var1.next();
      } while(!block.method_9539().replace("block.minecraft.", "").equalsIgnoreCase(Name));

      return block.method_9539().replace("block.minecraft.", "");
   }
}
