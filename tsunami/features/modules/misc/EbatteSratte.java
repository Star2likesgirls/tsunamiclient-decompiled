package tsunami.features.modules.misc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_1657;
import net.minecraft.class_634;
import org.jetbrains.annotations.NotNull;
import tsunami.events.impl.EventAttack;
import tsunami.features.modules.Module;
import tsunami.setting.Setting;
import tsunami.utility.Timer;

public class EbatteSratte extends Module {
   private final Setting<Integer> delay = new Setting("Delay", 5, 1, 30);
   private final Setting<EbatteSratte.Server> server;
   private final Setting<EbatteSratte.Messages> mode;
   private static final String[] WORDS = new String[]{"Я TBOЮ MATЬ БЛЯTЬ ПOДВEСИЛ НА КОЛ ОНА EБAHAЯ БЛЯДИHA", "МАМАШУ ТВОЮ АРМАТУРОЙ С ШИПАМИ ПО ХРЕБТУ ПИЗДИЛ", "Я ТВОЕЙ МАТЕРИ ПИЗДАК РАЗОРВАЛ СЫН БЛЯДИНЫ ТЫ ЕБАННОЙ", "ВГЕТАЙ ТАНДЕРХАК СЫН ЕБАННОЙ ШЛЮХИ", "ТЫ ПСИНА БЕЗ БРЕЙНА ДАВАЙ ТЕРПИ ТЕРПИ", "я твою мать об стол xуяpил сын тупорылой овчарки мать продал чит на кубики купил?", "СКУЛИ СВИHЬЯ ЕБAHAЯ , Я ТВОЮ MATЬ ПОДBECИЛ НА ЦЕПЬ И С ОКНА СБРОСИЛ ОНА ФЕМИНИСТКА ЕБАHAЯ ОНА СВОИМ ВЕСОМ 180КГ ПРОБУРИЛАСЬ ДО ЯДРА ЗЕМЛИ И СГОРЕЛА HAXУЙ АХАХАХАХА ЕБATЬ ОНА ГОРИТ ПРИКОЛЬНО", "ты мейн сначало свой пукни потом чет овысирай, с основы пиши нищ", "БАБКА СДОХЛА ОТ СТАРОСТИ Т.К. КОГДА ТВОЮ МATЬ РОДИЛИ ЕЙ БЫЛО 99 ЛЕТ И ОТ НЕРВОВ РАДОСТИ ОНА СДОХЛА ОЙ БЛ9TЬ ОТ РАДОСТИ ДЕД ТОЖЕ ОТ РАДОСТИ СДОХ HAXУЙ ДOЛБAЁБ EБAHЫЙ ЧТОБЫ ВЫЖИТЬ НА ПОМОЙКА МATЬ ТВOЯ ПOКА НЕ СДОХЛА EБAЛAСЬ С МУЖИКАМИ ЗА 2 КОПЕЙКИ", "ТЫ ПОНИМАЕШЬ ЧТО Я ТВОЮ МАТЬ ОТПРАВИЛ СО СВОЕГО XУЯ В НЕБО, ЧТОБ ОНА СВОИМ ПИЗДAKOМ ПРИНИМАЛА МИТЕОРИТНУЮ АТАКУ?)", "ТЫ ПОНИМАЕШЬ ЧТО ТBОЯ МATЬ СИДИТ У МЕНЯ НА ЦЕПИ И КАК БУЛЬДОГ EБАHЫЙ НА МОЙ XУЙ СЛЮНИ БЛ9ДЬ ПУСКАЕТ?))", "В ДЕТДОМЕ ТЕБЯ ПИЗДUЛИ ВСЕ КТО МОГ В ИТОГЕ ТЫ СДОХ НА УЛИЦЕ В 13 ЛЕТ ОТ НЕДОСТАТКА ЕДЫ ВОДУ ТЫ ЖЕ БРАЛ ЭТИМ ФИЛЬТРОМ И МОЧОЙ ДOЛБAЁБ ЕБAHЫЙ СУКA БЕЗ МATEPHAЯ ХУETА.", "Чё как нищий, купи тандерхак не позорься", "Your mom owned by Thunderhack Recode", "АЛО БОМЖАТИНА БЕЗ МАТЕРИ Я ТВОЮ МАТЬ ОБ СТОЛ УБИЛ ЧЕРЕП ЕЙ РАЗБИЛ НОГОЙ БАТЮ ТВОЕГО С ОКНА ВЫКИНУЛ СУКА ЧМО ЕБАННОЕ ОТВЕТЬ ЧМО ЕБЛАН ТВАРЬ ШАЛАВА", "1", "ГО 1 НА 1 РН СЫН ШЛЮХИ", "СКАЖЕШЬ - БАТЯ ПИДОР, ПРОМОЛЧИШЬ - МАТЬ ШЛЮХА"};
   private final Timer timer;
   private ArrayList<String> words;

   public EbatteSratte() {
      super("EbatteSratte", Module.Category.NONE);
      this.server = new Setting("Server", EbatteSratte.Server.FunnyGame);
      this.mode = new Setting("Mode", EbatteSratte.Messages.Default);
      this.timer = new Timer();
      this.words = new ArrayList();
      this.loadEZ();
   }

   public void onEnable() {
      this.loadEZ();
   }

   @EventHandler
   public void onAttackEntity(@NotNull EventAttack event) {
      if (event.getEntity() instanceof class_1657 && !event.isPre() && this.timer.passedS((double)(Integer)this.delay.getValue())) {
         class_1657 entity = (class_1657)event.getEntity();
         if (entity == null) {
            return;
         }

         int n;
         if (this.mode.getValue() == EbatteSratte.Messages.Default) {
            n = (int)Math.floor(Math.random() * (double)WORDS.length);
         } else {
            n = (int)Math.floor(Math.random() * (double)this.words.size());
         }

         String var10000;
         switch(((EbatteSratte.Server)this.server.getValue()).ordinal()) {
         case 0:
            var10000 = "!";
            break;
         case 1:
            var10000 = "/msg ";
            break;
         case 2:
            var10000 = ">";
            break;
         case 3:
            var10000 = "";
            break;
         default:
            throw new MatchException((String)null, (Throwable)null);
         }

         String chatPrefix = var10000;
         if (chatPrefix.contains("/")) {
            class_634 var5 = mc.method_1562();
            String var10001 = entity.method_5477().getString();
            var5.method_45730("/msg " + var10001 + " " + (this.mode.getValue() == EbatteSratte.Messages.Default ? WORDS[n] : (String)this.words.get(n)));
         } else {
            mc.method_1562().method_45729(chatPrefix + entity.method_5477().getString() + " " + (this.mode.getValue() == EbatteSratte.Messages.Default ? WORDS[n] : (String)this.words.get(n)));
         }

         this.timer.reset();
      }

   }

   public void loadEZ() {
      try {
         File file = new File("ThunderHackRecode/misc/EbatteSratte.txt");
         if (!file.exists() && !file.createNewFile()) {
            this.sendMessage("Error with creating file");
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
               Iterator var8 = lines.iterator();

               while(var8.hasNext()) {
                  String lx = (String)var8.next();
                  if (lx.isEmpty()) {
                     newline = true;
                     break;
                  }
               }

               this.words.clear();
               ArrayList<String> spamList = new ArrayList();
               if (!newline) {
                  spamList.addAll(lines);
               } else {
                  StringBuilder spamChunk = new StringBuilder();
                  Iterator var10 = lines.iterator();

                  while(var10.hasNext()) {
                     String l = (String)var10.next();
                     if (l.isEmpty()) {
                        if (!spamChunk.isEmpty()) {
                           spamList.add(spamChunk.toString());
                           spamChunk = new StringBuilder();
                        }
                     } else {
                        spamChunk.append(l).append(" ");
                     }
                  }

                  spamList.add(spamChunk.toString());
               }

               this.words = spamList;
            } catch (Exception var12) {
            }

         })).start();
      } catch (IOException var2) {
      }

   }

   public static enum Server {
      FunnyGame,
      DirectMessage,
      OldServer,
      Local;

      // $FF: synthetic method
      private static EbatteSratte.Server[] $values() {
         return new EbatteSratte.Server[]{FunnyGame, DirectMessage, OldServer, Local};
      }
   }

   public static enum Messages {
      Default,
      Custom;

      // $FF: synthetic method
      private static EbatteSratte.Messages[] $values() {
         return new EbatteSratte.Messages[]{Default, Custom};
      }
   }
}
