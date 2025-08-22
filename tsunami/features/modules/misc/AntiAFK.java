package tsunami.features.modules.misc;

import java.util.concurrent.ThreadLocalRandom;
import meteordevelopment.orbit.EventHandler;
import tsunami.core.Managers;
import tsunami.events.impl.EventKeyboardInput;
import tsunami.events.impl.EventSetting;
import tsunami.events.impl.PlayerUpdateEvent;
import tsunami.features.modules.Module;
import tsunami.setting.Setting;
import tsunami.utility.Timer;
import tsunami.utility.player.MovementUtility;

public class AntiAFK extends Module {
   private final Setting<AntiAFK.Mode> mode;
   private final Setting<Boolean> onlyWhenAfk;
   private final Setting<Boolean> command;
   private final Setting<Boolean> move;
   private final Setting<Boolean> spin;
   private final Setting<Float> rotateSpeed;
   private final Setting<Boolean> jump;
   private final Setting<Boolean> swing;
   private final Setting<Boolean> alwayssneak;
   private final Setting<Integer> radius;
   private int step;
   private Timer inactiveTime;

   public AntiAFK() {
      super("AntiAFK", Module.Category.MISC);
      this.mode = new Setting("Mode", AntiAFK.Mode.Simple);
      this.onlyWhenAfk = new Setting("OnlyWhenAFK", false);
      this.command = new Setting("Command", false, (v) -> {
         return this.mode.getValue() == AntiAFK.Mode.Simple;
      });
      this.move = new Setting("Move", false, (v) -> {
         return this.mode.getValue() == AntiAFK.Mode.Simple;
      });
      this.spin = new Setting("Spin", false, (v) -> {
         return this.mode.getValue() == AntiAFK.Mode.Simple;
      });
      this.rotateSpeed = new Setting("RotateSpeed", 5.0F, 1.0F, 7.0F, (v) -> {
         return this.mode.getValue() == AntiAFK.Mode.Simple;
      });
      this.jump = new Setting("Jump", false, (v) -> {
         return this.mode.getValue() == AntiAFK.Mode.Simple;
      });
      this.swing = new Setting("Swing", false, (v) -> {
         return this.mode.getValue() == AntiAFK.Mode.Simple;
      });
      this.alwayssneak = new Setting("AlwaysSneak", false, (v) -> {
         return this.mode.getValue() == AntiAFK.Mode.Simple;
      });
      this.radius = new Setting("Radius", 64, 1, 128, (v) -> {
         return this.mode.getValue() == AntiAFK.Mode.Baritone;
      });
      this.inactiveTime = new Timer();
   }

   public void onEnable() {
      if ((Boolean)this.alwayssneak.getValue()) {
         mc.field_1690.field_1832.method_23481(true);
      }

      this.step = 0;
   }

   @EventHandler
   public void onSettingChange(EventSetting e) {
      if (e.getSetting() == this.mode) {
         this.step = 0;
      }

   }

   @EventHandler
   public void onKeyboardInput(EventKeyboardInput e) {
      if (mc.field_1724 != null && this.mode.is(AntiAFK.Mode.Simple) && !MovementUtility.isMoving() && (Boolean)this.move.getValue() && this.isAfk()) {
         float angleToRad = (float)Math.toRadians((double)(9 * (mc.field_1724.field_6012 % 40)));
         float sin = (float)Math.clamp(Math.sin((double)angleToRad), -1.0D, 1.0D);
         float cos = (float)Math.clamp(Math.cos((double)angleToRad), -1.0D, 1.0D);
         mc.field_1724.field_3913.field_3905 = (float)Math.round(sin);
         mc.field_1724.field_3913.field_3907 = (float)Math.round(cos);
      }

   }

   @EventHandler(
      priority = -100
   )
   public void onUpdate(PlayerUpdateEvent e) {
      label62: {
         if (this.mode.is(AntiAFK.Mode.Simple)) {
            if (!this.isActive()) {
               break label62;
            }
         } else if (!((double)Managers.PLAYER.currentPlayerSpeed > 0.07D)) {
            break label62;
         }

         this.inactiveTime.reset();
      }

      if (this.mode.getValue() == AntiAFK.Mode.Simple) {
         if (!this.isAfk()) {
            return;
         }

         if ((Boolean)this.move.getValue()) {
            mc.field_1724.method_5728(false);
         }

         if ((Boolean)this.spin.getValue()) {
            double gcdFix = Math.pow((Double)mc.field_1690.method_42495().method_41753() * 0.6D + 0.2D, 3.0D) * 1.2D;
            float newYaw = mc.field_1724.method_36454() + (Float)this.rotateSpeed.getValue();
            mc.field_1724.method_36456((float)((double)newYaw - (double)(newYaw - mc.field_1724.method_36454()) % gcdFix));
         }

         if ((Boolean)this.jump.getValue() && mc.field_1724.method_24828()) {
            mc.field_1724.method_6043();
         }

         if ((Boolean)this.swing.getValue() && ThreadLocalRandom.current().nextInt(99) == 0) {
            mc.field_1724.method_6104(mc.field_1724.method_6058());
         }

         if ((Boolean)this.command.getValue() && ThreadLocalRandom.current().nextInt(99) == 0) {
            mc.field_1724.field_3944.method_45730("qwerty");
         }
      } else if (this.inactiveTime.every(5000L)) {
         if (this.step > 3) {
            this.step = 0;
         }

         switch(this.step) {
         case 0:
            mc.field_1724.field_3944.method_45729("#goto ~ ~" + String.valueOf(this.radius.getValue()));
            break;
         case 1:
            mc.field_1724.field_3944.method_45729("#goto ~" + String.valueOf(this.radius.getValue()) + " ~");
            break;
         case 2:
            mc.field_1724.field_3944.method_45729("#goto ~ ~-" + String.valueOf(this.radius.getValue()));
            break;
         case 3:
            mc.field_1724.field_3944.method_45729("#goto ~-" + String.valueOf(this.radius.getValue()) + " ~");
         }

         ++this.step;
      }

   }

   public void onDisable() {
      if ((Boolean)this.alwayssneak.getValue()) {
         mc.field_1690.field_1832.method_23481(false);
      }

      if (this.mode.getValue() == AntiAFK.Mode.Baritone) {
         mc.field_1724.field_3944.method_45729("#stop");
      }

   }

   private boolean isAfk() {
      return !(Boolean)this.onlyWhenAfk.getValue() || this.inactiveTime.passedS(10.0D);
   }

   private boolean isActive() {
      return mc.field_1690.field_1894.method_1434() || mc.field_1690.field_1913.method_1434() || mc.field_1690.field_1849.method_1434() || mc.field_1690.field_1881.method_1434();
   }

   private static enum Mode {
      Simple,
      Baritone;

      // $FF: synthetic method
      private static AntiAFK.Mode[] $values() {
         return new AntiAFK.Mode[]{Simple, Baritone};
      }
   }
}
