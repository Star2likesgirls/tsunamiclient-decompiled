package tsunami.injection;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.util.Optional;
import java.util.function.Consumer;
import net.minecraft.class_156;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_3532;
import net.minecraft.class_4011;
import net.minecraft.class_4071;
import net.minecraft.class_425;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tsunami.core.manager.client.ModuleManager;
import tsunami.features.modules.Module;
import tsunami.features.modules.client.ClientSettings;
import tsunami.utility.render.Render2DEngine;
import tsunami.utility.render.TextureStorage;

@Mixin({class_425.class})
public abstract class MixinSplashOverlay {
   @Final
   @Shadow
   private boolean field_18219;
   @Shadow
   private float field_17770;
   @Shadow
   private long field_17771 = -1L;
   @Shadow
   private long field_18220 = -1L;
   @Final
   @Shadow
   private class_4011 field_17767;
   @Final
   @Shadow
   private Consumer<Optional<Throwable>> field_18218;

   @Inject(
      method = {"render"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void render(class_332 context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
      if (!ModuleManager.unHook.isEnabled() && (Boolean)ClientSettings.customLoadingScreen.getValue()) {
         ci.cancel();
         this.renderCustom(context, mouseX, mouseY, delta);
      }
   }

   public void renderCustom(class_332 context, int mouseX, int mouseY, float delta) {
      int i = Module.mc.method_22683().method_4486();
      int j = Module.mc.method_22683().method_4502();
      long l = class_156.method_658();
      if (this.field_18219 && this.field_18220 == -1L) {
         this.field_18220 = l;
      }

      float f = this.field_17771 > -1L ? (float)(l - this.field_17771) / 1000.0F : -1.0F;
      float g = this.field_18220 > -1L ? (float)(l - this.field_18220) / 500.0F : -1.0F;
      float h;
      int k;
      float t;
      if (f >= 1.0F) {
         if (Module.mc.field_1755 != null) {
            Module.mc.field_1755.method_25394(context, 0, 0, delta);
         }

         k = class_3532.method_15386((1.0F - class_3532.method_15363(f - 1.0F, 0.0F, 1.0F)) * 255.0F);
         context.method_25294(0, 0, i, j, withAlpha((new Color(458773)).getRGB(), k));
         h = 1.0F - class_3532.method_15363(f - 1.0F, 0.0F, 1.0F);
      } else if (this.field_18219) {
         if (Module.mc.field_1755 != null && g < 1.0F) {
            Module.mc.field_1755.method_25394(context, mouseX, mouseY, delta);
         }

         k = class_3532.method_15384(class_3532.method_15350((double)g, 0.15D, 1.0D) * 255.0D);
         context.method_25294(0, 0, i, j, withAlpha((new Color(458773)).getRGB(), k));
         h = class_3532.method_15363(g, 0.0F, 1.0F);
      } else {
         k = (new Color(458773)).getRGB();
         float m = (float)(k >> 16 & 255) / 255.0F;
         t = (float)(k >> 8 & 255) / 255.0F;
         float o = (float)(k & 255) / 255.0F;
         GlStateManager._clearColor(m, t, o, 1.0F);
         GlStateManager._clear(16384, class_310.field_1703);
         h = 1.0F;
      }

      k = (int)((double)context.method_51421() * 0.5D);
      int p = (int)((double)context.method_51443() * 0.5D);
      RenderSystem.enableBlend();
      RenderSystem.blendFunc(770, 1);
      RenderSystem.setShaderColor(0.1F, 0.1F, 0.1F, h);
      context.method_25290(TextureStorage.thLogo, k - 150, p - 35, 0.0F, 0.0F, 300, 70, 300, 70);
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, h);
      Render2DEngine.addWindow(context.method_51448(), (float)(k - 150), (float)(p - 35), (float)(k - 150) + 300.0F * this.field_17770, (float)(p + 35), 1.0D);
      context.method_25290(TextureStorage.thLogo, k - 150, p - 35, 0.0F, 0.0F, 300, 70, 300, 70);
      Render2DEngine.popWindow();
      t = this.field_17767.method_18229();
      this.field_17770 = class_3532.method_15363(this.field_17770 * 0.95F + t * 0.050000012F, 0.0F, 1.0F);
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      RenderSystem.defaultBlendFunc();
      RenderSystem.disableBlend();
      if (f >= 2.0F) {
         Module.mc.method_18502((class_4071)null);
      }

      if (this.field_17771 == -1L && this.field_17767.method_18787() && (!this.field_18219 || g >= 2.0F)) {
         try {
            this.field_17767.method_18849();
            this.field_18218.accept(Optional.empty());
         } catch (Throwable var16) {
            this.field_18218.accept(Optional.of(var16));
         }

         this.field_17771 = class_156.method_658();
         if (Module.mc.field_1755 != null) {
            Module.mc.field_1755.method_25423(Module.mc, Module.mc.method_22683().method_4486(), Module.mc.method_22683().method_4502());
         }
      }

   }

   private static int withAlpha(int color, int alpha) {
      return color & 16777215 | alpha << 24;
   }
}
