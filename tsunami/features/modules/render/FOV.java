package tsunami.features.modules.render;

import tsunami.features.modules.Module;
import tsunami.setting.Setting;

public class FOV extends Module {
   public final Setting<Integer> fovModifier = new Setting("FOV modifier", 120, 0, 358);
   public final Setting<Boolean> itemFov = new Setting("Item Fov", false);
   public final Setting<Integer> itemFovModifier = new Setting("Item FOV modifier", 120, 0, 358);

   public FOV() {
      super("FOV", Module.Category.RENDER);
   }
}
