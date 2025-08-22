package tsunami.features.cmd.impl;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import java.util.Iterator;
import net.minecraft.class_2172;
import org.jetbrains.annotations.NotNull;
import tsunami.core.Managers;
import tsunami.features.cmd.Command;
import tsunami.features.modules.Module;
import tsunami.setting.impl.Bind;

public class ResetBindsCommand extends Command {
   public ResetBindsCommand() {
      super("resetbinds", "unbind", "fuckbinds");
   }

   public void executeBuild(@NotNull LiteralArgumentBuilder<class_2172> builder) {
      builder.executes((context) -> {
         Iterator var1 = Managers.MODULE.modules.iterator();

         while(var1.hasNext()) {
            Module mod = (Module)var1.next();
            mod.setBind(new Bind(-1, false, false));
         }

         return 1;
      });
   }
}
