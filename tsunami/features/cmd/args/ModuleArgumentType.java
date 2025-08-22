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
import tsunami.features.modules.Module;
import tsunami.features.modules.client.ClientSettings;

public class ModuleArgumentType implements ArgumentType<Module> {
   private static final Collection<String> EXAMPLES;

   public static ModuleArgumentType create() {
      return new ModuleArgumentType();
   }

   public Module parse(StringReader reader) throws CommandSyntaxException {
      Module module = Managers.MODULE.get(reader.readString());
      if (module == null) {
         throw (new DynamicCommandExceptionType((name) -> {
            return class_2561.method_43470(ClientSettings.isRu() ? "Модуля " + name.toString() + " не существует(" : "Module " + name.toString() + " does not exist :(");
         })).create(reader.readString());
      } else {
         return module;
      }
   }

   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
      return class_2172.method_9264(Managers.MODULE.modules.stream().map(Module::getName), builder);
   }

   public Collection<String> getExamples() {
      return EXAMPLES;
   }

   static {
      EXAMPLES = Managers.MODULE.modules.stream().map(Module::getName).limit(5L).toList();
   }
}
