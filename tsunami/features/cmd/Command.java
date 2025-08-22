package tsunami.features.cmd;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import net.minecraft.class_1074;
import net.minecraft.class_2170;
import net.minecraft.class_2172;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_7157;
import net.minecraft.class_746;
import net.minecraft.class_7887;
import org.jetbrains.annotations.NotNull;
import tsunami.core.manager.client.CommandManager;

public abstract class Command {
   protected static final class_7157 REGISTRY_ACCESS = class_2170.method_46732(class_7887.method_46817());
   protected static final class_310 mc = class_310.method_1551();
   protected final List<String> names;
   private final String description;

   public Command(String... names) {
      this.names = Arrays.asList(names);
      this.description = "descriptions.commands." + (String)this.names.get(0);
   }

   public abstract void executeBuild(LiteralArgumentBuilder<class_2172> var1);

   public static void sendMessage(String message) {
      if (mc.field_1724 != null) {
         class_746 var10000 = mc.field_1724;
         String var10001 = CommandManager.getClientMessage();
         var10000.method_43496(class_2561.method_30163(var10001 + " " + message));
      }
   }

   @NotNull
   protected static <T> RequiredArgumentBuilder<class_2172, T> arg(String name, ArgumentType<T> type) {
      return RequiredArgumentBuilder.argument(name, type);
   }

   @NotNull
   protected static LiteralArgumentBuilder<class_2172> literal(String name) {
      return LiteralArgumentBuilder.literal(name);
   }

   public void register(CommandDispatcher<class_2172> dispatcher) {
      Iterator var2 = this.names.iterator();

      while(var2.hasNext()) {
         String name = (String)var2.next();
         LiteralArgumentBuilder<class_2172> builder = LiteralArgumentBuilder.literal(name);
         this.executeBuild(builder);
         dispatcher.register(builder);
      }

   }

   public String getName() {
      return (String)this.names.get(0);
   }

   public String getAliases() {
      return String.join(", ", this.names.stream().filter((n) -> {
         return !n.equals(this.names.get(0));
      }).toList());
   }

   public String getDescription() {
      return class_1074.method_4662(this.description, new Object[0]);
   }
}
