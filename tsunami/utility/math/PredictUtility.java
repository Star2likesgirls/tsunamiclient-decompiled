package tsunami.utility.math;

import com.mojang.authlib.GameProfile;
import java.util.Iterator;
import java.util.UUID;
import net.minecraft.class_1293;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_2338;
import net.minecraft.class_238;
import net.minecraft.class_243;
import tsunami.features.modules.Module;

public class PredictUtility {
   public static class_1657 movePlayer(class_1657 entity, class_243 newPos) {
      return entity != null && newPos != null ? equipAndReturn(entity, newPos) : null;
   }

   public static class_1657 predictPlayer(class_1657 entity, int ticks) {
      class_243 posVec = predictPosition(entity, ticks);
      return posVec == null ? null : equipAndReturn(entity, posVec);
   }

   public static class_243 predictPosition(class_1657 entity, int ticks) {
      if (entity == null) {
         return null;
      } else {
         class_243 posVec = new class_243(entity.method_23317(), entity.method_23318(), entity.method_23321());
         double motionX = entity.method_18798().method_10216();
         double motionZ = entity.method_18798().method_10215();

         for(int i = 0; i < ticks; ++i) {
            float hbDeltaX = motionX > 0.0D ? 0.3F : -0.3F;
            float hbDeltaZ = motionZ > 0.0D ? 0.3F : -0.3F;
            if (!Module.mc.field_1687.method_22347(class_2338.method_49638(posVec.method_1031(motionX + (double)hbDeltaX, 0.1D, motionZ + (double)hbDeltaZ))) || !Module.mc.field_1687.method_22347(class_2338.method_49638(posVec.method_1031(motionX + (double)hbDeltaX, 1.0D, motionZ + (double)hbDeltaZ)))) {
               motionX = 0.0D;
               motionZ = 0.0D;
            }

            posVec = posVec.method_1031(motionX, 0.0D, motionZ);
         }

         return posVec;
      }
   }

   public static class_238 predictBox(class_1657 entity, int ticks) {
      class_243 posVec = predictPosition(entity, ticks);
      return posVec == null ? null : createBox(posVec, entity);
   }

   public static class_1657 equipAndReturn(class_1657 original, class_243 posVec) {
      class_1657 copyEntity = new class_1657(Module.mc.field_1687, original.method_24515(), original.method_36454(), new GameProfile(UUID.fromString("66123666-1234-5432-6666-667563866600"), "PredictEntity339")) {
         public boolean method_7325() {
            return false;
         }

         public boolean method_7337() {
            return false;
         }
      };
      copyEntity.method_33574(posVec);
      copyEntity.method_6033(original.method_6032());
      copyEntity.field_6014 = original.field_6014;
      copyEntity.field_5969 = original.field_5969;
      copyEntity.field_6036 = original.field_6036;
      copyEntity.method_31548().method_7377(original.method_31548());
      Iterator var3 = original.method_6026().iterator();

      while(var3.hasNext()) {
         class_1293 se = (class_1293)var3.next();
         copyEntity.method_6092(se);
      }

      return copyEntity;
   }

   public static class_238 createBox(class_243 vec, class_1297 entity) {
      return entity.method_5829().method_997(entity.method_19538().method_1035(vec));
   }
}
