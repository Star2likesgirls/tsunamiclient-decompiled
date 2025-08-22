package tsunami.features.modules.player;

import tsunami.features.modules.Module;
import tsunami.setting.Setting;

public final class NoEntityTrace extends Module {
   public static final Setting<Boolean> ponly = new Setting("Pickaxe Only", true);
   public static final Setting<Boolean> noSword = new Setting("No Sword", true);

   public NoEntityTrace() {
      super("NoEntityTrace", Module.Category.NONE);
   }
}
