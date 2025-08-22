package tsunami.features.modules.render;

import net.minecraft.class_4587;
import net.minecraft.class_5498;
import tsunami.features.modules.Module;
import tsunami.setting.Setting;
import tsunami.utility.render.animation.AnimationUtility;

public class NoCameraClip extends Module {
   public Setting<Boolean> antiFront = new Setting("AntiFront", false);
   public Setting<Float> distance = new Setting("Distance", 3.0F, 1.0F, 20.0F);
   private float animation;

   public NoCameraClip() {
      super("NoCameraClip", Module.Category.NONE);
   }

   public void onRender3D(class_4587 matrix) {
      if (mc.field_1690.method_31044() == class_5498.field_26664) {
         this.animation = AnimationUtility.fast(this.animation, 0.0F, 10.0F);
      } else {
         this.animation = AnimationUtility.fast(this.animation, 1.0F, 10.0F);
      }

      if (mc.field_1690.method_31044() == class_5498.field_26666 && (Boolean)this.antiFront.getValue()) {
         mc.field_1690.method_31043(class_5498.field_26664);
      }

   }

   public float getDistance() {
      return 1.0F + ((Float)this.distance.getValue() - 1.0F) * this.animation;
   }
}
