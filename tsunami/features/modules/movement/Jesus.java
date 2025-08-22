package tsunami.features.modules.movement;

import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_2246;
import net.minecraft.class_2404;
import tsunami.events.impl.EventCollision;
import tsunami.features.modules.Module;
import tsunami.setting.Setting;

public class Jesus extends Module {
   public final Setting<Jesus.Mode> mode;

   public Jesus() {
      super("Jesus", Module.Category.NONE);
      this.mode = new Setting("Mode", Jesus.Mode.SOLID);
   }

   @EventHandler
   public void onCollide(EventCollision e) {
      if (e.getState().method_26204() instanceof class_2404) {
         e.setState(this.mode.is(Jesus.Mode.SOLID) ? class_2246.field_10443.method_9564() : class_2246.field_10540.method_9564());
      }

   }

   public static enum Mode {
      SOLID,
      SOLID2;

      // $FF: synthetic method
      private static Jesus.Mode[] $values() {
         return new Jesus.Mode[]{SOLID, SOLID2};
      }
   }
}
