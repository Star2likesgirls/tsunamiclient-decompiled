package tsunami.features.modules.movement;

import java.util.Iterator;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_1268;
import net.minecraft.class_1294;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_1531;
import net.minecraft.class_1657;
import net.minecraft.class_1690;
import net.minecraft.class_1713;
import net.minecraft.class_1802;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_259;
import net.minecraft.class_265;
import net.minecraft.class_2815;
import net.minecraft.class_2846;
import net.minecraft.class_2848;
import net.minecraft.class_2868;
import net.minecraft.class_2885;
import net.minecraft.class_3965;
import net.minecraft.class_2828.class_2830;
import net.minecraft.class_2846.class_2847;
import net.minecraft.class_2848.class_2849;
import tsunami.TsunamiClient;
import tsunami.core.Managers;
import tsunami.core.manager.client.ModuleManager;
import tsunami.events.impl.EventMove;
import tsunami.events.impl.EventPlayerTravel;
import tsunami.events.impl.EventPostTick;
import tsunami.events.impl.EventSync;
import tsunami.events.impl.EventTick;
import tsunami.events.impl.PostPlayerUpdateEvent;
import tsunami.features.modules.Module;
import tsunami.features.modules.client.ClientSettings;
import tsunami.injection.accesors.IInteractionManager;
import tsunami.setting.Setting;
import tsunami.utility.interfaces.IEntity;
import tsunami.utility.player.InventoryUtility;
import tsunami.utility.player.MovementUtility;
import tsunami.utility.player.SearchInvResult;

public class Speed extends Module {
   public final Setting<Speed.Mode> mode;
   public Setting<Boolean> useTimer;
   public Setting<Boolean> pauseInLiquids;
   public Setting<Boolean> pauseWhileSneaking;
   public final Setting<Integer> hurttime;
   public final Setting<Float> boostFactor;
   public final Setting<Boolean> allowOffGround;
   public final Setting<Integer> shiftTicks;
   public final Setting<Integer> fireWorkSlot;
   public final Setting<Integer> delay;
   public final Setting<Boolean> strict;
   public final Setting<Float> matrixJBSpeed;
   public final Setting<Boolean> armorStands;
   public double baseSpeed;
   private int stage;
   private int ticks;
   private int prevSlot;
   private float prevForward;
   private tsunami.utility.Timer elytraDelay;
   private tsunami.utility.Timer startDelay;

   public Speed() {
      super("Speed", Module.Category.NONE);
      this.mode = new Setting("Mode", Speed.Mode.NCP);
      this.useTimer = new Setting("Use Timer", false);
      this.pauseInLiquids = new Setting("PauseInLiquids", false);
      this.pauseWhileSneaking = new Setting("PauseWhileSneaking", false);
      this.hurttime = new Setting("HurtTime", 0, 0, 10, (v) -> {
         return this.mode.is(Speed.Mode.MatrixDamage);
      });
      this.boostFactor = new Setting("BoostFactor", 2.0F, 0.0F, 10.0F, (v) -> {
         return this.mode.is(Speed.Mode.MatrixDamage) || this.mode.is(Speed.Mode.Vanilla);
      });
      this.allowOffGround = new Setting("AllowOffGround", true, (v) -> {
         return this.mode.is(Speed.Mode.MatrixDamage);
      });
      this.shiftTicks = new Setting("ShiftTicks", 0, 0, 10, (v) -> {
         return this.mode.is(Speed.Mode.MatrixDamage);
      });
      this.fireWorkSlot = new Setting("FireSlot", 1, 1, 9, (v) -> {
         return this.mode.getValue() == Speed.Mode.FireWork;
      });
      this.delay = new Setting("Delay", 8, 1, 20, (v) -> {
         return this.mode.getValue() == Speed.Mode.FireWork;
      });
      this.strict = new Setting("Strict", false, (v) -> {
         return this.mode.is(Speed.Mode.GrimIce);
      });
      this.matrixJBSpeed = new Setting("TimerSpeed", 1.088F, 1.0F, 2.0F, (v) -> {
         return this.mode.is(Speed.Mode.MatrixJB);
      });
      this.armorStands = new Setting("ArmorStands", false, (v) -> {
         return this.mode.is(Speed.Mode.GrimCombo) || this.mode.is(Speed.Mode.GrimEntity2);
      });
      this.prevForward = 0.0F;
      this.elytraDelay = new tsunami.utility.Timer();
      this.startDelay = new tsunami.utility.Timer();
   }

   public void onDisable() {
      TsunamiClient.TICK_TIMER = 1.0F;
   }

   public void onEnable() {
      this.stage = 1;
      this.ticks = 0;
      this.baseSpeed = 0.2873D;
      this.startDelay.reset();
      this.prevSlot = -1;
   }

   @EventHandler
   public void onSync(EventSync e) {
      if ((!mc.field_1724.method_52535() || !(Boolean)this.pauseInLiquids.getValue()) && (!mc.field_1724.method_5715() || !(Boolean)this.pauseWhileSneaking.getValue())) {
         if (this.mode.getValue() == Speed.Mode.MatrixJB) {
            boolean closeToGround = false;
            Iterator var3 = mc.field_1687.method_20812(mc.field_1724, mc.field_1724.method_5829().method_1009(0.5D, 0.0D, 0.5D).method_989(0.0D, -1.0D, 0.0D)).iterator();

            while(var3.hasNext()) {
               class_265 a = (class_265)var3.next();
               if (a != class_259.method_1073()) {
                  closeToGround = true;
                  break;
               }
            }

            if (MovementUtility.isMoving() && closeToGround && mc.field_1724.field_6017 <= 0.0F) {
               TsunamiClient.TICK_TIMER = 1.0F;
               mc.field_1724.method_24830(true);
               mc.field_1724.method_6043();
            } else if (mc.field_1724.field_6017 > 0.0F && (Boolean)this.useTimer.getValue()) {
               TsunamiClient.TICK_TIMER = (Float)this.matrixJBSpeed.getValue();
               mc.field_1724.method_5762(0.0D, -0.003000000026077032D, 0.0D);
            }
         }

      }
   }

   @EventHandler
   public void modifyVelocity(EventPlayerTravel e) {
      if (this.mode.getValue() == Speed.Mode.GrimEntity && !e.isPre() && TsunamiClient.core.getSetBackTime() > 1000L) {
         Iterator var2 = Managers.ASYNC.getAsyncPlayers().iterator();

         while(var2.hasNext()) {
            class_1657 ent = (class_1657)var2.next();
            if (ent != mc.field_1724 && mc.field_1724.method_5858(ent) <= 2.25D) {
               float p = mc.field_1687.method_8320(((IEntity)mc.field_1724).thunderHack_Recode$getVelocityBP()).method_26204().method_9499();
               float f = mc.field_1724.method_24828() ? p * 0.91F : 0.91F;
               float f2 = mc.field_1724.method_24828() ? p : 0.99F;
               mc.field_1724.method_18800(mc.field_1724.method_18798().method_10216() / (double)f * (double)f2, mc.field_1724.method_18798().method_10214(), mc.field_1724.method_18798().method_10215() / (double)f * (double)f2);
               break;
            }
         }
      }

      if ((this.mode.is(Speed.Mode.GrimEntity2) || this.mode.is(Speed.Mode.GrimCombo)) && !e.isPre() && TsunamiClient.core.getSetBackTime() > 1000L && MovementUtility.isMoving()) {
         int collisions = 0;
         Iterator var8 = mc.field_1687.method_18112().iterator();

         while(true) {
            class_1297 ent;
            do {
               do {
                  do {
                     if (!var8.hasNext()) {
                        double[] motion = MovementUtility.forward(0.08D * (double)collisions);
                        mc.field_1724.method_5762(motion[0], 0.0D, motion[1]);
                        return;
                     }

                     ent = (class_1297)var8.next();
                  } while(ent == mc.field_1724);
               } while(ent instanceof class_1531 && !(Boolean)this.armorStands.getValue());
            } while(!(ent instanceof class_1309) && !(ent instanceof class_1690));

            if (mc.field_1724.method_5829().method_1014(1.0D).method_994(ent.method_5829())) {
               ++collisions;
            }
         }
      }
   }

   @EventHandler
   public void onTick(EventTick e) {
      if ((this.mode.is(Speed.Mode.GrimIce) || this.mode.is(Speed.Mode.GrimCombo)) && mc.field_1724.method_24828()) {
         class_2338 pos = ((IEntity)mc.field_1724).thunderHack_Recode$getVelocityBP();
         SearchInvResult result = InventoryUtility.findBlockInHotBar(class_2246.field_10295, class_2246.field_10225, class_2246.field_10384);
         if (mc.field_1687.method_22347(pos) || !result.found() || !mc.field_1690.field_1903.method_1434()) {
            return;
         }

         this.prevSlot = mc.field_1724.method_31548().field_7545;
         result.switchTo();
         this.sendPacket(new class_2830(mc.field_1724.method_23317(), mc.field_1724.method_23318(), mc.field_1724.method_23321(), mc.field_1724.method_36454(), 90.0F, mc.field_1724.method_24828()));
         if ((Boolean)this.strict.getValue()) {
            this.sendPacket(new class_2846(class_2847.field_12968, pos, class_2350.field_11036));
            this.sendPacket(new class_2846(class_2847.field_12971, pos, class_2350.field_11036));
         }

         this.sendPacket(new class_2846(class_2847.field_12973, pos, class_2350.field_11036));
         this.sendSequencedPacket((id) -> {
            return new class_2885(class_1268.field_5808, new class_3965(pos.method_10074().method_46558().method_1031(0.0D, 0.5D, 0.0D), class_2350.field_11036, pos.method_10074(), false), id);
         });
         mc.field_1687.method_8501(pos, class_2246.field_10295.method_9564());
      }

   }

   @EventHandler
   public void onPostTick(EventPostTick e) {
      if ((this.mode.is(Speed.Mode.GrimIce) || this.mode.is(Speed.Mode.GrimCombo)) && this.prevSlot != -1) {
         mc.field_1724.method_31548().field_7545 = this.prevSlot;
         ((IInteractionManager)mc.field_1761).syncSlot();
         this.prevSlot = -1;
      }

   }

   public void onUpdate() {
      if ((!mc.field_1724.method_52535() || !(Boolean)this.pauseInLiquids.getValue()) && (!mc.field_1724.method_5715() || !(Boolean)this.pauseWhileSneaking.getValue())) {
         int ellySlot;
         if (this.mode.getValue() == Speed.Mode.FireWork) {
            --this.ticks;
            ellySlot = InventoryUtility.getElytra();
            int fireSlot = InventoryUtility.findItemInHotBar(class_1802.field_8639).slot();
            boolean inOffHand = mc.field_1724.method_6079().method_7909() == class_1802.field_8639;
            int prevSlot;
            if (fireSlot == -1) {
               prevSlot = InventoryUtility.findItemInInventory(class_1802.field_8639).slot();
               if (prevSlot != -1) {
                  mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, prevSlot, (Integer)this.fireWorkSlot.getValue() - 1, class_1713.field_7791, mc.field_1724);
               }
            }

            if (ellySlot != -1 && (fireSlot != -1 || inOffHand) && !mc.field_1724.method_24828() && mc.field_1724.field_6017 > 0.0F && this.ticks <= 0) {
               if (ellySlot != -2) {
                  mc.field_1761.method_2906(0, ellySlot, 1, class_1713.field_7790, mc.field_1724);
                  mc.field_1761.method_2906(0, 6, 1, class_1713.field_7790, mc.field_1724);
               }

               mc.field_1724.field_3944.method_52787(new class_2848(mc.field_1724, class_2849.field_12982));
               prevSlot = mc.field_1724.method_31548().field_7545;
               if (prevSlot != fireSlot && !inOffHand) {
                  this.sendPacket(new class_2868(fireSlot));
               }

               mc.field_1761.method_2919(mc.field_1724, inOffHand ? class_1268.field_5810 : class_1268.field_5808);
               if (prevSlot != fireSlot && !inOffHand) {
                  this.sendPacket(new class_2868(prevSlot));
               }

               if (ellySlot != -2) {
                  mc.field_1761.method_2906(0, 6, 1, class_1713.field_7790, mc.field_1724);
                  mc.field_1761.method_2906(0, ellySlot, 1, class_1713.field_7790, mc.field_1724);
               }

               mc.field_1724.field_3944.method_52787(new class_2815(mc.field_1724.field_7512.field_7763));
               this.ticks = (Integer)this.delay.getValue();
            }
         }

         if (this.mode.getValue() == Speed.Mode.ElytraLowHop) {
            if (mc.field_1724.method_24828()) {
               mc.field_1724.method_6043();
               return;
            }

            if (mc.field_1687.method_20812(mc.field_1724, mc.field_1724.method_5829().method_1009(-0.29D, 0.0D, -0.29D).method_989(0.0D, -3.0D, 0.0D)).iterator().hasNext() && this.elytraDelay.passedMs(150L) && this.startDelay.passedMs(500L)) {
               ellySlot = InventoryUtility.getElytra();
               if (ellySlot == -1) {
                  this.disable(ClientSettings.isRu() ? "Для этого режима нужна элитра!" : "You need elytra for this mode!");
               } else {
                  Strafe.disabler(ellySlot);
               }

               mc.field_1724.method_18800(mc.field_1724.method_18798().method_10216(), 0.0D, mc.field_1724.method_18798().method_10215());
               if (MovementUtility.isMoving()) {
                  MovementUtility.setMotion(0.85D);
               }

               this.elytraDelay.reset();
            }
         }

      }
   }

   @EventHandler
   public void onPostPlayerUpdate(PostPlayerUpdateEvent event) {
      if (this.mode.getValue() == Speed.Mode.MatrixDamage && MovementUtility.isMoving() && mc.field_1724.field_6235 > (Integer)this.hurttime.getValue()) {
         if (mc.field_1724.method_24828()) {
            MovementUtility.setMotion((double)(0.387F * (Float)this.boostFactor.getValue()));
         } else if (mc.field_1724.method_5799()) {
            MovementUtility.setMotion((double)(0.346F * (Float)this.boostFactor.getValue()));
         } else if (!mc.field_1724.method_24828() && (Boolean)this.allowOffGround.getValue()) {
            MovementUtility.setMotion((double)(0.448F * (Float)this.boostFactor.getValue()));
         }

         if ((Integer)this.shiftTicks.getValue() > 0) {
            event.cancel();
            event.setIterations((Integer)this.shiftTicks.getValue());
         }
      }

   }

   @EventHandler
   public void onMove(EventMove event) {
      if ((!mc.field_1724.method_52535() || !(Boolean)this.pauseInLiquids.getValue()) && (!mc.field_1724.method_5715() || !(Boolean)this.pauseWhileSneaking.getValue())) {
         if (this.mode.getValue() == Speed.Mode.NCP || this.mode.getValue() == Speed.Mode.StrictStrafe) {
            if (!mc.field_1724.method_31549().field_7479) {
               if (!mc.field_1724.method_6128()) {
                  if (mc.field_1724.method_7344().method_7586() > 6) {
                     if (!event.isCancelled()) {
                        event.cancel();
                        if (MovementUtility.isMoving()) {
                           TsunamiClient.TICK_TIMER = (Boolean)this.useTimer.getValue() ? 1.088F : 1.0F;
                           float currentSpeed = this.mode.getValue() == Speed.Mode.NCP && mc.field_1724.field_3913.field_3905 <= 0.0F && this.prevForward > 0.0F ? Managers.PLAYER.currentPlayerSpeed * 0.66F : Managers.PLAYER.currentPlayerSpeed;
                           boolean canJump = !mc.field_1724.field_5976 || ModuleManager.step.isDisabled();
                           if (this.stage == 1 && mc.field_1724.method_24828() && canJump) {
                              mc.field_1724.method_18800(mc.field_1724.method_18798().field_1352, MovementUtility.getJumpSpeed(), mc.field_1724.method_18798().field_1350);
                              event.setY(MovementUtility.getJumpSpeed());
                              this.baseSpeed *= 2.149D;
                              this.stage = 2;
                           } else if (this.stage == 2) {
                              this.baseSpeed = (double)currentSpeed - 0.66D * ((double)currentSpeed - MovementUtility.getBaseMoveSpeed());
                              this.stage = 3;
                           } else {
                              if (mc.field_1687.method_20812(mc.field_1724, mc.field_1724.method_5829().method_989(0.0D, mc.field_1724.method_18798().method_10214(), 0.0D)).iterator().hasNext() || mc.field_1724.field_5992) {
                                 this.stage = 1;
                              }

                              this.baseSpeed = (double)currentSpeed - (double)currentSpeed / 159.0D;
                           }

                           this.baseSpeed = Math.max(this.baseSpeed, MovementUtility.getBaseMoveSpeed());
                           double ncpSpeed = this.mode.getValue() != Speed.Mode.StrictStrafe && !(mc.field_1724.field_3913.field_3905 < 1.0F) ? 0.576D : 0.465D;
                           double ncpBypassSpeed = this.mode.getValue() != Speed.Mode.StrictStrafe && !(mc.field_1724.field_3913.field_3905 < 1.0F) ? 0.57D : 0.44D;
                           double amplifier;
                           if (mc.field_1724.method_6059(class_1294.field_5904)) {
                              amplifier = (double)mc.field_1724.method_6112(class_1294.field_5904).method_5578();
                              ncpSpeed *= 1.0D + 0.2D * (amplifier + 1.0D);
                              ncpBypassSpeed *= 1.0D + 0.2D * (amplifier + 1.0D);
                           }

                           if (mc.field_1724.method_6059(class_1294.field_5909)) {
                              amplifier = (double)mc.field_1724.method_6112(class_1294.field_5909).method_5578();
                              ncpSpeed /= 1.0D + 0.2D * (amplifier + 1.0D);
                              ncpBypassSpeed /= 1.0D + 0.2D * (amplifier + 1.0D);
                           }

                           this.baseSpeed = Math.min(this.baseSpeed, this.ticks > 25 ? ncpSpeed : ncpBypassSpeed);
                           if (this.ticks++ > 50) {
                              this.ticks = 0;
                           }

                           MovementUtility.modifyEventSpeed(event, this.baseSpeed);
                           this.prevForward = mc.field_1724.field_3913.field_3905;
                        } else {
                           TsunamiClient.TICK_TIMER = 1.0F;
                           event.setX(0.0D);
                           event.setZ(0.0D);
                        }

                     }
                  }
               }
            }
         }
      }
   }

   public static enum Mode {
      StrictStrafe,
      MatrixJB,
      NCP,
      ElytraLowHop,
      MatrixDamage,
      GrimEntity,
      GrimEntity2,
      FireWork,
      Vanilla,
      GrimIce,
      GrimCombo;

      // $FF: synthetic method
      private static Speed.Mode[] $values() {
         return new Speed.Mode[]{StrictStrafe, MatrixJB, NCP, ElytraLowHop, MatrixDamage, GrimEntity, GrimEntity2, FireWork, Vanilla, GrimIce, GrimCombo};
      }
   }
}
