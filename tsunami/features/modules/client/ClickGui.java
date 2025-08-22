package tsunami.features.modules.client;

import baritone.api.BaritoneAPI;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_2960;
import tsunami.TsunamiClient;
import tsunami.events.impl.EventSetting;
import tsunami.features.modules.Module;
import tsunami.gui.clickui.ClickGUI;
import tsunami.gui.font.FontRenderers;
import tsunami.setting.Setting;
import tsunami.setting.impl.BooleanSettingGroup;

public class ClickGui extends Module {
   public final Setting<ClickGui.Gradient> gradientMode;
   public final Setting<ClickGui.TextSide> textSide;
   public final Setting<ClickGui.scrollModeEn> scrollMode;
   public final Setting<Integer> catHeight;
   public final Setting<Boolean> descriptions;
   public final Setting<Boolean> blur;
   public final Setting<Boolean> tips;
   public final Setting<Boolean> pauseBaritone;
   public final Setting<ClickGui.Image> image;
   public final Setting<Integer> moduleWidth;
   public final Setting<Integer> moduleHeight;
   public final Setting<Integer> settingFontScale;
   public final Setting<Integer> modulesFontScale;
   public static final Setting<BooleanSettingGroup> gear = new Setting("Gear", new BooleanSettingGroup(true));
   public final Setting<Integer> gearScale;
   public static Setting<Float> gearDuration;
   public static Setting<Integer> gearStop;
   public final Setting<Boolean> closeAnimation;

   public ClickGui() {
      super("ClickGui", Module.Category.CLIENT);
      this.gradientMode = new Setting("Gradient", ClickGui.Gradient.LeftToRight);
      this.textSide = new Setting("TextSide", ClickGui.TextSide.Left);
      this.scrollMode = new Setting("ScrollMode", ClickGui.scrollModeEn.Old);
      this.catHeight = new Setting("CategoryHeight", 300, 100, 720, (v) -> {
         return this.scrollMode.is(ClickGui.scrollModeEn.New);
      });
      this.descriptions = new Setting("Descriptions", true);
      this.blur = new Setting("Blur", true);
      this.tips = new Setting("Tips", true);
      this.pauseBaritone = new Setting("PauseBaritone", false);
      this.image = new Setting("Image", ClickGui.Image.None);
      this.moduleWidth = new Setting("ModuleWidth", 100, 50, 200);
      this.moduleHeight = new Setting("ModuleHeight", 14, 8, 25);
      this.settingFontScale = new Setting("SettingFontScale", 12, 6, 24);
      this.modulesFontScale = new Setting("ModulesFontScale", 14, 6, 24);
      this.gearScale = (new Setting("GearScale", 60, 6, 300)).addToGroup(gear);
      this.closeAnimation = new Setting("CloseAnimation", true);
   }

   public void onEnable() {
      if ((Boolean)this.pauseBaritone.getValue() && !fullNullCheck() && TsunamiClient.baritone) {
         BaritoneAPI.getProvider().getPrimaryBaritone().getCommandManager().execute("pause");
      }

      this.setGui();
   }

   public void onDisable() {
      if ((Boolean)this.pauseBaritone.getValue() && !fullNullCheck() && TsunamiClient.baritone) {
         BaritoneAPI.getProvider().getPrimaryBaritone().getCommandManager().execute("resume");
      }

   }

   public void setGui() {
      mc.method_1507(ClickGUI.getClickGui());
   }

   public void onUpdate() {
      if (!(mc.field_1755 instanceof ClickGUI)) {
         this.disable();
      }

   }

   @EventHandler
   public void onSetting(EventSetting e) {
      try {
         if (e.getSetting() == this.settingFontScale) {
            FontRenderers.sf_medium_mini = FontRenderers.create((float)(Integer)this.settingFontScale.getValue(), "sf_medium");
         }

         if (e.getSetting() == this.modulesFontScale) {
            FontRenderers.sf_medium_modules = FontRenderers.create((float)(Integer)this.modulesFontScale.getValue(), "sf_medium");
         }

         if (e.getSetting() == this.image) {
            ClickGUI.getClickGui().imageAnimation.reset();
         }
      } catch (Exception var3) {
      }

   }

   static {
      gearDuration = (new Setting("GearDuration", 0.5F, 0.1F, 2.0F)).addToGroup(gear);
      gearStop = (new Setting("GearStop", 25, 10, 45)).addToGroup(gear);
   }

   public static enum Gradient {
      UpsideDown,
      LeftToRight,
      both;

      // $FF: synthetic method
      private static ClickGui.Gradient[] $values() {
         return new ClickGui.Gradient[]{UpsideDown, LeftToRight, both};
      }
   }

   public static enum TextSide {
      Left,
      Center;

      // $FF: synthetic method
      private static ClickGui.TextSide[] $values() {
         return new ClickGui.TextSide[]{Left, Center};
      }
   }

   public static enum scrollModeEn {
      New,
      Old;

      // $FF: synthetic method
      private static ClickGui.scrollModeEn[] $values() {
         return new ClickGui.scrollModeEn[]{New, Old};
      }
   }

   public static enum Image {
      None("", 0, 0, new int[]{0, 0}, 0),
      Bdsm("bdsm.png", 1414, 823, new int[]{-100, 90}, 900),
      Cutie("cutie.png", 476, 490, new int[]{500, 60}, 500),
      Cutie2("cutie2.png", 540, 540, new int[]{500, 60}, 500),
      Nya("nya.png", 246, 238, new int[]{500, 60}, 500),
      Smile("smile.png", 324, 406, new int[]{500, 40}, 500),
      Bat("bat.png", 352, 503, new int[]{500, 40}, 500),
      Gacha("gacha.png", 592, 592, new int[]{500, 60}, 500),
      NFT("nft.png", 562, 494, new int[]{400, 110}, 500),
      ThousandSeven("thousand_seven.png", 380, 336, new int[]{400, 130}, 500);

      public final int fileWidth;
      public final int fileHeight;
      public final class_2960 file;
      public final int[] pos;
      public final int size;

      private Image(String file, int fileWidth, int fileHeight, int[] pos, int size) {
         this.fileHeight = fileHeight;
         this.fileWidth = fileWidth;
         this.file = class_2960.method_60655("thunderhack", "textures/gui/images/" + file);
         this.pos = pos;
         this.size = size;
      }

      // $FF: synthetic method
      private static ClickGui.Image[] $values() {
         return new ClickGui.Image[]{None, Bdsm, Cutie, Cutie2, Nya, Smile, Bat, Gacha, NFT, ThousandSeven};
      }
   }

   public static enum colorModeEn {
      Static,
      Sky,
      LightRainbow,
      Rainbow,
      Fade,
      DoubleColor,
      Analogous;

      // $FF: synthetic method
      private static ClickGui.colorModeEn[] $values() {
         return new ClickGui.colorModeEn[]{Static, Sky, LightRainbow, Rainbow, Fade, DoubleColor, Analogous};
      }
   }
}
