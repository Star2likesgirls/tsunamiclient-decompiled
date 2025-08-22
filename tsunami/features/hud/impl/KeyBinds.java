package tsunami.features.hud.impl;

import java.awt.Color;
import java.util.Iterator;
import java.util.Objects;
import net.minecraft.class_332;
import org.jetbrains.annotations.NotNull;
import tsunami.core.Managers;
import tsunami.core.manager.client.ModuleManager;
import tsunami.features.hud.HudElement;
import tsunami.features.modules.Module;
import tsunami.features.modules.client.HudEditor;
import tsunami.gui.font.FontRenderers;
import tsunami.setting.Setting;
import tsunami.setting.impl.ColorSetting;
import tsunami.utility.render.Render2DEngine;
import tsunami.utility.render.animation.AnimationUtility;

public class KeyBinds extends HudElement {
   public final Setting<ColorSetting> oncolor = new Setting("OnColor", new ColorSetting(-1));
   public final Setting<ColorSetting> offcolor = new Setting("OffColor", new ColorSetting(1));
   public final Setting<Boolean> onlyEnabled = new Setting("OnlyEnabled", false);
   private float vAnimation;
   private float hAnimation;

   public KeyBinds() {
      super("KeyBinds", 100, 100);
   }

   public void onRender2D(class_332 context) {
      super.onRender2D(context);
      int y_offset1 = 0;
      float max_width = 50.0F;
      float maxBindWidth = 0.0F;
      float pointerX = 0.0F;
      Iterator var6 = Managers.MODULE.modules.iterator();

      while(true) {
         Module feature;
         do {
            if (!var6.hasNext()) {
               float px = this.getPosX() + 10.0F + pointerX;
               max_width = Math.max(20.0F + pointerX + maxBindWidth, 50.0F);
               this.vAnimation = AnimationUtility.fast(this.vAnimation, (float)(14 + y_offset1), 15.0F);
               this.hAnimation = AnimationUtility.fast(this.hAnimation, max_width, 15.0F);
               Render2DEngine.drawHudBase(context.method_51448(), this.getPosX(), this.getPosY(), this.hAnimation, this.vAnimation, (Float)HudEditor.hudRound.getValue());
               if (HudEditor.hudStyle.is(HudEditor.HudStyle.Glowing)) {
                  FontRenderers.sf_bold.drawCenteredString(context.method_51448(), "KeyBinds", (double)(this.getPosX() + this.hAnimation / 2.0F), (double)(this.getPosY() + 4.0F), ((ColorSetting)HudEditor.textColor.getValue()).getColorObject());
               } else {
                  FontRenderers.sf_bold.drawGradientCenteredString(context.method_51448(), "KeyBinds", this.getPosX() + this.hAnimation / 2.0F, this.getPosY() + 4.0F, 10);
               }

               if (y_offset1 > 0) {
                  if (HudEditor.hudStyle.is(HudEditor.HudStyle.Blurry)) {
                     Render2DEngine.drawRectDumbWay(context.method_51448(), this.getPosX() + 4.0F, this.getPosY() + 13.0F, this.getPosX() + this.getWidth() - 4.0F, this.getPosY() + 13.5F, new Color(1426063359, true));
                  } else {
                     Render2DEngine.horizontalGradient(context.method_51448(), this.getPosX() + 2.0F, this.getPosY() + 13.7F, this.getPosX() + 2.0F + this.hAnimation / 2.0F - 2.0F, this.getPosY() + 14.0F, Render2DEngine.injectAlpha(((ColorSetting)HudEditor.textColor.getValue()).getColorObject(), 0), ((ColorSetting)HudEditor.textColor.getValue()).getColorObject());
                     Render2DEngine.horizontalGradient(context.method_51448(), this.getPosX() + 2.0F + this.hAnimation / 2.0F - 2.0F, this.getPosY() + 13.7F, this.getPosX() + 2.0F + this.hAnimation - 4.0F, this.getPosY() + 14.0F, ((ColorSetting)HudEditor.textColor.getValue()).getColorObject(), Render2DEngine.injectAlpha(((ColorSetting)HudEditor.textColor.getValue()).getColorObject(), 0));
                  }
               }

               Render2DEngine.addWindow(context.method_51448(), this.getPosX(), this.getPosY(), this.getPosX() + this.hAnimation, this.getPosY() + this.vAnimation, 1.0D);
               int y_offset = 0;
               Iterator var12 = Managers.MODULE.modules.iterator();

               while(true) {
                  Module feature;
                  do {
                     if (!var12.hasNext()) {
                        Render2DEngine.popWindow();
                        this.setBounds(this.getPosX(), this.getPosY(), this.hAnimation, this.vAnimation);
                        return;
                     }

                     feature = (Module)var12.next();
                  } while(feature.isDisabled() && (Boolean)this.onlyEnabled.getValue());

                  if (!Objects.equals(feature.getBind().getBind(), "None") && feature != ModuleManager.clickGui && feature != ModuleManager.thunderHackGui) {
                     FontRenderers.sf_bold_mini.drawString(context.method_51448(), feature.getName(), (double)(this.getPosX() + 5.0F), (double)(this.getPosY() + 19.0F + (float)y_offset), feature.isOn() ? ((ColorSetting)this.oncolor.getValue()).getColor() : ((ColorSetting)this.offcolor.getValue()).getColor());
                     FontRenderers.sf_bold_mini.drawCenteredString(context.method_51448(), getShortKeyName(feature), (double)(px + (this.getPosX() + max_width - px) / 2.0F), (double)(this.getPosY() + 19.0F + (float)y_offset), feature.isOn() ? ((ColorSetting)this.oncolor.getValue()).getColor() : ((ColorSetting)this.offcolor.getValue()).getColor());
                     Render2DEngine.drawRect(context.method_51448(), px, this.getPosY() + 17.0F + (float)y_offset, 0.5F, 8.0F, new Color(1157627903, true));
                     y_offset += 9;
                  }
               }
            }

            feature = (Module)var6.next();
         } while(feature.isDisabled() && (Boolean)this.onlyEnabled.getValue());

         if (!Objects.equals(feature.getBind().getBind(), "None") && feature != ModuleManager.clickGui && feature != ModuleManager.thunderHackGui) {
            if (y_offset1 == 0) {
               y_offset1 += 4;
            }

            y_offset1 += 9;
            float nameWidth = FontRenderers.sf_bold_mini.getStringWidth(feature.getName());
            float bindWidth = FontRenderers.sf_bold_mini.getStringWidth(getShortKeyName(feature));
            if (bindWidth > maxBindWidth) {
               maxBindWidth = bindWidth;
            }

            if (nameWidth > pointerX) {
               pointerX = nameWidth;
            }
         }
      }
   }

   @NotNull
   public static String getShortKeyName(Module feature) {
      String sbind = feature.getBind().getBind();
      String var2 = feature.getBind().getBind();
      byte var3 = -1;
      switch(var2.hashCode()) {
      case -1792644783:
         if (var2.equals("LEFT_ALT")) {
            var3 = 4;
         }
         break;
      case -1294125985:
         if (var2.equals("RIGHT_SHIFT")) {
            var3 = 3;
         }
         break;
      case -855820027:
         if (var2.equals("LEFT_CONTROL")) {
            var3 = 0;
         }
         break;
      case -433254870:
         if (var2.equals("LEFT_SHIFT")) {
            var3 = 2;
         }
         break;
      case 775726586:
         if (var2.equals("RIGHT_CONTROL")) {
            var3 = 1;
         }
         break;
      case 1218746566:
         if (var2.equals("RIGHT_ALT")) {
            var3 = 5;
         }
      }

      String var10000;
      switch(var3) {
      case 0:
         var10000 = "LCtrl";
         break;
      case 1:
         var10000 = "RCtrl";
         break;
      case 2:
         var10000 = "LShift";
         break;
      case 3:
         var10000 = "RShift";
         break;
      case 4:
         var10000 = "LAlt";
         break;
      case 5:
         var10000 = "RAlt";
         break;
      default:
         var10000 = sbind.toUpperCase();
      }

      return var10000;
   }
}
