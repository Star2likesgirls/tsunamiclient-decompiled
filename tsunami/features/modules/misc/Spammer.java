package tsunami.features.modules.misc;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import org.apache.commons.io.IOUtils;
import tsunami.core.Managers;
import tsunami.features.hud.impl.StaffBoard;
import tsunami.features.modules.Module;
import tsunami.features.modules.client.ClientSettings;
import tsunami.setting.Setting;
import tsunami.utility.Timer;

public class Spammer extends Module {
   public static ArrayList<String> SpamList = new ArrayList();
   public Setting<Spammer.Mode> mode;
   public Setting<Spammer.Messages> messages;
   public Setting<Spammer.WhisperPrefix> whisper_prefix;
   public Setting<Boolean> global;
   public Setting<Boolean> antiSpam;
   public Setting<Float> delay;
   private final Timer timer_delay;
   private final Random random;
   private String fact;

   public Spammer() {
      super("Spammer", Module.Category.NONE);
      this.mode = new Setting("mode", Spammer.Mode.Chat);
      this.messages = new Setting("messages", Spammer.Messages.File);
      this.whisper_prefix = new Setting("prefix", Spammer.WhisperPrefix.W, (v) -> {
         return this.mode.getValue() == Spammer.Mode.Whispers;
      });
      this.global = new Setting("global", true, (v) -> {
         return this.mode.getValue() == Spammer.Mode.Chat;
      });
      this.antiSpam = new Setting("AntiSpam", false);
      this.delay = new Setting("delay", 5.0F, 0.0F, 30.0F);
      this.timer_delay = new Timer();
      this.random = new Random();
   }

   public static void loadSpammer() {
      try {
         File file = new File("ThunderHackRecode/misc/spammer.txt");
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
                  if (l.equals("")) {
                     newline = true;
                     break;
                  }
               }

               SpamList.clear();
               ArrayList<String> spamList = new ArrayList();
               if (!newline) {
                  spamList.addAll(lines);
               } else {
                  StringBuilder spamChunk = new StringBuilder();
                  Iterator var9 = lines.iterator();

                  while(var9.hasNext()) {
                     String lx = (String)var9.next();
                     if (lx.equals("")) {
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

               SpamList = spamList;
            } catch (Exception var11) {
            }

         })).start();
      } catch (IOException var1) {
      }

   }

   public String getPlayerName() {
      try {
         List<String> list = StaffBoard.getOnlinePlayer();
         return list.isEmpty() ? "" : (String)list.get(this.random.nextInt(0, list.size() - 1));
      } catch (NullPointerException var2) {
         return null;
      }
   }

   private void changeFact() {
      Managers.ASYNC.run(() -> {
         try {
            String jsonResponse = IOUtils.toString(new URL("https://catfact.ninja/fact?max_length=200"), StandardCharsets.UTF_8);
            JsonObject jsonObject = (new JsonParser()).parse(jsonResponse).getAsJsonObject();
            this.fact = jsonObject.get("fact").getAsString();
         } catch (IOException var3) {
            this.disable(ClientSettings.isRu() ? "Не удалось загрузить факт, может ты включишь интернет?" : "Failed to load the fact, can you turn on the Internet?");
         }

      });
   }

   public static String generateRandomSymbol() {
      Random random = new Random();
      String randomSymbol = "[";
      randomSymbol = randomSymbol + (char)(random.nextInt(26) + 97);
      randomSymbol = randomSymbol + random.nextInt(10);
      randomSymbol = randomSymbol + (char)(random.nextInt(26) + 97);
      randomSymbol = randomSymbol + "]";
      return randomSymbol;
   }

   public void onEnable() {
      loadSpammer();
      this.changeFact();
   }

   public void onUpdate() {
      if (this.timer_delay.passedMs((long)((Float)this.delay.getValue() * 1000.0F))) {
         String c;
         if (this.messages.getValue() == Spammer.Messages.File) {
            if (SpamList.isEmpty()) {
               this.disable(ClientSettings.isRu() ? "Файл spammer пустой!" : "The spammer file is empty!");
               return;
            }

            c = (String)SpamList.get((new Random()).nextInt(SpamList.size()));
         } else {
            if (this.fact == null) {
               return;
            }

            c = this.fact;
            this.changeFact();
         }

         if ((Boolean)this.antiSpam.getValue()) {
            c = c + generateRandomSymbol();
         }

         if (this.mode.getValue() == Spammer.Mode.Chat) {
            if (c.charAt(0) == '/') {
               c = c.replace("/", "");
               mc.field_1724.field_3944.method_45731(c);
            } else {
               mc.field_1724.field_3944.method_45729((Boolean)this.global.getValue() ? "!" + c : c);
            }
         } else {
            try {
               String prefix = ((Spammer.WhisperPrefix)this.whisper_prefix.getValue()).prefix;
               mc.field_1724.field_3944.method_45731(prefix + this.getPlayerName() + " " + c);
            } catch (NullPointerException var3) {
            }
         }

         this.timer_delay.reset();
      }

   }

   private static enum Mode {
      Chat,
      Whispers;

      // $FF: synthetic method
      private static Spammer.Mode[] $values() {
         return new Spammer.Mode[]{Chat, Whispers};
      }
   }

   private static enum Messages {
      File,
      CatFacts;

      // $FF: synthetic method
      private static Spammer.Messages[] $values() {
         return new Spammer.Messages[]{File, CatFacts};
      }
   }

   private static enum WhisperPrefix {
      W("w "),
      Msg("msg "),
      Tell("tell ");

      final String prefix;

      private WhisperPrefix(String p) {
         this.prefix = p;
      }

      // $FF: synthetic method
      private static Spammer.WhisperPrefix[] $values() {
         return new Spammer.WhisperPrefix[]{W, Msg, Tell};
      }
   }
}
