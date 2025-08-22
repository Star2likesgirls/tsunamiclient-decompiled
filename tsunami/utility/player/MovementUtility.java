package tsunami.utility.player;

import net.minecraft.class_1294;
import net.minecraft.class_3532;
import tsunami.core.Managers;
import tsunami.events.impl.EventMove;
import tsunami.features.modules.Module;

public final class MovementUtility {
   public static boolean isMoving() {
      return Module.mc.field_1724 != null && Module.mc.field_1687 != null && Module.mc.field_1724.field_3913 != null && ((double)Module.mc.field_1724.field_3913.field_3905 != 0.0D || (double)Module.mc.field_1724.field_3913.field_3907 != 0.0D);
   }

   public static double getSpeed() {
      return Math.hypot(Module.mc.field_1724.method_18798().field_1352, Module.mc.field_1724.method_18798().field_1350);
   }

   public static double[] forward(double d) {
      float f = Module.mc.field_1724.field_3913.field_3905;
      float f2 = Module.mc.field_1724.field_3913.field_3907;
      float f3 = Module.mc.field_1724.method_36454();
      if (f != 0.0F) {
         if (f2 > 0.0F) {
            f3 += (float)(f > 0.0F ? -45 : 45);
         } else if (f2 < 0.0F) {
            f3 += (float)(f > 0.0F ? 45 : -45);
         }

         f2 = 0.0F;
         if (f > 0.0F) {
            f = 1.0F;
         } else if (f < 0.0F) {
            f = -1.0F;
         }
      }

      double d2 = Math.sin(Math.toRadians((double)(f3 + 90.0F)));
      double d3 = Math.cos(Math.toRadians((double)(f3 + 90.0F)));
      double d4 = (double)f * d * d3 + (double)f2 * d * d2;
      double d5 = (double)f * d * d2 - (double)f2 * d * d3;
      return new double[]{d4, d5};
   }

   public static void setMotion(double speed) {
      double forward = (double)Module.mc.field_1724.field_3913.field_3905;
      double strafe = (double)Module.mc.field_1724.field_3913.field_3907;
      float yaw = Module.mc.field_1724.method_36454();
      if (forward == 0.0D && strafe == 0.0D) {
         Module.mc.field_1724.method_18800(0.0D, Module.mc.field_1724.method_18798().field_1351, 0.0D);
      } else {
         if (forward != 0.0D) {
            if (strafe > 0.0D) {
               yaw += (float)(forward > 0.0D ? -45 : 45);
            } else if (strafe < 0.0D) {
               yaw += (float)(forward > 0.0D ? 45 : -45);
            }

            strafe = 0.0D;
            if (forward > 0.0D) {
               forward = 1.0D;
            } else if (forward < 0.0D) {
               forward = -1.0D;
            }
         }

         double sin = (double)class_3532.method_15374((float)Math.toRadians((double)(yaw + 90.0F)));
         double cos = (double)class_3532.method_15362((float)Math.toRadians((double)(yaw + 90.0F)));
         Module.mc.field_1724.method_18800(forward * speed * cos + strafe * speed * sin, Module.mc.field_1724.method_18798().field_1351, forward * speed * sin - strafe * speed * cos);
      }

   }

   public static float getMoveDirection() {
      double forward = (double)Module.mc.field_1724.field_3913.field_3905;
      double strafe = (double)Module.mc.field_1724.field_3913.field_3907;
      if (strafe > 0.0D) {
         strafe = 1.0D;
      } else if (strafe < 0.0D) {
         strafe = -1.0D;
      }

      float yaw = Module.mc.field_1724.method_36454();
      if (forward == 0.0D && strafe == 0.0D) {
         return yaw;
      } else {
         if (forward != 0.0D) {
            if (strafe > 0.0D) {
               yaw += forward > 0.0D ? -45.0F : -135.0F;
            } else if (strafe < 0.0D) {
               yaw += forward > 0.0D ? 45.0F : 135.0F;
            } else if (forward < 0.0D) {
               yaw += 180.0F;
            }
         }

         if (forward == 0.0D) {
            if (strafe > 0.0D) {
               yaw -= 90.0F;
            } else if (strafe < 0.0D) {
               yaw += 90.0F;
            }
         }

         return yaw;
      }
   }

   public static double[] forwardWithoutStrafe(double d) {
      float f3 = Module.mc.field_1724.method_36454();
      double d4 = d * Math.cos(Math.toRadians((double)(f3 + 90.0F)));
      double d5 = d * Math.sin(Math.toRadians((double)(f3 + 90.0F)));
      return new double[]{d4, d5};
   }

   public static double getJumpSpeed() {
      double jumpSpeed = 0.3999999463558197D;
      if (Module.mc.field_1724.method_6059(class_1294.field_5913)) {
         double amplifier = (double)Module.mc.field_1724.method_6112(class_1294.field_5913).method_5578();
         jumpSpeed += (amplifier + 1.0D) * 0.1D;
      }

      return jumpSpeed;
   }

   public static void modifyEventSpeed(EventMove event, double d) {
      double d2 = (double)Module.mc.field_1724.field_3913.field_3905;
      double d3 = (double)Module.mc.field_1724.field_3913.field_3907;
      float f = Module.mc.field_1724.method_36454();
      if (d2 == 0.0D && d3 == 0.0D) {
         event.setX(0.0D);
         event.setZ(0.0D);
      } else {
         if (d2 != 0.0D) {
            if (d3 > 0.0D) {
               f += (float)(d2 > 0.0D ? -45 : 45);
            } else if (d3 < 0.0D) {
               f += (float)(d2 > 0.0D ? 45 : -45);
            }

            d3 = 0.0D;
            if (d2 > 0.0D) {
               d2 = 1.0D;
            } else if (d2 < 0.0D) {
               d2 = -1.0D;
            }
         }

         double sin = Math.sin(Math.toRadians((double)(f + 90.0F)));
         double cos = Math.cos(Math.toRadians((double)(f + 90.0F)));
         event.setX(d2 * d * cos + d3 * d * sin);
         event.setZ(d2 * d * sin - d3 * d * cos);
      }

   }

   public static double getBaseMoveSpeed() {
      double d = 0.2873D;
      if (Module.fullNullCheck()) {
         return d;
      } else {
         int n;
         if (Module.mc.field_1724.method_6059(class_1294.field_5904)) {
            n = Module.mc.field_1724.method_6112(class_1294.field_5904).method_5578();
            d *= 1.0D + 0.2D * (double)(n + 1);
         }

         if (Module.mc.field_1724.method_6059(class_1294.field_5913)) {
            n = Module.mc.field_1724.method_6112(class_1294.field_5913).method_5578();
            d /= 1.0D + 0.2D * (double)(n + 1);
         }

         if (Module.mc.field_1724.method_6059(class_1294.field_5909)) {
            n = Module.mc.field_1724.method_6112(class_1294.field_5909).method_5578();
            d /= 1.0D + 0.2D * (double)(n + 1);
         }

         return d;
      }
   }

   public static boolean sprintIsLegit(float yaw) {
      return Math.abs(Math.abs(class_3532.method_15393(yaw)) - Math.abs(class_3532.method_15393(Managers.PLAYER.yaw))) < 40.0F;
   }
}
