package tsunami.features.cmd.args;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import net.minecraft.class_2172;
import net.minecraft.class_2248;
import net.minecraft.class_2561;
import net.minecraft.class_7923;
import tsunami.features.modules.client.ClientSettings;

public class SearchArgumentType implements ArgumentType<String> {
   private static final List<String> EXAMPLES = getRegisteredBlocks().stream().limit(5L).toList();

   public static SearchArgumentType create() {
      return new SearchArgumentType();
   }

   public String parse(StringReader reader) throws CommandSyntaxException {
      String blockName = reader.readString();
      if (!getRegisteredBlocks().contains(blockName)) {
         throw (new DynamicCommandExceptionType((name) -> {
            return class_2561.method_43470(ClientSettings.isRu() ? "Такого блока нет!" : "There is no such block!");
         })).create(blockName);
      } else {
         return blockName;
      }
   }

   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
      return class_2172.method_9265(getRegisteredBlocks(), builder);
   }

   public Collection<String> getExamples() {
      return EXAMPLES;
   }

   public static List<String> getRegisteredBlocks() {
      List<String> result = new ArrayList();
      Iterator var1 = class_7923.field_41175.iterator();

      while(var1.hasNext()) {
         class_2248 block = (class_2248)var1.next();
         result.add(block.method_9539().replace("block.minecraft.", ""));
      }

      return result;
   }
}
