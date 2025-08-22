package tsunami.features.hud.impl;

import java.awt.Color;
import net.minecraft.class_332;
import tsunami.features.hud.HudElement;
import tsunami.features.modules.client.HudEditor;
import tsunami.gui.font.FontRenderers;
import tsunami.setting.impl.ColorSetting;
import tsunami.utility.render.Render2DEngine;
import tsunami.utility.render.animation.AnimationUtility;

public class Cooldowns extends HudElement {
   private float animation1;
   private float animation2;

   public Cooldowns() {
      super("Cooldowns", 100, 100);
   }

   public void onRender2D(class_332 context) {
      super.onRender2D(context);
      Render2DEngine.drawHudBase(context.method_51448(), this.getPosX(), this.getPosY(), 100.0F, 40.0F, (Float)HudEditor.hudRound.getValue());
      this.animation1 = AnimationUtility.fast(this.animation1, mc.field_1724.method_7261(0.5F), 50.0F);
      this.animation2 = AnimationUtility.fast(this.animation2, 1.0F - (float)mc.field_1724.field_6235 / 10.0F, 50.0F);
      Render2DEngine.drawGradientRound(context.method_51448(), this.getPosX() + 30.0F, this.getPosY() + 20.0F, 65.0F, 5.0F, 1.5F, HudEditor.getColor(90).darker().darker().darker(), HudEditor.getColor(180).darker().darker().darker(), HudEditor.getColor(0).darker().darker().darker(), HudEditor.getColor(270).darker().darker().darker());
      Render2DEngine.drawGradientRound(context.method_51448(), this.getPosX() + 30.0F, this.getPosY() + 30.0F, 65.0F, 5.0F, 1.5F, HudEditor.getColor(90).darker().darker().darker(), HudEditor.getColor(180).darker().darker().darker(), HudEditor.getColor(0).darker().darker().darker(), HudEditor.getColor(270).darker().darker().darker());
      Render2DEngine.drawRect(context.method_51448(), this.getPosX() + 30.0F, this.getPosY() + 20.0F, 65.0F * this.animation1, 5.0F, 1.5F, 1.0F);
      Render2DEngine.drawRect(context.method_51448(), this.getPosX() + 30.0F, this.getPosY() + 30.0F, 65.0F * this.animation2, 5.0F, 1.5F, 1.0F);
      if (HudEditor.hudStyle.is(HudEditor.HudStyle.Glowing)) {
         FontRenderers.sf_bold.drawCenteredString(context.method_51448(), "Cooldowns", (double)(this.getPosX() + 50.0F), (double)(this.getPosY() + 4.0F), ((ColorSetting)HudEditor.textColor.getValue()).getColorObject());
      } else {
         FontRenderers.sf_bold.drawGradientCenteredString(context.method_51448(), "Cooldowns", this.getPosX() + 50.0F, this.getPosY() + 4.0F, 10);
      }

      if (HudEditor.hudStyle.is(HudEditor.HudStyle.Blurry)) {
         Render2DEngine.drawRectDumbWay(context.method_51448(), this.getPosX() + 4.0F, this.getPosY() + 13.0F, this.getPosX() + this.getWidth() - 4.0F, this.getPosY() + 13.5F, new Color(1426063359, true));
      } else {
         Render2DEngine.horizontalGradient(context.method_51448(), this.getPosX() + 2.0F, this.getPosY() + 13.7F, this.getPosX() + 2.0F + 50.0F - 2.0F, this.getPosY() + 14.0F, Render2DEngine.injectAlpha(((ColorSetting)HudEditor.textColor.getValue()).getColorObject(), 0), ((ColorSetting)HudEditor.textColor.getValue()).getColorObject());
         Render2DEngine.horizontalGradient(context.method_51448(), this.getPosX() + 2.0F + 50.0F - 2.0F, this.getPosY() + 13.7F, this.getPosX() + 2.0F + 100.0F - 4.0F, this.getPosY() + 14.0F, ((ColorSetting)HudEditor.textColor.getValue()).getColorObject(), Render2DEngine.injectAlpha(((ColorSetting)HudEditor.textColor.getValue()).getColorObject(), 0));
      }

      FontRenderers.sf_bold_mini.drawString(context.method_51448(), "Attack", (double)(this.getPosX() + 5.0F), (double)(this.getPosY() + 20.0F), ((ColorSetting)HudEditor.textColor.getValue()).getColor());
      FontRenderers.sf_bold_mini.drawString(context.method_51448(), "Hurt", (double)(this.getPosX() + 5.0F), (double)(this.getPosY() + 30.0F), ((ColorSetting)HudEditor.textColor.getValue()).getColor());
      this.setBounds(this.getPosX(), this.getPosY(), 100.0F, 40.0F);
   }
}
