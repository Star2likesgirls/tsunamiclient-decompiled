package tsunami.features.modules.misc;

import io.netty.util.internal.ConcurrentSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_124;
import net.minecraft.class_1511;
import net.minecraft.class_1657;
import net.minecraft.class_1683;
import net.minecraft.class_1802;
import net.minecraft.class_2338;
import net.minecraft.class_2596;
import net.minecraft.class_2885;
import net.minecraft.class_2886;
import net.minecraft.class_7439;
import tsunami.events.impl.EventEntitySpawn;
import tsunami.events.impl.PacketEvent;
import tsunami.features.modules.Module;
import tsunami.features.modules.client.ClientSettings;
import tsunami.setting.Setting;
import tsunami.utility.math.MathUtility;

public class Tracker extends Module {
   protected final Setting<Boolean> only1v1 = new Setting("1v1-Only", true);
   protected final Set<class_2338> placed = new ConcurrentSet();
   protected final AtomicInteger awaitingExp = new AtomicInteger();
   protected static final AtomicInteger crystals = new AtomicInteger();
   protected static final AtomicInteger exp = new AtomicInteger();
   protected static class_1657 trackedPlayer;
   protected boolean awaiting;
   protected int crystalStacks;
   protected int expStacks;

   public Tracker() {
      super("Tracker", Module.Category.NONE);
   }

   public void onEnable() {
      this.awaiting = false;
      trackedPlayer = null;
      this.awaitingExp.set(0);
      crystals.set(0);
      exp.set(0);
      this.crystalStacks = 0;
      this.expStacks = 0;
   }

   public String getDisplayInfo() {
      return trackedPlayer == null ? null : trackedPlayer.method_5477().getString();
   }

   @EventHandler
   public void onPacketReceive(PacketEvent.Receive event) {
      class_2596 var3 = event.getPacket();
      if (var3 instanceof class_7439) {
         class_7439 pac = (class_7439)var3;
         String s = pac.comp_763().getString();
         if (!s.contains("<") && (s.contains("has accepted your duel request") || s.contains("Accepted the duel request from"))) {
            this.sendMessage(ClientSettings.isRu() ? "Дуель принята! Обновляю цель..." : "Duel accepted! Resetting target...");
            trackedPlayer = null;
            this.awaitingExp.set(0);
            crystals.set(0);
            exp.set(0);
            this.crystalStacks = 0;
            this.expStacks = 0;
         }
      }

   }

   @EventHandler
   public void onEntitySpawn(EventEntitySpawn e) {
      if (e.getEntity() instanceof class_1511 && !this.placed.remove(class_2338.method_49637(e.getEntity().method_23317(), e.getEntity().method_23318() - 1.0D, e.getEntity().method_23321()))) {
         crystals.incrementAndGet();
      }

      if (e.getEntity() instanceof class_1683) {
         if (this.awaitingExp.get() > 0) {
            if (mc.field_1724.method_5858(e.getEntity()) < 16.0D) {
               this.awaitingExp.decrementAndGet();
            } else {
               exp.incrementAndGet();
            }
         } else {
            exp.incrementAndGet();
         }
      }

   }

   public void onUpdate() {
      boolean found = false;
      Iterator var2 = mc.field_1687.method_18456().iterator();

      String var10001;
      while(var2.hasNext()) {
         class_1657 player = (class_1657)var2.next();
         if (player != null && !player.equals(mc.field_1724)) {
            if (found && (Boolean)this.only1v1.getValue()) {
               this.disable(ClientSettings.isRu() ? "Ты не в дуели! Отключаю.." : "Disabled, you are not in a 1v1! Disabling...");
               return;
            }

            if (trackedPlayer == null) {
               var10001 = String.valueOf(class_124.field_1076);
               this.sendMessage(var10001 + (ClientSettings.isRu() ? "Следим за " : "Now tracking ") + String.valueOf(class_124.field_1064) + player.method_5477().getString() + String.valueOf(class_124.field_1076) + "!");
            }

            trackedPlayer = player;
            found = true;
         }
      }

      if (trackedPlayer != null) {
         int exp = Tracker.exp.get() / 64;
         if (this.expStacks != exp) {
            this.expStacks = exp;
            if (ClientSettings.isRu()) {
               var10001 = String.valueOf(class_124.field_1064);
               this.sendMessage(var10001 + trackedPlayer.method_5477().getString() + String.valueOf(class_124.field_1076) + " использовал " + String.valueOf(class_124.field_1068) + exp + String.valueOf(class_124.field_1076) + (exp == 1 ? " стак" : " стаков") + " Пузырьков опыта!");
            } else {
               var10001 = String.valueOf(class_124.field_1064);
               this.sendMessage(var10001 + trackedPlayer.method_5477().getString() + String.valueOf(class_124.field_1076) + " used " + String.valueOf(class_124.field_1068) + exp + String.valueOf(class_124.field_1076) + (exp == 1 ? " stack" : " stacks") + " of XP Bottles!");
            }
         }

         int crystals = Tracker.crystals.get() / 64;
         if (this.crystalStacks != crystals) {
            this.crystalStacks = crystals;
            if (!ClientSettings.isRu()) {
               var10001 = String.valueOf(class_124.field_1064);
               this.sendMessage(var10001 + trackedPlayer.method_5477().getString() + String.valueOf(class_124.field_1076) + " used " + String.valueOf(class_124.field_1068) + crystals + String.valueOf(class_124.field_1076) + (crystals == 1 ? " stack" : " stacks") + " of Crystals!");
            } else {
               var10001 = String.valueOf(class_124.field_1064);
               this.sendMessage(var10001 + trackedPlayer.method_5477().getString() + String.valueOf(class_124.field_1076) + " использовал " + String.valueOf(class_124.field_1068) + crystals + String.valueOf(class_124.field_1076) + (crystals == 1 ? " стак" : " стаков") + " Кристаллов!");
            }
         }

      }
   }

   public void sendTrack() {
      if (trackedPlayer != null) {
         int c = crystals.get();
         int e = exp.get();
         StringBuilder builder;
         if (ClientSettings.isRu()) {
            builder = (new StringBuilder()).append(trackedPlayer.method_5477().getString()).append(class_124.field_1076).append(" использовал ").append(class_124.field_1068).append(c).append(class_124.field_1076).append(" (").append(class_124.field_1068);
         } else {
            builder = (new StringBuilder()).append(trackedPlayer.method_5477().getString()).append(class_124.field_1076).append(" has used ").append(class_124.field_1068).append(c).append(class_124.field_1076).append(" (").append(class_124.field_1068);
         }

         if (c % 64 == 0) {
            builder.append(c / 64);
         } else {
            builder.append(MathUtility.round((double)c / 64.0D, 1));
         }

         if (ClientSettings.isRu()) {
            builder.append(class_124.field_1076).append(") кристаллов и ").append(class_124.field_1068).append(e).append(class_124.field_1076).append(" (").append(class_124.field_1068);
         } else {
            builder.append(class_124.field_1076).append(") crystals and ").append(class_124.field_1068).append(e).append(class_124.field_1076).append(" (").append(class_124.field_1068);
         }

         if (e % 64 == 0) {
            builder.append(e / 64);
         } else {
            builder.append(MathUtility.round((double)e / 64.0D, 1));
         }

         if (ClientSettings.isRu()) {
            builder.append(class_124.field_1076).append(") пузырьков опыта.");
         } else {
            builder.append(class_124.field_1076).append(") bottles of experience.");
         }

         this.sendMessage(builder.toString());
      }

   }

   @EventHandler
   public void onPacketSend(PacketEvent.Send event) {
      if (event.getPacket() instanceof class_2886 && (mc.field_1724.method_6047().method_7909() == class_1802.field_8287 || mc.field_1724.method_6079().method_7909() == class_1802.field_8287)) {
         this.awaitingExp.incrementAndGet();
      }

      class_2596 var3 = event.getPacket();
      if (var3 instanceof class_2885) {
         class_2885 pac = (class_2885)var3;
         if (mc.field_1724.method_6047().method_7909() == class_1802.field_8301 || mc.field_1724.method_6079().method_7909() == class_1802.field_8301) {
            this.placed.add(pac.method_12543().method_17777());
         }
      }

   }
}
