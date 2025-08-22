package tsunami.gui.hud;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import net.minecraft.class_2561;
import net.minecraft.class_332;
import net.minecraft.class_3675;
import net.minecraft.class_437;
import tsunami.TsunamiClient;
import tsunami.core.Managers;
import tsunami.core.manager.client.ModuleManager;
import tsunami.features.hud.HudElement;
import tsunami.features.modules.Module;
import tsunami.features.modules.client.ClickGui;
import tsunami.gui.clickui.AbstractCategory;
import tsunami.gui.clickui.Category;
import tsunami.gui.clickui.ClickGUI;

public class HudEditorGui extends class_437 {
   public static HudElement currentlyDragging;
   private final List<AbstractCategory> windows = Lists.newArrayList();
   private static HudEditorGui instance = new HudEditorGui();
   private boolean firstOpen = true;
   private double dWheel;

   public HudEditorGui() {
      super(class_2561.method_30163("HudEditorGui"));
      this.setInstance();
   }

   protected void method_25426() {
      if (this.firstOpen) {
         Category window = new Category(Module.Category.HUD, Managers.MODULE.getModulesByCategory(Module.Category.HUD), (float)Module.mc.method_22683().method_4486() / 2.0F - 50.0F, 20.0F, 100.0F, 18.0F);
         window.setOpen(true);
         this.windows.add(window);
         this.firstOpen = false;
      }

      this.windows.forEach(AbstractCategory::init);
   }

   public boolean method_25421() {
      return false;
   }

   public void method_25394(class_332 context, int mouseX, int mouseY, float delta) {
      ClickGUI.anyHovered = false;
      Iterator var5;
      AbstractCategory window;
      if (ModuleManager.clickGui.scrollMode.getValue() == ClickGui.scrollModeEn.Old) {
         var5 = this.windows.iterator();

         while(var5.hasNext()) {
            window = (AbstractCategory)var5.next();
            if (class_3675.method_15987(Module.mc.method_22683().method_4490(), 264)) {
               window.setY(window.getY() + 2.0F);
            }

            if (class_3675.method_15987(Module.mc.method_22683().method_4490(), 265)) {
               window.setY(window.getY() - 2.0F);
            }

            if (class_3675.method_15987(Module.mc.method_22683().method_4490(), 262)) {
               window.setX(window.getX() + 2.0F);
            }

            if (class_3675.method_15987(Module.mc.method_22683().method_4490(), 263)) {
               window.setX(window.getX() - 2.0F);
            }

            if (this.dWheel != 0.0D) {
               window.setY((float)((double)window.getY() + this.dWheel));
            }
         }
      } else {
         var5 = this.windows.iterator();

         while(var5.hasNext()) {
            window = (AbstractCategory)var5.next();
            if (this.dWheel != 0.0D) {
               window.setModuleOffset((float)this.dWheel, (float)mouseX, (float)mouseY);
            }
         }
      }

      this.dWheel = 0.0D;
      var5 = this.windows.iterator();

      while(var5.hasNext()) {
         window = (AbstractCategory)var5.next();
         window.render(context, mouseX, mouseY, delta);
      }

   }

   public boolean method_25401(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
      this.dWheel = (double)((int)(verticalAmount * 5.0D));
      return super.method_25401(mouseX, mouseY, horizontalAmount, verticalAmount);
   }

   public boolean method_25402(double mouseX, double mouseY, int button) {
      this.windows.forEach((w) -> {
         w.mouseClicked((int)mouseX, (int)mouseY, button);
         this.windows.forEach((w1) -> {
            if (w.dragging && w != w1) {
               w1.dragging = false;
            }

         });
      });
      return super.method_25402(mouseX, mouseY, button);
   }

   public boolean method_25406(double mouseX, double mouseY, int button) {
      this.windows.forEach((w) -> {
         w.mouseReleased((int)mouseX, (int)mouseY, button);
      });
      return super.method_25406(mouseX, mouseY, button);
   }

   public boolean method_25404(int keyCode, int scanCode, int modifiers) {
      this.windows.forEach((w) -> {
         w.keyTyped(keyCode);
      });
      if (keyCode == 256) {
         super.method_25404(keyCode, scanCode, modifiers);
         return true;
      } else {
         return false;
      }
   }

   public void method_25432() {
      TsunamiClient.EVENT_BUS.unsubscribe((Object)this);
   }

   public void hudClicked(Module module) {
      Iterator var2 = this.windows.iterator();

      while(var2.hasNext()) {
         AbstractCategory window = (AbstractCategory)var2.next();
         window.hudClicked(module);
      }

   }

   public static HudEditorGui getInstance() {
      if (instance == null) {
         instance = new HudEditorGui();
      }

      return instance;
   }

   public static HudEditorGui getHudGui() {
      return getInstance();
   }

   private void setInstance() {
      instance = this;
   }
}
