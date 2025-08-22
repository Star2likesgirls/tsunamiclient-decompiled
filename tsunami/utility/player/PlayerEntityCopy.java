package tsunami.utility.player;

import java.util.Objects;
import java.util.UUID;
import net.minecraft.class_638;
import net.minecraft.class_745;
import net.minecraft.class_746;
import net.minecraft.class_1297.class_5529;
import tsunami.features.modules.Module;

public class PlayerEntityCopy extends class_745 {
   public PlayerEntityCopy() {
      super((class_638)Objects.requireNonNull(Module.mc.field_1687), ((class_746)Objects.requireNonNull(Module.mc.field_1724)).method_7334());
      this.method_5878(Module.mc.field_1724);
      this.method_3123();
      this.field_6011.method_12778(field_7518, (Byte)Module.mc.field_1724.method_5841().method_12789(field_7518));
      this.method_5826(UUID.randomUUID());
   }

   public void spawn() {
      if (Module.mc.field_1687 != null) {
         this.method_31482();
         Module.mc.field_1687.method_53875(this);
      }
   }

   public void deSpawn() {
      if (Module.mc.field_1687 != null) {
         Module.mc.field_1687.method_2945(this.method_5628(), class_5529.field_26999);
      }
   }
}
