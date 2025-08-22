package tsunami.events.impl;

import net.minecraft.class_243;
import tsunami.events.Event;

public class EventPlayerTravel extends Event {
   private class_243 mVec;
   private boolean pre;

   public EventPlayerTravel(class_243 mVec, boolean pre) {
      this.mVec = mVec;
      this.pre = pre;
   }

   public class_243 getmVec() {
      return this.mVec;
   }

   public boolean isPre() {
      return this.pre;
   }
}
