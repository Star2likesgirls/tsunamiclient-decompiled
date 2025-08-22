package tsunami.features.modules.movement;

import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_2596;
import net.minecraft.class_2664;
import net.minecraft.class_2708;
import net.minecraft.class_2743;
import net.minecraft.class_2846;
import net.minecraft.class_6373;
import net.minecraft.class_2828.class_2829;
import net.minecraft.class_2828.class_2830;
import net.minecraft.class_2846.class_2847;
import tsunami.core.manager.client.ModuleManager;
import tsunami.events.impl.PacketEvent;
import tsunami.features.modules.Module;
import tsunami.injection.accesors.IClientPlayerEntity;
import tsunami.injection.accesors.IExplosionS2CPacket;
import tsunami.injection.accesors.ISPacketEntityVelocity;
import tsunami.setting.Setting;
import tsunami.utility.player.MovementUtility;

public class Velocity extends Module {
   public Setting<Boolean> onlyAura = new Setting("OnlyDuringAura", false);
   public Setting<Boolean> pauseInWater = new Setting("PauseInLiquids", false);
   public Setting<Boolean> explosions = new Setting("Explosions", true);
   public Setting<Boolean> cc = new Setting("PauseOnFlag", false);
   public Setting<Boolean> fire = new Setting("PauseOnFire", false);
   private final Setting<Velocity.modeEn> mode;
   public Setting<Float> vertical;
   private final Setting<Velocity.jumpModeEn> jumpMode;
   public Setting<Float> horizontal;
   public Setting<Float> motion;
   public Setting<Boolean> fail;
   public Setting<Float> failRate;
   public Setting<Float> jumpRate;
   private boolean doJump;
   private boolean failJump;
   private boolean skip;
   private boolean flag;
   private int grimTicks;
   private int ccCooldown;

   public Velocity() {
      super("Velocity", Module.Category.NONE);
      this.mode = new Setting("Mode", Velocity.modeEn.Matrix);
      this.vertical = new Setting("Vertical", 0.0F, 0.0F, 100.0F, (v) -> {
         return this.mode.getValue() == Velocity.modeEn.Custom;
      });
      this.jumpMode = new Setting("JumpMode", Velocity.jumpModeEn.Jump, (v) -> {
         return this.mode.getValue() == Velocity.modeEn.Jump;
      });
      this.horizontal = new Setting("Horizontal", 0.0F, 0.0F, 100.0F, (v) -> {
         return this.mode.getValue() == Velocity.modeEn.Custom || this.mode.getValue() == Velocity.modeEn.Jump;
      });
      this.motion = new Setting("Motion", 0.42F, 0.4F, 0.5F, (v) -> {
         return this.mode.getValue() == Velocity.modeEn.Jump;
      });
      this.fail = new Setting("SmartFail", true, (v) -> {
         return this.mode.getValue() == Velocity.modeEn.Jump;
      });
      this.failRate = new Setting("FailRate", 0.3F, 0.0F, 1.0F, (v) -> {
         return this.mode.getValue() == Velocity.modeEn.Jump && (Boolean)this.fail.getValue();
      });
      this.jumpRate = new Setting("FailJumpRate", 0.25F, 0.0F, 1.0F, (v) -> {
         return this.mode.getValue() == Velocity.modeEn.Jump && (Boolean)this.fail.getValue();
      });
   }

   @EventHandler
   public void onPacketReceive(PacketEvent.Receive e) {
      if (!fullNullCheck()) {
         if (mc.field_1724 == null || !mc.field_1724.method_5799() && !mc.field_1724.method_5869() && !mc.field_1724.method_5771() || !(Boolean)this.pauseInWater.getValue()) {
            if (mc.field_1724 == null || !mc.field_1724.method_5809() || !(Boolean)this.fire.getValue() || mc.field_1724.field_6235 <= 0) {
               if (this.ccCooldown > 0) {
                  --this.ccCooldown;
               } else {
                  class_2596 var3 = e.getPacket();
                  if (var3 instanceof class_2743) {
                     class_2743 pac = (class_2743)var3;
                     if (pac.method_11818() == mc.field_1724.method_5628() && (!(Boolean)this.onlyAura.getValue() || ModuleManager.aura.isEnabled())) {
                        switch(((Velocity.modeEn)this.mode.getValue()).ordinal()) {
                        case 0:
                           if (!this.flag) {
                              e.cancel();
                              this.flag = true;
                           } else {
                              this.flag = false;
                              ((ISPacketEntityVelocity)pac).setMotionX((int)(pac.method_11815() * -0.1D));
                              ((ISPacketEntityVelocity)pac).setMotionZ((int)(pac.method_11819() * -0.1D));
                           }
                           break;
                        case 1:
                           e.cancel();
                           break;
                        case 2:
                           e.cancel();
                           this.sendPacket(new class_2829(mc.field_1724.method_23317(), -999.0D, mc.field_1724.method_23321(), true));
                           break;
                        case 3:
                           ((ISPacketEntityVelocity)pac).setMotionX((int)((float)pac.method_11815() * (Float)this.horizontal.getValue() / 100.0F));
                           ((ISPacketEntityVelocity)pac).setMotionY((int)((float)pac.method_11816() * (Float)this.vertical.getValue() / 100.0F));
                           ((ISPacketEntityVelocity)pac).setMotionZ((int)((float)pac.method_11819() * (Float)this.horizontal.getValue() / 100.0F));
                           break;
                        case 4:
                           double vX = Math.abs(pac.method_11815());
                           double vZ = Math.abs(pac.method_11819());
                           double[] motion = MovementUtility.forward(vX + vZ);
                           ((ISPacketEntityVelocity)pac).setMotionX((int)motion[0]);
                           ((ISPacketEntityVelocity)pac).setMotionY(0);
                           ((ISPacketEntityVelocity)pac).setMotionZ((int)motion[1]);
                           break;
                        case 5:
                           e.cancel();
                           this.grimTicks = 6;
                           break;
                        case 6:
                           ((ISPacketEntityVelocity)pac).setMotionX((int)((float)pac.method_11815() * (Float)this.horizontal.getValue() / 100.0F));
                           ((ISPacketEntityVelocity)pac).setMotionZ((int)((float)pac.method_11819() * (Float)this.horizontal.getValue() / 100.0F));
                           break;
                        case 7:
                           e.cancel();
                           this.flag = true;
                        }
                     }
                  }

                  var3 = e.getPacket();
                  if (var3 instanceof class_2664) {
                     class_2664 explosion = (class_2664)var3;
                     if ((Boolean)this.explosions.getValue()) {
                        switch(((Velocity.modeEn)this.mode.getValue()).ordinal()) {
                        case 1:
                           ((IExplosionS2CPacket)explosion).setMotionX(0.0F);
                           ((IExplosionS2CPacket)explosion).setMotionY(0.0F);
                           ((IExplosionS2CPacket)explosion).setMotionZ(0.0F);
                           break;
                        case 3:
                           ((IExplosionS2CPacket)explosion).setMotionX(((IExplosionS2CPacket)explosion).getMotionX() * (Float)this.horizontal.getValue() / 100.0F);
                           ((IExplosionS2CPacket)explosion).setMotionZ(((IExplosionS2CPacket)explosion).getMotionZ() * (Float)this.horizontal.getValue() / 100.0F);
                           ((IExplosionS2CPacket)explosion).setMotionY(((IExplosionS2CPacket)explosion).getMotionY() * (Float)this.vertical.getValue() / 100.0F);
                           break;
                        case 7:
                           ((IExplosionS2CPacket)explosion).setMotionX(0.0F);
                           ((IExplosionS2CPacket)explosion).setMotionY(0.0F);
                           ((IExplosionS2CPacket)explosion).setMotionZ(0.0F);
                           this.flag = true;
                        }
                     }
                  }

                  if (this.mode.getValue() == Velocity.modeEn.OldGrim && e.getPacket() instanceof class_6373 && this.grimTicks > 0) {
                     e.cancel();
                     --this.grimTicks;
                  }

                  if (e.getPacket() instanceof class_2708 && ((Boolean)this.cc.getValue() || this.mode.getValue() == Velocity.modeEn.GrimNew)) {
                     this.ccCooldown = 5;
                  }

               }
            }
         }
      }
   }

   public void onUpdate() {
      if (mc.field_1724 == null || !mc.field_1724.method_5799() && !mc.field_1724.method_5869() || !(Boolean)this.pauseInWater.getValue()) {
         switch(((Velocity.modeEn)this.mode.getValue()).ordinal()) {
         case 0:
            if (mc.field_1724.field_6235 > 0 && !mc.field_1724.method_24828()) {
               double var3 = (double)(mc.field_1724.method_36454() * 0.017453292F);
               double var5 = Math.sqrt(mc.field_1724.method_18798().field_1352 * mc.field_1724.method_18798().field_1352 + mc.field_1724.method_18798().field_1350 * mc.field_1724.method_18798().field_1350);
               mc.field_1724.method_18800(-Math.sin(var3) * var5, mc.field_1724.method_18798().field_1351, Math.cos(var3) * var5);
               mc.field_1724.method_5728(mc.field_1724.field_6012 % 2 != 0);
            }
            break;
         case 6:
            if ((this.failJump || mc.field_1724.field_6235 > 6) && mc.field_1724.method_24828()) {
               if (this.failJump) {
                  this.failJump = false;
               }

               if (!this.doJump) {
                  this.skip = true;
               }

               if (Math.random() <= (double)(Float)this.failRate.getValue() && (Boolean)this.fail.getValue()) {
                  if (Math.random() <= (double)(Float)this.jumpRate.getValue()) {
                     this.doJump = true;
                     this.failJump = true;
                  } else {
                     this.doJump = false;
                     this.failJump = false;
                  }
               } else {
                  this.doJump = true;
                  this.failJump = false;
               }

               if (this.skip) {
                  this.skip = false;
                  return;
               }

               switch(((Velocity.jumpModeEn)this.jumpMode.getValue()).ordinal()) {
               case 0:
                  mc.field_1724.method_18800(mc.field_1724.method_18798().method_10216(), (double)(Float)this.motion.getValue(), mc.field_1724.method_18798().method_10215());
                  break;
               case 1:
                  mc.field_1724.method_6043();
                  break;
               case 2:
                  mc.field_1724.method_6043();
                  mc.field_1724.method_18800(mc.field_1724.method_18798().method_10216(), (double)(Float)this.motion.getValue(), mc.field_1724.method_18798().method_10215());
               }
            }
            break;
         case 7:
            if (this.flag) {
               if (this.ccCooldown <= 0) {
                  this.sendPacket(new class_2830(mc.field_1724.method_23317(), mc.field_1724.method_23318(), mc.field_1724.method_23321(), ((IClientPlayerEntity)mc.field_1724).getLastYaw(), ((IClientPlayerEntity)mc.field_1724).getLastPitch(), mc.field_1724.method_24828()));
                  this.sendPacket(new class_2846(class_2847.field_12973, class_2338.method_49638(mc.field_1724.method_19538()), class_2350.field_11033));
               }

               this.flag = false;
            }
         }

         if (this.grimTicks > 0) {
            --this.grimTicks;
         }

      }
   }

   private boolean isValidMotion(double motion, double min, double max) {
      return Math.abs(motion) > min && Math.abs(motion) < max;
   }

   public void onEnable() {
      this.grimTicks = 0;
   }

   public static enum modeEn {
      Matrix,
      Cancel,
      Sunrise,
      Custom,
      Redirect,
      OldGrim,
      Jump,
      GrimNew;

      // $FF: synthetic method
      private static Velocity.modeEn[] $values() {
         return new Velocity.modeEn[]{Matrix, Cancel, Sunrise, Custom, Redirect, OldGrim, Jump, GrimNew};
      }
   }

   public static enum jumpModeEn {
      Motion,
      Jump,
      Both;

      // $FF: synthetic method
      private static Velocity.jumpModeEn[] $values() {
         return new Velocity.jumpModeEn[]{Motion, Jump, Both};
      }
   }
}
