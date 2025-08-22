package tsunami.utility.render;

import java.awt.Color;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.class_2338;
import net.minecraft.class_238;
import net.minecraft.class_4587;

public final class BlockAnimationUtility {
   private static final Map<BlockAnimationUtility.BlockRenderData, Long> blocks = new ConcurrentHashMap();

   public static void onRender(class_4587 matrixStack) {
      blocks.forEach((animation, time) -> {
         if ((float)(System.currentTimeMillis() - time) > 300.0F) {
            blocks.remove(animation);
         } else {
            animation.renderWithTime(System.currentTimeMillis() - time, matrixStack);
         }

      });
   }

   public static void renderBlock(class_2338 pos, Color lineColor, int lineWidth, Color fillColor, BlockAnimationUtility.BlockAnimationMode animationMode, BlockAnimationUtility.BlockRenderMode renderMode) {
      if (renderMode != BlockAnimationUtility.BlockRenderMode.None) {
         blocks.put(new BlockAnimationUtility.BlockRenderData(pos, lineColor, lineWidth, fillColor, animationMode, renderMode), System.currentTimeMillis());
      }
   }

   public static boolean isRendering(class_2338 pos) {
      return blocks.keySet().stream().anyMatch((blockRenderData) -> {
         return blockRenderData.pos().equals(pos);
      });
   }

   public static enum BlockRenderMode {
      Fill,
      Line,
      All,
      None;

      // $FF: synthetic method
      private static BlockAnimationUtility.BlockRenderMode[] $values() {
         return new BlockAnimationUtility.BlockRenderMode[]{Fill, Line, All, None};
      }
   }

   private static record BlockRenderData(class_2338 pos, Color lineColor, int lineWidth, Color fillColor, BlockAnimationUtility.BlockAnimationMode animationMode, BlockAnimationUtility.BlockRenderMode renderMode) {
      private BlockRenderData(class_2338 pos, Color lineColor, int lineWidth, Color fillColor, BlockAnimationUtility.BlockAnimationMode animationMode, BlockAnimationUtility.BlockRenderMode renderMode) {
         this.pos = pos;
         this.lineColor = lineColor;
         this.lineWidth = lineWidth;
         this.fillColor = fillColor;
         this.animationMode = animationMode;
         this.renderMode = renderMode;
      }

      void renderWithTime(Long time, class_4587 stack) {
         float scale;
         class_238 box;
         switch(this.animationMode.ordinal()) {
         case 0:
            class_238 box = new class_238(this.pos);
            renderBox(time, stack, box, this.renderMode, this.lineColor, this.lineWidth, this.fillColor);
            break;
         case 1:
            scale = 1.0F + (float)time / 1500.0F;
            box = new class_238((double)this.pos.method_10263(), (double)this.pos.method_10264(), (double)this.pos.method_10260(), (double)this.pos.method_10263(), (double)this.pos.method_10264(), (double)this.pos.method_10260());
            if (this.renderMode == BlockAnimationUtility.BlockRenderMode.All || this.renderMode == BlockAnimationUtility.BlockRenderMode.Line) {
               Render3DEngine.drawBoxOutline(box.method_1002((double)scale, (double)scale, (double)scale).method_989(0.5D + (double)(scale * 0.5F), 0.5D + (double)scale * 0.5D, 0.5D + (double)scale * 0.5D), Render2DEngine.injectAlpha(this.lineColor, (int)((float)this.lineColor.getAlpha() * (1.0F - (float)time / 300.0F))), (float)this.lineWidth);
            }

            if (this.renderMode == BlockAnimationUtility.BlockRenderMode.All || this.renderMode == BlockAnimationUtility.BlockRenderMode.Fill) {
               Render3DEngine.drawFilledBox(stack, box.method_1002((double)scale, (double)scale, (double)scale).method_989(0.5D + (double)scale * 0.5D, 0.5D + (double)scale * 0.5D, 0.5D + (double)scale * 0.5D), Render2DEngine.injectAlpha(this.fillColor, (int)((float)this.fillColor.getAlpha() * (1.0F - (float)time / 300.0F))));
            }
            break;
         case 2:
            scale = 1.0F - (float)time / 300.0F;
            box = new class_238((double)this.pos.method_10263(), (double)this.pos.method_10264(), (double)this.pos.method_10260(), (double)this.pos.method_10263(), (double)this.pos.method_10264(), (double)this.pos.method_10260());
            if (this.renderMode == BlockAnimationUtility.BlockRenderMode.All || this.renderMode == BlockAnimationUtility.BlockRenderMode.Line) {
               Render3DEngine.drawBoxOutline(box.method_1002((double)scale, (double)scale, (double)scale).method_989(0.5D + (double)scale * 0.5D, 0.5D + (double)scale * 0.5D, 0.5D + (double)scale * 0.5D), this.lineColor, (float)this.lineWidth);
            }

            if (this.renderMode == BlockAnimationUtility.BlockRenderMode.All || this.renderMode == BlockAnimationUtility.BlockRenderMode.Fill) {
               Render3DEngine.drawFilledBox(stack, box.method_1002((double)scale, (double)scale, (double)scale).method_989(0.5D + (double)scale * 0.5D, 0.5D + (double)scale * 0.5D, 0.5D + (double)scale * 0.5D), Render2DEngine.injectAlpha(this.fillColor, (int)((float)this.fillColor.getAlpha() * (1.0F - (float)time / 300.0F))));
            }
            break;
         case 3:
            if (this.renderMode == BlockAnimationUtility.BlockRenderMode.All || this.renderMode == BlockAnimationUtility.BlockRenderMode.Line) {
               Render3DEngine.drawBoxOutline(new class_238(this.pos), this.lineColor, (float)this.lineWidth);
            }

            if (this.renderMode == BlockAnimationUtility.BlockRenderMode.All || this.renderMode == BlockAnimationUtility.BlockRenderMode.Fill) {
               Render3DEngine.drawFilledBox(stack, new class_238(this.pos), this.fillColor);
            }
            break;
         case 4:
            if (time > 100L) {
               scale = 1.0F - (float)(time - 100L) / 400.0F;
            } else {
               scale = (float)time / 100.0F;
            }

            box = new class_238((double)this.pos.method_10263(), (double)this.pos.method_10264(), (double)this.pos.method_10260(), (double)this.pos.method_10263(), (double)this.pos.method_10264(), (double)this.pos.method_10260());
            if (this.renderMode == BlockAnimationUtility.BlockRenderMode.All || this.renderMode == BlockAnimationUtility.BlockRenderMode.Line) {
               Render3DEngine.drawBoxOutline(box.method_1002((double)scale, (double)scale, (double)scale).method_989(0.5D + (double)scale * 0.5D, 0.5D + (double)scale * 0.5D, 0.5D + (double)scale * 0.5D), this.lineColor, (float)this.lineWidth);
            }

            if (this.renderMode == BlockAnimationUtility.BlockRenderMode.All || this.renderMode == BlockAnimationUtility.BlockRenderMode.Fill) {
               Render3DEngine.drawFilledBox(stack, box.method_1002((double)scale, (double)scale, (double)scale).method_989(0.5D + (double)scale * 0.5D, 0.5D + (double)scale * 0.5D, 0.5D + (double)scale * 0.5D), Render2DEngine.injectAlpha(this.fillColor, (int)((float)this.fillColor.getAlpha() * scale)));
            }
            break;
         case 5:
            scale = (float)time / 300.0F;
            box = new class_238((double)this.pos.method_10263(), (double)((float)this.pos.method_10264() + scale), (double)this.pos.method_10260(), (double)(this.pos.method_10263() + 1), (double)this.pos.method_10264(), (double)(this.pos.method_10260() + 1));
            if (this.renderMode == BlockAnimationUtility.BlockRenderMode.All || this.renderMode == BlockAnimationUtility.BlockRenderMode.Line) {
               Render3DEngine.drawBoxOutline(box, this.lineColor, (float)this.lineWidth);
            }

            if (this.renderMode == BlockAnimationUtility.BlockRenderMode.All || this.renderMode == BlockAnimationUtility.BlockRenderMode.Fill) {
               Render3DEngine.drawFilledBox(stack, box, Render2DEngine.injectAlpha(this.fillColor, (int)((float)this.fillColor.getAlpha() * ((float)time / 300.0F))));
            }
            break;
         case 6:
            scale = (float)time / 300.0F;
            box = new class_238((double)this.pos.method_10263(), (double)this.pos.method_10264(), (double)this.pos.method_10260(), (double)this.pos.method_10263(), (double)this.pos.method_10264(), (double)this.pos.method_10260());
            if (this.renderMode == BlockAnimationUtility.BlockRenderMode.All || this.renderMode == BlockAnimationUtility.BlockRenderMode.Line) {
               Render3DEngine.drawBoxOutline(box.method_1002((double)scale, (double)scale, (double)scale).method_989(0.5D + (double)scale * 0.5D, 0.5D + (double)scale * 0.5D, 0.5D + (double)scale * 0.5D), this.lineColor, (float)this.lineWidth);
            }

            if (this.renderMode == BlockAnimationUtility.BlockRenderMode.All || this.renderMode == BlockAnimationUtility.BlockRenderMode.Fill) {
               Render3DEngine.drawFilledBox(stack, box.method_1002((double)scale, (double)scale, (double)scale).method_989(0.5D + (double)scale * 0.5D, 0.5D + (double)scale * 0.5D, 0.5D + (double)scale * 0.5D), Render2DEngine.injectAlpha(this.fillColor, (int)((float)this.fillColor.getAlpha() * ((float)time / 300.0F))));
            }
            break;
         case 7:
            if (time < 200L) {
               scale = 1.0F;
            } else {
               scale = 1.0F + ((float)time - 200.0F) / 400.0F;
            }

            box = new class_238((double)this.pos.method_10263(), (double)this.pos.method_10264(), (double)this.pos.method_10260(), (double)this.pos.method_10263(), (double)this.pos.method_10264(), (double)this.pos.method_10260());
            if (this.renderMode == BlockAnimationUtility.BlockRenderMode.All || this.renderMode == BlockAnimationUtility.BlockRenderMode.Line) {
               Render3DEngine.drawBoxOutline(box.method_1002((double)scale, (double)scale, (double)scale).method_989(0.5D + (double)(scale * 0.5F), 0.5D + (double)scale * 0.5D, 0.5D + (double)scale * 0.5D), this.lineColor, (float)this.lineWidth);
            }

            if (this.renderMode == BlockAnimationUtility.BlockRenderMode.All || this.renderMode == BlockAnimationUtility.BlockRenderMode.Fill) {
               Render3DEngine.drawFilledBox(stack, box.method_1002((double)scale, (double)scale, (double)scale).method_989(0.5D + (double)scale * 0.5D, 0.5D + (double)scale * 0.5D, 0.5D + (double)scale * 0.5D), Render2DEngine.injectAlpha(this.fillColor, (int)((float)this.fillColor.getAlpha() * scale)));
            }
            break;
         case 8:
            if (time < 200L) {
               scale = 1.5F - (float)time / 200.0F * 0.5F;
            } else {
               scale = 1.0F;
            }

            box = new class_238((double)this.pos.method_10263(), (double)this.pos.method_10264(), (double)this.pos.method_10260(), (double)this.pos.method_10263(), (double)this.pos.method_10264(), (double)this.pos.method_10260());
            if (this.renderMode == BlockAnimationUtility.BlockRenderMode.All || this.renderMode == BlockAnimationUtility.BlockRenderMode.Line) {
               Render3DEngine.drawBoxOutline(box.method_1002((double)scale, (double)scale, (double)scale).method_989(0.5D + (double)(scale * 0.5F), 0.5D + (double)scale * 0.5D, 0.5D + (double)scale * 0.5D), this.lineColor, (float)this.lineWidth);
            }

            if (this.renderMode == BlockAnimationUtility.BlockRenderMode.All || this.renderMode == BlockAnimationUtility.BlockRenderMode.Fill) {
               Render3DEngine.drawFilledBox(stack, box.method_1002((double)scale, (double)scale, (double)scale).method_989(0.5D + (double)scale * 0.5D, 0.5D + (double)scale * 0.5D, 0.5D + (double)scale * 0.5D), Render2DEngine.injectAlpha(this.fillColor, (int)((float)this.fillColor.getAlpha() * scale)));
            }
         }

      }

      private static void renderBox(Long time, class_4587 stack, class_238 box, BlockAnimationUtility.BlockRenderMode renderMode, Color lineColor, int lineWidth, Color fillColor) {
         if (renderMode == BlockAnimationUtility.BlockRenderMode.All || renderMode == BlockAnimationUtility.BlockRenderMode.Line) {
            Render3DEngine.drawBoxOutline(box, Render2DEngine.injectAlpha(lineColor, (int)((float)fillColor.getAlpha() * (1.0F - (float)time / 300.0F))), (float)lineWidth);
         }

         if (renderMode == BlockAnimationUtility.BlockRenderMode.All || renderMode == BlockAnimationUtility.BlockRenderMode.Fill) {
            Render3DEngine.drawFilledBox(stack, box, Render2DEngine.injectAlpha(fillColor, (int)((float)fillColor.getAlpha() * (1.0F - (float)time / 300.0F))));
         }

      }

      public class_2338 pos() {
         return this.pos;
      }

      public Color lineColor() {
         return this.lineColor;
      }

      public int lineWidth() {
         return this.lineWidth;
      }

      public Color fillColor() {
         return this.fillColor;
      }

      public BlockAnimationUtility.BlockAnimationMode animationMode() {
         return this.animationMode;
      }

      public BlockAnimationUtility.BlockRenderMode renderMode() {
         return this.renderMode;
      }
   }

   public static enum BlockAnimationMode {
      Fade,
      Hover,
      Decrease,
      Static,
      Flash,
      Grow,
      Fill,
      TNT,
      Pull;

      // $FF: synthetic method
      private static BlockAnimationUtility.BlockAnimationMode[] $values() {
         return new BlockAnimationUtility.BlockAnimationMode[]{Fade, Hover, Decrease, Static, Flash, Grow, Fill, TNT, Pull};
      }
   }
}
