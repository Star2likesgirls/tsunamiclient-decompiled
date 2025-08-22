package tsunami.features.cmd.impl;

import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.class_124;
import net.minecraft.class_1713;
import net.minecraft.class_1802;
import net.minecraft.class_2172;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_2848;
import net.minecraft.class_2828.class_2829;
import net.minecraft.class_2848.class_2849;
import org.jetbrains.annotations.NotNull;
import tsunami.features.cmd.Command;
import tsunami.utility.player.InventoryUtility;

public class EClipCommand extends Command {
   public EClipCommand() {
      super("eclip");
   }

   public void executeBuild(@NotNull LiteralArgumentBuilder<class_2172> builder) {
      builder.then(((LiteralArgumentBuilder)literal("bedrock").executes((context) -> {
         this.execute(-((float)mc.field_1724.method_23318()) - 3.0F);
         return 1;
      })).then(arg("number", FloatArgumentType.floatArg()).executes((context) -> {
         float y = -((float)mc.field_1724.method_23318()) - 3.0F;
         if (y == 0.0F) {
            y = (Float)context.getArgument("number", Float.class);
         }

         this.execute(y);
         return 1;
      })));
      builder.then(((LiteralArgumentBuilder)literal("down").executes((context) -> {
         float y = 0.0F;

         for(int i = 1; i < 255; ++i) {
            if (mc.field_1687.method_8320(class_2338.method_49638(mc.field_1724.method_19538()).method_10069(0, -i, 0)) == class_2246.field_10124.method_9564()) {
               y = (float)(-i - 1);
               break;
            }

            if (mc.field_1687.method_8320(class_2338.method_49638(mc.field_1724.method_19538()).method_10069(0, -i, 0)) == class_2246.field_9987.method_9564()) {
               sendMessage(String.valueOf(class_124.field_1061) + " можно телепортироваться только под бедрок");
               sendMessage(String.valueOf(class_124.field_1061) + " eclip bedrock");
               return 1;
            }
         }

         this.execute(y);
         return 1;
      })).then(arg("number", FloatArgumentType.floatArg()).executes((context) -> {
         float y = 0.0F;

         for(int i = 1; i < 255; ++i) {
            if (mc.field_1687.method_8320(class_2338.method_49638(mc.field_1724.method_19538()).method_10069(0, -i, 0)) == class_2246.field_10124.method_9564()) {
               y = (float)(-i - 1);
               break;
            }

            if (mc.field_1687.method_8320(class_2338.method_49638(mc.field_1724.method_19538()).method_10069(0, -i, 0)) == class_2246.field_9987.method_9564()) {
               sendMessage(String.valueOf(class_124.field_1061) + " можно телепортироваться только под бедрок");
               sendMessage(String.valueOf(class_124.field_1061) + " eclip bedrock");
               return 1;
            }
         }

         if (y == 0.0F) {
            y = (Float)context.getArgument("number", Float.class);
         }

         this.execute(y);
         return 1;
      })));
      builder.then(((LiteralArgumentBuilder)literal("up").executes((context) -> {
         float y = 0.0F;

         for(int i = 4; i < 255; ++i) {
            if (mc.field_1687.method_8320(class_2338.method_49638(mc.field_1724.method_19538()).method_10069(0, i, 0)) == class_2246.field_10124.method_9564()) {
               y = (float)(i + 1);
               break;
            }
         }

         this.execute(y);
         return 1;
      })).then(arg("number", FloatArgumentType.floatArg()).executes((context) -> {
         float y = 0.0F;

         for(int i = 4; i < 255; ++i) {
            if (mc.field_1687.method_8320(class_2338.method_49638(mc.field_1724.method_19538()).method_10069(0, i, 0)) == class_2246.field_10124.method_9564()) {
               y = (float)(i + 1);
               break;
            }
         }

         if (y == 0.0F) {
            y = (Float)context.getArgument("number", Float.class);
         }

         this.execute(y);
         return 1;
      })));
   }

   private void execute(float y) {
      int elytra;
      if ((elytra = InventoryUtility.findItemInInventory(class_1802.field_8833).slot()) == -1) {
         sendMessage(String.valueOf(class_124.field_1061) + "вам нужны элитры в инвентаре");
      } else {
         if (elytra != -2) {
            mc.field_1761.method_2906(0, elytra, 1, class_1713.field_7790, mc.field_1724);
            mc.field_1761.method_2906(0, 6, 1, class_1713.field_7790, mc.field_1724);
         }

         mc.field_1724.field_3944.method_52787(new class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318(), mc.field_1724.method_23321(), false));
         mc.field_1724.field_3944.method_52787(new class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318(), mc.field_1724.method_23321(), false));
         mc.field_1724.field_3944.method_52787(new class_2848(mc.field_1724, class_2849.field_12982));
         mc.field_1724.field_3944.method_52787(new class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318() + (double)y, mc.field_1724.method_23321(), false));
         mc.field_1724.field_3944.method_52787(new class_2848(mc.field_1724, class_2849.field_12982));
         if (elytra != -2) {
            mc.field_1761.method_2906(0, 6, 1, class_1713.field_7790, mc.field_1724);
            mc.field_1761.method_2906(0, elytra, 1, class_1713.field_7790, mc.field_1724);
         }

         mc.field_1724.method_5814(mc.field_1724.method_23317(), mc.field_1724.method_23318() + (double)y, mc.field_1724.method_23321());
      }
   }
}
