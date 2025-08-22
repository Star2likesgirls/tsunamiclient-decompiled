package tsunami.utility.player;

import java.util.Objects;
import net.minecraft.class_1799;
import net.minecraft.class_241;
import net.minecraft.class_243;
import net.minecraft.class_746;
import net.minecraft.class_9334;
import net.minecraft.class_239.class_240;
import org.jetbrains.annotations.NotNull;
import tsunami.features.modules.Module;
import tsunami.utility.world.ExplosionUtility;

public final class PlayerUtility {
   public static boolean isInHell() {
      return Module.mc.field_1687 == null ? false : Objects.equals(Module.mc.field_1687.method_27983().method_29177().method_12832(), "the_nether");
   }

   public static boolean isInEnd() {
      return Module.mc.field_1687 == null ? false : Objects.equals(Module.mc.field_1687.method_27983().method_29177().method_12832(), "the_end");
   }

   public static boolean isInOver() {
      return Module.mc.field_1687 == null ? false : Objects.equals(Module.mc.field_1687.method_27983().method_29177().method_12832(), "overworld");
   }

   public static boolean isEating() {
      if (Module.mc.field_1724 == null) {
         return false;
      } else {
         return (Module.mc.field_1724.method_6047().method_57353().method_57832(class_9334.field_50075) || Module.mc.field_1724.method_6079().method_57353().method_57832(class_9334.field_50075)) && Module.mc.field_1724.method_6115();
      }
   }

   public static boolean isMining() {
      return Module.mc.field_1761 == null ? false : Module.mc.field_1761.method_2923();
   }

   public static float squaredDistanceFromEyes(@NotNull class_243 targetPos) {
      if (Module.mc.field_1724 == null) {
         return 0.0F;
      } else {
         double dx = targetPos.field_1352 - Module.mc.field_1724.method_23317();
         double dy = targetPos.field_1351 - (Module.mc.field_1724.method_23318() + (double)Module.mc.field_1724.method_18381(Module.mc.field_1724.method_18376()));
         double dz = targetPos.field_1350 - Module.mc.field_1724.method_23321();
         return (float)(dx * dx + dy * dy + dz * dz);
      }
   }

   public static float squaredDistance2d(@NotNull class_241 point) {
      if (Module.mc.field_1724 == null) {
         return 0.0F;
      } else {
         double d = Module.mc.field_1724.method_23317() - (double)point.field_1343;
         double f = Module.mc.field_1724.method_23321() - (double)point.field_1342;
         return (float)(d * d + f * f);
      }
   }

   public static class_746 getPlayer() {
      return Module.mc.field_1724;
   }

   public static float calculatePercentage(@NotNull class_1799 stack) {
      float durability = (float)(stack.method_7936() - stack.method_7919());
      return durability / (float)stack.method_7936() * 100.0F;
   }

   public static float fixAngle(float angle) {
      return (float)Math.round(angle / (float)((double)getGCD() * 0.15D)) * (float)((double)getGCD() * 0.15D);
   }

   public static float getGCD() {
      double sensitivity = (Double)Module.mc.field_1690.method_42495().method_41753();
      double value = sensitivity * 0.6D + 0.2D;
      double result = Math.pow(value, 3.0D) * 8.0D;
      return (float)result;
   }

   public static float squaredDistance2d(double x, double z) {
      if (Module.mc.field_1724 == null) {
         return 0.0F;
      } else {
         double d = Module.mc.field_1724.method_23317() - x;
         double f = Module.mc.field_1724.method_23321() - z;
         return (float)(d * d + f * f);
      }
   }

   public static float getSquaredDistance2D(class_243 vec) {
      double d0 = Module.mc.field_1724.method_23317() - vec.method_10216();
      double d2 = Module.mc.field_1724.method_23321() - vec.method_10215();
      return (float)(d0 * d0 + d2 * d2);
   }

   public static boolean canSee(class_243 pos) {
      class_243 vec3d = new class_243(Module.mc.field_1724.method_23317(), Module.mc.field_1724.method_23320(), Module.mc.field_1724.method_23321());
      if (pos.method_1022(vec3d) > 128.0D) {
         return false;
      } else {
         return ExplosionUtility.raycast(vec3d, pos, false) == class_240.field_1333;
      }
   }

   public static boolean isFalling() {
      if (Module.mc.field_1724 == null) {
         return false;
      } else {
         return !Module.mc.field_1724.method_24828() && !Module.mc.field_1724.method_7337() && Module.mc.field_1724.method_18798().field_1351 < 0.0D;
      }
   }
}
