package tsunami.gui.thundergui.components;

import java.awt.Color;
import net.minecraft.class_4587;
import tsunami.features.modules.client.HudEditor;
import tsunami.gui.font.FontRenderers;
import tsunami.gui.thundergui.ThunderGui;
import tsunami.setting.Setting;
import tsunami.setting.impl.BooleanSettingGroup;
import tsunami.utility.render.Render2DEngine;
import tsunami.utility.render.animation.AnimationUtility;

public class BooleanParentComponent extends SettingElement {
   float animation = 0.0F;
   private final Setting<BooleanSettingGroup> parentSetting;

   public BooleanParentComponent(Setting setting) {
      super(setting);
      this.parentSetting = setting;
   }

   public void render(class_4587 stack, int mouseX, int mouseY, float partialTicks) {
      super.render(stack, mouseX, mouseY, partialTicks);
      if (!(this.getY() > (float)(ThunderGui.getInstance().main_posY + ThunderGui.getInstance().field_22790)) && !(this.getY() < (float)ThunderGui.getInstance().main_posY)) {
         FontRenderers.modules.drawString(stack, this.getSetting().getName(), (double)this.getX(), (double)(this.getY() + 5.0F), this.isHovered() ? -1 : (new Color(-1325400065, true)).getRGB());
         this.animation = AnimationUtility.fast(this.animation, ((BooleanSettingGroup)this.getParentSetting().getValue()).isEnabled() ? 1.0F : 0.0F, 15.0F);
         double paddingX = (double)(7.0F * this.animation);
         Color color = HudEditor.getColor(1);
         Render2DEngine.drawRound(stack, this.x + this.width - 18.0F, this.y + this.height / 2.0F - 4.0F, 15.0F, 8.0F, 4.0F, paddingX > 4.0D ? color : new Color(-5066319));
         Render2DEngine.drawRound(stack, (float)((double)(this.x + this.width - 17.0F) + paddingX), this.y + this.height / 2.0F - 3.0F, 6.0F, 6.0F, 3.0F, new Color(-1));
      }
   }

   public void mouseClicked(int mouseX, int mouseY, int button) {
      if (!(this.getY() > (float)(ThunderGui.getInstance().main_posY + ThunderGui.getInstance().field_22790)) && !(this.getY() < (float)ThunderGui.getInstance().main_posY)) {
         if (this.isHovered()) {
            ((BooleanSettingGroup)this.getParentSetting().getValue()).setEnabled(!((BooleanSettingGroup)this.getParentSetting().getValue()).isEnabled());
         }

      }
   }

   public Setting<BooleanSettingGroup> getParentSetting() {
      return this.parentSetting;
   }
}
