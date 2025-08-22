package tsunami.features.cmd.args;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import net.minecraft.class_2172;
import tsunami.features.modules.Module;
import tsunami.setting.Setting;

public class SettingArgumentType implements ArgumentType<String> {
   public static SettingArgumentType create() {
      return new SettingArgumentType();
   }

   public String parse(StringReader reader) throws CommandSyntaxException {
      return reader.readString();
   }

   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
      return class_2172.method_9265(getSettings((Module)context.getArgument("module", Module.class)), builder);
   }

   public static List<String> getSettings(Module module) {
      List<String> result = new ArrayList();
      Iterator var2 = module.getSettings().iterator();

      while(var2.hasNext()) {
         Setting<?> setting = (Setting)var2.next();
         result.add(setting.getName());
      }

      return result;
   }
}
