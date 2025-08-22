package tsunami.features.modules.misc;

import java.text.SimpleDateFormat;
import java.util.Date;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_124;
import net.minecraft.class_1268;
import net.minecraft.class_1802;
import net.minecraft.class_2350;
import net.minecraft.class_2877;
import net.minecraft.class_2879;
import net.minecraft.class_3965;
import net.minecraft.class_437;
import net.minecraft.class_498;
import tsunami.events.impl.EventScreen;
import tsunami.features.modules.Module;
import tsunami.features.modules.client.ClientSettings;
import tsunami.injection.accesors.ISignEditScreen;
import tsunami.setting.Setting;
import tsunami.utility.player.InventoryUtility;
import tsunami.utility.player.SearchInvResult;

public class AutoSign extends Module {
   private final Setting<String> line1 = new Setting("Line1", "<player>");
   private final Setting<String> line2 = new Setting("Line2", "was here");
   private final Setting<String> line3 = new Setting("Line3", "<------------->");
   private final Setting<String> line4 = new Setting("Line4", "<date>");
   private final Setting<String> dateFormat = new Setting("DateFormat", "dd/MM/yyyy", (v) -> {
      return ((String)this.line1.getValue()).contains("<date>") || ((String)this.line2.getValue()).contains("<date>") || ((String)this.line3.getValue()).contains("<date>") || ((String)this.line4.getValue()).contains("<date>");
   });
   private final Setting<Boolean> glow = new Setting("Glowing", false);

   public AutoSign() {
      super("AutoSign", Module.Category.NONE);
   }

   @EventHandler
   public void onScreen(EventScreen e) {
      class_437 var3 = e.getScreen();
      if (var3 instanceof class_498) {
         class_498 ses = (class_498)var3;
         e.cancel();
         this.sendPacketSilent(new class_2877(((ISignEditScreen)ses).getBlockEntity().method_11016(), ((ISignEditScreen)ses).isFront(), this.format((String)this.line1.getValue()), this.format((String)this.line2.getValue()), this.format((String)this.line3.getValue()), this.format((String)this.line4.getValue())));
         if ((Boolean)this.glow.getValue()) {
            SearchInvResult result = InventoryUtility.findItemInHotBar(class_1802.field_28410);
            boolean offhand = mc.field_1724.method_6079().method_7909() == class_1802.field_28410;
            if (result.found() || offhand) {
               InventoryUtility.saveSlot();
               result.switchTo();
               mc.field_1761.method_2896(mc.field_1724, offhand ? class_1268.field_5810 : class_1268.field_5808, new class_3965(((ISignEditScreen)ses).getBlockEntity().method_11016().method_46558().method_1031(0.0D, 0.5D, 0.0D), class_2350.field_11036, ((ISignEditScreen)ses).getBlockEntity().method_11016(), false));
               this.sendPacket(new class_2879(offhand ? class_1268.field_5810 : class_1268.field_5808));
               InventoryUtility.returnSlot();
            }
         }
      }

   }

   public String format(String s) {
      String format = "dd/MM/yyyy";

      try {
         format = (new SimpleDateFormat((String)this.dateFormat.getValue())).format(new Date());
      } catch (Exception var4) {
         String var10001 = String.valueOf(class_124.field_1061);
         this.sendMessage(var10001 + (ClientSettings.isRu() ? "У тебя не правильный формат даты!" : "Your date format is wrong!"));
      }

      return s.replace("<player>", mc.method_1548().method_1676()).replace("<date>", format);
   }
}
