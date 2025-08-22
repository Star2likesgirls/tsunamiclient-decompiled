package tsunami.features.modules.movement;

import java.util.ArrayList;
import java.util.Iterator;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_2338;
import net.minecraft.class_243;
import net.minecraft.class_3532;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tsunami.TsunamiClient;
import tsunami.events.impl.EventKeyboardInput;
import tsunami.events.impl.EventMove;
import tsunami.events.impl.EventPlayerJump;
import tsunami.events.impl.EventPlayerTravel;
import tsunami.features.modules.Module;
import tsunami.features.modules.client.ClientSettings;
import tsunami.setting.Setting;
import tsunami.setting.impl.SettingGroup;
import tsunami.utility.player.InteractionUtility;
import tsunami.utility.world.HoleUtility;

public class HoleSnap extends Module {
   private final Setting<HoleSnap.Mode> mode;
   private final Setting<Integer> searchRange;
   private final Setting<Integer> searchFOV;
   private final Setting<Boolean> useTimer;
   private final Setting<Float> timerValue;
   private final Setting<SettingGroup> autoDisable;
   private final Setting<Boolean> onDeath;
   private final Setting<Boolean> onInHole;
   private final Setting<Boolean> onNoHoleFound;
   private class_2338 hole;
   private float prevClientYaw;

   public HoleSnap() {
      super("HoleSnap", Module.Category.NONE);
      this.mode = new Setting("Mode", HoleSnap.Mode.Yaw);
      this.searchRange = new Setting("Search Range", 5, 1, 20);
      this.searchFOV = new Setting("Search FOV", 360, 1, 360);
      this.useTimer = new Setting("Use Timer", false);
      this.timerValue = new Setting("Timer Value", 1.0F, 0.0F, 20.0F);
      this.autoDisable = new Setting("Auto Disable", new SettingGroup(false, 0));
      this.onDeath = (new Setting("On Death", true)).addToGroup(this.autoDisable);
      this.onInHole = (new Setting("In Hole", true)).addToGroup(this.autoDisable);
      this.onNoHoleFound = (new Setting("No Holes", false)).addToGroup(this.autoDisable);
   }

   public void onEnable() {
      this.hole = this.findHole();
      if ((Boolean)this.useTimer.getValue()) {
         TsunamiClient.TICK_TIMER = (Float)this.timerValue.getValue();
      }

   }

   public void onDisable() {
      this.hole = null;
      if ((Boolean)this.useTimer.getValue()) {
         TsunamiClient.TICK_TIMER = 1.0F;
      }

   }

   public void onUpdate() {
      if ((Boolean)this.onDeath.getValue() && mc.field_1724 != null && (mc.field_1724.method_6032() + mc.field_1724.method_6067() <= 0.0F || mc.field_1724.method_29504())) {
         this.disable(ClientSettings.isRu() ? "Вы умерли! Выключаюсь..." : "You died! Disabling...");
      }

      if (mc.field_1724 != null && mc.field_1724.method_24515().equals(this.hole) && (Boolean)this.onInHole.getValue()) {
         this.disable(ClientSettings.isRu() ? "Ты в холке! Отключаю.." : "You're in a hole already! Disabling..");
      }

   }

   @EventHandler(
      priority = -200
   )
   private void onMove(EventMove event) {
      if (mc.field_1724 != null) {
         class_2338 bp = class_2338.method_49638(mc.field_1724.method_19538());

         for(int i = 1; i < 5; ++i) {
            if (HoleUtility.isSingleHole(bp.method_10087(i))) {
               class_243 center = new class_243(Math.floor(mc.field_1724.method_23317()) + 0.5D, mc.field_1724.method_23318(), Math.floor(mc.field_1724.method_23321()) + 0.5D);
               if (center.method_1022(mc.field_1724.method_19538()) < 0.15000000596046448D) {
                  event.setX(0.0D);
                  event.setZ(0.0D);
                  event.cancel();
               }
               break;
            }
         }

         if (mc.field_1724 != null && mc.field_1724.field_5976 && mc.field_1724.method_24828()) {
            mc.field_1724.method_6043();
         }

         if (this.mode.getValue() == HoleSnap.Mode.Move && this.hole != null) {
            double newYaw = Math.cos(Math.toRadians((double)(this.getNewYaw(this.hole.method_46558()) + 90.0F)));
            double newPitch = Math.sin(Math.toRadians((double)(this.getNewYaw(this.hole.method_46558()) + 90.0F)));
            double diffX = this.hole.method_46558().method_10216() - mc.field_1724.method_23317();
            double diffZ = this.hole.method_46558().method_10215() - mc.field_1724.method_23321();
            double x = 0.29D * newYaw;
            double z = 0.29D * newPitch;
            event.setX(Math.abs(x) < Math.abs(diffX) ? x : diffX);
            event.setZ(Math.abs(z) < Math.abs(diffZ) ? z : diffZ);
            event.cancel();
         }

      }
   }

   @EventHandler
   public void modifyVelocity(EventPlayerTravel event) {
      if (mc.field_1724 != null) {
         if (mc.field_1724.field_6012 % 10 == 0) {
            this.hole = this.findHole();
         }

         this.doYawModeLogic(event.isPre());
      }
   }

   @EventHandler
   public void modifyJump(EventPlayerJump event) {
      if (mc.field_1724 != null) {
         this.doYawModeLogic(event.isPre());
      }
   }

   @EventHandler
   public void onKeyboardInput(EventKeyboardInput e) {
      if (mc.field_1724 != null && this.mode.getValue() == HoleSnap.Mode.Yaw && this.hole != null) {
         mc.field_1724.field_3913.field_3905 = 1.0F;
      }
   }

   @Nullable
   private class_2338 findHole() {
      if (mc.field_1724 == null) {
         return null;
      } else {
         ArrayList<class_2338> blocks = new ArrayList();
         class_2338 centerPos = mc.field_1724.method_24515();

         class_2338 bp;
         for(int i = centerPos.method_10263() - (Integer)this.searchRange.getValue(); i < centerPos.method_10263() + (Integer)this.searchRange.getValue(); ++i) {
            for(int j = centerPos.method_10264() - 4; j < centerPos.method_10264() + 2; ++j) {
               for(int k = centerPos.method_10260() - (Integer)this.searchRange.getValue(); k < centerPos.method_10260() + (Integer)this.searchRange.getValue(); ++k) {
                  bp = new class_2338(i, j, k);
                  if (HoleUtility.isSingleHole(bp) && InteractionUtility.isVecInFOV(bp.method_46558(), (Integer)this.searchFOV.getValue() / 2)) {
                     blocks.add(new class_2338(bp));
                  }
               }
            }
         }

         float nearestDistance = 10.0F;
         class_2338 fbp = null;
         Iterator var9 = blocks.iterator();

         while(var9.hasNext()) {
            bp = (class_2338)var9.next();
            if (class_2338.method_49638(mc.field_1724.method_19538()).equals(bp) && (Boolean)this.onInHole.getValue()) {
               this.disable(ClientSettings.isRu() ? "Ты в холке! Отключаю.." : "You're in a hole already! Disabling..");
               return null;
            }

            if (mc.field_1724.method_5707(bp.method_46558()) < (double)nearestDistance) {
               nearestDistance = (float)mc.field_1724.method_5707(bp.method_46558());
               fbp = bp;
            }
         }

         if (fbp == null && (Boolean)this.onNoHoleFound.getValue()) {
            this.disable(ClientSettings.isRu() ? "Холка не найдена! Выключение..." : "Hole not found! Disabling...");
         }

         return fbp;
      }
   }

   private float getNewYaw(@NotNull class_243 pos) {
      return mc.field_1724 == null ? 0.0F : mc.field_1724.method_36454() + class_3532.method_15393((float)Math.toDegrees(Math.atan2(pos.method_10215() - mc.field_1724.method_23321(), pos.method_10216() - mc.field_1724.method_23317())) - mc.field_1724.method_36454() - 90.0F);
   }

   private void doYawModeLogic(boolean isPreEvent) {
      if (this.hole != null && mc.field_1724 != null && this.mode.getValue() == HoleSnap.Mode.Yaw) {
         if (isPreEvent) {
            this.prevClientYaw = mc.field_1724.method_36454();
            mc.field_1724.method_36456(InteractionUtility.calculateAngle(this.hole.method_46558())[0]);
         } else {
            mc.field_1724.method_36456(this.prevClientYaw);
         }

      }
   }

   private static enum Mode {
      Move,
      Yaw;

      // $FF: synthetic method
      private static HoleSnap.Mode[] $values() {
         return new HoleSnap.Mode[]{Move, Yaw};
      }
   }

   private static enum JumpMode {
      Normal,
      Move;

      // $FF: synthetic method
      private static HoleSnap.JumpMode[] $values() {
         return new HoleSnap.JumpMode[]{Normal, Move};
      }
   }
}
