package tsunami.features.modules.render;

import net.minecraft.class_238;
import net.minecraft.class_239;
import net.minecraft.class_3965;
import net.minecraft.class_4587;
import net.minecraft.class_239.class_240;
import tsunami.features.modules.Module;
import tsunami.setting.Setting;
import tsunami.setting.impl.ColorSetting;
import tsunami.utility.render.Render2DEngine;
import tsunami.utility.render.Render3DEngine;

public class BlockHighLight extends Module {
   private final Setting<BlockHighLight.Mode> mode;
   private final Setting<ColorSetting> color;
   private final Setting<Float> lineWidth;

   public BlockHighLight() {
      super("BlockHighLight", Module.Category.RENDER);
      this.mode = new Setting("Mode", BlockHighLight.Mode.Outline);
      this.color = new Setting("Color", new ColorSetting(-1));
      this.lineWidth = new Setting("LineWidth", 1.0F, 0.0F, 5.0F);
   }

   public void onRender3D(class_4587 stack) {
      if (mc.field_1765 != null) {
         if (mc.field_1765.method_17783() == class_240.field_1332) {
            class_239 var3 = mc.field_1765;
            if (var3 instanceof class_3965) {
               class_3965 bhr = (class_3965)var3;
               switch(((BlockHighLight.Mode)this.mode.getValue()).ordinal()) {
               case 0:
                  Render3DEngine.drawBoxOutline(new class_238(bhr.method_17777()), Render2DEngine.injectAlpha(((ColorSetting)this.color.getValue()).getColorObject(), 255), (Float)this.lineWidth.getValue());
                  Render3DEngine.drawFilledBox(stack, new class_238(bhr.method_17777()), ((ColorSetting)this.color.getValue()).getColorObject());
                  break;
               case 1:
                  Render3DEngine.drawSideOutline(new class_238(bhr.method_17777()), Render2DEngine.injectAlpha(((ColorSetting)this.color.getValue()).getColorObject(), 255), (Float)this.lineWidth.getValue(), bhr.method_17780());
                  Render3DEngine.drawFilledSide(stack, new class_238(bhr.method_17777()), ((ColorSetting)this.color.getValue()).getColorObject(), bhr.method_17780());
                  break;
               case 2:
                  Render3DEngine.drawFilledBox(stack, new class_238(bhr.method_17777()), ((ColorSetting)this.color.getValue()).getColorObject());
                  break;
               case 3:
                  Render3DEngine.drawFilledSide(stack, new class_238(bhr.method_17777()), ((ColorSetting)this.color.getValue()).getColorObject(), bhr.method_17780());
                  break;
               case 4:
                  Render3DEngine.drawBoxOutline(new class_238(bhr.method_17777()), Render2DEngine.injectAlpha(((ColorSetting)this.color.getValue()).getColorObject(), 255), (Float)this.lineWidth.getValue());
                  break;
               case 5:
                  Render3DEngine.drawSideOutline(new class_238(bhr.method_17777()), Render2DEngine.injectAlpha(((ColorSetting)this.color.getValue()).getColorObject(), 255), (Float)this.lineWidth.getValue(), bhr.method_17780());
               }

            }
         }
      }
   }

   private static enum Mode {
      Both,
      BothSide,
      Fill,
      FilledSide,
      Outline,
      OutlinedSide;

      // $FF: synthetic method
      private static BlockHighLight.Mode[] $values() {
         return new BlockHighLight.Mode[]{Both, BothSide, Fill, FilledSide, Outline, OutlinedSide};
      }
   }
}
