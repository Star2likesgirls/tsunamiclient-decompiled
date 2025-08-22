package tsunami.features.modules.combat;

import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_124;
import net.minecraft.class_1802;
import tsunami.core.manager.client.ModuleManager;
import tsunami.events.impl.PostPlayerUpdateEvent;
import tsunami.features.modules.Module;
import tsunami.features.modules.client.ClientSettings;
import tsunami.injection.accesors.IMinecraftClient;
import tsunami.setting.Setting;
import tsunami.utility.Timer;
import tsunami.utility.player.InventoryUtility;

public final class AutoGApple extends Module {
   public final Setting<Integer> Delay = new Setting("UseDelay", 0, 0, 2000);
   private final Setting<Float> health = new Setting("health", 15.0F, 1.0F, 36.0F);
   public Setting<Boolean> absorption = new Setting("Absorption", false);
   public Setting<Boolean> autoTotemIntegration = new Setting("AutoTotemIntegration", true);
   private boolean isActive;
   private final Timer useDelay = new Timer();

   public AutoGApple() {
      super("AutoGApple", Module.Category.NONE);
   }

   @EventHandler
   public void onUpdate(PostPlayerUpdateEvent e) {
      if (!fullNullCheck()) {
         if (this.GapInOffHand()) {
            if (mc.field_1724.method_6032() + ((Boolean)this.absorption.getValue() ? mc.field_1724.method_6067() : 0.0F) <= (Float)this.health.getValue() && this.useDelay.passedMs((long)(Integer)this.Delay.getValue())) {
               this.isActive = true;
               if (mc.field_1755 != null && !mc.field_1724.method_6115()) {
                  ((IMinecraftClient)mc).idoItemUse();
               } else {
                  mc.field_1690.field_1904.method_23481(true);
               }
            } else if (this.isActive) {
               this.isActive = false;
               mc.field_1690.field_1904.method_23481(false);
            }
         } else if (this.isActive) {
            this.isActive = false;
            mc.field_1690.field_1904.method_23481(false);
         }

      }
   }

   private boolean GapInOffHand() {
      if ((Boolean)this.autoTotemIntegration.getValue() && ModuleManager.autoTotem.isEnabled() && InventoryUtility.findItemInHotBar(class_1802.field_8463, class_1802.field_8367).found()) {
         if (!ModuleManager.autoTotem.rcGap.is(AutoTotem.RCGap.Off)) {
            return true;
         }

         String var10001 = String.valueOf(class_124.field_1061);
         this.sendMessage(var10001 + (ClientSettings.isRu() ? "Включи RcGap в AutoTotem!" : "Enable RcGap in AutoTotem"));
      }

      return !mc.field_1724.method_6079().method_7960() && (mc.field_1724.method_6079().method_7909() == class_1802.field_8463 || mc.field_1724.method_6079().method_7909() == class_1802.field_8367);
   }
}
