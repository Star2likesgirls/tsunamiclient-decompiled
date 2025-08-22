package tsunami.features.hud.impl;

import com.mojang.blaze3d.platform.GlStateManager.class_4534;
import com.mojang.blaze3d.platform.GlStateManager.class_4535;
import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import net.minecraft.class_332;
import net.minecraft.class_4587;
import tsunami.features.hud.HudElement;
import tsunami.setting.Setting;
import tsunami.utility.render.Render2DEngine;
import tsunami.utility.render.TextureStorage;
import tsunami.utility.render.animation.AnimationUtility;

public class CandleHud extends HudElement {
   private Setting<Integer> scale = new Setting("Scale", 25, 15, 100);
   private Setting<CandleHud.For> mode;
   private float xAnim;
   private float yAnim;
   private float prevPitch;

   public CandleHud() {
      super("Candle", 10, 100);
      this.mode = new Setting("For", CandleHud.For.Win);
   }

   public void onRender2D(class_332 context) {
      super.onRender2D(context);
      float yDelta = mc.field_1724.field_6259 - mc.field_1724.method_5791();
      if (yDelta > 0.0F) {
         this.xAnim = AnimationUtility.fast(this.xAnim, -15.0F, 10.0F);
      } else if (yDelta < 0.0F) {
         this.xAnim = AnimationUtility.fast(this.xAnim, 35.0F, 10.0F);
      } else {
         this.xAnim = AnimationUtility.fast(this.xAnim, 10.0F, 10.0F);
      }

      float pDelta = this.prevPitch - mc.field_1724.method_36455();
      if (pDelta > 0.0F) {
         this.yAnim = AnimationUtility.fast(this.yAnim, -15.0F, 10.0F);
      } else if (pDelta < 0.0F) {
         this.yAnim = AnimationUtility.fast(this.yAnim, 35.0F, 10.0F);
      } else {
         this.yAnim = AnimationUtility.fast(this.yAnim, 10.0F, 10.0F);
      }

      this.prevPitch = mc.field_1724.method_36455();
      context.method_51448().method_22903();
      context.method_51448().method_46416((float)((int)this.getPosX()), (float)((int)this.getPosY()), 0.0F);
      float scalefactor = (float)(Integer)this.scale.getValue() / 100.0F;
      context.method_51448().method_22905(scalefactor, scalefactor, 1.0F);
      context.method_25290(TextureStorage.candle, 0, -5, 0.0F, 0.0F, 102, 529, 102, 529);
      context.method_51448().method_22909();
      this.drawFire(context.method_51448(), this.getPosX() + (55.0F - this.xAnim) * scalefactor, this.getPosY() + (10.0F - this.yAnim) * scalefactor, 7.0F * scalefactor, 7.0F * scalefactor, Render2DEngine.applyOpacity(new Color(16401935), (float)Math.sin((double)((float)(mc.field_1724.field_6012 + 10) / 15.0F)) + 1.4F));
      this.drawFire(context.method_51448(), this.getPosX() + (45.5F - this.xAnim / 2.0F) * scalefactor, this.getPosY() + (15.0F - this.yAnim / 2.0F) * scalefactor, 15.0F * scalefactor, 15.0F * scalefactor, Render2DEngine.applyOpacity(new Color(16748318), (float)Math.sin((double)((float)(mc.field_1724.field_6012 + 20) / 15.0F)) + 1.5F));
      this.drawFire(context.method_51448(), this.getPosX() + (41.5F - this.xAnim / 3.0F) * scalefactor, this.getPosY() + (27.0F - this.yAnim / 3.0F) * scalefactor, 20.0F * scalefactor, 20.0F * scalefactor, Render2DEngine.applyOpacity(new Color(16565074), (float)Math.sin((double)((float)(mc.field_1724.field_6012 + 30) / 15.0F)) + 1.6F));
      this.drawFire(context.method_51448(), this.getPosX() + (40.0F - this.xAnim / 4.0F) * scalefactor, this.getPosY() + (45.0F - this.yAnim / 4.0F) * scalefactor, 25.0F * scalefactor, 25.0F * scalefactor, Render2DEngine.applyOpacity(new Color(16568455), (float)Math.sin((double)((float)(mc.field_1724.field_6012 + 40) / 15.0F)) + 1.7F));
      this.drawFire(context.method_51448(), this.getPosX() + (44.0F - this.xAnim / 4.0F) * scalefactor, this.getPosY() + (43.0F - this.yAnim / 4.0F) * scalefactor, 15.0F * scalefactor, 15.0F * scalefactor, Render2DEngine.applyOpacity(new Color(6721782), (float)Math.sin((double)((float)(mc.field_1724.field_6012 + 40) / 15.0F)) + 1.8F));
      this.setBounds(this.getPosX(), this.getPosY(), (float)((int)(102.0F * scalefactor)), (float)((int)(529.0F * scalefactor)));
   }

   private void drawFire(class_4587 matrices, float x, float y, float width, float height, Color color) {
      width += 14.0F;
      height += 14.0F;
      x -= 7.0F;
      y -= 7.0F;
      RenderSystem.enableBlend();
      RenderSystem.setShaderColor((float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F, (float)color.getAlpha() / 255.0F);
      RenderSystem.blendFunc(class_4535.SRC_ALPHA, class_4534.ONE);
      RenderSystem.setShaderTexture(0, TextureStorage.firefly);
      Render2DEngine.renderTexture(matrices, (double)x, (double)y, (double)width, (double)height, 0.0F, 0.0F, (double)width, (double)height, (double)width, (double)height);
      RenderSystem.disableBlend();
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
   }

   private static enum For {
      Luck,
      Win,
      LowPing,
      AntiKick;

      // $FF: synthetic method
      private static CandleHud.For[] $values() {
         return new CandleHud.For[]{Luck, Win, LowPing, AntiKick};
      }
   }
}
