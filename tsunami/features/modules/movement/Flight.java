package tsunami.features.modules.movement;

import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_124;
import net.minecraft.class_2596;
import net.minecraft.class_2708;
import net.minecraft.class_2743;
import net.minecraft.class_2846;
import net.minecraft.class_2828.class_2830;
import net.minecraft.class_2846.class_2847;
import tsunami.events.impl.EventSync;
import tsunami.events.impl.PacketEvent;
import tsunami.features.modules.Module;
import tsunami.features.modules.client.ClientSettings;
import tsunami.setting.Setting;
import tsunami.utility.player.MovementUtility;

public class Flight extends Module {
   private final Setting<Flight.Mode> mode;
   private final Setting<Float> hSpeed;
   private final Setting<Float> vSpeed;
   private final Setting<Float> boostValue;
   private final Setting<Boolean> autoToggle;
   private final Setting<Integer> boostTicks;
   private final Setting<Boolean> antiKick;
   private double prevX;
   private double prevY;
   private double prevZ;
   private double velocityMotion;
   public boolean onPosLook;
   private int flyTicks;

   public Flight() {
      super("Flight", Module.Category.MOVEMENT);
      this.mode = new Setting("Mode", Flight.Mode.Vanilla);
      this.hSpeed = new Setting("Horizontal", 1.0F, 0.0F, 10.0F, (v) -> {
         return !this.mode.is(Flight.Mode.StormBreak);
      });
      this.vSpeed = new Setting("Vertical", 0.78F, 0.0F, 5.0F, (v) -> {
         return !this.mode.is(Flight.Mode.StormBreak);
      });
      this.boostValue = new Setting("Boost", 1.0F, 0.1F, 1.0F, (v) -> {
         return this.mode.is(Flight.Mode.StormBreak);
      });
      this.autoToggle = new Setting("AutoToggle", false, (v) -> {
         return this.mode.is(Flight.Mode.MatrixJump);
      });
      this.boostTicks = new Setting("Ticks", 8, 0, 40, (v) -> {
         return this.mode.is(Flight.Mode.Damage);
      });
      this.antiKick = new Setting("AntiKick", false, (v) -> {
         return this.mode.is(Flight.Mode.Creative) || this.mode.is(Flight.Mode.Vanilla);
      });
      this.onPosLook = false;
      this.flyTicks = 0;
   }

   @EventHandler
   public void onEventSync(EventSync event) {
      double[] dir;
      switch(((Flight.Mode)this.mode.getValue()).ordinal()) {
      case 0:
         if (MovementUtility.isMoving()) {
            dir = MovementUtility.forward((double)(Float)this.hSpeed.getValue());
            mc.field_1724.method_18800(dir[0], 0.0D, dir[1]);
         } else {
            mc.field_1724.method_18800(0.0D, 0.0D, 0.0D);
         }

         if (mc.field_1690.field_1903.method_1434()) {
            mc.field_1724.method_18799(mc.field_1724.method_18798().method_1031(0.0D, (double)(Float)this.vSpeed.getValue(), 0.0D));
         }

         if (mc.field_1690.field_1832.method_1434()) {
            mc.field_1724.method_18799(mc.field_1724.method_18798().method_1031(0.0D, (double)(-(Float)this.vSpeed.getValue()), 0.0D));
         }
      case 1:
      default:
         break;
      case 2:
         if (MovementUtility.isMoving() && mc.field_1687.method_20812(mc.field_1724, mc.field_1724.method_5829().method_1009(0.5D, 0.0D, 0.5D).method_989(0.0D, -1.0D, 0.0D)).iterator().hasNext()) {
            mc.field_1724.method_24830(true);
            mc.field_1724.method_6043();
         }
         break;
      case 3:
         if (mc.field_1724.method_24828()) {
            mc.field_1724.method_6043();
            this.flyTicks = 5;
         } else if (this.flyTicks > 0) {
            if (MovementUtility.isMoving()) {
               dir = MovementUtility.forward((double)(Float)this.hSpeed.getValue());
               mc.field_1724.method_18800(dir[0], -0.04D, dir[1]);
            } else {
               mc.field_1724.method_18800(0.0D, -0.04D, 0.0D);
            }

            --this.flyTicks;
         }
         break;
      case 4:
         if (mc.field_1724.field_6012 % 60 == 0) {
            String var10001 = String.valueOf(class_124.field_1061);
            this.sendMessage(var10001 + (ClientSettings.isRu() ? "В этом режиме нужно ломать блоки!" : "In this mode you need to break blocks!"));
         }
      }

      if ((Boolean)this.antiKick.getValue() && (this.mode.is(Flight.Mode.Creative) || this.mode.is(Flight.Mode.Vanilla))) {
         mc.field_1724.method_18799(mc.field_1724.method_18798().method_1031(0.0D, -0.08D, 0.0D));
      }

   }

   public void onUpdate() {
      if (this.mode.is(Flight.Mode.Damage) && this.flyTicks-- > (Integer)this.boostTicks.getValue()) {
         mc.field_1724.method_18800(mc.field_1724.method_18798().field_1352, this.velocityMotion, mc.field_1724.method_18798().method_10215());
      }

      if (this.mode.is(Flight.Mode.MatrixJump)) {
         if (mc.field_1724.field_6017 == 0.0F) {
            return;
         }

         mc.field_1724.method_31549().field_7479 = false;
         mc.field_1724.method_18800(0.0D, 0.0D, 0.0D);
         if (mc.field_1690.field_1903.method_1434()) {
            mc.field_1724.method_18799(mc.field_1724.method_18798().method_1031(0.0D, (double)(Float)this.vSpeed.getValue(), 0.0D));
         }

         if (mc.field_1690.field_1832.method_1434()) {
            mc.field_1724.method_18799(mc.field_1724.method_18798().method_1031(0.0D, (double)(-(Float)this.vSpeed.getValue()), 0.0D));
         }

         double[] dir = MovementUtility.forward((double)(Float)this.hSpeed.getValue());
         mc.field_1724.method_18800(dir[0], mc.field_1724.method_18798().method_10214(), dir[1]);
      }

      if (this.mode.is(Flight.Mode.Creative)) {
         mc.field_1724.method_31549().field_7479 = true;
         mc.field_1724.method_31549().method_7248((Float)this.hSpeed.getValue() / 10.0F);
      }

   }

   @EventHandler
   public void onPacketReceive(PacketEvent.Receive e) {
      if (this.mode.is(Flight.Mode.MatrixJump)) {
         if (fullNullCheck()) {
            return;
         }

         if (e.getPacket() instanceof class_2708) {
            this.onPosLook = true;
            this.prevX = mc.field_1724.method_18798().method_10216();
            this.prevY = mc.field_1724.method_18798().method_10214();
            this.prevZ = mc.field_1724.method_18798().method_10215();
         }
      }

      if (this.mode.is(Flight.Mode.Damage)) {
         class_2596 var3 = e.getPacket();
         if (var3 instanceof class_2743) {
            class_2743 v = (class_2743)var3;
            if (v.method_11816() / 8000.0D > 0.2D) {
               this.velocityMotion = v.method_11816() / 8000.0D;
               this.flyTicks = (Integer)this.boostTicks.getValue();
            }
         }
      }

   }

   @EventHandler
   public void onPacketSend(PacketEvent.Send e) {
      if (this.mode.is(Flight.Mode.MatrixJump) && e.getPacket() instanceof class_2830 && this.onPosLook) {
         mc.field_1724.method_18800(this.prevX, this.prevY, this.prevZ);
         this.onPosLook = false;
         if ((Boolean)this.autoToggle.getValue()) {
            this.disable();
         }
      }

      if (this.mode.is(Flight.Mode.StormBreak)) {
         class_2596 var3 = e.getPacket();
         if (var3 instanceof class_2846) {
            class_2846 pac = (class_2846)var3;
            if (pac.method_12363() == class_2847.field_12973 || pac.method_12363() == class_2847.field_12968 && mc.field_1687.method_8320(pac.method_12362()).method_45474()) {
               double[] dir = MovementUtility.forward((double)(2.0F * (Float)this.boostValue.getValue()));
               mc.field_1724.method_18800(dir[0], (double)(3.0F * (Float)this.boostValue.getValue()), dir[1]);
            }
         }
      }

   }

   public void onDisable() {
      mc.field_1724.method_31549().field_7479 = false;
      mc.field_1724.method_31549().method_7248(0.05F);
   }

   private static enum Mode {
      Vanilla,
      MatrixJump,
      AirJump,
      MatrixGlide,
      StormBreak,
      Damage,
      Creative;

      // $FF: synthetic method
      private static Flight.Mode[] $values() {
         return new Flight.Mode[]{Vanilla, MatrixJump, AirJump, MatrixGlide, StormBreak, Damage, Creative};
      }
   }
}
