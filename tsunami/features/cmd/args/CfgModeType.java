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

public class CfgModeType implements ArgumentType<String> {
   private static final Collection<String> EXAMPLES = List.of("combat", "render", "player", "misc", "client", "hud", "movement");

   public static CfgModeType create() {
      return new CfgModeType();
   }

   public String parse(StringReader reader) throws CommandSyntaxException {
      String cat = reader.readString();
      if (!this.getExamples().contains(cat)) {
         throw (new DynamicCommandExceptionType((name) -> {
            return class_2561.method_43470(ClientSettings.isRu() ? "Вкладки " + name.toString() + " не существует(" : "Category " + name.toString() + " does not exist :(");
         })).create(cat);
      } else {
         return cat;
      }
   }

   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
      return class_2172.method_9265(Managers.CONFIG.getConfigList(), builder);
   }

   public Collection<String> getExamples() {
      return EXAMPLES;
   }
}
