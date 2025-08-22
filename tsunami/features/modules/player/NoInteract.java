package tsunami.features.modules.player;

import tsunami.features.modules.Module;
import tsunami.setting.Setting;

public class NoInteract extends Module {
   public static Setting<Boolean> onlyAura = new Setting("OnlyAura", false);

   public NoInteract() {
      super("NoInteract", Module.Category.PLAYER);
   }
}
