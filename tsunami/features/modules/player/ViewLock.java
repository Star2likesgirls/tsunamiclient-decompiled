package tsunami.features.modules.player;

import net.minecraft.class_4587;
import tsunami.features.modules.Module;
import tsunami.setting.Setting;

public class ViewLock extends Module {
   private final Setting<Boolean> lockCurrent = new Setting("LockCurrent", true);
   public final Setting<Boolean> pitch = new Setting("Pitch", true);
   public final Setting<Float> pitchValue = new Setting("PitchValue", 0.0F, -90.0F, 90.0F, (v) -> {
      return (Boolean)this.pitch.getValue();
   });
   public final Setting<Boolean> yaw = new Setting("Yaw", true);
   public final Setting<Float> yawValue = new Setting("YawValue", 0.0F, -180.0F, 180.0F, (v) -> {
      return (Boolean)this.yaw.getValue();
   });

   public ViewLock() {
      super("ViewLock", Module.Category.NONE);
   }

   public void onEnable() {
      if ((Boolean)this.lockCurrent.getValue()) {
         this.yawValue.setValue(mc.field_1724.method_36454());
         this.pitchValue.setValue(mc.field_1724.method_36455());
      }

   }

   public void onRender3D(class_4587 m) {
      if ((Boolean)this.pitch.getValue()) {
         mc.field_1724.method_36457((Float)this.pitchValue.getValue());
      }

      if ((Boolean)this.yaw.getValue()) {
         mc.field_1724.method_36456((Float)this.yawValue.getValue());
      }

   }
}
