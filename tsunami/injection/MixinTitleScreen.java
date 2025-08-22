package tsunami.injection;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.class_2561;
import net.minecraft.class_437;
import net.minecraft.class_442;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tsunami.core.manager.client.ModuleManager;
import tsunami.features.modules.Module;
import tsunami.features.modules.client.ClientSettings;
import tsunami.gui.mainmenu.MainMenuScreen;
import tsunami.gui.misc.DialogScreen;
import tsunami.utility.render.TextureStorage;

@Mixin({class_442.class})
public class MixinTitleScreen extends class_437 {
   private static final String VERSION_URL = "https://tsunamiclient.rf.gd/version.txt";
   private static final String CURRENT_VERSION = "1.1";

   protected MixinTitleScreen(class_2561 title) {
      super(title);
   }

   @Inject(
      method = {"init"},
      at = {@At("RETURN")}
   )
   public void postInitHook(CallbackInfo ci) {
      if ((Boolean)ClientSettings.customMainMenu.getValue() && !MainMenuScreen.getInstance().confirm && ModuleManager.clickGui.getBind().getKey() != -1) {
         Module.mc.method_1507(MainMenuScreen.getInstance());
      }

      if (ModuleManager.clickGui.getBind().getKey() == -1) {
         DialogScreen dialogScreen1 = new DialogScreen(TextureStorage.questionPic, "Hello!", "What's your language?", "Русский", "English", () -> {
            ClientSettings.language.setValue(ClientSettings.Language.RU);
            Module.mc.method_1507(MainMenuScreen.getInstance());
         }, () -> {
            ClientSettings.language.setValue(ClientSettings.Language.ENG);
            Module.mc.method_1507(MainMenuScreen.getInstance());
         });
         Module.mc.method_1507(dialogScreen1);
      }

      if (!FabricLoader.getInstance().isDevelopmentEnvironment()) {
      }

   }
}
