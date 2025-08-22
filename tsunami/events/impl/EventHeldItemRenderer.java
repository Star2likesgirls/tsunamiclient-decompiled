package tsunami.events.impl;

import net.minecraft.class_1268;
import net.minecraft.class_1799;
import net.minecraft.class_4587;
import tsunami.events.Event;

public class EventHeldItemRenderer extends Event {
   private final class_1268 hand;
   private final class_1799 item;
   private float ep;
   private final class_4587 stack;

   public EventHeldItemRenderer(class_1268 hand, class_1799 item, float equipProgress, class_4587 stack) {
      this.hand = hand;
      this.item = item;
      this.ep = equipProgress;
      this.stack = stack;
   }

   public class_1268 getHand() {
      return this.hand;
   }

   public class_1799 getItem() {
      return this.item;
   }

   public float getEp() {
      return this.ep;
   }

   public class_4587 getStack() {
      return this.stack;
   }
}
