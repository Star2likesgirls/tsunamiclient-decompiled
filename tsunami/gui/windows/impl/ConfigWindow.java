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

public class ConfigWindow extends WindowBase {
   private static ConfigWindow instance;
   private ArrayList<ConfigWindow.ConfigPlate> configPlates = new ArrayList();
   private int listeningId = -1;
   private String search = "Search";
   private String addName = "Name";

   public ConfigWindow(float x, float y, float width, float height, Setting<PositionSetting> position) {
      super(x, y, width, height, "Config", position, TextureStorage.configIcon);
      this.refresh();
   }

   public static ConfigWindow get(float x, float y, Setting<PositionSetting> position) {
      if (instance == null) {
         instance = new ConfigWindow(x, y, 200.0F, 180.0F, position);
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
      if (this.configPlates.isEmpty()) {
         FontRenderers.sf_medium.drawCenteredString(context.method_51448(), ClientSettings.isRu() ? "Тут пока пусто" : "It's empty here yet", (double)(this.getX() + this.getWidth() / 2.0F), (double)(this.getY() + this.getHeight() / 2.0F), (new Color(12434877)).getRGB());
      }

      String blink = System.currentTimeMillis() / 240L % 2L == 0L ? "" : "   <<<<";
      String blink2 = System.currentTimeMillis() / 240L % 2L == 0L ? "" : "l";
      boolean hover2 = Render2DEngine.isHovered((double)mouseX, (double)mouseY, (double)(this.getX() + 11.0F), (double)(this.getY() + 19.0F), (double)(this.getWidth() - 28.0F), 11.0D);
      Render2DEngine.drawRectWithOutline(context.method_51448(), this.getX() + 11.0F, this.getY() + 19.0F, this.getWidth() - 28.0F, 11.0F, hover2 ? hoveredColor : color, color2);
      FontRenderer var10000 = FontRenderers.sf_medium;
      class_4587 var10001 = context.method_51448();
      String var10002 = this.addName;
      var10000.drawString(var10001, var10002 + (this.listeningId == -3 ? blink2 : ""), (double)(this.getX() + 13.0F), (double)(this.getY() + 23.0F), (new Color(12434877)).getRGB());
      boolean hover5 = Render2DEngine.isHovered((double)mouseX, (double)mouseY, (double)(this.getX() + this.getWidth() - 15.0F), (double)(this.getY() + 19.0F), 11.0D, 11.0D);
      Render2DEngine.drawRectWithOutline(context.method_51448(), this.getX() + this.getWidth() - 15.0F, this.getY() + 19.0F, 11.0F, 11.0F, hover5 ? hoveredColor : color, color2);
      FontRenderers.categories.drawString(context.method_51448(), "+", (double)(this.getX() + this.getWidth() - 12.0F), (double)(this.getY() + 23.0F), -1);
      Render2DEngine.horizontalGradient(context.method_51448(), this.getX() + 2.0F, this.getY() + 33.0F, this.getX() + 2.0F + this.getWidth() / 2.0F - 2.0F, this.getY() + 33.5F, Render2DEngine.injectAlpha(((ColorSetting)HudEditor.textColor.getValue()).getColorObject(), 0), ((ColorSetting)HudEditor.textColor.getValue()).getColorObject());
      Render2DEngine.horizontalGradient(context.method_51448(), this.getX() + 2.0F + this.getWidth() / 2.0F - 2.0F, this.getY() + 33.0F, this.getX() + 2.0F + this.getWidth() - 4.0F, this.getY() + 33.5F, ((ColorSetting)HudEditor.textColor.getValue()).getColorObject(), Render2DEngine.injectAlpha(((ColorSetting)HudEditor.textColor.getValue()).getColorObject(), 0));
      Render2DEngine.addWindow(context.method_51448(), this.getX(), this.getY() + 38.0F, this.getX() + this.getWidth(), this.getY() + this.getHeight() - 1.0F, 1.0D);
      int id = 0;
      Iterator var17 = this.configPlates.iterator();

      while(var17.hasNext()) {
         ConfigWindow.ConfigPlate configPlate = (ConfigWindow.ConfigPlate)var17.next();
         ++id;
         if (!((float)((int)(configPlate.offset + this.getY() + 50.0F)) + this.getScrollOffset() > this.getY() + this.getHeight()) && !(configPlate.offset + this.getScrollOffset() + this.getY() < this.getY())) {
            Render2DEngine.drawRectWithOutline(context.method_51448(), this.getX() + 11.0F, configPlate.offset + this.getY() + 36.0F + this.getScrollOffset(), this.getWidth() - 52.0F, 11.0F, color, color2);
            var10000 = FontRenderers.sf_medium;
            var10001 = context.method_51448();
            var10002 = configPlate.name();
            String var10003 = configPlate.name();
            var10000.drawString(var10001, var10002 + (Objects.equals(var10003 + ".th", Managers.CONFIG.currentConfig.getName()) ? blink : ""), (double)(this.getX() + 13.0F), (double)(configPlate.offset + this.getY() + 40.0F + this.getScrollOffset()), textColor);
            boolean hover3 = Render2DEngine.isHovered((double)mouseX, (double)mouseY, (double)(this.getX() + this.getWidth() - 39.0F), (double)(configPlate.offset + this.getY() + 36.0F + this.getScrollOffset()), 22.0D, 11.0D);
            Render2DEngine.drawRectWithOutline(context.method_51448(), this.getX() + this.getWidth() - 39.0F, configPlate.offset + this.getY() + 36.0F + this.getScrollOffset(), 22.0F, 11.0F, hover3 ? hoveredColor : color, color2);
            FontRenderers.sf_medium.drawString(context.method_51448(), "Load", (double)(this.getX() + this.getWidth() - 37.0F), (double)(configPlate.offset + this.getY() + 40.0F + this.getScrollOffset()), (new Color(12434877)).getRGB());
            boolean hover5 = Render2DEngine.isHovered((double)mouseX, (double)mouseY, (double)(this.getX() + this.getWidth() - 15.0F), (double)(configPlate.offset + this.getY() + 36.0F + this.getScrollOffset()), 11.0D, 11.0D);
            Render2DEngine.drawRectWithOutline(context.method_51448(), this.getX() + this.getWidth() - 15.0F, configPlate.offset + this.getY() + 36.0F + this.getScrollOffset(), 11.0F, 11.0F, hover5 ? hoveredColor : color, color2);
            FontRenderers.icons.drawString(context.method_51448(), "w", (double)(this.getX() + this.getWidth() - 15.0F), (double)(configPlate.offset + this.getY() + 40.0F + this.getScrollOffset()), -1);
            FontRenderers.sf_medium_mini.drawString(context.method_51448(), id + ".", (double)(this.getX() + 3.0F), (double)(configPlate.offset + this.getY() + 41.0F + this.getScrollOffset()), textColor);
         }
      }

      this.setMaxElementsHeight((float)(this.configPlates.size() * 20));
      Render2DEngine.popWindow();
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

      boolean hoveringName = Render2DEngine.isHovered(mouseX, mouseY, (double)(this.getX() + 11.0F), (double)(this.getY() + 19.0F), (double)(this.getWidth() - 28.0F), 11.0D);
      boolean hoveringAdd = Render2DEngine.isHovered(mouseX, mouseY, (double)(this.getX() + this.getWidth() - 15.0F), (double)(this.getY() + 19.0F), 11.0D, 11.0D);
      if (hoveringName) {
         this.addName = "";
         this.listeningId = -3;
      }

      if (hoveringAdd && !this.addName.isEmpty()) {
         Managers.CONFIG.save(this.addName);
         this.addName = "";
         this.refresh();
      }

      ArrayList<ConfigWindow.ConfigPlate> copy = Lists.newArrayList(this.configPlates);
      Iterator var9 = copy.iterator();

      while(var9.hasNext()) {
         ConfigWindow.ConfigPlate configPlate = (ConfigWindow.ConfigPlate)var9.next();
         if (!((float)((int)(configPlate.offset + this.getY() + 50.0F)) + this.getScrollOffset() > this.getY() + this.getHeight())) {
            boolean hoveringRemove = Render2DEngine.isHovered(mouseX, mouseY, (double)(this.getX() + this.getWidth() - 15.0F), (double)(configPlate.offset + this.getY() + 36.0F + this.getScrollOffset()), 11.0D, 11.0D);
            boolean hoverLoad = Render2DEngine.isHovered(mouseX, mouseY, (double)(this.getX() + this.getWidth() - 39.0F), (double)(configPlate.offset + this.getY() + 36.0F + this.getScrollOffset()), 22.0D, 11.0D);
            if (hoverLoad) {
               Managers.CONFIG.load(configPlate.name());
            }

            if (hoveringRemove) {
               Managers.CONFIG.delete(configPlate.name());
               this.refresh();
            }
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

               if (this.listeningId == -3) {
                  this.addName = SliderElement.removeLastChar(this.addName);
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

         if (this.listeningId == -3) {
            this.addName = this.addName + key;
         }

         this.refresh();
      }

   }

   private void refresh() {
      this.resetScroll();
      this.configPlates.clear();
      int id1 = 0;
      Iterator var2 = Managers.CONFIG.getConfigList().iterator();

      while(true) {
         String s;
         do {
            if (!var2.hasNext()) {
               return;
            }

            s = (String)var2.next();
         } while(!this.search.equals("Search") && !this.search.isEmpty() && !s.contains(this.search));

         this.configPlates.add(new ConfigWindow.ConfigPlate(id1, (float)(id1 * 20 + 8), s));
         ++id1;
      }
   }

   private static record ConfigPlate(int id, float offset, String name) {
      private ConfigPlate(int id, float offset, String name) {
         this.id = id;
         this.offset = offset;
         this.name = name;
      }

      public int id() {
         return this.id;
      }

      public float offset() {
         return this.offset;
      }

      public String name() {
         return this.name;
      }
   }
}
