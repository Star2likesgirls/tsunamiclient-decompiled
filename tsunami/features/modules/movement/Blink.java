package tsunami.features.modules.movement;

import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_243;
import net.minecraft.class_2596;
import net.minecraft.class_2743;
import net.minecraft.class_2793;
import net.minecraft.class_2797;
import net.minecraft.class_2799;
import net.minecraft.class_2827;
import net.minecraft.class_2828;
import net.minecraft.class_2859;
import net.minecraft.class_4587;
import net.minecraft.class_6374;
import net.minecraft.class_746;
import net.minecraft.class_2828.class_2831;
import tsunami.events.impl.EventTick;
import tsunami.events.impl.PacketEvent;
import tsunami.features.modules.Module;
import tsunami.features.modules.client.ClientSettings;
import tsunami.setting.Setting;
import tsunami.setting.impl.Bind;
import tsunami.setting.impl.ColorSetting;
import tsunami.utility.player.PlayerEntityCopy;
import tsunami.utility.render.Render3DEngine;

public class Blink extends Module {
   private final Setting<Boolean> pulse = new Setting("Pulse", false);
   private final Setting<Boolean> autoDisable = new Setting("AutoDisable", false);
   private final Setting<Boolean> disableOnVelocity = new Setting("DisableOnVelocity", false);
   private final Setting<Integer> disablePackets = new Setting("DisablePackets", 17, 1, 1000, (v) -> {
      return (Boolean)this.autoDisable.getValue();
   });
   private final Setting<Integer> pulsePackets = new Setting("PulsePackets", 20, 1, 1000, (v) -> {
      return (Boolean)this.pulse.getValue();
   });
   private final Setting<Boolean> render = new Setting("Render", true);
   private final Setting<Blink.RenderMode> renderMode;
   private final Setting<ColorSetting> circleColor;
   private final Setting<Bind> cancel;
   private PlayerEntityCopy blinkPlayer;
   public static class_243 lastPos;
   private class_243 prevVelocity;
   private float prevYaw;
   private boolean prevSprinting;
   private final Queue<class_2596<?>> storedPackets;
   private final Queue<class_2596<?>> storedTransactions;
   private final AtomicBoolean sending;

   public Blink() {
      super("Blink", Module.Category.NONE);
      this.renderMode = new Setting("Render Mode", Blink.RenderMode.Circle, (value) -> {
         return (Boolean)this.render.getValue();
      });
      this.circleColor = new Setting("Color", new ColorSetting(-2464668), (value) -> {
         return (Boolean)this.render.getValue() && this.renderMode.getValue() == Blink.RenderMode.Circle || this.renderMode.getValue() == Blink.RenderMode.Both;
      });
      this.cancel = new Setting("Cancel", new Bind(340, false, false));
      this.prevVelocity = class_243.field_1353;
      this.prevYaw = 0.0F;
      this.prevSprinting = false;
      this.storedPackets = new LinkedList();
      this.storedTransactions = new LinkedList();
      this.sending = new AtomicBoolean(false);
   }

   public void onEnable() {
      if (mc.field_1724 != null && mc.field_1687 != null && !mc.method_1496() && mc.method_1562() != null) {
         this.storedTransactions.clear();
         lastPos = mc.field_1724.method_19538();
         this.prevVelocity = mc.field_1724.method_18798();
         this.prevYaw = mc.field_1724.method_36454();
         this.prevSprinting = mc.field_1724.method_5624();
         mc.field_1687.method_8649(new class_746(mc, mc.field_1687, mc.method_1562(), mc.field_1724.method_3143(), mc.field_1724.method_3130(), mc.field_1724.field_3919, mc.field_1724.method_5715()));
         this.sending.set(false);
         this.storedPackets.clear();
      } else {
         this.disable();
      }
   }

   public void onDisable() {
      if (mc.field_1687 != null && mc.field_1724 != null) {
         while(!this.storedPackets.isEmpty()) {
            this.sendPacket((class_2596)this.storedPackets.poll());
         }

         if (this.blinkPlayer != null) {
            this.blinkPlayer.deSpawn();
         }

         this.blinkPlayer = null;
      }
   }

   public String getDisplayInfo() {
      return Integer.toString(this.storedPackets.size());
   }

   @EventHandler
   public void onPacketReceive(PacketEvent.Receive event) {
      class_2596 var3 = event.getPacket();
      if (var3 instanceof class_2743) {
         class_2743 vel = (class_2743)var3;
         if (vel.method_11818() == mc.field_1724.method_5628() && (Boolean)this.disableOnVelocity.getValue()) {
            this.disable(ClientSettings.isRu() ? "Выключенно из-за велосити!" : "Disabled due to velocity!");
         }
      }

   }

   @EventHandler
   public void onPacketSend(PacketEvent.Send event) {
      if (!fullNullCheck()) {
         class_2596<?> packet = event.getPacket();
         if (!this.sending.get()) {
            if (packet instanceof class_6374) {
               this.storedTransactions.add(packet);
            }

            if ((Boolean)this.pulse.getValue()) {
               if (packet instanceof class_2828) {
                  event.cancel();
                  this.storedPackets.add(packet);
               }
            } else if (!(packet instanceof class_2797) && !(packet instanceof class_2793) && !(packet instanceof class_2827) && !(packet instanceof class_2859) && !(packet instanceof class_2799)) {
               event.cancel();
               this.storedPackets.add(packet);
            }

         }
      }
   }

   @EventHandler
   public void onUpdate(EventTick event) {
      if (!fullNullCheck()) {
         if (!this.isKeyPressed(this.cancel)) {
            if ((Boolean)this.pulse.getValue() && this.storedPackets.size() >= (Integer)this.pulsePackets.getValue()) {
               this.sendPackets();
            }

            if ((Boolean)this.autoDisable.getValue() && this.storedPackets.size() >= (Integer)this.disablePackets.getValue()) {
               this.disable();
            }

         } else {
            this.storedPackets.clear();
            mc.field_1724.method_23327(lastPos.method_10216(), lastPos.method_10214(), lastPos.method_10215());
            mc.field_1724.method_18799(this.prevVelocity);
            mc.field_1724.method_36456(this.prevYaw);
            mc.field_1724.method_5728(this.prevSprinting);
            mc.field_1724.method_5660(false);
            mc.field_1690.field_1832.method_23481(false);
            this.sending.set(true);

            while(!this.storedTransactions.isEmpty()) {
               this.sendPacket((class_2596)this.storedTransactions.poll());
            }

            this.sending.set(false);
            this.disable(ClientSettings.isRu() ? "Отменяю.." : "Canceling..");
         }
      }
   }

   private void sendPackets() {
      if (mc.field_1724 != null) {
         this.sending.set(true);

         while(true) {
            do {
               class_2596 packet;
               do {
                  do {
                     if (this.storedPackets.isEmpty()) {
                        this.sending.set(false);
                        this.storedPackets.clear();
                        return;
                     }

                     packet = (class_2596)this.storedPackets.poll();
                     this.sendPacket(packet);
                  } while(!(packet instanceof class_2828));
               } while(packet instanceof class_2831);

               lastPos = new class_243(((class_2828)packet).method_12269(mc.field_1724.method_23317()), ((class_2828)packet).method_12268(mc.field_1724.method_23318()), ((class_2828)packet).method_12274(mc.field_1724.method_23321()));
            } while(this.renderMode.getValue() != Blink.RenderMode.Model && this.renderMode.getValue() != Blink.RenderMode.Both);

            this.blinkPlayer.deSpawn();
            this.blinkPlayer = new PlayerEntityCopy();
            this.blinkPlayer.spawn();
         }
      }
   }

   public void onRender3D(class_4587 stack) {
      if (mc.field_1724 != null && mc.field_1687 != null) {
         if ((Boolean)this.render.getValue() && lastPos != null) {
            if (this.renderMode.getValue() == Blink.RenderMode.Circle || this.renderMode.getValue() == Blink.RenderMode.Both) {
               float[] hsb = Color.RGBtoHSB(((ColorSetting)this.circleColor.getValue()).getRed(), ((ColorSetting)this.circleColor.getValue()).getGreen(), ((ColorSetting)this.circleColor.getValue()).getBlue(), (float[])null);
               float hue = (float)(System.currentTimeMillis() % 7200L) / 7200.0F;
               int rgb = Color.getHSBColor(hue, hsb[1], hsb[2]).getRGB();
               ArrayList<class_243> vecs = new ArrayList();
               double x = lastPos.field_1352;
               double y = lastPos.field_1351;
               double z = lastPos.field_1350;

               int j;
               for(j = 0; j <= 360; ++j) {
                  class_243 vec = new class_243(x + Math.sin((double)j * 3.141592653589793D / 180.0D) * 0.5D, y + 0.01D, z + Math.cos((double)j * 3.141592653589793D / 180.0D) * 0.5D);
                  vecs.add(vec);
               }

               for(j = 0; j < vecs.size() - 1; ++j) {
                  Render3DEngine.drawLine((class_243)vecs.get(j), (class_243)vecs.get(j + 1), new Color(rgb));
                  hue += 0.0027777778F;
                  rgb = Color.getHSBColor(hue, hsb[1], hsb[2]).getRGB();
               }
            }

            if ((this.renderMode.getValue() == Blink.RenderMode.Model || this.renderMode.getValue() == Blink.RenderMode.Both) && this.blinkPlayer == null) {
               this.blinkPlayer = new PlayerEntityCopy();
               this.blinkPlayer.spawn();
            }
         }

      }
   }

   static {
      lastPos = class_243.field_1353;
   }

   private static enum RenderMode {
      Circle,
      Model,
      Both;

      // $FF: synthetic method
      private static Blink.RenderMode[] $values() {
         return new Blink.RenderMode[]{Circle, Model, Both};
      }
   }
}
