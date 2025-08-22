package tsunami.features.modules.movement;

import java.util.ArrayList;
import java.util.Iterator;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_1268;
import net.minecraft.class_1297;
import net.minecraft.class_1690;
import net.minecraft.class_1713;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_243;
import net.minecraft.class_2596;
import net.minecraft.class_2661;
import net.minecraft.class_2684;
import net.minecraft.class_2692;
import net.minecraft.class_2708;
import net.minecraft.class_2740;
import net.minecraft.class_2824;
import net.minecraft.class_2833;
import net.minecraft.class_2851;
import net.minecraft.class_2828.class_2831;
import org.jetbrains.annotations.NotNull;
import tsunami.TsunamiClient;
import tsunami.events.impl.EventPlayerTravel;
import tsunami.events.impl.PacketEvent;
import tsunami.features.modules.Module;
import tsunami.setting.Setting;
import tsunami.setting.impl.SettingGroup;
import tsunami.utility.player.MovementUtility;

public class BoatFly extends Module {
   private final Setting<BoatFly.Mode> mode;
   private final Setting<Boolean> phase;
   private final Setting<Boolean> gravity;
   private final Setting<Boolean> automount;
   public final Setting<Boolean> allowShift;
   private final Setting<Float> speed;
   private final Setting<Float> yspeed;
   public final Setting<SettingGroup> advanced;
   private final Setting<Float> glidespeed;
   private final Setting<Boolean> slotClick;
   private final Setting<Boolean> limit;
   private final Setting<Boolean> ongroundpacket;
   private final Setting<Boolean> spoofpackets;
   private final Setting<Float> jitter;
   private final Setting<Boolean> cancelrotations;
   private final Setting<Boolean> cancel;
   private final Setting<Boolean> pause;
   private final Setting<Integer> enableticks;
   private final Setting<Integer> waitTicks;
   private final Setting<Boolean> stopunloaded;
   private final Setting<Float> timer;
   public final Setting<Boolean> hideBoat;
   private final ArrayList<class_2833> vehiclePackets;
   private int ticksEnabled;
   private int enableDelay;
   private boolean waitedCooldown;
   private boolean returnGravity;
   private boolean jitterSwitch;

   public BoatFly() {
      super("BoatFly", Module.Category.NONE);
      this.mode = new Setting("Mode", BoatFly.Mode.Packet);
      this.phase = new Setting("Phase", false);
      this.gravity = new Setting("Gravity", false);
      this.automount = new Setting("AutoMount", true);
      this.allowShift = new Setting("AllowShift", true);
      this.speed = new Setting("Speed", 2.0F, 0.0F, 25.0F);
      this.yspeed = new Setting("YSpeed", 1.0F, 0.0F, 10.0F);
      this.advanced = new Setting("Advanced", new SettingGroup(false, 0));
      this.glidespeed = (new Setting("GlideSpeed", 0.0F, 0.0F, 10.0F)).addToGroup(this.advanced);
      this.slotClick = (new Setting("ClickSlot", false)).addToGroup(this.advanced);
      this.limit = (new Setting("Limit", true)).addToGroup(this.advanced);
      this.ongroundpacket = (new Setting("OnGroundPacket", false)).addToGroup(this.advanced);
      this.spoofpackets = (new Setting("SpoofPackets", false)).addToGroup(this.advanced);
      this.jitter = (new Setting("Jitter", 0.1F, 0.0F, 10.0F, (v) -> {
         return (Boolean)this.spoofpackets.getValue();
      })).addToGroup(this.advanced);
      this.cancelrotations = (new Setting("CancelRotations", true)).addToGroup(this.advanced);
      this.cancel = (new Setting("Cancel", true)).addToGroup(this.advanced);
      this.pause = (new Setting("Pause", false)).addToGroup(this.advanced);
      this.enableticks = (new Setting("EnableTicks", 10, 1, 100, (v) -> {
         return (Boolean)this.pause.getValue();
      })).addToGroup(this.advanced);
      this.waitTicks = (new Setting("WaitTicks", 10, 1, 100, (v) -> {
         return (Boolean)this.pause.getValue();
      })).addToGroup(this.advanced);
      this.stopunloaded = (new Setting("StopUnloaded", true)).addToGroup(this.advanced);
      this.timer = (new Setting("Timer", 1.0F, 0.1F, 5.0F)).addToGroup(this.advanced);
      this.hideBoat = (new Setting("HideBoat", true)).addToGroup(this.advanced);
      this.vehiclePackets = new ArrayList();
      this.ticksEnabled = 0;
      this.enableDelay = 0;
      this.waitedCooldown = false;
      this.returnGravity = false;
      this.jitterSwitch = false;
   }

   public void onEnable() {
      if (fullNullCheck()) {
         this.disable();
      } else {
         if ((Boolean)this.automount.getValue()) {
            this.mountToBoat();
         }

      }
   }

   public void onDisable() {
      TsunamiClient.TICK_TIMER = 1.0F;
      this.vehiclePackets.clear();
      this.waitedCooldown = false;
      if (mc.field_1724 != null) {
         if ((Boolean)this.phase.getValue() && this.mode.getValue() == BoatFly.Mode.Motion) {
            if (mc.field_1724.method_49694() != null) {
               mc.field_1724.method_49694().field_5960 = false;
            }

            mc.field_1724.field_5960 = false;
         }

         if (mc.field_1724.method_49694() != null) {
            mc.field_1724.method_49694().method_5875(false);
         }

         mc.field_1724.method_5875(false);
      }
   }

   private float randomizeYOffset() {
      this.jitterSwitch = !this.jitterSwitch;
      return this.jitterSwitch ? (Float)this.jitter.getValue() : -(Float)this.jitter.getValue();
   }

   private void sendMovePacket(class_2833 pac) {
      this.vehiclePackets.add(pac);
      this.sendPacket(pac);
   }

   private void teleportToGround(class_1297 boat) {
      class_2338 blockPos = class_2338.method_49638(boat.method_19538());

      for(int i = 0; i < 255; ++i) {
         if (!mc.field_1687.method_8320(blockPos).method_45474() || mc.field_1687.method_8320(blockPos).method_26204() == class_2246.field_10382) {
            boat.method_5814(boat.method_23317(), (double)(blockPos.method_10264() + 1), boat.method_23321());
            this.sendMovePacket(new class_2833(boat));
            boat.method_5814(boat.method_23317(), boat.method_23318(), boat.method_23321());
            break;
         }

         blockPos = blockPos.method_10074();
      }

   }

   private void mountToBoat() {
      Iterator var1 = mc.field_1687.method_18112().iterator();

      while(var1.hasNext()) {
         class_1297 entity = (class_1297)var1.next();
         if (entity instanceof class_1690 && !(mc.field_1724.method_5858(entity) > 25.0D)) {
            this.sendPacket(class_2824.method_34207(entity, false, class_1268.field_5808));
            break;
         }
      }

   }

   @EventHandler
   public void onPlayerTravel(@NotNull EventPlayerTravel ev) {
      if (ev.isPre()) {
         if (!fullNullCheck()) {
            if (mc.field_1724.method_49694() == null) {
               if ((Boolean)this.automount.getValue()) {
                  this.mountToBoat();
               }

            } else {
               if ((Boolean)this.phase.getValue() && this.mode.getValue() == BoatFly.Mode.Motion) {
                  mc.field_1724.method_49694().field_5960 = true;
                  mc.field_1724.method_49694().method_5875(true);
                  mc.field_1724.field_5960 = true;
               }

               if (!this.returnGravity) {
                  mc.field_1724.method_49694().method_5875(!(Boolean)this.gravity.getValue());
                  mc.field_1724.method_5875(!(Boolean)this.gravity.getValue());
               }

               if ((Boolean)this.pause.getValue()) {
                  if (this.ticksEnabled > (Integer)this.enableticks.getValue() && !this.waitedCooldown) {
                     this.ticksEnabled = 0;
                     this.waitedCooldown = true;
                     this.enableDelay = (Integer)this.waitTicks.getValue();
                  }

                  if (this.enableDelay > 0 && this.waitedCooldown) {
                     --this.enableDelay;
                     return;
                  }

                  if (this.enableDelay <= 0) {
                     this.waitedCooldown = false;
                  }
               }

               class_1297 entity = mc.field_1724.method_49694();
               if ((!mc.field_1687.method_8393((int)entity.method_19538().method_10216() >> 4, (int)entity.method_19538().method_10215() >> 4) || entity.method_19538().method_10214() < -60.0D) && (Boolean)this.stopunloaded.getValue()) {
                  this.returnGravity = true;
               } else {
                  if ((Float)this.timer.getValue() != 1.0F) {
                     TsunamiClient.TICK_TIMER = (Float)this.timer.getValue();
                  }

                  entity.method_36456(mc.field_1724.method_36454());
                  double[] boatMotion = MovementUtility.forward((double)(Float)this.speed.getValue());
                  double predictedX = entity.method_23317() + boatMotion[0];
                  double predictedZ = entity.method_23321() + boatMotion[1];
                  double predictedY = entity.method_23318();
                  if ((!mc.field_1687.method_8393((int)predictedX >> 4, (int)predictedZ >> 4) || entity.method_19538().method_10214() < -60.0D) && (Boolean)this.stopunloaded.getValue()) {
                     this.returnGravity = true;
                  } else {
                     this.returnGravity = false;
                     entity.method_18800(entity.method_18798().method_10216(), (double)(-(Float)this.glidespeed.getValue() / 100.0F), entity.method_18798().method_10215());
                     if (this.mode.getValue() == BoatFly.Mode.Motion) {
                        entity.method_18800(boatMotion[0], entity.method_18798().method_10214(), boatMotion[1]);
                     }

                     if (mc.field_1690.field_1903.method_1434()) {
                        if (this.mode.getValue() == BoatFly.Mode.Motion) {
                           entity.method_18800(entity.method_18798().method_10216(), entity.method_18798().method_10214() + (double)(Float)this.yspeed.getValue(), entity.method_18798().method_10215());
                        } else {
                           predictedY += (double)(Float)this.yspeed.getValue();
                        }
                     } else if (mc.field_1690.field_1832.method_1434()) {
                        if (this.mode.getValue() == BoatFly.Mode.Motion) {
                           entity.method_18800(entity.method_18798().method_10216(), entity.method_18798().method_10214() - (double)(Float)this.yspeed.getValue(), entity.method_18798().method_10215());
                        } else {
                           predictedY -= (double)(Float)this.yspeed.getValue();
                        }
                     }

                     if (!MovementUtility.isMoving()) {
                        entity.method_18800(0.0D, entity.method_18798().method_10214(), 0.0D);
                     }

                     if ((Boolean)this.ongroundpacket.getValue()) {
                        this.teleportToGround(entity);
                     }

                     if (this.mode.getValue() == BoatFly.Mode.Packet) {
                        entity.method_5814(predictedX, predictedY, predictedZ);
                        this.sendMovePacket(new class_2833(entity));
                     }

                     if ((Boolean)this.slotClick.getValue()) {
                        mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, 0, 0, class_1713.field_7796, mc.field_1724);
                     }

                     if ((Boolean)this.spoofpackets.getValue()) {
                        class_243 vec3d = entity.method_19538().method_1031(0.0D, (double)this.randomizeYOffset(), 0.0D);
                        class_1690 entityBoat = new class_1690(mc.field_1687, vec3d.field_1352, vec3d.field_1351, vec3d.field_1350);
                        entityBoat.method_36456(entity.method_36454());
                        entityBoat.method_36457(entity.method_36455());
                        this.sendMovePacket(new class_2833(entityBoat));
                     }

                     ev.cancel();
                     ++this.ticksEnabled;
                  }
               }
            }
         }
      }
   }

   @EventHandler
   public void onPacketReceive(PacketEvent.Receive event) {
      if (!fullNullCheck()) {
         if (event.getPacket() instanceof class_2661) {
            this.disable();
         }

         if (mc.field_1724.method_3144() && !this.returnGravity && !this.waitedCooldown) {
            if ((Boolean)this.cancel.getValue()) {
               if (event.getPacket() instanceof class_2692) {
                  event.cancel();
               }

               if (event.getPacket() instanceof class_2708) {
                  event.cancel();
               }

               if (event.getPacket() instanceof class_2684) {
                  event.cancel();
               }

               if (event.getPacket() instanceof class_2740) {
                  event.cancel();
               }
            }

         }
      }
   }

   @EventHandler
   public void onPacketSend(PacketEvent.Send event) {
      if (!fullNullCheck()) {
         if ((event.getPacket() instanceof class_2831 && (Boolean)this.cancelrotations.getValue() || event.getPacket() instanceof class_2851) && mc.field_1724.method_3144()) {
            event.cancel();
         }

         if (this.returnGravity && event.getPacket() instanceof class_2833) {
            event.cancel();
         }

         if (event.getPacket() instanceof class_2851 && (Boolean)this.allowShift.getValue()) {
            event.cancel();
         }

         if (mc.field_1724.method_49694() != null && !this.returnGravity && !this.waitedCooldown) {
            class_243 boatPos = mc.field_1724.method_49694().method_19538();
            if (mc.field_1687.method_8393((int)boatPos.method_10216() >> 4, (int)boatPos.method_10215() >> 4) && !(boatPos.method_10214() < -60.0D) || !(Boolean)this.stopunloaded.getValue()) {
               class_2596 var4 = event.getPacket();
               if (var4 instanceof class_2833) {
                  class_2833 pac = (class_2833)var4;
                  if ((Boolean)this.limit.getValue() && this.mode.getValue() == BoatFly.Mode.Packet) {
                     if (this.vehiclePackets.contains(pac)) {
                        this.vehiclePackets.remove(pac);
                     } else {
                        event.cancel();
                     }
                  }
               }

            }
         }
      }
   }

   public static enum Mode {
      Packet,
      Motion;

      // $FF: synthetic method
      private static BoatFly.Mode[] $values() {
         return new BoatFly.Mode[]{Packet, Motion};
      }
   }
}
