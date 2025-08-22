package tsunami.injection;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.proxy.Socks5ProxyHandler;
import java.net.InetSocketAddress;
import net.minecraft.class_2535;
import net.minecraft.class_2547;
import net.minecraft.class_2596;
import net.minecraft.class_2598;
import net.minecraft.class_8042;
import net.minecraft.class_8762;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tsunami.TsunamiClient;
import tsunami.core.Managers;
import tsunami.core.manager.client.ModuleManager;
import tsunami.core.manager.client.ProxyManager;
import tsunami.events.impl.PacketEvent;
import tsunami.features.modules.Module;

@Mixin({class_2535.class})
public class MixinClientConnection {
   @Inject(
      method = {"exceptionCaught"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void exceptionCaughtHook(ChannelHandlerContext context, Throwable t, CallbackInfo ci) {
      if (ModuleManager.antiPacketException.isEnabled()) {
         ModuleManager.antiPacketException.sendChatMessage(t.getMessage());
         ci.cancel();
      }

   }

   @Inject(
      method = {"handlePacket"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private static <T extends class_2547> void onHandlePacket(class_2596<T> packet, class_2547 listener, CallbackInfo info) {
      if (!Module.fullNullCheck()) {
         if (packet instanceof class_8042) {
            class_8042 packs = (class_8042)packet;
            packs.method_48324().forEach((p) -> {
               PacketEvent.Receive event = new PacketEvent.Receive(p);
               TsunamiClient.EVENT_BUS.post((Object)event);
               if (event.isCancelled()) {
                  info.cancel();
               }

            });
         } else {
            PacketEvent.Receive event = new PacketEvent.Receive(packet);
            TsunamiClient.EVENT_BUS.post((Object)event);
            if (event.isCancelled()) {
               info.cancel();
            }
         }

      }
   }

   @Inject(
      method = {"handlePacket"},
      at = {@At("TAIL")},
      cancellable = true
   )
   private static <T extends class_2547> void onHandlePacketPost(class_2596<T> packet, class_2547 listener, CallbackInfo info) {
      if (!Module.fullNullCheck()) {
         if (packet instanceof class_8042) {
            class_8042 packs = (class_8042)packet;
            packs.method_48324().forEach((p) -> {
               PacketEvent.ReceivePost event = new PacketEvent.ReceivePost(p);
               TsunamiClient.EVENT_BUS.post((Object)event);
               if (event.isCancelled()) {
                  info.cancel();
               }

            });
         } else {
            PacketEvent.ReceivePost event = new PacketEvent.ReceivePost(packet);
            TsunamiClient.EVENT_BUS.post((Object)event);
            if (event.isCancelled()) {
               info.cancel();
            }
         }

      }
   }

   @Inject(
      method = {"send(Lnet/minecraft/network/packet/Packet;)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onSendPacketPre(class_2596<?> packet, CallbackInfo info) {
      if (!Module.fullNullCheck()) {
         if (TsunamiClient.core.silentPackets.contains(packet)) {
            TsunamiClient.core.silentPackets.remove(packet);
         } else {
            PacketEvent.Send event = new PacketEvent.Send(packet);
            TsunamiClient.EVENT_BUS.post((Object)event);
            if (event.isCancelled()) {
               info.cancel();
            }

         }
      }
   }

   @Inject(
      method = {"send(Lnet/minecraft/network/packet/Packet;)V"},
      at = {@At("RETURN")},
      cancellable = true
   )
   private void onSendPacketPost(class_2596<?> packet, CallbackInfo info) {
      if (!Module.fullNullCheck()) {
         PacketEvent.SendPost event = new PacketEvent.SendPost(packet);
         TsunamiClient.EVENT_BUS.post((Object)event);
         if (event.isCancelled()) {
            info.cancel();
         }

      }
   }

   @Inject(
      method = {"addHandlers"},
      at = {@At("RETURN")}
   )
   private static void addHandlersHook(ChannelPipeline pipeline, class_2598 side, boolean local, class_8762 packetSizeLogger, CallbackInfo ci) {
      ProxyManager.ThProxy proxy = Managers.PROXY.getActiveProxy();
      if (proxy != null && side == class_2598.field_11942 && !local) {
         pipeline.addFirst(new ChannelHandler[]{new Socks5ProxyHandler(new InetSocketAddress(proxy.getIp(), proxy.getPort()), proxy.getL(), proxy.getP())});
      }

   }
}
