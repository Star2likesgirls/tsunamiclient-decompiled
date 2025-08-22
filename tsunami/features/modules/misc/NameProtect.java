package tsunami.features.modules.misc;

import tsunami.core.manager.client.ModuleManager;
import tsunami.features.modules.Module;
import tsunami.setting.Setting;

public class NameProtect extends Module {
   public static Setting<String> newName = new Setting("name", "TsunamiClient");
   public static Setting<Boolean> hideFriends = new Setting("Hide friends", true);

   public NameProtect() {
      super("NameProtect", Module.Category.MISC);
   }

   public static String getCustomName() {
      return ModuleManager.nameProtect.isEnabled() ? ((String)newName.getValue()).replaceAll("&", "§") : mc.method_53462().getName();
   }
}
