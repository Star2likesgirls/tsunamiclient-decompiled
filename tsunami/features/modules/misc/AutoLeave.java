package tsunami.features.modules.misc;

import java.util.Iterator;
import net.minecraft.class_124;
import net.minecraft.class_1657;
import net.minecraft.class_1802;
import net.minecraft.class_2561;
import net.minecraft.class_2868;
import tsunami.core.Managers;
import tsunami.core.manager.client.ModuleManager;
import tsunami.features.modules.Module;
import tsunami.features.modules.client.ClientSettings;
import tsunami.setting.Setting;
import tsunami.setting.impl.SettingGroup;
import tsunami.utility.Timer;
import tsunami.utility.player.InventoryUtility;

public class AutoLeave extends Module {
   private final Setting<Boolean> antiHelperLeave = new Setting("AntiHelperLeave", true);
   private final Setting<Boolean> antiKTLeave = new Setting("AntiKTLeave", true);
   private final Setting<Boolean> autoDisable = new Setting("AutoDisable", true);
   private final Setting<Boolean> fastLeave = new Setting("InstantLeave", true);
   public static Setting<String> command = new Setting("Command", "hub");
   private final Setting<SettingGroup> leaveIf = new Setting("Leave if", new SettingGroup(false, 0));
   private final Setting<Boolean> low_hp;
   private final Setting<Boolean> totems;
   private final Setting<Integer> totemsCount;
   private final Setting<Float> leaveHp;
   private final Setting<AutoLeave.LeaveMode> staff;
   private final Setting<AutoLeave.LeaveMode> players;
   private final Setting<Integer> distance;
   private final Timer chatDelay;
   private final Timer hurtTimer;

   public AutoLeave() {
      super("AutoLeave", Module.Category.MISC);
      this.low_hp = (new Setting("LowHp", false)).addToGroup(this.leaveIf);
      this.totems = (new Setting("Totems", false)).addToGroup(this.leaveIf);
      this.totemsCount = new Setting("TotemsCount", 2, 0, 10, (v) -> {
         return (Boolean)this.totems.getValue();
      });
      this.leaveHp = new Setting("HP", 8.0F, 1.0F, 20.0F, (v) -> {
         return (Boolean)this.low_hp.getValue();
      });
      this.staff = (new Setting("Staff", AutoLeave.LeaveMode.None)).addToGroup(this.leaveIf);
      this.players = (new Setting("Players", AutoLeave.LeaveMode.Leave)).addToGroup(this.leaveIf);
      this.distance = (new Setting("Distance", 256, 4, 256, (v) -> {
         return this.players.getValue() != AutoLeave.LeaveMode.None;
      })).addToGroup(this.leaveIf);
      this.chatDelay = new Timer();
      this.hurtTimer = new Timer();
   }

   public void onUpdate() {
      if (mc.field_1724 != null && mc.field_1687 != null) {
         if (mc.field_1724.field_6235 > 0) {
            this.hurtTimer.reset();
         }

         if (!(Boolean)this.antiKTLeave.getValue() || this.hurtTimer.passedMs(30000L)) {
            Iterator var1 = mc.field_1687.method_18456().iterator();

            while(true) {
               class_1657 pl;
               String prefix;
               do {
                  if (!var1.hasNext()) {
                     if ((Boolean)this.totems.getValue() && InventoryUtility.getItemCount(class_1802.field_8288) <= (Integer)this.totemsCount.getValue()) {
                        this.leave(ClientSettings.isRu() ? "Ливнул т.к. кончились тотемы" : "Logged out because out of totems");
                     }

                     if (mc.field_1724.method_6032() < (Float)this.leaveHp.getValue() && (Boolean)this.low_hp.getValue()) {
                        this.leave(ClientSettings.isRu() ? "Ливнул т.к. мало хп" : "Logged out because ur hp is low");
                     }

                     if (this.staff.getValue() != AutoLeave.LeaveMode.None && ModuleManager.staffBoard.isDisabled() && mc.field_1724.field_6012 % 5 == 0) {
                        this.sendMessage(ClientSettings.isRu() ? "Включи StaffBoard!" : "Turn on StaffBoard!");
                     }

                     return;
                  }

                  pl = (class_1657)var1.next();
                  if (pl.method_5781() == null || !(Boolean)this.antiHelperLeave.getValue()) {
                     break;
                  }

                  prefix = pl.method_5781().method_1144().getString();
               } while(isStaff(class_124.method_539(prefix)));

               if (pl != mc.field_1724 && !Managers.FRIEND.isFriend(pl) && this.players.getValue() != AutoLeave.LeaveMode.None && mc.field_1724.method_5707(pl.method_19538()) <= (double)this.distance.getPow2Value()) {
                  switch(((AutoLeave.LeaveMode)this.players.getValue()).ordinal()) {
                  case 1:
                     if ((Boolean)this.autoDisable.getValue()) {
                        this.disable();
                     }

                     this.sendMessage(ClientSettings.isRu() ? "Ливнул т.к. рядом появился игрок!" : "Logged out because there was a player!");
                     mc.field_1724.field_3944.method_45730((String)command.getValue());
                     return;
                  case 2:
                     this.leave(ClientSettings.isRu() ? "Ливнул т.к. рядом появился игрок" : "Logged out because there was a player");
                     return;
                  }
               }
            }
         }
      }
   }

   private void leave(String message) {
      if (this.chatDelay.passedMs(1000L)) {
         this.chatDelay.reset();
         if ((Boolean)this.autoDisable.getValue()) {
            this.disable(message);
         }

         if ((Boolean)this.fastLeave.getValue()) {
            this.sendPacket(new class_2868(228));
         } else {
            mc.field_1724.field_3944.method_48296().method_10747(class_2561.method_30163("[AutoLeave] " + message));
         }

      }
   }

   public static boolean isStaff(String name) {
      if (name == null) {
         return false;
      } else {
         name = name.toLowerCase();
         return name.contains("helper") || name.contains("moder") || name.contains("admin") || name.contains("owner") || name.contains("curator") || name.contains("куратор") || name.contains("модер") || name.contains("админ") || name.contains("хелпер") || name.contains("поддержка");
      }
   }

   private static enum LeaveMode {
      None,
      Command,
      Leave;

      // $FF: synthetic method
      private static AutoLeave.LeaveMode[] $values() {
         return new AutoLeave.LeaveMode[]{None, Command, Leave};
      }
   }
}
