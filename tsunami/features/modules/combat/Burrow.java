package tsunami.features.modules.combat;

import java.lang.reflect.Field;
import java.util.Iterator;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_1268;
import net.minecraft.class_1297;
import net.minecraft.class_1511;
import net.minecraft.class_1657;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_2664;
import net.minecraft.class_2680;
import net.minecraft.class_2708;
import net.minecraft.class_2709;
import net.minecraft.class_2824;
import net.minecraft.class_2879;
import net.minecraft.class_2828.class_2829;
import net.minecraft.class_2828.class_2830;
import net.minecraft.class_2828.class_2831;
import tsunami.events.impl.EventPostSync;
import tsunami.events.impl.PacketEvent;
import tsunami.features.modules.Module;
import tsunami.features.modules.client.ClientSettings;
import tsunami.setting.Setting;
import tsunami.utility.Timer;
import tsunami.utility.math.MathUtility;
import tsunami.utility.player.InteractionUtility;
import tsunami.utility.player.InventoryUtility;
import tsunami.utility.player.SearchInvResult;

public final class Burrow extends Module {
   private final Setting<Burrow.Mode> mode;
   private final Setting<Burrow.OffsetMode> offsetMode;
   private final Setting<Float> vClip;
   private final Setting<Boolean> scaleDown;
   private final Setting<Boolean> scaleVelocity;
   private final Setting<Boolean> scaleExplosion;
   private final Setting<Float> scaleFactor;
   private final Setting<Integer> scaleDelay;
   private final Setting<Boolean> attack;
   private final Setting<Boolean> placeDisable;
   private final Setting<Boolean> wait;
   private final Setting<Boolean> evade;
   private final Setting<Boolean> noVoid;
   private final Setting<Boolean> onGround;
   private final Setting<Boolean> allowUp;
   private final Setting<Boolean> rotate;
   private final Setting<Boolean> discrete;
   private final Setting<Boolean> air;
   private final Setting<Boolean> fallback;
   private final Setting<Boolean> skipZero;
   private double motionY;
   private class_2338 startPos;
   private volatile double last_x;
   private volatile double last_y;
   private volatile double last_z;
   private final Timer scaleTimer;
   private final Timer timer;

   public Burrow() {
      super("Burrow", Module.Category.NONE);
      this.mode = new Setting("Mode", Burrow.Mode.Default);
      this.offsetMode = new Setting("Mode", Burrow.OffsetMode.Smart, (v) -> {
         return this.mode.getValue() == Burrow.Mode.Default;
      });
      this.vClip = new Setting("VClip", -9.0F, -256.0F, 256.0F, (v) -> {
         return this.offsetMode.getValue() == Burrow.OffsetMode.Constant && this.mode.getValue() == Burrow.Mode.Default;
      });
      this.scaleDown = new Setting("ScaleDown", false, (v) -> {
         return this.mode.getValue() == Burrow.Mode.Default;
      });
      this.scaleVelocity = new Setting("ScaleVelocity", false, (v) -> {
         return this.mode.getValue() == Burrow.Mode.Default;
      });
      this.scaleExplosion = new Setting("Scale-xplosion", false, (v) -> {
         return this.mode.getValue() == Burrow.Mode.Default;
      });
      this.scaleFactor = new Setting("ScaleFactor", 1.0F, 0.1F, 10.0F, (v) -> {
         return this.mode.getValue() == Burrow.Mode.Default;
      });
      this.scaleDelay = new Setting("ScaleDelay", 250, 0, 1000, (v) -> {
         return this.mode.getValue() == Burrow.Mode.Default;
      });
      this.attack = new Setting("Attack", true, (v) -> {
         return this.mode.getValue() == Burrow.Mode.Default;
      });
      this.placeDisable = new Setting("PlaceDisable", false, (v) -> {
         return this.mode.getValue() == Burrow.Mode.Default;
      });
      this.wait = new Setting("Wait", true);
      this.evade = new Setting("Evade", false, (v) -> {
         return this.offsetMode.getValue() == Burrow.OffsetMode.Constant && this.mode.getValue() == Burrow.Mode.Default;
      });
      this.noVoid = new Setting("NoVoid", false, (v) -> {
         return this.offsetMode.getValue() == Burrow.OffsetMode.Smart && this.mode.getValue() == Burrow.Mode.Default;
      });
      this.onGround = new Setting("OnGround", true, (v) -> {
         return this.mode.getValue() == Burrow.Mode.Default;
      });
      this.allowUp = new Setting("IgnoreHeadBlock", false, (v) -> {
         return this.mode.getValue() == Burrow.Mode.Default;
      });
      this.rotate = new Setting("Rotate", true);
      this.discrete = new Setting("Discrete", true, (v) -> {
         return this.offsetMode.getValue() == Burrow.OffsetMode.Smart && this.mode.getValue() == Burrow.Mode.Default;
      });
      this.air = new Setting("Air", false, (v) -> {
         return this.offsetMode.getValue() == Burrow.OffsetMode.Smart && this.mode.getValue() == Burrow.Mode.Default;
      });
      this.fallback = new Setting("Fallback", true, (v) -> {
         return this.offsetMode.getValue() == Burrow.OffsetMode.Smart && this.mode.getValue() == Burrow.Mode.Default;
      });
      this.skipZero = new Setting("SkipZero", true, (v) -> {
         return this.offsetMode.getValue() == Burrow.OffsetMode.Smart && this.mode.getValue() == Burrow.Mode.Default;
      });
      this.scaleTimer = new Timer();
      this.timer = new Timer();
   }

   public void onEnable() {
      this.startPos = getPlayerPos();
   }

   @EventHandler
   public void onPacketReceive(PacketEvent.Receive event) {
      if (this.mode.getValue() == Burrow.Mode.Default) {
         if (event.getPacket() instanceof class_2664) {
            if ((Boolean)this.scaleExplosion.getValue()) {
               this.motionY = (double)((class_2664)event.getPacket()).method_11473();
               this.scaleTimer.reset();
            }

            if ((Boolean)this.scaleVelocity.getValue()) {
               return;
            }

            if (mc.field_1724 != null) {
               this.motionY = (double)((class_2664)event.getPacket()).method_11473() / 8000.0D;
               this.scaleTimer.reset();
            }
         }

         if (event.getPacket() instanceof class_2708) {
            class_2708 packet = (class_2708)event.getPacket();
            double x = packet.method_11734();
            double y = packet.method_11735();
            double z = packet.method_11738();
            if (packet.method_11733().contains(class_2709.field_12400)) {
               x += mc.field_1724.method_23317();
            }

            if (packet.method_11733().contains(class_2709.field_12398)) {
               y += mc.field_1724.method_23318();
            }

            if (packet.method_11733().contains(class_2709.field_12403)) {
               z += mc.field_1724.method_23321();
            }

            this.last_x = MathUtility.clamp(x, -3.0E7D, 3.0E7D);
            this.last_y = y;
            this.last_z = MathUtility.clamp(z, -3.0E7D, 3.0E7D);
         }

      }
   }

   @EventHandler
   public void onPostSync(EventPostSync e) {
      class_2338 pos;
      if ((Boolean)this.wait.getValue()) {
         pos = getPlayerPos();
         if (!pos.equals(this.startPos)) {
            this.disable(ClientSettings.isRu() ? "Отключен из-за движения!" : "Disabled due to movement!");
            return;
         }
      }

      pos = getPosition(mc.field_1724);
      if (!mc.field_1687.method_8320(pos).method_45474()) {
         if (!(Boolean)this.wait.getValue()) {
            this.disable(ClientSettings.isRu() ? "Невозможно поставить блок! Отключаю.." : "Can't place the block! Disabling..");
         }

      } else {
         Iterator var3 = mc.field_1687.method_18467(class_1297.class, new class_238(pos)).iterator();

         while(true) {
            class_1297 entity;
            do {
               do {
                  if (!var3.hasNext()) {
                     switch(((Burrow.Mode)this.mode.getValue()).ordinal()) {
                     case 0:
                        this.handleDefault(pos);
                        break;
                     case 1:
                        this.handleSkull(pos);
                        break;
                     case 2:
                        this.handleWeb(pos);
                     }

                     return;
                  }

                  entity = (class_1297)var3.next();
               } while(entity == null);
            } while(mc.field_1724.equals(entity));

            if (!(entity instanceof class_1511) || !(Boolean)this.attack.getValue()) {
               if (!(Boolean)this.wait.getValue()) {
                  this.disable(ClientSettings.isRu() ? "Невозможно поставить блок! Отключаю.." : "Can't place the block on! Disabling..");
               }

               return;
            }

            class_2824 attackPacket = class_2824.method_34206(mc.field_1724, mc.field_1724.method_5715());
            changeId(attackPacket, entity.method_5628());
            this.sendPacket(attackPacket);
            this.sendPacket(new class_2879(class_1268.field_5808));
         }
      }
   }

   private void handleWeb(class_2338 pos) {
      SearchInvResult webResult = InventoryUtility.findBlockInHotBar(class_2246.field_10343);
      if (!webResult.found()) {
         this.disable(ClientSettings.isRu() ? "Нет паутины!" : "No webs found!");
      } else {
         if (this.timer.passedMs(250L)) {
            if ((Boolean)this.rotate.getValue()) {
               this.sendPacket(new class_2831(mc.field_1724.method_36454(), 90.0F, (Boolean)this.onGround.getValue()));
            }

            InventoryUtility.saveSlot();
            InteractionUtility.placeBlock(pos, InteractionUtility.Rotate.None, InteractionUtility.Interact.Vanilla, InteractionUtility.PlaceMode.Packet, webResult.slot(), false, true);
            mc.field_1724.method_6104(class_1268.field_5808);
            this.timer.reset();
            InventoryUtility.returnSlot();
            if (!(Boolean)this.wait.getValue() || (Boolean)this.placeDisable.getValue()) {
               this.disable(ClientSettings.isRu() ? "Успешно забурровился! Отключаю.." : "Successfully burrowed! Disabling..");
            }
         }

      }
   }

   private void handleSkull(class_2338 pos) {
      SearchInvResult skullResult = InventoryUtility.getSkull();
      if (!skullResult.found()) {
         this.disable(ClientSettings.isRu() ? "Нет голов!" : "No heads found!");
      } else {
         if (this.timer.passedMs(250L)) {
            if ((Boolean)this.rotate.getValue()) {
               this.sendPacket(new class_2831(mc.field_1724.method_36454(), 90.0F, (Boolean)this.onGround.getValue()));
            }

            InventoryUtility.saveSlot();
            InteractionUtility.placeBlock(pos, InteractionUtility.Rotate.None, InteractionUtility.Interact.Vanilla, InteractionUtility.PlaceMode.Normal, skullResult.slot(), false, true);
            mc.field_1724.method_6104(class_1268.field_5808);
            this.timer.reset();
            InventoryUtility.returnSlot();
            if (!(Boolean)this.wait.getValue() || (Boolean)this.placeDisable.getValue()) {
               this.disable(ClientSettings.isRu() ? "Успешно забурровился! Отключаю.." : "Successfully burrowed! Disabling..");
            }
         }

      }
   }

   public void handleDefault(class_2338 pos) {
      if (!mc.field_1687.method_8320(class_2338.method_49638(mc.field_1724.method_19538().method_43206(class_2350.field_11036, 0.2D))).method_51366() && mc.field_1724.field_5992) {
         class_1657 rEntity = mc.field_1724;
         class_2338 posHead = getPosition(rEntity).method_10084().method_10084();
         if (mc.field_1687.method_8320(posHead).method_45474() || !(Boolean)this.wait.getValue()) {
            if (!(Boolean)this.allowUp.getValue()) {
               class_2338 upUp = pos.method_10086(2);
               class_2680 upState = mc.field_1687.method_8320(upUp);
               if (upState.method_51366()) {
                  if (!(Boolean)this.wait.getValue()) {
                     this.disable(ClientSettings.isRu() ? "Над головой блок, невозможно забурровиться! Отключаю.." : "Above the head block, impossible to burrow! Disabling..");
                  }

                  return;
               }
            }

            SearchInvResult obbyResult = InventoryUtility.findBlockInHotBar(class_2246.field_10540);
            SearchInvResult echestResult = InventoryUtility.findBlockInHotBar(class_2246.field_10443);
            int slot = obbyResult.found() && mc.field_1687.method_8320(pos.method_10074()).method_26204() != class_2246.field_10443 ? obbyResult.slot() : echestResult.slot();
            if (slot == -1) {
               this.disable(ClientSettings.isRu() ? "Нет блоков!" : "No Block found!");
            } else {
               double y = this.applyScale(this.getY(rEntity, (Burrow.OffsetMode)this.offsetMode.getValue()));
               if (!Double.isNaN(y)) {
                  float[] r = InteractionUtility.getPlaceAngle(pos, InteractionUtility.Interact.Strict, true);
                  if (mc.method_1542()) {
                     this.disable(ClientSettings.isRu() ? "Дебил! Ты в одиночке.." : "Retard! You're in singleplayer..");
                  } else {
                     if (this.timer.passedMs(1000L)) {
                        if ((Boolean)this.rotate.getValue() && r != null) {
                           if (rEntity.method_19538().equals(new class_243(this.last_x, this.last_y, this.last_z))) {
                              this.sendPacket(new class_2831(r[0], r[1], (Boolean)this.onGround.getValue()));
                           } else {
                              this.sendPacket(new class_2830(rEntity.method_23317(), rEntity.method_23318(), rEntity.method_23321(), r[0], r[1], (Boolean)this.onGround.getValue()));
                           }
                        }

                        this.sendPacket(new class_2829(rEntity.method_23317(), rEntity.method_23318() + 0.42D, rEntity.method_23321(), (Boolean)this.onGround.getValue()));
                        this.sendPacket(new class_2829(rEntity.method_23317(), rEntity.method_23318() + 0.75D, rEntity.method_23321(), (Boolean)this.onGround.getValue()));
                        this.sendPacket(new class_2829(rEntity.method_23317(), rEntity.method_23318() + 1.01D, rEntity.method_23321(), (Boolean)this.onGround.getValue()));
                        this.sendPacket(new class_2829(rEntity.method_23317(), rEntity.method_23318() + 1.16D, rEntity.method_23321(), (Boolean)this.onGround.getValue()));
                        InventoryUtility.saveSlot();
                        InteractionUtility.placeBlock(pos, InteractionUtility.Rotate.None, InteractionUtility.Interact.Vanilla, InteractionUtility.PlaceMode.Packet, slot, false, true);
                        mc.field_1724.method_6104(class_1268.field_5808);
                        this.sendPacket(new class_2829(rEntity.method_23317(), y, rEntity.method_23321(), false));
                        this.timer.reset();
                        InventoryUtility.returnSlot();
                        if (!(Boolean)this.wait.getValue() || (Boolean)this.placeDisable.getValue()) {
                           this.disable(ClientSettings.isRu() ? "Успешно забурровился! Отключаю.." : "Successfully burrowed! Disabling..");
                        }
                     }

                  }
               }
            }
         }
      }
   }

   public static void changeId(class_2824 packet, int id) {
      try {
         Field field = class_2824.class.getDeclaredField("field_12870");
         field.setAccessible(true);
         field.setInt(packet, id);
      } catch (Exception var3) {
      }

   }

   public double getY(class_1297 entity, Burrow.OffsetMode mode) {
      double d;
      if (mode == Burrow.OffsetMode.Constant) {
         d = entity.method_23318() + (double)(Float)this.vClip.getValue();
         if ((Boolean)this.evade.getValue() && Math.abs(d) < 1.0D) {
            d = -1.0D;
         }

         return d;
      } else {
         d = this.getY(entity, 3.0D, 10.0D, true);
         if (Double.isNaN(d)) {
            d = this.getY(entity, -3.0D, -10.0D, false);
            if (Double.isNaN(d) && (Boolean)this.fallback.getValue()) {
               return this.getY(entity, Burrow.OffsetMode.Constant);
            }
         }

         return d;
      }
   }

   public static class_2338 getPosition(class_1297 entity) {
      double y = entity.method_23318();
      if (entity.method_23318() - Math.floor(entity.method_23318()) > 0.5D) {
         y = Math.ceil(entity.method_23318());
      }

      return class_2338.method_49637(entity.method_23317(), y, entity.method_23321());
   }

   public double getY(class_1297 entity, double min, double max, boolean add) {
      if ((!(min > max) || !add) && (!(max > min) || add)) {
         double x = entity.method_23317();
         double y = entity.method_23318();
         double z = entity.method_23321();
         boolean air = false;
         double lastOff = 0.0D;
         class_2338 last = null;
         double off = min;

         while(true) {
            if (add) {
               if (!(off < max)) {
                  break;
               }
            } else if (!(off > max)) {
               break;
            }

            class_2338 pos = class_2338.method_49637(x, y - off, z);
            if (!(Boolean)this.noVoid.getValue() || pos.method_10264() >= 0) {
               if ((Boolean)this.skipZero.getValue() && Math.abs(y) < 1.0D) {
                  air = false;
                  last = pos;
                  lastOff = y - off;
               } else {
                  class_2680 state = mc.field_1687.method_8320(pos);
                  if (((Boolean)this.air.getValue() || state.method_51366()) && state.method_26204() != class_2246.field_10124) {
                     air = false;
                  } else {
                     if (air) {
                        if (add) {
                           return (Boolean)this.discrete.getValue() ? (double)pos.method_10264() : y - off;
                        }

                        return (Boolean)this.discrete.getValue() ? (double)last.method_10264() : lastOff;
                     }

                     air = true;
                  }

                  last = pos;
                  lastOff = y - off;
               }
            }

            off = add ? ++off : --off;
         }

         return Double.NaN;
      } else {
         return Double.NaN;
      }
   }

   protected double applyScale(double value) {
      if ((!(value < mc.field_1724.method_23318()) || (Boolean)this.scaleDown.getValue()) && ((Boolean)this.scaleExplosion.getValue() || (Boolean)this.scaleVelocity.getValue()) && !this.scaleTimer.passedMs((long)(Integer)this.scaleDelay.getValue()) && this.motionY != 0.0D) {
         if (value < mc.field_1724.method_23318()) {
            value -= this.motionY * (double)(Float)this.scaleFactor.getValue();
         } else {
            value += this.motionY * (double)(Float)this.scaleFactor.getValue();
         }

         return (Boolean)this.discrete.getValue() ? Math.floor(value) : value;
      } else {
         return value;
      }
   }

   public static class_2338 getPlayerPos() {
      return Math.abs(mc.field_1724.method_18798().method_10214()) > 0.1D ? class_2338.method_49638(mc.field_1724.method_19538()) : getPosition(mc.field_1724);
   }

   private static enum Mode {
      Default,
      Skull,
      Web;

      // $FF: synthetic method
      private static Burrow.Mode[] $values() {
         return new Burrow.Mode[]{Default, Skull, Web};
      }
   }

   public static enum OffsetMode {
      Constant,
      Smart;

      // $FF: synthetic method
      private static Burrow.OffsetMode[] $values() {
         return new Burrow.OffsetMode[]{Constant, Smart};
      }
   }
}
