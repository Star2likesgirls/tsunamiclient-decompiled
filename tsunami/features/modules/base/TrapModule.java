package tsunami.features.modules.base;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_1657;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_243;
import net.minecraft.class_3965;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tsunami.events.impl.EventPostSync;
import tsunami.events.impl.EventSync;
import tsunami.features.modules.Module;
import tsunami.setting.Setting;
import tsunami.utility.player.InteractionUtility;
import tsunami.utility.world.HoleUtility;

public abstract class TrapModule extends PlaceModule {
   protected final Setting<TrapModule.PlaceTiming> placeTiming;
   protected final Setting<Integer> blocksPerTick;
   protected final Setting<Integer> placeDelay;
   protected final Setting<TrapModule.TrapMode> trapMode;
   private int delay;
   protected class_1657 target;
   private final ArrayList<class_2338> sequentialBlocks;

   public TrapModule(@NotNull String name, @NotNull Module.Category category) {
      super(name, category);
      this.placeTiming = new Setting("Place Timing", TrapModule.PlaceTiming.Default);
      this.blocksPerTick = new Setting("Block/Tick", 8, 1, 12, (v) -> {
         return this.placeTiming.getValue() == TrapModule.PlaceTiming.Default;
      });
      this.placeDelay = new Setting("Delay/Place", 3, 0, 10);
      this.trapMode = new Setting("Trap Mode", TrapModule.TrapMode.Full);
      this.sequentialBlocks = new ArrayList();
   }

   protected abstract boolean needNewTarget();

   @Nullable
   protected abstract class_1657 getTarget();

   public void onDisable() {
      this.target = null;
      this.delay = 0;
      this.sequentialBlocks.clear();
   }

   @EventHandler
   private void onSync(EventSync event) {
      if (this.needNewTarget()) {
         this.target = this.getTarget();
      } else {
         if (this.placeTiming.getValue() == TrapModule.PlaceTiming.Vanilla && !this.rotate.is(InteractionUtility.Rotate.None)) {
            class_2338 targetBlock = this.getBlockToPlace();
            if (targetBlock != null && mc.field_1724 != null) {
               class_3965 result = InteractionUtility.getPlaceResult(targetBlock, (InteractionUtility.Interact)this.interact.getValue(), false);
               if (result != null) {
                  float[] angle = InteractionUtility.calculateAngle(result.method_17784());
                  mc.field_1724.method_36456(angle[0]);
                  mc.field_1724.method_36457(angle[1]);
               }
            }
         }

      }
   }

   @EventHandler
   private void onPostSync(EventPostSync event) {
      if (this.delay > 0) {
         --this.delay;
      } else {
         InteractionUtility.Rotate rotateMod = this.placeTiming.is(TrapModule.PlaceTiming.Vanilla) && !this.rotate.is(InteractionUtility.Rotate.None) ? InteractionUtility.Rotate.None : (InteractionUtility.Rotate)this.rotate.getValue();
         if (this.placeTiming.getValue() == TrapModule.PlaceTiming.Default) {
            int placed = 0;

            while(placed < (Integer)this.blocksPerTick.getValue()) {
               class_2338 targetBlock = this.getBlockToPlace();
               if (targetBlock == null || !this.placeBlock(targetBlock, rotateMod)) {
                  break;
               }

               ++placed;
               this.delay = (Integer)this.placeDelay.getValue();
               this.inactivityTimer.reset();
            }
         } else if (this.placeTiming.getValue() == TrapModule.PlaceTiming.Vanilla) {
            class_2338 targetBlock = this.getBlockToPlace();
            if (targetBlock != null && this.placeBlock(targetBlock, rotateMod)) {
               this.sequentialBlocks.add(targetBlock);
               this.delay = (Integer)this.placeDelay.getValue();
               this.inactivityTimer.reset();
            }
         }

      }
   }

   @Nullable
   protected class_2338 getBlockToPlace() {
      return this.target != null && mc.field_1724 != null ? (class_2338)this.getBlocks(this.target).stream().filter((pos) -> {
         return pos.method_19770(mc.field_1724.method_19538()) < (double)this.range.getPow2Value();
      }).filter((pos) -> {
         return InteractionUtility.canPlaceBlock(pos, (InteractionUtility.Interact)this.interact.getValue(), true);
      }).max(Comparator.comparing((pos) -> {
         return mc.field_1724.method_5707(pos.method_46558());
      })).orElse((Object)null) : null;
   }

   protected List<class_2338> getBlocks(@NotNull class_1657 player) {
      class_243 playerPos = player.method_19538();
      List<class_2338> offsets = new ArrayList();
      List<class_2338> holePoses = HoleUtility.getHolePoses(playerPos);
      List<class_2338> surroundPoses = HoleUtility.getSurroundPoses(playerPos);
      if (mc.field_1724 != null && mc.field_1687 != null) {
         switch(((TrapModule.TrapMode)this.trapMode.getValue()).ordinal()) {
         case 0:
            offsets.addAll(holePoses.stream().map(class_2338::method_10074).toList());
            if (this.interact.getValue() != InteractionUtility.Interact.AirPlace) {
               offsets.addAll(this.addHelpOffsets(surroundPoses));
            }

            offsets.addAll(surroundPoses);
            offsets.addAll(surroundPoses.stream().map(class_2338::method_10084).toList());
            if (this.interact.getValue() != InteractionUtility.Interact.AirPlace) {
               surroundPoses.stream().map((pos) -> {
                  return pos.method_10086(2);
               }).filter((pos) -> {
                  return pos.method_19770(mc.field_1724.method_19538()) < (double)this.range.getPow2Value();
               }).max(Comparator.comparing((pos) -> {
                  return mc.field_1724.method_5707(pos.method_46558());
               })).ifPresent((pos) -> {
                  offsets.add(pos);
                  offsets.add(pos.method_10074());
               });
            }

            offsets.addAll(holePoses.stream().map((pos) -> {
               return pos.method_10086(2);
            }).toList());
            break;
         case 1:
            offsets.addAll(holePoses.stream().map(class_2338::method_10074).toList());
            if (this.interact.getValue() != InteractionUtility.Interact.AirPlace) {
               surroundPoses.stream().filter((pos) -> {
                  return pos.method_19770(mc.field_1724.method_19538()) < (double)this.range.getPow2Value();
               }).max(Comparator.comparing((pos) -> {
                  return player.method_5707(pos.method_46558());
               })).ifPresent((pos) -> {
                  offsets.add(pos);
                  offsets.add(pos.method_10084());
                  offsets.add(pos.method_10086(2));
               });
               offsets.addAll(this.addHelpOffsets(surroundPoses));
            }

            offsets.addAll(surroundPoses);
            offsets.addAll(holePoses.stream().map((pos) -> {
               return pos.method_10086(2);
            }).toList());
            break;
         case 2:
            offsets.addAll(holePoses.stream().map(class_2338::method_10074).toList());
            if (this.interact.getValue() != InteractionUtility.Interact.AirPlace) {
               surroundPoses.stream().map(class_2338::method_10074).filter((pos) -> {
                  return pos.method_19770(mc.field_1724.method_19538()) < (double)this.range.getPow2Value();
               }).max(Comparator.comparing((pos) -> {
                  return player.method_5707(pos.method_46558());
               })).ifPresent((pos) -> {
                  offsets.add(pos);
                  offsets.add(pos.method_10084());
                  offsets.add(pos.method_10086(3));
               });
            }

            offsets.addAll(surroundPoses.stream().map(class_2338::method_10084).toList());
            offsets.addAll(holePoses.stream().map((pos) -> {
               return pos.method_10086(2);
            }).toList());
         }

         return offsets;
      } else {
         return offsets;
      }
   }

   @NotNull
   private List<class_2338> addHelpOffsets(@NotNull List<class_2338> surroundPoses) {
      List<class_2338> helpOffsets = new ArrayList();
      if (mc.field_1687 != null && mc.field_1724 != null) {
         Stream var10000 = surroundPoses.stream().map(class_2338::method_10074).filter((pos) -> {
            return pos.method_19770(mc.field_1724.method_19538()) < (double)this.range.getPow2Value();
         }).filter((pos) -> {
            class_2350[] var1 = class_2350.values();
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
               class_2350 dir = var1[var3];
               if (!mc.field_1687.method_8320(pos.method_10081(dir.method_10163().method_30931())).method_45474()) {
                  return false;
               }
            }

            return true;
         });
         Objects.requireNonNull(helpOffsets);
         var10000.forEach(helpOffsets::add);
         return helpOffsets;
      } else {
         return helpOffsets;
      }
   }

   protected static enum PlaceTiming {
      Default,
      Vanilla;

      // $FF: synthetic method
      private static TrapModule.PlaceTiming[] $values() {
         return new TrapModule.PlaceTiming[]{Default, Vanilla};
      }
   }

   protected static enum TrapMode {
      Full,
      Legs,
      Head;

      // $FF: synthetic method
      private static TrapModule.TrapMode[] $values() {
         return new TrapModule.TrapMode[]{Full, Legs, Head};
      }
   }
}
