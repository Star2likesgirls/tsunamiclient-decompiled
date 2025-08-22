package tsunami.features.modules.render;

import java.awt.Color;
import net.minecraft.class_332;
import tsunami.features.modules.Module;
import tsunami.utility.math.MathUtility;
import tsunami.utility.render.Render2DEngine;

public class DamageTint extends Module {
   public DamageTint() {
      super("DamageTint", Module.Category.NONE);
   }

   public void onRender2D(class_332 context) {
      float factor = 1.0F - MathUtility.clamp(mc.field_1724.method_6032(), 0.0F, 12.0F) / 12.0F;
      Color red = new Color(16711680, true);
      if (factor < 1.0F) {
         Render2DEngine.draw2DGradientRect(context.method_51448(), 0.0F, 0.0F, (float)mc.method_22683().method_4486(), (float)mc.method_22683().method_4502(), Render2DEngine.injectAlpha(red, (int)(factor * 170.0F)), red, Render2DEngine.injectAlpha(red, (int)(factor * 170.0F)), red);
      }

   }
}
