package tsunami.features.modules.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_124;
import net.minecraft.class_1802;
import net.minecraft.class_2596;
import net.minecraft.class_2797;
import net.minecraft.class_7439;
import org.apache.commons.lang3.StringEscapeUtils;
import tsunami.core.Managers;
import tsunami.events.impl.PacketEvent;
import tsunami.features.modules.Module;
import tsunami.setting.Setting;

public class ChatTranslator extends Module {
   private final Setting<ChatTranslator.Lang> urLang;
   private final Setting<ChatTranslator.Lang> outMessages;
   private List<String> exceptions;
   private String skip;

   public ChatTranslator() {
      super("ChatTranslator", Module.Category.CLIENT);
      this.urLang = new Setting("YourLanguage", ChatTranslator.Lang.ru);
      this.outMessages = new Setting("OutMessages", ChatTranslator.Lang.uk);
      this.exceptions = Arrays.asList("was killed by", "was destroyed by", "has joined a game", "has left the game");
   }

   @EventHandler
   public void onPacketReceive(PacketEvent.Receive e) {
      if (!fullNullCheck()) {
         class_2596 var3 = e.getPacket();
         if (var3 instanceof class_7439) {
            class_7439 mPacket = (class_7439)var3;
            String message = mPacket.comp_763().getString();
            Iterator var4 = this.exceptions.iterator();

            while(var4.hasNext()) {
               String s = (String)var4.next();
               if (message.contains(s)) {
                  return;
               }
            }

            Managers.ASYNC.run(() -> {
               try {
                  String var10001 = String.valueOf(class_124.field_1068);
                  this.sendMessage(var10001 + this.translate(message, ((ChatTranslator.Lang)this.urLang.getValue()).name()));
               } catch (Exception var3) {
                  var3.printStackTrace();
               }

            });
         }

      }
   }

   @EventHandler
   public void onPacketSend(PacketEvent.Send e) {
      if (!fullNullCheck()) {
         class_2596 var3 = e.getPacket();
         if (var3 instanceof class_2797) {
            class_2797 pac = (class_2797)var3;
            if (Objects.equals(pac.comp_945(), this.skip)) {
               return;
            }

            if (mc.field_1724.method_6047().method_7909() == class_1802.field_8204 || mc.field_1724.method_6079().method_7909() == class_1802.field_8204) {
               return;
            }

            if (pac.comp_945().startsWith("/") || pac.comp_945().startsWith(Managers.COMMAND.getPrefix())) {
               return;
            }

            Managers.ASYNC.run(() -> {
               try {
                  String outMessage = this.translate(pac.comp_945(), ((ChatTranslator.Lang)this.outMessages.getValue()).name());
                  if (Objects.equals(pac.comp_945(), outMessage)) {
                     this.skip = pac.comp_945();
                     mc.field_1724.field_3944.method_45729(pac.comp_945());
                  } else {
                     this.skip = outMessage;
                     mc.field_1724.field_3944.method_45729(outMessage);
                  }
               } catch (Exception var3) {
                  var3.printStackTrace();
               }

            });
            e.cancel();
         }

      }
   }

   public String translate(String text, String to) throws UnsupportedEncodingException, MalformedURLException {
      StringBuilder response = new StringBuilder();
      URL url = new URL(String.format("https://translate.google.com/m?hl=en&sl=auto&tl=%s&ie=UTF-8&prev=_m&q=%s", to, URLEncoder.encode(text.trim(), StandardCharsets.UTF_8)));

      String line;
      try {
         BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8));

         try {
            while((line = br.readLine()) != null) {
               response.append(line + "\n");
            }
         } catch (Throwable var9) {
            try {
               br.close();
            } catch (Throwable var8) {
               var9.addSuppressed(var8);
            }

            throw var9;
         }

         br.close();
      } catch (IOException var10) {
      }

      Matcher matcher = Pattern.compile("class=\"result-container\">([^<]*)<\\/div>", 8).matcher(response);
      matcher.find();
      line = matcher.group(1);
      return line != null && !line.isEmpty() ? StringEscapeUtils.unescapeHtml4(line) : "translation failed";
   }

   public static enum Lang {
      am,
      ar,
      eu,
      bn,
      bg,
      ca,
      chr,
      hr,
      cs,
      da,
      nl,
      en,
      et,
      fil,
      fi,
      fr,
      de,
      el,
      gu,
      iw,
      hi,
      hu,
      is,
      id,
      it,
      ja,
      kn,
      ko,
      lv,
      lt,
      ms,
      ml,
      mr,
      no,
      pl,
      ro,
      ru,
      sr,
      sk,
      sl,
      es,
      sw,
      sv,
      ta,
      te,
      th,
      tr,
      ur,
      uk,
      vi,
      cy,
      cn;

      // $FF: synthetic method
      private static ChatTranslator.Lang[] $values() {
         return new ChatTranslator.Lang[]{am, ar, eu, bn, bg, ca, chr, hr, cs, da, nl, en, et, fil, fi, fr, de, el, gu, iw, hi, hu, is, id, it, ja, kn, ko, lv, lt, ms, ml, mr, no, pl, ro, ru, sr, sk, sl, es, sw, sv, ta, te, th, tr, ur, uk, vi, cy, cn};
      }
   }
}
