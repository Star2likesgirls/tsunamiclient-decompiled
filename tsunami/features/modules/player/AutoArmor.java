package tsunami.features.modules.player;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import net.minecraft.class_1268;
import net.minecraft.class_1304;
import net.minecraft.class_1738;
import net.minecraft.class_1770;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1890;
import net.minecraft.class_1893;
import net.minecraft.class_2815;
import net.minecraft.class_2848;
import net.minecraft.class_2886;
import net.minecraft.class_408;
import net.minecraft.class_6880;
import net.minecraft.class_9304;
import net.minecraft.class_2848.class_2849;
import tsunami.core.manager.client.ModuleManager;
import tsunami.features.modules.Module;
import tsunami.gui.clickui.ClickGUI;
import tsunami.gui.hud.HudEditorGui;
import tsunami.setting.Setting;
import tsunami.utility.player.InventoryUtility;
import tsunami.utility.player.MovementUtility;

public class AutoArmor extends Module {
   private final Setting<AutoArmor.EnchantPriority> head;
   private final Setting<AutoArmor.EnchantPriority> body;
   private final Setting<AutoArmor.EnchantPriority> tights;
   private final Setting<AutoArmor.EnchantPriority> feet;
   private final Setting<AutoArmor.ElytraPriority> elytraPriority;
   private final Setting<Integer> delay;
   private final Setting<Boolean> oldVersion;
   private final Setting<Boolean> pauseInventory;
   private final Setting<Boolean> noMove;
   private final Setting<Boolean> ignoreCurse;
   private final Setting<Boolean> strict;
   private int tickDelay;
   List<AutoArmor.ArmorData> armorList;

   public AutoArmor() {
      super("AutoArmor", Module.Category.NONE);
      this.head = new Setting("Head", AutoArmor.EnchantPriority.Protection);
      this.body = new Setting("Body", AutoArmor.EnchantPriority.Protection);
      this.tights = new Setting("Tights", AutoArmor.EnchantPriority.Protection);
      this.feet = new Setting("Feet", AutoArmor.EnchantPriority.Protection);
      this.elytraPriority = new Setting("ElytraPriority", AutoArmor.ElytraPriority.Ignore);
      this.delay = new Setting("Delay", 5, 0, 10);
      this.oldVersion = new Setting("OldVersion", false);
      this.pauseInventory = new Setting("PauseInventory", false);
      this.noMove = new Setting("NoMove", false);
      this.ignoreCurse = new Setting("IgnoreCurse", true);
      this.strict = new Setting("Strict", false);
      this.tickDelay = 0;
      this.armorList = Arrays.asList(new AutoArmor.ArmorData(class_1304.field_6166, 36, -1, -1, -1), new AutoArmor.ArmorData(class_1304.field_6172, 37, -1, -1, -1), new AutoArmor.ArmorData(class_1304.field_6174, 38, -1, -1, -1), new AutoArmor.ArmorData(class_1304.field_6169, 39, -1, -1, -1));
   }

   public void onUpdate() {
      if (mc.field_1755 == null || !(Boolean)this.pauseInventory.getValue() || mc.field_1755 instanceof class_408 || mc.field_1755 instanceof ClickGUI || mc.field_1755 instanceof HudEditorGui) {
         if (this.tickDelay-- <= 0) {
            this.armorList.forEach(AutoArmor.ArmorData::reset);

            int slot;
            for(int i = 0; i < 36; ++i) {
               class_1799 stack = mc.field_1724.method_31548().method_5438(i);
               slot = this.getProtection(stack);
               if (slot > 0) {
                  Iterator var4 = this.armorList.iterator();

                  while(var4.hasNext()) {
                     AutoArmor.ArmorData e = (AutoArmor.ArmorData)var4.next();
                     class_1304 var10000 = e.getEquipmentSlot();
                     class_1792 var7 = stack.method_7909();
                     class_1304 var10001;
                     if (var7 instanceof class_1738) {
                        class_1738 ai = (class_1738)var7;
                        var10001 = ai.method_7685();
                     } else {
                        var10001 = class_1304.field_6174;
                     }

                     if (var10000 == var10001 && slot > e.getPrevProt() && slot > e.getNewProtection()) {
                        e.setNewSlot(i);
                        e.setNewProtection(slot);
                     }
                  }
               }
            }

            Iterator var8 = this.armorList.iterator();

            AutoArmor.ArmorData armorPiece;
            do {
               if (!var8.hasNext()) {
                  return;
               }

               armorPiece = (AutoArmor.ArmorData)var8.next();
               slot = armorPiece.getNewSlot();
            } while(slot == -1);

            if ((armorPiece.getPrevProt() == -1 || !(Boolean)this.oldVersion.getValue()) && slot < 9) {
               InventoryUtility.saveAndSwitchTo(slot);
               this.sendSequencedPacket((id) -> {
                  return new class_2886(class_1268.field_5808, id, mc.field_1724.method_36454(), mc.field_1724.method_36455());
               });
               InventoryUtility.returnSlot();
            } else {
               if (MovementUtility.isMoving() && (Boolean)this.noMove.getValue()) {
                  return;
               }

               int newArmorSlot = slot < 9 ? 36 + slot : slot;
               if ((Boolean)this.strict.getValue()) {
                  this.sendPacket(new class_2848(mc.field_1724, class_2849.field_12985));
               }

               clickSlot(newArmorSlot);
               clickSlot(armorPiece.getArmorSlot() - 34 + (39 - armorPiece.getArmorSlot()) * 2);
               if (armorPiece.getPrevProt() != -1) {
                  clickSlot(newArmorSlot);
               }

               this.sendPacket(new class_2815(mc.field_1724.field_7512.field_7763));
            }

            this.tickDelay = (Integer)this.delay.getValue();
         }
      }
   }

   private int getProtection(class_1799 is) {
      if (!(is.method_7909() instanceof class_1738) && !(is.method_7909() instanceof class_1770)) {
         return !is.method_7960() ? 0 : -1;
      } else {
         int prot = 0;
         class_1792 var5 = is.method_7909();
         class_1304 var10000;
         if (var5 instanceof class_1738) {
            class_1738 ai = (class_1738)var5;
            var10000 = ai.method_7685();
         } else {
            var10000 = class_1304.field_48824;
         }

         class_1304 slot = var10000;
         if (is.method_7909() instanceof class_1770) {
            if (!class_1770.method_7804(is)) {
               return 0;
            }

            boolean ePlus = this.elytraPriority.is(AutoArmor.ElytraPriority.ElytraPlus) && (ModuleManager.elytraRecast.isEnabled() || ModuleManager.elytraPlus.isEnabled());
            boolean ignore = this.elytraPriority.is(AutoArmor.ElytraPriority.Ignore) && mc.field_1724.method_31548().method_5438(38).method_7909() instanceof class_1770;
            if (ePlus || ignore || this.elytraPriority.is(AutoArmor.ElytraPriority.Always)) {
               prot = 999;
            }
         }

         int blastMultiplier = 1;
         int protectionMultiplier = 1;
         switch(slot) {
         case field_6169:
            if (this.head.is(AutoArmor.EnchantPriority.Protection)) {
               protectionMultiplier *= 2;
            } else {
               blastMultiplier *= 2;
            }
            break;
         case field_48824:
            if (this.body.is(AutoArmor.EnchantPriority.Protection)) {
               protectionMultiplier *= 2;
            } else {
               blastMultiplier *= 2;
            }
            break;
         case field_6172:
            if (this.tights.is(AutoArmor.EnchantPriority.Protection)) {
               protectionMultiplier *= 2;
            } else {
               blastMultiplier *= 2;
            }
            break;
         case field_6166:
            if (this.feet.is(AutoArmor.EnchantPriority.Protection)) {
               protectionMultiplier *= 2;
            } else {
               blastMultiplier *= 2;
            }
         }

         if (is.method_7942()) {
            class_9304 enchants = class_1890.method_57532(is);
            if (enchants.method_57534().contains(mc.field_1687.method_30349().method_30530(class_1893.field_9111.method_58273()).method_40264(class_1893.field_9111).get())) {
               prot += enchants.method_57536((class_6880)mc.field_1687.method_30349().method_30530(class_1893.field_9111.method_58273()).method_40264(class_1893.field_9111).get()) * protectionMultiplier;
            }

            if (enchants.method_57534().contains(mc.field_1687.method_30349().method_30530(class_1893.field_9107.method_58273()).method_40264(class_1893.field_9107).get())) {
               prot += enchants.method_57536((class_6880)mc.field_1687.method_30349().method_30530(class_1893.field_9107.method_58273()).method_40264(class_1893.field_9107).get()) * blastMultiplier;
            }

            if (enchants.method_57534().contains(mc.field_1687.method_30349().method_30530(class_1893.field_9107.method_58273()).method_40264(class_1893.field_9113).get()) && (Boolean)this.ignoreCurse.getValue()) {
               prot = -999;
            }
         }

         class_1792 var7 = is.method_7909();
         int var13;
         if (var7 instanceof class_1738) {
            class_1738 armorItem = (class_1738)var7;
            var13 = (armorItem.method_7687() + (int)Math.ceil((double)armorItem.method_26353())) * 10;
         } else {
            var13 = 0;
         }

         return var13 + prot;
      }
   }

   private static enum EnchantPriority {
      Blast,
      Protection;

      // $FF: synthetic method
      private static AutoArmor.EnchantPriority[] $values() {
         return new AutoArmor.EnchantPriority[]{Blast, Protection};
      }
   }

   private static enum ElytraPriority {
      None,
      Always,
      ElytraPlus,
      Ignore;

      // $FF: synthetic method
      private static AutoArmor.ElytraPriority[] $values() {
         return new AutoArmor.ElytraPriority[]{None, Always, ElytraPlus, Ignore};
      }
   }

   public class ArmorData {
      private class_1304 equipmentSlot;
      private int armorSlot;
      private int prevProtection;
      private int newSlot;
      private int newProtection;

      public ArmorData(class_1304 equipmentSlot, int armorSlot, int prevProtection, int newSlot, int newProtection) {
         this.equipmentSlot = equipmentSlot;
         this.armorSlot = armorSlot;
         this.prevProtection = prevProtection;
         this.newSlot = newSlot;
         this.newProtection = newProtection;
      }

      public int getArmorSlot() {
         return this.armorSlot;
      }

      public int getPrevProt() {
         return this.prevProtection;
      }

      public void setPrevProt(int prevProtection) {
         this.prevProtection = prevProtection;
      }

      public int getNewSlot() {
         return this.newSlot;
      }

      public void setNewSlot(int newSlot) {
         this.newSlot = newSlot;
      }

      public int getNewProtection() {
         return this.newProtection;
      }

      public void setNewProtection(int newProtection) {
         this.newProtection = newProtection;
      }

      public class_1304 getEquipmentSlot() {
         return this.equipmentSlot;
      }

      public void reset() {
         this.setPrevProt(AutoArmor.this.getProtection(Module.mc.field_1724.method_31548().method_5438(this.getArmorSlot())));
         this.setNewSlot(-1);
         this.setNewProtection(-1);
      }
   }
}
