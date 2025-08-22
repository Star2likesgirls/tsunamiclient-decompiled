package tsunami.features.cmd.impl;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.class_124;
import net.minecraft.class_2172;
import org.jetbrains.annotations.NotNull;
import tsunami.features.cmd.Command;
import tsunami.features.cmd.args.ModuleArgumentType;
import tsunami.features.modules.Module;
import tsunami.features.modules.client.ClientSettings;

public class DrawCommand extends Command {
   public DrawCommand() {
      super("draw");
   }

   public void executeBuild(@NotNull LiteralArgumentBuilder<class_2172> builder) {
      builder.then(arg("module", ModuleArgumentType.create()).executes((context) -> {
         Module module = (Module)context.getArgument("module", Module.class);
         module.setDrawn(!module.isDrawn());
         String var10000;
         if (ClientSettings.isRu()) {
            var10000 = String.valueOf(class_124.field_1060);
            sendMessage("Модуль " + var10000 + module.getName() + String.valueOf(class_124.field_1068) + " теперь " + (module.isDrawn() ? "виден в ArrayList" : "не виден в ArrayList"));
         } else {
            var10000 = String.valueOf(class_124.field_1060);
            sendMessage(var10000 + module.getName() + String.valueOf(class_124.field_1068) + " is now " + (module.isDrawn() ? "visible in ArrayList" : "invisible in ArrayList"));
         }

         return 1;
      }));
   }
}
