package tsunami.core.manager.client;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayDeque;
import java.util.Iterator;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_2761;
import net.minecraft.class_640;
import org.jetbrains.annotations.NotNull;
import tsunami.core.manager.IManager;
import tsunami.events.impl.PacketEvent;
import tsunami.utility.math.MathUtility;

public class ServerManager implements IManager {
   private final ArrayDeque<Float> tpsResult = new ArrayDeque(20);
   private long time;
   private long tickTime;
   private float tps;

   public float getTPS() {
      return round2((double)this.tps);
   }

   public float getTPS2() {
      return round2((double)(20.0F * ((float)this.tickTime / 1000.0F)));
   }

   public float getTPSFactor() {
      return (float)this.tickTime / 1000.0F;
   }

   public static float round2(double value) {
      BigDecimal bd = new BigDecimal(value);
      bd = bd.setScale(2, RoundingMode.HALF_UP);
      return bd.floatValue();
   }

   @EventHandler
   public void onPacketReceive(@NotNull PacketEvent.Receive event) {
      if (event.getPacket() instanceof class_2761) {
         if (this.time != 0L) {
            this.tickTime = System.currentTimeMillis() - this.time;
            if (this.tpsResult.size() > 20) {
               this.tpsResult.poll();
            }

            this.tpsResult.add(20.0F * (1000.0F / (float)this.tickTime));
            float average = 0.0F;

            Float value;
            for(Iterator var3 = this.tpsResult.iterator(); var3.hasNext(); average += MathUtility.clamp(value, 0.0F, 20.0F)) {
               value = (Float)var3.next();
            }

            this.tps = average / (float)this.tpsResult.size();
         }

         this.time = System.currentTimeMillis();
      }

   }

   public int getPing() {
      if (mc.method_1562() != null && mc.field_1724 != null) {
         if (ModuleManager.fastLatency.isEnabled()) {
            return ModuleManager.fastLatency.resolvedPing;
         } else {
            class_640 playerListEntry = mc.method_1562().method_2871(mc.field_1724.method_5667());
            return playerListEntry == null ? 0 : playerListEntry.method_2959();
         }
      } else {
         return 0;
      }
   }
}
