package tsunami.core.manager.client;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import net.minecraft.class_332;
import net.minecraft.class_4587;
import org.lwjgl.glfw.GLFW;
import tsunami.TsunamiClient;
import tsunami.core.manager.IManager;
import tsunami.features.hud.HudElement;
import tsunami.features.hud.impl.ArmorHud;
import tsunami.features.hud.impl.AutoCrystalInfo;
import tsunami.features.hud.impl.CandleHud;
import tsunami.features.hud.impl.ChestCounter;
import tsunami.features.hud.impl.Companion;
import tsunami.features.hud.impl.Cooldowns;
import tsunami.features.hud.impl.Coords;
import tsunami.features.hud.impl.Crosshair;
import tsunami.features.hud.impl.CrosshairArrows;
import tsunami.features.hud.impl.FpsCounter;
import tsunami.features.hud.impl.GapplesHud;
import tsunami.features.hud.impl.Hotbar;
import tsunami.features.hud.impl.KeyBinds;
import tsunami.features.hud.impl.KillFeed;
import tsunami.features.hud.impl.KillStats;
import tsunami.features.hud.impl.LegacyHud;
import tsunami.features.hud.impl.MemoryHud;
import tsunami.features.hud.impl.ModuleList;
import tsunami.features.hud.impl.PVPResources;
import tsunami.features.hud.impl.PingHud;
import tsunami.features.hud.impl.PotionHud;
import tsunami.features.hud.impl.Radar;
import tsunami.features.hud.impl.RadarRewrite;
import tsunami.features.hud.impl.Speedometer;
import tsunami.features.hud.impl.StaffBoard;
import tsunami.features.hud.impl.TPSCounter;
import tsunami.features.hud.impl.TargetHud;
import tsunami.features.hud.impl.TimerIndicator;
import tsunami.features.hud.impl.TotemCounter;
import tsunami.features.hud.impl.WaterMark;
import tsunami.features.modules.Module;
import tsunami.features.modules.client.AntiCrash;
import tsunami.features.modules.client.AntiPacketException;
import tsunami.features.modules.client.AntiServerAdd;
import tsunami.features.modules.client.AntiServerRP;
import tsunami.features.modules.client.BaritoneSettings;
import tsunami.features.modules.client.ChatTranslator;
import tsunami.features.modules.client.ClickGui;
import tsunami.features.modules.client.ClientSettings;
import tsunami.features.modules.client.ClientSpoof;
import tsunami.features.modules.client.FastLatency;
import tsunami.features.modules.client.HudEditor;
import tsunami.features.modules.client.Media;
import tsunami.features.modules.client.Notifications;
import tsunami.features.modules.client.OptifineCapes;
import tsunami.features.modules.client.RPC;
import tsunami.features.modules.client.Rotations;
import tsunami.features.modules.client.SoundFX;
import tsunami.features.modules.client.ThunderHackGui;
import tsunami.features.modules.client.UnHook;
import tsunami.features.modules.client.WayPoints;
import tsunami.features.modules.client.Windows;
import tsunami.features.modules.combat.AimBot;
import tsunami.features.modules.combat.AntiBot;
import tsunami.features.modules.combat.AntiLegitMiss;
import tsunami.features.modules.combat.Aura;
import tsunami.features.modules.combat.AutoAnchor;
import tsunami.features.modules.combat.AutoAnvil;
import tsunami.features.modules.combat.AutoBed;
import tsunami.features.modules.combat.AutoBuff;
import tsunami.features.modules.combat.AutoCrystal;
import tsunami.features.modules.combat.AutoCrystalBase;
import tsunami.features.modules.combat.AutoGApple;
import tsunami.features.modules.combat.AutoTotem;
import tsunami.features.modules.combat.AutoTrap;
import tsunami.features.modules.combat.AutoWeb;
import tsunami.features.modules.combat.Blocker;
import tsunami.features.modules.combat.BowPop;
import tsunami.features.modules.combat.BowSpam;
import tsunami.features.modules.combat.Breaker;
import tsunami.features.modules.combat.Burrow;
import tsunami.features.modules.combat.Criticals;
import tsunami.features.modules.combat.HitBox;
import tsunami.features.modules.combat.HoleFill;
import tsunami.features.modules.combat.LegitHelper;
import tsunami.features.modules.combat.MoreKnockback;
import tsunami.features.modules.combat.PistonAura;
import tsunami.features.modules.combat.PistonPush;
import tsunami.features.modules.combat.Quiver;
import tsunami.features.modules.combat.Reach;
import tsunami.features.modules.combat.SelfTrap;
import tsunami.features.modules.combat.Surround;
import tsunami.features.modules.combat.TNTAura;
import tsunami.features.modules.combat.TargetStrafe;
import tsunami.features.modules.combat.TriggerBot;
import tsunami.features.modules.misc.AntiAFK;
import tsunami.features.modules.misc.AntiAttack;
import tsunami.features.modules.misc.AutoAuth;
import tsunami.features.modules.misc.AutoEZ;
import tsunami.features.modules.misc.AutoFish;
import tsunami.features.modules.misc.AutoFlyme;
import tsunami.features.modules.misc.AutoGear;
import tsunami.features.modules.misc.AutoLeave;
import tsunami.features.modules.misc.AutoSign;
import tsunami.features.modules.misc.AutoSoup;
import tsunami.features.modules.misc.AutoTpAccept;
import tsunami.features.modules.misc.AutoTrader;
import tsunami.features.modules.misc.ChatUtils;
import tsunami.features.modules.misc.ChestStealer;
import tsunami.features.modules.misc.ChorusExploit;
import tsunami.features.modules.misc.EbatteSratte;
import tsunami.features.modules.misc.ExtraTab;
import tsunami.features.modules.misc.FakePlayer;
import tsunami.features.modules.misc.Ghost;
import tsunami.features.modules.misc.ItemScroller;
import tsunami.features.modules.misc.LagNotifier;
import tsunami.features.modules.misc.MessageAppend;
import tsunami.features.modules.misc.MiddleClick;
import tsunami.features.modules.misc.NameProtect;
import tsunami.features.modules.misc.NoCommentExploit;
import tsunami.features.modules.misc.Nuker;
import tsunami.features.modules.misc.PacketCanceler;
import tsunami.features.modules.misc.PearlChaser;
import tsunami.features.modules.misc.ServerHelper;
import tsunami.features.modules.misc.Spammer;
import tsunami.features.modules.misc.StashLogger;
import tsunami.features.modules.misc.TapeMouse;
import tsunami.features.modules.misc.TotemPopCounter;
import tsunami.features.modules.misc.Tracker;
import tsunami.features.modules.misc.VisualRange;
import tsunami.features.modules.movement.AntiVoid;
import tsunami.features.modules.movement.AntiWeb;
import tsunami.features.modules.movement.AutoSprint;
import tsunami.features.modules.movement.AutoWalk;
import tsunami.features.modules.movement.Avoid;
import tsunami.features.modules.movement.Blink;
import tsunami.features.modules.movement.BoatFly;
import tsunami.features.modules.movement.ClickTP;
import tsunami.features.modules.movement.ElytraPlus;
import tsunami.features.modules.movement.ElytraRecast;
import tsunami.features.modules.movement.EntityControl;
import tsunami.features.modules.movement.EntitySpeed;
import tsunami.features.modules.movement.Flight;
import tsunami.features.modules.movement.GuiMove;
import tsunami.features.modules.movement.HoleAnchor;
import tsunami.features.modules.movement.HoleSnap;
import tsunami.features.modules.movement.Jesus;
import tsunami.features.modules.movement.LevitationControl;
import tsunami.features.modules.movement.LongJump;
import tsunami.features.modules.movement.NoFall;
import tsunami.features.modules.movement.NoPush;
import tsunami.features.modules.movement.NoSlow;
import tsunami.features.modules.movement.NoWaterCollision;
import tsunami.features.modules.movement.PacketFly;
import tsunami.features.modules.movement.Parkour;
import tsunami.features.modules.movement.Phase;
import tsunami.features.modules.movement.ReverseStep;
import tsunami.features.modules.movement.SafeWalk;
import tsunami.features.modules.movement.Scaffold;
import tsunami.features.modules.movement.Speed;
import tsunami.features.modules.movement.Spider;
import tsunami.features.modules.movement.Step;
import tsunami.features.modules.movement.Strafe;
import tsunami.features.modules.movement.TickShift;
import tsunami.features.modules.movement.Timer;
import tsunami.features.modules.movement.TridentBoost;
import tsunami.features.modules.movement.Velocity;
import tsunami.features.modules.movement.WaterSpeed;
import tsunami.features.modules.player.AirPlace;
import tsunami.features.modules.player.AntiAim;
import tsunami.features.modules.player.AntiBadEffects;
import tsunami.features.modules.player.AntiBallPlace;
import tsunami.features.modules.player.AntiHunger;
import tsunami.features.modules.player.AutoArmor;
import tsunami.features.modules.player.AutoEat;
import tsunami.features.modules.player.AutoRespawn;
import tsunami.features.modules.player.AutoSex;
import tsunami.features.modules.player.AutoTool;
import tsunami.features.modules.player.DurabilityAlert;
import tsunami.features.modules.player.ElytraReplace;
import tsunami.features.modules.player.ElytraSwap;
import tsunami.features.modules.player.FastUse;
import tsunami.features.modules.player.HotbarReplenish;
import tsunami.features.modules.player.InventoryCleaner;
import tsunami.features.modules.player.MouseElytraFix;
import tsunami.features.modules.player.MultiTask;
import tsunami.features.modules.player.NoEntityTrace;
import tsunami.features.modules.player.NoInteract;
import tsunami.features.modules.player.NoJumpDelay;
import tsunami.features.modules.player.NoServerRotate;
import tsunami.features.modules.player.NoServerSlot;
import tsunami.features.modules.player.PearlBait;
import tsunami.features.modules.player.PearlBlockThrow;
import tsunami.features.modules.player.PerfectDelay;
import tsunami.features.modules.player.PortalGodMode;
import tsunami.features.modules.player.PortalInventory;
import tsunami.features.modules.player.Regen;
import tsunami.features.modules.player.SpeedMine;
import tsunami.features.modules.player.ToolSaver;
import tsunami.features.modules.player.TpsSync;
import tsunami.features.modules.player.ViewLock;
import tsunami.features.modules.player.XCarry;
import tsunami.features.modules.render.Animations;
import tsunami.features.modules.render.AspectRatio;
import tsunami.features.modules.render.BlockESP;
import tsunami.features.modules.render.BlockHighLight;
import tsunami.features.modules.render.BreadCrumbs;
import tsunami.features.modules.render.BreakHighLight;
import tsunami.features.modules.render.Chams;
import tsunami.features.modules.render.ChunkFinder;
import tsunami.features.modules.render.DamageTint;
import tsunami.features.modules.render.ESP;
import tsunami.features.modules.render.FOV;
import tsunami.features.modules.render.FreeCam;
import tsunami.features.modules.render.Fullbright;
import tsunami.features.modules.render.HitBubbles;
import tsunami.features.modules.render.HitParticles;
import tsunami.features.modules.render.HoleESP;
import tsunami.features.modules.render.ItemESP;
import tsunami.features.modules.render.JumpCircle;
import tsunami.features.modules.render.KelpEsp;
import tsunami.features.modules.render.KillEffect;
import tsunami.features.modules.render.LogoutSpots;
import tsunami.features.modules.render.NameTags;
import tsunami.features.modules.render.NoBob;
import tsunami.features.modules.render.NoCameraClip;
import tsunami.features.modules.render.NoRender;
import tsunami.features.modules.render.Particles;
import tsunami.features.modules.render.PenisESP;
import tsunami.features.modules.render.PopChams;
import tsunami.features.modules.render.Shaders;
import tsunami.features.modules.render.SoundESP;
import tsunami.features.modules.render.StorageEsp;
import tsunami.features.modules.render.Tooltips;
import tsunami.features.modules.render.TotemAnimation;
import tsunami.features.modules.render.Tracers;
import tsunami.features.modules.render.Trails;
import tsunami.features.modules.render.Trajectories;
import tsunami.features.modules.render.TunnelEsp;
import tsunami.features.modules.render.ViewModel;
import tsunami.features.modules.render.VoidESP;
import tsunami.features.modules.render.WanderingESP;
import tsunami.features.modules.render.WorldTweaks;
import tsunami.features.modules.render.XRay;
import tsunami.gui.clickui.ClickGUI;
import tsunami.gui.font.FontRenderers;

public class ModuleManager implements IManager {
   public ArrayList<Module> modules = new ArrayList();
   public List<Module> sortedModules = new ArrayList();
   public List<Integer> activeMouseKeys = new ArrayList();
   public static PenisESP penisESP = new PenisESP();
   public static AntiPacketException antiPacketException = new AntiPacketException();
   public static LevitationControl levitationControl = new LevitationControl();
   public static InventoryCleaner inventoryCleaner = new InventoryCleaner();
   public static NoCommentExploit noCommentExploit = new NoCommentExploit();
   public static NoWaterCollision noWaterCollision = new NoWaterCollision();
   public static BaritoneSettings baritoneSettings = new BaritoneSettings();
   public static PortalInventory portalInventory = new PortalInventory();
   public static TotemPopCounter totemPopCounter = new TotemPopCounter();
   public static HotbarReplenish hotbarReplenish = new HotbarReplenish();
   public static DurabilityAlert durabilityAlert = new DurabilityAlert();
   public static AutoCrystalBase autoCrystalBase = new AutoCrystalBase();
   public static CrosshairArrows crosshairArrows = new CrosshairArrows();
   public static PearlBlockThrow pearlBlockThrow = new PearlBlockThrow();
   public static AutoCrystalInfo autoCrystalInfo = new AutoCrystalInfo();
   public static ChatTranslator chatTranslator = new ChatTranslator();
   public static PacketCanceler packetCanceler = new PacketCanceler();
   public static ClientSettings clientSettings = new ClientSettings();
   public static TimerIndicator timerIndicator = new TimerIndicator();
   public static ThunderHackGui thunderHackGui = new ThunderHackGui();
   public static NoServerRotate noServerRotate = new NoServerRotate();
   public static BreakHighLight breakHighLight = new BreakHighLight();
   public static BlockHighLight blockHighLight = new BlockHighLight();
   public static AntiBadEffects antiBadEffects = new AntiBadEffects();
   public static MouseElytraFix mouseElytraFix = new MouseElytraFix();
   public static TotemAnimation totemAnimation = new TotemAnimation();
   public static PortalGodMode portalGodMode = new PortalGodMode();
   public static OptifineCapes optifineCapes = new OptifineCapes();
   public static Notifications notifications = new Notifications();
   public static NoEntityTrace noEntityTrace = new NoEntityTrace();
   public static MessageAppend messageAppend = new MessageAppend();
   public static EntityControl entityControl = new EntityControl();
   public static ElytraReplace elytraReplace = new ElytraReplace();
   public static ChorusExploit chorusExploit = new ChorusExploit();
   public static MoreKnockback moreKnockback = new MoreKnockback();
   public static AntiServerAdd antiServerAdd = new AntiServerAdd();
   public static AntiLegitMiss antiLegitMiss = new AntiLegitMiss();
   public static AntiBallPlace antiBallPlace = new AntiBallPlace();
   public static TridentBoost tridentBoost = new TridentBoost();
   public static Trajectories trajectories = new Trajectories();
   public static TargetStrafe targetStrafe = new TargetStrafe();
   public static RadarRewrite radarRewrite = new RadarRewrite();
   public static PVPResources pvpResources = new PVPResources();
   public static NoServerSlot noServerSlot = new NoServerSlot();
   public static NoCameraClip noCameraClip = new NoCameraClip();
   public static ItemScroller itemScroller = new ItemScroller();
   public static HitParticles hitParticles = new HitParticles();
   public static ElytraRecast elytraRecast = new ElytraRecast();
   public static EbatteSratte ebatteSratte = new EbatteSratte();
   public static ChestStealer chestStealer = new ChestStealer();
   public static AutoTpAccept autoTpAccept = new AutoTpAccept();
   public static AntiServerRP antiServerRP = new AntiServerRP();
   public static TotemCounter totemCounter = new TotemCounter();
   public static PerfectDelay perfectDelay = new PerfectDelay();
   public static ServerHelper serverHelper = new ServerHelper();
   public static ChestCounter chestCounter = new ChestCounter();
   public static StashLogger stashLogger = new StashLogger();
   public static FastLatency fastLatency = new FastLatency();
   public static PearlChaser pearlChaser = new PearlChaser();
   public static WorldTweaks worldTweaks = new WorldTweaks();
   public static VisualRange visualRange = new VisualRange();
   public static Speedometer speedometer = new Speedometer();
   public static ReverseStep reverseStep = new ReverseStep();
   public static NoJumpDelay noJumpDelay = new NoJumpDelay();
   public static NameProtect nameProtect = new NameProtect();
   public static MiddleClick middleClick = new MiddleClick();
   public static LogoutSpots logoutSpots = new LogoutSpots();
   public static LagNotifier lagNotifier = new LagNotifier();
   public static BreadCrumbs breadCrumbs = new BreadCrumbs();
   public static AutoRespawn autoRespawn = new AutoRespawn();
   public static AutoCrystal autoCrystal = new AutoCrystal();
   public static EntitySpeed entitySpeed = new EntitySpeed();
   public static AspectRatio aspectRatio = new AspectRatio();
   public static ClientSpoof clientSpoof = new ClientSpoof();
   public static LegitHelper legitHelper = new LegitHelper();
   public static AutoAnchor autoAnchor = new AutoAnchor();
   public static WaterSpeed waterSpeed = new WaterSpeed();
   public static TriggerBot triggerBot = new TriggerBot();
   public static TPSCounter tpsCounter = new TPSCounter();
   public static StorageEsp storageEsp = new StorageEsp();
   public static StaffBoard staffBoard = new StaffBoard();
   public static PistonPush pistonPush = new PistonPush();
   public static PistonAura pistonAura = new PistonAura();
   public static NoInteract noInteract = new NoInteract();
   public static ModuleList moduleList = new ModuleList();
   public static KillEffect killEffect = new KillEffect();
   public static JumpCircle jumpCircle = new JumpCircle();
   public static HoleAnchor holeAnchor = new HoleAnchor();
   public static Fullbright fullbright = new Fullbright();
   public static FpsCounter fpsCounter = new FpsCounter();
   public static FakePlayer fakePlayer = new FakePlayer();
   public static ElytraSwap elytraSwap = new ElytraSwap();
   public static ElytraPlus elytraPlus = new ElytraPlus();
   public static AutoSprint autoSprint = new AutoSprint();
   public static AutoGApple autoGApple = new AutoGApple();
   public static AntiHunger antiHunger = new AntiHunger();
   public static Animations animations = new Animations();
   public static DamageTint damageTint = new DamageTint();
   public static AntiAttack antiAttack = new AntiAttack();
   public static GapplesHud gapplesHud = new GapplesHud();
   public static HitBubbles hitBubbles = new HitBubbles();
   public static AutoTrader autoTrader = new AutoTrader();
   public static KillStats killStats = new KillStats();
   public static AutoAnvil autoAnvil = new AutoAnvil();
   public static CandleHud candleHud = new CandleHud();
   public static Particles particles = new Particles();
   public static ToolSaver toolSaver = new ToolSaver();
   public static WayPoints wayPoints = new WayPoints();
   public static WaterMark waterMark = new WaterMark();
   public static ViewModel viewModel = new ViewModel();
   public static TunnelEsp tunnelEsp = new TunnelEsp();
   public static TickShift tickShift = new TickShift();
   public static TargetHud targetHud = new TargetHud();
   public static SpeedMine speedMine = new SpeedMine();
   public static PotionHud potionHud = new PotionHud();
   public static PearlBait pearlBait = new PearlBait();
   public static PacketFly packetFly = new PacketFly();
   public static MultiTask multitask = new MultiTask();
   public static LegacyHud legacyHud = new LegacyHud();
   public static HudEditor hudEditor = new HudEditor();
   public static Crosshair crosshair = new Crosshair();
   public static Criticals criticals = new Criticals();
   public static ChatUtils chatUtils = new ChatUtils();
   public static AutoTotem autoTotem = new AutoTotem();
   public static AutoLeave autoLeave = new AutoLeave();
   public static AutoFlyme autoFlyme = new AutoFlyme();
   public static AutoArmor autoArmor = new AutoArmor();
   public static Cooldowns cooldowns = new Cooldowns();
   public static TapeMouse tapeMouse = new TapeMouse();
   public static Rotations rotations = new Rotations();
   public static MemoryHud memoryHud = new MemoryHud();
   public static Companion companion = new Companion();
   public static AntiCrash antiCrash = new AntiCrash();
   public static AutoGear autoGear = new AutoGear();
   public static ViewLock viewLock = new ViewLock();
   public static Velocity velocity = new Velocity();
   public static Tooltips tooltips = new Tooltips();
   public static Surround surround = new Surround();
   public static Scaffold scaffold = new Scaffold();
   public static PopChams popChams = new PopChams();
   public static NoRender noRender = new NoRender();
   public static NameTags nameTags = new NameTags();
   public static LongJump longJump = new LongJump();
   public static KeyBinds keyBinds = new KeyBinds();
   public static HoleSnap holeSnap = new HoleSnap();
   public static HoleFill holeFill = new HoleFill();
   public static ExtraTab extraTab = new ExtraTab();
   public static ClickGui clickGui = new ClickGui();
   public static AutoTrap autoTrap = new AutoTrap();
   public static AutoTool autoTool = new AutoTool();
   public static SoundESP soundESP = new SoundESP();
   public static AutoSoup autoSoup = new AutoSoup();
   public static AutoFish autoFish = new AutoFish();
   public static AutoBuff autoBuff = new AutoBuff();
   public static AutoAuth autoAuth = new AutoAuth();
   public static ArmorHud armorHud = new ArmorHud();
   public static AirPlace airPlace = new AirPlace();
   public static SelfTrap selfTrap = new SelfTrap();
   public static AntiVoid antiVoid = new AntiVoid();
   public static KillFeed killFeed = new KillFeed();
   public static AutoWalk autoWalk = new AutoWalk();
   public static AutoSign autoSign = new AutoSign();
   public static BlockESP blockESP = new BlockESP();
   public static SafeWalk safeWalk = new SafeWalk();
   public static Windows windows = new Windows();
   public static Breaker breaker = new Breaker();
   public static AutoEat autoEat = new AutoEat();
   public static AntiAFK antiAFK = new AntiAFK();
   public static SoundFX soundFX = new SoundFX();
   public static AutoBed autoBed = new AutoBed();
   public static TNTAura tntAura = new TNTAura();
   public static VoidESP voidESP = new VoidESP();
   public static Tracker tracker = new Tracker();
   public static TpsSync tpsSync = new TpsSync();
   public static Spammer spammer = new Spammer();
   public static Shaders shaders = new Shaders();
   public static PingHud pingHud = new PingHud();
   public static ItemESP itemESP = new ItemESP();
   public static HoleESP holeESP = new HoleESP();
   public static GuiMove guiMove = new GuiMove();
   public static FreeCam freeCam = new FreeCam();
   public static FastUse fastUse = new FastUse();
   public static BowSpam bowSpam = new BowSpam();
   public static BoatFly boatFly = new BoatFly();
   public static Blocker blocker = new Blocker();
   public static AutoWeb autoWeb = new AutoWeb();
   public static AntiWeb antiWeb = new AntiWeb();
   public static AntiBot antiBot = new AntiBot();
   public static AntiAim antiAim = new AntiAim();
   public static AutoSex autoSex = new AutoSex();
   public static Tracers tracers = new Tracers();
   public static Parkour parkour = new Parkour();
   public static ClickTP clickTP = new ClickTP();
   public static BowPop bowPop = new BowPop();
   public static XCarry xCarry = new XCarry();
   public static Trails trails = new Trails();
   public static Strafe strafe = new Strafe();
   public static Spider spider = new Spider();
   public static NoSlow noSlow = new NoSlow();
   public static NoFall noFall = new NoFall();
   public static Hotbar hotbar = new Hotbar();
   public static HitBox hitBox = new HitBox();
   public static Flight flight = new Flight();
   public static Coords coords = new Coords();
   public static Burrow burrow = new Burrow();
   public static AutoEZ autoEZ = new AutoEZ();
   public static AimBot aimBot = new AimBot();
   public static Quiver quiver = new Quiver();
   public static NoPush noPush = new NoPush();
   public static UnHook unHook = new UnHook();
   public static Avoid avoid = new Avoid();
   public static Timer timer = new Timer();
   public static Regen regen = new Regen();
   public static Speed speed = new Speed();
   public static Reach reach = new Reach();
   public static Radar radar = new Radar();
   public static Nuker nuker = new Nuker();
   public static Media media = new Media();
   public static Ghost ghost = new Ghost();
   public static Chams chams = new Chams();
   public static Blink blink = new Blink();
   public static Phase phase = new Phase();
   public static NoBob noBob = new NoBob();
   public static Jesus jesus = new Jesus();
   public static XRay xray = new XRay();
   public static Step step = new Step();
   public static Aura aura = new Aura();
   public static FOV fov = new FOV();
   public static ESP esp = new ESP();
   public static RPC rpc = new RPC();
   public static ChunkFinder chunkFinder = new ChunkFinder();
   public static WanderingESP wanderingESP = new WanderingESP();
   public static KelpEsp kelpEsp = new KelpEsp();

   public ModuleManager() {
      Field[] var1 = this.getClass().getDeclaredFields();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         Field field = var1[var3];
         if (Module.class.isAssignableFrom(field.getType())) {
            field.setAccessible(true);

            try {
               this.modules.add((Module)field.get(this));
            } catch (IllegalAccessException var6) {
               var6.printStackTrace();
            }
         }
      }

   }

   public Module get(String name) {
      Iterator var2 = this.modules.iterator();

      Module module;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         module = (Module)var2.next();
      } while(!module.getName().equalsIgnoreCase(name));

      return module;
   }

   public ArrayList<Module> getEnabledModules() {
      ArrayList<Module> enabledModules = new ArrayList();
      Iterator var2 = this.modules.iterator();

      while(var2.hasNext()) {
         Module module = (Module)var2.next();
         if (module.isEnabled()) {
            enabledModules.add(module);
         }
      }

      return enabledModules;
   }

   public ArrayList<Module> getModulesByCategory(Module.Category category) {
      ArrayList<Module> modulesCategory = new ArrayList();
      this.modules.forEach((module) -> {
         if (module.getCategory() == category) {
            modulesCategory.add(module);
         }

      });
      return modulesCategory;
   }

   public List<Module.Category> getCategories() {
      return new ArrayList(Module.Category.values());
   }

   public void onLoad(String category) {
      try {
         TsunamiClient.EVENT_BUS.unsubscribe((Object)unHook);
      } catch (Exception var3) {
      }

      unHook.setEnabled(false);
      this.modules.sort(Comparator.comparing(Module::getName));
      this.modules.forEach((m) -> {
         if (m.isEnabled() && (m.getCategory().getName().equalsIgnoreCase(category) || category.equals("none"))) {
            TsunamiClient.EVENT_BUS.subscribe((Object)m);
         }

      });
      if (ConfigManager.firstLaunch) {
         notifications.enable();
         rpc.enable();
         soundFX.enable();
      }

   }

   public void onUpdate() {
      if (!Module.fullNullCheck()) {
         this.modules.stream().filter(Module::isEnabled).forEach(Module::onUpdate);
      }
   }

   public void onRender2D(class_332 context) {
      if (!mc.method_53526().method_53536() && !mc.field_1690.field_1842) {
         HudElement.anyHovered = false;
         this.modules.stream().filter(Module::isEnabled).forEach((module) -> {
            module.onRender2D(context);
         });
         if (!HudElement.anyHovered && !ClickGUI.anyHovered && GLFW.glfwGetPlatform() != 393219) {
            GLFW.glfwSetCursor(mc.method_22683().method_4490(), GLFW.glfwCreateStandardCursor(221185));
         }

         TsunamiClient.core.onRender2D(context);
      }
   }

   public void onRender3D(class_4587 stack) {
      this.modules.stream().filter(Module::isEnabled).forEach((module) -> {
         module.onRender3D(stack);
      });
   }

   public void sortModules() {
      this.sortedModules = (List)this.getEnabledModules().stream().filter(Module::isDrawn).sorted(Comparator.comparing((module) -> {
         return FontRenderers.getModulesRenderer().getStringWidth(module.getFullArrayString()) * -1.0F;
      })).collect(Collectors.toList());
   }

   public void onLogout() {
      this.modules.forEach(Module::onLogout);
   }

   public void onLogin() {
      this.modules.forEach(Module::onLogin);
   }

   public void onUnload(String category) {
      this.modules.forEach((module) -> {
         if (module.isEnabled() && (module.getCategory().getName().equalsIgnoreCase(category) || category.equals("none"))) {
            TsunamiClient.EVENT_BUS.unsubscribe((Object)module);
            module.setEnabled(false);
         }

      });
      this.modules.forEach(Module::onUnload);
   }

   public void onKeyPressed(int eventKey) {
      if (eventKey != -1 && eventKey != 0 && !(mc.field_1755 instanceof ClickGUI)) {
         this.modules.forEach((module) -> {
            if (module.getBind().getKey() == eventKey) {
               module.toggle();
            }

         });
      }
   }

   public void onKeyReleased(int eventKey) {
      if (eventKey != -1 && eventKey != 0 && !(mc.field_1755 instanceof ClickGUI)) {
         this.modules.forEach((module) -> {
            if (module.getBind().getKey() == eventKey && module.getBind().isHold()) {
               module.disable();
            }

         });
      }
   }

   public void onMoseKeyPressed(int eventKey) {
      if (eventKey != -1 && !(mc.field_1755 instanceof ClickGUI)) {
         this.modules.forEach((module) -> {
            if (Objects.equals(module.getBind().getBind(), "M" + eventKey)) {
               module.toggle();
            }

         });
      }
   }

   public void onMoseKeyReleased(int eventKey) {
      if (eventKey != -1 && !(mc.field_1755 instanceof ClickGUI)) {
         this.activeMouseKeys.add(eventKey);
         this.modules.forEach((module) -> {
            if (Objects.equals(module.getBind().getBind(), "M" + eventKey) && module.getBind().isHold()) {
               module.disable();
            }

         });
      }
   }

   public ArrayList<Module> getModulesSearch(String string) {
      ArrayList<Module> modulesCategory = new ArrayList();
      this.modules.forEach((module) -> {
         if (module.getName().toLowerCase().contains(string.toLowerCase())) {
            modulesCategory.add(module);
         }

      });
      return modulesCategory;
   }

   public void registerModule(Module module) {
      if (module != null) {
         this.modules.add(module);
         if (module.isEnabled()) {
            TsunamiClient.EVENT_BUS.subscribe((Object)module);
         }

      }
   }

   public void registerHudElement(HudElement hudElement) {
      if (hudElement != null) {
         this.modules.add(hudElement);
      }
   }
}
