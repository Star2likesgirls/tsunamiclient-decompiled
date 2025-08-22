package tsunami.features.modules.client;

import net.minecraft.class_243;
import net.minecraft.class_3532;
import tsunami.core.Managers;
import tsunami.core.manager.client.ModuleManager;
import tsunami.events.impl.EventFixVelocity;
import tsunami.events.impl.EventKeyboardInput;
import tsunami.events.impl.EventPlayerJump;
import tsunami.events.impl.EventPlayerTravel;
import tsunami.features.modules.Module;
import tsunami.features.modules.combat.Aura;
import tsunami.setting.Setting;

public class Rotations extends Module {
   private final Setting<Rotations.MoveFix> moveFix;
   public final Setting<Boolean> clientLook;
   public float fixRotation;
   private float prevYaw;
   private float prevPitch;

   public Rotations() {
      super("Rotations", Module.Category.CLIENT);
      this.moveFix = new Setting("MoveFix", Rotations.MoveFix.Off);
      this.clientLook = new Setting("ClientLook", false);
   }

   public void onJump(EventPlayerJump e) {
      if (!Float.isNaN(this.fixRotation) && this.moveFix.getValue() != Rotations.MoveFix.Off && !mc.field_1724.method_3144()) {
         if (e.isPre()) {
            this.prevYaw = mc.field_1724.method_36454();
            mc.field_1724.method_36456(this.fixRotation);
         } else {
            mc.field_1724.method_36456(this.prevYaw);
         }

      }
   }

   public void onPlayerMove(EventFixVelocity event) {
      if (this.moveFix.getValue() == Rotations.MoveFix.Free) {
         if (Float.isNaN(this.fixRotation) || mc.field_1724.method_3144()) {
            return;
         }

         event.setVelocity(this.fix(this.fixRotation, event.getMovementInput(), event.getSpeed()));
      }

   }

   public void modifyVelocity(EventPlayerTravel e) {
      if (ModuleManager.aura.isEnabled()) {
         Aura var10000 = ModuleManager.aura;
         if (Aura.target != null && ModuleManager.aura.rotationMode.not(Aura.Mode.None) && (Boolean)ModuleManager.aura.elytraTarget.getValue() && Managers.PLAYER.ticksElytraFlying > 5) {
            if (e.isPre()) {
               this.prevYaw = mc.field_1724.method_36454();
               this.prevPitch = mc.field_1724.method_36455();
               mc.field_1724.method_36456(this.fixRotation);
               mc.field_1724.method_36457(ModuleManager.aura.rotationPitch);
            } else {
               mc.field_1724.method_36456(this.prevYaw);
               mc.field_1724.method_36457(this.prevPitch);
            }

            return;
         }
      }

      if (this.moveFix.getValue() == Rotations.MoveFix.Focused && !Float.isNaN(this.fixRotation) && !mc.field_1724.method_3144()) {
         if (e.isPre()) {
            this.prevYaw = mc.field_1724.method_36454();
            mc.field_1724.method_36456(this.fixRotation);
         } else {
            mc.field_1724.method_36456(this.prevYaw);
         }
      }

   }

   public void onKeyInput(EventKeyboardInput e) {
      if (this.moveFix.getValue() == Rotations.MoveFix.Free) {
         if (Float.isNaN(this.fixRotation) || mc.field_1724.method_3144()) {
            return;
         }

         float mF = mc.field_1724.field_3913.field_3905;
         float mS = mc.field_1724.field_3913.field_3907;
         float delta = (mc.field_1724.method_36454() - this.fixRotation) * 0.017453292F;
         float cos = class_3532.method_15362(delta);
         float sin = class_3532.method_15374(delta);
         mc.field_1724.field_3913.field_3907 = (float)Math.round(mS * cos - mF * sin);
         mc.field_1724.field_3913.field_3905 = (float)Math.round(mF * cos + mS * sin);
      }

   }

   private class_243 fix(float yaw, class_243 movementInput, float speed) {
      double d = movementInput.method_1027();
      if (d < 1.0E-7D) {
         return class_243.field_1353;
      } else {
         class_243 vec3d = (d > 1.0D ? movementInput.method_1029() : movementInput).method_1021((double)speed);
         float f = class_3532.method_15374(yaw * 0.017453292F);
         float g = class_3532.method_15362(yaw * 0.017453292F);
         return new class_243(vec3d.field_1352 * (double)g - vec3d.field_1350 * (double)f, vec3d.field_1351, vec3d.field_1350 * (double)g + vec3d.field_1352 * (double)f);
      }
   }

   public boolean isToggleable() {
      return false;
   }

   private static enum MoveFix {
      Off,
      Focused,
      Free;

      // $FF: synthetic method
      private static Rotations.MoveFix[] $values() {
         return new Rotations.MoveFix[]{Off, Focused, Free};
      }
   }
}
