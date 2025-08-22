package tsunami.features.modules.render;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_238;
import net.minecraft.class_4587;
import tsunami.features.modules.Module;
import tsunami.setting.Setting;
import tsunami.setting.impl.ColorSetting;
import tsunami.utility.render.Render3DEngine;

public class VoidESP extends Module {
   public final Setting<ColorSetting> color = new Setting("Color", new ColorSetting(new Color(-671111680, true)));
   public Setting<Float> range = new Setting("Range", 6.0F, 3.0F, 16.0F);
   private List<class_2338> holes = new ArrayList();

   public VoidESP() {
      super("VoidESP", Module.Category.RENDER);
   }

   public void onRender3D(class_4587 stack) {
      this.holes.forEach((h) -> {
         Render3DEngine.renderCrosses(new class_238(h), ((ColorSetting)this.color.getValue()).getColorObject(), 2.0F);
      });
   }

   public List<class_2338> calcHoles() {
      ArrayList<class_2338> voidHoles = new ArrayList();

      for(int x = (int)(mc.field_1724.method_23317() - (double)(Float)this.range.getValue()); (double)x < mc.field_1724.method_23317() + (double)(Float)this.range.getValue(); ++x) {
         for(int z = (int)(mc.field_1724.method_23321() - (double)(Float)this.range.getValue()); (double)z < mc.field_1724.method_23321() + (double)(Float)this.range.getValue(); ++z) {
            class_2338 pos = class_2338.method_49637((double)x, (double)mc.field_1687.method_31607(), (double)z);
            if (mc.field_1687.method_8320(pos).method_26204() != class_2246.field_9987) {
               voidHoles.add(pos);
            }
         }
      }

      return voidHoles;
   }

   public void onThread() {
      this.holes = this.calcHoles();
   }
}
