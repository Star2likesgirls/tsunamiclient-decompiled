package tsunami.features.modules.misc;

import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_7439;
import tsunami.TsunamiClient;
import tsunami.events.impl.EventSync;
import tsunami.events.impl.PacketEvent;
import tsunami.features.modules.Module;
import tsunami.setting.Setting;
import tsunami.utility.Timer;
import tsunami.utility.player.MovementUtility;

public class AutoFlyme extends Module {
   public final Setting<Boolean> instantSpeed = new Setting("InstantSpeed", true);
   public final Setting<Boolean> hover = new Setting("hover", false);
   public final Setting<Boolean> useTimer = new Setting("UseTimer", false);
   public Setting<Float> hoverY = new Setting("hoverY", 0.228F, 0.0F, 1.0F, (v) -> {
      return (Boolean)this.hover.getValue();
   });
   public Setting<Float> speed = new Setting("speed", 1.05F, 0.0F, 8.0F, (v) -> {
      return (Boolean)this.hover.getValue();
   });
   private final Timer timer = new Timer();

   public AutoFlyme() {
      super("AutoFlyme", Module.Category.NONE);
   }

   public void onEnable() {
      if (!mc.field_1724.method_31549().field_7479) {
         mc.field_1724.field_3944.method_45730("flyme");
      }

   }

   public void onDisable() {
      TsunamiClient.TICK_TIMER = 1.0F;
   }

   @EventHandler
   public void onPacketReceive(PacketEvent.Receive e) {
      if (e.getPacket() instanceof class_7439) {
         class_7439 packet = (class_7439)e.getPacket();
         if ((packet.comp_763().getString().contains("Вы атаковали игрока") || packet.comp_763().getString().contains("Возможность летать была удалена")) && this.timer.passedMs(1000L)) {
            mc.field_1724.field_3944.method_45730("flyme");
            mc.field_1724.field_3944.method_45730("flyme");
            this.timer.reset();
         }
      }

   }

   public void onUpdate() {
      if ((Boolean)this.useTimer.getValue()) {
         TsunamiClient.TICK_TIMER = 1.088F;
      }

      if (!mc.field_1724.method_31549().field_7479 && this.timer.passedMs(1000L) && !mc.field_1724.method_24828() && mc.field_1724.field_3913.field_3904) {
         mc.field_1724.field_3944.method_45730("flyme");
         this.timer.reset();
      }

      if (!mc.field_1690.field_1903.method_1434() && (Boolean)this.hover.getValue() && mc.field_1724.method_31549().field_7479 && mc.field_1724.method_31549().field_7479 && !mc.field_1724.method_24828() && !mc.field_1687.method_20812(mc.field_1724, mc.field_1724.method_5829().method_989(0.0D, (double)(-(Float)this.hoverY.getValue()), 0.0D)).iterator().hasNext()) {
         mc.field_1724.method_18800(mc.field_1724.method_18798().field_1352, -0.05D, mc.field_1724.method_18798().field_1350);
      }

   }

   @EventHandler
   public void onUpdateWalkingPlayer(EventSync event) {
      if ((Boolean)this.instantSpeed.getValue() && mc.field_1724.method_31549().field_7479) {
         double[] dir = MovementUtility.isMoving() ? MovementUtility.forward((double)(Float)this.speed.getValue()) : new double[]{0.0D, 0.0D};
         mc.field_1724.method_18800(dir[0], mc.field_1724.method_18798().field_1351, dir[1]);
      }
   }
}
