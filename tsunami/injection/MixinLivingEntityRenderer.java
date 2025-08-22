package tsunami.injection;

import java.util.Iterator;
import java.util.List;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_1657;
import net.minecraft.class_2350;
import net.minecraft.class_310;
import net.minecraft.class_3532;
import net.minecraft.class_3887;
import net.minecraft.class_4050;
import net.minecraft.class_4587;
import net.minecraft.class_4597;
import net.minecraft.class_583;
import net.minecraft.class_922;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
import tsunami.TsunamiClient;
import tsunami.core.Managers;
import tsunami.core.manager.client.ModuleManager;
import tsunami.features.modules.Module;
import tsunami.features.modules.client.ClientSettings;
import tsunami.injection.accesors.IClientPlayerEntity;
import tsunami.utility.math.MathUtility;
import tsunami.utility.render.Render2DEngine;
import tsunami.utility.render.Render3DEngine;

@Mixin({class_922.class})
public abstract class MixinLivingEntityRenderer<T extends class_1309, M extends class_583<T>> {
   private class_1309 lastEntity;
   private float originalHeadYaw;
   private float originalPrevHeadYaw;
   private float originalPrevHeadPitch;
   private float originalHeadPitch;
   @Shadow
   protected M field_4737;
   @Shadow
   @Final
   protected List<class_3887<T, M>> field_4738;

   @Inject(
      method = {"render"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void onRenderPre(T livingEntity, float f, float g, class_4587 matrixStack, class_4597 vertexConsumerProvider, int i, CallbackInfo ci) {
      if (!Module.fullNullCheck()) {
         if (Module.mc.field_1724 != null && livingEntity == Module.mc.field_1724 && Module.mc.field_1724.method_49694() == null && (Boolean)ClientSettings.renderRotations.getValue() && !TsunamiClient.isFuturePresent()) {
            this.originalHeadYaw = livingEntity.field_6241;
            this.originalPrevHeadYaw = livingEntity.field_6259;
            this.originalPrevHeadPitch = livingEntity.field_6004;
            this.originalHeadPitch = livingEntity.method_36455();
            livingEntity.method_36457(((IClientPlayerEntity)class_310.method_1551().field_1724).getLastPitch());
            livingEntity.field_6004 = Managers.PLAYER.lastPitch;
            livingEntity.field_6241 = ((IClientPlayerEntity)class_310.method_1551().field_1724).getLastYaw();
            livingEntity.field_6283 = Render2DEngine.interpolateFloat(Managers.PLAYER.prevBodyYaw, Managers.PLAYER.bodyYaw, (double)Render3DEngine.getTickDelta());
            livingEntity.field_6259 = Managers.PLAYER.lastYaw;
            livingEntity.field_6220 = Render2DEngine.interpolateFloat(Managers.PLAYER.prevBodyYaw, Managers.PLAYER.bodyYaw, (double)Render3DEngine.getTickDelta());
         }

         if (livingEntity != Module.mc.field_1724 && ModuleManager.freeCam.isEnabled() && (Boolean)ModuleManager.freeCam.track.getValue() && ModuleManager.freeCam.trackEntity != null && ModuleManager.freeCam.trackEntity == livingEntity) {
            ci.cancel();
         } else {
            this.lastEntity = livingEntity;
            if (livingEntity instanceof class_1657) {
               class_1657 pe = (class_1657)livingEntity;
               if (ModuleManager.chams.isEnabled() && (Boolean)ModuleManager.chams.players.getValue()) {
                  ModuleManager.chams.renderPlayer(pe, f, g, matrixStack, i, this.field_4737, ci, () -> {
                     this.postRender(livingEntity);
                  });
                  if (!pe.method_7325()) {
                     matrixStack.method_22903();
                     float h = class_3532.method_17821(g, pe.field_6220, pe.field_6283);
                     float j = class_3532.method_17821(g, pe.field_6259, pe.field_6241);
                     float k = j - h;
                     class_1297 entity;
                     float l;
                     if (pe.method_5765() && (entity = pe.method_5854()) instanceof class_1309) {
                        class_1309 livingEntity2 = (class_1309)entity;
                        h = class_3532.method_17821(g, livingEntity2.field_6220, livingEntity2.field_6283);
                        k = j - h;
                        l = class_3532.method_15393(k);
                        if (l < -85.0F) {
                           l = -85.0F;
                        }

                        if (l >= 85.0F) {
                           l = 85.0F;
                        }

                        h = j - l;
                        if (l * l > 2500.0F) {
                           h += l * 0.2F;
                        }

                        k = j - h;
                     }

                     float m = class_3532.method_16439(g, pe.field_6004, pe.method_36455());
                     if (class_922.method_38563(pe)) {
                        m *= -1.0F;
                        k *= -1.0F;
                     }

                     float n;
                     class_2350 direction;
                     if (pe.method_41328(class_4050.field_18078) && (direction = pe.method_18401()) != null) {
                        n = pe.method_18381(class_4050.field_18076) - 0.1F;
                        matrixStack.method_46416((float)(-direction.method_10148()) * n, 0.0F, (float)(-direction.method_10165()) * n);
                     }

                     l = (float)pe.field_6012 + g;
                     ModuleManager.chams.setupTransforms1(pe, matrixStack, l, h, g);
                     matrixStack.method_22905(-1.0F, -1.0F, 1.0F);
                     matrixStack.method_22905(0.9375F, 0.9375F, 0.9375F);
                     matrixStack.method_46416(0.0F, -1.501F, 0.0F);
                     n = 0.0F;
                     float o = 0.0F;
                     if (!pe.method_5765() && pe.method_5805()) {
                        n = pe.field_42108.method_48570(g);
                        o = pe.field_42108.method_48572(g);
                        if (pe.method_6109()) {
                           o *= 3.0F;
                        }

                        if (n > 1.0F) {
                           n = 1.0F;
                        }
                     }

                     Iterator var18 = this.field_4738.iterator();

                     while(var18.hasNext()) {
                        class_3887<T, M> featureRenderer = (class_3887)var18.next();
                        featureRenderer.method_4199(matrixStack, vertexConsumerProvider, i, livingEntity, o, n, g, l, k, m);
                     }

                     matrixStack.method_22909();
                  }
               }
            }

         }
      }
   }

   @Unique
   public void postRender(T livingEntity) {
      if (!Module.fullNullCheck()) {
         if (Module.mc.field_1724 != null && livingEntity == Module.mc.field_1724 && Module.mc.field_1724.method_49694() == null && (Boolean)ClientSettings.renderRotations.getValue() && !TsunamiClient.isFuturePresent()) {
            livingEntity.field_6004 = this.originalPrevHeadPitch;
            livingEntity.method_36457(this.originalHeadPitch);
            livingEntity.field_6241 = this.originalHeadYaw;
            livingEntity.field_6259 = this.originalPrevHeadYaw;
         }

      }
   }

   @Inject(
      method = {"render"},
      at = {@At("TAIL")}
   )
   public void onRenderPost(T livingEntity, float f, float g, class_4587 matrixStack, class_4597 vertexConsumerProvider, int i, CallbackInfo ci) {
      if (!Module.fullNullCheck()) {
         this.postRender(livingEntity);
      }
   }

   @ModifyArgs(
      method = {"render"},
      at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/client/render/entity/model/EntityModel;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;III)V"
)
   )
   private void renderHook(Args args) {
      if (!Module.fullNullCheck()) {
         float alpha = -1.0F;
         class_1657 pl;
         class_1309 var4;
         if (ModuleManager.noRender.isEnabled() && (Boolean)ModuleManager.noRender.antiPlayerCollision.getValue() && this.lastEntity != Module.mc.field_1724) {
            var4 = this.lastEntity;
            if (var4 instanceof class_1657) {
               pl = (class_1657)var4;
               if (!pl.method_5767()) {
                  alpha = MathUtility.clamp((float)(Module.mc.field_1724.method_5707(this.lastEntity.method_19538()) / 3.0D) + 0.2F, 0.0F, 1.0F);
               }
            }
         }

         if (this.lastEntity != Module.mc.field_1724) {
            var4 = this.lastEntity;
            if (var4 instanceof class_1657) {
               pl = (class_1657)var4;
               if (pl.method_5767() && ModuleManager.serverHelper.isEnabled() && (Boolean)ModuleManager.serverHelper.trueSight.getValue()) {
                  alpha = 0.3F;
               }
            }
         }

         if (alpha != -1.0F) {
            args.set(4, Render2DEngine.applyOpacity(654311423, alpha));
         }

      }
   }
}
