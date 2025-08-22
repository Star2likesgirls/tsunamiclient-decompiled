package tsunami.features.modules.movement;

import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_634;
import tsunami.events.impl.EventKeyboardInput;
import tsunami.features.modules.Module;
import tsunami.setting.Setting;

public class AutoWalk extends Module {
   private final Setting<AutoWalk.Mode> mode;

   public AutoWalk() {
      super("AutoWalk", Module.Category.MOVEMENT);
      this.mode = new Setting("Mode", AutoWalk.Mode.Simple);
   }

   public void onEnable() {
      if (this.mode.getValue() == AutoWalk.Mode.Baritone) {
         class_634 var10000 = mc.field_1724.field_3944;
         double var10001 = 3000000.0D * Math.cos(Math.toRadians((double)(mc.field_1724.method_36454() + 90.0F)));
         var10000.method_45729("#goto " + var10001 + " " + 3000000.0D * Math.sin(Math.toRadians((double)(mc.field_1724.method_36454() + 90.0F))));
      }

   }

   public void onDisable() {
      if (this.mode.getValue() == AutoWalk.Mode.Baritone) {
         mc.field_1724.field_3944.method_45729("#stop");
      }

   }

   @EventHandler
   public void onKey(EventKeyboardInput e) {
      if (this.mode.getValue() == AutoWalk.Mode.Simple) {
         mc.field_1724.field_3913.field_3905 = 1.0F;
      }

   }

   public static enum Mode {
      Simple,
      Baritone;

      // $FF: synthetic method
      private static AutoWalk.Mode[] $values() {
         return new AutoWalk.Mode[]{Simple, Baritone};
      }
   }
}
