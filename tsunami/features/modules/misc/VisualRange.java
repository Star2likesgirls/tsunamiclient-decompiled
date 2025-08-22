package tsunami.features.modules.misc;

import java.util.ArrayList;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_124;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_3417;
import net.minecraft.class_3419;
import tsunami.core.Managers;
import tsunami.core.manager.client.ModuleManager;
import tsunami.events.impl.EventEntityRemoved;
import tsunami.events.impl.EventEntitySpawn;
import tsunami.features.modules.Module;
import tsunami.gui.notification.Notification;
import tsunami.setting.Setting;

public class VisualRange extends Module {
   private static final ArrayList<String> entities = new ArrayList();
   private final Setting<Boolean> leave = new Setting("Leave", true);
   private final Setting<Boolean> enter = new Setting("Enter", true);
   private final Setting<Boolean> friends = new Setting("Friends", true);
   private final Setting<Boolean> soundpl = new Setting("Sound", true);
   private final Setting<VisualRange.Mode> mode;

   public VisualRange() {
      super("VisualRange", Module.Category.NONE);
      this.mode = new Setting("Mode", VisualRange.Mode.Notification);
   }

   @EventHandler
   public void onEntityAdded(EventEntitySpawn event) {
      if (this.isValid(event.getEntity())) {
         if (!entities.contains(event.getEntity().method_5477().getString())) {
            entities.add(event.getEntity().method_5477().getString());
            if ((Boolean)this.enter.getValue()) {
               this.notify(event.getEntity(), true);
            }

         }
      }
   }

   @EventHandler
   public void onEntityRemoved(EventEntityRemoved event) {
      if (this.isValid(event.entity)) {
         if (entities.contains(event.entity.method_5477().getString())) {
            entities.remove(event.entity.method_5477().getString());
            if ((Boolean)this.leave.getValue()) {
               this.notify(event.entity, false);
            }

         }
      }
   }

   public void notify(class_1297 entity, boolean enter) {
      String message = "";
      String var10000;
      if (ModuleManager.nameProtect.isEnabled() && (Boolean)NameProtect.hideFriends.getValue()) {
         var10000 = String.valueOf(class_124.field_1075);
         message = var10000 + (String)NameProtect.newName.getValue();
      }

      if (Managers.FRIEND.isFriend(entity.method_5477().getString())) {
         var10000 = String.valueOf(class_124.field_1075);
         message = var10000 + entity.method_5477().getString();
      } else {
         var10000 = String.valueOf(class_124.field_1080);
         message = var10000 + entity.method_5477().getString();
      }

      if (enter) {
         message = message + String.valueOf(class_124.field_1060) + " was found!";
      } else {
         message = message + String.valueOf(class_124.field_1061) + " left to X:" + (int)entity.method_23317() + " Z:" + (int)entity.method_23321();
      }

      if (this.mode.is(VisualRange.Mode.Chat) || this.mode.is(VisualRange.Mode.Both)) {
         this.sendMessage(message);
      }

      if (this.mode.is(VisualRange.Mode.Notification) || this.mode.is(VisualRange.Mode.Both)) {
         Managers.NOTIFICATION.publicity("VisualRange", message, 2, Notification.Type.WARNING);
      }

      if ((Boolean)this.soundpl.getValue()) {
         try {
            if (enter) {
               mc.field_1687.method_8396(mc.field_1724, mc.field_1724.method_24515(), class_3417.field_14709, class_3419.field_15245, 1.0F, 1.0F);
            } else {
               mc.field_1687.method_8396(mc.field_1724, mc.field_1724.method_24515(), class_3417.field_14627, class_3419.field_15245, 1.0F, 1.0F);
            }
         } catch (Exception var5) {
         }
      }

   }

   public boolean isValid(class_1297 entity) {
      if (!(entity instanceof class_1657)) {
         return false;
      } else {
         return entity != mc.field_1724 && (!Managers.FRIEND.isFriend(entity.method_5477().getString()) || (Boolean)this.friends.getValue());
      }
   }

   public static enum Mode {
      Chat,
      Notification,
      Both;

      // $FF: synthetic method
      private static VisualRange.Mode[] $values() {
         return new VisualRange.Mode[]{Chat, Notification, Both};
      }
   }
}
