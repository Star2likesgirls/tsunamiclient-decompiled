package tsunami.features.modules.client;

import tsunami.features.modules.Module;
import tsunami.gui.windows.WindowBase;
import tsunami.gui.windows.WindowsScreen;
import tsunami.gui.windows.impl.ConfigWindow;
import tsunami.gui.windows.impl.FriendsWindow;
import tsunami.gui.windows.impl.MacroWindow;
import tsunami.gui.windows.impl.ProxyWindow;
import tsunami.gui.windows.impl.WaypointWindow;
import tsunami.setting.Setting;
import tsunami.setting.impl.PositionSetting;

public class Windows extends Module {
   public final Setting<PositionSetting> macroPos = new Setting("macroPos", new PositionSetting(0.3F, 0.3F), (v) -> {
      return false;
   });
   public final Setting<PositionSetting> configPos = new Setting("configPos", new PositionSetting(0.35F, 0.35F), (v) -> {
      return false;
   });
   public final Setting<PositionSetting> friendPos = new Setting("friendPos", new PositionSetting(0.4F, 0.4F), (v) -> {
      return false;
   });
   public final Setting<PositionSetting> waypointPos = new Setting("waypointPos", new PositionSetting(0.45F, 0.45F), (v) -> {
      return false;
   });
   public final Setting<PositionSetting> proxyPos = new Setting("proxyPos", new PositionSetting(0.5F, 0.5F), (v) -> {
      return false;
   });

   public Windows() {
      super("Windows", Module.Category.NONE);
   }

   public void onEnable() {
      mc.method_1507(new WindowsScreen(new WindowBase[]{MacroWindow.get(((PositionSetting)this.macroPos.getValue()).getX() * (float)mc.method_22683().method_4486(), ((PositionSetting)this.macroPos.getValue()).getY() * (float)mc.method_22683().method_4502(), this.macroPos), ConfigWindow.get(((PositionSetting)this.configPos.getValue()).getX() * (float)mc.method_22683().method_4486(), ((PositionSetting)this.configPos.getValue()).getY() * (float)mc.method_22683().method_4502(), this.configPos), FriendsWindow.get(((PositionSetting)this.friendPos.getValue()).getX() * (float)mc.method_22683().method_4486(), ((PositionSetting)this.friendPos.getValue()).getY() * (float)mc.method_22683().method_4502(), this.friendPos), WaypointWindow.get(((PositionSetting)this.waypointPos.getValue()).getX() * (float)mc.method_22683().method_4486(), ((PositionSetting)this.waypointPos.getValue()).getY() * (float)mc.method_22683().method_4502(), this.waypointPos), ProxyWindow.get(((PositionSetting)this.proxyPos.getValue()).getX() * (float)mc.method_22683().method_4486(), ((PositionSetting)this.proxyPos.getValue()).getY() * (float)mc.method_22683().method_4502(), this.proxyPos)}));
      this.disable();
   }
}
