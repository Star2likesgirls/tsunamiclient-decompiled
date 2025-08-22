package tsunami.features.modules.player;

import java.util.ArrayList;
import net.minecraft.class_1703;
import net.minecraft.class_1707;
import net.minecraft.class_1713;
import net.minecraft.class_1735;
import net.minecraft.class_1799;
import net.minecraft.class_2815;
import net.minecraft.class_4587;
import net.minecraft.class_490;
import tsunami.features.modules.Module;
import tsunami.setting.Setting;
import tsunami.setting.impl.ItemSelectSetting;
import tsunami.utility.Timer;

public class InventoryCleaner extends Module {
   public final Setting<ItemSelectSetting> items = new Setting("Items", new ItemSelectSetting(new ArrayList()));
   private final Setting<InventoryCleaner.DropWhen> dropWhen;
   private final Setting<Integer> delay;
   private final Setting<Boolean> cleanChests;
   private final Timer delayTimer;
   private boolean dirty;

   public InventoryCleaner() {
      super("InventoryCleaner", Module.Category.NONE);
      this.dropWhen = new Setting("DropWhen", InventoryCleaner.DropWhen.NotInInventory);
      this.delay = new Setting("Delay", 50, 0, 500);
      this.cleanChests = new Setting("CleanChests", false);
      this.delayTimer = new Timer();
   }

   public void onRender3D(class_4587 stack) {
      boolean inInv = mc.field_1755 instanceof class_490;
      class_1703 var4 = mc.field_1724.field_7512;
      if (var4 instanceof class_1707) {
         class_1707 chest = (class_1707)var4;
         if ((Boolean)this.cleanChests.getValue()) {
            for(int i = 0; i < chest.method_7629().method_5439(); ++i) {
               class_1735 slot = chest.method_7611(i);
               if (slot.method_7681() && this.dropThisShit(slot.method_7677()) && !mc.field_1755.method_25440().getString().contains("Аукцион") && !mc.field_1755.method_25440().getString().contains("покупки") && this.delayTimer.every((long)(Integer)this.delay.getValue())) {
                  mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, i, 1, class_1713.field_7795, mc.field_1724);
                  this.dirty = true;
               }
            }
         }
      }

      if (this.dropWhen.getValue() != InventoryCleaner.DropWhen.Inventory || inInv) {
         if (this.dropWhen.getValue() != InventoryCleaner.DropWhen.NotInInventory || !inInv) {
            for(int slot = 0; slot < 36; ++slot) {
               class_1799 itemFromslot = mc.field_1724.method_31548().method_5438(slot);
               if (this.dropThisShit(itemFromslot)) {
                  this.drop(slot);
               }
            }

            if (this.dirty && this.delayTimer.passedMs((long)((Integer)this.delay.getValue() + 100))) {
               this.sendPacket(new class_2815(mc.field_1724.field_7512.field_7763));
               this.debug("after click cleaning...");
               this.dirty = false;
            }

         }
      }
   }

   private void drop(int slot) {
      if (this.delayTimer.every((long)(Integer)this.delay.getValue())) {
         mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, slot < 9 ? slot + 36 : slot, 1, class_1713.field_7795, mc.field_1724);
         this.dirty = true;
      }

   }

   private boolean dropThisShit(class_1799 stack) {
      return ((ItemSelectSetting)this.items.getValue()).getItemsById().contains(stack.method_7909().method_7876().replace("block.minecraft.", "").replace("item.minecraft.", ""));
   }

   public static enum DropWhen {
      Inventory,
      Always,
      NotInInventory;

      // $FF: synthetic method
      private static InventoryCleaner.DropWhen[] $values() {
         return new InventoryCleaner.DropWhen[]{Inventory, Always, NotInInventory};
      }
   }
}
