package tsunami.features.cmd.impl;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import net.minecraft.class_124;
import net.minecraft.class_156;
import net.minecraft.class_2172;
import org.jetbrains.annotations.NotNull;
import tsunami.core.Managers;
import tsunami.features.cmd.Command;
import tsunami.features.cmd.args.CategoryArgumentType;
import tsunami.features.cmd.args.CfgArgumentType;
import tsunami.features.cmd.args.ModuleArgumentType;
import tsunami.features.modules.Module;

public class CfgCommand extends Command {
   public CfgCommand() {
      super("cfg", "config");
   }

   public void executeBuild(@NotNull LiteralArgumentBuilder<class_2172> builder) {
      builder.executes((context) -> {
         StringBuilder configs = new StringBuilder("Configs: ");
         Iterator var2 = ((List)Objects.requireNonNull(Managers.CONFIG.getConfigList())).iterator();

         while(var2.hasNext()) {
            String str = (String)var2.next();
            boolean var10001 = str.equals(Managers.CONFIG.getCurrentConfig().getName().replace(".th", ""));
            configs.append("\n- " + String.valueOf(var10001 ? class_124.field_1060 : "")).append(str).append(class_124.field_1070);
         }

         sendMessage(configs.toString());
         return 1;
      });
      builder.then(literal("list").executes((context) -> {
         StringBuilder configs = new StringBuilder("Configs: ");
         Iterator var2 = ((List)Objects.requireNonNull(Managers.CONFIG.getConfigList())).iterator();

         while(var2.hasNext()) {
            String str = (String)var2.next();
            boolean var10001 = str.equals(Managers.CONFIG.getCurrentConfig().getName().replace(".th", ""));
            configs.append("\n- " + String.valueOf(var10001 ? class_124.field_1060 : "")).append(str).append(class_124.field_1070);
         }

         sendMessage(configs.toString());
         return 1;
      }));
      builder.then(literal("dir").executes((context) -> {
         try {
            class_156.method_668().method_673((new File("ThunderHackRecode/configs/")).toURI());
         } catch (Exception var2) {
            var2.printStackTrace();
         }

         return 1;
      }));
      builder.then(literal("save").then(arg("name", StringArgumentType.word()).executes((context) -> {
         Managers.CONFIG.save((String)context.getArgument("name", String.class));
         return 1;
      })));
      builder.then(literal("loadcloud").then(arg("name", StringArgumentType.word()).executes((context) -> {
         Managers.CONFIG.loadCloud((String)context.getArgument("name", String.class));
         return 1;
      })));
      builder.then(literal("cloudlist").executes((context) -> {
         StringBuilder configs = new StringBuilder("Cloud Configs: \n");
         Iterator var2 = ((List)Objects.requireNonNull(Managers.CONFIG.getCloudConfigs())).iterator();

         while(var2.hasNext()) {
            String str = (String)var2.next();
            String[] split = str.split(";");
            String var10001 = String.valueOf(class_124.field_1067);
            configs.append("\n- " + var10001 + split[0] + String.valueOf(class_124.field_1070) + String.valueOf(class_124.field_1080) + " author: " + String.valueOf(class_124.field_1070) + split[1] + String.valueOf(class_124.field_1080) + " last updated: " + String.valueOf(class_124.field_1070) + split[2]);
         }

         sendMessage(configs.toString());
         return 1;
      }));
      builder.then(literal("cloud").then(arg("name", StringArgumentType.word()).executes((context) -> {
         Managers.CONFIG.loadCloud((String)context.getArgument("name", String.class));
         return 1;
      })));
      builder.then(literal("set").then(((RequiredArgumentBuilder)arg("name", CfgArgumentType.create()).then(arg("module", ModuleArgumentType.create()).executes((context) -> {
         Managers.CONFIG.loadModuleOnly((String)context.getArgument("name", String.class), (Module)context.getArgument("module", Module.class));
         return 1;
      }))).executes((context) -> {
         Managers.CONFIG.load((String)context.getArgument("name", String.class));
         return 1;
      })));
      builder.then(literal("load").then(((RequiredArgumentBuilder)arg("name", CfgArgumentType.create()).then(arg("module", ModuleArgumentType.create()).executes((context) -> {
         Managers.CONFIG.loadModuleOnly((String)context.getArgument("name", String.class), (Module)context.getArgument("module", Module.class));
         return 1;
      }))).executes((context) -> {
         Managers.CONFIG.load((String)context.getArgument("name", String.class));
         return 1;
      })));
      builder.then(literal("loadCategory").then(arg("name", CfgArgumentType.create()).then(arg("category", CategoryArgumentType.create()).executes((context) -> {
         Managers.CONFIG.load((String)context.getArgument("name", String.class), (String)context.getArgument("category", String.class));
         return 1;
      }))));
      builder.then(literal("loadBinds").then(arg("name", CfgArgumentType.create()).executes((context) -> {
         Managers.CONFIG.loadBinds((String)context.getArgument("name", String.class));
         return 1;
      })));
   }
}
