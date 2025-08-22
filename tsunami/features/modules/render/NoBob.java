package tsunami.features.modules.render;

import net.minecraft.class_1657;
import net.minecraft.class_3532;
import net.minecraft.class_4587;
import tsunami.features.modules.Module;
import tsunami.setting.Setting;

public class NoBob extends Module {
   public static Setting<NoBob.Mode> mode;

   public NoBob() {
      super("NoBob", Module.Category.NONE);
   }

   public void bobView(class_4587 matrices, float tickDelta) {
      if (mc.method_1560() instanceof class_1657) {
         float g = -(mc.field_1724.field_5973 + (mc.field_1724.field_5973 - mc.field_1724.field_6039) * tickDelta);
         float h = class_3532.method_16439(tickDelta, mc.field_1724.field_7505, mc.field_1724.field_7483);
         matrices.method_22904(0.0D, -Math.abs((double)(g * h) * (mode.is(NoBob.Mode.Sexy) ? 3.5E-4D : 0.0D)), 0.0D);
      }
   }

   static {
      mode = new Setting("Mode", NoBob.Mode.Sexy);
   }

   public static enum Mode {
      Sexy,
      Off;

      // $FF: synthetic method
      private static NoBob.Mode[] $values() {
         return new NoBob.Mode[]{Sexy, Off};
      }
   }
}
