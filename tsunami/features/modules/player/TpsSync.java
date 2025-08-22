package tsunami.features.modules.player;

import meteordevelopment.orbit.EventHandler;
import tsunami.TsunamiClient;
import tsunami.core.Managers;
import tsunami.core.manager.client.ModuleManager;
import tsunami.events.impl.EventTick;
import tsunami.features.modules.Module;

public class TpsSync extends Module {
   public TpsSync() {
      super("TpsSync", Module.Category.NONE);
   }

   @EventHandler(
      priority = 100
   )
   public void onTick(EventTick e) {
      if (!ModuleManager.timer.isEnabled()) {
         if (Managers.SERVER.getTPS() > 1.0F) {
            TsunamiClient.TICK_TIMER = Managers.SERVER.getTPS() / 20.0F;
         } else {
            TsunamiClient.TICK_TIMER = 1.0F;
         }

      }
   }

   public void onDisable() {
      TsunamiClient.TICK_TIMER = 1.0F;
   }
}
