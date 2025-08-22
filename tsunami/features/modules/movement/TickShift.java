package tsunami.features.modules.movement;

import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_2596;
import net.minecraft.class_2708;
import net.minecraft.class_2828.class_2829;
import net.minecraft.class_2828.class_2830;
import net.minecraft.class_2828.class_2831;
import net.minecraft.class_2828.class_5911;
import tsunami.TsunamiClient;
import tsunami.events.impl.EventSync;
import tsunami.events.impl.PacketEvent;
import tsunami.features.modules.Module;
import tsunami.setting.Setting;
import tsunami.utility.player.MovementUtility;

public class TickShift extends Module {
   private final Setting<Float> timer = new Setting("Timer", 2.0F, 0.1F, 100.0F);
   private final Setting<Integer> packets = new Setting("Packets", 20, 0, 1000);
   private final Setting<Integer> lagTime = new Setting("LagTime", 1000, 0, 10000);
   private final Setting<Boolean> sneaking = new Setting("Sneaking", false);
   private final Setting<Boolean> cancelGround = new Setting("CancelGround", false);
   private final Setting<Boolean> cancelRotations = new Setting("CancelRotation", false);
   private static double prevPosX;
   private static double prevPosY;
   private static double prevPosZ;
   private tsunami.utility.Timer lagTimer = new tsunami.utility.Timer();
   private static float yaw;
   private static float pitch;
   private int ticks;

   public TickShift() {
      super("TickShift", Module.Category.NONE);
   }

   @EventHandler
   public void onSync(EventSync e) {
      if (notMoving()) {
         TsunamiClient.TICK_TIMER = 1.0F;
         this.ticks = this.ticks >= (Integer)this.packets.getValue() ? (Integer)this.packets.getValue() : this.ticks + 1;
      }

      prevPosX = mc.field_1724.method_23317();
      prevPosY = mc.field_1724.method_23318();
      prevPosZ = mc.field_1724.method_23321();
      yaw = mc.field_1724.method_36454();
      pitch = mc.field_1724.method_36455();
      if (mc.field_1724 != null && mc.field_1687 != null && this.lagTimer.passedMs((long)(Integer)this.lagTime.getValue())) {
         if (this.ticks <= 0 || !MovementUtility.isMoving() || !(Boolean)this.sneaking.getValue() && mc.field_1724.method_5715()) {
            TsunamiClient.TICK_TIMER = 1.0F;
         }
      } else {
         this.reset();
      }

   }

   @EventHandler
   public void onPacketReceive(PacketEvent.Receive e) {
      if (e.getPacket() instanceof class_2708) {
         this.lagTimer.reset();
      }

   }

   @EventHandler
   public void onPacketSend(PacketEvent.Send e) {
      if (e.getPacket() instanceof class_2830) {
         this.shift(e, true);
      }

      if (e.getPacket() instanceof class_2829) {
         this.shift(e, true);
      }

      class_2596 var3 = e.getPacket();
      if (var3 instanceof class_2831) {
         class_2831 pac = (class_2831)var3;
         if (!(Boolean)this.cancelRotations.getValue() || !(Boolean)this.cancelGround.getValue() && pac.method_12273() != mc.field_1724.method_24828()) {
            this.shift(e, false);
         } else {
            e.cancel();
         }
      }

      if (e.getPacket() instanceof class_5911) {
         if ((Boolean)this.cancelGround.getValue()) {
            e.cancel();
         } else {
            this.shift(e, false);
         }
      }

   }

   public String getDisplayInfo() {
      return this.ticks.makeConcatWithConstants<invokedynamic>(this.ticks);
   }

   public void onEnable() {
      this.reset();
   }

   public void onDisable() {
      this.reset();
   }

   private static boolean notMoving() {
      return prevPosX == mc.field_1724.method_23317() && prevPosY == mc.field_1724.method_23318() && prevPosZ == mc.field_1724.method_23321() && yaw == mc.field_1724.method_36454() && pitch == mc.field_1724.method_36455();
   }

   private void shift(PacketEvent.Send event, boolean moving) {
      if (!event.isCancelled()) {
         if (moving && MovementUtility.isMoving() && this.ticks > 0 && ((Boolean)this.sneaking.getValue() || !mc.field_1724.method_5715())) {
            TsunamiClient.TICK_TIMER = (Float)this.timer.getValue();
         }

         this.ticks = this.ticks <= 0 ? 0 : this.ticks - 1;
      }
   }

   public void reset() {
      TsunamiClient.TICK_TIMER = 1.0F;
      this.ticks = 0;
   }
}
