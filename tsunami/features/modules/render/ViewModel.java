package tsunami.features.modules.render;

import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_1268;
import net.minecraft.class_7833;
import tsunami.events.impl.EventHeldItemRenderer;
import tsunami.events.impl.EventSetting;
import tsunami.features.modules.Module;
import tsunami.setting.Setting;
import tsunami.setting.impl.SettingGroup;
import tsunami.utility.render.Render2DEngine;
import tsunami.utility.render.Render3DEngine;

public class ViewModel extends Module {
   public final Setting<Boolean> syncHands = new Setting("SyncHands", true);
   public final Setting<SettingGroup> mainHand = new Setting("MainHand", new SettingGroup(false, 0));
   public final Setting<Float> positionMainX;
   public final Setting<Float> positionMainY;
   public final Setting<Float> positionMainZ;
   public final Setting<Float> scaleMain;
   public final Setting<SettingGroup> rotationMain;
   public final Setting<Float> rotationMainX;
   public final Setting<Float> rotationMainY;
   public final Setting<Float> rotationMainZ;
   public final Setting<SettingGroup> animateMain;
   public final Setting<Boolean> animateMainX;
   public final Setting<Boolean> animateMainY;
   public final Setting<Boolean> animateMainZ;
   public final Setting<Float> speedAnimateMain;
   public final Setting<SettingGroup> offHand;
   public final Setting<Float> positionOffX;
   public final Setting<Float> positionOffY;
   public final Setting<Float> positionOffZ;
   public final Setting<Float> scaleOff;
   public final Setting<SettingGroup> rotationOff;
   public final Setting<Float> rotationOffX;
   public final Setting<Float> rotationOffY;
   public final Setting<Float> rotationOffZ;
   public final Setting<SettingGroup> animateOff;
   public final Setting<Boolean> animateOffX;
   public final Setting<Boolean> animateOffY;
   public final Setting<Boolean> animateOffZ;
   public final Setting<Float> speedAnimateOff;
   public final Setting<SettingGroup> eatMod;
   public final Setting<Float> eatX;
   public final Setting<Float> eatY;
   private float prevMainX;
   private float prevMainY;
   private float prevMainZ;
   private float prevOffX;
   private float prevOffY;
   private float prevOffZ;

   public ViewModel() {
      super("ViewModel", Module.Category.RENDER);
      this.positionMainX = (new Setting("positionMainX", 0.0F, -3.0F, 3.0F)).addToGroup(this.mainHand);
      this.positionMainY = (new Setting("positionMainY", 0.0F, -3.0F, 3.0F)).addToGroup(this.mainHand);
      this.positionMainZ = (new Setting("positionMainZ", 0.0F, -3.0F, 3.0F)).addToGroup(this.mainHand);
      this.scaleMain = (new Setting("ScaleMain", 1.0F, 0.1F, 1.5F)).addToGroup(this.mainHand);
      this.rotationMain = (new Setting("Rotation", new SettingGroup(false, 1))).addToGroup(this.mainHand);
      this.rotationMainX = (new Setting("rotationMainX", 0.0F, -180.0F, 180.0F)).addToGroup(this.rotationMain);
      this.rotationMainY = (new Setting("rotationMainY", 0.0F, -180.0F, 180.0F)).addToGroup(this.rotationMain);
      this.rotationMainZ = (new Setting("rotationMainZ", 0.0F, -180.0F, 180.0F)).addToGroup(this.rotationMain);
      this.animateMain = (new Setting("Animate", new SettingGroup(false, 1))).addToGroup(this.mainHand);
      this.animateMainX = (new Setting("animateMainX", false)).addToGroup(this.animateMain);
      this.animateMainY = (new Setting("animateMainY", false)).addToGroup(this.animateMain);
      this.animateMainZ = (new Setting("animateMainZ", false)).addToGroup(this.animateMain);
      this.speedAnimateMain = (new Setting("speedAnimateMain", 1.0F, 1.0F, 5.0F)).addToGroup(this.rotationMain);
      this.offHand = new Setting("OffHand", new SettingGroup(false, 0));
      this.positionOffX = (new Setting("positionOffX", 0.0F, -3.0F, 3.0F)).addToGroup(this.offHand);
      this.positionOffY = (new Setting("positionOffY", 0.0F, -3.0F, 3.0F)).addToGroup(this.offHand);
      this.positionOffZ = (new Setting("positionOffZ", 0.0F, -3.0F, 3.0F)).addToGroup(this.offHand);
      this.scaleOff = (new Setting("ScaleOff", 1.0F, 0.1F, 1.5F)).addToGroup(this.offHand);
      this.rotationOff = (new Setting("RotationOff", new SettingGroup(false, 1))).addToGroup(this.offHand);
      this.rotationOffX = (new Setting("rotationOffX", 0.0F, -180.0F, 180.0F)).addToGroup(this.rotationOff);
      this.rotationOffY = (new Setting("rotationOffY", 0.0F, -180.0F, 180.0F)).addToGroup(this.rotationOff);
      this.rotationOffZ = (new Setting("rotationOffZ", 0.0F, -180.0F, 180.0F)).addToGroup(this.rotationOff);
      this.animateOff = (new Setting("AnimateOff", new SettingGroup(false, 1))).addToGroup(this.offHand);
      this.animateOffX = (new Setting("animateOffX", false)).addToGroup(this.animateOff);
      this.animateOffY = (new Setting("animateOffY", false)).addToGroup(this.animateOff);
      this.animateOffZ = (new Setting("animateOffZ", false)).addToGroup(this.animateOff);
      this.speedAnimateOff = (new Setting("speedAnimateOff", 1.0F, 1.0F, 5.0F)).addToGroup(this.rotationOff);
      this.eatMod = new Setting("Eat", new SettingGroup(false, 0));
      this.eatX = (new Setting("EatX", 1.0F, -1.0F, 2.0F)).addToGroup(this.eatMod);
      this.eatY = (new Setting("EatY", 1.0F, -1.0F, 2.0F)).addToGroup(this.eatMod);
   }

   private float rotate(float value, float speed) {
      return value - speed <= 180.0F && value - speed > -180.0F ? value - speed : 180.0F;
   }

   @EventHandler
   public void onSettingChange(EventSetting e) {
      if ((Boolean)this.syncHands.getValue()) {
         if (e.getSetting() == this.positionMainX) {
            this.positionOffX.setValueSilent((Float)this.positionMainX.getValue());
         }

         if (e.getSetting() == this.positionMainY) {
            this.positionOffY.setValueSilent((Float)this.positionMainY.getValue());
         }

         if (e.getSetting() == this.positionMainZ) {
            this.positionOffZ.setValueSilent((Float)this.positionMainZ.getValue());
         }

         if (e.getSetting() == this.positionOffX) {
            this.positionMainX.setValueSilent((Float)this.positionOffX.getValue());
         }

         if (e.getSetting() == this.positionOffY) {
            this.positionMainY.setValueSilent((Float)this.positionOffY.getValue());
         }

         if (e.getSetting() == this.positionOffZ) {
            this.positionMainZ.setValueSilent((Float)this.positionOffZ.getValue());
         }

         if (e.getSetting() == this.scaleMain) {
            this.scaleOff.setValueSilent((Float)this.scaleMain.getValue());
         }

         if (e.getSetting() == this.scaleOff) {
            this.scaleMain.setValueSilent((Float)this.scaleOff.getValue());
         }

      }
   }

   public void onUpdate() {
      this.prevMainX = (Float)this.rotationMainX.getValue();
      this.prevMainY = (Float)this.rotationMainY.getValue();
      this.prevMainZ = (Float)this.rotationMainZ.getValue();
      this.prevOffX = (Float)this.rotationOffX.getValue();
      this.prevOffY = (Float)this.rotationOffY.getValue();
      this.prevOffZ = (Float)this.rotationOffZ.getValue();
      if ((Boolean)this.animateMainX.getValue()) {
         this.rotationMainX.setValue(this.rotate((Float)this.rotationMainX.getValue(), (Float)this.speedAnimateMain.getValue()));
      }

      if ((Boolean)this.animateMainY.getValue()) {
         this.rotationMainY.setValue(this.rotate((Float)this.rotationMainY.getValue(), (Float)this.speedAnimateMain.getValue()));
      }

      if ((Boolean)this.animateMainZ.getValue()) {
         this.rotationMainZ.setValue(this.rotate((Float)this.rotationMainZ.getValue(), (Float)this.speedAnimateMain.getValue()));
      }

      if ((Boolean)this.animateOffX.getValue()) {
         this.rotationOffX.setValue(this.rotate((Float)this.rotationOffX.getValue(), (Float)this.speedAnimateOff.getValue()));
      }

      if ((Boolean)this.animateOffY.getValue()) {
         this.rotationOffY.setValue(this.rotate((Float)this.rotationOffY.getValue(), (Float)this.speedAnimateOff.getValue()));
      }

      if ((Boolean)this.animateOffZ.getValue()) {
         this.rotationOffZ.setValue(this.rotate((Float)this.rotationOffZ.getValue(), (Float)this.speedAnimateOff.getValue()));
      }

   }

   @EventHandler
   private void onHeldItemRender(EventHeldItemRenderer event) {
      if (event.getHand() == class_1268.field_5808) {
         event.getStack().method_46416((Float)this.positionMainX.getValue(), (Float)this.positionMainY.getValue(), (Float)this.positionMainZ.getValue());
         event.getStack().method_22905((Float)this.scaleMain.getValue(), (Float)this.scaleMain.getValue(), (Float)this.scaleMain.getValue());
         event.getStack().method_22907(class_7833.field_40714.rotationDegrees(Render2DEngine.interpolateFloat(this.prevMainX, (Float)this.rotationMainX.getValue(), (double)Render3DEngine.getTickDelta())));
         event.getStack().method_22907(class_7833.field_40716.rotationDegrees(Render2DEngine.interpolateFloat(this.prevMainY, (Float)this.rotationMainY.getValue(), (double)Render3DEngine.getTickDelta())));
         event.getStack().method_22907(class_7833.field_40718.rotationDegrees(Render2DEngine.interpolateFloat(this.prevMainZ, (Float)this.rotationMainZ.getValue(), (double)Render3DEngine.getTickDelta())));
      } else {
         event.getStack().method_46416(-(Float)this.positionOffX.getValue(), (Float)this.positionOffY.getValue(), (Float)this.positionOffZ.getValue());
         event.getStack().method_22905((Float)this.scaleOff.getValue(), (Float)this.scaleOff.getValue(), (Float)this.scaleOff.getValue());
         event.getStack().method_22907(class_7833.field_40714.rotationDegrees(Render2DEngine.interpolateFloat(this.prevOffX, (Float)this.rotationOffX.getValue(), (double)Render3DEngine.getTickDelta())));
         event.getStack().method_22907(class_7833.field_40716.rotationDegrees(Render2DEngine.interpolateFloat(this.prevOffY, (Float)this.rotationOffY.getValue(), (double)Render3DEngine.getTickDelta())));
         event.getStack().method_22907(class_7833.field_40718.rotationDegrees(Render2DEngine.interpolateFloat(this.prevOffZ, (Float)this.rotationOffZ.getValue(), (double)Render3DEngine.getTickDelta())));
      }

   }
}
