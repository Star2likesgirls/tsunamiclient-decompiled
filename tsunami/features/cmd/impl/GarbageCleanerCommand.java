package tsunami.features.cmd.impl;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.class_2172;
import org.jetbrains.annotations.NotNull;
import tsunami.features.cmd.Command;

public class GarbageCleanerCommand extends Command {
   public GarbageCleanerCommand() {
      super("gc", "garbagecleaner", "clearram");
   }

   public void executeBuild(@NotNull LiteralArgumentBuilder<class_2172> builder) {
      builder.executes((context) -> {
         sendMessage("Cleaning RAM..");
         System.gc();
         sendMessage("Successfully cleaned RAM!");
         return 1;
      });
   }
}
