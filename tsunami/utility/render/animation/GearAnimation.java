package tsunami.utility.render.animation;

import tsunami.features.modules.client.ClickGui;
import tsunami.utility.render.Render2DEngine;
import tsunami.utility.render.Render3DEngine;

public class GearAnimation {
   private float espValue = 1.0F;
   private float prevEspValue;
   private float espSpeed = 1.0F;
   private boolean flipSpeed;

   public float getValue() {
      return Render2DEngine.interpolateFloat(this.prevEspValue, this.espValue, (double)Render3DEngine.getTickDelta());
   }

   public void tick() {
      this.prevEspValue = this.espValue;
      this.espValue += this.espSpeed;
      if (this.espSpeed > (float)(Integer)ClickGui.gearStop.getValue()) {
         this.flipSpeed = true;
      }

      if (this.espSpeed < (float)(-(Integer)ClickGui.gearStop.getValue())) {
         this.flipSpeed = false;
      }

      this.espSpeed = this.flipSpeed ? this.espSpeed - (Float)ClickGui.gearDuration.getValue() : this.espSpeed + (Float)ClickGui.gearDuration.getValue();
   }
}
