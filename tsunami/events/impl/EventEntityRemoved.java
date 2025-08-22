package tsunami.events.impl;

import net.minecraft.class_1297;
import tsunami.events.Event;

public class EventEntityRemoved extends Event {
   public class_1297 entity;

   public EventEntityRemoved(class_1297 entity) {
      this.entity = entity;
   }

   public class_1297 getEntity() {
      return this.entity;
   }
}
