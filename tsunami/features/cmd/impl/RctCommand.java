package tsunami.features.cmd.impl;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.class_2172;
import net.minecraft.class_266;
import tsunami.TsunamiClient;
import tsunami.core.Managers;
import tsunami.features.cmd.Command;
import tsunami.features.modules.client.ClientSettings;

public class RctCommand extends Command {
   public RctCommand() {
      super("rct");
   }

   public void executeBuild(LiteralArgumentBuilder<class_2172> builder) {
      builder.executes((context) -> {
         String sName = mc.field_1724.field_3944.method_45734() == null ? "none" : mc.field_1724.field_3944.method_45734().field_3761;
         if (!sName.contains("funtime") && !sName.contains("spookytime")) {
            sendMessage(ClientSettings.isRu() ? "Rct работает только на фанике и спуки" : "Rct works only on funtime and spookytime");
            return 1;
         } else {
            String var10000 = ((class_266)mc.field_1724.method_7327().method_1151().toArray()[0]).method_1114().getString();
            String an = "an" + var10000.substring(10);
            Managers.ASYNC.run(() -> {
               mc.field_1724.field_3944.method_45731("hub");
               long failSafe = System.currentTimeMillis();

               while(TsunamiClient.core.getSetBackTime() > 600L && System.currentTimeMillis() - failSafe <= 1000L) {
               }

               mc.field_1724.field_3944.method_45731(an);
            });
            return 1;
         }
      });
   }
}
