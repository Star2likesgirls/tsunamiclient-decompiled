package tsunami.gui.clickui.impl;

import java.awt.Color;
import net.minecraft.class_332;
import net.minecraft.class_4587;
import net.minecraft.class_7833;
import tsunami.core.Managers;
import tsunami.gui.clickui.AbstractElement;
import tsunami.gui.font.FontRenderers;
import tsunami.setting.Setting;
import tsunami.setting.impl.SettingGroup;
import tsunami.utility.render.TextureStorage;
import tsunami.utility.render.animation.AnimationUtility;

public class ParentElement extends AbstractElement {
   private final Setting<SettingGroup> parentSetting;
   private float animation;

   public ParentElement(Setting setting) {
      super(setting);
      this.parentSetting = setting;
   }

   public void render(class_332 context, int mouseX, int mouseY, float delta) {
      super.render(context, mouseX, mouseY, delta);
      class_4587 matrixStack = context.method_51448();
      float tx = this.x + this.width - 11.0F;
      float ty = this.y + 7.5F;
      this.animation = AnimationUtility.fast(this.animation, ((SettingGroup)this.getParentSetting().getValue()).isExtended() ? 0.0F : 1.0F, 15.0F);
      matrixStack.method_22903();
      matrixStack.method_46416(tx, ty, 0.0F);
      matrixStack.method_22907(class_7833.field_40718.rotationDegrees(-180.0F * this.animation));
      matrixStack.method_46416(-tx, -ty, 0.0F);
      matrixStack.method_46416(this.x + this.width - 14.0F, this.y + 4.5F, 0.0F);
      context.method_25290(TextureStorage.guiArrow, 0, 0, 0.0F, 0.0F, 6, 6, 6, 6);
      matrixStack.method_46416(-(this.x + this.width - 14.0F), -(this.y + 4.5F), 0.0F);
      matrixStack.method_22909();
      FontRenderers.sf_medium_mini.drawString(matrixStack, this.setting.getName(), (double)(this.x + 6.0F + (float)(6 * ((SettingGroup)this.getParentSetting().getValue()).getHierarchy())), (double)(this.y + this.height / 2.0F - 1.0F), (new Color(-1)).getRGB());
   }

   public void mouseClicked(int mouseX, int mouseY, int button) {
      if (this.hovered) {
         ((SettingGroup)this.getParentSetting().getValue()).setExtended(!((SettingGroup)this.getParentSetting().getValue()).isExtended());
         if (((SettingGroup)this.getParentSetting().getValue()).isExtended()) {
            Managers.SOUND.playSwipeIn();
         } else {
            Managers.SOUND.playSwipeOut();
         }
      }

      super.mouseClicked(mouseX, mouseY, button);
   }

   public Setting<SettingGroup> getParentSetting() {
      return this.parentSetting;
   }
}
