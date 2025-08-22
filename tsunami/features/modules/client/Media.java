package tsunami.features.modules.client;

import java.util.Iterator;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_2561;
import net.minecraft.class_2596;
import net.minecraft.class_640;
import net.minecraft.class_7439;
import org.jetbrains.annotations.NotNull;
import tsunami.events.impl.PacketEvent;
import tsunami.features.modules.Module;
import tsunami.injection.accesors.IGameMessageS2CPacket;
import tsunami.setting.Setting;

public final class Media extends Module {
   public static final Setting<Boolean> skinProtect = new Setting("Skin Protect", true);
   public static final Setting<Boolean> nickProtect = new Setting("Nick Protect", true);

   public Media() {
      super("Media", Module.Category.CLIENT);
   }

   @EventHandler
   public void onPacketReceive(@NotNull PacketEvent.Receive e) {
      class_2596 var3 = e.getPacket();
      if (var3 instanceof class_7439) {
         class_7439 pac = (class_7439)var3;
         if ((Boolean)nickProtect.getValue()) {
            Iterator var6 = mc.field_1724.field_3944.method_2880().iterator();

            while(var6.hasNext()) {
               class_640 ple = (class_640)var6.next();
               if (pac.comp_763().getString().contains(ple.method_2966().getName())) {
                  IGameMessageS2CPacket packet = (IGameMessageS2CPacket)e.getPacket();
                  packet.setContent(class_2561.method_30163(pac.comp_763().getString().replace(ple.method_2966().getName(), "Protected")));
               }
            }
         }
      }

   }
}
