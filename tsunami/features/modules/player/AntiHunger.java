package tsunami.features.modules.player;

import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_2596;
import net.minecraft.class_2828;
import net.minecraft.class_2848;
import net.minecraft.class_2848.class_2849;
import org.jetbrains.annotations.NotNull;
import tsunami.events.impl.PacketEvent;
import tsunami.features.modules.Module;
import tsunami.injection.accesors.IPlayerMoveC2SPacket;
import tsunami.setting.Setting;

public class AntiHunger extends Module {
   private final Setting<Boolean> ground = new Setting("CancelGround", true);
   private final Setting<Boolean> sprint = new Setting("CancelSprint", true);

   public AntiHunger() {
      super("AntiHunger", Module.Category.NONE);
   }

   @EventHandler
   public void onPacketSend(@NotNull PacketEvent.Send e) {
      class_2596 var3 = e.getPacket();
      if (var3 instanceof class_2828) {
         class_2828 pac = (class_2828)var3;
         if ((Boolean)this.ground.getValue()) {
            ((IPlayerMoveC2SPacket)pac).setOnGround(false);
         }
      }

      var3 = e.getPacket();
      if (var3 instanceof class_2848) {
         class_2848 pac = (class_2848)var3;
         if ((Boolean)this.sprint.getValue() && pac.method_12365() == class_2849.field_12981) {
            e.cancel();
         }
      }

   }
}
