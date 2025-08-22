package tsunami.gui.misc;

import java.awt.Color;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_332;
import net.minecraft.class_437;
import org.jetbrains.annotations.NotNull;
import tsunami.features.modules.Module;
import tsunami.gui.font.FontRenderers;
import tsunami.utility.render.Render2DEngine;

public class DialogScreen extends class_437 {
   private final class_2960 pic;
   private final String header;
   private final String description;
   private final String yesText;
   private final String noText;
   private final Runnable yesAction;
   private final Runnable noAction;

   public DialogScreen(class_2960 pic, String header, String description, String yesText, String noText, Runnable yesAction, Runnable noAction) {
      super(class_2561.method_30163("ThDialogScreen"));
      this.pic = pic;
      this.header = header;
      this.description = description;
      this.yesText = yesText;
      this.noText = noText;
      this.yesAction = yesAction;
      this.noAction = noAction;
   }

   public void method_25394(@NotNull class_332 context, int mouseX, int mouseY, float delta) {
      float halfOfWidth = (float)Module.mc.method_22683().method_4486() / 2.0F;
      float halfOfHeight = (float)Module.mc.method_22683().method_4502() / 2.0F;
      float mainX = halfOfWidth - 120.0F;
      float mainY = halfOfHeight - 80.0F;
      float mainWidth = 240.0F;
      float mainHeight = 140.0F;
      Render2DEngine.drawHudBase(context.method_51448(), mainX, mainY, mainWidth, mainHeight, 20.0F, false);
      FontRenderers.sf_medium.drawCenteredString(context.method_51448(), this.header, (double)(mainX + mainWidth / 2.0F), (double)(mainY + 5.0F), -1);
      FontRenderers.sf_medium.drawCenteredString(context.method_51448(), this.description, (double)(mainX + mainWidth / 2.0F), (double)(mainY + 12.0F), (new Color(-1409286145, true)).getRGB());
      Render2DEngine.drawHudBase(context.method_51448(), mainX + 5.0F, mainY + 95.0F, 110.0F, 40.0F, 15.0F, false);
      FontRenderers.sf_medium.drawCenteredString(context.method_51448(), this.yesText, (double)(mainX + 60.0F), (double)(mainY + 112.0F), this.yesHovered(mouseX, mouseY) ? -1 : (new Color(-1409286145, true)).getRGB());
      Render2DEngine.drawHudBase(context.method_51448(), mainX + 125.0F, mainY + 95.0F, 110.0F, 40.0F, 15.0F, false);
      FontRenderers.sf_medium.drawCenteredString(context.method_51448(), this.noText, (double)(mainX + 180.0F), (double)(mainY + 112.0F), this.noHovered(mouseX, mouseY) ? -1 : (new Color(-1409286145, true)).getRGB());
      context.method_25290(this.pic, (int)(mainX + mainWidth / 2.0F - 35.0F), (int)mainY + 25, 0.0F, 0.0F, 70, 65, 70, 65);
   }

   private boolean isHovered(int mouseX, int mouseY, int x, int y, int width, int height) {
      return mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height;
   }

   private boolean yesHovered(int mX, int mY) {
      float mainX = (float)Module.mc.method_22683().method_4486() / 2.0F - 120.0F;
      float mainY = (float)Module.mc.method_22683().method_4502() / 2.0F - 80.0F;
      return this.isHovered(mX, mY, (int)mainX + 5, (int)mainY + 95, 110, 40);
   }

   private boolean noHovered(int mX, int mY) {
      float mainX = (float)Module.mc.method_22683().method_4486() / 2.0F - 120.0F;
      float mainY = (float)Module.mc.method_22683().method_4502() / 2.0F - 80.0F;
      return this.isHovered(mX, mY, (int)mainX + 125, (int)mainY + 95, 110, 40);
   }

   public boolean method_25402(double mouseX, double mouseY, int button) {
      if (this.yesHovered((int)mouseX, (int)mouseY)) {
         this.yesAction.run();
      } else if (this.noHovered((int)mouseX, (int)mouseY)) {
         this.noAction.run();
      }

      return super.method_25402(mouseX, mouseY, button);
   }
}
