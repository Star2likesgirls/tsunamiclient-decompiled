package tsunami.features.cmd.impl;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.class_1713;
import net.minecraft.class_2172;
import net.minecraft.class_2815;
import org.jetbrains.annotations.NotNull;
import tsunami.core.Managers;
import tsunami.features.cmd.Command;

public class DropAllCommand extends Command {
   public DropAllCommand() {
      super("dropall", "drop");
   }

   public void executeBuild(@NotNull LiteralArgumentBuilder<class_2172> builder) {
      builder.then(literal("legit").executes((context) -> {
         Managers.ASYNC.run(() -> {
            for(int i = 5; i <= 45; ++i) {
               mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, i, 1, class_1713.field_7795, mc.field_1724);

               try {
                  Thread.sleep(70L);
               } catch (InterruptedException var2) {
                  throw new RuntimeException(var2);
               }
            }

            mc.field_1724.field_3944.method_52787(new class_2815(mc.field_1724.field_7512.field_7763));
         }, 1L);
         sendMessage("ok");
         return 1;
      }));
      builder.executes((context) -> {
         for(int i = 5; i <= 45; ++i) {
            mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, i, 1, class_1713.field_7795, mc.field_1724);
         }

         mc.field_1724.field_3944.method_52787(new class_2815(mc.field_1724.field_7512.field_7763));
         sendMessage("ok");
         return 1;
      });
   }
}
