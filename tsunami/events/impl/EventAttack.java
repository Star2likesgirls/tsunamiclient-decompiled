package tsunami.events.impl;

import net.minecraft.class_1297;
import tsunami.events.Event;

public class EventAttack extends Event {
   private class_1297 entity;
   boolean pre;

   public EventAttack(class_1297 entity, boolean pre) {
      this.entity = entity;
      this.pre = pre;
   }

   public class_1297 getEntity() {
      return this.entity;
   }

   public boolean isPre() {
      return this.pre;
   }
}
