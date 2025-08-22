package tsunami.features.hud.impl;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_332;
import tsunami.features.hud.HudElement;
import tsunami.features.modules.client.HudEditor;
import tsunami.gui.font.FontRenderers;
import tsunami.setting.impl.ColorSetting;
import tsunami.utility.render.Render2DEngine;

public class PVPResources extends HudElement {
   public PVPResources() {
      super("PVPResources", 50, 50);
   }

   public void onRender2D(class_332 context) {
      super.onRender2D(context);
      Render2DEngine.drawHudBase(context.method_51448(), this.getPosX(), this.getPosY(), 50.0F, 50.0F, (Float)HudEditor.hudRound.getValue());
      this.setBounds(this.getPosX(), this.getPosY(), 50.0F, 50.0F);
      if (HudEditor.hudStyle.is(HudEditor.HudStyle.Blurry)) {
         Render2DEngine.drawRectDumbWay(context.method_51448(), this.getPosX(), this.getPosY() + 24.5F, this.getPosX() + 50.0F, this.getPosY() + 25.0F, new Color(1426063359, true));
         Render2DEngine.drawRectDumbWay(context.method_51448(), this.getPosX() + 24.5F, this.getPosY() - 1.0F, this.getPosX() + 25.0F, this.getPosY() + 49.0F, new Color(1426063359, true));
      } else {
         Render2DEngine.horizontalGradient(context.method_51448(), this.getPosX() + 2.0F, this.getPosY() + 24.5F, this.getPosX() + 26.0F, this.getPosY() + 25.0F, Render2DEngine.injectAlpha(((ColorSetting)HudEditor.textColor.getValue()).getColorObject(), 0), ((ColorSetting)HudEditor.textColor.getValue()).getColorObject());
         Render2DEngine.horizontalGradient(context.method_51448(), this.getPosX() + 26.0F, this.getPosY() + 24.5F, this.getPosX() + 48.0F, this.getPosY() + 25.0F, ((ColorSetting)HudEditor.textColor.getValue()).getColorObject(), Render2DEngine.injectAlpha(((ColorSetting)HudEditor.textColor.getValue()).getColorObject(), 0));
         Render2DEngine.verticalGradient(context.method_51448(), this.getPosX() + 25.5F, this.getPosY() + 2.0F, this.getPosX() + 26.0F, this.getPosY() + 23.0F, Render2DEngine.injectAlpha(((ColorSetting)HudEditor.textColor.getValue()).getColorObject(), 0), ((ColorSetting)HudEditor.textColor.getValue()).getColorObject());
         Render2DEngine.verticalGradient(context.method_51448(), this.getPosX() + 25.5F, this.getPosY() + 23.0F, this.getPosX() + 26.0F, this.getPosY() + 48.0F, ((ColorSetting)HudEditor.textColor.getValue()).getColorObject(), Render2DEngine.injectAlpha(((ColorSetting)HudEditor.textColor.getValue()).getColorObject(), 0));
      }

      int totemCount = this.getItemCount(class_1802.field_8288);
      int xpCount = this.getItemCount(class_1802.field_8287);
      int crystalCount = this.getItemCount(class_1802.field_8301);
      int gappleCount = this.getItemCount(class_1802.field_8367);
      List<class_1799> list = new ArrayList();
      if (totemCount > 0) {
         list.add(new class_1799(class_1802.field_8288, totemCount));
      }

      if (xpCount > 0) {
         list.add(new class_1799(class_1802.field_8287, xpCount));
      }

      if (crystalCount > 0) {
         list.add(new class_1799(class_1802.field_8301, crystalCount));
      }

      if (gappleCount > 0) {
         list.add(new class_1799(class_1802.field_8367, gappleCount));
      }

      for(int i = 0; i < list.size(); ++i) {
         int offsetX = i % 2 * 25;
         int offsetY = i / 2 * 25;
         context.method_51427((class_1799)list.get(i), (int)(this.getPosX() + (float)offsetX + 4.0F), (int)(this.getPosY() + (float)offsetY + 4.0F));
         context.method_51448().method_22903();
         context.method_51448().method_46416(0.0F, 0.0F, 151.0F);
         Render2DEngine.drawBlurredShadow(context.method_51448(), this.getPosX() + (float)offsetX + 8.0F, this.getPosY() + (float)offsetY + 8.0F, 9.0F, 9.0F, 12, Color.BLACK);
         FontRenderers.sf_medium.drawCenteredString(context.method_51448(), String.valueOf(((class_1799)list.get(i)).method_7947()), (double)((int)(this.getPosX() + (float)offsetX + 12.0F)), (double)((int)(this.getPosY() + (float)offsetY + 11.0F)), ((ColorSetting)HudEditor.textColor.getValue()).getColor());
         context.method_51448().method_22909();
      }

   }

   public int getItemCount(class_1792 item) {
      if (mc.field_1724 == null) {
         return 0;
      } else {
         int n = 0;
         int n2 = 44;

         for(int i = 0; i <= n2; ++i) {
            class_1799 itemStack = mc.field_1724.method_31548().method_5438(i);
            if (itemStack.method_7909() == item) {
               n += itemStack.method_7947();
            }
         }

         return n;
      }
   }
}
