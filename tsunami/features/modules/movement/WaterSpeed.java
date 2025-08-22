package tsunami.features.modules.movement;

import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_1293;
import net.minecraft.class_1294;
import tsunami.events.impl.EventMove;
import tsunami.features.modules.Module;
import tsunami.setting.Setting;
import tsunami.utility.math.MathUtility;
import tsunami.utility.player.MovementUtility;

public class WaterSpeed extends Module {
   public final Setting<WaterSpeed.Mode> mode;
   private float acceleration;

   public WaterSpeed() {
      super("WaterSpeed", Module.Category.NONE);
      this.mode = new Setting("Mode", WaterSpeed.Mode.DolphinGrace);
      this.acceleration = 0.0F;
   }

   public void onUpdate() {
      if (this.mode.getValue() == WaterSpeed.Mode.DolphinGrace) {
         if (mc.field_1724.method_5681()) {
            mc.field_1724.method_6092(new class_1293(class_1294.field_5900, 2, 2));
         } else {
            mc.field_1724.method_6016(class_1294.field_5900);
         }
      }

   }

   @EventHandler
   public void onMove(EventMove e) {
      double[] dirSpeed;
      if (this.mode.getValue() == WaterSpeed.Mode.Intave) {
         if (mc.field_1724.method_5681()) {
            dirSpeed = MovementUtility.forward((double)(this.acceleration / (mc.field_1724.field_3913.field_3907 != 0.0F ? 2.2F : 2.0F)));
            e.setX(e.getX() + dirSpeed[0]);
            e.setZ(e.getZ() + dirSpeed[1]);
            e.cancel();
            this.acceleration += 0.05F;
            this.acceleration = MathUtility.clamp(this.acceleration, 0.0F, 1.0F);
         } else {
            this.acceleration = 0.0F;
         }

         if (!MovementUtility.isMoving()) {
            this.acceleration = 0.0F;
         }
      }

      if (this.mode.getValue() == WaterSpeed.Mode.FunTimeNew) {
         if (mc.field_1724.method_5681()) {
            mc.field_1724.field_3913.field_3907 = 0.0F;
            dirSpeed = MovementUtility.forward((double)(this.acceleration / 6.3447F));
            e.setX(e.getX() + dirSpeed[0]);
            e.setZ(e.getZ() + dirSpeed[1]);
            e.cancel();
            if (Math.abs(mc.field_1724.method_36454() - mc.field_1724.field_5982) > 3.0F) {
               this.acceleration -= 0.1F;
            } else {
               this.acceleration += 0.015F;
            }

            this.acceleration = MathUtility.clamp(this.acceleration, 0.0F, 1.0F);
         } else {
            this.acceleration = 0.0F;
         }

         if (!MovementUtility.isMoving() || mc.field_1724.field_5976 || mc.field_1724.field_5992) {
            this.acceleration = 0.0F;
         }
      }

   }

   public void onDisable() {
      if (this.mode.getValue() == WaterSpeed.Mode.DolphinGrace) {
         mc.field_1724.method_6016(class_1294.field_5900);
      }

   }

   public static enum Mode {
      DolphinGrace,
      Intave,
      CancelResurface,
      FunTimeNew;

      // $FF: synthetic method
      private static WaterSpeed.Mode[] $values() {
         return new WaterSpeed.Mode[]{DolphinGrace, Intave, CancelResurface, FunTimeNew};
      }
   }
}
