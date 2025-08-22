package tsunami.events.impl;

import net.minecraft.class_1657;
import tsunami.events.Event;

public class EventDeath extends Event {
   private final class_1657 player;

   public EventDeath(class_1657 player) {
      this.player = player;
   }

   public class_1657 getPlayer() {
      return this.player;
   }
}
