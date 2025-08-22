package tsunami.features.cmd.impl;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.class_124;
import net.minecraft.class_2172;
import net.minecraft.class_2338;
import net.minecraft.class_243;
import tsunami.features.cmd.Command;
import tsunami.features.modules.client.ClientSettings;
import tsunami.utility.world.ExplosionUtility;

public class BenchMarkCommand extends Command {
   public BenchMarkCommand() {
      super("benchmark");
   }

   public void executeBuild(LiteralArgumentBuilder<class_2172> builder) {
      builder.executes((context) -> {
         (new Thread(() -> {
            long time = System.currentTimeMillis();
            class_2338 playerPos = class_2338.method_49638(mc.field_1724.method_19538());
            int r = 6;

            int x;
            for(x = playerPos.method_10263() - r; x <= playerPos.method_10263() + r; ++x) {
               for(int y = playerPos.method_10264() - r; y <= playerPos.method_10264() + r; ++y) {
                  for(int z = playerPos.method_10260() - r; z <= playerPos.method_10260() + r; ++z) {
                     float var7 = ExplosionUtility.getExplosionDamage(new class_243((double)x, (double)y, (double)z), mc.field_1724, false);
                  }
               }
            }

            time = System.currentTimeMillis() - time;
            x = (int)(216.0F / (float)time * 10000.0F);
            String var10000 = ClientSettings.isRu() ? "Твой CPU набрал: " : "Your CPU score: ";
            sendMessage(var10000 + String.valueOf(class_124.field_1080) + x);
         })).start();
         return 1;
      });
   }
}
