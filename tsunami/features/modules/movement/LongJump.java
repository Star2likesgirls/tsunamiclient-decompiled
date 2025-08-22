package tsunami.features.modules.movement;

import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_1294;
import net.minecraft.class_243;
import net.minecraft.class_2708;
import tsunami.TsunamiClient;
import tsunami.events.impl.EventMove;
import tsunami.events.impl.EventSync;
import tsunami.events.impl.PacketEvent;
import tsunami.features.modules.Module;
import tsunami.features.modules.client.ClientSettings;
import tsunami.setting.Setting;
import tsunami.utility.player.MovementUtility;

public class LongJump extends Module {
   private final Setting<Boolean> useTimer = new Setting("Timer", false);
   private final Setting<Boolean> jumpDisable = new Setting("JumpDisable", true);
   private final Setting<Float> timerValue = new Setting("TimerSpeed", 1.0F, 0.5F, 3.0F, (v) -> {
      return (Boolean)this.useTimer.getValue();
   });
   private final Setting<Float> speed = new Setting("Speed", 1.35F, 0.1F, 10.0F);
   private final Setting<Float> maxDistance = new Setting("MaxDistance", 10.0F, 5.0F, 40.0F);
   private float plannedSpeed;
   private float realSpeed;
   private int stage = 0;
   private class_243 prevPosition;

   public LongJump() {
      super("LongJump", Module.Category.NONE);
   }

   @EventHandler
   public void onMove(EventMove e) {
      if (this.prevPosition != null && mc.field_1724.method_19538().method_1025(this.prevPosition) > (double)this.maxDistance.getPow2Value()) {
         this.disable(ClientSettings.isRu() ? "Прыжок выполнен! Отключаю.." : "Jump complete! Disabling..");
      }

      if (MovementUtility.isMoving()) {
         if ((Boolean)this.useTimer.getValue()) {
            TsunamiClient.TICK_TIMER = (Float)this.timerValue.getValue();
         }

         switch(this.stage) {
         case 0:
            this.plannedSpeed = (float)((double)(Float)this.speed.getValue() * MovementUtility.getBaseMoveSpeed());
            this.realSpeed = 0.0F;
            ++this.stage;
            break;
         case 1:
            mc.field_1724.method_18800(mc.field_1724.method_18798().method_10216(), 0.42D + (double)this.isJumpBoost(), mc.field_1724.method_18798().method_10215());
            e.setY(0.42D + (double)this.isJumpBoost());
            this.plannedSpeed *= 2.149F;
            ++this.stage;
            break;
         case 2:
            double d = 0.6600000262260437D * ((double)this.realSpeed - MovementUtility.getBaseMoveSpeed());
            this.plannedSpeed = (float)((double)this.realSpeed - d);
            ++this.stage;
            break;
         case 3:
            if (mc.field_1724.field_5992 || mc.field_1687.method_20812(mc.field_1724, mc.field_1724.method_5829().method_1009(-0.2D, 0.0D, -0.2D).method_989(0.0D, mc.field_1724.method_18798().method_10214(), 0.0D)).iterator().hasNext()) {
               if ((Boolean)this.jumpDisable.getValue()) {
                  this.disable(ClientSettings.isRu() ? "Прыжок выполнен! Отключаю.." : "Jump complete! Disabling..");
               }

               this.stage = 0;
               this.realSpeed = 0.0F;
            }

            this.plannedSpeed = this.realSpeed - this.realSpeed / 159.0F;
         }
      }

      this.plannedSpeed = (float)Math.max(MovementUtility.getBaseMoveSpeed(), (double)this.plannedSpeed);
      MovementUtility.modifyEventSpeed(e, (double)this.plannedSpeed);
      e.cancel();
   }

   @EventHandler
   public void onPacketReceive(PacketEvent.Receive e) {
      if (e.getPacket() instanceof class_2708) {
         this.disable(ClientSettings.isRu() ? "Тебя флагнуло! Отключаю.." : "You've been flagged! Disabling..");
      }

   }

   public void onDisable() {
      this.resetValues();
   }

   public void onEnable() {
      this.resetValues();
   }

   public void resetValues() {
      this.prevPosition = mc.field_1724.method_19538();
      TsunamiClient.TICK_TIMER = 1.0F;
      this.plannedSpeed = 0.0F;
      this.realSpeed = 0.0F;
      this.stage = 0;
   }

   public float isJumpBoost() {
      return mc.field_1724.method_6059(class_1294.field_5913) ? 0.2F : 0.0F;
   }

   @EventHandler
   public void onEntitySync(EventSync eventSync) {
      if (MovementUtility.isMoving()) {
         this.realSpeed = (float)Math.hypot(mc.field_1724.method_23317() - mc.field_1724.field_6014, mc.field_1724.method_23321() - mc.field_1724.field_5969);
      } else {
         this.resetValues();
      }

   }
}
