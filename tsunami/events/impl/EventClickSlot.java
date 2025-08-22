package tsunami.events.impl;

import net.minecraft.class_1713;
import tsunami.events.Event;

public class EventClickSlot extends Event {
   private final class_1713 slotActionType;
   private final int slot;
   private final int button;
   private final int id;

   public EventClickSlot(class_1713 slotActionType, int slot, int button, int id) {
      this.slot = slot;
      this.button = button;
      this.id = id;
      this.slotActionType = slotActionType;
   }

   public class_1713 getSlotActionType() {
      return this.slotActionType;
   }

   public int getSlot() {
      return this.slot;
   }

   public int getButton() {
      return this.button;
   }

   public int getId() {
      return this.id;
   }
}
