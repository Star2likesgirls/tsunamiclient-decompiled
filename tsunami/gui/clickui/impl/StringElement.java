package tsunami.gui.clickui.impl;

import java.awt.Color;
import net.minecraft.class_332;
import net.minecraft.class_3544;
import org.lwjgl.glfw.GLFW;
import tsunami.TsunamiClient;
import tsunami.features.modules.Module;
import tsunami.gui.clickui.AbstractElement;
import tsunami.gui.clickui.ClickGUI;
import tsunami.gui.font.FontRenderers;
import tsunami.setting.Setting;
import tsunami.utility.render.Render2DEngine;

public class StringElement extends AbstractElement {
   public boolean listening;
   private String currentString = "";

   public StringElement(Setting setting) {
      super(setting);
   }

   public void render(class_332 context, int mouseX, int mouseY, float delta) {
      super.render(context, mouseX, mouseY, delta);
      Render2DEngine.drawRect(context.method_51448(), this.getX() + 5.0F, this.getY() + 2.0F, this.getWidth() - 11.0F, 10.0F, new Color(-1811939328, true));
      FontRenderers.sf_medium_mini.drawString(context.method_51448(), this.listening ? this.currentString + (Module.mc.field_1724 != null && Module.mc.field_1724.field_6012 % 5 != 0 ? "" : "_") : (String)this.setting.getValue(), (double)(this.x + 6.0F), (double)(this.y + this.height / 2.0F), -1);
      if (Render2DEngine.isHovered((double)mouseX, (double)mouseY, (double)(this.getX() + 5.0F), (double)(this.getY() + 2.0F), (double)(this.getWidth() - 11.0F), 10.0D)) {
         if (GLFW.glfwGetPlatform() != 393219) {
            GLFW.glfwSetCursor(Module.mc.method_22683().method_4490(), GLFW.glfwCreateStandardCursor(221186));
         }

         ClickGUI.anyHovered = true;
      }

   }

   public void mouseClicked(int mouseX, int mouseY, int button) {
      if (this.hovered && button == 0) {
         this.listening = !this.listening;
      }

      if (this.listening) {
         TsunamiClient.currentKeyListener = TsunamiClient.KeyListening.Strings;
         this.currentString = (String)this.setting.getValue();
      }

      super.mouseClicked(mouseX, mouseY, button);
   }

   public void charTyped(char key, int keyCode) {
      if (class_3544.method_57175(key)) {
         this.currentString = this.currentString + key;
      }

   }

   public void keyTyped(int keyCode) {
      if (TsunamiClient.currentKeyListener == TsunamiClient.KeyListening.Strings) {
         if (this.listening) {
            switch(keyCode) {
            case 32:
               this.currentString = this.currentString + " ";
            case 256:
            default:
               break;
            case 257:
               this.setting.setValue(this.currentString != null && !this.currentString.isEmpty() ? this.currentString : this.setting.getDefaultValue());
               this.currentString = "";
               this.listening = !this.listening;
               break;
            case 259:
               this.currentString = SliderElement.removeLastChar(this.currentString);
            }
         }

      }
   }
}
