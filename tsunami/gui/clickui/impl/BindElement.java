package tsunami.gui.clickui.impl;

import java.awt.Color;
import net.minecraft.class_332;
import tsunami.gui.clickui.AbstractElement;
import tsunami.gui.font.FontRenderers;
import tsunami.setting.Setting;
import tsunami.setting.impl.Bind;
import tsunami.utility.render.Render2DEngine;

public class BindElement extends AbstractElement {
   public boolean isListening;

   public BindElement(Setting setting) {
      super(setting);
   }

   public void render(class_332 context, int mouseX, int mouseY, float delta) {
      super.render(context, mouseX, mouseY, delta);
      FontRenderers.sf_medium_mini.drawString(context.method_51448(), this.setting.getName(), (double)(this.getX() + 6.0F), (double)(this.getY() + this.height / 2.0F - 3.0F + 2.0F), (new Color(-1)).getRGB());
      float tWidth = FontRenderers.sf_medium_mini.getStringWidth(this.isListening ? "..." : ((Bind)this.setting.getValue()).getBind());
      Render2DEngine.drawRect(context.method_51448(), this.getX() + (this.getWidth() - tWidth - 11.0F), this.getY() + 2.0F, tWidth + 4.0F, 10.0F, new Color(-1811939328, true));
      FontRenderers.sf_medium_mini.drawString(context.method_51448(), this.isListening ? "..." : ((Bind)this.setting.getValue()).getBind(), (double)(this.getX() + (this.getWidth() - tWidth - 9.0F)), (double)(this.getY() + this.height / 2.0F - 1.0F), (new Color(-1)).getRGB());
   }

   public void mouseClicked(int mouseX, int mouseY, int button) {
      if (this.isListening) {
         Bind b = new Bind(button, true, false);
         this.setting.setValue(b);
         this.isListening = false;
      }

      if (this.hovered && button == 0) {
         this.isListening = !this.isListening;
      }

      super.mouseClicked(mouseX, mouseY, button);
   }

   public void keyTyped(int keyCode) {
      if (this.isListening) {
         Bind b;
         if (keyCode != 256 && keyCode != 261) {
            b = new Bind(keyCode, false, false);
            this.setting.setValue(b);
         } else {
            b = new Bind(-1, false, false);
            this.setting.setValue(b);
         }

         this.isListening = false;
      }

   }
}
