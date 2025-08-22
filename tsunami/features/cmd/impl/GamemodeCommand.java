package tsunami.features.cmd.impl;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.class_1934;
import net.minecraft.class_2172;
import tsunami.features.cmd.Command;

public class GamemodeCommand extends Command {
   public GamemodeCommand() {
      super("gamemode", "gm");
   }

   public void executeBuild(LiteralArgumentBuilder<class_2172> builder) {
      builder.then(arg("mode", StringArgumentType.greedyString()).executes((context) -> {
         String mode = (String)context.getArgument("mode", String.class);
         byte var3 = -1;
         switch(mode.hashCode()) {
         case -1684593425:
            if (mode.equals("spectator")) {
               var3 = 4;
            }
            break;
         case -1600582850:
            if (mode.equals("survival")) {
               var3 = 0;
            }
            break;
         case -694094064:
            if (mode.equals("adventure")) {
               var3 = 6;
            }
            break;
         case 48:
            if (mode.equals("0")) {
               var3 = 1;
            }
            break;
         case 49:
            if (mode.equals("1")) {
               var3 = 3;
            }
            break;
         case 50:
            if (mode.equals("2")) {
               var3 = 5;
            }
            break;
         case 51:
            if (mode.equals("3")) {
               var3 = 7;
            }
            break;
         case 1820422063:
            if (mode.equals("creative")) {
               var3 = 2;
            }
         }

         switch(var3) {
         case 0:
         case 1:
            mc.field_1761.method_2907(class_1934.field_9215);
         case 2:
         case 3:
            mc.field_1761.method_2907(class_1934.field_9220);
         case 4:
         case 5:
            mc.field_1761.method_2907(class_1934.field_9219);
         case 6:
         case 7:
            mc.field_1761.method_2907(class_1934.field_9216);
         default:
            return 1;
         }
      }));
   }
}
