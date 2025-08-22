package tsunami.events.impl;

import net.minecraft.class_2248;
import net.minecraft.class_2338;
import tsunami.events.Event;

public class EventPlaceBlock extends Event {
   private final class_2338 blockPos;
   private final class_2248 block;

   public EventPlaceBlock(class_2338 blockPos, class_2248 block) {
      this.blockPos = blockPos;
      this.block = block;
   }

   public class_2338 getBlockPos() {
      return this.blockPos;
   }

   public class_2248 getBlock() {
      return this.block;
   }
}
