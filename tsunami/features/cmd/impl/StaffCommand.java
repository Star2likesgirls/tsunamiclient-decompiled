package tsunami.features.cmd.impl;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.class_124;
import net.minecraft.class_2172;
import org.jetbrains.annotations.NotNull;
import tsunami.features.cmd.Command;

public class StaffCommand extends Command {
   public static List<String> staffNames = new ArrayList();

   public StaffCommand() {
      super("staff");
   }

   public void executeBuild(@NotNull LiteralArgumentBuilder<class_2172> builder) {
      builder.then(literal("reset").executes((context) -> {
         staffNames.clear();
         sendMessage("staff list got reset.");
         return 1;
      }));
      builder.then(literal("add").then(arg("name", StringArgumentType.word()).executes((context) -> {
         String name = (String)context.getArgument("name", String.class);
         staffNames.add(name);
         String var10000 = String.valueOf(class_124.field_1060);
         sendMessage(var10000 + name + " added to staff list");
         return 1;
      })));
      builder.then(literal("del").then(arg("name", StringArgumentType.word()).executes((context) -> {
         String name = (String)context.getArgument("name", String.class);
         staffNames.remove(name);
         String var10000 = String.valueOf(class_124.field_1060);
         sendMessage(var10000 + name + " removed from staff list");
         return 1;
      })));
      builder.executes((context) -> {
         if (staffNames.isEmpty()) {
            sendMessage("Staff list empty");
         } else {
            StringBuilder f = new StringBuilder("Staff: ");
            Iterator var2 = staffNames.iterator();

            while(var2.hasNext()) {
               String staff = (String)var2.next();

               try {
                  f.append(staff).append(", ");
               } catch (Exception var5) {
               }
            }

            sendMessage(f.toString());
         }

         return 1;
      });
   }
}
