package tsunami.utility.discord.callbacks;

import com.sun.jna.Callback;
import tsunami.utility.discord.DiscordUser;

public interface JoinRequestCallback extends Callback {
   void apply(DiscordUser var1);
}
