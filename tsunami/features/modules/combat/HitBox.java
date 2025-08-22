package tsunami.features.modules.combat;

import tsunami.core.manager.client.ServerManager;
import tsunami.features.modules.Module;
import tsunami.setting.Setting;

public final class HitBox extends Module {
   public static final Setting<Float> XZExpand = new Setting("XZExpand", 1.0F, 0.0F, 5.0F);
   public static final Setting<Float> YExpand = new Setting("YExpand", 0.0F, 0.0F, 5.0F);
   public static final Setting<Boolean> affectToAura = new Setting("AffectToAura", false);

   public HitBox() {
      super("HitBoxes", Module.Category.NONE);
   }

   public String getDisplayInfo() {
      float var10000 = ServerManager.round2((double)(Float)XZExpand.getValue());
      return "H: " + var10000 + " V: " + ServerManager.round2((double)(Float)YExpand.getValue());
   }
}
