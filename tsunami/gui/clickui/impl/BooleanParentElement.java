package tsunami.gui.clickui.impl;

import java.awt.Color;
import net.minecraft.class_332;
import net.minecraft.class_4587;
import net.minecraft.class_7833;
import org.lwjgl.glfw.GLFW;
import tsunami.core.Managers;
import tsunami.core.manager.IManager;
import tsunami.features.modules.client.HudEditor;
import tsunami.gui.clickui.AbstractElement;
import tsunami.gui.clickui.ClickGUI;
import tsunami.gui.font.FontRenderers;
import tsunami.setting.Setting;
import tsunami.setting.impl.BooleanSettingGroup;
import tsunami.utility.render.Render2DEngine;
import tsunami.utility.render.TextureStorage;
import tsunami.utility.render.animation.AnimationUtility;

public class BooleanParentElement extends AbstractElement {
   private final Setting<BooleanSettingGroup> parentSetting;
   float animation;
   float arrowAnimation;

   public BooleanParentElement(Setting setting) {
      super(setting);
      this.parentSetting = setting;
   }

   public void render(class_332 context, int mouseX, int mouseY, float delta) {
      super.render(context, mouseX, mouseY, delta);
      class_4587 matrixStack = context.method_51448();
      float tx = this.x + this.width - 11.0F;
      float ty = this.y + 7.5F;
      this.arrowAnimation = AnimationUtility.fast(this.arrowAnimation, ((BooleanSettingGroup)this.getParentSetting().getValue()).isExtended() ? 0.0F : 1.0F, 15.0F);
      matrixStack.method_22903();
      matrixStack.method_46416(tx, ty, 0.0F);
      matrixStack.method_22907(class_7833.field_40718.rotationDegrees(-180.0F * this.arrowAnimation));
      matrixStack.method_46416(-tx, -ty, 0.0F);
      matrixStack.method_46416(this.x + this.width - 14.0F, this.y + 4.5F, 0.0F);
      context.method_25290(TextureStorage.guiArrow, 0, 0, 0.0F, 0.0F, 6, 6, 6, 6);
      matrixStack.method_46416(-(this.x + this.width - 14.0F), -(this.y + 4.5F), 0.0F);
      matrixStack.method_22909();
      FontRenderers.sf_medium_mini.drawString(matrixStack, this.setting.getName(), (double)(this.x + 6.0F), (double)(this.y + this.height / 2.0F - 1.0F), (new Color(-1)).getRGB());
      this.animation = AnimationUtility.fast(this.animation, ((BooleanSettingGroup)this.getParentSetting().getValue()).isEnabled() ? 1.0F : 0.0F, 15.0F);
      float paddingX = 7.0F * this.animation;
      Color color = HudEditor.getColor(0);
      Render2DEngine.drawRound(context.method_51448(), this.x + this.width - 36.0F, this.y + this.height / 2.0F - 4.0F, 15.0F, 8.0F, 1.0F, paddingX > 4.0F ? color : new Color(687865855, true));
      Render2DEngine.drawRound(context.method_51448(), this.x + this.width - 35.0F + paddingX, this.y + this.height / 2.0F - 3.0F, 6.0F, 6.0F, 1.0F, new Color(-1));
      if (7.0F * this.animation > 4.0F) {
         FontRenderers.sf_bold_mini.drawString(context.method_51448(), "v", (double)(this.x + this.width - 34.0F), (double)(this.y + this.height / 2.0F - 2.0F), (new Color(-1)).getRGB());
      } else {
         FontRenderers.sf_bold_mini.drawString(context.method_51448(), "x", (double)(this.x + this.width - 27.0F), (double)(this.y + this.height / 2.0F - 2.0F), (new Color(-1)).getRGB());
      }

      if (Render2DEngine.isHovered((double)mouseX, (double)mouseY, (double)(this.x + this.width - 36.0F), (double)(this.y + this.height / 2.0F - 4.0F), 15.0D, 8.0D)) {
         if (GLFW.glfwGetPlatform() != 393219) {
            GLFW.glfwSetCursor(IManager.mc.method_22683().method_4490(), GLFW.glfwCreateStandardCursor(221188));
         }

         ClickGUI.anyHovered = true;
      }

   }

   public void mouseClicked(int mouseX, int mouseY, int button) {
      if (this.hovered) {
         if (button == 0) {
            ((BooleanSettingGroup)this.getParentSetting().getValue()).setEnabled(!((BooleanSettingGroup)this.getParentSetting().getValue()).isEnabled());
            Managers.SOUND.playBoolean();
         } else {
            ((BooleanSettingGroup)this.getParentSetting().getValue()).setExtended(!((BooleanSettingGroup)this.getParentSetting().getValue()).isExtended());
            if (((BooleanSettingGroup)this.getParentSetting().getValue()).isExtended()) {
               Managers.SOUND.playSwipeIn();
            } else {
               Managers.SOUND.playSwipeOut();
            }
         }
      }

      super.mouseClicked(mouseX, mouseY, button);
   }

   public Setting<BooleanSettingGroup> getParentSetting() {
      return this.parentSetting;
   }
}
