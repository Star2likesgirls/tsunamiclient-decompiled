package tsunami.features.hud.impl;

import com.google.common.collect.Lists;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_124;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_2596;
import net.minecraft.class_2663;
import net.minecraft.class_332;
import org.jetbrains.annotations.NotNull;
import tsunami.events.impl.PacketEvent;
import tsunami.features.hud.HudElement;
import tsunami.features.modules.client.HudEditor;
import tsunami.features.modules.combat.Aura;
import tsunami.features.modules.combat.AutoCrystal;
import tsunami.gui.font.FontRenderers;
import tsunami.setting.Setting;
import tsunami.setting.impl.ColorSetting;
import tsunami.utility.render.Render2DEngine;
import tsunami.utility.render.animation.AnimationUtility;

public class KillFeed extends HudElement {
   private Setting<Boolean> resetOnDeath = new Setting("ResetOnDeath", true);
   private final List<KillFeed.KillComponent> players = new ArrayList();
   private float vAnimation;
   private float hAnimation;

   public KillFeed() {
      super("KillFeed", 50, 50);
   }

   public void onRender2D(class_332 context) {
      super.onRender2D(context);
      int y_offset1 = 0;
      float scale_x = 30.0F;

      for(Iterator var4 = Lists.newArrayList(this.players).iterator(); var4.hasNext(); y_offset1 += 15) {
         KillFeed.KillComponent kc = (KillFeed.KillComponent)var4.next();
         if (FontRenderers.modules.getStringWidth(kc.getString()) > scale_x) {
            scale_x = FontRenderers.modules.getStringWidth(kc.getString());
         }
      }

      this.vAnimation = AnimationUtility.fast(this.vAnimation, (float)(14 + y_offset1), 15.0F);
      this.hAnimation = AnimationUtility.fast(this.hAnimation, scale_x + 10.0F, 15.0F);
      Render2DEngine.drawHudBase(context.method_51448(), this.getPosX(), this.getPosY(), this.hAnimation, this.vAnimation, (Float)HudEditor.hudRound.getValue());
      if (HudEditor.hudStyle.is(HudEditor.HudStyle.Glowing)) {
         FontRenderers.sf_bold.drawCenteredString(context.method_51448(), "KillFeed", (double)(this.getPosX() + this.hAnimation / 2.0F), (double)(this.getPosY() + 4.0F), ((ColorSetting)HudEditor.textColor.getValue()).getColorObject());
      } else {
         FontRenderers.sf_bold.drawGradientCenteredString(context.method_51448(), "KillFeed", this.getPosX() + this.hAnimation / 2.0F, this.getPosY() + 4.0F, 10);
      }

      if (y_offset1 > 0) {
         if (HudEditor.hudStyle.is(HudEditor.HudStyle.Glowing)) {
            Render2DEngine.horizontalGradient(context.method_51448(), this.getPosX() + 2.0F, this.getPosY() + 13.7F, this.getPosX() + 2.0F + this.hAnimation / 2.0F - 2.0F, this.getPosY() + 14.0F, Render2DEngine.injectAlpha(((ColorSetting)HudEditor.textColor.getValue()).getColorObject(), 0), ((ColorSetting)HudEditor.textColor.getValue()).getColorObject());
            Render2DEngine.horizontalGradient(context.method_51448(), this.getPosX() + 2.0F + this.hAnimation / 2.0F - 2.0F, this.getPosY() + 13.7F, this.getPosX() + 2.0F + this.hAnimation - 4.0F, this.getPosY() + 14.0F, ((ColorSetting)HudEditor.textColor.getValue()).getColorObject(), Render2DEngine.injectAlpha(((ColorSetting)HudEditor.textColor.getValue()).getColorObject(), 0));
         } else {
            Render2DEngine.drawRectDumbWay(context.method_51448(), this.getPosX() + 4.0F, this.getPosY() + 13.0F, this.getPosX() + this.getWidth() - 4.0F, this.getPosY() + 13.5F, new Color(1426063359, true));
         }
      }

      Render2DEngine.addWindow(context.method_51448(), this.getPosX(), this.getPosY(), this.getPosX() + this.hAnimation, this.getPosY() + this.vAnimation, 1.0D);
      int y_offset = 3;

      for(Iterator var8 = Lists.newArrayList(this.players).iterator(); var8.hasNext(); y_offset += 10) {
         KillFeed.KillComponent kc = (KillFeed.KillComponent)var8.next();
         FontRenderers.modules.drawString(context.method_51448(), kc.getString(), (double)(this.getPosX() + 5.0F), (double)(this.getPosY() + 18.0F + (float)y_offset), -1);
      }

      Render2DEngine.popWindow();
      this.setBounds(this.getPosX(), this.getPosY(), this.hAnimation, this.vAnimation);
   }

   @EventHandler
   public void onPacket(@NotNull PacketEvent.Receive e) {
      class_2596 var3 = e.getPacket();
      if (var3 instanceof class_2663) {
         class_2663 pac = (class_2663)var3;
         if (pac.method_11470() == 3) {
            class_1297 var4 = pac.method_11469(mc.field_1687);
            if (var4 instanceof class_1657) {
               class_1657 pl = (class_1657)var4;
               if (pl == mc.field_1724 && (Boolean)this.resetOnDeath.getValue()) {
                  this.players.clear();
                  return;
               }

               if (Aura.target != null && Aura.target == pac.method_11469(mc.field_1687) || AutoCrystal.target != null && AutoCrystal.target == pac.method_11469(mc.field_1687)) {
                  Iterator var7 = Lists.newArrayList(this.players).iterator();

                  while(var7.hasNext()) {
                     KillFeed.KillComponent kc = (KillFeed.KillComponent)var7.next();
                     if (Objects.equals(kc.getName(), pl.method_5477().getString())) {
                        kc.increase();
                        return;
                     }
                  }

                  this.players.add(new KillFeed.KillComponent(this, pl.method_5477().getString()));
               }
            }
         }

      }
   }

   public void onDisable() {
      this.players.clear();
   }

   private class KillComponent {
      private String name;
      private int count;

      public KillComponent(final KillFeed param1, String name) {
         this.name = name;
         this.count = 1;
      }

      public void increase() {
         ++this.count;
      }

      public String getName() {
         return this.name;
      }

      public String getString() {
         String var10000 = String.valueOf(class_124.field_1061);
         return var10000 + "EZ - " + String.valueOf(class_124.field_1070) + this.name + (this.count > 1 ? " [" + String.valueOf(class_124.field_1080) + "x" + this.count + String.valueOf(class_124.field_1070) + "]" : "");
      }
   }
}
