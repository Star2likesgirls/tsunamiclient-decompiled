package tsunami.features.modules.misc;

import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_1590;
import net.minecraft.class_1646;
import net.minecraft.class_2596;
import net.minecraft.class_2824;
import org.jetbrains.annotations.NotNull;
import tsunami.core.Managers;
import tsunami.events.impl.PacketEvent;
import tsunami.features.modules.Module;
import tsunami.features.modules.combat.Criticals;
import tsunami.setting.Setting;

public final class AntiAttack extends Module {
   private final Setting<Boolean> friend = new Setting("Friend", true);
   private final Setting<Boolean> zoglin = new Setting("Zoglin", true);
   private final Setting<Boolean> villager = new Setting("Villager", false);
   private final Setting<Boolean> oneHp = new Setting("OneHp", false);
   private final Setting<Float> hp = new Setting("Hp", 1.0F, 0.0F, 20.0F, (v) -> {
      return (Boolean)this.oneHp.getValue();
   });

   public AntiAttack() {
      super("AntiAttack", Module.Category.NONE);
   }

   @EventHandler
   private void onPacketSend(@NotNull PacketEvent.Send e) {
      class_2596 var3 = e.getPacket();
      if (var3 instanceof class_2824) {
         class_2824 pac = (class_2824)var3;
         class_1297 entity = Criticals.getEntity(pac);
         if (entity == null) {
            return;
         }

         if (Managers.FRIEND.isFriend(entity.method_5477().getString()) && (Boolean)this.friend.getValue()) {
            e.cancel();
         }

         if (entity instanceof class_1590 && (Boolean)this.zoglin.getValue()) {
            e.cancel();
         }

         if (entity instanceof class_1646 && (Boolean)this.villager.getValue()) {
            e.cancel();
         } else if ((Boolean)this.oneHp.getValue() && entity instanceof class_1309) {
            class_1309 lent = (class_1309)entity;
            if (lent.method_6032() <= (Float)this.hp.getValue()) {
               e.cancel();
            }
         }
      }

   }
}
