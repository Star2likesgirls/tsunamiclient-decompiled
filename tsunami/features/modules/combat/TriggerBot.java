package tsunami.features.modules.combat;

import java.util.Random;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_1268;
import net.minecraft.class_1294;
import net.minecraft.class_1297;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import tsunami.core.Managers;
import tsunami.core.manager.client.ModuleManager;
import tsunami.events.impl.PlayerUpdateEvent;
import tsunami.features.modules.Module;
import tsunami.setting.Setting;
import tsunami.setting.impl.BooleanSettingGroup;

public final class TriggerBot extends Module {
   public final Setting<Float> attackRange = new Setting("Range", 3.0F, 1.0F, 7.0F);
   public final Setting<BooleanSettingGroup> smartCrit = new Setting("SmartCrit", new BooleanSettingGroup(true));
   public final Setting<Boolean> onlySpace;
   public final Setting<Boolean> autoJump;
   public final Setting<Boolean> ignoreWalls;
   public final Setting<Boolean> pauseEating;
   public final Setting<Integer> minDelay;
   public final Setting<Integer> maxDelay;
   private int delay;
   private final Random random;

   public TriggerBot() {
      super("TriggerBot", Module.Category.NONE);
      this.onlySpace = (new Setting("OnlyCrit", false)).addToGroup(this.smartCrit);
      this.autoJump = (new Setting("AutoJump", false)).addToGroup(this.smartCrit);
      this.ignoreWalls = new Setting("IgnoreWalls", false);
      this.pauseEating = new Setting("PauseWhileEating", false);
      this.minDelay = new Setting("RandomDelayMin", 2, 0, 20);
      this.maxDelay = new Setting("RandomDelayMax", 13, 0, 20);
      this.random = new Random();
   }

   @EventHandler
   public void onAttack(PlayerUpdateEvent e) {
      if (!mc.field_1724.method_6115() || !(Boolean)this.pauseEating.getValue()) {
         if (!mc.field_1690.field_1903.method_1434() && mc.field_1724.method_24828() && (Boolean)this.autoJump.getValue()) {
            mc.field_1724.method_6043();
         }

         if (!this.autoCrit() && this.delay > 0) {
            --this.delay;
         } else {
            class_1297 ent = Managers.PLAYER.getRtxTarget(mc.field_1724.method_36454(), mc.field_1724.method_36455(), (Float)this.attackRange.getValue(), (Boolean)this.ignoreWalls.getValue());
            if (ent != null && !Managers.FRIEND.isFriend(ent.method_5477().getString())) {
               mc.field_1761.method_2918(mc.field_1724, ent);
               mc.field_1724.method_6104(class_1268.field_5808);
               this.delay = this.random.nextInt((Integer)this.minDelay.getValue(), (Integer)this.maxDelay.getValue() + 1);
            }

         }
      }
   }

   private boolean autoCrit() {
      boolean reasonForSkipCrit = !((BooleanSettingGroup)this.smartCrit.getValue()).isEnabled() || mc.field_1724.method_31549().field_7479 || mc.field_1724.method_6128() || ModuleManager.elytraPlus.isEnabled() || mc.field_1724.method_6059(class_1294.field_5919) || mc.field_1724.method_21754() || mc.field_1687.method_8320(class_2338.method_49638(mc.field_1724.method_19538())).method_26204() == class_2246.field_10343;
      if (mc.field_1724.field_6017 > 1.0F && (double)mc.field_1724.field_6017 < 1.14D) {
         return false;
      } else if (ModuleManager.aura.getAttackCooldown() < (mc.field_1724.method_24828() ? 1.0F : 0.9F)) {
         return false;
      } else {
         boolean mergeWithTargetStrafe = !ModuleManager.targetStrafe.isEnabled() || !(Boolean)ModuleManager.targetStrafe.jump.getValue();
         boolean mergeWithSpeed = !ModuleManager.speed.isEnabled() || mc.field_1724.method_24828();
         if (!mc.field_1690.field_1903.method_1434() && mergeWithTargetStrafe && mergeWithSpeed && !(Boolean)this.onlySpace.getValue() && !(Boolean)this.autoJump.getValue()) {
            return true;
         } else if (mc.field_1724.method_5771()) {
            return true;
         } else if (!mc.field_1690.field_1903.method_1434() && ModuleManager.aura.isAboveWater()) {
            return true;
         } else if (reasonForSkipCrit) {
            return true;
         } else {
            return !mc.field_1724.method_24828() && mc.field_1724.field_6017 > 0.0F;
         }
      }
   }
}
