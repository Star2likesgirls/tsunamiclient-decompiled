package tsunami.features.modules.misc;

import java.util.Iterator;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_1657;
import net.minecraft.class_332;
import net.minecraft.class_7439;
import tsunami.core.Managers;
import tsunami.events.impl.PacketEvent;
import tsunami.features.modules.Module;
import tsunami.features.modules.client.ClientSettings;
import tsunami.features.modules.client.HudEditor;
import tsunami.gui.font.FontRenderers;
import tsunami.setting.Setting;
import tsunami.utility.ThunderUtility;
import tsunami.utility.math.MathUtility;

public class AutoTpAccept extends Module {
   public Setting<Boolean> grief = new Setting("Grief", false);
   public Setting<Boolean> onlyFriends = new Setting("onlyFriends", true);
   public Setting<Boolean> duo = new Setting("Duo", false);
   private final Setting<Integer> timeOut = new Setting("TimeOut", 60, 1, 180, (v) -> {
      return (Boolean)this.duo.getValue();
   });
   private AutoTpAccept.TpTask tpTask;

   public AutoTpAccept() {
      super("AutoTPaccept", Module.Category.MISC);
   }

   @EventHandler
   public void onPacketReceive(PacketEvent.Receive event) {
      if (!fullNullCheck()) {
         if (event.getPacket() instanceof class_7439) {
            class_7439 packet = (class_7439)event.getPacket();
            if (packet.comp_763().getString().contains("телепортироваться") || packet.comp_763().getString().contains("tpaccept")) {
               if ((Boolean)this.onlyFriends.getValue()) {
                  if (Managers.FRIEND.isFriend(ThunderUtility.solveName(packet.comp_763().getString()))) {
                     if (!(Boolean)this.duo.getValue()) {
                        this.acceptRequest(packet.comp_763().getString());
                     } else {
                        this.tpTask = new AutoTpAccept.TpTask(() -> {
                           this.acceptRequest(packet.comp_763.getString());
                        }, System.currentTimeMillis());
                     }
                  }
               } else {
                  this.acceptRequest(packet.comp_763().getString());
               }
            }
         }

      }
   }

   public void onRender2D(class_332 context) {
      if ((Boolean)this.duo.getValue() && this.tpTask != null) {
         String var10000 = ClientSettings.isRu() ? "Ждем таргета " : "Awaiting target ";
         String text = var10000 + MathUtility.round((float)((long)((Integer)this.timeOut.getValue() * 1000) - (System.currentTimeMillis() - this.tpTask.time())) / 1000.0F, 1);
         FontRenderers.sf_bold.drawCenteredString(context.method_51448(), text, (double)((float)mc.method_22683().method_4486() / 2.0F), (double)((float)mc.method_22683().method_4502() / 2.0F + 30.0F), HudEditor.getColor(1).getRGB());
      }

   }

   public void onUpdate() {
      if ((Boolean)this.duo.getValue() && this.tpTask != null) {
         if (System.currentTimeMillis() - this.tpTask.time > (long)((Integer)this.timeOut.getValue() * 1000)) {
            this.tpTask = null;
            return;
         }

         Iterator var1 = mc.field_1687.method_18456().iterator();

         while(var1.hasNext()) {
            class_1657 pl = (class_1657)var1.next();
            if (pl != mc.field_1724 && !Managers.FRIEND.isFriend(pl)) {
               this.tpTask.task.run();
               this.tpTask = null;
               break;
            }
         }
      }

   }

   public void acceptRequest(String name) {
      if ((Boolean)this.grief.getValue()) {
         mc.method_1562().method_45730("tpaccept " + ThunderUtility.solveName(name));
      } else {
         mc.method_1562().method_45730("tpaccept");
      }

   }

   private static record TpTask(Runnable task, long time) {
      private TpTask(Runnable task, long time) {
         this.task = task;
         this.time = time;
      }

      public Runnable task() {
         return this.task;
      }

      public long time() {
         return this.time;
      }
   }
}
