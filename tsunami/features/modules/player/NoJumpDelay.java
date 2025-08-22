package tsunami.features.modules.player;

import tsunami.features.modules.Module;
import tsunami.injection.accesors.ILivingEntity;
import tsunami.setting.Setting;

public class NoJumpDelay extends Module {
   private final Setting<Integer> delay = new Setting("Delay", 1, 0, 4);

   public NoJumpDelay() {
      super("NoJumpDelay", Module.Category.NONE);
   }

   public void onUpdate() {
      if (((ILivingEntity)mc.field_1724).getLastJumpCooldown() > (Integer)this.delay.getValue()) {
         ((ILivingEntity)mc.field_1724).setLastJumpCooldown((Integer)this.delay.getValue());
      }

   }
}
