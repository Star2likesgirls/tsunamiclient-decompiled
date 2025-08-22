package tsunami.events.impl;

import net.minecraft.class_243;
import tsunami.events.Event;

public class EventFixVelocity extends Event {
   class_243 movementInput;
   float speed;
   float yaw;
   class_243 velocity;

   public EventFixVelocity(class_243 movementInput, float speed, float yaw, class_243 velocity) {
      this.movementInput = movementInput;
      this.speed = speed;
      this.yaw = yaw;
      this.velocity = velocity;
   }

   public class_243 getMovementInput() {
      return this.movementInput;
   }

   public float getSpeed() {
      return this.speed;
   }

   public class_243 getVelocity() {
      return this.velocity;
   }

   public void setVelocity(class_243 velocity) {
      this.velocity = velocity;
   }
}
