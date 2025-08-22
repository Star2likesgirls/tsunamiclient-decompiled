package tsunami.features.modules.client;

import java.awt.Color;
import tsunami.features.modules.Module;
import tsunami.gui.thundergui.ThunderGui;
import tsunami.setting.Setting;
import tsunami.setting.impl.ColorSetting;

public final class ThunderHackGui extends Module {
   public static final Setting<ColorSetting> onColor1 = new Setting("OnColor1", new ColorSetting((new Color(71, 0, 117, 255)).getRGB()));
   public static final Setting<ColorSetting> onColor2 = new Setting("OnColor2", new ColorSetting((new Color(32, 1, 96, 255)).getRGB()));
   public static final Setting<Float> scrollSpeed = new Setting("ScrollSpeed", 1.0F, 0.1F, 2.0F);

   public ThunderHackGui() {
      super("ThunderGui", Module.Category.NONE);
   }

   public void onEnable() {
      mc.method_1507(ThunderGui.getThunderGui());
      this.disable();
   }

   public static Color getColorByTheme(int id) {
      Color var10000;
      switch(id) {
      case 0:
         var10000 = new Color(37, 27, 41, 250);
         break;
      case 1:
         var10000 = new Color(50, 35, 60, 250);
         break;
      case 2:
         var10000 = new Color(-1);
         break;
      case 3:
      case 8:
         var10000 = new Color(6645093);
         break;
      case 4:
         var10000 = new Color(50, 35, 60, 178);
         break;
      case 5:
         var10000 = new Color(133, 93, 162, 178);
         break;
      case 6:
         var10000 = new Color(88, 64, 107, 178);
         break;
      case 7:
         var10000 = new Color(25, 20, 30, 255);
         break;
      case 9:
         var10000 = new Color(50, 35, 60, 178);
         break;
      default:
         var10000 = new Color(37, 27, 41, 250);
      }

      return var10000;
   }
}
