package tsunami.features.modules.render;

import com.mojang.blaze3d.platform.GlStateManager.class_4534;
import com.mojang.blaze3d.platform.GlStateManager.class_4535;
import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.class_2338;
import net.minecraft.class_243;
import net.minecraft.class_287;
import net.minecraft.class_289;
import net.minecraft.class_290;
import net.minecraft.class_4184;
import net.minecraft.class_4587;
import net.minecraft.class_757;
import net.minecraft.class_7833;
import net.minecraft.class_293.class_5596;
import org.joml.Matrix4f;
import tsunami.features.modules.Module;
import tsunami.features.modules.client.HudEditor;
import tsunami.setting.Setting;
import tsunami.setting.impl.BooleanSettingGroup;
import tsunami.setting.impl.ColorSetting;
import tsunami.utility.math.MathUtility;
import tsunami.utility.render.Render2DEngine;
import tsunami.utility.render.Render3DEngine;
import tsunami.utility.render.TextureStorage;

public class Particles extends Module {
   private final Setting<BooleanSettingGroup> FireFlies = new Setting("FireFlies", new BooleanSettingGroup(true));
   private final Setting<Integer> ffcount;
   private final Setting<Float> ffsize;
   private final Setting<Particles.Mode> mode;
   private final Setting<Integer> count;
   private final Setting<Float> size;
   private final Setting<Particles.ColorMode> lmode;
   private final Setting<ColorSetting> color;
   private final Setting<Particles.Physics> physics;
   private final ArrayList<Particles.ParticleBase> fireFlies;
   private final ArrayList<Particles.ParticleBase> particles;

   public Particles() {
      super("Particles", Module.Category.NONE);
      this.ffcount = (new Setting("FFCount", 30, 20, 200)).addToGroup(this.FireFlies);
      this.ffsize = (new Setting("FFSize", 1.0F, 0.1F, 2.0F)).addToGroup(this.FireFlies);
      this.mode = new Setting("Mode", Particles.Mode.SnowFlake);
      this.count = new Setting("Count", 100, 20, 800);
      this.size = new Setting("Size", 1.0F, 0.1F, 6.0F);
      this.lmode = new Setting("ColorMode", Particles.ColorMode.Sync);
      this.color = new Setting("Color", new ColorSetting(3649978), (v) -> {
         return this.lmode.getValue() == Particles.ColorMode.Custom;
      });
      this.physics = new Setting("Physics", Particles.Physics.Fly, (v) -> {
         return this.mode.getValue() != Particles.Mode.Off;
      });
      this.fireFlies = new ArrayList();
      this.particles = new ArrayList();
   }

   public void onUpdate() {
      this.fireFlies.removeIf(Particles.ParticleBase::tick);
      this.particles.removeIf(Particles.ParticleBase::tick);

      int j;
      for(j = this.fireFlies.size(); j < (Integer)this.ffcount.getValue(); ++j) {
         if (((BooleanSettingGroup)this.FireFlies.getValue()).isEnabled()) {
            this.fireFlies.add(new Particles.FireFly((float)(mc.field_1724.method_23317() + (double)MathUtility.random(-25.0F, 25.0F)), (float)(mc.field_1724.method_23318() + (double)MathUtility.random(2.0F, 15.0F)), (float)(mc.field_1724.method_23321() + (double)MathUtility.random(-25.0F, 25.0F)), MathUtility.random(-0.2F, 0.2F), MathUtility.random(-0.1F, 0.1F), MathUtility.random(-0.2F, 0.2F)));
         }
      }

      for(j = this.particles.size(); j < (Integer)this.count.getValue(); ++j) {
         boolean drop = this.physics.getValue() == Particles.Physics.Drop;
         if (this.mode.getValue() != Particles.Mode.Off) {
            this.particles.add(new Particles.ParticleBase((float)(mc.field_1724.method_23317() + (double)MathUtility.random(-48.0F, 48.0F)), (float)(mc.field_1724.method_23318() + (double)MathUtility.random(2.0F, 48.0F)), (float)(mc.field_1724.method_23321() + (double)MathUtility.random(-48.0F, 48.0F)), drop ? 0.0F : MathUtility.random(-0.4F, 0.4F), drop ? MathUtility.random(-0.2F, -0.05F) : MathUtility.random(-0.1F, 0.1F), drop ? 0.0F : MathUtility.random(-0.4F, 0.4F)));
         }
      }

   }

   public void onRender3D(class_4587 stack) {
      class_287 bufferBuilder;
      if (((BooleanSettingGroup)this.FireFlies.getValue()).isEnabled()) {
         stack.method_22903();
         RenderSystem.setShaderTexture(0, TextureStorage.firefly);
         RenderSystem.enableBlend();
         RenderSystem.blendFunc(class_4535.SRC_ALPHA, class_4534.ONE);
         RenderSystem.enableDepthTest();
         RenderSystem.depthMask(false);
         RenderSystem.setShader(class_757::method_34543);
         bufferBuilder = class_289.method_1348().method_60827(class_5596.field_27382, class_290.field_1575);
         this.fireFlies.forEach((p) -> {
            p.render(bufferBuilder);
         });
         Render2DEngine.endBuilding(bufferBuilder);
         RenderSystem.depthMask(true);
         RenderSystem.disableDepthTest();
         RenderSystem.disableBlend();
         stack.method_22909();
      }

      if (this.mode.getValue() != Particles.Mode.Off) {
         stack.method_22903();
         RenderSystem.enableBlend();
         RenderSystem.blendFunc(class_4535.SRC_ALPHA, class_4534.ONE);
         RenderSystem.enableDepthTest();
         RenderSystem.depthMask(false);
         RenderSystem.setShader(class_757::method_34543);
         bufferBuilder = class_289.method_1348().method_60827(class_5596.field_27382, class_290.field_1575);
         this.particles.forEach((p) -> {
            p.render(bufferBuilder);
         });
         Render2DEngine.endBuilding(bufferBuilder);
         RenderSystem.depthMask(true);
         RenderSystem.blendFunc(class_4535.SRC_ALPHA, class_4534.ONE_MINUS_SRC_ALPHA);
         RenderSystem.disableDepthTest();
         RenderSystem.disableBlend();
         stack.method_22909();
      }

   }

   public static enum Mode {
      Off,
      SnowFlake,
      Stars,
      Hearts,
      Dollars,
      Bloom;

      // $FF: synthetic method
      private static Particles.Mode[] $values() {
         return new Particles.Mode[]{Off, SnowFlake, Stars, Hearts, Dollars, Bloom};
      }
   }

   public static enum ColorMode {
      Custom,
      Sync;

      // $FF: synthetic method
      private static Particles.ColorMode[] $values() {
         return new Particles.ColorMode[]{Custom, Sync};
      }
   }

   public static enum Physics {
      Drop,
      Fly;

      // $FF: synthetic method
      private static Particles.Physics[] $values() {
         return new Particles.Physics[]{Drop, Fly};
      }
   }

   public class FireFly extends Particles.ParticleBase {
      private final List<Trails.Trail> trails = new ArrayList();

      public FireFly(float posX, float posY, float posZ, float motionX, float motionY, float motionZ) {
         super(posX, posY, posZ, motionX, motionY, motionZ);
      }

      public boolean tick() {
         if (Module.mc.field_1724.method_5649((double)this.posX, (double)this.posY, (double)this.posZ) > 100.0D) {
            this.age -= 4;
         } else if (!Module.mc.field_1687.method_8320(new class_2338((int)this.posX, (int)this.posY, (int)this.posZ)).method_26215()) {
            this.age -= 8;
         } else {
            --this.age;
         }

         if (this.age < 0) {
            return true;
         } else {
            this.trails.removeIf(Trails.Trail::update);
            this.prevposX = this.posX;
            this.prevposY = this.posY;
            this.prevposZ = this.posZ;
            this.posX += this.motionX;
            this.posY += this.motionY;
            this.posZ += this.motionZ;
            this.trails.add(new Trails.Trail(new class_243((double)this.prevposX, (double)this.prevposY, (double)this.prevposZ), new class_243((double)this.posX, (double)this.posY, (double)this.posZ), Particles.this.lmode.getValue() == Particles.ColorMode.Sync ? HudEditor.getColor(this.age * 10) : ((ColorSetting)Particles.this.color.getValue()).getColorObject()));
            this.motionX *= 0.99F;
            this.motionY *= 0.99F;
            this.motionZ *= 0.99F;
            return false;
         }
      }

      public void render(class_287 bufferBuilder) {
         RenderSystem.setShaderTexture(0, TextureStorage.firefly);
         if (!this.trails.isEmpty()) {
            class_4184 camera = Module.mc.field_1773.method_19418();
            Iterator var3 = this.trails.iterator();

            while(var3.hasNext()) {
               Trails.Trail ctx = (Trails.Trail)var3.next();
               class_243 pos = ctx.interpolate(1.0F);
               class_4587 matrices = new class_4587();
               matrices.method_22907(class_7833.field_40714.rotationDegrees(camera.method_19329()));
               matrices.method_22907(class_7833.field_40716.rotationDegrees(camera.method_19330() + 180.0F));
               matrices.method_22904(pos.field_1352, pos.field_1351, pos.field_1350);
               matrices.method_22907(class_7833.field_40716.rotationDegrees(-camera.method_19330()));
               matrices.method_22907(class_7833.field_40714.rotationDegrees(camera.method_19329()));
               Matrix4f matrix = matrices.method_23760().method_23761();
               bufferBuilder.method_22918(matrix, 0.0F, -(Float)Particles.this.ffsize.getValue(), 0.0F).method_22913(0.0F, 1.0F).method_39415(Render2DEngine.injectAlpha(ctx.color(), (int)((double)(255.0F * ((float)this.age / (float)this.maxAge)) * ctx.animation(Render3DEngine.getTickDelta()))).getRGB());
               bufferBuilder.method_22918(matrix, -(Float)Particles.this.ffsize.getValue(), -(Float)Particles.this.ffsize.getValue(), 0.0F).method_22913(1.0F, 1.0F).method_39415(Render2DEngine.injectAlpha(ctx.color(), (int)((double)(255.0F * ((float)this.age / (float)this.maxAge)) * ctx.animation(Render3DEngine.getTickDelta()))).getRGB());
               bufferBuilder.method_22918(matrix, -(Float)Particles.this.ffsize.getValue(), 0.0F, 0.0F).method_22913(1.0F, 0.0F).method_39415(Render2DEngine.injectAlpha(ctx.color(), (int)((double)(255.0F * ((float)this.age / (float)this.maxAge)) * ctx.animation(Render3DEngine.getTickDelta()))).getRGB());
               bufferBuilder.method_22918(matrix, 0.0F, 0.0F, 0.0F).method_22913(0.0F, 0.0F).method_39415(Render2DEngine.injectAlpha(ctx.color(), (int)((double)(255.0F * ((float)this.age / (float)this.maxAge)) * ctx.animation(Render3DEngine.getTickDelta()))).getRGB());
            }
         }

      }
   }

   public class ParticleBase {
      protected float prevposX;
      protected float prevposY;
      protected float prevposZ;
      protected float posX;
      protected float posY;
      protected float posZ;
      protected float motionX;
      protected float motionY;
      protected float motionZ;
      protected int age;
      protected int maxAge;

      public ParticleBase(float posX, float posY, float posZ, float motionX, float motionY, float motionZ) {
         this.posX = posX;
         this.posY = posY;
         this.posZ = posZ;
         this.prevposX = posX;
         this.prevposY = posY;
         this.prevposZ = posZ;
         this.motionX = motionX;
         this.motionY = motionY;
         this.motionZ = motionZ;
         this.age = (int)MathUtility.random(100.0F, 300.0F);
         this.maxAge = this.age;
      }

      public boolean tick() {
         if (Module.mc.field_1724.method_5649((double)this.posX, (double)this.posY, (double)this.posZ) > 4096.0D) {
            this.age -= 8;
         } else {
            --this.age;
         }

         if (this.age < 0) {
            return true;
         } else {
            this.prevposX = this.posX;
            this.prevposY = this.posY;
            this.prevposZ = this.posZ;
            this.posX += this.motionX;
            this.posY += this.motionY;
            this.posZ += this.motionZ;
            this.motionX *= 0.9F;
            if (Particles.this.physics.getValue() == Particles.Physics.Fly) {
               this.motionY *= 0.9F;
            }

            this.motionZ *= 0.9F;
            this.motionY -= 0.001F;
            return false;
         }
      }

      public void render(class_287 bufferBuilder) {
         switch(((Particles.Mode)Particles.this.mode.getValue()).ordinal()) {
         case 1:
            RenderSystem.setShaderTexture(0, TextureStorage.snowflake);
            break;
         case 2:
            RenderSystem.setShaderTexture(0, TextureStorage.star);
            break;
         case 3:
            RenderSystem.setShaderTexture(0, TextureStorage.heart);
            break;
         case 4:
            RenderSystem.setShaderTexture(0, TextureStorage.dollar);
            break;
         case 5:
            RenderSystem.setShaderTexture(0, TextureStorage.firefly);
         }

         class_4184 camera = Module.mc.field_1773.method_19418();
         Color color1 = Particles.this.lmode.getValue() == Particles.ColorMode.Sync ? HudEditor.getColor(this.age * 2) : ((ColorSetting)Particles.this.color.getValue()).getColorObject();
         class_243 pos = Render3DEngine.interpolatePos(this.prevposX, this.prevposY, this.prevposZ, this.posX, this.posY, this.posZ);
         class_4587 matrices = new class_4587();
         matrices.method_22907(class_7833.field_40714.rotationDegrees(camera.method_19329()));
         matrices.method_22907(class_7833.field_40716.rotationDegrees(camera.method_19330() + 180.0F));
         matrices.method_22904(pos.field_1352, pos.field_1351, pos.field_1350);
         matrices.method_22907(class_7833.field_40716.rotationDegrees(-camera.method_19330()));
         matrices.method_22907(class_7833.field_40714.rotationDegrees(camera.method_19329()));
         Matrix4f matrix1 = matrices.method_23760().method_23761();
         bufferBuilder.method_22918(matrix1, 0.0F, -(Float)Particles.this.size.getValue(), 0.0F).method_22913(0.0F, 1.0F).method_39415(Render2DEngine.injectAlpha(color1, (int)(255.0F * ((float)this.age / (float)this.maxAge))).getRGB());
         bufferBuilder.method_22918(matrix1, -(Float)Particles.this.size.getValue(), -(Float)Particles.this.size.getValue(), 0.0F).method_22913(1.0F, 1.0F).method_39415(Render2DEngine.injectAlpha(color1, (int)(255.0F * ((float)this.age / (float)this.maxAge))).getRGB());
         bufferBuilder.method_22918(matrix1, -(Float)Particles.this.size.getValue(), 0.0F, 0.0F).method_22913(1.0F, 0.0F).method_39415(Render2DEngine.injectAlpha(color1, (int)(255.0F * ((float)this.age / (float)this.maxAge))).getRGB());
         bufferBuilder.method_22918(matrix1, 0.0F, 0.0F, 0.0F).method_22913(0.0F, 0.0F).method_39415(Render2DEngine.injectAlpha(color1, (int)(255.0F * ((float)this.age / (float)this.maxAge))).getRGB());
      }
   }
}
