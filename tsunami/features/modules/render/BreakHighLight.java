package tsunami.features.modules.render;

import java.awt.Color;
import net.minecraft.class_1297;
import net.minecraft.class_2338;
import net.minecraft.class_238;
import net.minecraft.class_239;
import net.minecraft.class_3965;
import net.minecraft.class_4587;
import tsunami.features.modules.Module;
import tsunami.injection.accesors.IWorldRenderer;
import tsunami.setting.Setting;
import tsunami.setting.impl.ColorSetting;
import tsunami.utility.math.MathUtility;
import tsunami.utility.render.Render2DEngine;
import tsunami.utility.render.Render3DEngine;

public class BreakHighLight extends Module {
   private final Setting<BreakHighLight.Mode> mode;
   private final Setting<ColorSetting> color;
   private final Setting<ColorSetting> color2;
   private final Setting<ColorSetting> ocolor;
   private final Setting<ColorSetting> ocolor2;
   private final Setting<ColorSetting> textColor;
   private final Setting<Float> lineWidth;
   private final Setting<Boolean> otherPlayer;
   private float prevProgress;

   public BreakHighLight() {
      super("BreakHighLight", Module.Category.RENDER);
      this.mode = new Setting("Mode", BreakHighLight.Mode.Shrink);
      this.color = new Setting("Color", new ColorSetting(new Color(-1862467584, true)));
      this.color2 = new Setting("Color2", new ColorSetting(new Color(-196608, true)));
      this.ocolor = new Setting("OutlineColor", new ColorSetting(new Color(-1874854656, true)));
      this.ocolor2 = new Setting("OutlineColor2", new ColorSetting(new Color(-13697792, true)));
      this.textColor = new Setting("TextColor", new ColorSetting(-1));
      this.lineWidth = new Setting("LineWidth", 2.0F, 0.0F, 5.0F);
      this.otherPlayer = new Setting("OtherPlayer", true);
   }

   public void onRender3D(class_4587 stack) {
      if (mc.field_1761.method_2923() && mc.field_1765 != null) {
         class_239 var3 = mc.field_1765;
         if (var3 instanceof class_3965) {
            class_3965 bhr = (class_3965)var3;
            if (!mc.field_1687.method_22347(bhr.method_17777())) {
               class_238 shrunkMineBox = new class_238((double)bhr.method_17777().method_10263(), (double)bhr.method_17777().method_10264(), (double)bhr.method_17777().method_10260(), (double)bhr.method_17777().method_10263(), (double)bhr.method_17777().method_10264(), (double)bhr.method_17777().method_10260());
               float noom;
               switch(((BreakHighLight.Mode)this.mode.getValue()).ordinal()) {
               case 0:
                  noom = Render2DEngine.interpolateFloat(this.prevProgress, MathUtility.clamp(mc.field_1761.field_3715, 0.0F, 1.0F), (double)Render3DEngine.getTickDelta());
                  break;
               case 1:
                  noom = 1.0F - Render2DEngine.interpolateFloat(this.prevProgress, mc.field_1761.field_3715, (double)Render3DEngine.getTickDelta());
                  break;
               default:
                  noom = 1.0F;
               }

               Render3DEngine.drawFilledBox(stack, shrunkMineBox.method_1002((double)noom, (double)noom, (double)noom).method_989(0.5D + (double)noom * 0.5D, 0.5D + (double)noom * 0.5D, 0.5D + (double)noom * 0.5D), Render2DEngine.interpolateColorC(((ColorSetting)this.color.getValue()).getColorObject(), ((ColorSetting)this.color2.getValue()).getColorObject(), noom));
               Render3DEngine.drawBoxOutline(shrunkMineBox.method_1002((double)noom, (double)noom, (double)noom).method_989(0.5D + (double)noom * 0.5D, 0.5D + (double)noom * 0.5D, 0.5D + (double)noom * 0.5D), Render2DEngine.interpolateColorC(((ColorSetting)this.ocolor.getValue()).getColorObject(), ((ColorSetting)this.ocolor2.getValue()).getColorObject(), noom), (Float)this.lineWidth.getValue());
               switch(((BreakHighLight.Mode)this.mode.getValue()).ordinal()) {
               case 0:
                  this.prevProgress = noom;
                  break;
               case 1:
                  this.prevProgress = 1.0F - noom;
                  break;
               default:
                  this.prevProgress = 1.0F;
               }
            }
         }
      }

      ((IWorldRenderer)mc.field_1769).getBlockBreakingInfos().forEach((integer, destroyBlockProgress) -> {
         class_1297 object = mc.field_1687.method_8469(integer);
         if (object != null && (Boolean)this.otherPlayer.getValue() && !object.method_5477().equals(mc.field_1724.method_5477())) {
            class_2338 pos = destroyBlockProgress.method_13991();
            Render3DEngine.drawTextIn3D(String.valueOf(object.method_5477().getString()), pos.method_46558(), 0.0D, 0.1D, 0.0D, ((ColorSetting)this.textColor.getValue()).getColorObject());
            class_238 shrunkMineBox = new class_238((double)pos.method_10263(), (double)pos.method_10264(), (double)pos.method_10260(), (double)pos.method_10263(), (double)pos.method_10264(), (double)pos.method_10260());
            float noom;
            switch(((BreakHighLight.Mode)this.mode.getValue()).ordinal()) {
            case 0:
               noom = MathUtility.clamp((float)destroyBlockProgress.method_13988() / 10.0F, 0.0F, 1.0F);
               break;
            case 1:
               noom = 1.0F - (float)destroyBlockProgress.method_13988() / 10.0F;
               break;
            default:
               noom = 1.0F;
            }

            Render3DEngine.drawFilledBox(stack, shrunkMineBox.method_1002((double)noom, (double)noom, (double)noom).method_989(0.5D + (double)noom * 0.5D, 0.5D + (double)noom * 0.5D, 0.5D + (double)noom * 0.5D), Render2DEngine.interpolateColorC(((ColorSetting)this.color.getValue()).getColorObject(), ((ColorSetting)this.color2.getValue()).getColorObject(), noom));
            Render3DEngine.drawBoxOutline(shrunkMineBox.method_1002((double)noom, (double)noom, (double)noom).method_989(0.5D + (double)noom * 0.5D, 0.5D + (double)noom * 0.5D, 0.5D + (double)noom * 0.5D), Render2DEngine.interpolateColorC(((ColorSetting)this.ocolor.getValue()).getColorObject(), ((ColorSetting)this.ocolor2.getValue()).getColorObject(), noom), (Float)this.lineWidth.getValue());
         }

      });
   }

   private static enum Mode {
      Grow,
      Shrink,
      Static;

      // $FF: synthetic method
      private static BreakHighLight.Mode[] $values() {
         return new BreakHighLight.Mode[]{Grow, Shrink, Static};
      }
   }
}
