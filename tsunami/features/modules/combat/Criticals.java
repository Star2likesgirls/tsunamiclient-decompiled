package tsunami.features.modules.combat;

import io.netty.buffer.Unpooled;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_1297;
import net.minecraft.class_1511;
import net.minecraft.class_2540;
import net.minecraft.class_2824;
import net.minecraft.class_2828.class_2829;
import net.minecraft.class_2828.class_2830;
import org.jetbrains.annotations.NotNull;
import tsunami.events.impl.PacketEvent;
import tsunami.features.modules.Module;
import tsunami.injection.accesors.IClientPlayerEntity;
import tsunami.setting.Setting;

public final class Criticals extends Module {
   public final Setting<Criticals.Mode> mode;
   public static boolean cancelCrit;

   public Criticals() {
      super("Criticals", Module.Category.NONE);
      this.mode = new Setting("Mode", Criticals.Mode.UpdatedNCP);
   }

   @EventHandler
   public void onPacketSend(@NotNull PacketEvent.Send event) {
      if (event.getPacket() instanceof class_2824 && getInteractType((class_2824)event.getPacket()) == Criticals.InteractType.ATTACK) {
         class_1297 ent = getEntity((class_2824)event.getPacket());
         if (ent == null || ent instanceof class_1511 || cancelCrit) {
            return;
         }

         this.doCrit();
      }

   }

   public void doCrit() {
      if (!this.isDisabled() && mc.field_1724 != null && mc.field_1687 != null) {
         if ((mc.field_1724.method_24828() || mc.field_1724.method_31549().field_7479 || this.mode.is(Criticals.Mode.Grim)) && !mc.field_1724.method_5771() && !mc.field_1724.method_5869()) {
            switch(((Criticals.Mode)this.mode.getValue()).ordinal()) {
            case 0:
               this.critPacket(0.0625D, false);
               this.critPacket(0.0D, false);
               break;
            case 1:
               this.critPacket(0.062600301692775D, false);
               this.critPacket(0.07260029960661D, false);
               this.critPacket(0.0D, false);
               this.critPacket(0.0D, false);
               break;
            case 2:
               this.critPacket(1.058293536E-5D, false);
               this.critPacket(9.16580235E-6D, false);
               this.critPacket(1.0371854E-7D, false);
               break;
            case 3:
               this.critPacket(2.71875E-7D, false);
               this.critPacket(0.0D, false);
               break;
            case 4:
               if (!mc.field_1724.method_24828()) {
                  this.critPacket(-1.0E-6D, true);
               }
            }
         }

      }
   }

   private void critPacket(double yDelta, boolean full) {
      if (!full) {
         this.sendPacket(new class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318() + yDelta, mc.field_1724.method_23321(), false));
      } else {
         this.sendPacket(new class_2830(mc.field_1724.method_23317(), mc.field_1724.method_23318() + yDelta, mc.field_1724.method_23321(), ((IClientPlayerEntity)mc.field_1724).getLastYaw(), ((IClientPlayerEntity)mc.field_1724).getLastPitch(), false));
      }

   }

   public static class_1297 getEntity(@NotNull class_2824 packet) {
      class_2540 packetBuf = new class_2540(Unpooled.buffer());
      packet.method_55976(packetBuf);
      return mc.field_1687.method_8469(packetBuf.method_10816());
   }

   public static Criticals.InteractType getInteractType(@NotNull class_2824 packet) {
      class_2540 packetBuf = new class_2540(Unpooled.buffer());
      packet.method_55976(packetBuf);
      packetBuf.method_10816();
      return (Criticals.InteractType)packetBuf.method_10818(Criticals.InteractType.class);
   }

   public static enum Mode {
      Ncp,
      Strict,
      OldNCP,
      UpdatedNCP,
      Grim;

      // $FF: synthetic method
      private static Criticals.Mode[] $values() {
         return new Criticals.Mode[]{Ncp, Strict, OldNCP, UpdatedNCP, Grim};
      }
   }

   public static enum InteractType {
      INTERACT,
      ATTACK,
      INTERACT_AT;

      // $FF: synthetic method
      private static Criticals.InteractType[] $values() {
         return new Criticals.InteractType[]{INTERACT, ATTACK, INTERACT_AT};
      }
   }
}
