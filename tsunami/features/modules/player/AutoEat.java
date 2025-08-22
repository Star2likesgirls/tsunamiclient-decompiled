package tsunami.features.modules.player;

import baritone.api.BaritoneAPI;
import net.minecraft.class_1268;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_2868;
import net.minecraft.class_9334;
import tsunami.TsunamiClient;
import tsunami.features.modules.Module;
import tsunami.injection.accesors.IMinecraftClient;
import tsunami.setting.Setting;

public class AutoEat extends Module {
   public final Setting<Integer> hunger = new Setting("Hunger", 8, 0, 20);
   public final Setting<Boolean> gapple = new Setting("Gapple", false);
   public final Setting<Boolean> chorus = new Setting("Chorus", false);
   public final Setting<Boolean> rottenFlesh = new Setting("RottenFlesh", false);
   public final Setting<Boolean> spiderEye = new Setting("SpiderEye", false);
   public final Setting<Boolean> pufferfish = new Setting("Pufferfish", false);
   public final Setting<Boolean> swapBack = new Setting("SwapBack", true);
   public final Setting<Boolean> pauseBaritone = new Setting("PauseBaritone", true, (v) -> {
      return TsunamiClient.baritone;
   });
   private boolean eating;
   private int prevSlot;

   public AutoEat() {
      super("AutoEat", Module.Category.PLAYER);
   }

   public void onUpdate() {
      if (mc.field_1724.method_7344().method_7586() <= (Integer)this.hunger.getValue()) {
         boolean found;
         if (!this.isHandGood(class_1268.field_5808) && !this.isHandGood(class_1268.field_5810)) {
            found = this.switchToFood();
         } else {
            found = true;
         }

         if (!found) {
            if (this.eating) {
               this.stopEating();
            }

            return;
         }

         this.startEating();
      } else if (this.eating) {
         this.stopEating();
      }

   }

   public void startEating() {
      this.eating = true;
      if (mc.field_1755 != null && !mc.field_1724.method_6115()) {
         ((IMinecraftClient)mc).idoItemUse();
      } else {
         if ((Boolean)this.pauseBaritone.getValue() && TsunamiClient.baritone) {
            BaritoneAPI.getProvider().getPrimaryBaritone().getCommandManager().execute("pause");
         }

         mc.field_1690.field_1904.method_23481(true);
      }

   }

   public void stopEating() {
      this.eating = false;
      mc.field_1690.field_1904.method_23481(false);
      if ((Boolean)this.swapBack.getValue()) {
         mc.field_1724.method_31548().field_7545 = this.prevSlot;
      }

      if ((Boolean)this.pauseBaritone.getValue() && TsunamiClient.baritone) {
         BaritoneAPI.getProvider().getPrimaryBaritone().getCommandManager().execute("resume");
      }

   }

   public boolean switchToFood() {
      for(int i = 0; i < 9; ++i) {
         class_1799 stack = mc.field_1724.method_31548().method_5438(i);
         if (stack.method_57353().method_57832(class_9334.field_50075) && ((Boolean)this.gapple.getValue() || stack.method_7909() != class_1802.field_8463 && stack.method_7909() != class_1802.field_8367) && ((Boolean)this.chorus.getValue() || stack.method_7909() != class_1802.field_8233) && ((Boolean)this.rottenFlesh.getValue() || stack.method_7909() != class_1802.field_8511) && ((Boolean)this.spiderEye.getValue() || stack.method_7909() != class_1802.field_8680) && ((Boolean)this.pufferfish.getValue() || stack.method_7909() != class_1802.field_8323)) {
            this.prevSlot = mc.field_1724.method_31548().field_7545;
            mc.field_1724.method_31548().field_7545 = i;
            this.sendPacket(new class_2868(i));
            return true;
         }
      }

      return false;
   }

   private boolean isHandGood(class_1268 hand) {
      class_1799 stack = hand == class_1268.field_5808 ? mc.field_1724.method_6047() : mc.field_1724.method_6079();
      class_1792 item = stack.method_7909();
      return stack.method_57353().method_57832(class_9334.field_50075) && ((Boolean)this.gapple.getValue() || item != class_1802.field_8463 && item != class_1802.field_8367) && ((Boolean)this.chorus.getValue() || item != class_1802.field_8233) && ((Boolean)this.rottenFlesh.getValue() || item != class_1802.field_8511) && ((Boolean)this.spiderEye.getValue() || item != class_1802.field_8680) && ((Boolean)this.pufferfish.getValue() || item != class_1802.field_8323);
   }
}
