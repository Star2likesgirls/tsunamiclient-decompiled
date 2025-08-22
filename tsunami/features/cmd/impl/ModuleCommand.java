package tsunami.features.cmd.impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import java.util.Iterator;
import java.util.Objects;
import net.minecraft.class_1074;
import net.minecraft.class_124;
import net.minecraft.class_2172;
import org.jetbrains.annotations.NotNull;
import tsunami.core.Managers;
import tsunami.features.cmd.Command;
import tsunami.features.cmd.args.ModuleArgumentType;
import tsunami.features.cmd.args.SettingArgumentType;
import tsunami.features.modules.Module;
import tsunami.features.modules.client.ClientSettings;
import tsunami.setting.Setting;
import tsunami.setting.impl.BooleanSettingGroup;
import tsunami.setting.impl.ColorSetting;
import tsunami.setting.impl.EnumConverter;
import tsunami.setting.impl.PositionSetting;

public class ModuleCommand extends Command {
   public ModuleCommand() {
      super("module", "modules");
   }

   public void executeBuild(@NotNull LiteralArgumentBuilder<class_2172> builder) {
      builder.then(arg("module", ModuleArgumentType.create()).executes((context) -> {
         Module module = (Module)context.getArgument("module", Module.class);
         String var10000 = module.getDisplayName();
         sendMessage(var10000 + " : " + class_1074.method_4662(module.getDescription(), new Object[0]));
         Iterator var2 = module.getSettings().iterator();

         while(var2.hasNext()) {
            Setting<?> setting2 = (Setting)var2.next();
            var10000 = setting2.getName();
            sendMessage(var10000 + " : " + String.valueOf(setting2.getValue()));
         }

         return 1;
      }));
      builder.then(((RequiredArgumentBuilder)arg("module", ModuleArgumentType.create()).then(literal("reset").executes((context) -> {
         Module module = (Module)context.getArgument("module", Module.class);
         Iterator var2 = module.getSettings().iterator();

         while(var2.hasNext()) {
            Setting s = (Setting)var2.next();
            Object patt0$temp = s.getValue();
            if (patt0$temp instanceof ColorSetting) {
               ColorSetting cs = (ColorSetting)patt0$temp;
               cs.setDefault();
            } else {
               s.setValue(s.getDefaultValue());
            }
         }

         return 1;
      }))).then(arg("setting", SettingArgumentType.create()).then(arg("settingValue", StringArgumentType.greedyString()).executes((context) -> {
         Module module = (Module)context.getArgument("module", Module.class);
         String settingName = (String)context.getArgument("setting", String.class);
         String settingValue = (String)context.getArgument("settingValue", String.class);
         Setting setting = null;
         Iterator var5 = module.getSettings().iterator();

         while(var5.hasNext()) {
            Setting set = (Setting)var5.next();
            if (Objects.equals(set.getName(), settingName)) {
               setting = set;
            }
         }

         if (setting == null) {
            sendMessage("No such setting");
            return 1;
         } else {
            JsonParser jp = new JsonParser();
            String var10000;
            if (setting.getValue().getClass().getSimpleName().equalsIgnoreCase("String")) {
               setting.setValue(settingValue);
               var10000 = String.valueOf(class_124.field_1063);
               sendMessage(var10000 + module.getName() + " " + setting.getName() + (ClientSettings.isRu() ? " был выставлен " : " has been set to ") + settingValue);
               return 1;
            } else {
               try {
                  if (setting.getName().equalsIgnoreCase("Enabled")) {
                     if (settingValue.equalsIgnoreCase("true")) {
                        module.enable();
                     }

                     if (settingValue.equalsIgnoreCase("false")) {
                        module.disable();
                     }
                  }

                  setCommandValue(module, setting, jp.parse(settingValue));
               } catch (Exception var7) {
                  var10000 = ClientSettings.isRu() ? "Неверное значение! Эта настройка требует тип: " : "Bad Value! This setting requires a: ";
                  sendMessage(var10000 + setting.getValue().getClass().getSimpleName());
                  return 1;
               }

               if (settingValue.contains("toggle")) {
                  var10000 = String.valueOf(class_124.field_1080);
                  sendMessage(var10000 + module.getName() + " " + setting.getName() + (ClientSettings.isRu() ? " был переключен" : " has been toggled"));
               } else {
                  var10000 = String.valueOf(class_124.field_1080);
                  sendMessage(var10000 + module.getName() + " " + setting.getName() + (ClientSettings.isRu() ? " был выставлен " : " has been set to ") + settingValue);
               }

               return 1;
            }
         }
      }))));
      builder.executes((context) -> {
         sendMessage("Modules: ");
         Iterator var1 = Managers.MODULE.getCategories().iterator();

         while(var1.hasNext()) {
            Module.Category category = (Module.Category)var1.next();
            StringBuilder modules = new StringBuilder(category.getName() + ": ");
            Iterator var4 = Managers.MODULE.getModulesByCategory(category).iterator();

            while(var4.hasNext()) {
               Module module1 = (Module)var4.next();
               modules.append(module1.isEnabled() ? class_124.field_1060 : class_124.field_1061).append(module1.getName()).append(class_124.field_1068).append(", ");
            }

            sendMessage(modules.toString());
         }

         return 1;
      });
   }

   public static void setCommandValue(@NotNull Module feature, Setting setting, JsonElement element) {
      Iterator var4 = feature.getSettings().iterator();

      while(var4.hasNext()) {
         Setting checkSetting = (Setting)var4.next();
         if (Objects.equals(setting.getName(), checkSetting.getName())) {
            String var6 = checkSetting.getValue().getClass().getSimpleName();
            byte var7 = -1;
            switch(var6.hashCode()) {
            case -1808118735:
               if (var6.equals("String")) {
                  var7 = 7;
               }
               break;
            case -1069951801:
               if (var6.equals("PositionSetting")) {
                  var7 = 9;
               }
               break;
            case -672261858:
               if (var6.equals("Integer")) {
                  var7 = 6;
               }
               break;
            case 2070621:
               if (var6.equals("Bind")) {
                  var7 = 1;
               }
               break;
            case 67973692:
               if (var6.equals("Float")) {
                  var7 = 5;
               }
               break;
            case 120407437:
               if (var6.equals("ColorSetting")) {
                  var7 = 8;
               }
               break;
            case 277673527:
               if (var6.equals("BooleanSettingGroup")) {
                  var7 = 3;
               }
               break;
            case 658947631:
               if (var6.equals("SettingGroup")) {
                  var7 = 0;
               }
               break;
            case 1729365000:
               if (var6.equals("Boolean")) {
                  var7 = 2;
               }
               break;
            case 2052876273:
               if (var6.equals("Double")) {
                  var7 = 4;
               }
            }

            JsonArray array3;
            switch(var7) {
            case 0:
            case 1:
               return;
            case 2:
               if (element.getAsString().equals("toggle")) {
                  checkSetting.setValue(!(Boolean)checkSetting.getValue());
                  return;
               }

               checkSetting.setValue(element.getAsBoolean());
               return;
            case 3:
               ((BooleanSettingGroup)checkSetting.getValue()).setEnabled(element.getAsBoolean());
               break;
            case 4:
               checkSetting.setValue(element.getAsDouble());
               return;
            case 5:
               checkSetting.setValue(element.getAsFloat());
               return;
            case 6:
               checkSetting.setValue(element.getAsInt());
               return;
            case 7:
               String str = element.getAsString();
               checkSetting.setValue(str.replace("_", " "));
               return;
            case 8:
               array3 = element.getAsJsonArray();
               ((ColorSetting)checkSetting.getValue()).setColor(array3.get(0).getAsInt());
               ((ColorSetting)checkSetting.getValue()).setRainbow(array3.get(1).getAsBoolean());
               return;
            case 9:
               array3 = element.getAsJsonArray();
               ((PositionSetting)checkSetting.getValue()).setX(array3.get(0).getAsFloat());
               ((PositionSetting)checkSetting.getValue()).setY(array3.get(1).getAsFloat());
               return;
            default:
               try {
                  EnumConverter converter = new EnumConverter(((Enum)checkSetting.getValue()).getClass());
                  Enum value = converter.doBackward(element);
                  checkSetting.setValue(value == null ? checkSetting.getDefaultValue() : value);
               } catch (Exception var10) {
               }
            }
         }
      }

   }
}
