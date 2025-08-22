package tsunami.gui.clickui.impl;

import java.awt.Color;
import net.minecraft.class_332;
import net.minecraft.class_4587;
import tsunami.TsunamiClient;
import tsunami.gui.clickui.AbstractElement;
import tsunami.gui.font.FontRenderers;
import tsunami.setting.Setting;
import tsunami.setting.impl.ColorSetting;
import tsunami.utility.math.MathUtility;
import tsunami.utility.render.Render2DEngine;

public class ColorPickerElement extends AbstractElement {
   private float hue;
   private float saturation;
   private float brightness;
   private int alpha;
   private boolean afocused;
   private boolean hfocused;
   private boolean sbfocused;
   private float spos;
   private float bpos;
   private float hpos;
   private float apos;
   private Color prevColor;
   private boolean firstInit;
   private boolean extended;
   private final Setting colorSetting;

   public ColorSetting getColorSetting() {
      return (ColorSetting)this.colorSetting.getValue();
   }

   public ColorPickerElement(Setting setting) {
      super(setting);
      this.colorSetting = setting;
      this.prevColor = this.getColorSetting().getColorObject();
      this.updatePos();
      this.firstInit = true;
   }

   public void render(class_332 context, int mouseX, int mouseY, float delta) {
      class_4587 matrixStack = context.method_51448();
      boolean colorHovered = Render2DEngine.isHovered((double)mouseX, (double)mouseY, (double)this.x, (double)(this.y + 5.0F), 90.0D, 7.0D);
      FontRenderers.sf_medium_mini.drawString(matrixStack, this.setting.getName(), (double)(this.x + 6.0F), (double)(this.y + 8.0F), (new Color(-1)).getRGB());
      Render2DEngine.drawBlurredShadow(matrixStack, this.x + this.width - 22.0F, this.y + 5.0F, 14.0F, 7.0F, colorHovered ? 6 : 10, this.getColorSetting().getColorObject());
      if (colorHovered) {
         Render2DEngine.drawRound(matrixStack, this.x + this.width - 22.5F, this.y + 4.5F, 15.0F, 8.0F, 1.0F, this.getColorSetting().getColorObject());
      } else {
         Render2DEngine.drawRound(matrixStack, this.x + this.width - 22.0F, this.y + 5.0F, 14.0F, 7.0F, 1.0F, this.getColorSetting().getColorObject());
      }

      if (this.extended) {
         boolean rainbowHovered = Render2DEngine.isHovered((double)mouseX, (double)mouseY, (double)(this.x + 36.0F), (double)(this.y + 54.0F), 24.0D, 7.0D);
         boolean copyHovered = Render2DEngine.isHovered((double)mouseX, (double)mouseY, (double)(this.x + 9.0F), (double)(this.y + 54.0F), 24.0D, 7.0D);
         boolean pasteHovered = Render2DEngine.isHovered((double)mouseX, (double)mouseY, (double)(this.x + 63.0F), (double)(this.y + 54.0F), 24.0D, 7.0D);
         boolean dark = Render2DEngine.isDark(TsunamiClient.copy_color);
         boolean dark2 = Render2DEngine.isDark(this.getColorSetting().getColorObject());
         Render2DEngine.drawRect(matrixStack, this.x + 9.0F, this.y + 54.0F, 24.0F, 7.0F, new Color(4342338));
         FontRenderers.sf_medium_mini.drawString(matrixStack, "Сopy", (double)(this.x + 13.0F), (double)(this.y + 56.5F), copyHovered ? (new Color(-1543503873, true)).getRGB() : Color.WHITE.getRGB());
         Render2DEngine.drawRect(matrixStack, this.x + 36.0F, this.y + 54.0F, 24.0F, 7.0F, this.getColorSetting().isRainbow() ? this.getColorSetting().getColorObject() : new Color(4342338));
         FontRenderers.sf_medium_mini.drawString(matrixStack, "RB", (double)(this.x + 44.0F), (double)(this.y + 56.5F), rainbowHovered ? (new Color(-1543503873, true)).getRGB() : (dark2 ? Color.WHITE.getRGB() : Color.BLACK.getRGB()));
         Render2DEngine.drawRect(matrixStack, this.x + 63.0F, this.y + 54.0F, 24.0F, 7.0F, TsunamiClient.copy_color);
         FontRenderers.sf_medium_mini.drawString(matrixStack, "Paste", (double)(this.x + 67.0F), (double)(this.y + 56.5F), pasteHovered ? (new Color(-1543503873, true)).getRGB() : (dark ? Color.WHITE.getRGB() : Color.BLACK.getRGB()));
         this.renderPicker(matrixStack, mouseX, mouseY, this.getColorSetting().getColorObject());
      }
   }

   public float getHeight() {
      return this.extended ? 66.0F : 15.0F;
   }

   private void renderPicker(class_4587 matrixStack, int mouseX, int mouseY, Color color) {
      double cx = (double)(this.x + 6.0F);
      double cy = (double)(this.y + 16.0F);
      double cw = (double)(this.width - 38.0F);
      double ch = (double)(this.height - 30.0F);
      if (this.prevColor != this.getColorSetting().getColorObject()) {
         this.updatePos();
         this.prevColor = this.getColorSetting().getColorObject();
      }

      if (this.firstInit) {
         this.spos = (float)(cx + cw - (cw - cw * (double)this.saturation));
         this.bpos = (float)(cy + (ch - ch * (double)this.brightness));
         this.hpos = (float)(cy + ch - 3.0D + (ch - 3.0D) * (double)this.hue);
         this.apos = (float)(cy + (ch - 3.0D - (ch - 3.0D) * (double)((float)this.alpha / 255.0F)));
         this.firstInit = false;
      }

      this.spos = Render2DEngine.scrollAnimate(this.spos, (float)(cx + cw - (cw - cw * (double)this.saturation)), 0.6F);
      this.bpos = Render2DEngine.scrollAnimate(this.bpos, (float)(cy + (ch - ch * (double)this.brightness)), 0.6F);
      this.hpos = Render2DEngine.scrollAnimate(this.hpos, (float)(cy + ch - 3.0D + (ch - 3.0D) * (double)this.hue), 0.6F);
      this.apos = Render2DEngine.scrollAnimate(this.apos, (float)(cy + (ch - 3.0D - (ch - 3.0D) * (double)((float)this.alpha / 255.0F))), 0.6F);
      Color colorA = Color.getHSBColor(this.hue, 0.0F, 1.0F);
      Color colorB = Color.getHSBColor(this.hue, 1.0F, 1.0F);
      Color colorC = new Color(0, 0, 0, 0);
      Color colorD = new Color(0, 0, 0);
      Render2DEngine.horizontalGradient(matrixStack, (float)cx + 2.0F, (float)cy, (float)(cx + cw), (float)(cy + ch), colorA, colorB);
      Render2DEngine.verticalGradient(matrixStack, (float)(cx + 2.0D), (float)cy, (float)(cx + cw), (float)(cy + ch), colorC, colorD);

      for(float i = 1.0F; (double)i < ch - 2.0D; ++i) {
         float curHue = (float)(1.0D / (ch / (double)i));
         Render2DEngine.drawRect(matrixStack, (float)(cx + cw + 4.0D), (float)(cy + (double)i), 8.0F, 1.0F, Color.getHSBColor(curHue, 1.0F, 1.0F));
      }

      Render2DEngine.drawRect(matrixStack, (float)(cx + cw + 17.0D), (float)(cy + 1.0D), 8.0F, (float)(ch - 3.0D), new Color(-1));
      Render2DEngine.verticalGradient(matrixStack, (float)(cx + cw + 17.0D), (float)(cy + 0.800000011920929D), (float)(cx + cw + 25.0D), (float)(cy + ch - 2.0D), new Color(color.getRed(), color.getGreen(), color.getBlue(), 255), new Color(0, 0, 0, 0));
      Render2DEngine.drawRect(matrixStack, (float)(cx + cw + 3.0D), this.hpos + 0.5F, 10.0F, 1.0F, Color.WHITE);
      Render2DEngine.drawRect(matrixStack, (float)(cx + cw + 16.0D), this.apos + 0.5F, 10.0F, 1.0F, Color.WHITE);
      Render2DEngine.drawRound(matrixStack, this.spos - 1.5F, this.bpos - 1.5F, 3.0F, 3.0F, 1.5F, new Color(-1));
      Color value = Color.getHSBColor(this.hue, this.saturation, this.brightness);
      if (this.sbfocused) {
         this.saturation = (float)((double)MathUtility.clamp((float)((double)mouseX - cx), 0.0F, (float)cw) / cw);
         this.brightness = (float)((ch - (double)MathUtility.clamp((float)((double)mouseY - cy), 0.0F, (float)ch)) / ch);
         value = Color.getHSBColor(this.hue, this.saturation, this.brightness);
         this.setColor(new Color(value.getRed(), value.getGreen(), value.getBlue(), this.alpha));
      }

      if (this.hfocused) {
         this.hue = (float)(-((ch - (double)MathUtility.clamp((float)((double)mouseY - cy), 0.0F, (float)ch)) / ch));
         value = Color.getHSBColor(this.hue, this.saturation, this.brightness);
         this.setColor(new Color(value.getRed(), value.getGreen(), value.getBlue(), this.alpha));
      }

      if (this.afocused) {
         this.alpha = (int)((ch - (double)MathUtility.clamp((float)((double)mouseY - cy), 0.0F, (float)ch)) / ch * 255.0D);
         this.setColor(new Color(value.getRed(), value.getGreen(), value.getBlue(), this.alpha));
      }

   }

   private void updatePos() {
      float[] hsb = Color.RGBtoHSB(this.getColorSetting().getColorObject().getRed(), this.getColorSetting().getColorObject().getGreen(), this.getColorSetting().getColorObject().getBlue(), (float[])null);
      this.hue = -1.0F + hsb[0];
      this.saturation = hsb[1];
      this.brightness = hsb[2];
      this.alpha = this.getColorSetting().getAlpha();
   }

   private void setColor(Color color) {
      this.getColorSetting().setColor(color.getRGB());
      this.prevColor = color;
   }

   public void mouseClicked(int mouseX, int mouseY, int button) {
      double cx = (double)(this.x + 4.0F);
      double cy = (double)(this.y + 17.0F);
      double cw = (double)(this.width - 34.0F);
      double ch = (double)(this.height - 30.0F);
      boolean rainbowHovered = Render2DEngine.isHovered((double)mouseX, (double)mouseY, (double)(this.x + 36.0F), (double)(this.y + 54.0F), 24.0D, 7.0D);
      boolean copyHovered = Render2DEngine.isHovered((double)mouseX, (double)mouseY, (double)(this.x + 9.0F), (double)(this.y + 54.0F), 24.0D, 7.0D);
      boolean pasteHovered = Render2DEngine.isHovered((double)mouseX, (double)mouseY, (double)(this.x + 63.0F), (double)(this.y + 54.0F), 24.0D, 7.0D);
      boolean colorHovered = Render2DEngine.isHovered((double)mouseX, (double)mouseY, (double)this.x, (double)this.y, 90.0D, 11.0D);
      if (colorHovered) {
         this.extended = !this.extended;
      }

      if (this.extended) {
         if (Render2DEngine.isHovered((double)mouseX, (double)mouseY, cx + cw + 17.0D, cy, 8.0D, ch) && button == 0) {
            this.afocused = true;
         } else if (Render2DEngine.isHovered((double)mouseX, (double)mouseY, cx + cw + 4.0D, cy, 8.0D, ch) && button == 0) {
            this.hfocused = true;
         } else if (Render2DEngine.isHovered((double)mouseX, (double)mouseY, cx, cy, cw, ch) && button == 0) {
            this.sbfocused = true;
         } else if (rainbowHovered && button == 0) {
            this.getColorSetting().setRainbow(!this.getColorSetting().isRainbow());
         } else if (copyHovered) {
            TsunamiClient.copy_color = this.getColorSetting().getColorObject();
         } else if (pasteHovered) {
            this.getColorSetting().setColor(TsunamiClient.copy_color.getRGB());
         }

         super.mouseClicked(mouseX, mouseY, button);
      }
   }

   public void mouseReleased(int mouseX, int mouseY, int button) {
      this.hfocused = false;
      this.afocused = false;
      this.sbfocused = false;
   }

   public void onClose() {
      this.hfocused = false;
      this.afocused = false;
      this.sbfocused = false;
   }
}
