package tsunami.features.modules.misc;

import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_2596;
import net.minecraft.class_7439;
import org.jetbrains.annotations.NotNull;
import tsunami.TsunamiClient;
import tsunami.events.impl.PacketEvent;
import tsunami.features.modules.Module;
import tsunami.setting.Setting;
import tsunami.utility.Timer;

public final class AutoDuel extends Module {
   private final Setting<AutoDuel.Mode> mode;
   private final Setting<String> nickname;
   private final Setting<Float> delay;
   private final Timer timer;
   private boolean waiting;

   public AutoDuel() {
      super("AutoDuel", Module.Category.NONE);
      this.mode = new Setting("Mode", AutoDuel.Mode.Accept);
      this.nickname = new Setting("Nickname", "06ED");
      this.delay = new Setting("Delay", 2.0F, 0.0F, 30.0F);
      this.timer = new Timer();
      this.waiting = false;
   }

   public void onEnable() {
      this.waiting = false;
   }

   @EventHandler
   public void onPacketReceive(@NotNull PacketEvent.Receive event) {
      if (this.waiting && this.timer.passedMs((long)(1000.0F * (Float)this.delay.getValue()))) {
         clickSlot(0);
         this.timer.reset();
         this.waiting = false;
      }

      class_2596 var3 = event.getPacket();
      if (var3 instanceof class_7439) {
         class_7439 pac = (class_7439)var3;
         String message = pac.comp_763().getString().toLowerCase();
         switch(((AutoDuel.Mode)this.mode.getValue()).ordinal()) {
         case 0:
            TsunamiClient.LOGGER.info(message);
            if (message.contains("[duels]") && message.contains(this.nickname.toString().toLowerCase()) && message.contains(mc.method_1548().method_1676().toLowerCase())) {
               this.sendChatCommand("duel " + (String)this.nickname.getValue());
               this.waiting = true;
            }
            break;
         case 1:
            if (!pac.comp_763.getString().contains("duel request received from " + ((String)this.nickname.getValue()).toLowerCase())) {
               return;
            }

            this.sendChatCommand("duel accept " + (String)this.nickname.getValue());
         }
      }

   }

   private static enum Mode {
      Send,
      Accept;

      // $FF: synthetic method
      private static AutoDuel.Mode[] $values() {
         return new AutoDuel.Mode[]{Send, Accept};
      }
   }

   private static enum Server {
      CC;

      // $FF: synthetic method
      private static AutoDuel.Server[] $values() {
         return new AutoDuel.Server[]{CC};
      }
   }
}
