package tsunami.gui.thundergui.components;

import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Objects;
import net.minecraft.class_1011;
import net.minecraft.class_1043;
import net.minecraft.class_156;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_332;
import tsunami.core.Core;
import tsunami.core.Managers;
import tsunami.features.modules.Module;
import tsunami.gui.font.FontRenderers;
import tsunami.gui.thundergui.ThunderGui;
import tsunami.utility.render.Render2DEngine;
import tsunami.utility.render.TextureStorage;
import tsunami.utility.render.animation.AnimationUtility;

public class FriendComponent {
   float scroll_animation = 0.0F;
   private class_2960 head = null;
   private final String name;
   private int posX;
   private int posY;
   private int progress;
   private int fade;
   private final int index;
   private boolean first_open = true;
   private float scrollPosY;
   private float prevPosY;

   public FriendComponent(String name, int posX, int posY, int index) {
      this.name = name;
      this.posX = posX;
      this.posY = posY;
      this.fade = 0;
      this.index = index * 5;
      this.loadHead(name);
      this.scrollPosY = (float)posY;
      this.scroll_animation = 0.0F;
   }

   public void loadHead(String name) {
      if (Core.HEADS.containsKey(name)) {
         this.head = (class_2960)Core.HEADS.get(name);
      }

      class_156.method_18349().execute(() -> {
         try {
            class_1043 nIBT = getHeadFromURL("https://minotar.net/helm/" + name + "/22.png");
            this.head = class_310.method_1551().method_1531().method_4617("th-heads-" + name, nIBT);
            Core.HEADS.put(name, this.head);
         } catch (Exception var3) {
            this.head = null;
         }

      });
   }

   public static class_1043 getHeadFromURL(String HeadStringURL) {
      try {
         URL capeURL = new URL(HeadStringURL);
         return getHeadFromStream(capeURL.openStream());
      } catch (IOException var2) {
         return null;
      }
   }

   public static class_1043 getHeadFromStream(InputStream image) {
      class_1011 Head = null;

      try {
         Head = class_1011.method_4309(image);
      } catch (IOException var3) {
         var3.printStackTrace();
      }

      if (Head != null) {
         class_1043 nIBT = new class_1043(parseHead(Head));
         return nIBT;
      } else {
         return null;
      }
   }

   public static class_1011 parseHead(class_1011 image) {
      int imageWidth = 22;
      int imageHeight = 22;
      int imageSrcWidth = image.method_4307();
      int srcHeight = image.method_4323();

      for(int imageSrcHeight = image.method_4323(); imageWidth < imageSrcWidth || imageHeight < imageSrcHeight; imageHeight *= 2) {
         imageWidth *= 2;
      }

      class_1011 imgNew = new class_1011(imageWidth, imageHeight, true);

      for(int x = 0; x < imageSrcWidth; ++x) {
         for(int y = 0; y < srcHeight; ++y) {
            imgNew.method_4305(x, y, image.method_4315(x, y));
         }
      }

      image.close();
      return imgNew;
   }

   public void render(class_332 context, int MouseX, int MouseY) {
      if (this.scrollPosY != (float)this.posY) {
         this.scroll_animation = AnimationUtility.fast(this.scroll_animation, 1.0F, 15.0F);
         this.posY = (int)Render2DEngine.interpolate((double)this.prevPosY, (double)this.scrollPosY, (double)this.scroll_animation);
      }

      if (this.posY <= ThunderGui.getInstance().main_posY + ThunderGui.getInstance().field_22790 && this.posY >= ThunderGui.getInstance().main_posY) {
         Render2DEngine.drawRound(context.method_51448(), (float)(this.posX + 5), (float)this.posY, 285.0F, 30.0F, 4.0F, Render2DEngine.applyOpacity(new Color(44, 35, 52, 255), this.getFadeFactor()));
         if (this.first_open) {
            Render2DEngine.addWindow(context.method_51448(), (float)(this.posX + 5), (float)this.posY, (float)(this.posX + 5 + 285), (float)(this.posY + 30), 1.0D);
            Render2DEngine.drawBlurredShadow(context.method_51448(), (float)(MouseX - 20), (float)(MouseY - 20), 40.0F, 40.0F, 60, Render2DEngine.applyOpacity(new Color(-1017816450, true), this.getFadeFactor()));
            Render2DEngine.popWindow();
            this.first_open = false;
         }

         if (this.isHovered(MouseX, MouseY)) {
            Render2DEngine.addWindow(context.method_51448(), (float)(this.posX + 5), (float)this.posY, (float)(this.posX + 5 + 285), (float)(this.posY + 30), 1.0D);
            Render2DEngine.drawBlurredShadow(context.method_51448(), (float)(MouseX - 20), (float)(MouseY - 20), 40.0F, 40.0F, 60, Render2DEngine.applyOpacity(new Color(-1017816450, true), this.getFadeFactor()));
            Render2DEngine.popWindow();
         }

         Render2DEngine.drawRound(context.method_51448(), (float)(this.posX + 266), (float)(this.posY + 8), 14.0F, 14.0F, 2.0F, Render2DEngine.applyOpacity(new Color(25, 20, 30, 255), this.getFadeFactor()));
         if (Render2DEngine.isHovered((double)MouseX, (double)MouseY, (double)(this.posX + 268), (double)(this.posY + 10), 10.0D, 10.0D)) {
            Render2DEngine.drawRound(context.method_51448(), (float)(this.posX + 268), (float)(this.posY + 10), 10.0F, 10.0F, 2.0F, Render2DEngine.applyOpacity(new Color(65, 1, 13, 255), this.getFadeFactor()));
         } else {
            Render2DEngine.drawRound(context.method_51448(), (float)(this.posX + 268), (float)(this.posY + 10), 10.0F, 10.0F, 2.0F, Render2DEngine.applyOpacity(new Color(94, 1, 18, 255), this.getFadeFactor()));
         }

         FontRenderers.icons.drawString(context.method_51448(), "w", (double)(this.posX + 268), (double)(this.posY + 13), Render2DEngine.applyOpacity(-1, this.getFadeFactor()));
         context.method_25290((class_2960)Objects.requireNonNullElse(this.head, TextureStorage.crackedSkin), this.posX + 10, this.posY + 3, 0.0F, 0.0F, 22, 22, 22, 22);
         FontRenderers.modules.drawString(context.method_51448(), this.name, (double)(this.posX + 37), (double)(this.posY + 6), Render2DEngine.applyOpacity(-1, this.getFadeFactor()));
         boolean online = Module.mc.field_1724.field_3944.method_2880().stream().map((p) -> {
            return p.method_2966().getName();
         }).toList().contains(this.name);
         FontRenderers.settings.drawString(context.method_51448(), online ? "online" : "offline", (double)(this.posX + 37), (double)(this.posY + 17), online ? Render2DEngine.applyOpacity((new Color(-16025088, true)).getRGB(), this.getFadeFactor()) : Render2DEngine.applyOpacity((new Color(-4342339, true)).getRGB(), this.getFadeFactor()));
      }
   }

   private float getFadeFactor() {
      return (float)this.fade / (5.0F + (float)this.index);
   }

   public void onTick() {
      if (this.progress > 4) {
         this.progress = 0;
      }

      ++this.progress;
      if (this.fade < 10 + this.index) {
         ++this.fade;
      }

   }

   private boolean isHovered(int mouseX, int mouseY) {
      return mouseX > this.posX && mouseX < this.posX + 295 && mouseY > this.posY && mouseY < this.posY + 30;
   }

   public void movePosition(float deltaX, float deltaY) {
      this.posY = (int)((float)this.posY + deltaY);
      this.posX = (int)((float)this.posX + deltaX);
      this.scrollPosY = (float)this.posY;
   }

   public void mouseClicked(int MouseX, int MouseY, int clickedButton) {
      if (this.posY <= ThunderGui.getInstance().main_posY + ThunderGui.getInstance().field_22790 && this.posY >= ThunderGui.getInstance().main_posY) {
         if (Render2DEngine.isHovered((double)MouseX, (double)MouseY, (double)(this.posX + 268), (double)(this.posY + 10), 10.0D, 10.0D)) {
            Managers.FRIEND.removeFriend(this.name);
            ThunderGui.getInstance().loadFriends();
         }

      }
   }

   public double getPosX() {
      return (double)this.posX;
   }

   public double getPosY() {
      return (double)this.posY;
   }

   public void scrollElement(float deltaY) {
      this.scroll_animation = 0.0F;
      this.prevPosY = (float)this.posY;
      this.scrollPosY += deltaY;
   }
}
