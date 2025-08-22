package tsunami.features.hud;

import java.awt.Color;
import java.util.Iterator;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_332;
import net.minecraft.class_408;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;
import tsunami.core.Managers;
import tsunami.events.impl.EventMouse;
import tsunami.features.modules.Module;
import tsunami.features.modules.client.HudEditor;
import tsunami.gui.hud.HudEditorGui;
import tsunami.setting.Setting;
import tsunami.setting.impl.PositionSetting;
import tsunami.utility.render.Render2DEngine;
import tsunami.utility.render.Render3DEngine;

public class HudElement extends Module {
   private final Setting<PositionSetting> pos = new Setting("Position", new PositionSetting(0.5F, 0.5F));
   private boolean mouseState = false;
   private boolean mouseButton = false;
   private float x;
   private float y;
   private float dragX;
   private float dragY;
   private float hitX;
   private float hitY;
   private float height;
   private float width;
   public static boolean anyHovered = false;

   public HudElement(String name, int width, int height) {
      super(name, Module.Category.HUD);
      this.height = (float)height;
      this.width = (float)width;
   }

   public void onRender2D(class_332 context) {
      this.y = (float)mc.method_22683().method_4502() * ((PositionSetting)this.pos.getValue()).getY();
      this.x = (float)mc.method_22683().method_4486() * ((PositionSetting)this.pos.getValue()).getX();
      if ((mc.field_1755 instanceof class_408 || mc.field_1755 instanceof HudEditorGui) && this.mouseButton && this.mouseState) {
         ((PositionSetting)this.pos.getValue()).setX(Math.clamp(Render2DEngine.scrollAnimate(((float)this.normaliseX() - this.dragX) / (float)mc.method_22683().method_4486(), ((PositionSetting)this.pos.getValue()).getX(), 0.1F), 0.0F, 1.0F));
         ((PositionSetting)this.pos.getValue()).setY(Math.clamp(Render2DEngine.scrollAnimate(((float)this.normaliseY() - this.dragY) / (float)mc.method_22683().method_4502(), ((PositionSetting)this.pos.getValue()).getY(), 0.1F), 0.0F, 1.0F - this.height / (float)mc.method_22683().method_4502()));
         float finalX = 0.0F;
         float finalY = 0.0F;
         if ((Boolean)HudEditor.sticky.getValue()) {
            Iterator var5 = Managers.MODULE.getEnabledModules().iterator();

            label101:
            while(true) {
               HudElement hudElement;
               do {
                  do {
                     Module m;
                     do {
                        if (!var5.hasNext()) {
                           break label101;
                        }

                        m = (Module)var5.next();
                     } while(!(m instanceof HudElement));

                     hudElement = (HudElement)m;
                  } while(hudElement == this);
               } while(hudElement.getPosX() == 0.0F && hudElement.getPosY() == 0.0F);

               if (this.getPosX() > (float)mc.method_22683().method_4486() / 2.0F) {
                  if (this.isNear(hudElement.getHitX() + hudElement.getWidth(), this.getHitX() + this.getWidth())) {
                     finalX = hudElement.getHitX() + hudElement.getWidth() - this.getWidth();
                  }
               } else if (this.isNear(hudElement.getHitX(), this.getHitX())) {
                  finalX = hudElement.getHitX();
               }

               if (this.isNear(hudElement.getHitY(), this.getHitY())) {
                  finalY = hudElement.getHitY();
               }
            }
         }

         if (finalX != 0.0F || finalY != 0.0F) {
            Render2DEngine.drawRound(context.method_51448(), finalX == 0.0F ? this.getHitX() : finalX, finalY == 0.0F ? this.getHitY() : finalY, this.width, this.height, 3.0F, new Color(2066689839, true));
         }

         if (finalX != 0.0F) {
            Render2DEngine.drawLine(finalX, 0.0F, finalX, (float)mc.method_22683().method_4502(), -1);
         }

         if (finalY != 0.0F) {
            Render2DEngine.drawLine(0.0F, finalY, (float)mc.method_22683().method_4486(), finalY, -1);
         }
      }

      if (this.mouseButton) {
         if (!this.mouseState && this.isHovering()) {
            this.dragX = (float)((int)((float)this.normaliseX() - ((PositionSetting)this.pos.getValue()).getX() * (float)mc.method_22683().method_4486()));
            this.dragY = (float)((int)((float)this.normaliseY() - ((PositionSetting)this.pos.getValue()).getY() * (float)mc.method_22683().method_4502()));
            this.mouseState = true;
         }
      } else {
         this.mouseState = false;
      }

      if (this.isHovering() && (mc.field_1755 instanceof class_408 || mc.field_1755 instanceof HudEditorGui)) {
         if (GLFW.glfwGetPlatform() != 393219) {
            GLFW.glfwSetCursor(mc.method_22683().method_4490(), this.mouseState ? GLFW.glfwCreateStandardCursor(221187) : GLFW.glfwCreateStandardCursor(221188));
         }

         anyHovered = true;
      }

   }

   @EventHandler
   public void onMouse(@NotNull EventMouse event) {
      if (event.getAction() == 0 && event.getButton() == 1 && this.isHovering() && mc.field_1755 instanceof HudEditorGui) {
         HudEditorGui.getHudGui().hudClicked(this);
      }

      if (event.getAction() == 0) {
         if (this.mouseButton && this.mouseState && (Boolean)HudEditor.sticky.getValue()) {
            Iterator var2 = Managers.MODULE.getEnabledModules().iterator();

            label69:
            while(true) {
               HudElement hudElement;
               do {
                  do {
                     Module m;
                     do {
                        if (!var2.hasNext()) {
                           break label69;
                        }

                        m = (Module)var2.next();
                     } while(!(m instanceof HudElement));

                     hudElement = (HudElement)m;
                  } while(hudElement == this);
               } while(hudElement.getHitX() == 0.0F && hudElement.getHitY() == 0.0F);

               float hitDifX = this.getPosX() - this.getHitX();
               float hitDifY = this.getPosY() - this.getHitY();
               if (this.getPosX() > (float)mc.method_22683().method_4486() / 2.0F) {
                  if (this.isNear(hudElement.getHitX() + hudElement.getWidth(), this.getHitX() + this.getWidth())) {
                     ((PositionSetting)this.pos.getValue()).setX((hudElement.getHitX() + hitDifX + hudElement.getWidth() - this.getWidth()) / (float)mc.method_22683().method_4486());
                  }
               } else if (this.isNear(hudElement.getHitX(), this.getHitX())) {
                  ((PositionSetting)this.pos.getValue()).setX((hudElement.getHitX() + hitDifX) / (float)mc.method_22683().method_4486());
               }

               if (this.isNear(hudElement.getHitY(), this.getHitY())) {
                  ((PositionSetting)this.pos.getValue()).setY((hudElement.getHitY() + hitDifY) / (float)mc.method_22683().method_4502());
               }
            }
         }

         HudEditorGui.currentlyDragging = null;
         this.mouseButton = false;
      }

      if (event.getAction() == 1 && this.isHovering() && HudEditorGui.currentlyDragging == null) {
         HudEditorGui.currentlyDragging = this;
         this.mouseButton = true;
      }

   }

   public int normaliseX() {
      return (int)(mc.field_1729.method_1603() / Render3DEngine.getScaleFactor());
   }

   public int normaliseY() {
      return (int)(mc.field_1729.method_1604() / Render3DEngine.getScaleFactor());
   }

   public boolean isHovering() {
      return (float)this.normaliseX() > Math.min(this.hitX, this.hitX + this.width) && (float)this.normaliseX() < Math.max(this.hitX, this.hitX + this.width) && (float)this.normaliseY() > Math.min(this.hitY, this.hitY + this.height) && (float)this.normaliseY() < Math.max(this.hitY, this.hitY + this.height);
   }

   public void setWidth(float width) {
      this.width = width;
   }

   public void setHeight(float height) {
      this.height = height;
   }

   public void setHitX(float hitX) {
      this.hitX = hitX;
   }

   public void setHitY(float hitY) {
      this.hitY = hitY;
   }

   public void setBounds(float x, float y, float w, float h) {
      this.setHitX(x);
      this.setHitY(y);
      this.setWidth(w);
      this.setHeight(h);
   }

   public float getPosX() {
      return this.x;
   }

   public float getHitX() {
      return this.hitX;
   }

   public float getHitY() {
      return this.hitY;
   }

   public float getPosY() {
      return this.y;
   }

   public float getX() {
      return ((PositionSetting)this.pos.getValue()).x;
   }

   public float getY() {
      return ((PositionSetting)this.pos.getValue()).y;
   }

   public float getHeight() {
      return this.height;
   }

   public float getWidth() {
      return this.width;
   }

   private boolean isNear(float n1, float n2) {
      return Math.abs(n1 - n2) < 10.0F;
   }
}
