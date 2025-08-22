package tsunami.features.cmd.impl;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import java.util.Iterator;
import java.util.Objects;
import net.minecraft.class_124;
import net.minecraft.class_2172;
import net.minecraft.class_3675;
import org.jetbrains.annotations.NotNull;
import tsunami.core.Managers;
import tsunami.features.cmd.Command;
import tsunami.features.cmd.args.ModuleArgumentType;
import tsunami.features.hud.impl.KeyBinds;
import tsunami.features.modules.Module;
import tsunami.features.modules.client.ClientSettings;
import tsunami.setting.impl.Bind;

public class BindCommand extends Command {
   public BindCommand() {
      super("bind");
   }

   public void executeBuild(@NotNull LiteralArgumentBuilder<class_2172> builder) {
      builder.then(arg("module", ModuleArgumentType.create()).then(arg("key", StringArgumentType.word()).executes((context) -> {
         Module module = (Module)context.getArgument("module", Module.class);
         String stringKey = (String)context.getArgument("key", String.class);
         String var10000;
         if (stringKey == null) {
            var10000 = module.getName();
            sendMessage(var10000 + " is bound to " + String.valueOf(class_124.field_1080) + module.getBind().getBind());
            return 1;
         } else {
            int key;
            if (!stringKey.equalsIgnoreCase("none") && !stringKey.equalsIgnoreCase("null")) {
               try {
                  key = class_3675.method_15981("key.keyboard." + stringKey.toLowerCase()).method_1444();
               } catch (NumberFormatException var5) {
                  sendMessage(ClientSettings.isRu() ? "Такой кнопки не существует!" : "There is no such button");
                  return 1;
               }
            } else {
               key = -1;
            }

            if (key == 0) {
               sendMessage("Unknown key '" + stringKey + "'!");
               return 1;
            } else {
               module.setBind(key, !stringKey.equals("M") && stringKey.contains("M"), false);
               var10000 = String.valueOf(class_124.field_1060);
               sendMessage("Bind for " + var10000 + module.getName() + String.valueOf(class_124.field_1068) + " set to " + String.valueOf(class_124.field_1080) + stringKey.toUpperCase());
               return 1;
            }
         }
      })));
      builder.then(literal("list").executes((context) -> {
         StringBuilder binds = new StringBuilder("Binds: ");
         Iterator var2 = Managers.MODULE.modules.iterator();

         while(var2.hasNext()) {
            Module feature = (Module)var2.next();
            if (!Objects.equals(feature.getBind().getBind(), "None")) {
               binds.append("\n- ").append(feature.getName()).append(" -> ").append(KeyBinds.getShortKeyName(feature)).append(feature.getBind().isHold() ? "[hold]" : "");
            }
         }

         sendMessage(binds.toString());
         return 1;
      }));
      builder.then(literal("clear").executes((context) -> {
         Iterator var1 = Managers.MODULE.modules.iterator();

         while(var1.hasNext()) {
            Module mod = (Module)var1.next();
            mod.setBind(new Bind(-1, false, false));
         }

         return 1;
      }));
      builder.then(literal("reset").executes((context) -> {
         Iterator var1 = Managers.MODULE.modules.iterator();

         while(var1.hasNext()) {
            Module mod = (Module)var1.next();
            mod.setBind(new Bind(-1, false, false));
         }

         sendMessage("Done!");
         return 1;
      }));
   }
}
