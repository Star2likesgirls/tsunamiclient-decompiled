package tsunami.features.modules.render;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_1297;
import net.minecraft.class_1511;
import net.minecraft.class_1531;
import net.minecraft.class_1542;
import net.minecraft.class_1667;
import net.minecraft.class_1681;
import net.minecraft.class_1683;
import net.minecraft.class_1686;
import net.minecraft.class_5904;
import net.minecraft.class_1297.class_5529;
import tsunami.core.Managers;
import tsunami.events.impl.EventSync;
import tsunami.events.impl.PacketEvent;
import tsunami.features.modules.Module;
import tsunami.features.modules.client.ClientSettings;
import tsunami.gui.notification.Notification;
import tsunami.setting.Setting;

public class NoRender extends Module {
   public final Setting<Boolean> disableGuiBackGround = new Setting("noGuiBackGround", true);
   public final Setting<Boolean> noSwing = new Setting("NoHandSwing", false);
   public final Setting<Boolean> auto = new Setting("Auto", false);
   public final Setting<Boolean> hurtCam = new Setting("HurtCam", true);
   public final Setting<Boolean> potions = new Setting("Potions", false);
   public final Setting<Boolean> xp = new Setting("XP", false);
   public final Setting<Boolean> arrows = new Setting("Arrows", false);
   public final Setting<Boolean> eggs = new Setting("Eggs", false);
   public final Setting<Boolean> elderGuardian = new Setting("Guardian", false);
   public final Setting<Boolean> vignette = new Setting("Vignette", true);
   public final Setting<Boolean> portal = new Setting("Portal", true);
   public final Setting<Boolean> explosions = new Setting("Explosions", false);
   public final Setting<Boolean> campFire = new Setting("CampFire", false);
   public final Setting<Boolean> fireworks = new Setting("Fireworks", false);
   public final Setting<Boolean> armor = new Setting("Armor", false);
   public final Setting<Boolean> bossbar = new Setting("Bossbar", false);
   public final Setting<Boolean> fireOverlay = new Setting("FireOverlay", false);
   public final Setting<Boolean> waterOverlay = new Setting("WaterOverlay", false);
   public final Setting<Boolean> blockOverlay = new Setting("BlockOverlay", false);
   public final Setting<Boolean> nausea = new Setting("Nausea", false);
   public final Setting<Boolean> blindness = new Setting("Blindness", false);
   public final Setting<Boolean> fog = new Setting("Fog", false);
   public final Setting<Boolean> darkness = new Setting("Darkness", false);
   public final Setting<Boolean> items = new Setting("Items", false);
   public final Setting<Boolean> crystals = new Setting("Crystals", false);
   public final Setting<Boolean> fireEntity = new Setting("FireEntity", true);
   public final Setting<Boolean> breakParticles = new Setting("BreakParticles", true);
   public final Setting<Boolean> antiTitle = new Setting("AntiTitle", false);
   public final Setting<Boolean> antiPlayerCollision = new Setting("AntiPlayerCollision", true);
   public final Setting<Boolean> noScoreBoard = new Setting("NoScoreBoard", true);
   public final Setting<Boolean> signText = new Setting("SignText", false);
   public final Setting<Boolean> noWeather = new Setting("NoWeather", false);
   public final Setting<Boolean> noArmorStands = new Setting("NoArmorStands", false);
   public final Setting<Boolean> spawnerEntity = new Setting("SpawnerEntity", false);
   public final Setting<Boolean> hotbarItemName = new Setting("HotbarItemName", false);
   private int potionCouter;
   private int xpCounter;
   private int arrowCounter;
   private int itemsCounter;

   public NoRender() {
      super("NoRender", Module.Category.NONE);
   }

   @EventHandler
   public void onPacketReceive(PacketEvent.Receive e) {
      if (e.getPacket() instanceof class_5904 && (Boolean)this.antiTitle.getValue()) {
         e.cancel();
      }

   }

   @EventHandler
   public void onSync(EventSync e) {
      Iterator var2 = Managers.ASYNC.getAsyncEntities().iterator();

      while(var2.hasNext()) {
         class_1297 ent = (class_1297)var2.next();
         if (ent instanceof class_1686) {
            ++this.potionCouter;
            if ((Boolean)this.potions.getValue()) {
               mc.field_1687.method_2945(ent.method_5628(), class_5529.field_26998);
            }
         }

         if (ent instanceof class_1683) {
            ++this.xpCounter;
            if ((Boolean)this.xp.getValue()) {
               mc.field_1687.method_2945(ent.method_5628(), class_5529.field_26998);
            }
         }

         if (ent instanceof class_1511 && (Boolean)this.crystals.getValue()) {
            mc.field_1687.method_2945(ent.method_5628(), class_5529.field_26998);
         }

         if (ent instanceof class_1667) {
            ++this.arrowCounter;
            if ((Boolean)this.arrows.getValue()) {
               mc.field_1687.method_2945(ent.method_5628(), class_5529.field_26998);
            }
         }

         if (ent instanceof class_1681 && (Boolean)this.eggs.getValue()) {
            mc.field_1687.method_2945(ent.method_5628(), class_5529.field_26998);
         }

         if (ent instanceof class_1542) {
            ++this.itemsCounter;
            if ((Boolean)this.items.getValue()) {
               mc.field_1687.method_2945(ent.method_5628(), class_5529.field_26998);
            }
         }

         if (ent instanceof class_1531 && (Boolean)this.noArmorStands.getValue()) {
            mc.field_1687.method_2945(ent.method_5628(), class_5529.field_26998);
         }
      }

      if ((Boolean)this.auto.getValue()) {
         if (this.arrowCounter > 64) {
            Managers.NOTIFICATION.publicity("NoRender", ClientSettings.isRu() ? "Превышен лимит стрел! Удаляю..." : "Arrows limit reached! Removing...", 3, Notification.Type.SUCCESS);
         }

         if (this.itemsCounter > 16) {
            Managers.NOTIFICATION.publicity("NoRender", ClientSettings.isRu() ? "Превышен лимит вещей! Удаляю..." : "Item limit reached! Removing...", 3, Notification.Type.SUCCESS);
         }

         if (this.xpCounter > 16) {
            Managers.NOTIFICATION.publicity("NoRender", ClientSettings.isRu() ? "Превышен лимит пузырьков опыта! Удаляю..." : "XP orbs limit reached! Removing...", 3, Notification.Type.SUCCESS);
         }

         if (this.potionCouter > 8) {
            Managers.NOTIFICATION.publicity("NoRender", ClientSettings.isRu() ? "Превышен лимит зелий! Удаляю..." : "Potions limit reached! Removing...", 3, Notification.Type.SUCCESS);
         }

         List<Integer> toRemove = new ArrayList();
         Iterator var7 = Managers.ASYNC.getAsyncEntities().iterator();

         while(var7.hasNext()) {
            class_1297 ent = (class_1297)var7.next();
            if (ent instanceof class_1667 && this.arrowCounter > 64) {
               toRemove.add(ent.method_5628());
            }

            if (ent instanceof class_1542 && this.itemsCounter > 16) {
               toRemove.add(ent.method_5628());
            }

            if (ent instanceof class_1683 && this.xpCounter > 16) {
               toRemove.add(ent.method_5628());
            }

            if (ent instanceof class_1686 && this.potionCouter > 8) {
               toRemove.add(ent.method_5628());
            }
         }

         try {
            toRemove.forEach((id) -> {
               mc.field_1687.method_2945(id, class_5529.field_26998);
            });
         } catch (Exception var5) {
         }
      }

      this.arrowCounter = 0;
      this.itemsCounter = 0;
      this.potionCouter = 0;
      this.xpCounter = 0;
   }
}
