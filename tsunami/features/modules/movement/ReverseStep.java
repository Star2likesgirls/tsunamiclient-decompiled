package tsunami.features.modules.movement;

import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_2680;
import tsunami.TsunamiClient;
import tsunami.core.manager.client.ModuleManager;
import tsunami.events.impl.EventSync;
import tsunami.features.modules.Module;
import tsunami.setting.Setting;

public class ReverseStep extends Module {
   private final Setting<ReverseStep.Mode> mode;
   private final Setting<Float> timer;
   private final Setting<Float> motion;
   private final Setting<Boolean> anyblock;
   private final Setting<Boolean> pauseIfShift;
   private boolean disableTimer;
   private boolean prevGround;

   public ReverseStep() {
      super("ReverseStep", Module.Category.NONE);
      this.mode = new Setting("Mode", ReverseStep.Mode.Motion);
      this.timer = new Setting("Timer", 3.0F, 1.0F, 10.0F, (v) -> {
         return this.mode.getValue() == ReverseStep.Mode.Timer;
      });
      this.motion = new Setting("Motion", 1.0F, 0.1F, 10.0F, (v) -> {
         return this.mode.getValue() == ReverseStep.Mode.Motion;
      });
      this.anyblock = new Setting("AnyBlock", false);
      this.pauseIfShift = new Setting("PauseIfShift", false);
      this.disableTimer = true;
      this.prevGround = false;
   }

   @EventHandler
   public void onEntitySync(EventSync eventPlayerUpdateWalking) {
      if (!ModuleManager.packetFly.isEnabled()) {
         class_2338 playerPos = class_2338.method_49638(mc.field_1724.method_19538());
         if ((Boolean)this.pauseIfShift.getValue() && mc.field_1690.field_1832.method_1434()) {
            this.disableTimer();
         } else if (!mc.field_1724.method_5799() && !mc.field_1724.method_5869() && !mc.field_1724.method_5771() && !mc.field_1724.method_6128() && !mc.field_1724.method_31549().field_7479 && mc.field_1687.method_8320(playerPos).method_26204() != class_2246.field_10343) {
            if (this.checkBlock(mc.field_1687.method_8320(playerPos.method_10087(2))) || this.checkBlock(mc.field_1687.method_8320(playerPos.method_10087(3))) || this.checkBlock(mc.field_1687.method_8320(playerPos.method_10087(4)))) {
               this.doStep();
            }

            if (this.disableTimer && mc.field_1724.method_24828()) {
               this.disableTimer = false;
               TsunamiClient.TICK_TIMER = 1.0F;
            }

            this.prevGround = mc.field_1724.method_24828();
         } else {
            this.disableTimer();
         }
      }
   }

   private void disableTimer() {
      if (this.disableTimer) {
         TsunamiClient.TICK_TIMER = 1.0F;
      }

   }

   private boolean checkBlock(class_2680 bs) {
      return bs.method_26204() == class_2246.field_9987 || bs.method_26204() != class_2246.field_10540 || (Boolean)this.anyblock.getValue();
   }

   private void doStep() {
      if (this.mode.getValue() == ReverseStep.Mode.Timer && this.prevGround && !mc.field_1724.method_24828() && mc.field_1724.method_18798().method_10214() < -0.1D && !this.disableTimer) {
         TsunamiClient.TICK_TIMER = (Float)this.timer.getValue();
         this.disableTimer = true;
      }

      if (mc.field_1724.method_24828() && this.mode.getValue() == ReverseStep.Mode.Motion) {
         mc.field_1724.method_18799(mc.field_1724.method_18798().method_1031(0.0D, (double)(-(Float)this.motion.getValue()), 0.0D));
      }

   }

   public static enum Mode {
      Timer,
      Motion;

      // $FF: synthetic method
      private static ReverseStep.Mode[] $values() {
         return new ReverseStep.Mode[]{Timer, Motion};
      }
   }
}
