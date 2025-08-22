package tsunami.features.hud.impl;

import java.awt.Color;
import net.minecraft.class_332;
import tsunami.features.hud.HudElement;
import tsunami.features.modules.client.HudEditor;
import tsunami.features.modules.movement.Timer;
import tsunami.gui.font.FontRenderers;
import tsunami.setting.impl.ColorSetting;
import tsunami.utility.render.Render2DEngine;
import tsunami.utility.render.animation.EaseOutCirc;

public class TimerIndicator extends HudElement {
   private final EaseOutCirc timerAnimation = new EaseOutCirc();

   public TimerIndicator() {
      super("TimerIndicator", 60, 10);
   }

   public void onRender2D(class_332 context) {
      super.onRender2D(context);
      if (HudEditor.hudStyle.is(HudEditor.HudStyle.Blurry)) {
         Render2DEngine.drawRoundedBlur(context.method_51448(), this.getPosX(), this.getPosY(), 65.0F, 15.0F, 3.0F, ((ColorSetting)HudEditor.blurColor.getValue()).getColorObject());
         Render2DEngine.drawRect(context.method_51448(), this.getPosX(), this.getPosY(), 65.0F * Timer.energy, 15.0F, 3.0F, 0.4F);
         FontRenderers.sf_bold_mini.drawCenteredString(context.method_51448(), Timer.energy >= 0.99F ? "100%" : (int)Math.ceil((double)(Timer.energy * 100.0F)) + "%", (double)(this.getPosX() + 32.0F), (double)(this.getPosY() + 5.5F), (new Color(200, 200, 200, 255)).getRGB());
         this.setBounds(this.getPosX(), this.getPosY(), 65.0F, 15.0F);
      } else {
         Render2DEngine.drawGradientBlurredShadow(context.method_51448(), this.getPosX() - 1.0F, this.getPosY() - 1.0F, 62.0F, 12.0F, 6, HudEditor.getColor(90), HudEditor.getColor(180), HudEditor.getColor(0), HudEditor.getColor(270));
         Render2DEngine.drawRect(context.method_51448(), this.getPosX(), this.getPosY(), 60.0F, 10.0F, new Color(-1644167168, true));
         Render2DEngine.draw2DGradientRect(context.method_51448(), this.getPosX(), this.getPosY(), this.getPosX() + 60.0F * Timer.energy, this.getPosY() + 10.0F, HudEditor.getColor(90), HudEditor.getColor(180), HudEditor.getColor(0), HudEditor.getColor(270));
         Render2DEngine.drawBlurredShadow(context.method_51448(), this.getPosX() + 20.0F, this.getPosY(), 22.0F, 10.0F, 6, new Color(1191182336, true));
         FontRenderers.sf_bold_mini.drawCenteredString(context.method_51448(), Timer.energy >= 0.99F ? "100%" : (int)Math.ceil((double)(Timer.energy * 100.0F)) + "%", (double)(this.getPosX() + 31.0F), (double)(this.getPosY() + 3.5F), (new Color(200, 200, 200, 255)).getRGB());
         this.setBounds(this.getPosX(), this.getPosY(), 60.0F, 10.0F);
      }

   }

   public void onUpdate() {
      this.timerAnimation.update();
   }
}
