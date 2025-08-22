package tsunami.events.impl;

import net.minecraft.class_2338;
import net.minecraft.class_2680;
import tsunami.events.Event;

public class EventSetBlockState extends Event {
   private final class_2338 pos;
   private final class_2680 state;
   private final class_2680 prevState;

   public EventSetBlockState(class_2338 pos, class_2680 state, class_2680 prevState) {
      this.pos = pos;
      this.state = state;
      this.prevState = prevState;
   }

   public class_2338 getPos() {
      return this.pos;
   }

   public class_2680 getState() {
      return this.state;
   }

   public class_2680 getPrevState() {
      return this.prevState;
   }
}
