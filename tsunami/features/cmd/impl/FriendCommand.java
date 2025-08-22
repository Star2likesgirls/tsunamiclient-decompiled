package tsunami.features.cmd.impl;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import java.util.Iterator;
import net.minecraft.class_2172;
import net.minecraft.class_640;
import org.jetbrains.annotations.NotNull;
import tsunami.core.Managers;
import tsunami.features.cmd.Command;
import tsunami.features.cmd.args.FriendArgumentType;
import tsunami.features.cmd.args.PlayerArgumentType;

public class FriendCommand extends Command {
   public FriendCommand() {
      super("friend", "friends");
   }

   public void executeBuild(@NotNull LiteralArgumentBuilder<class_2172> builder) {
      builder.then(literal("reset").executes((context) -> {
         Managers.FRIEND.clear();
         sendMessage("Friends got reset.");
         return 1;
      }));
      builder.then(literal("add").then(arg("player", PlayerArgumentType.create()).executes((context) -> {
         class_640 player = (class_640)context.getArgument("player", class_640.class);
         Managers.FRIEND.addFriend(player.method_2966().getName());
         sendMessage(player.method_2966().getName() + " has been friended");
         return 1;
      })));
      builder.then(literal("remove").then(arg("player", FriendArgumentType.create()).executes((context) -> {
         String nickname = (String)context.getArgument("player", String.class);
         Managers.FRIEND.removeFriend(nickname);
         sendMessage(nickname + " has been unfriended");
         return 1;
      })));
      builder.then(literal("is").then(arg("player", PlayerArgumentType.create()).executes((context) -> {
         class_640 player = (class_640)context.getArgument("player", class_640.class);
         String var10000 = player.method_2966().getName();
         sendMessage(var10000 + (Managers.FRIEND.isFriend(player.method_2966().getName()) ? " is friended." : " isn't friended."));
         return 1;
      })));
      builder.executes((context) -> {
         if (Managers.FRIEND.getFriends().isEmpty()) {
            sendMessage("Friend list empty D:");
         } else {
            StringBuilder f = new StringBuilder("Friends: ");
            Iterator var2 = Managers.FRIEND.getFriends().iterator();

            while(var2.hasNext()) {
               String friend = (String)var2.next();

               try {
                  f.append(friend).append(", ");
               } catch (Exception var5) {
               }
            }

            sendMessage(f.toString());
         }

         return 1;
      });
   }
}
