package tsunami.injection;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.class_1297;
import net.minecraft.class_1799;
import net.minecraft.class_1810;
import net.minecraft.class_1829;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_239;
import net.minecraft.class_243;
import net.minecraft.class_332;
import net.minecraft.class_3532;
import net.minecraft.class_3965;
import net.minecraft.class_4184;
import net.minecraft.class_4587;
import net.minecraft.class_5498;
import net.minecraft.class_5912;
import net.minecraft.class_757;
import net.minecraft.class_7833;
import net.minecraft.class_9779;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tsunami.TsunamiClient;
import tsunami.core.Managers;
import tsunami.core.manager.client.ModuleManager;
import tsunami.features.modules.Module;
import tsunami.features.modules.client.ClientSettings;
import tsunami.features.modules.player.NoEntityTrace;
import tsunami.utility.math.FrameRateCounter;
import tsunami.utility.render.BlockAnimationUtility;
import tsunami.utility.render.Render3DEngine;
import tsunami.utility.render.shaders.satin.impl.ReloadableShaderEffectManager;

@Mixin({class_757.class})
public abstract class MixinGameRenderer {
   @Shadow
   private float field_4005;
   @Shadow
   private float field_3988;
   @Shadow
   private float field_4004;
   @Shadow
   private float field_4025;

   @Shadow
   public abstract void method_3182();

   @Inject(
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/util/profiler/Profiler;pop()V",
   ordinal = 1,
   shift = Shift.BEFORE
)},
      method = {"render"}
   )
   void postHudRenderHook(class_9779 tickCounter, boolean tick, CallbackInfo ci) {
      FrameRateCounter.INSTANCE.recordFrame();
   }

   @Inject(
      at = {@At(
   value = "FIELD",
   target = "Lnet/minecraft/client/render/GameRenderer;renderHand:Z",
   opcode = 180,
   ordinal = 0
)},
      method = {"renderWorld"}
   )
   void render3dHook(class_9779 tickCounter, CallbackInfo ci) {
      if (!Module.fullNullCheck()) {
         class_4184 camera = Module.mc.field_1773.method_19418();
         class_4587 matrixStack = new class_4587();
         RenderSystem.getModelViewStack().pushMatrix().mul(matrixStack.method_23760().method_23761());
         matrixStack.method_22907(class_7833.field_40714.rotationDegrees(camera.method_19329()));
         matrixStack.method_22907(class_7833.field_40716.rotationDegrees(camera.method_19330() + 180.0F));
         RenderSystem.applyModelViewMatrix();
         Render3DEngine.lastProjMat.set(RenderSystem.getProjectionMatrix());
         Render3DEngine.lastModMat.set(RenderSystem.getModelViewMatrix());
         Render3DEngine.lastWorldSpaceMatrix.set(matrixStack.method_23760().method_23761());
         Managers.MODULE.onRender3D(matrixStack);
         BlockAnimationUtility.onRender(matrixStack);
         Render3DEngine.onRender3D(matrixStack);
         RenderSystem.getModelViewStack().popMatrix();
         RenderSystem.applyModelViewMatrix();
      }
   }

   @Inject(
      method = {"renderWorld"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/client/render/GameRenderer;renderHand(Lnet/minecraft/client/render/Camera;FLorg/joml/Matrix4f;)V",
   shift = Shift.AFTER
)}
   )
   public void postRender3dHook(class_9779 tickCounter, CallbackInfo ci) {
      if (!Module.fullNullCheck()) {
         Managers.SHADER.renderShaders();
      }
   }

   @Redirect(
      method = {"renderWorld"},
      at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/util/math/MathHelper;lerp(FFF)F"
)
   )
   private float renderWorldHook(float delta, float first, float second) {
      return ModuleManager.noRender.isEnabled() && (Boolean)ModuleManager.noRender.nausea.getValue() ? 0.0F : class_3532.method_16439(delta, first, second);
   }

   @Inject(
      method = {"loadPrograms"},
      at = {@At("RETURN")}
   )
   private void loadSatinPrograms(class_5912 factory, CallbackInfo ci) {
      ReloadableShaderEffectManager.INSTANCE.reload(factory);
   }

   @Inject(
      method = {"updateCrosshairTarget"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/client/render/GameRenderer;findCrosshairTarget(Lnet/minecraft/entity/Entity;DDF)Lnet/minecraft/util/hit/HitResult;"
)},
      cancellable = true
   )
   private void onUpdateTargetedEntity(float tickDelta, CallbackInfo info) {
      if (!Module.fullNullCheck()) {
         if (ModuleManager.freeCam.isEnabled()) {
            Module.mc.method_16011().method_15407();
            info.cancel();
            Module.mc.field_1765 = Managers.PLAYER.getRtxTarget(ModuleManager.freeCam.getFakeYaw(), ModuleManager.freeCam.getFakePitch(), ModuleManager.freeCam.getFakeX(), ModuleManager.freeCam.getFakeY(), ModuleManager.freeCam.getFakeZ());
         }

      }
   }

   @Inject(
      method = {"findCrosshairTarget"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void findCrosshairTargetHook(class_1297 camera, double blockInteractionRange, double entityInteractionRange, float tickDelta, CallbackInfoReturnable<class_239> cir) {
      if (ModuleManager.noEntityTrace.isEnabled() && (Module.mc.field_1724.method_6047().method_7909() instanceof class_1810 || !(Boolean)NoEntityTrace.ponly.getValue())) {
         if (Module.mc.field_1724.method_6047().method_7909() instanceof class_1829 && (Boolean)NoEntityTrace.noSword.getValue()) {
            return;
         }

         double d = Math.max(blockInteractionRange, entityInteractionRange);
         class_243 vec3d = camera.method_5836(tickDelta);
         class_239 hitResult = camera.method_5745(d, tickDelta, false);
         cir.setReturnValue(this.ensureTargetInRangeCustom(hitResult, vec3d, blockInteractionRange));
      }

   }

   @Inject(
      method = {"getBasicProjectionMatrix"},
      at = {@At("TAIL")},
      cancellable = true
   )
   public void getBasicProjectionMatrixHook(double fov, CallbackInfoReturnable<Matrix4f> cir) {
      if (ModuleManager.aspectRatio.isEnabled()) {
         class_4587 matrixStack = new class_4587();
         matrixStack.method_23760().method_23761().identity();
         if (this.field_4005 != 1.0F) {
            matrixStack.method_46416(this.field_3988, -this.field_4004, 0.0F);
            matrixStack.method_22905(this.field_4005, this.field_4005, 1.0F);
         }

         matrixStack.method_23760().method_23761().mul((new Matrix4f()).setPerspective((float)(fov * 0.01745329238474369D), (Float)ModuleManager.aspectRatio.ratio.getValue(), 0.05F, this.field_4025 * 4.0F));
         cir.setReturnValue(matrixStack.method_23760().method_23761());
      }

   }

   @Inject(
      method = {"getFov(Lnet/minecraft/client/render/Camera;FZ)D"},
      at = {@At("TAIL")},
      cancellable = true
   )
   public void getFov(class_4184 camera, float tickDelta, boolean changingFov, CallbackInfoReturnable<Double> cb) {
      if (ModuleManager.fov.isEnabled()) {
         if ((Double)cb.getReturnValue() == 70.0D && !(Boolean)ModuleManager.fov.itemFov.getValue() && Module.mc.field_1690.method_31044() != class_5498.field_26664) {
            return;
         }

         if ((Boolean)ModuleManager.fov.itemFov.getValue() && (Double)cb.getReturnValue() == 70.0D) {
            cb.setReturnValue(((Integer)ModuleManager.fov.itemFovModifier.getValue()).doubleValue());
            return;
         }

         if (Module.mc.field_1724.method_5869()) {
            return;
         }

         cb.setReturnValue(((Integer)ModuleManager.fov.fovModifier.getValue()).doubleValue());
      }

   }

   @Inject(
      method = {"bobView"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void bobViewHook(class_4587 matrices, float tickDelta, CallbackInfo ci) {
      if (!Module.fullNullCheck()) {
         if (ModuleManager.noBob.isEnabled()) {
            ModuleManager.noBob.bobView(matrices, tickDelta);
            ci.cancel();
         } else {
            if ((Boolean)ClientSettings.customBob.getValue()) {
               TsunamiClient.core.bobView(matrices, tickDelta);
               ci.cancel();
            }

         }
      }
   }

   @Unique
   private class_239 ensureTargetInRangeCustom(class_239 hitResult, class_243 cameraPos, double interactionRange) {
      class_243 vec3d = hitResult.method_17784();
      if (!vec3d.method_24802(cameraPos, interactionRange)) {
         class_243 vec3d2 = hitResult.method_17784();
         class_2350 direction = class_2350.method_10142(vec3d2.field_1352 - cameraPos.field_1352, vec3d2.field_1351 - cameraPos.field_1351, vec3d2.field_1350 - cameraPos.field_1350);
         return class_3965.method_17778(vec3d2, direction, class_2338.method_49638(vec3d2));
      } else {
         return hitResult;
      }
   }

   @Inject(
      method = {"showFloatingItem"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void showFloatingItemHook(class_1799 floatingItem, CallbackInfo info) {
      if (ModuleManager.totemAnimation.isEnabled()) {
         ModuleManager.totemAnimation.showFloatingItem(floatingItem);
         info.cancel();
      }

   }

   @Inject(
      method = {"renderFloatingItem"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void renderFloatingItemHook(class_332 context, float tickDelta, CallbackInfo ci) {
      if (ModuleManager.totemAnimation.isEnabled()) {
         ModuleManager.totemAnimation.renderFloatingItem(tickDelta);
         ci.cancel();
      }

   }

   @Inject(
      method = {"tiltViewWhenHurt"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void tiltViewWhenHurtHook(class_4587 matrices, float tickDelta, CallbackInfo ci) {
      if (ModuleManager.noRender.isEnabled() && (Boolean)ModuleManager.noRender.hurtCam.getValue()) {
         ci.cancel();
      }

   }
}
