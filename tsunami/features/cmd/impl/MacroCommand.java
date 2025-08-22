package tsunami.features.cmd.impl;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import java.lang.reflect.Field;
import net.minecraft.class_2172;
import net.minecraft.class_3675;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;
import tsunami.core.Managers;
import tsunami.core.manager.client.MacroManager;
import tsunami.features.cmd.Command;
import tsunami.features.cmd.args.MacroArgumentType;
import tsunami.features.modules.client.ClientSettings;

public class MacroCommand extends Command {
   public MacroCommand() {
      super("macro", "macros");
   }

   public void executeBuild(@NotNull LiteralArgumentBuilder<class_2172> builder) {
      builder.then(literal("list").executes((context) -> {
         sendMessage(ClientSettings.isRu() ? "Макросы:" : "Macros:");
         sendMessage(" ");
         Managers.MACRO.getMacros().forEach((macro) -> {
            String var10000 = macro.getName();
            sendMessage(var10000 + (macro.getBind() != -1 ? " [" + this.toString(macro.getBind()) + "]" : "") + " {" + macro.getText() + "}");
         });
         return 1;
      }));
      builder.then(literal("remove").then(arg("macro", MacroArgumentType.create()).executes((context) -> {
         MacroManager.Macro macro = (MacroManager.Macro)context.getArgument("macro", MacroManager.Macro.class);
         if (macro == null) {
            sendMessage(ClientSettings.isRu() ? "Не существует такого макроса!" : "Wrong macro name!");
            return 1;
         } else {
            Managers.MACRO.removeMacro(macro);
            String var10000 = ClientSettings.isRu() ? "Удален макрос " : "Removed macro ";
            sendMessage(var10000 + macro.getName());
            return 1;
         }
      })));
      builder.then(literal("add").then(arg("name", StringArgumentType.word()).then(arg("bind", StringArgumentType.word()).then(arg("args", StringArgumentType.greedyString()).executes((context) -> {
         String name = (String)context.getArgument("name", String.class);
         String bind = ((String)context.getArgument("bind", String.class)).toUpperCase();
         String args = (String)context.getArgument("args", String.class);
         if (class_3675.method_15981("key.keyboard." + bind.toLowerCase()).method_1444() == -1) {
            sendMessage(ClientSettings.isRu() ? "Неправильный бинд!" : "Wrong bind!");
            return 1;
         } else {
            MacroManager.Macro macro = new MacroManager.Macro(name, args, class_3675.method_15981("key.keyboard." + bind.toLowerCase()).method_1444());
            MacroManager.addMacro(macro);
            sendMessage(ClientSettings.isRu() ? "Добавлен макрос " + name + " на кнопку " + this.toString(macro.getBind()) : "Added macro " + name + " to " + this.toString(macro.getBind()));
            return 1;
         }
      })))));
      builder.executes((context) -> {
         sendMessage(this.usage());
         return 1;
      });
   }

   public String toString(int key) {
      String kn = key > 0 ? GLFW.glfwGetKeyName(key, GLFW.glfwGetKeyScancode(key)) : "None";
      if (kn == null) {
         try {
            Field[] var3 = GLFW.class.getDeclaredFields();
            int var4 = var3.length;

            for(int var5 = 0; var5 < var4; ++var5) {
               Field declaredField = var3[var5];
               if (declaredField.getName().startsWith("GLFW_KEY_")) {
                  int a = (Integer)declaredField.get((Object)null);
                  if (a == key) {
                     String nb = declaredField.getName().substring("GLFW_KEY_".length());
                     String var10000 = nb.substring(0, 1).toUpperCase();
                     kn = var10000 + nb.substring(1).toLowerCase();
                  }
               }
            }
         } catch (Exception var9) {
            kn = "unknown." + key;
         }
      }

      return key == -1 ? "None" : kn.makeConcatWithConstants<invokedynamic>(kn).toUpperCase();
   }

   String usage() {
      return "macro add/remove/list (macro add name key text), (macro remove name)";
   }
}
