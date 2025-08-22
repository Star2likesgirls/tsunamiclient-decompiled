package tsunami.features.modules.movement;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_1713;
import net.minecraft.class_2596;
import net.minecraft.class_2813;
import net.minecraft.class_2815;
import net.minecraft.class_2848;
import net.minecraft.class_304;
import net.minecraft.class_3675;
import net.minecraft.class_408;
import net.minecraft.class_2828.class_2829;
import net.minecraft.class_2848.class_2849;
import tsunami.events.impl.EventClickSlot;
import tsunami.events.impl.PacketEvent;
import tsunami.features.modules.Module;
import tsunami.setting.Setting;
import tsunami.utility.player.MovementUtility;

public class GuiMove extends Module {
   private final Setting<GuiMove.Bypass> clickBypass;
   private final Setting<Boolean> rotateOnArrows;
   private final Setting<Boolean> sneak;
   private final Queue<class_2813> storedClicks;
   private AtomicBoolean pause;

   public GuiMove() {
      super("GuiMove", Module.Category.MOVEMENT);
      this.clickBypass = new Setting("Bypass", GuiMove.Bypass.None);
      this.rotateOnArrows = new Setting("RotateOnArrows", true);
      this.sneak = new Setting("sneak", false);
      this.storedClicks = new LinkedList();
      this.pause = new AtomicBoolean();
   }

   public void onUpdate() {
      if (mc.field_1755 != null && !(mc.field_1755 instanceof class_408)) {
         class_304[] var1 = new class_304[]{mc.field_1690.field_1894, mc.field_1690.field_1881, mc.field_1690.field_1913, mc.field_1690.field_1849, mc.field_1690.field_1903, mc.field_1690.field_1867};
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            class_304 k = var1[var3];
            k.method_23481(this.isKeyPressed(class_3675.method_15981(k.method_1428()).method_1444()));
         }

         float deltaX = 0.0F;
         float deltaY = 0.0F;
         if ((Boolean)this.rotateOnArrows.getValue()) {
            if (this.isKeyPressed(264)) {
               deltaY += 30.0F;
            }

            if (this.isKeyPressed(265)) {
               deltaY -= 30.0F;
            }

            if (this.isKeyPressed(262)) {
               deltaX += 30.0F;
            }

            if (this.isKeyPressed(263)) {
               deltaX -= 30.0F;
            }

            if (deltaX != 0.0F || deltaY != 0.0F) {
               mc.field_1724.method_5872((double)deltaX, (double)deltaY);
            }
         }

         if ((Boolean)this.sneak.getValue()) {
            mc.field_1690.field_1832.method_23481(this.isKeyPressed(class_3675.method_15981(mc.field_1690.field_1832.method_1428()).method_1444()));
         }
      }

   }

   @EventHandler
   public void onClickSlot(EventClickSlot e) {
      if (this.clickBypass.is(GuiMove.Bypass.DisableClicks) && (MovementUtility.isMoving() || mc.field_1690.field_1903.method_1434())) {
         e.cancel();
      }

   }

   @EventHandler
   public void onPacketSend(PacketEvent.Send e) {
      if (MovementUtility.isMoving() && mc.field_1690.field_1903.method_1434() && !this.pause.get()) {
         class_2596 var3 = e.getPacket();
         if (var3 instanceof class_2813) {
            class_2813 click = (class_2813)var3;
            switch(((GuiMove.Bypass)this.clickBypass.getValue()).ordinal()) {
            case 2:
               if (mc.field_1724.method_24828() && !mc.field_1687.method_20812(mc.field_1724, mc.field_1724.method_5829().method_989(0.0D, 0.0656D, 0.0D)).iterator().hasNext()) {
                  if (mc.field_1724.method_5624()) {
                     this.sendPacket(new class_2848(mc.field_1724, class_2849.field_12985));
                  }

                  this.sendPacket(new class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318() + 0.0656D, mc.field_1724.method_23321(), false));
               }
               break;
            case 3:
               if (click.method_12195() != class_1713.field_7790 && click.method_12195() != class_1713.field_7793) {
                  this.sendPacket(new class_2815(0));
               }
               break;
            case 4:
               this.sendPacket(new class_2848(mc.field_1724, class_2849.field_12985));
               mc.field_1690.field_1894.method_23481(false);
               mc.field_1724.field_3913.field_3905 = 0.0F;
               mc.field_1724.field_3913.field_3910 = false;
               break;
            case 5:
               this.storedClicks.add(click);
               e.cancel();
               break;
            case 6:
               if (mc.field_1724.method_24828() && !mc.field_1687.method_20812(mc.field_1724, mc.field_1724.method_5829().method_989(0.0D, 2.71875E-7D, 0.0D)).iterator().hasNext()) {
                  if (mc.field_1724.method_5624()) {
                     this.sendPacket(new class_2848(mc.field_1724, class_2849.field_12985));
                  }

                  this.sendPacket(new class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318() + 2.71875E-7D, mc.field_1724.method_23321(), false));
               }
            }
         }

         if (e.getPacket() instanceof class_2815 && this.clickBypass.is(GuiMove.Bypass.Delay)) {
            this.pause.set(true);

            while(!this.storedClicks.isEmpty()) {
               this.sendPacket((class_2596)this.storedClicks.poll());
            }

            this.pause.set(false);
         }

      }
   }

   @EventHandler
   public void onPacketSendPost(PacketEvent.SendPost e) {
      if (e.getPacket() instanceof class_2813 && mc.field_1724.method_5624() && this.clickBypass.is(GuiMove.Bypass.StrictNCP)) {
         this.sendPacket(new class_2848(mc.field_1724, class_2849.field_12981));
      }

   }

   private static enum Bypass {
      DisableClicks,
      None,
      StrictNCP,
      GrimSwap,
      MatrixNcp,
      Delay,
      StrictNCP2;

      // $FF: synthetic method
      private static GuiMove.Bypass[] $values() {
         return new GuiMove.Bypass[]{DisableClicks, None, StrictNCP, GrimSwap, MatrixNcp, Delay, StrictNCP2};
      }
   }
}
