package tsunami.features.cmd.impl;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.class_2172;
import org.jetbrains.annotations.NotNull;
import tsunami.core.manager.client.ModuleManager;
import tsunami.features.cmd.Command;

public class TrackerCommand extends Command {
   public TrackerCommand() {
      super("tracker");
   }

   public void executeBuild(@NotNull LiteralArgumentBuilder<class_2172> builder) {
      builder.executes((context) -> {
         if (ModuleManager.tracker.isEnabled()) {
            ModuleManager.tracker.sendTrack();
         }

         return 1;
      });
   }
}
