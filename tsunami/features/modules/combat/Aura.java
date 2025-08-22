package tsunami.features.modules.combat;

import baritone.api.BaritoneAPI;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_1268;
import net.minecraft.class_1294;
import net.minecraft.class_1297;
import net.minecraft.class_1308;
import net.minecraft.class_1309;
import net.minecraft.class_1429;
import net.minecraft.class_1451;
import net.minecraft.class_1531;
import net.minecraft.class_1588;
import net.minecraft.class_1621;
import net.minecraft.class_1646;
import net.minecraft.class_1657;
import net.minecraft.class_1674;
import net.minecraft.class_1676;
import net.minecraft.class_1678;
import net.minecraft.class_1713;
import net.minecraft.class_1743;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1829;
import net.minecraft.class_1835;
import net.minecraft.class_1839;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_2596;
import net.minecraft.class_2663;
import net.minecraft.class_2708;
import net.minecraft.class_2815;
import net.minecraft.class_2824;
import net.minecraft.class_2846;
import net.minecraft.class_2848;
import net.minecraft.class_2868;
import net.minecraft.class_2886;
import net.minecraft.class_3532;
import net.minecraft.class_4587;
import net.minecraft.class_5134;
import net.minecraft.class_745;
import net.minecraft.class_2828.class_2830;
import net.minecraft.class_2846.class_2847;
import net.minecraft.class_2848.class_2849;
import org.jetbrains.annotations.NotNull;
import tsunami.TsunamiClient;
import tsunami.core.Core;
import tsunami.core.Managers;
import tsunami.core.manager.client.ModuleManager;
import tsunami.core.manager.player.PlayerManager;
import tsunami.events.impl.EventSync;
import tsunami.events.impl.PacketEvent;
import tsunami.events.impl.PlayerUpdateEvent;
import tsunami.features.modules.Module;
import tsunami.features.modules.client.ClientSettings;
import tsunami.features.modules.client.HudEditor;
import tsunami.gui.notification.Notification;
import tsunami.injection.accesors.ILivingEntity;
import tsunami.setting.Setting;
import tsunami.setting.impl.BooleanSettingGroup;
import tsunami.setting.impl.SettingGroup;
import tsunami.utility.Timer;
import tsunami.utility.interfaces.IOtherClientPlayerEntity;
import tsunami.utility.math.MathUtility;
import tsunami.utility.player.InteractionUtility;
import tsunami.utility.player.InventoryUtility;
import tsunami.utility.player.PlayerUtility;
import tsunami.utility.player.SearchInvResult;
import tsunami.utility.render.Render2DEngine;
import tsunami.utility.render.Render3DEngine;
import tsunami.utility.render.animation.CaptureMark;

public class Aura extends Module {
   public final Setting<Float> attackRange = new Setting("Range", 3.1F, 1.0F, 6.0F);
   public final Setting<Float> wallRange = new Setting("ThroughWallsRange", 3.1F, 0.0F, 6.0F);
   public final Setting<Boolean> elytra = new Setting("ElytraOverride", false);
   public final Setting<Float> elytraAttackRange = new Setting("ElytraRange", 3.1F, 1.0F, 6.0F, (v) -> {
      return (Boolean)this.elytra.getValue();
   });
   public final Setting<Float> elytraWallRange = new Setting("ElytraThroughWallsRange", 3.1F, 0.0F, 6.0F, (v) -> {
      return (Boolean)this.elytra.getValue();
   });
   public final Setting<Aura.WallsBypass> wallsBypass;
   public final Setting<Integer> fov;
   public final Setting<Aura.Mode> rotationMode;
   public final Setting<Integer> interactTicks;
   public final Setting<Aura.Switch> switchMode;
   public final Setting<Boolean> onlyWeapon;
   public final Setting<BooleanSettingGroup> smartCrit;
   public final Setting<Boolean> onlySpace;
   public final Setting<Boolean> autoJump;
   public final Setting<Boolean> shieldBreaker;
   public final Setting<Boolean> pauseWhileEating;
   public final Setting<Boolean> tpsSync;
   public final Setting<Boolean> clientLook;
   public final Setting<Boolean> pauseBaritone;
   public final Setting<BooleanSettingGroup> oldDelay;
   public final Setting<Integer> minCPS;
   public final Setting<Integer> maxCPS;
   public final Setting<Aura.ESP> esp;
   public final Setting<SettingGroup> espGroup;
   public final Setting<Integer> espLength;
   public final Setting<Integer> espFactor;
   public final Setting<Float> espShaking;
   public final Setting<Float> espAmplitude;
   public final Setting<Aura.Sort> sort;
   public final Setting<Boolean> lockTarget;
   public final Setting<Boolean> elytraTarget;
   public final Setting<SettingGroup> advanced;
   public final Setting<Float> aimRange;
   public final Setting<Boolean> randomHitDelay;
   public final Setting<Boolean> pauseInInventory;
   public final Setting<Boolean> dropSprint;
   public final Setting<Boolean> returnSprint;
   public final Setting<Aura.RayTrace> rayTrace;
   public final Setting<Boolean> grimRayTrace;
   public final Setting<Boolean> unpressShield;
   public final Setting<Boolean> deathDisable;
   public final Setting<Boolean> tpDisable;
   public final Setting<Boolean> pullDown;
   public final Setting<Boolean> onlyJumpBoost;
   public final Setting<Float> pullValue;
   public final Setting<Aura.AttackHand> attackHand;
   public final Setting<Aura.Resolver> resolver;
   public final Setting<Integer> backTicks;
   public final Setting<Boolean> resolverVisualisation;
   public final Setting<Aura.AccelerateOnHit> accelerateOnHit;
   public final Setting<Integer> minYawStep;
   public final Setting<Integer> maxYawStep;
   public final Setting<Float> aimedPitchStep;
   public final Setting<Float> maxPitchStep;
   public final Setting<Float> pitchAccelerate;
   public final Setting<Float> attackCooldown;
   public final Setting<Float> attackBaseTime;
   public final Setting<Integer> attackTickLimit;
   public final Setting<Float> critFallDistance;
   public final Setting<SettingGroup> targets;
   public final Setting<Boolean> Players;
   public final Setting<Boolean> Mobs;
   public final Setting<Boolean> Animals;
   public final Setting<Boolean> Villagers;
   public final Setting<Boolean> Slimes;
   public final Setting<Boolean> hostiles;
   public final Setting<Boolean> onlyAngry;
   public final Setting<Boolean> Projectiles;
   public final Setting<Boolean> ignoreInvisible;
   public final Setting<Boolean> ignoreNamed;
   public final Setting<Boolean> ignoreTeam;
   public final Setting<Boolean> ignoreCreative;
   public final Setting<Boolean> ignoreNaked;
   public final Setting<Boolean> ignoreShield;
   public static class_1297 target;
   public float rotationYaw;
   public float rotationPitch;
   public float pitchAcceleration;
   private class_243 rotationPoint;
   private class_243 rotationMotion;
   private int hitTicks;
   private int trackticks;
   private boolean lookingAtHitbox;
   private final Timer delayTimer;
   private final Timer pauseTimer;
   public class_238 resolvedBox;
   static boolean wasTargeted = false;

   public Aura() {
      super("Aura", Module.Category.NONE);
      this.wallsBypass = new Setting("WallsBypass", Aura.WallsBypass.Off, (v) -> {
         return this.getWallRange() > 0.0F;
      });
      this.fov = new Setting("FOV", 180, 1, 180);
      this.rotationMode = new Setting("RotationMode", Aura.Mode.Track);
      this.interactTicks = new Setting("InteractTicks", 3, 1, 10, (v) -> {
         return this.rotationMode.getValue() == Aura.Mode.Interact;
      });
      this.switchMode = new Setting("AutoWeapon", Aura.Switch.None);
      this.onlyWeapon = new Setting("OnlyWeapon", false, (v) -> {
         return this.switchMode.getValue() != Aura.Switch.Silent;
      });
      this.smartCrit = new Setting("SmartCrit", new BooleanSettingGroup(true));
      this.onlySpace = (new Setting("OnlyCrit", false)).addToGroup(this.smartCrit);
      this.autoJump = (new Setting("AutoJump", false)).addToGroup(this.smartCrit);
      this.shieldBreaker = new Setting("ShieldBreaker", true);
      this.pauseWhileEating = new Setting("PauseWhileEating", false);
      this.tpsSync = new Setting("TPSSync", false);
      this.clientLook = new Setting("ClientLook", false);
      this.pauseBaritone = new Setting("PauseBaritone", false);
      this.oldDelay = new Setting("OldDelay", new BooleanSettingGroup(false));
      this.minCPS = (new Setting("MinCPS", 7, 1, 20)).addToGroup(this.oldDelay);
      this.maxCPS = (new Setting("MaxCPS", 12, 1, 20)).addToGroup(this.oldDelay);
      this.esp = new Setting("ESP", Aura.ESP.ThunderHack);
      this.espGroup = new Setting("ESPSettings", new SettingGroup(false, 0), (v) -> {
         return this.esp.is(Aura.ESP.ThunderHackV2);
      });
      this.espLength = (new Setting("ESPLength", 14, 1, 40, (v) -> {
         return this.esp.is(Aura.ESP.ThunderHackV2);
      })).addToGroup(this.espGroup);
      this.espFactor = (new Setting("ESPFactor", 8, 1, 20, (v) -> {
         return this.esp.is(Aura.ESP.ThunderHackV2);
      })).addToGroup(this.espGroup);
      this.espShaking = (new Setting("ESPShaking", 1.8F, 1.5F, 10.0F, (v) -> {
         return this.esp.is(Aura.ESP.ThunderHackV2);
      })).addToGroup(this.espGroup);
      this.espAmplitude = (new Setting("ESPAmplitude", 3.0F, 0.1F, 8.0F, (v) -> {
         return this.esp.is(Aura.ESP.ThunderHackV2);
      })).addToGroup(this.espGroup);
      this.sort = new Setting("Sort", Aura.Sort.LowestDistance);
      this.lockTarget = new Setting("LockTarget", true);
      this.elytraTarget = new Setting("ElytraTarget", true);
      this.advanced = new Setting("Advanced", new SettingGroup(false, 0));
      this.aimRange = (new Setting("AimRange", 3.1F, 0.0F, 6.0F)).addToGroup(this.advanced);
      this.randomHitDelay = (new Setting("RandomHitDelay", false)).addToGroup(this.advanced);
      this.pauseInInventory = (new Setting("PauseInInventory", true)).addToGroup(this.advanced);
      this.dropSprint = (new Setting("DropSprint", true)).addToGroup(this.advanced);
      this.returnSprint = (new Setting("ReturnSprint", true, (v) -> {
         return (Boolean)this.dropSprint.getValue();
      })).addToGroup(this.advanced);
      this.rayTrace = (new Setting("RayTrace", Aura.RayTrace.OnlyTarget)).addToGroup(this.advanced);
      this.grimRayTrace = (new Setting("GrimRayTrace", true)).addToGroup(this.advanced);
      this.unpressShield = (new Setting("UnpressShield", true)).addToGroup(this.advanced);
      this.deathDisable = (new Setting("DisableOnDeath", true)).addToGroup(this.advanced);
      this.tpDisable = (new Setting("TPDisable", false)).addToGroup(this.advanced);
      this.pullDown = (new Setting("FastFall", false)).addToGroup(this.advanced);
      this.onlyJumpBoost = (new Setting("OnlyJumpBoost", false, (v) -> {
         return (Boolean)this.pullDown.getValue();
      })).addToGroup(this.advanced);
      this.pullValue = (new Setting("PullValue", 3.0F, 0.0F, 20.0F, (v) -> {
         return (Boolean)this.pullDown.getValue();
      })).addToGroup(this.advanced);
      this.attackHand = (new Setting("AttackHand", Aura.AttackHand.MainHand)).addToGroup(this.advanced);
      this.resolver = (new Setting("Resolver", Aura.Resolver.Advantage)).addToGroup(this.advanced);
      this.backTicks = (new Setting("BackTicks", 4, 1, 20, (v) -> {
         return this.resolver.is(Aura.Resolver.BackTrack);
      })).addToGroup(this.advanced);
      this.resolverVisualisation = (new Setting("ResolverVisualisation", false, (v) -> {
         return !this.resolver.is(Aura.Resolver.Off);
      })).addToGroup(this.advanced);
      this.accelerateOnHit = (new Setting("AccelerateOnHit", Aura.AccelerateOnHit.Off)).addToGroup(this.advanced);
      this.minYawStep = (new Setting("MinYawStep", 65, 1, 180)).addToGroup(this.advanced);
      this.maxYawStep = (new Setting("MaxYawStep", 75, 1, 180)).addToGroup(this.advanced);
      this.aimedPitchStep = (new Setting("AimedPitchStep", 1.0F, 0.0F, 90.0F)).addToGroup(this.advanced);
      this.maxPitchStep = (new Setting("MaxPitchStep", 8.0F, 1.0F, 90.0F)).addToGroup(this.advanced);
      this.pitchAccelerate = (new Setting("PitchAccelerate", 1.65F, 1.0F, 10.0F)).addToGroup(this.advanced);
      this.attackCooldown = (new Setting("AttackCooldown", 0.9F, 0.5F, 1.0F)).addToGroup(this.advanced);
      this.attackBaseTime = (new Setting("AttackBaseTime", 0.5F, 0.0F, 2.0F)).addToGroup(this.advanced);
      this.attackTickLimit = (new Setting("AttackTickLimit", 11, 0, 20)).addToGroup(this.advanced);
      this.critFallDistance = (new Setting("CritFallDistance", 0.0F, 0.0F, 1.0F)).addToGroup(this.advanced);
      this.targets = new Setting("Targets", new SettingGroup(false, 0));
      this.Players = (new Setting("Players", true)).addToGroup(this.targets);
      this.Mobs = (new Setting("Mobs", true)).addToGroup(this.targets);
      this.Animals = (new Setting("Animals", true)).addToGroup(this.targets);
      this.Villagers = (new Setting("Villagers", true)).addToGroup(this.targets);
      this.Slimes = (new Setting("Slimes", true)).addToGroup(this.targets);
      this.hostiles = (new Setting("Hostiles", true)).addToGroup(this.targets);
      this.onlyAngry = (new Setting("OnlyAngryHostiles", true, (v) -> {
         return (Boolean)this.hostiles.getValue();
      })).addToGroup(this.targets);
      this.Projectiles = (new Setting("Projectiles", true)).addToGroup(this.targets);
      this.ignoreInvisible = (new Setting("IgnoreInvisibleEntities", false)).addToGroup(this.targets);
      this.ignoreNamed = (new Setting("IgnoreNamed", false)).addToGroup(this.targets);
      this.ignoreTeam = (new Setting("IgnoreTeam", false)).addToGroup(this.targets);
      this.ignoreCreative = (new Setting("IgnoreCreative", true)).addToGroup(this.targets);
      this.ignoreNaked = (new Setting("IgnoreNaked", false)).addToGroup(this.targets);
      this.ignoreShield = (new Setting("AttackShieldingEntities", true)).addToGroup(this.targets);
      this.pitchAcceleration = 1.0F;
      this.rotationPoint = class_243.field_1353;
      this.rotationMotion = class_243.field_1353;
      this.delayTimer = new Timer();
      this.pauseTimer = new Timer();
   }

   private float getRange() {
      return (Boolean)this.elytra.getValue() && mc.field_1724.method_6128() ? (Float)this.elytraAttackRange.getValue() : (Float)this.attackRange.getValue();
   }

   private float getWallRange() {
      return (Boolean)this.elytra.getValue() && mc.field_1724 != null && mc.field_1724.method_6128() ? (Float)this.elytraWallRange.getValue() : (Float)this.wallRange.getValue();
   }

   public void auraLogic() {
      if (!this.haveWeapon()) {
         target = null;
      } else {
         this.handleKill();
         this.updateTarget();
         if (target != null) {
            if (!mc.field_1690.field_1903.method_1434() && mc.field_1724.method_24828() && (Boolean)this.autoJump.getValue()) {
               mc.field_1724.method_6043();
            }

            boolean readyForAttack;
            if ((Boolean)this.grimRayTrace.getValue()) {
               readyForAttack = this.autoCrit() && (this.lookingAtHitbox || this.skipRayTraceCheck());
               this.calcRotations(this.autoCrit());
            } else {
               this.calcRotations(this.autoCrit());
               readyForAttack = this.autoCrit() && (this.lookingAtHitbox || this.skipRayTraceCheck());
            }

            if (readyForAttack) {
               if (this.shieldBreaker(false)) {
                  return;
               }

               boolean[] playerState;
               label45: {
                  playerState = this.preAttack();
                  class_1297 var4 = target;
                  if (var4 instanceof class_1657) {
                     class_1657 pl = (class_1657)var4;
                     if (pl.method_6115() && pl.method_6079().method_7909() == class_1802.field_8255 && !(Boolean)this.ignoreShield.getValue()) {
                        break label45;
                     }
                  }

                  this.attack();
               }

               this.postAttack(playerState[0], playerState[1]);
            }

         }
      }
   }

   private boolean haveWeapon() {
      class_1792 handItem = mc.field_1724.method_6047().method_7909();
      if (!(Boolean)this.onlyWeapon.getValue()) {
         return true;
      } else if (this.switchMode.getValue() == Aura.Switch.None) {
         return handItem instanceof class_1829 || handItem instanceof class_1743 || handItem instanceof class_1835;
      } else {
         return InventoryUtility.getSwordHotBar().found() || InventoryUtility.getAxeHotBar().found();
      }
   }

   private boolean skipRayTraceCheck() {
      return this.rotationMode.getValue() == Aura.Mode.None || this.rayTrace.getValue() == Aura.RayTrace.OFF || this.rotationMode.is(Aura.Mode.Grim) || this.rotationMode.is(Aura.Mode.Interact) && ((Integer)this.interactTicks.getValue() <= 1 || mc.field_1687.method_20812(mc.field_1724, mc.field_1724.method_5829().method_1009(-0.25D, 0.0D, -0.25D).method_989(0.0D, 1.0D, 0.0D)).iterator().hasNext());
   }

   public void attack() {
      Criticals.cancelCrit = true;
      ModuleManager.criticals.doCrit();
      int prevSlot = this.switchMethod();
      mc.field_1761.method_2918(mc.field_1724, target);
      Criticals.cancelCrit = false;
      this.swingHand();
      this.hitTicks = this.getHitTicks();
      if (prevSlot != -1) {
         InventoryUtility.switchTo(prevSlot);
      }

   }

   @NotNull
   private boolean[] preAttack() {
      boolean blocking = mc.field_1724.method_6115() && mc.field_1724.method_6030().method_7909().method_7853(mc.field_1724.method_6030()) == class_1839.field_8949;
      if (blocking && (Boolean)this.unpressShield.getValue()) {
         this.sendPacket(new class_2846(class_2847.field_12974, class_2338.field_10980, class_2350.field_11033));
      }

      boolean sprint = Core.serverSprint;
      if (sprint && (Boolean)this.dropSprint.getValue()) {
         this.disableSprint();
      }

      if (this.rotationMode.is(Aura.Mode.Grim)) {
         this.sendPacket(new class_2830(mc.field_1724.method_23317(), mc.field_1724.method_23318(), mc.field_1724.method_23321(), this.rotationYaw, this.rotationPitch, mc.field_1724.method_24828()));
      }

      return new boolean[]{blocking, sprint};
   }

   public void postAttack(boolean block, boolean sprint) {
      if (sprint && (Boolean)this.returnSprint.getValue() && (Boolean)this.dropSprint.getValue()) {
         this.enableSprint();
      }

      if (block && (Boolean)this.unpressShield.getValue()) {
         this.sendSequencedPacket((id) -> {
            return new class_2886(class_1268.field_5810, id, this.rotationYaw, this.rotationPitch);
         });
      }

      if (this.rotationMode.is(Aura.Mode.Grim)) {
         this.sendPacket(new class_2830(mc.field_1724.method_23317(), mc.field_1724.method_23318(), mc.field_1724.method_23321(), mc.field_1724.method_36454(), mc.field_1724.method_36455(), mc.field_1724.method_24828()));
      }

   }

   private void disableSprint() {
      mc.field_1724.method_5728(false);
      mc.field_1690.field_1867.method_23481(false);
      this.sendPacket(new class_2848(mc.field_1724, class_2849.field_12985));
   }

   private void enableSprint() {
      mc.field_1724.method_5728(true);
      mc.field_1690.field_1867.method_23481(true);
      this.sendPacket(new class_2848(mc.field_1724, class_2849.field_12981));
   }

   public void resolvePlayers() {
      if (this.resolver.not(Aura.Resolver.Off)) {
         Iterator var1 = mc.field_1687.method_18456().iterator();

         while(var1.hasNext()) {
            class_1657 player = (class_1657)var1.next();
            if (player instanceof class_745) {
               ((IOtherClientPlayerEntity)player).resolve((Aura.Resolver)this.resolver.getValue());
            }
         }
      }

   }

   public void restorePlayers() {
      if (this.resolver.not(Aura.Resolver.Off)) {
         Iterator var1 = mc.field_1687.method_18456().iterator();

         while(var1.hasNext()) {
            class_1657 player = (class_1657)var1.next();
            if (player instanceof class_745) {
               ((IOtherClientPlayerEntity)player).releaseResolver();
            }
         }
      }

   }

   public void handleKill() {
      if (target instanceof class_1309 && (((class_1309)target).method_6032() <= 0.0F || ((class_1309)target).method_29504())) {
         Managers.NOTIFICATION.publicity("Aura", ClientSettings.isRu() ? "Цель успешно нейтрализована!" : "Target successfully neutralized!", 3, Notification.Type.SUCCESS);
      }

   }

   private int switchMethod() {
      int prevSlot = -1;
      SearchInvResult swordResult = InventoryUtility.getSwordHotBar();
      if (swordResult.found() && this.switchMode.getValue() != Aura.Switch.None) {
         if (this.switchMode.getValue() == Aura.Switch.Silent) {
            prevSlot = mc.field_1724.method_31548().field_7545;
         }

         swordResult.switchTo();
      }

      return prevSlot;
   }

   private int getHitTicks() {
      return ((BooleanSettingGroup)this.oldDelay.getValue()).isEnabled() ? 1 + (int)(20.0F / MathUtility.random((float)(Integer)this.minCPS.getValue(), (float)(Integer)this.maxCPS.getValue())) : (this.shouldRandomizeDelay() ? (int)MathUtility.random(11.0F, 13.0F) : (Integer)this.attackTickLimit.getValue());
   }

   @EventHandler
   public void onUpdate(PlayerUpdateEvent e) {
      if (this.pauseTimer.passedMs(1000L)) {
         if (!mc.field_1724.method_6115() || !(Boolean)this.pauseWhileEating.getValue()) {
            if ((Boolean)this.pauseBaritone.getValue() && TsunamiClient.baritone) {
               boolean isTargeted = target != null;
               if (isTargeted && !wasTargeted) {
                  BaritoneAPI.getProvider().getPrimaryBaritone().getCommandManager().execute("pause");
                  wasTargeted = true;
               } else if (!isTargeted && wasTargeted) {
                  BaritoneAPI.getProvider().getPrimaryBaritone().getCommandManager().execute("resume");
                  wasTargeted = false;
               }
            }

            this.resolvePlayers();
            this.auraLogic();
            this.restorePlayers();
            --this.hitTicks;
         }
      }
   }

   @EventHandler
   public void onSync(EventSync e) {
      if (this.pauseTimer.passedMs(1000L)) {
         if (!mc.field_1724.method_6115() || !(Boolean)this.pauseWhileEating.getValue()) {
            if (this.haveWeapon()) {
               if (target != null && this.rotationMode.getValue() != Aura.Mode.None && this.rotationMode.getValue() != Aura.Mode.Grim) {
                  mc.field_1724.method_36456(this.rotationYaw);
                  mc.field_1724.method_36457(this.rotationPitch);
               } else {
                  this.rotationYaw = mc.field_1724.method_36454();
                  this.rotationPitch = mc.field_1724.method_36455();
               }

               if (((BooleanSettingGroup)this.oldDelay.getValue()).isEnabled() && (Integer)this.minCPS.getValue() > (Integer)this.maxCPS.getValue()) {
                  this.minCPS.setValue((Integer)this.maxCPS.getValue());
               }

               if (target != null && (Boolean)this.pullDown.getValue() && (mc.field_1724.method_6059(class_1294.field_5913) || !(Boolean)this.onlyJumpBoost.getValue())) {
                  mc.field_1724.method_5762(0.0D, (double)(-(Float)this.pullValue.getValue() / 1000.0F), 0.0D);
               }

            }
         }
      }
   }

   @EventHandler
   public void onPacketSend(@NotNull PacketEvent.Send e) {
      class_2596 var3 = e.getPacket();
      if (var3 instanceof class_2824) {
         class_2824 pie = (class_2824)var3;
         if (Criticals.getInteractType(pie) != Criticals.InteractType.ATTACK && target != null) {
            e.cancel();
         }
      }

   }

   @EventHandler
   public void onPacketReceive(@NotNull PacketEvent.Receive e) {
      class_2596 var3 = e.getPacket();
      class_2663 pac;
      if (var3 instanceof class_2663) {
         pac = (class_2663)var3;
         if (pac.method_11470() == 30 && pac.method_11469(mc.field_1687) != null && target != null && pac.method_11469(mc.field_1687) == target) {
            Managers.NOTIFICATION.publicity("Aura", ClientSettings.isRu() ? "Успешно сломали щит игроку " + target.method_5477().getString() : "Succesfully destroyed " + target.method_5477().getString() + "'s shield", 2, Notification.Type.SUCCESS);
         }
      }

      if (e.getPacket() instanceof class_2708 && (Boolean)this.tpDisable.getValue()) {
         this.disable(ClientSettings.isRu() ? "Отключаю из-за телепортации!" : "Disabling due to tp!");
      }

      var3 = e.getPacket();
      if (var3 instanceof class_2663) {
         pac = (class_2663)var3;
         if (pac.method_11470() == 3 && pac.method_11469(mc.field_1687) == mc.field_1724 && (Boolean)this.deathDisable.getValue()) {
            this.disable(ClientSettings.isRu() ? "Отключаю из-за смерти!" : "Disabling due to death!");
         }
      }

   }

   public void onEnable() {
      target = null;
      this.lookingAtHitbox = false;
      this.rotationPoint = class_243.field_1353;
      this.rotationMotion = class_243.field_1353;
      this.rotationYaw = mc.field_1724.method_36454();
      this.rotationPitch = mc.field_1724.method_36455();
      this.delayTimer.reset();
   }

   private boolean autoCrit() {
      boolean reasonForSkipCrit = !((BooleanSettingGroup)this.smartCrit.getValue()).isEnabled() || mc.field_1724.method_31549().field_7479 || mc.field_1724.method_6128() || ModuleManager.elytraPlus.isEnabled() || mc.field_1724.method_6059(class_1294.field_5919) || mc.field_1724.method_6059(class_1294.field_5906) || Managers.PLAYER.isInWeb();
      if (this.hitTicks > 0) {
         return false;
      } else if ((Boolean)this.pauseInInventory.getValue() && Managers.PLAYER.inInventory) {
         return false;
      } else if (this.getAttackCooldown() < (Float)this.attackCooldown.getValue() && !((BooleanSettingGroup)this.oldDelay.getValue()).isEnabled()) {
         return false;
      } else if (ModuleManager.criticals.isEnabled() && ModuleManager.criticals.mode.is(Criticals.Mode.Grim)) {
         return true;
      } else {
         boolean mergeWithTargetStrafe = !ModuleManager.targetStrafe.isEnabled() || !(Boolean)ModuleManager.targetStrafe.jump.getValue();
         boolean mergeWithSpeed = !ModuleManager.speed.isEnabled() || mc.field_1724.method_24828();
         if (!mc.field_1690.field_1903.method_1434() && mergeWithTargetStrafe && mergeWithSpeed && !(Boolean)this.onlySpace.getValue() && !(Boolean)this.autoJump.getValue()) {
            return true;
         } else if (!mc.field_1724.method_5771() && !mc.field_1724.method_5869()) {
            if (!mc.field_1690.field_1903.method_1434() && this.isAboveWater()) {
               return true;
            } else if (mc.field_1724.field_6017 > 1.0F && (double)mc.field_1724.field_6017 < 1.14D) {
               return false;
            } else if (reasonForSkipCrit) {
               return true;
            } else {
               return !mc.field_1724.method_24828() && mc.field_1724.field_6017 > (this.shouldRandomizeFallDistance() ? MathUtility.random(0.15F, 0.7F) : (Float)this.critFallDistance.getValue());
            }
         } else {
            return true;
         }
      }
   }

   private boolean shieldBreaker(boolean instant) {
      int axeSlot = InventoryUtility.getAxe().slot();
      if (axeSlot == -1) {
         return false;
      } else if (!(Boolean)this.shieldBreaker.getValue()) {
         return false;
      } else if (!(target instanceof class_1657)) {
         return false;
      } else if (!((class_1657)target).method_6115() && !instant) {
         return false;
      } else if (((class_1657)target).method_6079().method_7909() != class_1802.field_8255 && ((class_1657)target).method_6047().method_7909() != class_1802.field_8255) {
         return false;
      } else {
         if (axeSlot >= 9) {
            mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, axeSlot, mc.field_1724.method_31548().field_7545, class_1713.field_7791, mc.field_1724);
            this.sendPacket(new class_2815(mc.field_1724.field_7512.field_7763));
            mc.field_1761.method_2918(mc.field_1724, target);
            this.swingHand();
            mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, axeSlot, mc.field_1724.method_31548().field_7545, class_1713.field_7791, mc.field_1724);
            this.sendPacket(new class_2815(mc.field_1724.field_7512.field_7763));
         } else {
            this.sendPacket(new class_2868(axeSlot));
            mc.field_1761.method_2918(mc.field_1724, target);
            this.swingHand();
            this.sendPacket(new class_2868(mc.field_1724.method_31548().field_7545));
         }

         this.hitTicks = 10;
         return true;
      }
   }

   private void swingHand() {
      switch(((Aura.AttackHand)this.attackHand.getValue()).ordinal()) {
      case 0:
         mc.field_1724.method_6104(class_1268.field_5808);
         break;
      case 1:
         mc.field_1724.method_6104(class_1268.field_5810);
      }

   }

   public boolean isAboveWater() {
      return mc.field_1724.method_5869() || mc.field_1687.method_8320(class_2338.method_49638(mc.field_1724.method_19538().method_1031(0.0D, -0.4D, 0.0D))).method_26204() == class_2246.field_10382;
   }

   public float getAttackCooldownProgressPerTick() {
      return (float)(1.0D / mc.field_1724.method_45325(class_5134.field_23723) * 20.0D * (double)TsunamiClient.TICK_TIMER * (double)((Boolean)this.tpsSync.getValue() ? Managers.SERVER.getTPSFactor() : 1.0F));
   }

   public float getAttackCooldown() {
      return class_3532.method_15363(((float)((ILivingEntity)mc.field_1724).getLastAttackedTicks() + (Float)this.attackBaseTime.getValue()) / this.getAttackCooldownProgressPerTick(), 0.0F, 1.0F);
   }

   private void updateTarget() {
      class_1297 candidat = this.findTarget();
      if (target == null) {
         target = candidat;
      } else {
         if (this.sort.getValue() == Aura.Sort.FOV || !(Boolean)this.lockTarget.getValue()) {
            target = candidat;
         }

         if (candidat instanceof class_1676) {
            target = candidat;
         }

         if (this.skipEntity(target)) {
            target = null;
         }

      }
   }

   private void calcRotations(boolean ready) {
      if (ready) {
         this.trackticks = mc.field_1687.method_20812(mc.field_1724, mc.field_1724.method_5829().method_1009(-0.25D, 0.0D, -0.25D).method_989(0.0D, 1.0D, 0.0D)).iterator().hasNext() ? 1 : (Integer)this.interactTicks.getValue();
      } else if (this.trackticks > 0) {
         --this.trackticks;
      }

      if (target != null) {
         class_243 targetVec;
         if (!mc.field_1724.method_6128() && !ModuleManager.elytraPlus.isEnabled()) {
            targetVec = this.getLegitLook(target);
         } else {
            targetVec = target.method_33571();
         }

         if (targetVec != null) {
            this.pitchAcceleration = Managers.PLAYER.checkRtx(this.rotationYaw, this.rotationPitch, this.getRange() + (Float)this.aimRange.getValue(), this.getRange() + (Float)this.aimRange.getValue(), (Aura.RayTrace)this.rayTrace.getValue()) ? (Float)this.aimedPitchStep.getValue() : (this.pitchAcceleration < (Float)this.maxPitchStep.getValue() ? this.pitchAcceleration * (Float)this.pitchAccelerate.getValue() : (Float)this.maxPitchStep.getValue());
            float delta_yaw = class_3532.method_15393((float)class_3532.method_15338(Math.toDegrees(Math.atan2(targetVec.field_1350 - mc.field_1724.method_23321(), targetVec.field_1352 - mc.field_1724.method_23317())) - 90.0D) - this.rotationYaw) + (float)(this.wallsBypass.is(Aura.WallsBypass.V2) && !ready && !mc.field_1724.method_6057(target) ? 20 : 0);
            float delta_pitch = (float)(-Math.toDegrees(Math.atan2(targetVec.field_1351 - (mc.field_1724.method_19538().field_1351 + (double)mc.field_1724.method_18381(mc.field_1724.method_18376())), Math.sqrt(Math.pow(targetVec.field_1352 - mc.field_1724.method_23317(), 2.0D) + Math.pow(targetVec.field_1350 - mc.field_1724.method_23321(), 2.0D))))) - this.rotationPitch;
            float yawStep = this.rotationMode.getValue() != Aura.Mode.Track ? 360.0F : MathUtility.random((float)(Integer)this.minYawStep.getValue(), (float)(Integer)this.maxYawStep.getValue());
            float pitchStep = this.rotationMode.getValue() != Aura.Mode.Track ? 180.0F : (Managers.PLAYER.ticksElytraFlying > 5 ? 180.0F : this.pitchAcceleration + MathUtility.random(-1.0F, 1.0F));
            if (ready) {
               switch(((Aura.AccelerateOnHit)this.accelerateOnHit.getValue()).ordinal()) {
               case 1:
                  yawStep = 180.0F;
                  break;
               case 2:
                  pitchStep = 90.0F;
                  break;
               case 3:
                  yawStep = 180.0F;
                  pitchStep = 90.0F;
               }
            }

            if (delta_yaw > 180.0F) {
               delta_yaw -= 180.0F;
            }

            float deltaYaw = class_3532.method_15363(class_3532.method_15379(delta_yaw), -yawStep, yawStep);
            float deltaPitch = class_3532.method_15363(delta_pitch, -pitchStep, pitchStep);
            float newYaw = this.rotationYaw + (delta_yaw > 0.0F ? deltaYaw : -deltaYaw);
            float newPitch = class_3532.method_15363(this.rotationPitch + deltaPitch, -90.0F, 90.0F);
            double gcdFix = Math.pow((Double)mc.field_1690.method_42495().method_41753() * 0.6D + 0.2D, 3.0D) * 1.2D;
            if (this.trackticks <= 0 && this.rotationMode.getValue() != Aura.Mode.Track) {
               this.rotationYaw = mc.field_1724.method_36454();
               this.rotationPitch = mc.field_1724.method_36455();
            } else {
               this.rotationYaw = (float)((double)newYaw - (double)(newYaw - this.rotationYaw) % gcdFix);
               this.rotationPitch = (float)((double)newPitch - (double)(newPitch - this.rotationPitch) % gcdFix);
            }

            if (!this.rotationMode.is(Aura.Mode.Grim)) {
               ModuleManager.rotations.fixRotation = this.rotationYaw;
            }

            this.lookingAtHitbox = Managers.PLAYER.checkRtx(this.rotationYaw, this.rotationPitch, this.getRange(), this.getWallRange(), (Aura.RayTrace)this.rayTrace.getValue());
         }
      }
   }

   public void onRender3D(class_4587 stack) {
      if (this.haveWeapon() && target != null) {
         if ((this.resolver.is(Aura.Resolver.BackTrack) || (Boolean)this.resolverVisualisation.getValue()) && this.resolvedBox != null) {
            Render3DEngine.OUTLINE_QUEUE.add(new Render3DEngine.OutlineAction(this.resolvedBox, HudEditor.getColor(0), 1.0F));
         }

         switch(((Aura.ESP)this.esp.getValue()).ordinal()) {
         case 1:
            Render3DEngine.drawTargetEsp(stack, target);
            break;
         case 2:
            CaptureMark.render(target);
            break;
         case 3:
            Render3DEngine.drawOldTargetEsp(stack, target);
            break;
         case 4:
            Render3DEngine.renderGhosts((Integer)this.espLength.getValue(), (Integer)this.espFactor.getValue(), (Float)this.espShaking.getValue(), (Float)this.espAmplitude.getValue(), target);
         }

         if ((Boolean)this.clientLook.getValue() && this.rotationMode.getValue() != Aura.Mode.None) {
            mc.field_1724.method_36456((float)Render2DEngine.interpolate((double)mc.field_1724.field_5982, (double)this.rotationYaw, (double)Render3DEngine.getTickDelta()));
            mc.field_1724.method_36457((float)Render2DEngine.interpolate((double)mc.field_1724.field_6004, (double)this.rotationPitch, (double)Render3DEngine.getTickDelta()));
         }

      }
   }

   public void onDisable() {
      target = null;
   }

   public float getSquaredRotateDistance() {
      float dst = this.getRange();
      dst += (Float)this.aimRange.getValue();
      if ((mc.field_1724.method_6128() || ModuleManager.elytraPlus.isEnabled()) && target != null) {
         dst += 4.0F;
      }

      if (ModuleManager.strafe.isEnabled()) {
         dst += 4.0F;
      }

      if (this.rotationMode.getValue() != Aura.Mode.Track || this.rayTrace.getValue() == Aura.RayTrace.OFF) {
         dst = this.getRange();
      }

      return dst * dst;
   }

   public class_243 getLegitLook(class_1297 target) {
      float minMotionXZ = 0.003F;
      float maxMotionXZ = 0.03F;
      float minMotionY = 0.001F;
      float maxMotionY = 0.03F;
      double lenghtX = target.method_5829().method_17939();
      double lenghtY = target.method_5829().method_17940();
      double lenghtZ = target.method_5829().method_17941();
      if (this.rotationMotion.equals(class_243.field_1353)) {
         this.rotationMotion = new class_243((double)MathUtility.random(-0.05F, 0.05F), (double)MathUtility.random(-0.05F, 0.05F), (double)MathUtility.random(-0.05F, 0.05F));
      }

      this.rotationPoint = this.rotationPoint.method_1019(this.rotationMotion);
      if (this.rotationPoint.field_1352 >= (lenghtX - 0.05D) / 2.0D) {
         this.rotationMotion = new class_243((double)(-MathUtility.random(minMotionXZ, maxMotionXZ)), this.rotationMotion.method_10214(), this.rotationMotion.method_10215());
      }

      if (this.rotationPoint.field_1351 >= lenghtY) {
         this.rotationMotion = new class_243(this.rotationMotion.method_10216(), (double)(-MathUtility.random(minMotionY, maxMotionY)), this.rotationMotion.method_10215());
      }

      if (this.rotationPoint.field_1350 >= (lenghtZ - 0.05D) / 2.0D) {
         this.rotationMotion = new class_243(this.rotationMotion.method_10216(), this.rotationMotion.method_10214(), (double)(-MathUtility.random(minMotionXZ, maxMotionXZ)));
      }

      if (this.rotationPoint.field_1352 <= -(lenghtX - 0.05D) / 2.0D) {
         this.rotationMotion = new class_243((double)MathUtility.random(minMotionXZ, 0.03F), this.rotationMotion.method_10214(), this.rotationMotion.method_10215());
      }

      if (this.rotationPoint.field_1351 <= 0.05D) {
         this.rotationMotion = new class_243(this.rotationMotion.method_10216(), (double)MathUtility.random(minMotionY, maxMotionY), this.rotationMotion.method_10215());
      }

      if (this.rotationPoint.field_1350 <= -(lenghtZ - 0.05D) / 2.0D) {
         this.rotationMotion = new class_243(this.rotationMotion.method_10216(), this.rotationMotion.method_10214(), (double)MathUtility.random(minMotionXZ, maxMotionXZ));
      }

      this.rotationPoint.method_1031((double)MathUtility.random(-0.03F, 0.03F), 0.0D, (double)MathUtility.random(-0.03F, 0.03F));
      if (!mc.field_1724.method_6057(target) && Objects.requireNonNull((Aura.WallsBypass)this.wallsBypass.getValue()) == Aura.WallsBypass.V1) {
         return target.method_19538().method_1031(MathUtility.random(-0.15D, 0.15D), lenghtY, MathUtility.random(-0.15D, 0.15D));
      } else {
         if (!Managers.PLAYER.checkRtx(this.rotationYaw, this.rotationPitch, this.getRange(), this.getWallRange(), (Aura.RayTrace)this.rayTrace.getValue())) {
            PlayerManager var10000 = Managers.PLAYER;
            float[] rotation1 = PlayerManager.calcAngle(target.method_19538().method_1031(0.0D, (double)(target.method_18381(target.method_18376()) / 2.0F), 0.0D));
            if (PlayerUtility.squaredDistanceFromEyes(target.method_19538().method_1031(0.0D, (double)(target.method_18381(target.method_18376()) / 2.0F), 0.0D)) <= this.attackRange.getPow2Value() && Managers.PLAYER.checkRtx(rotation1[0], rotation1[1], this.getRange(), 0.0F, (Aura.RayTrace)this.rayTrace.getValue())) {
               this.rotationPoint = new class_243((double)MathUtility.random(-0.1F, 0.1F), (double)(target.method_18381(target.method_18376()) / MathUtility.random(1.8F, 2.5F)), (double)MathUtility.random(-0.1F, 0.1F));
            } else {
               float halfBox = (float)(lenghtX / 2.0D);

               for(float x1 = -halfBox; x1 <= halfBox; x1 += 0.05F) {
                  for(float z1 = -halfBox; z1 <= halfBox; z1 += 0.05F) {
                     for(float y1 = 0.05F; (double)y1 <= target.method_5829().method_17940(); y1 += 0.15F) {
                        class_243 v1 = new class_243(target.method_23317() + (double)x1, target.method_23318() + (double)y1, target.method_23321() + (double)z1);
                        if (!(PlayerUtility.squaredDistanceFromEyes(v1) > this.attackRange.getPow2Value())) {
                           var10000 = Managers.PLAYER;
                           float[] rotation = PlayerManager.calcAngle(v1);
                           if (Managers.PLAYER.checkRtx(rotation[0], rotation[1], this.getRange(), 0.0F, (Aura.RayTrace)this.rayTrace.getValue())) {
                              this.rotationPoint = new class_243((double)x1, (double)y1, (double)z1);
                              break;
                           }
                        }
                     }
                  }
               }
            }
         }

         return target.method_19538().method_1019(this.rotationPoint);
      }
   }

   public boolean isInRange(class_1297 target) {
      if (PlayerUtility.squaredDistanceFromEyes(target.method_19538().method_1031(0.0D, (double)target.method_18381(target.method_18376()), 0.0D)) > this.getSquaredRotateDistance() + 4.0F) {
         return false;
      } else {
         float halfBox = (float)(target.method_5829().method_17939() / 2.0D);

         for(float x1 = -halfBox; x1 <= halfBox; x1 += 0.15F) {
            for(float z1 = -halfBox; z1 <= halfBox; z1 += 0.15F) {
               for(float y1 = 0.05F; (double)y1 <= target.method_5829().method_17940(); y1 += 0.25F) {
                  if (!(PlayerUtility.squaredDistanceFromEyes(new class_243(target.method_23317() + (double)x1, target.method_23318() + (double)y1, target.method_23321() + (double)z1)) > this.getSquaredRotateDistance())) {
                     PlayerManager var10000 = Managers.PLAYER;
                     float[] rotation = PlayerManager.calcAngle(new class_243(target.method_23317() + (double)x1, target.method_23318() + (double)y1, target.method_23321() + (double)z1));
                     if (Managers.PLAYER.checkRtx(rotation[0], rotation[1], (float)Math.sqrt((double)this.getSquaredRotateDistance()), this.getWallRange(), (Aura.RayTrace)this.rayTrace.getValue())) {
                        return true;
                     }
                  }
               }
            }
         }

         return false;
      }
   }

   public class_1297 findTarget() {
      List<class_1309> first_stage = new CopyOnWriteArrayList();
      Iterator var2 = mc.field_1687.method_18112().iterator();

      while(var2.hasNext()) {
         class_1297 ent = (class_1297)var2.next();
         if ((ent instanceof class_1678 || ent instanceof class_1674) && ent.method_5805() && this.isInRange(ent) && (Boolean)this.Projectiles.getValue()) {
            return ent;
         }

         if (!this.skipEntity(ent) && ent instanceof class_1309) {
            first_stage.add((class_1309)ent);
         }
      }

      class_1309 var10000;
      switch(((Aura.Sort)this.sort.getValue()).ordinal()) {
      case 0:
         var10000 = (class_1309)first_stage.stream().min(Comparator.comparing((e) -> {
            return mc.field_1724.method_5707(e.method_19538());
         })).orElse((Object)null);
         break;
      case 1:
         var10000 = (class_1309)first_stage.stream().max(Comparator.comparing((e) -> {
            return mc.field_1724.method_5707(e.method_19538());
         })).orElse((Object)null);
         break;
      case 2:
         var10000 = (class_1309)first_stage.stream().min(Comparator.comparing((e) -> {
            return e.method_6032() + e.method_6067();
         })).orElse((Object)null);
         break;
      case 3:
         var10000 = (class_1309)first_stage.stream().max(Comparator.comparing((e) -> {
            return e.method_6032() + e.method_6067();
         })).orElse((Object)null);
         break;
      case 4:
         var10000 = (class_1309)first_stage.stream().min(Comparator.comparing((e) -> {
            float v = 0.0F;
            Iterator var2 = e.method_5661().iterator();

            while(var2.hasNext()) {
               class_1799 armor = (class_1799)var2.next();
               if (armor != null && !armor.method_7909().equals(class_1802.field_8162)) {
                  v += (float)(armor.method_7936() - armor.method_7919()) / (float)armor.method_7936();
               }
            }

            return v;
         })).orElse((Object)null);
         break;
      case 5:
         var10000 = (class_1309)first_stage.stream().max(Comparator.comparing((e) -> {
            float v = 0.0F;
            Iterator var2 = e.method_5661().iterator();

            while(var2.hasNext()) {
               class_1799 armor = (class_1799)var2.next();
               if (armor != null && !armor.method_7909().equals(class_1802.field_8162)) {
                  v += (float)(armor.method_7936() - armor.method_7919()) / (float)armor.method_7936();
               }
            }

            return v;
         })).orElse((Object)null);
         break;
      case 6:
         var10000 = (class_1309)first_stage.stream().min(Comparator.comparing(this::getFOVAngle)).orElse((Object)null);
         break;
      default:
         throw new MatchException((String)null, (Throwable)null);
      }

      return var10000;
   }

   private boolean skipEntity(class_1297 entity) {
      if (this.isBullet(entity)) {
         return false;
      } else if (!(entity instanceof class_1309)) {
         return true;
      } else {
         class_1309 ent = (class_1309)entity;
         if (!ent.method_29504() && entity.method_5805()) {
            if (entity instanceof class_1531) {
               return true;
            } else if (entity instanceof class_1451) {
               return true;
            } else if (this.skipNotSelected(entity)) {
               return true;
            } else if (!InteractionUtility.isVecInFOV(ent.method_19538(), (Integer)this.fov.getValue())) {
               return true;
            } else {
               if (entity instanceof class_1657) {
                  class_1657 player = (class_1657)entity;
                  if (ModuleManager.antiBot.isEnabled() && AntiBot.bots.contains(entity)) {
                     return true;
                  }

                  if (player == mc.field_1724 || Managers.FRIEND.isFriend(player)) {
                     return true;
                  }

                  if (player.method_7337() && (Boolean)this.ignoreCreative.getValue()) {
                     return true;
                  }

                  if (player.method_6096() == 0 && (Boolean)this.ignoreNaked.getValue()) {
                     return true;
                  }

                  if (player.method_5767() && (Boolean)this.ignoreInvisible.getValue()) {
                     return true;
                  }

                  if (player.method_22861() == mc.field_1724.method_22861() && (Boolean)this.ignoreTeam.getValue() && mc.field_1724.method_22861() != 16777215) {
                     return true;
                  }
               }

               return !this.isInRange(entity) || entity.method_16914() && (Boolean)this.ignoreNamed.getValue();
            }
         } else {
            return true;
         }
      }
   }

   private boolean isBullet(class_1297 entity) {
      return (entity instanceof class_1678 || entity instanceof class_1674) && entity.method_5805() && PlayerUtility.squaredDistanceFromEyes(entity.method_19538()) < this.getSquaredRotateDistance() && (Boolean)this.Projectiles.getValue();
   }

   private boolean skipNotSelected(class_1297 entity) {
      if (entity instanceof class_1621 && !(Boolean)this.Slimes.getValue()) {
         return true;
      } else {
         if (entity instanceof class_1588) {
            class_1588 he = (class_1588)entity;
            if (!(Boolean)this.hostiles.getValue()) {
               return true;
            }

            if ((Boolean)this.onlyAngry.getValue()) {
               return !he.method_7076(mc.field_1724);
            }
         }

         if (entity instanceof class_1657 && !(Boolean)this.Players.getValue()) {
            return true;
         } else if (entity instanceof class_1646 && !(Boolean)this.Villagers.getValue()) {
            return true;
         } else if (entity instanceof class_1308 && !(Boolean)this.Mobs.getValue()) {
            return true;
         } else {
            return entity instanceof class_1429 && !(Boolean)this.Animals.getValue();
         }
      }
   }

   private float getFOVAngle(@NotNull class_1309 e) {
      double difX = e.method_23317() - mc.field_1724.method_23317();
      double difZ = e.method_23321() - mc.field_1724.method_23321();
      float yaw = (float)class_3532.method_15338(Math.toDegrees(Math.atan2(difZ, difX)) - 90.0D);
      return Math.abs(yaw - class_3532.method_15393(mc.field_1724.method_36454()));
   }

   public void pause() {
      this.pauseTimer.reset();
   }

   private boolean shouldRandomizeDelay() {
      return (Boolean)this.randomHitDelay.getValue() && (mc.field_1724.method_24828() || mc.field_1724.field_6017 < 0.12F || mc.field_1724.method_5681() || mc.field_1724.method_6128());
   }

   private boolean shouldRandomizeFallDistance() {
      return (Boolean)this.randomHitDelay.getValue() && !this.shouldRandomizeDelay();
   }

   public static enum WallsBypass {
      Off,
      V1,
      V2;

      // $FF: synthetic method
      private static Aura.WallsBypass[] $values() {
         return new Aura.WallsBypass[]{Off, V1, V2};
      }
   }

   public static enum Mode {
      Interact,
      Track,
      Grim,
      None;

      // $FF: synthetic method
      private static Aura.Mode[] $values() {
         return new Aura.Mode[]{Interact, Track, Grim, None};
      }
   }

   public static enum Switch {
      Normal,
      None,
      Silent;

      // $FF: synthetic method
      private static Aura.Switch[] $values() {
         return new Aura.Switch[]{Normal, None, Silent};
      }
   }

   public static enum ESP {
      Off,
      ThunderHack,
      NurikZapen,
      CelkaPasta,
      ThunderHackV2;

      // $FF: synthetic method
      private static Aura.ESP[] $values() {
         return new Aura.ESP[]{Off, ThunderHack, NurikZapen, CelkaPasta, ThunderHackV2};
      }
   }

   public static enum Sort {
      LowestDistance,
      HighestDistance,
      LowestHealth,
      HighestHealth,
      LowestDurability,
      HighestDurability,
      FOV;

      // $FF: synthetic method
      private static Aura.Sort[] $values() {
         return new Aura.Sort[]{LowestDistance, HighestDistance, LowestHealth, HighestHealth, LowestDurability, HighestDurability, FOV};
      }
   }

   public static enum RayTrace {
      OFF,
      OnlyTarget,
      AllEntities;

      // $FF: synthetic method
      private static Aura.RayTrace[] $values() {
         return new Aura.RayTrace[]{OFF, OnlyTarget, AllEntities};
      }
   }

   public static enum AttackHand {
      MainHand,
      OffHand,
      None;

      // $FF: synthetic method
      private static Aura.AttackHand[] $values() {
         return new Aura.AttackHand[]{MainHand, OffHand, None};
      }
   }

   public static enum Resolver {
      Off,
      Advantage,
      Predictive,
      BackTrack;

      // $FF: synthetic method
      private static Aura.Resolver[] $values() {
         return new Aura.Resolver[]{Off, Advantage, Predictive, BackTrack};
      }
   }

   public static enum AccelerateOnHit {
      Off,
      Yaw,
      Pitch,
      Both;

      // $FF: synthetic method
      private static Aura.AccelerateOnHit[] $values() {
         return new Aura.AccelerateOnHit[]{Off, Yaw, Pitch, Both};
      }
   }

   public static class Position {
      private double x;
      private double y;
      private double z;
      private int ticks;

      public Position(double x, double y, double z) {
         this.x = x;
         this.y = y;
         this.z = z;
      }

      public boolean shouldRemove() {
         return this.ticks++ > (Integer)ModuleManager.aura.backTicks.getValue();
      }

      public double getX() {
         return this.x;
      }

      public double getY() {
         return this.y;
      }

      public double getZ() {
         return this.z;
      }
   }
}
