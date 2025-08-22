package tsunami.features.hud.impl;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_332;
import net.minecraft.class_7833;
import tsunami.core.manager.client.ModuleManager;
import tsunami.events.impl.TotemPopEvent;
import tsunami.features.hud.HudElement;
import tsunami.gui.font.FontRenderers;
import tsunami.utility.render.Render2DEngine;
import tsunami.utility.render.Render3DEngine;

public class TotemCounter extends HudElement {
   private float angle;
   private float prevAngle;

   public TotemCounter() {
      super("TotemCounter", 0, 0);
   }

   public void onRender2D(class_332 context) {
      if (this.getItemCount(class_1802.field_8288) != 0) {
         float xPos = ModuleManager.crosshair.getAnimatedPosX();
         float yPos = ModuleManager.crosshair.getAnimatedPosY();
         float factor = Math.abs(this.angle < 0.0F ? this.angle / 15.0F : 0.0F);
         context.method_51448().method_22903();
         context.method_51448().method_46416(xPos, yPos, 0.0F);
         context.method_51448().method_22907(class_7833.field_40717.rotation((float)Math.toRadians((double)(-Render2DEngine.interpolateFloat(this.prevAngle, this.angle, (double)Render3DEngine.getTickDelta())))));
         context.method_51448().method_46416(-xPos, -yPos, 0.0F);
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
         context.method_51448().method_46416(xPos - 36.0F, yPos - 9.0F, 0.0F);
         context.method_51427(class_1802.field_8288.method_7854(), 0, 0);
         context.method_51448().method_46416(-(xPos - 36.0F), -(yPos - 9.0F), 0.0F);
         RenderSystem.setShaderColor(1.0F, 1.0F - factor, 1.0F - factor, 1.0F);
         if (factor > 0.0F) {
            Render2DEngine.drawBlurredShadow(context.method_51448(), xPos - 34.0F, yPos - 6.0F, 11.0F, 11.0F, 8, Render2DEngine.injectAlpha(new Color(16711680), (int)(255.0F * factor)));
         }

         FontRenderers.sf_bold_mini.drawCenteredString(context.method_51448(), this.getItemCount(class_1802.field_8288).makeConcatWithConstants<invokedynamic>(this.getItemCount(class_1802.field_8288)), (double)(xPos - 28.0F), (double)(yPos + 8.0F), -1);
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
         context.method_51448().method_22909();
      }
   }

   @EventHandler
   public void onTotemPop(TotemPopEvent e) {
      if (e.getEntity() == mc.field_1724) {
         this.angle = -15.0F;
      }

   }

   public void onUpdate() {
      this.prevAngle = this.angle;
      if (this.angle < 0.0F) {
         ++this.angle;
      }

   }

   public int getItemCount(class_1792 item) {
      if (mc.field_1724 == null) {
         return 0;
      } else {
         int n = 0;
         int n2 = 44;

         for(int i = 0; i <= n2; ++i) {
            class_1799 itemStack = mc.field_1724.method_31548().method_5438(i);
            if (itemStack.method_7909() == item) {
               n += itemStack.method_7947();
            }
         }

         return n;
      }
   }
}
