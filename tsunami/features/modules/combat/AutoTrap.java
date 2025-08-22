package tsunami.features.modules.combat;

import net.minecraft.class_1657;
import org.jetbrains.annotations.Nullable;
import tsunami.core.Managers;
import tsunami.core.manager.player.CombatManager;
import tsunami.features.modules.Module;
import tsunami.features.modules.base.TrapModule;
import tsunami.setting.Setting;

public final class AutoTrap extends TrapModule {
   private final Setting<CombatManager.TargetBy> targetBy;
   private final Setting<Boolean> targetMovingPlayers;

   public AutoTrap() {
      super("AutoTrap", Module.Category.NONE);
      this.targetBy = new Setting("Target By", CombatManager.TargetBy.Distance);
      this.targetMovingPlayers = new Setting("MovingPlayers", false);
   }

   protected boolean needNewTarget() {
      return this.target == null || this.target.method_5739(mc.field_1724) > (Float)this.range.getValue() || this.target.method_6032() + this.target.method_6067() <= 0.0F || this.target.method_29504();
   }

   @Nullable
   protected class_1657 getTarget() {
      return Managers.COMBAT.getTarget((Float)this.range.getValue(), (CombatManager.TargetBy)this.targetBy.getValue(), (p) -> {
         return p.method_18798().method_1027() < 0.08D || (Boolean)this.targetMovingPlayers.getValue();
      });
   }
}
