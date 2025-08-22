package tsunami.gui.clickui.impl;

import java.awt.Color;
import java.util.Objects;
import net.minecraft.class_332;
import net.minecraft.class_3532;
import net.minecraft.class_3544;
import net.minecraft.class_4587;
import org.lwjgl.glfw.GLFW;
import tsunami.TsunamiClient;
import tsunami.core.Managers;
import tsunami.core.manager.IManager;
import tsunami.features.modules.client.HudEditor;
import tsunami.gui.clickui.AbstractElement;
import tsunami.gui.clickui.ClickGUI;
import tsunami.gui.font.FontRenderers;
import tsunami.setting.Setting;
import tsunami.utility.math.MathUtility;
import tsunami.utility.render.Render2DEngine;

public class SliderElement extends AbstractElement {
   private final float min;
   private final float max;
   private float animation;
   private float prevValue;
   private boolean dragging;
   private boolean listening;
   public String Stringnumber = "";

   public SliderElement(Setting setting) {
      super(setting);
      this.min = ((Number)setting.getMin()).floatValue();
      this.max = ((Number)setting.getMax()).floatValue();
   }

   public void render(class_332 context, int mouseX, int mouseY, float delta) {
      super.render(context, mouseX, mouseY, delta);
      this.animation = Render2DEngine.scrollAnimate(this.animation, (((Number)this.setting.getValue()).floatValue() - this.min) / (this.max - this.min), 0.4F);
      class_4587 matrixStack = context.method_51448();
      if (this.setting.group != null) {
         Render2DEngine.drawRect(context.method_51448(), this.x + 4.0F, this.y, 1.0F, 18.0F, HudEditor.getColor(1));
      }

      if (!this.dragging) {
         FontRenderers.sf_medium_mini.drawString(matrixStack, this.setting.getName(), (double)((this.setting.group != null ? 2.0F : 0.0F) + this.x + 6.0F), (double)(this.y + 4.0F), (new Color(-1)).getRGB());
         FontRenderers.sf_medium_mini.drawString(matrixStack, this.listening ? (Objects.equals(this.Stringnumber, "") ? "..." : this.Stringnumber) : String.valueOf(this.setting.getValue()).makeConcatWithConstants<invokedynamic>(String.valueOf(this.setting.getValue())), (double)((int)(this.x + this.width - 6.0F - FontRenderers.sf_medium_mini.getStringWidth(this.listening ? (Objects.equals(this.Stringnumber, "") ? "..." : this.Stringnumber) : String.valueOf(this.setting.getValue()).makeConcatWithConstants<invokedynamic>(String.valueOf(this.setting.getValue()))))), (double)(this.y + 5.0F), (new Color(-1)).getRGB());
      } else {
         if (this.animation > 0.2F) {
            FontRenderers.sf_medium_mini.drawString(matrixStack, String.valueOf(this.setting.getMin()).makeConcatWithConstants<invokedynamic>(String.valueOf(this.setting.getMin())), (double)(this.x + 6.0F), (double)(this.y + 4.0F), (new Color(-1)).getRGB());
         }

         if (this.animation < 0.8F) {
            FontRenderers.sf_medium_mini.drawString(matrixStack, String.valueOf(this.setting.getMax()).makeConcatWithConstants<invokedynamic>(String.valueOf(this.setting.getMax())), (double)(this.x + this.width - FontRenderers.sf_medium_mini.getStringWidth(String.valueOf(this.setting.getMax()).makeConcatWithConstants<invokedynamic>(String.valueOf(this.setting.getMax()))) - 6.0F), (double)(this.y + 4.0F), (new Color(-1)).getRGB());
         }

         FontRenderers.sf_medium_mini.drawString(matrixStack, this.listening ? (Objects.equals(this.Stringnumber, "") ? "..." : this.Stringnumber) : String.valueOf(this.setting.getValue()).makeConcatWithConstants<invokedynamic>(String.valueOf(this.setting.getValue())), this.animation > 0.2F ? (this.animation < 0.8F ? (double)(this.x + 6.0F + (this.width - 14.0F) * this.animation - FontRenderers.sf_medium_mini.getStringWidth(String.valueOf(this.setting.getValue()).makeConcatWithConstants<invokedynamic>(String.valueOf(this.setting.getValue()))) / 2.0F) : (double)(this.x + this.width - FontRenderers.sf_medium_mini.getStringWidth(String.valueOf(this.setting.getMax()).makeConcatWithConstants<invokedynamic>(String.valueOf(this.setting.getMax()))) - 6.0F)) : (double)(this.x + 6.0F), (double)(this.y + 4.0F), (new Color(-1)).getRGB());
      }

      Render2DEngine.drawRect(matrixStack, this.x + 6.0F, this.y + this.height - 6.0F, this.width - 12.0F, 2.0F, new Color(687865855, true));
      Render2DEngine.draw2DGradientRect(matrixStack, this.x + 6.0F, this.y + this.height - 6.0F, this.x + 6.0F + (this.width - 12.0F) * this.animation, this.y + this.height - 4.0F, HudEditor.getColor(180), HudEditor.getColor(180), HudEditor.getColor(0), HudEditor.getColor(0));
      Render2DEngine.drawRect(matrixStack, this.x + 6.0F + (this.width - 14.0F) * this.animation, this.y + this.height - 7.5F, 2.0F, 5.0F, new Color(-1973791));
      this.animation = MathUtility.clamp(this.animation, 0.0F, 1.0F);
      if (this.dragging) {
         this.setValue(mouseX, (double)(this.x + 7.0F), (double)(this.width - 14.0F));
      }

      if (Render2DEngine.isHovered((double)mouseX, (double)mouseY, (double)(this.x + 6.0F), (double)(this.y + this.height - 7.0F), (double)(this.width - 12.0F), 3.0D)) {
         if (GLFW.glfwGetPlatform() != 393219) {
            GLFW.glfwSetCursor(IManager.mc.method_22683().method_4490(), GLFW.glfwCreateStandardCursor(221189));
         }

         ClickGUI.anyHovered = true;
      }

   }

   private void setValue(int mouseX, double x, double width) {
      float value = Render2DEngine.interpolateFloat(((Number)this.setting.getMin()).floatValue(), ((Number)this.setting.getMax()).floatValue(), class_3532.method_15350(((double)((float)mouseX) - x) / width, 0.0D, 1.0D));
      if (this.setting.getValue() instanceof Float) {
         this.setting.setValue(MathUtility.round2((double)value));
      } else if (this.setting.getValue() instanceof Integer) {
         this.setting.setValue((int)value);
      }

      if (value != this.prevValue) {
         Managers.SOUND.playSlider();
      }

      this.prevValue = value;
   }

   public void mouseClicked(int mouseX, int mouseY, int button) {
      if (button == 0 && this.hovered) {
         this.dragging = true;
      } else if (this.hovered) {
         this.Stringnumber = "";
         this.listening = true;
      }

      if (this.listening) {
         TsunamiClient.currentKeyListener = TsunamiClient.KeyListening.Sliders;
      }

      super.mouseClicked(mouseX, mouseY, button);
   }

   public void mouseReleased(int mouseX, int mouseY, int button) {
      this.dragging = false;
   }

   public void keyTyped(int keyCode) {
      if (TsunamiClient.currentKeyListener == TsunamiClient.KeyListening.Sliders) {
         if (this.listening) {
            switch(keyCode) {
            case 256:
               this.listening = false;
               this.Stringnumber = "";
               return;
            case 257:
               try {
                  this.searchNumber();
               } catch (Exception var3) {
                  this.Stringnumber = "";
                  this.listening = false;
               }

               return;
            case 258:
            case 260:
            default:
               break;
            case 259:
               this.Stringnumber = removeLastChar(this.Stringnumber);
               return;
            case 261:
               this.Stringnumber = "";
               this.listening = false;
               return;
            }
         }

      }
   }

   public void charTyped(char key, int keyCode) {
      if (class_3544.method_57175(key)) {
         String k = key == '-' ? "-" : ".";

         try {
            k = String.valueOf(Integer.parseInt(String.valueOf(key)));
         } catch (Exception var5) {
         }

         this.Stringnumber = this.Stringnumber + k;
      }

   }

   public static String removeLastChar(String str) {
      String output = "";
      if (str != null && !str.isEmpty()) {
         output = str.substring(0, str.length() - 1);
      }

      return output;
   }

   private void searchNumber() {
      if (this.setting.getValue() instanceof Float) {
         this.setting.setValue(Float.valueOf(this.Stringnumber));
         this.Stringnumber = "";
         this.listening = false;
      } else if (this.setting.getValue() instanceof Integer) {
         this.setting.setValue(Integer.valueOf(this.Stringnumber));
         this.Stringnumber = "";
         this.listening = false;
      }

   }
}
