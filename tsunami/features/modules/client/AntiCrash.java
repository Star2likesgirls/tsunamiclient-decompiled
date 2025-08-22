package tsunami.features.modules.client;

import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_2596;
import net.minecraft.class_2664;
import net.minecraft.class_2675;
import net.minecraft.class_2708;
import org.jetbrains.annotations.NotNull;
import tsunami.events.impl.PacketEvent;
import tsunami.features.modules.Module;
import tsunami.setting.Setting;
import tsunami.utility.Timer;

public class AntiCrash extends Module {
   public final Setting<Boolean> debug = new Setting("Debug", false);
   private Timer debugTimer = new Timer();

   public AntiCrash() {
      super("AntiCrash", Module.Category.NONE);
   }

   @EventHandler
   public void onPacketReceive(@NotNull PacketEvent.Receive receive) {
      class_2596 var5 = receive.getPacket();
      if (var5 instanceof class_2664) {
         class_2664 exp = (class_2664)var5;
         if (exp.method_11475() > 1.0E9D || exp.method_11477() > 1.0E9D || exp.method_11478() > 1.0E9D || (double)exp.method_11476() > 1.0E9D) {
            if ((Boolean)this.debug.getValue() && this.debugTimer.passedMs(1000L)) {
               this.sendMessage("ExplosionS2CPacket canceled");
               this.debugTimer.reset();
            }

            receive.cancel();
            return;
         }
      }

      var5 = receive.getPacket();
      if (var5 instanceof class_2675) {
         class_2675 p = (class_2675)var5;
         if (p.method_11544() > 1.0E9D || p.method_11547() > 1.0E9D || p.method_11546() > 1.0E9D || (double)p.method_11543() > 1.0E9D || (double)p.method_11548() > 1.0E9D || (double)p.method_11549() > 1.0E9D || (double)p.method_11550() > 1.0E9D) {
            if ((Boolean)this.debug.getValue() && this.debugTimer.passedMs(1000L)) {
               this.sendMessage("ParticleS2CPacket canceled");
               this.debugTimer.reset();
            }

            receive.cancel();
            return;
         }
      }

      var5 = receive.getPacket();
      if (var5 instanceof class_2708) {
         class_2708 pos = (class_2708)var5;
         if (pos.method_11734() > 1.0E9D || pos.method_11735() > 1.0E9D || pos.method_11738() > 1.0E9D || (double)pos.method_11736() > 1.0E9D || (double)pos.method_11739() > 1.0E9D) {
            if ((Boolean)this.debug.getValue() && this.debugTimer.passedMs(1000L)) {
               this.sendMessage("PlayerPositionLookS2CPacket canceled");
               this.debugTimer.reset();
            }

            receive.cancel();
         }
      }

   }
}
