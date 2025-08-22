package tsunami.gui.clickui;

import com.mojang.blaze3d.platform.GlStateManager.class_4534;
import com.mojang.blaze3d.platform.GlStateManager.class_4535;
import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.class_1074;
import net.minecraft.class_124;
import net.minecraft.class_332;
import net.minecraft.class_3675;
import net.minecraft.class_437;
import net.minecraft.class_4587;
import net.minecraft.class_7833;
import org.jetbrains.annotations.NotNull;
import tsunami.core.Managers;
import tsunami.core.manager.client.ModuleManager;
import tsunami.features.cmd.Command;
import tsunami.features.hud.impl.TargetHud;
import tsunami.features.modules.Module;
import tsunami.features.modules.client.ClickGui;
import tsunami.features.modules.client.ClientSettings;
import tsunami.features.modules.client.HudEditor;
import tsunami.gui.clickui.impl.BindElement;
import tsunami.gui.clickui.impl.BooleanElement;
import tsunami.gui.clickui.impl.BooleanParentElement;
import tsunami.gui.clickui.impl.ColorPickerElement;
import tsunami.gui.clickui.impl.ItemSelectElement;
import tsunami.gui.clickui.impl.ModeElement;
import tsunami.gui.clickui.impl.ParentElement;
import tsunami.gui.clickui.impl.SliderElement;
import tsunami.gui.clickui.impl.StringElement;
import tsunami.gui.font.FontRenderer;
import tsunami.gui.font.FontRenderers;
import tsunami.gui.misc.DialogScreen;
import tsunami.setting.Setting;
import tsunami.setting.impl.Bind;
import tsunami.setting.impl.BooleanSettingGroup;
import tsunami.setting.impl.ColorSetting;
import tsunami.setting.impl.ItemSelectSetting;
import tsunami.setting.impl.PositionSetting;
import tsunami.setting.impl.SettingGroup;
import tsunami.utility.render.Render2DEngine;
import tsunami.utility.render.Render3DEngine;
import tsunami.utility.render.TextureStorage;
import tsunami.utility.render.animation.AnimationUtility;
import tsunami.utility.render.animation.GearAnimation;

public class ModuleButton extends AbstractButton {
   private final List<AbstractElement> elements;
   public final Module module;
   private boolean open;
   private boolean hovered;
   private boolean prevHovered;
   private float animation;
   private float animation2;
   float category_animation = 0.0F;
   int ticksOpened;
   private final GearAnimation gearAnimation = new GearAnimation();
   private boolean binding = false;
   private boolean holdbind = false;

   public ModuleButton(Module module) {
      this.module = module;
      this.elements = new ArrayList();
      Iterator var2 = module.getSettings().iterator();

      while(true) {
         while(var2.hasNext()) {
            Setting setting = (Setting)var2.next();
            if (setting.getValue() instanceof Boolean && !setting.getName().equals("Enabled") && !setting.getName().equals("Drawn")) {
               this.elements.add(new BooleanElement(setting));
            } else if (setting.getValue() instanceof ColorSetting) {
               this.elements.add(new ColorPickerElement(setting));
            } else if (setting.getValue() instanceof BooleanSettingGroup) {
               this.elements.add(new BooleanParentElement(setting));
            } else if (setting.isNumberSetting() && setting.hasRestriction()) {
               this.elements.add(new SliderElement(setting));
            } else if (setting.getValue() instanceof ItemSelectSetting) {
               this.elements.add(new ItemSelectElement(setting));
            } else if (setting.getValue() instanceof SettingGroup) {
               this.elements.add(new ParentElement(setting));
            } else if (setting.isEnumSetting() && !(setting.getValue() instanceof PositionSetting)) {
               this.elements.add(new ModeElement(setting));
            } else if (setting.getValue() instanceof Bind && !setting.getName().equals("Keybind")) {
               this.elements.add(new BindElement(setting));
            } else if ((setting.getValue() instanceof String || setting.getValue() instanceof Character) && !setting.getName().equalsIgnoreCase("displayName")) {
               this.elements.add(new StringElement(setting));
            }
         }

         return;
      }
   }

   public void init() {
      this.elements.forEach(AbstractElement::init);
   }

   public void render(class_332 context, int mouseX, int mouseY, float delta) {
      this.hovered = Render2DEngine.isHovered((double)mouseX, (double)mouseY, (double)this.x, (double)this.y, (double)this.width, (double)this.height);
      this.animation = AnimationUtility.fast(this.animation, this.module.isEnabled() ? 1.0F : 0.0F, 8.0F);
      this.animation2 = AnimationUtility.fast(this.animation2, 1.0F, 10.0F);
      if (this.hovered) {
         if (!this.prevHovered) {
            Managers.SOUND.playScroll();
         }

         ClickGUI.currentDescription = class_1074.method_4662(this.module.getDescription(), new Object[0]);
      }

      this.prevHovered = this.hovered;
      float ix = this.x + 5.0F;
      float iy = this.y + this.height / 2.0F - 3.0F;
      this.offsetY = AnimationUtility.fast(this.offsetY, this.target_offset, 20.0F);
      float offsetY = 0.0F;
      if (this.isOpen()) {
         Render2DEngine.drawGuiBase(context.method_51448(), this.x + 4.0F, this.y + 2.0F, this.width - 8.0F, this.height + (float)this.getElementsHeight(), 1.0F, 0.0F);
         Render2DEngine.addWindow(context.method_51448(), new Render2DEngine.Rectangle(this.x + 1.0F, this.y + this.height - 2.0F, this.width + this.x - 2.0F, (float)((double)(this.height + this.y + 1.0F) + this.getElementsHeight())));
         if (Module.mc.field_1724 != null) {
            ClickGui var10000 = ModuleManager.clickGui;
            if (((BooleanSettingGroup)ClickGui.gear.getValue()).isEnabled()) {
               Render2DEngine.addWindow(context.method_51448(), new Render2DEngine.Rectangle(this.x, this.y + this.height + 1.0F, this.width + this.x + 6.0F, (float)((double)(this.height + this.y + 1.0F) + this.getElementsHeight())));
               float px = this.x + 4.0F + (this.width - 8.0F) / 2.0F;
               float py = this.y + 12.0F + (this.height + (float)this.getElementsHeight()) / 2.0F;
               int gScale = (Integer)ModuleManager.clickGui.gearScale.getValue();
               context.method_51448().method_22903();
               context.method_51448().method_46416(px, py, 0.0F);
               context.method_51448().method_22907(class_7833.field_40718.rotationDegrees(this.gearAnimation.getValue()));
               context.method_51448().method_46416(-px, -py, 0.0F);
               RenderSystem.setShaderTexture(0, TextureStorage.Gear);
               RenderSystem.enableBlend();
               RenderSystem.blendFunc(class_4535.SRC_ALPHA, class_4534.ONE);
               Render2DEngine.renderGradientTexture(context.method_51448(), (double)(px - (float)gScale / 2.0F), (double)(py - (float)gScale / 2.0F), (double)gScale, (double)gScale, 0.0F, 0.0F, (double)gScale, (double)gScale, (double)gScale, (double)gScale, Render2DEngine.injectAlpha(HudEditor.getColor(270).darker(), 110), Render2DEngine.injectAlpha(HudEditor.getColor(0).darker(), 110), Render2DEngine.injectAlpha(HudEditor.getColor(180).darker(), 110), Render2DEngine.injectAlpha(HudEditor.getColor(90).darker(), 110));
               RenderSystem.disableBlend();
               context.method_51448().method_46416(px, py, 0.0F);
               context.method_51448().method_22907(class_7833.field_40718.rotationDegrees((float)Render2DEngine.interpolate((double)(Module.mc.field_1724.field_6012 - 1), (double)Module.mc.field_1724.field_6012, (double)Render3DEngine.getTickDelta()) * -4.0F));
               context.method_51448().method_46416(-px, -py, 0.0F);
               context.method_51448().method_22909();
               Render2DEngine.popWindow();
            }
         }

         if (Render2DEngine.isHovered((double)mouseX, (double)mouseY, (double)(this.x + 4.0F), (double)(this.y + this.height - 12.0F), (double)(this.width - 8.0F), (double)(this.height + (float)this.getElementsHeight()))) {
            Render2DEngine.drawBlurredShadow(context.method_51448(), (float)(mouseX - 10), (float)(mouseY - 10), 20.0F, 20.0F, 40, HudEditor.getColor(270));
         }

         Iterator var11 = this.elements.iterator();

         while(var11.hasNext()) {
            AbstractElement element = (AbstractElement)var11.next();
            if (element.isVisible()) {
               element.setOffsetY(offsetY);
               element.setX(this.x);
               element.setY(this.y + this.height + 2.0F);
               element.setWidth(this.width);
               element.setHeight(13.0F);
               if (element instanceof ColorPickerElement) {
                  ColorPickerElement picker = (ColorPickerElement)element;
                  element.setHeight(picker.getHeight());
               } else if (element instanceof SliderElement) {
                  element.setHeight(18.0F);
               }

               if (element instanceof ModeElement) {
                  ModeElement combobox = (ModeElement)element;
                  combobox.setWHeight(13.0D);
                  if (combobox.isOpen()) {
                     element.setHeight((float)(13 + combobox.getSetting().getModes().length * 12));
                  } else {
                     element.setHeight(13.0F);
                  }
               }

               offsetY += element.getHeight();
            }
         }

         context.method_51448().method_22903();
         TargetHud.sizeAnimation(context.method_51448(), (double)(this.x + this.width / 2.0F + 6.0F), (double)(this.y + this.height / 2.0F - 12.0F), this.ticksOpened < 5 ? (double)Math.clamp(this.category_animation / offsetY, 0.0F, 1.0F) : 1.0D);
         this.elements.forEach((e) -> {
            if (e.isVisible()) {
               e.render(context, mouseX, mouseY, delta);
            }

         });
         context.method_51448().method_22909();
         Render2DEngine.drawBlurredShadow(context.method_51448(), this.x + 3.0F, this.y + this.height, this.width - 6.0F, 3.0F, 13, HudEditor.getColor(1));
         if (!this.module.isEnabled()) {
            Render2DEngine.draw2DGradientRect(context.method_51448(), this.x + 4.0F, this.y + this.height - 1.0F, this.x + 3.0F + this.width - 7.0F, 3.0F + this.y + this.height, Render2DEngine.applyOpacity(HudEditor.getColor(0), 0.0F), HudEditor.getColor(0), Render2DEngine.applyOpacity(HudEditor.getColor(90), 0.0F), HudEditor.getColor(90));
         }

         Render2DEngine.popWindow();
      } else if (this.hovered) {
         Render2DEngine.addWindow(context.method_51448(), this.x + 1.0F, this.y, this.x + this.width - 2.0F, this.y + this.height, 1.0D);
         Render2DEngine.drawBlurredShadow(context.method_51448(), (float)(mouseX - 10), (float)(mouseY - 10), 20.0F, 20.0F, 35, HudEditor.getColor(270));
         Render2DEngine.popWindow();
      }

      this.category_animation = AnimationUtility.fast(this.category_animation, offsetY, 20.0F);
      if ((double)this.animation < 0.05D) {
         Render2DEngine.drawRect(context.method_51448(), this.x + 4.0F, this.y + 1.0F, this.width - 8.0F, this.height - 2.0F, Render2DEngine.applyOpacity(((ColorSetting)HudEditor.plateColor.getValue()).getColorObject().darker(), 0.15F));
      } else {
         switch((ClickGui.Gradient)ModuleManager.clickGui.gradientMode.getValue()) {
         case both:
            Render2DEngine.draw2DGradientRect(context.method_51448(), this.x + 4.0F, this.y + 1.0F, this.x + 4.0F + this.width - 8.0F, this.y + 1.0F + this.height - 2.0F, Render2DEngine.applyOpacity(HudEditor.getColor(270), this.animation * 2.0F), Render2DEngine.applyOpacity(HudEditor.getColor(0), this.animation * 2.0F), Render2DEngine.applyOpacity(HudEditor.getColor(180), this.animation), Render2DEngine.applyOpacity(HudEditor.getColor(90), this.animation));
            break;
         case UpsideDown:
            Render2DEngine.draw2DGradientRect(context.method_51448(), this.x + 4.0F, this.y + 1.0F, this.x + 4.0F + this.width - 8.0F, this.y + 1.0F + this.height - 2.0F, Render2DEngine.applyOpacity(HudEditor.getColor(270), this.animation * 2.0F), Render2DEngine.applyOpacity(HudEditor.getColor(0), this.animation * 2.0F), Render2DEngine.applyOpacity(HudEditor.getColor(270), this.animation), Render2DEngine.applyOpacity(HudEditor.getColor(0), this.animation));
            break;
         case LeftToRight:
            Render2DEngine.draw2DGradientRect(context.method_51448(), this.x + 4.0F, this.y + 1.0F, this.x + 4.0F + this.width - 8.0F, this.y + 1.0F + this.height - 2.0F, Render2DEngine.applyOpacity(HudEditor.getColor(270), this.animation * 2.0F), Render2DEngine.applyOpacity(HudEditor.getColor(270), this.animation * 2.0F), Render2DEngine.applyOpacity(HudEditor.getColor(0), this.animation), Render2DEngine.applyOpacity(HudEditor.getColor(0), this.animation));
         }
      }

      if (!this.module.getBind().getBind().equalsIgnoreCase("none") && !this.binding) {
         FontRenderers.sf_medium_modules.drawString(context.method_51448(), this.getSbind(), (double)(this.x + this.width - 11.0F - FontRenderers.sf_medium_modules.getStringWidth(this.getSbind())), (double)(this.y + 6.0F), this.module.isEnabled() ? ((ColorSetting)HudEditor.textColor2.getValue()).getColor() : ((ColorSetting)HudEditor.textColor.getValue()).getColor());
      }

      if (this.binding) {
         FontRenderers.sf_medium_modules.drawString(context.method_51448(), this.holdbind ? String.valueOf(class_124.field_1080) + "Toggle / " + String.valueOf(class_124.field_1070) + "Hold" : String.valueOf(class_124.field_1070) + "Toggle " + String.valueOf(class_124.field_1080) + "/ Hold", (double)(this.x + this.width - 11.0F - FontRenderers.sf_medium_modules.getStringWidth("Toggle/Hold")), (double)(iy + 2.0F), Render2DEngine.applyOpacity(Color.WHITE.getRGB(), this.animation2));
      }

      if (this.hovered && class_3675.method_15987(Module.mc.method_22683().method_4490(), 340)) {
         FontRenderer var15 = FontRenderers.sf_medium_modules;
         class_4587 var10001 = context.method_51448();
         String var10002 = this.module.isDrawn() ? String.valueOf(class_124.field_1060) + "TRUE" : String.valueOf(class_124.field_1061) + "FALSE";
         var15.drawString(var10001, "Drawn " + var10002, (double)(ix + 1.0F), (double)(iy + 2.0F), this.module.isEnabled() ? ((ColorSetting)HudEditor.textColor2.getValue()).getColor() : ((ColorSetting)HudEditor.textColor.getValue()).getColor());
      } else if (this.binding) {
         FontRenderers.sf_medium_modules.drawString(context.method_51448(), "PressKey", (double)ix, (double)(iy + 2.0F), this.module.isEnabled() ? Render2DEngine.applyOpacity(((ColorSetting)HudEditor.textColor2.getValue()).getColor(), this.animation2) : Render2DEngine.applyOpacity(((ColorSetting)HudEditor.textColor.getValue()).getColor(), this.animation2));
      } else if (ModuleManager.clickGui.textSide.getValue() == ClickGui.TextSide.Left) {
         FontRenderers.sf_medium_modules.drawString(context.method_51448(), this.module.getName(), (double)(ix + 2.0F), (double)(iy + 2.0F), this.module.isEnabled() ? ((ColorSetting)HudEditor.textColor2.getValue()).getColor() : ((ColorSetting)HudEditor.textColor.getValue()).getColor());
      } else {
         FontRenderers.sf_medium_modules.drawCenteredString(context.method_51448(), this.module.getName(), (double)(ix + this.getWidth() / 2.0F - 4.0F), (double)(iy + 2.0F), this.module.isEnabled() ? ((ColorSetting)HudEditor.textColor2.getValue()).getColor() : ((ColorSetting)HudEditor.textColor.getValue()).getColor());
      }

   }

   @NotNull
   private String getSbind() {
      String sbind = this.module.getBind().getBind();
      if (sbind.equals("LEFT_CONTROL")) {
         sbind = "LCtrl";
      }

      if (sbind.equals("RIGHT_CONTROL")) {
         sbind = "RCtrl";
      }

      if (sbind.equals("LEFT_SHIFT")) {
         sbind = "LShift";
      }

      if (sbind.equals("RIGHT_SHIFT")) {
         sbind = "RShift";
      }

      if (sbind.equals("LEFT_ALT")) {
         sbind = "LAlt";
      }

      if (sbind.equals("RIGHT_ALT")) {
         sbind = "RAlt";
      }

      return sbind;
   }

   public void mouseClicked(int mouseX, int mouseY, int button) {
      if (this.binding) {
         if ((float)mouseX > this.x + 56.0F && (float)mouseX < this.x + 67.0F && (float)mouseY > this.y && (float)mouseY < this.y + this.height) {
            this.holdbind = false;
            this.module.getBind().setHold(false);
            return;
         }

         if ((float)mouseX > this.x + 78.0F && (float)mouseX < this.x + 88.0F && (float)mouseY > this.y && (float)mouseY < this.y + this.height) {
            this.holdbind = true;
            this.module.getBind().setHold(true);
            return;
         }

         this.module.setBind(button, true, this.holdbind);
         this.binding = false;
      }

      if (this.hovered) {
         if (class_3675.method_15987(Module.mc.method_22683().method_4490(), 340) && button == 0) {
            this.module.setDrawn(!this.module.isDrawn());
            return;
         }

         if (class_3675.method_15987(Module.mc.method_22683().method_4490(), 261) && button == 0) {
            DialogScreen dialogScreen = new DialogScreen(TextureStorage.questionPic, ClientSettings.isRu() ? "Сброс модуля" : "Reset module", ClientSettings.isRu() ? "Ты действительно хочешь сбросить " + this.module.getName() + "?" : "Are you sure you want to reset " + this.module.getName() + "?", ClientSettings.isRu() ? "Да" : "Yes", ClientSettings.isRu() ? "Нет" : "No", () -> {
               if (this.module.isEnabled()) {
                  this.module.disable("reseting");
               }

               Iterator var1 = this.module.getSettings().iterator();

               while(var1.hasNext()) {
                  Setting s = (Setting)var1.next();
                  Object patt0$temp = s.getValue();
                  if (patt0$temp instanceof ColorSetting) {
                     ColorSetting cs = (ColorSetting)patt0$temp;
                     cs.setDefault();
                  } else {
                     s.setValue(s.getDefaultValue());
                  }
               }

               Module.mc.method_1507((class_437)null);
            }, () -> {
               Module.mc.method_1507((class_437)null);
            });
            Module.mc.method_1507(dialogScreen);
         }

         if (button == 0) {
            if (this.module.isToggleable()) {
               this.module.toggle();
            }
         } else if (button == 1 && this.module.getSettings().size() > 3) {
            this.setOpen(!this.isOpen());
            if (this.open) {
               Managers.SOUND.playSwipeIn();
            } else {
               Managers.SOUND.playSwipeOut();
            }

            this.animation = 0.5F;
         } else if (button == 2) {
            this.animation2 = 0.0F;
            this.binding = !this.binding;
         }
      }

      if (this.open) {
         this.elements.forEach((element) -> {
            if (element.isVisible()) {
               element.mouseClicked(mouseX, mouseY, button);
            }

         });
      }

   }

   public void mouseReleased(int mouseX, int mouseY, int button) {
      if (this.isOpen()) {
         this.elements.forEach((element) -> {
            element.mouseReleased(mouseX, mouseY, button);
         });
      }

   }

   public void charTyped(char key, int keyCode) {
      if (this.isOpen()) {
         Iterator var3 = this.elements.iterator();

         while(var3.hasNext()) {
            AbstractElement element = (AbstractElement)var3.next();
            element.charTyped(key, keyCode);
         }
      }

   }

   public void keyTyped(int keyCode) {
      if (this.isOpen()) {
         Iterator var2 = this.elements.iterator();

         while(var2.hasNext()) {
            AbstractElement element = (AbstractElement)var2.next();
            element.keyTyped(keyCode);
         }
      }

      if (this.binding) {
         String var10000;
         if (keyCode != 256 && keyCode != 261) {
            this.module.setBind(keyCode, false, this.holdbind);
            var10000 = this.module.getName();
            Command.sendMessage(var10000 + (ClientSettings.isRu() ? " бинд изменен на " : " bind changed to ") + this.module.getBind().getBind());
         } else {
            this.module.setBind(-1, false, this.holdbind);
            var10000 = ClientSettings.isRu() ? "Удален бинд с модуля " : "Removed bind from ";
            Command.sendMessage(var10000 + this.module.getName());
         }

         this.binding = false;
      }

   }

   public void onGuiClosed() {
      this.elements.forEach(AbstractElement::onClose);
   }

   public List<AbstractElement> getElements() {
      return this.elements;
   }

   public double getElementsHeight() {
      return (double)this.category_animation;
   }

   public double interp(double d, double d2, float d3) {
      return d2 + (d - d2) * (double)d3;
   }

   public boolean isOpen() {
      return this.open;
   }

   public void setOpen(boolean open) {
      this.open = open;
   }

   public void tick() {
      if (this.isOpen()) {
         this.gearAnimation.tick();
         ++this.ticksOpened;
      } else {
         this.ticksOpened = 0;
      }

   }
}
