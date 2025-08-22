package tsunami.features.modules.movement;

import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_1268;
import net.minecraft.class_1747;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_243;
import net.minecraft.class_2846;
import net.minecraft.class_2848;
import net.minecraft.class_2868;
import net.minecraft.class_2885;
import net.minecraft.class_3965;
import net.minecraft.class_746;
import net.minecraft.class_2846.class_2847;
import net.minecraft.class_2848.class_2849;
import tsunami.events.impl.EventSync;
import tsunami.features.modules.Module;
import tsunami.setting.Setting;
import tsunami.utility.player.MovementUtility;

public class Spider extends Module {
   public final Setting<Integer> delay = new Setting("delay", 2, 1, 15);
   private final Setting<Spider.Mode> mode;

   public Spider() {
      super("Spider", Module.Category.NONE);
      this.mode = new Setting("Mode", Spider.Mode.Matrix);
   }

   public static class_2350 getPlaceableSide(class_2338 pos) {
      class_2350[] var1 = class_2350.values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         class_2350 side = var1[var3];
         class_2338 neighbour = pos.method_10093(side);
         if (!mc.field_1687.method_22347(neighbour) && !mc.field_1687.method_8320(neighbour).method_45474()) {
            return side;
         }
      }

      return null;
   }

   public void onUpdate() {
      if (mc.field_1724.field_5976) {
         if (mc.field_1724.field_6012 % 2 == 0 && mc.field_1690.field_1903.method_1434() && this.mode.getValue() == Spider.Mode.FunTime) {
            float pitch = mc.field_1724.method_36455();
            mc.field_1724.method_36457(82.0F);
            int slot = this.getAtHotBar();
            if (slot != -1) {
               int originalSlot = mc.field_1724.method_31548().field_7545;
               mc.field_1724.method_31548().field_7545 = slot;
               this.sendPacket(new class_2868(slot));
               mc.field_1761.method_2919(mc.field_1724, class_1268.field_5808);
               mc.field_1724.method_6104(class_1268.field_5808);
               mc.field_1724.method_31548().field_7545 = originalSlot;
               this.sendPacket(new class_2868(originalSlot));
            }

            mc.field_1724.method_36457(pitch);
         }

         if (this.mode.getValue() == Spider.Mode.Default) {
            mc.field_1724.method_18800(mc.field_1724.method_18798().method_10216(), 0.21D, mc.field_1724.method_18798().method_10215());
         } else if (this.mode.getValue() == Spider.Mode.Matrix) {
            mc.field_1724.method_24830(mc.field_1724.field_6012 % (Integer)this.delay.getValue() == 0);
            class_746 var10000 = mc.field_1724;
            var10000.field_6036 -= 2.0E-232D;
            if (mc.field_1724.method_24828()) {
               mc.field_1724.method_18800(mc.field_1724.method_18798().method_10216(), 0.42D, mc.field_1724.method_18798().method_10215());
            }
         }

      }
   }

   @EventHandler
   public void onSync(EventSync event) {
      if (mc.field_1690.field_1903.method_1434() && mc.field_1724.method_18798().method_10214() <= -0.3739040364667221D && this.mode.getValue() == Spider.Mode.MatrixNew) {
         mc.field_1724.method_24830(true);
         mc.field_1724.method_18800(mc.field_1724.method_18798().method_10216(), 0.48114514191918D, mc.field_1724.method_18798().method_10215());
      }

      if (mc.field_1724.field_6012 % (Integer)this.delay.getValue() == 0 && mc.field_1724.field_5976 && MovementUtility.isMoving() && this.mode.getValue() == Spider.Mode.Blocks) {
         int find = -2;

         for(int i = 0; i <= 8; ++i) {
            if (mc.field_1724.method_31548().method_5438(i).method_7909() instanceof class_1747) {
               find = i;
            }
         }

         if (find == -2) {
            return;
         }

         class_2338 pos = class_2338.method_49637(mc.field_1724.method_23317(), mc.field_1724.method_23318() + 2.0D, mc.field_1724.method_23321());
         class_2350 side = getPlaceableSide(pos);
         if (side != null) {
            this.sendPacket(new class_2868(find));
            class_2338 neighbour = class_2338.method_49637(mc.field_1724.method_23317(), mc.field_1724.method_23318() + 2.0D, mc.field_1724.method_23321()).method_10093(side);
            class_2350 opposite = side.method_10153();
            class_243 hitVec = (new class_243((double)neighbour.method_10263() + 0.5D, (double)neighbour.method_10264() + 0.5D, (double)neighbour.method_10260() + 0.5D)).method_1019((new class_243(opposite.method_23955())).method_1021(0.5D));
            this.sendSequencedPacket((id) -> {
               return new class_2885(class_1268.field_5808, new class_3965(hitVec, opposite, neighbour, false), id);
            });
            this.sendPacket(new class_2848(mc.field_1724, class_2849.field_12979));
            if (mc.field_1687.method_8320(class_2338.method_49638(mc.field_1724.method_19538()).method_10069(0, 2, 0)).method_26204() != class_2246.field_10124) {
               this.sendPacket(new class_2846(class_2847.field_12968, neighbour, opposite));
               this.sendPacket(new class_2846(class_2847.field_12973, neighbour, opposite));
            }

            this.sendPacket(new class_2848(mc.field_1724, class_2849.field_12984));
         }

         mc.field_1724.method_24830(true);
         mc.field_1724.method_6043();
         this.sendPacket(new class_2868(mc.field_1724.method_31548().field_7545));
      }

   }

   private int getAtHotBar() {
      for(int i = 0; i < 9; ++i) {
         class_1799 itemStack = mc.field_1724.method_31548().method_5438(i);
         if (itemStack.method_7909() == class_1802.field_8705) {
            return i;
         }
      }

      return -1;
   }

   public static enum Mode {
      Default,
      Matrix,
      MatrixNew,
      Blocks,
      FunTime;

      // $FF: synthetic method
      private static Spider.Mode[] $values() {
         return new Spider.Mode[]{Default, Matrix, MatrixNew, Blocks, FunTime};
      }
   }
}
