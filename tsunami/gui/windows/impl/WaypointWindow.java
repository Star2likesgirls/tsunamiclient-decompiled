package tsunami.gui.windows.impl;

import com.google.common.collect.Lists;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import net.minecraft.class_332;
import net.minecraft.class_3544;
import net.minecraft.class_3675;
import net.minecraft.class_4587;
import tsunami.core.Managers;
import tsunami.core.manager.world.WayPointManager;
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

public class WaypointWindow extends WindowBase {
   private static WaypointWindow instance;
   private ArrayList<WaypointWindow.WaypointPlate> waypointPlates = new ArrayList();
   private int listeningId = -1;
   private WaypointWindow.ListeningType listeningType;
   private String search = "Search";
   private String addName = "Name";
   private String addX = "X";
   private String addY = "Y";
   private String addZ = "Z";
   private String addServer = "2b2t.org";
   private String addDimension = "overworld";

   public WaypointWindow(float x, float y, float width, float height, Setting<PositionSetting> position) {
      super(x, y, width, height, "Waypoints", position, TextureStorage.waypointIcon);
      this.refresh();
   }

   public static WaypointWindow get(float x, float y, Setting<PositionSetting> position) {
      if (instance == null) {
         instance = new WaypointWindow(x, y, 340.0F, 180.0F, position);
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
      if (this.waypointPlates.isEmpty()) {
         FontRenderers.sf_medium.drawCenteredString(context.method_51448(), ClientSettings.isRu() ? "Тут пока пусто" : "It's empty here yet", (double)(this.getX() + this.getWidth() / 2.0F), (double)(this.getY() + this.getHeight() / 2.0F), (new Color(12434877)).getRGB());
      }

      String blink = System.currentTimeMillis() / 240L % 2L == 0L ? "" : "l";
      float nameX = this.getX() + 11.0F;
      float nameWidth = this.getWidth() / 5.5F;
      float posXX = nameX + nameWidth + 2.0F;
      float posXWidth = this.getWidth() / 8.0F;
      float posYX = posXX + posXWidth + 2.0F;
      float posYWidth = this.getWidth() / 8.0F;
      float posZX = posYX + posYWidth + 2.0F;
      float posZWidth = this.getWidth() / 8.0F;
      float serverX = posZX + posZWidth + 2.0F;
      float serverWidth = this.getWidth() / 6.0F;
      float dimensionX = serverX + serverWidth + 2.0F;
      float dimensionWidth = this.getWidth() / 7.0F;
      boolean hover2 = Render2DEngine.isHovered((double)mouseX, (double)mouseY, (double)nameX, (double)(this.getY() + 19.0F), (double)nameWidth, 11.0D);
      Render2DEngine.drawRectWithOutline(context.method_51448(), nameX, this.getY() + 19.0F, nameWidth, 11.0F, hover2 ? hoveredColor : color, color2);
      FontRenderer var10000 = FontRenderers.sf_medium;
      class_4587 var10001 = context.method_51448();
      String var10002 = this.addName;
      var10000.drawString(var10001, var10002 + (this.listeningId == -3 && this.listeningType == WaypointWindow.ListeningType.Name ? blink : ""), (double)(nameX + 2.0F), (double)(this.getY() + 23.0F), (new Color(12434877)).getRGB());
      boolean hover3 = Render2DEngine.isHovered((double)mouseX, (double)mouseY, (double)posXX, (double)(this.getY() + 19.0F), (double)posXWidth, 11.0D);
      Render2DEngine.drawRectWithOutline(context.method_51448(), posXX, this.getY() + 19.0F, posXWidth, 11.0F, hover3 ? hoveredColor : color, color2);
      var10000 = FontRenderers.sf_medium;
      var10001 = context.method_51448();
      var10002 = this.addX;
      var10000.drawString(var10001, var10002 + (this.listeningId == -3 && this.listeningType == WaypointWindow.ListeningType.X ? blink : ""), (double)(posXX + 2.0F), (double)(this.getY() + 23.0F), textColor);
      boolean hover4 = Render2DEngine.isHovered((double)mouseX, (double)mouseY, (double)posYX, (double)(this.getY() + 19.0F), (double)posYWidth, 11.0D);
      Render2DEngine.drawRectWithOutline(context.method_51448(), posYX, this.getY() + 19.0F, posYWidth, 11.0F, hover4 ? hoveredColor : color, color2);
      var10000 = FontRenderers.sf_medium;
      var10001 = context.method_51448();
      var10002 = this.addY;
      var10000.drawString(var10001, var10002 + (this.listeningId == -3 && this.listeningType == WaypointWindow.ListeningType.Y ? blink : ""), (double)(posYX + 2.0F), (double)(this.getY() + 23.0F), (new Color(12434877)).getRGB());
      boolean hover2 = Render2DEngine.isHovered((double)mouseX, (double)mouseY, (double)posZX, (double)(this.getY() + 19.0F), (double)posZWidth, 11.0D);
      Render2DEngine.drawRectWithOutline(context.method_51448(), posZX, this.getY() + 19.0F, posZWidth, 11.0F, hover2 ? hoveredColor : color, color2);
      var10000 = FontRenderers.sf_medium;
      var10001 = context.method_51448();
      var10002 = this.addZ;
      var10000.drawString(var10001, var10002 + (this.listeningId == -3 && this.listeningType == WaypointWindow.ListeningType.Z ? blink : ""), (double)(posZX + 2.0F), (double)(this.getY() + 23.0F), (new Color(12434877)).getRGB());
      boolean hover3 = Render2DEngine.isHovered((double)mouseX, (double)mouseY, (double)serverX, (double)(this.getY() + 19.0F), (double)serverWidth, 11.0D);
      Render2DEngine.drawRectWithOutline(context.method_51448(), serverX, this.getY() + 19.0F, serverWidth, 11.0F, hover3 ? hoveredColor : color, color2);
      var10000 = FontRenderers.sf_medium;
      var10001 = context.method_51448();
      var10002 = this.addServer;
      var10000.drawString(var10001, var10002 + (this.listeningId == -3 && this.listeningType == WaypointWindow.ListeningType.Server ? blink : ""), (double)(serverX + 2.0F), (double)(this.getY() + 23.0F), (new Color(12434877)).getRGB());
      boolean hover4 = Render2DEngine.isHovered((double)mouseX, (double)mouseY, (double)dimensionX, (double)(this.getY() + 19.0F), (double)dimensionWidth, 11.0D);
      Render2DEngine.drawRectWithOutline(context.method_51448(), dimensionX, this.getY() + 19.0F, dimensionWidth, 11.0F, hover4 ? hoveredColor : color, color2);
      FontRenderers.sf_medium.drawString(context.method_51448(), this.addDimension, (double)(dimensionX + 2.0F), (double)(this.getY() + 23.0F), (new Color(12434877)).getRGB());
      boolean hover5 = Render2DEngine.isHovered((double)mouseX, (double)mouseY, (double)(this.getX() + this.getWidth() - 15.0F), (double)(this.getY() + 19.0F), 11.0D, 11.0D);
      Render2DEngine.drawRectWithOutline(context.method_51448(), this.getX() + this.getWidth() - 15.0F, this.getY() + 19.0F, 11.0F, 11.0F, hover5 ? hoveredColor : color, color2);
      FontRenderers.categories.drawString(context.method_51448(), "+", (double)(this.getX() + this.getWidth() - 12.0F), (double)(this.getY() + 23.0F), -1);
      Render2DEngine.horizontalGradient(context.method_51448(), this.getX() + 2.0F, this.getY() + 33.0F, this.getX() + 2.0F + this.getWidth() / 2.0F - 2.0F, this.getY() + 33.5F, Render2DEngine.injectAlpha(((ColorSetting)HudEditor.textColor.getValue()).getColorObject(), 0), ((ColorSetting)HudEditor.textColor.getValue()).getColorObject());
      Render2DEngine.horizontalGradient(context.method_51448(), this.getX() + 2.0F + this.getWidth() / 2.0F - 2.0F, this.getY() + 33.0F, this.getX() + 2.0F + this.getWidth() - 4.0F, this.getY() + 33.5F, ((ColorSetting)HudEditor.textColor.getValue()).getColorObject(), Render2DEngine.injectAlpha(((ColorSetting)HudEditor.textColor.getValue()).getColorObject(), 0));
      FontRenderers.sf_medium.drawCenteredString(context.method_51448(), "Name", (double)(nameX + nameWidth / 2.0F), (double)(this.getY() + 40.0F), textColor);
      FontRenderers.sf_medium.drawCenteredString(context.method_51448(), "X", (double)(posXX + posXWidth / 2.0F), (double)(this.getY() + 40.0F), textColor);
      FontRenderers.sf_medium.drawCenteredString(context.method_51448(), "Y", (double)(posYX + posYWidth / 2.0F), (double)(this.getY() + 40.0F), textColor);
      FontRenderers.sf_medium.drawCenteredString(context.method_51448(), "Z", (double)(posZX + posZWidth / 2.0F), (double)(this.getY() + 40.0F), textColor);
      FontRenderers.sf_medium.drawCenteredString(context.method_51448(), "Server", (double)(serverX + serverWidth / 2.0F), (double)(this.getY() + 40.0F), textColor);
      FontRenderers.sf_medium.drawCenteredString(context.method_51448(), "Dimension", (double)(dimensionX + dimensionWidth / 2.0F), (double)(this.getY() + 40.0F), textColor);
      Render2DEngine.addWindow(context.method_51448(), this.getX(), this.getY() + 50.0F, this.getX() + this.getWidth(), this.getY() + this.getHeight() - 1.0F, 1.0D);
      int id = 0;
      Iterator var34 = this.waypointPlates.iterator();

      while(true) {
         WaypointWindow.WaypointPlate waypointPlate;
         do {
            do {
               if (!var34.hasNext()) {
                  this.setMaxElementsHeight((float)(this.waypointPlates.size() * 20));
                  Render2DEngine.popWindow();
                  return;
               }

               waypointPlate = (WaypointWindow.WaypointPlate)var34.next();
               ++id;
            } while((float)((int)(waypointPlate.offset + this.getY() + 25.0F)) + this.getScrollOffset() > this.getY() + this.getHeight());
         } while(waypointPlate.offset + this.getScrollOffset() + this.getY() + 10.0F < this.getY());

         hover2 = Render2DEngine.isHovered((double)mouseX, (double)mouseY, (double)nameX, (double)(this.getY() + 36.0F + this.getScrollOffset() + waypointPlate.offset), (double)nameWidth, 11.0D);
         Render2DEngine.drawRectWithOutline(context.method_51448(), nameX, this.getY() + 36.0F + this.getScrollOffset() + waypointPlate.offset, nameWidth, 11.0F, hover2 ? hoveredColor : color, color2);
         var10000 = FontRenderers.sf_medium;
         var10001 = context.method_51448();
         var10002 = waypointPlate.waypoint.getName();
         var10000.drawString(var10001, var10002 + (this.listeningId == waypointPlate.id && this.listeningType == WaypointWindow.ListeningType.Name ? blink : ""), (double)(nameX + 2.0F), (double)(this.getY() + 40.0F + this.getScrollOffset() + waypointPlate.offset), (new Color(12434877)).getRGB());
         hover3 = Render2DEngine.isHovered((double)mouseX, (double)mouseY, (double)posXX, (double)(this.getY() + 36.0F + this.getScrollOffset() + waypointPlate.offset), (double)posXWidth, 11.0D);
         Render2DEngine.drawRectWithOutline(context.method_51448(), posXX, this.getY() + 36.0F + this.getScrollOffset() + waypointPlate.offset, posXWidth, 11.0F, hover3 ? hoveredColor : color, color2);
         var10000 = FontRenderers.sf_medium;
         var10001 = context.method_51448();
         int var32 = waypointPlate.waypoint.getX();
         var10000.drawString(var10001, var32 + (this.listeningId == waypointPlate.id && this.listeningType == WaypointWindow.ListeningType.X ? blink : ""), (double)(posXX + 2.0F), (double)(this.getY() + 40.0F + this.getScrollOffset() + waypointPlate.offset), textColor);
         hover4 = Render2DEngine.isHovered((double)mouseX, (double)mouseY, (double)posYX, (double)(this.getY() + 36.0F + this.getScrollOffset() + waypointPlate.offset), (double)posYWidth, 11.0D);
         Render2DEngine.drawRectWithOutline(context.method_51448(), posYX, this.getY() + 36.0F + this.getScrollOffset() + waypointPlate.offset, posYWidth, 11.0F, hover4 ? hoveredColor : color, color2);
         var10000 = FontRenderers.sf_medium;
         var10001 = context.method_51448();
         var32 = waypointPlate.waypoint.getY();
         var10000.drawString(var10001, var32 + (this.listeningId == waypointPlate.id && this.listeningType == WaypointWindow.ListeningType.Y ? blink : ""), (double)(posYX + 2.0F), (double)(this.getY() + 40.0F + this.getScrollOffset() + waypointPlate.offset), (new Color(12434877)).getRGB());
         hover5 = Render2DEngine.isHovered((double)mouseX, (double)mouseY, (double)posZX, (double)(this.getY() + 36.0F + this.getScrollOffset() + waypointPlate.offset), (double)posZWidth, 11.0D);
         Render2DEngine.drawRectWithOutline(context.method_51448(), posZX, this.getY() + 36.0F + this.getScrollOffset() + waypointPlate.offset, posZWidth, 11.0F, hover5 ? hoveredColor : color, color2);
         var10000 = FontRenderers.sf_medium;
         var10001 = context.method_51448();
         var32 = waypointPlate.waypoint.getZ();
         var10000.drawString(var10001, var32 + (this.listeningId == waypointPlate.id && this.listeningType == WaypointWindow.ListeningType.Z ? blink : ""), (double)(posZX + 2.0F), (double)(this.getY() + 40.0F + this.getScrollOffset() + waypointPlate.offset), (new Color(12434877)).getRGB());
         boolean hover6 = Render2DEngine.isHovered((double)mouseX, (double)mouseY, (double)serverX, (double)(this.getY() + 36.0F + this.getScrollOffset() + waypointPlate.offset), (double)serverWidth, 11.0D);
         Render2DEngine.drawRectWithOutline(context.method_51448(), serverX, this.getY() + 36.0F + this.getScrollOffset() + waypointPlate.offset, serverWidth, 11.0F, hover6 ? hoveredColor : color, color2);
         var10000 = FontRenderers.sf_medium;
         var10001 = context.method_51448();
         var10002 = waypointPlate.waypoint.getServer();
         var10000.drawString(var10001, var10002 + (this.listeningId == waypointPlate.id && this.listeningType == WaypointWindow.ListeningType.Server ? blink : ""), (double)(serverX + 2.0F), (double)(this.getY() + 40.0F + this.getScrollOffset() + waypointPlate.offset), (new Color(12434877)).getRGB());
         boolean hover7 = Render2DEngine.isHovered((double)mouseX, (double)mouseY, (double)dimensionX, (double)(this.getY() + 36.0F + this.getScrollOffset() + waypointPlate.offset), (double)dimensionWidth, 11.0D);
         Render2DEngine.drawRectWithOutline(context.method_51448(), dimensionX, this.getY() + 36.0F + this.getScrollOffset() + waypointPlate.offset, dimensionWidth, 11.0F, hover7 ? hoveredColor : color, color2);
         FontRenderers.sf_medium.drawString(context.method_51448(), waypointPlate.waypoint.getDimension(), (double)(dimensionX + 2.0F), (double)(this.getY() + 40.0F + this.getScrollOffset() + waypointPlate.offset), (new Color(12434877)).getRGB());
         boolean hover8 = Render2DEngine.isHovered((double)mouseX, (double)mouseY, (double)(this.getX() + this.getWidth() - 15.0F), (double)(this.getY() + 36.0F + this.getScrollOffset() + waypointPlate.offset), 11.0D, 11.0D);
         Render2DEngine.drawRectWithOutline(context.method_51448(), this.getX() + this.getWidth() - 15.0F, this.getY() + 36.0F + this.getScrollOffset() + waypointPlate.offset, 11.0F, 11.0F, hover8 ? hoveredColor : color, color2);
         FontRenderers.icons.drawString(context.method_51448(), "w", (double)(this.getX() + this.getWidth() - 15.0F), (double)(waypointPlate.offset + this.getY() + 40.0F + this.getScrollOffset()), -1);
         FontRenderers.sf_medium_mini.drawString(context.method_51448(), id + ".", (double)(this.getX() + 3.0F), (double)(this.getY() + 41.0F + this.getScrollOffset() + waypointPlate.offset), textColor);
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
      float posXWidth = this.getWidth() / 8.0F;
      float posYX = posXX + posXWidth + 2.0F;
      float posYWidth = this.getWidth() / 8.0F;
      float posZX = posYX + posYWidth + 2.0F;
      float posZWidth = this.getWidth() / 8.0F;
      float serverX = posZX + posZWidth + 2.0F;
      float serverWidth = this.getWidth() / 6.0F;
      float dimensionX = serverX + serverWidth + 2.0F;
      float dimensionWidth = this.getWidth() / 7.0F;
      boolean hoveringName = Render2DEngine.isHovered(mouseX, mouseY, (double)nameX, (double)(this.getY() + 19.0F), (double)nameWidth, 11.0D);
      boolean hoveringX = Render2DEngine.isHovered(mouseX, mouseY, (double)posXX, (double)(this.getY() + 19.0F), (double)posXWidth, 11.0D);
      boolean hoveringY = Render2DEngine.isHovered(mouseX, mouseY, (double)posYX, (double)(this.getY() + 19.0F), (double)posYWidth, 11.0D);
      boolean hoveringName = Render2DEngine.isHovered(mouseX, mouseY, (double)posZX, (double)(this.getY() + 19.0F), (double)posZWidth, 11.0D);
      boolean hoveringX = Render2DEngine.isHovered(mouseX, mouseY, (double)serverX, (double)(this.getY() + 19.0F), (double)serverWidth, 11.0D);
      boolean hoveringY = Render2DEngine.isHovered(mouseX, mouseY, (double)dimensionX, (double)(this.getY() + 19.0F), (double)dimensionWidth, 11.0D);
      boolean hoveringZ = Render2DEngine.isHovered(mouseX, mouseY, (double)(this.getX() + this.getWidth() - 15.0F), (double)(this.getY() + 19.0F), 11.0D, 11.0D);
      if (hoveringName) {
         this.listeningType = WaypointWindow.ListeningType.Name;
         this.addName = "";
      }

      if (hoveringX) {
         this.listeningType = WaypointWindow.ListeningType.X;
         this.addX = "";
      }

      if (hoveringY) {
         this.listeningType = WaypointWindow.ListeningType.Y;
         this.addY = "";
      }

      if (hoveringName) {
         this.listeningType = WaypointWindow.ListeningType.Z;
         this.addZ = "";
      }

      if (hoveringX) {
         this.listeningType = WaypointWindow.ListeningType.Server;
         this.addServer = "";
      }

      if (hoveringY) {
         this.addDimension = this.switchType(this.addDimension);
      }

      if (hoveringName || hoveringX || hoveringY || hoveringName || hoveringX || hoveringY) {
         this.listeningId = -3;
      }

      if (hoveringZ) {
         try {
            Managers.WAYPOINT.addWayPoint(new WayPointManager.WayPoint(Integer.parseInt(this.addX), Integer.parseInt(this.addY), Integer.parseInt(this.addZ), this.addName, this.addServer, this.addDimension));
         } catch (Exception var28) {
         }

         this.refresh();
      }

      ArrayList<WaypointWindow.WaypointPlate> copy = Lists.newArrayList(this.waypointPlates);
      Iterator var30 = copy.iterator();

      while(true) {
         WaypointWindow.WaypointPlate waypointPlate;
         do {
            if (!var30.hasNext()) {
               return;
            }

            waypointPlate = (WaypointWindow.WaypointPlate)var30.next();
         } while((float)((int)(waypointPlate.offset + this.getY() + 50.0F)) + this.getScrollOffset() > this.getY() + this.getHeight());

         hoveringName = Render2DEngine.isHovered(mouseX, mouseY, (double)nameX, (double)(this.getY() + 36.0F + this.getScrollOffset() + waypointPlate.offset), (double)nameWidth, 11.0D);
         hoveringX = Render2DEngine.isHovered(mouseX, mouseY, (double)posXX, (double)(this.getY() + 36.0F + this.getScrollOffset() + waypointPlate.offset), (double)posXWidth, 11.0D);
         hoveringY = Render2DEngine.isHovered(mouseX, mouseY, (double)posYX, (double)(this.getY() + 36.0F + this.getScrollOffset() + waypointPlate.offset), (double)posYWidth, 11.0D);
         hoveringZ = Render2DEngine.isHovered(mouseX, mouseY, (double)posZX, (double)(this.getY() + 36.0F + this.getScrollOffset() + waypointPlate.offset), (double)posZWidth, 11.0D);
         boolean hoveringServer = Render2DEngine.isHovered(mouseX, mouseY, (double)serverX, (double)(this.getY() + 36.0F + this.getScrollOffset() + waypointPlate.offset), (double)serverWidth, 11.0D);
         boolean hoveringDimension = Render2DEngine.isHovered(mouseX, mouseY, (double)dimensionX, (double)(this.getY() + 36.0F + this.getScrollOffset() + waypointPlate.offset), (double)dimensionWidth, 11.0D);
         boolean hoveringRemove = Render2DEngine.isHovered(mouseX, mouseY, (double)(this.getX() + this.getWidth() - 15.0F), (double)(this.getY() + 36.0F + this.getScrollOffset() + waypointPlate.offset), 11.0D, 11.0D);
         if (hoveringName) {
            this.listeningType = WaypointWindow.ListeningType.Name;
         }

         if (hoveringX) {
            this.listeningType = WaypointWindow.ListeningType.X;
         }

         if (hoveringY) {
            this.listeningType = WaypointWindow.ListeningType.Y;
         }

         if (hoveringZ) {
            this.listeningType = WaypointWindow.ListeningType.Z;
         }

         if (hoveringServer) {
            this.listeningType = WaypointWindow.ListeningType.Server;
         }

         if (hoveringDimension) {
            waypointPlate.waypoint.setDimension(this.switchType(waypointPlate.waypoint.getDimension()));
         }

         if (hoveringName || hoveringX || hoveringY || hoveringZ || hoveringServer || hoveringDimension) {
            this.listeningId = waypointPlate.id;
         }

         if (hoveringRemove) {
            Managers.WAYPOINT.removeWayPoint(waypointPlate.waypoint);
            this.refresh();
         }
      }
   }

   public void keyPressed(int keyCode, int scanCode, int modifiers) {
      if (keyCode != 70 || !class_3675.method_15987(Module.mc.method_22683().method_4490(), 341) && !class_3675.method_15987(Module.mc.method_22683().method_4490(), 345)) {
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

               Iterator var4 = this.waypointPlates.iterator();

               while(var4.hasNext()) {
                  WaypointWindow.WaypointPlate plate = (WaypointWindow.WaypointPlate)var4.next();
                  if (this.listeningId == plate.id) {
                     String num;
                     switch(this.listeningType.ordinal()) {
                     case 0:
                        plate.waypoint.setName(SliderElement.removeLastChar(plate.waypoint.getName()));
                        return;
                     case 1:
                        num = SliderElement.removeLastChar(String.valueOf(plate.waypoint.getX()));
                        if (num.isEmpty()) {
                           num = "0";
                        }

                        plate.waypoint.setX(Integer.parseInt(num));
                        return;
                     case 2:
                        num = SliderElement.removeLastChar(String.valueOf(plate.waypoint.getY()));
                        if (num.isEmpty()) {
                           num = "0";
                        }

                        plate.waypoint.setY(Integer.parseInt(num));
                        return;
                     case 3:
                        num = SliderElement.removeLastChar(String.valueOf(plate.waypoint.getZ()));
                        if (num.isEmpty()) {
                           num = "0";
                        }

                        plate.waypoint.setZ(Integer.parseInt(num));
                        return;
                     case 4:
                        plate.waypoint.setServer(SliderElement.removeLastChar(plate.waypoint.getServer()));
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
                     this.addX = SliderElement.removeLastChar(this.addX);
                     break;
                  case 2:
                     this.addY = SliderElement.removeLastChar(this.addY);
                     break;
                  case 3:
                     this.addZ = SliderElement.removeLastChar(this.addZ);
                     break;
                  case 4:
                     this.addServer = SliderElement.removeLastChar(this.addServer);
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

         Iterator var3 = this.waypointPlates.iterator();

         while(var3.hasNext()) {
            WaypointWindow.WaypointPlate plate = (WaypointWindow.WaypointPlate)var3.next();
            if (this.listeningId == plate.id) {
               try {
                  WayPointManager.WayPoint var10000;
                  String var10001;
                  switch(this.listeningType.ordinal()) {
                  case 0:
                     var10000 = plate.waypoint;
                     var10001 = plate.waypoint.getName();
                     var10000.setName(var10001 + key);
                     break;
                  case 1:
                     var10000 = plate.waypoint;
                     var10001 = String.valueOf(plate.waypoint.getX());
                     var10000.setX(Integer.parseInt(var10001 + key));
                     break;
                  case 2:
                     var10000 = plate.waypoint;
                     var10001 = String.valueOf(plate.waypoint.getY());
                     var10000.setY(Integer.parseInt(var10001 + key));
                     break;
                  case 3:
                     var10000 = plate.waypoint;
                     var10001 = String.valueOf(plate.waypoint.getZ());
                     var10000.setZ(Integer.parseInt(var10001 + key));
                     break;
                  case 4:
                     var10000 = plate.waypoint;
                     var10001 = plate.waypoint.getServer();
                     var10000.setServer(var10001 + key);
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
                  this.addX = this.addX + key;
                  break;
               case 2:
                  this.addY = this.addY + key;
                  break;
               case 3:
                  this.addZ = this.addZ + key;
                  break;
               case 4:
                  this.addServer = this.addServer + key;
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
      this.waypointPlates.clear();
      int id1 = 0;
      Iterator var2 = Managers.WAYPOINT.getWayPoints().iterator();

      while(true) {
         WayPointManager.WayPoint w;
         do {
            if (!var2.hasNext()) {
               this.addServer = !Module.mc.method_1542() && !Module.fullNullCheck() ? Module.mc.method_1562().method_45734().field_3761 : "SinglePlayer";
               this.addDimension = Module.fullNullCheck() ? "overworld" : Module.mc.field_1687.method_27983().method_29177().method_12832();
               return;
            }

            w = (WayPointManager.WayPoint)var2.next();
         } while(!this.search.equals("Search") && !this.search.isEmpty() && !w.getName().contains(this.search) && !w.getServer().contains(this.search));

         this.waypointPlates.add(new WaypointWindow.WaypointPlate(id1, (float)(id1 * 20 + 18), w));
         ++id1;
      }
   }

   private String switchType(String t) {
      byte var3 = -1;
      switch(t.hashCode()) {
      case -1350117363:
         if (t.equals("the_end")) {
            var3 = 0;
         }
         break;
      case -745159874:
         if (t.equals("overworld")) {
            var3 = 1;
         }
         break;
      case 1272296422:
         if (t.equals("the_nether")) {
            var3 = 2;
         }
      }

      switch(var3) {
      case 0:
         return "overworld";
      case 1:
         return "the_nether";
      case 2:
         return "the_end";
      default:
         return "overworld";
      }
   }

   public int getMinWidth() {
      return 340;
   }

   private static enum ListeningType {
      Name,
      X,
      Y,
      Z,
      Server;

      // $FF: synthetic method
      private static WaypointWindow.ListeningType[] $values() {
         return new WaypointWindow.ListeningType[]{Name, X, Y, Z, Server};
      }
   }

   private static record WaypointPlate(int id, float offset, WayPointManager.WayPoint waypoint) {
      private WaypointPlate(int id, float offset, WayPointManager.WayPoint waypoint) {
         this.id = id;
         this.offset = offset;
         this.waypoint = waypoint;
      }

      public int id() {
         return this.id;
      }

      public float offset() {
         return this.offset;
      }

      public WayPointManager.WayPoint waypoint() {
         return this.waypoint;
      }
   }
}
