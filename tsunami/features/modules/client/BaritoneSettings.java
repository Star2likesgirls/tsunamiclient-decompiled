package tsunami.features.modules.client;

import baritone.api.BaritoneAPI;
import meteordevelopment.orbit.EventHandler;
import tsunami.TsunamiClient;
import tsunami.events.impl.EventSetting;
import tsunami.features.modules.Module;
import tsunami.setting.Setting;

public final class BaritoneSettings extends Module {
   public final Setting<Boolean> allowBreakBlock = new Setting("AllowBreakBlock", true);
   public final Setting<Boolean> allowPlace = new Setting("AllowPlace", true);
   public final Setting<Boolean> allowSprint = new Setting("AllowSprint", true);
   public final Setting<Boolean> debug = new Setting("Debug", false);
   public final Setting<Boolean> enterPortal = new Setting("EnterPortal", false);
   public final Setting<Boolean> desktopNotifications = new Setting("DesktopNotifications", false);

   public BaritoneSettings() {
      super("BaritoneSettings", Module.Category.CLIENT);
   }

   @EventHandler
   public void onSettingChange(EventSetting e) {
      if (!TsunamiClient.baritone) {
         this.sendMessage(ClientSettings.isRu() ? "Баритон не найден (можешь скачать на https://meteorclient.com)" : "Baritone not found (you can download it at https://meteorclient.com)");
      } else {
         BaritoneAPI.getSettings().allowBreak.value = this.allowBreakBlock.getValue();
         BaritoneAPI.getSettings().allowPlace.value = this.allowPlace.getValue();
         BaritoneAPI.getSettings().allowSprint.value = this.allowSprint.getValue();
         BaritoneAPI.getSettings().chatDebug.value = this.debug.getValue();
         BaritoneAPI.getSettings().enterPortal.value = this.enterPortal.getValue();
         BaritoneAPI.getSettings().desktopNotifications.value = this.desktopNotifications.getValue();
      }
   }

   public boolean isToggleable() {
      return false;
   }
}
