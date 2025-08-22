package tsunami.features.modules.movement;

import com.mojang.blaze3d.systems.RenderSystem;
import java.util.Iterator;
import java.util.List;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_124;
import net.minecraft.class_1268;
import net.minecraft.class_1304;
import net.minecraft.class_1713;
import net.minecraft.class_1770;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_2338;
import net.minecraft.class_2404;
import net.minecraft.class_243;
import net.minecraft.class_2596;
import net.minecraft.class_2708;
import net.minecraft.class_2739;
import net.minecraft.class_2824;
import net.minecraft.class_2848;
import net.minecraft.class_2868;
import net.minecraft.class_287;
import net.minecraft.class_2886;
import net.minecraft.class_289;
import net.minecraft.class_290;
import net.minecraft.class_332;
import net.minecraft.class_3417;
import net.minecraft.class_3419;
import net.minecraft.class_3532;
import net.minecraft.class_3675;
import net.minecraft.class_4587;
import net.minecraft.class_6373;
import net.minecraft.class_757;
import net.minecraft.class_2848.class_2849;
import net.minecraft.class_293.class_5596;
import net.minecraft.class_2945.class_7834;
import tsunami.TsunamiClient;
import tsunami.core.Managers;
import tsunami.events.impl.EventMove;
import tsunami.events.impl.EventSync;
import tsunami.events.impl.EventTravel;
import tsunami.events.impl.PacketEvent;
import tsunami.events.impl.PlayerUpdateEvent;
import tsunami.features.modules.Module;
import tsunami.features.modules.client.ClientSettings;
import tsunami.features.modules.client.HudEditor;
import tsunami.features.modules.combat.Criticals;
import tsunami.features.modules.player.ElytraSwap;
import tsunami.gui.font.FontRenderers;
import tsunami.gui.notification.Notification;
import tsunami.setting.Setting;
import tsunami.setting.impl.Bind;
import tsunami.setting.impl.BooleanSettingGroup;
import tsunami.utility.math.MathUtility;
import tsunami.utility.player.InventoryUtility;
import tsunami.utility.player.MovementUtility;
import tsunami.utility.player.PlayerUtility;
import tsunami.utility.render.Render2DEngine;
import tsunami.utility.render.Render3DEngine;

public class ElytraPlus extends Module {
   public final Setting<ElytraPlus.Mode> mode;
   private final Setting<Integer> disablerDelay;
   private final Setting<Boolean> twoBee;
   private final Setting<Boolean> onlySpace;
   private final Setting<Boolean> stopOnGround;
   private final Setting<Boolean> infDurability;
   private final Setting<Boolean> vertical;
   private final Setting<ElytraPlus.NCPStrict> ncpStrict;
   private final Setting<ElytraPlus.AntiKick> antiKick;
   private final Setting<Float> xzSpeed;
   private final Setting<Float> ySpeed;
   private final Setting<Integer> fireSlot;
   private final Setting<BooleanSettingGroup> accelerate;
   private final Setting<Float> accelerateFactor;
   private final Setting<Float> fireDelay;
   private final Setting<BooleanSettingGroup> grim;
   private final Setting<Boolean> rotate;
   private final Setting<Boolean> fireWorkExtender;
   private final Setting<Boolean> stayMad;
   private final Setting<Boolean> keepFlying;
   private final Setting<Boolean> disableOnFlag;
   private final Setting<Boolean> allowFireSwap;
   private final Setting<Boolean> bowBomb;
   private final Setting<Bind> bombKey;
   private final Setting<Boolean> instantFly;
   private final Setting<Boolean> cruiseControl;
   private final Setting<Float> factor;
   private final Setting<Float> upSpeed;
   private final Setting<Float> downFactor;
   private final Setting<Boolean> stopMotion;
   private final Setting<Float> minUpSpeed;
   private final Setting<Boolean> forceHeight;
   private final Setting<Integer> manualHeight;
   private final Setting<Float> sneakDownSpeed;
   private final Setting<Boolean> speedLimit;
   private final Setting<Float> maxSpeed;
   private final Setting<Float> redeployInterval;
   private final Setting<Float> redeployTimeOut;
   private final Setting<Float> redeployDelay;
   private final Setting<Float> infiniteMaxSpeed;
   private final Setting<Float> infiniteMinSpeed;
   private final Setting<Integer> infiniteMaxHeight;
   private final tsunami.utility.Timer startTimer;
   private final tsunami.utility.Timer redeployTimer;
   private final tsunami.utility.Timer strictTimer;
   private final tsunami.utility.Timer pingTimer;
   private boolean infiniteFlag;
   private boolean hasTouchedGround;
   private boolean elytraEquiped;
   private boolean flying;
   private boolean started;
   private float acceleration;
   private float accelerationY;
   private float height;
   private float prevClientPitch;
   private float infinitePitch;
   private float lastInfinitePitch;
   private class_1799 prevArmorItemCopy;
   private class_1799 getStackInSlotCopy;
   private class_1792 prevArmorItem;
   private class_1792 prevItemInHand;
   private class_243 flightZonePos;
   private int prevElytraSlot;
   private int disablerTicks;
   private int slotWithFireWorks;
   private long lastFireworkTime;

   public ElytraPlus() {
      super("Elytra+", Module.Category.NONE);
      this.mode = new Setting("Mode", ElytraPlus.Mode.FireWork);
      this.disablerDelay = new Setting("DisablerDelay", 1, 0, 10, (v) -> {
         return this.mode.is(ElytraPlus.Mode.SunriseOld);
      });
      this.twoBee = new Setting("2b2t", false, (v) -> {
         return this.mode.is(ElytraPlus.Mode.Boost);
      });
      this.onlySpace = new Setting("OnlySpace", true, (v) -> {
         return this.mode.is(ElytraPlus.Mode.Boost) && (Boolean)this.twoBee.getValue();
      });
      this.stopOnGround = new Setting("StopOnGround", false, (v) -> {
         return this.mode.is(ElytraPlus.Mode.Packet);
      });
      this.infDurability = new Setting("InfDurability", true, (v) -> {
         return this.mode.is(ElytraPlus.Mode.Packet);
      });
      this.vertical = new Setting("Vertical", false, (v) -> {
         return this.mode.is(ElytraPlus.Mode.Packet);
      });
      this.ncpStrict = new Setting("NCPStrict", ElytraPlus.NCPStrict.Off, (v) -> {
         return this.mode.is(ElytraPlus.Mode.Packet);
      });
      this.antiKick = new Setting("AntiKick", ElytraPlus.AntiKick.Jitter, (v) -> {
         return this.mode.is(ElytraPlus.Mode.FireWork) || this.mode.is(ElytraPlus.Mode.SunriseOld);
      });
      this.xzSpeed = new Setting("XZSpeed", 1.55F, 0.1F, 10.0F, (v) -> {
         return !this.mode.is(ElytraPlus.Mode.Boost) && this.mode.getValue() != ElytraPlus.Mode.Pitch40Infinite;
      });
      this.ySpeed = new Setting("YSpeed", 0.47F, 0.0F, 2.0F, (v) -> {
         return this.mode.is(ElytraPlus.Mode.FireWork) || this.mode.getValue() == ElytraPlus.Mode.SunriseOld || this.mode.is(ElytraPlus.Mode.Packet) && (Boolean)this.vertical.getValue();
      });
      this.fireSlot = new Setting("FireSlot", 1, 1, 9, (v) -> {
         return this.mode.is(ElytraPlus.Mode.FireWork);
      });
      this.accelerate = new Setting("Acceleration", new BooleanSettingGroup(false), (v) -> {
         return this.mode.is(ElytraPlus.Mode.Control) || this.mode.is(ElytraPlus.Mode.Packet);
      });
      this.accelerateFactor = (new Setting("AccelerateFactor", 9.0F, 0.0F, 100.0F, (v) -> {
         return this.mode.is(ElytraPlus.Mode.Control) || this.mode.is(ElytraPlus.Mode.Packet);
      })).addToGroup(this.accelerate);
      this.fireDelay = new Setting("FireDelay", 1.5F, 0.0F, 1.5F, (v) -> {
         return this.mode.is(ElytraPlus.Mode.FireWork);
      });
      this.grim = new Setting("Grim", new BooleanSettingGroup(false), (v) -> {
         return this.mode.is(ElytraPlus.Mode.FireWork);
      });
      this.rotate = (new Setting("Rotate", true, (v) -> {
         return this.mode.is(ElytraPlus.Mode.FireWork);
      })).addToGroup(this.grim);
      this.fireWorkExtender = (new Setting("FireWorkExtender", true, (v) -> {
         return this.mode.is(ElytraPlus.Mode.FireWork);
      })).addToGroup(this.grim);
      this.stayMad = new Setting("GroundSafe", false, (v) -> {
         return this.mode.is(ElytraPlus.Mode.FireWork);
      });
      this.keepFlying = new Setting("KeepFlying", false, (v) -> {
         return this.mode.is(ElytraPlus.Mode.FireWork);
      });
      this.disableOnFlag = new Setting("DisableOnFlag", false, (v) -> {
         return this.mode.is(ElytraPlus.Mode.FireWork);
      });
      this.allowFireSwap = new Setting("AllowFireSwap", false, (v) -> {
         return this.mode.is(ElytraPlus.Mode.FireWork);
      });
      this.bowBomb = new Setting("BowBomb", false, (v) -> {
         return this.mode.is(ElytraPlus.Mode.FireWork) || this.mode.getValue() == ElytraPlus.Mode.SunriseOld;
      });
      this.bombKey = new Setting("BombKey", new Bind(-1, false, false), (v) -> {
         return this.mode.getValue() == ElytraPlus.Mode.SunriseOld;
      });
      this.instantFly = new Setting("InstantFly", true, (v) -> {
         return this.mode.is(ElytraPlus.Mode.Boost) && !(Boolean)this.twoBee.getValue() || this.mode.is(ElytraPlus.Mode.Control);
      });
      this.cruiseControl = new Setting("CruiseControl", false, (v) -> {
         return this.mode.is(ElytraPlus.Mode.Boost);
      });
      this.factor = new Setting("Factor", 1.5F, 0.1F, 50.0F, (v) -> {
         return this.mode.is(ElytraPlus.Mode.Boost);
      });
      this.upSpeed = new Setting("UpSpeed", 1.0F, 0.01F, 5.0F, (v) -> {
         return this.mode.is(ElytraPlus.Mode.Boost) && !(Boolean)this.twoBee.getValue() || this.mode.is(ElytraPlus.Mode.Control);
      });
      this.downFactor = new Setting("Glide", 1.0F, 0.0F, 2.0F, (v) -> {
         return this.mode.is(ElytraPlus.Mode.Boost) && !(Boolean)this.twoBee.getValue() || this.mode.is(ElytraPlus.Mode.Control);
      });
      this.stopMotion = new Setting("StopMotion", true, (v) -> {
         return this.mode.is(ElytraPlus.Mode.Boost) && !(Boolean)this.twoBee.getValue();
      });
      this.minUpSpeed = new Setting("MinUpSpeed", 0.5F, 0.1F, 5.0F, (v) -> {
         return this.mode.is(ElytraPlus.Mode.Boost) && (Boolean)this.cruiseControl.getValue();
      });
      this.forceHeight = new Setting("ForceHeight", false, (v) -> {
         return this.mode.is(ElytraPlus.Mode.Boost) && (Boolean)this.cruiseControl.getValue();
      });
      this.manualHeight = new Setting("Height", 121, 1, 256, (v) -> {
         return this.mode.is(ElytraPlus.Mode.Boost) && (Boolean)this.forceHeight.getValue();
      });
      this.sneakDownSpeed = new Setting("DownSpeed", 1.0F, 0.01F, 5.0F, (v) -> {
         return this.mode.is(ElytraPlus.Mode.Control);
      });
      this.speedLimit = new Setting("SpeedLimit", true, (v) -> {
         return this.mode.is(ElytraPlus.Mode.Boost);
      });
      this.maxSpeed = new Setting("MaxSpeed", 2.5F, 0.1F, 10.0F, (v) -> {
         return this.mode.is(ElytraPlus.Mode.Boost);
      });
      this.redeployInterval = new Setting("RedeployInterval", 1.0F, 0.1F, 5.0F, (v) -> {
         return this.mode.is(ElytraPlus.Mode.Boost) && !(Boolean)this.twoBee.getValue();
      });
      this.redeployTimeOut = new Setting("RedeployTimeout", 5.0F, 0.1F, 20.0F, (v) -> {
         return this.mode.is(ElytraPlus.Mode.Boost) && !(Boolean)this.twoBee.getValue();
      });
      this.redeployDelay = new Setting("RedeployDelay", 0.5F, 0.1F, 1.0F, (v) -> {
         return this.mode.is(ElytraPlus.Mode.Boost) && !(Boolean)this.twoBee.getValue();
      });
      this.infiniteMaxSpeed = new Setting("InfiniteMaxSpeed", 150.0F, 50.0F, 170.0F, (v) -> {
         return this.mode.getValue() == ElytraPlus.Mode.Pitch40Infinite;
      });
      this.infiniteMinSpeed = new Setting("InfiniteMinSpeed", 25.0F, 10.0F, 70.0F, (v) -> {
         return this.mode.getValue() == ElytraPlus.Mode.Pitch40Infinite;
      });
      this.infiniteMaxHeight = new Setting("InfiniteMaxHeight", 200, 50, 360, (v) -> {
         return this.mode.getValue() == ElytraPlus.Mode.Pitch40Infinite;
      });
      this.startTimer = new tsunami.utility.Timer();
      this.redeployTimer = new tsunami.utility.Timer();
      this.strictTimer = new tsunami.utility.Timer();
      this.pingTimer = new tsunami.utility.Timer();
      this.prevArmorItem = class_1802.field_8162;
      this.prevItemInHand = class_1802.field_8162;
      this.prevElytraSlot = -1;
      this.slotWithFireWorks = -1;
   }

   public void onEnable() {
      if (mc.field_1724.method_23318() < (double)(Integer)this.infiniteMaxHeight.getValue() && this.mode.getValue() == ElytraPlus.Mode.Pitch40Infinite) {
         this.disable(ClientSettings.isRu() ? "Поднимись выше " + String.valueOf(class_124.field_1075) + String.valueOf(this.infiniteMaxHeight.getValue()) + String.valueOf(class_124.field_1080) + " высоты!" : "Go above " + String.valueOf(class_124.field_1075) + String.valueOf(this.infiniteMaxHeight.getValue()) + String.valueOf(class_124.field_1080) + " height!");
      }

      this.flying = false;
      this.reset();
      this.infiniteFlag = false;
      this.acceleration = 0.0F;
      this.accelerationY = 0.0F;
      if (mc.field_1724 != null) {
         this.height = (float)mc.field_1724.method_23318();
      }

      this.pingTimer.reset();
      if (this.mode.is(ElytraPlus.Mode.FireWork)) {
         this.fireworkOnEnable();
      }

   }

   @EventHandler
   public void modifyVelocity(EventTravel e) {
      if (this.mode.getValue() == ElytraPlus.Mode.Pitch40Infinite) {
         if (e.isPre()) {
            this.prevClientPitch = mc.field_1724.method_36455();
            mc.field_1724.method_36457(this.lastInfinitePitch);
         } else {
            mc.field_1724.method_36457(this.prevClientPitch);
         }
      }

      if (this.mode.is(ElytraPlus.Mode.FireWork) && Managers.PLAYER.ticksElytraFlying < 4) {
         if (e.isPre()) {
            this.prevClientPitch = mc.field_1724.method_36455();
            mc.field_1724.method_36457(-45.0F);
         } else {
            mc.field_1724.method_36457(this.prevClientPitch);
         }
      }

      if (this.mode.getValue() == ElytraPlus.Mode.SunriseNew) {
         if (mc.field_1690.field_1903.method_1434()) {
            if (e.isPre()) {
               this.prevClientPitch = mc.field_1724.method_36455();
               mc.field_1724.method_36457(-45.0F);
            } else {
               mc.field_1724.method_36457(this.prevClientPitch);
            }
         } else if (mc.field_1690.field_1832.method_1434()) {
            if (e.isPre()) {
               this.prevClientPitch = mc.field_1724.method_36455();
               mc.field_1724.method_36457(45.0F);
            } else {
               mc.field_1724.method_36457(this.prevClientPitch);
            }
         }
      }

   }

   @EventHandler
   public void onSync(EventSync e) {
      switch(((ElytraPlus.Mode)this.mode.getValue()).ordinal()) {
      case 0:
         this.fireworkOnSync();
         break;
      case 1:
         this.doSunrise();
         break;
      case 2:
      case 3:
         this.doPreLegacy();
         break;
      case 4:
         this.doPitch40Infinite();
         break;
      case 5:
         this.doSunriseNew();
         break;
      case 6:
         this.doPacket(e);
      }

   }

   private void doPacket(EventSync e) {
      if ((!this.isBoxCollidingGround() || !(Boolean)this.stopOnGround.getValue()) && mc.field_1724.method_31548().method_5438(38).method_7909() == class_1802.field_8833) {
         if ((Boolean)this.infDurability.getValue() || !mc.field_1724.method_6128()) {
            this.sendPacket(new class_2848(mc.field_1724, class_2849.field_12982));
         }

         if (mc.field_1724.field_6012 % 3 != 0 && this.ncpStrict.is(ElytraPlus.NCPStrict.Motion)) {
            e.cancel();
         }
      }

   }

   private void doPitch40Infinite() {
      class_1799 is = mc.field_1724.method_6118(class_1304.field_6174);
      if (is.method_31574(class_1802.field_8833)) {
         mc.field_1724.method_36457(this.lastInfinitePitch);
         if (is.method_7919() > 380 && mc.field_1724.field_6012 % 100 == 0) {
            Managers.NOTIFICATION.publicity("Elytra+", ClientSettings.isRu() ? "Элитра скоро сломается!" : "Elytra's about to break!", 2, Notification.Type.WARNING);
            mc.field_1687.method_8465(mc.field_1724, mc.field_1724.method_23317(), mc.field_1724.method_23318(), mc.field_1724.method_23321(), class_3417.field_15152, class_3419.field_15256, 10.0F, 1.0F, 0L);
         }
      }

   }

   private void doSunriseNew() {
      if (mc.field_1724.field_5976) {
         this.acceleration = 0.0F;
      }

      int elytra = InventoryUtility.getElytra();
      if (elytra != -1) {
         if (mc.field_1724.method_24828()) {
            mc.field_1724.method_6043();
            this.acceleration = 0.0F;
         } else if (!(mc.field_1724.field_6017 <= 0.0F)) {
            if (!mc.field_1690.field_1903.method_1434() && !mc.field_1690.field_1832.method_1434()) {
               this.takeOnChestPlate();
               if (mc.field_1724.field_6012 % 8 == 0) {
                  this.matrixDisabler(elytra);
               }

               MovementUtility.setMotion((double)Math.min((this.acceleration += 8.0F / (Float)this.xzSpeed.getValue()) / 100.0F, (Float)this.xzSpeed.getValue()));
               if (!MovementUtility.isMoving()) {
                  this.acceleration = 0.0F;
               }

               mc.field_1724.method_18800(mc.field_1724.method_18798().method_10216(), -0.004999999888241291D, mc.field_1724.method_18798().method_10215());
            } else {
               this.acceleration = 0.0F;
               this.takeOnElytra();
            }

         }
      }
   }

   private void takeOnElytra() {
      int elytra = InventoryUtility.getElytra();
      if (elytra != -1) {
         elytra = elytra >= 0 && elytra < 9 ? elytra + 36 : elytra;
         if (elytra != -2) {
            clickSlot(elytra);
            clickSlot(6);
            clickSlot(elytra);
            mc.field_1724.field_3944.method_52787(new class_2848(mc.field_1724, class_2849.field_12982));
         }

      }
   }

   private void takeOnChestPlate() {
      int slot = ElytraSwap.getChestPlateSlot();
      if (slot != -1) {
         if (slot != -2) {
            clickSlot(slot);
            clickSlot(6);
            clickSlot(slot);
         }

      }
   }

   private float getInfinitePitch() {
      if (mc.field_1724.method_23318() < (double)(Integer)this.infiniteMaxHeight.getValue()) {
         if (Managers.PLAYER.currentPlayerSpeed * 72.0F < (Float)this.infiniteMinSpeed.getValue() && !this.infiniteFlag) {
            this.infiniteFlag = true;
         }

         if (Managers.PLAYER.currentPlayerSpeed * 72.0F > (Float)this.infiniteMaxSpeed.getValue() && this.infiniteFlag) {
            this.infiniteFlag = false;
         }
      } else {
         this.infiniteFlag = true;
      }

      if (this.infiniteFlag) {
         this.infinitePitch += 3.0F;
      } else {
         this.infinitePitch -= 3.0F;
      }

      this.infinitePitch = MathUtility.clamp(this.infinitePitch, -40.0F, 40.0F);
      return this.infinitePitch;
   }

   public void onDisable() {
      TsunamiClient.TICK_TIMER = 1.0F;
      mc.field_1724.method_31549().field_7479 = false;
      mc.field_1724.method_31549().method_7248(0.05F);
      if (this.mode.is(ElytraPlus.Mode.FireWork)) {
         this.fireworkOnDisable();
      }

   }

   @EventHandler(
      priority = -200
   )
   public void onMove(EventMove e) {
      switch(((ElytraPlus.Mode)this.mode.getValue()).ordinal()) {
      case 0:
         this.fireworkOnMove(e);
      case 1:
      case 4:
      case 5:
      default:
         break;
      case 2:
         this.doBoost(e);
         break;
      case 3:
         this.doControl(e);
         break;
      case 6:
         this.doMotionPacket(e);
      }

   }

   @EventHandler
   public void onPacketSend(PacketEvent.SendPost event) {
      if (!fullNullCheck()) {
         class_2596 var3 = event.getPacket();
         if (var3 instanceof class_2848) {
            class_2848 command = (class_2848)var3;
            if (this.mode.is(ElytraPlus.Mode.FireWork) && command.method_12365() == class_2849.field_12982) {
               this.doFireWork(false);
            }
         }

         var3 = event.getPacket();
         if (var3 instanceof class_2824) {
            class_2824 p = (class_2824)var3;
            if (this.mode.is(ElytraPlus.Mode.FireWork) && ((BooleanSettingGroup)this.grim.getValue()).isEnabled() && (Boolean)this.fireWorkExtender.getValue() && this.flying && this.flightZonePos != null && (float)Criticals.getEntity(p).field_6012 < (float)this.pingTimer.getPassedTimeMs() / 50.0F) {
               String var10001 = String.valueOf(class_124.field_1061);
               this.sendMessage(var10001 + (ClientSettings.isRu() ? "В этом режиме нельзя бить сущностей которые появились после включения модуля!" : "In this mode, you cannot hit entities that spawned after the module was turned on!"));
            }
         }

      }
   }

   @EventHandler
   public void onPacketReceive(PacketEvent.Receive e) {
      class_2596 var3 = e.getPacket();
      if (var3 instanceof class_2739) {
         class_2739 pac = (class_2739)var3;
         if (pac.comp_1127() == mc.field_1724.method_5628() && (this.mode.is(ElytraPlus.Mode.Packet) || this.mode.is(ElytraPlus.Mode.SunriseOld))) {
            List<class_7834<?>> values = pac.comp_1128();
            if (values.isEmpty()) {
               return;
            }

            Iterator var4 = values.iterator();

            label69:
            while(true) {
               class_7834 value;
               do {
                  if (!var4.hasNext()) {
                     break label69;
                  }

                  value = (class_7834)var4.next();
               } while(!value.comp_1117().toString().equals("FALL_FLYING") && (value.comp_1115() != 0 || !value.comp_1117().toString().equals("-120") && !value.comp_1117().toString().equals("-128") && !value.comp_1117().toString().equals("-126")));

               e.cancel();
            }
         }
      }

      if (e.getPacket() instanceof class_2708) {
         this.acceleration = 0.0F;
         this.accelerationY = 0.0F;
         this.pingTimer.reset();
         if ((Boolean)this.disableOnFlag.getValue() && this.mode.is(ElytraPlus.Mode.FireWork)) {
            this.disable(ClientSettings.isRu() ? "Выключен из-за флага!" : "Disabled due to flag!");
         }
      }

      if (e.getPacket() instanceof class_6373 && this.mode.is(ElytraPlus.Mode.FireWork) && ((BooleanSettingGroup)this.grim.getValue()).isEnabled() && (Boolean)this.fireWorkExtender.getValue() && this.flying) {
         if (!this.pingTimer.passedMs(50000L)) {
            if (this.pingTimer.passedMs(1000L) && PlayerUtility.getSquaredDistance2D(this.flightZonePos) < 7000.0F) {
               e.cancel();
            }
         } else {
            this.pingTimer.reset();
         }
      }

   }

   @EventHandler
   public void onPlayerUpdate(PlayerUpdateEvent e) {
      switch(((ElytraPlus.Mode)this.mode.getValue()).ordinal()) {
      case 0:
         this.fireWorkOnPlayerUpdate();
         break;
      case 4:
         this.lastInfinitePitch = PlayerUtility.fixAngle(this.getInfinitePitch());
      }

   }

   private void doMotionPacket(EventMove e) {
      mc.field_1724.method_31549().field_7479 = false;
      mc.field_1724.method_31549().method_7248(0.05F);
      if ((!this.isBoxCollidingGround() || !(Boolean)this.stopOnGround.getValue()) && mc.field_1724.method_31548().method_5438(38).method_7909() == class_1802.field_8833) {
         mc.field_1724.method_31549().field_7479 = true;
         mc.field_1724.method_31549().method_7248((Float)this.xzSpeed.getValue() / 15.0F * (((BooleanSettingGroup)this.accelerate.getValue()).isEnabled() ? Math.min((this.acceleration += (Float)this.accelerateFactor.getValue()) / 100.0F, 1.0F) : 1.0F));
         e.cancel();
         if (mc.field_1724.field_6012 % 3 == 0 && this.ncpStrict.is(ElytraPlus.NCPStrict.Motion)) {
            e.setY(0.0D);
            e.setX(0.0D);
            e.setZ(0.0D);
         } else {
            if (Math.abs(e.getX()) < 0.05D) {
               e.setX(0.0D);
            }

            if (Math.abs(e.getZ()) < 0.05D) {
               e.setZ(0.0D);
            }

            e.setY((Boolean)this.vertical.getValue() ? (mc.field_1690.field_1903.method_1434() ? (double)(Float)this.ySpeed.getValue() : (mc.field_1690.field_1832.method_1434() ? (double)(-(Float)this.ySpeed.getValue()) : 0.0D)) : 0.0D);
            switch(((ElytraPlus.NCPStrict)this.ncpStrict.getValue()).ordinal()) {
            case 1:
               e.setY(2.0E-4D - (mc.field_1724.field_6012 % 2 == 0 ? 0.0D : 1.0E-6D) + MathUtility.random(0.0D, 9.0E-7D));
               break;
            case 2:
               e.setY(-1.000088900582341E-12D);
               break;
            case 3:
               e.setY(-4.000355602329364E-12D);
            }

            if (mc.field_1724.field_5976 && (this.ncpStrict.is(ElytraPlus.NCPStrict.New) || this.ncpStrict.is(ElytraPlus.NCPStrict.Motion)) && mc.field_1724.field_6012 % 2 == 0) {
               e.setY(-0.07840000152587923D);
            }

            if (((Boolean)this.infDurability.getValue() || this.ncpStrict.is(ElytraPlus.NCPStrict.Motion)) && !MovementUtility.isMoving() && Math.abs(e.getX()) < 0.121D) {
               float angleToRad = (float)Math.toRadians(4.5D * (double)(mc.field_1724.field_6012 % 80));
               e.setX(Math.sin((double)angleToRad) * 0.12D);
               e.setZ(Math.cos((double)angleToRad) * 0.12D);
            }

         }
      }
   }

   private void doPreLegacy() {
      if (!(Boolean)this.twoBee.getValue() || !this.mode.is(ElytraPlus.Mode.Boost)) {
         if (mc.field_1724.method_24828()) {
            this.hasTouchedGround = true;
         }

         if (!(Boolean)this.cruiseControl.getValue()) {
            this.height = (float)mc.field_1724.method_23318();
         }

         if (this.strictTimer.passedMs(1500L) && !this.strictTimer.passedMs(2000L)) {
            TsunamiClient.TICK_TIMER = 1.0F;
         }

         if (!mc.field_1724.method_6128()) {
            if (this.hasTouchedGround && !mc.field_1724.method_24828() && mc.field_1724.field_6017 > 0.0F && (Boolean)this.instantFly.getValue()) {
               TsunamiClient.TICK_TIMER = 0.3F;
            }

            if (!mc.field_1724.method_24828() && (Boolean)this.instantFly.getValue() && mc.field_1724.method_18798().method_10214() < 0.0D) {
               if (!this.startTimer.passedMs((long)(1000.0F * (Float)this.redeployDelay.getValue()))) {
                  return;
               }

               this.startTimer.reset();
               this.sendPacket(new class_2848(mc.field_1724, class_2849.field_12982));
               this.hasTouchedGround = false;
               this.strictTimer.reset();
            }
         }

      }
   }

   public void onRender3D(class_4587 stack) {
      if (this.mode.is(ElytraPlus.Mode.FireWork) && ((BooleanSettingGroup)this.grim.getValue()).isEnabled() && (Boolean)this.fireWorkExtender.getValue() && this.flying && this.flightZonePos != null) {
         stack.method_22903();
         Render3DEngine.setupRender();
         RenderSystem.disableCull();
         class_289 tessellator = class_289.method_1348();
         RenderSystem.setShader(class_757::method_34540);
         class_287 bufferBuilder = class_289.method_1348().method_60827(class_5596.field_27380, class_290.field_1576);

         for(int i = 0; i <= 30; ++i) {
            float cos = (float)(this.flightZonePos.method_10216() - mc.method_1561().field_4686.method_19326().method_10216() + Math.cos((double)i * 6.283185307179586D / 30.0D) * 95.0D);
            float sin = (float)(this.flightZonePos.method_10215() - mc.method_1561().field_4686.method_19326().method_10215() + Math.sin((double)i * 6.283185307179586D / 30.0D) * 95.0D);
            bufferBuilder.method_22918(stack.method_23760().method_23761(), cos, (float)(-mc.method_1561().field_4686.method_19326().method_10214()), sin).method_39415(Render2DEngine.injectAlpha(HudEditor.getColor(i), 255).getRGB());
            bufferBuilder.method_22918(stack.method_23760().method_23761(), cos, (float)(128.0D - mc.method_1561().field_4686.method_19326().method_10214()), sin).method_39415(Render2DEngine.injectAlpha(HudEditor.getColor(i), 0).getRGB());
         }

         Render2DEngine.endBuilding(bufferBuilder);
         RenderSystem.enableCull();
         Render3DEngine.endRender();
         stack.method_22909();
      }

   }

   public void onRender2D(class_332 context) {
      if (this.mode.is(ElytraPlus.Mode.FireWork) && ((BooleanSettingGroup)this.grim.getValue()).isEnabled() && (Boolean)this.fireWorkExtender.getValue() && this.flying && !this.pingTimer.passedMs(50000L) && this.pingTimer.passedMs(1000L)) {
         int timeS = (int)MathUtility.round2((double)((float)(50000L - this.pingTimer.getPassedTimeMs()) / 1000.0F));
         int dist = (int)(83.0D - Math.sqrt((double)PlayerUtility.getSquaredDistance2D(this.flightZonePos)));
         FontRenderers.sf_bold.drawCenteredString(context.method_51448(), ClientSettings.isRu() ? "Осталось " + timeS + " секунд и " + dist + " метров" : timeS + " seconds and " + dist + " meters left", (double)((float)mc.method_22683().method_4486() / 2.0F), (double)((float)mc.method_22683().method_4502() / 2.0F + 30.0F), -1);
      }

   }

   private void doSunrise() {
      if (mc.field_1724.field_5976) {
         this.acceleration = 0.0F;
      }

      if (mc.field_1724.field_5992) {
         this.acceleration = 0.0F;
         mc.field_1724.method_18800(mc.field_1724.method_18798().method_10216(), 0.41999998688697815D, mc.field_1724.method_18798().method_10215());
      }

      int elytra = InventoryUtility.getElytra();
      if (elytra != -1) {
         if (mc.field_1724.method_24828()) {
            mc.field_1724.method_6043();
         }

         if (this.disablerTicks-- <= 0) {
            this.matrixDisabler(elytra);
         }

         if (mc.field_1724.field_6017 > 0.25F) {
            MovementUtility.setMotion((double)Math.min((this.acceleration += 11.0F / (Float)this.xzSpeed.getValue()) / 100.0F, (Float)this.xzSpeed.getValue()));
            if (!MovementUtility.isMoving()) {
               this.acceleration = 0.0F;
            }

            if (class_3675.method_15987(mc.method_22683().method_4490(), ((Bind)this.bombKey.getValue()).getKey())) {
               MovementUtility.setMotion(0.800000011920929D);
               mc.field_1724.method_18800(mc.field_1724.method_18798().method_10216(), mc.field_1724.field_6012 % 2 == 0 ? 0.41999998688697815D : -0.41999998688697815D, mc.field_1724.method_18798().method_10215());
               this.acceleration = 70.0F;
            } else {
               switch(((ElytraPlus.AntiKick)this.antiKick.getValue()).ordinal()) {
               case 0:
                  mc.field_1724.method_18800(mc.field_1724.method_18798().method_10216(), 0.0D, mc.field_1724.method_18798().method_10215());
                  break;
               case 1:
                  mc.field_1724.method_18800(mc.field_1724.method_18798().method_10216(), mc.field_1724.field_6012 % 2 == 0 ? 0.08D : -0.08D, mc.field_1724.method_18798().method_10215());
                  break;
               case 2:
                  mc.field_1724.method_18800(mc.field_1724.method_18798().method_10216(), (double)(-0.01F - (mc.field_1724.field_6012 % 2 == 0 ? 1.0E-4F : 0.006F)), mc.field_1724.method_18798().method_10215());
               }
            }

            if (!mc.field_1724.method_5715() && mc.field_1690.field_1903.method_1434()) {
               mc.field_1724.method_18800(mc.field_1724.method_18798().method_10216(), (double)(Float)this.ySpeed.getValue(), mc.field_1724.method_18798().method_10215());
            }

            if (mc.field_1690.field_1832.method_1434()) {
               mc.field_1724.method_18800(mc.field_1724.method_18798().method_10216(), (double)(-(Float)this.ySpeed.getValue()), mc.field_1724.method_18798().method_10215());
            }
         }

      }
   }

   private void doBoost(EventMove e) {
      if (mc.field_1724.method_31548().method_5438(38).method_7909() == class_1802.field_8833 && mc.field_1724.method_6128() && !mc.field_1724.method_5799() && !mc.field_1724.method_5771() && mc.field_1724.method_6128()) {
         float moveForward = mc.field_1724.field_3913.field_3905;
         double heightPct;
         double pDist;
         if ((Boolean)this.cruiseControl.getValue()) {
            if (mc.field_1690.field_1903.method_1434()) {
               ++this.height;
            } else if (mc.field_1690.field_1832.method_1434()) {
               --this.height;
            }

            if ((Boolean)this.forceHeight.getValue()) {
               this.height = (float)(Integer)this.manualHeight.getValue();
            }

            if ((Boolean)this.twoBee.getValue()) {
               if (Managers.PLAYER.currentPlayerSpeed >= (Float)this.minUpSpeed.getValue()) {
                  mc.field_1724.method_36457((float)class_3532.method_15350(class_3532.method_15338(Math.toDegrees(Math.atan2(((double)this.height - mc.field_1724.method_23318()) * -1.0D, 10.0D))), -50.0D, 50.0D));
               } else {
                  mc.field_1724.method_36457(0.25F);
               }
            } else {
               heightPct = 1.0D - Math.sqrt(class_3532.method_15350((double)Managers.PLAYER.currentPlayerSpeed / 1.7D, 0.0D, 1.0D));
               if (Managers.PLAYER.currentPlayerSpeed >= (Float)this.minUpSpeed.getValue() && this.startTimer.passedMs((long)(2000.0F * (Float)this.redeployInterval.getValue()))) {
                  double pitch = -(44.4D * heightPct + 0.6D);
                  double diff = ((double)(this.height + 1.0F) - mc.field_1724.method_23318()) * 2.0D;
                  pDist = -Math.toDegrees(Math.atan2(Math.abs(diff), (double)Managers.PLAYER.currentPlayerSpeed * 30.0D)) * Math.signum(diff);
                  mc.field_1724.method_36457((float)(pitch + (pDist - pitch) * class_3532.method_15350(Math.abs(diff), 0.0D, 1.0D)));
               } else {
                  mc.field_1724.method_36457(0.25F);
                  moveForward = 1.0F;
               }
            }
         }

         if ((Boolean)this.twoBee.getValue()) {
            if (mc.field_1690.field_1903.method_1434() || !(Boolean)this.onlySpace.getValue() || (Boolean)this.cruiseControl.getValue()) {
               double[] m = MovementUtility.forwardWithoutStrafe((double)((Float)this.factor.getValue() / 10.0F));
               e.setX(e.getX() + m[0]);
               e.setZ(e.getZ() + m[1]);
            }
         } else {
            class_243 rotationVec = mc.field_1724.method_5828(Render3DEngine.getTickDelta());
            double d6 = Math.hypot(rotationVec.field_1352, rotationVec.field_1350);
            double currentSpeed = Math.hypot(e.getX(), e.getZ());
            float f4 = (float)(Math.pow(Math.cos(Math.toRadians((double)mc.field_1724.method_36455())), 2.0D) * Math.min(1.0D, rotationVec.method_1033() / 0.4D));
            e.setY(e.getY() + -0.08D + (double)f4 * 0.06D);
            if (e.getY() < 0.0D && d6 > 0.0D) {
               pDist = e.getY() * -0.1D * (double)f4;
               e.setY(e.getY() + pDist);
               e.setX(e.getX() + rotationVec.field_1352 * pDist / d6);
               e.setZ(e.getZ() + rotationVec.field_1350 * pDist / d6);
            }

            if (mc.field_1724.method_36455() < 0.0F) {
               pDist = currentSpeed * -Math.sin(Math.toRadians((double)mc.field_1724.method_36455())) * 0.04D;
               e.setY(e.getY() + pDist * 3.2D);
               e.setX(e.getX() - rotationVec.field_1352 * pDist / d6);
               e.setZ(e.getZ() - rotationVec.field_1350 * pDist / d6);
            }

            if (d6 > 0.0D) {
               e.setX(e.getX() + (rotationVec.field_1352 / d6 * currentSpeed - e.getX()) * 0.1D);
               e.setZ(e.getZ() + (rotationVec.field_1350 / d6 * currentSpeed - e.getZ()) * 0.1D);
            }

            if (mc.field_1724.method_36455() > 0.0F && e.getY() < 0.0D) {
               if (moveForward != 0.0F && this.startTimer.passedMs((long)(2000.0F * (Float)this.redeployInterval.getValue())) && this.redeployTimer.passedMs((long)(1000.0F * (Float)this.redeployTimeOut.getValue()))) {
                  if ((Boolean)this.stopMotion.getValue()) {
                     e.setX(0.0D);
                     e.setZ(0.0D);
                  }

                  this.startTimer.reset();
                  this.sendPacket(new class_2848(mc.field_1724, class_2849.field_12982));
               } else if (!this.startTimer.passedMs((long)(2000.0F * (Float)this.redeployInterval.getValue()))) {
                  e.setX(e.getX() - (double)moveForward * Math.sin(Math.toRadians((double)mc.field_1724.method_36454())) * (double)(Float)this.factor.getValue() / 20.0D);
                  e.setZ(e.getZ() + (double)moveForward * Math.cos(Math.toRadians((double)mc.field_1724.method_36454())) * (double)(Float)this.factor.getValue() / 20.0D);
                  this.redeployTimer.reset();
               }
            }
         }

         heightPct = Math.hypot(e.getX(), e.getZ());
         if ((Boolean)this.speedLimit.getValue() && heightPct > (double)(Float)this.maxSpeed.getValue()) {
            e.setX(e.getX() * (double)(Float)this.maxSpeed.getValue() / heightPct);
            e.setZ(e.getZ() * (double)(Float)this.maxSpeed.getValue() / heightPct);
         }

         mc.field_1724.method_18800(e.getX(), e.getY(), e.getZ());
         e.cancel();
      }
   }

   private void doControl(EventMove e) {
      if (mc.field_1724.method_31548().method_5438(38).method_7909() == class_1802.field_8833 && mc.field_1724.method_6128()) {
         double[] dir = MovementUtility.forward((double)((Float)this.xzSpeed.getValue() * (((BooleanSettingGroup)this.accelerate.getValue()).isEnabled() ? Math.min((this.acceleration += (Float)this.accelerateFactor.getValue()) / 100.0F, 1.0F) : 1.0F)));
         e.setX(dir[0]);
         e.setY(mc.field_1690.field_1903.method_1434() ? (double)(Float)this.upSpeed.getValue() : (mc.field_1690.field_1832.method_1434() ? (double)(-(Float)this.sneakDownSpeed.getValue()) : -0.08D * (double)(Float)this.downFactor.getValue()));
         e.setZ(dir[1]);
         if (!MovementUtility.isMoving()) {
            this.acceleration = 0.0F;
         }

         mc.field_1724.method_18800(e.getX(), e.getY(), e.getZ());
         e.cancel();
      }
   }

   public void matrixDisabler(int elytra) {
      elytra = elytra >= 0 && elytra < 9 ? elytra + 36 : elytra;
      if (elytra != -2) {
         mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, elytra, 1, class_1713.field_7790, mc.field_1724);
         mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, 6, 1, class_1713.field_7790, mc.field_1724);
      }

      mc.field_1724.field_3944.method_52787(new class_2848(mc.field_1724, class_2849.field_12982));
      if (elytra != -2) {
         mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, 6, 1, class_1713.field_7790, mc.field_1724);
         mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, elytra, 1, class_1713.field_7790, mc.field_1724);
      }

      this.disablerTicks = (Integer)this.disablerDelay.getValue();
   }

   private int getFireWorks(boolean hotbar) {
      return hotbar ? InventoryUtility.findItemInHotBar(class_1802.field_8639).slot() : InventoryUtility.findItemInInventory(class_1802.field_8639).slot();
   }

   private void noFireworks() {
      this.disable(ClientSettings.isRu() ? "Нету фейерверков в инвентаре!" : "No fireworks in the hotbar!");
      this.flying = false;
   }

   private void noElytra() {
      this.disable(ClientSettings.isRu() ? "Нету элитр в инвентаре!" : "No elytras found in the inventory!");
      this.flying = false;
   }

   private void reset() {
      this.slotWithFireWorks = -1;
      this.prevItemInHand = class_1802.field_8162;
      this.getStackInSlotCopy = null;
   }

   private void resetPrevItems() {
      this.prevElytraSlot = -1;
      this.prevArmorItem = class_1802.field_8162;
      this.prevArmorItemCopy = null;
   }

   private void moveFireworksToHotbar(int n2) {
      clickSlot(n2);
      clickSlot((Integer)this.fireSlot.getValue() - 1 + 36);
      clickSlot(n2);
   }

   private void returnItem() {
      if (this.slotWithFireWorks != -1 && this.getStackInSlotCopy != null && this.prevItemInHand != class_1802.field_8639 && this.prevItemInHand != class_1802.field_8162) {
         int n2 = findInInventory(this.getStackInSlotCopy, this.prevItemInHand);
         n2 = n2 < 9 && n2 != -1 ? n2 + 36 : n2;
         clickSlot(n2);
         clickSlot((Integer)this.fireSlot.getValue() - 1 + 36);
         clickSlot(n2);
      }
   }

   public static int findInInventory(class_1799 stack, class_1792 item) {
      if (stack == null) {
         return -1;
      } else {
         for(int i2 = 0; i2 < 45; ++i2) {
            class_1799 is = mc.field_1724.method_31548().method_5438(i2);
            if (class_1799.method_7984(is, stack) && is.method_7909() == item) {
               return i2;
            }
         }

         return -1;
      }
   }

   private int getFireworks() {
      if (mc.field_1724.method_6079().method_7909() == class_1802.field_8639) {
         return -2;
      } else {
         int firesInHotbar = this.getFireWorks(true);
         int firesInInventory = this.getFireWorks(false);
         if (firesInInventory == -1) {
            this.noFireworks();
            return -1;
         } else if (firesInHotbar == -1) {
            if (!(Boolean)this.allowFireSwap.getValue()) {
               this.disable(ClientSettings.isRu() ? "Нет фейерверков!" : "No fireworks!");
               return (Integer)this.fireSlot.getValue() - 1;
            } else {
               this.moveFireworksToHotbar(firesInInventory);
               return (Integer)this.fireSlot.getValue() - 1;
            }
         } else {
            return firesInHotbar;
         }
      }
   }

   private boolean canFly() {
      if (this.shouldSwapToElytra()) {
         return false;
      } else {
         return this.getFireworks() != -1;
      }
   }

   private boolean shouldSwapToElytra() {
      class_1799 is = mc.field_1724.method_6118(class_1304.field_6174);
      return is.method_7909() != class_1802.field_8833 || !class_1770.method_7804(is);
   }

   private void doFireWork(boolean started) {
      if (!started || !((float)(System.currentTimeMillis() - this.lastFireworkTime) < (Float)this.fireDelay.getValue() * 1000.0F)) {
         if (!((BooleanSettingGroup)this.grim.getValue()).isEnabled() || !(Boolean)this.fireWorkExtender.getValue() || !started || !this.pingTimer.passedMs(200L) || this.flightZonePos == null || !(PlayerUtility.getSquaredDistance2D(this.flightZonePos) < 7000.0F)) {
            if (!started || mc.field_1724.method_6128()) {
               if (started || Managers.PLAYER.ticksElytraFlying <= 1) {
                  int slot = this.getFireworks();
                  if (slot == -1) {
                     this.slotWithFireWorks = -1;
                  } else {
                     this.slotWithFireWorks = slot;
                     boolean inOffhand = mc.field_1724.method_6079().method_7909() == class_1802.field_8639;
                     int prevSlot = mc.field_1724.method_31548().field_7545;
                     if (!inOffhand && prevSlot != slot) {
                        this.sendPacket(new class_2868(slot));
                     }

                     this.sendSequencedPacket((id) -> {
                        return new class_2886(inOffhand ? class_1268.field_5810 : class_1268.field_5808, id, mc.field_1724.method_36454(), mc.field_1724.method_36455());
                     });
                     if (!inOffhand && prevSlot != mc.field_1724.method_31548().field_7545) {
                        this.sendPacket(new class_2868(prevSlot));
                     }

                     this.flying = true;
                     this.lastFireworkTime = System.currentTimeMillis();
                     this.pingTimer.reset();
                     this.flightZonePos = mc.field_1724.method_19538();
                  }
               }
            }
         }
      }
   }

   private void equipElytra() {
      int elytraSlot = InventoryUtility.getElytra();
      if (elytraSlot == -1 && mc.field_1724.field_7512.method_34255().method_7909() != class_1802.field_8833) {
         this.noElytra();
      } else if (this.shouldSwapToElytra()) {
         if (this.prevElytraSlot == -1) {
            class_1799 is = mc.field_1724.method_6118(class_1304.field_6174);
            this.prevElytraSlot = elytraSlot;
            this.prevArmorItem = is.method_7909();
            this.prevArmorItemCopy = is.method_7972();
         }

         clickSlot(elytraSlot);
         clickSlot(6);
         if (this.prevElytraSlot != -1) {
            clickSlot(this.prevElytraSlot);
         }

         this.elytraEquiped = true;
      }
   }

   private void returnChestPlate() {
      if (this.prevElytraSlot != -1 && this.prevArmorItem != class_1802.field_8162) {
         if (!this.elytraEquiped) {
            return;
         }

         class_1799 is = mc.field_1724.method_31548().method_5438(this.prevElytraSlot);
         boolean bl2 = is != class_1799.field_8037 && !class_1799.method_7984(is, this.prevArmorItemCopy);
         int n2 = findInInventory(this.prevArmorItemCopy, this.prevArmorItem);
         n2 = n2 < 9 && n2 != -1 ? n2 + 36 : n2;
         if (mc.field_1724.field_7512.method_34255().method_7909() != class_1802.field_8162) {
            clickSlot(6);
            if (this.prevElytraSlot != -1) {
               clickSlot(this.prevElytraSlot);
            }

            return;
         }

         if (n2 == -1) {
            return;
         }

         clickSlot(n2);
         clickSlot(6);
         if (!bl2) {
            clickSlot(n2);
         } else {
            int n4 = findEmpty(false);
            if (n4 != -1) {
               clickSlot(n4);
            }
         }
      }

      this.resetPrevItems();
   }

   public static int findEmpty(boolean hotbar) {
      for(int i2 = hotbar ? 0 : 9; i2 < (hotbar ? 9 : 45); ++i2) {
         if (mc.field_1724.method_31548().method_5438(i2).method_7960()) {
            return i2;
         }
      }

      return -1;
   }

   public void fireWorkOnPlayerUpdate() {
      boolean inAir = mc.field_1687.method_22347(class_2338.method_49638(mc.field_1724.method_19538()));
      boolean aboveLiquid = isAboveLiquid(0.1F) && inAir && mc.field_1724.method_18798().method_10214() < 0.0D;
      if ((!(mc.field_1724.field_6017 > 0.0F) || !inAir) && !aboveLiquid) {
         if (mc.field_1724.method_24828()) {
            this.started = false;
            return;
         }
      } else {
         this.equipElytra();
      }

      if (!MovementUtility.isMoving()) {
         this.acceleration = 0.0F;
      }

      if (this.canFly()) {
         if (!mc.field_1724.method_6128() && !this.started && mc.field_1724.method_18798().method_10214() < 0.0D) {
            this.sendPacket(new class_2848(mc.field_1724, class_2849.field_12982));
            this.started = true;
         }

         if (Managers.PLAYER.ticksElytraFlying < 4) {
            mc.field_1690.field_1903.method_23481(false);
         }

         this.doFireWork(true);
      }
   }

   public void fireworkOnSync() {
      if (((BooleanSettingGroup)this.grim.getValue()).isEnabled() && (Boolean)this.rotate.getValue()) {
         if (mc.field_1690.field_1903.method_1434() && mc.field_1724.method_6128() && this.flying) {
            mc.field_1724.method_36457(-45.0F);
         }

         if (mc.field_1690.field_1832.method_1434() && mc.field_1724.method_6128() && this.flying) {
            mc.field_1724.method_36457(45.0F);
         }

         mc.field_1724.method_36456(MovementUtility.getMoveDirection());
      }

      if (!MovementUtility.isMoving() && mc.field_1690.field_1903.method_1434() && mc.field_1724.method_6128() && this.flying) {
         mc.field_1724.method_36457(-90.0F);
      }

      if (Managers.PLAYER.ticksElytraFlying < 5 && !mc.field_1724.method_24828()) {
         mc.field_1724.method_36457(-45.0F);
      }

   }

   public void fireworkOnMove(EventMove e) {
      if (mc.field_1724.method_6128() && this.flying) {
         if (mc.field_1724.field_5976 || mc.field_1724.field_5992) {
            this.acceleration = 0.0F;
            this.accelerationY = 0.0F;
         }

         if (Managers.PLAYER.ticksElytraFlying < 4) {
            e.setY(0.20000000298023224D);
            e.cancel();
            return;
         }

         if (mc.field_1690.field_1903.method_1434()) {
            e.setY((double)((Float)this.ySpeed.getValue() * Math.min((this.accelerationY += 9.0F) / 100.0F, 1.0F)));
         } else if (mc.field_1690.field_1832.method_1434()) {
            e.setY((double)(-(Float)this.ySpeed.getValue() * Math.min((this.accelerationY += 9.0F) / 100.0F, 1.0F)));
         } else if ((Boolean)this.bowBomb.getValue() && checkGround(2.0F)) {
            e.setY(mc.field_1724.field_6012 % 2 == 0 ? 0.41999998688697815D : -0.41999998688697815D);
         } else {
            switch(((ElytraPlus.AntiKick)this.antiKick.getValue()).ordinal()) {
            case 0:
               e.setY(0.0D);
               break;
            case 1:
               e.setY(mc.field_1724.field_6012 % 2 == 0 ? 0.07999999821186066D : -0.07999999821186066D);
               break;
            case 2:
               e.setY(-0.07999999821186066D);
            }
         }

         if (!MovementUtility.isMoving()) {
            this.acceleration = 0.0F;
         }

         if (mc.field_1724.field_3913.field_3907 > 0.0F) {
            mc.field_1724.field_3913.field_3907 = 1.0F;
         } else if (mc.field_1724.field_3913.field_3907 < 0.0F) {
            mc.field_1724.field_3913.field_3907 = -1.0F;
         }

         MovementUtility.modifyEventSpeed(e, (double)((Float)this.xzSpeed.getValue() * Math.min((this.acceleration += 9.0F) / 100.0F, 1.0F)));
         if ((Boolean)this.stayMad.getValue() && !checkGround(3.0F) && Managers.PLAYER.ticksElytraFlying > 10) {
            e.setY(0.41999998688697815D);
         }

         e.cancel();
      }

   }

   public static boolean checkGround(float f2) {
      if (mc.field_1724.method_23318() < 0.0D) {
         return false;
      } else {
         return !mc.field_1687.method_20812(mc.field_1724, mc.field_1724.method_5829().method_989(0.0D, (double)(-f2), 0.0D)).iterator().hasNext();
      }
   }

   public static boolean isAboveLiquid(float offset) {
      return mc.field_1724 == null ? false : mc.field_1687.method_8320(class_2338.method_49637(mc.field_1724.method_23317(), mc.field_1724.method_23318() - (double)offset, mc.field_1724.method_23321())).method_26204() instanceof class_2404;
   }

   public void fireworkOnEnable() {
      if (mc.field_1724.method_6118(class_1304.field_6174).method_7909() != class_1802.field_8833 && mc.field_1724.field_7512.method_34255().method_7909() != class_1802.field_8833 && InventoryUtility.getElytra() == -1) {
         this.noElytra();
      } else if (this.getFireWorks(false) == -1) {
         this.noFireworks();
      } else if (this.getFireWorks(true) == -1) {
         this.getStackInSlotCopy = mc.field_1724.method_31548().method_5438((Integer)this.fireSlot.getValue() - 1).method_7972();
         this.prevItemInHand = mc.field_1724.method_31548().method_5438((Integer)this.fireSlot.getValue() - 1).method_7909();
      }
   }

   public void fireworkOnDisable() {
      this.started = false;
      if (!(Boolean)this.keepFlying.getValue()) {
         mc.field_1724.method_18800(0.0D, mc.field_1724.method_18798().method_10214(), 0.0D);
         (new Thread(() -> {
            this.sendPacket(new class_2848(mc.field_1724, class_2849.field_12982));
            TsunamiClient.TICK_TIMER = 0.1F;
            this.returnItem();
            this.reset();

            try {
               Thread.sleep(200L);
            } catch (InterruptedException var2) {
               TsunamiClient.TICK_TIMER = 1.0F;
               var2.printStackTrace();
            }

            this.returnChestPlate();
            this.resetPrevItems();
            TsunamiClient.TICK_TIMER = 1.0F;
         })).start();
      }
   }

   private boolean isBoxCollidingGround() {
      return mc.field_1687.method_20812(mc.field_1724, mc.field_1724.method_5829().method_1009(-0.25D, 0.0D, -0.25D).method_989(0.0D, -0.3D, 0.0D)).iterator().hasNext();
   }

   public static enum Mode {
      FireWork,
      SunriseOld,
      Boost,
      Control,
      Pitch40Infinite,
      SunriseNew,
      Packet;

      // $FF: synthetic method
      private static ElytraPlus.Mode[] $values() {
         return new ElytraPlus.Mode[]{FireWork, SunriseOld, Boost, Control, Pitch40Infinite, SunriseNew, Packet};
      }
   }

   public static enum NCPStrict {
      Off,
      Old,
      New,
      Motion;

      // $FF: synthetic method
      private static ElytraPlus.NCPStrict[] $values() {
         return new ElytraPlus.NCPStrict[]{Off, Old, New, Motion};
      }
   }

   public static enum AntiKick {
      Off,
      Jitter,
      Glide;

      // $FF: synthetic method
      private static ElytraPlus.AntiKick[] $values() {
         return new ElytraPlus.AntiKick[]{Off, Jitter, Glide};
      }
   }
}
