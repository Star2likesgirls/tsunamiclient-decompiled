package tsunami.core;

import tsunami.TsunamiClient;
import tsunami.core.manager.client.AddonManager;
import tsunami.core.manager.client.AsyncManager;
import tsunami.core.manager.client.CommandManager;
import tsunami.core.manager.client.ConfigManager;
import tsunami.core.manager.client.MacroManager;
import tsunami.core.manager.client.ModuleManager;
import tsunami.core.manager.client.NotificationManager;
import tsunami.core.manager.client.ProxyManager;
import tsunami.core.manager.client.ServerManager;
import tsunami.core.manager.client.ShaderManager;
import tsunami.core.manager.client.SoundManager;
import tsunami.core.manager.client.TelemetryManager;
import tsunami.core.manager.player.CombatManager;
import tsunami.core.manager.player.FriendManager;
import tsunami.core.manager.player.PlayerManager;
import tsunami.core.manager.world.HoleManager;
import tsunami.core.manager.world.WayPointManager;
import tsunami.utility.ThunderUtility;

public class Managers {
   public static final CombatManager COMBAT = new CombatManager();
   public static final FriendManager FRIEND = new FriendManager();
   public static final PlayerManager PLAYER = new PlayerManager();
   public static final HoleManager HOLE = new HoleManager();
   public static final WayPointManager WAYPOINT = new WayPointManager();
   public static final AddonManager ADDON = new AddonManager();
   public static final AsyncManager ASYNC = new AsyncManager();
   public static final ModuleManager MODULE = new ModuleManager();
   public static final ConfigManager CONFIG = new ConfigManager();
   public static final MacroManager MACRO = new MacroManager();
   public static final NotificationManager NOTIFICATION = new NotificationManager();
   public static final ProxyManager PROXY = new ProxyManager();
   public static final ServerManager SERVER = new ServerManager();
   public static final ShaderManager SHADER = new ShaderManager();
   public static final SoundManager SOUND = new SoundManager();
   public static final TelemetryManager TELEMETRY = new TelemetryManager();
   public static final CommandManager COMMAND = new CommandManager();

   public static void init() {
      ADDON.initAddons();
      CONFIG.load(CONFIG.getCurrentConfig());
      MODULE.onLoad("none");
      FRIEND.loadFriends();
      MACRO.onLoad();
      WAYPOINT.onLoad();
      PROXY.onLoad();
      SOUND.registerSounds();
      ASYNC.run(() -> {
         ThunderUtility.syncContributors();
         ThunderUtility.parseStarGazer();
         ThunderUtility.parseCommits();
         TELEMETRY.fetchData();
      });
   }

   public static void subscribe() {
      TsunamiClient.EVENT_BUS.subscribe((Object)NOTIFICATION);
      TsunamiClient.EVENT_BUS.subscribe((Object)SERVER);
      TsunamiClient.EVENT_BUS.subscribe((Object)PLAYER);
      TsunamiClient.EVENT_BUS.subscribe((Object)COMBAT);
      TsunamiClient.EVENT_BUS.subscribe((Object)ASYNC);
      TsunamiClient.EVENT_BUS.subscribe((Object)TELEMETRY);
   }
}
