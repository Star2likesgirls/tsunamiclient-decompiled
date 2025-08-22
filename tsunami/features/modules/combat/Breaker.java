package tsunami.features.modules.combat;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_1268;
import net.minecraft.class_1657;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_243;
import net.minecraft.class_2480;
import net.minecraft.class_2680;
import tsunami.core.Managers;
import tsunami.core.manager.client.ModuleManager;
import tsunami.core.manager.player.CombatManager;
import tsunami.events.impl.EventSync;
import tsunami.features.modules.Module;
import tsunami.features.modules.player.SpeedMine;
import tsunami.setting.Setting;
import tsunami.utility.Timer;
import tsunami.utility.world.ExplosionUtility;
import tsunami.utility.world.HoleUtility;

public final class Breaker extends Module {
   private final Setting<Breaker.Target> targetMode;
   private final Setting<Boolean> onlyIfHole;
   private final Setting<CombatManager.TargetBy> targetBy;
   private final Setting<Integer> range;
   private final Setting<Float> minDamage;
   private final Setting<Float> maxSelfDamage;
   private final Setting<Boolean> cevPriority;
   private final Setting<Boolean> antiShulker;
   private class_2338 blockPos;
   private final Timer pause;

   public Breaker() {
      super("Breaker", Module.Category.NONE);
      this.targetMode = new Setting("Target", Breaker.Target.AutoCrystal);
      this.onlyIfHole = new Setting("OnlyIfHole", false);
      this.targetBy = new Setting("TargetBy", CombatManager.TargetBy.Distance, (v) -> {
         return this.targetMode.is(Breaker.Target.Breaker);
      });
      this.range = new Setting("Range", 5, 1, 7, (v) -> {
         return this.targetMode.is(Breaker.Target.Breaker);
      });
      this.minDamage = new Setting("MinDamage", 7.0F, 0.0F, 36.0F);
      this.maxSelfDamage = new Setting("MaxSelfDamage", 4.0F, 0.0F, 36.0F);
      this.cevPriority = new Setting("CevPriority", true);
      this.antiShulker = new Setting("AntiShulker", true);
      this.pause = new Timer();
   }

   @EventHandler
   private void onSync(EventSync event) {
      class_1657 target;
      if (this.targetMode.is(Breaker.Target.Breaker)) {
         target = Managers.COMBAT.getTarget((float)(Integer)this.range.getValue(), (CombatManager.TargetBy)this.targetBy.getValue());
      } else {
         target = AutoCrystal.target;
      }

      if (target != null) {
         class_2338 burrow = class_2338.method_49638(target.method_19538());
         class_2680 burrowState = mc.field_1687.method_8320(burrow);
         if (this.pause.passedMs(600L)) {
            if (this.blockPos != null) {
               if (mc.field_1687.method_22347(this.blockPos) || mc.field_1724.method_5707(this.blockPos.method_46558()) > (double)(ModuleManager.speedMine.isEnabled() ? ModuleManager.speedMine.range.getPow2Value() : (ModuleManager.reach.isEnabled() ? ModuleManager.reach.blocksRange.getPow2Value() : 9.0F))) {
                  this.blockPos = null;
                  return;
               }

               if (!ModuleManager.speedMine.isEnabled()) {
                  mc.field_1761.method_2902(this.blockPos, class_2350.field_11036);
               } else {
                  if (ModuleManager.speedMine.alreadyActing(this.blockPos)) {
                     return;
                  }

                  Iterator var5 = ModuleManager.speedMine.actions.iterator();

                  while(var5.hasNext()) {
                     SpeedMine.MineAction action = (SpeedMine.MineAction)var5.next();
                     if (action.instantBreaking()) {
                        return;
                     }
                  }

                  mc.field_1761.method_2910(this.blockPos, class_2350.field_11036);
               }

               mc.field_1724.method_6104(class_1268.field_5808);
            }

            ArrayList<Breaker.BreakData> list = new ArrayList();
            if ((Boolean)this.cevPriority.getValue()) {
               for(int y = 2; y <= 3; ++y) {
                  class_2338 bp = class_2338.method_49637(target.method_23317(), target.method_23318() + (double)y, target.method_23321());
                  if (mc.field_1687.method_8320(bp).method_26204() == class_2246.field_10540 && !bp.equals(class_2338.method_49638(target.method_19538()).method_10074()) && ModuleManager.autoCrystal.getInteractResult(bp, new class_243((double)(0.5F + (float)bp.method_10263()), (double)(1.0F + (float)bp.method_10264()), (double)(0.5F + (float)bp.method_10260()))) != null) {
                     class_2680 currentState = mc.field_1687.method_8320(bp);
                     mc.field_1687.method_8501(bp, class_2246.field_10124.method_9564());
                     float damage = ExplosionUtility.getExplosionDamage(bp.method_46558().method_1031(0.0D, -0.5D, 0.0D), target, false);
                     float selfDamage = ExplosionUtility.getExplosionDamage(bp.method_46558().method_1031(0.0D, -0.5D, 0.0D), mc.field_1724, false);
                     mc.field_1687.method_8501(bp, currentState);
                     if ((Float.isNaN(ModuleManager.autoCrystal.renderDamage) || ModuleManager.autoCrystal.renderDamage < damage) && selfDamage < (Float)this.maxSelfDamage.getValue() && damage >= (Float)this.minDamage.getValue()) {
                        list.add(new Breaker.BreakData(bp, damage));
                     }
                  }
               }

               Breaker.BreakData best = (Breaker.BreakData)list.stream().max(Comparator.comparing(Breaker.BreakData::damage)).orElse((Object)null);
               if (best != null && (Boolean)this.cevPriority.getValue()) {
                  list.add(new Breaker.BreakData(best.blockPos, 999.0F));
               }
            }

            boolean inBurrow = burrowState.method_26204() == class_2246.field_10540 || burrowState.method_26204() == class_2246.field_10443;
            if (inBurrow) {
               list.add(new Breaker.BreakData(burrow, 995.0F));
               mc.field_1687.method_8501(burrow, class_2246.field_10124.method_9564());
            } else if ((Boolean)this.onlyIfHole.getValue() && !HoleUtility.isHole(class_2338.method_49638(target.method_19538()))) {
               return;
            }

            for(int x = -2; x <= 2; ++x) {
               for(int y = 0; y <= 3; ++y) {
                  for(int z = -2; z <= 2; ++z) {
                     if (y <= 1 || x != -2 && z != -2 && x != 2 && z != 2) {
                        class_2338 bp = class_2338.method_49637(target.method_23317() + (double)x, target.method_23318() + (double)y, target.method_23321() + (double)z);
                        if (mc.field_1687.method_8320(bp).method_26204() instanceof class_2480 && (Boolean)this.antiShulker.getValue()) {
                           list.add(new Breaker.BreakData(burrow, 990.0F));
                        }

                        if ((mc.field_1687.method_8320(bp).method_26204() == class_2246.field_10540 || mc.field_1687.method_8320(bp).method_26204() == class_2246.field_10443) && (mc.field_1687.method_8320(bp.method_10074()).method_26204() == class_2246.field_10540 || mc.field_1687.method_8320(bp.method_10074()).method_26204() == class_2246.field_9987) && !bp.equals(class_2338.method_49638(target.method_19538()).method_10074()) && ModuleManager.autoCrystal.getInteractResult(bp, new class_243((double)(0.5F + (float)bp.method_10263()), (double)(1.0F + (float)bp.method_10264()), (double)(0.5F + (float)bp.method_10260()))) != null) {
                           class_2680 currentState = mc.field_1687.method_8320(bp);
                           mc.field_1687.method_8501(bp, class_2246.field_10124.method_9564());
                           float damage = ExplosionUtility.getExplosionDamage(bp.method_46558().method_1031(0.0D, -0.5D, 0.0D), target, false);
                           float selfDamage = ExplosionUtility.getExplosionDamage(bp.method_46558().method_1031(0.0D, -0.5D, 0.0D), mc.field_1724, false);
                           mc.field_1687.method_8501(bp, currentState);
                           if (ModuleManager.autoCrystal.renderDamage < damage && selfDamage <= (Float)this.maxSelfDamage.getValue() && damage >= (Float)this.minDamage.getValue() && bp != this.blockPos) {
                              list.add(new Breaker.BreakData(bp, damage));
                           }
                        }
                     }
                  }
               }
            }

            if (inBurrow) {
               mc.field_1687.method_8501(burrow, burrowState);
            }

            Breaker.BreakData best = (Breaker.BreakData)list.stream().max(Comparator.comparing(Breaker.BreakData::damage)).orElse((Object)null);
            Breaker.BreakData secondBest = (Boolean)ModuleManager.speedMine.doubleMine.getValue() ? (Breaker.BreakData)list.stream().sorted(Comparator.comparing(Breaker.BreakData::damage).reversed()).skip(1L).findFirst().orElse((Object)null) : null;
            this.blockPos = best == null ? null : (ModuleManager.speedMine.alreadyActing(best.blockPos()) && secondBest != null ? secondBest.blockPos() : best.blockPos());
         }
      }
   }

   public void pause() {
      this.pause.reset();
   }

   private static enum Target {
      AutoCrystal,
      Breaker;

      // $FF: synthetic method
      private static Breaker.Target[] $values() {
         return new Breaker.Target[]{AutoCrystal, Breaker};
      }
   }

   private static record BreakData(class_2338 blockPos, float damage) {
      private BreakData(class_2338 blockPos, float damage) {
         this.blockPos = blockPos;
         this.damage = damage;
      }

      public class_2338 blockPos() {
         return this.blockPos;
      }

      public float damage() {
         return this.damage;
      }
   }
}
