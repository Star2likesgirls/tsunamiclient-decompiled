package tsunami.features.hud.impl;

import java.awt.Color;
import net.minecraft.class_1657;
import net.minecraft.class_1799;
import net.minecraft.class_332;
import net.minecraft.class_4587;
import tsunami.features.hud.HudElement;
import tsunami.features.modules.client.HudEditor;
import tsunami.gui.font.FontRenderers;
import tsunami.gui.windows.WindowsScreen;
import tsunami.setting.Setting;
import tsunami.setting.impl.ColorSetting;
import tsunami.utility.render.Render2DEngine;

public class Hotbar extends HudElement {
   public static final Setting<Hotbar.Mode> lmode;

   public Hotbar() {
      super("Hotbar", 0, 0);
   }

   public void onRender2D(class_332 context) {
      if (!(mc.field_1755 instanceof WindowsScreen)) {
         class_1657 playerEntity = mc.field_1724;
         if (playerEntity != null) {
            class_4587 matrices = context.method_51448();
            int i = mc.method_22683().method_4486() / 2;
            if (mc.field_1724.method_6079().method_7960()) {
               Render2DEngine.drawHudBase(matrices, (float)(i - 90), (float)(mc.method_22683().method_4502() - 25), 180.0F, 20.0F, (Float)HudEditor.hudRound.getValue());
            } else if (lmode.getValue() == Hotbar.Mode.Merged) {
               Render2DEngine.drawHudBase(matrices, (float)(i - 111), (float)(mc.method_22683().method_4502() - 25), 201.0F, 20.0F, (Float)HudEditor.hudRound.getValue());
               if (HudEditor.hudStyle.is(HudEditor.HudStyle.Blurry)) {
                  Render2DEngine.drawRect(context.method_51448(), (float)(i - 109 + 18), (float)(mc.method_22683().method_4502() - 23), 0.5F, 15.0F, new Color(1157627903, true));
               } else {
                  Render2DEngine.verticalGradient(matrices, (float)(i - 109 + 18), (float)(mc.method_22683().method_4502() - 22 + 1 - 4), (float)(i - 108 + 18) - 0.5F, (float)(mc.method_22683().method_4502() - 11 + 1 - 4), Render2DEngine.injectAlpha(((ColorSetting)HudEditor.textColor.getValue()).getColorObject(), 0), ((ColorSetting)HudEditor.textColor.getValue()).getColorObject());
                  Render2DEngine.verticalGradient(matrices, (float)(i - 109 + 18), (float)(mc.method_22683().method_4502() - 11 - 4), (float)(i - 108 + 18) - 0.5F, (float)(mc.method_22683().method_4502() - 5), ((ColorSetting)HudEditor.textColor.getValue()).getColorObject(), Render2DEngine.injectAlpha(((ColorSetting)HudEditor.textColor.getValue()).getColorObject(), 0));
               }
            } else {
               Render2DEngine.drawHudBase(matrices, (float)(i - 90), (float)(mc.method_22683().method_4502() - 25), 180.0F, 20.0F, (Float)HudEditor.hudRound.getValue());
               Render2DEngine.drawHudBase(matrices, (float)i - 112.5F, (float)(mc.method_22683().method_4502() - 25), 20.0F, 20.0F, (Float)HudEditor.hudRound.getValue());
            }

            Color c = HudEditor.hudStyle.is(HudEditor.HudStyle.Blurry) ? new Color(2081756437, true) : new Color(2083467055, true);
            Render2DEngine.drawRect(matrices, (float)(i - 88) + (float)playerEntity.method_31548().field_7545 * 19.8F, (float)(mc.method_22683().method_4502() - 24), 17.0F, 17.0F, (Float)HudEditor.hudRound.getValue(), 0.7F, c, c, c, c);
         }

      }
   }

   public static void renderHotBarItems(float tickDelta, class_332 context) {
      if (!(mc.field_1755 instanceof WindowsScreen)) {
         class_1657 playerEntity = mc.field_1724;
         if (playerEntity != null) {
            class_4587 matrices = context.method_51448();
            int i = mc.method_22683().method_4486() / 2;
            int o = mc.method_22683().method_4502() - 16 - 3;
            if (!mc.field_1724.method_6079().method_7960()) {
               if (lmode.getValue() == Hotbar.Mode.Merged) {
                  renderHotbarItem(context, i - 109, o - 5, playerEntity.method_6079());
               } else {
                  renderHotbarItem(context, i - 111, o - 5, playerEntity.method_6079());
               }
            }

            for(int m = 0; m < 9; ++m) {
               int n = i - 90 + m * 20 + 2;
               if (m == mc.field_1724.method_31548().field_7545) {
                  renderHotbarItem(context, n, o - 7, (class_1799)playerEntity.method_31548().field_7547.get(m));
               } else {
                  renderHotbarItem(context, n, o - 5, (class_1799)playerEntity.method_31548().field_7547.get(m));
               }
            }
         }

      }
   }

   private static void renderHotbarItem(class_332 context, int i, int j, class_1799 itemStack) {
      if (!itemStack.method_7960()) {
         context.method_51448().method_22903();
         context.method_51448().method_46416((float)(i + 8), (float)(j + 12), 0.0F);
         context.method_51448().method_22905(0.9F, 0.9F, 1.0F);
         context.method_51448().method_46416((float)(-(i + 8)), (float)(-(j + 12)), 0.0F);
         context.method_51427(itemStack, i, j);
         context.method_51431(mc.field_1772, itemStack, i, j);
         context.method_51448().method_22909();
      }

   }

   public static void renderXpBar(int x, class_4587 matrices) {
      mc.method_16011().method_15396("expBar");
      mc.method_16011().method_15407();
      if (mc.field_1724.field_7520 > 0) {
         mc.method_16011().method_15396("expLevel");
         String string = mc.field_1724.field_7520.makeConcatWithConstants<invokedynamic>(mc.field_1724.field_7520);
         int k = (int)(((float)mc.method_22683().method_4486() - FontRenderers.sf_bold_mini.getStringWidth(string)) / 2.0F);
         int l = mc.method_22683().method_4502() - 31 - 4;
         FontRenderers.sf_bold_mini.drawString(matrices, string, (double)k, (double)l, 8453920);
         mc.method_16011().method_15407();
      }

   }

   static {
      lmode = new Setting("LeftHandMode", Hotbar.Mode.Merged);
   }

   public static enum Mode {
      Merged,
      Separately;

      // $FF: synthetic method
      private static Hotbar.Mode[] $values() {
         return new Hotbar.Mode[]{Merged, Separately};
      }
   }
}
