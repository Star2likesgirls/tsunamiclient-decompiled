package tsunami.features.hud.impl;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.class_1291;
import net.minecraft.class_1293;
import net.minecraft.class_1294;
import net.minecraft.class_1309;
import net.minecraft.class_1657;
import net.minecraft.class_1799;
import net.minecraft.class_266;
import net.minecraft.class_2960;
import net.minecraft.class_332;
import net.minecraft.class_408;
import net.minecraft.class_4587;
import net.minecraft.class_5250;
import net.minecraft.class_742;
import net.minecraft.class_757;
import net.minecraft.class_8646;
import net.minecraft.class_9013;
import net.minecraft.class_9025;
import tsunami.core.manager.client.ModuleManager;
import tsunami.features.hud.HudElement;
import tsunami.features.modules.client.HudEditor;
import tsunami.features.modules.combat.Aura;
import tsunami.features.modules.combat.AutoAnchor;
import tsunami.features.modules.combat.AutoCrystal;
import tsunami.features.modules.misc.NameProtect;
import tsunami.gui.font.FontRenderer;
import tsunami.gui.font.FontRenderers;
import tsunami.gui.hud.HudEditorGui;
import tsunami.setting.Setting;
import tsunami.setting.impl.ColorSetting;
import tsunami.utility.ThunderUtility;
import tsunami.utility.Timer;
import tsunami.utility.math.MathUtility;
import tsunami.utility.render.Render2DEngine;
import tsunami.utility.render.Render3DEngine;
import tsunami.utility.render.TextureStorage;
import tsunami.utility.render.animation.EaseOutBack;
import tsunami.utility.render.animation.EaseOutCirc;

public class TargetHud extends HudElement {
   private final Setting<Integer> blurRadius = new Setting("BallonBlur", 10, 1, 10);
   private final Setting<Integer> animX = new Setting("AnimationX", 0, -2000, 2000);
   private final Setting<Integer> animY = new Setting("AnimationY", 0, -2000, 2000);
   private final Setting<TargetHud.HPmodeEn> hpMode;
   private final Setting<TargetHud.ImageModeEn> imageMode;
   private final Setting<TargetHud.ModeEn> Mode;
   private final Setting<ColorSetting> color;
   private final Setting<ColorSetting> color2;
   private final Setting<Boolean> funTimeHP;
   private final Setting<Boolean> mini;
   private final Setting<Boolean> absorp;
   private static class_2960 custom;
   public EaseOutBack animation;
   public static EaseOutCirc healthAnimation = new EaseOutCirc();
   public static EaseOutCirc headAnimation = new EaseOutCirc();
   private final ArrayList<Particles> particles;
   private boolean sentParticles;
   private boolean direction;
   private class_1309 target;
   float ticks;
   private final Timer timer;

   public TargetHud() {
      super("TargetHud", 150, 50);
      this.hpMode = new Setting("HP Mode", TargetHud.HPmodeEn.HP);
      this.imageMode = new Setting("Image", TargetHud.ImageModeEn.Anime);
      this.Mode = new Setting("Mode", TargetHud.ModeEn.ThunderHack);
      this.color = new Setting("Color1", new ColorSetting(-16492289), (v) -> {
         return this.Mode.getValue() == TargetHud.ModeEn.CelkaPasta;
      });
      this.color2 = new Setting("Color2", new ColorSetting(-16492289), (v) -> {
         return this.Mode.getValue() == TargetHud.ModeEn.CelkaPasta;
      });
      this.funTimeHP = new Setting("FunTimeHP", false);
      this.mini = new Setting("Mini", false, (v) -> {
         return this.Mode.getValue() == TargetHud.ModeEn.NurikZapen;
      });
      this.absorp = new Setting("Absorption", true);
      this.animation = new EaseOutBack();
      this.particles = new ArrayList();
      this.direction = false;
      this.timer = new Timer();
   }

   public void onEnable() {
      try {
         custom = ThunderUtility.getCustomImg("thud");
      } catch (Exception var2) {
      }

   }

   public String getDurationString(class_1293 pe) {
      if (pe.method_48559()) {
         return "*:*";
      } else {
         int var10000 = pe.method_5584() / 1200;
         return var10000 + ":" + pe.method_5584() % 1200 / 20;
      }
   }

   public void onUpdate() {
      this.animation.update(this.direction);
      healthAnimation.update();
      headAnimation.update();
   }

   public void onRender2D(class_332 context) {
      super.onRender2D(context);
      this.getTarget();
      if (this.target == null) {
         this.direction = false;
      } else if (!(this.animation.getAnimationd() <= 0.02D)) {
         float health = Math.min(this.target.method_6063(), this.getHealth());
         context.method_51448().method_22903();
         if (!HudEditor.hudStyle.is(HudEditor.HudStyle.Blurry)) {
            if (this.Mode.is(TargetHud.ModeEn.NurikZapen) && (Boolean)this.mini.getValue()) {
               sizeAnimation(context.method_51448(), (double)(this.getPosX() + 45.0F + (float)(Integer)this.animX.getValue()), (double)(this.getPosY() + 15.0F + (float)(Integer)this.animY.getValue()), this.animation.getAnimationd());
            } else {
               sizeAnimation(context.method_51448(), (double)(this.getPosX() + 75.0F + (float)(Integer)this.animX.getValue()), (double)(this.getPosY() + 25.0F + (float)(Integer)this.animY.getValue()), this.animation.getAnimationd());
            }
         }

         float animationFactor = (float)MathUtility.clamp(this.animation.getAnimationd(), 0.0D, 1.0D);
         switch(((TargetHud.ModeEn)this.Mode.getValue()).ordinal()) {
         case 0:
            this.renderThunderHack(context, health, animationFactor);
            break;
         case 1:
            if ((Boolean)this.mini.getValue()) {
               this.renderMiniNurik(context, health, animationFactor);
            } else {
               this.renderNurik(context, health, animationFactor);
            }
            break;
         case 2:
            this.renderCelkaPasta(context, health);
         }

         context.method_51448().method_22909();
      }
   }

   private void getTarget() {
      if (AutoCrystal.target != null && !AutoCrystal.target.method_29504()) {
         this.target = AutoCrystal.target;
         this.direction = true;
      } else if (Aura.target != null && Aura.target instanceof class_1309 && !((class_1309)Aura.target).method_29504()) {
         this.target = (class_1309)Aura.target;
         this.direction = true;
      } else if (AutoAnchor.target != null && !AutoAnchor.target.method_29504()) {
         this.target = AutoAnchor.target;
         this.direction = true;
      } else if (mc.field_1692 != null && mc.field_1692 instanceof class_1309 && !((class_1309)mc.field_1692).method_29504()) {
         this.target = (class_1309)mc.field_1692;
         this.direction = true;
      } else if (!(mc.field_1755 instanceof class_408) && !(mc.field_1755 instanceof HudEditorGui)) {
         this.direction = false;
         if (this.animation.getAnimationd() < 0.02D) {
            this.target = null;
         }
      } else {
         this.target = mc.field_1724;
         this.direction = true;
      }

      if (this.target != null && this.target.method_29504() && !(mc.field_1755 instanceof class_408) && !(mc.field_1755 instanceof HudEditorGui)) {
         this.direction = false;
      }

   }

   private void renderCelkaPasta(class_332 context, float health) {
      float hurtPercent = (float)this.target.field_6235 / 6.0F;
      Render2DEngine.drawBlurredShadow(context.method_51448(), this.getPosX() - 2.0F, this.getPosY() - 2.0F, 164.0F, 51.0F, 5, ((ColorSetting)this.color.getValue()).getColorObject());
      Render2DEngine.drawRect(context.method_51448(), this.getPosX(), this.getPosY(), 160.0F, 47.0F, new Color(1711276032, true));
      this.setBounds(this.getPosX(), this.getPosY(), 160.0F, 47.0F);
      Render2DEngine.drawRect(context.method_51448(), this.getPosX() + 117.0F, this.getPosY() + 4.0F, 18.0F, 18.0F, new Color(1291845632, true));
      Render2DEngine.drawRect(context.method_51448(), this.getPosX() + 137.0F, this.getPosY() + 4.0F, 18.0F, 18.0F, new Color(1291845632, true));
      Render2DEngine.drawRect(context.method_51448(), this.getPosX() + 117.0F, this.getPosY() + 25.0F, 18.0F, 18.0F, new Color(1291845632, true));
      Render2DEngine.drawRect(context.method_51448(), this.getPosX() + 137.0F, this.getPosY() + 25.0F, 18.0F, 18.0F, new Color(1291845632, true));
      Render2DEngine.drawBlurredShadow(context.method_51448(), this.getPosX() + 49.0F, this.getPosY() + 29.0F, 62.0F, 12.0F, 5, ((ColorSetting)this.color.getValue()).getColorObject().brighter().brighter().brighter());
      Render2DEngine.drawRect(context.method_51448(), this.getPosX() + 50.0F, this.getPosY() + 30.0F, 60.0F, 10.0F, new Color(-1644167168, true));
      Render2DEngine.drawRect(context.method_51448(), this.getPosX() + 50.0F, this.getPosY() + 30.0F, (float)MathUtility.clamp((int)(60.0F * (health / this.target.method_6063())), 0, 60), 10.0F, ((ColorSetting)this.color.getValue()).getColorObject().brighter().brighter().brighter());
      if (this.target instanceof class_1657) {
         RenderSystem.setShaderTexture(0, ((class_742)this.target).method_52814().comp_1626());
      } else {
         RenderSystem.setShaderTexture(0, mc.method_1561().method_3953(this.target).method_3931(this.target));
      }

      RenderSystem.setShaderColor(1.0F, 1.0F - hurtPercent, 1.0F - hurtPercent, 1.0F);
      Render2DEngine.renderTexture(context.method_51448(), (double)(this.getPosX() + 3.5F + hurtPercent), (double)(this.getPosY() + 3.5F + hurtPercent), (double)(40.0F - hurtPercent * 2.0F), (double)(40.0F - hurtPercent * 2.0F), 8.0F, 8.0F, 8.0D, 8.0D, 64.0D, 64.0D);
      Render2DEngine.renderTexture(context.method_51448(), (double)(this.getPosX() + 3.5F + hurtPercent), (double)(this.getPosY() + 3.5F + hurtPercent), (double)(40.0F - hurtPercent * 2.0F), (double)(40.0F - hurtPercent * 2.0F), 40.0F, 8.0F, 8.0D, 8.0D, 64.0D, 64.0D);
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      FontRenderers.modules.drawString(context.method_51448(), ModuleManager.media.isEnabled() ? "Protected " : (ModuleManager.nameProtect.isEnabled() && this.target == mc.field_1724 ? NameProtect.getCustomName() : this.target.method_5477().getString()), (double)(this.getPosX() + 50.0F), (double)(this.getPosY() + 7.0F), -1);
      FontRenderers.modules.drawCenteredString(context.method_51448(), this.hpMode.getValue() == TargetHud.HPmodeEn.HP ? String.valueOf((double)Math.round(10.0D * (double)this.getHealth()) / 10.0D) : (double)Math.round(10.0D * (double)this.getHealth()) / 10.0D / 20.0D * 100.0D + "%", (double)(this.getPosX() + 81.0F), (double)(this.getPosY() + 34.0F), -1);
      class_1309 var5 = this.target;
      if (var5 instanceof class_1657) {
         class_1657 pe = (class_1657)var5;
         this.celestialArmor(context, pe, this.getPosX(), this.getPosY());
         this.celestialHands(context, pe, this.getPosX(), this.getPosY());
      }

      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
   }

   private void renderNurik(class_332 context, float health, float animationFactor) {
      float hurtPercent = Render2DEngine.interpolateFloat((float)MathUtility.clamp(this.target.field_6235 == 0 ? 0 : this.target.field_6235 + 1, 0, 10), (float)this.target.field_6235, (double)Render3DEngine.getTickDelta()) / 8.0F;
      healthAnimation.setValue((double)health);
      health = (float)healthAnimation.getAnimationD();
      if (this.animation.getAnimationd() != 1.0D && !HudEditor.hudStyle.is(HudEditor.HudStyle.Blurry)) {
         Render2DEngine.drawGradientBlurredShadow1(context.method_51448(), this.getPosX() + 4.0F, this.getPosY() + 4.0F, 131.0F, 40.0F, 14, HudEditor.getColor(270), HudEditor.getColor(0), HudEditor.getColor(180), HudEditor.getColor(90));
         Render2DEngine.renderRoundedGradientRect(context.method_51448(), HudEditor.getColor(270), HudEditor.getColor(0), HudEditor.getColor(180), HudEditor.getColor(90), this.getPosX(), this.getPosY(), 137.0F, 47.5F, 9.0F);
         Render2DEngine.drawRound(context.method_51448(), this.getPosX() + 0.5F, this.getPosY() + 0.5F, 136.0F, 46.0F, 9.0F, Render2DEngine.injectAlpha(Color.BLACK, 220));
      } else {
         Render2DEngine.drawHudBase2(context.method_51448(), this.getPosX(), this.getPosY(), 137.0F, 47.5F, 9.0F, (Float)HudEditor.blurStrength.getValue(), (Float)HudEditor.blurOpacity.getValue(), animationFactor);
      }

      this.setBounds(this.getPosX(), this.getPosY(), 137.0F, 47.5F);
      if (this.target instanceof class_1657) {
         RenderSystem.setShaderTexture(0, ((class_742)this.target).method_52814().comp_1626());
      } else {
         RenderSystem.setShaderTexture(0, mc.method_1561().method_3953(this.target).method_3931(this.target));
      }

      context.method_51448().method_22903();
      context.method_51448().method_46416(this.getPosX() + 3.5F + 20.0F, this.getPosY() + 3.5F + 20.0F, 0.0F);
      context.method_51448().method_22905(1.0F - hurtPercent / 15.0F, 1.0F - hurtPercent / 15.0F, 1.0F);
      context.method_51448().method_46416(-(this.getPosX() + 3.5F + 20.0F), -(this.getPosY() + 3.5F + 20.0F), 0.0F);
      RenderSystem.enableBlend();
      RenderSystem.colorMask(false, false, false, true);
      RenderSystem.clearColor(0.0F, 0.0F, 0.0F, 0.0F);
      RenderSystem.clear(16384, false);
      RenderSystem.colorMask(true, true, true, true);
      RenderSystem.setShader(class_757::method_34540);
      Render2DEngine.renderRoundedQuadInternal(context.method_51448().method_23760().method_23761(), animationFactor, animationFactor, animationFactor, animationFactor, (double)(this.getPosX() + 3.5F), (double)(this.getPosY() + 3.5F), (double)(this.getPosX() + 3.5F + 40.0F), (double)(this.getPosY() + 3.5F + 40.0F), 7.0D, 10.0D);
      RenderSystem.blendFunc(772, 773);
      RenderSystem.setShaderColor(animationFactor, animationFactor - hurtPercent / 2.0F, animationFactor - hurtPercent / 2.0F, (float)MathUtility.clamp(this.animation.getAnimationd(), 0.0D, 1.0D));
      Render2DEngine.renderTexture(context.method_51448(), (double)(this.getPosX() + 3.5F), (double)(this.getPosY() + 3.5F), 40.0D, 40.0D, 8.0F, 8.0F, 8.0D, 8.0D, 64.0D, 64.0D);
      Render2DEngine.renderTexture(context.method_51448(), (double)(this.getPosX() + 3.5F), (double)(this.getPosY() + 3.5F), 40.0D, 40.0D, 40.0F, 8.0F, 8.0D, 8.0D, 64.0D, 64.0D);
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      RenderSystem.defaultBlendFunc();
      context.method_51448().method_22909();
      if (HudEditor.hudStyle.is(HudEditor.HudStyle.Blurry)) {
         Render2DEngine.drawRect(context.method_51448(), this.getPosX() + 48.0F, this.getPosY() + 32.0F, 85.0F, 11.0F, 4.0F, (float)(0.15000000596046448D * this.animation.getAnimationd()));
         Render2DEngine.drawRect(context.method_51448(), this.getPosX() + 48.0F, this.getPosY() + 32.0F, MathUtility.clamp(85.0F * (health / this.target.method_6063()), 8.0F, 85.0F), 11.0F, 4.0F, (float)this.animation.getAnimationd());
      } else {
         Render2DEngine.drawGradientRound(context.method_51448(), this.getPosX() + 48.0F, this.getPosY() + 32.0F, 85.0F, 11.0F, 4.0F, HudEditor.getColor(0).darker().darker(), HudEditor.getColor(0).darker().darker().darker().darker(), HudEditor.getColor(0).darker().darker().darker().darker(), HudEditor.getColor(0).darker().darker().darker().darker());
         Render2DEngine.renderRoundedGradientRect(context.method_51448(), HudEditor.getColor(270), HudEditor.getColor(0), HudEditor.getColor(0), HudEditor.getColor(270), this.getPosX() + 48.0F, this.getPosY() + 32.0F, (float)((int)MathUtility.clamp(85.0F * (health / this.target.method_6063()), 8.0F, 85.0F)), 11.0F, 4.0F);
      }

      FontRenderers.sf_bold.drawCenteredString(context.method_51448(), this.hpMode.getValue() == TargetHud.HPmodeEn.HP ? String.valueOf((double)Math.round(10.0D * (double)this.getHealth()) / 10.0D) : (double)Math.round(10.0D * (double)this.getHealth()) / 10.0D / 20.0D * 100.0D + "%", (double)(this.getPosX() + 92.0F), (double)(this.getPosY() + 35.0F), Render2DEngine.applyOpacity(-1, animationFactor));
      FontRenderers.sf_bold.drawString(context.method_51448(), ModuleManager.media.isEnabled() ? "Protected " : (ModuleManager.nameProtect.isEnabled() && this.target == mc.field_1724 ? NameProtect.getCustomName() : this.target.method_5477().getString()), (double)(this.getPosX() + 48.0F), (double)(this.getPosY() + 7.0F), Render2DEngine.applyOpacity(-1, animationFactor));
      if (this.target instanceof class_1657) {
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, (float)MathUtility.clamp(this.animation.getAnimationd(), 0.0D, 1.0D));
         List<class_1799> armor = ((class_1657)this.target).method_31548().field_7548;
         class_1799[] items = new class_1799[]{this.target.method_6047(), (class_1799)armor.get(3), (class_1799)armor.get(2), (class_1799)armor.get(1), (class_1799)armor.get(0), this.target.method_6079()};
         float xItemOffset = this.getPosX() + 48.0F;
         class_1799[] var8 = items;
         int var9 = items.length;

         for(int var10 = 0; var10 < var9; ++var10) {
            class_1799 itemStack = var8[var10];
            context.method_51448().method_22903();
            context.method_51448().method_46416(xItemOffset, this.getPosY() + 15.0F, 0.0F);
            context.method_51448().method_22905(0.75F, 0.75F, 0.75F);
            context.method_51427(itemStack, 0, 0);
            context.method_51431(mc.field_1772, itemStack, 0, 0);
            context.method_51448().method_22909();
            xItemOffset += 12.0F;
         }

         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      }

   }

   private void renderMiniNurik(class_332 context, float health, float animationFactor) {
      float hurtPercent = Render2DEngine.interpolateFloat((float)MathUtility.clamp(this.target.field_6235 == 0 ? 0 : this.target.field_6235 + 1, 0, 10), (float)this.target.field_6235, (double)Render3DEngine.getTickDelta()) / 8.0F;
      healthAnimation.setValue((double)health);
      health = (float)healthAnimation.getAnimationD();
      if (this.animation.getAnimationd() != 1.0D && !HudEditor.hudStyle.is(HudEditor.HudStyle.Blurry)) {
         Render2DEngine.drawGradientBlurredShadow1(context.method_51448(), this.getPosX() + 2.0F, this.getPosY() + 2.0F, 91.0F, 31.0F, 12, HudEditor.getColor(270), HudEditor.getColor(0), HudEditor.getColor(180), HudEditor.getColor(90));
         Render2DEngine.renderRoundedGradientRect(context.method_51448(), HudEditor.getColor(270), HudEditor.getColor(0), HudEditor.getColor(180), HudEditor.getColor(90), this.getPosX(), this.getPosY(), 95.0F, 35.0F, 7.0F);
         Render2DEngine.drawRound(context.method_51448(), this.getPosX() + 0.5F, this.getPosY() + 0.5F, 94.0F, 34.0F, 7.0F, Render2DEngine.injectAlpha(Color.BLACK, 220));
      } else {
         Render2DEngine.drawHudBase2(context.method_51448(), this.getPosX(), this.getPosY(), 95.0F, 35.5F, 8.0F, (Float)HudEditor.blurStrength.getValue(), (Float)HudEditor.blurOpacity.getValue(), animationFactor);
      }

      this.setBounds(this.getPosX(), this.getPosY(), 95.0F, 35.5F);
      if (this.target instanceof class_1657) {
         RenderSystem.setShaderTexture(0, ((class_742)this.target).method_52814().comp_1626());
      } else {
         RenderSystem.setShaderTexture(0, mc.method_1561().method_3953(this.target).method_3931(this.target));
      }

      context.method_51448().method_22903();
      context.method_51448().method_22904((double)this.getPosX() + 2.5D + 15.0D, (double)this.getPosY() + 2.5D + 15.0D, 0.0D);
      context.method_51448().method_22905(1.0F - hurtPercent / 20.0F, 1.0F - hurtPercent / 20.0F, 1.0F);
      context.method_51448().method_22904(-((double)this.getPosX() + 2.5D + 15.0D), -((double)this.getPosY() + 2.5D + 15.0D), 0.0D);
      RenderSystem.enableBlend();
      RenderSystem.colorMask(false, false, false, true);
      RenderSystem.clearColor(0.0F, 0.0F, 0.0F, 0.0F);
      RenderSystem.clear(16384, false);
      RenderSystem.colorMask(true, true, true, true);
      RenderSystem.setShader(class_757::method_34540);
      Render2DEngine.renderRoundedQuadInternal(context.method_51448().method_23760().method_23761(), animationFactor, animationFactor, animationFactor, animationFactor, (double)this.getPosX() + 2.5D, (double)this.getPosY() + 2.5D, (double)this.getPosX() + 2.5D + 30.0D, (double)this.getPosY() + 2.5D + 30.0D, 5.0D, 10.0D);
      RenderSystem.blendFunc(772, 773);
      RenderSystem.setShaderColor(animationFactor, animationFactor - hurtPercent / 2.0F, animationFactor - hurtPercent / 2.0F, animationFactor);
      Render2DEngine.renderTexture(context.method_51448(), (double)this.getPosX() + 2.5D, (double)this.getPosY() + 2.5D, 30.0D, 30.0D, 8.0F, 8.0F, 8.0D, 8.0D, 64.0D, 64.0D);
      Render2DEngine.renderTexture(context.method_51448(), (double)this.getPosX() + 2.5D, (double)this.getPosY() + 2.5D, 30.0D, 30.0D, 40.0F, 8.0F, 8.0D, 8.0D, 64.0D, 64.0D);
      RenderSystem.defaultBlendFunc();
      context.method_51448().method_22909();
      if (HudEditor.hudStyle.is(HudEditor.HudStyle.Blurry)) {
         Render2DEngine.drawRect(context.method_51448(), this.getPosX() + 38.0F, this.getPosY() + 25.0F, 52.0F, 7.0F, 2.0F, (float)(0.15000000596046448D * this.animation.getAnimationd()));
         Render2DEngine.drawRect(context.method_51448(), this.getPosX() + 38.0F, this.getPosY() + 25.0F, MathUtility.clamp(52.0F * (health / this.target.method_6063()), 8.0F, 52.0F), 7.0F, 2.0F, (float)this.animation.getAnimationd());
      } else {
         Render2DEngine.drawGradientRound(context.method_51448(), this.getPosX() + 38.0F, this.getPosY() + 25.0F, 52.0F, 7.0F, 2.0F, HudEditor.getColor(0).darker().darker(), HudEditor.getColor(0).darker().darker().darker().darker(), HudEditor.getColor(0).darker().darker().darker().darker(), HudEditor.getColor(0).darker().darker().darker().darker());
         Render2DEngine.renderRoundedGradientRect(context.method_51448(), HudEditor.getColor(270), HudEditor.getColor(0), HudEditor.getColor(0), HudEditor.getColor(270), this.getPosX() + 38.0F, this.getPosY() + 25.0F, (float)((int)MathUtility.clamp(52.0F * (health / this.target.method_6063()), 8.0F, 52.0F)), 7.0F, 2.0F);
      }

      FontRenderers.sf_bold_mini.drawCenteredString(context.method_51448(), this.hpMode.getValue() == TargetHud.HPmodeEn.HP ? String.valueOf((double)Math.round(10.0D * (double)this.getHealth()) / 10.0D) : (double)Math.round(10.0D * (double)this.getHealth()) / 10.0D / 20.0D * 100.0D + "%", (double)(this.getPosX() + 65.0F), (double)(this.getPosY() + 27.0F), Render2DEngine.applyOpacity(-1, animationFactor));
      FontRenderers.sf_bold_mini.drawString(context.method_51448(), ModuleManager.media.isEnabled() ? "Protected " : (ModuleManager.nameProtect.isEnabled() && this.target == mc.field_1724 ? NameProtect.getCustomName() : this.target.method_5477().getString()), (double)(this.getPosX() + 38.0F), (double)(this.getPosY() + 5.0F), Render2DEngine.applyOpacity(-1, animationFactor));
      if (this.target instanceof class_1657) {
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, (float)MathUtility.clamp(this.animation.getAnimationd(), 0.0D, 1.0D));
         List<class_1799> armor = ((class_1657)this.target).method_31548().field_7548;
         class_1799[] items = new class_1799[]{this.target.method_6047(), (class_1799)armor.get(3), (class_1799)armor.get(2), (class_1799)armor.get(1), (class_1799)armor.get(0), this.target.method_6079()};
         float xItemOffset = this.getPosX() + 38.0F;
         class_1799[] var8 = items;
         int var9 = items.length;

         for(int var10 = 0; var10 < var9; ++var10) {
            class_1799 itemStack = var8[var10];
            context.method_51448().method_22903();
            context.method_51448().method_46416(xItemOffset, this.getPosY() + 13.0F, 0.0F);
            context.method_51448().method_22905(0.5F, 0.5F, 0.5F);
            context.method_51427(itemStack, 0, 0);
            context.method_51431(mc.field_1772, itemStack, 0, 0);
            context.method_51448().method_22909();
            xItemOffset += 9.0F;
         }

         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      }

   }

   private void renderThunderHack(class_332 context, float health, float animationFactor) {
      float hurtPercent = (float)this.target.field_6235 / 6.0F;
      Render2DEngine.drawRound(context.method_51448(), this.getPosX(), this.getPosY(), 70.0F, 50.0F, 6.0F, new Color(0, 0, 0, 139));
      Render2DEngine.drawRound(context.method_51448(), this.getPosX() + 50.0F, this.getPosY(), 100.0F, 50.0F, 6.0F, new Color(0, 0, 0, 255));
      this.setBounds(this.getPosX(), this.getPosY(), 150.0F, 50.0F);
      if (!this.imageMode.is(TargetHud.ImageModeEn.None)) {
         label124: {
            if (this.imageMode.is(TargetHud.ImageModeEn.Anime)) {
               RenderSystem.setShaderTexture(0, TextureStorage.thudPic);
            } else {
               if (custom == null) {
                  break label124;
               }

               RenderSystem.setShaderTexture(0, custom);
            }

            context.method_51448().method_22903();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            Render2DEngine.drawRound(context.method_51448(), this.getPosX() + 50.0F, this.getPosY(), 100.0F, 50.0F, 12.0F, new Color(0, 0, 0, 255));
            RenderSystem.disableBlend();
            RenderSystem.setShaderColor(0.3F, 0.3F, 0.3F, 1.0F);
            Render2DEngine.renderTexture(context.method_51448(), (double)(this.getPosX() + 50.0F), (double)this.getPosY(), 95.0D, 50.0D, 0.0F, 0.0F, 100.0D, 50.0D, 100.0D, 50.0D);
            context.method_51448().method_22909();
         }
      }

      Iterator var5 = this.particles.iterator();

      Particles p;
      while(var5.hasNext()) {
         p = (Particles)var5.next();
         if (p.opacity > 4.0D) {
            p.render2D(context.method_51448());
         }
      }

      if (this.timer.passedMs(16L)) {
         this.ticks += 0.1F;
         var5 = this.particles.iterator();

         while(var5.hasNext()) {
            p = (Particles)var5.next();
            p.updatePosition();
            if (p.opacity < 1.0D) {
               this.particles.remove(p);
            }
         }

         this.timer.reset();
      }

      ArrayList<Particles> removeList = new ArrayList();
      Iterator var15 = this.particles.iterator();

      Particles p;
      while(var15.hasNext()) {
         p = (Particles)var15.next();
         if (p.opacity <= 1.0D) {
            removeList.add(p);
         }
      }

      var15 = removeList.iterator();

      while(var15.hasNext()) {
         p = (Particles)var15.next();
         this.particles.remove(p);
      }

      if (this.target.field_6235 == 9 && !this.sentParticles) {
         for(int i = 0; i <= 6; ++i) {
            p = new Particles();
            Color c = Particles.mixColors(((ColorSetting)this.color.getValue()).getColorObject(), ((ColorSetting)this.color2.getValue()).getColorObject(), (Math.sin((double)(this.ticks + this.getPosX() * 0.4F + (float)i)) + 1.0D) * 0.5D);
            p.init((double)this.getPosX(), (double)this.getPosY(), (double)MathUtility.random(-3.0F, 3.0F), (double)MathUtility.random(-3.0F, 3.0F), 20.0D, c);
            this.particles.add(p);
         }

         this.sentParticles = true;
      }

      if (this.target.field_6235 == 8) {
         this.sentParticles = false;
      }

      headAnimation.setValue((double)hurtPercent);
      if (this.target instanceof class_1657) {
         RenderSystem.setShaderTexture(0, ((class_742)this.target).method_52814().comp_1626());
      } else {
         RenderSystem.setShaderTexture(0, mc.method_1561().method_3953(this.target).method_3931(this.target));
      }

      context.method_51448().method_22903();
      context.method_51448().method_22904((double)this.getPosX() + 2.5D + 15.0D, (double)this.getPosY() + 2.5D + 15.0D, 0.0D);
      context.method_51448().method_22905(1.0F - hurtPercent / 20.0F, 1.0F - hurtPercent / 20.0F, 1.0F);
      context.method_51448().method_22904(-((double)this.getPosX() + 2.5D + 15.0D), -((double)this.getPosY() + 2.5D + 15.0D), 0.0D);
      RenderSystem.enableBlend();
      RenderSystem.colorMask(false, false, false, true);
      RenderSystem.clearColor(0.0F, 0.0F, 0.0F, 0.0F);
      RenderSystem.clear(16384, false);
      RenderSystem.colorMask(true, true, true, true);
      RenderSystem.setShader(class_757::method_34540);
      Render2DEngine.renderRoundedQuadInternal(context.method_51448().method_23760().method_23761(), animationFactor, animationFactor, animationFactor, animationFactor, (double)this.getPosX() + 2.5D, (double)this.getPosY() + 2.5D, (double)this.getPosX() + 2.5D + 45.0D, (double)this.getPosY() + 2.5D + 45.0D, 5.0D, 10.0D);
      RenderSystem.blendFunc(772, 773);
      RenderSystem.setShaderColor(animationFactor, animationFactor - hurtPercent / 2.0F, animationFactor - hurtPercent / 2.0F, animationFactor);
      Render2DEngine.renderTexture(context.method_51448(), (double)this.getPosX() + 2.5D, (double)this.getPosY() + 2.5D, 45.0D, 45.0D, 8.0F, 8.0F, 8.0D, 8.0D, 64.0D, 64.0D);
      Render2DEngine.renderTexture(context.method_51448(), (double)this.getPosX() + 2.5D, (double)this.getPosY() + 2.5D, 45.0D, 45.0D, 40.0F, 8.0F, 8.0D, 8.0D, 64.0D, 64.0D);
      RenderSystem.defaultBlendFunc();
      context.method_51448().method_22909();
      healthAnimation.setValue((double)health);
      health = (float)healthAnimation.getAnimationD();
      Render2DEngine.drawBlurredShadow(context.method_51448(), this.getPosX() + 55.0F, this.getPosY() + 22.0F, 90.0F, 8.0F, (Integer)this.blurRadius.getValue(), HudEditor.getColor(0));
      Render2DEngine.drawGradientRound(context.method_51448(), this.getPosX() + 55.0F, this.getPosY() + 35.0F - 14.0F, 90.0F, 10.0F, 2.0F, HudEditor.getColor(0).darker().darker(), HudEditor.getColor(0).darker().darker().darker().darker(), HudEditor.getColor(0).darker().darker().darker().darker(), HudEditor.getColor(0).darker().darker().darker().darker());
      Render2DEngine.renderRoundedGradientRect(context.method_51448(), HudEditor.getColor(270), HudEditor.getColor(0), HudEditor.getColor(0), HudEditor.getColor(270), this.getPosX() + 55.0F, this.getPosY() + 35.0F - 14.0F, (float)((int)MathUtility.clamp(90.0F * (health / this.target.method_6063()), 3.0F, 90.0F)), 10.0F, 2.0F);
      FontRenderers.sf_bold.drawCenteredString(context.method_51448(), this.hpMode.getValue() == TargetHud.HPmodeEn.HP ? String.valueOf((double)Math.round(10.0D * (double)this.getHealth()) / 10.0D) : (int)((double)Math.round(10.0D * (double)health) / 10.0D / 20.0D * 100.0D) + "%", (double)(this.getPosX() + 102.0F), (double)(this.getPosY() + 24.0F), Render2DEngine.applyOpacity(-1, animationFactor));
      FontRenderers.sf_bold.drawString(context.method_51448(), ModuleManager.media.isEnabled() ? "Protected " : (ModuleManager.nameProtect.isEnabled() && this.target == mc.field_1724 ? NameProtect.getCustomName() : this.target.method_5477().getString()), (double)(this.getPosX() + 55.0F), (double)(this.getPosY() + 5.0F), Render2DEngine.applyOpacity(-1, animationFactor));
      if (this.target instanceof class_1657) {
         List<class_1799> armor = ((class_1657)this.target).method_31548().field_7548;
         class_1799[] items = new class_1799[]{this.target.method_6047(), (class_1799)armor.get(3), (class_1799)armor.get(2), (class_1799)armor.get(1), (class_1799)armor.get(0), this.target.method_6079()};
         float xItemOffset = this.getPosX() + 60.0F;
         class_1799[] var10 = items;
         int var11 = items.length;

         for(int var12 = 0; var12 < var11; ++var12) {
            class_1799 itemStack = var10[var12];
            if (!itemStack.method_7960()) {
               context.method_51448().method_22903();
               context.method_51448().method_46416(xItemOffset, this.getPosY() + 35.0F, 0.0F);
               context.method_51448().method_22905(0.75F, 0.75F, 0.75F);
               context.method_51427(itemStack, 0, 0);
               context.method_51431(mc.field_1772, itemStack, 0, 0);
               context.method_51448().method_22909();
               xItemOffset += 14.0F;
            }
         }

         this.drawPotionEffect(context.method_51448(), (class_1657)this.target);
      }

   }

   private void celestialArmor(class_332 context, class_1657 target, float posX, float posY) {
      for(int i = 0; i < 4; ++i) {
         if (!((class_1799)target.method_31548().field_7548.get(3 - i)).method_7960()) {
            context.method_51448().method_22903();
            context.method_51448().method_46416(posX + (float)(i > 1 ? 138 : 118), posY + (float)(i % 2 == 0 ? 5 : 26), 0.0F);
            context.method_51427((class_1799)target.method_31548().field_7548.get(3 - i), 0, 0);
            context.method_51431(mc.field_1772, (class_1799)target.method_31548().field_7548.get(3 - i), 0, 0);
            context.method_51448().method_22909();
         }
      }

   }

   private void celestialHands(class_332 context, class_1657 target, float posX, float posY) {
      for(int i = 0; i < 2; ++i) {
         if (!(i == 0 ? target.method_6047() : target.method_6079()).method_7960()) {
            context.method_51448().method_22903();
            context.method_51448().method_46416(posX + (float)(i == 0 ? 50 : 77), posY + 14.0F, 0.0F);
            context.method_51448().method_22905(0.75F, 0.75F, 1.0F);
            context.method_51427(i == 0 ? target.method_6047() : target.method_6079(), 0, 0);
            context.method_51448().method_22909();
            FontRenderer var10000 = FontRenderers.settings;
            class_4587 var10001 = context.method_51448();
            int var10002 = (i == 0 ? target.method_6047() : target.method_6079()).method_7947();
            var10000.drawString(var10001, "x" + var10002, (double)(posX + (float)(i == 0 ? 50 : 77) + 12.0F), (double)(posY + 21.0F), -1);
         }
      }

   }

   private void drawPotionEffect(class_4587 ms, class_1657 entity) {
      StringBuilder finalString = new StringBuilder();
      Iterator var4 = entity.method_6026().iterator();

      while(true) {
         class_1293 potionEffect;
         class_1291 potion;
         do {
            if (!var4.hasNext()) {
               FontRenderers.settings.drawString(ms, finalString.toString(), (double)(this.getPosX() + 55.0F), (double)(this.getPosY() + 15.0F), (new Color(9276813)).getRGB());
               return;
            }

            potionEffect = (class_1293)var4.next();
            potion = (class_1291)potionEffect.method_5579().comp_349();
         } while(potion != class_1294.field_5924.comp_349() && potion != class_1294.field_5904.comp_349() && potion != class_1294.field_5910.comp_349() && potion != class_1294.field_5911.comp_349());

         boolean potRanOut = (double)potionEffect.method_5584() != 0.0D;
         if (entity.method_6059(potionEffect.method_5579()) && potRanOut) {
            finalString.append(getPotionName(potion)).append(potionEffect.method_5578() < 1 ? "" : potionEffect.method_5578() + 1).append(" ").append(this.getDurationString(potionEffect)).append(" ");
         }
      }
   }

   public float getHealth() {
      class_1309 scoreBoard = this.target;
      if (scoreBoard instanceof class_1657) {
         class_1657 ent = (class_1657)scoreBoard;
         if (mc.method_1562() != null && mc.method_1562().method_45734() != null && mc.method_1562().method_45734().field_3761.contains("funtime") || (Boolean)this.funTimeHP.getValue()) {
            scoreBoard = null;
            String resolvedHp = "";
            if (ent.method_7327().method_1189(class_8646.field_45158) != null) {
               class_266 scoreBoard = ent.method_7327().method_1189(class_8646.field_45158);
               if (scoreBoard != null) {
                  class_9013 readableScoreboardScore = ent.method_7327().method_55430(ent, scoreBoard);
                  class_5250 text2 = class_9013.method_55398(readableScoreboardScore, scoreBoard.method_55380(class_9025.field_47566));
                  resolvedHp = text2.getString();
               }
            }

            float numValue = 0.0F;

            try {
               numValue = Float.parseFloat(resolvedHp);
            } catch (NumberFormatException var6) {
            }

            return (Boolean)this.absorp.getValue() ? numValue + this.target.method_6067() : numValue;
         }
      }

      return (Boolean)this.absorp.getValue() ? this.target.method_6032() + this.target.method_6067() : this.target.method_6032();
   }

   public static void sizeAnimation(class_4587 matrixStack, double width, double height, double animation) {
      matrixStack.method_22904(width, height, 0.0D);
      matrixStack.method_22905((float)animation, (float)animation, 1.0F);
      matrixStack.method_22904(-width, -height, 0.0D);
   }

   public static String getPotionName(class_1291 p) {
      if (p == class_1294.field_5924.comp_349()) {
         return "Reg";
      } else if (p == class_1294.field_5910.comp_349()) {
         return "Str";
      } else if (p == class_1294.field_5904.comp_349()) {
         return "Spd";
      } else if (p == class_1294.field_5917.comp_349()) {
         return "H";
      } else if (p == class_1294.field_5911.comp_349()) {
         return "W";
      } else {
         return p == class_1294.field_5907.comp_349() ? "Res" : "pon";
      }
   }

   public static enum HPmodeEn {
      HP,
      Percentage;

      // $FF: synthetic method
      private static TargetHud.HPmodeEn[] $values() {
         return new TargetHud.HPmodeEn[]{HP, Percentage};
      }
   }

   public static enum ImageModeEn {
      None,
      Anime,
      Custom;

      // $FF: synthetic method
      private static TargetHud.ImageModeEn[] $values() {
         return new TargetHud.ImageModeEn[]{None, Anime, Custom};
      }
   }

   public static enum ModeEn {
      ThunderHack,
      NurikZapen,
      CelkaPasta;

      // $FF: synthetic method
      private static TargetHud.ModeEn[] $values() {
         return new TargetHud.ModeEn[]{ThunderHack, NurikZapen, CelkaPasta};
      }
   }
}
