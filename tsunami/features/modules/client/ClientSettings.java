package tsunami.features.modules.client;

import tsunami.features.modules.Module;
import tsunami.setting.Setting;

public final class ClientSettings extends Module {
   public static Setting<Boolean> futureCompatibility = new Setting("FutureCompatibility", false);
   public static Setting<Boolean> customMainMenu = new Setting("CustomMainMenu", true);
   public static Setting<Boolean> customPanorama = new Setting("CustomPanorama", true);
   public static Setting<Boolean> customLoadingScreen = new Setting("CustomLoadingScreen", true);
   public static Setting<Boolean> scaleFactorFix = new Setting("ScaleFactorFix", false);
   public static Setting<Float> scaleFactorFixValue = new Setting("ScaleFactorFixValue", 2.0F, 0.0F, 4.0F);
   public static Setting<Boolean> renderRotations = new Setting("RenderRotations", true);
   public static Setting<Boolean> skullEmoji = new Setting("SkullEmoji", true);
   public static Setting<Boolean> clientMessages = new Setting("ClientMessages", true);
   public static Setting<Boolean> debug = new Setting("Debug", false);
   public static Setting<Boolean> customBob = new Setting("CustomBob", true);
   public static Setting<Boolean> telemetry = new Setting("Telemetry", true);
   public static Setting<ClientSettings.Language> language;
   public static Setting<String> prefix;
   public static Setting<ClientSettings.ClipCommandMode> clipCommandMode;

   public ClientSettings() {
      super("ClientSettings", Module.Category.NONE);
   }

   public static boolean isRu() {
      return language.is(ClientSettings.Language.RU);
   }

   public boolean isToggleable() {
      return false;
   }

   static {
      language = new Setting("Language", ClientSettings.Language.ENG);
      prefix = new Setting("Prefix", "@");
      clipCommandMode = new Setting("ClipCommandMode", ClientSettings.ClipCommandMode.Matrix);
   }

   public static enum Language {
      RU,
      ENG;

      // $FF: synthetic method
      private static ClientSettings.Language[] $values() {
         return new ClientSettings.Language[]{RU, ENG};
      }
   }

   public static enum ClipCommandMode {
      Default,
      Matrix;

      // $FF: synthetic method
      private static ClientSettings.ClipCommandMode[] $values() {
         return new ClientSettings.ClipCommandMode[]{Default, Matrix};
      }
   }
}
