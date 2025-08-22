package tsunami.features.modules.combat;

import com.google.common.collect.Lists;
import java.awt.Color;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import meteordevelopment.orbit.EventHandler;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.class_1268;
import net.minecraft.class_1293;
import net.minecraft.class_1294;
import net.minecraft.class_1297;
import net.minecraft.class_1303;
import net.minecraft.class_1511;
import net.minecraft.class_1657;
import net.minecraft.class_1713;
import net.minecraft.class_1743;
import net.minecraft.class_1774;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1810;
import net.minecraft.class_1821;
import net.minecraft.class_1829;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_2596;
import net.minecraft.class_2604;
import net.minecraft.class_2606;
import net.minecraft.class_2664;
import net.minecraft.class_2815;
import net.minecraft.class_2824;
import net.minecraft.class_2868;
import net.minecraft.class_2879;
import net.minecraft.class_2885;
import net.minecraft.class_3532;
import net.minecraft.class_3959;
import net.minecraft.class_3965;
import net.minecraft.class_4587;
import net.minecraft.class_239.class_240;
import net.minecraft.class_2828.class_2830;
import net.minecraft.class_3959.class_242;
import net.minecraft.class_3959.class_3960;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tsunami.core.Managers;
import tsunami.core.manager.client.ModuleManager;
import tsunami.core.manager.player.CombatManager;
import tsunami.core.manager.world.CrystalManager;
import tsunami.events.impl.EventBreakBlock;
import tsunami.events.impl.EventEntitySpawnPost;
import tsunami.events.impl.EventPostSync;
import tsunami.events.impl.EventSync;
import tsunami.events.impl.EventTick;
import tsunami.events.impl.PacketEvent;
import tsunami.events.impl.PlayerUpdateEvent;
import tsunami.features.modules.Module;
import tsunami.features.modules.client.HudEditor;
import tsunami.setting.Setting;
import tsunami.setting.impl.Bind;
import tsunami.setting.impl.BooleanSettingGroup;
import tsunami.setting.impl.ColorSetting;
import tsunami.setting.impl.SettingGroup;
import tsunami.utility.TickTimer;
import tsunami.utility.Timer;
import tsunami.utility.math.MathUtility;
import tsunami.utility.math.PredictUtility;
import tsunami.utility.player.InteractionUtility;
import tsunami.utility.player.InventoryUtility;
import tsunami.utility.player.PlayerUtility;
import tsunami.utility.player.SearchInvResult;
import tsunami.utility.render.Render2DEngine;
import tsunami.utility.render.Render3DEngine;
import tsunami.utility.render.animation.CaptureMark;
import tsunami.utility.world.ExplosionUtility;

public class AutoCrystal extends Module {
   private static final Setting<AutoCrystal.Pages> page;
   private final Setting<Boolean> await = new Setting("Await", true, (v) -> {
      return page.is(AutoCrystal.Pages.Main);
   });
   private final Setting<AutoCrystal.Timing> timing;
   private final Setting<AutoCrystal.Sequential> sequential;
   private final Setting<AutoCrystal.Rotation> rotate;
   private final Setting<BooleanSettingGroup> yawStep;
   private final Setting<Float> yawAngle;
   private final Setting<CombatManager.TargetBy> targetLogic;
   private final Setting<Float> targetRange;
   private final Setting<Integer> extrapolation;
   private final Setting<AutoCrystal.Interact> interact;
   private final Setting<Boolean> strictCenter;
   private final Setting<Boolean> rayTraceBypass;
   private final Setting<Boolean> oldVer;
   private final Setting<Integer> placeDelay;
   private final Setting<Integer> lowPlaceDelay;
   private final Setting<Float> placeRange;
   private final Setting<Float> placeWallRange;
   private final Setting<Boolean> inhibit;
   private final Setting<Integer> breakDelay;
   private final Setting<Integer> lowBreakDelay;
   private final Setting<Float> explodeRange;
   private final Setting<Float> explodeWallRange;
   private final Setting<Integer> crystalAge;
   private final Setting<Boolean> mining;
   private final Setting<Boolean> eating;
   private final Setting<Boolean> aura;
   private final Setting<Boolean> pistonAura;
   private final Setting<Boolean> surround;
   private final Setting<Boolean> middleClick;
   private final Setting<Boolean> inventory;
   private final Setting<Float> pauseHP;
   private final Setting<BooleanSettingGroup> switchPause;
   private final Setting<Integer> switchDelay;
   public final Setting<Boolean> assumeBestArmor;
   public final Setting<Float> minDamage;
   public final Setting<Float> maxSelfDamage;
   private final Setting<BooleanSettingGroup> efficiency;
   private final Setting<Float> efficiencyFactor;
   public final Setting<Boolean> protectFriends;
   private final Setting<BooleanSettingGroup> overrideSelfDamage;
   public final Setting<Boolean> sacrificeTotem;
   private final Setting<BooleanSettingGroup> armorBreaker;
   private final Setting<Float> armorScale;
   private final Setting<Float> facePlaceHp;
   private final Setting<Bind> facePlaceButton;
   public final Setting<Boolean> ignoreTerrain;
   private final Setting<Boolean> autoGapple;
   private final Setting<AutoCrystal.Switch> autoSwitch;
   private final Setting<AutoCrystal.Switch> antiWeakness;
   public final Setting<Boolean> placeFailsafe;
   public final Setting<Boolean> breakFailsafe;
   public final Setting<Integer> attempts;
   public final Setting<Boolean> idPredict;
   public final Setting<Integer> idAttacks;
   public final Setting<Boolean> idPredictOnlyDuels;
   public final Setting<Aura.ESP> targetEsp;
   public final Setting<SettingGroup> espGroup;
   public final Setting<Integer> espLength;
   public final Setting<Integer> espFactor;
   public final Setting<Float> espShaking;
   public final Setting<Float> espAmplitude;
   private final Setting<AutoCrystal.Swing> swingMode;
   private final Setting<Boolean> render;
   private final Setting<BooleanSettingGroup> renderExtrapolation;
   private final Setting<ColorSetting> extrapolationColor;
   private final Setting<BooleanSettingGroup> renderInteractVector;
   private final Setting<ColorSetting> interactColor;
   private final Setting<AutoCrystal.Render> renderMode;
   private final Setting<Boolean> rselfDamage;
   private final Setting<Boolean> drawDamage;
   private final Setting<ColorSetting> fillColor;
   private final Setting<ColorSetting> lineColor;
   private final Setting<Integer> lineWidth;
   private final Setting<Integer> slideDelay;
   private final Setting<ColorSetting> textColor;
   private final Setting<Boolean> targetName;
   private final Setting<Boolean> currentSide;
   private final Setting<Boolean> speed;
   private final Setting<Boolean> confirmInfo;
   private final Setting<Boolean> calcInfo;
   public static class_1657 target;
   private AutoCrystal.PlaceData currentData;
   private class_3965 bestPosition;
   private class_1511 bestCrystal;
   private class_1511 secondaryCrystal;
   private final TickTimer placeTimer;
   private final TickTimer breakTimer;
   private final TickTimer calcTimer;
   private final TickTimer placeSyncTimer;
   private final Timer pauseTimer;
   private final CrystalManager crystalManager;
   public float renderDamage;
   public float renderSelfDamage;
   public float rotationYaw;
   public float rotationPitch;
   private int prevCrystalsAmount;
   private int crystalSpeed;
   private int invTimer;
   private int rotationTicks;
   private boolean rotated;
   private boolean rotating;
   private boolean facePlacing;
   private boolean placedOnSpawn;
   private long confirmTime;
   private long calcTime;
   private class_2338 renderPos;
   private class_2338 prevRenderPos;
   private long renderMultiplier;
   private long currentId;
   private final Map<class_2338, Long> renderPositions;
   private AutoCrystal.RotationVec rotationVec;
   public AutoCrystal.State currentState;

   public AutoCrystal() {
      super("AutoCrystal", Module.Category.COMBAT);
      this.timing = new Setting("Timing", AutoCrystal.Timing.NORMAL, (v) -> {
         return page.is(AutoCrystal.Pages.Main);
      });
      this.sequential = new Setting("Sequential", AutoCrystal.Sequential.Strong, (v) -> {
         return page.is(AutoCrystal.Pages.Main);
      });
      this.rotate = new Setting("Rotate", AutoCrystal.Rotation.CC, (v) -> {
         return page.is(AutoCrystal.Pages.Main);
      });
      this.yawStep = new Setting("YawStep", new BooleanSettingGroup(false), (v) -> {
         return !this.rotate.is(AutoCrystal.Rotation.OFF) && page.is(AutoCrystal.Pages.Main);
      });
      this.yawAngle = (new Setting("YawAngle", 180.0F, 1.0F, 180.0F, (v) -> {
         return !this.rotate.is(AutoCrystal.Rotation.OFF) && page.is(AutoCrystal.Pages.Main);
      })).addToGroup(this.yawStep);
      this.targetLogic = new Setting("TargetLogic", CombatManager.TargetBy.Distance, (v) -> {
         return page.is(AutoCrystal.Pages.Main);
      });
      this.targetRange = new Setting("TargetRange", 10.0F, 1.0F, 15.0F, (v) -> {
         return page.is(AutoCrystal.Pages.Main);
      });
      this.extrapolation = new Setting("Extrapolation", 0, 0, 20, (v) -> {
         return page.is(AutoCrystal.Pages.Main);
      });
      this.interact = new Setting("Interact", AutoCrystal.Interact.Default, (v) -> {
         return page.is(AutoCrystal.Pages.Place);
      });
      this.strictCenter = new Setting("ССStrict", true, (v) -> {
         return page.is(AutoCrystal.Pages.Place) && this.interact.getValue() == AutoCrystal.Interact.Strict;
      });
      this.rayTraceBypass = new Setting("RayTraceBypass", false, (v) -> {
         return page.is(AutoCrystal.Pages.Place);
      });
      this.oldVer = new Setting("1.12", false, (v) -> {
         return page.is(AutoCrystal.Pages.Place);
      });
      this.placeDelay = new Setting("PlaceDelay", 0, 0, 20, (v) -> {
         return page.is(AutoCrystal.Pages.Place);
      });
      this.lowPlaceDelay = new Setting("LowPlaceDelay", 11, 0, 20, (v) -> {
         return page.is(AutoCrystal.Pages.Place);
      });
      this.placeRange = new Setting("PlaceRange", 5.0F, 1.0F, 6.0F, (v) -> {
         return page.is(AutoCrystal.Pages.Place);
      });
      this.placeWallRange = new Setting("PlaceWallRange", 3.5F, 0.0F, 6.0F, (v) -> {
         return page.is(AutoCrystal.Pages.Place);
      });
      this.inhibit = new Setting("Inhibit", true, (v) -> {
         return page.is(AutoCrystal.Pages.Break);
      });
      this.breakDelay = new Setting("BreakDelay", 0, 0, 20, (v) -> {
         return page.is(AutoCrystal.Pages.Break);
      });
      this.lowBreakDelay = new Setting("LowBreakDelay", 11, 0, 20, (v) -> {
         return page.is(AutoCrystal.Pages.Break);
      });
      this.explodeRange = new Setting("BreakRange", 5.0F, 1.0F, 6.0F, (v) -> {
         return page.is(AutoCrystal.Pages.Break);
      });
      this.explodeWallRange = new Setting("BreakWallRange", 3.5F, 0.0F, 6.0F, (v) -> {
         return page.is(AutoCrystal.Pages.Break);
      });
      this.crystalAge = new Setting("CrystalAge", 0, 0, 20, (v) -> {
         return page.is(AutoCrystal.Pages.Break);
      });
      this.mining = new Setting("Mining", true, (v) -> {
         return page.is(AutoCrystal.Pages.Pause);
      });
      this.eating = new Setting("Eating", true, (v) -> {
         return page.is(AutoCrystal.Pages.Pause);
      });
      this.aura = new Setting("Aura", false, (v) -> {
         return page.is(AutoCrystal.Pages.Pause);
      });
      this.pistonAura = new Setting("PistonAura", true, (v) -> {
         return page.is(AutoCrystal.Pages.Pause);
      });
      this.surround = new Setting("Surround", true, (v) -> {
         return page.is(AutoCrystal.Pages.Pause);
      });
      this.middleClick = new Setting("MiddleClick", true, (v) -> {
         return page.is(AutoCrystal.Pages.Pause);
      });
      this.inventory = new Setting("Inventory", false, (v) -> {
         return page.is(AutoCrystal.Pages.Pause);
      });
      this.pauseHP = new Setting("HP", 8.0F, 2.0F, 10.0F, (v) -> {
         return page.is(AutoCrystal.Pages.Pause);
      });
      this.switchPause = new Setting("SwitchPause", new BooleanSettingGroup(true), (v) -> {
         return page.is(AutoCrystal.Pages.Pause);
      });
      this.switchDelay = (new Setting("SwitchDelay", 100, 0, 1000, (v) -> {
         return page.is(AutoCrystal.Pages.Pause);
      })).addToGroup(this.switchPause);
      this.assumeBestArmor = new Setting("AssumeBestArmor", false, (v) -> {
         return page.is(AutoCrystal.Pages.Damages);
      });
      this.minDamage = new Setting("MinDamage", 6.0F, 2.0F, 20.0F, (v) -> {
         return page.is(AutoCrystal.Pages.Damages);
      });
      this.maxSelfDamage = new Setting("MaxSelfDamage", 10.0F, 2.0F, 20.0F, (v) -> {
         return page.is(AutoCrystal.Pages.Damages);
      });
      this.efficiency = new Setting("Efficiency", new BooleanSettingGroup(false), (v) -> {
         return page.is(AutoCrystal.Pages.Damages);
      });
      this.efficiencyFactor = (new Setting("EfficiencyFactor", 1.0F, 0.1F, 5.0F, (v) -> {
         return page.is(AutoCrystal.Pages.Damages);
      })).addToGroup(this.efficiency);
      this.protectFriends = new Setting("ProtectFriends", true, (v) -> {
         return page.is(AutoCrystal.Pages.Damages);
      });
      this.overrideSelfDamage = new Setting("OverrideSelfDamage", new BooleanSettingGroup(true), (v) -> {
         return page.is(AutoCrystal.Pages.Damages);
      });
      this.sacrificeTotem = (new Setting("SacrificeTotem", true, (v) -> {
         return page.is(AutoCrystal.Pages.Damages);
      })).addToGroup(this.overrideSelfDamage);
      this.armorBreaker = new Setting("ArmorBreaker", new BooleanSettingGroup(true), (v) -> {
         return page.is(AutoCrystal.Pages.Damages);
      });
      this.armorScale = (new Setting("Armor %", 5.0F, 0.0F, 40.0F, (v) -> {
         return page.is(AutoCrystal.Pages.Damages);
      })).addToGroup(this.armorBreaker);
      this.facePlaceHp = new Setting("FacePlaceHp", 5.0F, 0.0F, 20.0F, (v) -> {
         return page.is(AutoCrystal.Pages.Damages);
      });
      this.facePlaceButton = new Setting("FacePlaceBtn", new Bind(340, false, false), (v) -> {
         return page.is(AutoCrystal.Pages.Damages);
      });
      this.ignoreTerrain = new Setting("IgnoreTerrain", true, (v) -> {
         return page.is(AutoCrystal.Pages.Damages);
      });
      this.autoGapple = new Setting("AutoGapple", true, (v) -> {
         return page.is(AutoCrystal.Pages.Switch);
      });
      this.autoSwitch = new Setting("Switch", AutoCrystal.Switch.NORMAL, (v) -> {
         return page.is(AutoCrystal.Pages.Switch);
      });
      this.antiWeakness = new Setting("AntiWeakness", AutoCrystal.Switch.SILENT, (v) -> {
         return page.is(AutoCrystal.Pages.Switch);
      });
      this.placeFailsafe = new Setting("PlaceFailsafe", true, (v) -> {
         return page.is(AutoCrystal.Pages.FailSafe);
      });
      this.breakFailsafe = new Setting("BreakFailsafe", true, (v) -> {
         return page.is(AutoCrystal.Pages.FailSafe);
      });
      this.attempts = new Setting("MaxAttempts", 5, 1, 30, (v) -> {
         return page.is(AutoCrystal.Pages.FailSafe);
      });
      this.idPredict = new Setting("IDPredict", false, (v) -> {
         return page.is(AutoCrystal.Pages.IDPredict);
      });
      this.idAttacks = new Setting("IDAttacks", 3, 1, 10, (v) -> {
         return page.is(AutoCrystal.Pages.IDPredict);
      });
      this.idPredictOnlyDuels = new Setting("IDPredictOnlyDuels", false, (v) -> {
         return page.is(AutoCrystal.Pages.IDPredict);
      });
      this.targetEsp = new Setting("TargetESP", Aura.ESP.ThunderHack, (v) -> {
         return page.is(AutoCrystal.Pages.Render);
      });
      this.espGroup = new Setting("ESPSettings", new SettingGroup(false, 0), (v) -> {
         return this.targetEsp.is(Aura.ESP.ThunderHackV2) && page.is(AutoCrystal.Pages.Render);
      });
      this.espLength = (new Setting("ESPLength", 14, 1, 40, (v) -> {
         return this.targetEsp.is(Aura.ESP.ThunderHackV2) && page.is(AutoCrystal.Pages.Render);
      })).addToGroup(this.espGroup);
      this.espFactor = (new Setting("ESPFactor", 8, 1, 20, (v) -> {
         return this.targetEsp.is(Aura.ESP.ThunderHackV2) && page.is(AutoCrystal.Pages.Render);
      })).addToGroup(this.espGroup);
      this.espShaking = (new Setting("ESPShaking", 1.8F, 1.5F, 10.0F, (v) -> {
         return this.targetEsp.is(Aura.ESP.ThunderHackV2) && page.is(AutoCrystal.Pages.Render);
      })).addToGroup(this.espGroup);
      this.espAmplitude = (new Setting("ESPAmplitude", 3.0F, 0.1F, 8.0F, (v) -> {
         return this.targetEsp.is(Aura.ESP.ThunderHackV2) && page.is(AutoCrystal.Pages.Render);
      })).addToGroup(this.espGroup);
      this.swingMode = new Setting("Swing", AutoCrystal.Swing.Place, (v) -> {
         return page.is(AutoCrystal.Pages.Render);
      });
      this.render = new Setting("Render", true, (v) -> {
         return page.is(AutoCrystal.Pages.Render);
      });
      this.renderExtrapolation = new Setting("RExtrapolation", new BooleanSettingGroup(false), (v) -> {
         return page.is(AutoCrystal.Pages.Render);
      });
      this.extrapolationColor = (new Setting("ExtrapolationColor", new ColorSetting(Color.white), (v) -> {
         return page.is(AutoCrystal.Pages.Render);
      })).addToGroup(this.renderExtrapolation);
      this.renderInteractVector = new Setting("RInteractVector", new BooleanSettingGroup(true), (v) -> {
         return page.is(AutoCrystal.Pages.Render);
      });
      this.interactColor = (new Setting("InteractColor", new ColorSetting(Color.red), (v) -> {
         return page.is(AutoCrystal.Pages.Render);
      })).addToGroup(this.renderInteractVector);
      this.renderMode = new Setting("RenderMode", AutoCrystal.Render.Fade, (v) -> {
         return page.is(AutoCrystal.Pages.Render);
      });
      this.rselfDamage = new Setting("SelfDamage", true, (v) -> {
         return page.is(AutoCrystal.Pages.Render);
      });
      this.drawDamage = new Setting("RenderDamage", true, (v) -> {
         return page.is(AutoCrystal.Pages.Render);
      });
      this.fillColor = new Setting("Block Fill Color", new ColorSetting(HudEditor.getColor(0)), (v) -> {
         return page.is(AutoCrystal.Pages.Render);
      });
      this.lineColor = new Setting("Block Line Color", new ColorSetting(HudEditor.getColor(0)), (v) -> {
         return page.is(AutoCrystal.Pages.Render);
      });
      this.lineWidth = new Setting("Block Line Width", 2, 1, 10, (v) -> {
         return page.is(AutoCrystal.Pages.Render);
      });
      this.slideDelay = new Setting("Slide Delay", 200, 1, 1000, (v) -> {
         return page.is(AutoCrystal.Pages.Render);
      });
      this.textColor = new Setting("Text Color", new ColorSetting(Color.WHITE), (v) -> {
         return page.is(AutoCrystal.Pages.Render);
      });
      this.targetName = new Setting("TargetName", false, (v) -> {
         return page.is(AutoCrystal.Pages.Info);
      });
      this.currentSide = new Setting("CurrentSide", true, (v) -> {
         return page.is(AutoCrystal.Pages.Info);
      });
      this.speed = new Setting("Speed", true, (v) -> {
         return page.is(AutoCrystal.Pages.Info);
      });
      this.confirmInfo = new Setting("ConfirmTime", true, (v) -> {
         return page.is(AutoCrystal.Pages.Info);
      });
      this.calcInfo = new Setting("CalcInfo", false, (v) -> {
         return page.is(AutoCrystal.Pages.Info);
      });
      this.placeTimer = new TickTimer();
      this.breakTimer = new TickTimer();
      this.calcTimer = new TickTimer();
      this.placeSyncTimer = new TickTimer();
      this.pauseTimer = new Timer();
      this.crystalManager = new CrystalManager();
      this.renderPositions = new ConcurrentHashMap();
      this.currentState = AutoCrystal.State.NoTarget;
   }

   public void onEnable() {
      this.resetVars();
   }

   public void onDisable() {
      this.resetVars();
   }

   private void resetVars() {
      this.facePlacing = false;
      this.rotated = false;
      this.renderDamage = 0.0F;
      this.renderSelfDamage = 0.0F;
      this.renderMultiplier = 0L;
      this.confirmTime = 0L;
      this.renderPositions.clear();
      this.crystalManager.reset();
      this.breakTimer.reset();
      this.placeTimer.reset();
      this.calcTimer.reset();
      this.bestCrystal = null;
      this.bestPosition = null;
      this.currentData = null;
      this.renderPos = null;
      this.prevRenderPos = null;
      this.currentState = AutoCrystal.State.NoTarget;
      target = null;
      this.rotationVec = null;
   }

   @EventHandler
   public void onPlayerUpdate(PlayerUpdateEvent event) {
      if (!this.rotate.is(AutoCrystal.Rotation.OFF)) {
         this.calcRotations();
      }

   }

   @EventHandler
   public void onSync(EventSync e) {
      if (mc.field_1724 != null && mc.field_1687 != null) {
         target = Managers.COMBAT.getTarget((Float)this.targetRange.getValue(), (CombatManager.TargetBy)this.targetLogic.getValue());
         if (target != null && (target.method_29504() || target.method_6032() < 0.0F)) {
            target = null;
            this.currentState = AutoCrystal.State.NoTarget;
         } else {
            if (this.placeTimer.passedTicks(20L)) {
               this.renderDamage = 0.0F;
            }

            if (mc.field_1724.method_6079().method_7909() != class_1802.field_8301 && (Boolean)this.autoGapple.getValue() && mc.field_1690.field_1904.method_1434() && mc.field_1724.method_6047().method_7909() == class_1802.field_8301) {
               InventoryUtility.findItemInHotBar(class_1802.field_8367).switchTo();
            }

            if (this.invTimer++ >= 20) {
               this.crystalSpeed = MathUtility.clamp(this.prevCrystalsAmount - InventoryUtility.getItemCount(class_1802.field_8301), 0, 255);
               this.prevCrystalsAmount = InventoryUtility.getItemCount(class_1802.field_8301);
               this.invTimer = 0;
            }

            if (!this.rotate.is(AutoCrystal.Rotation.OFF) && mc.field_1724 != null && this.rotating) {
               boolean hitVisible = this.bestCrystal == null || PlayerUtility.canSee(this.bestCrystal.method_19538());
               boolean placeVisible = this.bestPosition == null || PlayerUtility.canSee(this.bestPosition.method_17784());
               if (mc.field_1724.field_6012 % 5 != 0 || !(Boolean)this.rayTraceBypass.getValue() || hitVisible && placeVisible) {
                  mc.field_1724.method_36457(this.rotationPitch);
               } else {
                  mc.field_1724.method_36457(-90.0F);
               }

               mc.field_1724.method_36456(this.rotationYaw);
            }

         }
      }
   }

   @EventHandler(
      priority = 200
   )
   public void onTick(EventTick e) {
      if (mc.field_1724 != null && mc.field_1687 != null) {
         if (mc.field_1724 != null && (!(Boolean)this.await.getValue() || this.placeTimer.passedTicks(20L) || this.calcTimer.passedTicks((long)((float)Managers.SERVER.getPing() / 25.0F)))) {
            this.calcPosition((Float)this.placeRange.getValue(), mc.field_1724.method_19538());
         }

         this.getCrystalToExplode();
         if (this.timing.is(AutoCrystal.Timing.NORMAL)) {
            this.doAction();
         }

      }
   }

   @EventHandler
   public void onPostSync(EventPostSync e) {
      if (this.timing.is(AutoCrystal.Timing.SEQUENTIAL)) {
         this.doAction();
      }

   }

   private void doAction() {
      if (!this.sequential.is(AutoCrystal.Sequential.Off) && !this.rotate.is(AutoCrystal.Rotation.MATRIX)) {
         if (this.bestCrystal != null && this.breakTimer.passedTicks((long)this.facePlacing ? (Integer)this.lowBreakDelay.getValue() : (Integer)this.breakDelay.getValue())) {
            this.attackCrystal(this.bestCrystal);
         }

         if (this.bestPosition != null && this.placeTimer.passedTicks((long)this.facePlacing ? (Integer)this.lowPlaceDelay.getValue() : (Integer)this.placeDelay.getValue()) && !this.placedOnSpawn) {
            this.placeCrystal(this.bestPosition, false, false);
         }
      } else if (this.bestCrystal != null && this.breakTimer.passedTicks((long)this.facePlacing ? (Integer)this.lowBreakDelay.getValue() : (Integer)this.breakDelay.getValue())) {
         this.attackCrystal(this.bestCrystal);
      } else if (this.bestPosition != null && this.placeTimer.passedTicks((long)this.facePlacing ? (Integer)this.lowPlaceDelay.getValue() : (Integer)this.placeDelay.getValue()) && !this.placedOnSpawn) {
         this.placeCrystal(this.bestPosition, false, false);
      }

      this.placedOnSpawn = false;
   }

   public String getDisplayInfo() {
      StringBuilder info = new StringBuilder();
      class_2350 side = this.bestPosition == null ? null : this.bestPosition.method_17780();
      if (side != null) {
         if ((Boolean)this.targetName.getValue() && target != null) {
            info.append(target.method_5477().getString()).append(" | ");
         }

         if ((Boolean)this.speed.getValue()) {
            info.append(this.crystalSpeed).append(" c/s").append(" | ");
         }

         if ((Boolean)this.currentSide.getValue()) {
            info.append(side.toString().toUpperCase()).append(" | ");
         }

         if ((Boolean)this.confirmInfo.getValue()) {
            info.append("c: ").append(this.confirmTime).append(" | ");
         }

         if ((Boolean)this.calcInfo.getValue()) {
            info.append("calc: ").append(this.calcTime).append(" | ");
         }
      }

      return info.length() < 4 ? info.toString() : info.substring(0, info.length() - 3);
   }

   @EventHandler
   public void onCrystalSpawn(@NotNull EventEntitySpawnPost e) {
      class_1297 var3 = e.getEntity();
      if (var3 instanceof class_1511) {
         class_1511 cr = (class_1511)var3;
         if (this.crystalAge.is(0)) {
            long now = System.currentTimeMillis();
            Iterator var5 = this.crystalManager.getAwaitingPositions().entrySet().iterator();

            while(var5.hasNext()) {
               Entry<class_2338, CrystalManager.Attempt> entry = (Entry)var5.next();
               class_2338 bp = (class_2338)entry.getKey();
               CrystalManager.Attempt attempt = (CrystalManager.Attempt)entry.getValue();
               if (cr.method_5707(bp.method_46558()) < 0.3D) {
                  this.confirmTime = now - attempt.getTime();
                  ModuleManager.autoCrystalInfo.onSpawn();
                  this.crystalManager.confirmSpawn(bp);
                  if (this.breakTimer.passedTicks((long)this.facePlacing ? (Integer)this.lowBreakDelay.getValue() : (Integer)this.breakDelay.getValue())) {
                     this.handleSpawn(cr);
                  }
               }
            }
         }
      }

   }

   @EventHandler
   public void onPacketReceive(PacketEvent.Receive e) {
      if (mc.field_1724 != null && mc.field_1687 != null) {
         class_2596 var3 = e.getPacket();
         if (var3 instanceof class_2606) {
            class_2606 spawn = (class_2606)var3;
            this.processSpawnPacket(spawn.method_11183());
         }

         var3 = e.getPacket();
         if (var3 instanceof class_2604) {
            class_2604 spawn = (class_2604)var3;
            this.processSpawnPacket(spawn.method_11167());
         }

         var3 = e.getPacket();
         if (var3 instanceof class_2664) {
            class_2664 explosion = (class_2664)var3;
            Iterator var8 = Lists.newArrayList(mc.field_1687.method_18112()).iterator();

            while(var8.hasNext()) {
               class_1297 ent = (class_1297)var8.next();
               if (ent instanceof class_1511) {
                  class_1511 crystal = (class_1511)ent;
                  if (crystal.method_5649(explosion.method_11475(), explosion.method_11477(), explosion.method_11478()) <= 144.0D && !this.crystalManager.isDead(crystal.method_5628())) {
                     this.crystalManager.setDead(crystal.method_5628(), System.currentTimeMillis());
                  }
               }
            }
         }

      }
   }

   @EventHandler
   public void onBlockBreakClient(EventBreakBlock e) {
      if (target != null && target.method_5707(e.getPos().method_46558()) <= 4.0D) {
         this.calcPosition(2.0F, e.getPos().method_46558());
      }

   }

   private void handleSpawn(class_1511 crystal) {
      if (mc.field_1724 != null && mc.field_1687 != null) {
         if (this.canAttackCrystal(crystal)) {
            this.attackCrystal(crystal);
            if (this.sequential.is(AutoCrystal.Sequential.Strong) && this.placeTimer.passedTicks((long)this.facePlacing ? (Integer)this.lowPlaceDelay.getValue() : (Integer)this.placeDelay.getValue())) {
               Managers.ASYNC.run(() -> {
                  this.calcPosition((Float)this.placeRange.getValue(), mc.field_1724.method_19538());
                  if (this.bestPosition != null) {
                     this.placeCrystal(this.bestPosition, false, true);
                  }

               });
            }
         }

      }
   }

   public void calcRotations() {
      if (!this.rotate.is(AutoCrystal.Rotation.OFF) && !this.shouldPause()) {
         label61: {
            if (!((AutoCrystal.Rotation)this.rotate.getValue()).needSeparate()) {
               if (this.bestPosition == null && this.bestCrystal == null) {
                  break label61;
               }
            } else if (this.rotationVec == null) {
               break label61;
            }

            if (mc.field_1724 != null) {
               if (this.rotationTicks-- < 0 && ((AutoCrystal.Rotation)this.rotate.getValue()).needSeparate()) {
                  this.rotationVec = null;
                  return;
               }

               class_243 vec = !((AutoCrystal.Rotation)this.rotate.getValue()).needSeparate() ? (this.bestPosition == null ? this.bestCrystal.method_19538() : ((AutoCrystal.Rotation)this.rotate.getValue()).getVector(this.bestPosition)) : (this.rotationVec.hitVec() == null ? this.rotationVec.vec() : ((AutoCrystal.Rotation)this.rotate.getValue()).getVector(this.rotationVec.hitVec()));
               float yawDelta = class_3532.method_15393((float)class_3532.method_15338(Math.toDegrees(Math.atan2(vec.field_1350 - mc.field_1724.method_23321(), vec.field_1352 - mc.field_1724.method_23317())) - 90.0D) - this.rotationYaw);
               float pitchDelta = (float)(-Math.toDegrees(Math.atan2(vec.field_1351 - (mc.field_1724.method_19538().field_1351 + (double)mc.field_1724.method_18381(mc.field_1724.method_18376())), Math.sqrt(Math.pow(vec.field_1352 - mc.field_1724.method_23317(), 2.0D) + Math.pow(vec.field_1350 - mc.field_1724.method_23321(), 2.0D))))) - this.rotationPitch;
               float angleToRad = (float)Math.toRadians((double)(27 * (mc.field_1724.field_6012 % 30)));
               yawDelta = (float)((double)yawDelta + Math.sin((double)angleToRad) * 3.0D) + MathUtility.random(-1.0F, 1.0F);
               pitchDelta += MathUtility.random(-0.6F, 0.6F);
               if (yawDelta > 180.0F) {
                  yawDelta -= 180.0F;
               }

               float yawStepVal = 180.0F;
               if (((BooleanSettingGroup)this.yawStep.getValue()).isEnabled()) {
                  yawStepVal = (Float)this.yawAngle.getValue();
               }

               float clampedYawDelta = class_3532.method_15363(class_3532.method_15379(yawDelta), -yawStepVal, yawStepVal);
               float clampedPitchDelta = class_3532.method_15363(pitchDelta, -45.0F, 45.0F);
               this.rotated = class_3532.method_15379(yawDelta) <= yawStepVal || !((BooleanSettingGroup)this.yawStep.getValue()).isEnabled();
               float newYaw = this.rotationYaw + (yawDelta > 0.0F ? clampedYawDelta : -clampedYawDelta);
               float newPitch = class_3532.method_15363(this.rotationPitch + clampedPitchDelta, -90.0F, 90.0F);
               double gcdFix = Math.pow((Double)mc.field_1690.method_42495().method_41753() * 0.6D + 0.2D, 3.0D) * 1.2D;
               this.rotationYaw = (float)((double)newYaw - (double)(newYaw - this.rotationYaw) % gcdFix);
               this.rotationPitch = (float)((double)newPitch - (double)(newPitch - this.rotationPitch) % gcdFix);
               ModuleManager.rotations.fixRotation = this.rotationYaw;
               this.rotating = true;
               return;
            }
         }
      }

      this.rotationYaw = mc.field_1724.method_36454();
      this.rotationPitch = mc.field_1724.method_36455();
      this.rotating = false;
   }

   public void onRender3D(class_4587 stack) {
      this.crystalManager.update();
      if (target != null) {
         switch((Aura.ESP)this.targetEsp.getValue()) {
         case CelkaPasta:
            Render3DEngine.drawOldTargetEsp(stack, target);
            break;
         case NurikZapen:
            CaptureMark.render(target);
            break;
         case ThunderHackV2:
            Render3DEngine.renderGhosts((Integer)this.espLength.getValue(), (Integer)this.espFactor.getValue(), (Float)this.espShaking.getValue(), (Float)this.espAmplitude.getValue(), target);
            break;
         case ThunderHack:
            Render3DEngine.drawTargetEsp(stack, target);
         }
      }

      if ((Boolean)this.render.getValue()) {
         Map<class_2338, Long> cache = new ConcurrentHashMap(this.renderPositions);
         cache.forEach((pos, time) -> {
            if (System.currentTimeMillis() - time > 500L) {
               this.renderPositions.remove(pos);
            }

         });
         float var10000 = MathUtility.round2((double)this.renderDamage);
         String dmg = var10000 + ((Boolean)this.rselfDamage.getValue() ? " / " + MathUtility.round2((double)this.renderSelfDamage) : "");
         if (this.renderMode.is(AutoCrystal.Render.Fade)) {
            cache.forEach((pos, time) -> {
               if (System.currentTimeMillis() - time < 500L) {
                  int alpha = (int)(100.0F * (1.0F - (float)(System.currentTimeMillis() - time) / 500.0F));
                  Render3DEngine.FILLED_QUEUE.add(new Render3DEngine.FillAction(new class_238(pos), Render2DEngine.injectAlpha(((ColorSetting)this.fillColor.getValue()).getColorObject(), alpha)));
                  Render3DEngine.OUTLINE_QUEUE.add(new Render3DEngine.OutlineAction(new class_238(pos), Render2DEngine.injectAlpha(((ColorSetting)this.lineColor.getValue()).getColorObject(), alpha), (float)(Integer)this.lineWidth.getValue()));
                  if ((Boolean)this.drawDamage.getValue()) {
                     Render3DEngine.drawTextIn3D(dmg, pos.method_46558(), 0.0D, 0.1D, 0.0D, Render2DEngine.applyOpacity(((ColorSetting)this.textColor.getValue()).getColorObject(), (float)alpha / 100.0F));
                  }
               }

            });
         } else if (this.renderMode.getValue() == AutoCrystal.Render.Slide && this.renderPos != null) {
            if (this.prevRenderPos == null) {
               this.prevRenderPos = this.renderPos;
            }

            if (this.renderPositions.isEmpty()) {
               return;
            }

            float mult = MathUtility.clamp((float)(System.currentTimeMillis() - this.renderMultiplier) / (float)(Integer)this.slideDelay.getValue(), 0.0F, 1.0F);
            class_238 interpolatedBox = Render3DEngine.interpolateBox(new class_238(this.prevRenderPos), new class_238(this.renderPos), mult);
            this.renderBox(dmg, interpolatedBox);
         } else if (this.renderPos != null) {
            if (this.renderPositions.isEmpty()) {
               return;
            }

            this.renderBox(dmg, new class_238(this.renderPos));
         }
      }

      if (target != null && ((BooleanSettingGroup)this.renderExtrapolation.getValue()).isEnabled()) {
         Render3DEngine.OUTLINE_QUEUE.add(new Render3DEngine.OutlineAction(PredictUtility.predictBox(target, (Integer)this.extrapolation.getValue()), ((ColorSetting)this.extrapolationColor.getValue()).getColorObject(), 1.0F));
      }

      if (target != null && this.bestPosition != null && ((BooleanSettingGroup)this.renderInteractVector.getValue()).isEnabled()) {
         class_243 vec = this.bestPosition.method_17784();
         class_238 b = new class_238(vec.method_10216() - 0.05D, vec.method_10214() - 0.05D, vec.method_10215() - 0.05D, vec.method_10216() + 0.05D, vec.method_10214() + 0.05D, vec.method_10215() + 0.05D);
         Render3DEngine.OUTLINE_QUEUE.add(new Render3DEngine.OutlineAction(b, ((ColorSetting)this.interactColor.getValue()).getColorObject(), 1.0F));
         Render3DEngine.FILLED_QUEUE.add(new Render3DEngine.FillAction(b, Render2DEngine.applyOpacity(((ColorSetting)this.interactColor.getValue()).getColorObject(), 0.6F)));
      }

   }

   private void renderBox(String dmg, class_238 box) {
      Render3DEngine.FILLED_QUEUE.add(new Render3DEngine.FillAction(box, ((ColorSetting)this.fillColor.getValue()).getColorObject()));
      Render3DEngine.OUTLINE_QUEUE.add(new Render3DEngine.OutlineAction(box, ((ColorSetting)this.lineColor.getValue()).getColorObject(), (float)(Integer)this.lineWidth.getValue()));
      if ((Boolean)this.drawDamage.getValue()) {
         Render3DEngine.drawTextIn3D(dmg, box.method_1005(), 0.0D, 0.1D, 0.0D, ((ColorSetting)this.textColor.getValue()).getColorObject());
      }

   }

   public boolean shouldPause() {
      if (mc.field_1724 != null && mc.field_1687 != null && mc.field_1761 != null) {
         boolean offhand = mc.field_1724.method_6079().method_7909() instanceof class_1774;
         boolean mainHand = mc.field_1724.method_6047().method_7909() instanceof class_1774;
         if (!this.pauseTimer.passedMs(1000L)) {
            this.currentState = AutoCrystal.State.ExternalPause;
            return true;
         } else if (mc.field_1761.method_2923() && !offhand && (Boolean)this.mining.getValue()) {
            this.currentState = AutoCrystal.State.Mining;
            return true;
         } else if (this.autoSwitch.is(AutoCrystal.Switch.NONE) && !offhand && !mainHand) {
            this.currentState = AutoCrystal.State.NoCrystalls;
            return true;
         } else if ((this.autoSwitch.is(AutoCrystal.Switch.SILENT) || this.autoSwitch.is(AutoCrystal.Switch.NORMAL)) && !InventoryUtility.findItemInHotBar(class_1802.field_8301).found() && !offhand) {
            this.currentState = AutoCrystal.State.NoCrystalls;
            return true;
         } else if (this.autoSwitch.is(AutoCrystal.Switch.INVENTORY) && !InventoryUtility.findItemInInventory(class_1802.field_8301).found() && !offhand) {
            this.currentState = AutoCrystal.State.NoCrystalls;
            return true;
         } else if (mc.field_1724.method_6115() && (Boolean)this.eating.getValue()) {
            this.currentState = AutoCrystal.State.Eating;
            return true;
         } else if (this.rotationMarkedDirty()) {
            this.currentState = AutoCrystal.State.ExternalPause;
            return true;
         } else if (mc.field_1724.method_6032() + mc.field_1724.method_6067() < (Float)this.pauseHP.getValue()) {
            this.currentState = AutoCrystal.State.LowHP;
            return true;
         } else if (!offhand && (Boolean)this.autoGapple.getValue() && mc.field_1690.field_1904.method_1434() && mc.field_1724.method_6047().method_7909() == class_1802.field_8367) {
            this.currentState = AutoCrystal.State.Eating;
            return true;
         } else {
            boolean silentWeakness = this.antiWeakness.getValue() == AutoCrystal.Switch.SILENT || this.antiWeakness.getValue() == AutoCrystal.Switch.INVENTORY;
            boolean silent = this.autoSwitch.getValue() == AutoCrystal.Switch.SILENT || this.autoSwitch.getValue() == AutoCrystal.Switch.INVENTORY;
            boolean switchPause1 = ((BooleanSettingGroup)this.switchPause.getValue()).isEnabled() && !Managers.PLAYER.switchTimer.passedMs((long)(Integer)this.switchDelay.getValue()) && !silent && !silentWeakness;
            if (!switchPause1) {
               this.currentState = AutoCrystal.State.Active;
            }

            return switchPause1;
         }
      } else {
         return true;
      }
   }

   public boolean rotationMarkedDirty() {
      if (ModuleManager.surround.isEnabled() && !ModuleManager.surround.inactivityTimer.passedMs(500L) && (Boolean)this.surround.getValue()) {
         return true;
      } else if (ModuleManager.middleClick.isEnabled() && mc.field_1690.field_1871.method_1434() && (Boolean)this.middleClick.getValue()) {
         return true;
      } else if ((Boolean)this.inventory.getValue() && Managers.PLAYER.inInventory) {
         return true;
      } else if (ModuleManager.autoTrap.isEnabled() && !ModuleManager.surround.inactivityTimer.passedMs(500L)) {
         return true;
      } else if (ModuleManager.blocker.isEnabled() && !ModuleManager.surround.inactivityTimer.passedMs(500L)) {
         return true;
      } else if (ModuleManager.holeFill.isEnabled() && !HoleFill.inactivityTimer.passedMs(500L)) {
         return true;
      } else if (ModuleManager.aura.isEnabled() && Aura.target != null && (Boolean)this.aura.getValue()) {
         return true;
      } else {
         return ModuleManager.pistonAura.isEnabled() && (Boolean)this.pistonAura.getValue();
      }
   }

   public void attackCrystal(class_1511 crystal) {
      if (mc.field_1724 != null && mc.field_1687 != null && mc.field_1761 != null && crystal != null && (!this.crystalManager.isDead(crystal.method_5628()) || !(Boolean)this.inhibit.getValue())) {
         if (!this.shouldPause() && target != null) {
            class_1293 weaknessEffect = mc.field_1724.method_6112(class_1294.field_5911);
            class_1293 strengthEffect = mc.field_1724.method_6112(class_1294.field_5910);
            int prevSlot = -1;
            SearchInvResult antiWeaknessResult = InventoryUtility.getAntiWeaknessItem();
            SearchInvResult antiWeaknessResultInv = InventoryUtility.findInInventory((itemStack) -> {
               return itemStack.method_7909() instanceof class_1829 || itemStack.method_7909() instanceof class_1810 || itemStack.method_7909() instanceof class_1743 || itemStack.method_7909() instanceof class_1821;
            });
            if (this.antiWeakness.getValue() != AutoCrystal.Switch.NONE && weaknessEffect != null && (strengthEffect == null || strengthEffect.method_5578() < weaknessEffect.method_5578())) {
               prevSlot = this.switchTo(antiWeaknessResult, antiWeaknessResultInv, this.antiWeakness);
            }

            this.sendPacket(class_2824.method_34206(crystal, mc.field_1724.method_5715()));
            this.swingHand(false, true);
            if (((AutoCrystal.Rotation)this.rotate.getValue()).needSeparate() && !Managers.PLAYER.checkRtx(this.rotationYaw, this.rotationPitch, (Float)this.explodeRange.getValue(), (Float)this.explodeWallRange.getValue(), (class_1297)crystal)) {
               this.rotationVec = new AutoCrystal.RotationVec(crystal.method_5829().method_1005(), (class_3965)null, false);
            }

            this.breakTimer.reset();
            this.crystalManager.onAttack(crystal);
            this.rotationTicks = 10;
            Iterator var7 = Lists.newArrayList(mc.field_1687.method_18112()).iterator();

            while(var7.hasNext()) {
               class_1297 ent = (class_1297)var7.next();
               if (ent instanceof class_1511) {
                  class_1511 exCrystal = (class_1511)ent;
                  if (exCrystal.method_5649(crystal.method_23317(), crystal.method_23318(), crystal.method_23321()) <= 144.0D && !this.crystalManager.isDead(exCrystal.method_5628())) {
                     this.crystalManager.setDead(exCrystal.method_5628(), System.currentTimeMillis());
                  }
               }
            }

            if (prevSlot != -1) {
               if (this.antiWeakness.getValue() == AutoCrystal.Switch.SILENT) {
                  mc.field_1724.method_31548().field_7545 = prevSlot;
                  this.sendPacket(new class_2868(prevSlot));
               }

               if (this.antiWeakness.getValue() == AutoCrystal.Switch.INVENTORY) {
                  mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, prevSlot, mc.field_1724.method_31548().field_7545, class_1713.field_7791, mc.field_1724);
                  this.sendPacket(new class_2815(mc.field_1724.field_7512.field_7763));
               }
            }

         }
      }
   }

   private boolean canAttackCrystal(class_1511 cr) {
      if ((Integer)this.crystalAge.getValue() != 0 && cr.field_6012 < (Integer)this.crystalAge.getValue()) {
         return false;
      } else if (PlayerUtility.squaredDistanceFromEyes(cr.method_5829().method_1005()) > (InteractionUtility.canSee((class_1297)cr) ? this.explodeRange.getPow2Value() : this.explodeWallRange.getPow2Value())) {
         return false;
      } else if (!cr.method_5805()) {
         return false;
      } else {
         float damage = ExplosionUtility.getAutoCrystalDamage(cr.method_19538(), target, this.getPredictTicks(), false);
         float selfDamage = ExplosionUtility.getSelfExplosionDamage(cr.method_19538(), this.getSelfPredictTicks(), false);
         boolean overrideDamage = this.shouldOverrideMaxSelfDmg(damage, selfDamage);
         if ((Boolean)this.protectFriends.getValue()) {
            List<class_1657> players = Lists.newArrayList(mc.field_1687.method_18456());
            Iterator var6 = players.iterator();

            while(var6.hasNext()) {
               class_1657 pl = (class_1657)var6.next();
               if (Managers.FRIEND.isFriend(pl)) {
                  float fdamage = ExplosionUtility.getAutoCrystalDamage(cr.method_19538(), pl, this.getPredictTicks(), false);
                  if (fdamage > selfDamage) {
                     selfDamage = fdamage;
                  }
               }
            }
         }

         return !(selfDamage > (Float)this.maxSelfDamage.getValue()) || overrideDamage;
      }
   }

   private int switchTo(SearchInvResult result, SearchInvResult resultInv, @NotNull Setting<AutoCrystal.Switch> switchMode) {
      if (mc.field_1724 != null && mc.field_1687 != null && mc.field_1761 != null) {
         int prevSlot = mc.field_1724.method_31548().field_7545;
         switch(((AutoCrystal.Switch)switchMode.getValue()).ordinal()) {
         case 0:
         default:
            break;
         case 1:
            result.switchTo();
            break;
         case 2:
            result.switchToSilent();
            break;
         case 3:
            if (resultInv.found()) {
               prevSlot = resultInv.slot();
               mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, prevSlot, mc.field_1724.method_31548().field_7545, class_1713.field_7791, mc.field_1724);
               this.sendPacket(new class_2815(mc.field_1724.field_7512.field_7763));
            }
         }

         return prevSlot;
      } else {
         return -1;
      }
   }

   public void placeCrystal(class_3965 bhr, boolean packetRotate, boolean onSpawn) {
      if (!this.shouldPause() && mc.field_1724 != null && bhr != null) {
         int prevSlot = -1;
         SearchInvResult crystalResult = InventoryUtility.findItemInHotBar(class_1802.field_8301);
         SearchInvResult crystalResultInv = InventoryUtility.findItemInInventory(class_1802.field_8301);
         boolean offhand = mc.field_1724.method_6079().method_7909() instanceof class_1774;
         boolean holdingCrystal = mc.field_1724.method_6047().method_7909() instanceof class_1774 || offhand;
         if (!this.rotate.is(AutoCrystal.Rotation.OFF)) {
            this.rotationVec = new AutoCrystal.RotationVec(bhr.method_17784(), bhr, true);
            if (packetRotate) {
               float[] angle = InteractionUtility.calculateAngle(bhr.method_17784());
               this.sendPacket(new class_2830(mc.field_1724.method_23317(), mc.field_1724.method_23318(), mc.field_1724.method_23321(), angle[0], angle[1], mc.field_1724.method_24828()));
            } else if (!this.rotated && !((AutoCrystal.Rotation)this.rotate.getValue()).needSeparate()) {
               return;
            }
         }

         if (!this.isPositionBlockedByEntity(bhr.method_17777(), false)) {
            if (this.autoSwitch.getValue() != AutoCrystal.Switch.NONE && !holdingCrystal) {
               prevSlot = this.switchTo(crystalResult, crystalResultInv, this.autoSwitch);
            }

            if (mc.field_1724.method_6047().method_7909() instanceof class_1774 || offhand || this.autoSwitch.getValue() == AutoCrystal.Switch.SILENT) {
               this.sendSequencedPacket((id) -> {
                  return new class_2885(offhand ? class_1268.field_5810 : class_1268.field_5808, bhr, id);
               });
               this.swingHand(offhand, false);
               if (this.breakTimer.passedTicks((long)(Integer)this.breakDelay.getValue()) && (Boolean)this.idPredict.getValue() && (!(Boolean)this.idPredictOnlyDuels.getValue() || mc.field_1687.method_8621().method_11965() == 100.0D)) {
                  this.predictAttack();
               }

               this.placeTimer.reset();
               this.rotationTicks = 10;
               if (!bhr.method_17777().equals(this.renderPos)) {
                  this.renderMultiplier = System.currentTimeMillis();
                  this.prevRenderPos = this.renderPos;
                  this.renderPos = bhr.method_17777();
               }

               this.crystalManager.addAwaitingPos(bhr.method_17777());
               this.renderPositions.put(bhr.method_17777(), System.currentTimeMillis());
               this.postPlaceSwitch(prevSlot);
               if (onSpawn) {
                  this.placedOnSpawn = true;
                  this.placeSyncTimer.reset();
               }

            }
         }
      }
   }

   private void postPlaceSwitch(int slot) {
      if (mc.field_1724 != null && mc.field_1687 != null && mc.field_1761 != null) {
         if (this.autoSwitch.getValue() == AutoCrystal.Switch.SILENT && slot != -1) {
            mc.field_1724.method_31548().field_7545 = slot;
            this.sendPacket(new class_2868(slot));
         }

         if (this.autoSwitch.getValue() == AutoCrystal.Switch.INVENTORY && slot != -1) {
            mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, slot, mc.field_1724.method_31548().field_7545, class_1713.field_7791, mc.field_1724);
            this.sendPacket(new class_2815(mc.field_1724.field_7512.field_7763));
         }

      }
   }

   public void calcPosition(float range, class_243 center) {
      if (mc.field_1724 != null && mc.field_1687 != null) {
         long currentTime = System.currentTimeMillis();
         this.calcTimer.reset();
         if (target == null) {
            this.renderPos = null;
            this.prevRenderPos = null;
            this.bestPosition = null;
            this.currentData = null;
         } else {
            List<AutoCrystal.PlaceData> list = this.getPossibleBlocks(target, center, range).stream().filter((data) -> {
               return this.isSafe(data.damage, data.selfDamage, data.overrideDamage);
            }).toList();
            this.bestPosition = list.isEmpty() ? null : this.filterPositions(list);
            this.calcTime = System.currentTimeMillis() - currentTime;
         }
      }
   }

   @NotNull
   private List<AutoCrystal.PlaceData> getPossibleBlocks(class_1657 target, class_243 center, float range) {
      List<AutoCrystal.PlaceData> blocks = new ArrayList();
      class_2338 playerPos = class_2338.method_49638(center);
      class_243 predictedPlayerPos = PredictUtility.predictPosition(mc.field_1724, this.getSelfPredictTicks());
      int r = (int)Math.ceil((double)range);

      for(int x = playerPos.method_10263() - r; x <= playerPos.method_10263() + r; ++x) {
         for(int y = playerPos.method_10264() - r; y <= playerPos.method_10264() + r; ++y) {
            for(int z = playerPos.method_10260() - r; z <= playerPos.method_10260() + r; ++z) {
               AutoCrystal.PlaceData data = this.getPlaceData(new class_2338(x, y, z), target, predictedPlayerPos);
               if (data != null) {
                  blocks.add(data);
               }
            }
         }
      }

      return blocks;
   }

   @NotNull
   private List<AutoCrystal.CrystalData> getPossibleCrystals(class_1657 target) {
      List<AutoCrystal.CrystalData> crystals = new ArrayList();
      if (mc.field_1724 != null && mc.field_1687 != null) {
         Iterable<class_1297> entities = Lists.newArrayList(mc.field_1687.method_18112());
         Iterator var4 = entities.iterator();

         while(true) {
            class_1297 ent;
            float damage;
            float selfDamage;
            boolean overrideDamage;
            do {
               do {
                  class_1511 cr;
                  do {
                     do {
                        do {
                           do {
                              do {
                                 do {
                                    if (!var4.hasNext()) {
                                       return crystals;
                                    }

                                    ent = (class_1297)var4.next();
                                 } while(!(ent instanceof class_1511));

                                 cr = (class_1511)ent;
                              } while(this.crystalManager.isBlocked(cr.method_5628()));
                           } while((Integer)this.crystalAge.getValue() != 0 && cr.field_6012 < (Integer)this.crystalAge.getValue());
                        } while(this.crystalManager.isDead(cr.method_5628()) && (Boolean)this.inhibit.getValue());
                     } while(PlayerUtility.squaredDistanceFromEyes(ent.method_5829().method_1005()) > (InteractionUtility.canSee((class_1297)cr) ? this.explodeRange.getPow2Value() : this.explodeWallRange.getPow2Value()));
                  } while(!ent.method_5805());

                  damage = ExplosionUtility.getAutoCrystalDamage(ent.method_19538(), target, this.getPredictTicks(), false);
                  selfDamage = ExplosionUtility.getSelfExplosionDamage(ent.method_19538(), this.getSelfPredictTicks(), false);
                  overrideDamage = this.shouldOverrideMaxSelfDmg(damage, selfDamage);
                  if ((Boolean)this.protectFriends.getValue()) {
                     List<class_1657> players = Lists.newArrayList(mc.field_1687.method_18456());
                     Iterator var11 = players.iterator();

                     while(var11.hasNext()) {
                        class_1657 pl = (class_1657)var11.next();
                        if (Managers.FRIEND.isFriend(pl)) {
                           float fdamage = ExplosionUtility.getAutoCrystalDamage(ent.method_19538(), pl, this.getPredictTicks(), false);
                           if (fdamage > selfDamage) {
                              selfDamage = fdamage;
                           }
                        }
                     }
                  }
               } while(damage < 1.5F);
            } while(selfDamage > (Float)this.maxSelfDamage.getValue() && !overrideDamage);

            crystals.add(new AutoCrystal.CrystalData((class_1511)ent, damage, selfDamage, overrideDamage));
         }
      } else {
         return crystals;
      }
   }

   public void getCrystalToExplode() {
      if (target == null) {
         this.bestCrystal = null;
      } else if (this.secondaryCrystal != null) {
         if (this.canAttackCrystal(this.secondaryCrystal)) {
            this.bestCrystal = this.secondaryCrystal;
            this.debug("secondary crystal accepted");
         } else {
            this.debug("secondary crystal declined");
         }

         this.secondaryCrystal = null;
      } else {
         List<AutoCrystal.CrystalData> list = this.getPossibleCrystals(target).stream().filter((data) -> {
            return this.isSafe(data.damage, data.selfDamage, data.overrideDamage);
         }).toList();
         this.bestCrystal = list.isEmpty() ? null : this.filterCrystals(list);
      }
   }

   public boolean isSafe(float damage, float selfDamage, boolean overrideDamage) {
      if (mc.field_1724 != null && mc.field_1687 != null) {
         if (overrideDamage) {
            return true;
         } else if ((double)selfDamage + 0.5D > (double)(mc.field_1724.method_6032() + mc.field_1724.method_6067())) {
            return false;
         } else if (((BooleanSettingGroup)this.efficiency.getValue()).isEnabled()) {
            return damage / selfDamage >= (Float)this.efficiencyFactor.getValue();
         } else {
            return true;
         }
      } else {
         return false;
      }
   }

   @Nullable
   private class_3965 filterPositions(@NotNull List<AutoCrystal.PlaceData> clearedList) {
      AutoCrystal.PlaceData bestData = null;
      float bestVal = 0.0F;
      Iterator var4 = clearedList.iterator();

      while(true) {
         while(true) {
            AutoCrystal.PlaceData data;
            do {
               do {
                  if (!var4.hasNext()) {
                     if (bestData == null) {
                        return null;
                     }

                     this.facePlacing = bestData.damage < (Float)this.minDamage.getValue();
                     this.renderDamage = bestData.damage;
                     this.renderSelfDamage = bestData.selfDamage;
                     this.currentData = bestData;
                     return bestData.bhr;
                  }

                  data = (AutoCrystal.PlaceData)var4.next();
               } while(!this.shouldOverrideMinDmg(data.damage) && !(data.damage > (Float)this.minDamage.getValue()));
            } while(bestData != null && data.overrideDamage && target.method_6067() + target.method_6032() < bestData.damage && bestData.selfDamage < data.selfDamage);

            boolean shouldStopOverride = bestData != null && bestData.overrideDamage && data.damage > target.method_6032() + target.method_6067() && data.selfDamage < bestData.selfDamage;
            float safetyComparatorDelta = shouldStopOverride ? 10.0F : 1.0F;
            if (bestData != null && Math.abs(bestData.damage - data.damage) < safetyComparatorDelta && Math.abs(bestData.selfDamage - data.selfDamage) > 1.0F) {
               if (bestData.selfDamage >= data.selfDamage) {
                  bestData = data;
                  bestVal = data.damage;
               }
            } else if (bestVal < data.damage) {
               bestData = data;
               bestVal = data.damage;
            }
         }
      }
   }

   @Nullable
   private class_1511 filterCrystals(@NotNull List<AutoCrystal.CrystalData> clearedList) {
      AutoCrystal.CrystalData bestData = null;
      float bestVal = 0.0F;
      Iterator var4 = clearedList.iterator();

      while(true) {
         while(true) {
            AutoCrystal.CrystalData data;
            do {
               do {
                  if (!var4.hasNext()) {
                     if (bestData == null) {
                        return null;
                     }

                     this.renderDamage = bestData.damage();
                     this.renderSelfDamage = bestData.selfDamage();
                     return bestData.crystal;
                  }

                  data = (AutoCrystal.CrystalData)var4.next();
               } while(!this.shouldOverrideMinDmg(data.damage) && !(data.damage > (Float)this.minDamage.getValue()));
            } while(bestData != null && data.overrideDamage && target.method_6067() + target.method_6032() < bestData.damage && bestData.selfDamage < data.selfDamage);

            boolean shouldStopOverride = bestData != null && bestData.overrideDamage && data.damage > target.method_6032() + target.method_6067() && data.selfDamage < bestData.selfDamage;
            float safetyComparatorDelta = shouldStopOverride ? 10.0F : 1.0F;
            if (bestData != null && Math.abs(bestData.damage - data.damage) < safetyComparatorDelta && Math.abs(bestData.selfDamage - data.selfDamage) > 1.0F) {
               if (bestData.selfDamage >= data.selfDamage) {
                  bestData = data;
                  bestVal = data.damage;
               }
            } else if (bestVal < data.damage) {
               bestData = data;
               bestVal = data.damage;
            }
         }
      }
   }

   @Nullable
   public AutoCrystal.PlaceData getPlaceData(class_2338 bp, class_1657 target, class_243 predictedPlayerPos) {
      if (mc.field_1724 != null && mc.field_1687 != null) {
         if (this.crystalManager.isPositionBlocked(bp)) {
            return null;
         } else if (!this.predictCrystalSpawn(bp, predictedPlayerPos)) {
            return null;
         } else if (target != null && target.method_19538().method_1025(bp.method_46558().method_1031(0.0D, 0.5D, 0.0D)) > 144.0D) {
            return null;
         } else {
            class_2248 base = mc.field_1687.method_8320(bp).method_26204();
            if (base != class_2246.field_10540 && base != class_2246.field_9987) {
               return null;
            } else {
               boolean freeSpace = mc.field_1687.method_22347(bp.method_10084());
               if (!freeSpace || (Boolean)this.oldVer.getValue() && !mc.field_1687.method_22347(bp.method_10084().method_10084())) {
                  return null;
               } else if (this.isPositionBlockedByEntity(bp, true)) {
                  return null;
               } else {
                  class_243 crystalVec = new class_243((double)(0.5F + (float)bp.method_10263()), (double)(1.0F + (float)bp.method_10264()), (double)(0.5F + (float)bp.method_10260()));
                  float damage = target == null ? 10.0F : ExplosionUtility.getAutoCrystalDamage(crystalVec, target, this.getPredictTicks(), false);
                  if (damage < 1.5F) {
                     return null;
                  } else {
                     float selfDamage = ExplosionUtility.getSelfExplosionDamage(crystalVec, this.getSelfPredictTicks(), false);
                     boolean overrideDamage = this.shouldOverrideMaxSelfDmg(damage, selfDamage);
                     if ((Boolean)this.protectFriends.getValue()) {
                        List<class_1657> players = Lists.newArrayList(mc.field_1687.method_18456());
                        Iterator var11 = players.iterator();

                        while(var11.hasNext()) {
                           class_1657 pl = (class_1657)var11.next();
                           if (Managers.FRIEND.isFriend(pl)) {
                              float fdamage = ExplosionUtility.getAutoCrystalDamage(crystalVec, pl, this.getPredictTicks(), false);
                              if (fdamage > selfDamage) {
                                 selfDamage = fdamage;
                              }
                           }
                        }
                     }

                     if (selfDamage > (Float)this.maxSelfDamage.getValue() && !overrideDamage) {
                        return null;
                     } else {
                        class_3965 interactResult = this.getInteractResult(bp, crystalVec);
                        return interactResult == null ? null : new AutoCrystal.PlaceData(interactResult, damage, selfDamage, overrideDamage);
                     }
                  }
               }
            }
         }
      } else {
         return null;
      }
   }

   public boolean predictCrystalSpawn(class_2338 bp, class_243 predictedPlayerPos) {
      class_243 predictedPos = bp.method_46558().method_1031(0.0D, 1.5D, 0.0D);
      float distance = (float)predictedPlayerPos.method_1031(0.0D, (double)mc.field_1724.method_18381(mc.field_1724.method_18376()), 0.0D).method_1025(predictedPos);
      if (InteractionUtility.canSee(predictedPos)) {
         return distance <= this.explodeRange.getPow2Value();
      } else {
         return distance <= this.explodeWallRange.getPow2Value();
      }
   }

   public class_3965 getInteractResult(class_2338 bp, class_243 crystalVec) {
      class_3965 interactResult = null;
      switch(((AutoCrystal.Interact)this.interact.getValue()).ordinal()) {
      case 0:
         interactResult = this.getDefaultInteract(crystalVec, bp);
         break;
      case 1:
         interactResult = this.getStrictInteract(bp);
      }

      return interactResult;
   }

   public boolean isPositionBlockedByEntity(@NotNull class_2338 base, boolean calcPhase) {
      if (mc.field_1724 != null && mc.field_1687 != null) {
         class_238 box = new class_238(base.method_10084());
         if (mc.method_1562() == null || mc.method_1562().method_45734() == null || !mc.method_1562().method_45734().field_3761.contains("crystalpvp.cc")) {
            box = box.method_1009(0.0D, 1.0D, 0.0D);
         }

         Iterator var4 = Lists.newArrayList(mc.field_1687.method_18112()).iterator();

         while(true) {
            class_1297 ent;
            do {
               do {
                  do {
                     do {
                        if (!var4.hasNext()) {
                           return false;
                        }

                        ent = (class_1297)var4.next();
                     } while(ent == null);
                  } while(!ent.method_5829().method_994(box));
               } while(ent instanceof class_1303);
            } while(ModuleManager.speedMine.isBlockDrop(ent));

            if (!(ent instanceof class_1511)) {
               break;
            }

            class_1511 cr = (class_1511)ent;
            if (!this.crystalManager.isDead(cr.method_5628())) {
               if (this.crystalManager.isBlocked(cr.method_5628())) {
                  return true;
               }

               if (calcPhase) {
                  if (this.canAttackCrystal(cr)) {
                     continue;
                  }
                  break;
               }

               if (cr.method_19538().method_1025(box.method_1005()) > 0.3D) {
                  this.secondaryCrystal = cr;
                  this.debug("secondary crystal created");
               }
               break;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   public boolean isPositionBlockedByCrystal(@NotNull class_2338 base) {
      class_238 box = new class_238(base.method_10084());
      return Lists.newArrayList(mc.field_1687.method_18112()).stream().anyMatch((ent) -> {
         return ent != null && ent.method_5829().method_994(box) && ent instanceof class_1511;
      });
   }

   public boolean shouldOverrideMaxSelfDmg(float damage, float selfDamage) {
      if (((BooleanSettingGroup)this.overrideSelfDamage.getValue()).isEnabled() && target != null) {
         if (mc.field_1724 != null && mc.field_1687 != null) {
            boolean targetSafe = target.method_6079().method_7909() == class_1802.field_8288 || target.method_6047().method_7909() == class_1802.field_8288;
            boolean playerSafe = mc.field_1724.method_6079().method_7909() == class_1802.field_8288 || mc.field_1724.method_6047().method_7909() == class_1802.field_8288;
            float targetHp = target.method_6032() + target.method_6067();
            float playerHp = mc.field_1724.method_6032() + mc.field_1724.method_6067();
            boolean canPop = damage > targetHp && targetSafe;
            boolean canKill = damage > targetHp && !targetSafe;
            boolean canPopSelf = selfDamage > playerHp && playerSafe;
            boolean canKillSelf = selfDamage > playerHp && !playerSafe;
            if (canPopSelf && canKill && (Boolean)this.sacrificeTotem.getValue()) {
               return true;
            } else {
               return selfDamage > (Float)this.maxSelfDamage.getValue() && (canPop || canKill) && !canKillSelf && !canPopSelf;
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public boolean shouldOverrideMinDmg(float damage) {
      if (target == null) {
         return false;
      } else if (this.isKeyPressed(this.facePlaceButton)) {
         return true;
      } else if (target.method_6032() + target.method_6067() - damage < 0.0F) {
         return true;
      } else {
         if (((BooleanSettingGroup)this.armorBreaker.getValue()).isEnabled()) {
            Iterator var2 = target.method_5661().iterator();

            while(var2.hasNext()) {
               class_1799 armor = (class_1799)var2.next();
               if (armor != null && !armor.method_7909().equals(class_1802.field_8162) && (float)(armor.method_7936() - armor.method_7919()) / (float)armor.method_7936() * 100.0F < (Float)this.armorScale.getValue()) {
                  return true;
               }
            }
         }

         return target.method_6032() + target.method_6067() <= (Float)this.facePlaceHp.getValue();
      }
   }

   @Nullable
   private class_3965 getDefaultInteract(class_243 crystalVector, class_2338 bp) {
      if (mc.field_1724 != null && mc.field_1687 != null) {
         if (PlayerUtility.squaredDistanceFromEyes(crystalVector) > this.placeRange.getPow2Value()) {
            return null;
         } else {
            class_3965 wallCheck = mc.field_1687.method_17742(new class_3959(InteractionUtility.getEyesPos(mc.field_1724), crystalVector, class_3960.field_17558, class_242.field_1348, mc.field_1724));
            return wallCheck != null && wallCheck.method_17783() == class_240.field_1332 && wallCheck.method_17777() != bp && PlayerUtility.squaredDistanceFromEyes(crystalVector) > this.placeWallRange.getPow2Value() ? null : new class_3965(crystalVector, mc.field_1687.method_24794(bp.method_10084()) ? class_2350.field_11036 : class_2350.field_11033, bp, false);
         }
      } else {
         return null;
      }
   }

   @Nullable
   public class_3965 getStrictInteract(@NotNull class_2338 bp) {
      if (mc.field_1724 != null && mc.field_1687 != null) {
         float bestDistance = Float.MAX_VALUE;
         class_2350 bestDirection = null;
         float upPoint = (Boolean)this.strictCenter.getValue() ? (float)bp.method_46558().method_10214() : (float)bp.method_10084().method_10264();
         if (mc.field_1724.method_33571().method_10214() > (double)upPoint) {
            bestDirection = class_2350.field_11036;
         } else if (mc.field_1724.method_33571().method_10214() < (double)bp.method_10264() && mc.field_1687.method_22347(bp.method_10074())) {
            bestDirection = class_2350.field_11033;
         } else {
            Iterator var5 = InteractionUtility.getStrictBlockDirections(bp).iterator();

            while(var5.hasNext()) {
               class_2350 dir = (class_2350)var5.next();
               if (dir != class_2350.field_11036 && dir != class_2350.field_11033) {
                  class_243 directionVec = new class_243((double)bp.method_10263() + 0.5D + (double)dir.method_10163().method_10263() * 0.5D, (double)bp.method_10264() + 0.99D, (double)bp.method_10260() + 0.5D + (double)dir.method_10163().method_10260() * 0.5D);
                  if (mc.field_1687.method_8320(bp.method_10093(dir)).method_45474()) {
                     float distance = PlayerUtility.squaredDistanceFromEyes(directionVec);
                     if (bestDistance > distance) {
                        bestDirection = dir;
                        bestDistance = distance;
                     }
                  }
               }
            }
         }

         if (bestDirection == null) {
            return null;
         } else {
            class_243 vec = InteractionUtility.getVisibleDirectionPoint(bestDirection, bp, (Float)this.placeWallRange.getValue(), (Float)this.placeRange.getValue());
            return vec == null ? null : new class_3965(vec, bestDirection, bp, false);
         }
      } else {
         return null;
      }
   }

   private void swingHand(boolean offHand, boolean attack) {
      switch(((AutoCrystal.Swing)this.swingMode.getValue()).ordinal()) {
      case 0:
         mc.field_1724.method_6104(offHand ? class_1268.field_5810 : class_1268.field_5808);
         break;
      case 1:
         if (!attack) {
            mc.field_1724.method_6104(offHand ? class_1268.field_5810 : class_1268.field_5808);
         } else {
            this.sendPacket(new class_2879(offHand ? class_1268.field_5810 : class_1268.field_5808));
         }
         break;
      case 2:
         if (attack) {
            mc.field_1724.method_6104(class_1268.field_5808);
         } else {
            this.sendPacket(new class_2879(class_1268.field_5808));
         }
      }

   }

   public void predictAttack() {
      for(int i = 1; i <= (Integer)this.idAttacks.getValue(); ++i) {
         int id = (int)(this.currentId + (long)i);
         class_1297 entity = mc.field_1687.method_8469(id);
         if (entity == null || entity instanceof class_1511) {
            class_2824 attackPacket = class_2824.method_34206(mc.field_1724, mc.field_1724.method_5715());
            changeId(attackPacket, id);
            this.sendPacket(attackPacket);
            this.sendPacket(new class_2879(class_1268.field_5808));
         }
      }

   }

   public static void changeId(class_2824 packet, int id) {
      try {
         Field field = class_2824.class.getDeclaredField(FabricLoader.getInstance().isDevelopmentEnvironment() ? "entityId" : "field_12870");
         field.setAccessible(true);
         field.setInt(packet, id);
      } catch (Exception var3) {
      }

   }

   public void processSpawnPacket(int id) {
      if ((long)id > this.currentId) {
         this.currentId = (long)id;
      }

   }

   public AutoCrystal.PlaceData getCurrentData() {
      return this.currentData;
   }

   public long getCalcTime() {
      return this.calcTime;
   }

   public void pause() {
      this.pauseTimer.reset();
   }

   private int getPredictTicks() {
      return (Integer)this.extrapolation.getValue();
   }

   private int getSelfPredictTicks() {
      return (int)Math.ceil((double)((float)Managers.SERVER.getPing() * 1.5F / 50.0F));
   }

   public String getPauseState() {
      return this.currentState.name();
   }

   static {
      page = new Setting("Page", AutoCrystal.Pages.Main);
   }

   private static enum Timing {
      NORMAL,
      SEQUENTIAL;

      // $FF: synthetic method
      private static AutoCrystal.Timing[] $values() {
         return new AutoCrystal.Timing[]{NORMAL, SEQUENTIAL};
      }
   }

   public static enum Sequential {
      Off,
      Strict,
      Strong;

      // $FF: synthetic method
      private static AutoCrystal.Sequential[] $values() {
         return new AutoCrystal.Sequential[]{Off, Strict, Strong};
      }
   }

   public static enum Rotation {
      OFF(false, false),
      CC(false, true),
      NCP(false, false),
      GRIM(false, true),
      MATRIX(true, true);

      final boolean needSeparate;
      final boolean vecMod;

      private Rotation(boolean needSeparate, boolean vecMod) {
         this.needSeparate = needSeparate;
         this.vecMod = vecMod;
      }

      public class_243 getVector(class_3965 vec) {
         return this.vecMod ? vec.method_17777().method_46558().method_1031(0.0D, 0.475D, 0.0D) : vec.method_17784();
      }

      public boolean needSeparate() {
         return this.needSeparate;
      }

      // $FF: synthetic method
      private static AutoCrystal.Rotation[] $values() {
         return new AutoCrystal.Rotation[]{OFF, CC, NCP, GRIM, MATRIX};
      }
   }

   private static enum Interact {
      Default,
      Strict;

      // $FF: synthetic method
      private static AutoCrystal.Interact[] $values() {
         return new AutoCrystal.Interact[]{Default, Strict};
      }
   }

   private static enum Switch {
      NONE,
      NORMAL,
      SILENT,
      INVENTORY;

      // $FF: synthetic method
      private static AutoCrystal.Switch[] $values() {
         return new AutoCrystal.Switch[]{NONE, NORMAL, SILENT, INVENTORY};
      }
   }

   public static enum Swing {
      Both,
      Place,
      Break,
      ServerSide;

      // $FF: synthetic method
      private static AutoCrystal.Swing[] $values() {
         return new AutoCrystal.Swing[]{Both, Place, Break, ServerSide};
      }
   }

   public static enum Render {
      Fade,
      Slide,
      Default;

      // $FF: synthetic method
      private static AutoCrystal.Render[] $values() {
         return new AutoCrystal.Render[]{Fade, Slide, Default};
      }
   }

   public static enum State {
      Active,
      Eating,
      LowHP,
      NoTarget,
      NoCrystalls,
      ExternalPause,
      Mining;

      // $FF: synthetic method
      private static AutoCrystal.State[] $values() {
         return new AutoCrystal.State[]{Active, Eating, LowHP, NoTarget, NoCrystalls, ExternalPause, Mining};
      }
   }

   public static record PlaceData(class_3965 bhr, float damage, float selfDamage, boolean overrideDamage) {
      public PlaceData(class_3965 bhr, float damage, float selfDamage, boolean overrideDamage) {
         this.bhr = bhr;
         this.damage = damage;
         this.selfDamage = selfDamage;
         this.overrideDamage = overrideDamage;
      }

      public class_3965 bhr() {
         return this.bhr;
      }

      public float damage() {
         return this.damage;
      }

      public float selfDamage() {
         return this.selfDamage;
      }

      public boolean overrideDamage() {
         return this.overrideDamage;
      }
   }

   private static record RotationVec(class_243 vec, class_3965 hitVec, boolean place) {
      private RotationVec(class_243 vec, class_3965 hitVec, boolean place) {
         this.vec = vec;
         this.hitVec = hitVec;
         this.place = place;
      }

      public class_243 vec() {
         return this.vec;
      }

      public class_3965 hitVec() {
         return this.hitVec;
      }

      public boolean place() {
         return this.place;
      }
   }

   private static record CrystalData(class_1511 crystal, float damage, float selfDamage, boolean overrideDamage) {
      private CrystalData(class_1511 crystal, float damage, float selfDamage, boolean overrideDamage) {
         this.crystal = crystal;
         this.damage = damage;
         this.selfDamage = selfDamage;
         this.overrideDamage = overrideDamage;
      }

      public class_1511 crystal() {
         return this.crystal;
      }

      public float damage() {
         return this.damage;
      }

      public float selfDamage() {
         return this.selfDamage;
      }

      public boolean overrideDamage() {
         return this.overrideDamage;
      }
   }

   private static enum Pages {
      Main,
      Place,
      Break,
      Damages,
      Pause,
      Render,
      Switch,
      FailSafe,
      Info,
      IDPredict;

      // $FF: synthetic method
      private static AutoCrystal.Pages[] $values() {
         return new AutoCrystal.Pages[]{Main, Place, Break, Damages, Pause, Render, Switch, FailSafe, Info, IDPredict};
      }
   }
}
