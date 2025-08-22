package tsunami.features.modules.player;

import meteordevelopment.orbit.EventHandler;
import tsunami.core.manager.client.ModuleManager;
import tsunami.events.impl.EventSync;
import tsunami.events.impl.PlayerUpdateEvent;
import tsunami.features.modules.Module;
import tsunami.setting.Setting;
import tsunami.utility.math.MathUtility;

public class AntiAim extends Module {
   private final Setting<AntiAim.Mode> pitchMode;
   private final Setting<AntiAim.Mode> yawMode;
   public Setting<Integer> Speed;
   public Setting<Integer> yawDelta;
   public Setting<Integer> pitchDelta;
   public Setting<Integer> yawOffset;
   public final Setting<Boolean> bodySync;
   public final Setting<Boolean> allowInteract;
   private float rotationYaw;
   private float rotationPitch;
   private float pitch_sinus_step;
   private float yaw_sinus_step;

   public AntiAim() {
      super("AntiAim", Module.Category.NONE);
      this.pitchMode = new Setting("PitchMode", AntiAim.Mode.None);
      this.yawMode = new Setting("YawMode", AntiAim.Mode.None);
      this.Speed = new Setting("Speed", 1, 1, 45);
      this.yawDelta = new Setting("YawDelta", 60, -360, 360);
      this.pitchDelta = new Setting("PitchDelta", 10, -90, 90);
      this.yawOffset = new Setting("YawOffset", 0, -180, 180);
      this.bodySync = new Setting("BodySync", true);
      this.allowInteract = new Setting("AllowInteract", true);
   }

   @EventHandler(
      priority = 99
   )
   public void onSync(EventSync e) {
      if (!(Boolean)this.allowInteract.getValue() || !mc.field_1690.field_1886.method_1434() && !mc.field_1690.field_1886.method_1434()) {
         double gcdFix = Math.pow((Double)mc.field_1690.method_42495().method_41753() * 0.6D + 0.2D, 3.0D) * 1.2D;
         if (this.yawMode.getValue() != AntiAim.Mode.None) {
            mc.field_1724.method_36456((float)((double)this.rotationYaw - (double)(this.rotationYaw - mc.field_1724.method_36454()) % gcdFix));
            if ((Boolean)this.bodySync.getValue()) {
               mc.field_1724.method_5636(this.rotationYaw);
            }
         }

         if (this.pitchMode.getValue() != AntiAim.Mode.None) {
            mc.field_1724.method_36457((float)((double)this.rotationPitch - (double)(this.rotationPitch - mc.field_1724.method_36455()) % gcdFix));
         }

      }
   }

   @EventHandler(
      priority = 100
   )
   public void onCalc(PlayerUpdateEvent e) {
      if (this.pitchMode.getValue() == AntiAim.Mode.RandomAngle && mc.field_1724.field_6012 % (Integer)this.Speed.getValue() == 0) {
         this.rotationPitch = MathUtility.random(90.0F, -90.0F);
      }

      if (this.yawMode.getValue() == AntiAim.Mode.RandomAngle && mc.field_1724.field_6012 % (Integer)this.Speed.getValue() == 0) {
         this.rotationYaw = MathUtility.random(0.0F, 360.0F);
      }

      if (this.yawMode.getValue() == AntiAim.Mode.Spin && mc.field_1724.field_6012 % (Integer)this.Speed.getValue() == 0) {
         this.rotationYaw += (float)(Integer)this.yawDelta.getValue();
         if (this.rotationYaw > 360.0F) {
            this.rotationYaw = 0.0F;
         }

         if (this.rotationYaw < 0.0F) {
            this.rotationYaw = 360.0F;
         }
      }

      if (this.pitchMode.getValue() == AntiAim.Mode.Spin && mc.field_1724.field_6012 % (Integer)this.Speed.getValue() == 0) {
         this.rotationPitch += (float)(Integer)this.pitchDelta.getValue();
         if (this.rotationPitch > 90.0F) {
            this.rotationPitch = -90.0F;
         }

         if (this.rotationPitch < -90.0F) {
            this.rotationPitch = 90.0F;
         }
      }

      if (this.pitchMode.getValue() == AntiAim.Mode.Sinus) {
         this.pitch_sinus_step += (float)(Integer)this.Speed.getValue() / 10.0F;
         this.rotationPitch = (float)((double)mc.field_1724.method_36455() + (double)(Integer)this.pitchDelta.getValue() * Math.sin((double)this.pitch_sinus_step));
         this.rotationPitch = MathUtility.clamp(this.rotationPitch, -90.0F, 90.0F);
      }

      if (this.yawMode.getValue() == AntiAim.Mode.Sinus) {
         this.yaw_sinus_step += (float)(Integer)this.Speed.getValue() / 10.0F;
         this.rotationYaw = (float)((double)mc.field_1724.method_36454() + (double)(Integer)this.yawDelta.getValue() * Math.sin((double)this.yaw_sinus_step) + (double)(Integer)this.yawOffset.getValue());
      }

      if (this.pitchMode.getValue() == AntiAim.Mode.Fixed) {
         this.rotationPitch = (float)(Integer)this.pitchDelta.getValue();
      }

      if (this.yawMode.getValue() == AntiAim.Mode.Fixed) {
         this.rotationYaw = (float)(Integer)this.yawDelta.getValue();
      }

      if (this.pitchMode.getValue() == AntiAim.Mode.Static) {
         this.rotationPitch = mc.field_1724.method_36455() + (float)(Integer)this.pitchDelta.getValue();
         this.rotationPitch = MathUtility.clamp(this.rotationPitch, -90.0F, 90.0F);
      }

      if (this.yawMode.getValue() == AntiAim.Mode.Static) {
         this.rotationYaw = mc.field_1724.method_36454() % 360.0F + (float)(Integer)this.yawDelta.getValue();
      }

      if (this.pitchMode.getValue() == AntiAim.Mode.Jitter) {
         if (mc.field_1724.field_6012 % ((Integer)this.Speed.getValue() * 2) == 0) {
            this.rotationPitch = (float)(Integer)this.pitchDelta.getValue() / 2.0F;
         }

         if (mc.field_1724.field_6012 % ((Integer)this.Speed.getValue() * 2) == (Integer)this.Speed.getValue()) {
            this.rotationPitch = (float)(Integer)this.pitchDelta.getValue() / -2.0F;
         }
      }

      if (this.yawMode.getValue() == AntiAim.Mode.Jitter) {
         if (mc.field_1724.field_6012 % ((Integer)this.Speed.getValue() * 2) == 0) {
            this.rotationYaw = (float)(Integer)this.yawDelta.getValue() / 2.0F + (float)(Integer)this.yawOffset.getValue() + mc.field_1724.method_36454();
         }

         if (mc.field_1724.field_6012 % ((Integer)this.Speed.getValue() * 2) == (Integer)this.Speed.getValue()) {
            this.rotationYaw = (float)(Integer)this.yawDelta.getValue() / -2.0F + (float)(Integer)this.yawOffset.getValue() + mc.field_1724.method_36454();
         }
      }

      ModuleManager.rotations.fixRotation = this.rotationYaw;
   }

   public static enum Mode {
      None,
      RandomAngle,
      Spin,
      Sinus,
      Fixed,
      Static,
      Jitter;

      // $FF: synthetic method
      private static AntiAim.Mode[] $values() {
         return new AntiAim.Mode[]{None, RandomAngle, Spin, Sinus, Fixed, Static, Jitter};
      }
   }
}
