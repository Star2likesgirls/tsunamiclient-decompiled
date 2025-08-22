package tsunami.utility;

import com.mojang.authlib.GameProfile;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import net.minecraft.class_1011;
import net.minecraft.class_1043;
import net.minecraft.class_2960;
import net.minecraft.class_310;

public final class OptifineCapes {
   public static void loadPlayerCape(GameProfile player, OptifineCapes.ReturnCapeTexture response) {
      try {
         String uuid = player.getId().toString();
         class_1043 nIBT = getCapeFromURL(String.format("http://s.optifine.net/capes/%s.png", player.getName()));
         class_2960 capeTexture = class_310.method_1551().method_1531().method_4617("th-cape-" + uuid, nIBT);
         response.response(capeTexture);
      } catch (Exception var5) {
      }

   }

   public static class_1043 getCapeFromURL(String capeStringURL) {
      try {
         URL capeURL = new URL(capeStringURL);
         return getCapeFromStream(capeURL.openStream());
      } catch (IOException var2) {
         return null;
      }
   }

   public static class_1043 getCapeFromStream(InputStream image) {
      class_1011 cape = null;

      try {
         cape = class_1011.method_4309(image);
      } catch (IOException var3) {
         var3.printStackTrace();
      }

      return cape != null ? new class_1043(parseCape(cape)) : null;
   }

   public static class_1011 parseCape(class_1011 image) {
      int imageWidth = 64;
      int imageHeight = 32;
      int imageSrcWidth = image.method_4307();
      int srcHeight = image.method_4323();

      for(int imageSrcHeight = image.method_4323(); imageWidth < imageSrcWidth || imageHeight < imageSrcHeight; imageHeight *= 2) {
         imageWidth *= 2;
      }

      class_1011 imgNew = new class_1011(imageWidth, imageHeight, true);

      for(int x = 0; x < imageSrcWidth; ++x) {
         for(int y = 0; y < srcHeight; ++y) {
            imgNew.method_4305(x, y, image.method_4315(x, y));
         }
      }

      image.close();
      return imgNew;
   }

   public interface ReturnCapeTexture {
      void response(class_2960 var1);
   }
}
