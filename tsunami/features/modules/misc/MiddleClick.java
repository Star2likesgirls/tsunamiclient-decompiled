package tsunami.features.modules.misc;

import java.util.Iterator;
import java.util.function.Consumer;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_1268;
import net.minecraft.class_1293;
import net.minecraft.class_1294;
import net.minecraft.class_1657;
import net.minecraft.class_1713;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1844;
import net.minecraft.class_239;
import net.minecraft.class_2868;
import net.minecraft.class_2879;
import net.minecraft.class_2886;
import net.minecraft.class_3965;
import net.minecraft.class_3966;
import net.minecraft.class_746;
import net.minecraft.class_9334;
import net.minecraft.class_2828.class_2831;
import tsunami.core.Managers;
import tsunami.core.manager.client.AsyncManager;
import tsunami.core.manager.client.ModuleManager;
import tsunami.events.impl.EventSync;
import tsunami.features.cmd.Command;
import tsunami.features.modules.Module;
import tsunami.features.modules.client.ClientSettings;
import tsunami.features.modules.combat.Aura;
import tsunami.setting.Setting;
import tsunami.setting.impl.BooleanSettingGroup;
import tsunami.utility.Timer;
import tsunami.utility.player.InteractionUtility;
import tsunami.utility.player.InventoryUtility;
import tsunami.utility.player.PlayerUtility;

public class MiddleClick extends Module {
   private final Setting<MiddleClick.Action> onBlock;
   private final Setting<MiddleClick.Action> onAir;
   private final Setting<MiddleClick.Action> onEntity;
   private final Setting<MiddleClick.Action> onFlying;
   private final Setting<Boolean> silent;
   private final Setting<Boolean> inventory;
   private final Setting<Integer> swapDelay;
   private final Setting<BooleanSettingGroup> antiWaste;
   private final Setting<Integer> durability;
   public final Setting<Boolean> antiPickUp;
   private final Setting<Boolean> feetExp;
   private static final Timer timer = new Timer();
   private String state;

   public MiddleClick() {
      super("MiddleClick", Module.Category.MISC);
      this.onBlock = new Setting("OnBlock", MiddleClick.Action.Xp);
      this.onAir = new Setting("OnAir", MiddleClick.Action.Pearl);
      this.onEntity = new Setting("OnEntity", MiddleClick.Action.Friend);
      this.onFlying = new Setting("OnElytra", MiddleClick.Action.Firework);
      this.silent = new Setting("Silent", true);
      this.inventory = new Setting("Inventory", true);
      this.swapDelay = new Setting("SwapDelay", 100, 0, 1000, (v) -> {
         return !(Boolean)this.silent.getValue();
      });
      this.antiWaste = new Setting("AntiWaste", new BooleanSettingGroup(true));
      this.durability = (new Setting("StopOn", 90, 0, 100)).addToGroup(this.antiWaste);
      this.antiPickUp = new Setting("AntiPickUp", true);
      this.feetExp = new Setting("FeetXP", false);
      this.state = "none";
   }

   public String getDisplayInfo() {
      return this.state;
   }

   @EventHandler
   private void onSync(EventSync event) {
      if (mc.field_1755 == null) {
         class_239 target = mc.field_1765;
         if (mc.field_1724.method_6128()) {
            if (mc.field_1690.field_1871.method_1434()) {
               ((MiddleClick.Action)this.onFlying.getValue()).doAction(event);
            }

            this.state = ((MiddleClick.Action)this.onFlying.getValue()).toString();
            return;
         }

         if (target instanceof class_3966) {
            class_3966 ehr = (class_3966)target;
            if (ehr.method_17782() instanceof class_1657) {
               if (mc.field_1690.field_1871.method_1434()) {
                  ((MiddleClick.Action)this.onEntity.getValue()).doAction(event);
               }

               this.state = ((MiddleClick.Action)this.onEntity.getValue()).toString();
               return;
            }
         }

         if (target instanceof class_3965) {
            class_3965 bhr = (class_3965)target;
            if (mc.field_1687.method_22347(bhr.method_17777())) {
               if (mc.field_1690.field_1871.method_1434()) {
                  ((MiddleClick.Action)this.onAir.getValue()).doAction(event);
               }

               this.state = ((MiddleClick.Action)this.onAir.getValue()).toString();
            } else {
               if (mc.field_1690.field_1871.method_1434()) {
                  ((MiddleClick.Action)this.onBlock.getValue()).doAction(event);
               }

               this.state = ((MiddleClick.Action)this.onBlock.getValue()).toString();
            }

            return;
         }

         if (mc.field_1690.field_1871.method_1434()) {
            ((MiddleClick.Action)this.onAir.getValue()).doAction(event);
         }

         this.state = ((MiddleClick.Action)this.onAir.getValue()).toString();
      }

   }

   private static boolean needXp() {
      Iterator var0 = mc.field_1724.method_5661().iterator();

      class_1799 stack;
      do {
         if (!var0.hasNext()) {
            return false;
         }

         stack = (class_1799)var0.next();
      } while(!(PlayerUtility.calculatePercentage(stack) < (float)(Integer)ModuleManager.middleClick.durability.getValue()));

      return true;
   }

   private static int getHpSlot() {
      for(int i = 0; i < 9; ++i) {
         if (isStackPotion(mc.field_1724.method_31548().method_5438(i))) {
            return i;
         }
      }

      return -1;
   }

   private static int findHpInInventory() {
      for(int i = 36; i >= 0; --i) {
         if (isStackPotion(mc.field_1724.method_31548().method_5438(i))) {
            return i < 9 ? i + 36 : i;
         }
      }

      return -1;
   }

   private static boolean isStackPotion(class_1799 stack) {
      if (stack == null) {
         return false;
      } else {
         if (stack.method_7909() == class_1802.field_8436) {
            class_1844 potionContentsComponent = (class_1844)stack.method_57825(class_9334.field_49651, class_1844.field_49274);
            Iterator var2 = potionContentsComponent.method_57397().iterator();

            while(var2.hasNext()) {
               class_1293 effect = (class_1293)var2.next();
               if (effect.method_5579().comp_349() == class_1294.field_5915.comp_349()) {
                  return true;
               }
            }
         }

         return false;
      }
   }

   public static enum Action {
      HealingPot((e) -> {
         if (MiddleClick.timer.every(250L)) {
            int hpSlot;
            if (MiddleClick.getHpSlot() != -1) {
               hpSlot = MiddleClick.getHpSlot();
               int originalSlot = Module.mc.field_1724.method_31548().field_7545;
               if (hpSlot != -1) {
                  InventoryUtility.switchTo(hpSlot);
                  InteractionUtility.sendSequencedPacket((id) -> {
                     return new class_2886(class_1268.field_5808, id, Module.mc.field_1724.method_36454(), Module.mc.field_1724.method_36455());
                  });
                  InventoryUtility.switchTo(originalSlot);
               }
            } else {
               hpSlot = MiddleClick.findHpInInventory();
               if (hpSlot != -1) {
                  Module.mc.field_1761.method_2906(Module.mc.field_1724.field_7512.field_7763, hpSlot, Module.mc.field_1724.method_31548().field_7545, class_1713.field_7791, Module.mc.field_1724);
                  InteractionUtility.sendSequencedPacket((id) -> {
                     return new class_2886(class_1268.field_5808, id, Module.mc.field_1724.method_36454(), Module.mc.field_1724.method_36455());
                  });
                  Module.mc.field_1724.field_3944.method_52787(new class_2879(class_1268.field_5808));
                  Module.mc.field_1761.method_2906(Module.mc.field_1724.field_7512.field_7763, hpSlot, Module.mc.field_1724.method_31548().field_7545, class_1713.field_7791, Module.mc.field_1724);
               }
            }
         }

      }),
      Firework((e) -> {
         if (MiddleClick.timer.every(250L)) {
            int epSlot1 = InventoryUtility.findItemInHotBar(class_1802.field_8639).slot();
            int epSlot;
            if ((Boolean)ModuleManager.middleClick.silent.getValue()) {
               if (!(Boolean)ModuleManager.middleClick.inventory.getValue() || (Boolean)ModuleManager.middleClick.inventory.getValue() && epSlot1 != -1) {
                  epSlot = Module.mc.field_1724.method_31548().field_7545;
                  if (epSlot1 != -1) {
                     Module.mc.field_1724.method_31548().field_7545 = epSlot1;
                     Module.mc.field_1724.field_3944.method_52787(new class_2868(epSlot1));
                     InteractionUtility.sendSequencedPacket((id) -> {
                        return new class_2886(class_1268.field_5808, id, Module.mc.field_1724.method_36454(), Module.mc.field_1724.method_36455());
                     });
                     Module.mc.field_1724.field_3944.method_52787(new class_2879(class_1268.field_5808));
                     Module.mc.field_1724.method_31548().field_7545 = epSlot;
                     Module.mc.field_1724.field_3944.method_52787(new class_2868(epSlot));
                  }
               } else {
                  epSlot = InventoryUtility.findItemInInventory(class_1802.field_8639).slot();
                  if (epSlot != -1) {
                     Module.mc.field_1761.method_2906(Module.mc.field_1724.field_7512.field_7763, epSlot, Module.mc.field_1724.method_31548().field_7545, class_1713.field_7791, Module.mc.field_1724);
                     InteractionUtility.sendSequencedPacket((id) -> {
                        return new class_2886(class_1268.field_5808, id, Module.mc.field_1724.method_36454(), Module.mc.field_1724.method_36455());
                     });
                     Module.mc.field_1724.field_3944.method_52787(new class_2879(class_1268.field_5808));
                     Module.mc.field_1761.method_2906(Module.mc.field_1724.field_7512.field_7763, epSlot, Module.mc.field_1724.method_31548().field_7545, class_1713.field_7791, Module.mc.field_1724);
                  }
               }
            } else if (!(Boolean)ModuleManager.middleClick.inventory.getValue() || (Boolean)ModuleManager.middleClick.inventory.getValue() && epSlot1 != -1) {
               if (epSlot1 != -1) {
                  (new MiddleClick.PearlThread(Module.mc.field_1724, epSlot1, Module.mc.field_1724.method_31548().field_7545, (Integer)ModuleManager.middleClick.swapDelay.getValue(), false)).start();
               }
            } else {
               epSlot = InventoryUtility.findItemInInventory(class_1802.field_8639).slot();
               if (epSlot != -1) {
                  (new MiddleClick.PearlThread(Module.mc.field_1724, epSlot, Module.mc.field_1724.method_31548().field_7545, (Integer)ModuleManager.middleClick.swapDelay.getValue(), true)).start();
               }
            }
         }

      }),
      Friend((e) -> {
         if (MiddleClick.timer.every(800L)) {
            class_239 patt0$temp = Module.mc.field_1765;
            if (patt0$temp instanceof class_3966) {
               class_3966 ehr = (class_3966)patt0$temp;
               if (ehr.method_17782() instanceof class_1657) {
                  if (Managers.FRIEND.isFriend(ehr.method_17782().method_5477().getString())) {
                     Managers.FRIEND.removeFriend(ehr.method_17782().method_5477().getString());
                     Command.sendMessage(ClientSettings.isRu() ? "§b" + ehr.method_17782().method_5477().getString() + "§r удален из друзей!" : "§b" + ehr.method_17782().method_5477().getString() + "§r removed from friends!");
                  } else {
                     Managers.FRIEND.addFriend(ehr.method_17782().method_5477().getString());
                     Command.sendMessage(ClientSettings.isRu() ? "Добавлен друг §b" + ehr.method_17782().method_5477().getString() : "Added friend §b" + ehr.method_17782().method_5477().getString());
                  }
               }
            }
         }

      }),
      Pearl((e) -> {
         if (MiddleClick.timer.every(500L)) {
            if (ModuleManager.aura.isEnabled() && Aura.target != null) {
               Module.mc.field_1724.field_3944.method_52787(new class_2831(Module.mc.field_1724.method_36454(), Module.mc.field_1724.method_36455(), Module.mc.field_1724.method_24828()));
            }

            int epSlot1 = InventoryUtility.findItemInHotBar(class_1802.field_8634).slot();
            int epSlot;
            if ((Boolean)ModuleManager.middleClick.silent.getValue()) {
               if (!(Boolean)ModuleManager.middleClick.inventory.getValue() || (Boolean)ModuleManager.middleClick.inventory.getValue() && epSlot1 != -1) {
                  epSlot = Module.mc.field_1724.method_31548().field_7545;
                  if (epSlot1 != -1) {
                     Module.mc.field_1724.method_31548().field_7545 = epSlot1;
                     Module.mc.field_1724.field_3944.method_52787(new class_2868(epSlot1));
                     InteractionUtility.sendSequencedPacket((id) -> {
                        return new class_2886(class_1268.field_5808, id, Module.mc.field_1724.method_36454(), Module.mc.field_1724.method_36455());
                     });
                     Module.mc.field_1724.field_3944.method_52787(new class_2879(class_1268.field_5808));
                     Module.mc.field_1724.method_31548().field_7545 = epSlot;
                     Module.mc.field_1724.field_3944.method_52787(new class_2868(epSlot));
                  }
               } else {
                  epSlot = InventoryUtility.findItemInInventory(class_1802.field_8634).slot();
                  if (epSlot != -1) {
                     Module.mc.field_1761.method_2906(Module.mc.field_1724.field_7512.field_7763, epSlot, Module.mc.field_1724.method_31548().field_7545, class_1713.field_7791, Module.mc.field_1724);
                     InteractionUtility.sendSequencedPacket((id) -> {
                        return new class_2886(class_1268.field_5808, id, Module.mc.field_1724.method_36454(), Module.mc.field_1724.method_36455());
                     });
                     Module.mc.field_1724.field_3944.method_52787(new class_2879(class_1268.field_5808));
                     Module.mc.field_1761.method_2906(Module.mc.field_1724.field_7512.field_7763, epSlot, Module.mc.field_1724.method_31548().field_7545, class_1713.field_7791, Module.mc.field_1724);
                  }
               }
            } else if (!(Boolean)ModuleManager.middleClick.inventory.getValue() || (Boolean)ModuleManager.middleClick.inventory.getValue() && epSlot1 != -1) {
               if (epSlot1 != -1) {
                  (new MiddleClick.PearlThread(Module.mc.field_1724, epSlot1, Module.mc.field_1724.method_31548().field_7545, (Integer)ModuleManager.middleClick.swapDelay.getValue(), false)).start();
               }
            } else {
               epSlot = InventoryUtility.findItemInInventory(class_1802.field_8634).slot();
               if (epSlot != -1) {
                  (new MiddleClick.PearlThread(Module.mc.field_1724, epSlot, Module.mc.field_1724.method_31548().field_7545, (Integer)ModuleManager.middleClick.swapDelay.getValue(), true)).start();
               }
            }
         }

      }),
      Xp((e) -> {
         if ((Boolean)ModuleManager.middleClick.feetExp.getValue() && (!((BooleanSettingGroup)ModuleManager.middleClick.antiWaste.getValue()).isEnabled() || MiddleClick.needXp())) {
            Module.mc.field_1724.method_36457(90.0F);
         }

         e.addPostAction(() -> {
            if (!((BooleanSettingGroup)ModuleManager.middleClick.antiWaste.getValue()).isEnabled() || MiddleClick.needXp()) {
               if (Module.mc.field_1690.field_1871.method_1434()) {
                  int slot = InventoryUtility.findItemInHotBar(class_1802.field_8287).slot();
                  if (slot != -1) {
                     int lastSlot = Module.mc.field_1724.method_31548().field_7545;
                     InventoryUtility.switchTo(slot);
                     InteractionUtility.sendSequencedPacket((id) -> {
                        return new class_2886(class_1268.field_5808, id, Module.mc.field_1724.method_36454(), Module.mc.field_1724.method_36455());
                     });
                     if ((Boolean)ModuleManager.middleClick.silent.getValue()) {
                        InventoryUtility.switchTo(lastSlot);
                     }
                  }
               }

            }
         });
      }),
      None((e) -> {
      });

      private final Consumer<EventSync> r;

      private Action(Consumer<EventSync> r) {
         this.r = r;
      }

      public void doAction(EventSync e) {
         this.r.accept(e);
      }

      // $FF: synthetic method
      private static MiddleClick.Action[] $values() {
         return new MiddleClick.Action[]{HealingPot, Firework, Friend, Pearl, Xp, None};
      }
   }

   public static class PearlThread extends Thread {
      public class_746 player;
      int epSlot;
      int originalSlot;
      int delay;
      boolean inv;

      public PearlThread(class_746 entityPlayer, int epSlot, int originalSlot, int delay, boolean inventory) {
         this.player = entityPlayer;
         this.epSlot = epSlot;
         this.originalSlot = originalSlot;
         this.delay = delay;
         this.inv = inventory;
      }

      public void run() {
         if (!this.inv) {
            InventoryUtility.switchTo(this.epSlot);
            AsyncManager.sleep(this.delay);
            InteractionUtility.sendSequencedPacket((id) -> {
               return new class_2886(class_1268.field_5808, id, Module.mc.field_1724.method_36454(), Module.mc.field_1724.method_36455());
            });
            Module.mc.field_1724.field_3944.method_52787(new class_2879(class_1268.field_5808));
            AsyncManager.sleep(this.delay);
            InventoryUtility.switchTo(this.originalSlot);
         } else {
            Module.mc.field_1761.method_2906(Module.mc.field_1724.field_7512.field_7763, this.epSlot, this.originalSlot, class_1713.field_7791, Module.mc.field_1724);
            AsyncManager.sleep(this.delay);
            if (ModuleManager.aura.isEnabled() && Aura.target != null) {
               Module.mc.field_1724.field_3944.method_52787(new class_2831(Module.mc.field_1724.method_36454(), Module.mc.field_1724.method_36455(), Module.mc.field_1724.method_24828()));
            }

            InteractionUtility.sendSequencedPacket((id) -> {
               return new class_2886(class_1268.field_5808, id, Module.mc.field_1724.method_36454(), Module.mc.field_1724.method_36455());
            });
            Module.mc.field_1724.field_3944.method_52787(new class_2879(class_1268.field_5808));
            AsyncManager.sleep(this.delay);
            Module.mc.field_1761.method_2906(Module.mc.field_1724.field_7512.field_7763, this.epSlot, this.originalSlot, class_1713.field_7791, Module.mc.field_1724);
         }

         super.run();
      }
   }
}
