package tsunami.utility;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.jar.Manifest;
import net.fabricmc.loader.api.metadata.Person;
import net.minecraft.class_1011;
import net.minecraft.class_1043;
import net.minecraft.class_1060;
import net.minecraft.class_124;
import net.minecraft.class_2960;
import net.minecraft.class_634;
import org.jetbrains.annotations.NotNull;
import tsunami.TsunamiClient;
import tsunami.core.manager.client.ConfigManager;
import tsunami.features.modules.Module;
import tsunami.utility.math.MathUtility;

public final class ThunderUtility {
   public static List<String> changeLog = new ArrayList();
   public static List<String> starGazer = new ArrayList();

   @NotNull
   public static String getAuthors() {
      List<String> names = TsunamiClient.MOD_META.getAuthors().stream().map(Person::getName).toList();
      return String.join(", ", names);
   }

   public static String solveName(String notSolved) {
      AtomicReference<String> mb = new AtomicReference("FATAL ERROR");
      ((class_634)Objects.requireNonNull(Module.mc.method_1562())).method_45732().forEach((player) -> {
         if (notSolved.contains(player.method_2966().getName())) {
            mb.set(player.method_2966().getName());
         }

      });
      return (String)mb.get();
   }

   public static class_2960 getCustomImg(String name) throws IOException {
      class_1060 var10000 = Module.mc.method_1531();
      String var10001 = "th-" + name + "-" + (int)MathUtility.random(0.0F, 1000.0F);
      String var10006 = String.valueOf(ConfigManager.IMAGES_FOLDER);
      return var10000.method_4617(var10001, new class_1043(class_1011.method_4309(new FileInputStream(var10006 + "/" + name + ".png"))));
   }

   public static void syncVersion() {
      try {
         if (!(new BufferedReader(new InputStreamReader((new URL("https://raw.githubusercontent.com/Pan4ur/THRecodeUtil/main/syncVersion121.txt")).openStream()))).readLine().equals("1.7b2407")) {
            TsunamiClient.isOutdated = true;
         }
      } catch (Exception var1) {
      }

   }

   public static void parseStarGazer() {
      ArrayList starGazers = new ArrayList();

      try {
         for(int page = 1; page <= 3; ++page) {
            URL url = new URL("https://api.github.com/repos/Pan4ur/ThunderHack-Recode/stargazers?per_page=100&page=" + page);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuilder response = new StringBuilder();

            String inputLine;
            while((inputLine = in.readLine()) != null) {
               response.append(inputLine);
            }

            in.close();
            JsonArray jsonArray = JsonParser.parseString(response.toString()).getAsJsonArray();

            for(int i = 0; i < jsonArray.size(); ++i) {
               JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
               starGazers.add(jsonObject.getAsJsonPrimitive("login").getAsString());
            }

            Thread.sleep(1500L);
         }
      } catch (Exception var9) {
         var9.printStackTrace();
      }

   }

   public static void syncContributors() {
      try {
         URL list = new URL("https://raw.githubusercontent.com/Pan4ur/THRecodeUtil/main/thTeam.txt");
         BufferedReader in = new BufferedReader(new InputStreamReader(list.openStream(), StandardCharsets.UTF_8));

         String inputLine;
         for(int i = 0; (inputLine = in.readLine()) != null; ++i) {
            TsunamiClient.contributors[i] = inputLine.trim();
         }

         in.close();
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   public static String readManifestField(String fieldName) {
      try {
         Enumeration en = Thread.currentThread().getContextClassLoader().getResources("META-INF/MANIFEST.MF");

         while(en.hasMoreElements()) {
            try {
               URL url = (URL)en.nextElement();
               InputStream is = url.openStream();
               if (is != null) {
                  String s = (new Manifest(is)).getMainAttributes().getValue(fieldName);
                  if (s != null) {
                     return s;
                  }
               }
            } catch (Exception var5) {
            }
         }
      } catch (Exception var6) {
      }

      return "0";
   }

   public static void parseCommits() {
      try {
         URL url = new URL("https://api.github.com/repos/Pan4ur/ThunderHack-Recode/commits?per_page=50");
         BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8));

         String inputLine;
         while((inputLine = in.readLine()) != null) {
            JsonArray jsonArray = JsonParser.parseString(inputLine).getAsJsonArray();

            for(int i = 0; i < jsonArray.size(); ++i) {
               JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
               JsonObject commitObject = jsonObject.getAsJsonObject("commit");
               JsonObject authorObject = commitObject.getAsJsonObject("author");
               String name = authorObject.get("name").getAsString().replace("\n", "");
               String date = authorObject.get("date").getAsString().replace("\n", "");
               String info = commitObject.get("message").getAsString().replace("\n", "");
               if (!name.contains("ImgBot") && !info.startsWith("Merge") && !info.startsWith("Revert")) {
                  String var10000 = String.valueOf(class_124.field_1080);
                  String formattedDate = var10000 + date.split("T")[0] + String.valueOf(class_124.field_1070);
                  var10000 = String.valueOf(class_124.field_1061);
                  String formattedName = "@" + var10000 + name + String.valueOf(class_124.field_1070);
                  changeLog.add("- " + info + " [" + formattedDate + "]  (" + formattedName + ")");
               }
            }
         }

         in.close();
      } catch (Exception var13) {
         var13.printStackTrace();
      }

   }
}
