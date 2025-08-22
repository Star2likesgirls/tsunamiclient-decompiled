package tsunami.features.cmd.impl;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.class_124;
import net.minecraft.class_2172;
import org.jetbrains.annotations.NotNull;
import tsunami.core.Managers;
import tsunami.features.cmd.Command;
import tsunami.features.modules.client.ClientSettings;

public class PrefixCommand extends Command {
   public PrefixCommand() {
      super("prefix");
   }

   public void executeBuild(@NotNull LiteralArgumentBuilder<class_2172> builder) {
      builder.then(literal("set").then(arg("prefix", StringArgumentType.greedyString()).executes((context) -> {
         String prefix = (String)context.getArgument("prefix", String.class);
         Managers.COMMAND.setPrefix(prefix);
         String var10000 = String.valueOf(class_124.field_1060);
         sendMessage(var10000 + (ClientSettings.isRu() ? "Префикс изменен на " : "Changed prefix to ") + prefix);
         ClientSettings.prefix.setValue(prefix);
         return 1;
      })));
      builder.executes((context) -> {
         String var10000 = String.valueOf(class_124.field_1060);
         sendMessage(var10000 + (ClientSettings.isRu() ? "Текущий префикс: " : "Current prefix: ") + Managers.COMMAND.getPrefix());
         return 1;
      });
   }
}
