package tsunami.features.modules.movement;

import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_1294;
import net.minecraft.class_1713;
import net.minecraft.class_238;
import net.minecraft.class_2708;
import net.minecraft.class_2743;
import net.minecraft.class_2848;
import net.minecraft.class_2338.class_2339;
import net.minecraft.class_2350.class_2351;
import net.minecraft.class_2848.class_2849;
import tsunami.core.Core;
import tsunami.core.manager.client.ModuleManager;
import tsunami.events.impl.EventMove;
import tsunami.events.impl.EventSprint;
import tsunami.events.impl.EventSync;
import tsunami.events.impl.PacketEvent;
import tsunami.events.impl.PlayerUpdateEvent;
import tsunami.features.modules.Module;
import tsunami.injection.accesors.ISPacketEntityVelocity;
import tsunami.setting.Setting;
import tsunami.utility.player.InventoryUtility;
import tsunami.utility.player.MovementUtility;

public class Strafe extends Module {
   private final Setting<Strafe.Boost> boost;
   private final Setting<Float> setSpeed;
   private final Setting<Float> velReduction;
   private final Setting<Float> maxVelocitySpeed;
   private final Setting<Boolean> sunrise;
   public static double oldSpeed;
   public static double contextFriction;
   public static double fovval;
   public static boolean needSwap;
   public static boolean needSprintState;
   public static boolean disabled;
   public static int noSlowTicks;
   static long disableTime;

   public Strafe() {
      super("Strafe", Module.Category.NONE);
      this.boost = new Setting("Boost", Strafe.Boost.None);
      this.setSpeed = new Setting("speed", 1.3F, 0.0F, 2.0F, (v) -> {
         return this.boost.getValue() == Strafe.Boost.Elytra;
      });
      this.velReduction = new Setting("Reduction", 6.0F, 0.1F, 10.0F, (v) -> {
         return this.boost.getValue() == Strafe.Boost.Damage;
      });
      this.maxVelocitySpeed = new Setting("MaxVelocity", 0.8F, 0.1F, 2.0F, (v) -> {
         return this.boost.getValue() == Strafe.Boost.Damage;
      });
      this.sunrise = new Setting("Sunrise", true, (v) -> {
         return this.boost.getValue() == Strafe.Boost.Elytra;
      });
   }

   public double calculateSpeed(EventMove move) {
      float speedAttributes = this.getAIMoveSpeed();
      float frictionFactor = mc.field_1687.method_8320((new class_2339()).method_10102(mc.field_1724.method_23317(), this.getBoundingBox().method_1001(class_2351.field_11052) - move.getY(), mc.field_1724.method_23321())).method_26204().method_9499() * 0.91F;
      float n6 = mc.field_1724.method_6059(class_1294.field_5913) && mc.field_1724.method_6115() ? 0.88F : (float)(oldSpeed > 0.32D && mc.field_1724.method_6115() ? 0.88D : 0.9100000262260437D);
      if (mc.field_1724.method_24828()) {
         n6 = frictionFactor;
      }

      float n7 = (float)(0.163100004196167D / Math.pow((double)n6, 3.0D));
      float n8;
      if (mc.field_1724.method_24828()) {
         n8 = speedAttributes * n7;
         if (move.getY() > 0.0D) {
            n8 += this.boost.getValue() == Strafe.Boost.Elytra && InventoryUtility.getElytra() != -1 && disabled && System.currentTimeMillis() - disableTime < 300L ? 0.65F : 0.2F;
         }

         disabled = false;
      } else {
         n8 = 0.0255F;
      }

      boolean noslow = false;
      double max2 = oldSpeed + (double)n8;
      double max = 0.0D;
      if (mc.field_1724.method_6115() && move.getY() <= 0.0D && !(Boolean)this.sunrise.getValue()) {
         double n10 = oldSpeed + (double)n8 * 0.25D;
         double motionY2 = move.getY();
         if (motionY2 != 0.0D && Math.abs(motionY2) < 0.08D) {
            n10 += 0.055D;
         }

         if (max2 > (max = Math.max(0.043D, n10))) {
            noslow = true;
            ++noSlowTicks;
         } else {
            noSlowTicks = Math.max(noSlowTicks - 1, 0);
         }
      } else {
         noSlowTicks = 0;
      }

      if (noSlowTicks > 3) {
         max2 = max - 0.019D;
      } else {
         max2 = Math.max(noslow ? 0.0D : 0.25D, max2) - (mc.field_1724.field_6012 % 2 == 0 ? 0.001D : 0.002D);
      }

      contextFriction = (double)n6;
      if (!mc.field_1724.method_24828()) {
         needSprintState = !mc.field_1724.field_3919;
         needSwap = true;
      } else {
         needSprintState = false;
      }

      return max2;
   }

   public float getAIMoveSpeed() {
      boolean prevSprinting = mc.field_1724.method_5624();
      mc.field_1724.method_5728(false);
      float speed = mc.field_1724.method_6029() * 1.3F;
      mc.field_1724.method_5728(prevSprinting);
      return speed;
   }

   public static void disabler(int elytra) {
      if (elytra != -1) {
         if (System.currentTimeMillis() - disableTime > 190L) {
            if (elytra != -2) {
               mc.field_1761.method_2906(0, elytra, 1, class_1713.field_7790, mc.field_1724);
               mc.field_1761.method_2906(0, 6, 1, class_1713.field_7790, mc.field_1724);
            }

            mc.field_1724.field_3944.method_52787(new class_2848(mc.field_1724, class_2849.field_12982));
            mc.field_1724.field_3944.method_52787(new class_2848(mc.field_1724, class_2849.field_12982));
            if (elytra != -2) {
               mc.field_1761.method_2906(0, 6, 1, class_1713.field_7790, mc.field_1724);
               mc.field_1761.method_2906(0, elytra, 1, class_1713.field_7790, mc.field_1724);
            }

            disableTime = System.currentTimeMillis();
         }

         disabled = true;
      }
   }

   public void onEnable() {
      oldSpeed = 0.0D;
      fovval = (Double)mc.field_1690.method_42454().method_41753();
      mc.field_1690.method_42454().method_41748(0.0D);
   }

   public void onDisable() {
      mc.field_1690.method_42454().method_41748(fovval);
   }

   public boolean canStrafe() {
      if (mc.field_1724.method_5715()) {
         return false;
      } else if (mc.field_1724.method_5771()) {
         return false;
      } else if (ModuleManager.scaffold.isEnabled()) {
         return false;
      } else if (ModuleManager.speed.isEnabled()) {
         return false;
      } else if (mc.field_1724.method_5869()) {
         return false;
      } else {
         return !mc.field_1724.method_31549().field_7479;
      }
   }

   public class_238 getBoundingBox() {
      return new class_238(mc.field_1724.method_23317() - 0.1D, mc.field_1724.method_23318(), mc.field_1724.method_23321() - 0.1D, mc.field_1724.method_23317() + 0.1D, mc.field_1724.method_23318() + 1.0D, mc.field_1724.method_23321() + 0.1D);
   }

   @EventHandler
   public void onMove(EventMove event) {
      int elytraSlot = InventoryUtility.getElytra();
      if (this.boost.getValue() == Strafe.Boost.Elytra && elytraSlot != -1 && MovementUtility.isMoving() && !mc.field_1724.method_24828() && mc.field_1687.method_20812(mc.field_1724, mc.field_1724.method_5829().method_989(0.0D, event.getY(), 0.0D)).iterator().hasNext() && disabled) {
         oldSpeed = (double)(Float)this.setSpeed.getValue();
      }

      if (this.canStrafe()) {
         if (MovementUtility.isMoving()) {
            double[] motions = MovementUtility.forward(this.calculateSpeed(event));
            event.setX(motions[0]);
            event.setZ(motions[1]);
         } else {
            oldSpeed = 0.0D;
            event.setX(0.0D);
            event.setZ(0.0D);
         }

         event.cancel();
      } else {
         oldSpeed = 0.0D;
      }

   }

   @EventHandler
   public void onSync(EventSync e) {
      oldSpeed = Math.hypot(mc.field_1724.method_23317() - mc.field_1724.field_6014, mc.field_1724.method_23321() - mc.field_1724.field_5969) * contextFriction;
   }

   @EventHandler
   public void onPacketReceive(PacketEvent.Receive e) {
      if (e.getPacket() instanceof class_2708) {
         oldSpeed = 0.0D;
      }

      class_2743 velocity;
      if (e.getPacket() instanceof class_2743 && (velocity = (class_2743)e.getPacket()).method_11818() == mc.field_1724.method_5628() && this.boost.getValue() == Strafe.Boost.Damage) {
         if (mc.field_1724.method_24828()) {
            return;
         }

         double vX = velocity.method_11815();
         double vZ = velocity.method_11819();
         if (vX < 0.0D) {
            vX *= -1.0D;
         }

         if (vZ < 0.0D) {
            vZ *= -1.0D;
         }

         oldSpeed = (vX + vZ) / (double)((Float)this.velReduction.getValue() * 1000.0F);
         oldSpeed = Math.min(oldSpeed, (double)(Float)this.maxVelocitySpeed.getValue());
         ((ISPacketEntityVelocity)velocity).setMotionX(0);
         ((ISPacketEntityVelocity)velocity).setMotionY(0);
         ((ISPacketEntityVelocity)velocity).setMotionZ(0);
      }

   }

   @EventHandler
   public void actionEvent(EventSprint eventAction) {
      if (this.canStrafe() && Core.serverSprint != needSprintState) {
         eventAction.setSprintState(!Core.serverSprint);
      }

      if (needSwap) {
         eventAction.setSprintState(!mc.field_1724.field_3919);
         needSwap = false;
      }

   }

   @EventHandler
   public void onUpdate(PlayerUpdateEvent event) {
      if (this.boost.getValue() == Strafe.Boost.Elytra && InventoryUtility.getElytra() != -1 && !mc.field_1724.method_24828() && mc.field_1724.field_6017 > 0.0F && !disabled && (!mc.field_1687.method_20812(mc.field_1724, mc.field_1724.method_5829().method_989(0.0D, -1.100000023841858D, 0.0D)).iterator().hasNext() || !(Boolean)this.sunrise.getValue())) {
         disabler(InventoryUtility.getElytra());
      }

   }

   private static enum Boost {
      None,
      Elytra,
      Damage;

      // $FF: synthetic method
      private static Strafe.Boost[] $values() {
         return new Strafe.Boost[]{None, Elytra, Damage};
      }
   }
}
