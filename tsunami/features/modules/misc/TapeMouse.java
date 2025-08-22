package tsunami.features.modules.misc;

import net.minecraft.class_1268;
import net.minecraft.class_239;
import net.minecraft.class_3965;
import net.minecraft.class_3966;
import tsunami.features.modules.Module;
import tsunami.injection.accesors.IMinecraftClient;
import tsunami.setting.Setting;
import tsunami.setting.impl.BooleanSettingGroup;
import tsunami.utility.Timer;
import tsunami.utility.math.MathUtility;

public class TapeMouse extends Module {
   private final Setting<Integer> delay = new Setting("Delay", 600, 0, 10000);
   private final Setting<BooleanSettingGroup> randomize = new Setting("Randomize", new BooleanSettingGroup(false));
   private final Setting<Integer> randomizeValue;
   private final Setting<TapeMouse.Mode> mode;
   private final Setting<Boolean> legit;
   private final Timer timer;

   public TapeMouse() {
      super("TapeMouse", Module.Category.NONE);
      this.randomizeValue = (new Setting("Value", 600, 0, 10000)).addToGroup(this.randomize);
      this.mode = new Setting("Mode", TapeMouse.Mode.Left);
      this.legit = new Setting("Legit", false, (v) -> {
         return this.mode.getValue() == TapeMouse.Mode.Left;
      });
      this.timer = new Timer();
   }

   public void onUpdate() {
      if (this.timer.every((long)((float)(Integer)this.delay.getValue() + (((BooleanSettingGroup)this.randomize.getValue()).isEnabled() ? MathUtility.random(0.0F, (float)(Integer)this.randomizeValue.getValue()) : 0.0F)))) {
         if (this.mode.getValue() == TapeMouse.Mode.Left) {
            if (!(Boolean)this.legit.getValue()) {
               class_239 hr = mc.field_1765;
               if (hr != null) {
                  if (hr instanceof class_3966) {
                     class_3966 ehr = (class_3966)hr;
                     if (ehr.method_17782() != null) {
                        mc.field_1761.method_2918(mc.field_1724, ehr.method_17782());
                        mc.field_1724.method_6104(class_1268.field_5808);
                        return;
                     }
                  }

                  if (hr instanceof class_3965) {
                     class_3965 bhr = (class_3965)hr;
                     if (bhr.method_17777() != null && bhr.method_17780() != null && !mc.field_1687.method_22347(bhr.method_17777())) {
                        mc.field_1761.method_2910(bhr.method_17777(), bhr.method_17780());
                        mc.field_1724.method_6104(class_1268.field_5808);
                     }
                  }
               }
            } else {
               ((IMinecraftClient)mc).idoAttack();
            }
         } else {
            ((IMinecraftClient)mc).idoItemUse();
         }
      }

   }

   private static enum Mode {
      Right,
      Left;

      // $FF: synthetic method
      private static TapeMouse.Mode[] $values() {
         return new TapeMouse.Mode[]{Right, Left};
      }
   }
}
