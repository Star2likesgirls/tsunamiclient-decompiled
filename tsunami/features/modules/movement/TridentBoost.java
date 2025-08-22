package tsunami.features.modules.movement;

import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_1313;
import net.minecraft.class_1657;
import net.minecraft.class_1890;
import net.minecraft.class_243;
import net.minecraft.class_3414;
import net.minecraft.class_3417;
import net.minecraft.class_3419;
import net.minecraft.class_3532;
import net.minecraft.class_6880;
import net.minecraft.class_9701;
import tsunami.events.impl.UseTridentEvent;
import tsunami.features.modules.Module;
import tsunami.setting.Setting;

public class TridentBoost extends Module {
   private final Setting<TridentBoost.Mode> mode;
   private final Setting<Float> factor;
   public final Setting<Integer> cooldown;
   public final Setting<Boolean> anyWeather;

   public TridentBoost() {
      super("TridentBoost", Module.Category.NONE);
      this.mode = new Setting("Mode", TridentBoost.Mode.Motion);
      this.factor = new Setting("Factor", 1.0F, 0.1F, 20.0F);
      this.cooldown = new Setting("Cooldown", 10, 0, 20);
      this.anyWeather = new Setting("AnyWeather", true);
   }

   @EventHandler
   public void onUseTrident(UseTridentEvent e) {
      if (mc.field_1724.method_6048() >= (Integer)this.cooldown.getValue()) {
         float j = class_1890.method_60123(mc.field_1724.method_6030(), mc.field_1724);
         if (((Boolean)this.anyWeather.getValue() || mc.field_1724.method_5721()) && j > 0.0F) {
            float f = mc.field_1724.method_36454();
            float g = mc.field_1724.method_36455();
            float speedX = -class_3532.method_15374(f * 0.017453292F) * class_3532.method_15362(g * 0.017453292F);
            float speedY = -class_3532.method_15374(g * 0.017453292F);
            float speedZ = class_3532.method_15362(f * 0.017453292F) * class_3532.method_15362(g * 0.017453292F);
            float plannedSpeed = class_3532.method_15355(speedX * speedX + speedY * speedY + speedZ * speedZ);
            float n = this.mode.is(TridentBoost.Mode.Factor) ? (Float)this.factor.getValue() * 3.0F * ((1.0F + j) / 4.0F) : (Float)this.factor.getValue();
            speedX *= n / plannedSpeed;
            speedY *= n / plannedSpeed;
            speedZ *= n / plannedSpeed;
            mc.field_1724.method_5762((double)speedX, (double)speedY, (double)speedZ);
            mc.field_1724.method_40126(20, 8.0F, mc.field_1724.method_6030());
            if (mc.field_1724.method_24828()) {
               mc.field_1724.method_5784(class_1313.field_6308, new class_243(0.0D, 1.1999999284744263D, 0.0D));
            }

            class_6880<class_3414> registryEntry = (class_6880)class_1890.method_60165(mc.field_1724.method_6030(), class_9701.field_51654).orElse(class_3417.field_15001);
            mc.field_1687.method_43129((class_1657)null, mc.field_1724, (class_3414)registryEntry.comp_349(), class_3419.field_15248, 1.0F, 1.0F);
         }
      }

      e.cancel();
   }

   private static enum Mode {
      Motion,
      Factor;

      // $FF: synthetic method
      private static TridentBoost.Mode[] $values() {
         return new TridentBoost.Mode[]{Motion, Factor};
      }
   }
}
