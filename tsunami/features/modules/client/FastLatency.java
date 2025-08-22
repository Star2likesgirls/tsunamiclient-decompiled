package tsunami.features.modules.client;

import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_2596;
import net.minecraft.class_2639;
import net.minecraft.class_2805;
import net.minecraft.class_4587;
import org.jetbrains.annotations.NotNull;
import tsunami.events.impl.PacketEvent;
import tsunami.features.modules.Module;
import tsunami.setting.Setting;
import tsunami.utility.Timer;
import tsunami.utility.math.MathUtility;

public final class FastLatency extends Module {
   private final Setting<Integer> delay = new Setting("Delay", 80, 0, 1000);
   private final Timer timer = new Timer();
   private final Timer limitTimer = new Timer();
   private long ping;
   public int resolvedPing;

   public FastLatency() {
      super("FastLatency", Module.Category.NONE);
   }

   public void onRender3D(class_4587 stack) {
      if (this.timer.passedMs(5000L) && this.limitTimer.every((long)(Integer)this.delay.getValue())) {
         this.sendPacket(new class_2805(1337, "w "));
         this.ping = System.currentTimeMillis();
         this.timer.reset();
      }

   }

   @EventHandler
   public void onPacketReceive(@NotNull PacketEvent.Receive e) {
      class_2596 var3 = e.getPacket();
      if (var3 instanceof class_2639) {
         class_2639 c = (class_2639)var3;
         if (c.comp_2262() == 1337) {
            this.resolvedPing = (int)MathUtility.clamp((float)(System.currentTimeMillis() - this.ping), 0.0F, 1000.0F);
            this.timer.setMs(5000L);
         }
      }

   }
}
