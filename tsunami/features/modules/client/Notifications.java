package tsunami.features.modules.client;

import tsunami.features.modules.Module;
import tsunami.setting.Setting;

public final class Notifications extends Module {
   public final Setting<Notifications.Mode> mode;

   public Notifications() {
      super("Notifications", Module.Category.CLIENT);
      this.mode = new Setting("Mode", Notifications.Mode.CrossHair);
   }

   public static enum Mode {
      Default,
      CrossHair,
      Text;

      // $FF: synthetic method
      private static Notifications.Mode[] $values() {
         return new Notifications.Mode[]{Default, CrossHair, Text};
      }
   }
}
