package tsunami.gui.thundergui.components;

import java.awt.Color;
import net.minecraft.class_4587;
import tsunami.gui.font.FontRenderers;
import tsunami.gui.thundergui.ThunderGui;
import tsunami.setting.Setting;
import tsunami.setting.impl.SettingGroup;
import tsunami.utility.render.Render2DEngine;

public class ParentComponent extends SettingElement {
   public ParentComponent(Setting setting) {
      super(setting);
      SettingGroup settingGroup = (SettingGroup)setting.getValue();
      settingGroup.setExtended(true);
   }

   public void render(class_4587 stack, int mouseX, int mouseY, float partialTicks) {
      super.render(stack, mouseX, mouseY, partialTicks);
      if (!(this.getY() > (float)(ThunderGui.getInstance().main_posY + ThunderGui.getInstance().field_22790)) && !(this.getY() < (float)ThunderGui.getInstance().main_posY)) {
         FontRenderers.modules.drawCenteredString(stack, this.getSetting().getName(), (double)(this.getX() + this.width / 2.0F), (double)(this.getY() + 2.0F), (new Color(-1325400065, true)).getRGB());
         Render2DEngine.draw2DGradientRect(stack, this.getX() + 10.0F, this.getY() + 6.0F, this.getX() + this.width / 2.0F - 20.0F, this.getY() + 7.0F, new Color(16777215, true), new Color(16777215, true), new Color(-1325400065, true), new Color(-1325400065, true));
         Render2DEngine.draw2DGradientRect(stack, this.getX() + this.width / 2.0F + 20.0F, this.getY() + 6.0F, this.getX() + this.width - 10.0F, this.getY() + 7.0F, new Color(-1325400065, true), new Color(-1325400065, true), new Color(16777215, true), new Color(16777215, true));
      }
   }

   public void mouseClicked(int mouseX, int mouseY, int button) {
      if (!(this.getY() > (float)(ThunderGui.getInstance().main_posY + ThunderGui.getInstance().field_22790)) && !(this.getY() < (float)ThunderGui.getInstance().main_posY)) {
         if (this.hovered) {
            SettingGroup settingGroup = (SettingGroup)this.setting.getValue();
            settingGroup.setExtended(!settingGroup.isExtended());
         }

      }
   }
}
