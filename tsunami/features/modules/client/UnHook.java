package tsunami.features.modules.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.class_124;
import net.minecraft.class_155;
import net.minecraft.class_437;
import net.minecraft.class_8518;
import tsunami.core.Managers;
import tsunami.core.manager.client.ConfigManager;
import tsunami.features.modules.Module;
import tsunami.utility.math.MathUtility;

public class UnHook extends Module {
   List<Module> list;
   public int code = 0;

   public UnHook() {
      super("UnHook", Module.Category.CLIENT);
   }

   public void onEnable() {
      this.code = (int)MathUtility.random(10.0F, 99.0F);

      for(int i = 0; i < 20; ++i) {
         this.sendMessage(ClientSettings.isRu() ? String.valueOf(class_124.field_1061) + "Ща все свернется, напиши в чат " + String.valueOf(class_124.field_1068) + this.code + String.valueOf(class_124.field_1061) + " чтобы все вернуть!" : String.valueOf(class_124.field_1061) + "It's all close now, write to the chat " + String.valueOf(class_124.field_1068) + this.code + String.valueOf(class_124.field_1061) + " to return everything!");
      }

      this.list = Managers.MODULE.getEnabledModules();
      mc.method_1507((class_437)null);
      Managers.ASYNC.run(() -> {
         mc.method_40000(() -> {
            Iterator var1 = this.list.iterator();

            while(var1.hasNext()) {
               Module module = (Module)var1.next();
               if (!module.equals(this)) {
                  module.disable();
               }
            }

            ClientSettings.customMainMenu.setValue(false);

            try {
               mc.method_22683().method_4491(mc.method_45573(), class_155.method_16673().method_48022() ? class_8518.field_44650 : class_8518.field_44651);
            } catch (Exception var10) {
            }

            mc.field_1705.method_1743().method_1808(true);
            this.setEnabled(true);

            try {
               String var10002 = String.valueOf(mc.field_1697);
               File file = new File(var10002 + File.separator + "logs" + File.separator + "latest.log");
               FileInputStream fis = new FileInputStream(file);
               BufferedReader reader = new BufferedReader(new InputStreamReader(fis, StandardCharsets.UTF_8));
               ArrayList lines = new ArrayList();

               String line;
               while((line = reader.readLine()) != null) {
                  if (!line.contains("thunderhack") && !line.contains("ThunderHack") && !line.contains("$$") && !line.contains("\\______/") && !line.contains("By pan4ur, 06ED") && !line.contains("⚡") && !line.contains("thunder.hack")) {
                     lines.add(line);
                  }
               }

               fis.close();

               try {
                  BufferedWriter writer = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8));

                  try {
                     Iterator var7 = lines.iterator();

                     while(var7.hasNext()) {
                        String s = (String)var7.next();
                        writer.write(s + "\n");
                     }
                  } catch (Throwable var11) {
                     try {
                        writer.close();
                     } catch (Throwable var9) {
                        var11.addSuppressed(var9);
                     }

                     throw var11;
                  }

                  writer.close();
               } catch (Exception var12) {
               }

               ConfigManager.MAIN_FOLDER.renameTo(new File("XaeroWaypoints_BACKUP092738"));
            } catch (IOException var13) {
            }

         });
      }, 5000L);
   }

   public void onDisable() {
      if (this.list != null) {
         Iterator var1 = this.list.iterator();

         while(var1.hasNext()) {
            Module module = (Module)var1.next();
            if (!module.equals(this)) {
               module.enable();
            }
         }

         ClientSettings.customMainMenu.setValue(true);

         try {
            (new File("XaeroWaypoints_BACKUP092738")).renameTo(new File("ThunderHackRecode"));
         } catch (Exception var3) {
         }

      }
   }
}
