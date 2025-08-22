package tsunami.features.modules.movement;

import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_2246;
import net.minecraft.class_2560;
import tsunami.TsunamiClient;
import tsunami.core.Managers;
import tsunami.events.impl.EventCollision;
import tsunami.events.impl.PlayerUpdateEvent;
import tsunami.features.modules.Module;
import tsunami.setting.Setting;
import tsunami.utility.player.MovementUtility;

public class AntiWeb extends Module {
   public static final Setting<AntiWeb.Mode> mode;
   public static final Setting<Boolean> grim;
   public static final Setting<Float> timer;
   public Setting<Float> speed = new Setting("Speed", 0.3F, 0.0F, 10.0F, (v) -> {
      return mode.getValue() == AntiWeb.Mode.Fly;
   });
   private boolean timerEnabled = false;

   public AntiWeb() {
      super("AntiWeb", Module.Category.NONE);
   }

   @EventHandler
   public void onPlayerUpdate(PlayerUpdateEvent e) {
      if (Managers.PLAYER.isInWeb()) {
         if (mode.getValue() == AntiWeb.Mode.Timer) {
            if (mc.field_1724.method_24828()) {
               TsunamiClient.TICK_TIMER = 1.0F;
            } else {
               TsunamiClient.TICK_TIMER = (Float)timer.getValue();
               this.timerEnabled = true;
            }
         }

         if (mode.getValue() == AntiWeb.Mode.Fly) {
            double[] dir = MovementUtility.forward((double)(Float)this.speed.getValue());
            mc.field_1724.method_18800(dir[0], 0.0D, dir[1]);
            if (mc.field_1690.field_1903.method_1434()) {
               mc.field_1724.method_18799(mc.field_1724.method_18798().method_1031(0.0D, (double)(Float)this.speed.getValue(), 0.0D));
            }

            if (mc.field_1690.field_1832.method_1434()) {
               mc.field_1724.method_18799(mc.field_1724.method_18798().method_1031(0.0D, (double)(-(Float)this.speed.getValue()), 0.0D));
            }
         }
      } else if (this.timerEnabled) {
         this.timerEnabled = false;
         TsunamiClient.TICK_TIMER = 1.0F;
      }

   }

   @EventHandler
   public void onCollide(EventCollision e) {
      if (e.getState().method_26204() instanceof class_2560 && mode.getValue() == AntiWeb.Mode.Solid) {
         e.setState(class_2246.field_10566.method_9564());
      }

   }

   static {
      mode = new Setting("Mode", AntiWeb.Mode.Solid);
      grim = new Setting("Grim", false, (v) -> {
         return mode.is(AntiWeb.Mode.Ignore);
      });
      timer = new Setting("Timer", 20.0F, 1.0F, 50.0F, (v) -> {
         return mode.getValue() == AntiWeb.Mode.Timer;
      });
   }

   public static enum Mode {
      Timer,
      Solid,
      Ignore,
      Fly;

      // $FF: synthetic method
      private static AntiWeb.Mode[] $values() {
         return new AntiWeb.Mode[]{Timer, Solid, Ignore, Fly};
      }
   }
}
