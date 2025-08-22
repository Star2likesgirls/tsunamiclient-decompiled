package tsunami.features.hud.impl;

import java.awt.Color;
import java.util.Iterator;
import net.minecraft.class_124;
import net.minecraft.class_1291;
import net.minecraft.class_1293;
import net.minecraft.class_332;
import tsunami.features.hud.HudElement;
import tsunami.features.modules.client.HudEditor;
import tsunami.gui.font.FontRenderer;
import tsunami.gui.font.FontRenderers;
import tsunami.setting.Setting;
import tsunami.setting.impl.ColorSetting;
import tsunami.utility.render.Render2DEngine;
import tsunami.utility.render.animation.AnimationUtility;

public class PotionHud extends HudElement {
   private float vAnimation;
   private float hAnimation;
   private final Setting<Boolean> colored = new Setting("Colored", false);

   public PotionHud() {
      super("Potions", 100, 100);
   }

   public static String getDuration(class_1293 pe) {
      if (pe.method_48559()) {
         return "*:*";
      } else {
         int var1 = pe.method_5584();
         int mins = var1 / 1200;
         String sec = String.format("%02d", var1 % 1200 / 20);
         return mins + ":" + sec;
      }
   }

   public void onRender2D(class_332 context) {
      super.onRender2D(context);
      int y_offset1 = 0;
      float max_width = 50.0F;
      float pointerX = 0.0F;
      Iterator var5 = mc.field_1724.method_6026().iterator();

      float px;
      while(var5.hasNext()) {
         class_1293 potionEffect = (class_1293)var5.next();
         class_1291 potion = (class_1291)potionEffect.method_5579().comp_349();
         if (y_offset1 == 0) {
            y_offset1 += 4;
         }

         y_offset1 += 9;
         FontRenderer var10000 = FontRenderers.sf_bold_mini;
         String var10001 = potion.method_5560().getString();
         float nameWidth = var10000.getStringWidth(var10001 + " " + (potionEffect.method_5578() + 1));
         px = FontRenderers.sf_bold_mini.getStringWidth(getDuration(potionEffect));
         float width = (nameWidth + px) * 1.4F;
         if (width > max_width) {
            max_width = width;
         }

         if (px > pointerX) {
            pointerX = px;
         }
      }

      this.vAnimation = AnimationUtility.fast(this.vAnimation, (float)(14 + y_offset1), 15.0F);
      this.hAnimation = AnimationUtility.fast(this.hAnimation, max_width, 15.0F);
      Render2DEngine.drawHudBase(context.method_51448(), this.getPosX(), this.getPosY(), this.hAnimation, this.vAnimation, (Float)HudEditor.hudRound.getValue());
      if (HudEditor.hudStyle.is(HudEditor.HudStyle.Glowing)) {
         FontRenderers.sf_bold.drawCenteredString(context.method_51448(), "Potions", (double)(this.getPosX() + this.hAnimation / 2.0F), (double)(this.getPosY() + 4.0F), ((ColorSetting)HudEditor.textColor.getValue()).getColorObject());
      } else {
         FontRenderers.sf_bold.drawGradientCenteredString(context.method_51448(), "Potions", this.getPosX() + this.hAnimation / 2.0F, this.getPosY() + 4.0F, 10);
      }

      if (y_offset1 > 0) {
         if (HudEditor.hudStyle.is(HudEditor.HudStyle.Blurry)) {
            Render2DEngine.drawRectDumbWay(context.method_51448(), this.getPosX() + 4.0F, this.getPosY() + 13.0F, this.getPosX() + this.getWidth() - 4.0F, this.getPosY() + 13.5F, new Color(1426063359, true));
         } else {
            Render2DEngine.horizontalGradient(context.method_51448(), this.getPosX() + 2.0F, this.getPosY() + 13.7F, this.getPosX() + 2.0F + this.hAnimation / 2.0F - 2.0F, this.getPosY() + 14.0F, Render2DEngine.injectAlpha(((ColorSetting)HudEditor.textColor.getValue()).getColorObject(), 0), ((ColorSetting)HudEditor.textColor.getValue()).getColorObject());
            Render2DEngine.horizontalGradient(context.method_51448(), this.getPosX() + 2.0F + this.hAnimation / 2.0F - 2.0F, this.getPosY() + 13.7F, this.getPosX() + 2.0F + this.hAnimation - 4.0F, this.getPosY() + 14.0F, ((ColorSetting)HudEditor.textColor.getValue()).getColorObject(), Render2DEngine.injectAlpha(((ColorSetting)HudEditor.textColor.getValue()).getColorObject(), 0));
         }
      }

      Render2DEngine.addWindow(context.method_51448(), this.getPosX(), this.getPosY(), this.getPosX() + this.hAnimation, this.getPosY() + this.vAnimation, 1.0D);
      int y_offset = 0;

      for(Iterator var12 = mc.field_1724.method_6026().iterator(); var12.hasNext(); y_offset += 9) {
         class_1293 potionEffect = (class_1293)var12.next();
         class_1291 potion = (class_1291)potionEffect.method_5579().comp_349();
         px = this.getPosX() + (max_width - pointerX - 10.0F);
         context.method_51448().method_22903();
         context.method_51448().method_46416(this.getPosX() + 2.0F, this.getPosY() + 16.0F + (float)y_offset, 0.0F);
         context.method_25298(0, 0, 0, 8, 8, mc.method_18505().method_18663(potionEffect.method_5579()));
         context.method_51448().method_22909();
         FontRenderers.sf_bold_mini.drawString(context.method_51448(), potion.method_5560().getString() + " " + String.valueOf(class_124.field_1061) + (potionEffect.method_5578() + 1), (double)(this.getPosX() + 12.0F), (double)(this.getPosY() + 19.0F + (float)y_offset), ((ColorSetting)HudEditor.textColor.getValue()).getColor());
         FontRenderers.sf_bold_mini.drawCenteredString(context.method_51448(), getDuration(potionEffect), (double)(px + (this.getPosX() + max_width - px) / 2.0F), (double)(this.getPosY() + 19.0F + (float)y_offset), ((ColorSetting)HudEditor.textColor.getValue()).getColor());
         Render2DEngine.drawRect(context.method_51448(), px, this.getPosY() + 17.0F + (float)y_offset, 0.5F, 8.0F, new Color(1157627903, true));
      }

      Render2DEngine.popWindow();
      this.setBounds(this.getPosX(), this.getPosY(), this.hAnimation, this.vAnimation);
   }
}
