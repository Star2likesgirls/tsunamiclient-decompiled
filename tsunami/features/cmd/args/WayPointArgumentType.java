package tsunami.features.cmd.args;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import net.minecraft.class_2172;
import net.minecraft.class_2561;
import tsunami.core.Managers;
import tsunami.core.manager.world.WayPointManager;
import tsunami.features.modules.client.ClientSettings;

public class WayPointArgumentType implements ArgumentType<WayPointManager.WayPoint> {
   private static final Collection<String> EXAMPLES;

   public static WayPointArgumentType create() {
      return new WayPointArgumentType();
   }

   public WayPointManager.WayPoint parse(StringReader reader) throws CommandSyntaxException {
      WayPointManager.WayPoint wp = Managers.WAYPOINT.getWayPointByName(reader.readString());
      if (wp == null) {
         throw (new DynamicCommandExceptionType((name) -> {
            return class_2561.method_43470(ClientSettings.isRu() ? "Вейпоинта " + name.toString() + " не существует(" : "Waypoint " + name.toString() + " does not exist :(");
         })).create(reader.readString());
      } else {
         return wp;
      }
   }

   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
      return class_2172.method_9264(Managers.WAYPOINT.getWayPoints().stream().map(WayPointManager.WayPoint::getName), builder);
   }

   public Collection<String> getExamples() {
      return EXAMPLES;
   }

   static {
      EXAMPLES = Managers.WAYPOINT.getWayPoints().stream().map(WayPointManager.WayPoint::getName).limit(5L).toList();
   }
}
