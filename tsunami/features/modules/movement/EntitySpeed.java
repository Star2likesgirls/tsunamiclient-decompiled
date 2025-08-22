package tsunami.features.modules.movement;

import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_1297;
import net.minecraft.class_2708;
import org.jetbrains.annotations.NotNull;
import tsunami.TsunamiClient;
import tsunami.events.impl.EventPlayerTravel;
import tsunami.events.impl.PacketEvent;
import tsunami.features.modules.Module;
import tsunami.setting.Setting;
import tsunami.utility.player.MovementUtility;

public class EntitySpeed extends Module {
   private final Setting<Boolean> accelerate = new Setting("Accelerate", true);
   private final Setting<Float> accelerateFactor = new Setting("AccelerateFactor", 9.0F, 0.0F, 20.0F, (v) -> {
      return (Boolean)this.accelerate.getValue();
   });
   private final Setting<Boolean> stopunloaded = new Setting("StopUnloaded", true);
   private final Setting<Float> speed = new Setting("Speed", 0.77F, 0.1F, 5.0F);
   private final Setting<Float> timer = new Setting("Timer", 1.0F, 0.1F, 5.0F);
   private final Setting<Float> jitter = new Setting("Jitter", 0.05F, 0.0F, 0.5F);
   private int ticks;
   private float acceleration;

   public EntitySpeed() {
      super("EntitySpeed", Module.Category.NONE);
   }

   public void onEnable() {
      this.ticks = 0;
      this.acceleration = 0.0F;
   }

   public void onDisable() {
      TsunamiClient.TICK_TIMER = 1.0F;
   }

   @EventHandler
   public void onPlayerTravel(@NotNull EventPlayerTravel ev) {
      if (ev.isPre()) {
         if (!fullNullCheck()) {
            class_1297 entity = mc.field_1724.method_49694();
            if (entity != null) {
               if (mc.field_1687.method_8393((int)entity.method_19538().method_10216() >> 4, (int)entity.method_19538().method_10215() >> 4) && !(entity.method_19538().method_10214() < -60.0D) || !(Boolean)this.stopunloaded.getValue()) {
                  if (entity.field_5976 || mc.field_1724.field_5976) {
                     this.acceleration = 0.0F;
                  }

                  if ((Float)this.timer.getValue() != 1.0F) {
                     TsunamiClient.TICK_TIMER = (Float)this.timer.getValue();
                  }

                  double[] motion = MovementUtility.forward((double)this.getSpeed());
                  double predictedX = entity.method_23317() + motion[0];
                  double predictedZ = entity.method_23321() + motion[1];
                  if (mc.field_1687.method_8393((int)predictedX >> 4, (int)predictedZ >> 4) && !(entity.method_19538().method_10214() < -60.0D) || !(Boolean)this.stopunloaded.getValue()) {
                     if (MovementUtility.isMoving()) {
                        entity.method_18800(motion[0], entity.method_18798().method_10214(), motion[1]);
                     } else {
                        entity.method_18800(0.0D, entity.method_18798().method_10214(), 0.0D);
                     }

                     if (this.ticks++ > 50) {
                        this.ticks = 0;
                     }

                     ev.cancel();
                  }
               }
            }
         }
      }
   }

   @EventHandler
   public void onPacketReceive(PacketEvent.Receive e) {
      if (e.getPacket() instanceof class_2708) {
         this.acceleration = 0.0F;
      }

   }

   private float getSpeed() {
      float baseSpeed = Math.min((this.acceleration += (20.0F - (Float)this.accelerateFactor.getValue()) / (Float)this.speed.getValue()) / 100.0F, (Float)this.speed.getValue());
      if (!(Boolean)this.accelerate.getValue()) {
         baseSpeed = (Float)this.speed.getValue();
      }

      baseSpeed += this.ticks > 25 ? (Float)this.jitter.getValue() : 0.0F;
      return baseSpeed;
   }
}
