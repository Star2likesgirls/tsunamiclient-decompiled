package tsunami.features.hud.impl;

import java.awt.Color;
import java.util.Iterator;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_1657;
import net.minecraft.class_332;
import org.jetbrains.annotations.NotNull;
import tsunami.core.Managers;
import tsunami.events.impl.TotemPopEvent;
import tsunami.features.hud.HudElement;
import tsunami.features.modules.client.ClientSettings;
import tsunami.features.modules.combat.AntiBot;
import tsunami.gui.font.FontRenderers;
import tsunami.setting.Setting;
import tsunami.utility.Timer;
import tsunami.utility.math.MathUtility;
import tsunami.utility.render.Render2DEngine;
import tsunami.utility.render.TextureStorage;

public class Companion extends HudElement {
   public Setting<Integer> scale = new Setting("Scale", 50, 0, 100);
   public Setting<Companion.Mode> mode;
   public static int currentFrame;
   private String message;
   private Timer lastPop;
   private Timer frameRate;

   public Companion() {
      super("2DCompanion", 50, 10);
      this.mode = new Setting("Mode", Companion.Mode.Boykisser);
      this.message = "";
      this.lastPop = new Timer();
      this.frameRate = new Timer();
   }

   public void onUpdate() {
      Iterator var1 = mc.field_1687.method_18456().iterator();

      while(var1.hasNext()) {
         class_1657 player = (class_1657)var1.next();
         if (player != mc.field_1724 && !AntiBot.bots.contains(player) && !(player.method_6032() > 0.0F) && Managers.COMBAT.popList.containsKey(player.method_5477().getString())) {
            String var10001;
            if (ClientSettings.isRu()) {
               var10001 = player.method_5477().getString();
               this.message = var10001 + " попнул " + ((Integer)Managers.COMBAT.popList.get(player.method_5477().getString()) > 1 ? String.valueOf(Managers.COMBAT.popList.get(player.method_5477().getString())) + " тотемов и сдох! ИЗЗЗЗИИ" : "тотем и сдох! ИЗЗЗЗИИ");
            } else {
               var10001 = player.method_5477().getString();
               this.message = var10001 + " popped " + ((Integer)Managers.COMBAT.popList.get(player.method_5477().getString()) > 1 ? String.valueOf(Managers.COMBAT.popList.get(player.method_5477().getString())) + " totems and died EZ LMAO!" : "totem and died EZ LMAO!");
            }

            this.lastPop.reset();
         }
      }

   }

   public void onRender2D(class_332 context) {
      super.onRender2D(context);
      context.method_51448().method_22903();
      context.method_51448().method_46416((float)((int)this.getPosX() + 100), (float)((int)this.getPosY() + 100), 0.0F);
      context.method_51448().method_22905((float)(Integer)this.scale.getValue() / 100.0F, (float)(Integer)this.scale.getValue() / 100.0F, 1.0F);
      context.method_51448().method_46416((float)(-((int)this.getPosX() + 100)), (float)(-((int)this.getPosY() + 100)), 0.0F);
      if (this.mode.getValue() == Companion.Mode.Boykisser) {
         context.method_25290(TextureStorage.boykisser, (int)this.getPosX(), (int)this.getPosY(), 0.0F, (float)(currentFrame * 128), 130, 128, 130, 6784);
      } else if (this.mode.getValue() == Companion.Mode.Paimon) {
         context.method_25290(TextureStorage.paimon, (int)this.getPosX(), (int)this.getPosY(), 0.0F, (float)(currentFrame * 200), 200, 200, 200, 10600);
      } else if (this.mode.getValue() == Companion.Mode.Baltika) {
         context.method_25290(TextureStorage.baltika, (int)this.getPosX(), (int)this.getPosY(), 0.0F, 0.0F, 421, 800, 421, 800);
      } else if (this.mode.getValue() == Companion.Mode.Kowk) {
         context.method_25290(TextureStorage.kowk, (int)this.getPosX(), (int)this.getPosY(), 0.0F, 0.0F, 287, 252, 287, 252);
      }

      context.method_51448().method_22909();
      if (!this.lastPop.passedMs(2000L)) {
         float w = FontRenderers.sf_bold.getStringWidth(this.message) + 8.0F;
         float factor = MathUtility.clamp((float)this.lastPop.getPassedTimeMs(), 0.0F, 500.0F) / 500.0F;
         Render2DEngine.drawRound(context.method_51448(), this.getPosX() + (float)(Integer)this.scale.getValue() / 3.0F, this.getPosY() + 70.0F - (float)(Integer)this.scale.getValue(), factor * w, 10.0F, 3.0F, new Color(16570333));
         Render2DEngine.addWindow(context.method_51448(), this.getPosX() + (float)(Integer)this.scale.getValue() / 3.0F, this.getPosY() + 72.0F - (float)(Integer)this.scale.getValue(), factor * w + this.getPosX() + (float)(Integer)this.scale.getValue() / 3.0F, 20.0F + this.getPosY() + 72.0F - (float)(Integer)this.scale.getValue(), 1.0D);
         FontRenderers.sf_bold.drawString(context.method_51448(), this.message, (double)(this.getPosX() + 2.0F + (float)(Integer)this.scale.getValue() / 3.0F), (double)(this.getPosY() + 72.0F - (float)(Integer)this.scale.getValue()), (new Color(4737096)).getRGB());
         Render2DEngine.popWindow();
      }

      if (this.frameRate.passedMs(64L)) {
         this.frameRate.reset();
         ++currentFrame;
         if (currentFrame > 52) {
            currentFrame = 0;
         }
      }

      if (this.mode.getValue() == Companion.Mode.Baltika) {
         this.setBounds(this.getPosX() + 100.0F, this.getPosY() + 100.0F, (float)(Integer)this.scale.getValue() * 3.0F, (float)(Integer)this.scale.getValue() * 3.0F);
      } else {
         this.setBounds(this.getPosX(), this.getPosY(), (float)(Integer)this.scale.getValue() * 3.0F, (float)(Integer)this.scale.getValue() * 3.0F);
      }

   }

   @EventHandler
   public void onTotemPop(@NotNull TotemPopEvent event) {
      if (event.getEntity() != mc.field_1724) {
         String var10001;
         if (ClientSettings.isRu()) {
            var10001 = event.getEntity().method_5477().getString();
            this.message = var10001 + " попнул " + (event.getPops() > 1 ? event.getPops() + " тотемов!" : "тотем!");
         } else {
            var10001 = event.getEntity().method_5477().getString();
            this.message = var10001 + " popped " + (event.getPops() > 1 ? event.getPops() + " totems!" : " a totem!");
         }

         this.lastPop.reset();
      }
   }

   private static enum Mode {
      Boykisser,
      Paimon,
      Baltika,
      Kowk;

      // $FF: synthetic method
      private static Companion.Mode[] $values() {
         return new Companion.Mode[]{Boykisser, Paimon, Baltika, Kowk};
      }
   }
}
