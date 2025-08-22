package tsunami.features.hud.impl;

import com.mojang.blaze3d.platform.GlStateManager.class_4534;
import com.mojang.blaze3d.platform.GlStateManager.class_4535;
import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import net.minecraft.class_286;
import net.minecraft.class_287;
import net.minecraft.class_289;
import net.minecraft.class_290;
import net.minecraft.class_332;
import net.minecraft.class_757;
import net.minecraft.class_7833;
import net.minecraft.class_293.class_5596;
import org.joml.Matrix4f;
import tsunami.features.modules.Module;
import tsunami.features.modules.client.HudEditor;
import tsunami.setting.Setting;
import tsunami.setting.impl.ColorSetting;
import tsunami.utility.render.Render2DEngine;
import tsunami.utility.render.Render3DEngine;
import tsunami.utility.render.TextureStorage;
import tsunami.utility.render.animation.AnimationUtility;

public class Crosshair extends Module {
   private final Setting<Crosshair.Mode> mode;
   private final Setting<Boolean> animated;
   private final Setting<Boolean> dot;
   private final Setting<Boolean> t;
   private final Setting<Crosshair.ColorMode> colorMode;
   public final Setting<ColorSetting> color;
   private final Setting<Boolean> dynamic;
   private final Setting<Float> range;
   private final Setting<Float> speed;
   private final Setting<Float> backSpeed;
   private float xAnim;
   private float yAnim;
   private float prevPitch;
   private float prevProgress;

   public Crosshair() {
      super("Crosshair", Module.Category.HUD);
      this.mode = new Setting("Mode", Crosshair.Mode.Circle);
      this.animated = new Setting("Animated", true, (v) -> {
         return this.mode.is(Crosshair.Mode.Default);
      });
      this.dot = new Setting("Dot", false, (v) -> {
         return this.mode.is(Crosshair.Mode.Default);
      });
      this.t = new Setting("T", false, (v) -> {
         return this.mode.is(Crosshair.Mode.Default);
      });
      this.colorMode = new Setting("ColorMode", Crosshair.ColorMode.Sync);
      this.color = new Setting("Color", new ColorSetting(575714484));
      this.dynamic = new Setting("Dynamic", true);
      this.range = new Setting("Range", 30.0F, 0.1F, 120.0F);
      this.speed = new Setting("Speed", 3.0F, 0.1F, 20.0F);
      this.backSpeed = new Setting("BackSpeed", 5.0F, 0.1F, 20.0F);
   }

   public void onRender2D(class_332 context) {
      if (mc.field_1690.method_31044().method_31034()) {
         float midX = (float)mc.method_22683().method_4486() / 2.0F;
         float midY = (float)mc.method_22683().method_4502() / 2.0F;
         float yawDelta = mc.field_1724.field_6259 - mc.field_1724.method_5791();
         float pitchDelta = this.prevPitch - mc.field_1724.method_36455();
         if (yawDelta > 0.0F) {
            this.xAnim = AnimationUtility.fast(this.xAnim, midX - (Float)this.range.getValue(), (Float)this.speed.getValue());
         } else if (yawDelta < 0.0F) {
            this.xAnim = AnimationUtility.fast(this.xAnim, midX + (Float)this.range.getValue(), (Float)this.speed.getValue());
         } else {
            this.xAnim = AnimationUtility.fast(this.xAnim, midX, (Float)this.backSpeed.getValue());
         }

         if (pitchDelta > 0.0F) {
            this.yAnim = AnimationUtility.fast(this.yAnim, midY - (Float)this.range.getValue(), (Float)this.speed.getValue());
         } else if (pitchDelta < 0.0F) {
            this.yAnim = AnimationUtility.fast(this.yAnim, midY + (Float)this.range.getValue(), (Float)this.speed.getValue());
         } else {
            this.yAnim = AnimationUtility.fast(this.yAnim, midY, (Float)this.backSpeed.getValue());
         }

         this.prevPitch = mc.field_1724.method_36455();
         if (!(Boolean)this.dynamic.getValue()) {
            this.xAnim = midX;
            this.yAnim = midY;
         }

         float progress = 360.0F * mc.field_1724.method_7261(0.5F);
         progress = progress == 0.0F ? 360.0F : progress;
         Color color;
         Color color1;
         switch(((Crosshair.Mode)this.mode.getValue()).ordinal()) {
         case 0:
            color = this.colorMode.getValue() == Crosshair.ColorMode.Sync ? ((ColorSetting)HudEditor.hcolor1.getValue()).getColorObject() : ((ColorSetting)this.color.getValue()).getColorObject();
            color1 = this.colorMode.getValue() == Crosshair.ColorMode.Sync ? ((ColorSetting)HudEditor.acolor.getValue()).getColorObject() : ((ColorSetting)this.color.getValue()).getColorObject();
            Render2DEngine.drawArc(context.method_51448(), this.xAnim - 25.0F, this.yAnim - 25.0F, 50.0F, 50.0F, 0.05F, 0.12F, 0.0F, Render2DEngine.interpolateFloat(this.prevProgress, progress, (double)Render3DEngine.getTickDelta()), color, color1);
            this.prevProgress = progress;
            break;
         case 1:
            color = ((ColorSetting)this.color.getValue()).getColorObject();
            context.method_51448().method_22903();
            context.method_51448().method_46416(this.xAnim, this.yAnim, 0.0F);
            context.method_51448().method_22907(class_7833.field_40718.rotation((float)(System.currentTimeMillis() % 70000L) / 70000.0F * 360.0F));
            context.method_51448().method_46416(-this.xAnim, -this.yAnim, 0.0F);
            Render2DEngine.drawRect(context.method_51448(), this.xAnim - 0.75F, this.yAnim - 5.0F, 1.5F, 10.0F, color);
            Render2DEngine.drawRect(context.method_51448(), this.xAnim - 5.0F, this.yAnim - 0.75F, 10.0F, 1.5F, color);
            Render2DEngine.drawRect(context.method_51448(), this.xAnim, this.yAnim - 5.0F, 5.0F, 1.5F, color);
            Render2DEngine.drawRect(context.method_51448(), this.xAnim - 5.0F, this.yAnim + 4.0F, 5.25F, 1.5F, color);
            Render2DEngine.drawRect(context.method_51448(), this.xAnim - 5.0F, this.yAnim - 5.0F, 1.5F, 4.25F, color);
            Render2DEngine.drawRect(context.method_51448(), this.xAnim + 3.5F, this.yAnim, 1.5F, 5.5F, color);
            context.method_51448().method_22909();
            break;
         case 2:
            context.method_51448().method_22903();
            context.method_51448().method_46416(this.xAnim + 4.0F, this.yAnim + 4.0F, 0.0F);
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(class_4535.SRC_ALPHA, class_4534.ONE);
            RenderSystem.setShader(class_757::method_34543);
            class_287 bufferBuilder = class_289.method_1348().method_60827(class_5596.field_27382, class_290.field_1575);
            RenderSystem.setShaderTexture(0, TextureStorage.firefly);
            color1 = this.colorMode.getValue() == Crosshair.ColorMode.Sync ? HudEditor.getColor(1) : ((ColorSetting)this.color.getValue()).getColorObject();
            Matrix4f posMatrix = context.method_51448().method_23760().method_23761();
            bufferBuilder.method_22918(posMatrix, 0.0F, -8.0F, 0.0F).method_22913(0.0F, 1.0F).method_39415(color1.getRGB());
            bufferBuilder.method_22918(posMatrix, -8.0F, -8.0F, 0.0F).method_22913(1.0F, 1.0F).method_39415(color1.getRGB());
            bufferBuilder.method_22918(posMatrix, -8.0F, 0.0F, 0.0F).method_22913(1.0F, 0.0F).method_39415(color1.getRGB());
            bufferBuilder.method_22918(posMatrix, 0.0F, 0.0F, 0.0F).method_22913(0.0F, 0.0F).method_39415(color1.getRGB());
            class_286.method_43433(bufferBuilder.method_60800());
            RenderSystem.defaultBlendFunc();
            RenderSystem.disableBlend();
            context.method_51448().method_22909();
            break;
         case 3:
            color = ((ColorSetting)this.color.getValue()).getColorObject();
            float offset = (Boolean)this.animated.getValue() ? -3.0F + Render2DEngine.interpolateFloat(this.prevProgress, progress, (double)Render3DEngine.getTickDelta()) / 100.0F : 0.0F;
            this.prevProgress = progress;
            if (!(Boolean)this.t.getValue()) {
               Render2DEngine.drawRect(context.method_51448(), this.xAnim - 1.0F, this.yAnim - 6.0F + offset, 2.0F, 4.0F, Color.BLACK);
               Render2DEngine.drawRect(context.method_51448(), this.xAnim - 0.5F, this.yAnim - 5.5F + offset, 1.0F, 3.0F, color);
            }

            Render2DEngine.drawRect(context.method_51448(), this.xAnim - 1.0F, this.yAnim + 2.0F - offset, 2.0F, 4.0F, Color.BLACK);
            Render2DEngine.drawRect(context.method_51448(), this.xAnim - 0.5F, this.yAnim + 2.5F - offset, 1.0F, 3.0F, color);
            Render2DEngine.drawRect(context.method_51448(), this.xAnim - 6.0F + offset, this.yAnim - 1.0F, 4.0F, 2.0F, Color.BLACK);
            Render2DEngine.drawRect(context.method_51448(), this.xAnim - 5.5F + offset, this.yAnim - 0.5F, 3.0F, 1.0F, color);
            Render2DEngine.drawRect(context.method_51448(), this.xAnim + 2.0F - offset, this.yAnim - 1.0F, 4.0F, 2.0F, Color.BLACK);
            Render2DEngine.drawRect(context.method_51448(), this.xAnim + 2.5F - offset, this.yAnim - 0.5F, 3.0F, 1.0F, color);
            if ((Boolean)this.dot.getValue()) {
               Render2DEngine.drawRect(context.method_51448(), this.xAnim - 1.0F, this.yAnim - 1.0F, 2.0F, 2.0F, Color.BLACK);
               Render2DEngine.drawRect(context.method_51448(), this.xAnim - 0.5F, this.yAnim - 0.5F, 1.0F, 1.0F, color);
            }
         }

      }
   }

   public float getAnimatedPosX() {
      return this.xAnim == 0.0F ? (float)mc.method_22683().method_4486() / 2.0F : this.xAnim;
   }

   public float getAnimatedPosY() {
      return this.yAnim == 0.0F ? (float)mc.method_22683().method_4502() / 2.0F : this.yAnim;
   }

   private static enum Mode {
      Circle,
      WiseTree,
      Dot,
      Default;

      // $FF: synthetic method
      private static Crosshair.Mode[] $values() {
         return new Crosshair.Mode[]{Circle, WiseTree, Dot, Default};
      }
   }

   private static enum ColorMode {
      Custom,
      Sync;

      // $FF: synthetic method
      private static Crosshair.ColorMode[] $values() {
         return new Crosshair.ColorMode[]{Custom, Sync};
      }
   }
}
