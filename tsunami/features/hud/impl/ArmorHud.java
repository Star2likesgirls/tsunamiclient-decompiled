package tsunami.features.hud.impl;

import com.mojang.blaze3d.systems.RenderSystem;
import java.util.Iterator;
import net.minecraft.class_1304;
import net.minecraft.class_1738;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_332;
import tsunami.features.hud.HudElement;
import tsunami.setting.Setting;
import tsunami.utility.render.Render2DEngine;

public class ArmorHud extends HudElement {
   private final Setting<ArmorHud.Mode> mode;

   public ArmorHud() {
      super("ArmorHud", 60, 25);
      this.mode = new Setting("Mode", ArmorHud.Mode.V2);
   }

   public void onRender2D(class_332 context) {
      super.onRender2D(context);
      float xItemOffset = this.getPosX();
      Iterator var3 = mc.field_1724.method_31548().field_7548.reversed().iterator();

      while(true) {
         class_1799 itemStack;
         do {
            if (!var3.hasNext()) {
               this.setBounds(this.getPosX(), this.getPosY(), 60.0F, 25.0F);
               return;
            }

            itemStack = (class_1799)var3.next();
         } while(itemStack.method_7960());

         if (this.mode.is(ArmorHud.Mode.V1)) {
            context.method_51427(itemStack, (int)xItemOffset, (int)this.getPosY());
            context.method_51431(mc.field_1772, itemStack, (int)xItemOffset, (int)this.getPosY());
         } else {
            byte var10000;
            label28: {
               RenderSystem.setShaderColor(0.4F, 0.4F, 0.4F, 0.35F);
               context.method_51427(itemStack, (int)xItemOffset, (int)this.getPosY());
               RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
               class_1792 var7 = itemStack.method_7909();
               if (var7 instanceof class_1738) {
                  class_1738 ai = (class_1738)var7;
                  if (ai.method_7685() == class_1304.field_6169) {
                     var10000 = -4;
                     break label28;
                  }
               }

               var10000 = 0;
            }

            float offset = (float)var10000;
            Render2DEngine.addWindow(context.method_51448(), (float)((int)xItemOffset), this.getPosY() + offset + (15.0F - offset) * ((float)itemStack.method_7919() / (float)itemStack.method_7936()), xItemOffset + 15.0F, this.getPosY() + 15.0F, 1.0D);
            context.method_51427(itemStack, (int)xItemOffset, (int)this.getPosY());
            Render2DEngine.popWindow();
         }

         xItemOffset += 20.0F;
      }
   }

   private static enum Mode {
      V1,
      V2;

      // $FF: synthetic method
      private static ArmorHud.Mode[] $values() {
         return new ArmorHud.Mode[]{V1, V2};
      }
   }
}
