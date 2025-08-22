package tsunami.events.impl;

import net.minecraft.class_2338;
import tsunami.events.Event;

public class EventBreakBlock extends Event {
   private class_2338 bp;

   public EventBreakBlock(class_2338 bp) {
      this.bp = bp;
   }

   public class_2338 getPos() {
      return this.bp;
   }
}
