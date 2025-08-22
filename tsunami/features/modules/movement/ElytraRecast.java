package tsunami.features.modules.movement;

import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_1294;
import net.minecraft.class_1304;
import net.minecraft.class_1770;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_2848;
import net.minecraft.class_3675;
import net.minecraft.class_2848.class_2849;
import tsunami.TsunamiClient;
import tsunami.events.impl.EventSync;
import tsunami.events.impl.EventTravel;
import tsunami.features.modules.Module;
import tsunami.injection.accesors.ILivingEntity;
import tsunami.setting.Setting;
import tsunami.utility.math.MathUtility;

public class ElytraRecast extends Module {
   public Setting<ElytraRecast.Exploit> exploit;
   public Setting<Boolean> changePitch;
   public Setting<Float> pitchValue;
   public Setting<Boolean> autoWalk;
   public Setting<Boolean> autoJump;
   public Setting<Boolean> allowBroken;
   private float prevClientPitch;
   private float prevClientYaw;
   private float jitter;

   public ElytraRecast() {
      super("ElytraRecast", Module.Category.NONE);
      this.exploit = new Setting("Exploit", ElytraRecast.Exploit.None);
      this.changePitch = new Setting("ChangePitch", true);
      this.pitchValue = new Setting("PitchValue", 55.0F, -90.0F, 90.0F, (v) -> {
         return (Boolean)this.changePitch.getValue();
      });
      this.autoWalk = new Setting("AutoWalk", true);
      this.autoJump = new Setting("AutoJump", true);
      this.allowBroken = new Setting("AllowBroken", true);
   }

   @EventHandler
   public void onSync(EventSync e) {
      if ((Boolean)this.changePitch.getValue()) {
         mc.field_1724.method_36457((Float)this.pitchValue.getValue());
      }

      switch(((ElytraRecast.Exploit)this.exploit.getValue()).ordinal()) {
      case 0:
      default:
         break;
      case 1:
         mc.field_1724.method_36456(mc.field_1724.method_36454() + this.jitter);
         break;
      case 2:
         mc.field_1724.method_36457((Float)this.pitchValue.getValue() - Math.abs(this.jitter / 2.0F));
      }

   }

   @EventHandler
   public void modifyVelocity(EventTravel e) {
      if ((Boolean)this.changePitch.getValue()) {
         if (e.isPre()) {
            this.prevClientPitch = mc.field_1724.method_36455();
            this.prevClientYaw = mc.field_1724.method_36454();
            mc.field_1724.method_36457((Float)this.pitchValue.getValue());
            switch(((ElytraRecast.Exploit)this.exploit.getValue()).ordinal()) {
            case 0:
            default:
               break;
            case 1:
               mc.field_1724.method_36456(mc.field_1724.method_36454() + this.jitter);
               break;
            case 2:
               mc.field_1724.method_36457((Float)this.pitchValue.getValue() - Math.abs(this.jitter / 2.0F));
            }
         } else {
            mc.field_1724.method_36457(this.prevClientPitch);
            if (this.exploit.getValue() == ElytraRecast.Exploit.Strict) {
               mc.field_1724.method_36456(this.prevClientYaw);
            }
         }
      }

   }

   public void onDisable() {
      if (!class_3675.method_15987(mc.method_22683().method_4490(), mc.field_1690.field_1894.method_1429().method_1444())) {
         mc.field_1690.field_1894.method_23481(false);
      }

      if (!class_3675.method_15987(mc.method_22683().method_4490(), mc.field_1690.field_1903.method_1429().method_1444())) {
         mc.field_1690.field_1903.method_23481(false);
      }

   }

   public void onUpdate() {
      if ((Boolean)this.autoJump.getValue()) {
         mc.field_1690.field_1903.method_23481(true);
      }

      if ((Boolean)this.autoWalk.getValue()) {
         mc.field_1690.field_1894.method_23481(true);
      }

      if (!mc.field_1724.method_6128() && mc.field_1724.field_6017 > 0.0F && this.checkElytra() && !mc.field_1724.method_6128()) {
         this.castElytra();
      }

      this.jitter = 20.0F * MathUtility.sin((float)(System.currentTimeMillis() - TsunamiClient.initTime) / 50.0F);
      ((ILivingEntity)mc.field_1724).setLastJumpCooldown(0);
   }

   public boolean castElytra() {
      if (this.checkElytra() && this.check()) {
         this.sendPacket(new class_2848(mc.field_1724, class_2849.field_12982));
         return true;
      } else {
         return false;
      }
   }

   private boolean checkElytra() {
      if (mc.field_1724.field_3913.field_3904 && !mc.field_1724.method_31549().field_7479 && !mc.field_1724.method_5765() && !mc.field_1724.method_6101()) {
         class_1799 is = mc.field_1724.method_6118(class_1304.field_6174);
         return is.method_31574(class_1802.field_8833) && (class_1770.method_7804(is) || (Boolean)this.allowBroken.getValue());
      } else {
         return false;
      }
   }

   private boolean check() {
      if (!mc.field_1724.method_5799() && !mc.field_1724.method_6059(class_1294.field_5902)) {
         class_1799 is = mc.field_1724.method_6118(class_1304.field_6174);
         if (is.method_31574(class_1802.field_8833) && (class_1770.method_7804(is) || (Boolean)this.allowBroken.getValue())) {
            mc.field_1724.method_23669();
            return true;
         }
      }

      return false;
   }

   private static enum Exploit {
      None,
      Strict,
      Strong;

      // $FF: synthetic method
      private static ElytraRecast.Exploit[] $values() {
         return new ElytraRecast.Exploit[]{None, Strict, Strong};
      }
   }
}
