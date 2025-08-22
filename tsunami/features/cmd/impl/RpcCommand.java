package tsunami.features.cmd.impl;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.class_2172;
import org.jetbrains.annotations.NotNull;
import tsunami.features.cmd.Command;
import tsunami.features.modules.client.RPC;

public class RpcCommand extends Command {
   public RpcCommand() {
      super("rpc");
   }

   public void executeBuild(@NotNull LiteralArgumentBuilder<class_2172> builder) {
      builder.then(((RequiredArgumentBuilder)arg("bigImg", StringArgumentType.greedyString()).executes((context) -> {
         String bigImg = (String)context.getArgument("bigImg", String.class);
         RPC.WriteFile(bigImg, "none");
         sendMessage("Большая картинка RPC изменена на " + bigImg);
         return 1;
      })).then(arg("littleImg", StringArgumentType.greedyString()).executes((context) -> {
         String bigImg = (String)context.getArgument("bigImg", String.class);
         String littleImg = (String)context.getArgument("littleImg", String.class);
         RPC.WriteFile(bigImg, littleImg);
         sendMessage("Большая картинка RPC изменена на " + bigImg);
         sendMessage("Маленькая картинка RPC изменена на " + littleImg);
         return 1;
      })));
      builder.executes((context) -> {
         sendMessage(".rpc url or .rpc url url");
         return 1;
      });
   }
}
