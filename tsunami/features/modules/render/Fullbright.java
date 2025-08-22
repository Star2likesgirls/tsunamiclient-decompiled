package tsunami.features.modules.render;

import tsunami.features.modules.Module;
import tsunami.setting.Setting;

public class Fullbright extends Module {
   public static Setting<Float> minBright = new Setting("MinBright", 0.5F, 0.0F, 1.0F);

   public Fullbright() {
      super("Fullbright", Module.Category.RENDER);
   }
}
