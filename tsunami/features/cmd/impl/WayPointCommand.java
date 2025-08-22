package tsunami.features.cmd.impl;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.class_2172;
import net.minecraft.class_2338;
import org.jetbrains.annotations.NotNull;
import tsunami.core.Managers;
import tsunami.core.manager.world.WayPointManager;
import tsunami.features.cmd.Command;
import tsunami.features.cmd.args.WayPointArgumentType;
import tsunami.features.modules.client.ClientSettings;

public class WayPointCommand extends Command {
   public WayPointCommand() {
      super("waypoint", "waypoints");
   }

   public void executeBuild(@NotNull LiteralArgumentBuilder<class_2172> builder) {
      builder.then(literal("list").executes((context) -> {
         sendMessage(ClientSettings.isRu() ? "Метки:" : "Waypoints:");
         sendMessage(" ");
         Managers.WAYPOINT.getWayPoints().forEach((wp) -> {
            String var10000 = wp.getName();
            sendMessage(var10000 + " X: " + wp.getX() + " Y: " + wp.getY() + " Z: " + wp.getZ() + " Server: " + wp.getServer() + " Dimension: " + wp.getDimension());
         });
         return 1;
      }));
      builder.then(literal("remove").then(arg("name", WayPointArgumentType.create()).executes((context) -> {
         WayPointManager.WayPoint wp = (WayPointManager.WayPoint)context.getArgument("name", WayPointManager.WayPoint.class);
         Managers.WAYPOINT.removeWayPoint(wp);
         sendMessage(ClientSettings.isRu() ? "Удалена метка " : "Deleted waypoint " + wp.getName());
         return 1;
      })));
      builder.then(literal("add").then(((RequiredArgumentBuilder)arg("name", StringArgumentType.word()).executes((context) -> {
         String name = (String)context.getArgument("name", String.class);
         WayPointManager.WayPoint wp = new WayPointManager.WayPoint((int)mc.field_1724.method_23317(), (int)mc.field_1724.method_23318(), (int)mc.field_1724.method_23321(), name, mc.method_1542() ? "SinglePlayer" : mc.method_1562().method_45734().field_3761, mc.field_1687.method_27983().method_29177().method_12832());
         Managers.WAYPOINT.addWayPoint(wp);
         String var10000 = ClientSettings.isRu() ? "Добавлена метка " + name + " с координатами" : "Added waypoint " + name + " with coords";
         sendMessage(var10000 + " X: " + (int)mc.field_1724.method_23317() + " Y: " + (int)mc.field_1724.method_23318() + " Z: " + (int)mc.field_1724.method_23321());
         return 1;
      })).then(arg("x", IntegerArgumentType.integer()).then(arg("y", IntegerArgumentType.integer()).then(arg("z", IntegerArgumentType.integer()).executes((context) -> {
         String name = (String)context.getArgument("name", String.class);
         class_2338 pos = new class_2338((Integer)context.getArgument("x", Integer.class), (Integer)context.getArgument("y", Integer.class), (Integer)context.getArgument("z", Integer.class));
         WayPointManager.WayPoint wp = new WayPointManager.WayPoint(pos.method_10263(), pos.method_10264(), pos.method_10260(), name, mc.method_1542() ? "SinglePlayer" : mc.method_1562().method_45734().field_3761, mc.field_1687.method_27983().method_29177().method_12832());
         Managers.WAYPOINT.addWayPoint(wp);
         String var10000 = ClientSettings.isRu() ? "Добавлена метка " + name + " с координатами X: " : "Added waypoint " + name + " with coords";
         sendMessage(var10000 + pos.method_10263() + " Y: " + pos.method_10264() + " Z: " + pos.method_10260());
         return 1;
      }))))));
      builder.executes((context) -> {
         sendMessage(this.usage());
         return 1;
      });
   }

   String usage() {
      return "waypoint add/remove/list (waypoint add x y z name), (waypoint remove name)";
   }
}
