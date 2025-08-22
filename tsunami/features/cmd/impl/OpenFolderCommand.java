package tsunami.features.cmd.impl;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import java.io.File;
import net.minecraft.class_156;
import net.minecraft.class_2172;
import org.jetbrains.annotations.NotNull;
import tsunami.features.cmd.Command;

public class OpenFolderCommand extends Command {
   public OpenFolderCommand() {
      super("openfolder", "folder");
   }

   public void executeBuild(@NotNull LiteralArgumentBuilder<class_2172> builder) {
      builder.executes((context) -> {
         class_156.method_668().method_672(new File("ThunderHackRecode/configs/"));
         return 1;
      });
   }
}
