package tsunami.features.modules.misc;

import java.util.Objects;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_1802;
import net.minecraft.class_2596;
import net.minecraft.class_2797;
import net.minecraft.class_634;
import tsunami.core.Managers;
import tsunami.events.impl.PacketEvent;
import tsunami.features.modules.Module;
import tsunami.setting.Setting;

public class MessageAppend extends Module {
   private final Setting<String> word = new Setting("word", " TH RECODE");
   private String skip;

   public MessageAppend() {
      super("MessageAppend", Module.Category.NONE);
   }

   @EventHandler
   public void onPacketSend(PacketEvent.Send e) {
      if (!fullNullCheck()) {
         class_2596 var3 = e.getPacket();
         if (var3 instanceof class_2797) {
            class_2797 pac = (class_2797)var3;
            if (Objects.equals(pac.comp_945(), this.skip)) {
               return;
            }

            if (mc.field_1724.method_6047().method_7909() == class_1802.field_8204 || mc.field_1724.method_6079().method_7909() == class_1802.field_8204) {
               return;
            }

            if (pac.comp_945().startsWith("/") || pac.comp_945().startsWith(Managers.COMMAND.getPrefix())) {
               return;
            }

            String var10001 = pac.comp_945();
            this.skip = var10001 + (String)this.word.getValue();
            class_634 var10000 = mc.field_1724.field_3944;
            var10001 = pac.comp_945();
            var10000.method_45729(var10001 + (String)this.word.getValue());
            e.cancel();
         }

      }
   }
}
