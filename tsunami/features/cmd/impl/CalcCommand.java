package tsunami.features.cmd.impl;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.class_2172;
import org.jetbrains.annotations.NotNull;
import tsunami.features.cmd.Command;

public class CalcCommand extends Command {
   public CalcCommand() {
      super("calc");
   }

   public void executeBuild(@NotNull LiteralArgumentBuilder<class_2172> builder) {
      builder.then(arg("count", StringArgumentType.string()).executes((context) -> {
         String expression = (String)context.getArgument("count", String.class);

         try {
            sendMessage(evaluateExpression(expression));
            return 1;
         } catch (Exception var3) {
            sendMessage("Try use operators: + - m(*) d(/)");
            return -1;
         }
      }));
   }

   public static String evaluateExpression(String expression) {
      char operator = 0;
      int operand1 = 0;
      int operand2 = 0;

      for(int i = 0; i < expression.length(); ++i) {
         char ch = expression.charAt(i);
         if (ch == '+' || ch == '-' || ch == 'm' || ch == 'd') {
            operator = ch;
            operand1 = Integer.parseInt(expression.substring(0, i));
            operand2 = Integer.parseInt(expression.substring(i + 1));
            break;
         }
      }

      String var10000;
      switch(operator) {
      case '+':
         var10000 = String.valueOf(operand1 + operand2);
         break;
      case '-':
         var10000 = String.valueOf(operand1 - operand2);
         break;
      case 'd':
         var10000 = String.valueOf(operand1 / operand2);
         break;
      case 'm':
         var10000 = String.valueOf(operand1 * operand2);
         break;
      default:
         throw new IllegalArgumentException("Wrong");
      }

      return var10000;
   }
}
