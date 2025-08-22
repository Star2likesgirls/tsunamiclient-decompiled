package tsunami.utility.math;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.class_1297;
import net.minecraft.class_243;
import net.minecraft.class_3532;
import tsunami.features.modules.Module;

public final class MathUtility {
   public static double random(double min, double max) {
      return ThreadLocalRandom.current().nextDouble() * (max - min) + min;
   }

   public static float random(float min, float max) {
      return (float)(Math.random() * (double)(max - min) + (double)min);
   }

   public static double getDistanceSq(double x, double y, double z) {
      double d0 = Module.mc.field_1724.method_23317() - x;
      double d1 = Module.mc.field_1724.method_23318() - y;
      double d2 = Module.mc.field_1724.method_23321() - z;
      return d0 * d0 + d1 * d1 + d2 * d2;
   }

   public static double getDistance(double x1, double y1, double z1, double x2, double y2, double z2) {
      double d0 = x1 - x2;
      double d1 = y1 - y2;
      double d2 = z1 - z2;
      return Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
   }

   public static double getSqrDistance(double x1, double y1, double z1, double x2, double y2, double z2) {
      double d0 = x1 - x2;
      double d1 = y1 - y2;
      double d2 = z1 - z2;
      return Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
   }

   public static float round(float value) {
      BigDecimal bd = new BigDecimal((double)value);
      bd = bd.setScale(2, RoundingMode.HALF_UP);
      return bd.floatValue();
   }

   public static double getDistanceSq(class_1297 ent) {
      return getDistanceSq(ent.method_23317(), ent.method_23318(), ent.method_23321());
   }

   public static double roundToDecimal(double n, int point) {
      if (point == 0) {
         return Math.floor(n);
      } else {
         double factor = Math.pow(10.0D, (double)point);
         return (double)Math.round(n * factor) / factor;
      }
   }

   public static double angle(class_243 vec3d, class_243 other) {
      double lengthSq = vec3d.method_1033() * other.method_1033();
      if (lengthSq < 1.0E-4D) {
         return 0.0D;
      } else {
         double dot = vec3d.method_1026(other);
         double arg = dot / lengthSq;
         if (arg > 1.0D) {
            return 0.0D;
         } else {
            return arg < -1.0D ? 180.0D : Math.acos(arg) * 180.0D / 3.141592653589793D;
         }
      }
   }

   public static class_243 fromTo(class_243 from, double x, double y, double z) {
      return fromTo(from.field_1352, from.field_1351, from.field_1350, x, y, z);
   }

   public static float lerp(float f, float st, float en) {
      return st + f * (en - st);
   }

   public static class_243 fromTo(double x, double y, double z, double x2, double y2, double z2) {
      return new class_243(x2 - x, y2 - y, z2 - z);
   }

   public static float rad(float angle) {
      return (float)((double)angle * 3.141592653589793D / 180.0D);
   }

   public static int clamp(int num, int min, int max) {
      return num < min ? min : Math.min(num, max);
   }

   public static float clamp(float num, float min, float max) {
      return num < min ? min : Math.min(num, max);
   }

   public static double clamp(double num, double min, double max) {
      return num < min ? min : Math.min(num, max);
   }

   public static float sin(float value) {
      return class_3532.method_15374(value);
   }

   public static float cos(float value) {
      return class_3532.method_15362(value);
   }

   public static float wrapDegrees(float value) {
      return class_3532.method_15393(value);
   }

   public static double wrapDegrees(double value) {
      return class_3532.method_15338(value);
   }

   public static double square(double input) {
      return input * input;
   }

   public static double round(double value, int places) {
      BigDecimal bd = BigDecimal.valueOf(value);
      bd = bd.setScale(places, RoundingMode.HALF_UP);
      return bd.doubleValue();
   }

   public static float wrap(float angle) {
      float wrappedAngle = angle % 360.0F;
      if (wrappedAngle >= 180.0F) {
         wrappedAngle -= 360.0F;
      }

      if (wrappedAngle < -180.0F) {
         wrappedAngle += 360.0F;
      }

      return wrappedAngle;
   }

   public static class_243 direction(float yaw) {
      return new class_243(Math.cos(degToRad((double)(yaw + 90.0F))), 0.0D, Math.sin(degToRad((double)(yaw + 90.0F))));
   }

   public static float round(float value, int places) {
      if (places < 0) {
         throw new IllegalArgumentException();
      } else {
         BigDecimal bd = BigDecimal.valueOf((double)value);
         bd = bd.setScale(places, RoundingMode.FLOOR);
         return bd.floatValue();
      }
   }

   public static float round2(double value) {
      BigDecimal bd = new BigDecimal(value);
      bd = bd.setScale(2, RoundingMode.HALF_UP);
      return bd.floatValue();
   }

   public static <K, V extends Comparable<? super V>> LinkedHashMap<K, V> sortByValue(Map<K, V> map, boolean descending) {
      List<Entry<K, V>> list = new LinkedList(map.entrySet());
      if (descending) {
         list.sort(Entry.comparingByValue(Comparator.reverseOrder()));
      } else {
         list.sort(Entry.comparingByValue());
      }

      LinkedHashMap<K, V> result = new LinkedHashMap();
      Iterator var4 = list.iterator();

      while(var4.hasNext()) {
         Entry<K, V> entry = (Entry)var4.next();
         result.put(entry.getKey(), (Comparable)entry.getValue());
      }

      return result;
   }

   public static double degToRad(double deg) {
      return deg * 0.01745329238474369D;
   }
}
