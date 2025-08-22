package tsunami.features.modules.player;

import net.minecraft.class_1713;
import net.minecraft.class_1799;
import tsunami.features.modules.Module;
import tsunami.setting.Setting;
import tsunami.utility.Timer;

public class HotbarReplenish extends Module {
   private final Setting<HotbarReplenish.Mode> mode;
   private final Setting<Integer> delay;
   private final Setting<Integer> refillThr;
   private final Setting<Integer> refillSmallThr;
   private final Timer timer;

   public HotbarReplenish() {
      super("HotbarReplenish", Module.Category.NONE);
      this.mode = new Setting("Mode", HotbarReplenish.Mode.SWAP);
      this.delay = new Setting("Delay", 2, 0, 10);
      this.refillThr = new Setting("Threshold", 16, 1, 63);
      this.refillSmallThr = new Setting("PearlsThreshold", 4, 1, 15);
      this.timer = new Timer();
   }

   public void onUpdate() {
      if (mc.field_1755 == null) {
         if (this.timer.passedMs((long)((Integer)this.delay.getValue() * 1000))) {
            for(int i = 0; i < 9; ++i) {
               if (this.need(i)) {
                  this.timer.reset();
                  return;
               }
            }

         }
      }
   }

   private boolean need(int slot) {
      class_1799 stack = mc.field_1724.method_31548().method_5438(slot);
      if (!stack.method_7960() && stack.method_7946()) {
         if (stack.method_7914() == 16 && stack.method_7947() > (Integer)this.refillSmallThr.getValue()) {
            return false;
         } else if (stack.method_7914() == 64 && stack.method_7947() > (Integer)this.refillThr.getValue()) {
            return false;
         } else {
            for(int i = 9; i < 36; ++i) {
               class_1799 item = mc.field_1724.method_31548().method_5438(i);
               if (!item.method_7960() && this.canMerge(stack, item)) {
                  boolean swap = this.mode.is(HotbarReplenish.Mode.QUICK_MOVE);
                  clickSlot(i, swap ? slot : 0, swap ? class_1713.field_7791 : class_1713.field_7794);
                  return true;
               }
            }

            return false;
         }
      } else {
         return false;
      }
   }

   private boolean canMerge(class_1799 source, class_1799 stack) {
      return source.method_7909() == stack.method_7909() && source.method_7964().equals(stack.method_7964());
   }

   private static enum Mode {
      QUICK_MOVE,
      SWAP;

      // $FF: synthetic method
      private static HotbarReplenish.Mode[] $values() {
         return new HotbarReplenish.Mode[]{QUICK_MOVE, SWAP};
      }
   }
}
