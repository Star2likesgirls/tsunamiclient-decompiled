package tsunami.features.modules.player;

import net.minecraft.class_1268;
import net.minecraft.class_239;
import net.minecraft.class_3965;
import net.minecraft.class_3966;
import tsunami.features.modules.Module;

public class MultiTask extends Module {
   public MultiTask() {
      super("MultiTask", Module.Category.NONE);
   }

   public void onUpdate() {
      class_239 var2 = mc.field_1765;
      if (var2 instanceof class_3965) {
         class_3965 crossHair = (class_3965)var2;
         if (crossHair.method_17777() != null && mc.field_1690.field_1886.method_1434() && !mc.field_1687.method_8320(crossHair.method_17777()).method_26215()) {
            mc.field_1761.method_2910(crossHair.method_17777(), crossHair.method_17780());
            mc.field_1724.method_6104(class_1268.field_5808);
         }
      }

      var2 = mc.field_1765;
      if (var2 instanceof class_3966) {
         class_3966 ehr = (class_3966)var2;
         if (ehr.method_17782() != null && mc.field_1690.field_1886.method_1434() && mc.field_1724.method_7261(0.5F) > 0.9F) {
            mc.field_1761.method_2918(mc.field_1724, ehr.method_17782());
            mc.field_1724.method_6104(class_1268.field_5808);
         }
      }

   }
}
