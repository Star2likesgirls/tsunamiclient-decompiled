package tsunami.features.modules.misc;

import com.google.common.collect.Lists;
import java.util.Comparator;
import java.util.HashMap;
import net.minecraft.class_1268;
import net.minecraft.class_1297;
import net.minecraft.class_1646;
import net.minecraft.class_1713;
import net.minecraft.class_1728;
import net.minecraft.class_1914;
import net.minecraft.class_1916;
import net.minecraft.class_2863;
import net.minecraft.class_437;
import net.minecraft.class_492;
import tsunami.features.modules.Module;
import tsunami.features.modules.client.ClientSettings;
import tsunami.gui.clickui.ClickGUI;
import tsunami.setting.Setting;
import tsunami.setting.impl.BooleanSettingGroup;
import tsunami.setting.impl.SettingGroup;
import tsunami.utility.player.InteractionUtility;
import tsunami.utility.player.InventoryUtility;

public class AutoTrader extends Module {
   private final Setting<BooleanSettingGroup> buy = new Setting("Buy", new BooleanSettingGroup(true));
   private final Setting<String> buyItem;
   private final Setting<BooleanSettingGroup> sell;
   private final Setting<String> sellItem;
   private final Setting<SettingGroup> disable;
   private final Setting<Boolean> noVillagers;
   private final Setting<Boolean> noItems;
   private int interactTicks;
   private int cooldown;
   private int lastVillager;
   private HashMap<Integer, Integer> villagers;

   public AutoTrader() {
      super("AutoTrader", Module.Category.NONE);
      this.buyItem = (new Setting("BuyItem", "apple")).addToGroup(this.buy);
      this.sell = new Setting("Sell", new BooleanSettingGroup(false));
      this.sellItem = (new Setting("SellItem", "bread")).addToGroup(this.sell);
      this.disable = new Setting("Disable", new SettingGroup(false, 0));
      this.noVillagers = (new Setting("NoVillagers", true)).addToGroup(this.disable);
      this.noItems = (new Setting("NoItems", false)).addToGroup(this.disable);
      this.villagers = new HashMap();
   }

   public void onUpdate() {
      if (!fullNullCheck()) {
         if (this.interactTicks > 0) {
            --this.interactTicks;
         }

         if (this.cooldown > 0) {
            --this.cooldown;
         } else {
            HashMap<Integer, Integer> cacheVillagers = new HashMap(this.villagers);
            cacheVillagers.forEach((id, time) -> {
               if (mc.field_1724.field_6012 - time > 160) {
                  this.villagers.remove(id);
               }

            });
            class_437 var3 = mc.field_1755;
            if (var3 instanceof class_492) {
               class_492 merch = (class_492)var3;
               class_1728 msh = (class_1728)merch.method_17577();
               class_1916 offers = msh.method_17438();

               for(int i = 0; i < offers.size(); ++i) {
                  class_1914 offer = (class_1914)offers.get(i);
                  if (this.goodDeal(offer)) {
                     msh.method_20215(i);
                     msh.method_7650(i);
                     this.sendPacket(new class_2863(i));
                     clickSlot(2, class_1713.field_7794);
                     this.cooldown = 3;
                     return;
                  }

                  if (!msh.method_7611(0).method_7677().method_7960()) {
                     clickSlot(0, class_1713.field_7794);
                     this.cooldown = 3;
                     return;
                  }

                  if (!msh.method_7611(1).method_7677().method_7960()) {
                     clickSlot(1, class_1713.field_7794);
                     this.cooldown = 3;
                     return;
                  }

                  if (offer.method_8255()) {
                     this.villagers.put(this.lastVillager, mc.field_1724.field_6012);
                  }
               }

               mc.field_1724.method_7346();
            } else if (this.interactTicks <= 0 && !(mc.field_1755 instanceof ClickGUI)) {
               class_1297 ent = (class_1297)Lists.newArrayList(mc.field_1687.method_18112()).stream().filter((e) -> {
                  return e instanceof class_1646;
               }).filter((e) -> {
                  return mc.field_1724.method_5858(e) < 16.0D;
               }).filter((e) -> {
                  return !this.villagers.containsKey(e.method_5628());
               }).min(Comparator.comparing((e) -> {
                  return mc.field_1724.method_5739(e);
               })).orElse((Object)null);
               if (ent != null) {
                  float[] angles = InteractionUtility.calculateAngle(ent.method_33571().method_1031(Math.random() * 0.2D, 0.0D, Math.random() * 0.2D));
                  mc.field_1724.method_36456(angles[0]);
                  mc.field_1724.method_36457(angles[1]);
                  mc.field_1761.method_2905(mc.field_1724, ent, class_1268.field_5808);
                  this.lastVillager = ent.method_5628();
                  this.interactTicks = 12;
               } else if ((Boolean)this.noVillagers.getValue()) {
                  this.disable(ClientSettings.isRu() ? "Рядом нет жителей!" : "There are no villagers nearby!");
               }
            }

         }
      }
   }

   private boolean goodDeal(class_1914 offer) {
      boolean selectedBuyItem = offer.method_8250().method_7909().method_7876().equals("item.minecraft." + (String)this.buyItem.getValue()) || offer.method_8250().method_7909().method_7876().equals("block.minecraft." + (String)this.buyItem.getValue());
      boolean selectedSellItem = offer.method_19272().method_7909().method_7876().equals("item.minecraft." + (String)this.sellItem.getValue()) || offer.method_19272().method_7909().method_7876().equals("block.minecraft." + (String)this.sellItem.getValue());
      boolean haveItems = offer.method_19272().method_7947() <= InventoryUtility.getItemCount(offer.method_19272().method_7909());
      boolean canBuy = selectedBuyItem && !offer.method_8255() && ((BooleanSettingGroup)this.buy.getValue()).isEnabled();
      boolean canSell = selectedSellItem && !offer.method_8255() && ((BooleanSettingGroup)this.sell.getValue()).isEnabled();
      if ((canBuy || canSell) && !haveItems) {
         if ((Boolean)this.noItems.getValue()) {
            this.disable(ClientSettings.isRu() ? "Кончились предметы!" : "Out of items!");
         }

         return false;
      } else {
         return canBuy || canSell;
      }
   }
}
