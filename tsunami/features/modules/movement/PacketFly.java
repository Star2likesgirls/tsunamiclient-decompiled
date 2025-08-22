package tsunami.features.modules.movement;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_243;
import net.minecraft.class_2596;
import net.minecraft.class_2708;
import net.minecraft.class_2793;
import net.minecraft.class_2828;
import net.minecraft.class_434;
import net.minecraft.class_2828.class_2829;
import org.jetbrains.annotations.NotNull;
import tsunami.TsunamiClient;
import tsunami.events.impl.EventMove;
import tsunami.events.impl.EventSync;
import tsunami.events.impl.PacketEvent;
import tsunami.features.modules.Module;
import tsunami.injection.accesors.IPlayerPositionLookS2CPacket;
import tsunami.setting.Setting;
import tsunami.setting.impl.BooleanSettingGroup;
import tsunami.utility.player.MovementUtility;

public class PacketFly extends Module {
   private final Setting<PacketFly.Mode> mode;
   private final Setting<PacketFly.Type> type;
   private final Setting<PacketFly.Phase> phase;
   private final Setting<Boolean> limit;
   private final Setting<BooleanSettingGroup> antiKick;
   private final Setting<Integer> interval;
   private final Setting<Integer> upInterval;
   private final Setting<Float> anticKickOffset;
   private final Setting<Float> speed;
   private final Setting<Float> upSpeed;
   private final Setting<Float> timer;
   private final Setting<Integer> increaseTicks;
   private final Setting<Float> factor;
   private final Setting<Float> offset;
   private final ConcurrentHashMap<Integer, PacketFly.Teleport> teleports;
   private final ArrayList<class_2828> movePackets;
   private int ticks;
   private int factorTicks;
   private int teleportId;
   private boolean flip;

   public PacketFly() {
      super("PacketFly", Module.Category.NONE);
      this.mode = new Setting("Mode", PacketFly.Mode.Fast);
      this.type = new Setting("Type", PacketFly.Type.Preserve);
      this.phase = new Setting("Phase", PacketFly.Phase.Full);
      this.limit = new Setting("Limit", true);
      this.antiKick = new Setting("AntiKick", new BooleanSettingGroup(true));
      this.interval = (new Setting("Interval", 4, 1, 50)).addToGroup(this.antiKick);
      this.upInterval = (new Setting("UpInterval", 20, 1, 50)).addToGroup(this.antiKick);
      this.anticKickOffset = (new Setting("anticKickOffset", 0.04F, 0.008F, 1.0F)).addToGroup(this.antiKick);
      this.speed = new Setting("Speed", 1.0F, 0.0F, 10.0F);
      this.upSpeed = new Setting("UpSpeed", 0.062F, 0.001F, 0.1F);
      this.timer = new Setting("Timer", 1.0F, 0.1F, 5.0F);
      this.increaseTicks = new Setting("IncreaseTicks", 1, 1, 20);
      this.factor = new Setting("Factor", 1.0F, 1.0F, 10.0F);
      this.offset = new Setting("Offset", 1337.0F, 1.0F, 1337.0F, (v) -> {
         return this.type.is(PacketFly.Type.Up) || this.type.is(PacketFly.Type.Down);
      });
      this.teleports = new ConcurrentHashMap();
      this.movePackets = new ArrayList();
      this.teleportId = -1;
      this.flip = false;
   }

   public void onEnable() {
      this.teleportId = -1;
      if (fullNullCheck() && mc.field_1724 != null) {
         this.ticks = 0;
         this.teleportId = 0;
         this.movePackets.clear();
         this.teleports.clear();
      }

      this.factorTicks = 0;
   }

   public void onDisable() {
      TsunamiClient.TICK_TIMER = 1.0F;
   }

   public boolean getTickCounter(int n) {
      ++this.ticks;
      if (this.ticks >= n) {
         this.ticks = 0;
         return true;
      } else {
         return false;
      }
   }

   private int getWorldBorder() {
      if (mc.method_1542()) {
         return 1;
      } else {
         int n = ThreadLocalRandom.current().nextInt(29000000);
         return ThreadLocalRandom.current().nextBoolean() ? n : -n;
      }
   }

   public class_243 getVectorByMode(@NotNull class_243 vec3d, class_243 vec3d2) {
      class_243 vec3d3 = vec3d.method_1019(vec3d2);
      switch(((PacketFly.Type)this.type.getValue()).ordinal()) {
      case 0:
         vec3d3 = vec3d3.method_1031((double)this.getWorldBorder(), 0.0D, (double)this.getWorldBorder());
         break;
      case 1:
         vec3d3 = vec3d3.method_1031(0.0D, (double)(Float)this.offset.getValue(), 0.0D);
         break;
      case 2:
         vec3d3 = vec3d3.method_1031(0.0D, (double)(-(Float)this.offset.getValue()), 0.0D);
         break;
      case 3:
         vec3d3 = new class_243(vec3d3.field_1352, mc.field_1724.method_23318() <= 10.0D ? 255.0D : 1.0D, vec3d3.field_1350);
      }

      return vec3d3;
   }

   public void sendPackets(class_243 vec3d, boolean confirm) {
      class_243 motion = mc.field_1724.method_19538().method_1019(vec3d);
      class_243 rubberBand = this.getVectorByMode(vec3d, motion);
      class_2828 motionPacket = new class_2829(motion.field_1352, motion.field_1351, motion.field_1350, mc.field_1724.method_24828());
      this.movePackets.add(motionPacket);
      this.sendPacket(motionPacket);
      class_2828 rubberBandPacket = new class_2829(rubberBand.field_1352, rubberBand.field_1351, rubberBand.field_1350, mc.field_1724.method_24828());
      this.movePackets.add(rubberBandPacket);
      this.sendPacket(rubberBandPacket);
      if (confirm) {
         this.sendPacket(new class_2793(++this.teleportId));
         this.teleports.put(this.teleportId, new PacketFly.Teleport(motion.field_1352, motion.field_1351, motion.field_1350, System.currentTimeMillis()));
      }

   }

   @EventHandler
   public void onPacketReceive(PacketEvent.Receive event) {
      if (!fullNullCheck()) {
         if (mc.field_1724 != null) {
            class_2596 var3 = event.getPacket();
            if (var3 instanceof class_2708) {
               class_2708 pac = (class_2708)var3;
               PacketFly.Teleport teleport = (PacketFly.Teleport)this.teleports.remove(pac.method_11737());
               if (mc.field_1724.method_5805() && mc.field_1687.method_8393((int)mc.field_1724.method_23317() >> 4, (int)mc.field_1724.method_23321() >> 4) && !(mc.field_1755 instanceof class_434) && this.mode.getValue() != PacketFly.Mode.Rubber && teleport != null && teleport.x == pac.method_11734() && teleport.y == pac.method_11735() && teleport.z == pac.method_11738()) {
                  event.cancel();
                  return;
               }

               ((IPlayerPositionLookS2CPacket)pac).setYaw(mc.field_1724.method_36454());
               ((IPlayerPositionLookS2CPacket)pac).setPitch(mc.field_1724.method_36455());
               this.teleportId = pac.method_11737();
            }
         }

      }
   }

   @EventHandler
   public void onPacketSend(@NotNull PacketEvent.Send event) {
      if (event.getPacket() instanceof class_2828) {
         if (this.movePackets.contains((class_2828)event.getPacket())) {
            this.movePackets.remove((class_2828)event.getPacket());
            return;
         }

         event.cancel();
      }

   }

   public void onUpdate() {
      this.teleports.entrySet().removeIf((entry) -> {
         return System.currentTimeMillis() - ((PacketFly.Teleport)entry.getValue()).time > 30000L;
      });
   }

   @EventHandler
   public void onMove(@NotNull EventMove event) {
      if (!event.isCancelled()) {
         if (this.mode.getValue() != PacketFly.Mode.Rubber && this.teleportId == 0) {
            return;
         }

         event.cancel();
         event.setX(mc.field_1724.method_18798().field_1352);
         event.setY(mc.field_1724.method_18798().field_1351);
         event.setZ(mc.field_1724.method_18798().field_1350);
         if (this.phase.getValue() != PacketFly.Phase.Off && (this.phase.getValue() == PacketFly.Phase.Semi || mc.field_1687.method_20812(mc.field_1724, mc.field_1724.method_5829().method_1009(-0.0625D, -0.0625D, -0.0625D)).iterator().hasNext())) {
            mc.field_1724.field_5960 = true;
         }
      }

   }

   @EventHandler
   public void onSync(EventSync eventPlayerUpdateWalking) {
      if ((double)(Float)this.timer.getValue() != 1.0D) {
         TsunamiClient.TICK_TIMER = (Float)this.timer.getValue();
      }

      mc.field_1724.method_18800(0.0D, 0.0D, 0.0D);
      if (this.mode.getValue() != PacketFly.Mode.Rubber && this.teleportId == 0) {
         if (this.getTickCounter(4)) {
            this.sendPackets(class_243.field_1353, false);
         }

      } else {
         boolean insideBlock = mc.field_1687.method_20812(mc.field_1724, mc.field_1724.method_5829().method_1009(-0.0625D, -0.0625D, -0.0625D)).iterator().hasNext();
         double upMotion = 0.0D;
         if (!mc.field_1690.field_1903.method_1434() || !insideBlock && MovementUtility.isMoving()) {
            if (mc.field_1690.field_1832.method_1434()) {
               upMotion = (double)(-(Float)this.upSpeed.getValue());
            } else if (((BooleanSettingGroup)this.antiKick.getValue()).isEnabled() && !insideBlock) {
               upMotion = this.getTickCounter((Integer)this.interval.getValue()) ? (double)(-(Float)this.anticKickOffset.getValue()) : 0.0D;
            }
         } else if (((BooleanSettingGroup)this.antiKick.getValue()).isEnabled() && !insideBlock) {
            upMotion = this.getTickCounter(this.mode.is(PacketFly.Mode.Rubber) ? (Integer)this.upInterval.getValue() / 2 : (Integer)this.upInterval.getValue()) ? (double)(-(Float)this.upSpeed.getValue() / 2.0F) : (double)(Float)this.upSpeed.getValue();
         } else {
            upMotion = (double)(Float)this.upSpeed.getValue();
         }

         if (this.phase.is(PacketFly.Phase.Full) && insideBlock && MovementUtility.isMoving() && upMotion != 0.0D) {
            upMotion = mc.field_1690.field_1903.method_1434() ? upMotion / 2.5D : upMotion / 1.5D;
         }

         double[] motion = MovementUtility.forward(this.phase.is(PacketFly.Phase.Full) && insideBlock ? 0.034444444444444444D : (double)(Float)this.speed.getValue() * 0.26D);
         int factorInt = 1;
         if (this.mode.getValue() == PacketFly.Mode.Factor && mc.field_1724.field_6012 % (Integer)this.increaseTicks.getValue() == 0) {
            factorInt = (int)Math.floor((double)(Float)this.factor.getValue());
            ++this.factorTicks;
            if (this.factorTicks > (int)(20.0D / ((double)((Float)this.factor.getValue() - (float)factorInt) * 20.0D))) {
               ++factorInt;
               this.factorTicks = 0;
            }
         }

         for(int i = 1; i <= factorInt; ++i) {
            if (this.mode.getValue() == PacketFly.Mode.Limit) {
               if (mc.field_1724.field_6012 % 2 == 0) {
                  if (this.flip && upMotion >= 0.0D) {
                     this.flip = false;
                     upMotion = (double)(-(Float)this.upSpeed.getValue() / 2.0F);
                  }

                  mc.field_1724.method_18800(motion[0] * (double)i, upMotion * (double)i, motion[1] * (double)i);
                  this.sendPackets(mc.field_1724.method_18798(), !(Boolean)this.limit.getValue());
               } else if (upMotion < 0.0D) {
                  this.flip = true;
               }
            } else {
               mc.field_1724.method_18800(motion[0] * (double)i, upMotion * (double)i, motion[1] * (double)i);
               this.sendPackets(mc.field_1724.method_18798(), !this.mode.is(PacketFly.Mode.Rubber));
            }
         }

      }
   }

   public static enum Mode {
      Fast,
      Factor,
      Rubber,
      Limit;

      // $FF: synthetic method
      private static PacketFly.Mode[] $values() {
         return new PacketFly.Mode[]{Fast, Factor, Rubber, Limit};
      }
   }

   public static enum Type {
      Preserve,
      Up,
      Down,
      Bounds;

      // $FF: synthetic method
      private static PacketFly.Type[] $values() {
         return new PacketFly.Type[]{Preserve, Up, Down, Bounds};
      }
   }

   public static enum Phase {
      Full,
      Off,
      Semi;

      // $FF: synthetic method
      private static PacketFly.Phase[] $values() {
         return new PacketFly.Phase[]{Full, Off, Semi};
      }
   }

   public static record Teleport(double x, double y, double z, long time) {
      public Teleport(double x, double y, double z, long time) {
         this.x = x;
         this.y = y;
         this.z = z;
         this.time = time;
      }

      public double x() {
         return this.x;
      }

      public double y() {
         return this.y;
      }

      public double z() {
         return this.z;
      }

      public long time() {
         return this.time;
      }
   }
}
