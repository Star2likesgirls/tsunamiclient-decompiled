package tsunami.features.modules.movement;

import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_2596;
import net.minecraft.class_2708;
import net.minecraft.class_2743;
import net.minecraft.class_3532;
import net.minecraft.class_6373;
import tsunami.TsunamiClient;
import tsunami.events.impl.PacketEvent;
import tsunami.events.impl.PostPlayerUpdateEvent;
import tsunami.features.modules.Module;
import tsunami.features.modules.client.ClientSettings;
import tsunami.setting.Setting;
import tsunami.setting.impl.Bind;
import tsunami.utility.player.MovementUtility;

public class Timer extends Module {
   private final Setting<Timer.Mode> mode;
   private final Setting<Boolean> old;
   public final Setting<Float> speed;
   private final Setting<Integer> shiftTicks;
   private final Setting<Bind> boostKey;
   private final Setting<Timer.OnFlag> onFlag;
   public static float energy;
   public static float yaw;
   public static float pitch;
   private static double prevPosX;
   private static double prevPosY;
   private static double prevPosZ;
   private long cancelTime;

   public Timer() {
      super("Timer", Module.Category.NONE);
      this.mode = new Setting("Mode", Timer.Mode.Normal);
      this.old = new Setting("Old", false, (v) -> {
         return this.mode.is(Timer.Mode.Matrix);
      });
      this.speed = new Setting("Speed", 2.0F, 0.1F, 10.0F, (v) -> {
         return this.mode.getValue() != Timer.Mode.Shift;
      });
      this.shiftTicks = new Setting("ShiftTicks", 10, 1, 40, (v) -> {
         return this.mode.getValue() == Timer.Mode.Shift;
      });
      this.boostKey = new Setting("BoostKey", new Bind(-1, false, false), (v) -> {
         return this.mode.getValue() == Timer.Mode.Grim;
      });
      this.onFlag = new Setting("OnFlag", Timer.OnFlag.Reset);
   }

   public void onEnable() {
      TsunamiClient.TICK_TIMER = 1.0F;
      if (!this.mode.is(Timer.Mode.Matrix)) {
         energy = 0.0F;
      }

      if (this.mode.is(Timer.Mode.Grim)) {
         this.cancelTime = System.currentTimeMillis();
      }

   }

   public void onDisable() {
      TsunamiClient.TICK_TIMER = 1.0F;
   }

   public void onUpdate() {
      switch(((Timer.Mode)this.mode.getValue()).ordinal()) {
      case 0:
         TsunamiClient.TICK_TIMER = (Float)this.speed.getValue();
         break;
      case 1:
         if (!MovementUtility.isMoving()) {
            TsunamiClient.TICK_TIMER = 1.0F;
            return;
         }

         TsunamiClient.TICK_TIMER = Math.max((Float)this.speed.getValue(), 1.0F);
         if (energy > 0.0F) {
            energy = class_3532.method_15363(energy - (0.1F * (Float)this.speed.getValue() - 0.1F), 0.0F, 1.0F);
         } else {
            this.disable(ClientSettings.isRu() ? "Заряд таймера кончился! Отключаю.." : "Timer's out of charge! Disabling..");
         }
      case 2:
      default:
         break;
      case 3:
         if (energy <= 0.0F || !this.isKeyPressed(this.boostKey) || TsunamiClient.core.getSetBackTime() < 2000L) {
            TsunamiClient.TICK_TIMER = 1.0F;
            return;
         }

         TsunamiClient.TICK_TIMER = Math.max((Float)this.speed.getValue(), 1.0F);
         energy = class_3532.method_15363(energy - (0.0025F * (Float)this.speed.getValue() - 0.0025F), 0.0F, 1.0F);
      }

   }

   @EventHandler
   public void onPacketReceive(PacketEvent.Receive e) {
      if (this.mode.is(Timer.Mode.Grim) && e.getPacket() instanceof class_6373 && TsunamiClient.core.getSetBackTime() > 2000L) {
         if (System.currentTimeMillis() - this.cancelTime > 25000L) {
            this.cancelTime = System.currentTimeMillis();
            energy = 0.0F;
            return;
         }

         if (!MovementUtility.isMoving()) {
            energy = Math.clamp(energy + 0.005F, 0.0F, 1.0F);
         }

         e.cancel();
      }

      if (e.getPacket() instanceof class_2708) {
         switch(((Timer.OnFlag)this.onFlag.getValue()).ordinal()) {
         case 0:
            energy = 0.0F;
            this.disable(ClientSettings.isRu() ? "Отключён т.к. тебя флагнуло!" : "Disabled because you got flagged!");
            break;
         case 2:
            TsunamiClient.TICK_TIMER = 1.0F;
            energy = 0.0F;
         }
      }

      class_2596 var3 = e.getPacket();
      if (var3 instanceof class_2743) {
         class_2743 velo = (class_2743)var3;
         if (velo.method_11818() == mc.field_1724.method_5628() && this.mode.is(Timer.Mode.Grim)) {
            TsunamiClient.TICK_TIMER = 1.0F;
            energy = 0.0F;
         }
      }

   }

   @EventHandler
   public void onPostPlayerUpdate(PostPlayerUpdateEvent event) {
      if (this.mode.getValue() == Timer.Mode.Shift) {
         if (energy < 0.9F) {
            this.disable(ClientSettings.isRu() ? "Перед повторным использованием необходимо постоять на месте!" : "Standing still is required before reuse!");
            return;
         }

         event.cancel();
         event.setIterations((Integer)this.shiftTicks.getValue());
         this.disable(ClientSettings.isRu() ? "Тики пропущены! Отключаю.." : "Ticks shifted! Disabling..");
      }

   }

   public void onEntitySync() {
      if (this.mode.is(Timer.Mode.Matrix)) {
         energy = Math.clamp(notMoving() ? energy + 0.025F : energy - ((Boolean)this.old.getValue() ? 0.005F : 0.0F), 0.0F, 1.0F);
      }

      prevPosX = mc.field_1724.method_23317();
      prevPosY = mc.field_1724.method_23318();
      prevPosZ = mc.field_1724.method_23321();
      yaw = mc.field_1724.method_36454();
      pitch = mc.field_1724.method_36455();
   }

   private static boolean notMoving() {
      return prevPosX == mc.field_1724.method_23317() && prevPosY == mc.field_1724.method_23318() && prevPosZ == mc.field_1724.method_23321() && yaw == mc.field_1724.method_36454() && pitch == mc.field_1724.method_36455();
   }

   public static enum Mode {
      Normal,
      Matrix,
      Shift,
      Grim;

      // $FF: synthetic method
      private static Timer.Mode[] $values() {
         return new Timer.Mode[]{Normal, Matrix, Shift, Grim};
      }
   }

   public static enum OnFlag {
      Disable,
      None,
      Reset;

      // $FF: synthetic method
      private static Timer.OnFlag[] $values() {
         return new Timer.OnFlag[]{Disable, None, Reset};
      }
   }
}
