package tsunami.features.hud.impl;

import java.awt.Color;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import net.minecraft.class_124;
import net.minecraft.class_332;
import net.minecraft.class_4587;
import tsunami.core.Managers;
import tsunami.features.hud.HudElement;
import tsunami.features.modules.Module;
import tsunami.features.modules.client.HudEditor;
import tsunami.gui.font.FontRenderer;
import tsunami.gui.font.FontRenderers;
import tsunami.setting.Setting;
import tsunami.setting.impl.ColorSetting;
import tsunami.utility.render.Render2DEngine;

public class ModuleList extends HudElement {
   private final Setting<ModuleList.Mode> mode;
   private final Setting<Integer> gste;
   private final Setting<Boolean> glow;
   private final Setting<Boolean> hrender;
   private final Setting<Boolean> hhud;
   private final Setting<ColorSetting> color3;
   private final Setting<ColorSetting> color4;

   public ModuleList() {
      super("ArrayList", 50, 30);
      this.mode = new Setting("Mode", ModuleList.Mode.ColorText);
      this.gste = new Setting("GS", 30, 1, 50);
      this.glow = new Setting("glow", false);
      this.hrender = new Setting("HideHud", true);
      this.hhud = new Setting("HideRender", true);
      this.color3 = new Setting("RectColor", new ColorSetting(-16777216));
      this.color4 = new Setting("SideRectColor", new ColorSetting(-16777216));
   }

   public void onRender2D(class_332 context) {
      super.onRender2D(context);
      boolean reverse = this.getPosX() > (float)mc.method_22683().method_4486() / 2.0F;
      int offset = 0;
      float maxWidth = 0.0F;
      float reversedX = this.getPosX();

      List list;
      try {
         list = Managers.MODULE.getEnabledModules().stream().sorted(Comparator.comparing((modulex) -> {
            return FontRenderers.modules.getStringWidth(modulex.getFullArrayString()) * -1.0F;
         })).toList();
      } catch (IllegalArgumentException var11) {
         return;
      }

      Iterator var8 = list.iterator();

      int stringWidth;
      Module module;
      Color color1;
      FontRenderer var10000;
      String var10001;
      while(var8.hasNext()) {
         module = (Module)var8.next();
         if (this.shouldRender(module)) {
            color1 = HudEditor.getColor(offset);
            var10000 = FontRenderers.modules;
            var10001 = module.getDisplayName();
            stringWidth = (int)(var10000.getStringWidth(var10001 + String.valueOf(class_124.field_1080) + (module.getDisplayInfo() != null ? " [" + String.valueOf(class_124.field_1068) + module.getDisplayInfo() + String.valueOf(class_124.field_1080) + "]" : "")) + 3.0F);
            if ((Boolean)this.glow.getValue()) {
               Render2DEngine.drawBlurredShadow(context.method_51448(), reverse ? reversedX - (float)stringWidth - 3.0F : this.getPosX(), this.getPosY() + (float)offset - 1.0F, (float)(stringWidth + 4), 9.0F, (Integer)this.gste.getValue(), color1);
            }

            if ((float)stringWidth > maxWidth) {
               maxWidth = (float)stringWidth;
            }

            offset += 9;
         }
      }

      offset = 0;
      var8 = list.iterator();

      while(var8.hasNext()) {
         module = (Module)var8.next();
         if (this.shouldRender(module)) {
            var10000 = FontRenderers.modules;
            var10001 = module.getDisplayName();
            stringWidth = (int)(var10000.getStringWidth(var10001 + String.valueOf(class_124.field_1080) + (module.getDisplayInfo() != null ? " [" + String.valueOf(class_124.field_1068) + module.getDisplayInfo() + String.valueOf(class_124.field_1080) + "]" : "")) + 3.0F);
            color1 = HudEditor.getColor(offset);
            if (HudEditor.hudStyle.is(HudEditor.HudStyle.Blurry)) {
               Render2DEngine.drawRoundedBlur(context.method_51448(), reverse ? reversedX - (float)stringWidth : this.getPosX(), this.getPosY() + (float)offset, (float)stringWidth + 1.0F, 9.0F, 2.0F, ((ColorSetting)HudEditor.blurColor.getValue()).getColorObject());
            } else {
               Render2DEngine.drawRect(context.method_51448(), reverse ? reversedX - (float)stringWidth : this.getPosX(), this.getPosY() + (float)offset, (float)stringWidth + 1.0F, 9.0F, this.mode.getValue() == ModuleList.Mode.ColorRect ? color1 : ((ColorSetting)this.color3.getValue()).getColorObject());
               Render2DEngine.drawRect(context.method_51448(), reverse ? reversedX + 1.0F : this.getPosX() - 2.0F, this.getPosY() + (float)offset, 2.0F, 9.0F, this.mode.getValue() == ModuleList.Mode.ColorRect ? ((ColorSetting)this.color4.getValue()).getColorObject() : color1);
            }

            var10000 = FontRenderers.modules;
            class_4587 var12 = context.method_51448();
            String var10002 = module.getDisplayName();
            var10000.drawString(var12, var10002 + String.valueOf(class_124.field_1080) + (module.getDisplayInfo() != null ? " [" + String.valueOf(class_124.field_1068) + module.getDisplayInfo() + String.valueOf(class_124.field_1080) + "]" : ""), reverse ? (double)(reversedX - (float)stringWidth + 2.0F) : (double)(this.getPosX() + 3.0F), (double)(this.getPosY() + 3.0F + (float)offset), this.mode.getValue() == ModuleList.Mode.ColorRect ? -1 : color1.getRGB());
            offset += 9;
         }
      }

      this.setBounds(this.getPosX(), this.getPosY(), (float)((int)maxWidth * (reverse ? -1 : 1)), (float)offset);
   }

   private boolean shouldRender(Module m) {
      return m.isDrawn() && (!(Boolean)this.hrender.getValue() || m.getCategory() != Module.Category.RENDER) && (!(Boolean)this.hhud.getValue() || m.getCategory() != Module.Category.HUD);
   }

   private static enum Mode {
      ColorText,
      ColorRect;

      // $FF: synthetic method
      private static ModuleList.Mode[] $values() {
         return new ModuleList.Mode[]{ColorText, ColorRect};
      }
   }
}
