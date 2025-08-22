package tsunami.features.modules.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Objects;
import net.minecraft.class_422;
import net.minecraft.class_442;
import net.minecraft.class_500;
import tsunami.TsunamiClient;
import tsunami.core.Managers;
import tsunami.features.modules.Module;
import tsunami.setting.Setting;
import tsunami.utility.Timer;
import tsunami.utility.discord.DiscordEventHandlers;
import tsunami.utility.discord.DiscordRPC;
import tsunami.utility.discord.DiscordRichPresence;

public final class RPC extends Module {
   private static final DiscordRPC rpc;
   public static Setting<RPC.Mode> mode;
   public static Setting<Boolean> showIP;
   public static Setting<RPC.sMode> smode;
   public static Setting<String> state;
   public static Setting<Boolean> nickname;
   public static DiscordRichPresence presence;
   public static boolean started;
   static String String1;
   private final Timer timer_delay = new Timer();
   private static Thread thread;
   String slov;
   String[] rpc_perebor_en = new String[]{"Parkour", "Reporting cheaters", "Touching grass", "Asks how to bind", "Reporting bugs", "Watching Kilab"};
   String[] rpc_perebor_ru = new String[]{"Паркурит", "Репортит читеров", "Трогает траву", "Спрашивает как забиндить", "Репортит баги", "Смотрит Флюгера"};
   int randomInt;

   public RPC() {
      super("DiscordRPC", Module.Category.CLIENT);
   }

   public static void readFile() {
      try {
         File file = new File("Tsunami/misc/RPC.txt");
         if (file.exists()) {
            BufferedReader reader = new BufferedReader(new FileReader(file));

            try {
               while(reader.ready()) {
                  String1 = reader.readLine();
               }
            } catch (Throwable var5) {
               try {
                  reader.close();
               } catch (Throwable var4) {
                  var5.addSuppressed(var4);
               }

               throw var5;
            }

            reader.close();
         }
      } catch (Exception var6) {
      }

   }

   public static void WriteFile(String url1, String url2) {
      File file = new File("Tsunami/misc/RPC.txt");

      try {
         file.createNewFile();

         try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));

            try {
               writer.write(url1 + "SEPARATOR" + url2 + "\n");
            } catch (Throwable var7) {
               try {
                  writer.close();
               } catch (Throwable var6) {
                  var7.addSuppressed(var6);
               }

               throw var7;
            }

            writer.close();
         } catch (Exception var8) {
         }
      } catch (Exception var9) {
      }

   }

   public void onDisable() {
      started = false;
      if (thread != null && !thread.isInterrupted()) {
         thread.interrupt();
      }

      rpc.Discord_Shutdown();
   }

   public void onUpdate() {
      this.startRpc();
   }

   public void startRpc() {
      if (!this.isDisabled()) {
         if (!started) {
            started = true;
            DiscordEventHandlers handlers = new DiscordEventHandlers();
            rpc.Discord_Initialize("1406960808449282248", handlers, true, "");
            presence.startTimestamp = System.currentTimeMillis() / 1000L;
            presence.largeImageText = "v1.7b2407 [" + TsunamiClient.GITHUB_HASH + "]";
            rpc.Discord_UpdatePresence(presence);
            thread = new Thread(() -> {
               while(!Thread.currentThread().isInterrupted()) {
                  rpc.Discord_RunCallbacks();
                  presence.details = this.getDetails();
                  switch(((RPC.sMode)smode.getValue()).ordinal()) {
                  case 0:
                     presence.state = (String)state.getValue();
                     break;
                  case 1:
                     DiscordRichPresence var10000 = presence;
                     int var10001 = Managers.MODULE.getEnabledModules().size();
                     var10000.state = "Hacks: " + var10001 + " / " + Managers.MODULE.modules.size();
                     break;
                  case 2:
                     presence.state = "v1.7b2407 for mc 1.21";
                  }

                  if ((Boolean)nickname.getValue()) {
                     presence.smallImageText = "Tsunami Enjoyer";
                     presence.smallImageKey = "https://minotar.net/helm/" + mc.method_1548().method_1676() + "/100.png";
                  } else {
                     presence.smallImageText = "";
                     presence.smallImageKey = "";
                  }

                  presence.button_label_1 = "Download";
                  presence.button_url_1 = "https://discord.gg/h24F9j5Pwk";
                  switch(((RPC.Mode)mode.getValue()).ordinal()) {
                  case 0:
                     readFile();
                     presence.largeImageKey = String1.split("SEPARATOR")[0];
                     if (!Objects.equals(String1.split("SEPARATOR")[1], "none")) {
                        presence.smallImageKey = String1.split("SEPARATOR")[1];
                     }
                     break;
                  case 1:
                     presence.largeImageKey = "https://media.discordapp.net/attachments/1380158408468074516/1407079045396697098/639207e799a068a186599f00f33b424d.png?ex=68a4cbfa&is=68a37a7a&hm=638c932060eab3c52b40cc70328603366590a103413309011ec779f393221c0f&=&format=webp&quality=lossless&width=384&height=384";
                     break;
                  case 2:
                     presence.largeImageKey = "https://media.discordapp.net/attachments/1380158408468074516/1407079045396697098/639207e799a068a186599f00f33b424d.png?ex=68a4cbfa&is=68a37a7a&hm=638c932060eab3c52b40cc70328603366590a103413309011ec779f393221c0f&=&format=webp&quality=lossless&width=384&height=384";
                  }

                  rpc.Discord_UpdatePresence(presence);

                  try {
                     Thread.sleep(2000L);
                  } catch (InterruptedException var2) {
                  }
               }

            }, "TH-RPC-Handler");
            thread.start();
         }

      }
   }

   private String getDetails() {
      String result = "";
      if (!(mc.field_1755 instanceof class_500) && !(mc.field_1755 instanceof class_422) && !(mc.field_1755 instanceof class_442)) {
         if (mc.method_1558() != null) {
            result = ClientSettings.isRu() ? ((Boolean)showIP.getValue() ? "Играет на " + mc.method_1558().field_3761 : "Играет на сервере") : ((Boolean)showIP.getValue() ? "Playing on " + mc.method_1558().field_3761 : "Playing on server");
         } else if (mc.method_1542()) {
            result = ClientSettings.isRu() ? "Читерит в одиночке" : "SinglePlayer hacker";
         }
      } else {
         if (this.timer_delay.passedMs(60000L)) {
            this.randomInt = (int)(Math.random() * 6.0D + 0.0D);
            this.slov = ClientSettings.isRu() ? this.rpc_perebor_ru[this.randomInt] : this.rpc_perebor_en[this.randomInt];
            this.timer_delay.reset();
         }

         result = this.slov;
      }

      return result;
   }

   static {
      rpc = DiscordRPC.INSTANCE;
      mode = new Setting("Picture", RPC.Mode.Recode);
      showIP = new Setting("ShowIP", true);
      smode = new Setting("StateMode", RPC.sMode.Stats);
      state = new Setting("State", "Beta? Recode? NextGen?");
      nickname = new Setting("Nickname", true);
      presence = new DiscordRichPresence();
      String1 = "none";
   }

   public static enum sMode {
      Custom,
      Stats,
      Version;

      // $FF: synthetic method
      private static RPC.sMode[] $values() {
         return new RPC.sMode[]{Custom, Stats, Version};
      }
   }

   public static enum Mode {
      Custom,
      MegaCute,
      Recode;

      // $FF: synthetic method
      private static RPC.Mode[] $values() {
         return new RPC.Mode[]{Custom, MegaCute, Recode};
      }
   }
}
