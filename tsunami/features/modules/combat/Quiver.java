package tsunami.features.modules.combat;

import java.util.Objects;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_1294;
import net.minecraft.class_1297;
import net.minecraft.class_1753;
import net.minecraft.class_1792;
import net.minecraft.class_1802;
import net.minecraft.class_1833;
import net.minecraft.class_2815;
import net.minecraft.class_2848;
import net.minecraft.class_2828.class_2831;
import net.minecraft.class_2848.class_2849;
import tsunami.events.impl.EventSync;
import tsunami.features.modules.Module;
import tsunami.features.modules.client.ClientSettings;
import tsunami.setting.Setting;
import tsunami.utility.player.InventoryUtility;
import tsunami.utility.player.SearchInvResult;
import tsunami.utility.world.HoleUtility;

public final class Quiver extends Module {
   private final Setting<Boolean> onlyInHole = new Setting("Only In Hole", false);
   private int preBowSlot;
   private int count;

   public Quiver() {
      super("Quiver", Module.Category.NONE);
   }

   public void onEnable() {
      this.count = 0;
      this.preBowSlot = mc.field_1724.method_31548().field_7545;
   }

   public void onDisable() {
      if (this.preBowSlot != -1) {
         InventoryUtility.switchTo(this.preBowSlot);
      }

      mc.field_1690.field_1904.method_23481(false);
   }

   @EventHandler
   private void onSync(EventSync event) {
      if ((HoleUtility.isHole(mc.field_1724.method_24515()) || !(Boolean)this.onlyInHole.getValue()) && (!mc.field_1724.method_6115() || mc.field_1724.method_6047().method_7909().equals(class_1802.field_8102))) {
         SearchInvResult strength = this.getArrow("strength");
         SearchInvResult swiftness = this.getArrow("swiftness");
         boolean hasStrength = !strength.found() || mc.field_1724.method_6059(class_1294.field_5910) && mc.field_1724.method_6112(class_1294.field_5910).method_5584() > 100;
         boolean hasSwiftness = !swiftness.found() || mc.field_1724.method_6059(class_1294.field_5904) && mc.field_1724.method_6112(class_1294.field_5904).method_5584() > 100;
         if (!strength.found() && !swiftness.found()) {
            this.disable(ClientSettings.isRu() ? "В интвенторе отсутствуют нужные стрелы! Отключение..." : "No arrows in hotbar! Disabling...");
         } else if (hasSwiftness && hasStrength) {
            this.disable();
         } else {
            SearchInvResult result = InventoryUtility.findItemInHotBar(class_1802.field_8102);
            if (!result.found()) {
               this.disable(ClientSettings.isRu() ? "В хотбаре отсутствует лук! Отключение..." : "No bow in hotbar! Disabling...");
            } else {
               result.switchTo();
               if ((double)class_1753.method_7722(mc.field_1724.method_6048()) >= 0.15D) {
                  this.releaseBow();
                  this.switchInvSlot(strength.slot(), swiftness.slot());
               } else if (this.count >= (strength.found() && swiftness.found() ? 2 : 1)) {
                  this.disable();
               } else {
                  mc.field_1690.field_1904.method_23481(true);
               }
            }
         }
      }
   }

   private void releaseBow() {
      this.sendPacket(new class_2831(mc.field_1724.method_36454(), -90.0F, mc.field_1724.method_24828()));
      mc.field_1690.field_1904.method_23481(false);
      mc.field_1761.method_2897(mc.field_1724);
      ++this.count;
   }

   private SearchInvResult getArrow(String name) {
      return InventoryUtility.findInInventory((stack) -> {
         class_1792 patt0$temp = stack.method_7909();
         if (patt0$temp instanceof class_1833) {
            class_1833 tai = (class_1833)patt0$temp;
            String key = tai.method_7866(stack);
            return key.contains("effect." + name);
         } else {
            return false;
         }
      });
   }

   private void switchInvSlot(int from, int to) {
      if (from != -1 && to != -1) {
         this.sendPacket(new class_2848((class_1297)Objects.requireNonNull(mc.field_1724), class_2849.field_12985));
         clickSlot(from);
         clickSlot(to);
         clickSlot(from);
         this.sendPacket(new class_2815(mc.field_1724.field_7512.field_7763));
      }
   }
}
