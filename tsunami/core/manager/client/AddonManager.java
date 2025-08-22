package tsunami.core.manager.client;

import com.mojang.logging.LogUtils;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.entrypoint.EntrypointContainer;
import tsunami.TsunamiClient;
import tsunami.api.IAddon;
import tsunami.core.Managers;
import tsunami.core.manager.IManager;

public class AddonManager implements IManager {
   private int totalAddons = 0;
   private final List<IAddon> addons = new ArrayList();

   public void incrementAddonCount() {
      ++this.totalAddons;
   }

   public int getTotalAddons() {
      return this.totalAddons;
   }

   public void addAddon(IAddon addon) {
      this.addons.add(addon);
   }

   public List<IAddon> getAddons() {
      return this.addons;
   }

   public void initAddons() {
      LogUtils.getLogger().info("Starting addon initialization.");
      Iterator var1 = FabricLoader.getInstance().getEntrypointContainers("thunderhack", IAddon.class).iterator();

      while(var1.hasNext()) {
         EntrypointContainer<IAddon> entrypoint = (EntrypointContainer)var1.next();
         IAddon addon = (IAddon)entrypoint.getEntrypoint();

         try {
            LogUtils.getLogger().info("Initializing addon: " + addon.getClass().getName());
            LogUtils.getLogger().debug("Addon class loader: " + String.valueOf(addon.getClass().getClassLoader()));
            addon.onInitialize();
            LogUtils.getLogger().info("Addon initialized successfully: " + addon.getClass().getName());
            this.incrementAddonCount();
            LogUtils.getLogger().debug("Addon count incremented.");
            this.addAddon(addon);
            LogUtils.getLogger().debug("Addon added to manager.");
            TsunamiClient.EVENT_BUS.registerLambdaFactory(addon.getPackage(), (lookupInMethod, klass) -> {
               return (Lookup)lookupInMethod.invoke((Object)null, klass, MethodHandles.lookup());
            });
            if (addon.getModules() != null) {
               addon.getModules().stream().filter(Objects::nonNull).forEach((module) -> {
                  try {
                     LogUtils.getLogger().info("Registering module: " + module.getClass().getName());
                     LogUtils.getLogger().debug("Module class loader: " + String.valueOf(module.getClass().getClassLoader()));
                     Managers.MODULE.registerModule(module);
                     LogUtils.getLogger().info("Module registered successfully: " + module.getClass().getName());
                  } catch (Exception var2) {
                     LogUtils.getLogger().error("Error registering module: " + module.getClass().getName(), var2);
                  }

               });
            }

            if (addon.getCommands() != null) {
               addon.getCommands().stream().filter(Objects::nonNull).forEach((command) -> {
                  try {
                     LogUtils.getLogger().info("Registering command: " + command.getClass().getName());
                     LogUtils.getLogger().debug("Command class loader: " + String.valueOf(command.getClass().getClassLoader()));
                     Managers.COMMAND.registerCommand(command);
                     LogUtils.getLogger().info("Command registered successfully: " + command.getClass().getName());
                  } catch (Exception var2) {
                     LogUtils.getLogger().error("Error registering command: " + command.getClass().getName(), var2);
                  }

               });
            }

            if (addon.getHudElements() != null) {
               addon.getHudElements().stream().filter(Objects::nonNull).forEach((hudElement) -> {
                  try {
                     LogUtils.getLogger().info("Registering HUD element: " + hudElement.getClass().getName());
                     LogUtils.getLogger().debug("HUD element class loader: " + String.valueOf(hudElement.getClass().getClassLoader()));
                     Managers.MODULE.registerHudElement(hudElement);
                     LogUtils.getLogger().info("HUD element registered successfully: " + hudElement.getClass().getName());
                  } catch (Exception var2) {
                     LogUtils.getLogger().error("Error registering HUD element: " + hudElement.getClass().getName(), var2);
                  }

               });
            }
         } catch (Exception var5) {
            LogUtils.getLogger().error("Error initializing addon: " + addon.getClass().getName(), var5);
         }
      }

      LogUtils.getLogger().info("Addon initialization complete.");
   }

   public void shutDown() {
      Iterator var1 = this.getAddons().iterator();

      while(var1.hasNext()) {
         IAddon addon = (IAddon)var1.next();

         try {
            addon.onShutdown();
         } catch (Exception var4) {
            LogUtils.getLogger().error("Error running addon onShutdown method: " + addon.getClass().getName(), var4);
         }
      }

   }
}
