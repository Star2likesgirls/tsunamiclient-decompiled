package tsunami.features.cmd.impl;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import net.minecraft.class_2172;
import tsunami.api.IAddon;
import tsunami.core.Managers;
import tsunami.features.cmd.Command;

public class AddonsCommand extends Command {
   public AddonsCommand() {
      super("addons");
   }

   public void executeBuild(LiteralArgumentBuilder<class_2172> builder) {
      builder.executes((context) -> {
         List<IAddon> sortedAddons = Managers.ADDON.getAddons().stream().filter(Objects::nonNull).sorted(Comparator.comparing(IAddon::getName)).toList();
         if (sortedAddons.isEmpty()) {
            sendMessage("No addons installed.");
            return 1;
         } else {
            sortedAddons.forEach((iAddon) -> {
               String var10000 = iAddon.getName();
               sendMessage(var10000 + " by " + iAddon.getAuthor());
            });
            return 1;
         }
      });
   }
}
