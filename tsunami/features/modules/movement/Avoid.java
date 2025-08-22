package tsunami.features.modules.movement;

import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2440;
import net.minecraft.class_2537;
import net.minecraft.class_2538;
import tsunami.events.impl.EventCollision;
import tsunami.features.modules.Module;
import tsunami.setting.Setting;

public class Avoid extends Module {
   private final Setting<Boolean> voidAir = new Setting("Void", true);
   private final Setting<Boolean> cactus = new Setting("Cactus", true);
   private final Setting<Boolean> fire = new Setting("Fire", true);
   private final Setting<Boolean> berryBush = new Setting("BerryBush", true);
   private final Setting<Boolean> powderSnow = new Setting("PowderSnow", true);
   private final Setting<Boolean> unloaded = new Setting("Unloaded", true);
   private final Setting<Boolean> lava = new Setting("Lava", true);
   private final Setting<Boolean> plate = new Setting("Plate", true);
   private final Setting<Boolean> trapString = new Setting("Tripwire", true);

   public Avoid() {
      super("Avoid", Module.Category.MOVEMENT);
   }

   @EventHandler
   public void onCollide(EventCollision e) {
      if (!fullNullCheck()) {
         class_2248 b = e.getState().method_26204();
         boolean avoidUnloaded = !mc.field_1687.method_8393(e.getPos().method_10263() >> 4, e.getPos().method_10260() >> 4) && (Boolean)this.unloaded.getValue();
         boolean avoidVoid = e.getPos().method_10264() < mc.field_1687.method_31607() && (Boolean)this.voidAir.getValue();
         boolean avoidCactus = b == class_2246.field_10029 && (Boolean)this.cactus.getValue();
         boolean avoidFire = (b == class_2246.field_10036 || b == class_2246.field_22089) && (Boolean)this.fire.getValue();
         boolean avoidBerryBush = b == class_2246.field_16999 && (Boolean)this.berryBush.getValue();
         boolean avoidSusSnow = b == class_2246.field_27879 && (Boolean)this.powderSnow.getValue();
         boolean avoidLava = b == class_2246.field_10164 && (Boolean)this.lava.getValue();
         boolean avoidPlate = (b instanceof class_2440 || b == class_2246.field_10224 || b == class_2246.field_10582) && (Boolean)this.plate.getValue();
         boolean avoidTrapString = b instanceof class_2538 && (Boolean)e.getState().method_11654(class_2537.field_11669) && (Boolean)this.trapString.getValue();
         if (avoidUnloaded || avoidFire || avoidCactus || avoidLava || avoidBerryBush || avoidSusSnow || avoidPlate || avoidTrapString || avoidVoid) {
            e.setState(class_2246.field_10566.method_9564());
         }

      }
   }
}
