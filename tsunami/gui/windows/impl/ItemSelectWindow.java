package tsunami.gui.windows.impl;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import net.minecraft.class_1074;
import net.minecraft.class_124;
import net.minecraft.class_1792;
import net.minecraft.class_2248;
import net.minecraft.class_286;
import net.minecraft.class_287;
import net.minecraft.class_289;
import net.minecraft.class_290;
import net.minecraft.class_2960;
import net.minecraft.class_332;
import net.minecraft.class_3544;
import net.minecraft.class_3675;
import net.minecraft.class_757;
import net.minecraft.class_7923;
import net.minecraft.class_293.class_5596;
import tsunami.core.Managers;
import tsunami.features.modules.Module;
import tsunami.features.modules.client.ClientSettings;
import tsunami.gui.clickui.ClickGUI;
import tsunami.gui.clickui.impl.SliderElement;
import tsunami.gui.font.FontRenderers;
import tsunami.gui.windows.WindowBase;
import tsunami.setting.Setting;
import tsunami.setting.impl.ItemSelectSetting;
import tsunami.utility.render.Render2DEngine;

public class ItemSelectWindow extends WindowBase {
   private Setting<ItemSelectSetting> itemSetting;
   private ArrayList<ItemSelectWindow.ItemPlate> itemPlates;
   private ArrayList<ItemSelectWindow.ItemPlate> allItems;
   private boolean allTab;
   private boolean listening;
   private String search;

   public ItemSelectWindow(Setting<ItemSelectSetting> itemSetting) {
      this((float)Module.mc.method_22683().method_4486() / 2.0F - 100.0F, (float)Module.mc.method_22683().method_4502() / 2.0F - 150.0F, 200.0F, 300.0F, itemSetting);
   }

   public ItemSelectWindow(float x, float y, float width, float height, Setting<ItemSelectSetting> itemSetting) {
      super(x, y, width, height, "Items / " + String.valueOf(class_124.field_1080) + itemSetting.getModule().getName(), (Setting)null, (class_2960)null);
      this.itemPlates = new ArrayList();
      this.allItems = new ArrayList();
      this.allTab = true;
      this.listening = false;
      this.search = "Search";
      this.itemSetting = itemSetting;
      this.refreshItemPlates();
      int id1 = 0;

      Iterator var7;
      for(var7 = class_7923.field_41175.iterator(); var7.hasNext(); ++id1) {
         class_2248 block = (class_2248)var7.next();
         this.allItems.add(new ItemSelectWindow.ItemPlate((float)id1, (float)(id1 * 20), block.method_8389(), block.method_9539()));
      }

      for(var7 = class_7923.field_41178.iterator(); var7.hasNext(); ++id1) {
         class_1792 item = (class_1792)var7.next();
         this.allItems.add(new ItemSelectWindow.ItemPlate((float)id1, (float)(id1 * 20), item, item.method_7876()));
      }

   }

   public void render(class_332 context, int mouseX, int mouseY) {
      super.render(context, mouseX, mouseY);
      boolean hover1 = Render2DEngine.isHovered((double)mouseX, (double)mouseY, (double)(this.getX() + this.getWidth() - 90.0F), (double)(this.getY() + 3.0F), 70.0D, 10.0D);
      Render2DEngine.drawRect(context.method_51448(), this.getX() + this.getWidth() - 90.0F, this.getY() + 3.0F, 70.0F, 10.0F, hover1 ? new Color(-981236861, true) : new Color(-984131753, true));
      FontRenderers.sf_medium_mini.drawString(context.method_51448(), this.search, (double)(this.getX() + this.getWidth() - 86.0F), (double)(this.getY() + 7.0F), (new Color(14013909)).getRGB());
      RenderSystem.setShader(class_757::method_34540);
      int tabColor1 = this.allTab ? (new Color(14013909)).getRGB() : Color.GRAY.getRGB();
      int tabColor2 = this.allTab ? Color.GRAY.getRGB() : (new Color(12434877)).getRGB();
      class_287 bufferBuilder = class_289.method_1348().method_60827(class_5596.field_29345, class_290.field_1576);
      bufferBuilder.method_22912(this.getX() + 1.5F, this.getY() + 29.0F, 0.0F).method_39415(Color.DARK_GRAY.getRGB());
      bufferBuilder.method_22912(this.getX() + 8.0F, this.getY() + 29.0F, 0.0F).method_39415(tabColor1);
      bufferBuilder.method_22912(this.getX() + 8.0F, this.getY() + 19.0F, 0.0F).method_39415(tabColor1);
      bufferBuilder.method_22912(this.getX() + 48.0F, this.getY() + 19.0F, 0.0F).method_39415(tabColor1);
      bufferBuilder.method_22912(this.getX() + 54.0F, this.getY() + 29.0F, 0.0F).method_39415(tabColor1);
      bufferBuilder.method_22912(this.getX() + 52.0F, this.getY() + 25.0F, 0.0F).method_39415(tabColor2);
      bufferBuilder.method_22912(this.getX() + 52.0F, this.getY() + 19.0F, 0.0F).method_39415(tabColor2);
      bufferBuilder.method_22912(this.getX() + 92.0F, this.getY() + 19.0F, 0.0F).method_39415(tabColor2);
      bufferBuilder.method_22912(this.getX() + 100.0F, this.getY() + 29.0F, 0.0F).method_39415(Color.GRAY.getRGB());
      bufferBuilder.method_22912(this.getX() + this.getWidth() - 1.0F, this.getY() + 29.0F, 0.0F).method_39415(Color.DARK_GRAY.getRGB());
      class_286.method_43433(bufferBuilder.method_60800());
      FontRenderers.sf_medium_mini.drawString(context.method_51448(), "All", (double)(this.getX() + 25.0F), (double)(this.getY() + 25.0F), tabColor1);
      FontRenderers.sf_medium_mini.drawString(context.method_51448(), "Selected", (double)(this.getX() + 60.0F), (double)(this.getY() + 25.0F), tabColor2);
      if (!this.allTab && this.itemPlates.isEmpty()) {
         FontRenderers.sf_medium.drawCenteredString(context.method_51448(), ClientSettings.isRu() ? "Тут пока пусто" : "It's empty here yet", (double)(this.getX() + this.getWidth() / 2.0F), (double)(this.getY() + this.getHeight() / 2.0F), (new Color(12434877)).getRGB());
      }

      Render2DEngine.addWindow(context.method_51448(), this.getX(), this.getY() + 30.0F, this.getX() + this.getWidth(), this.getY() + this.getHeight() - 1.0F, 1.0D);
      Iterator var8 = (this.allTab ? this.allItems : this.itemPlates).iterator();

      while(true) {
         while(true) {
            ItemSelectWindow.ItemPlate itemPlate;
            do {
               do {
                  if (!var8.hasNext()) {
                     this.setMaxElementsHeight((float)((this.allTab ? this.allItems : this.itemPlates).size() * 20));
                     Render2DEngine.popWindow();
                     return;
                  }

                  itemPlate = (ItemSelectWindow.ItemPlate)var8.next();
               } while(itemPlate.offset + this.getY() + 25.0F + this.getScrollOffset() > this.getY() + this.getHeight());
            } while(itemPlate.offset + this.getScrollOffset() + this.getY() + 10.0F < this.getY());

            context.method_51448().method_22903();
            context.method_51448().method_46416(this.getX() + 6.0F, itemPlate.offset + this.getY() + 32.0F + this.getScrollOffset(), 0.0F);
            context.method_51427(itemPlate.item().method_7854(), 0, 0);
            context.method_51448().method_22909();
            FontRenderers.sf_medium.drawString(context.method_51448(), class_1074.method_4662(itemPlate.key(), new Object[0]), (double)(this.getX() + 26.0F), (double)(itemPlate.offset + this.getY() + 38.0F + this.getScrollOffset()), (new Color(12434877)).getRGB());
            boolean hover2 = Render2DEngine.isHovered((double)mouseX, (double)mouseY, (double)(this.getX() + this.getWidth() - 20.0F), (double)(itemPlate.offset + this.getY() + 35.0F + this.getScrollOffset()), 11.0D, 11.0D);
            Render2DEngine.drawRect(context.method_51448(), this.getX() + this.getWidth() - 20.0F, itemPlate.offset + this.getY() + 35.0F + this.getScrollOffset(), 11.0F, 11.0F, hover2 ? new Color(-981828998, true) : new Color(-984131753, true));
            boolean selected = this.itemPlates.stream().anyMatch((sI) -> {
               return Objects.equals(sI.key, itemPlate.key);
            });
            if (this.allTab && !selected) {
               FontRenderers.categories.drawString(context.method_51448(), "+", (double)(this.getX() + this.getWidth() - 17.0F), (double)(itemPlate.offset + this.getY() + 39.0F + this.getScrollOffset()), -1);
            } else {
               FontRenderers.icons.drawString(context.method_51448(), "w", (double)(this.getX() + this.getWidth() - 19.5F), (double)(itemPlate.offset + this.getY() + 39.0F + this.getScrollOffset()), -1);
            }
         }
      }
   }

   public void mouseClicked(double mouseX, double mouseY, int button) {
      super.mouseClicked(mouseX, mouseY, button);
      if (Render2DEngine.isHovered(mouseX, mouseY, (double)(this.getX() + 8.0F), (double)(this.getY() + 19.0F), 52.0D, 19.0D)) {
         this.allTab = true;
         this.resetScroll();
         Managers.SOUND.playBoolean();
      }

      if (Render2DEngine.isHovered(mouseX, mouseY, (double)(this.getX() + 54.0F), (double)(this.getY() + 19.0F), 70.0D, 19.0D)) {
         this.allTab = false;
         this.resetScroll();
         Managers.SOUND.playBoolean();
      }

      if (Render2DEngine.isHovered(mouseX, mouseY, (double)(this.getX() + this.getWidth() - 90.0F), (double)(this.getY() + 3.0F), 70.0D, 10.0D)) {
         this.listening = true;
         this.search = "";
      }

      if (Render2DEngine.isHovered(mouseX, mouseY, (double)(this.getX() + this.getWidth() - 15.0F), (double)(this.getY() + 3.0F), 10.0D, 10.0D)) {
         Module.mc.method_1507(ClickGUI.getClickGui());
      }

      ArrayList<ItemSelectWindow.ItemPlate> copy = Lists.newArrayList(this.allTab ? this.allItems : this.itemPlates);
      Iterator var7 = copy.iterator();

      while(true) {
         while(true) {
            ItemSelectWindow.ItemPlate itemPlate;
            String name;
            do {
               do {
                  if (!var7.hasNext()) {
                     return;
                  }

                  itemPlate = (ItemSelectWindow.ItemPlate)var7.next();
               } while((float)((int)(itemPlate.offset + this.getY() + 50.0F)) + this.getScrollOffset() > this.getY() + this.getHeight());

               name = itemPlate.key().replace("item.minecraft.", "").replace("block.minecraft.", "");
            } while(!Render2DEngine.isHovered(mouseX, mouseY, (double)(this.getX() + this.getWidth() - 20.0F), (double)(itemPlate.offset + this.getY() + 35.0F + this.getScrollOffset()), 10.0D, 10.0D));

            boolean selected = this.itemPlates.stream().anyMatch((sI) -> {
               return Objects.equals(sI.key(), itemPlate.key);
            });
            if (this.allTab && !selected) {
               if (((ItemSelectSetting)this.itemSetting.getValue()).getItemsById().contains(name)) {
                  continue;
               }

               ((ItemSelectSetting)this.itemSetting.getValue()).getItemsById().add(name);
               this.refreshItemPlates();
               break;
            }

            ((ItemSelectSetting)this.itemSetting.getValue()).getItemsById().remove(name);
            this.refreshItemPlates();
            break;
         }

         Managers.SOUND.playScroll();
      }
   }

   public void keyPressed(int keyCode, int scanCode, int modifiers) {
      if (keyCode != 70 || !class_3675.method_15987(Module.mc.method_22683().method_4490(), 341) && !class_3675.method_15987(Module.mc.method_22683().method_4490(), 345)) {
         if (this.listening) {
            switch(keyCode) {
            case 32:
               this.search = this.search + " ";
               break;
            case 256:
               this.listening = false;
               this.search = "Search";
               this.refreshAllItems();
               break;
            case 259:
               this.search = SliderElement.removeLastChar(this.search);
               this.refreshAllItems();
               if (Objects.equals(this.search, "")) {
                  this.listening = false;
                  this.search = "Search";
               }
            }
         }

      } else {
         this.listening = !this.listening;
      }
   }

   public void charTyped(char key, int keyCode) {
      if (class_3544.method_57175(key) && this.listening) {
         this.search = this.search + key;
         this.refreshAllItems();
      }

   }

   private void refreshItemPlates() {
      this.itemPlates.clear();
      int id = 0;
      Iterator var2 = class_7923.field_41175.iterator();

      while(var2.hasNext()) {
         class_2248 block = (class_2248)var2.next();
         if (((ItemSelectSetting)this.itemSetting.getValue()).getItemsById().contains(block.method_9539().replace("block.minecraft.", ""))) {
            this.itemPlates.add(new ItemSelectWindow.ItemPlate((float)id, (float)(id * 20), block.method_8389(), block.method_9539()));
            ++id;
         }
      }

      var2 = class_7923.field_41178.iterator();

      while(var2.hasNext()) {
         class_1792 item = (class_1792)var2.next();
         if (((ItemSelectSetting)this.itemSetting.getValue()).getItemsById().contains(item.method_7876().replace("item.minecraft.", ""))) {
            this.itemPlates.add(new ItemSelectWindow.ItemPlate((float)id, (float)(id * 20), item, item.method_7876()));
            ++id;
         }
      }

   }

   private void refreshAllItems() {
      this.allItems.clear();
      this.resetScroll();
      int id1 = 0;
      Iterator var2 = class_7923.field_41175.iterator();

      while(true) {
         class_2248 block;
         do {
            if (!var2.hasNext()) {
               var2 = class_7923.field_41178.iterator();

               while(true) {
                  class_1792 item;
                  do {
                     if (!var2.hasNext()) {
                        return;
                     }

                     item = (class_1792)var2.next();
                  } while(!this.search.equals("Search") && !this.search.isEmpty() && !item.method_7876().contains(this.search) && !item.method_7848().getString().toLowerCase().contains(this.search.toLowerCase()));

                  this.allItems.add(new ItemSelectWindow.ItemPlate((float)id1, (float)(id1 * 20), item, item.method_7876()));
                  ++id1;
               }
            }

            block = (class_2248)var2.next();
         } while(!this.search.equals("Search") && !this.search.isEmpty() && !block.method_9539().contains(this.search) && !class_1074.method_4662(block.method_9539(), new Object[0]).toLowerCase().contains(this.search.toLowerCase()));

         this.allItems.add(new ItemSelectWindow.ItemPlate((float)id1, (float)(id1 * 20), block.method_8389(), block.method_9539()));
         ++id1;
      }
   }

   private static record ItemPlate(float id, float offset, class_1792 item, String key) {
      private ItemPlate(float id, float offset, class_1792 item, String key) {
         this.id = id;
         this.offset = offset;
         this.item = item;
         this.key = key;
      }

      public float id() {
         return this.id;
      }

      public float offset() {
         return this.offset;
      }

      public class_1792 item() {
         return this.item;
      }

      public String key() {
         return this.key;
      }
   }
}
