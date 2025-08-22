package tsunami.features.cmd.impl;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.class_124;
import net.minecraft.class_2172;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_2828.class_2829;
import org.jetbrains.annotations.NotNull;
import tsunami.features.cmd.Command;
import tsunami.features.modules.client.ClientSettings;

public class VClipCommand extends Command {
   public VClipCommand() {
      super("vclip");
   }

   public void executeBuild(@NotNull LiteralArgumentBuilder<class_2172> builder) {
      builder.then(literal("down").executes((context) -> {
         float y = 0.0F;

         for(int i = 1; i < 255; ++i) {
            if (mc.field_1687.method_8320(class_2338.method_49638(mc.field_1724.method_19538()).method_10069(0, -i, 0)) == class_2246.field_10124.method_9564()) {
               y = (float)(-i - 1);
               break;
            }

            if (mc.field_1687.method_8320(class_2338.method_49638(mc.field_1724.method_19538()).method_10069(0, -i, 0)) == class_2246.field_9987.method_9564()) {
               String var10000 = String.valueOf(class_124.field_1061);
               sendMessage(var10000 + (ClientSettings.isRu() ? "Некуда клипать!" : "There's nowhere to clip!"));
               return 1;
            }
         }

         this.clip((double)y);
         return 1;
      }));
      builder.then(literal("up").executes((context) -> {
         float y = 0.0F;

         for(int i = 4; i < 255; ++i) {
            if (mc.field_1687.method_8320(class_2338.method_49638(mc.field_1724.method_19538()).method_10069(0, i, 0)) == class_2246.field_10124.method_9564()) {
               y = (float)(i + 1);
               break;
            }
         }

         this.clip((double)y);
         return 1;
      }));
      builder.then(arg("count", DoubleArgumentType.doubleArg()).executes((context) -> {
         double count = (Double)context.getArgument("count", Double.class);

         try {
            String var10000 = String.valueOf(class_124.field_1060);
            sendMessage(var10000 + "Клипаемся на " + count + " блоков");
            this.clip(count);
         } catch (Exception var5) {
         }

         return 1;
      }));
      builder.executes((context) -> {
         sendMessage("Попробуй .vclip <число>");
         return 1;
      });
   }

   private void clip(double b) {
      if (ClientSettings.clipCommandMode.getValue() == ClientSettings.ClipCommandMode.Matrix) {
         int i;
         for(i = 0; i < 10; ++i) {
            mc.field_1724.field_3944.method_52787(new class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318(), mc.field_1724.method_23321(), false));
         }

         for(i = 0; i < 10; ++i) {
            mc.field_1724.field_3944.method_52787(new class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318() + b, mc.field_1724.method_23321(), false));
         }
      } else {
         mc.field_1724.field_3944.method_52787(new class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318() + b, mc.field_1724.method_23321(), false));
      }

      mc.field_1724.method_5814(mc.field_1724.method_23317(), mc.field_1724.method_23318() + b, mc.field_1724.method_23321());
   }
}
