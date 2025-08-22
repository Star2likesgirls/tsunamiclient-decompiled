package tsunami.features.modules.misc;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.class_2586;
import net.minecraft.class_2595;
import net.minecraft.class_2627;
import net.minecraft.class_2818;
import net.minecraft.class_3417;
import net.minecraft.class_3419;
import tsunami.core.Managers;
import tsunami.core.manager.client.ConfigManager;
import tsunami.features.modules.Module;
import tsunami.features.modules.render.StorageEsp;
import tsunami.gui.notification.Notification;
import tsunami.setting.Setting;

public class StashLogger extends Module {
   private final Setting<Boolean> sound = new Setting("Sound", true);
   private final Setting<Boolean> saveToFile = new Setting("SaveToFile", true);
   private final Setting<Integer> minChests = new Setting("MinChests", 5, 0, 100);
   private final Setting<Integer> minShulkers = new Setting("MinShulkers", 0, 0, 100);
   private List<class_2818> savedChunks = new ArrayList();

   public StashLogger() {
      super("StashFinder", Module.Category.DONUT);
   }

   public void onEnable() {
      this.savedChunks.clear();
   }

   public void onUpdate() {
      Iterator var1 = StorageEsp.getLoadedChunks().iterator();

      while(true) {
         class_2818 chunk;
         do {
            if (!var1.hasNext()) {
               return;
            }

            chunk = (class_2818)var1.next();
         } while(this.savedChunks.contains(chunk));

         List<class_2586> storages = chunk.method_12214().values().stream().toList();
         int chests = 0;
         int shulkers = 0;
         Iterator var6 = storages.iterator();

         while(var6.hasNext()) {
            class_2586 storage = (class_2586)var6.next();
            if (storage instanceof class_2595) {
               ++chests;
            }

            if (storage instanceof class_2627) {
               ++shulkers;
            }
         }

         if (chests >= (Integer)this.minChests.getValue() && shulkers >= (Integer)this.minShulkers.getValue()) {
            this.savedChunks.add(chunk);
            String str = "Stash pos: X:" + chunk.method_12004().method_33940() + " Z:" + chunk.method_12004().method_33942() + " Chests: " + chests + " Shulkers: " + shulkers;
            Managers.NOTIFICATION.publicity("StashLogger", str, 5, Notification.Type.SUCCESS);
            this.sendMessage(str);
            if ((Boolean)this.sound.getValue()) {
               mc.field_1687.method_8396(mc.field_1724, mc.field_1724.method_24515(), class_3417.field_14709, class_3419.field_15245, 1.0F, 1.0F);
            }

            String serverIP = "unknown_server";
            if (mc.method_1562().method_45734() != null && mc.method_1562().method_45734().field_3761 != null) {
               serverIP = mc.method_1562().method_45734().field_3761.replace(':', '_');
            }

            if ((Boolean)this.saveToFile.getValue()) {
               try {
                  BufferedWriter writer = new BufferedWriter(new FileWriter(new File(ConfigManager.STASHLOGGER_FOLDER, serverIP + ".txt"), true));
                  int var10001 = chunk.method_12004().method_33940();
                  writer.append("\nStash pos: X:" + var10001 + " Z:" + chunk.method_12004().method_33942() + " Chests: " + chests + " Shulkers: " + shulkers);
                  writer.close();
               } catch (Exception var9) {
               }
            }
         }
      }
   }
}
