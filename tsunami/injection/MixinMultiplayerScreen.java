package tsunami.injection;

import net.minecraft.class_2561;
import net.minecraft.class_4185;
import net.minecraft.class_437;
import net.minecraft.class_500;
import net.minecraft.class_4185.class_7840;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tsunami.core.manager.IManager;
import tsunami.core.manager.client.ModuleManager;
import tsunami.gui.windows.WindowBase;
import tsunami.gui.windows.WindowsScreen;
import tsunami.gui.windows.impl.ConfigWindow;
import tsunami.gui.windows.impl.FriendsWindow;
import tsunami.gui.windows.impl.MacroWindow;
import tsunami.gui.windows.impl.ProxyWindow;
import tsunami.gui.windows.impl.WaypointWindow;
import tsunami.setting.impl.PositionSetting;

@Mixin({class_500.class})
public abstract class MixinMultiplayerScreen extends class_437 {
   public MixinMultiplayerScreen(class_2561 title) {
      super(title);
   }

   @Inject(
      method = {"init"},
      at = {@At("RETURN")}
   )
   private void initHook(CallbackInfo ci) {
      class_7840 builder = class_4185.method_46430(class_2561.method_43470("⚡"), (button) -> {
         IManager.mc.method_1507(new WindowsScreen(new WindowBase[]{MacroWindow.get(((PositionSetting)ModuleManager.windows.macroPos.getValue()).getX() * (float)IManager.mc.method_22683().method_4486(), ((PositionSetting)ModuleManager.windows.macroPos.getValue()).getY() * (float)IManager.mc.method_22683().method_4502(), ModuleManager.windows.macroPos), ConfigWindow.get(((PositionSetting)ModuleManager.windows.configPos.getValue()).getX() * (float)IManager.mc.method_22683().method_4486(), ((PositionSetting)ModuleManager.windows.configPos.getValue()).getY() * (float)IManager.mc.method_22683().method_4502(), ModuleManager.windows.configPos), FriendsWindow.get(((PositionSetting)ModuleManager.windows.friendPos.getValue()).getX() * (float)IManager.mc.method_22683().method_4486(), ((PositionSetting)ModuleManager.windows.friendPos.getValue()).getY() * (float)IManager.mc.method_22683().method_4502(), ModuleManager.windows.friendPos), WaypointWindow.get(((PositionSetting)ModuleManager.windows.waypointPos.getValue()).getX() * (float)IManager.mc.method_22683().method_4486(), ((PositionSetting)ModuleManager.windows.waypointPos.getValue()).getY() * (float)IManager.mc.method_22683().method_4502(), ModuleManager.windows.waypointPos), ProxyWindow.get(((PositionSetting)ModuleManager.windows.proxyPos.getValue()).getX() * (float)IManager.mc.method_22683().method_4486(), ((PositionSetting)ModuleManager.windows.proxyPos.getValue()).getY() * (float)IManager.mc.method_22683().method_4502(), ModuleManager.windows.proxyPos)}));
      }).method_46437(60, 20);
      if (!ModuleManager.unHook.isEnabled()) {
         this.method_37063(builder.method_46433(this.field_22789 - 65, this.field_22790 - 25).method_46431());
      }

   }
}
