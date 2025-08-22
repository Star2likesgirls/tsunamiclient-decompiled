package tsunami.core;

import com.mojang.blaze3d.systems.RenderSystem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_124;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_2338;
import net.minecraft.class_241;
import net.minecraft.class_2596;
import net.minecraft.class_2678;
import net.minecraft.class_2708;
import net.minecraft.class_2828;
import net.minecraft.class_2848;
import net.minecraft.class_2960;
import net.minecraft.class_332;
import net.minecraft.class_3417;
import net.minecraft.class_3419;
import net.minecraft.class_3532;
import net.minecraft.class_3675;
import net.minecraft.class_4587;
import net.minecraft.class_7439;
import net.minecraft.class_7833;
import net.minecraft.class_2828.class_5911;
import net.minecraft.class_2848.class_2849;
import org.jetbrains.annotations.NotNull;
import tsunami.TsunamiClient;
import tsunami.core.manager.client.MacroManager;
import tsunami.core.manager.client.ModuleManager;
import tsunami.events.impl.EventDeath;
import tsunami.events.impl.EventKeyPress;
import tsunami.events.impl.EventMouse;
import tsunami.events.impl.EventSync;
import tsunami.events.impl.PacketEvent;
import tsunami.events.impl.PlayerUpdateEvent;
import tsunami.features.cmd.Command;
import tsunami.features.modules.Module;
import tsunami.features.modules.client.ClientSettings;
import tsunami.features.modules.client.HudEditor;
import tsunami.gui.font.FontRenderers;
import tsunami.gui.notification.Notification;
import tsunami.gui.thundergui.ThunderGui;
import tsunami.utility.Timer;
import tsunami.utility.player.InteractionUtility;
import tsunami.utility.render.Render2DEngine;
import tsunami.utility.render.Render3DEngine;
import tsunami.utility.render.TextureStorage;
import tsunami.utility.render.animation.CaptureMark;

public final class Core {
   public static boolean lockSprint;
   public static boolean serverSprint;
   public static boolean hold_mouse0;
   public static boolean showSkull;
   public static final Map<String, class_2960> HEADS = new ConcurrentHashMap();
   public ArrayList<class_2596<?>> silentPackets = new ArrayList();
   private final Timer skullTimer = new Timer();
   private final Timer lastPacket = new Timer();
   private final Timer autoSave = new Timer();
   private final Timer setBackTimer = new Timer();

   @EventHandler
   public void onTick(PlayerUpdateEvent event) {
      if (!Module.fullNullCheck()) {
         Managers.NOTIFICATION.onUpdate();
         Managers.MODULE.onUpdate();
         ThunderGui.getInstance().onTick();
         if (ModuleManager.clickGui.getBind().getKey() == -1) {
            String var10000 = String.valueOf(class_124.field_1061);
            Command.sendMessage(var10000 + (ClientSettings.isRu() ? "Привязка клавиш Clickgui по умолчанию -> P" : "Default clickgui keybind --> P"));
            var10000 = String.valueOf(class_124.field_1061);
            Command.sendMessage(var10000 + (ClientSettings.isRu() ? "Вы можете получить готовую конфигурацию, выполнив следующую команду -> @cfg cloudlist." : "You can obtain a pre-built configuration by executing the following command -> @cfg cloudlist."));
            ModuleManager.clickGui.setBind(class_3675.method_15981("key.keyboard.p").method_1444(), false, false);
         }

         Iterator var2 = Module.mc.field_1687.method_18456().iterator();

         while(true) {
            class_1657 p;
            do {
               if (!var2.hasNext()) {
                  if (!Objects.equals(Managers.COMMAND.getPrefix(), ClientSettings.prefix.getValue())) {
                     Managers.COMMAND.setPrefix((String)ClientSettings.prefix.getValue());
                  }

                  (new HashMap(InteractionUtility.awaiting)).forEach((bp, time) -> {
                     if ((float)(System.currentTimeMillis() - time) > (float)Managers.SERVER.getPing() * 2.0F) {
                        InteractionUtility.awaiting.remove(bp);
                     }

                  });
                  if (this.autoSave.every(600000L)) {
                     Managers.FRIEND.saveFriends();
                     Managers.CONFIG.save(Managers.CONFIG.getCurrentConfig());
                     Managers.WAYPOINT.saveWayPoints();
                     Managers.MACRO.saveMacro();
                     Managers.NOTIFICATION.publicity("AutoSave", ClientSettings.isRu() ? "Сохраняю конфиг.." : "Saving config..", 3, Notification.Type.INFO);
                  }

                  return;
               }

               p = (class_1657)var2.next();
            } while(!p.method_29504() && p.method_6032() != 0.0F);

            TsunamiClient.EVENT_BUS.post((Object)(new EventDeath(p)));
         }
      }
   }

   @EventHandler
   public void onPacketSend(@NotNull PacketEvent.Send e) {
      if (e.getPacket() instanceof class_2828 && !(e.getPacket() instanceof class_5911)) {
         this.lastPacket.reset();
      }

      class_2596 var3 = e.getPacket();
      if (var3 instanceof class_2848) {
         class_2848 c = (class_2848)var3;
         if (c.method_12365() == class_2849.field_12981 || c.method_12365() == class_2849.field_12985) {
            if (lockSprint) {
               e.cancel();
               return;
            }

            switch(c.method_12365()) {
            case field_12981:
               serverSprint = true;
               break;
            case field_12985:
               serverSprint = false;
            }
         }
      }

   }

   @EventHandler
   public void onSync(EventSync event) {
      if (!Module.fullNullCheck()) {
         CaptureMark.tick();
         Render3DEngine.updateTargetESP();
      }
   }

   public void onRender2D(class_332 e) {
      this.drawGps(e);
      this.drawSkull(e);
   }

   @EventHandler
   public void onPacketReceive(PacketEvent.Receive e) {
      if (!Module.fullNullCheck()) {
         if (e.getPacket() instanceof class_7439) {
            class_7439 packet = (class_7439)e.getPacket();
            if (packet.comp_763().getString().contains("skull")) {
               showSkull = true;
               this.skullTimer.reset();
               Module.mc.field_1687.method_8396(Module.mc.field_1724, Module.mc.field_1724.method_24515(), class_3417.field_14877, class_3419.field_15245, 1.0F, 1.0F);
            }
         }

         if (e.getPacket() instanceof class_2678) {
            Managers.MODULE.onLogin();
         }

         if (e.getPacket() instanceof class_2708) {
            this.setBackTimer.reset();
         }

      }
   }

   public void drawSkull(class_332 e) {
      if (showSkull && !this.skullTimer.passedMs(3000L) && (Boolean)ClientSettings.skullEmoji.getValue()) {
         int xPos = (int)((float)Module.mc.method_22683().method_4486() / 2.0F - 150.0F);
         int yPos = (int)((float)Module.mc.method_22683().method_4502() / 2.0F - 150.0F);
         float alpha = 1.0F - (float)this.skullTimer.getPassedTimeMs() / 3000.0F;
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha);
         e.method_25290(TextureStorage.skull, xPos, yPos, 0.0F, 0.0F, 300, 300, 300, 300);
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      } else {
         showSkull = false;
      }

   }

   public void drawGps(class_332 e) {
      if (TsunamiClient.gps_position != null) {
         float dst = (float)this.getDistance(TsunamiClient.gps_position);
         float xOffset = (float)Module.mc.method_22683().method_4486() / 2.0F;
         float yOffset = (float)Module.mc.method_22683().method_4502() / 2.0F;
         float yaw = getRotations(new class_241((float)TsunamiClient.gps_position.method_10263(), (float)TsunamiClient.gps_position.method_10260())) - Module.mc.field_1724.method_36454();
         e.method_51448().method_46416(xOffset, yOffset, 0.0F);
         e.method_51448().method_22907(class_7833.field_40718.rotationDegrees(yaw));
         e.method_51448().method_46416(-xOffset, -yOffset, 0.0F);
         Render2DEngine.drawTracerPointer(e.method_51448(), xOffset, yOffset - 50.0F, 12.5F, 0.5F, 3.63F, true, true, HudEditor.getColor(1).getRGB());
         e.method_51448().method_46416(xOffset, yOffset, 0.0F);
         e.method_51448().method_22907(class_7833.field_40718.rotationDegrees(-yaw));
         e.method_51448().method_46416(-xOffset, -yOffset, 0.0F);
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
         FontRenderers.modules.drawCenteredString(e.method_51448(), "gps (" + dst + "m)", (double)((float)(Math.sin(Math.toRadians((double)yaw)) * 50.0D) + xOffset), (double)((float)((double)yOffset - Math.cos(Math.toRadians((double)yaw)) * 50.0D) - 23.0F), -1);
         if (dst < 10.0F) {
            TsunamiClient.gps_position = null;
         }
      }

   }

   @EventHandler
   public void onKeyPress(EventKeyPress event) {
      if (event.getKey() != -1) {
         Iterator var2 = Managers.MACRO.getMacros().iterator();

         while(var2.hasNext()) {
            MacroManager.Macro m = (MacroManager.Macro)var2.next();
            if (m.getBind() == event.getKey()) {
               m.runMacro();
            }
         }

      }
   }

   @EventHandler
   public void onMouse(EventMouse event) {
      if (event.getAction() == 0) {
         hold_mouse0 = false;
      }

      if (event.getAction() == 1) {
         hold_mouse0 = true;
      }

   }

   public int getDistance(class_2338 bp) {
      double d0 = Module.mc.field_1724.method_23317() - (double)bp.method_10263();
      double d2 = Module.mc.field_1724.method_23321() - (double)bp.method_10260();
      return (int)class_3532.method_15355((float)(d0 * d0 + d2 * d2));
   }

   public long getSetBackTime() {
      return this.setBackTimer.getPassedTimeMs();
   }

   public static float getRotations(class_241 vec) {
      if (Module.mc.field_1724 == null) {
         return 0.0F;
      } else {
         double x = (double)vec.field_1343 - Module.mc.field_1724.method_19538().field_1352;
         double z = (double)vec.field_1342 - Module.mc.field_1724.method_19538().field_1350;
         return (float)(-(Math.atan2(x, z) * 57.29577951308232D));
      }
   }

   public void bobView(class_4587 matrices, float tickDelta) {
      class_1297 var4 = Module.mc.method_1560();
      if (var4 instanceof class_1657) {
         class_1657 playerEntity = (class_1657)var4;
         float g = -(playerEntity.field_5973 + (playerEntity.field_5973 - playerEntity.field_6039) * tickDelta);
         float h = class_3532.method_16439(tickDelta, playerEntity.field_7505, playerEntity.field_7483);
         matrices.method_22904((double)(class_3532.method_15374(g * 3.1415927F) * h * 0.1F), (double)(-Math.abs(class_3532.method_15362(g * 3.1415927F) * h)) * 0.3D, 0.0D);
         matrices.method_22907(class_7833.field_40718.rotationDegrees(class_3532.method_15374(g * 3.1415927F) * h * 3.0F));
         matrices.method_22907(class_7833.field_40714.rotationDegrees(Math.abs(class_3532.method_15362(g * 3.1415927F - 0.2F) * h) * 0.3F));
      }
   }
}
