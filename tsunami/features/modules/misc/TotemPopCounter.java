package tsunami.features.modules.misc;

import java.util.Iterator;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_124;
import net.minecraft.class_1657;
import org.jetbrains.annotations.NotNull;
import tsunami.core.Managers;
import tsunami.events.impl.TotemPopEvent;
import tsunami.features.modules.Module;
import tsunami.features.modules.client.ClientSettings;
import tsunami.features.modules.combat.AntiBot;
import tsunami.gui.notification.Notification;
import tsunami.setting.Setting;

public class TotemPopCounter extends Module {
   public Setting<Boolean> notification = new Setting("Notification", true);

   public TotemPopCounter() {
      super("TotemPopCounter", Module.Category.MISC);
   }

   @EventHandler
   public void onTotemPop(@NotNull TotemPopEvent event) {
      if (event.getEntity() != mc.field_1724) {
         String var10000;
         String s;
         if (ClientSettings.isRu()) {
            var10000 = String.valueOf(class_124.field_1060);
            s = var10000 + event.getEntity().method_5477().getString() + String.valueOf(class_124.field_1068) + " попнул " + String.valueOf(class_124.field_1075) + (event.getPops() > 1 ? event.getPops() + String.valueOf(class_124.field_1068) + " тотемов!" : String.valueOf(class_124.field_1068) + "тотем!");
         } else {
            var10000 = String.valueOf(class_124.field_1060);
            s = var10000 + event.getEntity().method_5477().getString() + String.valueOf(class_124.field_1068) + " popped " + String.valueOf(class_124.field_1075) + (event.getPops() > 1 ? event.getPops() + String.valueOf(class_124.field_1068) + " totems!" : String.valueOf(class_124.field_1068) + " a totem!");
         }

         this.sendMessage(s);
         if ((Boolean)this.notification.getValue()) {
            Managers.NOTIFICATION.publicity("TotemPopCounter", s, 2, Notification.Type.INFO);
         }

      }
   }

   public void onUpdate() {
      Iterator var1 = mc.field_1687.method_18456().iterator();

      while(var1.hasNext()) {
         class_1657 player = (class_1657)var1.next();
         if (player != mc.field_1724 && !AntiBot.bots.contains(player) && !(player.method_6032() > 0.0F) && Managers.COMBAT.popList.containsKey(player.method_5477().getString())) {
            String var10000;
            String s;
            if (ClientSettings.isRu()) {
               var10000 = String.valueOf(class_124.field_1060);
               s = var10000 + player.method_5477().getString() + String.valueOf(class_124.field_1068) + " попнул " + ((Integer)Managers.COMBAT.popList.get(player.method_5477().getString()) > 1 ? String.valueOf(Managers.COMBAT.popList.get(player.method_5477().getString())) + String.valueOf(class_124.field_1068) + " тотемов и сдох!" : String.valueOf(class_124.field_1068) + "тотем и сдох!");
            } else {
               var10000 = String.valueOf(class_124.field_1060);
               s = var10000 + player.method_5477().getString() + String.valueOf(class_124.field_1068) + " popped " + ((Integer)Managers.COMBAT.popList.get(player.method_5477().getString()) > 1 ? String.valueOf(Managers.COMBAT.popList.get(player.method_5477().getString())) + String.valueOf(class_124.field_1068) + " totems and died EZ LMAO!" : String.valueOf(class_124.field_1068) + "totem and died EZ LMAO!");
            }

            this.sendMessage(s);
            if ((Boolean)this.notification.getValue()) {
               Managers.NOTIFICATION.publicity("TotemPopCounter", s, 2, Notification.Type.INFO);
            }
         }
      }

   }
}
