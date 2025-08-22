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
import tsunami.events.impl.EventEatFood;
import tsunami.features.hud.HudElement;
import tsunami.gui.font.FontRenderers;
import tsunami.setting.Setting;
import tsunami.utility.math.MathUtility;
import tsunami.utility.render.Render2DEngine;
import tsunami.utility.render.Render3DEngine;

public class GapplesHud extends HudElement {
   private float angle;
   private float prevAngle;
   private final Setting<Boolean> crapple = new Setting("Crapple", true);

   public GapplesHud() {
      super("GapplesHud", 0, 0);
   }

   public void onRender2D(class_332 context) {
      class_1792 targetItem = (Boolean)this.crapple.getValue() ? class_1802.field_8463 : class_1802.field_8367;
      if (this.getItemCount(targetItem) != 0) {
         float xPos = ModuleManager.crosshair.getAnimatedPosX();
         float yPos = ModuleManager.crosshair.getAnimatedPosY();
         float factor = this.angle > 0.0F ? this.angle / 15.0F : 0.0F;
         float factor2 = 1.0F - (float)mc.field_1724.method_6048() / 40.0F;
         if (mc.field_1724.method_6030().method_7909() != targetItem) {
            factor2 = 1.0F;
         }

         factor2 = MathUtility.clamp(factor2, 0.01F, 1.0F);
         context.method_51448().method_22903();
         context.method_51448().method_46416(xPos, yPos, 0.0F);
         context.method_51448().method_22907(class_7833.field_40717.rotation((float)Math.toRadians((double)(-Render2DEngine.interpolateFloat(this.prevAngle, this.angle, (double)Render3DEngine.getTickDelta())))));
         context.method_51448().method_46416(-xPos, -yPos, 0.0F);
         RenderSystem.setShaderColor(0.3F, 0.3F, 0.3F, 1.0F);
         context.method_51448().method_46416(xPos + 20.0F, yPos - 9.0F, 0.0F);
         context.method_51427(targetItem.method_7854(), 0, 0);
         context.method_51448().method_46416(-(xPos + 20.0F), -(yPos - 9.0F), 0.0F);
         RenderSystem.setShaderColor(1.0F, 1.0F - factor, 1.0F - factor, 1.0F);
         context.method_51448().method_46416(xPos + 28.0F, yPos - 1.0F, 0.0F);
         context.method_51448().method_22905(factor2, factor2, 1.0F);
         context.method_51427(targetItem.method_7854(), -8, -8);
         context.method_51448().method_22905(factor2 != 0.0F ? 1.0F / factor2 : 1.0F, factor2 != 0.0F ? 1.0F / factor2 : 1.0F, 1.0F);
         context.method_51448().method_46416(-(xPos + 28.0F), -(yPos - 1.0F), 0.0F);
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
         if (factor > 0.0F) {
            Render2DEngine.drawBlurredShadow(context.method_51448(), xPos + 22.0F, yPos - 6.0F, 11.0F, 11.0F, 8, Render2DEngine.injectAlpha(new Color(16717056), (int)(255.0F * factor)));
         }

         FontRenderers.sf_bold_mini.drawCenteredString(context.method_51448(), this.getItemCount(targetItem).makeConcatWithConstants<invokedynamic>(this.getItemCount(targetItem)), (double)(xPos + 28.5F), (double)(yPos + 8.0F), -1);
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
         context.method_51448().method_22909();
      }
   }

   @EventHandler
   public void onEatFood(EventEatFood e) {
      if (e.getFood().method_7909() == class_1802.field_8463 || e.getFood().method_7909() == class_1802.field_8367) {
         this.angle = 15.0F;
      }

   }

   public void onUpdate() {
      this.prevAngle = this.angle;
      if (this.angle > 0.0F) {
         --this.angle;
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
