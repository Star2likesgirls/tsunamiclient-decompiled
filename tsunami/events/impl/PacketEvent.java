package tsunami.events.impl;

import net.minecraft.class_2596;
import tsunami.events.Event;

public class PacketEvent extends Event {
   private final class_2596<?> packet;

   public PacketEvent(class_2596<?> packet) {
      this.packet = packet;
   }

   public <T extends class_2596<?>> T getPacket() {
      return this.packet;
   }

   public static class ReceivePost extends PacketEvent {
      public ReceivePost(class_2596<?> packet) {
         super(packet);
      }
   }

   public static class SendPost extends PacketEvent {
      public SendPost(class_2596<?> packet) {
         super(packet);
      }
   }

   public static class Receive extends PacketEvent {
      public Receive(class_2596<?> packet) {
         super(packet);
      }
   }

   public static class Send extends PacketEvent {
      public Send(class_2596<?> packet) {
         super(packet);
      }
   }
}
