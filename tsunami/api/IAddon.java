package tsunami.api;

import com.mojang.logging.LogUtils;
import java.util.List;
import tsunami.features.cmd.Command;
import tsunami.features.hud.HudElement;
import tsunami.features.modules.Module;

public interface IAddon {
   void onInitialize();

   List<Module> getModules();

   List<Command> getCommands();

   List<HudElement> getHudElements();

   String getPackage();

   String getName();

   String getAuthor();

   String getRepo();

   String getVersion();

   default String getDescription() {
      return "";
   }

   default void onShutdown() {
      LogUtils.getLogger().info("Shutting down addon: " + this.getName());
   }
}
