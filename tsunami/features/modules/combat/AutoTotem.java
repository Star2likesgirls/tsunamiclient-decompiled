package tsunami.features.modules.combat;

import java.util.Iterator;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_1268;
import net.minecraft.class_1294;
import net.minecraft.class_1297;
import net.minecraft.class_1299;
import net.minecraft.class_1511;
import net.minecraft.class_1541;
import net.minecraft.class_1548;
import net.minecraft.class_1657;
import net.minecraft.class_1701;
import net.minecraft.class_1713;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1819;
import net.minecraft.class_1829;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_243;
import net.minecraft.class_2596;
import net.minecraft.class_2604;
import net.minecraft.class_2626;
import net.minecraft.class_2815;
import net.minecraft.class_2838;
import net.minecraft.class_2846;
import net.minecraft.class_2848;
import net.minecraft.class_2868;
import net.minecraft.class_476;
import net.minecraft.class_2846.class_2847;
import net.minecraft.class_2848.class_2849;
import org.jetbrains.annotations.NotNull;
import tsunami.core.Managers;
import tsunami.core.manager.client.ModuleManager;
import tsunami.events.impl.EventSync;
import tsunami.events.impl.PacketEvent;
import tsunami.features.modules.Module;
import tsunami.features.modules.movement.Blink;
import tsunami.injection.accesors.IMinecraftClient;
import tsunami.setting.Setting;
import tsunami.setting.impl.Bind;
import tsunami.setting.impl.BooleanSettingGroup;
import tsunami.setting.impl.SettingGroup;
import tsunami.utility.Timer;
import tsunami.utility.math.PredictUtility;
import tsunami.utility.player.InventoryUtility;
import tsunami.utility.player.SearchInvResult;
import tsunami.utility.world.ExplosionUtility;

public final class AutoTotem extends Module {
   private final Setting<AutoTotem.Mode> mode;
   private final Setting<AutoTotem.OffHand> offhand;
   private final Setting<BooleanSettingGroup> bindSwap;
   private final Setting<Bind> swapButton;
   private final Setting<AutoTotem.Swap> swapMode;
   private final Setting<Boolean> ncpStrict;
   private final Setting<Float> healthF;
   private final Setting<Float> healthS;
   private final Setting<Boolean> calcAbsorption;
   private final Setting<Boolean> stopMotion;
   private final Setting<Boolean> resetAttackCooldown;
   private final Setting<SettingGroup> safety;
   private final Setting<Boolean> hotbarFallBack;
   private final Setting<Boolean> fallBackCalc;
   private final Setting<Boolean> onElytra;
   private final Setting<Boolean> onFall;
   private final Setting<Boolean> onCrystal;
   private final Setting<Boolean> onObsidianPlace;
   private final Setting<Boolean> onCrystalInHand;
   private final Setting<Boolean> onMinecartTnt;
   private final Setting<Boolean> onCreeper;
   private final Setting<Boolean> onAnchor;
   private final Setting<Boolean> onTnt;
   public final Setting<AutoTotem.RCGap> rcGap;
   private final Setting<Boolean> crappleSpoof;
   private int delay;
   private Timer bindDelay;
   private class_1792 prevItem;

   public AutoTotem() {
      super("AutoTotem", Module.Category.COMBAT);
      this.mode = new Setting("Mode", AutoTotem.Mode.Matrix);
      this.offhand = new Setting("Item", AutoTotem.OffHand.Totem);
      this.bindSwap = new Setting("BindSwap", new BooleanSettingGroup(false), (v) -> {
         return this.offhand.is(AutoTotem.OffHand.Totem);
      });
      this.swapButton = (new Setting("SwapButton", new Bind(280, false, false))).addToGroup(this.bindSwap);
      this.swapMode = (new Setting("Swap", AutoTotem.Swap.GappleShield)).addToGroup(this.bindSwap);
      this.ncpStrict = new Setting("NCPStrict", false);
      this.healthF = new Setting("HP", 16.0F, 0.0F, 36.0F);
      this.healthS = new Setting("ShieldGappleHp", 16.0F, 0.0F, 20.0F, (v) -> {
         return this.offhand.getValue() == AutoTotem.OffHand.Shield;
      });
      this.calcAbsorption = new Setting("CalcAbsorption", true);
      this.stopMotion = new Setting("StopMotion", false);
      this.resetAttackCooldown = new Setting("ResetAttackCooldown", false);
      this.safety = new Setting("Safety", new SettingGroup(false, 0));
      this.hotbarFallBack = (new Setting("HotbarFallback", false)).addToGroup(this.safety);
      this.fallBackCalc = (new Setting("FallBackCalc", true, (v) -> {
         return (Boolean)this.hotbarFallBack.getValue();
      })).addToGroup(this.safety);
      this.onElytra = (new Setting("OnElytra", true)).addToGroup(this.safety);
      this.onFall = (new Setting("OnFall", true)).addToGroup(this.safety);
      this.onCrystal = (new Setting("OnCrystal", true)).addToGroup(this.safety);
      this.onObsidianPlace = (new Setting("OnObsidianPlace", false)).addToGroup(this.safety);
      this.onCrystalInHand = (new Setting("OnCrystalInHand", false)).addToGroup(this.safety);
      this.onMinecartTnt = (new Setting("OnMinecartTNT", true)).addToGroup(this.safety);
      this.onCreeper = (new Setting("OnCreeper", true)).addToGroup(this.safety);
      this.onAnchor = (new Setting("OnAnchor", true)).addToGroup(this.safety);
      this.onTnt = (new Setting("OnTNT", true)).addToGroup(this.safety);
      this.rcGap = new Setting("RightClickGapple", AutoTotem.RCGap.Off);
      this.crappleSpoof = new Setting("CrappleSpoof", true, (v) -> {
         return this.offhand.getValue() == AutoTotem.OffHand.GApple;
      });
      this.bindDelay = new Timer();
   }

   @EventHandler
   public void onSync(EventSync e) {
      this.swapTo(this.getItemSlot());
      if (this.rcGap.not(AutoTotem.RCGap.Off) && mc.field_1724.method_6047().method_7909() instanceof class_1829 && mc.field_1690.field_1904.method_1434() && !mc.field_1724.method_6115()) {
         ((IMinecraftClient)mc).idoItemUse();
      }

      --this.delay;
   }

   @EventHandler
   public void onPacketReceive(@NotNull PacketEvent.Receive e) {
      class_2596 var3 = e.getPacket();
      if (var3 instanceof class_2604) {
         class_2604 spawn = (class_2604)var3;
         if (spawn.method_11169() == class_1299.field_6110 && this.getPlayerPos().method_1028(spawn.method_11175(), spawn.method_11174(), spawn.method_11176()) < 36.0D) {
            if ((Boolean)this.hotbarFallBack.getValue()) {
               if ((Boolean)this.fallBackCalc.getValue() && ExplosionUtility.getExplosionDamageWPredict(new class_243(spawn.method_11175(), spawn.method_11174(), spawn.method_11176()), mc.field_1724, PredictUtility.createBox(this.getPlayerPos(), mc.field_1724), false) < this.getTriggerHealth() + 4.0F) {
                  return;
               }

               this.runInstant();
            }

            if ((Boolean)this.onCrystal.getValue() && (double)(this.getTriggerHealth() - ExplosionUtility.getExplosionDamageWPredict(new class_243(spawn.method_11175(), spawn.method_11174(), spawn.method_11176()), mc.field_1724, PredictUtility.createBox(this.getPlayerPos(), mc.field_1724), false)) < 0.5D) {
               int slot = -1;

               for(int i = 9; i < 45; ++i) {
                  if (mc.field_1724.method_31548().method_5438(i >= 36 ? i - 36 : i).method_7909().equals(class_1802.field_8288)) {
                     slot = i >= 36 ? i - 36 : i;
                     break;
                  }
               }

               this.swapTo(slot);
               this.debug("spawn switch");
            }
         }
      }

      var3 = e.getPacket();
      if (var3 instanceof class_2626) {
         class_2626 blockUpdate = (class_2626)var3;
         if (blockUpdate.method_11308().method_26204() == class_2246.field_10540 && (Boolean)this.onObsidianPlace.getValue() && this.getPlayerPos().method_1025(blockUpdate.method_11309().method_46558()) < 36.0D && this.delay <= 0) {
            this.runInstant();
         }
      }

   }

   private float getTriggerHealth() {
      return mc.field_1724.method_6032() + ((Boolean)this.calcAbsorption.getValue() ? mc.field_1724.method_6067() : 0.0F);
   }

   private void runInstant() {
      SearchInvResult hotbarResult = InventoryUtility.findItemInHotBar(class_1802.field_8288);
      SearchInvResult invResult = InventoryUtility.findItemInInventory(class_1802.field_8288);
      if (hotbarResult.found()) {
         hotbarResult.switchTo();
         this.delay = 20;
      } else if (invResult.found()) {
         int slot = invResult.slot() >= 36 ? invResult.slot() - 36 : invResult.slot();
         if (!(Boolean)this.hotbarFallBack.getValue()) {
            this.swapTo(slot);
         } else {
            mc.field_1761.method_2916(slot);
         }

         this.delay = 20;
      }

   }

   public void swapTo(int slot) {
      if (slot != -1 && this.delay <= 0) {
         if (mc.field_1755 instanceof class_476) {
            return;
         }

         if ((Boolean)this.stopMotion.getValue()) {
            mc.field_1724.method_18800(0.0D, mc.field_1724.method_18798().method_10214(), 0.0D);
         }

         int nearestSlot = findNearestCurrentItem();
         int prevCurrentItem = mc.field_1724.method_31548().field_7545;
         if (slot >= 9) {
            switch(((AutoTotem.Mode)this.mode.getValue()).ordinal()) {
            case 0:
               if ((Boolean)this.ncpStrict.getValue()) {
                  this.sendPacket(new class_2848(mc.field_1724, class_2849.field_12985));
               }

               clickSlot(slot);
               clickSlot(45);
               clickSlot(slot);
               this.sendPacket(new class_2815(mc.field_1724.field_7512.field_7763));
               break;
            case 1:
               if ((Boolean)this.ncpStrict.getValue()) {
                  this.sendPacket(new class_2848(mc.field_1724, class_2849.field_12985));
               }

               clickSlot(slot, nearestSlot, class_1713.field_7791);
               clickSlot(45, nearestSlot, class_1713.field_7791);
               clickSlot(slot, nearestSlot, class_1713.field_7791);
               this.sendPacket(new class_2815(mc.field_1724.field_7512.field_7763));
               break;
            case 2:
               if ((Boolean)this.ncpStrict.getValue()) {
                  this.sendPacket(new class_2848(mc.field_1724, class_2849.field_12985));
               }

               mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, slot, nearestSlot, class_1713.field_7791, mc.field_1724);
               this.debug(slot + " " + nearestSlot);
               this.sendPacket(new class_2868(nearestSlot));
               mc.field_1724.method_31548().field_7545 = nearestSlot;
               class_1799 itemstack = mc.field_1724.method_6079();
               mc.field_1724.method_6122(class_1268.field_5810, mc.field_1724.method_6047());
               mc.field_1724.method_6122(class_1268.field_5808, itemstack);
               this.sendPacket(new class_2846(class_2847.field_12969, class_2338.field_10980, class_2350.field_11033));
               this.sendPacket(new class_2868(prevCurrentItem));
               mc.field_1724.method_31548().field_7545 = prevCurrentItem;
               mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, slot, nearestSlot, class_1713.field_7791, mc.field_1724);
               this.sendPacket(new class_2815(mc.field_1724.field_7512.field_7763));
               if ((Boolean)this.resetAttackCooldown.getValue()) {
                  mc.field_1724.method_7350();
               }
               break;
            case 3:
               this.debug(slot + " pick");
               this.sendPacket(new class_2838(slot));
               this.sendPacket(new class_2846(class_2847.field_12969, class_2338.field_10980, class_2350.field_11033));
               int prevSlot = mc.field_1724.method_31548().field_7545;
               Managers.ASYNC.run(() -> {
                  mc.field_1724.method_31548().field_7545 = prevSlot;
               }, 300L);
               break;
            case 4:
               this.debug(slot + " swap");
               mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, slot, 40, class_1713.field_7791, mc.field_1724);
               this.sendPacket(new class_2815(mc.field_1724.field_7512.field_7763));
            }
         } else {
            this.sendPacket(new class_2868(slot));
            mc.field_1724.method_31548().field_7545 = slot;
            this.debug(slot + " select");
            this.sendPacket(new class_2846(class_2847.field_12969, class_2338.field_10980, class_2350.field_11033));
            this.sendPacket(new class_2868(prevCurrentItem));
            mc.field_1724.method_31548().field_7545 = prevCurrentItem;
            if ((Boolean)this.resetAttackCooldown.getValue()) {
               mc.field_1724.method_7350();
            }
         }

         this.delay = (int)(2.0F + (float)Managers.SERVER.getPing() / 25.0F);
      }

   }

   public static int findNearestCurrentItem() {
      int i = mc.field_1724.method_31548().field_7545;
      if (i == 8) {
         return 7;
      } else {
         return i == 0 ? 1 : i - 1;
      }
   }

   public int getItemSlot() {
      if (mc.field_1724 != null && mc.field_1687 != null) {
         SearchInvResult gapple = InventoryUtility.findItemInInventory(class_1802.field_8367);
         SearchInvResult crapple = InventoryUtility.findItemInInventory(class_1802.field_8463);
         SearchInvResult shield = InventoryUtility.findItemInInventory(class_1802.field_8255);
         class_1792 offHandItem = mc.field_1724.method_6079().method_7909();
         int itemSlot = -1;
         class_1792 item = null;
         switch(((AutoTotem.OffHand)this.offhand.getValue()).ordinal()) {
         case 0:
            if (offHandItem != class_1802.field_8288 && !mc.field_1724.method_6079().method_7960()) {
               this.prevItem = offHandItem;
            }

            item = this.prevItem;
            if (((BooleanSettingGroup)this.bindSwap.getValue()).isEnabled() && this.isKeyPressed(this.swapButton) && this.bindDelay.every(250L)) {
               switch(((AutoTotem.Swap)this.swapMode.getValue()).ordinal()) {
               case 0:
                  if (!mc.field_1724.method_6079().method_7960() && offHandItem != class_1802.field_8255) {
                     item = class_1802.field_8255;
                     break;
                  }

                  item = class_1802.field_8463;
                  break;
               case 1:
                  if (!mc.field_1724.method_6079().method_7960() && offHandItem != class_1802.field_8255) {
                     item = class_1802.field_8255;
                     break;
                  }

                  item = class_1802.field_8575;
                  break;
               case 2:
                  if (!mc.field_1724.method_6079().method_7960() && offHandItem != class_1802.field_8463) {
                     item = class_1802.field_8463;
                     break;
                  }

                  item = class_1802.field_8575;
                  break;
               case 3:
                  if (!mc.field_1724.method_6079().method_7960() && offHandItem != class_1802.field_8288) {
                     item = class_1802.field_8288;
                  } else {
                     item = class_1802.field_8575;
                  }
               }

               this.prevItem = item;
            }
            break;
         case 1:
            item = class_1802.field_8301;
            break;
         case 2:
            if ((Boolean)this.crappleSpoof.getValue()) {
               if (mc.field_1724.method_6059(class_1294.field_5898) && mc.field_1724.method_6112(class_1294.field_5898).method_5578() > 2) {
                  if (!crapple.found() && offHandItem != class_1802.field_8463) {
                     if (gapple.found() || offHandItem == class_1802.field_8367) {
                        item = class_1802.field_8367;
                     }
                  } else {
                     item = class_1802.field_8463;
                  }
               } else if (!gapple.found() && offHandItem != class_1802.field_8367) {
                  if (crapple.found() || offHandItem == class_1802.field_8463) {
                     item = class_1802.field_8463;
                  }
               } else {
                  item = class_1802.field_8367;
               }
            } else if (!crapple.found() && offHandItem != class_1802.field_8463) {
               if (gapple.found() || offHandItem == class_1802.field_8367) {
                  item = class_1802.field_8367;
               }
            } else {
               item = class_1802.field_8463;
            }
            break;
         case 3:
            if (!shield.found() && offHandItem != class_1802.field_8255) {
               if (crapple.found() || offHandItem == class_1802.field_8463) {
                  item = class_1802.field_8463;
               }
            } else if (this.getTriggerHealth() <= (Float)this.healthS.getValue()) {
               if (!crapple.found() && offHandItem != class_1802.field_8463) {
                  if (gapple.found() || offHandItem == class_1802.field_8367) {
                     item = class_1802.field_8367;
                  }
               } else {
                  item = class_1802.field_8463;
               }
            } else if (!mc.field_1724.method_7357().method_7904(class_1802.field_8255)) {
               item = class_1802.field_8255;
            } else if (!crapple.found() && offHandItem != class_1802.field_8463) {
               if (gapple.found() || offHandItem == class_1802.field_8367) {
                  item = class_1802.field_8367;
               }
            } else {
               item = class_1802.field_8463;
            }
         }

         if (this.getTriggerHealth() <= (Float)this.healthF.getValue() && (InventoryUtility.findItemInInventory(class_1802.field_8288).found() || offHandItem == class_1802.field_8288)) {
            item = class_1802.field_8288;
         }

         if (!this.rcGap.is(AutoTotem.RCGap.Off) && mc.field_1724.method_6047().method_7909() instanceof class_1829 && mc.field_1690.field_1904.method_1434() && !(offHandItem instanceof class_1819) && (this.rcGap.is(AutoTotem.RCGap.Always) || this.rcGap.is(AutoTotem.RCGap.OnlySafe) && this.getTriggerHealth() > (Float)this.healthF.getValue())) {
            if (crapple.found() || offHandItem == class_1802.field_8463) {
               item = class_1802.field_8463;
            }

            if (gapple.found() || offHandItem == class_1802.field_8367) {
               item = class_1802.field_8367;
            }
         }

         if ((Boolean)this.onFall.getValue() && (double)(this.getTriggerHealth() - ((mc.field_1724.field_6017 - 3.0F) / 2.0F + 3.5F)) < 0.5D) {
            item = class_1802.field_8288;
         }

         if ((Boolean)this.onElytra.getValue() && mc.field_1724.method_6128()) {
            item = class_1802.field_8288;
         }

         Iterator var7;
         if ((Boolean)this.onCrystalInHand.getValue()) {
            var7 = Managers.ASYNC.getAsyncPlayers().iterator();

            label254:
            while(true) {
               class_1657 pl;
               do {
                  do {
                     do {
                        do {
                           if (!var7.hasNext()) {
                              break label254;
                           }

                           pl = (class_1657)var7.next();
                        } while(Managers.FRIEND.isFriend(pl));
                     } while(pl == mc.field_1724);
                  } while(!(this.getPlayerPos().method_1025(pl.method_19538()) < 36.0D));
               } while(pl.method_6047().method_7909() != class_1802.field_8281 && pl.method_6047().method_7909() != class_1802.field_8301 && pl.method_6079().method_7909() != class_1802.field_8281 && pl.method_6079().method_7909() != class_1802.field_8301);

               item = class_1802.field_8288;
            }
         }

         var7 = mc.field_1687.method_18112().iterator();

         while(var7.hasNext()) {
            class_1297 entity = (class_1297)var7.next();
            if (entity != null && entity.method_5805() && !(this.getPlayerPos().method_1025(entity.method_19538()) > 36.0D)) {
               if ((Boolean)this.onCrystal.getValue() && entity instanceof class_1511 && (double)(this.getTriggerHealth() - ExplosionUtility.getExplosionDamageWPredict(entity.method_19538(), mc.field_1724, PredictUtility.createBox(this.getPlayerPos(), mc.field_1724), false)) < 0.5D) {
                  item = class_1802.field_8288;
                  break;
               }

               if ((Boolean)this.onTnt.getValue() && entity instanceof class_1541) {
                  item = class_1802.field_8288;
                  break;
               }

               if ((Boolean)this.onMinecartTnt.getValue() && entity instanceof class_1701) {
                  item = class_1802.field_8288;
                  break;
               }

               if ((Boolean)this.onCreeper.getValue() && entity instanceof class_1548) {
                  item = class_1802.field_8288;
                  break;
               }
            }
         }

         int i;
         if ((Boolean)this.onAnchor.getValue()) {
            for(i = -6; i <= 6; ++i) {
               for(int y = -6; y <= 6; ++y) {
                  for(int z = -6; z <= 6; ++z) {
                     class_2338 bp = new class_2338(i, y, z);
                     if (mc.field_1687.method_8320(bp).method_26204() == class_2246.field_23152) {
                        item = class_1802.field_8288;
                        break;
                     }
                  }
               }
            }
         }

         i = 9;

         while(true) {
            if (i < 45) {
               if (mc.field_1724.method_6079().method_7909() == item) {
                  return -1;
               }

               if (!mc.field_1724.method_31548().method_5438(i >= 36 ? i - 36 : i).method_7909().equals(item)) {
                  ++i;
                  continue;
               }

               itemSlot = i >= 36 ? i - 36 : i;
            }

            if (item == mc.field_1724.method_6047().method_7909() && mc.field_1690.field_1904.method_1434()) {
               return -1;
            }

            return itemSlot;
         }
      } else {
         return -1;
      }
   }

   private class_243 getPlayerPos() {
      return ModuleManager.blink.isEnabled() ? Blink.lastPos : mc.field_1724.method_19538();
   }

   private static enum Mode {
      Default,
      Alternative,
      Matrix,
      MatrixPick,
      NewVersion;

      // $FF: synthetic method
      private static AutoTotem.Mode[] $values() {
         return new AutoTotem.Mode[]{Default, Alternative, Matrix, MatrixPick, NewVersion};
      }
   }

   private static enum OffHand {
      Totem,
      Crystal,
      GApple,
      Shield;

      // $FF: synthetic method
      private static AutoTotem.OffHand[] $values() {
         return new AutoTotem.OffHand[]{Totem, Crystal, GApple, Shield};
      }
   }

   private static enum Swap {
      GappleShield,
      BallShield,
      GappleBall,
      BallTotem;

      // $FF: synthetic method
      private static AutoTotem.Swap[] $values() {
         return new AutoTotem.Swap[]{GappleShield, BallShield, GappleBall, BallTotem};
      }
   }

   public static enum RCGap {
      Off,
      Always,
      OnlySafe;

      // $FF: synthetic method
      private static AutoTotem.RCGap[] $values() {
         return new AutoTotem.RCGap[]{Off, Always, OnlySafe};
      }
   }
}
