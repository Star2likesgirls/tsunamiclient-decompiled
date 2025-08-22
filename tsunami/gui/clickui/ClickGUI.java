package tsunami.gui.clickui;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import net.minecraft.class_2561;
import net.minecraft.class_332;
import net.minecraft.class_3675;
import net.minecraft.class_437;
import org.lwjgl.glfw.GLFW;
import tsunami.core.Managers;
import tsunami.core.manager.client.ModuleManager;
import tsunami.features.hud.HudElement;
import tsunami.features.modules.Module;
import tsunami.features.modules.client.ClickGui;
import tsunami.features.modules.client.ClientSettings;
import tsunami.gui.font.FontRenderers;
import tsunami.utility.render.Render2DEngine;
import tsunami.utility.render.animation.AnimationUtility;
import tsunami.utility.render.animation.EaseOutBack;

public class ClickGUI extends class_437 {
   public static List<AbstractCategory> windows;
   public static boolean anyHovered;
   public static final Color DARK_BLUE_PRIMARY = new Color(13, 34, 71);
   public static final Color DARK_BLUE_SECONDARY = new Color(19, 53, 107);
   public static final Color CYAN_PRIMARY = new Color(0, 188, 212);
   public static final Color CYAN_SECONDARY = new Color(0, 229, 255);
   public static final Color CYAN_DARK = new Color(0, 96, 100);
   public static final Color DARK_BLUE_LIGHT = new Color(25, 67, 135);
   private boolean firstOpen;
   private float scrollY;
   private float closeAnimation;
   private float prevYaw;
   private float prevPitch;
   private float closeDirectionX;
   private float closeDirectionY;
   public static boolean close = false;
   public static boolean imageDirection;
   public static String currentDescription = "";
   public EaseOutBack imageAnimation = new EaseOutBack(6);
   private static ClickGUI INSTANCE = new ClickGUI();

   public ClickGUI() {
      super(class_2561.method_30163("NewClickGUI"));
      windows = Lists.newArrayList();
      this.firstOpen = true;
      this.setInstance();
   }

   public static ClickGUI getInstance() {
      if (INSTANCE == null) {
         INSTANCE = new ClickGUI();
      }

      imageDirection = true;
      return INSTANCE;
   }

   public static ClickGUI getClickGui() {
      windows.forEach(AbstractCategory::init);
      return getInstance();
   }

   private void setInstance() {
      INSTANCE = this;
   }

   public static int getThemeColor(String type) {
      String var1 = type.toLowerCase();
      byte var2 = -1;
      switch(var1.hashCode()) {
      case -1423461174:
         if (var1.equals("accent")) {
            var2 = 4;
         }
         break;
      case -1380798726:
         if (var1.equals("bright")) {
            var2 = 7;
         }
         break;
      case -1332194002:
         if (var1.equals("background")) {
            var2 = 0;
         }
         break;
      case -817598092:
         if (var1.equals("secondary")) {
            var2 = 2;
         }
         break;
      case -681210700:
         if (var1.equals("highlight")) {
            var2 = 6;
         }
         break;
      case -314765822:
         if (var1.equals("primary")) {
            var2 = 5;
         }
         break;
      case -73605918:
         if (var1.equals("text_secondary")) {
            var2 = 11;
         }
         break;
      case 3141:
         if (var1.equals("bg")) {
            var2 = 1;
         }
         break;
      case 113745:
         if (var1.equals("sec")) {
            var2 = 3;
         }
         break;
      case 3556653:
         if (var1.equals("text")) {
            var2 = 10;
         }
         break;
      case 991960078:
         if (var1.equals("light_bg")) {
            var2 = 9;
         }
         break;
      case 2002293171:
         if (var1.equals("dark_accent")) {
            var2 = 8;
         }
      }

      int var10000;
      switch(var2) {
      case 0:
      case 1:
         var10000 = DARK_BLUE_PRIMARY.getRGB();
         break;
      case 2:
      case 3:
         var10000 = DARK_BLUE_SECONDARY.getRGB();
         break;
      case 4:
      case 5:
         var10000 = CYAN_PRIMARY.getRGB();
         break;
      case 6:
      case 7:
         var10000 = CYAN_SECONDARY.getRGB();
         break;
      case 8:
         var10000 = CYAN_DARK.getRGB();
         break;
      case 9:
         var10000 = DARK_BLUE_LIGHT.getRGB();
         break;
      case 10:
         var10000 = Color.WHITE.getRGB();
         break;
      case 11:
         var10000 = (new Color(200, 200, 200)).getRGB();
         break;
      default:
         var10000 = CYAN_PRIMARY.getRGB();
      }

      return var10000;
   }

   protected void method_25426() {
      float offset;
      int halfWidth;
      if (this.firstOpen) {
         offset = 0.0F;
         int windowHeight = 18;
         halfWidth = Module.mc.method_22683().method_4486() / 2;
         int halfWidthCats = (int)(((float)Module.Category.values().size() - 1.0F) / 2.0F * ((float)(Integer)ModuleManager.clickGui.moduleWidth.getValue() + 4.0F));
         Iterator var5 = Managers.MODULE.getCategories().iterator();

         while(var5.hasNext()) {
            Module.Category category = (Module.Category)var5.next();
            if (category != Module.Category.HUD) {
               Category window = new Category(category, Managers.MODULE.getModulesByCategory(category), (float)(halfWidth - halfWidthCats) + offset, 20.0F, 100.0F, (float)windowHeight);
               window.setOpen(true);
               windows.add(window);
               offset += (float)((Integer)ModuleManager.clickGui.moduleWidth.getValue() + 2);
               if (offset > (float)Module.mc.method_22683().method_4486()) {
                  offset = 0.0F;
               }
            }
         }

         this.firstOpen = false;
      } else if (((AbstractCategory)windows.getFirst()).getX() < 0.0F || ((AbstractCategory)windows.getFirst()).getY() < 0.0F) {
         offset = 0.0F;
         int halfWidth = Module.mc.method_22683().method_4486() / 2;
         halfWidth = (int)(3.0F * ((float)(Integer)ModuleManager.clickGui.moduleWidth.getValue() + 4.0F));
         Iterator var9 = windows.iterator();

         while(var9.hasNext()) {
            AbstractCategory w = (AbstractCategory)var9.next();
            w.setX((float)(halfWidth - halfWidth) + offset);
            w.setY(20.0F);
            offset += (float)((Integer)ModuleManager.clickGui.moduleWidth.getValue() + 2);
            if (offset > (float)Module.mc.method_22683().method_4486()) {
               offset = 0.0F;
            }
         }
      }

      windows.forEach(AbstractCategory::init);
   }

   public boolean method_25421() {
      return false;
   }

   public void method_25393() {
      windows.forEach(AbstractCategory::tick);
      this.imageAnimation.update(imageDirection);
      if (close) {
         if (Module.mc.field_1724 != null) {
            if (Module.mc.field_1724.method_36455() > this.prevPitch) {
               this.closeDirectionY = (this.prevPitch - Module.mc.field_1724.method_36455()) * 300.0F;
            }

            if (Module.mc.field_1724.method_36455() < this.prevPitch) {
               this.closeDirectionY = (this.prevPitch - Module.mc.field_1724.method_36455()) * 300.0F;
            }

            if (Module.mc.field_1724.method_36454() > this.prevYaw) {
               this.closeDirectionX = (this.prevYaw - Module.mc.field_1724.method_36454()) * 300.0F;
            }

            if (Module.mc.field_1724.method_36454() < this.prevYaw) {
               this.closeDirectionX = (this.prevYaw - Module.mc.field_1724.method_36454()) * 300.0F;
            }
         }

         if (this.closeDirectionX < 1.0F && this.closeDirectionY < 1.0F && this.closeAnimation > 2.0F) {
            this.closeDirectionY = -3000.0F;
         }

         ++this.closeAnimation;
         if (this.closeAnimation > 6.0F) {
            close = false;
            windows.forEach(AbstractCategory::restorePos);
            this.method_25419();
         }
      }

   }

   public void method_25394(class_332 context, int mouseX, int mouseY, float delta) {
      if ((Boolean)ModuleManager.clickGui.blur.getValue()) {
         this.method_57734(delta);
      }

      anyHovered = false;
      ClickGui.Image image = (ClickGui.Image)ModuleManager.clickGui.image.getValue();
      if (image != ClickGui.Image.None) {
         RenderSystem.setShaderTexture(0, image.file);
         Render2DEngine.renderTexture(context.method_51448(), (double)Module.mc.method_22683().method_4486() - (double)image.fileWidth * this.imageAnimation.getAnimationd(), (double)(Module.mc.method_22683().method_4502() - image.fileHeight), (double)image.fileWidth, (double)image.fileHeight, 0.0F, 0.0F, (double)image.fileWidth, (double)image.fileHeight, (double)image.fileWidth, (double)image.fileHeight);
      }

      if (this.closeAnimation <= 6.0F) {
         windows.forEach((w) -> {
            w.setX(w.getX() + this.closeDirectionX * AnimationUtility.deltaTime());
            w.setY(w.getY() + this.closeDirectionY * AnimationUtility.deltaTime());
         });
      }

      if (Module.fullNullCheck()) {
         this.method_25420(context, mouseX, mouseY, delta);
      }

      Iterator var6;
      AbstractCategory window;
      if (ModuleManager.clickGui.scrollMode.getValue() == ClickGui.scrollModeEn.Old) {
         var6 = windows.iterator();

         while(var6.hasNext()) {
            window = (AbstractCategory)var6.next();
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

            if (this.scrollY != 0.0F) {
               window.setY(window.getY() + this.scrollY);
            }
         }
      } else {
         var6 = windows.iterator();

         while(var6.hasNext()) {
            window = (AbstractCategory)var6.next();
            if (this.scrollY != 0.0F) {
               window.setModuleOffset(this.scrollY, (float)mouseX, (float)mouseY);
            }
         }
      }

      this.scrollY = 0.0F;
      windows.forEach((w) -> {
         w.render(context, mouseX, mouseY, delta);
      });
      if (!Objects.equals(currentDescription, "") && (Boolean)ModuleManager.clickGui.descriptions.getValue()) {
         Render2DEngine.drawHudBase(context.method_51448(), (float)(mouseX + 7), (float)(mouseY + 5), FontRenderers.sf_medium.getStringWidth(currentDescription) + 6.0F, 11.0F, 1.0F, false);
         FontRenderers.sf_medium.drawString(context.method_51448(), currentDescription, (double)(mouseX + 10), (double)(mouseY + 8), getThemeColor("text"));
         currentDescription = "";
      }

      if ((Boolean)ModuleManager.clickGui.tips.getValue() && !close) {
         FontRenderers.sf_medium.drawString(context.method_51448(), ClientSettings.isRu() ? "Щелкните левой кнопкой мыши, чтобы включить модуль.\nЩелкните правой кнопкой мыши, чтобы открыть настройки модуля.\nЩелкните колёсиком мыши, чтобы привязать модуль\nCtrl + F, чтобы начать поиск\nПерекиньте конфиг в окошко майна, чтобы загрузить его\nShift + Left Mouse Click, чтобы изменить отображение модуля в Array list\nЩелкните колёсиком мыши по слайдеру, чтобы ввести значение с клавиатуры.\nDelete + Left Mouse Click по модулю, чтобы сбросить его настройки" : "Left Mouse Click to enable module\nRight Mouse Click to open module settings\nMiddle Mouse Click to bind module\nCtrl + F to start searching\nDrag n Drop config there to load\nShift + Left Mouse Click to change module visibility in Array list\nMiddle Mouse Click on slider to enter value from keyboard\nDelete + Left Mouse Click on module to reset", 5.0D, (double)(Module.mc.method_22683().method_4502() - 80), getThemeColor("text_secondary"));
      }

      if (!HudElement.anyHovered && !anyHovered && GLFW.glfwGetPlatform() != 393219) {
         GLFW.glfwSetCursor(Module.mc.method_22683().method_4490(), GLFW.glfwCreateStandardCursor(221185));
      }

   }

   public boolean method_25401(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
      this.scrollY += (float)((int)(verticalAmount * 5.0D));
      return super.method_25401(mouseX, mouseY, horizontalAmount, verticalAmount);
   }

   public boolean method_25402(double mouseX, double mouseY, int button) {
      windows.forEach((w) -> {
         w.mouseClicked((int)mouseX, (int)mouseY, button);
         windows.forEach((w1) -> {
            if (w.dragging && w != w1) {
               w1.dragging = false;
            }

         });
      });
      return super.method_25402(mouseX, mouseY, button);
   }

   public boolean method_25406(double mouseX, double mouseY, int button) {
      windows.forEach((w) -> {
         w.mouseReleased((int)mouseX, (int)mouseY, button);
      });
      return super.method_25406(mouseX, mouseY, button);
   }

   public boolean method_25400(char key, int modifier) {
      windows.forEach((w) -> {
         w.charTyped(key, modifier);
      });
      return true;
   }

   public boolean method_25404(int keyCode, int scanCode, int modifiers) {
      windows.forEach((w) -> {
         w.keyTyped(keyCode);
      });
      if (keyCode == 256) {
         if (Module.mc.field_1724 != null && (Boolean)ModuleManager.clickGui.closeAnimation.getValue()) {
            if (close) {
               return true;
            } else {
               imageDirection = false;
               windows.forEach(AbstractCategory::savePos);
               this.closeDirectionX = 0.0F;
               this.closeDirectionY = 0.0F;
               close = true;
               Module.mc.field_1729.method_1612();
               this.closeAnimation = 0.0F;
               if (Module.mc.field_1724 != null) {
                  this.prevYaw = Module.mc.field_1724.method_36454();
                  this.prevPitch = Module.mc.field_1724.method_36455();
               }

               return true;
            }
         } else {
            imageDirection = false;
            this.imageAnimation.reset();
            super.method_25404(keyCode, scanCode, modifiers);
            return true;
         }
      } else {
         return false;
      }
   }
}
