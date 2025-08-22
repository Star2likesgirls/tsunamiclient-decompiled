package tsunami.injection;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.class_1268;
import net.minecraft.class_1306;
import net.minecraft.class_1657;
import net.minecraft.class_1799;
import net.minecraft.class_1806;
import net.minecraft.class_3532;
import net.minecraft.class_4587;
import net.minecraft.class_4597;
import net.minecraft.class_742;
import net.minecraft.class_759;
import net.minecraft.class_7833;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
import tsunami.TsunamiClient;
import tsunami.core.Managers;
import tsunami.core.manager.client.ModuleManager;
import tsunami.events.impl.EventHeldItemRenderer;
import tsunami.features.modules.Module;

@Mixin({class_759.class})
public abstract class MixinHeldItemRenderer {
   @Inject(
      method = {"renderFirstPersonItem"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/client/render/item/HeldItemRenderer;renderItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"
)}
   )
   private void onRenderItem(class_742 player, float tickDelta, float pitch, class_1268 hand, float swingProgress, class_1799 item, float equipProgress, class_4587 matrices, class_4597 vertexConsumers, int light, CallbackInfo ci) {
      if (!Module.fullNullCheck()) {
         EventHeldItemRenderer event = new EventHeldItemRenderer(hand, item, equipProgress, matrices);
         TsunamiClient.EVENT_BUS.post((Object)event);
      }
   }

   @Inject(
      method = {"renderFirstPersonItem"},
      at = {@At("RETURN")}
   )
   private void onRenderItemPost(class_742 player, float tickDelta, float pitch, class_1268 hand, float swingProgress, class_1799 item, float equipProgress, class_4587 matrices, class_4597 vertexConsumers, int light, CallbackInfo ci) {
      if (ModuleManager.chams.isEnabled() && (Boolean)ModuleManager.chams.handItems.getValue()) {
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      }

   }

   @Inject(
      method = {"renderFirstPersonItem"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onRenderItemHook(class_742 player, float tickDelta, float pitch, class_1268 hand, float swingProgress, class_1799 item, float equipProgress, class_4587 matrices, class_4597 vertexConsumers, int light, CallbackInfo ci) {
      if (Managers.MODULE != null && ModuleManager.animations.shouldAnimate() && !item.method_7960() && !(item.method_7909() instanceof class_1806)) {
         ci.cancel();
         ModuleManager.animations.renderFirstPersonItemCustom(player, tickDelta, pitch, hand, swingProgress, item, equipProgress, matrices, vertexConsumers, light);
      }

   }

   private void applyEatOrDrinkTransformationCustom(class_4587 matrices, float tickDelta, class_1306 arm, @NotNull class_1799 stack) {
      float f = (float)Module.mc.field_1724.method_6014() - tickDelta + 1.0F;
      float g = f / (float)stack.method_7935(Module.mc.field_1724);
      float h;
      if (g < 0.8F) {
         h = class_3532.method_15379(class_3532.method_15362(f / 4.0F * 3.1415927F) * 0.005F);
         matrices.method_46416(0.0F, h, 0.0F);
      }

      h = 1.0F - (float)Math.pow((double)g, 27.0D);
      int i = arm == class_1306.field_6183 ? 1 : -1;
      matrices.method_46416(h * 0.6F * (float)i * (Float)ModuleManager.viewModel.eatX.getValue(), h * -0.5F * (Float)ModuleManager.viewModel.eatY.getValue(), h * 0.0F);
      matrices.method_22907(class_7833.field_40716.rotationDegrees((float)i * h * 90.0F));
      matrices.method_22907(class_7833.field_40714.rotationDegrees(h * 10.0F));
      matrices.method_22907(class_7833.field_40718.rotationDegrees((float)i * h * 30.0F));
   }

   @Inject(
      method = {"applyEatOrDrinkTransformation"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void applyEatOrDrinkTransformationHook(class_4587 matrices, float tickDelta, class_1306 arm, class_1799 stack, class_1657 player, CallbackInfo ci) {
      if (ModuleManager.animations.isEnabled()) {
         this.applyEatOrDrinkTransformationCustom(matrices, tickDelta, arm, stack);
         ci.cancel();
      }

   }

   @ModifyArgs(
      method = {"renderItem(FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;Lnet/minecraft/client/network/ClientPlayerEntity;I)V"},
      at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/client/render/item/HeldItemRenderer;renderFirstPersonItem(Lnet/minecraft/client/network/AbstractClientPlayerEntity;FFLnet/minecraft/util/Hand;FLnet/minecraft/item/ItemStack;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"
)
   )
   private void renderItem(Args args) {
      if ((Boolean)ModuleManager.noRender.noSwing.getValue()) {
         args.set(6, 0.0F);
      }

   }
}
