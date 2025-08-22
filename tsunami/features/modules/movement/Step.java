package tsunami.features.modules.movement;

import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_5134;
import net.minecraft.class_2828.class_2829;
import tsunami.TsunamiClient;
import tsunami.core.manager.client.ModuleManager;
import tsunami.events.impl.EventSync;
import tsunami.features.modules.Module;
import tsunami.setting.Setting;
import tsunami.utility.world.HoleUtility;

public class Step extends Module {
   private final Setting<Boolean> strict = new Setting("Strict", false);
   private final Setting<Float> height = new Setting("Height", 2.0F, 1.0F, 2.5F, (v) -> {
      return !(Boolean)this.strict.getValue();
   });
   private final Setting<Boolean> useTimer = new Setting("Timer", true);
   private final Setting<Boolean> pauseIfShift = new Setting("PauseIfShift", false);
   private final Setting<Integer> stepDelay = new Setting("StepDelay", 200, 0, 1000);
   private final Setting<Boolean> holeDisable = new Setting("HoleDisable", false);
   private final Setting<Step.Mode> mode;
   private final tsunami.utility.Timer stepTimer;
   private boolean alreadyInHole;
   private boolean timer;

   public Step() {
      super("Step", Module.Category.NONE);
      this.mode = new Setting("Mode", Step.Mode.NCP);
      this.stepTimer = new tsunami.utility.Timer();
   }

   public void onEnable() {
      this.alreadyInHole = mc.field_1724 != null && HoleUtility.isHole(mc.field_1724.method_24515());
   }

   public void onDisable() {
      TsunamiClient.TICK_TIMER = 1.0F;
      this.setStepHeight(0.6F);
   }

   public void onUpdate() {
      if ((Boolean)this.holeDisable.getValue() && HoleUtility.isHole(mc.field_1724.method_24515()) && !this.alreadyInHole) {
         this.disable("Player in hole... Disabling...");
      } else {
         this.alreadyInHole = mc.field_1724 != null && HoleUtility.isHole(mc.field_1724.method_24515());
         if ((Boolean)this.pauseIfShift.getValue() && mc.field_1690.field_1832.method_1434()) {
            this.setStepHeight(0.6F);
         } else if (!mc.field_1724.method_31549().field_7479 && !ModuleManager.freeCam.isOn() && !mc.field_1724.method_3144() && !mc.field_1724.method_5799()) {
            if (this.timer && mc.field_1724.method_24828()) {
               TsunamiClient.TICK_TIMER = 1.0F;
               this.timer = false;
            }

            if (mc.field_1724.method_24828() && this.stepTimer.passedMs((long)(Integer)this.stepDelay.getValue())) {
               this.setStepHeight((Float)this.height.getValue());
            } else {
               this.setStepHeight(0.6F);
            }

         } else {
            this.setStepHeight(0.6F);
         }
      }
   }

   @EventHandler
   public void onStep(EventSync event) {
      if (this.mode.getValue() == Step.Mode.NCP) {
         double stepHeight = mc.field_1724.method_23318() - mc.field_1724.field_6036;
         if (stepHeight <= 0.75D || stepHeight > (double)(Float)this.height.getValue() || (Boolean)this.strict.getValue() && stepHeight > 1.0D) {
            return;
         }

         double[] offsets = this.getOffset(stepHeight);
         if (offsets != null && offsets.length > 1) {
            if ((Boolean)this.useTimer.getValue()) {
               TsunamiClient.TICK_TIMER = 1.0F / (float)offsets.length;
               this.timer = true;
            }

            double[] var5 = offsets;
            int var6 = offsets.length;

            for(int var7 = 0; var7 < var6; ++var7) {
               double offset = var5[var7];
               this.sendPacket(new class_2829(mc.field_1724.field_6014, mc.field_1724.field_6036 + offset, mc.field_1724.field_5969, false));
            }

            if ((Boolean)this.strict.getValue()) {
               this.sendPacket(new class_2829(mc.field_1724.field_6014, mc.field_1724.field_6036 + stepHeight, mc.field_1724.field_5969, false));
            }
         }

         this.stepTimer.reset();
      }

   }

   public double[] getOffset(double h) {
      double[] var10000;
      switch((int)(h * 10000.0D)) {
      case 7500:
      case 10000:
         var10000 = new double[]{0.42D, 0.753D};
         break;
      case 8125:
      case 8750:
         var10000 = new double[]{0.39D, 0.7D};
         break;
      case 15000:
         var10000 = new double[]{0.42D, 0.75D, 1.0D, 1.16D, 1.23D, 1.2D};
         break;
      case 20000:
         var10000 = new double[]{0.42D, 0.78D, 0.63D, 0.51D, 0.9D, 1.21D, 1.45D, 1.43D};
         break;
      case 250000:
         var10000 = new double[]{0.425D, 0.821D, 0.699D, 0.599D, 1.022D, 1.372D, 1.652D, 1.869D, 2.019D, 1.907D};
         break;
      default:
         var10000 = null;
      }

      return var10000;
   }

   private void setStepHeight(float v) {
      mc.field_1724.method_5996(class_5134.field_47761).method_6192((double)v);
   }

   public static enum Mode {
      NCP,
      VANILLA;

      // $FF: synthetic method
      private static Step.Mode[] $values() {
         return new Step.Mode[]{NCP, VANILLA};
      }
   }
}
