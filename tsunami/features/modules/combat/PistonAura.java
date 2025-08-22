package tsunami.features.modules.combat;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_1268;
import net.minecraft.class_1297;
import net.minecraft.class_1303;
import net.minecraft.class_1511;
import net.minecraft.class_1657;
import net.minecraft.class_1802;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_2358;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_2436;
import net.minecraft.class_2665;
import net.minecraft.class_2680;
import net.minecraft.class_2765;
import net.minecraft.class_2767;
import net.minecraft.class_2868;
import net.minecraft.class_2879;
import net.minecraft.class_2885;
import net.minecraft.class_3414;
import net.minecraft.class_3417;
import net.minecraft.class_3419;
import net.minecraft.class_3959;
import net.minecraft.class_3965;
import net.minecraft.class_4587;
import net.minecraft.class_239.class_240;
import net.minecraft.class_2828.class_2831;
import net.minecraft.class_3959.class_242;
import net.minecraft.class_3959.class_3960;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tsunami.core.Managers;
import tsunami.events.impl.EventEntityRemoved;
import tsunami.events.impl.EventPostSync;
import tsunami.events.impl.EventSync;
import tsunami.events.impl.PacketEvent;
import tsunami.features.modules.Module;
import tsunami.features.modules.client.ClientSettings;
import tsunami.injection.accesors.IClientPlayerEntity;
import tsunami.setting.Setting;
import tsunami.utility.Timer;
import tsunami.utility.math.MathUtility;
import tsunami.utility.player.InteractionUtility;
import tsunami.utility.player.InventoryUtility;
import tsunami.utility.player.PlayerUtility;
import tsunami.utility.player.SearchInvResult;
import tsunami.utility.render.Render2DEngine;
import tsunami.utility.render.Render3DEngine;
import tsunami.utility.world.ExplosionUtility;

public final class PistonAura extends Module {
   private final Setting<Integer> placeDelay = new Setting("Delay/Place", 1, 0, 25);
   private final Setting<Integer> blocksPerTick = new Setting("Block/Tick", 3, 1, 4);
   private final Setting<PistonAura.Pattern> patternsSetting;
   private final Setting<Boolean> supportPlace;
   private final Setting<Boolean> bypass;
   private final Setting<Boolean> oldVersion;
   private final Setting<Boolean> trap;
   private final Setting<Float> targetRange;
   private final Setting<Float> placeRange;
   private final Setting<Float> wallRange;
   private final Setting<InteractionUtility.PlaceMode> placeMode;
   private final Setting<InteractionUtility.Interact> interact;
   private final Setting<InteractionUtility.Rotate> rotate;
   public class_1657 target;
   private class_2338 targetPos;
   private class_2338 pistonPos;
   private class_2338 crystalPos;
   private class_2338 redStonePos;
   private class_2338 firePos;
   private class_2338 pistonHeadPos;
   private boolean builtTrap;
   private boolean isFire;
   private final Timer trapTimer;
   private final Timer attackTimer;
   private int delay;
   private Runnable postAction;
   private PistonAura.Stage stage;
   private class_1511 lastCrystal;
   private class_243 rotations;

   public PistonAura() {
      super("PistonAura", Module.Category.NONE);
      this.patternsSetting = new Setting("Pattern", PistonAura.Pattern.All);
      this.supportPlace = new Setting("SupportPlace", false);
      this.bypass = new Setting("FireBypass", false);
      this.oldVersion = new Setting("OldVersion", false);
      this.trap = new Setting("Trap", false);
      this.targetRange = new Setting("Target Range", 10.0F, 0.0F, 12.0F);
      this.placeRange = new Setting("PlaceRange", 4.0F, 1.0F, 7.0F);
      this.wallRange = new Setting("WallRange", 4.0F, 1.0F, 7.0F);
      this.placeMode = new Setting("Place Mode", InteractionUtility.PlaceMode.Normal);
      this.interact = new Setting("Interact", InteractionUtility.Interact.Strict);
      this.rotate = new Setting("Rotate", InteractionUtility.Rotate.None);
      this.trapTimer = new Timer();
      this.attackTimer = new Timer();
      this.delay = 0;
      this.postAction = null;
      this.stage = PistonAura.Stage.Searching;
   }

   public void reset() {
      this.builtTrap = false;
      this.target = null;
      this.stage = PistonAura.Stage.Searching;
      this.trapTimer.reset();
      this.attackTimer.reset();
      this.rotations = class_243.field_1353;
      this.pistonPos = null;
      this.targetPos = null;
      this.firePos = null;
      this.pistonHeadPos = null;
      this.lastCrystal = null;
      this.delay = 0;
      this.trapTimer.reset();
      this.attackTimer.reset();
      this.postAction = null;
   }

   public void onEnable() {
      this.reset();
   }

   @EventHandler
   public void onSync(EventSync event) {
      if (this.delay < (Integer)this.placeDelay.getValue()) {
         ++this.delay;
      }

      if (this.stage == PistonAura.Stage.Break) {
         if (this.isFire) {
            this.reset();
         } else {
            this.breakCrystal();
         }
      } else if (this.delay >= (Integer)this.placeDelay.getValue()) {
         this.handlePistonAura(false);
      }
   }

   @EventHandler
   private void onPostSync(EventPostSync event) {
      if (this.postAction != null) {
         this.delay = 0;
         this.postAction.run();
         this.postAction = null;

         for(int extraBlocks = 1; extraBlocks < (Integer)this.blocksPerTick.getValue(); ++extraBlocks) {
            this.handlePistonAura(true);
            if (this.postAction == null) {
               return;
            }

            this.postAction.run();
            this.postAction = null;
         }
      }

      this.postAction = null;
   }

   public void handlePistonAura(boolean extra) {
      if (InventoryUtility.findBlockInHotBar(class_2246.field_10540).found() || !(Boolean)this.trap.getValue() && !(Boolean)this.supportPlace.getValue()) {
         if (!InventoryUtility.findBlockInHotBar(class_2246.field_10002).found() && !InventoryUtility.findBlockInHotBar(class_2246.field_10523).found()) {
            this.disable(ClientSettings.isRu() ? "Нет редстоуна!" : "No redstone!");
         } else if (!InventoryUtility.findItemInHotBar(class_1802.field_8301).found() && mc.field_1724.method_6079().method_7909() != class_1802.field_8301) {
            this.disable(ClientSettings.isRu() ? "Нет кристаллов!" : "No crystals!");
         } else if (!InventoryUtility.findItemInHotBar(class_1802.field_8249).found() && !InventoryUtility.findItemInHotBar(class_1802.field_8105).found()) {
            this.disable(ClientSettings.isRu() ? "Нет поршней!" : "No pistons!");
         } else {
            switch(this.stage.ordinal()) {
            case 0:
               this.findPos();
               this.stage = PistonAura.Stage.Trap;
               break;
            case 1:
               this.buildTrap();
               break;
            case 2:
               this.placePiston(extra);
               break;
            case 3:
               this.placeFire(extra);
               break;
            case 4:
               this.placeCrystal(extra);
               break;
            case 5:
               this.placeRedStone(extra);
               break;
            case 6:
               if (this.isFire) {
                  this.stage = PistonAura.Stage.Searching;
               }
            }

         }
      } else {
         this.disable(ClientSettings.isRu() ? "Нет обсидиана!" : "No obsidian!");
      }
   }

   private void placeRedStone(boolean extra) {
      if (this.redStonePos == null) {
         this.stage = PistonAura.Stage.Searching;
      } else {
         if (mc.field_1687.method_8320(this.redStonePos).method_26204() instanceof class_2436) {
            this.stage = PistonAura.Stage.Break;
         }

         if (mc.field_1687.method_8320(this.redStonePos.method_10074()).method_45474() && (Boolean)this.supportPlace.getValue()) {
            InteractionUtility.placeBlock(this.redStonePos.method_10074(), (InteractionUtility.Rotate)this.rotate.getValue(), (InteractionUtility.Interact)this.interact.getValue(), (InteractionUtility.PlaceMode)this.placeMode.getValue(), InventoryUtility.findBlockInHotBar(class_2246.field_10540), false, false);
         } else {
            float[] angle = InteractionUtility.getPlaceAngle(this.redStonePos, (InteractionUtility.Interact)this.interact.getValue(), false);
            if (angle != null) {
               if (extra) {
                  this.sendPacket(new class_2831(angle[0], angle[1], mc.field_1724.method_24828()));
               } else {
                  mc.field_1724.method_36456(angle[0]);
                  mc.field_1724.method_36457(angle[1]);
               }

               this.postAction = () -> {
                  int redstone_slot = -1;
                  SearchInvResult redBlockResult = InventoryUtility.findBlockInHotBar(class_2246.field_10002);
                  SearchInvResult redTorchResult = InventoryUtility.findBlockInHotBar(class_2246.field_10523);
                  if (!redBlockResult.found()) {
                     if (!redTorchResult.found()) {
                        this.disable(ClientSettings.isRu() ? "Нет редстоуна!" : "No redstone!");
                     } else {
                        redstone_slot = redTorchResult.slot();
                     }
                  } else {
                     redstone_slot = redBlockResult.slot();
                  }

                  InteractionUtility.placeBlock(this.redStonePos, InteractionUtility.Rotate.None, (InteractionUtility.Interact)this.interact.getValue(), (InteractionUtility.PlaceMode)this.placeMode.getValue(), redstone_slot, false, false);
                  this.stage = PistonAura.Stage.Break;
               };
            }
         }
      }
   }

   private void placeCrystal(boolean extra) {
      if (this.crystalPos == null) {
         this.stage = PistonAura.Stage.Searching;
      } else if (mc.field_1687.method_8320(this.crystalPos).method_45474() && (Boolean)this.supportPlace.getValue()) {
         InteractionUtility.placeBlock(this.crystalPos, (InteractionUtility.Rotate)this.rotate.getValue(), (InteractionUtility.Interact)this.interact.getValue(), (InteractionUtility.PlaceMode)this.placeMode.getValue(), InventoryUtility.findBlockInHotBar(class_2246.field_10540), false, false);
      } else {
         class_3965 result = this.getPlaceData(this.crystalPos);
         if (result != null) {
            float[] angle = InteractionUtility.calculateAngle(this.rotations);
            if (extra) {
               this.sendPacket(new class_2831(angle[0] + MathUtility.random(-0.2F, 0.2F), angle[1], mc.field_1724.method_24828()));
            } else {
               mc.field_1724.method_36456(angle[0] + MathUtility.random(-0.2F, 0.2F));
               mc.field_1724.method_36457(angle[1]);
            }

            this.postAction = () -> {
               boolean offHand = mc.field_1724.method_6079().method_7909() == class_1802.field_8301;
               int prev_slot = -1;
               if (!offHand) {
                  int crystal_slot = InventoryUtility.findItemInHotBar(class_1802.field_8301).slot();
                  prev_slot = mc.field_1724.method_31548().field_7545;
                  if (crystal_slot != -1) {
                     mc.field_1724.method_31548().field_7545 = crystal_slot;
                     this.sendPacket(new class_2868(crystal_slot));
                  }
               }

               this.sendSequencedPacket((id) -> {
                  return new class_2885(offHand ? class_1268.field_5810 : class_1268.field_5808, result, id);
               });
               this.sendPacket(new class_2879(offHand ? class_1268.field_5810 : class_1268.field_5808));
               if (!offHand) {
                  mc.field_1724.method_31548().field_7545 = prev_slot;
                  this.sendPacket(new class_2868(prev_slot));
               }

               this.stage = PistonAura.Stage.RedStone;
            };
         }
      }
   }

   private void placeFire(boolean extra) {
      if (this.firePos == null) {
         this.stage = PistonAura.Stage.Searching;
      } else {
         if (mc.field_1687.method_8320(this.firePos).method_26204() instanceof class_2358) {
            this.stage = PistonAura.Stage.Crystal;
         }

         if (mc.field_1687.method_8320(this.firePos.method_10074()).method_45474() && (Boolean)this.supportPlace.getValue()) {
            InteractionUtility.placeBlock(this.firePos.method_10074(), (InteractionUtility.Rotate)this.rotate.getValue(), (InteractionUtility.Interact)this.interact.getValue(), (InteractionUtility.PlaceMode)this.placeMode.getValue(), InventoryUtility.findBlockInHotBar(class_2246.field_10540), false, false);
         } else {
            float[] angle = InteractionUtility.getPlaceAngle(this.firePos, (InteractionUtility.Interact)this.interact.getValue(), false);
            if (angle != null) {
               if (extra) {
                  this.sendPacket(new class_2831(angle[0], angle[1], mc.field_1724.method_24828()));
               } else {
                  mc.field_1724.method_36456(angle[0]);
                  mc.field_1724.method_36457(angle[1]);
               }

               this.postAction = () -> {
                  InteractionUtility.placeBlock(this.firePos, InteractionUtility.Rotate.None, (InteractionUtility.Interact)this.interact.getValue(), (InteractionUtility.PlaceMode)this.placeMode.getValue(), InventoryUtility.findItemInHotBar(class_1802.field_8884).slot(), false, false);
                  this.stage = PistonAura.Stage.Crystal;
               };
            }
         }
      }
   }

   public void buildTrap() {
      if (!(Boolean)this.trap.getValue()) {
         this.stage = PistonAura.Stage.Piston;
      } else if (mc.field_1687.method_8320(this.targetPos.method_10069(0, 2, 0)).method_26204() != class_2246.field_10540 && this.pistonPos.method_10264() < this.targetPos.method_10069(0, 2, 0).method_10264()) {
         if (!this.builtTrap) {
            class_2338 offset = new class_2338(this.crystalPos.method_10263() - this.targetPos.method_10263(), 0, this.crystalPos.method_10260() - this.targetPos.method_10260());
            class_2338 trapBase = this.targetPos.method_10069(offset.method_10263() * -1, 0, offset.method_10260() * -1);
            List<class_2338> trapPos = new ArrayList();
            trapPos.add(this.targetPos.method_10069(0, 2, 0));
            trapPos.add(trapBase.method_10069(0, 2, 0));
            trapPos.add(trapBase.method_10069(0, 1, 0));
            InventoryUtility.saveSlot();
            Iterator var4 = trapPos.iterator();

            while(var4.hasNext()) {
               class_2338 bp = (class_2338)var4.next();
               if (InteractionUtility.placeBlock(bp, (InteractionUtility.Rotate)this.rotate.getValue(), (InteractionUtility.Interact)this.interact.getValue(), (InteractionUtility.PlaceMode)this.placeMode.getValue(), InventoryUtility.findBlockInHotBar(class_2246.field_10540), false, false)) {
                  if (bp == this.targetPos.method_10069(0, 2, 0)) {
                     this.builtTrap = true;
                     this.stage = PistonAura.Stage.Piston;
                  }
                  break;
               }
            }

            InventoryUtility.returnSlot();
         }

      } else {
         this.stage = PistonAura.Stage.Piston;
      }
   }

   public void placePiston(boolean extra) {
      if (this.pistonPos == null) {
         this.stage = PistonAura.Stage.Searching;
      } else if (this.pistonHeadPos == null) {
         this.stage = PistonAura.Stage.Searching;
      } else {
         if (mc.field_1687.method_8320(this.pistonPos).method_26204() instanceof class_2665) {
            this.stage = this.isFire ? PistonAura.Stage.Fire : PistonAura.Stage.Crystal;
         }

         if (mc.field_1687.method_8320(this.pistonPos.method_10074()).method_45474() && (Boolean)this.supportPlace.getValue()) {
            InteractionUtility.placeBlock(this.pistonPos.method_10074(), (InteractionUtility.Rotate)this.rotate.getValue(), (InteractionUtility.Interact)this.interact.getValue(), (InteractionUtility.PlaceMode)this.placeMode.getValue(), InventoryUtility.findBlockInHotBar(class_2246.field_10540), false, false);
         } else {
            float[] angle = InteractionUtility.getPlaceAngle(this.pistonPos, (InteractionUtility.Interact)this.interact.getValue(), false);
            if (angle != null) {
               if (extra) {
                  this.sendPacket(new class_2831(angle[0], angle[1], mc.field_1724.method_24828()));
               } else {
                  mc.field_1724.method_36456(angle[0]);
                  mc.field_1724.method_36457(angle[1]);
               }

               this.postAction = () -> {
                  int piston_slot;
                  if (!InventoryUtility.findBlockInHotBar(class_2246.field_10560).found()) {
                     if (!InventoryUtility.findBlockInHotBar(class_2246.field_10615).found()) {
                        this.disable(ClientSettings.isRu() ? "Нет поршней!" : "No pistons!");
                        return;
                     }

                     piston_slot = InventoryUtility.findBlockInHotBar(class_2246.field_10615).slot();
                  } else {
                     piston_slot = InventoryUtility.findBlockInHotBar(class_2246.field_10560).slot();
                  }

                  float angle2 = InteractionUtility.calculateAngle(this.pistonHeadPos.method_46558(), this.pistonPos.method_46558())[0];
                  this.sendPacket(new class_2831(angle2, 0.0F, mc.field_1724.method_24828()));
                  float prevYaw = mc.field_1724.method_36454();
                  mc.field_1724.method_36456(angle2);
                  mc.field_1724.field_5982 = angle2;
                  ((IClientPlayerEntity)mc.field_1724).setLastYaw(angle2);
                  int prevSlot = mc.field_1724.method_31548().field_7545;
                  InteractionUtility.placeBlock(this.pistonPos, InteractionUtility.Rotate.None, (InteractionUtility.Interact)this.interact.getValue(), (InteractionUtility.PlaceMode)this.placeMode.getValue(), piston_slot, false, false);
                  this.sendPacket(new class_2868(prevSlot));
                  mc.field_1724.method_31548().field_7545 = prevSlot;
                  mc.field_1724.method_36456(prevYaw);
                  this.stage = this.isFire ? PistonAura.Stage.Fire : PistonAura.Stage.Crystal;
               };
            }
         }
      }
   }

   @Nullable
   public class_3965 getPlaceData(class_2338 bp) {
      class_2248 base = mc.field_1687.method_8320(bp).method_26204();
      class_2248 freeSpace = mc.field_1687.method_8320(bp.method_10084()).method_26204();
      class_2248 legacyFreeSpace = mc.field_1687.method_8320(bp.method_10084().method_10084()).method_26204();
      if (base != class_2246.field_10540 && base != class_2246.field_9987) {
         return null;
      } else if (freeSpace == class_2246.field_10124 && (!(Boolean)this.oldVersion.getValue() || legacyFreeSpace == class_2246.field_10124)) {
         if (this.checkEntities(bp)) {
            return null;
         } else {
            class_243 crystalVec = new class_243((double)(0.5F + (float)bp.method_10263()), (double)(1.0F + (float)bp.method_10264()), (double)(0.5F + (float)bp.method_10260()));
            class_3965 interactResult = null;
            switch((InteractionUtility.Interact)this.interact.getValue()) {
            case Vanilla:
               interactResult = this.getDefaultInteract(crystalVec, bp);
               break;
            case Strict:
               interactResult = this.getStrictInteract(bp);
               break;
            case Legit:
               interactResult = this.getLegitInteract(bp);
            }

            return interactResult;
         }
      } else {
         return null;
      }
   }

   private boolean checkEntities(@NotNull class_2338 base) {
      class_238 posBoundingBox = new class_238(base.method_10084());
      posBoundingBox = posBoundingBox.method_1009(0.0D, 1.0D, 0.0D);
      Iterator var3 = mc.field_1687.method_18112().iterator();

      class_1297 ent;
      do {
         if (!var3.hasNext()) {
            return false;
         }

         ent = (class_1297)var3.next();
      } while(ent == null || !ent.method_5829().method_994(posBoundingBox) || ent instanceof class_1303 || ent instanceof class_1511);

      return true;
   }

   @Nullable
   private class_3965 getDefaultInteract(class_243 crystalVector, class_2338 bp) {
      if (PlayerUtility.squaredDistanceFromEyes(crystalVector) > this.placeRange.getPow2Value()) {
         return null;
      } else {
         class_3965 wallCheck = mc.field_1687.method_17742(new class_3959(InteractionUtility.getEyesPos(mc.field_1724), crystalVector, class_3960.field_17558, class_242.field_1348, mc.field_1724));
         return wallCheck != null && wallCheck.method_17783() == class_240.field_1332 && wallCheck.method_17777() != bp && PlayerUtility.squaredDistanceFromEyes(crystalVector) > this.wallRange.getPow2Value() ? null : new class_3965(crystalVector, class_2350.field_11033, bp, false);
      }
   }

   @Nullable
   public class_3965 getStrictInteract(@NotNull class_2338 bp) {
      float bestDistance = 999.0F;
      class_2350 bestDirection = null;
      class_243 bestVector = null;
      if (mc.field_1724.method_33571().method_10214() > (double)bp.method_10084().method_10264()) {
         bestDirection = class_2350.field_11036;
         bestVector = new class_243((double)bp.method_10263() + 0.5D, (double)(bp.method_10264() + 1), (double)bp.method_10260() + 0.5D);
      } else if (mc.field_1724.method_33571().method_10214() < (double)bp.method_10264()) {
         bestDirection = class_2350.field_11033;
         bestVector = new class_243((double)bp.method_10263() + 0.5D, (double)bp.method_10264(), (double)bp.method_10260() + 0.5D);
      } else {
         class_2350[] var5 = class_2350.values();
         int var6 = var5.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            class_2350 dir = var5[var7];
            class_243 directionVec = new class_243((double)bp.method_10263() + 0.5D + (double)dir.method_10163().method_10263() * 0.5D, (double)bp.method_10264() + 0.5D + (double)dir.method_10163().method_10264() * 0.5D, (double)bp.method_10260() + 0.5D + (double)dir.method_10163().method_10260() * 0.5D);
            float distance = PlayerUtility.squaredDistanceFromEyes(directionVec);
            if (bestDistance > distance) {
               bestDirection = dir;
               bestVector = directionVec;
               bestDistance = distance;
            }
         }
      }

      if (bestVector == null) {
         return null;
      } else if (PlayerUtility.squaredDistanceFromEyes(bestVector) > this.placeRange.getPow2Value()) {
         return null;
      } else {
         class_3965 wallCheck = mc.field_1687.method_17742(new class_3959(InteractionUtility.getEyesPos(mc.field_1724), bestVector, class_3960.field_17558, class_242.field_1348, mc.field_1724));
         return wallCheck != null && wallCheck.method_17783() == class_240.field_1332 && wallCheck.method_17777() != bp && PlayerUtility.squaredDistanceFromEyes(bestVector) > this.wallRange.getPow2Value() ? null : new class_3965(bestVector, bestDirection, bp, false);
      }
   }

   public class_3965 getLegitInteract(class_2338 bp) {
      float bestDistance = 999.0F;
      class_3965 bestResult = null;

      for(float x = 0.0F; x <= 1.0F; x += 0.2F) {
         for(float y = 0.0F; y <= 1.0F; y += 0.2F) {
            for(float z = 0.0F; z <= 1.0F; z += 0.2F) {
               class_243 point = new class_243((double)((float)bp.method_10263() + x), (double)((float)bp.method_10264() + y), (double)((float)bp.method_10260() + z));
               float distance = PlayerUtility.squaredDistanceFromEyes(point);
               class_3965 wallCheck = mc.field_1687.method_17742(new class_3959(InteractionUtility.getEyesPos(mc.field_1724), point, class_3960.field_17558, class_242.field_1348, mc.field_1724));
               if (wallCheck == null || wallCheck.method_17783() != class_240.field_1332 || wallCheck.method_17777() == bp || !(distance > this.wallRange.getPow2Value())) {
                  class_3965 result = ExplosionUtility.rayCastBlock(new class_3959(InteractionUtility.getEyesPos(mc.field_1724), point, class_3960.field_17558, class_242.field_1348, mc.field_1724), bp);
                  if (!(distance > this.placeRange.getPow2Value()) && distance < bestDistance && result != null && result.method_17783() == class_240.field_1332) {
                     bestResult = result;
                     bestDistance = distance;
                  }
               }
            }
         }
      }

      return bestResult;
   }

   public void breakCrystal() {
      Iterator var1 = mc.field_1687.method_18112().iterator();

      while(var1.hasNext()) {
         class_1297 ent = (class_1297)var1.next();
         if (ent instanceof class_1511 && !(this.target.method_5707(ent.method_19538()) > 16.0D) && ent.field_6012 >= 2) {
            float[] angle = InteractionUtility.calculateAngle(ent.method_19538());
            mc.field_1724.method_36456(angle[0] + MathUtility.random(-3.0F, 3.0F));
            mc.field_1724.method_36457(angle[1]);
            if (this.attackTimer.passedMs(200L)) {
               mc.field_1761.method_2918(mc.field_1724, ent);
               mc.field_1724.method_6104(class_1268.field_5808);
               this.attackTimer.reset();
            }

            this.lastCrystal = (class_1511)ent;
         }
      }

   }

   @EventHandler
   private void onPacketReceive(@NotNull PacketEvent.Receive event) {
      if (event.getPacket() instanceof class_2767 && ((class_2767)event.getPacket()).method_11888().equals(class_3419.field_15245) && ((class_3414)((class_2767)event.getPacket()).method_11894().comp_349()).equals(class_3417.field_15152)) {
         if (this.lastCrystal == null || !this.lastCrystal.method_5805()) {
            return;
         }

         double soundRange = this.lastCrystal.method_5649(((class_2767)event.getPacket()).method_11890() + 0.5D, ((class_2767)event.getPacket()).method_11889() + 0.5D, ((class_2767)event.getPacket()).method_11893() + 0.5D);
         if (soundRange > 121.0D) {
            return;
         }

         this.reset();
      }

      if (event.getPacket() instanceof class_2765 && ((class_2765)event.getPacket()).method_11881().equals(class_3419.field_15245) && ((class_3414)((class_2765)event.getPacket()).method_11882().comp_349()).equals(class_3417.field_15152)) {
         if (this.lastCrystal == null || !this.lastCrystal.method_5805()) {
            return;
         }

         if (((class_2765)event.getPacket()).method_11883() != this.lastCrystal.method_5628()) {
            return;
         }

         this.reset();
      }

   }

   @EventHandler
   public void onEntityRemove(EventEntityRemoved e) {
      if (this.lastCrystal != null && this.lastCrystal == e.entity) {
         this.reset();
      }

   }

   @NotNull
   public String getDisplayInfo() {
      String var10000 = this.stage.toString();
      return var10000 + (this.target != null ? " | " + this.target.method_5477().getString() : "");
   }

   public void onRender3D(class_4587 stack) {
      if (this.pistonPos != null && this.crystalPos != null && this.redStonePos != null) {
         Render3DEngine.FILLED_QUEUE.add(new Render3DEngine.FillAction(new class_238(this.pistonHeadPos.method_10074()), Render2DEngine.injectAlpha(Color.CYAN, 100)));
         Render3DEngine.FILLED_QUEUE.add(new Render3DEngine.FillAction(new class_238(this.crystalPos), Render2DEngine.injectAlpha(Color.PINK, 100)));
         Render3DEngine.FILLED_QUEUE.add(new Render3DEngine.FillAction(new class_238(this.pistonPos.method_10074()), Render2DEngine.injectAlpha(Color.GREEN, 100)));
         Render3DEngine.FILLED_QUEUE.add(new Render3DEngine.FillAction(new class_238(this.redStonePos.method_10074()), Render2DEngine.injectAlpha(Color.RED, 100)));
         if (this.firePos != null) {
            Render3DEngine.FILLED_QUEUE.add(new Render3DEngine.FillAction(new class_238(this.firePos.method_10074()), Render2DEngine.injectAlpha(Color.yellow, 100)));
         }

      }
   }

   private void findPos() {
      ArrayList<PistonAura.Structure> list = new ArrayList();

      class_1657 target;
      for(Iterator var2 = ((List)Objects.requireNonNull(getPlayersSorted((Float)this.targetRange.getValue()))).iterator(); var2.hasNext(); this.target = target) {
         target = (class_1657)var2.next();

         for(int i = 0; i <= 2; ++i) {
            if (this.patternsSetting.getValue() == PistonAura.Pattern.Small || this.patternsSetting.getValue() == PistonAura.Pattern.All) {
               list.add(new PistonAura.Structure(target, new class_2338(1, i, 0), new class_2338(1, i, -1), new class_2338(0, i, -1), new class_2338[]{new class_2338(1, 0, -2)}, new class_2338[]{new class_2338(0, 0, 1), new class_2338(1, i, 1), new class_2338(0, 1 + i, 1)}));
               list.add(new PistonAura.Structure(target, new class_2338(1, i, 0), new class_2338(1, i, 1), new class_2338(0, i, 1), new class_2338[]{new class_2338(1, i, 2)}, new class_2338[]{new class_2338(0, i, -1), new class_2338(1, i, -1), new class_2338(0, 1 + i, -1)}));
               list.add(new PistonAura.Structure(target, new class_2338(0, i, 1), new class_2338(1, i, 1), new class_2338(1, i, 0), new class_2338[]{new class_2338(2, i, 1)}, new class_2338[]{new class_2338(-1, i, 1), new class_2338(-1, i, 0), new class_2338(-1, 1 + i, 0)}));
               list.add(new PistonAura.Structure(target, new class_2338(0, i, 1), new class_2338(-1, i, 1), new class_2338(-1, i, 0), new class_2338[]{new class_2338(-2, i, 1)}, new class_2338[]{new class_2338(1, i, 1), new class_2338(1, i, 0), new class_2338(1, 1 + i, 0)}));
               list.add(new PistonAura.Structure(target, new class_2338(-1, i, 0), new class_2338(-1, i, 1), new class_2338(0, i, 1), new class_2338[]{new class_2338(-1, i, 2)}, new class_2338[]{new class_2338(-1, i, -1), new class_2338(0, i, -1), new class_2338(0, 1 + i, -1)}));
               list.add(new PistonAura.Structure(target, new class_2338(-1, i, 0), new class_2338(-1, i, -1), new class_2338(0, i, -1), new class_2338[]{new class_2338(-1, i, -2)}, new class_2338[]{new class_2338(-1, i, 1), new class_2338(0, i, 1), new class_2338(0, 1 + i, 1)}));
               list.add(new PistonAura.Structure(target, new class_2338(0, i, -1), new class_2338(-1, i, -1), new class_2338(-1, i, 0), new class_2338[]{new class_2338(-2, i, -1)}, new class_2338[]{new class_2338(1, i, 0), new class_2338(1, i, -1), new class_2338(1, i + 1, 0)}));
               list.add(new PistonAura.Structure(target, new class_2338(0, i, -1), new class_2338(1, i, -1), new class_2338(1, i, 0), new class_2338[]{new class_2338(2, i, -1)}, new class_2338[]{new class_2338(-1, i, 0), new class_2338(-1, i, -1), new class_2338(-1, 1 + i, 0)}));
            }

            if (this.patternsSetting.getValue() == PistonAura.Pattern.Cross || this.patternsSetting.getValue() == PistonAura.Pattern.All) {
               list.add(new PistonAura.Structure(target, new class_2338(1, i, 0), new class_2338(2, i, -1), new class_2338(1, i, -1), new class_2338[]{new class_2338(3, i, -1), new class_2338(2, i, -2)}, new class_2338[]{new class_2338(0, i, -1), new class_2338(1, i, 1), new class_2338(0, i, 1), new class_2338(0, 1 + i, 1), new class_2338(0, 1 + i, -1)}));
               list.add(new PistonAura.Structure(target, new class_2338(1, i, 0), new class_2338(2, i, 1), new class_2338(1, i, 1), new class_2338[]{new class_2338(3, i, 1), new class_2338(2, i, 2)}, new class_2338[]{new class_2338(0, i, 1), new class_2338(1, i, -1), new class_2338(0, i, -1), new class_2338(0, 1 + i, -1), new class_2338(0, 1 + i, 1)}));
               list.add(new PistonAura.Structure(target, new class_2338(0, i, 1), new class_2338(1, i, 2), new class_2338(1, i, 1), new class_2338[]{new class_2338(1, i, 3), new class_2338(2, i, 2)}, new class_2338[]{new class_2338(1, i, 0), new class_2338(-1, i, 0), new class_2338(-1, i, 1), new class_2338(-1, 1 + i, 0), new class_2338(1, 1 + i, 0)}));
               list.add(new PistonAura.Structure(target, new class_2338(0, i, 1), new class_2338(-1, i, 2), new class_2338(-1, i, 1), new class_2338[]{new class_2338(-1, i, 3), new class_2338(-2, i, 2)}, new class_2338[]{new class_2338(-1, i, 0), new class_2338(1, i, 1), new class_2338(1, i, 0), new class_2338(-1, 1 + i, 0), new class_2338(1, 1 + i, 0)}));
               list.add(new PistonAura.Structure(target, new class_2338(-1, i, 0), new class_2338(-2, i, 1), new class_2338(-1, i, 1), new class_2338[]{new class_2338(-3, i, 1), new class_2338(-2, i, 2)}, new class_2338[]{new class_2338(0, i, 1), new class_2338(-1, i, -1), new class_2338(0, i, -1), new class_2338(0, 1 + i, 1), new class_2338(0, 1 + i, -1)}));
               list.add(new PistonAura.Structure(target, new class_2338(-1, i, 0), new class_2338(-2, i, -1), new class_2338(-1, i, -1), new class_2338[]{new class_2338(-3, i, -1), new class_2338(-2, i, -2)}, new class_2338[]{new class_2338(0, i, -1), new class_2338(0, i, -1), new class_2338(-1, i, 1), new class_2338(0, 1 + i, 1), new class_2338(0, 1 + i, -1)}));
               list.add(new PistonAura.Structure(target, new class_2338(0, i, -1), new class_2338(-1, i, -2), new class_2338(-1, i, -1), new class_2338[]{new class_2338(-1, i, -3), new class_2338(-2, i, -2)}, new class_2338[]{new class_2338(-1, i, 0), new class_2338(1, i, 0), new class_2338(1, i, -1), new class_2338(-1, 1 + i, 0), new class_2338(1, 1 + i, 0)}));
            }

            if (this.patternsSetting.getValue() == PistonAura.Pattern.Liner || this.patternsSetting.getValue() == PistonAura.Pattern.All) {
               list.add(new PistonAura.Structure(target, new class_2338(1, i, 0), new class_2338(2, i, 0), new class_2338(1, i, 0), new class_2338[]{new class_2338(3, i, 0)}, new class_2338[]{new class_2338(1, i, 1), new class_2338(1, i, -1), new class_2338(0, i, 1), new class_2338(0, i, -1), new class_2338(0, 1 + i, 1), new class_2338(0, 1 + i, -1)}));
               list.add(new PistonAura.Structure(target, new class_2338(-1, i, 0), new class_2338(-2, i, 0), new class_2338(-1, i, 0), new class_2338[]{new class_2338(-3, i, 0)}, new class_2338[]{new class_2338(-1, i, -1), new class_2338(-1, i, 1), new class_2338(0, i, 1), new class_2338(0, i, -1), new class_2338(0, 1 + i, 1), new class_2338(0, 1 + i, -1)}));
               list.add(new PistonAura.Structure(target, new class_2338(0, i, 1), new class_2338(0, i, 2), new class_2338(0, i, 1), new class_2338[]{new class_2338(0, i, 3)}, new class_2338[]{new class_2338(-1, i, 1), new class_2338(1, i, 1), new class_2338(-1, i, 0), new class_2338(1, i, 0), new class_2338(1, 1 + i, 0), new class_2338(-1, 1 + i, 0)}));
               list.add(new PistonAura.Structure(target, new class_2338(0, i, -1), new class_2338(0, i, -2), new class_2338(0, i, -1), new class_2338[]{new class_2338(0, i, -3)}, new class_2338[]{new class_2338(-1, i, -1), new class_2338(1, i, -1), new class_2338(-1, i, 0), new class_2338(1, i, 0), new class_2338(1, 1 + i, 0), new class_2338(-1, 1 + i, 0)}));
            }
         }
      }

      List bestStructure;
      PistonAura.Structure structure;
      if ((Boolean)this.bypass.getValue() && InventoryUtility.findItemInHotBar(class_1802.field_8884).found()) {
         bestStructure = list.stream().filter(PistonAura.Structure::isFirePa).sorted(Comparator.comparingDouble(PistonAura.Structure::getMaxRange)).toList();
         if (bestStructure.isEmpty()) {
            this.isFire = false;
            bestStructure = list.stream().filter(PistonAura.Structure::isNormalPa).sorted(Comparator.comparingDouble(PistonAura.Structure::getMaxRange)).toList();
            if (!bestStructure.isEmpty()) {
               structure = (PistonAura.Structure)bestStructure.get(0);
               this.pistonPos = structure.getPistonPos();
               this.crystalPos = structure.getCrystalPos();
               this.redStonePos = structure.getRedStonePos();
               this.pistonHeadPos = structure.getPistonHeadPos();
               this.targetPos = structure.targetPos;
               this.target = structure.getTarget();
            } else {
               this.disable(ClientSettings.isRu() ? "Нет цели или места!" : "No target or free space!");
            }
         } else {
            this.isFire = true;
            structure = (PistonAura.Structure)bestStructure.get(0);
            this.pistonPos = structure.getPistonPos();
            this.crystalPos = structure.getCrystalPos();
            this.redStonePos = structure.getRedStonePos();
            this.firePos = structure.getFirePos();
            this.pistonHeadPos = structure.getPistonHeadPos();
            this.targetPos = structure.targetPos;
            this.target = structure.getTarget();
         }
      } else {
         this.isFire = false;
         bestStructure = list.stream().filter(PistonAura.Structure::isNormalPa).sorted(Comparator.comparingDouble(PistonAura.Structure::getMaxRange)).toList();
         if (!bestStructure.isEmpty()) {
            structure = (PistonAura.Structure)bestStructure.getFirst();
            this.pistonPos = structure.getPistonPos();
            this.crystalPos = structure.getCrystalPos();
            this.redStonePos = structure.getRedStonePos();
            this.pistonHeadPos = structure.getPistonHeadPos();
            this.targetPos = structure.targetPos;
            this.target = structure.getTarget();
         } else {
            this.disable(ClientSettings.isRu() ? "Нет цели или места!" : "No target or free space!");
         }
      }

   }

   @NotNull
   public static List<class_1657> getPlayersSorted(float range) {
      synchronized(mc.field_1687.method_18456()) {
         List<class_1657> playerList = new ArrayList();
         Iterator var3 = mc.field_1687.method_18456().iterator();

         while(var3.hasNext()) {
            class_1657 player = (class_1657)var3.next();
            if (mc.field_1724 != player && !Managers.FRIEND.isFriend(player) && mc.field_1724.method_5858(player) <= (double)(range * range)) {
               playerList.add(player);
            }
         }

         playerList.sort(Comparator.comparing((playerx) -> {
            return mc.field_1724.method_5858(playerx);
         }));
         return playerList;
      }
   }

   public static enum Pattern {
      Small,
      Cross,
      Liner,
      All;

      // $FF: synthetic method
      private static PistonAura.Pattern[] $values() {
         return new PistonAura.Pattern[]{Small, Cross, Liner, All};
      }
   }

   public static enum Stage {
      Searching,
      Trap,
      Piston,
      Fire,
      Crystal,
      RedStone,
      Break;

      // $FF: synthetic method
      private static PistonAura.Stage[] $values() {
         return new PistonAura.Stage[]{Searching, Trap, Piston, Fire, Crystal, RedStone, Break};
      }
   }

   public class Structure {
      private final class_2338 pistonPos;
      private class_2338 crystalPos;
      private final class_2338 targetPos;
      private class_2338 redStonePos;
      private class_2338 firePos;
      private final class_1657 target;
      private class_2338 pistonHeadPos;

      public class_2338 getPistonHeadPos() {
         return this.pistonHeadPos;
      }

      public class_2338 getPistonPos() {
         return this.pistonPos;
      }

      public class_2338 getCrystalPos() {
         return this.crystalPos;
      }

      public class_2338 getRedStonePos() {
         return this.redStonePos;
      }

      public class_2338 getFirePos() {
         return this.firePos;
      }

      public class_1657 getTarget() {
         return this.target;
      }

      public Structure(@NotNull class_1657 target, @NotNull class_2338 crystalPos, @NotNull class_2338 pistonPos, @NotNull class_2338 pistonHeadPos, class_2338[] redStonePos, class_2338[] firePos) {
         this.target = target;
         this.targetPos = class_2338.method_49638(target.method_19538());
         this.pistonPos = this.canPlace(this.targetPos.method_10069(pistonPos.method_10263(), pistonPos.method_10264() + 1, pistonPos.method_10260())) ? this.targetPos.method_10069(pistonPos.method_10263(), pistonPos.method_10264() + 1, pistonPos.method_10260()) : null;
         this.crystalPos = PistonAura.this.getPlaceData(this.targetPos.method_10069(crystalPos.method_10263(), crystalPos.method_10264(), crystalPos.method_10260())) != null ? this.targetPos.method_10069(crystalPos.method_10263(), crystalPos.method_10264(), crystalPos.method_10260()) : null;
         this.pistonHeadPos = Module.mc.field_1687.method_22347(this.targetPos.method_10069(pistonHeadPos.method_10263(), pistonHeadPos.method_10264() + 1, pistonHeadPos.method_10260())) ? this.targetPos.method_10069(pistonHeadPos.method_10263(), pistonHeadPos.method_10264() + 1, pistonHeadPos.method_10260()) : null;
         if (this.pistonHeadPos != null && !Module.mc.field_1687.method_18467(class_1657.class, new class_238(this.pistonHeadPos)).isEmpty()) {
            this.pistonHeadPos = null;
         }

         if (this.crystalPos != null && !Module.mc.field_1687.method_18467(class_1297.class, new class_238(this.crystalPos)).isEmpty()) {
            this.crystalPos = null;
         }

         this.redStonePos = null;
         List<class_2338> tempRed = Arrays.stream(redStonePos).map((blockPos) -> {
            return this.targetPos.method_10069(blockPos.method_10263(), blockPos.method_10264() + 1, blockPos.method_10260());
         }).toList();
         class_2680 preState = Module.mc.field_1687.method_8320(pistonPos);
         Module.mc.field_1687.method_8501(pistonPos, class_2246.field_10560.method_9564());
         Iterator var10 = tempRed.iterator();

         while(var10.hasNext()) {
            class_2338 pos = (class_2338)var10.next();
            if (this.canPlace(pos)) {
               this.redStonePos = pos;
               break;
            }
         }

         Module.mc.field_1687.method_8501(pistonPos, preState);
         this.firePos = null;
         List<class_2338> tempFire = Arrays.stream(firePos).map((blockPos) -> {
            return this.targetPos.method_10069(blockPos.method_10263(), blockPos.method_10264() + 1, blockPos.method_10260());
         }).toList();
         Iterator var14 = tempFire.iterator();

         while(var14.hasNext()) {
            class_2338 posx = (class_2338)var14.next();
            if (this.canPlace(posx)) {
               this.firePos = posx;
               break;
            }
         }

      }

      public boolean isNormalPa() {
         return this.pistonPos != null && this.crystalPos != null && this.targetPos != null && this.redStonePos != null && this.pistonHeadPos != null;
      }

      public boolean isFirePa() {
         return this.pistonPos != null && this.crystalPos != null && this.targetPos != null && this.redStonePos != null && this.pistonHeadPos != null && this.firePos != null;
      }

      private boolean canPlace(class_2338 pos) {
         if (pos == null) {
            return false;
         } else {
            class_2680 prevBlockState = null;
            if ((Boolean)PistonAura.this.supportPlace.getValue()) {
               prevBlockState = Module.mc.field_1687.method_8320(pos.method_10074());
               if (prevBlockState.method_45474()) {
                  Module.mc.field_1687.method_8501(pos.method_10074(), class_2246.field_10540.method_9564());
               } else {
                  prevBlockState = null;
               }
            }

            boolean canPlace = InteractionUtility.canPlaceBlock(pos, (InteractionUtility.Interact)PistonAura.this.interact.getValue(), false);
            if (prevBlockState != null) {
               Module.mc.field_1687.method_8501(pos.method_10074(), prevBlockState);
            }

            return canPlace;
         }
      }

      public double getMaxRange() {
         if (this.pistonPos != null && this.crystalPos != null && this.redStonePos != null) {
            double piston = (double)InteractionUtility.squaredDistanceFromEyes(this.pistonPos.method_46558());
            double crystal = (double)InteractionUtility.squaredDistanceFromEyes(this.crystalPos.method_46558());
            double redStone = (double)InteractionUtility.squaredDistanceFromEyes(this.redStonePos.method_46558());
            class_2338 firePos = this.firePos != null ? this.firePos : this.pistonPos;
            double fire = (double)InteractionUtility.squaredDistanceFromEyes(firePos.method_46558());
            return Math.max(Math.max(fire, crystal), Math.max(redStone, piston));
         } else {
            return 999.0D;
         }
      }
   }
}
