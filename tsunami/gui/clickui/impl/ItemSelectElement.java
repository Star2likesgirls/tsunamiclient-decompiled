package tsunami.gui.clickui.impl;

import java.awt.Color;
import net.minecraft.class_332;
import net.minecraft.class_4587;
import tsunami.core.Managers;
import tsunami.core.manager.IManager;
import tsunami.gui.clickui.AbstractElement;
import tsunami.gui.font.FontRenderers;
import tsunami.gui.windows.WindowBase;
import tsunami.gui.windows.WindowsScreen;
import tsunami.gui.windows.impl.ItemSelectWindow;
import tsunami.setting.Setting;
import tsunami.setting.impl.ItemSelectSetting;

public class ItemSelectElement extends AbstractElement {
   private final Setting<ItemSelectSetting> setting;

   public ItemSelectElement(Setting setting) {
      super(setting);
      this.setting = setting;
   }

   public void render(class_332 context, int mouseX, int mouseY, float delta) {
      super.render(context, mouseX, mouseY, delta);
      class_4587 matrixStack = context.method_51448();
      FontRenderers.icons.drawString(matrixStack, "H", (double)(this.x + this.width - 14.0F), (double)(this.y + 6.0F), (new Color(-1250068, true)).getRGB());
      FontRenderers.sf_medium_mini.drawString(matrixStack, this.setting.getName(), (double)(this.x + 6.0F), (double)(this.y + this.height / 2.0F - 1.0F), (new Color(-1)).getRGB());
   }

   public void mouseClicked(int mouseX, int mouseY, int button) {
      if (this.hovered) {
         IManager.mc.method_1507(new WindowsScreen(new WindowBase[]{new ItemSelectWindow(this.getItemSetting())}));
         Managers.SOUND.playSwipeIn();
      }

      super.mouseClicked(mouseX, mouseY, button);
   }

   public Setting<ItemSelectSetting> getItemSetting() {
      return this.setting;
   }
}
