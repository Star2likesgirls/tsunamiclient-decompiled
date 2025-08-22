package tsunami.features.modules.misc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import java.util.Random;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_7439;
import tsunami.events.impl.EventDeath;
import tsunami.events.impl.PacketEvent;
import tsunami.features.modules.Module;
import tsunami.features.modules.client.ClientSettings;
import tsunami.features.modules.combat.Aura;
import tsunami.features.modules.combat.AutoCrystal;
import tsunami.setting.Setting;
import tsunami.utility.ThunderUtility;

public final class AutoEZ extends Module {
   public static ArrayList<String> EZWORDS = new ArrayList();
   public Setting<Boolean> global = new Setting("global", true);
   String[] EZ = new String[]{"%player% АНБРЕЙН ГЕТАЙ ТХ РЕКОД", "%player% ТВОЯ МАТЬ БУДЕТ СЛЕДУЮЩЕЙ))))", "%player% БИЧАРА БЕЗ ТХ", "%player% ЧЕ ТАК БЫСТРО СЛИЛСЯ ТО А?", "%player% ПЛАЧЬ", "%player% УПССС ЗАБЫЛ КИЛЛКУ ВЫРУБИТЬ", "ОДНОКЛЕТОЧНЫЙ %player% БЫЛ ВПЕНЕН", "%player% ИЗИ БЛЯТЬ АХААХАХАХАХААХ", "%player% БОЖЕ МНЕ ТЕБЯ ЖАЛКО ВГЕТАЙ ТХ", "%player% ОПРАВДЫВАЙСЯ В ХУЙ ЧЕ СДОХ ТО)))", "%player% СПС ЗА ОТСОС)))"};
   private final Setting<AutoEZ.ModeEn> mode;
   private final Setting<AutoEZ.ServerMode> server;

   public AutoEZ() {
      super("AutoEZ", Module.Category.NONE);
      this.mode = new Setting("Mode", AutoEZ.ModeEn.Basic);
      this.server = new Setting("Server", AutoEZ.ServerMode.Universal);
      loadEZ();
   }

   public static void loadEZ() {
      try {
         File file = new File("ThunderHackRecode/misc/AutoEZ.txt");
         if (!file.exists()) {
            file.createNewFile();
         }

         (new Thread(() -> {
            try {
               FileInputStream fis = new FileInputStream(file);
               InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
               BufferedReader reader = new BufferedReader(isr);
               ArrayList lines = new ArrayList();

               String line;
               while((line = reader.readLine()) != null) {
                  lines.add(line);
               }

               boolean newline = false;
               Iterator var7 = lines.iterator();

               while(var7.hasNext()) {
                  String l = (String)var7.next();
                  if (l.isEmpty()) {
                     newline = true;
                     break;
                  }
               }

               EZWORDS.clear();
               ArrayList<String> spamList = new ArrayList();
               if (!newline) {
                  spamList.addAll(lines);
               } else {
                  StringBuilder spamChunk = new StringBuilder();
                  Iterator var9 = lines.iterator();

                  while(var9.hasNext()) {
                     String lx = (String)var9.next();
                     if (lx.isEmpty()) {
                        if (!spamChunk.isEmpty()) {
                           spamList.add(spamChunk.toString());
                           spamChunk = new StringBuilder();
                        }
                     } else {
                        spamChunk.append(lx).append(" ");
                     }
                  }

                  spamList.add(spamChunk.toString());
               }

               EZWORDS = spamList;
            } catch (Exception var11) {
            }

         })).start();
      } catch (IOException var1) {
      }

   }

   public void onEnable() {
      loadEZ();
   }

   @EventHandler
   public void onPacketReceive(PacketEvent.Receive e) {
      if (!fullNullCheck()) {
         if (this.server.getValue() != AutoEZ.ServerMode.Universal) {
            if (e.getPacket() instanceof class_7439) {
               class_7439 packet = (class_7439)e.getPacket();
               if (packet.comp_763().getString().contains("Вы убили игрока")) {
                  String name = ThunderUtility.solveName(packet.comp_763().getString());
                  if (Objects.equals(name, "FATAL ERROR")) {
                     return;
                  }

                  String finalword;
                  if (this.mode.getValue() == AutoEZ.ModeEn.Basic) {
                     int n = (int)Math.floor(Math.random() * (double)this.EZ.length);
                     finalword = this.EZ[n].replace("%player%", name);
                  } else {
                     if (EZWORDS.isEmpty()) {
                        this.sendMessage(ClientSettings.isRu() ? "Файл с AutoEZ пустой!" : "AutoEZ.txt is empty!");
                        return;
                     }

                     finalword = (String)EZWORDS.get((new Random()).nextInt(EZWORDS.size()));
                     finalword = finalword.replaceAll("%player%", name);
                  }

                  mc.field_1724.field_3944.method_45729((Boolean)this.global.getValue() ? "!" + finalword : finalword);
               }
            }

         }
      }
   }

   @EventHandler
   public void onDeath(EventDeath e) {
      if (this.server.getValue() == AutoEZ.ServerMode.Universal) {
         if (Aura.target != null && Aura.target == e.getPlayer()) {
            this.sayEZ(e.getPlayer().method_5477().getString());
         } else {
            if (AutoCrystal.target != null && AutoCrystal.target == e.getPlayer()) {
               this.sayEZ(e.getPlayer().method_5477().getString());
            }

         }
      }
   }

   public void sayEZ(String pn) {
      String finalword;
      if (this.mode.getValue() == AutoEZ.ModeEn.Basic) {
         int n = (int)Math.floor(Math.random() * (double)this.EZ.length);
         finalword = this.EZ[n].replace("%player%", pn);
      } else {
         if (EZWORDS.isEmpty()) {
            this.sendMessage(ClientSettings.isRu() ? "Файл с AutoEZ пустой!" : "AutoEZ.txt is empty!");
            return;
         }

         finalword = (String)EZWORDS.get((new Random()).nextInt(EZWORDS.size()));
         finalword = finalword.replaceAll("%player%", pn);
      }

      mc.field_1724.field_3944.method_45729((Boolean)this.global.getValue() ? "!" + finalword : finalword);
   }

   public static enum ModeEn {
      Custom,
      Basic;

      // $FF: synthetic method
      private static AutoEZ.ModeEn[] $values() {
         return new AutoEZ.ModeEn[]{Custom, Basic};
      }
   }

   public static enum ServerMode {
      Universal,
      FunnyGame;

      // $FF: synthetic method
      private static AutoEZ.ServerMode[] $values() {
         return new AutoEZ.ServerMode[]{Universal, FunnyGame};
      }
   }
}
