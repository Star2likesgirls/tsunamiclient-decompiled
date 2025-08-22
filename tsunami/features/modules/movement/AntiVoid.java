package tsunami.features.modules.movement;

import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_2338;
import net.minecraft.class_2828.class_2829;
import tsunami.TsunamiClient;
import tsunami.events.impl.EventMove;
import tsunami.features.modules.Module;
import tsunami.setting.Setting;

public class AntiVoid extends Module {
   private static final Setting<AntiVoid.Mode> mode;
   private final Setting<Boolean> sendPacket = new Setting("SendPacket", true, (v) -> {
      return mode.getValue() == AntiVoid.Mode.NCP;
   });
   boolean timerFlag;

   public AntiVoid() {
      super("AntiVoid", Module.Category.NONE);
   }

   public void onDisable() {
      if (this.timerFlag) {
         TsunamiClient.TICK_TIMER = 1.0F;
      }

   }

   @EventHandler(
      priority = -200
   )
   public void onMove(EventMove e) {
      if (!fullNullCheck()) {
         if (this.fallingToVoid()) {
            if (mode.getValue() == AntiVoid.Mode.NCP) {
               e.cancel();
               e.setY(0.0D);
               if ((Boolean)this.sendPacket.getValue()) {
                  this.sendPacket(new class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318(), mc.field_1724.method_23321(), true));
               }
            } else {
               TsunamiClient.TICK_TIMER = 0.2F;
               this.timerFlag = true;
            }
         } else if (this.timerFlag) {
            TsunamiClient.TICK_TIMER = 1.0F;
            this.timerFlag = false;
         }

      }
   }

   private boolean fallingToVoid() {
      for(int i = (int)mc.field_1724.method_23318(); i >= -64; --i) {
         if (!mc.field_1687.method_22347(class_2338.method_49637(mc.field_1724.method_23317(), (double)i, mc.field_1724.method_23321()))) {
            return false;
         }
      }

      return mc.field_1724.field_6017 > 0.0F;
   }

   static {
      mode = new Setting("Mode", AntiVoid.Mode.NCP);
   }

   private static enum Mode {
      NCP,
      Timer;

      // $FF: synthetic method
      private static AntiVoid.Mode[] $values() {
         return new AntiVoid.Mode[]{NCP, Timer};
      }
   }
}
