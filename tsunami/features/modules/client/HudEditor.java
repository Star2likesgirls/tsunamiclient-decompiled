package tsunami.features.modules.client;

import java.awt.Color;
import tsunami.features.modules.Module;
import tsunami.gui.hud.HudEditorGui;
import tsunami.setting.Setting;
import tsunami.setting.impl.ColorSetting;
import tsunami.utility.render.Render2DEngine;

public final class HudEditor extends Module {
   public static final Setting<Boolean> sticky = new Setting("Sticky", true);
   public static final Setting<HudEditor.HudStyle> hudStyle;
   public static final Setting<HudEditor.ArrowsStyle> arrowsStyle;
   public static final Setting<ClickGui.colorModeEn> colorMode;
   public static final Setting<Integer> colorSpeed;
   public static final Setting<Boolean> glow;
   public static final Setting<ColorSetting> hcolor1;
   public static final Setting<ColorSetting> acolor;
   public static final Setting<ColorSetting> plateColor;
   public static final Setting<ColorSetting> textColor;
   public static final Setting<ColorSetting> textColor2;
   public static final Setting<ColorSetting> blurColor;
   public static final Setting<Float> hudRound;
   public static final Setting<Float> alpha;
   public static final Setting<Float> blend;
   public static final Setting<Float> outline;
   public static final Setting<Float> glow1;
   public static final Setting<Float> blurOpacity;
   public static final Setting<Float> blurStrength;

   public HudEditor() {
      super("HudEditor", Module.Category.CLIENT);
   }

   public static Color getColor(int count) {
      Color var10000;
      switch((ClickGui.colorModeEn)colorMode.getValue()) {
      case Sky:
         var10000 = Render2DEngine.skyRainbow((Integer)colorSpeed.getValue(), count);
         break;
      case LightRainbow:
         var10000 = Render2DEngine.rainbow((Integer)colorSpeed.getValue(), count, 0.6F, 1.0F, 1.0F);
         break;
      case Rainbow:
         var10000 = Render2DEngine.rainbow((Integer)colorSpeed.getValue(), count, 1.0F, 1.0F, 1.0F);
         break;
      case Fade:
         var10000 = Render2DEngine.fade((Integer)colorSpeed.getValue(), count, ((ColorSetting)hcolor1.getValue()).getColorObject(), 1.0F);
         break;
      case DoubleColor:
         var10000 = Render2DEngine.TwoColoreffect(((ColorSetting)hcolor1.getValue()).getColorObject(), ((ColorSetting)acolor.getValue()).getColorObject(), (double)(Integer)colorSpeed.getValue(), (double)count);
         break;
      case Analogous:
         var10000 = Render2DEngine.interpolateColorsBackAndForth((Integer)colorSpeed.getValue(), count, ((ColorSetting)hcolor1.getValue()).getColorObject(), Render2DEngine.getAnalogousColor(((ColorSetting)acolor.getValue()).getColorObject()), true);
         break;
      default:
         var10000 = ((ColorSetting)hcolor1.getValue()).getColorObject();
      }

      return var10000;
   }

   public void onEnable() {
      HudEditorGui hudGui = HudEditorGui.getHudGui();
      if (hudGui != null && mc != null) {
         mc.method_1507(hudGui);
      } else {
         System.out.println("HudEditor: GUI or MC is null!");
      }

      this.disable();
   }

   static {
      hudStyle = new Setting("HudStyle", HudEditor.HudStyle.Blurry);
      arrowsStyle = new Setting("ArrowsStyle", HudEditor.ArrowsStyle.Default);
      colorMode = new Setting("ColorMode", ClickGui.colorModeEn.Static);
      colorSpeed = new Setting("ColorSpeed", 18, 2, 54);
      glow = new Setting("Light", true);
      hcolor1 = new Setting("Color", new ColorSetting(-16728876));
      acolor = new Setting("Color2", new ColorSetting(-15916473));
      plateColor = new Setting("PlateColor", new ColorSetting(-16711681));
      textColor = new Setting("TextColor", new ColorSetting(-1));
      textColor2 = new Setting("TextColor2", new ColorSetting(-1));
      blurColor = new Setting("BlurColor", new ColorSetting(-16773595));
      hudRound = new Setting("HudRound", 4.0F, 1.0F, 7.0F);
      alpha = new Setting("Alpha", 0.9F, 0.0F, 1.0F);
      blend = new Setting("Blend", 10.0F, 1.0F, 15.0F);
      outline = new Setting("Outline", 0.5F, 0.0F, 2.5F);
      glow1 = new Setting("Glow", 0.5F, 0.0F, 1.0F);
      blurOpacity = new Setting("BlurOpacity", 0.55F, 0.0F, 1.0F);
      blurStrength = new Setting("BlurStrength", 20.0F, 5.0F, 50.0F);
   }

   public static enum HudStyle {
      Blurry,
      Glowing;

      // $FF: synthetic method
      private static HudEditor.HudStyle[] $values() {
         return new HudEditor.HudStyle[]{Blurry, Glowing};
      }
   }

   public static enum ArrowsStyle {
      Default,
      New;

      // $FF: synthetic method
      private static HudEditor.ArrowsStyle[] $values() {
         return new HudEditor.ArrowsStyle[]{Default, New};
      }
   }
}
