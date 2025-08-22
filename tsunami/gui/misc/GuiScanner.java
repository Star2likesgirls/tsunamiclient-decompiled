package tsunami.gui.misc;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.class_2561;
import net.minecraft.class_332;
import net.minecraft.class_437;
import tsunami.features.modules.Module;
import tsunami.features.modules.misc.NoCommentExploit;
import tsunami.gui.font.FontRenderers;
import tsunami.utility.render.Render2DEngine;

public class GuiScanner extends class_437 {
   public static boolean neartrack = false;
   public static boolean track = false;
   public static boolean busy = false;
   public ArrayList<NoCommentExploit.Cout> consoleout = new ArrayList();
   int radarx;
   int radary;
   int radarx1;
   int radary1;
   int centerx;
   int centery;
   int consolex;
   int consoley;
   int consolex1;
   int consoley1;
   int hovery;
   int hoverx;
   int searchx;
   int searchy;
   int wheely;

   public GuiScanner() {
      super(class_2561.method_30163("GuiScanner"));
   }

   public void method_25394(class_332 context, int mouseX, int mouseY, float delta) {
      if (Module.mc.field_1724 != null) {
         this.radarx = Module.mc.method_22683().method_4486() / 8;
         this.radarx1 = Module.mc.method_22683().method_4486() * 5 / 8;
         this.radary = Module.mc.method_22683().method_4502() / 2 - (this.radarx1 - this.radarx) / 2;
         this.radary1 = Module.mc.method_22683().method_4502() / 2 + (this.radarx1 - this.radarx) / 2;
         this.centerx = (this.radarx + this.radarx1) / 2;
         this.centery = (this.radary + this.radary1) / 2;
         this.consolex = (int)((float)Module.mc.method_22683().method_4486() * 5.5F / 8.0F);
         this.consolex1 = Module.mc.method_22683().method_4486() - 50;
         this.consoley = this.radary;
         this.consoley1 = this.radary1 - 50;
         Render2DEngine.drawRectDumbWay(context.method_51448(), (float)this.consolex, (float)this.consoley, (float)this.consolex1, (float)this.consoley1, new Color(-150205428, true));
         Render2DEngine.drawRectDumbWay(context.method_51448(), (float)this.consolex, (float)(this.consoley1 + 3), (float)this.consolex1, (float)(this.consoley1 + 17), new Color(-150205428, true));
         FontRenderers.monsterrat.drawString(context.method_51448(), "cursor pos: " + this.hoverx * 64 + "x  " + this.hovery * 64 + "z", (double)(this.consolex + 4), (double)(this.consoley1 + 6), -1);
         if (!track) {
            Render2DEngine.drawRectDumbWay(context.method_51448(), (float)this.consolex, (float)(this.consoley1 + 20), (float)this.consolex1, (float)(this.consoley1 + 35), new Color(-150205428, true));
            FontRenderers.monsterrat.drawString(context.method_51448(), "tracker off", (double)(this.consolex + 4), (double)(this.consoley1 + 26), -1);
         } else {
            Render2DEngine.drawRectDumbWay(context.method_51448(), (float)this.consolex, (float)(this.consoley1 + 20), (float)this.consolex1, (float)(this.consoley1 + 35), new Color(-144810402, true));
            FontRenderers.monsterrat.drawString(context.method_51448(), "tracker on", (double)(this.consolex + 4), (double)(this.consoley1 + 26), -1);
         }

         Render2DEngine.drawRectDumbWay(context.method_51448(), (float)this.consolex, (float)(this.consoley1 + 38), (float)this.consolex1, (float)(this.consoley1 + 53), new Color(-150205428, true));
         FontRenderers.monsterrat.drawString(context.method_51448(), "clear console", (double)(this.consolex + 4), (double)(this.consoley1 + 42), -1);
         Render2DEngine.drawRectDumbWay(context.method_51448(), (float)this.radarx, (float)this.radary, (float)this.radarx1, (float)this.radary1, new Color(-535489259, true));
         Iterator var5 = (new ArrayList(NoCommentExploit.dots)).iterator();

         while(var5.hasNext()) {
            NoCommentExploit.Dot point = (NoCommentExploit.Dot)var5.next();
            if (point.type() == NoCommentExploit.DotType.Searched) {
               Render2DEngine.drawRectDumbWay(context.method_51448(), (float)point.posX() / 4.0F + (float)this.centerx, (float)point.posY() / 4.0F + (float)this.centery, (float)point.posX() / 4.0F + (float)(this.radarx1 - this.radarx) / 300.0F + (float)this.centerx, (float)point.posY() / 4.0F + (float)(this.radary1 - this.radary) / 300.0F + (float)this.centery, new Color(-408377176, true));
            } else {
               Render2DEngine.drawRectDumbWay(context.method_51448(), (float)point.posX() / 4.0F + (float)this.centerx, (float)point.posY() / 4.0F + (float)this.centery, (float)point.posX() / 4.0F + (float)(this.radarx1 - this.radarx) / 300.0F + (float)this.centerx, (float)point.posY() / 4.0F + (float)(this.radary1 - this.radary) / 300.0F + (float)this.centery, new Color(3991304));
            }
         }

         Render2DEngine.drawRectDumbWay(context.method_51448(), (float)this.centerx - 1.0F, (float)this.centery - 1.0F, (float)this.centerx + 1.0F, (float)this.centery + 1.0F, new Color(16712451));
         Render2DEngine.drawRectDumbWay(context.method_51448(), (float)(Module.mc.field_1724.method_23317() / 16.0D / 4.0D + (double)this.centerx), (float)(Module.mc.field_1724.method_23321() / 16.0D / 4.0D + (double)this.centery), (float)(Module.mc.field_1724.method_23317() / 16.0D / 4.0D + (double)((float)(this.radarx1 - this.radarx) / 300.0F) + (double)this.centerx), (float)(Module.mc.field_1724.method_23321() / 16.0D / 4.0D + (double)((float)(this.radary1 - this.radary) / 300.0F) + (double)this.centery), new Color(4863));
         if (mouseX > this.radarx && mouseX < this.radarx1 && mouseY > this.radary && mouseY < this.radary1) {
            this.hoverx = mouseX - this.centerx;
            this.hovery = mouseY - this.centery;
         }

         Render2DEngine.addWindow(context.method_51448(), (float)this.consolex, (float)this.consoley, (float)this.consolex1, (float)(this.consoley1 - 10), 1.0D);
         var5 = (new ArrayList(this.consoleout)).iterator();

         while(var5.hasNext()) {
            NoCommentExploit.Cout out = (NoCommentExploit.Cout)var5.next();
            FontRenderers.monsterrat.drawString(context.method_51448(), out.out(), (double)(this.consolex + 4), (double)(this.consoley + 6 + out.posY() * 11 + this.wheely), -1);
         }

         Render2DEngine.popWindow();
         FontRenderers.monsterrat.drawString(context.method_51448(), "X+", (double)(this.radarx1 + 5), (double)this.centery, -1);
         FontRenderers.monsterrat.drawString(context.method_51448(), "X-", (double)(this.radarx - 15), (double)this.centery, -1);
         FontRenderers.monsterrat.drawString(context.method_51448(), "Y+", (double)this.centerx, (double)(this.radary1 + 5), -1);
         FontRenderers.monsterrat.drawString(context.method_51448(), "Y-", (double)this.centerx, (double)(this.radary - 8), -1);
      }
   }

   public boolean method_25402(double mouseX, double mouseY, int button) {
      if (mouseX > (double)this.radarx && mouseX < (double)this.radarx1 && mouseY > (double)this.radary && mouseY < (double)this.radary1) {
         busy = true;
         this.searchx = (int)(mouseX - (double)this.centerx);
         this.searchy = (int)(mouseY - (double)this.centery);
      }

      if (mouseX > (double)this.consolex && mouseX < (double)this.consolex1 && mouseY > (double)(this.consoley1 + 20) && mouseY < (double)(this.consoley1 + 36)) {
         track = !track;
      }

      if (mouseX > (double)this.consolex && mouseX < (double)this.consolex1 && mouseY > (double)(this.consoley1 + 38) && mouseY < (double)(this.consoley1 + 53)) {
         this.consoleout.clear();
      }

      return super.method_25402(mouseX, mouseY, button);
   }

   public boolean method_25401(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
      this.wheely += (int)(verticalAmount * 5.0D);
      return super.method_25401(mouseX, mouseY, horizontalAmount, verticalAmount);
   }
}
