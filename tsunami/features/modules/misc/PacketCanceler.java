package tsunami.features.modules.misc;

import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_2813;
import net.minecraft.class_2828.class_2829;
import net.minecraft.class_2828.class_2831;
import net.minecraft.class_2828.class_5911;
import org.jetbrains.annotations.NotNull;
import tsunami.events.impl.PacketEvent;
import tsunami.features.modules.Module;
import tsunami.setting.Setting;

public final class PacketCanceler extends Module {
   private final Setting<Boolean> cliclSlot = new Setting("ClickSlotC2SPacket", false);
   private final Setting<Boolean> playerMovePosAndOnGround = new Setting("PositionAndOnGround", false);
   private final Setting<Boolean> playerMoveOnGroundOnly = new Setting("OnGroundOnly", false);
   private final Setting<Boolean> playerMoveLookAndOnGround = new Setting("LookAndOnGround", false);

   public PacketCanceler() {
      super("PacketCanceler", Module.Category.MISC);
   }

   @EventHandler
   private void onPacketSend(@NotNull PacketEvent.Send e) {
      if (e.getPacket() instanceof class_2813 && (Boolean)this.cliclSlot.getValue()) {
         e.cancel();
      } else if (e.getPacket() instanceof class_2829 && (Boolean)this.playerMovePosAndOnGround.getValue()) {
         e.cancel();
      } else if (e.getPacket() instanceof class_5911 && (Boolean)this.playerMoveOnGroundOnly.getValue()) {
         e.cancel();
      } else if (e.getPacket() instanceof class_2831 && (Boolean)this.playerMoveLookAndOnGround.getValue()) {
         e.cancel();
      }

   }
}
