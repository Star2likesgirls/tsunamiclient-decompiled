package tsunami.events.impl;

import net.minecraft.class_1657;
import tsunami.events.Event;

public class TotemPopEvent extends Event {
   private final class_1657 entity;
   private int pops;

   public TotemPopEvent(class_1657 entity, int pops) {
      this.entity = entity;
      this.pops = pops;
   }

   public class_1657 getEntity() {
      return this.entity;
   }

   public int getPops() {
      return this.pops;
   }
}
