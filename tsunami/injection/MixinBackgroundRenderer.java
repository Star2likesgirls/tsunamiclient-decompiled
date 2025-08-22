package tsunami.injection;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.class_1297;
import net.minecraft.class_4184;
import net.minecraft.class_758;
import net.minecraft.class_758.class_4596;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tsunami.core.manager.client.ModuleManager;
import tsunami.features.modules.render.WorldTweaks;
import tsunami.setting.impl.BooleanSettingGroup;
import tsunami.setting.impl.ColorSetting;

@Mixin({class_758.class})
public class MixinBackgroundRenderer {
   @Inject(
      method = {"applyFog"},
      at = {@At("TAIL")}
   )
   private static void onApplyFog(class_4184 camera, class_4596 fogType, float viewDistance, boolean thickFog, float tickDelta, CallbackInfo info) {
      if (ModuleManager.noRender.isEnabled() && (Boolean)ModuleManager.noRender.fog.getValue() && fogType == class_4596.field_20946) {
         RenderSystem.setShaderFogStart(viewDistance * 4.0F);
         RenderSystem.setShaderFogEnd(viewDistance * 4.25F);
      }

      if (ModuleManager.worldTweaks.isEnabled() && ((BooleanSettingGroup)WorldTweaks.fogModify.getValue()).isEnabled()) {
         RenderSystem.setShaderFogStart((float)(Integer)WorldTweaks.fogStart.getValue());
         RenderSystem.setShaderFogEnd((float)(Integer)WorldTweaks.fogEnd.getValue());
         RenderSystem.setShaderFogColor(((ColorSetting)WorldTweaks.fogColor.getValue()).getGlRed(), ((ColorSetting)WorldTweaks.fogColor.getValue()).getGlGreen(), ((ColorSetting)WorldTweaks.fogColor.getValue()).getGlBlue());
      }

   }

   @Inject(
      method = {"getFogModifier(Lnet/minecraft/entity/Entity;F)Lnet/minecraft/client/render/BackgroundRenderer$StatusEffectFogModifier;"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private static void onGetFogModifier(class_1297 entity, float tickDelta, CallbackInfoReturnable<Object> info) {
      if (ModuleManager.noRender.isEnabled() && (Boolean)ModuleManager.noRender.blindness.getValue()) {
         info.setReturnValue((Object)null);
      }

   }
}
