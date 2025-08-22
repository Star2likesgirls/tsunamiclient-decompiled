package tsunami.events.impl;

import net.minecraft.class_2338;
import net.minecraft.class_2350;
import tsunami.events.Event;

public class EventAttackBlock extends Event {
   private class_2338 blockPos;
   private class_2350 enumFacing;

   public EventAttackBlock(class_2338 blockPos, class_2350 enumFacing) {
      this.blockPos = blockPos;
      this.enumFacing = enumFacing;
   }

   public class_2338 getBlockPos() {
      return this.blockPos;
   }

   public void setBlockPos(class_2338 blockPos) {
      this.blockPos = blockPos;
   }

   public class_2350 getEnumFacing() {
      return this.enumFacing;
   }

   public void setEnumFacing(class_2350 enumFacing) {
      this.enumFacing = enumFacing;
   }
}
