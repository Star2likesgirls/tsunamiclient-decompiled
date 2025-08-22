package tsunami.gui.clickui;

import com.mojang.blaze3d.platform.GlStateManager.class_4534;
import com.mojang.blaze3d.platform.GlStateManager.class_4535;
import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.class_286;
import net.minecraft.class_287;
import net.minecraft.class_289;
import net.minecraft.class_290;
import net.minecraft.class_2960;
import net.minecraft.class_332;
import net.minecraft.class_757;
import net.minecraft.class_293.class_5596;
import tsunami.TsunamiClient;
import tsunami.core.manager.client.ModuleManager;
import tsunami.features.modules.Module;
import tsunami.features.modules.client.BaritoneSettings;
import tsunami.features.modules.client.ClickGui;
import tsunami.features.modules.client.HudEditor;
import tsunami.gui.clickui.impl.SearchBar;
import tsunami.gui.font.FontRenderers;
import tsunami.utility.render.Render2DEngine;
import tsunami.utility.render.animation.AnimationUtility;

public class Category extends AbstractCategory {
   private final class_2960 ICON;
   private boolean scrollHover;
   private final List<AbstractButton> buttons = new ArrayList();
   public float catHeight;

   public Category(Module.Category category, ArrayList<Module> features, float x, float y, float width, float height) {
      super(category.getName(), x, y, width, height);
      String var10002 = Module.Category.isCustomCategory(category) ? "stock" : category.getName().toLowerCase();
      this.ICON = class_2960.method_60655("thunderhack", "textures/gui/headers/" + var10002 + ".png");
      if (category.getName().equals("Client")) {
         this.buttons.add(new SearchBar());
      }

      features.forEach((feature) -> {
         if (!(feature instanceof BaritoneSettings) || TsunamiClient.baritone) {
            this.buttons.add(new ModuleButton(feature));
         }

      });
   }

   public void init() {
      this.buttons.forEach(AbstractButton::init);
   }

   public void render(class_332 context, int mouseX, int mouseY, float delta) {
      super.render(context, mouseX, mouseY, delta);
      this.setWidth((float)(Integer)ModuleManager.clickGui.moduleWidth.getValue());
      this.scrollHover = Render2DEngine.isHovered((double)mouseX, (double)mouseY, (double)this.getX(), (double)(this.getY() + this.height), (double)this.width, (double)(this.catHeight + 20.0F));
      context.method_51448().method_22903();
      boolean popStack = false;
      float height1;
      if (ModuleManager.clickGui.scrollMode.getValue() != ClickGui.scrollModeEn.Old && !(this.getButtonsHeight() < (double)(Integer)ModuleManager.clickGui.catHeight.getValue())) {
         height1 = (float)(Integer)ModuleManager.clickGui.catHeight.getValue();
      } else {
         height1 = (float)this.getButtonsHeight();
      }

      this.catHeight = AnimationUtility.fast(this.catHeight, height1, 30.0F);
      Color m1 = HudEditor.getColor(270);
      Color m2 = HudEditor.getColor(0);
      Color m3 = HudEditor.getColor(180);
      Color m4 = HudEditor.getColor(90);
      if (this.isOpen()) {
         Render2DEngine.drawHudBase(context.method_51448(), this.getX() + 3.0F, this.getY() + this.height - 6.0F, this.width - 6.0F, this.catHeight, 1.0F, false);
         if (ModuleManager.clickGui.scrollMode.getValue() != ClickGui.scrollModeEn.Old && !(this.getButtonsHeight() < (double)(Integer)ModuleManager.clickGui.catHeight.getValue())) {
            Render2DEngine.addWindow(context.method_51448(), this.getX() + 3.0F, this.getY() + this.height - 6.0F, this.getX() + 3.0F + this.width - 6.0F, this.getY() + this.height - 6.0F + (float)(Integer)ModuleManager.clickGui.catHeight.getValue(), 1.0D);
            popStack = true;
         }

         Render2DEngine.drawBlurredShadow(context.method_51448(), (float)((int)this.getX() + 4), (float)((int)(this.getY() + this.height - 6.0F)), (float)((int)this.width - 8), 8.0F, 7, new Color(0, 0, 0, 180));
         Iterator var11 = this.buttons.iterator();

         label47:
         while(true) {
            AbstractButton button;
            ModuleButton mb;
            do {
               if (!var11.hasNext()) {
                  break label47;
               }

               button = (AbstractButton)var11.next();
               if (!(button instanceof ModuleButton)) {
                  break;
               }

               mb = (ModuleButton)button;
            } while(SearchBar.listening && !mb.module.getName().toLowerCase().contains(SearchBar.moduleName.toLowerCase()));

            if (popStack && ((AbstractButton)this.buttons.getFirst()).getY() + this.moduleOffset < this.getY() + this.height) {
               button.setY(this.getY() + this.height + this.moduleOffset);
            } else {
               button.setY(this.getY() + this.height);
               this.moduleOffset = 0.0F;
            }

            button.setX(this.getX() + 2.0F);
            button.setWidth(this.width - 4.0F);
            button.setHeight((float)(Integer)ModuleManager.clickGui.moduleHeight.getValue());
            button.render(context, mouseX, mouseY, delta);
         }
      }

      if (popStack) {
         Render2DEngine.popWindow();
      }

      Render2DEngine.drawHudBase(context.method_51448(), this.getX() + 2.0F, this.getY() - 5.0F, this.width - 4.0F, this.height, 1.0F, false);
      RenderSystem.setShaderTexture(0, this.ICON);
      RenderSystem.enableBlend();
      RenderSystem.blendFunc(class_4535.SRC_ALPHA, class_4534.ONE);
      Render2DEngine.addWindow(context.method_51448(), this.getX() + 2.0F, this.getY() - 4.0F, this.getX() + 2.0F + this.width - 4.0F, this.getY() - 5.0F + this.height, 1.0D);
      RenderSystem.setShader(class_757::method_34543);
      class_287 b = class_289.method_1348().method_60827(class_5596.field_27382, class_290.field_1575);
      Render2DEngine.renderGradientTextureInternal(b, context.method_51448(), (double)(this.getX() + 85.0F), (double)(this.getY() + (this.height - 24.0F) / 2.0F), 12.0D, 12.0D, 0.0F, 0.0F, 12.0D, 12.0D, 12.0D, 12.0D, m1.darker(), m2.darker(), m3.darker(), m4.darker());
      Render2DEngine.renderGradientTextureInternal(b, context.method_51448(), (double)(this.getX() + 75.0F), (double)(this.getY() + (this.height - 34.0F) / 2.0F), 16.0D, 16.0D, 0.0F, 0.0F, 16.0D, 16.0D, 16.0D, 16.0D, m1, m2, m3, m4);
      Render2DEngine.renderGradientTextureInternal(b, context.method_51448(), (double)(this.getX() + 65.0F), (double)(this.getY() + (this.height - 20.0F) / 2.0F), 12.0D, 12.0D, 0.0F, 0.0F, 12.0D, 12.0D, 12.0D, 12.0D, m1.darker().darker(), m2.darker().darker(), m3.darker().darker(), m4.darker().darker());
      Render2DEngine.renderGradientTextureInternal(b, context.method_51448(), (double)(this.getX() + 55.0F), (double)(this.getY() + (this.height - 28.0F) / 2.0F), 6.0D, 6.0D, 0.0F, 0.0F, 6.0D, 6.0D, 6.0D, 6.0D, m1, m2, m3, m4);
      Render2DEngine.renderGradientTextureInternal(b, context.method_51448(), (double)(this.getX() + 45.0F), (double)(this.getY() + (this.height - 17.0F) / 2.0F), 17.0D, 17.0D, 0.0F, 0.0F, 17.0D, 17.0D, 17.0D, 17.0D, m1, m2, m3, m4);
      Render2DEngine.renderGradientTextureInternal(b, context.method_51448(), (double)(this.getX() + 35.0F), (double)(this.getY() + (this.height - 30.0F) / 2.0F), 15.0D, 15.0D, 0.0F, 0.0F, 15.0D, 15.0D, 15.0D, 15.0D, m1.darker().darker().darker(), m2.darker().darker().darker(), m3.darker().darker().darker(), m4.darker().darker().darker());
      Render2DEngine.renderGradientTextureInternal(b, context.method_51448(), (double)(this.getX() + 25.0F), (double)(this.getY() + (this.height - 21.0F) / 2.0F), 8.0D, 8.0D, 0.0F, 0.0F, 8.0D, 8.0D, 8.0D, 8.0D, m1, m2, m3, m4);
      Render2DEngine.renderGradientTextureInternal(b, context.method_51448(), (double)(this.getX() + 15.0F), (double)(this.getY() + (this.height - 22.0F) / 2.0F), 12.0D, 12.0D, 0.0F, 0.0F, 12.0D, 12.0D, 12.0D, 12.0D, m1.darker().darker().darker(), m2.darker().darker().darker(), m3.darker().darker().darker(), m4.darker().darker().darker());
      Render2DEngine.renderGradientTextureInternal(b, context.method_51448(), (double)(this.getX() + 5.0F), (double)(this.getY() + (this.height - 28.0F) / 2.0F), 20.0D, 20.0D, 0.0F, 0.0F, 20.0D, 20.0D, 20.0D, 20.0D, m1, m2, m3, m4);
      class_286.method_43433(b.method_60800());
      RenderSystem.disableBlend();
      Render2DEngine.popWindow();
      Render2DEngine.drawBlurredShadow(context.method_51448(), (float)((int)this.getX()) + (this.width - 4.0F) / 2.0F - FontRenderers.categories.getStringWidth(this.getName()) / 2.0F, (float)((int)this.getY()) + (float)((int)this.height) / 2.0F - 10.0F, FontRenderers.categories.getStringWidth(this.getName()) + 6.0F, 13.0F, 20, Render2DEngine.injectAlpha(Color.black, 170));
      FontRenderers.categories.drawCenteredString(context.method_51448(), this.getName(), (double)((float)((int)this.getX() + 2) + (this.width - 4.0F) / 2.0F), (double)((float)((int)this.getY()) + (float)((int)this.height) / 2.0F - 7.0F), (new Color(-1)).getRGB());
      context.method_51448().method_22909();
      this.updatePosition();
   }

   public void mouseClicked(int mouseX, int mouseY, int button) {
      if (button == 1 && this.hovered) {
         this.setOpen(!this.isOpen());
      }

      super.mouseClicked(mouseX, mouseY, button);
      if (this.isOpen() && this.scrollHover) {
         this.buttons.forEach((b) -> {
            b.mouseClicked(mouseX, mouseY, button);
         });
      }

   }

   public void mouseReleased(int mouseX, int mouseY, int button) {
      super.mouseReleased(mouseX, mouseY, button);
      if (this.isOpen()) {
         this.buttons.forEach((b) -> {
            b.mouseReleased(mouseX, mouseY, button);
         });
      }

   }

   public boolean keyTyped(int keyCode) {
      if (this.isOpen()) {
         Iterator var2 = this.buttons.iterator();

         while(var2.hasNext()) {
            AbstractButton button = (AbstractButton)var2.next();
            button.keyTyped(keyCode);
         }
      }

      return false;
   }

   public void charTyped(char key, int keyCode) {
      if (this.isOpen()) {
         Iterator var3 = this.buttons.iterator();

         while(var3.hasNext()) {
            AbstractButton button = (AbstractButton)var3.next();
            button.charTyped(key, keyCode);
         }
      }

   }

   public void onClose() {
      super.onClose();
      this.buttons.forEach(AbstractButton::onGuiClosed);
   }

   public void tick() {
      this.buttons.forEach(AbstractButton::tick);
   }

   private void updatePosition() {
      float offsetY = 0.0F;
      float openY = 0.0F;
      Iterator var3 = this.buttons.iterator();

      while(true) {
         AbstractButton button;
         ModuleButton mbutton;
         do {
            if (!var3.hasNext()) {
               return;
            }

            button = (AbstractButton)var3.next();
            if (!(button instanceof ModuleButton)) {
               break;
            }

            mbutton = (ModuleButton)button;
         } while(SearchBar.listening && !mbutton.module.getName().toLowerCase().contains(SearchBar.moduleName.toLowerCase()));

         button.setTargetOffset(offsetY);
         if (button instanceof ModuleButton) {
            mbutton = (ModuleButton)button;
            if (mbutton.isOpen()) {
               Iterator var6 = mbutton.getElements().iterator();

               while(var6.hasNext()) {
                  AbstractElement element = (AbstractElement)var6.next();
                  if (element.isVisible()) {
                     offsetY += element.getHeight();
                  }
               }

               offsetY += 2.0F;
            }
         }

         offsetY += button.getHeight() + openY;
      }
   }

   public void hudClicked(Module module) {
      Iterator var2 = this.buttons.iterator();

      while(var2.hasNext()) {
         AbstractButton button = (AbstractButton)var2.next();
         if (button instanceof ModuleButton) {
            ModuleButton mbutton = (ModuleButton)button;
            if (mbutton.module == module) {
               mbutton.setOpen(true);
            }
         }
      }

   }

   public double getButtonsHeight() {
      double height = 8.0D;
      Iterator var3 = this.buttons.iterator();

      while(true) {
         AbstractButton button;
         ModuleButton mbutton;
         do {
            if (!var3.hasNext()) {
               return height;
            }

            button = (AbstractButton)var3.next();
            if (!(button instanceof ModuleButton)) {
               break;
            }

            mbutton = (ModuleButton)button;
         } while(SearchBar.listening && !mbutton.module.getName().toLowerCase().contains(SearchBar.moduleName.toLowerCase()));

         if (button instanceof ModuleButton) {
            mbutton = (ModuleButton)button;
            if (mbutton.isOpen()) {
               height += 2.0D;
            }

            height += mbutton.getElementsHeight();
         }

         height += (double)button.getHeight();
      }
   }
}
