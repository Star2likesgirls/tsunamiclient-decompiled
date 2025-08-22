package tsunami.gui.thundergui.components;

import java.awt.Color;
import java.util.Objects;
import net.minecraft.class_1074;
import net.minecraft.class_4587;
import net.minecraft.class_7833;
import tsunami.features.cmd.Command;
import tsunami.features.modules.Module;
import tsunami.features.modules.client.ThunderHackGui;
import tsunami.gui.font.FontRenderers;
import tsunami.gui.thundergui.ThunderGui;
import tsunami.setting.impl.Bind;
import tsunami.setting.impl.ColorSetting;
import tsunami.utility.render.Render2DEngine;
import tsunami.utility.render.animation.AnimationUtility;

public class ModulePlate {
   float scroll_animation = 0.0F;
   private final Module module;
   private int posX;
   private int posY;
   private float scrollPosY;
   private float prevPosY;
   private int progress;
   private int fade;
   private final int index;
   private boolean first_open = true;
   private boolean listening_bind = false;
   private boolean holdbind = false;

   public ModulePlate(Module module, int posX, int posY, int index) {
      this.module = module;
      this.posX = posX;
      this.posY = posY;
      this.fade = 0;
      this.index = index * 5;
      this.scrollPosY = (float)posY;
      this.scroll_animation = 0.0F;
   }

   public void render(class_4587 stack, int MouseX, int MouseY) {
      if (this.scrollPosY != (float)this.posY) {
         this.scroll_animation = AnimationUtility.fast(this.scroll_animation, 1.0F, 15.0F);
         this.posY = (int)Render2DEngine.interpolate((double)this.prevPosY, (double)this.scrollPosY, (double)this.scroll_animation);
      }

      if (this.posY <= ThunderGui.getInstance().main_posY + ThunderGui.getInstance().field_22790 && this.posY >= ThunderGui.getInstance().main_posY) {
         Render2DEngine.addWindow(stack, new Render2DEngine.Rectangle((float)(this.posX + 1), (float)(this.posY + 1), (float)(this.posX + 90), (float)(this.posY + 30)));
         if (this.module.isOn()) {
            Render2DEngine.drawGradientRound(stack, (float)(this.posX + 1), (float)this.posY, 89.0F, 30.0F, 4.0F, Render2DEngine.applyOpacity(((ColorSetting)ThunderHackGui.onColor1.getValue()).getColorObject(), this.getFadeFactor()), Render2DEngine.applyOpacity(((ColorSetting)ThunderHackGui.onColor1.getValue()).getColorObject(), this.getFadeFactor()), Render2DEngine.applyOpacity(((ColorSetting)ThunderHackGui.onColor2.getValue()).getColorObject(), this.getFadeFactor()), Render2DEngine.applyOpacity(((ColorSetting)ThunderHackGui.onColor2.getValue()).getColorObject(), this.getFadeFactor()));
         } else {
            Render2DEngine.drawRound(stack, (float)(this.posX + 1), (float)this.posY, 89.0F, 30.0F, 4.0F, Render2DEngine.applyOpacity(new Color(25, 20, 30, 255), this.getFadeFactor()));
         }

         if (this.first_open) {
            Render2DEngine.drawBlurredShadow(stack, (float)(MouseX - 20), (float)(MouseY - 20), 40.0F, 40.0F, 60, Render2DEngine.applyOpacity(new Color(-1017816450, true), this.getFadeFactor()));
            this.first_open = false;
         }

         if (this.isHovered(MouseX, MouseY)) {
            Render2DEngine.drawBlurredShadow(stack, (float)(MouseX - 20), (float)(MouseY - 20), 40.0F, 40.0F, 60, Render2DEngine.applyOpacity(new Color(-1017816450, true), this.getFadeFactor()));
         }

         if (ThunderGui.selected_plate != this) {
            FontRenderers.icons.drawString(stack, "H", (double)((int)((float)this.posX + 80.0F)), (double)((int)((float)this.posY + 22.0F)), Render2DEngine.applyOpacity((new Color(-1250068, true)).getRGB(), this.getFadeFactor()));
         } else {
            stack.method_22903();
            stack.method_46416((float)this.posX + 91.0F, (float)this.posY + 15.0F, 0.0F);
            stack.method_22907(class_7833.field_40718.rotationDegrees((float)(Module.mc.field_1724.field_6012 * 4)));
            stack.method_46416(-((float)this.posX + 91.0F), -((float)this.posY + 15.0F), 0.0F);
            FontRenderers.big_icons.drawString(stack, "H", (double)((float)this.posX + 78.0F), (double)((float)this.posY + 5.0F), Render2DEngine.applyOpacity((new Color(-10197916, true)).getRGB(), this.getFadeFactor()));
            stack.method_46416((float)this.posX + 91.0F, (float)this.posY + 15.0F, 0.0F);
            stack.method_22907(class_7833.field_40718.rotationDegrees((float)(-Module.mc.field_1724.field_6012 * 4)));
            stack.method_46416(-((float)this.posX + 91.0F), -((float)this.posY + 15.0F), 0.0F);
            stack.method_22909();
         }

         if (!this.listening_bind) {
            FontRenderers.sf_medium.drawString(stack, this.module.getName(), (double)(this.posX + 5), (double)(this.posY + 5), Render2DEngine.applyOpacity(-1, this.getFadeFactor()));
         }

         if (this.listening_bind) {
            FontRenderers.modules.drawString(stack, "PressKey", (double)((float)(this.posX + 85) - FontRenderers.modules.getStringWidth("PressKey")), (double)(this.posY + 5), Render2DEngine.applyOpacity(new Color(11579568), this.getFadeFactor()).getRGB());
         } else if (!Objects.equals(this.module.getBind().getBind(), "None")) {
            String sbind = this.module.getBind().getBind();
            if (sbind.equals("LEFT_CONTROL")) {
               sbind = "LCtrl";
            }

            if (sbind.equals("RIGHT_CONTROL")) {
               sbind = "RCtrl";
            }

            if (sbind.equals("LEFT_SHIFT")) {
               sbind = "LShift";
            }

            if (sbind.equals("RIGHT_SHIFT")) {
               sbind = "RShift";
            }

            if (sbind.equals("LEFT_ALT")) {
               sbind = "LAlt";
            }

            if (sbind.equals("RIGHT_ALT")) {
               sbind = "RAlt";
            }

            FontRenderers.modules.drawString(stack, sbind, (double)((float)(this.posX + 86) - FontRenderers.modules.getStringWidth(sbind)), (double)(this.posY + 6), Render2DEngine.applyOpacity(new Color(11579568), this.getFadeFactor()).getRGB());
         }

         if (!this.listening_bind && this.module.getDescription() != null) {
            int step = 0;
            StringBuilder firstString = new StringBuilder();
            String[] var6 = class_1074.method_4662(this.module.getDescription(), new Object[0]).split(" ");
            int var7 = var6.length;

            for(int var8 = 0; var8 < var7; ++var8) {
               String word = var6[var8];
               firstString.append(word + " ");
               String[] splitString2 = firstString.toString().split("\n");
               if (FontRenderers.sf_medium_mini.getStringWidth(splitString2[step]) > 70.0F) {
                  firstString.append("\n");
                  ++step;
               }
            }

            FontRenderers.sf_medium_mini.drawString(stack, firstString.toString(), (double)(this.posX + 5), (double)(this.posY + 14), Render2DEngine.applyOpacity((new Color(-4342339, true)).getRGB(), this.getFadeFactor()));
         }

         if (this.listening_bind) {
            Render2DEngine.drawRound(stack, (float)(this.posX + 5), (float)(this.posY + 5), 40.0F, 20.0F, 3.0F, Color.BLACK);
            if (!this.holdbind) {
               Render2DEngine.drawRound(stack, (float)(this.posX + 6), (float)(this.posY + 6), 38.0F, 8.0F, 2.0F, Render2DEngine.injectAlpha(((ColorSetting)ThunderHackGui.onColor1.getValue()).getColorObject(), 170));
               FontRenderers.settings.drawCenteredString(stack, "Toggle", (double)(this.posX + 25), (double)(this.posY + 7), -1);
               FontRenderers.settings.drawCenteredString(stack, "Hold", (double)(this.posX + 25), (double)(this.posY + 17), (new Color(-1459617793, true)).getRGB());
            } else {
               Render2DEngine.drawRound(stack, (float)(this.posX + 6), (float)(this.posY + 16), 38.0F, 8.0F, 2.0F, Render2DEngine.injectAlpha(((ColorSetting)ThunderHackGui.onColor1.getValue()).getColorObject(), 170));
               FontRenderers.settings.drawCenteredString(stack, "Hold", (double)(this.posX + 25), (double)(this.posY + 17), -1);
               FontRenderers.settings.drawCenteredString(stack, "Toggle", (double)(this.posX + 25), (double)(this.posY + 7), (new Color(-1459617793, true)).getRGB());
            }
         }

         Render2DEngine.popWindow();
      }
   }

   private float getFadeFactor() {
      return (float)this.fade / (5.0F + (float)this.index);
   }

   public void onTick() {
      if (this.progress > 4) {
         this.progress = 0;
      }

      ++this.progress;
      if (this.fade < 10 + this.index) {
         ++this.fade;
      }

   }

   private boolean isHovered(int mouseX, int mouseY) {
      return mouseX > this.posX && mouseX < this.posX + 90 && mouseY > this.posY && mouseY < this.posY + 30;
   }

   public void movePosition(float deltaX, float deltaY) {
      this.posY = (int)((float)this.posY + deltaY);
      this.posX = (int)((float)this.posX + deltaX);
      this.scrollPosY = (float)this.posY;
   }

   public void scrollElement(float deltaY) {
      this.scroll_animation = 0.0F;
      this.prevPosY = (float)this.posY;
      this.scrollPosY += deltaY;
   }

   public void mouseClicked(int mouseX, int mouseY, int clickedButton) {
      if (this.posY <= ThunderGui.getInstance().main_posY + ThunderGui.getInstance().field_22790 && this.posY >= ThunderGui.getInstance().main_posY) {
         if (this.listening_bind) {
            if (mouseX > this.posX + 6 && mouseX < this.posX + 44 && mouseY > this.posY + 6 && mouseY < this.posY + 14) {
               this.holdbind = false;
               this.module.getBind().setHold(false);
               return;
            }

            if (mouseX > this.posX + 6 && mouseX < this.posX + 44 && mouseY > this.posY + 16 && mouseY < this.posY + 24) {
               this.holdbind = true;
               this.module.getBind().setHold(true);
               return;
            }

            this.module.setBind(clickedButton, true, this.holdbind);
            String var10000 = this.module.getName();
            Command.sendMessage(var10000 + " бинд изменен на " + this.module.getBind().getBind());
            this.listening_bind = false;
         }

         if (mouseX > this.posX && mouseX < this.posX + 90 && mouseY > this.posY && mouseY < this.posY + 30) {
            switch(clickedButton) {
            case 0:
               this.module.toggle();
               break;
            case 1:
               ThunderGui.selected_plate = this;
               break;
            case 2:
               this.listening_bind = !this.listening_bind;
            }
         }

      }
   }

   public void keyTyped(String typedChar, int keyCode) {
      if (this.listening_bind) {
         Bind bind = new Bind(keyCode, false, this.holdbind);
         if (bind.getBind().equalsIgnoreCase("Escape")) {
            return;
         }

         if (bind.getBind().equalsIgnoreCase("Delete")) {
            bind = new Bind(-1, false, this.holdbind);
         }

         this.module.setBind(bind);
         this.listening_bind = false;
      }

   }

   public double getPosX() {
      return (double)this.posX;
   }

   public double getPosY() {
      return (double)this.posY;
   }

   public Module getModule() {
      return this.module;
   }
}
