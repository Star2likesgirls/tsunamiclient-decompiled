package tsunami.features.modules.render;

import net.minecraft.class_1799;
import net.minecraft.class_9288;
import net.minecraft.class_9334;
import tsunami.features.modules.Module;
import tsunami.setting.Setting;

public class Tooltips extends Module {
   public static final Setting<Boolean> middleClickOpen = new Setting("MiddleClickOpen", true);
   public static final Setting<Boolean> storage = new Setting("Storage", true);
   public static final Setting<Boolean> maps = new Setting("Maps", true);
   public final Setting<Boolean> shulkerRegear = new Setting("ShulkerRegear", true);
   public final Setting<Boolean> shulkerRegearShiftMode = new Setting("RegearShift", true);

   public Tooltips() {
      super("Tooltips", Module.Category.MISC);
   }

   public static boolean hasItems(class_1799 itemStack) {
      class_9288 compoundTag = (class_9288)itemStack.method_57824(class_9334.field_49622);
      return compoundTag != null && !compoundTag.method_57489().toList().isEmpty();
   }
}
