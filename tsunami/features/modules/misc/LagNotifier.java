package tsunami.features.modules.misc;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.text.DecimalFormat;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_2708;
import net.minecraft.class_2761;
import net.minecraft.class_332;
import tsunami.core.Managers;
import tsunami.core.manager.client.ModuleManager;
import tsunami.events.impl.PacketEvent;
import tsunami.features.modules.Module;
import tsunami.features.modules.client.ClientSettings;
import tsunami.gui.font.FontRenderers;
import tsunami.gui.notification.Notification;
import tsunami.setting.Setting;
import tsunami.utility.Timer;
import tsunami.utility.render.Render2DEngine;
import tsunami.utility.render.TextureStorage;

public class LagNotifier extends Module {
   private final Setting<Boolean> rubberbandNotify = new Setting("Rubberband", true);
   private final Setting<Boolean> serverResponseNotify = new Setting("ServerResponse", true);
   private final Setting<Integer> responseTreshold = new Setting("ResponseTreshold", 5, 0, 15, (v) -> {
      return (Boolean)this.serverResponseNotify.getValue();
   });
   private final Setting<Boolean> tpsNotify = new Setting("TPS", true);
   private Timer notifyTimer = new Timer();
   private Timer rubberbandTimer = new Timer();
   private Timer packetTimer = new Timer();
   private boolean isLagging = false;

   public LagNotifier() {
      super("LagNotifier", Module.Category.NONE);
   }

   public void onEnable() {
      this.notifyTimer = new Timer();
      this.rubberbandTimer = new Timer();
      this.packetTimer = new Timer();
      this.isLagging = false;
      super.onEnable();
   }

   @EventHandler
   public void onPacketReceive(PacketEvent.Receive e) {
      if (!fullNullCheck()) {
         if (e.getPacket() instanceof class_2708) {
            this.rubberbandTimer.reset();
         }

         if (e.getPacket() instanceof class_2761) {
            this.packetTimer.reset();
         }

      }
   }

   public void onRender2D(class_332 context) {
      Render2DEngine.setupRender();
      RenderSystem.defaultBlendFunc();
      DecimalFormat decimalFormat;
      if (!this.rubberbandTimer.passedMs(5000L) && (Boolean)this.rubberbandNotify.getValue()) {
         decimalFormat = new DecimalFormat("#.#");
         FontRenderers.modules.drawCenteredString(context.method_51448(), (ClientSettings.isRu() ? "Обнаружен руббербенд! " : "Rubberband detected! ") + decimalFormat.format((double)((5000.0F - (float)this.rubberbandTimer.getTimeMs()) / 1000.0F)), (double)((float)mc.method_22683().method_4486() / 2.0F), (double)((float)mc.method_22683().method_4502() / 3.0F), (new Color(16768768)).getRGB());
      }

      if (this.packetTimer.passedMs((long)(Integer)this.responseTreshold.getValue() * 1000L) && (Boolean)this.serverResponseNotify.getValue()) {
         decimalFormat = new DecimalFormat("#.#");
         FontRenderers.modules.drawCenteredString(context.method_51448(), (ClientSettings.isRu() ? "Сервер перестал отвечать! " : "The server stopped responding! ") + decimalFormat.format((double)((float)this.packetTimer.getTimeMs() / 1000.0F)), (double)((float)mc.method_22683().method_4486() / 2.0F), (double)((float)mc.method_22683().method_4502() / 3.0F), (new Color(16768768)).getRGB());
         RenderSystem.setShaderColor(1.0F, 0.87F, 0.0F, 1.0F);
         context.method_25290(TextureStorage.lagIcon, (int)((float)mc.method_22683().method_4486() / 2.0F - 40.0F), (int)((float)mc.method_22683().method_4502() / 3.0F - 120.0F), 0.0F, 0.0F, 80, 80, 80, 80);
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      }

      if (Managers.SERVER.getTPS() < 10.0F && this.notifyTimer.passedMs(60000L) && (Boolean)this.tpsNotify.getValue()) {
         String msg = ClientSettings.isRu() ? "ТПС сервера ниже 10!" : "Server TPS is below 10!";
         if (ModuleManager.tpsSync.isDisabled()) {
            msg = msg + (ClientSettings.isRu() ? " Рекомендуется включить TPSSync" : "It is recommended to enable TPSSync");
         }

         Managers.NOTIFICATION.publicity("LagNotifier", msg, 8, Notification.Type.ERROR);
         this.isLagging = true;
         this.notifyTimer.reset();
      }

      if (Managers.SERVER.getTPS() > 15.0F && this.isLagging) {
         Managers.NOTIFICATION.publicity("LagNotifier", ClientSettings.isRu() ? "ТПС сервера стабилизировался!" : "Server TPS has stabilized!", 8, Notification.Type.SUCCESS);
         this.isLagging = false;
      }

      Render2DEngine.endRender();
   }
}
