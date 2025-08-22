package tsunami.features.modules.render;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.ArrayList;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_243;
import net.minecraft.class_4587;
import net.minecraft.class_7833;
import tsunami.core.Managers;
import tsunami.core.manager.client.ModuleManager;
import tsunami.events.impl.EventAttack;
import tsunami.features.modules.Module;
import tsunami.injection.accesors.IClientPlayerEntity;
import tsunami.setting.Setting;
import tsunami.utility.Timer;
import tsunami.utility.render.Render2DEngine;

public class HitBubbles extends Module {
   public final Setting<Integer> lifeTime = new Setting("LifeTime", 30, 1, 150);
   private final ArrayList<HitBubbles.HitBubble> bubbles = new ArrayList();

   public HitBubbles() {
      super("HitBubbles", Module.Category.NONE);
   }

   @EventHandler
   public void onHit(EventAttack e) {
      class_243 point = Managers.PLAYER.getRtxPoint(((IClientPlayerEntity)mc.field_1724).getLastYaw(), ((IClientPlayerEntity)mc.field_1724).getLastPitch(), (Float)ModuleManager.aura.attackRange.getValue());
      if (point != null && !e.isPre()) {
         this.bubbles.add(new HitBubbles.HitBubble((float)point.field_1352, (float)point.field_1351, (float)point.field_1350, -((IClientPlayerEntity)mc.field_1724).getLastYaw(), ((IClientPlayerEntity)mc.field_1724).getLastPitch(), new Timer()));
      }

   }

   public void onRender3D(class_4587 matrixStack) {
      RenderSystem.disableDepthTest();
      ArrayList<HitBubbles.HitBubble> bubblesCopy = Lists.newArrayList(this.bubbles);
      bubblesCopy.forEach((b) -> {
         matrixStack.method_22903();
         matrixStack.method_22904((double)b.x - mc.method_1561().field_4686.method_19326().method_10216(), (double)b.y - mc.method_1561().field_4686.method_19326().method_10214(), (double)b.z - mc.method_1561().field_4686.method_19326().method_10215());
         matrixStack.method_22907(class_7833.field_40716.rotationDegrees(b.yaw));
         matrixStack.method_22907(class_7833.field_40714.rotationDegrees(b.pitch));
         Render2DEngine.drawBubble(matrixStack, (float)(-b.life.getPassedTimeMs()) / 4.0F, (float)b.life.getPassedTimeMs() / 1500.0F);
         matrixStack.method_22909();
      });
      RenderSystem.enableDepthTest();
      this.bubbles.removeIf((b) -> {
         return b.life.passedMs((long)((Integer)this.lifeTime.getValue() * 50));
      });
   }

   public static record HitBubble(float x, float y, float z, float yaw, float pitch, Timer life) {
      public HitBubble(float x, float y, float z, float yaw, float pitch, Timer life) {
         this.x = x;
         this.y = y;
         this.z = z;
         this.yaw = yaw;
         this.pitch = pitch;
         this.life = life;
      }

      public float x() {
         return this.x;
      }

      public float y() {
         return this.y;
      }

      public float z() {
         return this.z;
      }

      public float yaw() {
         return this.yaw;
      }

      public float pitch() {
         return this.pitch;
      }

      public Timer life() {
         return this.life;
      }
   }
}
