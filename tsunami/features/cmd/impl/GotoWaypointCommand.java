package tsunami.features.cmd.impl;

import baritone.api.BaritoneAPI;
import baritone.api.command.manager.ICommandManager;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.class_2172;
import tsunami.TsunamiClient;
import tsunami.core.manager.world.WayPointManager;
import tsunami.features.cmd.Command;
import tsunami.features.cmd.args.WayPointArgumentType;
import tsunami.features.modules.client.ClientSettings;

public class GotoWaypointCommand extends Command {
   public GotoWaypointCommand() {
      super("goto");
   }

   public void executeBuild(LiteralArgumentBuilder<class_2172> builder) {
      builder.then(arg("name", WayPointArgumentType.create()).executes((context) -> {
         if (!TsunamiClient.baritone) {
            sendMessage(ClientSettings.isRu() ? "Баритон не найден (можешь скачать на https://meteorclient.com)" : "Baritone not found (you can download it at https://meteorclient.com)");
            return 1;
         } else {
            WayPointManager.WayPoint wp = (WayPointManager.WayPoint)context.getArgument("name", WayPointManager.WayPoint.class);
            if (!mc.field_1687.method_27983().method_29177().method_12832().equals(wp.getDimension())) {
               sendMessage(ClientSettings.isRu() ? "Метка в другом измерении" : "Waypoint is in another dimension");
               return 1;
            } else {
               ICommandManager var10000 = BaritoneAPI.getProvider().getPrimaryBaritone().getCommandManager();
               int var10001 = wp.getX();
               var10000.execute("goto " + var10001 + " " + wp.getY() + " " + wp.getZ());
               return 1;
            }
         }
      }));
   }
}
