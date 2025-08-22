package tsunami.features.cmd.args;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import net.minecraft.class_2172;
import net.minecraft.class_2561;
import tsunami.core.Managers;
import tsunami.features.modules.client.ClientSettings;

public class FriendArgumentType implements ArgumentType<String> {
   private static final List<String> EXAMPLES;

   public static FriendArgumentType create() {
      return new FriendArgumentType();
   }

   public String parse(StringReader reader) throws CommandSyntaxException {
      String friend = reader.readString();
      if (!Managers.FRIEND.isFriend(friend)) {
         throw (new DynamicCommandExceptionType((name) -> {
            return class_2561.method_43470(ClientSettings.isRu() ? "Друга с именем " + name.toString() + " не существует(" : "Friend with name " + name.toString() + " does not exist :(");
         })).create(friend);
      } else {
         return friend;
      }
   }

   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
      return class_2172.method_9265(Managers.FRIEND.getFriends(), builder);
   }

   public Collection<String> getExamples() {
      return EXAMPLES;
   }

   static {
      EXAMPLES = Managers.FRIEND.getFriends().stream().limit(5L).toList();
   }
}
