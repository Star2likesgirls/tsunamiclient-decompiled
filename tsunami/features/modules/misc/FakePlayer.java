package tsunami.features.modules.misc;

import com.mojang.authlib.GameProfile;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_1268;
import net.minecraft.class_1293;
import net.minecraft.class_1294;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_243;
import net.minecraft.class_2596;
import net.minecraft.class_2663;
import net.minecraft.class_2664;
import net.minecraft.class_3417;
import net.minecraft.class_3419;
import net.minecraft.class_745;
import net.minecraft.class_1297.class_5529;
import tsunami.TsunamiClient;
import tsunami.core.manager.client.ModuleManager;
import tsunami.events.impl.EventAttack;
import tsunami.events.impl.EventSync;
import tsunami.events.impl.PacketEvent;
import tsunami.events.impl.TotemPopEvent;
import tsunami.features.modules.Module;
import tsunami.setting.Setting;
import tsunami.utility.player.InventoryUtility;
import tsunami.utility.world.ExplosionUtility;

public class FakePlayer extends Module {
   private final Setting<Boolean> copyInventory = new Setting("CopyInventory", false);
   public static class_745 fakePlayer;
   private Setting<Boolean> record = new Setting("Record", false);
   private Setting<Boolean> play = new Setting("Play", false);
   private Setting<Boolean> autoTotem = new Setting("AutoTotem", false);
   private Setting<String> name = new Setting("Name", "TsunamiEnjoyer");
   private final List<FakePlayer.PlayerState> positions = new ArrayList();
   int movementTick;
   int deathTime;

   public FakePlayer() {
      super("FakePlayer", Module.Category.MISC);
   }

   public void onEnable() {
      fakePlayer = new class_745(mc.field_1687, new GameProfile(UUID.fromString("66123666-6666-6666-6666-666666666600"), (String)this.name.getValue()));
      fakePlayer.method_5719(mc.field_1724);
      if ((Boolean)this.copyInventory.getValue()) {
         fakePlayer.method_6122(class_1268.field_5808, mc.field_1724.method_6047().method_7972());
         fakePlayer.method_6122(class_1268.field_5810, mc.field_1724.method_6079().method_7972());
         fakePlayer.method_31548().method_5447(36, mc.field_1724.method_31548().method_5438(36).method_7972());
         fakePlayer.method_31548().method_5447(37, mc.field_1724.method_31548().method_5438(37).method_7972());
         fakePlayer.method_31548().method_5447(38, mc.field_1724.method_31548().method_5438(38).method_7972());
         fakePlayer.method_31548().method_5447(39, mc.field_1724.method_31548().method_5438(39).method_7972());
      }

      mc.field_1687.method_53875(fakePlayer);
      fakePlayer.method_6092(new class_1293(class_1294.field_5924, 9999, 2));
      fakePlayer.method_6092(new class_1293(class_1294.field_5898, 9999, 4));
      fakePlayer.method_6092(new class_1293(class_1294.field_5907, 9999, 1));
   }

   @EventHandler
   public void onPacketReceive(PacketEvent.Receive e) {
      class_2596 var3 = e.getPacket();
      if (var3 instanceof class_2664) {
         class_2664 explosion = (class_2664)var3;
         if (fakePlayer != null && fakePlayer.field_6235 == 0) {
            fakePlayer.method_48922(mc.field_1687.method_48963().method_48830());
            fakePlayer.method_6033(fakePlayer.method_6032() + fakePlayer.method_6067() - ExplosionUtility.getAutoCrystalDamage(new class_243(explosion.method_11475(), explosion.method_11477(), explosion.method_11478()), fakePlayer, 0, false));
            if (fakePlayer.method_29504() && fakePlayer.method_6095(mc.field_1687.method_48963().method_48830())) {
               fakePlayer.method_6033(10.0F);
               TsunamiClient.EVENT_BUS.post((Object)(new TotemPopEvent(fakePlayer, 1)));
            }
         }
      }

   }

   @EventHandler
   public void onSync(EventSync e) {
      if ((Boolean)this.record.getValue()) {
         this.positions.add(new FakePlayer.PlayerState(mc.field_1724.method_23317(), mc.field_1724.method_23318(), mc.field_1724.method_23321(), mc.field_1724.method_36454(), mc.field_1724.method_36455()));
      } else {
         if (fakePlayer != null) {
            if ((Boolean)this.play.getValue() && !this.positions.isEmpty()) {
               ++this.movementTick;
               if (this.movementTick >= this.positions.size()) {
                  this.movementTick = 0;
                  return;
               }

               FakePlayer.PlayerState p = (FakePlayer.PlayerState)this.positions.get(this.movementTick);
               fakePlayer.method_36456(p.yaw);
               fakePlayer.method_36457(p.pitch);
               fakePlayer.method_5847(p.yaw);
               fakePlayer.method_43391(p.x, p.y, p.z);
               fakePlayer.method_5759(p.x, p.y, p.z, p.yaw, p.pitch, 3);
            } else {
               this.movementTick = 0;
            }

            if ((Boolean)this.autoTotem.getValue() && fakePlayer.method_6079().method_7909() != class_1802.field_8288) {
               fakePlayer.method_6122(class_1268.field_5810, new class_1799(class_1802.field_8288));
            }

            if (fakePlayer.method_29504()) {
               ++this.deathTime;
               if (this.deathTime > 10) {
                  this.disable();
               }
            }
         }

      }
   }

   @EventHandler
   public void onAttack(EventAttack e) {
      if (fakePlayer != null && e.getEntity() == fakePlayer && fakePlayer.field_6235 == 0 && !e.isPre()) {
         mc.field_1687.method_43128(mc.field_1724, fakePlayer.method_23317(), fakePlayer.method_23318(), fakePlayer.method_23321(), class_3417.field_15115, class_3419.field_15248, 1.0F, 1.0F);
         if (mc.field_1724.field_6017 > 0.0F || ModuleManager.criticals.isEnabled()) {
            mc.field_1687.method_43128(mc.field_1724, fakePlayer.method_23317(), fakePlayer.method_23318(), fakePlayer.method_23321(), class_3417.field_15016, class_3419.field_15248, 1.0F, 1.0F);
         }

         fakePlayer.method_48922(mc.field_1687.method_48963().method_48830());
         if ((double)ModuleManager.aura.getAttackCooldown() >= 0.85D) {
            fakePlayer.method_6033(fakePlayer.method_6032() + fakePlayer.method_6067() - InventoryUtility.getHitDamage(mc.field_1724.method_6047(), fakePlayer));
         } else {
            fakePlayer.method_6033(fakePlayer.method_6032() + fakePlayer.method_6067() - 1.0F);
         }

         if (fakePlayer.method_29504() && fakePlayer.method_6095(mc.field_1687.method_48963().method_48830())) {
            fakePlayer.method_6033(10.0F);
            (new class_2663(fakePlayer, (byte)35)).method_11471(mc.field_1724.field_3944);
         }
      }

   }

   public void onDisable() {
      if (fakePlayer != null) {
         fakePlayer.method_5768();
         fakePlayer.method_31745(class_5529.field_26998);
         fakePlayer.method_36209();
         fakePlayer = null;
         this.positions.clear();
         this.deathTime = 0;
      }
   }

   private static record PlayerState(double x, double y, double z, float yaw, float pitch) {
      private PlayerState(double x, double y, double z, float yaw, float pitch) {
         this.x = x;
         this.y = y;
         this.z = z;
         this.yaw = yaw;
         this.pitch = pitch;
      }

      public double x() {
         return this.x;
      }

      public double y() {
         return this.y;
      }

      public double z() {
         return this.z;
      }

      public float yaw() {
         return this.yaw;
      }

      public float pitch() {
         return this.pitch;
      }
   }
}
