package tsunami.features.cmd.impl;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.class_124;
import net.minecraft.class_2172;
import net.minecraft.class_2561;
import net.minecraft.class_268;
import net.minecraft.class_640;
import tsunami.core.manager.client.ConfigManager;
import tsunami.features.cmd.Command;
import tsunami.features.modules.client.ClientSettings;

public class TabParseCommand extends Command {
   public TabParseCommand() {
      super("tabparse");
   }

   public void executeBuild(LiteralArgumentBuilder<class_2172> builder) {
      builder.executes((context) -> {
         String serverIP = "unknown_server";
         if (mc.method_1562().method_45734() != null && mc.method_1562().method_45734().field_3761 != null) {
            serverIP = mc.method_1562().method_45734().field_3761.replace(':', '_');
         }

         String randomSuffix = this.generateRandomString(5);
         File dir = new File(ConfigManager.TABPARSER_FOLDER, serverIP);
         if (!dir.exists()) {
            dir.mkdirs();
         }

         String fileName = serverIP + "-" + (new SimpleDateFormat("dd.MM.yyyy")).format(new Date()) + "-" + randomSuffix + ".txt";
         File file = new File(dir, fileName);

         try {
            file.createNewFile();
            OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
            writer.write("========================\n\n");
            writer.write("Server: " + mc.method_1562().method_45734().field_3761 + "\n");
            SimpleDateFormat var10001 = new SimpleDateFormat("dd.MM.yyyy");
            Date var10002 = new Date();
            writer.write("Date: " + var10001.format(var10002) + "\n\n");
            writer.write("========================\n\n");
            List<class_640> sortedPlayers = new ArrayList(mc.method_1562().method_2880());
            sortedPlayers.sort((player1, player2) -> {
               String prefix1 = player1.method_2955().method_1144().getString();
               String prefix2 = player2.method_2955().method_1144().getString();
               return prefix2.compareTo(prefix1);
            });
            Iterator var9 = sortedPlayers.iterator();

            while(var9.hasNext()) {
               class_640 entry = (class_640)var9.next();
               String var12 = class_268.method_1142(entry.method_2955(), class_2561.method_43470(entry.method_2966().getName())).getString();
               writer.write(var12 + "\n");
            }

            writer.close();
            sendMessage(ClientSettings.isRu() ? String.valueOf(class_124.field_1060) + "Таб успешно сохранен в " + file.getPath() : String.valueOf(class_124.field_1060) + "Tab was successfully saved in " + file.getPath());
         } catch (IOException var11) {
            var11.printStackTrace();
         }

         return 1;
      });
   }

   private String generateRandomString(int length) {
      char[] chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
      StringBuilder sb = new StringBuilder();
      Random random = new Random();

      for(int i = 0; i < length; ++i) {
         char c = chars[random.nextInt(chars.length)];
         sb.append(c);
      }

      return sb.toString();
   }

   private String getPlayerPrefix(class_640 playerInfo) {
      return playerInfo.method_2971() != null ? playerInfo.method_2971().getString() : "";
   }
}
