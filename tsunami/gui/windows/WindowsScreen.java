package tsunami.gui.windows;

import com.mojang.blaze3d.platform.GlStateManager.class_4534;
import com.mojang.blaze3d.platform.GlStateManager.class_4535;
import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_332;
import net.minecraft.class_437;
import net.minecraft.class_4587;
import tsunami.core.manager.IManager;
import tsunami.features.modules.Module;
import tsunami.features.modules.client.HudEditor;
import tsunami.gui.clickui.ClickGUI;
import tsunami.utility.render.Render2DEngine;
import tsunami.utility.render.TextureStorage;

public class WindowsScreen extends class_437 {
   private List<WindowBase> windows = new ArrayList();
   public static WindowBase lastClickedWindow;
   public static WindowBase draggingWindow;
   private static final class_2960 clickGuiIcon = class_2960.method_60655("thunderhack", "textures/gui/elements/clickgui.png");

   public WindowsScreen(WindowBase... windows) {
      super(class_2561.method_30163("THWindows"));
      this.windows.clear();
      lastClickedWindow = null;
      this.windows = Arrays.stream(windows).toList();
   }

   public void method_25394(class_332 context, int mouseX, int mouseY, float delta) {
      if (Module.fullNullCheck()) {
         this.method_25420(context, mouseX, mouseY, delta);
      }

      class_4587 matrices = context.method_51448();
      int i = IManager.mc.method_22683().method_4486() / 2;
      float offset = (float)this.windows.size() * 20.0F / -2.0F - 23.0F;
      Render2DEngine.drawHudBase(matrices, (float)i + offset - 1.5F, (float)(IManager.mc.method_22683().method_4502() - 25), (float)this.windows.size() * 20.0F + 23.0F, 19.0F, (Float)HudEditor.hudRound.getValue());
      RenderSystem.enableBlend();
      RenderSystem.defaultBlendFunc();
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, Render2DEngine.isHovered((double)mouseX, (double)mouseY, (double)((float)i + offset + 1.0F), (double)(IManager.mc.method_22683().method_4502() - 23), 15.0D, 15.0D) ? 0.95F : 0.7F);
      context.method_25293(clickGuiIcon, (int)((float)i + offset) + 1, IManager.mc.method_22683().method_4502() - 23, 15, 15, 0.0F, 0.0F, 15, 15, 15, 15);
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      RenderSystem.disableBlend();
      Render2DEngine.drawLine((float)i + offset + 20.0F, (float)(IManager.mc.method_22683().method_4502() - 23), (float)i + offset + 20.0F, (float)(IManager.mc.method_22683().method_4502() - 9), Color.GRAY.getRGB());
      offset += 23.0F;

      for(Iterator var8 = this.windows.iterator(); var8.hasNext(); offset += 20.0F) {
         WindowBase w = (WindowBase)var8.next();
         Color c = Render2DEngine.isHovered((double)mouseX, (double)mouseY, (double)((float)i + offset), (double)(IManager.mc.method_22683().method_4502() - 24), 17.0D, 17.0D) ? new Color(2083467055, true) : (!w.isVisible() ? new Color(2082348574, true) : new Color(2084256571, true));
         Render2DEngine.drawRect(matrices, (float)i + offset, (float)(IManager.mc.method_22683().method_4502() - 24), 17.0F, 17.0F, (Float)HudEditor.hudRound.getValue(), 0.7F, c, c, c, c);
         RenderSystem.enableBlend();
         RenderSystem.blendFunc(class_4535.SRC_ALPHA, class_4534.ONE);
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, Render2DEngine.isHovered((double)mouseX, (double)mouseY, (double)((float)i + offset + 1.0F), (double)(IManager.mc.method_22683().method_4502() - 23), 15.0D, 15.0D) ? 0.95F : 0.7F);
         context.method_25293(w.getIcon() != null ? w.getIcon() : TextureStorage.configIcon, (int)((float)i + offset) + 3, IManager.mc.method_22683().method_4502() - 21, 11, 11, 0.0F, 0.0F, 11, 11, 11, 11);
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
         RenderSystem.disableBlend();
      }

      this.windows.stream().filter(WindowBase::isVisible).forEach((wx) -> {
         if (wx != lastClickedWindow) {
            wx.render(context, mouseX, mouseY);
         }

      });
      if (lastClickedWindow != null && lastClickedWindow.isVisible()) {
         lastClickedWindow.render(context, mouseX, mouseY);
      }

   }

   public boolean method_25406(double mouseX, double mouseY, int button) {
      this.windows.forEach((w) -> {
         w.mouseReleased(mouseX, mouseY, button);
      });
      return super.method_25406(mouseX, mouseY, button);
   }

   public boolean method_25402(double mouseX, double mouseY, int button) {
      this.windows.stream().filter(WindowBase::isVisible).forEach((wx) -> {
         wx.mouseClicked(mouseX, mouseY, button);
      });
      int i = IManager.mc.method_22683().method_4486() / 2;
      float offset = (float)this.windows.size() * 20.0F / -2.0F - 23.0F;
      if (Render2DEngine.isHovered(mouseX, mouseY, (double)((float)i + offset + 1.0F), (double)(IManager.mc.method_22683().method_4502() - 23), 15.0D, 15.0D)) {
         IManager.mc.method_1507(ClickGUI.getClickGui());
      }

      offset += 23.0F;

      for(Iterator var8 = this.windows.iterator(); var8.hasNext(); offset += 20.0F) {
         WindowBase w = (WindowBase)var8.next();
         if (Render2DEngine.isHovered(mouseX, mouseY, (double)((float)i + offset), (double)(IManager.mc.method_22683().method_4502() - 24), 17.0D, 17.0D)) {
            w.setVisible(!w.isVisible());
         }
      }

      return super.method_25402(mouseX, mouseY, button);
   }

   public boolean method_25404(int keyCode, int scanCode, int modifiers) {
      this.windows.stream().filter(WindowBase::isVisible).forEach((w) -> {
         w.keyPressed(keyCode, scanCode, modifiers);
      });
      return super.method_25404(keyCode, scanCode, modifiers);
   }

   public boolean method_25400(char key, int keyCode) {
      this.windows.stream().filter(WindowBase::isVisible).forEach((w) -> {
         w.charTyped(key, keyCode);
      });
      return super.method_25400(key, keyCode);
   }

   public boolean method_25401(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
      this.windows.stream().filter(WindowBase::isVisible).forEach((w) -> {
         w.mouseScrolled((int)(verticalAmount * 5.0D));
      });
      return super.method_25401(mouseX, mouseY, horizontalAmount, verticalAmount);
   }
}
