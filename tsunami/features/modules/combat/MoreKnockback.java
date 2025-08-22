package tsunami.features.modules.combat;

import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_1294;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_1511;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_2824;
import net.minecraft.class_2848;
import net.minecraft.class_2848.class_2849;
import tsunami.core.manager.client.ModuleManager;
import tsunami.events.impl.PacketEvent;
import tsunami.features.modules.Module;
import tsunami.setting.Setting;
import tsunami.utility.math.MathUtility;
import tsunami.utility.player.MovementUtility;

public class MoreKnockback extends Module {
   public Setting<Boolean> inMove = new Setting("InMove", true);
   public Setting<Integer> hurtTime = new Setting("HurtTime", 10, 0, 10);
   public Setting<Integer> chance = new Setting("Chance", 100, 0, 100);

   public MoreKnockback() {
      super("MoreKnockback", Module.Category.NONE);
   }

   @EventHandler
   public void onSendPacket(PacketEvent.Send event) {
      if ((!MovementUtility.isMoving() || (Boolean)this.inMove.getValue()) && event.getPacket() instanceof class_2824 && Criticals.getInteractType((class_2824)event.getPacket()) == Criticals.InteractType.ATTACK && !(Criticals.getEntity((class_2824)event.getPacket()) instanceof class_1511)) {
         class_1297 var3 = Criticals.getEntity((class_2824)event.getPacket());
         if (var3 instanceof class_1309) {
            class_1309 lent = (class_1309)var3;
            if (lent.field_6235 <= (Integer)this.hurtTime.getValue() && MathUtility.random(0.0F, 100.0F) >= (float)(100 - (Integer)this.chance.getValue()) && !this.canCrit()) {
               if (mc.field_1724.method_5624()) {
                  this.sendPacket(new class_2848(mc.field_1724, class_2849.field_12985));
               }

               this.sendPacket(new class_2848(mc.field_1724, class_2849.field_12981));
               this.sendPacket(new class_2848(mc.field_1724, class_2849.field_12985));
               this.sendPacket(new class_2848(mc.field_1724, class_2849.field_12981));
               this.debug("wtap");
               mc.field_1724.method_5728(true);
               mc.field_1724.field_3919 = true;
            }
         }
      }

   }

   private boolean canCrit() {
      boolean reasonForSkipCrit = mc.field_1724.method_31549().field_7479 || mc.field_1724.method_6128() || ModuleManager.elytraPlus.isEnabled() || mc.field_1724.method_6059(class_1294.field_5919) || mc.field_1687.method_8320(class_2338.method_49638(mc.field_1724.method_19538())).method_26204() == class_2246.field_10343 || mc.field_1724.method_5771() || mc.field_1724.method_5869();
      if (mc.field_1724.method_7261(0.5F) < 0.9F) {
         return false;
      } else if (ModuleManager.criticals.isEnabled() && !ModuleManager.criticals.mode.is(Criticals.Mode.Grim)) {
         return true;
      } else if (ModuleManager.criticals.isEnabled() && ModuleManager.criticals.mode.is(Criticals.Mode.Grim) && !mc.field_1724.method_24828()) {
         return true;
      } else if (reasonForSkipCrit) {
         return false;
      } else {
         return !mc.field_1724.method_24828() && mc.field_1724.field_6017 > 0.0F;
      }
   }
}
