package tsunami.features.modules.render;

import com.mojang.blaze3d.platform.GlStateManager.class_4534;
import com.mojang.blaze3d.platform.GlStateManager.class_4535;
import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_1667;
import net.minecraft.class_1683;
import net.minecraft.class_1684;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2338;
import net.minecraft.class_243;
import net.minecraft.class_287;
import net.minecraft.class_289;
import net.minecraft.class_290;
import net.minecraft.class_3959;
import net.minecraft.class_4184;
import net.minecraft.class_4587;
import net.minecraft.class_757;
import net.minecraft.class_7833;
import net.minecraft.class_239.class_240;
import net.minecraft.class_293.class_5596;
import net.minecraft.class_3959.class_242;
import net.minecraft.class_3959.class_3960;
import org.joml.Matrix4f;
import tsunami.core.Managers;
import tsunami.features.modules.Module;
import tsunami.features.modules.client.HudEditor;
import tsunami.setting.Setting;
import tsunami.setting.impl.ColorSetting;
import tsunami.utility.interfaces.IEntity;
import tsunami.utility.math.MathUtility;
import tsunami.utility.render.Render2DEngine;
import tsunami.utility.render.Render3DEngine;
import tsunami.utility.render.TextureStorage;

public class Trails extends Module {
   private final Setting<Boolean> xp = new Setting("Xp", false);
   private final Setting<Trails.Particles> pearls;
   private final Setting<Trails.Particles> arrows;
   private final Setting<Trails.Players> players;
   private final Setting<Boolean> onlySelf;
   private final Setting<ColorSetting> color;
   private final Setting<Float> down;
   private final Setting<Float> width;
   private final Setting<Integer> speed;
   private final Setting<HitParticles.Mode> mode;
   private final Setting<HitParticles.Physics> physics;
   private final Setting<Integer> starsScale;
   private final Setting<Integer> amount;
   private final Setting<Integer> lifeTime;
   private final Setting<Trails.Mode> lmode;
   private final Setting<ColorSetting> lcolor;
   private List<Trails.Particle> particles;

   public Trails() {
      super("Trails", Module.Category.RENDER);
      this.pearls = new Setting("Pearls", Trails.Particles.Particles);
      this.arrows = new Setting("Arrows", Trails.Particles.Particles);
      this.players = new Setting("Players", Trails.Players.Particles);
      this.onlySelf = new Setting("OnlySelf", false, (v) -> {
         return this.players.getValue() != Trails.Players.None;
      });
      this.color = new Setting("Color", new ColorSetting(-2013200640));
      this.down = new Setting("Down", 0.5F, 0.0F, 2.0F);
      this.width = new Setting("Height", 1.3F, 0.1F, 2.0F);
      this.speed = new Setting("Speed", 2, 1, 20, (v) -> {
         return this.players.is(Trails.Players.Particles) || this.arrows.is(Trails.Particles.Particles) || this.pearls.is(Trails.Particles.Particles);
      });
      this.mode = new Setting("Mode", HitParticles.Mode.Stars);
      this.physics = new Setting("Physics", HitParticles.Physics.Fall, (v) -> {
         return this.players.is(Trails.Players.Particles) || this.arrows.is(Trails.Particles.Particles) || this.pearls.is(Trails.Particles.Particles);
      });
      this.starsScale = new Setting("Scale", 3, 1, 10, (v) -> {
         return this.players.is(Trails.Players.Particles) || this.arrows.is(Trails.Particles.Particles) || this.pearls.is(Trails.Particles.Particles);
      });
      this.amount = new Setting("Amount", 2, 1, 5, (v) -> {
         return this.players.is(Trails.Players.Particles) || this.arrows.is(Trails.Particles.Particles) || this.pearls.is(Trails.Particles.Particles);
      });
      this.lifeTime = new Setting("LifeTime", 2, 1, 10, (v) -> {
         return this.players.is(Trails.Players.Particles) || this.arrows.is(Trails.Particles.Particles) || this.pearls.is(Trails.Particles.Particles);
      });
      this.lmode = new Setting("ColorMode", Trails.Mode.Sync);
      this.lcolor = new Setting("Color2", new ColorSetting(575714484), (v) -> {
         return this.lmode.getValue() == Trails.Mode.Custom;
      });
      this.particles = new ArrayList();
   }

   public void onRender3D(class_4587 stack) {
      Iterator var2 = Managers.ASYNC.getAsyncEntities().iterator();

      while(var2.hasNext()) {
         class_1297 en = (class_1297)var2.next();
         if (en instanceof class_1684 && this.pearls.is(Trails.Particles.Trail)) {
            this.calcTrajectory(en);
         }

         if (en instanceof class_1667 && this.arrows.is(Trails.Particles.Trail)) {
            this.calcTrajectory(en);
         }

         if (en instanceof class_1683 && (Boolean)this.xp.getValue()) {
            this.calcTrajectory(en);
         }
      }

      float alpha;
      class_1657 entity;
      if (this.players.getValue() == Trails.Players.Trail) {
         var2 = mc.field_1687.method_18456().iterator();

         label176:
         while(true) {
            do {
               do {
                  if (!var2.hasNext()) {
                     break label176;
                  }

                  entity = (class_1657)var2.next();
               } while(entity != mc.field_1724 && (Boolean)this.onlySelf.getValue());

               alpha = (float)((ColorSetting)this.color.getValue()).getAlpha() / 255.0F;
            } while(((IEntity)entity).getTrails().isEmpty());

            stack.method_22903();
            RenderSystem.disableCull();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.enableDepthTest();
            RenderSystem.depthFunc(515);
            RenderSystem.setShader(class_757::method_34540);
            class_287 bufferBuilder = class_289.method_1348().method_60827(class_5596.field_27380, class_290.field_1576);

            for(int i = 0; i < ((IEntity)entity).getTrails().size(); ++i) {
               Trails.Trail ctx = (Trails.Trail)((IEntity)entity).getTrails().get(i);
               class_243 pos = ctx.interpolate(Render3DEngine.getTickDelta());
               bufferBuilder.method_22918(stack.method_23760().method_23761(), (float)pos.field_1352, (float)pos.field_1351 + (Float)this.down.getValue(), (float)pos.field_1350).method_39415(Render2DEngine.injectAlpha(((Trails.Trail)((IEntity)entity).getTrails().get(i)).color(), (int)((double)alpha * ctx.animation(Render3DEngine.getTickDelta()) * 255.0D)).getRGB());
               bufferBuilder.method_22918(stack.method_23760().method_23761(), (float)pos.field_1352, (float)pos.field_1351 + (Float)this.width.getValue() + (Float)this.down.getValue(), (float)pos.field_1350).method_39415(Render2DEngine.injectAlpha(((Trails.Trail)((IEntity)entity).getTrails().get(i)).color(), (int)((double)alpha * ctx.animation(Render3DEngine.getTickDelta()) * 255.0D)).getRGB());
            }

            Render2DEngine.endBuilding(bufferBuilder);
            Render3DEngine.endRender();
            RenderSystem.enableCull();
            RenderSystem.disableDepthTest();
            stack.method_22909();
         }
      } else {
         class_287 bufferBuilder;
         int i;
         if (this.players.getValue() == Trails.Players.Tail) {
            var2 = mc.field_1687.method_18456().iterator();

            label154:
            while(true) {
               do {
                  if (!var2.hasNext()) {
                     break label154;
                  }

                  entity = (class_1657)var2.next();
               } while(entity != mc.field_1724 && (Boolean)this.onlySelf.getValue());

               alpha = (float)((ColorSetting)this.color.getValue()).getAlpha();
               class_4184 camera = mc.field_1773.method_19418();
               stack.method_22903();
               RenderSystem.setShaderTexture(0, TextureStorage.firefly);
               RenderSystem.enableBlend();
               RenderSystem.blendFunc(class_4535.SRC_ALPHA, class_4534.ONE);
               RenderSystem.enableDepthTest();
               RenderSystem.depthMask(false);
               RenderSystem.setShader(class_757::method_34543);
               bufferBuilder = class_289.method_1348().method_60827(class_5596.field_27382, class_290.field_1575);
               i = ((IEntity)entity).getTrails().size();
               if (!((IEntity)entity).getTrails().isEmpty()) {
                  for(int i = 0; i < i; ++i) {
                     Trails.Trail ctx = (Trails.Trail)((IEntity)entity).getTrails().get(i);
                     class_4587 matrices = new class_4587();
                     matrices.method_22907(class_7833.field_40714.rotationDegrees(camera.method_19329()));
                     matrices.method_22907(class_7833.field_40716.rotationDegrees(camera.method_19330() + 180.0F));
                     class_243 pos = ctx.interpolate(Render3DEngine.getTickDelta());
                     matrices.method_22904(pos.field_1352, pos.field_1351 + 0.8999999761581421D, pos.field_1350);
                     matrices.method_22907(class_7833.field_40716.rotationDegrees(-camera.method_19330()));
                     matrices.method_22907(class_7833.field_40714.rotationDegrees(camera.method_19329()));
                     Matrix4f matrix = matrices.method_23760().method_23761();
                     Color col = ctx.color();
                     float sc = 0.6F;
                     float colorFactor = (float)i / (float)i;
                     col = Render2DEngine.interpolateColorC(col, Color.WHITE, (float)Math.pow((double)(1.0F - colorFactor), 2.0D));
                     float animPow = (float)ctx.animation(Render3DEngine.getTickDelta());
                     int animatedAlpha = (int)(alpha * animPow);
                     bufferBuilder.method_22918(matrix, -sc, sc, 0.0F).method_22913(0.0F, 1.0F).method_39415(Render2DEngine.injectAlpha(col, animatedAlpha).getRGB());
                     bufferBuilder.method_22918(matrix, sc, sc, 0.0F).method_22913(1.0F, 1.0F).method_39415(Render2DEngine.injectAlpha(col, animatedAlpha).getRGB());
                     bufferBuilder.method_22918(matrix, sc, -sc, 0.0F).method_22913(1.0F, 0.0F).method_39415(Render2DEngine.injectAlpha(col, animatedAlpha).getRGB());
                     bufferBuilder.method_22918(matrix, -sc, -sc, 0.0F).method_22913(0.0F, 0.0F).method_39415(Render2DEngine.injectAlpha(col, animatedAlpha).getRGB());
                  }
               }

               Render2DEngine.endBuilding(bufferBuilder);
               RenderSystem.depthMask(true);
               RenderSystem.disableDepthTest();
               RenderSystem.disableBlend();
               stack.method_22909();
            }
         } else if (this.players.getValue() == Trails.Players.Cute) {
            var2 = mc.field_1687.method_18456().iterator();

            label134:
            while(true) {
               do {
                  do {
                     if (!var2.hasNext()) {
                        break label134;
                     }

                     entity = (class_1657)var2.next();
                  } while(entity != mc.field_1724 && (Boolean)this.onlySelf.getValue());

                  alpha = (float)((ColorSetting)this.color.getValue()).getAlpha() / 255.0F;
               } while(((IEntity)entity).getTrails().isEmpty());

               stack.method_22903();
               RenderSystem.disableCull();
               RenderSystem.enableBlend();
               RenderSystem.defaultBlendFunc();
               RenderSystem.enableDepthTest();
               RenderSystem.depthFunc(515);
               float step = (float)(mc.field_1724.method_5829().method_17940() / 5.0D);
               RenderSystem.setShader(class_757::method_34540);
               bufferBuilder = class_289.method_1348().method_60827(class_5596.field_27380, class_290.field_1576);

               Trails.Trail ctx;
               class_243 pos;
               for(i = 0; i < ((IEntity)entity).getTrails().size(); ++i) {
                  ctx = (Trails.Trail)((IEntity)entity).getTrails().get(i);
                  pos = ctx.interpolate(Render3DEngine.getTickDelta());
                  bufferBuilder.method_22918(stack.method_23760().method_23761(), (float)pos.field_1352, (float)pos.field_1351, (float)pos.field_1350).method_39415(Render2DEngine.injectAlpha(new Color(69, 221, 255), (int)((double)alpha * ctx.animation(Render3DEngine.getTickDelta()) * 255.0D)).getRGB());
                  bufferBuilder.method_22918(stack.method_23760().method_23761(), (float)pos.field_1352, (float)pos.field_1351 + step, (float)pos.field_1350).method_39415(Render2DEngine.injectAlpha(new Color(69, 221, 255), (int)((double)alpha * ctx.animation(Render3DEngine.getTickDelta()) * 255.0D)).getRGB());
               }

               Render2DEngine.endBuilding(bufferBuilder);
               RenderSystem.setShader(class_757::method_34540);
               bufferBuilder = class_289.method_1348().method_60827(class_5596.field_27380, class_290.field_1576);

               for(i = 0; i < ((IEntity)entity).getTrails().size(); ++i) {
                  ctx = (Trails.Trail)((IEntity)entity).getTrails().get(i);
                  pos = ctx.interpolate(Render3DEngine.getTickDelta());
                  bufferBuilder.method_22918(stack.method_23760().method_23761(), (float)pos.field_1352, (float)pos.field_1351 + step, (float)pos.field_1350).method_39415(Render2DEngine.injectAlpha(new Color(248, 139, 160), (int)((double)alpha * ctx.animation(Render3DEngine.getTickDelta()) * 255.0D)).getRGB());
                  bufferBuilder.method_22918(stack.method_23760().method_23761(), (float)pos.field_1352, (float)pos.field_1351 + step * 2.0F, (float)pos.field_1350).method_39415(Render2DEngine.injectAlpha(new Color(248, 139, 160), (int)((double)alpha * ctx.animation(Render3DEngine.getTickDelta()) * 255.0D)).getRGB());
               }

               Render2DEngine.endBuilding(bufferBuilder);
               RenderSystem.setShader(class_757::method_34540);
               bufferBuilder = class_289.method_1348().method_60827(class_5596.field_27380, class_290.field_1576);

               for(i = 0; i < ((IEntity)entity).getTrails().size(); ++i) {
                  ctx = (Trails.Trail)((IEntity)entity).getTrails().get(i);
                  pos = ctx.interpolate(Render3DEngine.getTickDelta());
                  bufferBuilder.method_22918(stack.method_23760().method_23761(), (float)pos.field_1352, (float)pos.field_1351 + step * 2.0F, (float)pos.field_1350).method_39415(Render2DEngine.injectAlpha(new Color(255, 255, 255), (int)((double)alpha * ctx.animation(Render3DEngine.getTickDelta()) * 255.0D)).getRGB());
                  bufferBuilder.method_22918(stack.method_23760().method_23761(), (float)pos.field_1352, (float)pos.field_1351 + step * 3.0F, (float)pos.field_1350).method_39415(Render2DEngine.injectAlpha(new Color(255, 255, 255), (int)((double)alpha * ctx.animation(Render3DEngine.getTickDelta()) * 255.0D)).getRGB());
               }

               Render2DEngine.endBuilding(bufferBuilder);
               RenderSystem.setShader(class_757::method_34540);
               bufferBuilder = class_289.method_1348().method_60827(class_5596.field_27380, class_290.field_1576);

               for(i = 0; i < ((IEntity)entity).getTrails().size(); ++i) {
                  ctx = (Trails.Trail)((IEntity)entity).getTrails().get(i);
                  pos = ctx.interpolate(Render3DEngine.getTickDelta());
                  bufferBuilder.method_22918(stack.method_23760().method_23761(), (float)pos.field_1352, (float)pos.field_1351 + step * 3.0F, (float)pos.field_1350).method_39415(Render2DEngine.injectAlpha(new Color(248, 139, 160), (int)((double)alpha * ctx.animation(Render3DEngine.getTickDelta()) * 255.0D)).getRGB());
                  bufferBuilder.method_22918(stack.method_23760().method_23761(), (float)pos.field_1352, (float)pos.field_1351 + step * 4.0F, (float)pos.field_1350).method_39415(Render2DEngine.injectAlpha(new Color(248, 139, 160), (int)((double)alpha * ctx.animation(Render3DEngine.getTickDelta()) * 255.0D)).getRGB());
               }

               Render2DEngine.endBuilding(bufferBuilder);
               RenderSystem.setShader(class_757::method_34540);
               bufferBuilder = class_289.method_1348().method_60827(class_5596.field_27380, class_290.field_1576);

               for(i = 0; i < ((IEntity)entity).getTrails().size(); ++i) {
                  ctx = (Trails.Trail)((IEntity)entity).getTrails().get(i);
                  pos = ctx.interpolate(Render3DEngine.getTickDelta());
                  bufferBuilder.method_22918(stack.method_23760().method_23761(), (float)pos.field_1352, (float)pos.field_1351 + step * 4.0F, (float)pos.field_1350).method_39415(Render2DEngine.injectAlpha(new Color(69, 221, 255), (int)((double)alpha * ctx.animation(Render3DEngine.getTickDelta()) * 255.0D)).getRGB());
                  bufferBuilder.method_22918(stack.method_23760().method_23761(), (float)pos.field_1352, (float)pos.field_1351 + step * 5.0F, (float)pos.field_1350).method_39415(Render2DEngine.injectAlpha(new Color(69, 221, 255), (int)((double)alpha * ctx.animation(Render3DEngine.getTickDelta()) * 255.0D)).getRGB());
               }

               Render2DEngine.endBuilding(bufferBuilder);
               Render3DEngine.endRender();
               RenderSystem.enableCull();
               RenderSystem.disableDepthTest();
               stack.method_22909();
            }
         }
      }

      if (!this.particles.isEmpty()) {
         RenderSystem.enableBlend();
         RenderSystem.enableDepthTest();
         RenderSystem.depthMask(false);
         RenderSystem.blendFunc(class_4535.SRC_ALPHA, class_4534.ONE);
         switch((HitParticles.Mode)this.mode.getValue()) {
         case Stars:
            RenderSystem.setShaderTexture(0, TextureStorage.star);
            break;
         case Bloom:
            RenderSystem.setShaderTexture(0, TextureStorage.firefly);
            break;
         case Hearts:
            RenderSystem.setShaderTexture(0, TextureStorage.heart);
            break;
         default:
            return;
         }

         RenderSystem.setShader(class_757::method_34543);
         class_287 bufferBuilder = class_289.method_1348().method_60827(class_5596.field_27382, class_290.field_1575);
         if (mc.field_1724 != null && mc.field_1687 != null) {
            this.particles.forEach((p) -> {
               p.render(stack, bufferBuilder);
            });
         }

         Render2DEngine.endBuilding(bufferBuilder);
         RenderSystem.disableBlend();
         RenderSystem.depthMask(true);
         RenderSystem.disableDepthTest();
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      }

   }

   public void onUpdate() {
      Color c = this.lmode.getValue() == Trails.Mode.Sync ? HudEditor.getColor(mc.field_1724.field_6012 % 360) : ((ColorSetting)this.lcolor.getValue()).getColorObject();

      Iterator var2;
      class_1657 player;
      int i;
      for(var2 = mc.field_1687.method_18456().iterator(); var2.hasNext(); ((IEntity)player).getTrails().removeIf(Trails.Trail::update)) {
         player = (class_1657)var2.next();
         if (player.method_19538().method_10215() != player.field_5969 || player.method_19538().method_10216() != player.field_6014 && !(Boolean)this.onlySelf.getValue()) {
            ((IEntity)player).getTrails().add(new Trails.Trail(new class_243(player.field_6014, player.field_6036, player.field_5969), player.method_19538(), c));
            if (this.players.is(Trails.Players.Particles)) {
               for(i = 0; i < (Integer)this.amount.getValue(); ++i) {
                  this.particles.add(new Trails.Particle(player.method_23317(), (double)MathUtility.random((float)(player.method_23318() + (double)player.method_17682()), (float)player.method_23318()), player.method_23321(), c));
               }
            }
         }
      }

      var2 = Managers.ASYNC.getAsyncEntities().iterator();

      while(true) {
         class_1297 en;
         do {
            do {
               if (!var2.hasNext()) {
                  if (Managers.PLAYER.currentPlayerSpeed != 0.0F) {
                     ((IEntity)mc.field_1724).getTrails().add(new Trails.Trail(new class_243(mc.field_1724.field_6014, mc.field_1724.field_6036, mc.field_1724.field_5969), mc.field_1724.method_19538(), c));
                     if (this.players.is(Trails.Players.Particles)) {
                        for(int i = 0; i < (Integer)this.amount.getValue(); ++i) {
                           this.particles.add(new Trails.Particle(mc.field_1724.method_23317(), (double)MathUtility.random((float)(mc.field_1724.method_23318() + (double)mc.field_1724.method_17682()), (float)mc.field_1724.method_23318()), mc.field_1724.method_23321(), c));
                        }
                     }
                  }

                  this.particles.removeIf((particle) -> {
                     return (float)(System.currentTimeMillis() - particle.time) > 1000.0F * (float)(Integer)this.lifeTime.getValue();
                  });
                  return;
               }

               en = (class_1297)var2.next();
               if (en instanceof class_1667) {
                  class_1667 ae = (class_1667)en;
                  if (ae.field_6036 != ae.method_23318() && this.arrows.is(Trails.Particles.Particles)) {
                     for(int i = 0; i < 5; ++i) {
                        this.particles.add(new Trails.Particle(en.method_23317(), en.method_23318(), en.method_23321(), HudEditor.getColor(mc.field_1724.field_6012)));
                     }
                  }
               }
            } while(!(en instanceof class_1684));
         } while(!this.pearls.is(Trails.Particles.Particles));

         for(i = 0; i < 5; ++i) {
            this.particles.add(new Trails.Particle(en.method_23317(), en.method_23318(), en.method_23321(), HudEditor.getColor(mc.field_1724.field_6012)));
         }
      }
   }

   private void calcTrajectory(class_1297 e) {
      double motionX = e.method_18798().field_1352;
      double motionY = e.method_18798().field_1351;
      double motionZ = e.method_18798().field_1350;
      double x = e.method_23317();
      double y = e.method_23318();
      double z = e.method_23321();
      new class_243(x, y, z);

      for(int i = 0; i < 300; ++i) {
         class_243 lastPos = new class_243(x, y, z);
         x += motionX;
         y += motionY;
         z += motionZ;
         if (mc.field_1687.method_8320(new class_2338((int)x, (int)y, (int)z)).method_26204() == class_2246.field_10382) {
            motionX *= 0.8D;
            motionY *= 0.8D;
            motionZ *= 0.8D;
         } else {
            motionX *= 0.99D;
            motionY *= 0.99D;
            motionZ *= 0.99D;
         }

         if (e instanceof class_1667) {
            motionY -= 0.05000000074505806D;
         } else {
            motionY -= 0.029999999329447746D;
         }

         class_243 pos = new class_243(x, y, z);
         if (mc.field_1687.method_17742(new class_3959(lastPos, pos, class_3960.field_17559, class_242.field_1348, mc.field_1724)) != null && (mc.field_1687.method_17742(new class_3959(lastPos, pos, class_3960.field_17559, class_242.field_1348, mc.field_1724)).method_17783() == class_240.field_1331 || mc.field_1687.method_17742(new class_3959(lastPos, pos, class_3960.field_17559, class_242.field_1348, mc.field_1724)).method_17783() == class_240.field_1332) || y <= -65.0D) {
            break;
         }

         if (e.method_18798().field_1352 != 0.0D || e.method_18798().field_1351 != 0.0D || e.method_18798().field_1350 != 0.0D) {
            int alpha = (int)MathUtility.clamp(255.0F * ((float)i / 8.0F), 0.0F, 255.0F);
            Render3DEngine.drawLine(lastPos, pos, this.lmode.getValue() == Trails.Mode.Sync ? Render2DEngine.injectAlpha(HudEditor.getColor(i * 5), alpha) : Render2DEngine.injectAlpha(((ColorSetting)this.lcolor.getValue()).getColorObject(), alpha));
         }
      }

   }

   private static enum Particles {
      Trail,
      Particles,
      None;

      // $FF: synthetic method
      private static Trails.Particles[] $values() {
         return new Trails.Particles[]{Trail, Particles, None};
      }
   }

   private static enum Players {
      Trail,
      Particles,
      Cute,
      Tail,
      None;

      // $FF: synthetic method
      private static Trails.Players[] $values() {
         return new Trails.Players[]{Trail, Particles, Cute, Tail, None};
      }
   }

   private static enum Mode {
      Custom,
      Sync;

      // $FF: synthetic method
      private static Trails.Mode[] $values() {
         return new Trails.Mode[]{Custom, Sync};
      }
   }

   public static class Trail {
      private final class_243 from;
      private final class_243 to;
      private final Color color;
      private int ticks;
      private int prevTicks;

      public Trail(class_243 from, class_243 to, Color color) {
         this.from = from;
         this.to = to;
         this.ticks = 10;
         this.color = color;
      }

      public class_243 interpolate(float pt) {
         double x = this.from.field_1352 + (this.to.field_1352 - this.from.field_1352) * (double)pt - Module.mc.method_1561().field_4686.method_19326().method_10216();
         double y = this.from.field_1351 + (this.to.field_1351 - this.from.field_1351) * (double)pt - Module.mc.method_1561().field_4686.method_19326().method_10214();
         double z = this.from.field_1350 + (this.to.field_1350 - this.from.field_1350) * (double)pt - Module.mc.method_1561().field_4686.method_19326().method_10215();
         return new class_243(x, y, z);
      }

      public double animation(float pt) {
         return (double)((float)this.prevTicks + (float)(this.ticks - this.prevTicks) * pt) / 10.0D;
      }

      public boolean update() {
         this.prevTicks = this.ticks;
         return this.ticks-- <= 0;
      }

      public Color color() {
         return this.color;
      }
   }

   public class Particle {
      double x;
      double y;
      double z;
      double motionX;
      double motionY;
      double motionZ;
      long time;
      Color color;

      public Particle(double x, double y, double z, Color color) {
         this.x = x;
         this.y = y;
         this.z = z;
         this.motionX = (double)MathUtility.random(-((float)(Integer)Trails.this.speed.getValue()) / 200.0F, (float)(Integer)Trails.this.speed.getValue() / 200.0F);
         this.motionY = (double)MathUtility.random(-((float)(Integer)Trails.this.speed.getValue()) / 200.0F, (float)(Integer)Trails.this.speed.getValue() / 200.0F);
         this.motionZ = (double)MathUtility.random(-((float)(Integer)Trails.this.speed.getValue()) / 200.0F, (float)(Integer)Trails.this.speed.getValue() / 200.0F);
         this.time = System.currentTimeMillis();
         this.color = color;
      }

      public void update() {
         double sp = (double)((float)(Integer)Trails.this.starsScale.getValue() / 10.0F);
         this.x += this.motionX;
         this.y += this.motionY;
         this.z += this.motionZ;
         if (this.posBlock(this.x, this.y - (double)((float)(Integer)Trails.this.starsScale.getValue() / 10.0F), this.z)) {
            this.motionY = -this.motionY / 1.1D;
         } else if (this.posBlock(this.x, this.y, this.z) || this.posBlock(this.x - sp, this.y, this.z - sp) || this.posBlock(this.x + sp, this.y, this.z + sp) || this.posBlock(this.x + sp, this.y, this.z - sp) || this.posBlock(this.x - sp, this.y, this.z + sp) || this.posBlock(this.x + sp, this.y, this.z) || this.posBlock(this.x - sp, this.y, this.z) || this.posBlock(this.x, this.y, this.z - sp) || this.posBlock(this.x, this.y, this.z + sp)) {
            this.motionX = -this.motionX;
            this.motionZ = -this.motionZ;
         }

         if (Trails.this.physics.getValue() == HitParticles.Physics.Fall) {
            this.motionY -= 5.000000237487257E-4D;
         }

         this.motionX /= 1.005D;
         this.motionZ /= 1.005D;
         this.motionY /= 1.005D;
      }

      public void render(class_4587 matrixStack, class_287 bufferBuilder) {
         this.update();
         float scale = (float)(Integer)Trails.this.starsScale.getValue() / 10.0F;
         double posX = this.x - Module.mc.method_1561().field_4686.method_19326().method_10216();
         double posY = this.y - Module.mc.method_1561().field_4686.method_19326().method_10214();
         double posZ = this.z - Module.mc.method_1561().field_4686.method_19326().method_10215();
         class_4184 camera = Module.mc.field_1773.method_19418();
         class_4587 matrices = new class_4587();
         matrices.method_22907(class_7833.field_40714.rotationDegrees(camera.method_19329()));
         matrices.method_22907(class_7833.field_40716.rotationDegrees(camera.method_19330() + 180.0F));
         matrices.method_22904(posX, posY, posZ);
         matrices.method_22907(class_7833.field_40716.rotationDegrees(-camera.method_19330()));
         matrices.method_22907(class_7833.field_40714.rotationDegrees(camera.method_19329()));
         Matrix4f matrix = matrices.method_23760().method_23761();
         float colorAnim = (float)(System.currentTimeMillis() - this.time) / (1000.0F * (float)(Integer)Trails.this.lifeTime.getValue());
         Color c = Trails.this.lmode.getValue() == Trails.Mode.Sync ? HudEditor.getColor((int)(360.0F * colorAnim)) : ((ColorSetting)Trails.this.lcolor.getValue()).getColorObject();
         bufferBuilder.method_22918(matrix, -scale / 2.0F, scale, 0.0F).method_22913(0.0F, 1.0F).method_39415(Render2DEngine.applyOpacity(c, 1.0F - colorAnim).getRGB());
         bufferBuilder.method_22918(matrix, scale, scale, 0.0F).method_22913(1.0F, 1.0F).method_39415(Render2DEngine.applyOpacity(c, 1.0F - colorAnim).getRGB());
         bufferBuilder.method_22918(matrix, scale, -scale / 2.0F, 0.0F).method_22913(1.0F, 0.0F).method_39415(Render2DEngine.applyOpacity(c, 1.0F - colorAnim).getRGB());
         bufferBuilder.method_22918(matrix, -scale / 2.0F, -scale / 2.0F, 0.0F).method_22913(0.0F, 0.0F).method_39415(Render2DEngine.applyOpacity(c, 1.0F - colorAnim).getRGB());
      }

      private boolean posBlock(double x, double y, double z) {
         class_2248 b = Module.mc.field_1687.method_8320(class_2338.method_49637(x, y, z)).method_26204();
         return b != class_2246.field_10124 && b != class_2246.field_10382 && b != class_2246.field_10164;
      }
   }
}
