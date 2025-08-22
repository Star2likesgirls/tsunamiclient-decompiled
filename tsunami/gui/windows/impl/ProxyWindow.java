package tsunami.gui.windows.impl;

import com.google.common.collect.Lists;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import net.minecraft.class_124;
import net.minecraft.class_332;
import net.minecraft.class_3544;
import net.minecraft.class_3675;
import net.minecraft.class_4587;
import org.lwjgl.glfw.GLFW;
import tsunami.core.Managers;
import tsunami.core.manager.client.ProxyManager;
import tsunami.features.modules.Module;
import tsunami.features.modules.client.ClientSettings;
import tsunami.features.modules.client.HudEditor;
import tsunami.gui.clickui.ClickGUI;
import tsunami.gui.clickui.impl.SliderElement;
import tsunami.gui.font.FontRenderer;
import tsunami.gui.font.FontRenderers;
import tsunami.gui.windows.WindowBase;
import tsunami.setting.Setting;
import tsunami.setting.impl.ColorSetting;
import tsunami.setting.impl.PositionSetting;
import tsunami.utility.render.Render2DEngine;
import tsunami.utility.render.TextureStorage;

public class ProxyWindow extends WindowBase {
   private static ProxyWindow instance;
   private ArrayList<ProxyWindow.ProxyPlate> proxyPlates = new ArrayList();
   private int listeningId = -1;
   private ProxyWindow.ListeningType listeningType;
   private String search = "Search";
   private String addIp = "Ip";
   private String addPort = "Port";
   private String addPassword = "Password";
   private String addLogin = "Login";
   private String addName = "Name";
   private String[] pinging = new String[]{".", "..", "..."};

   public ProxyWindow(float x, float y, float width, float height, Setting<PositionSetting> position) {
      super(x, y, width, height, "Proxies", position, TextureStorage.proxyIcon);
      this.refresh();
   }

   public static ProxyWindow get(float x, float y, Setting<PositionSetting> position) {
      if (instance == null) {
         instance = new ProxyWindow(x, y, 340.0F, 180.0F, position);
      }

      instance.refresh();
      return instance;
   }

   public void render(class_332 context, int mouseX, int mouseY) {
      super.render(context, mouseX, mouseY);
      Color color = new Color(-986500301, true);
      Color color2 = new Color(-983868581, true);
      Color hoveredColor = new Color(-985052855, true);
      int textColor = (new Color(12434877)).getRGB();
      boolean hover1 = Render2DEngine.isHovered((double)mouseX, (double)mouseY, (double)(this.getX() + this.getWidth() - 90.0F), (double)(this.getY() + 3.0F), 70.0D, 10.0D);
      Render2DEngine.drawRectWithOutline(context.method_51448(), this.getX() + this.getWidth() - 90.0F, this.getY() + 3.0F, 70.0F, 10.0F, hover1 ? hoveredColor : color, color2);
      FontRenderers.sf_medium_mini.drawString(context.method_51448(), this.search, (double)(this.getX() + this.getWidth() - 86.0F), (double)(this.getY() + 7.0F), (new Color(14013909)).getRGB());
      if (this.proxyPlates.isEmpty()) {
         FontRenderers.sf_medium.drawCenteredString(context.method_51448(), ClientSettings.isRu() ? "Тут пока пусто" : "It's empty here yet", (double)(this.getX() + this.getWidth() / 2.0F), (double)(this.getY() + this.getHeight() / 2.0F), (new Color(12434877)).getRGB());
      }

      String blink = System.currentTimeMillis() / 240L % 2L == 0L ? "" : "l";
      float nameX = this.getX() + 11.0F;
      float nameWidth = this.getWidth() / 5.5F;
      float ipX = nameX + nameWidth + 2.0F;
      float posXWidth = this.getWidth() / 5.0F;
      float portX = ipX + posXWidth + 2.0F;
      float posYWidth = this.getWidth() / 12.0F;
      float posZX = portX + posYWidth + 2.0F;
      float posZWidth = this.getWidth() / 6.5F;
      float serverX = posZX + posZWidth + 2.0F;
      float serverWidth = this.getWidth() / 6.0F;
      boolean hover2 = Render2DEngine.isHovered((double)mouseX, (double)mouseY, (double)nameX, (double)(this.getY() + 19.0F), (double)nameWidth, 11.0D);
      Render2DEngine.drawRectWithOutline(context.method_51448(), nameX, this.getY() + 19.0F, nameWidth, 11.0F, hover2 ? hoveredColor : color, color2);
      FontRenderer var10000 = FontRenderers.sf_medium;
      class_4587 var10001 = context.method_51448();
      String var10002 = this.addName;
      var10000.drawString(var10001, var10002 + (this.listeningId == -3 && this.listeningType == ProxyWindow.ListeningType.Name ? blink : ""), (double)(nameX + 2.0F), (double)(this.getY() + 23.0F), (new Color(12434877)).getRGB());
      boolean hover3 = Render2DEngine.isHovered((double)mouseX, (double)mouseY, (double)ipX, (double)(this.getY() + 19.0F), (double)posXWidth, 11.0D);
      Render2DEngine.drawRectWithOutline(context.method_51448(), ipX, this.getY() + 19.0F, posXWidth, 11.0F, hover3 ? hoveredColor : color, color2);
      var10000 = FontRenderers.sf_medium;
      var10001 = context.method_51448();
      var10002 = this.addIp;
      var10000.drawString(var10001, var10002 + (this.listeningId == -3 && this.listeningType == ProxyWindow.ListeningType.Ip ? blink : ""), (double)(ipX + 2.0F), (double)(this.getY() + 23.0F), textColor);
      boolean hover4 = Render2DEngine.isHovered((double)mouseX, (double)mouseY, (double)portX, (double)(this.getY() + 19.0F), (double)posYWidth, 11.0D);
      Render2DEngine.drawRectWithOutline(context.method_51448(), portX, this.getY() + 19.0F, posYWidth, 11.0F, hover4 ? hoveredColor : color, color2);
      var10000 = FontRenderers.sf_medium;
      var10001 = context.method_51448();
      var10002 = this.addPort;
      var10000.drawString(var10001, var10002 + (this.listeningId == -3 && this.listeningType == ProxyWindow.ListeningType.Port ? blink : ""), (double)(portX + 2.0F), (double)(this.getY() + 23.0F), (new Color(12434877)).getRGB());
      boolean hover2 = Render2DEngine.isHovered((double)mouseX, (double)mouseY, (double)posZX, (double)(this.getY() + 19.0F), (double)posZWidth, 11.0D);
      Render2DEngine.drawRectWithOutline(context.method_51448(), posZX, this.getY() + 19.0F, posZWidth, 11.0F, hover2 ? hoveredColor : color, color2);
      var10000 = FontRenderers.sf_medium;
      var10001 = context.method_51448();
      var10002 = this.addLogin;
      var10000.drawString(var10001, var10002 + (this.listeningId == -3 && this.listeningType == ProxyWindow.ListeningType.Login ? blink : ""), (double)(posZX + 2.0F), (double)(this.getY() + 23.0F), (new Color(12434877)).getRGB());
      boolean hover3 = Render2DEngine.isHovered((double)mouseX, (double)mouseY, (double)serverX, (double)(this.getY() + 19.0F), (double)serverWidth, 11.0D);
      Render2DEngine.drawRectWithOutline(context.method_51448(), serverX, this.getY() + 19.0F, serverWidth, 11.0F, hover3 ? hoveredColor : color, color2);
      var10000 = FontRenderers.sf_medium;
      var10001 = context.method_51448();
      var10002 = this.addPassword;
      var10000.drawString(var10001, var10002 + (this.listeningId == -3 && this.listeningType == ProxyWindow.ListeningType.Password ? blink : ""), (double)(serverX + 2.0F), (double)(this.getY() + 23.0F), (new Color(12434877)).getRGB());
      boolean hover4 = Render2DEngine.isHovered((double)mouseX, (double)mouseY, (double)(this.getX() + this.getWidth() - 15.0F), (double)(this.getY() + 19.0F), 11.0D, 11.0D);
      Render2DEngine.drawRectWithOutline(context.method_51448(), this.getX() + this.getWidth() - 15.0F, this.getY() + 19.0F, 11.0F, 11.0F, hover4 ? hoveredColor : color, color2);
      FontRenderers.categories.drawString(context.method_51448(), "+", (double)(this.getX() + this.getWidth() - 12.0F), (double)(this.getY() + 23.0F), -1);
      Render2DEngine.horizontalGradient(context.method_51448(), this.getX() + 2.0F, this.getY() + 33.0F, this.getX() + 2.0F + this.getWidth() / 2.0F - 2.0F, this.getY() + 33.5F, Render2DEngine.injectAlpha(((ColorSetting)HudEditor.textColor.getValue()).getColorObject(), 0), ((ColorSetting)HudEditor.textColor.getValue()).getColorObject());
      Render2DEngine.horizontalGradient(context.method_51448(), this.getX() + 2.0F + this.getWidth() / 2.0F - 2.0F, this.getY() + 33.0F, this.getX() + 2.0F + this.getWidth() - 4.0F, this.getY() + 33.5F, ((ColorSetting)HudEditor.textColor.getValue()).getColorObject(), Render2DEngine.injectAlpha(((ColorSetting)HudEditor.textColor.getValue()).getColorObject(), 0));
      FontRenderers.sf_medium.drawCenteredString(context.method_51448(), "Name", (double)(nameX + nameWidth / 2.0F), (double)(this.getY() + 40.0F), textColor);
      FontRenderers.sf_medium.drawCenteredString(context.method_51448(), "IP", (double)(ipX + posXWidth / 2.0F), (double)(this.getY() + 40.0F), textColor);
      FontRenderers.sf_medium.drawCenteredString(context.method_51448(), "Port", (double)(portX + posYWidth / 2.0F), (double)(this.getY() + 40.0F), textColor);
      FontRenderers.sf_medium.drawCenteredString(context.method_51448(), "Login", (double)(posZX + posZWidth / 2.0F), (double)(this.getY() + 40.0F), textColor);
      FontRenderers.sf_medium.drawCenteredString(context.method_51448(), "Password", (double)(serverX + serverWidth / 2.0F), (double)(this.getY() + 40.0F), textColor);
      Render2DEngine.addWindow(context.method_51448(), this.getX(), this.getY() + 50.0F, this.getX() + this.getWidth(), this.getY() + this.getHeight() - 1.0F, 1.0D);
      int id = 0;
      Iterator var35 = this.proxyPlates.iterator();

      while(true) {
         ProxyWindow.ProxyPlate proxyPlate;
         do {
            do {
               if (!var35.hasNext()) {
                  this.setMaxElementsHeight((float)(this.proxyPlates.size() * 20));
                  Render2DEngine.popWindow();
                  return;
               }

               proxyPlate = (ProxyWindow.ProxyPlate)var35.next();
               ++id;
            } while((float)((int)(proxyPlate.offset + this.getY() + 25.0F)) + this.getScrollOffset() > this.getY() + this.getHeight());
         } while(proxyPlate.offset + this.getScrollOffset() + this.getY() + 10.0F < this.getY());

         hover2 = Render2DEngine.isHovered((double)mouseX, (double)mouseY, (double)nameX, (double)(this.getY() + 36.0F + this.getScrollOffset() + proxyPlate.offset), (double)nameWidth, 11.0D);
         Render2DEngine.drawRectWithOutline(context.method_51448(), nameX, this.getY() + 36.0F + this.getScrollOffset() + proxyPlate.offset, nameWidth, 11.0F, hover2 ? hoveredColor : color, color2);
         var10000 = FontRenderers.sf_medium;
         var10001 = context.method_51448();
         var10002 = proxyPlate.proxy.getName();
         var10000.drawString(var10001, var10002 + (this.listeningId == proxyPlate.id && this.listeningType == ProxyWindow.ListeningType.Name ? blink : ""), (double)(nameX + 2.0F), (double)(this.getY() + 40.0F + this.getScrollOffset() + proxyPlate.offset), (new Color(12434877)).getRGB());
         hover3 = Render2DEngine.isHovered((double)mouseX, (double)mouseY, (double)ipX, (double)(this.getY() + 36.0F + this.getScrollOffset() + proxyPlate.offset), (double)posXWidth, 11.0D);
         Render2DEngine.drawRectWithOutline(context.method_51448(), ipX, this.getY() + 36.0F + this.getScrollOffset() + proxyPlate.offset, posXWidth, 11.0F, hover3 ? hoveredColor : color, color2);
         var10000 = FontRenderers.sf_medium;
         var10001 = context.method_51448();
         var10002 = proxyPlate.proxy.getIp();
         var10000.drawString(var10001, var10002 + (this.listeningId == proxyPlate.id && this.listeningType == ProxyWindow.ListeningType.Ip ? blink : ""), (double)(ipX + 2.0F), (double)(this.getY() + 40.0F + this.getScrollOffset() + proxyPlate.offset), textColor);
         hover4 = Render2DEngine.isHovered((double)mouseX, (double)mouseY, (double)portX, (double)(this.getY() + 36.0F + this.getScrollOffset() + proxyPlate.offset), (double)posYWidth, 11.0D);
         Render2DEngine.drawRectWithOutline(context.method_51448(), portX, this.getY() + 36.0F + this.getScrollOffset() + proxyPlate.offset, posYWidth, 11.0F, hover4 ? hoveredColor : color, color2);
         var10000 = FontRenderers.sf_medium;
         var10001 = context.method_51448();
         int var34 = proxyPlate.proxy.getPort();
         var10000.drawString(var10001, var34 + (this.listeningId == proxyPlate.id && this.listeningType == ProxyWindow.ListeningType.Port ? blink : ""), (double)(portX + 2.0F), (double)(this.getY() + 40.0F + this.getScrollOffset() + proxyPlate.offset), (new Color(12434877)).getRGB());
         boolean hover5 = Render2DEngine.isHovered((double)mouseX, (double)mouseY, (double)posZX, (double)(this.getY() + 36.0F + this.getScrollOffset() + proxyPlate.offset), (double)posZWidth, 11.0D);
         Render2DEngine.drawRectWithOutline(context.method_51448(), posZX, this.getY() + 36.0F + this.getScrollOffset() + proxyPlate.offset, posZWidth, 11.0F, hover5 ? hoveredColor : color, color2);
         FontRenderers.sf_medium.drawString(context.method_51448(), !hover5 ? "*******" : proxyPlate.proxy.getL() + (this.listeningId == proxyPlate.id && this.listeningType == ProxyWindow.ListeningType.Login ? blink : ""), (double)(posZX + 2.0F), (double)(this.getY() + 40.0F + this.getScrollOffset() + proxyPlate.offset), (new Color(12434877)).getRGB());
         boolean hover6 = Render2DEngine.isHovered((double)mouseX, (double)mouseY, (double)serverX, (double)(this.getY() + 36.0F + this.getScrollOffset() + proxyPlate.offset), (double)serverWidth, 11.0D);
         Render2DEngine.drawRectWithOutline(context.method_51448(), serverX, this.getY() + 36.0F + this.getScrollOffset() + proxyPlate.offset, serverWidth, 11.0F, hover6 ? hoveredColor : color, color2);
         FontRenderers.sf_medium.drawString(context.method_51448(), !hover6 ? "*******" : proxyPlate.proxy.getP() + (this.listeningId == proxyPlate.id && this.listeningType == ProxyWindow.ListeningType.Password ? blink : ""), (double)(serverX + 2.0F), (double)(this.getY() + 40.0F + this.getScrollOffset() + proxyPlate.offset), (new Color(12434877)).getRGB());
         boolean hover8 = Render2DEngine.isHovered((double)mouseX, (double)mouseY, (double)(this.getX() + this.getWidth() - 15.0F), (double)(this.getY() + 36.0F + this.getScrollOffset() + proxyPlate.offset), 11.0D, 11.0D);
         Render2DEngine.drawRectWithOutline(context.method_51448(), this.getX() + this.getWidth() - 15.0F, this.getY() + 36.0F + this.getScrollOffset() + proxyPlate.offset, 11.0F, 11.0F, hover8 ? hoveredColor : color, color2);
         FontRenderers.icons.drawString(context.method_51448(), "w", (double)(this.getX() + this.getWidth() - 15.0F), (double)(proxyPlate.offset + this.getY() + 40.0F + this.getScrollOffset()), -1);
         FontRenderers.sf_medium_mini.drawString(context.method_51448(), id + ".", (double)(this.getX() + 3.0F), (double)(this.getY() + 41.0F + this.getScrollOffset() + proxyPlate.offset), textColor);
         boolean selected = Managers.PROXY.getActiveProxy() == proxyPlate.proxy;
         boolean hover9 = Render2DEngine.isHovered((double)mouseX, (double)mouseY, (double)(this.getX() + this.getWidth() - 28.0F), (double)(this.getY() + 36.0F + this.getScrollOffset() + proxyPlate.offset), 11.0D, 11.0D);
         Render2DEngine.drawRectWithOutline(context.method_51448(), this.getX() + this.getWidth() - 28.0F, this.getY() + 36.0F + this.getScrollOffset() + proxyPlate.offset, 11.0F, 11.0F, hover9 ? hoveredColor : color, color2);
         FontRenderers.icons.drawString(context.method_51448(), selected ? "i" : "k", (double)(this.getX() + this.getWidth() - 26.0F), (double)(proxyPlate.offset + this.getY() + 41.0F + this.getScrollOffset()), selected ? -1 : Color.GRAY.getRGB());
         boolean hover10 = Render2DEngine.isHovered((double)mouseX, (double)mouseY, (double)(this.getX() + this.getWidth() - 51.5F), (double)(this.getY() + 36.0F + this.getScrollOffset() + proxyPlate.offset), 21.5D, 11.0D);
         String ping = proxyPlate.proxy.getPing() <= 0 ? (proxyPlate.proxy.getPing() == -1 ? String.valueOf(class_124.field_1061) + "shit" : (proxyPlate.proxy.getPing() == -2 ? this.pinging[(int)(System.currentTimeMillis() / 200L % 3L)] : "check")) : proxyPlate.proxy.getPing() + "ms";
         Render2DEngine.drawRectWithOutline(context.method_51448(), this.getX() + this.getWidth() - 51.5F, this.getY() + 36.0F + this.getScrollOffset() + proxyPlate.offset, 21.5F, 11.0F, hover10 ? hoveredColor : color, color2);
         FontRenderers.sf_medium_mini.drawCenteredString(context.method_51448(), ping, (double)(this.getX() + this.getWidth() - 41.0F), (double)(proxyPlate.offset + this.getY() + 40.0F + this.getScrollOffset()), hover10 ? -1 : Color.GRAY.getRGB());
      }
   }

   public void mouseClicked(double mouseX, double mouseY, int button) {
      super.mouseClicked(mouseX, mouseY, button);
      if (Render2DEngine.isHovered(mouseX, mouseY, (double)(this.getX() + this.getWidth() - 90.0F), (double)(this.getY() + 3.0F), 70.0D, 10.0D)) {
         this.listeningId = -2;
         this.search = "";
      }

      if (Render2DEngine.isHovered(mouseX, mouseY, (double)(this.getX() + this.getWidth() - 15.0F), (double)(this.getY() + 3.0F), 10.0D, 10.0D)) {
         Module.mc.method_1507(ClickGUI.getClickGui());
      }

      float nameX = this.getX() + 11.0F;
      float nameWidth = this.getWidth() / 5.5F;
      float posXX = nameX + nameWidth + 2.0F;
      float posXWidth = this.getWidth() / 5.0F;
      float posYX = posXX + posXWidth + 2.0F;
      float posYWidth = this.getWidth() / 12.0F;
      float posZX = posYX + posYWidth + 2.0F;
      float posZWidth = this.getWidth() / 6.5F;
      float serverX = posZX + posZWidth + 2.0F;
      float serverWidth = this.getWidth() / 6.0F;
      boolean hoveringName = Render2DEngine.isHovered(mouseX, mouseY, (double)nameX, (double)(this.getY() + 19.0F), (double)nameWidth, 11.0D);
      boolean hoveringX = Render2DEngine.isHovered(mouseX, mouseY, (double)posXX, (double)(this.getY() + 19.0F), (double)posXWidth, 11.0D);
      boolean hoveringY = Render2DEngine.isHovered(mouseX, mouseY, (double)posYX, (double)(this.getY() + 19.0F), (double)posYWidth, 11.0D);
      boolean hoveringName = Render2DEngine.isHovered(mouseX, mouseY, (double)posZX, (double)(this.getY() + 19.0F), (double)posZWidth, 11.0D);
      boolean hoveringIp = Render2DEngine.isHovered(mouseX, mouseY, (double)serverX, (double)(this.getY() + 19.0F), (double)serverWidth, 11.0D);
      boolean hoveringPort = Render2DEngine.isHovered(mouseX, mouseY, (double)(this.getX() + this.getWidth() - 15.0F), (double)(this.getY() + 19.0F), 11.0D, 11.0D);
      if (hoveringName) {
         this.listeningType = ProxyWindow.ListeningType.Name;
         this.addName = "";
      }

      if (hoveringX) {
         this.listeningType = ProxyWindow.ListeningType.Ip;
         this.addIp = "";
      }

      if (hoveringY) {
         this.listeningType = ProxyWindow.ListeningType.Port;
         this.addPort = "";
      }

      if (hoveringName) {
         this.listeningType = ProxyWindow.ListeningType.Login;
         this.addLogin = "";
      }

      if (hoveringIp) {
         this.listeningType = ProxyWindow.ListeningType.Password;
         this.addPassword = "";
      }

      if (hoveringName || hoveringX || hoveringY || hoveringName || hoveringIp) {
         this.listeningId = -3;
      }

      if (hoveringPort) {
         try {
            Managers.PROXY.addProxy(new ProxyManager.ThProxy(this.addName, this.addIp, Integer.parseInt(this.addPort), this.addLogin, this.addPassword));
         } catch (Exception var27) {
         }

         this.refresh();
      }

      ArrayList<ProxyWindow.ProxyPlate> copy = Lists.newArrayList(this.proxyPlates);
      Iterator var29 = copy.iterator();

      while(true) {
         ProxyWindow.ProxyPlate proxyPlate;
         do {
            if (!var29.hasNext()) {
               return;
            }

            proxyPlate = (ProxyWindow.ProxyPlate)var29.next();
         } while((float)((int)(proxyPlate.offset + this.getY() + 50.0F)) + this.getScrollOffset() > this.getY() + this.getHeight());

         hoveringName = Render2DEngine.isHovered(mouseX, mouseY, (double)nameX, (double)(this.getY() + 36.0F + this.getScrollOffset() + proxyPlate.offset), (double)nameWidth, 11.0D);
         hoveringIp = Render2DEngine.isHovered(mouseX, mouseY, (double)posXX, (double)(this.getY() + 36.0F + this.getScrollOffset() + proxyPlate.offset), (double)posXWidth, 11.0D);
         hoveringPort = Render2DEngine.isHovered(mouseX, mouseY, (double)posYX, (double)(this.getY() + 36.0F + this.getScrollOffset() + proxyPlate.offset), (double)posYWidth, 11.0D);
         boolean hoveringLogin = Render2DEngine.isHovered(mouseX, mouseY, (double)posZX, (double)(this.getY() + 36.0F + this.getScrollOffset() + proxyPlate.offset), (double)posZWidth, 11.0D);
         boolean hoveringPassword = Render2DEngine.isHovered(mouseX, mouseY, (double)serverX, (double)(this.getY() + 36.0F + this.getScrollOffset() + proxyPlate.offset), (double)serverWidth, 11.0D);
         boolean hoveringRemove = Render2DEngine.isHovered(mouseX, mouseY, (double)(this.getX() + this.getWidth() - 15.0F), (double)(this.getY() + 36.0F + this.getScrollOffset() + proxyPlate.offset), 11.0D, 11.0D);
         boolean hoveringSelect = Render2DEngine.isHovered(mouseX, mouseY, (double)(this.getX() + this.getWidth() - 28.0F), (double)(this.getY() + 36.0F + this.getScrollOffset() + proxyPlate.offset), 11.0D, 11.0D);
         boolean hoveringCheck = Render2DEngine.isHovered(mouseX, mouseY, (double)(this.getX() + this.getWidth() - 51.5F), (double)(this.getY() + 36.0F + this.getScrollOffset() + proxyPlate.offset), 21.5D, 11.0D);
         if (hoveringName) {
            this.listeningType = ProxyWindow.ListeningType.Name;
         }

         if (hoveringIp) {
            this.listeningType = ProxyWindow.ListeningType.Ip;
         }

         if (hoveringPort) {
            this.listeningType = ProxyWindow.ListeningType.Port;
         }

         if (hoveringLogin) {
            this.listeningType = ProxyWindow.ListeningType.Login;
         }

         if (hoveringPassword) {
            this.listeningType = ProxyWindow.ListeningType.Password;
         }

         if (hoveringCheck) {
            Managers.PROXY.checkPing(proxyPlate.proxy());
         }

         if (hoveringName || hoveringIp || hoveringPort || hoveringLogin || hoveringPassword) {
            this.listeningId = proxyPlate.id;
         }

         if (hoveringSelect) {
            if (Managers.PROXY.getActiveProxy() == proxyPlate.proxy) {
               Managers.PROXY.setActiveProxy((ProxyManager.ThProxy)null);
            } else {
               Managers.PROXY.setActiveProxy(proxyPlate.proxy);
            }
         }

         if (hoveringRemove) {
            Managers.PROXY.removeProxy(proxyPlate.proxy);
            this.refresh();
         }
      }
   }

   public void keyPressed(int keyCode, int scanCode, int modifiers) {
      if (keyCode != 70 || !class_3675.method_15987(Module.mc.method_22683().method_4490(), 341) && !class_3675.method_15987(Module.mc.method_22683().method_4490(), 345)) {
         if (keyCode == 86 && (class_3675.method_15987(Module.mc.method_22683().method_4490(), 341) || class_3675.method_15987(Module.mc.method_22683().method_4490(), 345))) {
            String paste = GLFW.glfwGetClipboardString(Module.mc.method_22683().method_4490());
            if (paste != null) {
               Iterator var10 = this.proxyPlates.iterator();

               while(var10.hasNext()) {
                  ProxyWindow.ProxyPlate plate = (ProxyWindow.ProxyPlate)var10.next();
                  if (this.listeningId == plate.id) {
                     switch(this.listeningType.ordinal()) {
                     case 0:
                        plate.proxy.setName(paste);
                        return;
                     case 1:
                        plate.proxy.setIp(paste);
                        return;
                     case 2:
                        try {
                           plate.proxy.setPort(Integer.parseInt(paste));
                        } catch (Exception var8) {
                           var8.printStackTrace();
                        }

                        return;
                     case 3:
                        plate.proxy.setL(paste);
                        return;
                     case 4:
                        plate.proxy.setP(paste);
                        return;
                     }
                  }
               }

               if (this.listeningId == -3) {
                  switch(this.listeningType.ordinal()) {
                  case 0:
                     this.addName = paste;
                     break;
                  case 1:
                     this.addIp = paste;
                     break;
                  case 2:
                     this.addPort = paste;
                     break;
                  case 3:
                     this.addLogin = paste;
                     break;
                  case 4:
                     this.addPassword = paste;
                  }
               }

            }
         } else {
            if (this.listeningId != -1) {
               switch(keyCode) {
               case 32:
                  if (this.listeningId == -2) {
                     this.search = this.search + " ";
                  }
                  break;
               case 256:
                  if (this.listeningId == -2) {
                     this.search = "Search";
                  }

                  this.listeningId = -1;
                  this.refresh();
                  break;
               case 257:
                  if (this.listeningId != -2) {
                     this.listeningId = -1;
                  }
                  break;
               case 259:
                  if (this.listeningId == -2) {
                     this.search = SliderElement.removeLastChar(this.search);
                     this.refresh();
                     if (Objects.equals(this.search, "")) {
                        this.listeningId = -1;
                        this.search = "Search";
                     }

                     return;
                  }

                  Iterator var4 = this.proxyPlates.iterator();

                  while(var4.hasNext()) {
                     ProxyWindow.ProxyPlate plate = (ProxyWindow.ProxyPlate)var4.next();
                     if (this.listeningId == plate.id) {
                        switch(this.listeningType.ordinal()) {
                        case 0:
                           plate.proxy.setName(SliderElement.removeLastChar(plate.proxy.getName()));
                           return;
                        case 1:
                           plate.proxy.setIp(SliderElement.removeLastChar(plate.proxy.getIp()));
                           return;
                        case 2:
                           String num = SliderElement.removeLastChar(String.valueOf(plate.proxy.getPort()));
                           if (num.isEmpty()) {
                              num = "0";
                           }

                           plate.proxy.setPort(Integer.parseInt(num));
                           return;
                        case 3:
                           plate.proxy.setL(SliderElement.removeLastChar(plate.proxy.getL()));
                           return;
                        case 4:
                           plate.proxy.setP(SliderElement.removeLastChar(plate.proxy.getP()));
                           return;
                        }
                     }
                  }

                  if (this.listeningId == -3) {
                     switch(this.listeningType.ordinal()) {
                     case 0:
                        this.addName = SliderElement.removeLastChar(this.addName);
                        break;
                     case 1:
                        this.addIp = SliderElement.removeLastChar(this.addIp);
                        break;
                     case 2:
                        this.addPort = SliderElement.removeLastChar(this.addPort);
                        break;
                     case 3:
                        this.addLogin = SliderElement.removeLastChar(this.addLogin);
                        break;
                     case 4:
                        this.addPassword = SliderElement.removeLastChar(this.addPassword);
                     }
                  }
               }
            }

         }
      } else {
         this.listeningId = -2;
      }
   }

   public void charTyped(char key, int keyCode) {
      if (class_3544.method_57175(key) && this.listeningId != -1) {
         if (this.listeningId == -2) {
            this.search = this.search + key;
         }

         Iterator var3 = this.proxyPlates.iterator();

         while(var3.hasNext()) {
            ProxyWindow.ProxyPlate plate = (ProxyWindow.ProxyPlate)var3.next();
            if (this.listeningId == plate.id) {
               try {
                  ProxyManager.ThProxy var10000;
                  String var10001;
                  switch(this.listeningType.ordinal()) {
                  case 0:
                     var10000 = plate.proxy;
                     var10001 = plate.proxy.getName();
                     var10000.setName(var10001 + key);
                     break;
                  case 1:
                     var10000 = plate.proxy;
                     var10001 = plate.proxy.getIp();
                     var10000.setIp(var10001 + key);
                     break;
                  case 2:
                     var10000 = plate.proxy;
                     var10001 = String.valueOf(plate.proxy.getPort());
                     var10000.setPort(Integer.parseInt(var10001 + key));
                     break;
                  case 3:
                     var10000 = plate.proxy;
                     var10001 = plate.proxy.getL();
                     var10000.setL(var10001 + key);
                     break;
                  case 4:
                     var10000 = plate.proxy;
                     var10001 = plate.proxy.getP();
                     var10000.setP(var10001 + key);
                  }
               } catch (Exception var6) {
                  var6.printStackTrace();
               }

               return;
            }
         }

         if (this.listeningId == -3) {
            try {
               switch(this.listeningType.ordinal()) {
               case 0:
                  this.addName = this.addName + key;
                  break;
               case 1:
                  this.addIp = this.addIp + key;
                  break;
               case 2:
                  this.addPort = this.addPort + key;
                  break;
               case 3:
                  this.addLogin = this.addLogin + key;
                  break;
               case 4:
                  this.addPassword = this.addPassword + key;
               }
            } catch (Exception var7) {
               var7.printStackTrace();
            }
         }

         this.refresh();
      }

   }

   private void refresh() {
      this.resetScroll();
      this.proxyPlates.clear();
      int id1 = 0;
      Iterator var2 = Managers.PROXY.getProxies().iterator();

      while(true) {
         ProxyManager.ThProxy p;
         do {
            if (!var2.hasNext()) {
               return;
            }

            p = (ProxyManager.ThProxy)var2.next();
         } while(!this.search.equals("Search") && !this.search.isEmpty() && !p.getName().contains(this.search));

         this.proxyPlates.add(new ProxyWindow.ProxyPlate(id1, (float)(id1 * 20 + 18), p));
         ++id1;
      }
   }

   public int getMinWidth() {
      return 340;
   }

   private static enum ListeningType {
      Name,
      Ip,
      Port,
      Login,
      Password;

      // $FF: synthetic method
      private static ProxyWindow.ListeningType[] $values() {
         return new ProxyWindow.ListeningType[]{Name, Ip, Port, Login, Password};
      }
   }

   private static record ProxyPlate(int id, float offset, ProxyManager.ThProxy proxy) {
      private ProxyPlate(int id, float offset, ProxyManager.ThProxy proxy) {
         this.id = id;
         this.offset = offset;
         this.proxy = proxy;
      }

      public int id() {
         return this.id;
      }

      public float offset() {
         return this.offset;
      }

      public ProxyManager.ThProxy proxy() {
         return this.proxy;
      }
   }
}
