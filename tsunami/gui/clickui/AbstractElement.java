package tsunami.gui.clickui;

import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_3675;
import tsunami.setting.Setting;
import tsunami.setting.impl.ColorSetting;
import tsunami.utility.render.Render2DEngine;

public abstract class AbstractElement {
   protected Setting setting;
   protected float x;
   protected float y;
   protected float width;
   protected float height;
   protected float offsetY;
   protected boolean hovered;

   public AbstractElement(Setting setting) {
      this.setting = setting;
   }

   public void render(class_332 context, int mouseX, int mouseY, float delta) {
      this.hovered = Render2DEngine.isHovered((double)mouseX, (double)mouseY, (double)this.x, (double)this.y, (double)this.width, (double)this.height);
   }

   public void init() {
   }

   public void mouseClicked(int mouseX, int mouseY, int button) {
      if (class_3675.method_15987(class_310.method_1551().method_22683().method_4490(), 261) && button == 2 && this.hovered) {
         Object var5 = this.setting.getValue();
         if (var5 instanceof ColorSetting) {
            ColorSetting cs = (ColorSetting)var5;
            cs.setDefault();
         } else {
            this.setting.setValue(this.setting.getDefaultValue());
         }
      }

   }

   public void mouseReleased(int mouseX, int mouseY, int button) {
   }

   public void keyTyped(int keyCode) {
   }

   public void onClose() {
   }

   public Setting getSetting() {
      return this.setting;
   }

   public float getX() {
      return this.x;
   }

   public float getY() {
      return this.y;
   }

   public float getWidth() {
      return this.width;
   }

   public float getHeight() {
      return this.height;
   }

   public void setX(float x) {
      this.x = x;
   }

   public void setY(float y) {
      this.y = y + this.offsetY;
   }

   public void setWidth(float width) {
      this.width = width;
   }

   public void setHeight(float height) {
      this.height = height;
   }

   public void setOffsetY(float offsetY) {
      this.offsetY = offsetY;
   }

   public boolean isVisible() {
      return this.setting.isVisible();
   }

   public void charTyped(char key, int keyCode) {
   }
}
