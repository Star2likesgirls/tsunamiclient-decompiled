package tsunami.features.modules.movement;

import net.minecraft.class_4587;
import tsunami.features.modules.Module;
import tsunami.setting.Setting;

public class Parkour extends Module {
   private final Setting<Float> jumpFactor = new Setting("JumpFactor", 0.01F, 0.001F, 0.3F);
   private final tsunami.utility.Timer delay = new tsunami.utility.Timer();

   public Parkour() {
      super("Parkour", Module.Category.NONE);
   }

   public void onRender3D(class_4587 stack) {
      if (mc.field_1724.method_24828() && !mc.field_1690.field_1903.method_1434() && !mc.field_1687.method_20812(mc.field_1724, mc.field_1724.method_5829().method_1009((double)(-(Float)this.jumpFactor.getValue()), 0.0D, (double)(-(Float)this.jumpFactor.getValue())).method_989(0.0D, -0.99D, 0.0D)).iterator().hasNext() && this.delay.every(150L)) {
         mc.field_1724.method_6043();
      }

   }
}
