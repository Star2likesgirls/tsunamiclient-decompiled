package tsunami.features.hud.impl;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_124;
import net.minecraft.class_1657;
import net.minecraft.class_2596;
import net.minecraft.class_2663;
import net.minecraft.class_332;
import tsunami.events.impl.PacketEvent;
import tsunami.features.hud.HudElement;
import tsunami.features.modules.client.HudEditor;
import tsunami.features.modules.combat.Aura;
import tsunami.features.modules.combat.AutoCrystal;
import tsunami.gui.font.FontRenderers;
import tsunami.setting.impl.ColorSetting;
import tsunami.utility.math.MathUtility;
import tsunami.utility.render.Render2DEngine;
import tsunami.utility.render.TextureStorage;

public class KillStats extends HudElement {
   int death = 0;
   int killstreak = 0;
   int kills = 0;

   public KillStats() {
      super("KillStats", 100, 35);
   }

   public void onDisable() {
      this.death = 0;
      this.kills = 0;
      this.killstreak = 0;
   }

   @EventHandler
   private void death(PacketEvent.Receive event) {
      class_2596 var3 = event.getPacket();
      if (var3 instanceof class_2663) {
         class_2663 pac = (class_2663)var3;
         if (pac.method_11470() == 3) {
            if (!(pac.method_11469(mc.field_1687) instanceof class_1657)) {
               return;
            }

            if (pac.method_11469(mc.field_1687) == mc.field_1724) {
               ++this.death;
               this.killstreak = 0;
            } else if (Aura.target == pac.method_11469(mc.field_1687) || AutoCrystal.target == pac.method_11469(mc.field_1687)) {
               ++this.killstreak;
               ++this.kills;
            }
         }
      }

   }

   public void onRender2D(class_332 context) {
      super.onRender2D(context);
      String var10000 = String.valueOf(class_124.field_1068);
      String streak = "KillStreak: " + var10000 + this.killstreak;
      var10000 = String.valueOf(class_124.field_1068);
      String kd = " KD: " + var10000 + MathUtility.round((float)this.kills / (float)(this.death > 0 ? this.death : 1));
      float pX = this.getPosX() > (float)mc.method_22683().method_4486() / 2.0F ? this.getPosX() - FontRenderers.getModulesRenderer().getStringWidth(streak) - FontRenderers.getModulesRenderer().getStringWidth(kd) : this.getPosX();
      if (HudEditor.hudStyle.is(HudEditor.HudStyle.Blurry)) {
         Render2DEngine.drawRoundedBlur(context.method_51448(), pX, this.getPosY(), FontRenderers.getModulesRenderer().getStringWidth(streak) + FontRenderers.getModulesRenderer().getStringWidth(kd) + 21.0F, 13.0F, 3.0F, ((ColorSetting)HudEditor.blurColor.getValue()).getColorObject());
         Render2DEngine.drawRect(context.method_51448(), pX + 14.0F, this.getPosY() + 2.0F, 0.5F, 8.0F, new Color(1157627903, true));
         Render2DEngine.setupRender();
         RenderSystem.setShaderTexture(0, TextureStorage.swordIcon);
         Render2DEngine.renderGradientTexture(context.method_51448(), (double)(pX + 2.0F), (double)(this.getPosY() + 1.0F), 10.0D, 10.0D, 0.0F, 0.0F, 16.0D, 16.0D, 16.0D, 16.0D, HudEditor.getColor(270), HudEditor.getColor(0), HudEditor.getColor(180), HudEditor.getColor(90));
         Render2DEngine.endRender();
      }

      FontRenderers.getModulesRenderer().drawString(context.method_51448(), streak, (double)(pX + 18.0F), (double)(this.getPosY() + 5.0F), HudEditor.getColor(1).getRGB());
      FontRenderers.getModulesRenderer().drawString(context.method_51448(), kd, (double)(pX + 18.0F + FontRenderers.getModulesRenderer().getStringWidth(streak)), (double)(this.getPosY() + 5.0F), HudEditor.getColor(1).getRGB());
      this.setBounds(pX, this.getPosY(), FontRenderers.getModulesRenderer().getStringWidth(streak) + FontRenderers.getModulesRenderer().getStringWidth(kd) + 21.0F, 13.0F);
   }
}
