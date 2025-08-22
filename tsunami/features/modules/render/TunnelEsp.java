package tsunami.features.modules.render;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import net.minecraft.class_2338;
import net.minecraft.class_238;
import net.minecraft.class_4587;
import tsunami.features.modules.Module;
import tsunami.setting.Setting;
import tsunami.setting.impl.ColorSetting;
import tsunami.utility.Timer;
import tsunami.utility.render.Render2DEngine;
import tsunami.utility.render.Render3DEngine;

public class TunnelEsp extends Module {
   private final Setting<ColorSetting> color = new Setting("Color", new ColorSetting(new Color(-1366652170, true)));
   public Setting<Boolean> box = new Setting("Box", true);
   public Setting<Boolean> outline = new Setting("Outline", true);
   List<class_238> renderBoxes = new ArrayList();
   private Timer delayTimer = new Timer();

   public TunnelEsp() {
      super("TunnelEsp", Module.Category.NONE);
   }

   public void onRender3D(class_4587 stack) {
      try {
         Iterator var2 = this.renderBoxes.iterator();

         while(true) {
            class_238 box_;
            do {
               if (!var2.hasNext()) {
                  return;
               }

               box_ = (class_238)var2.next();
            } while(box_.method_17941() < 5.0D && box_.method_17939() < 5.0D);

            if ((Boolean)this.box.getValue()) {
               Render3DEngine.drawFilledBox(stack, box_, ((ColorSetting)this.color.getValue()).getColorObject());
            }

            if ((Boolean)this.outline.getValue()) {
               Render3DEngine.drawBoxOutline(box_, Render2DEngine.injectAlpha(((ColorSetting)this.color.getValue()).getColorObject(), 255), 2.0F);
            }
         }
      } catch (Exception var4) {
      }
   }

   public void onEnable() {
      this.renderBoxes.clear();
   }

   public void onThread() {
      if (this.delayTimer.passedMs(2000L)) {
         for(int x = (int)(mc.field_1724.method_23317() - 128.0D); (double)x < mc.field_1724.method_23317() + 128.0D; ++x) {
            for(int z = (int)(mc.field_1724.method_23321() - 128.0D); (double)z < mc.field_1724.method_23321() + 128.0D; ++z) {
               for(int y = 0; y < 121; ++y) {
                  class_2338 bp = new class_2338(x, y, z);
                  class_238 renderBox;
                  if (one_two(bp) && !this.alreadyIn(new class_238((double)bp.method_10263(), (double)bp.method_10264(), (double)bp.method_10260(), (double)(bp.method_10263() + 1), (double)(bp.method_10264() + 2), (double)(bp.method_10260() + 1)))) {
                     renderBox = new class_238((double)bp.method_10263(), (double)bp.method_10264(), (double)bp.method_10260(), (double)(bp.method_10263() + 1), (double)(bp.method_10264() + 2), (double)(bp.method_10260() + 1));
                     this.renderBoxes.add(this.getFullBox(renderBox, x, y, z, 1));
                  }

                  if (one_one(bp) && !this.alreadyIn(new class_238(bp))) {
                     renderBox = new class_238(bp);
                     this.renderBoxes.add(this.getFullBox(renderBox, x, y, z, 0));
                  }
               }
            }
         }

         this.delayTimer.reset();
      }

   }

   private class_238 getFullBox(class_238 raw, int x, int y, int z, int mode) {
      class_2338 checkBp1 = new class_2338(x, y, z + 1);

      Function check;
      for(check = this.getCheckByMode(mode); (Boolean)check.apply(checkBp1); checkBp1 = checkBp1.method_10072()) {
         raw = raw.method_35579(raw.field_1324 + 1.0D);
      }

      for(class_2338 checkBp2 = new class_2338(x + 1, y, z); (Boolean)check.apply(checkBp2); checkBp2 = checkBp2.method_10078()) {
         raw = raw.method_35577(raw.field_1320 + 1.0D);
      }

      for(class_2338 checkBp3 = new class_2338(x, y, z - 1); (Boolean)check.apply(checkBp3); checkBp3 = checkBp3.method_10095()) {
         raw = raw.method_35576(raw.field_1321 - 1.0D);
      }

      for(class_2338 checkBp4 = new class_2338(x - 1, y, z); (Boolean)check.apply(checkBp4); checkBp4 = checkBp4.method_10067()) {
         raw = raw.method_35574(raw.field_1323 - 1.0D);
      }

      return raw;
   }

   private Function<class_2338, Boolean> getCheckByMode(int mode) {
      Function var10000;
      switch(mode) {
      case 1:
         var10000 = TunnelEsp::one_two;
         break;
      case 2:
         var10000 = TunnelEsp::one_three;
         break;
      default:
         var10000 = TunnelEsp::one_one;
      }

      return var10000;
   }

   private boolean alreadyIn(class_238 box) {
      Iterator var2 = this.renderBoxes.iterator();

      class_238 box2;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         box2 = (class_238)var2.next();
      } while(!box.method_994(box2));

      return true;
   }

   private static boolean one_three(class_2338 pos) {
      if (isAir(pos) && isAir(pos.method_10084()) && isAir(pos.method_10084().method_10084())) {
         if (!isAir(pos.method_10074()) && !isAir(pos.method_10084().method_10084().method_10084())) {
            if (isAir(pos.method_10084().method_10095()) && isAir(pos.method_10084().method_10072())) {
               return !isAir(pos.method_10084().method_10078()) && !isAir(pos.method_10084().method_10067());
            } else if (isAir(pos.method_10084().method_10078()) && isAir(pos.method_10084().method_10067())) {
               return !isAir(pos.method_10084().method_10095()) && !isAir(pos.method_10084().method_10072());
            } else {
               return false;
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private static boolean one_two(class_2338 pos) {
      if (isAir(pos) && isAir(pos.method_10084())) {
         if (!isAir(pos.method_10074()) && !isAir(pos.method_10084().method_10084())) {
            if (isAir(pos.method_10095()) && isAir(pos.method_10072()) && isAir(pos.method_10084().method_10095()) && isAir(pos.method_10084().method_10072())) {
               return !isAir(pos.method_10078()) && !isAir(pos.method_10067()) && !isAir(pos.method_10084().method_10078()) && !isAir(pos.method_10084().method_10067());
            } else if (isAir(pos.method_10078()) && isAir(pos.method_10067()) && isAir(pos.method_10084().method_10078()) && isAir(pos.method_10084().method_10067())) {
               return !isAir(pos.method_10095()) && !isAir(pos.method_10072()) && !isAir(pos.method_10084().method_10095()) && !isAir(pos.method_10084().method_10072());
            } else {
               return false;
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private static boolean one_one(class_2338 pos) {
      if (!isAir(pos)) {
         return false;
      } else if (!isAir(pos.method_10074()) && !isAir(pos.method_10084())) {
         if (isAir(pos.method_10095()) && isAir(pos.method_10072())) {
            return !isAir(pos.method_10078()) && !isAir(pos.method_10067()) && !isAir(pos.method_10084().method_10078()) && !isAir(pos.method_10084().method_10067());
         } else if (isAir(pos.method_10078()) && isAir(pos.method_10067())) {
            return !isAir(pos.method_10095()) && !isAir(pos.method_10072());
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private static boolean isAir(class_2338 bp) {
      return mc.field_1687.method_22347(bp);
   }
}
