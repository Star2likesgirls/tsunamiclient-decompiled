package tsunami.features.modules.misc;

import java.lang.Character.UnicodeBlock;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.Map.Entry;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_124;
import net.minecraft.class_1802;
import net.minecraft.class_2561;
import net.minecraft.class_2596;
import net.minecraft.class_2703;
import net.minecraft.class_2797;
import net.minecraft.class_3417;
import net.minecraft.class_3419;
import net.minecraft.class_634;
import net.minecraft.class_640;
import net.minecraft.class_7439;
import net.minecraft.class_7828;
import net.minecraft.class_2703.class_2705;
import net.minecraft.class_2703.class_5893;
import org.jetbrains.annotations.NotNull;
import tsunami.core.Managers;
import tsunami.events.impl.PacketEvent;
import tsunami.events.impl.TotemPopEvent;
import tsunami.features.modules.Module;
import tsunami.features.modules.client.ClientSettings;
import tsunami.gui.notification.Notification;
import tsunami.injection.accesors.IGameMessageS2CPacket;
import tsunami.setting.Setting;
import tsunami.utility.Timer;

public class ChatUtils extends Module {
   private final Setting<ChatUtils.Welcomer> welcomer;
   private final Setting<ChatUtils.Prefix> prefix;
   private final Setting<Boolean> totems;
   private final Setting<Boolean> time;
   private final Setting<Boolean> mention;
   private final Setting<ChatUtils.PMSound> pmSound;
   private final Setting<Boolean> zov;
   private final Setting<Boolean> wavy;
   private final Setting<Boolean> translit;
   private final Setting<Boolean> antiCoordLeak;
   private final Timer timer;
   private final Timer antiSpam;
   private final Timer messageTimer;
   private final LinkedHashMap<UUID, String> nameMap;
   private String skip;
   Map<String, String> ruToEng;
   private final String[] bb;
   private final String[] qq;
   private final String[] popMessages;

   public ChatUtils() {
      super("ChatUtils", Module.Category.NONE);
      this.welcomer = new Setting("Welcomer", ChatUtils.Welcomer.Off);
      this.prefix = new Setting("Prefix", ChatUtils.Prefix.None);
      this.totems = new Setting("Totems", false);
      this.time = new Setting("Time", false);
      this.mention = new Setting("Mention", false);
      this.pmSound = new Setting("PMSound", ChatUtils.PMSound.Default);
      this.zov = new Setting("ZOV", false);
      this.wavy = new Setting("wAvY", false);
      this.translit = new Setting("Translit", false);
      this.antiCoordLeak = new Setting("AntiCoordLeak", false);
      this.timer = new Timer();
      this.antiSpam = new Timer();
      this.messageTimer = new Timer();
      this.nameMap = new LinkedHashMap();
      this.ruToEng = Map.ofEntries(new Entry[]{Map.entry("а", "a"), Map.entry("б", "6"), Map.entry("в", "B"), Map.entry("г", "r"), Map.entry("д", "d"), Map.entry("е", "e"), Map.entry("ё", "e"), Map.entry("ж", ">I<"), Map.entry("з", "3"), Map.entry("и", "u"), Map.entry("й", "u"), Map.entry("к", "k"), Map.entry("л", "JI"), Map.entry("м", "m"), Map.entry("н", "H"), Map.entry("о", "o"), Map.entry("п", "n"), Map.entry("р", "p"), Map.entry("с", "c"), Map.entry("т", "T"), Map.entry("у", "y"), Map.entry("ф", "f"), Map.entry("х", "x"), Map.entry("ц", "lI"), Map.entry("ч", "4"), Map.entry("ш", "w"), Map.entry("щ", "w"), Map.entry("ь", "b"), Map.entry("ы", "bI"), Map.entry("ъ", "b"), Map.entry("э", "-)"), Map.entry("ю", "I-O"), Map.entry("я", "9I")});
      this.bb = new String[]{"See you later, ", "Catch ya later, ", "See you next time, ", "Farewell, ", "Bye, ", "Good bye, ", "Later, "};
      this.qq = new String[]{"Good to see you, ", "Greetings, ", "Hello, ", "Howdy, ", "Hey, ", "Good evening, ", "Welcome to SERVERIP1D5A9E, "};
      this.popMessages = new String[]{" EZZZ POP <pop> TIMES PIECE OF SHIT GET GOOD", " ez pop <pop> times fuckin unbrain", " pop <pop> times get good kiddo ", " EZZZZZZZ pop <pop> times GO LEARN PVP PUSSY", " piece of shit popped <pop> times so ez", " easiest pop <pop> times in my life", " HAHAHAHA BRO POPPED <pop> TIMES SO EZ LMAO", " POP <pop> TIMES OMG MAN UR SO BAD LMAO", " my grandma has more skill than you nigga pop <pop> times", " trash pop <pop> times retard ", " ezz no skill dog pop <pop> times", " lame dude tryes to pvp with me but dyes) hahah pop <pop> times", " get better tbh bruh pop <pop> times", " pop <pop> times ur eyes don't work right? ", " cringelord popped <pop> times so ez "};
   }

   public void onDisable() {
      this.nameMap.clear();
   }

   public void onUpdate() {
      if (this.timer.passedMs(15000L)) {
         Iterator var1 = mc.field_1724.field_3944.method_2880().iterator();

         while(var1.hasNext()) {
            class_640 b = (class_640)var1.next();
            if (!this.nameMap.containsKey(b.method_2966().getId())) {
               this.nameMap.put(b.method_2966().getId(), b.method_2966().getName());
            }
         }

         this.timer.reset();
      }

   }

   @EventHandler
   public void onPacketReceive(PacketEvent.Receive event) {
      class_2596 var3;
      String var10001;
      if (this.welcomer.getValue() != ChatUtils.Welcomer.Off && this.antiSpam.passedMs(3000L)) {
         var3 = event.getPacket();
         if (var3 instanceof class_2703) {
            class_2703 pck = (class_2703)var3;
            int n2 = (int)Math.floor(Math.random() * (double)this.qq.length);
            String string1;
            if (mc.field_1724.field_3944.method_45734() != null) {
               string1 = this.qq[n2].replace("SERVERIP1D5A9E", mc.field_1724.field_3944.method_45734().field_3761);
            } else {
               string1 = "server";
            }

            class_2705 ple;
            if (pck.method_46327().contains(class_5893.field_29136)) {
               for(Iterator var5 = pck.method_46330().iterator(); var5.hasNext(); this.nameMap.put(ple.comp_1107().getId(), ple.comp_1107().getName())) {
                  ple = (class_2705)var5.next();
                  if (this.antiBot(ple.comp_1107().getName())) {
                     return;
                  }

                  if (Objects.equals(ple.comp_1107().getName(), mc.field_1724.method_5477().getString())) {
                     return;
                  }

                  if (this.welcomer.getValue() == ChatUtils.Welcomer.Server) {
                     mc.field_1724.field_3944.method_45729(this.getPrefix() + string1 + ple.comp_1107().getName());
                     this.antiSpam.reset();
                  } else {
                     this.sendMessage(string1 + ple.comp_1107().getName());
                  }
               }
            }
         }

         var3 = event.getPacket();
         if (var3 instanceof class_7828) {
            class_7828 pac = (class_7828)var3;

            UUID uuid2;
            for(Iterator var10 = pac.comp_1105.iterator(); var10.hasNext(); this.nameMap.remove(uuid2)) {
               uuid2 = (UUID)var10.next();
               if (!this.nameMap.containsKey(uuid2)) {
                  return;
               }

               if (this.antiBot((String)this.nameMap.get(uuid2))) {
                  return;
               }

               if (Objects.equals(this.nameMap.get(uuid2), mc.field_1724.method_5477().getString())) {
                  return;
               }

               int n = (int)Math.floor(Math.random() * (double)this.bb.length);
               if (this.welcomer.getValue() == ChatUtils.Welcomer.Server) {
                  class_634 var10000 = mc.field_1724.field_3944;
                  var10001 = this.getPrefix();
                  var10000.method_45729(var10001 + this.bb[n] + (String)this.nameMap.get(uuid2));
                  this.antiSpam.reset();
               } else {
                  var10001 = this.bb[n];
                  this.sendMessage(var10001 + (String)this.nameMap.get(uuid2));
               }
            }
         }
      }

      var3 = event.getPacket();
      if (var3 instanceof class_7439) {
         class_7439 pac = (class_7439)var3;
         if ((Boolean)this.time.getValue()) {
            IGameMessageS2CPacket pac2 = (IGameMessageS2CPacket)event.getPacket();
            var10001 = String.valueOf(class_124.field_1080);
            pac2.setContent(class_2561.method_30163("[" + var10001 + (new SimpleDateFormat("HH:mm:ss")).format(Calendar.getInstance().getTime()) + String.valueOf(class_124.field_1070) + "] ").method_27661().method_10852(pac.comp_763));
         }

         if ((Boolean)this.mention.getValue() && pac.comp_763.getString().contains(mc.field_1724.method_5477().getString()) && this.messageTimer.passedMs(1000L)) {
            Managers.NOTIFICATION.publicity("ChatUtils", ClientSettings.isRu() ? "Тебя помянули в чате!" : "You were mentioned in the chat!", 4, Notification.Type.WARNING);
            mc.field_1687.method_8396(mc.field_1724, mc.field_1724.method_24515(), class_3417.field_14709, class_3419.field_15245, 5.0F, 1.0F);
         }

         String content = pac.comp_763.getString().toLowerCase();
         if (!this.pmSound.is(ChatUtils.PMSound.Off) && (content.contains("whisper") || content.contains("-> я") || content.contains("-> " + NameProtect.getCustomName()) || content.contains("-> me") || content.contains(" says:"))) {
            Managers.SOUND.playPmSound((ChatUtils.PMSound)this.pmSound.getValue());
         }
      }

   }

   @EventHandler
   public void onTotem(TotemPopEvent e) {
      if ((Boolean)this.totems.getValue() && this.antiSpam.passedMs(3000L) && e.getEntity() != mc.field_1724) {
         int n = (int)Math.floor(Math.random() * (double)this.popMessages.length);
         String s = this.popMessages[n].replace("<pop>", e.getPops().makeConcatWithConstants<invokedynamic>(e.getPops()));
         class_634 var10000 = mc.field_1724.field_3944;
         String var10001 = this.getPrefix();
         var10000.method_45729(var10001 + e.getEntity().method_5477().getString() + s);
         this.antiSpam.reset();
      }

   }

   @NotNull
   private String getPrefix() {
      String var10000;
      switch(((ChatUtils.Prefix)this.prefix.getValue()).ordinal()) {
      case 0:
         var10000 = ">";
         break;
      case 1:
         var10000 = "!";
         break;
      case 2:
         var10000 = "";
         break;
      default:
         throw new MatchException((String)null, (Throwable)null);
      }

      return var10000;
   }

   public boolean antiBot(@NotNull String s) {
      if (!s.contains("soon_") && !s.contains("_npc") && !s.contains("CIT-")) {
         for(int i = 0; i < s.length(); ++i) {
            if (UnicodeBlock.of(s.charAt(i)).equals(UnicodeBlock.CYRILLIC)) {
               return true;
            }
         }

         return false;
      } else {
         return true;
      }
   }

   @EventHandler
   public void onPacketSend(@NotNull PacketEvent.Send e) {
      class_2596 var3 = e.getPacket();
      class_2797 pac;
      if (var3 instanceof class_2797) {
         pac = (class_2797)var3;
         if ((Boolean)this.antiCoordLeak.getValue() && pac.comp_945.replaceAll("\\D", "").length() >= 6) {
            this.sendMessage("[ChatUtils] " + (ClientSettings.isRu() ? "В сообщении содержатся координаты!" : "The message contains coordinates!"));
            e.cancel();
         }

         this.messageTimer.reset();
      }

      if (!fullNullCheck()) {
         var3 = e.getPacket();
         if (var3 instanceof class_2797) {
            pac = (class_2797)var3;
            if ((Boolean)this.zov.getValue() || (Boolean)this.wavy.getValue() || (Boolean)this.translit.getValue()) {
               if (Objects.equals(pac.comp_945(), this.skip)) {
                  return;
               }

               if (mc.field_1724.method_6047().method_7909() != class_1802.field_8204 && mc.field_1724.method_6079().method_7909() != class_1802.field_8204) {
                  if (!pac.comp_945().startsWith("/") && !pac.comp_945().startsWith(Managers.COMMAND.getPrefix())) {
                     String message = pac.comp_945();
                     StringBuilder builder;
                     int var7;
                     if ((Boolean)this.zov.getValue()) {
                        builder = new StringBuilder();
                        char[] var5 = message.toCharArray();
                        int var6 = var5.length;
                        var7 = 0;

                        while(true) {
                           if (var7 >= var6) {
                              message = builder.toString();
                              break;
                           }

                           char Z = var5[var7];
                           if (1047 != Z && 1079 != Z) {
                              if (1042 != Z && 1074 != Z) {
                                 builder.append(Z);
                              } else {
                                 builder.append("V");
                              }
                           } else {
                              builder.append("Z");
                           }

                           ++var7;
                        }
                     }

                     if ((Boolean)this.wavy.getValue()) {
                        builder = new StringBuilder();
                        boolean up = false;
                        char[] var12 = message.toCharArray();
                        var7 = var12.length;

                        for(int var13 = 0; var13 < var7; ++var13) {
                           char C = var12[var13];
                           if (up) {
                              builder.append(Character.toUpperCase(C));
                           } else {
                              builder.append(Character.toLowerCase(C));
                           }

                           up = Character.isLetter(C) != up;
                        }

                        message = builder.toString();
                     }

                     if ((Boolean)this.translit.getValue()) {
                        message = this.transliterate(message);
                     }

                     this.skip = message;
                     mc.field_1724.field_3944.method_45729(this.skip);
                     e.cancel();
                     return;
                  }

                  return;
               }

               return;
            }
         }

      }
   }

   public String transliterate(String text) {
      StringBuilder result = new StringBuilder();
      char[] var3 = text.toCharArray();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         char ch = var3[var5];
         String str = (String)this.ruToEng.get(ch.makeConcatWithConstants<invokedynamic>(ch));
         result.append(str != null ? str : ch);
      }

      return result.toString();
   }

   private static enum Welcomer {
      Off,
      Server,
      Client;

      // $FF: synthetic method
      private static ChatUtils.Welcomer[] $values() {
         return new ChatUtils.Welcomer[]{Off, Server, Client};
      }
   }

   private static enum Prefix {
      Green,
      Global,
      None;

      // $FF: synthetic method
      private static ChatUtils.Prefix[] $values() {
         return new ChatUtils.Prefix[]{Green, Global, None};
      }
   }

   public static enum PMSound {
      Off,
      Default,
      Custom;

      // $FF: synthetic method
      private static ChatUtils.PMSound[] $values() {
         return new ChatUtils.PMSound[]{Off, Default, Custom};
      }
   }
}
