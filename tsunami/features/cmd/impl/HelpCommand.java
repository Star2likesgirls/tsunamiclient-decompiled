package tsunami.features.cmd.impl;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import java.util.concurrent.atomic.AtomicBoolean;
import net.minecraft.class_124;
import net.minecraft.class_2172;
import net.minecraft.class_2561;
import net.minecraft.class_746;
import org.jetbrains.annotations.NotNull;
import tsunami.core.Managers;
import tsunami.features.cmd.Command;

public class HelpCommand extends Command {
   public HelpCommand() {
      super("help");
   }

   public void executeBuild(@NotNull LiteralArgumentBuilder<class_2172> builder) {
      builder.executes((context) -> {
         sendMessage("Commands: \n");
         AtomicBoolean flip = new AtomicBoolean(false);
         Managers.COMMAND.getCommands().forEach((command) -> {
            class_746 var10000 = mc.field_1724;
            String var10001 = String.valueOf(flip.get() ? class_124.field_1076 : class_124.field_1064);
            var10000.method_43496(class_2561.method_30163(var10001 + Managers.COMMAND.getPrefix() + String.valueOf(flip.get() ? class_124.field_1075 : class_124.field_1062) + command.getName() + (command.getAliases().isEmpty() ? "" : " (" + command.getAliases() + ")") + String.valueOf(class_124.field_1063) + " -> " + String.valueOf(flip.get() ? class_124.field_1068 : class_124.field_1080) + command.getDescription()));
            flip.set(!flip.get());
         });
         return 1;
      });
   }
}
