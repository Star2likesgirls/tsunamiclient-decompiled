package tsunami.features.modules.player;

import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_1268;
import net.minecraft.class_1713;
import net.minecraft.class_1792;
import net.minecraft.class_1802;
import net.minecraft.class_2596;
import net.minecraft.class_2815;
import net.minecraft.class_2848;
import net.minecraft.class_2879;
import net.minecraft.class_2886;
import net.minecraft.class_2848.class_2849;
import tsunami.core.Managers;
import tsunami.events.impl.PacketEvent;
import tsunami.features.modules.Module;
import tsunami.features.modules.client.ClientSettings;
import tsunami.setting.Setting;
import tsunami.setting.impl.Bind;
import tsunami.utility.Timer;
import tsunami.utility.player.InventoryUtility;
import tsunami.utility.player.SearchInvResult;

public class ElytraSwap extends Module {
   private final Setting<Boolean> delay = new Setting("Delay", false);
   private final Setting<ElytraSwap.Mode> mode;
   private final Setting<Bind> switchButton;
   private final Setting<Bind> fireWorkButton;
   private final Setting<Boolean> startFireWork;
   private final Setting<ElytraSwap.FireWorkMode> fireWorkMode;
   private final Timer switchTimer;
   private final Timer fireworkTimer;
   public static boolean swapping = false;

   public ElytraSwap() {
      super("ElytraSwap", Module.Category.PLAYER);
      this.mode = new Setting("Mode", ElytraSwap.Mode.Enable);
      this.switchButton = new Setting("SwitchButton", new Bind(-1, false, false), (v) -> {
         return this.mode.getValue() == ElytraSwap.Mode.Bind;
      });
      this.fireWorkButton = new Setting("FireWorkButton", new Bind(-1, false, false), (v) -> {
         return this.mode.getValue() == ElytraSwap.Mode.Bind;
      });
      this.startFireWork = new Setting("StartFireWork", true, (v) -> {
         return this.mode.getValue() == ElytraSwap.Mode.Bind;
      });
      this.fireWorkMode = new Setting("FireWorkMode", ElytraSwap.FireWorkMode.Normal, (v) -> {
         return this.mode.getValue() == ElytraSwap.Mode.Bind;
      });
      this.switchTimer = new Timer();
      this.fireworkTimer = new Timer();
   }

   public void onEnable() {
      if (this.mode.getValue() == ElytraSwap.Mode.Enable) {
         this.swapChest(true);
      }

   }

   public void onUpdate() {
      if (this.mode.getValue() == ElytraSwap.Mode.Bind && mc.field_1755 == null) {
         if (((Bind)this.switchButton.getValue()).getKey() != -1 && this.isKeyPressed(((Bind)this.switchButton.getValue()).getKey()) && this.switchTimer.every(500L)) {
            this.swapChest(false);
         }

         if (((Bind)this.fireWorkButton.getValue()).getKey() != -1 && this.isKeyPressed(((Bind)this.fireWorkButton.getValue()).getKey()) && this.fireworkTimer.every(500L) && mc.field_1724.method_6128()) {
            this.useFireWork();
         }
      }

   }

   @EventHandler
   public void onPacketSend(PacketEvent.SendPost e) {
      class_2596 var3 = e.getPacket();
      if (var3 instanceof class_2848) {
         class_2848 command = (class_2848)var3;
         if (command.method_12365() == class_2849.field_12982 && this.mode.getValue() == ElytraSwap.Mode.Bind && (Boolean)this.startFireWork.getValue()) {
            this.useFireWork();
         }
      }

   }

   public void useFireWork() {
      SearchInvResult hotbarFireWorkResult = InventoryUtility.findItemInHotBar(class_1802.field_8639);
      SearchInvResult fireWorkResult = InventoryUtility.findItemInInventory(class_1802.field_8639);
      InventoryUtility.saveSlot();
      if (hotbarFireWorkResult.found()) {
         hotbarFireWorkResult.switchTo();
      } else {
         if (!fireWorkResult.found()) {
            this.sendMessage(ClientSettings.isRu() ? "У тебя нет фейерверков!" : "You've got no fireworks!");
            return;
         }

         mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, fireWorkResult.slot(), mc.field_1724.method_31548().field_7545, class_1713.field_7791, mc.field_1724);
         this.sendPacket(new class_2815(mc.field_1724.field_7512.field_7763));
      }

      this.sendSequencedPacket((id) -> {
         return new class_2886(class_1268.field_5808, id, mc.field_1724.method_36454(), mc.field_1724.method_36455());
      });
      this.sendPacket(new class_2879(class_1268.field_5808));
      if (this.fireWorkMode.getValue() == ElytraSwap.FireWorkMode.Silent) {
         InventoryUtility.returnSlot();
         if (!hotbarFireWorkResult.found()) {
            mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, fireWorkResult.slot(), mc.field_1724.method_31548().field_7545, class_1713.field_7791, mc.field_1724);
            this.sendPacket(new class_2815(mc.field_1724.field_7512.field_7763));
         }
      }

   }

   public static int getChestPlateSlot() {
      class_1792[] items = new class_1792[]{class_1802.field_22028, class_1802.field_8058, class_1802.field_8873, class_1802.field_8523, class_1802.field_8678, class_1802.field_8577};
      class_1792[] var1 = items;
      int var2 = items.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         class_1792 item = var1[var3];
         SearchInvResult slot = InventoryUtility.findItemInInventory(item);
         if (slot.found()) {
            return slot.slot();
         }
      }

      return -1;
   }

   private void swapChest(boolean disable) {
      SearchInvResult result = InventoryUtility.findItemInInventory(class_1802.field_8833);
      if (mc.field_1724.method_31548().method_5438(38).method_7909() == class_1802.field_8833) {
         int slot = getChestPlateSlot();
         if (slot == -1) {
            if (disable) {
               this.disable(ClientSettings.isRu() ? "У тебя нет нагрудника!" : "You don't have a chestplate!");
            } else {
               this.sendMessage(ClientSettings.isRu() ? "У тебя нет нагрудника!" : "You don't have a chestplate!");
            }

            return;
         }

         if ((Boolean)this.delay.getValue()) {
            Managers.ASYNC.run(() -> {
               swapping = true;
               clickSlot(slot);

               try {
                  Thread.sleep(200L);
               } catch (Exception var4) {
               }

               clickSlot(6);

               try {
                  Thread.sleep(200L);
               } catch (Exception var3) {
               }

               clickSlot(slot);
               this.sendPacket(new class_2815(mc.field_1724.field_7512.field_7763));
               swapping = false;
            });
         } else {
            clickSlot(slot);
            clickSlot(6);
            clickSlot(slot);
            this.sendPacket(new class_2815(mc.field_1724.field_7512.field_7763));
         }
      } else {
         if (!result.found()) {
            if (disable) {
               this.disable(ClientSettings.isRu() ? "У тебя нет элитры!" : "You don't have an elytra!");
            } else {
               this.sendMessage(ClientSettings.isRu() ? "У тебя нет элитры!" : "You don't have an elytra!");
            }

            return;
         }

         if ((Boolean)this.delay.getValue()) {
            (new Thread(() -> {
               swapping = true;
               clickSlot(result.slot());

               try {
                  Thread.sleep(200L);
               } catch (Exception var4) {
               }

               clickSlot(6);

               try {
                  Thread.sleep(200L);
               } catch (Exception var3) {
               }

               clickSlot(result.slot());
               this.sendPacket(new class_2815(mc.field_1724.field_7512.field_7763));
               if ((Boolean)this.startFireWork.getValue() && mc.field_1724.field_6017 > 0.0F) {
                  this.sendPacket(new class_2848(mc.field_1724, class_2849.field_12982));
               }

               swapping = false;
            })).start();
         } else {
            clickSlot(result.slot());
            clickSlot(6);
            clickSlot(result.slot());
            this.sendPacket(new class_2815(mc.field_1724.field_7512.field_7763));
            if ((Boolean)this.startFireWork.getValue() && mc.field_1724.field_6017 > 0.0F) {
               this.sendPacket(new class_2848(mc.field_1724, class_2849.field_12982));
            }
         }
      }

      if (disable) {
         this.disable(ClientSettings.isRu() ? "Свапнул! Отключаю.." : "Swapped! Disabling..");
      }

   }

   private static enum Mode {
      Enable,
      Bind;

      // $FF: synthetic method
      private static ElytraSwap.Mode[] $values() {
         return new ElytraSwap.Mode[]{Enable, Bind};
      }
   }

   private static enum FireWorkMode {
      Silent,
      Normal;

      // $FF: synthetic method
      private static ElytraSwap.FireWorkMode[] $values() {
         return new ElytraSwap.FireWorkMode[]{Silent, Normal};
      }
   }
}
