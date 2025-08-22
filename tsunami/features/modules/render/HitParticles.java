package tsunami.features.modules.render;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_1657;
import net.minecraft.class_2189;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2338;
import net.minecraft.class_4587;
import net.minecraft.class_7833;
import tsunami.features.modules.Module;
import tsunami.features.modules.client.HudEditor;
import tsunami.gui.font.FontRenderers;
import tsunami.setting.Setting;
import tsunami.setting.impl.ColorSetting;
import tsunami.utility.math.MathUtility;
import tsunami.utility.render.Render2DEngine;
import tsunami.utility.render.Render3DEngine;
import tsunami.utility.render.animation.AnimationUtility;

public class HitParticles extends Module {
   private final Setting<HitParticles.Mode> mode;
   private final Setting<HitParticles.Physics> physics;
   private final Setting<ColorSetting> colorrr;
   private final Setting<Boolean> onlySelf;
   private final Setting<Integer> amount;
   private final Setting<Integer> lifeTime;
   private final Setting<Integer> speed;
   private final Setting<Float> starsScale;
   private final Setting<HitParticles.ColorMode> colorMode;
   private final Setting<ColorSetting> colorH;
   private final Setting<ColorSetting> colorD;
   private final HashMap<Integer, Float> healthMap;
   private final CopyOnWriteArrayList<HitParticles.Particle> particles;

   public HitParticles() {
      super("HitParticles", Module.Category.NONE);
      this.mode = new Setting("Mode", HitParticles.Mode.Stars);
      this.physics = new Setting("Physics", HitParticles.Physics.Fall);
      this.colorrr = new Setting("Color", new ColorSetting(-2013200640));
      this.onlySelf = new Setting("Self", false);
      this.amount = new Setting("Amount", 2, 1, 5);
      this.lifeTime = new Setting("LifeTime", 2, 1, 10);
      this.speed = new Setting("Speed", 2, 1, 20);
      this.starsScale = new Setting("Scale", 3.0F, 1.0F, 10.0F, (v) -> {
         return this.mode.getValue() != HitParticles.Mode.Orbiz;
      });
      this.colorMode = new Setting("ColorMode", HitParticles.ColorMode.Sync);
      this.colorH = new Setting("HealColor", new ColorSetting(3142544), (v) -> {
         return this.mode.is(HitParticles.Mode.Text);
      });
      this.colorD = new Setting("DamageColor", new ColorSetting(15811379), (v) -> {
         return this.mode.is(HitParticles.Mode.Text);
      });
      this.healthMap = new HashMap();
      this.particles = new CopyOnWriteArrayList();
   }

   public void onUpdate() {
      this.particles.removeIf(HitParticles.Particle::update);
      Iterator var1;
      if (this.mode.is(HitParticles.Mode.Text)) {
         var1 = mc.field_1687.method_18112().iterator();

         while(var1.hasNext()) {
            class_1297 entity = (class_1297)var1.next();
            if (entity != null && !(mc.field_1724.method_5858(entity) > 256.0D) && entity.method_5805() && entity instanceof class_1309) {
               class_1309 lent = (class_1309)entity;
               Color c = this.colorMode.getValue() == HitParticles.ColorMode.Sync ? HudEditor.getColor((int)MathUtility.random(1.0F, 228.0F)) : ((ColorSetting)this.colorrr.getValue()).getColorObject();
               float health = lent.method_6032() + lent.method_6067();
               float lastHealth = (Float)this.healthMap.getOrDefault(entity.method_5628(), health);
               this.healthMap.put(entity.method_5628(), health);
               if (lastHealth != health) {
                  this.particles.add(new HitParticles.Particle((float)lent.method_23317(), MathUtility.random((float)(lent.method_23318() + (double)lent.method_17682()), (float)lent.method_23318()), (float)lent.method_23321(), c, MathUtility.random(0.0F, 180.0F), MathUtility.random(10.0F, 60.0F), health - lastHealth));
               }
            }
         }

      } else {
         var1 = mc.field_1687.method_18456().iterator();

         while(true) {
            class_1657 player;
            do {
               do {
                  if (!var1.hasNext()) {
                     return;
                  }

                  player = (class_1657)var1.next();
               } while((Boolean)this.onlySelf.getValue() && player != mc.field_1724);
            } while(player.field_6235 <= 0);

            Color c = this.colorMode.getValue() == HitParticles.ColorMode.Sync ? HudEditor.getColor((int)MathUtility.random(1.0F, 228.0F)) : ((ColorSetting)this.colorrr.getValue()).getColorObject();

            for(int i = 0; i < (Integer)this.amount.getValue(); ++i) {
               this.particles.add(new HitParticles.Particle((float)player.method_23317(), MathUtility.random((float)(player.method_23318() + (double)player.method_17682()), (float)player.method_23318()), (float)player.method_23321(), c, MathUtility.random(0.0F, 180.0F), MathUtility.random(10.0F, 60.0F), 0.0F));
            }
         }
      }
   }

   public void onRender3D(class_4587 stack) {
      RenderSystem.disableDepthTest();
      if (mc.field_1724 != null && mc.field_1687 != null) {
         Iterator var2 = this.particles.iterator();

         while(var2.hasNext()) {
            HitParticles.Particle particle = (HitParticles.Particle)var2.next();
            particle.render(stack);
         }
      }

      RenderSystem.enableDepthTest();
   }

   public static enum Mode {
      Orbiz,
      Stars,
      Hearts,
      Bloom,
      Text;

      // $FF: synthetic method
      private static HitParticles.Mode[] $values() {
         return new HitParticles.Mode[]{Orbiz, Stars, Hearts, Bloom, Text};
      }
   }

   public static enum Physics {
      Fall,
      Fly;

      // $FF: synthetic method
      private static HitParticles.Physics[] $values() {
         return new HitParticles.Physics[]{Fall, Fly};
      }
   }

   public static enum ColorMode {
      Custom,
      Sync;

      // $FF: synthetic method
      private static HitParticles.ColorMode[] $values() {
         return new HitParticles.ColorMode[]{Custom, Sync};
      }
   }

   public class Particle {
      float x;
      float y;
      float z;
      float px;
      float py;
      float pz;
      float motionX;
      float motionY;
      float motionZ;
      float rotationAngle;
      float rotationSpeed;
      float health;
      long time;
      Color color;

      public Particle(float x, float y, float z, Color color, float rotationAngle, float rotationSpeed, float health) {
         this.x = x;
         this.y = y;
         this.z = z;
         this.px = x;
         this.py = y;
         this.pz = z;
         this.motionX = MathUtility.random(-((float)(Integer)HitParticles.this.speed.getValue()) / 50.0F, (float)(Integer)HitParticles.this.speed.getValue() / 50.0F);
         this.motionY = MathUtility.random(-((float)(Integer)HitParticles.this.speed.getValue()) / 50.0F, (float)(Integer)HitParticles.this.speed.getValue() / 50.0F);
         this.motionZ = MathUtility.random(-((float)(Integer)HitParticles.this.speed.getValue()) / 50.0F, (float)(Integer)HitParticles.this.speed.getValue() / 50.0F);
         this.time = System.currentTimeMillis();
         this.color = color;
         this.rotationAngle = rotationAngle;
         this.rotationSpeed = rotationSpeed;
         this.health = health;
      }

      public long getTime() {
         return this.time;
      }

      public boolean update() {
         double sp = Math.sqrt((double)(this.motionX * this.motionX + this.motionZ * this.motionZ));
         this.px = this.x;
         this.py = this.y;
         this.pz = this.z;
         this.x += this.motionX;
         this.y += this.motionY;
         this.z += this.motionZ;
         if (this.posBlock((double)this.x, (double)(this.y - (Float)HitParticles.this.starsScale.getValue() / 10.0F), (double)this.z)) {
            this.motionY = -this.motionY / 1.1F;
            this.motionX /= 1.1F;
            this.motionZ /= 1.1F;
         } else if (this.posBlock((double)this.x - sp, (double)this.y, (double)this.z - sp) || this.posBlock((double)this.x + sp, (double)this.y, (double)this.z + sp) || this.posBlock((double)this.x + sp, (double)this.y, (double)this.z - sp) || this.posBlock((double)this.x - sp, (double)this.y, (double)this.z + sp) || this.posBlock((double)this.x + sp, (double)this.y, (double)this.z) || this.posBlock((double)this.x - sp, (double)this.y, (double)this.z) || this.posBlock((double)this.x, (double)this.y, (double)this.z + sp) || this.posBlock((double)this.x, (double)this.y, (double)this.z - sp)) {
            this.motionX = -this.motionX;
            this.motionZ = -this.motionZ;
         }

         if (HitParticles.this.physics.getValue() == HitParticles.Physics.Fall) {
            this.motionY -= 0.035F;
         }

         this.motionX /= 1.005F;
         this.motionZ /= 1.005F;
         this.motionY /= 1.005F;
         return System.currentTimeMillis() - this.getTime() > (long)((Integer)HitParticles.this.lifeTime.getValue() * 1000);
      }

      public void render(class_4587 matrixStack) {
         float size = (Float)HitParticles.this.starsScale.getValue();
         float scale = HitParticles.this.mode.is(HitParticles.Mode.Text) ? 0.025F * size : 0.07F;
         double posX = Render2DEngine.interpolate((double)this.px, (double)this.x, (double)Render3DEngine.getTickDelta()) - Module.mc.method_1561().field_4686.method_19326().method_10216();
         double posY = Render2DEngine.interpolate((double)this.py, (double)this.y, (double)Render3DEngine.getTickDelta()) + 0.1D - Module.mc.method_1561().field_4686.method_19326().method_10214();
         double posZ = Render2DEngine.interpolate((double)this.pz, (double)this.z, (double)Render3DEngine.getTickDelta()) - Module.mc.method_1561().field_4686.method_19326().method_10215();
         matrixStack.method_22903();
         matrixStack.method_22904(posX, posY, posZ);
         matrixStack.method_22905(scale, scale, scale);
         matrixStack.method_46416(size / 2.0F, size / 2.0F, size / 2.0F);
         matrixStack.method_22907(class_7833.field_40716.rotationDegrees(-Module.mc.field_1773.method_19418().method_19330()));
         matrixStack.method_22907(class_7833.field_40714.rotationDegrees(Module.mc.field_1773.method_19418().method_19329()));
         if (HitParticles.this.mode.is(HitParticles.Mode.Text)) {
            matrixStack.method_22907(class_7833.field_40718.rotationDegrees(180.0F));
         } else {
            matrixStack.method_22907(class_7833.field_40718.rotationDegrees(this.rotationAngle += AnimationUtility.deltaTime() * this.rotationSpeed));
         }

         matrixStack.method_46416(-size / 2.0F, -size / 2.0F, -size / 2.0F);
         switch(((HitParticles.Mode)HitParticles.this.mode.getValue()).ordinal()) {
         case 0:
            Render2DEngine.drawOrbiz(matrixStack, 0.0F, 0.3D, this.color);
            Render2DEngine.drawOrbiz(matrixStack, -0.1F, 0.5D, this.color);
            Render2DEngine.drawOrbiz(matrixStack, -0.2F, 0.7D, this.color);
            break;
         case 1:
            Render2DEngine.drawStar(matrixStack, this.color, size);
            break;
         case 2:
            Render2DEngine.drawHeart(matrixStack, this.color, size);
            break;
         case 3:
            Render2DEngine.drawBloom(matrixStack, this.color, size);
            break;
         case 4:
            FontRenderers.sf_medium.drawCenteredString(matrixStack, MathUtility.round2((double)this.health) + " ", 0.0D, 0.0D, (this.health > 0.0F ? (ColorSetting)HitParticles.this.colorH.getValue() : (ColorSetting)HitParticles.this.colorD.getValue()).getColorObject());
         }

         matrixStack.method_22905(0.8F, 0.8F, 0.8F);
         matrixStack.method_22909();
      }

      private boolean posBlock(double x, double y, double z) {
         class_2248 b = Module.mc.field_1687.method_8320(class_2338.method_49637(x, y, z)).method_26204();
         return !(b instanceof class_2189) && b != class_2246.field_10382 && b != class_2246.field_10164;
      }
   }
}
