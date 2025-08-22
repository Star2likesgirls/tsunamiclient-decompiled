package tsunami.features.cmd.impl;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import net.minecraft.class_124;
import net.minecraft.class_1799;
import net.minecraft.class_1812;
import net.minecraft.class_1844;
import net.minecraft.class_2172;
import net.minecraft.class_9334;
import org.jetbrains.annotations.NotNull;
import tsunami.core.manager.client.ModuleManager;
import tsunami.features.cmd.Command;
import tsunami.features.modules.client.ClientSettings;

public class KitCommand extends Command {
   private static final String PATH = "ThunderHackRecode/misc/AutoGear.json";

   public KitCommand() {
      super("kit");
   }

   public void executeBuild(@NotNull LiteralArgumentBuilder<class_2172> builder) {
      builder.then(literal("list").executes((context) -> {
         this.listMessage();
         return 1;
      }));
      builder.then(literal("create").then(arg("name", StringArgumentType.word()).executes((context) -> {
         this.save((String)context.getArgument("name", String.class));
         return 1;
      })));
      builder.then(literal("set").then(arg("name", StringArgumentType.word()).executes((context) -> {
         this.set((String)context.getArgument("name", String.class));
         return 1;
      })));
      builder.then(literal("del").then(arg("name", StringArgumentType.word()).executes((context) -> {
         this.delete((String)context.getArgument("name", String.class));
         return 1;
      })));
      builder.executes((context) -> {
         sendMessage("kit <create/set/del/list> <name>");
         return 1;
      });
   }

   public static String getSelectedKit() {
      try {
         JsonObject json = (new JsonParser()).parse(new FileReader("ThunderHackRecode/misc/AutoGear.json")).getAsJsonObject();
         if (!json.get("selected").getAsString().equals("none")) {
            return json.get("selected").getAsString();
         }
      } catch (Exception var1) {
      }

      sendMessage(ClientSettings.isRu() ? "Кит не найден" : "Kit not found");
      return "";
   }

   public static String getKitItems(String kit) {
      try {
         JsonObject json = (new JsonParser()).parse(new FileReader("ThunderHackRecode/misc/AutoGear.json")).getAsJsonObject();
         return json.get(kit).getAsString();
      } catch (Exception var2) {
         sendMessage(ClientSettings.isRu() ? "Кит не найден" : "Kit not found");
         return "";
      }
   }

   private void listMessage() {
      try {
         JsonObject json = (new JsonParser()).parse(new FileReader("ThunderHackRecode/misc/AutoGear.json")).getAsJsonObject();
         sendMessage(ClientSettings.isRu() ? "Доступные киты:" : "Available kits:");

         for(int i = 0; i < json.entrySet().size(); ++i) {
            String item = json.entrySet().toArray()[i].toString().split("=")[0];
            String var10000 = String.valueOf(class_124.field_1080);
            sendMessage(var10000 + "-> " + item + (item.equals("selected") ? (ClientSettings.isRu() ? "(Выбран)" : " (Selected)") : ""));
         }
      } catch (Exception var4) {
         sendMessage(ClientSettings.isRu() ? "Проблема с конфигурацией китов!" : "Error with kit cfg!");
      }

   }

   private void delete(String name) {
      try {
         JsonObject json = (new JsonParser()).parse(new FileReader("ThunderHackRecode/misc/AutoGear.json")).getAsJsonObject();
         if (json.get(name) != null && !name.equals("selected")) {
            json.remove(name);
            if (json.get("selected").getAsString().equals(name)) {
               json.addProperty("selected", "none");
            }

            this.saveFile(json, name, ClientSettings.isRu() ? "удален" : "deleted");
         } else {
            sendMessage("Kit not found");
         }
      } catch (Exception var3) {
         sendMessage(ClientSettings.isRu() ? "Кит не найден" : "Kit not found");
      }

   }

   private void set(String name) {
      try {
         JsonObject json = (new JsonParser()).parse(new FileReader("ThunderHackRecode/misc/AutoGear.json")).getAsJsonObject();
         if (json.get(name) != null && !name.equals("selected")) {
            json.addProperty("selected", name);
            this.saveFile(json, name, ClientSettings.isRu() ? "выбран" : "selected");
            ModuleManager.autoGear.setup();
         } else {
            sendMessage(ClientSettings.isRu() ? "Кит не найден" : "Kit not found");
         }
      } catch (Exception var3) {
         sendMessage(ClientSettings.isRu() ? "Кит не найден" : "Kit not found");
      }

   }

   private void save(String name) {
      JsonObject json = new JsonObject();

      try {
         json = (new JsonParser()).parse(new FileReader("ThunderHackRecode/misc/AutoGear.json")).getAsJsonObject();
         if (json.get(name) != null && !name.equals("selected")) {
            sendMessage(ClientSettings.isRu() ? "Этот кит уже существует" : "This kit arleady exist");
            return;
         }
      } catch (IOException var6) {
         json.addProperty("selected", "none");
      }

      StringBuilder jsonInventory = new StringBuilder();
      Iterator var4 = mc.field_1724.method_31548().field_7547.iterator();

      while(var4.hasNext()) {
         class_1799 item = (class_1799)var4.next();
         jsonInventory.append(item.method_7909() instanceof class_1812 ? item.method_7909().method_7876() + ((class_1844)item.method_7909().method_57347().method_57829(class_9334.field_49651)).method_8064() : item.method_7909().method_7876()).append(" ");
      }

      json.addProperty(name, jsonInventory.toString());
      this.saveFile(json, name, ClientSettings.isRu() ? "сохранен" : "saved");
   }

   private void saveFile(@NotNull JsonObject completeJson, String name, String operation) {
      try {
         File file = new File("ThunderHackRecode/misc/AutoGear.json");

         try {
            file.createNewFile();
         } catch (Exception var6) {
         }

         BufferedWriter bw = new BufferedWriter(new FileWriter("ThunderHackRecode/misc/AutoGear.json"));
         bw.write(completeJson.toString());
         bw.close();
         String var10000 = ClientSettings.isRu() ? "Кит " : "Kit ";
         sendMessage(var10000 + String.valueOf(class_124.field_1075) + name + String.valueOf(class_124.field_1070) + " " + operation);
      } catch (IOException var7) {
         sendMessage(ClientSettings.isRu() ? "Ошибка сохранения файла" : "Error saving the file");
      }

   }
}
