package tsunami.setting.impl;

import java.awt.Color;
import org.jetbrains.annotations.NotNull;
import tsunami.features.modules.client.HudEditor;
import tsunami.utility.render.Render2DEngine;

public final class ColorSetting {
   private int color;
   private final int defaultColor;
   private boolean rainbow;

   public ColorSetting(@NotNull Color color) {
      this(color.getRGB());
   }

   public ColorSetting(int color) {
      this.color = color;
      this.defaultColor = color;
   }

   @NotNull
   public ColorSetting withAlpha(int alpha) {
      int red = this.getColor() >> 16 & 255;
      int green = this.getColor() >> 8 & 255;
      int blue = this.getColor() & 255;
      return new ColorSetting((alpha & 255) << 24 | (red & 255) << 16 | (green & 255) << 8 | blue & 255);
   }

   public int getColor() {
      return this.rainbow ? Render2DEngine.rainbow((Integer)HudEditor.colorSpeed.getValue(), 1, 1.0F, 1.0F, 1.0F).getRGB() : this.color;
   }

   public void setColor(int color) {
      this.color = color;
   }

   public int getRed() {
      return this.rainbow ? Render2DEngine.rainbow((Integer)HudEditor.colorSpeed.getValue(), 1, 1.0F, 1.0F, 1.0F).getRed() : this.color >> 16 & 255;
   }

   public int getGreen() {
      return this.rainbow ? Render2DEngine.rainbow((Integer)HudEditor.colorSpeed.getValue(), 1, 1.0F, 1.0F, 1.0F).getGreen() : this.color >> 8 & 255;
   }

   public int getBlue() {
      return this.rainbow ? Render2DEngine.rainbow((Integer)HudEditor.colorSpeed.getValue(), 1, 1.0F, 1.0F, 1.0F).getBlue() : this.color & 255;
   }

   public float getGlRed() {
      return (float)this.getRed() / 255.0F;
   }

   public float getGlBlue() {
      return (float)this.getBlue() / 255.0F;
   }

   public float getGlGreen() {
      return (float)this.getGreen() / 255.0F;
   }

   public float getGlAlpha() {
      return (float)this.getAlpha() / 255.0F;
   }

   public int getAlpha() {
      return this.color >> 24 & 255;
   }

   @NotNull
   public Color getColorObject() {
      return this.rainbow ? Render2DEngine.rainbow((Integer)HudEditor.colorSpeed.getValue(), 1, 1.0F, 1.0F, 1.0F) : new Color(this.color, true);
   }

   public int getRawColor() {
      return this.rainbow ? Render2DEngine.rainbow((Integer)HudEditor.colorSpeed.getValue(), 1, 1.0F, 1.0F, 1.0F).getRGB() : this.color;
   }

   public boolean isRainbow() {
      return this.rainbow;
   }

   public void setRainbow(boolean rainbow) {
      this.rainbow = rainbow;
   }

   public void setDefault() {
      this.setColor(this.defaultColor);
   }
}
