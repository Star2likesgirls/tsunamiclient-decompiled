package tsunami.features.hud.impl;

import com.mojang.blaze3d.platform.GlStateManager.class_4534;
import com.mojang.blaze3d.platform.GlStateManager.class_4535;
import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import net.minecraft.class_124;
import net.minecraft.class_332;
import tsunami.TsunamiClient;
import tsunami.core.Managers;
import tsunami.features.hud.HudElement;
import tsunami.features.modules.client.HudEditor;
import tsunami.gui.font.FontRenderers;
import tsunami.setting.Setting;
import tsunami.setting.impl.ColorSetting;
import tsunami.utility.math.MathUtility;
import tsunami.utility.render.Render2DEngine;
import tsunami.utility.render.TextureStorage;

public class Speedometer extends HudElement {
   public float speed = 0.0F;
   private final Setting<Boolean> bps = new Setting("BPS", false);
   private final Setting<Boolean> average = new Setting("Average", false);

   public Speedometer() {
      super("Speedometer", 50, 10);
   }

   public void onRender2D(class_332 context) {
      super.onRender2D(context);
      String str = "Speed " + String.valueOf(class_124.field_1068);
      if (!(Boolean)this.bps.getValue()) {
         str = str + MathUtility.round(this.getSpeedKpH() * TsunamiClient.TICK_TIMER) + " km/h";
      } else {
         str = str + MathUtility.round(this.getSpeedMpS() * TsunamiClient.TICK_TIMER) + " b/s";
      }

      float pX = this.getPosX() > (float)mc.method_22683().method_4486() / 2.0F ? this.getPosX() - FontRenderers.getModulesRenderer().getStringWidth(str) : this.getPosX();
      if (HudEditor.hudStyle.is(HudEditor.HudStyle.Blurry)) {
         Render2DEngine.drawRoundedBlur(context.method_51448(), pX, this.getPosY(), FontRenderers.getModulesRenderer().getStringWidth(str) + 21.0F, 13.0F, 3.0F, ((ColorSetting)HudEditor.blurColor.getValue()).getColorObject());
         Render2DEngine.drawRect(context.method_51448(), pX + 14.0F, this.getPosY() + 2.0F, 0.5F, 8.0F, new Color(1157627903, true));
         Render2DEngine.setupRender();
         RenderSystem.blendFunc(class_4535.SRC_ALPHA, class_4534.ONE);
         RenderSystem.setShaderTexture(0, TextureStorage.speedometerIcon);
         Render2DEngine.renderGradientTexture(context.method_51448(), (double)(pX + 2.0F), (double)(this.getPosY() + 1.0F), 10.0D, 10.0D, 0.0F, 0.0F, 512.0D, 512.0D, 512.0D, 512.0D, HudEditor.getColor(270), HudEditor.getColor(0), HudEditor.getColor(180), HudEditor.getColor(90));
         Render2DEngine.endRender();
      }

      FontRenderers.getModulesRenderer().drawString(context.method_51448(), str, (double)(pX + 18.0F), (double)(this.getPosY() + 5.0F), HudEditor.getColor(1).getRGB());
      this.setBounds(pX, this.getPosY(), FontRenderers.getModulesRenderer().getStringWidth(str) + 21.0F, 13.0F);
   }

   public float getSpeedKpH() {
      return ((Boolean)this.average.getValue() ? Managers.PLAYER.averagePlayerSpeed : Managers.PLAYER.currentPlayerSpeed) * 72.0F;
   }

   public float getSpeedMpS() {
      return ((Boolean)this.average.getValue() ? Managers.PLAYER.averagePlayerSpeed : Managers.PLAYER.currentPlayerSpeed) * 20.0F;
   }
}
