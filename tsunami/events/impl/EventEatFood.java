package tsunami.events.impl;

import net.minecraft.class_1799;
import tsunami.events.Event;

public class EventEatFood extends Event {
   private final class_1799 stack;

   public EventEatFood(class_1799 stack) {
      this.stack = stack;
   }

   public class_1799 getFood() {
      return this.stack;
   }
}
