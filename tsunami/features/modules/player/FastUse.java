package tsunami.features.modules.player;

import net.minecraft.class_1747;
import net.minecraft.class_1792;
import net.minecraft.class_1802;
import tsunami.features.modules.Module;
import tsunami.injection.accesors.IMinecraftClient;
import tsunami.setting.Setting;

public class FastUse extends Module {
   private final Setting<Integer> delay = new Setting("Delay", 0, 0, 5);
   public Setting<Boolean> blocks = new Setting("Blocks", false);
   public Setting<Boolean> crystals = new Setting("Crystals", false);
   public Setting<Boolean> xp = new Setting("XP", false);
   public Setting<Boolean> all = new Setting("All", true);

   public FastUse() {
      super("FastUse", Module.Category.PLAYER);
   }

   public void onUpdate() {
      if (this.check(mc.field_1724.method_6047().method_7909()) && ((IMinecraftClient)mc).getUseCooldown() > (Integer)this.delay.getValue()) {
         ((IMinecraftClient)mc).setUseCooldown((Integer)this.delay.getValue());
      }

   }

   public boolean check(class_1792 item) {
      return item instanceof class_1747 && (Boolean)this.blocks.getValue() || item == class_1802.field_8301 && (Boolean)this.crystals.getValue() || item == class_1802.field_8287 && (Boolean)this.xp.getValue() || (Boolean)this.all.getValue();
   }
}
