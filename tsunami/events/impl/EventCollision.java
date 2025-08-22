package tsunami.events.impl;

import net.minecraft.class_2338;
import net.minecraft.class_2680;
import tsunami.events.Event;

public class EventCollision extends Event {
   private class_2680 bs;
   private class_2338 bp;

   public EventCollision(class_2680 bs, class_2338 bp) {
      this.bs = bs;
      this.bp = bp;
   }

   public class_2680 getState() {
      return this.bs;
   }

   public class_2338 getPos() {
      return this.bp;
   }

   public void setState(class_2680 bs) {
      this.bs = bs;
   }
}
