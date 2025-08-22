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
import tsunami.core.manager.client.MacroManager;
import tsunami.features.modules.client.ClientSettings;

public class MacroArgumentType implements ArgumentType<MacroManager.Macro> {
   private static final Collection<String> EXAMPLES;

   public static MacroArgumentType create() {
      return new MacroArgumentType();
   }

   public MacroManager.Macro parse(StringReader reader) throws CommandSyntaxException {
      MacroManager.Macro macro = Managers.MACRO.getMacroByName(reader.readString());
      if (macro == null) {
         throw (new DynamicCommandExceptionType((name) -> {
            return class_2561.method_43470(ClientSettings.isRu() ? "Макроса " + name.toString() + " не существует(" : "Macro with name " + name.toString() + " does not exists(");
         })).create(reader.readString());
      } else {
         return macro;
      }
   }

   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
      return class_2172.method_9264(Managers.MACRO.getMacros().stream().map(MacroManager.Macro::getName), builder);
   }

   public Collection<String> getExamples() {
      return EXAMPLES;
   }

   static {
      EXAMPLES = Managers.MACRO.getMacros().stream().map(MacroManager.Macro::getName).limit(5L).toList();
   }
}
