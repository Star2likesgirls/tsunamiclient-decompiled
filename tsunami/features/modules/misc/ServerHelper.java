package tsunami.features.modules.misc;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_1268;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_1657;
import net.minecraft.class_1703;
import net.minecraft.class_1707;
import net.minecraft.class_1713;
import net.minecraft.class_1735;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_2338;
import net.minecraft.class_2596;
import net.minecraft.class_2815;
import net.minecraft.class_2886;
import net.minecraft.class_332;
import net.minecraft.class_634;
import net.minecraft.class_640;
import net.minecraft.class_7439;
import net.minecraft.class_7472;
import net.minecraft.class_1297.class_5529;
import org.apache.commons.lang3.StringUtils;
import tsunami.TsunamiClient;
import tsunami.core.Managers;
import tsunami.events.impl.EventSync;
import tsunami.events.impl.PacketEvent;
import tsunami.features.modules.Module;
import tsunami.features.modules.client.ClientSettings;
import tsunami.gui.notification.Notification;
import tsunami.setting.Setting;
import tsunami.setting.impl.Bind;
import tsunami.utility.ThunderUtility;
import tsunami.utility.Timer;
import tsunami.utility.player.InventoryUtility;
import tsunami.utility.player.SearchInvResult;
import tsunami.utility.render.Render2DEngine;

public class ServerHelper extends Module {
   private final Setting<Boolean> photomath = new Setting("PhotoMath", false);
   private final Setting<Boolean> antiTpHere = new Setting("AntiTpHere", false);
   private final Setting<Boolean> clanInvite = new Setting("ClanInvite", false);
   private final Setting<Integer> clanInviteDelay = new Setting("InviteDelay", 10, 1, 30, (v) -> {
      return (Boolean)this.clanInvite.getValue();
   });
   private final Setting<Boolean> fixAll = new Setting("/fix all", true);
   private final Setting<Boolean> feed = new Setting("/feed", true);
   private final Setting<Boolean> near = new Setting("/near", true);
   private final Setting<Boolean> airDropWay = new Setting("AirDropWay", true);
   private final Setting<Boolean> farmilka = new Setting("Farmilka", true);
   public final Setting<Boolean> trueSight = new Setting("TrueSight", true);
   private final Setting<Boolean> spek = new Setting("SpekNotify", true);
   private final Setting<Bind> desorient = new Setting("Desorient", new Bind(-1, false, false));
   private final Setting<Bind> trap = new Setting("Trap", new Bind(-1, false, false));
   public final Setting<Boolean> aucHelper = new Setting("AucHelper", true);
   private final Setting<ServerHelper.GroupBy> groupBy;
   private final Setting<Integer> contrast;
   private final Timer pvpTimer;
   private final Timer inviteTimer;
   private final Timer atphtimer;
   private final Timer checktimer;
   private List<ServerHelper.AucItem> result;
   private final Timer disorientTimer;
   private final Timer trapTimer;
   private boolean flag;

   public ServerHelper() {
      super("ServerHelper", Module.Category.NONE);
      this.groupBy = new Setting("GroupBy", ServerHelper.GroupBy.ItemType, (v) -> {
         return (Boolean)this.aucHelper.getValue();
      });
      this.contrast = new Setting("Contrast", 4, 1, 15, (v) -> {
         return (Boolean)this.aucHelper.getValue();
      });
      this.pvpTimer = new Timer();
      this.inviteTimer = new Timer();
      this.atphtimer = new Timer();
      this.checktimer = new Timer();
      this.result = new ArrayList();
      this.disorientTimer = new Timer();
      this.trapTimer = new Timer();
      this.flag = false;
   }

   @EventHandler
   public void onPacketReceive(PacketEvent.Receive event) {
      class_2596 var3 = event.getPacket();
      if (var3 instanceof class_7439) {
         class_7439 pac = (class_7439)var3;
         if ((Boolean)this.spek.getValue()) {
            String content = pac.comp_763().getString().toLowerCase();
            if (content.contains("спек") || content.contains("ызус") || content.contains("spec") || content.contains("spek") || content.contains("ызул")) {
               String name = ThunderUtility.solveName(pac.comp_763().getString());
               Managers.NOTIFICATION.publicity("SpekNotification", ClientSettings.isRu() ? name + " хочет чтобы за ним проследили" : name + " wants to be followed", 3, Notification.Type.WARNING);
            }
         }

         if ((Boolean)this.photomath.getValue() && pac.comp_763().getString().contains("Решите: ") && Objects.equals(ThunderUtility.solveName(pac.comp_763().getString()), "FATAL ERROR")) {
            try {
               mc.field_1724.field_3944.method_45729(String.valueOf(Integer.parseInt(StringUtils.substringBetween(pac.comp_763().getString(), "Решите: ", " + ")) + Integer.parseInt(StringUtils.substringBetween(pac.comp_763().getString(), " + ", " кто первый"))));
            } catch (Exception var7) {
            }
         }

         if ((Boolean)this.antiTpHere.getValue() && pac.comp_763().getString().contains("Телепортирование...") && this.check(pac.comp_763().getString())) {
            this.flag = true;
            this.atphtimer.reset();
         }

         if ((Boolean)this.airDropWay.getValue() && pac.comp_763().getString().contains("Аирдроп")) {
            try {
               int xCord = Integer.parseInt(StringUtils.substringBetween(pac.comp_763().getString(), "координаты X: ", " Y:"));
               int yCord = Integer.parseInt(StringUtils.substringBetween(pac.comp_763().getString(), "Y: ", " Z:"));
               int zCord = Integer.parseInt(StringUtils.substringBetween(pac.comp_763().getString() + "nigga", "Z: ", "nigga"));
               TsunamiClient.gps_position = new class_2338(xCord, yCord, zCord);
               Managers.NOTIFICATION.publicity("FGHelper", "Поставлена метка на аирдроп! X: " + xCord + " Y: " + yCord + " Z: " + zCord, 5, Notification.Type.SUCCESS);
            } catch (Exception var6) {
            }
         }
      }

   }

   public void onUpdate() {
      Iterator var2;
      if (this.flag && this.atphtimer.passedMs(100L) && (Boolean)this.antiTpHere.getValue()) {
         int var10002 = (int)mc.field_1724.method_23317();
         StringBuilder log = new StringBuilder("Тебя телепортировали в X: " + var10002 + " Z: " + (int)mc.field_1724.method_23321() + ". Ближайшие игроки : ");
         var2 = mc.field_1687.method_18456().iterator();

         while(var2.hasNext()) {
            class_1657 entity = (class_1657)var2.next();
            if (entity != mc.field_1724) {
               log.append(entity.method_5477().getString()).append(" ");
            }
         }

         this.sendMessage(String.valueOf(log));
         mc.field_1724.field_3944.method_45731("back");
         this.flag = false;
      }

      if (this.inviteTimer.passedS((double)(Integer)this.clanInviteDelay.getValue()) && (Boolean)this.clanInvite.getValue()) {
         ArrayList<String> playersNames = new ArrayList();
         var2 = mc.field_1724.field_3944.method_2880().iterator();

         while(var2.hasNext()) {
            class_640 player = (class_640)var2.next();
            playersNames.add(player.method_2966().getName());
         }

         if (playersNames.size() > 1) {
            int randomName = (int)Math.floor(Math.random() * (double)playersNames.size());
            class_634 var10000 = mc.field_1724.field_3944;
            Object var10001 = playersNames.get(randomName);
            var10000.method_45731("c invite " + (String)var10001);
            playersNames.clear();
            this.inviteTimer.reset();
         }
      }

      if ((Boolean)this.feed.getValue() && mc.field_1724.method_7344().method_7586() < 8 && this.canSendCommand()) {
         mc.field_1724.field_3944.method_45730("feed");
      }

      if ((Boolean)this.fixAll.getValue() && this.canSendCommand()) {
         mc.field_1724.field_3944.method_45730("fix all");
      }

      if (mc.field_1724.field_6235 > 0) {
         this.pvpTimer.reset();
      }

      if ((Boolean)this.near.getValue() && mc.field_1724.field_6012 % 30 == 0) {
         mc.field_1724.field_3944.method_45730("near");
      }

      if ((Boolean)this.farmilka.getValue()) {
         Iterator var8 = Managers.ASYNC.getAsyncEntities().iterator();

         while(var8.hasNext()) {
            class_1297 ent = (class_1297)var8.next();
            if (!(ent instanceof class_1657) && ent instanceof class_1309 && ((class_1309)ent).method_29504()) {
               mc.field_1687.method_2945(ent.method_5628(), class_5529.field_26998);
            }
         }
      }

      class_1703 var13 = mc.field_1724.field_7512;
      if (var13 instanceof class_1707) {
         class_1707 chest = (class_1707)var13;
         if ((Boolean)this.aucHelper.getValue() && (mc.field_1755.method_25440().getString().contains("Аукцион") || mc.field_1755.method_25440().getString().contains("Поиск"))) {
            this.result.clear();
            Map<String, Integer> itemMap = new HashMap();
            int slot = 0;
            Iterator var4 = chest.method_7602().iterator();

            class_1799 itemStack;
            int price;
            while(var4.hasNext()) {
               itemStack = (class_1799)var4.next();
               if (slot <= 44) {
                  price = this.getPrice(itemStack);
                  if (itemStack.method_7947() > 1) {
                     price /= itemStack.method_7947();
                  }

                  itemMap.put(this.getKey(itemStack), Math.min((Integer)itemMap.getOrDefault(this.getKey(itemStack), 999999999), price));
                  ++slot;
               }
            }

            slot = 0;

            for(var4 = chest.method_7602().iterator(); var4.hasNext(); ++slot) {
               itemStack = (class_1799)var4.next();
               if (itemMap.get(this.getKey(itemStack)) != null) {
                  price = this.getPrice(itemStack);
                  if (itemStack.method_7947() > 1) {
                     price /= itemStack.method_7947();
                  }

                  this.result.add(new ServerHelper.AucItem(itemStack.method_7909(), price, (Integer)itemMap.get(this.getKey(itemStack)), slot));
               }
            }
         }
      }

   }

   private boolean canSendCommand() {
      if (this.pvpTimer.passedMs(30000L)) {
         this.pvpTimer.reset();
         return true;
      } else {
         return false;
      }
   }

   public boolean check(String checkstring) {
      return this.checktimer.passedMs(3000L) && Objects.equals(ThunderUtility.solveName(checkstring), "FATAL ERROR");
   }

   @EventHandler
   public void onPacketSend(PacketEvent.Send e) {
      if (e.getPacket() instanceof class_7472) {
         this.checktimer.reset();
      }

   }

   @EventHandler
   private void onSync(EventSync event) {
      if (!fullNullCheck()) {
         if (this.isKeyPressed(((Bind)this.desorient.getValue()).getKey()) && this.disorientTimer.passedMs(3000L) && mc.field_1755 == null) {
            this.use(InventoryUtility.findInHotBar((i) -> {
               return i.method_7909() == class_1802.field_8449;
            }), InventoryUtility.findInInventory((i) -> {
               return i.method_7909() == class_1802.field_8449;
            }));
            this.disorientTimer.reset();
         }

         if (this.isKeyPressed(((Bind)this.trap.getValue()).getKey()) && this.trapTimer.passedMs(3000L) && mc.field_1755 == null) {
            this.use(InventoryUtility.findInHotBar((i) -> {
               return i.method_7909() == class_1802.field_22021;
            }), InventoryUtility.findInInventory((i) -> {
               return i.method_7909() == class_1802.field_22021;
            }));
            this.trapTimer.reset();
         }

      }
   }

   private String getKey(class_1799 stack) {
      return this.groupBy.is(ServerHelper.GroupBy.Name) ? stack.method_7964().getString() : stack.method_7922();
   }

   public void onRenderChest(class_332 context, class_1735 slot) {
      class_1703 var4 = mc.field_1724.field_7512;
      if (var4 instanceof class_1707) {
         class_1707 chest = (class_1707)var4;
         if (mc.field_1755.method_25440().getString().contains("Аукцион") || mc.field_1755.method_25440().getString().contains("Поиск")) {
            Iterator var7 = this.result.iterator();

            while(var7.hasNext()) {
               ServerHelper.AucItem item = (ServerHelper.AucItem)var7.next();
               if (item.id == slot.field_7874 && slot.field_7874 <= 44 && !slot.method_7677().method_7960()) {
                  float ratio = (float)(Math.pow((double)item.lowestPrice, (double)(Integer)this.contrast.getValue()) / Math.pow((double)item.price, (double)(Integer)this.contrast.getValue()));
                  Render2DEngine.drawRect(context.method_51448(), (float)slot.field_7873, (float)slot.field_7872, 16.0F, 16.0F, Render2DEngine.interpolateColorC(new Color(-16777216, true), new Color(65280), ratio));
                  return;
               }
            }
         }
      }

   }

   public int getPrice(class_1799 stack) {
      if (stack.method_57353().toString() == null) {
         return 999999999;
      } else {
         String string2 = StringUtils.substringBetween(stack.method_57353().toString(), "\"text\":\" $", "\"}]");
         if (string2 == null) {
            return 999999999;
         } else {
            string2 = string2.replaceAll(" ", "");
            int price = 999999999;

            try {
               price = Integer.parseInt(string2);
            } catch (NumberFormatException var5) {
            }

            return price;
         }
      }
   }

   private void use(SearchInvResult result, SearchInvResult invResult) {
      if (result.found()) {
         InventoryUtility.saveAndSwitchTo(result.slot());
         this.sendSequencedPacket((id) -> {
            return new class_2886(class_1268.field_5808, id, mc.field_1724.method_36454(), mc.field_1724.method_36455());
         });
         InventoryUtility.returnSlot();
      } else if (invResult.found()) {
         clickSlot(invResult.slot(), mc.field_1724.method_31548().field_7545, class_1713.field_7791);
         this.sendSequencedPacket((id) -> {
            return new class_2886(class_1268.field_5808, id, mc.field_1724.method_36454(), mc.field_1724.method_36455());
         });
         clickSlot(invResult.slot(), mc.field_1724.method_31548().field_7545, class_1713.field_7791);
         this.sendPacket(new class_2815(mc.field_1724.field_7512.field_7763));
      }

      this.disorientTimer.reset();
   }

   private static enum GroupBy {
      Name,
      ItemType;

      // $FF: synthetic method
      private static ServerHelper.GroupBy[] $values() {
         return new ServerHelper.GroupBy[]{Name, ItemType};
      }
   }

   private static record AucItem(class_1792 item, int price, int lowestPrice, int id) {
      private AucItem(class_1792 item, int price, int lowestPrice, int id) {
         this.item = item;
         this.price = price;
         this.lowestPrice = lowestPrice;
         this.id = id;
      }

      public class_1792 item() {
         return this.item;
      }

      public int price() {
         return this.price;
      }

      public int lowestPrice() {
         return this.lowestPrice;
      }

      public int id() {
         return this.id;
      }
   }
}
