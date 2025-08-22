package tsunami.utility.render.animation;

import net.minecraft.class_3532;
import tsunami.utility.render.Render3DEngine;

public class EaseOutBack {
   private int prevTick;
   private int tick;
   private final int maxTick;

   public EaseOutBack(int maxTick) {
      this.maxTick = maxTick;
   }

   public EaseOutBack() {
      this(10);
   }

   public static double dropAnimation(double value) {
      return 1.0D + 2.70158D * Math.pow(value - 1.0D, 3.0D) + 1.70158D * Math.pow(value - 1.0D, 2.0D);
   }

   public void update(boolean update) {
      this.prevTick = this.tick;
      this.tick = class_3532.method_15340(this.tick + (update ? 1 : -1), 0, this.maxTick);
   }

   public double getAnimationd() {
      return dropAnimation((double)(((float)this.prevTick + (float)(this.tick - this.prevTick) * Render3DEngine.getTickDelta()) / (float)this.maxTick));
   }

   public void reset() {
      this.prevTick = 0;
      this.tick = 0;
   }
}
