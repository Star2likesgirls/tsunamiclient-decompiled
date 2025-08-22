package tsunami.features.modules.player;

import net.minecraft.class_1498;
import net.minecraft.class_1753;
import net.minecraft.class_1764;
import net.minecraft.class_1799;
import net.minecraft.class_1835;
import net.minecraft.class_1890;
import net.minecraft.class_1893;
import net.minecraft.class_6880;
import tsunami.core.manager.client.ModuleManager;
import tsunami.features.modules.Module;
import tsunami.injection.accesors.IClientPlayerEntity;
import tsunami.setting.Setting;

public class PerfectDelay extends Module {
   private final Setting<PerfectDelay.HorseJump> horse;
   private final Setting<Boolean> bow;
   private final Setting<Boolean> crossbow;
   private final Setting<Boolean> trident;

   public PerfectDelay() {
      super("PerfectDelay", Module.Category.NONE);
      this.horse = new Setting("Horse", PerfectDelay.HorseJump.Legit);
      this.bow = new Setting("Bow", true);
      this.crossbow = new Setting("Crossbow", true);
      this.trident = new Setting("Trident", true);
   }

   private float getEnchantLevel(class_1799 stack) {
      return (float)class_1890.method_8225((class_6880)mc.field_1687.method_30349().method_30530(class_1893.field_9111.method_58273()).method_40264(class_1893.field_9098).get(), stack);
   }

   public void onUpdate() {
      if (mc.field_1724.method_6030().method_7909() instanceof class_1835 && (Boolean)this.trident.getValue() && mc.field_1724.method_6048() > (ModuleManager.tridentBoost.isEnabled() ? (Integer)ModuleManager.tridentBoost.cooldown.getValue() : 9)) {
         mc.field_1761.method_2897(mc.field_1724);
      }

      if (mc.field_1724.method_6030().method_7909() instanceof class_1764 && (Boolean)this.crossbow.getValue() && (double)mc.field_1724.method_6048() >= 25.0D - 0.25D * (double)this.getEnchantLevel(mc.field_1724.method_6030()) * 20.0D) {
         mc.field_1761.method_2897(mc.field_1724);
      }

      if (mc.field_1724.method_6030().method_7909() instanceof class_1753 && (Boolean)this.bow.getValue() && mc.field_1724.method_6048() > 19) {
         mc.field_1761.method_2897(mc.field_1724);
      }

      if (mc.field_1724.method_49694() != null && mc.field_1724.method_49694() instanceof class_1498 && this.horse.is(PerfectDelay.HorseJump.Rage)) {
         ((IClientPlayerEntity)mc.field_1724).setMountJumpStrength(1.0F);
      }

      if (mc.field_1724.method_49694() != null && mc.field_1724.method_49694() instanceof class_1498 && this.horse.is(PerfectDelay.HorseJump.Legit) && mc.field_1724.method_3151() >= 1.0F) {
         mc.field_1690.field_1903.method_23481(false);
      }

   }

   private static enum HorseJump {
      Legit,
      Rage,
      Off;

      // $FF: synthetic method
      private static PerfectDelay.HorseJump[] $values() {
         return new PerfectDelay.HorseJump[]{Legit, Rage, Off};
      }
   }
}
