package tsunami.features.modules.movement;

import tsunami.core.manager.client.ModuleManager;
import tsunami.features.modules.Module;
import tsunami.features.modules.combat.Aura;
import tsunami.setting.Setting;

public class AutoSprint extends Module {
   public static final Setting<Boolean> sprint = new Setting("KeepSprint", true);
   public static final Setting<Float> motion = new Setting("Motion", 1.0F, 0.0F, 1.0F, (v) -> {
      return (Boolean)sprint.getValue();
   });
   private final Setting<Boolean> stopWhileUsing = new Setting("StopWhileUsing", false);
   private final Setting<Boolean> pauseWhileAura = new Setting("PauseWhileAura", false);

   public AutoSprint() {
      super("AutoSprint", Module.Category.MOVEMENT);
   }

   public void onUpdate() {
      mc.field_1724.method_5728(mc.field_1724.method_7344().method_7586() > 6 && !mc.field_1724.field_5976 && mc.field_1724.field_3913.field_3905 > 0.0F && (!mc.field_1724.method_5715() || ModuleManager.noSlow.isEnabled() && (Boolean)ModuleManager.noSlow.sneak.getValue()) && (!mc.field_1724.method_6115() || !(Boolean)this.stopWhileUsing.getValue()) && (!ModuleManager.aura.isEnabled() || Aura.target == null || !(Boolean)this.pauseWhileAura.getValue()));
   }
}
