package tsunami.features.modules.render;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_4587;
import org.jetbrains.annotations.NotNull;
import tsunami.features.modules.Module;
import tsunami.setting.Setting;
import tsunami.setting.impl.ColorSetting;
import tsunami.utility.Timer;
import tsunami.utility.math.MathUtility;
import tsunami.utility.player.PlayerUtility;
import tsunami.utility.render.Render2DEngine;
import tsunami.utility.render.Render3DEngine;
import tsunami.utility.world.HoleUtility;

public class HoleESP extends Module {
   private final Setting<HoleESP.Mode> mode;
   private final Setting<Integer> rangeXZ;
   private final Setting<Integer> rangeY;
   private final Setting<ColorSetting> indestrictibleColor;
   private final Setting<ColorSetting> bedrockColor;
   private final Setting<Float> height;
   private final Setting<Float> lineWith;
   public final Setting<Boolean> culling;
   private final Timer logicTimer;
   private final List<HoleESP.BoxWithColor> positions;

   public HoleESP() {
      super("HoleESP", Module.Category.NONE);
      this.mode = new Setting("Mode", HoleESP.Mode.CubeOutline);
      this.rangeXZ = new Setting("Range XY", 10, 1, 128);
      this.rangeY = new Setting("Range Y", 5, 1, 128);
      this.indestrictibleColor = new Setting("Indestructible", new ColorSetting((new Color(7995647)).getRGB()));
      this.bedrockColor = new Setting("Bedrock", new ColorSetting((new Color(65361)).getRGB()));
      this.height = new Setting("Height", 1.0F, 0.01F, 5.0F);
      this.lineWith = new Setting("Line Width", 0.5F, 0.01F, 5.0F);
      this.culling = new Setting("Culling", true, (v) -> {
         return this.mode.getValue() == HoleESP.Mode.Fade || this.mode.getValue() == HoleESP.Mode.Fade2;
      });
      this.logicTimer = new Timer();
      this.positions = new CopyOnWriteArrayList();
   }

   public void onDisable() {
      this.positions.clear();
   }

   public void onRender3D(class_4587 stack) {
      if (!this.positions.isEmpty()) {
         Iterator var2 = this.positions.iterator();

         while(var2.hasNext()) {
            HoleESP.BoxWithColor pwc = (HoleESP.BoxWithColor)var2.next();
            switch(((HoleESP.Mode)this.mode.getValue()).ordinal()) {
            case 0:
               this.renderFade(pwc);
               break;
            case 1:
               this.renderFade2(pwc);
               break;
            case 2:
               this.renderOutline(pwc);
               break;
            case 3:
               this.renderFill(pwc);
               break;
            case 4:
               this.renderOutline(pwc);
               this.renderFill(pwc);
            }
         }

      }
   }

   public void renderFade(@NotNull HoleESP.BoxWithColor posWithColor) {
      Render3DEngine.FADE_QUEUE.add(new Render3DEngine.FadeAction(posWithColor.box, this.getColor(posWithColor.box, posWithColor.color(), 60), this.getColor(posWithColor.box, posWithColor.color(), 0)));
      Render3DEngine.OUTLINE_SIDE_QUEUE.add(new Render3DEngine.OutlineSideAction(posWithColor.box, this.getColor(posWithColor.box, posWithColor.color(), posWithColor.color.getAlpha()), (Float)this.lineWith.getValue(), class_2350.field_11033));
   }

   public void renderFade2(@NotNull HoleESP.BoxWithColor boxWithColor) {
      Render3DEngine.FADE_QUEUE.add(new Render3DEngine.FadeAction(boxWithColor.box, this.getColor(boxWithColor.box, boxWithColor.color(), 60), this.getColor(boxWithColor.box, boxWithColor.color(), 0)));
      Render3DEngine.drawHoleOutline(boxWithColor.box, this.getColor(boxWithColor.box, boxWithColor.color(), boxWithColor.color.getAlpha()), (Float)this.lineWith.getValue());
      Render3DEngine.FILLED_QUEUE.add(new Render3DEngine.FillAction(new class_238(boxWithColor.box.field_1323, boxWithColor.box.field_1322, boxWithColor.box.field_1321, boxWithColor.box.field_1320, boxWithColor.box.field_1322 + 0.009999999776482582D, boxWithColor.box.field_1324), this.getColor(boxWithColor.box, boxWithColor.color(), boxWithColor.color.getAlpha())));
   }

   private Color getColor(class_238 box, Color color, int alpha) {
      float dist = PlayerUtility.squaredDistance2d(box.method_1005().method_10216(), box.method_1005().method_10215());
      float factor = dist / this.rangeXZ.getPow2Value();
      factor = 1.0F - this.easeOutExpo(factor);
      factor = MathUtility.clamp(factor, 0.0F, 1.0F);
      return Render2DEngine.injectAlpha(color, (int)(factor * (float)alpha));
   }

   private float easeOutExpo(float x) {
      return x == 1.0F ? 1.0F : (float)(1.0D - Math.pow(2.0D, (double)(-10.0F * x)));
   }

   public void renderOutline(@NotNull HoleESP.BoxWithColor boxWithColor) {
      Render3DEngine.OUTLINE_QUEUE.add(new Render3DEngine.OutlineAction(boxWithColor.box, this.getColor(boxWithColor.box, boxWithColor.color(), boxWithColor.color.getAlpha()), (Float)this.lineWith.getValue()));
   }

   public void renderFill(@NotNull HoleESP.BoxWithColor boxWithColor) {
      Render3DEngine.FILLED_QUEUE.add(new Render3DEngine.FillAction(boxWithColor.box(), this.getColor(boxWithColor.box, boxWithColor.color(), boxWithColor.color.getAlpha())));
   }

   public void onThread() {
      if (!fullNullCheck() && this.logicTimer.passedMs(500L)) {
         this.findHoles();
         this.logicTimer.reset();
      }
   }

   private void findHoles() {
      ArrayList<HoleESP.BoxWithColor> blocks = new ArrayList();
      if (mc.field_1687 != null && mc.field_1724 != null) {
         class_2338 centerPos = class_2338.method_49638(mc.field_1724.method_19538());
         List<class_238> boxes = new ArrayList();

         for(int i = centerPos.method_10263() - (Integer)this.rangeXZ.getValue(); i < centerPos.method_10263() + (Integer)this.rangeXZ.getValue(); ++i) {
            for(int j = centerPos.method_10264() - (Integer)this.rangeY.getValue(); j < centerPos.method_10264() + (Integer)this.rangeY.getValue(); ++j) {
               for(int k = centerPos.method_10260() - (Integer)this.rangeXZ.getValue(); k < centerPos.method_10260() + (Integer)this.rangeXZ.getValue(); ++k) {
                  class_2338 pos = new class_2338(i, j, k);
                  class_238 box = new class_238((double)pos.method_10263(), (double)pos.method_10264(), (double)pos.method_10260(), (double)(pos.method_10263() + 1), (double)((float)pos.method_10264() + (Float)this.height.getValue()), (double)(pos.method_10260() + 1));
                  Color color = ((ColorSetting)this.indestrictibleColor.getValue()).getColorObject();
                  boolean east;
                  if (!HoleUtility.validIndestructible(pos)) {
                     if (HoleUtility.validBedrock(pos)) {
                        color = ((ColorSetting)this.bedrockColor.getValue()).getColorObject();
                     } else {
                        boolean south;
                        if (HoleUtility.validTwoBlockBedrock(pos)) {
                           east = mc.field_1687.method_22347(pos.method_10093(class_2350.field_11034));
                           south = mc.field_1687.method_22347(pos.method_10093(class_2350.field_11035));
                           box = new class_238(box.field_1323, box.field_1322, box.field_1321, box.field_1320 + (double)(east ? 1 : 0), box.field_1325, box.field_1324 + (double)(south ? 1 : 0));
                           color = ((ColorSetting)this.bedrockColor.getValue()).getColorObject();
                        } else if (HoleUtility.validTwoBlockIndestructible(pos)) {
                           east = mc.field_1687.method_22347(pos.method_10093(class_2350.field_11034));
                           south = mc.field_1687.method_22347(pos.method_10093(class_2350.field_11035));
                           box = new class_238(box.field_1323, box.field_1322, box.field_1321, box.field_1320 + (double)(east ? 1 : 0), box.field_1325, box.field_1324 + (double)(south ? 1 : 0));
                        } else if (HoleUtility.validQuadBedrock(pos)) {
                           box = new class_238(box.field_1323, box.field_1322, box.field_1321, box.field_1320 + 1.0D, box.field_1325, box.field_1324 + 1.0D);
                           color = ((ColorSetting)this.bedrockColor.getValue()).getColorObject();
                        } else {
                           if (!HoleUtility.validQuadIndestructible(pos)) {
                              continue;
                           }

                           box = new class_238(box.field_1323, box.field_1322, box.field_1321, box.field_1320 + 1.0D, box.field_1325, box.field_1324 + 1.0D);
                        }
                     }
                  }

                  east = false;
                  Iterator var13 = boxes.iterator();

                  while(var13.hasNext()) {
                     class_238 boxOffset = (class_238)var13.next();
                     if (boxOffset.method_994(box)) {
                        east = true;
                     }
                  }

                  if (!east) {
                     blocks.add(new HoleESP.BoxWithColor(box, color));
                     boxes.add(box);
                  }
               }
            }
         }

         this.positions.clear();
         this.positions.addAll(blocks);
      } else {
         this.positions.clear();
      }
   }

   private static enum Mode {
      Fade,
      Fade2,
      CubeOutline,
      CubeFill,
      CubeBoth;

      // $FF: synthetic method
      private static HoleESP.Mode[] $values() {
         return new HoleESP.Mode[]{Fade, Fade2, CubeOutline, CubeFill, CubeBoth};
      }
   }

   public static record BoxWithColor(class_238 box, Color color) {
      public BoxWithColor(class_238 box, Color color) {
         this.box = box;
         this.color = color;
      }

      public class_238 box() {
         return this.box;
      }

      public Color color() {
         return this.color;
      }
   }
}
