package tsunami.gui.mainmenu;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import net.minecraft.class_1011;
import net.minecraft.class_1043;
import net.minecraft.class_156;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_437;
import org.jetbrains.annotations.NotNull;
import tsunami.TsunamiClient;
import tsunami.features.modules.Module;
import tsunami.gui.font.FontRenderers;
import tsunami.utility.math.MathUtility;
import tsunami.utility.render.Render2DEngine;
import tsunami.utility.render.Render3DEngine;
import tsunami.utility.render.TextureStorage;

public class CreditsScreen extends class_437 {
   public ArrayList<CreditsScreen.Contributor> contributors = new ArrayList();
   private static int scroll;
   private static final int SCROLL_SPEED = 1;
   private static CreditsScreen INSTANCE = new CreditsScreen();

   protected CreditsScreen() {
      super(class_2561.method_30163("CreditsScreen"));
      INSTANCE = this;
      String[] var1 = TsunamiClient.contributors;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         String line = var1[var3];
         if (line != null) {
            String name = line.split(";")[0];
            String avatar = line.split(";")[1];
            String role = line.split(";")[2];
            String description = line.split(";")[3];
            String clickAction = line.split(";")[4];
            getInstance().contributors.add(new CreditsScreen.Contributor(name, getAvatar(avatar), role, description.replace('т', '\n'), clickAction));
         }
      }

   }

   public static CreditsScreen getInstance() {
      scroll = 150;
      if (INSTANCE == null) {
         INSTANCE = new CreditsScreen();
      }

      return INSTANCE;
   }

   public void method_25394(@NotNull class_332 context, int mouseX, int mouseY, float delta) {
      float halfOfWidth = (float)Module.mc.method_22683().method_4486() / 2.0F;
      float halfOfHeight = (float)Module.mc.method_22683().method_4502() / 2.0F;
      float globalOffset = (float)(this.contributors.size() * 150) / 2.0F;
      this.method_25420(context, mouseX, mouseY, delta);
      RenderSystem.enableBlend();
      RenderSystem.defaultBlendFunc();
      int offset = 0;

      for(Iterator var9 = this.contributors.iterator(); var9.hasNext(); offset += 150) {
         CreditsScreen.Contributor contributor = (CreditsScreen.Contributor)var9.next();
         float cX = halfOfWidth + (float)offset - globalOffset + (float)scroll;
         float cY = halfOfHeight - 120.0F;
         Render2DEngine.drawHudBase(context.method_51448(), cX, cY, 140.0F, 240.0F, 20.0F, false);
         FontRenderers.sf_medium.drawGradientString(context.method_51448(), contributor.name, cX + 70.0F - FontRenderers.sf_medium.getStringWidth(contributor.name) / 2.0F, halfOfHeight - 57.0F, 30);
         FontRenderers.sf_medium.drawCenteredString(context.method_51448(), contributor.role, (double)(cX + 70.0F), (double)(halfOfHeight - 48.0F), (new Color(8487297)).getRGB());
         Render2DEngine.horizontalGradient(context.method_51448(), cX + 2.0F, cY + 90.0F, cX + 70.0F, cY + 91.0F, Render2DEngine.injectAlpha(new Color(-1), 0), new Color(-1));
         Render2DEngine.horizontalGradient(context.method_51448(), cX + 70.0F, cY + 90.0F, cX + 138.0F, cY + 91.0F, new Color(-1), Render2DEngine.injectAlpha(new Color(-1), 0));
         Render2DEngine.drawRound(context.method_51448(), cX + 5.0F, cY + 100.0F, 130.0F, 130.0F, 8.0F, new Color(1929379840, true));
         FontRenderers.sf_medium.drawString(context.method_51448(), contributor.description, (double)(cX + 10.0F), (double)(cY + 108.0F), (new Color(8487298)).getRGB());
         if (contributor.avatar != null) {
            context.method_25293(contributor.avatar, (int)(cX + 70.0F - 24.0F), (int)(halfOfHeight - 110.0F), 48, 48, 0.0F, 0.0F, 96, 96, 96, 96);
         }

         if (Render2DEngine.isHovered((double)mouseX, (double)mouseY, (double)cX, (double)cY, 140.0D, 240.0D) && !Objects.equals(contributor.clickAction, "")) {
            Render2DEngine.drawRound(context.method_51448(), cX, cY, 140.0F, 240.0F, 8.0F, new Color(100663295, true));
         }
      }

      RenderSystem.disableBlend();
      Render2DEngine.drawHudBase(context.method_51448(), (float)(Module.mc.method_22683().method_4486() - 40), (float)(Module.mc.method_22683().method_4502() - 40), 30.0F, 30.0F, 5.0F, Render2DEngine.isHovered((double)mouseX, (double)mouseY, (double)(Module.mc.method_22683().method_4486() - 60), (double)(Module.mc.method_22683().method_4502() - 60), 40.0D, 40.0D) ? 0.7F : 1.0F);
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, Render2DEngine.isHovered((double)mouseX, (double)mouseY, (double)(Module.mc.method_22683().method_4486() - 40), (double)(Module.mc.method_22683().method_4502() - 40), 30.0D, 30.0D) ? 0.7F : 1.0F);
      context.method_25293(TextureStorage.thTeam, Module.mc.method_22683().method_4486() - 40, Module.mc.method_22683().method_4502() - 40, 30, 30, 0.0F, 0.0F, 30, 30, 30, 30);
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
   }

   public boolean method_25402(double mouseX, double mouseY, int button) {
      float halfOfWidth = (float)Module.mc.method_22683().method_4486() / 2.0F;
      float halfOfHeight = (float)Module.mc.method_22683().method_4502() / 2.0F;
      float globalOffset = (float)(this.contributors.size() * 150) / 2.0F;
      int offset = 0;

      for(Iterator var10 = this.contributors.iterator(); var10.hasNext(); offset += 150) {
         CreditsScreen.Contributor contributor = (CreditsScreen.Contributor)var10.next();
         float cX = (float)((double)(halfOfWidth + (float)offset - globalOffset) + Render2DEngine.interpolate((double)scroll, (double)(scroll + 1), (double)Render3DEngine.getTickDelta()));
         float cY = halfOfHeight - 120.0F;
         if (Render2DEngine.isHovered(mouseX, mouseY, (double)cX, (double)cY, 140.0D, 240.0D) && !Objects.equals(contributor.clickAction, "none")) {
            class_156.method_668().method_673(URI.create(contributor.clickAction));
         }
      }

      if (Render2DEngine.isHovered(mouseX, mouseY, (double)(Module.mc.method_22683().method_4486() - 40), (double)(Module.mc.method_22683().method_4502() - 40), 40.0D, 40.0D)) {
         Module.mc.method_1507(MainMenuScreen.getInstance());
      }

      return super.method_25402(mouseX, mouseY, button);
   }

   public static class_2960 getAvatar(String name) {
      try {
         class_1043 nIBT = getAvatarFromURL("https://cdn.discordapp.com/avatars/" + name + ".png?size=96");
         return nIBT != null ? class_310.method_1551().method_1531().method_4617("th-contributors-" + (int)MathUtility.random(0.0F, 1000000.0F), nIBT) : null;
      } catch (Exception var2) {
         var2.printStackTrace();
         return null;
      }
   }

   public static class_1043 getAvatarFromURL(String HeadStringURL) {
      try {
         return getAvatarFromStream((new URL(HeadStringURL)).openStream());
      } catch (IOException var2) {
         var2.printStackTrace();
         return null;
      }
   }

   public static class_1043 getAvatarFromStream(InputStream image) {
      class_1011 pic = null;

      try {
         pic = class_1011.method_4309(image);
      } catch (IOException var3) {
         var3.printStackTrace();
      }

      return pic != null ? new class_1043(parseAvatar(pic)) : null;
   }

   public static class_1011 parseAvatar(class_1011 image) {
      class_1011 imgNew = new class_1011(96, 96, true);

      for(int x = 0; x < 96; ++x) {
         for(int y = 0; y < 96; ++y) {
            if (Math.hypot((double)(x - 48), (double)(y - 48)) > 45.0D) {
               imgNew.method_4305(x, y, Render2DEngine.injectAlpha(new Color(image.method_4315(x, y)), (int)((float)(48.0D - Math.hypot((double)(x - 48), (double)(y - 48))) / 3.0F * 255.0F)).getRGB());
            } else {
               imgNew.method_4305(x, y, image.method_4315(x, y));
            }
         }
      }

      image.close();
      return imgNew;
   }

   public boolean method_25401(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
      scroll += (int)(verticalAmount * 5.0D);
      return super.method_25401(mouseX, mouseY, horizontalAmount, verticalAmount);
   }

   public void method_25393() {
      --scroll;
      if (scroll <= -(this.contributors.size() * 150) + 100) {
         scroll = 0;
      }

   }

   public static record Contributor(String name, class_2960 avatar, String role, String description, String clickAction) {
      public Contributor(String name, class_2960 avatar, String role, String description, String clickAction) {
         this.name = name;
         this.avatar = avatar;
         this.role = role;
         this.description = description;
         this.clickAction = clickAction;
      }

      public String name() {
         return this.name;
      }

      public class_2960 avatar() {
         return this.avatar;
      }

      public String role() {
         return this.role;
      }

      public String description() {
         return this.description;
      }

      public String clickAction() {
         return this.clickAction;
      }
   }
}
