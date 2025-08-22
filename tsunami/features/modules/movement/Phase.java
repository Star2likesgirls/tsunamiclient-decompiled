package tsunami.features.modules.movement;

import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_1268;
import net.minecraft.class_1802;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_2846;
import net.minecraft.class_2848;
import net.minecraft.class_2879;
import net.minecraft.class_2886;
import net.minecraft.class_3959;
import net.minecraft.class_239.class_240;
import net.minecraft.class_2828.class_2829;
import net.minecraft.class_2846.class_2847;
import net.minecraft.class_2848.class_2849;
import net.minecraft.class_3959.class_242;
import net.minecraft.class_3959.class_3960;
import tsunami.core.manager.client.ModuleManager;
import tsunami.events.impl.EventBreakBlock;
import tsunami.events.impl.EventCollision;
import tsunami.events.impl.EventPostSync;
import tsunami.events.impl.EventSync;
import tsunami.features.modules.Module;
import tsunami.features.modules.player.AutoTool;
import tsunami.setting.Setting;
import tsunami.utility.player.InteractionUtility;
import tsunami.utility.player.InventoryUtility;
import tsunami.utility.player.MovementUtility;

public class Phase extends Module {
   private final Setting<Phase.Mode> mode;
   private final Setting<Boolean> silent;
   private final Setting<Boolean> waitBreak;
   private final Setting<Boolean> onlyOnGround;
   private final Setting<Boolean> autoDisable;
   private final Setting<Integer> afterBreak;
   private final Setting<Integer> afterPearl;
   private final Setting<Float> pitch;
   private final Setting<Boolean> strict;
   public int clipTimer;
   public int afterPearlTime;

   public Phase() {
      super("Phase", Module.Category.NONE);
      this.mode = new Setting("Mode", Phase.Mode.Vanilla);
      this.silent = new Setting("Silent", false, (v) -> {
         return this.mode.getValue() == Phase.Mode.Sunrise;
      });
      this.waitBreak = new Setting("WaitBreak", true, (v) -> {
         return this.mode.getValue() == Phase.Mode.Sunrise;
      });
      this.onlyOnGround = new Setting("OnlyOnGround", false, (v) -> {
         return this.mode.is(Phase.Mode.Pearl);
      });
      this.autoDisable = new Setting("AutoDisable", false, (v) -> {
         return this.mode.getValue() == Phase.Mode.Pearl;
      });
      this.afterBreak = new Setting("BreakTimeout", 4, 1, 20, (v) -> {
         return this.mode.getValue() == Phase.Mode.Sunrise && (Boolean)this.waitBreak.getValue();
      });
      this.afterPearl = new Setting("PearlTimeout", 0, 0, 60, (v) -> {
         return this.mode.getValue() == Phase.Mode.Pearl;
      });
      this.pitch = new Setting("Pitch", 80.0F, 0.0F, 90.0F, (v) -> {
         return this.mode.getValue() == Phase.Mode.Pearl;
      });
      this.strict = new Setting("Strict", false, (v) -> {
         return this.mode.is(Phase.Mode.ForceMine);
      });
   }

   @EventHandler
   public void onCollide(EventCollision e) {
      if (!fullNullCheck()) {
         class_2338 playerPos = class_2338.method_49638(mc.field_1724.method_19538());
         if ((!this.mode.is(Phase.Mode.CCClip) && !this.mode.is(Phase.Mode.Pearl) && !this.mode.is(Phase.Mode.ForceMine) && this.canNoClip() || this.afterPearlTime > 0) && (!e.getPos().equals(playerPos.method_10074()) || mc.field_1690.field_1832.method_1434())) {
            e.setState(class_2246.field_10124.method_9564());
         }

         if (this.mode.is(Phase.Mode.ForceMine)) {
            float xDelta = (float)Math.abs(playerPos.method_10263() - e.getPos().method_10263());
            float zDelta = (float)Math.abs(playerPos.method_10260() - e.getPos().method_10260());
            if (xDelta != 0.0F && zDelta != 0.0F && (Boolean)this.strict.getValue()) {
               return;
            }

            if (!e.getPos().equals(playerPos.method_10074()) || mc.field_1690.field_1832.method_1434()) {
               e.setState(class_2246.field_10124.method_9564());
            }
         }

      }
   }

   public void onEnable() {
      this.afterPearlTime = 0;
      this.clipTimer = 0;
      if (mc.field_1724.method_24828() && this.mode.is(Phase.Mode.CCClip)) {
         double[] diagonalOffset = MovementUtility.forwardWithoutStrafe(0.44D);
         boolean diagonal = mc.field_1724.method_36454() % 90.0F > 35.0F && mc.field_1724.method_36454() % 90.0F < 55.0F;
         this.sendPacket(new class_2848(mc.field_1724, class_2849.field_12981));
         double[] directionVec;
         int height;
         if (diagonal) {
            directionVec = MovementUtility.forwardWithoutStrafe(0.51D);
            height = mc.field_1687.method_17742(new class_3959(mc.field_1724.method_33571(), mc.field_1724.method_33571().method_1031(diagonalOffset[0], 0.0D, diagonalOffset[1]), class_3960.field_17558, class_242.field_1348, mc.field_1724)).method_17783().equals(class_240.field_1333) ? 1 : 2;
            mc.field_1724.method_5814(mc.field_1724.method_23317() + directionVec[0], mc.field_1724.method_23318() + (double)height, mc.field_1724.method_23321() + directionVec[1]);
            this.sendPacket(new class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318(), mc.field_1724.method_23321(), true));
            height = mc.field_1687.method_22347(class_2338.method_49638(mc.field_1724.method_19538().method_1031(diagonalOffset[0], -2.0D, diagonalOffset[1]))) ? 2 : 1;
            mc.field_1724.method_5814(mc.field_1724.method_23317() + directionVec[0], mc.field_1724.method_23318() - (double)height, mc.field_1724.method_23321() + directionVec[1]);
            this.sendPacket(new class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318(), mc.field_1724.method_23321(), true));
            this.disable("diagonal");
         } else {
            directionVec = MovementUtility.forwardWithoutStrafe(0.57D);
            height = mc.field_1687.method_17742(new class_3959(mc.field_1724.method_33571(), mc.field_1724.method_33571().method_1031(diagonalOffset[0], 0.0D, diagonalOffset[1]), class_3960.field_17558, class_242.field_1348, mc.field_1724)).method_17783().equals(class_240.field_1333) ? 1 : 2;
            mc.field_1724.method_5814(mc.field_1724.method_23317() + directionVec[0], mc.field_1724.method_23318() + (double)height, mc.field_1724.method_23321() + directionVec[1]);
            this.sendPacket(new class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318(), mc.field_1724.method_23321(), true));
            mc.field_1724.method_5814(mc.field_1724.method_23317() + directionVec[0], mc.field_1724.method_23318(), mc.field_1724.method_23321() + directionVec[1]);
            this.sendPacket(new class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318(), mc.field_1724.method_23321(), true));
            height = mc.field_1687.method_22347(class_2338.method_49638(mc.field_1724.method_19538().method_1031(diagonalOffset[0], -2.0D, diagonalOffset[1]))) ? 2 : 1;
            mc.field_1724.method_5814(mc.field_1724.method_23317() + directionVec[0], mc.field_1724.method_23318() - (double)height, mc.field_1724.method_23321() + directionVec[1]);
            this.sendPacket(new class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318(), mc.field_1724.method_23321(), true));
            this.disable("normal");
         }
      }

   }

   @EventHandler
   public void onSync(EventSync e) {
      if (!fullNullCheck()) {
         if (this.clipTimer > 0) {
            --this.clipTimer;
         }

         if (this.afterPearlTime > 0) {
            --this.afterPearlTime;
         }

         double[] dir;
         class_2338 blockToBreak;
         int z;
         int prevItem;
         if (this.mode.getValue() == Phase.Mode.Sunrise && (mc.field_1724.field_5976 || this.playerInsideBlock()) && !mc.field_1724.method_5869() && !mc.field_1724.method_5771() && this.clipTimer <= 0) {
            dir = MovementUtility.forward(0.5D);
            blockToBreak = null;
            if (mc.field_1690.field_1903.method_1434()) {
               blockToBreak = class_2338.method_49637(mc.field_1724.method_23317() + dir[0], mc.field_1724.method_23318() + 2.0D, mc.field_1724.method_23321() + dir[1]);
            } else if (mc.field_1690.field_1832.method_1434()) {
               blockToBreak = class_2338.method_49637(mc.field_1724.method_23317() + dir[0], mc.field_1724.method_23318() - 1.0D, mc.field_1724.method_23321() + dir[1]);
            } else if (MovementUtility.isMoving()) {
               blockToBreak = class_2338.method_49637(mc.field_1724.method_23317() + dir[0], mc.field_1724.method_23318(), mc.field_1724.method_23321() + dir[1]);
            }

            if (blockToBreak == null) {
               return;
            }

            z = AutoTool.getTool(blockToBreak);
            if (z == -1) {
               return;
            }

            prevItem = mc.field_1724.method_31548().field_7545;
            InventoryUtility.switchTo(z);
            mc.field_1761.method_2902(blockToBreak, mc.field_1724.method_5735());
            mc.field_1724.method_6104(class_1268.field_5808);
            if ((Boolean)this.silent.getValue()) {
               InventoryUtility.switchTo(prevItem);
            }
         }

         if (this.mode.getValue() == Phase.Mode.ForceMine && (mc.field_1724.field_5976 || this.playerInsideBlock()) && !mc.field_1724.method_5869() && !mc.field_1724.method_5771()) {
            for(int x = -2; x < 2; ++x) {
               for(int y = -1; y < 3; ++y) {
                  for(z = -2; z < 2; ++z) {
                     if ((x != 0 || y != 0 || z != 0) && (x != 0 || y != 1 || z != 0) || mc.field_1690.field_1832.method_1434()) {
                        class_2338 bp = class_2338.method_49638(mc.field_1724.method_19538()).method_10069(x, y, z);
                        if (mc.field_1724.method_5829().method_994(new class_238(bp)) && !mc.field_1687.method_22347(bp)) {
                           this.sendPacket(new class_2846(class_2847.field_12973, bp, class_2350.field_11036));
                        }
                     }
                  }
               }
            }
         }

         if (this.mode.getValue() == Phase.Mode.Pearl && (mc.field_1724.method_24828() || !(Boolean)this.onlyOnGround.getValue()) && mc.field_1724.field_5976 && !this.playerInsideBlock() && this.clipTimer <= 0 && mc.field_1724.field_6012 > 60) {
            dir = MovementUtility.forward(0.5D);
            blockToBreak = class_2338.method_49637(mc.field_1724.method_23317() + dir[0], mc.field_1724.method_23318(), mc.field_1724.method_23321() + dir[1]);
            if (mc.field_1690.field_1832.method_1434()) {
               return;
            }

            float[] angle = InteractionUtility.calculateAngle(blockToBreak.method_46558());
            prevItem = this.findEPSlot();
            if (prevItem != -1) {
               ModuleManager.autoCrystal.pause();
               ModuleManager.aura.pause();
               mc.field_1724.method_36456(angle[0]);
               mc.field_1724.method_36457((Float)this.pitch.getValue());
            }
         }

      }
   }

   @EventHandler
   public void onPostSync(EventPostSync e) {
      if (this.mode.getValue() == Phase.Mode.Pearl && (mc.field_1724.method_24828() || !(Boolean)this.onlyOnGround.getValue()) && mc.field_1724.field_5976 && !this.playerInsideBlock() && this.clipTimer <= 0 && mc.field_1724.field_6012 > 60) {
         if (mc.field_1690.field_1832.method_1434()) {
            return;
         }

         int epSlot = this.findEPSlot();
         int prevItem = mc.field_1724.method_31548().field_7545;
         if (epSlot != -1) {
            InventoryUtility.switchTo(epSlot);
            this.sendSequencedPacket((id) -> {
               return new class_2886(class_1268.field_5808, id, mc.field_1724.method_36454(), mc.field_1724.method_36455());
            });
            this.sendPacket(new class_2879(class_1268.field_5808));
            InventoryUtility.switchTo(prevItem);
            if ((Boolean)this.autoDisable.getValue()) {
               this.disable();
            }
         }

         this.clipTimer = 20;
         this.afterPearlTime = (Integer)this.afterPearl.getValue();
      }

   }

   private int findEPSlot() {
      int epSlot = -1;
      if (mc.field_1724.method_6047().method_7909() == class_1802.field_8634) {
         epSlot = mc.field_1724.method_31548().field_7545;
      }

      if (epSlot == -1) {
         for(int l = 0; l < 9; ++l) {
            if (mc.field_1724.method_31548().method_5438(l).method_7909() == class_1802.field_8634) {
               epSlot = l;
               break;
            }
         }
      }

      return epSlot;
   }

   public boolean canNoClip() {
      if (this.mode.is(Phase.Mode.Vanilla)) {
         return true;
      } else if (!(Boolean)this.waitBreak.getValue()) {
         return true;
      } else {
         return this.clipTimer != 0;
      }
   }

   public boolean playerInsideBlock() {
      return !mc.field_1687.method_22347(class_2338.method_49638(mc.field_1724.method_19538()));
   }

   @EventHandler
   public void onBreakBlock(EventBreakBlock e) {
      this.clipTimer = (Integer)this.afterBreak.getValue();
   }

   private static enum Mode {
      Vanilla,
      Pearl,
      Sunrise,
      ForceMine,
      CCClip;

      // $FF: synthetic method
      private static Phase.Mode[] $values() {
         return new Phase.Mode[]{Vanilla, Pearl, Sunrise, ForceMine, CCClip};
      }
   }
}
