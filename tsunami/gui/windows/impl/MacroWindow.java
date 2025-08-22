package tsunami.gui.windows.impl;

import com.google.common.collect.Lists;
import java.awt.Color;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import net.minecraft.class_332;
import net.minecraft.class_3544;
import net.minecraft.class_3675;
import net.minecraft.class_4587;
import org.lwjgl.glfw.GLFW;
import tsunami.core.Managers;
import tsunami.core.manager.client.MacroManager;
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

public class MacroWindow extends WindowBase {
   private static MacroWindow instance;
   private ArrayList<MacroWindow.MacroPlate> macroPlates = new ArrayList();
   private int listeningId = -1;
   private int addBindKeyCode = -1;
   private MacroWindow.ListeningType listeningType;
   private String search = "Search";
   private String addName = "Name";
   private String addBind = "Bind";
   private String addText = "Text";

   public MacroWindow(float x, float y, float width, float height, Setting<PositionSetting> position) {
      super(x, y, width, height, "Macros", position, TextureStorage.macrosIcon);
      this.refresh();
   }

   public static MacroWindow get(float x, float y, Setting<PositionSetting> position) {
      if (instance == null) {
         instance = new MacroWindow(x, y, 200.0F, 180.0F, position);
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
      if (this.macroPlates.isEmpty()) {
         FontRenderers.sf_medium.drawCenteredString(context.method_51448(), ClientSettings.isRu() ? "Тут пока пусто" : "It's empty here yet", (double)(this.getX() + this.getWidth() / 2.0F), (double)(this.getY() + this.getHeight() / 2.0F), (new Color(12434877)).getRGB());
      }

      String blink = System.currentTimeMillis() / 240L % 2L == 0L ? "" : "l";
      float nameX = this.getX() + 11.0F;
      float nameWidth = this.getWidth() / 4.5F;
      float bindX = nameX + nameWidth + 2.0F;
      float bindWidth = 27.0F;
      float textX = bindX + bindWidth + 2.0F;
      float textWidth = this.getWidth() - (textX - this.getX()) - 17.0F;
      boolean hover2 = Render2DEngine.isHovered((double)mouseX, (double)mouseY, (double)nameX, (double)(this.getY() + 19.0F), (double)nameWidth, 11.0D);
      Render2DEngine.drawRectWithOutline(context.method_51448(), nameX, this.getY() + 19.0F, nameWidth, 11.0F, hover2 ? hoveredColor : color, color2);
      FontRenderer var10000 = FontRenderers.sf_medium;
      class_4587 var10001 = context.method_51448();
      String var10002 = this.addName;
      var10000.drawString(var10001, var10002 + (this.listeningId == -3 && this.listeningType == MacroWindow.ListeningType.Name ? blink : ""), (double)(nameX + 2.0F), (double)(this.getY() + 23.0F), (new Color(12434877)).getRGB());
      boolean hover3 = Render2DEngine.isHovered((double)mouseX, (double)mouseY, (double)bindX, (double)(this.getY() + 19.0F), (double)bindWidth, 11.0D);
      Render2DEngine.drawRectWithOutline(context.method_51448(), bindX, this.getY() + 19.0F, bindWidth, 11.0F, hover3 ? hoveredColor : color, color2);
      var10000 = FontRenderers.sf_medium;
      var10001 = context.method_51448();
      var10002 = this.getSbind(this.addBind);
      var10000.drawCenteredString(var10001, var10002 + (this.listeningId == -3 && this.listeningType == MacroWindow.ListeningType.Bind ? blink : ""), (double)(bindX + 13.5F), (double)(this.getY() + 23.0F), textColor);
      boolean hover4 = Render2DEngine.isHovered((double)mouseX, (double)mouseY, (double)textX, (double)(this.getY() + 19.0F), (double)textWidth, 11.0D);
      Render2DEngine.drawRectWithOutline(context.method_51448(), textX, this.getY() + 19.0F, textWidth, 11.0F, hover4 ? hoveredColor : color, color2);
      var10000 = FontRenderers.sf_medium;
      var10001 = context.method_51448();
      var10002 = this.addText;
      var10000.drawString(var10001, var10002 + (this.listeningId == -3 && this.listeningType == MacroWindow.ListeningType.Text ? blink : ""), (double)(textX + 2.0F), (double)(this.getY() + 23.0F), (new Color(12434877)).getRGB());
      boolean hover2 = Render2DEngine.isHovered((double)mouseX, (double)mouseY, (double)(this.getX() + this.getWidth() - 15.0F), (double)(this.getY() + 19.0F), 11.0D, 11.0D);
      Render2DEngine.drawRectWithOutline(context.method_51448(), this.getX() + this.getWidth() - 15.0F, this.getY() + 19.0F, 11.0F, 11.0F, hover2 ? hoveredColor : color, color2);
      FontRenderers.categories.drawString(context.method_51448(), "+", (double)(this.getX() + this.getWidth() - 12.0F), (double)(this.getY() + 23.0F), -1);
      Render2DEngine.horizontalGradient(context.method_51448(), this.getX() + 2.0F, this.getY() + 33.0F, this.getX() + 2.0F + this.getWidth() / 2.0F - 2.0F, this.getY() + 33.5F, Render2DEngine.injectAlpha(((ColorSetting)HudEditor.textColor.getValue()).getColorObject(), 0), ((ColorSetting)HudEditor.textColor.getValue()).getColorObject());
      Render2DEngine.horizontalGradient(context.method_51448(), this.getX() + 2.0F + this.getWidth() / 2.0F - 2.0F, this.getY() + 33.0F, this.getX() + 2.0F + this.getWidth() - 4.0F, this.getY() + 33.5F, ((ColorSetting)HudEditor.textColor.getValue()).getColorObject(), Render2DEngine.injectAlpha(((ColorSetting)HudEditor.textColor.getValue()).getColorObject(), 0));
      FontRenderers.sf_medium.drawCenteredString(context.method_51448(), "Name", (double)(nameX + nameWidth / 2.0F), (double)(this.getY() + 40.0F), textColor);
      FontRenderers.sf_medium.drawCenteredString(context.method_51448(), "l   Bind   l", (double)(bindX + bindWidth / 2.0F), (double)(this.getY() + 40.0F), textColor);
      FontRenderers.sf_medium.drawCenteredString(context.method_51448(), "Text", (double)(textX + textWidth / 2.0F), (double)(this.getY() + 40.0F), textColor);
      Render2DEngine.addWindow(context.method_51448(), this.getX(), this.getY() + 50.0F, this.getX() + this.getWidth(), this.getY() + this.getHeight() - 1.0F, 1.0D);
      int id = 0;
      Iterator var24 = this.macroPlates.iterator();

      while(true) {
         MacroWindow.MacroPlate macroPlate;
         do {
            do {
               if (!var24.hasNext()) {
                  this.setMaxElementsHeight((float)(this.macroPlates.size() * 20));
                  Render2DEngine.popWindow();
                  return;
               }

               macroPlate = (MacroWindow.MacroPlate)var24.next();
               ++id;
            } while((float)((int)(macroPlate.offset + this.getY() + 25.0F)) + this.getScrollOffset() > this.getY() + this.getHeight());
         } while(macroPlate.offset + this.getScrollOffset() + this.getY() + 10.0F < this.getY());

         hover2 = Render2DEngine.isHovered((double)mouseX, (double)mouseY, (double)nameX, (double)(macroPlate.offset + this.getY() + 36.0F + this.getScrollOffset()), (double)nameWidth, 11.0D);
         Render2DEngine.drawRectWithOutline(context.method_51448(), nameX, macroPlate.offset + this.getY() + 36.0F + this.getScrollOffset(), nameWidth, 11.0F, hover2 ? hoveredColor : color, color2);
         var10000 = FontRenderers.sf_medium;
         var10001 = context.method_51448();
         var10002 = macroPlate.macro().getName();
         var10000.drawString(var10001, var10002 + (macroPlate.id() == this.listeningId && this.listeningType == MacroWindow.ListeningType.Name ? blink : ""), (double)(nameX + 2.0F), (double)(macroPlate.offset + this.getY() + 40.0F + this.getScrollOffset()), textColor);
         boolean hover3 = Render2DEngine.isHovered((double)mouseX, (double)mouseY, (double)bindX, (double)(macroPlate.offset + this.getY() + 36.0F + this.getScrollOffset()), (double)bindWidth, 11.0D);
         Render2DEngine.drawRectWithOutline(context.method_51448(), bindX, macroPlate.offset + this.getY() + 36.0F + this.getScrollOffset(), bindWidth, 11.0F, hover3 ? hoveredColor : color, color2);
         var10000 = FontRenderers.sf_medium;
         var10001 = context.method_51448();
         var10002 = this.getSbind(this.toString(macroPlate.macro().getBind()));
         var10000.drawCenteredString(var10001, var10002 + (macroPlate.id() == this.listeningId && this.listeningType == MacroWindow.ListeningType.Bind ? blink : ""), (double)(bindX + 13.5F), (double)(macroPlate.offset + this.getY() + 40.0F + this.getScrollOffset()), textColor);
         boolean hover4 = Render2DEngine.isHovered((double)mouseX, (double)mouseY, (double)textX, (double)(macroPlate.offset + this.getY() + 36.0F + this.getScrollOffset()), (double)textWidth, 11.0D);
         Render2DEngine.drawRectWithOutline(context.method_51448(), textX, macroPlate.offset + this.getY() + 36.0F + this.getScrollOffset(), textWidth, 11.0F, hover4 ? hoveredColor : color, color2);
         var10000 = FontRenderers.sf_medium;
         var10001 = context.method_51448();
         var10002 = macroPlate.macro().getText();
         var10000.drawString(var10001, var10002 + (macroPlate.id() == this.listeningId && this.listeningType == MacroWindow.ListeningType.Text ? blink : ""), (double)(textX + 2.0F), (double)(macroPlate.offset + this.getY() + 40.0F + this.getScrollOffset()), textColor);
         boolean hover5 = Render2DEngine.isHovered((double)mouseX, (double)mouseY, (double)(this.getX() + this.getWidth() - 15.0F), (double)(macroPlate.offset + this.getY() + 36.0F + this.getScrollOffset()), 11.0D, 11.0D);
         Render2DEngine.drawRectWithOutline(context.method_51448(), this.getX() + this.getWidth() - 15.0F, macroPlate.offset + this.getY() + 36.0F + this.getScrollOffset(), 11.0F, 11.0F, hover5 ? hoveredColor : color, color2);
         FontRenderers.icons.drawString(context.method_51448(), "w", (double)(this.getX() + this.getWidth() - 15.0F), (double)(macroPlate.offset + this.getY() + 40.0F + this.getScrollOffset()), -1);
         FontRenderers.sf_medium_mini.drawString(context.method_51448(), id + ".", (double)(this.getX() + 3.0F), (double)(macroPlate.offset + this.getY() + 41.0F + this.getScrollOffset()), textColor);
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
      float nameWidth = this.getWidth() / 4.5F;
      float bindX = nameX + nameWidth + 2.0F;
      float bindWidth = 27.0F;
      float textX = bindX + bindWidth + 2.0F;
      float textWidth = this.getWidth() - (textX - this.getX()) - 17.0F;
      boolean hoveringName = Render2DEngine.isHovered(mouseX, mouseY, (double)nameX, (double)(this.getY() + 19.0F), (double)nameWidth, 11.0D);
      boolean hoveringBind = Render2DEngine.isHovered(mouseX, mouseY, (double)bindX, (double)(this.getY() + 19.0F), (double)bindWidth, 11.0D);
      boolean hoveringText = Render2DEngine.isHovered(mouseX, mouseY, (double)textX, (double)(this.getY() + 19.0F), (double)textWidth, 11.0D);
      boolean hoveringAdd = Render2DEngine.isHovered(mouseX, mouseY, (double)(this.getX() + this.getWidth() - 15.0F), (double)(this.getY() + 19.0F), 11.0D, 11.0D);
      if (hoveringName) {
         this.listeningType = MacroWindow.ListeningType.Name;
         this.addName = "";
      }

      if (hoveringBind) {
         this.listeningType = MacroWindow.ListeningType.Bind;
         this.addBind = "";
         this.addBindKeyCode = -1;
      }

      if (hoveringText) {
         this.listeningType = MacroWindow.ListeningType.Text;
         this.addText = "";
      }

      if (hoveringText || hoveringName || hoveringBind) {
         this.listeningId = -3;
      }

      if (hoveringAdd && this.addBindKeyCode != -1) {
         MacroManager.addMacro(new MacroManager.Macro(this.addName, this.addText, this.addBindKeyCode));
         this.refresh();
      }

      ArrayList<MacroWindow.MacroPlate> copy = Lists.newArrayList(this.macroPlates);
      Iterator var17 = copy.iterator();

      while(true) {
         MacroWindow.MacroPlate macroPlate;
         do {
            if (!var17.hasNext()) {
               return;
            }

            macroPlate = (MacroWindow.MacroPlate)var17.next();
         } while((float)((int)(macroPlate.offset + this.getY() + 50.0F)) + this.getScrollOffset() > this.getY() + this.getHeight());

         boolean hoveringName1 = Render2DEngine.isHovered(mouseX, mouseY, (double)nameX, (double)(macroPlate.offset + this.getY() + 36.0F + this.getScrollOffset()), (double)nameWidth, 11.0D);
         boolean hoveringBind1 = Render2DEngine.isHovered(mouseX, mouseY, (double)bindX, (double)(macroPlate.offset + this.getY() + 36.0F + this.getScrollOffset()), (double)bindWidth, 11.0D);
         boolean hoveringText1 = Render2DEngine.isHovered(mouseX, mouseY, (double)textX, (double)(macroPlate.offset + this.getY() + 36.0F + this.getScrollOffset()), (double)textWidth, 11.0D);
         boolean hoveringRemove = Render2DEngine.isHovered(mouseX, mouseY, (double)(this.getX() + this.getWidth() - 15.0F), (double)(macroPlate.offset + this.getY() + 36.0F + this.getScrollOffset()), 11.0D, 11.0D);
         if (hoveringName1) {
            this.listeningType = MacroWindow.ListeningType.Name;
         }

         if (hoveringBind1) {
            this.listeningType = MacroWindow.ListeningType.Bind;
         }

         if (hoveringText1) {
            this.listeningType = MacroWindow.ListeningType.Text;
         }

         if (hoveringName1 || hoveringBind1 || hoveringText1) {
            this.listeningId = macroPlate.id;
         }

         if (hoveringRemove) {
            Managers.MACRO.removeMacro(macroPlate.macro());
            this.refresh();
         }
      }
   }

   public void keyPressed(int keyCode, int scanCode, int modifiers) {
      if (keyCode != 70 || !class_3675.method_15987(Module.mc.method_22683().method_4490(), 341) && !class_3675.method_15987(Module.mc.method_22683().method_4490(), 345)) {
         if (keyCode == 86 && (class_3675.method_15987(Module.mc.method_22683().method_4490(), 341) || class_3675.method_15987(Module.mc.method_22683().method_4490(), 345))) {
            String paste = GLFW.glfwGetClipboardString(Module.mc.method_22683().method_4490());
            if (paste != null) {
               Iterator var8 = this.macroPlates.iterator();

               while(var8.hasNext()) {
                  MacroWindow.MacroPlate plate = (MacroWindow.MacroPlate)var8.next();
                  if (this.listeningId == plate.id) {
                     switch(this.listeningType.ordinal()) {
                     case 0:
                        plate.macro.setName(paste);
                        return;
                     case 1:
                        plate.macro.setText(paste);
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
                     this.addText = paste;
                  }
               }

            }
         } else {
            if (this.listeningId != -1) {
               Iterator var4;
               MacroWindow.MacroPlate plate;
               switch(keyCode) {
               case 32:
                  if (this.listeningId == -2) {
                     this.search = this.search + " ";
                     return;
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

                  var4 = this.macroPlates.iterator();

                  while(var4.hasNext()) {
                     plate = (MacroWindow.MacroPlate)var4.next();
                     if (this.listeningId == plate.id) {
                        switch(this.listeningType.ordinal()) {
                        case 0:
                           plate.macro.setName(SliderElement.removeLastChar(plate.macro.getName()));
                           return;
                        case 1:
                           plate.macro.setText(SliderElement.removeLastChar(plate.macro.getText()));
                           return;
                        case 2:
                           return;
                        }
                     }
                  }

                  if (this.listeningId == -3) {
                     switch(this.listeningType.ordinal()) {
                     case 0:
                        this.addName = SliderElement.removeLastChar(this.addName);
                        return;
                     case 1:
                        this.addText = SliderElement.removeLastChar(this.addText);
                        return;
                     case 2:
                        return;
                     }
                  }
               }

               var4 = this.macroPlates.iterator();

               while(var4.hasNext()) {
                  plate = (MacroWindow.MacroPlate)var4.next();
                  if (keyCode != -1 && this.listeningId == plate.id && this.listeningType == MacroWindow.ListeningType.Bind) {
                     plate.macro.setBind(keyCode);
                     this.listeningId = -1;
                  }
               }

               if (this.listeningId == -3 && keyCode != -1 && this.listeningType == MacroWindow.ListeningType.Bind) {
                  this.addBind = this.toString(keyCode);
                  this.addBindKeyCode = keyCode;
                  this.listeningId = -1;
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

         Iterator var3 = this.macroPlates.iterator();

         while(var3.hasNext()) {
            MacroWindow.MacroPlate plate = (MacroWindow.MacroPlate)var3.next();
            if (this.listeningId == plate.id) {
               MacroManager.Macro var10000;
               String var10001;
               switch(this.listeningType.ordinal()) {
               case 0:
                  var10000 = plate.macro;
                  var10001 = plate.macro.getName();
                  var10000.setName(var10001 + key);
                  break;
               case 1:
                  var10000 = plate.macro;
                  var10001 = plate.macro.getText();
                  var10000.setText(var10001 + key);
               case 2:
               }

               return;
            }
         }

         if (this.listeningId == -3) {
            switch(this.listeningType.ordinal()) {
            case 0:
               this.addName = this.addName + key;
               break;
            case 1:
               this.addText = this.addText + key;
            case 2:
            }
         }

         this.refresh();
      }

   }

   private void refresh() {
      this.resetScroll();
      this.macroPlates.clear();
      int id1 = 0;
      Iterator var2 = Managers.MACRO.getMacros().iterator();

      while(true) {
         MacroManager.Macro m;
         do {
            if (!var2.hasNext()) {
               return;
            }

            m = (MacroManager.Macro)var2.next();
         } while(!this.search.equals("Search") && !this.search.isEmpty() && !m.getName().contains(this.search) && !m.getText().contains(this.search));

         this.macroPlates.add(new MacroWindow.MacroPlate(id1, (float)(id1 * 20 + 18), m));
         ++id1;
      }
   }

   public String toString(int key) {
      String kn = key > 0 ? GLFW.glfwGetKeyName(key, GLFW.glfwGetKeyScancode(key)) : "None";
      if (kn == null) {
         try {
            Field[] var3 = GLFW.class.getDeclaredFields();
            int var4 = var3.length;

            for(int var5 = 0; var5 < var4; ++var5) {
               Field declaredField = var3[var5];
               if (declaredField.getName().startsWith("GLFW_KEY_")) {
                  int a = (Integer)declaredField.get((Object)null);
                  if (a == key) {
                     String nb = declaredField.getName().substring("GLFW_KEY_".length());
                     String var10000 = nb.substring(0, 1).toUpperCase();
                     kn = var10000 + nb.substring(1).toLowerCase();
                  }
               }
            }
         } catch (Exception var9) {
            kn = "unknown." + key;
         }
      }

      return key == -1 ? "None" : kn.makeConcatWithConstants<invokedynamic>(kn).toUpperCase();
   }

   private String getSbind(String sbind) {
      if (sbind.equals("LEFT_CONTROL")) {
         sbind = "LCtrl";
      }

      if (sbind.equals("RIGHT_CONTROL")) {
         sbind = "RCtrl";
      }

      if (sbind.equals("LEFT_SHIFT")) {
         sbind = "LShift";
      }

      if (sbind.equals("RIGHT_SHIFT")) {
         sbind = "RShift";
      }

      if (sbind.equals("LEFT_ALT")) {
         sbind = "LAlt";
      }

      if (sbind.equals("RIGHT_ALT")) {
         sbind = "RAlt";
      }

      return sbind;
   }

   private static enum ListeningType {
      Name,
      Text,
      Bind;

      // $FF: synthetic method
      private static MacroWindow.ListeningType[] $values() {
         return new MacroWindow.ListeningType[]{Name, Text, Bind};
      }
   }

   private static record MacroPlate(int id, float offset, MacroManager.Macro macro) {
      private MacroPlate(int id, float offset, MacroManager.Macro macro) {
         this.id = id;
         this.offset = offset;
         this.macro = macro;
      }

      public int id() {
         return this.id;
      }

      public float offset() {
         return this.offset;
      }

      public MacroManager.Macro macro() {
         return this.macro;
      }
   }
}
