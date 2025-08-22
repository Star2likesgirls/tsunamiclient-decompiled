package tsunami.features.modules.render;

import com.mojang.blaze3d.platform.GlStateManager.class_4534;
import com.mojang.blaze3d.platform.GlStateManager.class_4535;
import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_1511;
import net.minecraft.class_1657;
import net.minecraft.class_2350;
import net.minecraft.class_243;
import net.minecraft.class_287;
import net.minecraft.class_289;
import net.minecraft.class_290;
import net.minecraft.class_2960;
import net.minecraft.class_3532;
import net.minecraft.class_4050;
import net.minecraft.class_4587;
import net.minecraft.class_4608;
import net.minecraft.class_583;
import net.minecraft.class_630;
import net.minecraft.class_742;
import net.minecraft.class_757;
import net.minecraft.class_7833;
import net.minecraft.class_892;
import net.minecraft.class_922;
import net.minecraft.class_293.class_5596;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tsunami.core.Managers;
import tsunami.events.impl.EventHeldItemRenderer;
import tsunami.features.modules.Module;
import tsunami.setting.Setting;
import tsunami.setting.impl.ColorSetting;
import tsunami.utility.render.Render2DEngine;
import tsunami.utility.render.TextureStorage;

public class Chams extends Module {
   public final Setting<Boolean> handItems = new Setting("HandItems", false);
   private final Setting<ColorSetting> handItemsColor = new Setting("HandItemsColor", new ColorSetting(new Color(-1827152291, true)), (v) -> {
      return (Boolean)this.handItems.getValue();
   });
   public final Setting<Boolean> crystals = new Setting("Crystals", false);
   private final Setting<ColorSetting> crystalColor = new Setting("CrystalColor", new ColorSetting(new Color(-1825711896, true)), (v) -> {
      return (Boolean)this.crystals.getValue();
   });
   private final Setting<Boolean> staticCrystal = new Setting("StaticCrystal", true, (v) -> {
      return (Boolean)this.crystals.getValue();
   });
   private final Setting<Chams.CMode> crystalMode;
   public final Setting<Boolean> players;
   private final Setting<ColorSetting> playerColor;
   private final Setting<ColorSetting> friendColor;
   private final Setting<Boolean> playerTexture;
   private final Setting<Boolean> simple;
   private final Setting<Boolean> alternativeBlending;
   private final class_2960 crystalTexture;
   private static final float SINE_45_DEGREES = (float)Math.sin(0.7853981633974483D);

   public Chams() {
      super("Chams", Module.Category.NONE);
      this.crystalMode = new Setting("CrystalMode", Chams.CMode.One, (v) -> {
         return (Boolean)this.crystals.getValue();
      });
      this.players = new Setting("Players", false);
      this.playerColor = new Setting("PlayerColor", new ColorSetting(new Color(-1825711896, true)), (v) -> {
         return (Boolean)this.players.getValue();
      });
      this.friendColor = new Setting("FriendColor", new ColorSetting(new Color(-1825707984, true)), (v) -> {
         return (Boolean)this.players.getValue();
      });
      this.playerTexture = new Setting("PlayerTexture", true, (v) -> {
         return (Boolean)this.players.getValue();
      });
      this.simple = new Setting("Simple", false, (v) -> {
         return (Boolean)this.players.getValue();
      });
      this.alternativeBlending = new Setting("AlternativeBlending", true);
      this.crystalTexture = class_2960.method_60654("textures/entity/end_crystal/end_crystal.png");
   }

   public void renderCrystal(class_1511 endCrystalEntity, float f, float g, class_4587 matrixStack, int i, class_630 core, class_630 frame) {
      RenderSystem.enableBlend();
      if ((Boolean)this.alternativeBlending.getValue()) {
         RenderSystem.blendFunc(class_4535.SRC_ALPHA, class_4534.ONE);
      } else {
         RenderSystem.defaultBlendFunc();
      }

      RenderSystem.disableCull();
      RenderSystem.disableDepthTest();
      class_287 buffer;
      if (this.crystalMode.getValue() != Chams.CMode.One) {
         if (this.crystalMode.getValue() == Chams.CMode.Three) {
            RenderSystem.setShaderTexture(0, this.crystalTexture);
         } else {
            RenderSystem.setShaderTexture(0, TextureStorage.crystalTexture2);
         }

         RenderSystem.setShader(class_757::method_34542);
         buffer = class_289.method_1348().method_60827(class_5596.field_27382, class_290.field_1585);
      } else {
         RenderSystem.setShader(class_757::method_34539);
         buffer = class_289.method_1348().method_60827(class_5596.field_27382, class_290.field_1592);
      }

      matrixStack.method_22903();
      float h = (Boolean)this.staticCrystal.getValue() ? -1.4F : class_892.method_23155(endCrystalEntity, g);
      float j = ((float)endCrystalEntity.field_7034 + g) * 3.0F;
      matrixStack.method_22903();
      RenderSystem.setShaderColor(((ColorSetting)this.crystalColor.getValue()).getGlRed(), ((ColorSetting)this.crystalColor.getValue()).getGlGreen(), ((ColorSetting)this.crystalColor.getValue()).getGlBlue(), ((ColorSetting)this.crystalColor.getValue()).getGlAlpha());
      matrixStack.method_22905(2.0F, 2.0F, 2.0F);
      matrixStack.method_46416(0.0F, -0.5F, 0.0F);
      int k = class_4608.field_21444;
      matrixStack.method_22907(class_7833.field_40716.rotationDegrees(j));
      matrixStack.method_46416(0.0F, 1.5F + h / 2.0F, 0.0F);
      matrixStack.method_22907((new Quaternionf()).setAngleAxis(1.0471976F, SINE_45_DEGREES, 0.0F, SINE_45_DEGREES));
      frame.method_22698(matrixStack, buffer, i, k);
      matrixStack.method_22905(0.875F, 0.875F, 0.875F);
      matrixStack.method_22907((new Quaternionf()).setAngleAxis(1.0471976F, SINE_45_DEGREES, 0.0F, SINE_45_DEGREES));
      matrixStack.method_22907(class_7833.field_40716.rotationDegrees(j));
      frame.method_22698(matrixStack, buffer, i, k);
      matrixStack.method_22905(0.875F, 0.875F, 0.875F);
      matrixStack.method_22907((new Quaternionf()).setAngleAxis(1.0471976F, SINE_45_DEGREES, 0.0F, SINE_45_DEGREES));
      matrixStack.method_22907(class_7833.field_40716.rotationDegrees(j));
      core.method_22698(matrixStack, buffer, i, k);
      matrixStack.method_22909();
      matrixStack.method_22909();
      Render2DEngine.endBuilding(buffer);
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      RenderSystem.disableBlend();
      RenderSystem.enableDepthTest();
      RenderSystem.enableCull();
   }

   public void renderPlayer(class_1657 pe, float f, float g, class_4587 matrixStack, int i, class_583 model, CallbackInfo ci, Runnable post) {
      RenderSystem.enableBlend();
      if ((Boolean)this.alternativeBlending.getValue()) {
         RenderSystem.blendFunc(class_4535.SRC_ALPHA, class_4534.ONE);
      } else {
         RenderSystem.defaultBlendFunc();
      }

      RenderSystem.enableCull();
      RenderSystem.disableDepthTest();
      class_287 buffer;
      if (!(Boolean)this.simple.getValue()) {
         RenderSystem.setShaderTexture(0, ((class_742)pe).method_52814().comp_1626());
         RenderSystem.setShader(class_757::method_34542);
         buffer = class_289.method_1348().method_60827(class_5596.field_27382, class_290.field_1585);
      } else {
         RenderSystem.setShader(class_757::method_34539);
         buffer = class_289.method_1348().method_60827(class_5596.field_27382, class_290.field_1592);
      }

      matrixStack.method_22903();
      if (Managers.FRIEND.isFriend(pe)) {
         RenderSystem.setShaderColor(((ColorSetting)this.friendColor.getValue()).getGlRed(), ((ColorSetting)this.friendColor.getValue()).getGlGreen(), ((ColorSetting)this.friendColor.getValue()).getGlBlue(), ((ColorSetting)this.friendColor.getValue()).getGlAlpha());
      } else {
         RenderSystem.setShaderColor(((ColorSetting)this.playerColor.getValue()).getGlRed(), ((ColorSetting)this.playerColor.getValue()).getGlGreen(), ((ColorSetting)this.playerColor.getValue()).getGlBlue(), ((ColorSetting)this.playerColor.getValue()).getGlAlpha());
      }

      model.field_3447 = pe.method_6055(g);
      model.field_3449 = pe.method_5765();
      model.field_3448 = false;
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
      this.setupTransforms1(pe, matrixStack, l, h, g);
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

      model.method_2816(pe, o, n, g);
      model.method_2819(pe, o, n, l, k, m);
      int p = class_922.method_23622(pe, 0.0F);
      model.method_60879(matrixStack, buffer, i, p);
      Render2DEngine.endBuilding(buffer);
      RenderSystem.disableBlend();
      RenderSystem.disableCull();
      matrixStack.method_22909();
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      RenderSystem.enableDepthTest();
      if (!(Boolean)this.playerTexture.getValue()) {
         ci.cancel();
         post.run();
      }

   }

   public void setupTransforms1(class_1657 abstractClientPlayerEntity, class_4587 matrixStack, float f, float g, float h) {
      float j = abstractClientPlayerEntity.method_6024(h);
      float k = abstractClientPlayerEntity.method_5695(h);
      float l;
      float m;
      if (abstractClientPlayerEntity.method_6128()) {
         this.setupTransforms(abstractClientPlayerEntity, matrixStack, f, g, h);
         l = (float)abstractClientPlayerEntity.method_6003() + h;
         m = class_3532.method_15363(l * l / 100.0F, 0.0F, 1.0F);
         if (!abstractClientPlayerEntity.method_6123()) {
            matrixStack.method_22907(class_7833.field_40714.rotationDegrees(m * (-90.0F - k)));
         }

         class_243 vec3d = abstractClientPlayerEntity.method_5828(h);
         class_243 vec3d2 = abstractClientPlayerEntity.method_18798();
         double d = vec3d2.method_37268();
         double e = vec3d.method_37268();
         if (d > 0.0D && e > 0.0D) {
            double n = (vec3d2.field_1352 * vec3d.field_1352 + vec3d2.field_1350 * vec3d.field_1350) / Math.sqrt(d * e);
            double o = vec3d2.field_1352 * vec3d.field_1350 - vec3d2.field_1350 * vec3d.field_1352;
            matrixStack.method_22907(class_7833.field_40716.rotation((float)(Math.signum(o) * Math.acos(n))));
         }
      } else if (j > 0.0F) {
         this.setupTransforms(abstractClientPlayerEntity, matrixStack, f, g, h);
         l = abstractClientPlayerEntity.method_5799() ? -90.0F - k : -90.0F;
         m = class_3532.method_16439(j, 0.0F, l);
         matrixStack.method_22907(class_7833.field_40714.rotationDegrees(m));
         if (abstractClientPlayerEntity.method_20232()) {
            matrixStack.method_46416(0.0F, -1.0F, 0.3F);
         }
      } else {
         this.setupTransforms(abstractClientPlayerEntity, matrixStack, f, g, h);
      }

   }

   private void setupTransforms(class_1657 entity, class_4587 matrices, float animationProgress, float bodyYaw, float tickDelta) {
      if (!entity.method_41328(class_4050.field_18078)) {
         matrices.method_22907(class_7833.field_40716.rotationDegrees(180.0F - bodyYaw));
      }

      if (entity.field_6213 > 0) {
         float f = ((float)entity.field_6213 + tickDelta - 1.0F) / 20.0F * 1.6F;
         f = class_3532.method_15355(f);
         if (f > 1.0F) {
            f = 1.0F;
         }

         matrices.method_22907(class_7833.field_40718.rotationDegrees(f * 90.0F));
      } else if (entity.method_6123()) {
         matrices.method_22907(class_7833.field_40714.rotationDegrees(-90.0F - entity.method_36455()));
         matrices.method_22907(class_7833.field_40716.rotationDegrees(((float)entity.field_6012 + tickDelta) * -75.0F));
      } else if (entity.method_41328(class_4050.field_18078)) {
         class_2350 direction = entity.method_18401();
         float g = direction != null ? getYaw(direction) : bodyYaw;
         matrices.method_22907(class_7833.field_40716.rotationDegrees(g));
         matrices.method_22907(class_7833.field_40718.rotationDegrees(90.0F));
         matrices.method_22907(class_7833.field_40716.rotationDegrees(270.0F));
      }

   }

   private static float getYaw(class_2350 direction) {
      float var10000;
      switch(direction) {
      case field_11043:
         var10000 = 270.0F;
         break;
      case field_11035:
         var10000 = 90.0F;
         break;
      case field_11034:
         var10000 = 180.0F;
         break;
      default:
         var10000 = 0.0F;
      }

      return var10000;
   }

   @EventHandler
   public void onRenderHands(EventHeldItemRenderer e) {
      if ((Boolean)this.handItems.getValue()) {
         RenderSystem.setShaderColor((float)((ColorSetting)this.handItemsColor.getValue()).getRed() / 255.0F, (float)((ColorSetting)this.handItemsColor.getValue()).getGreen() / 255.0F, (float)((ColorSetting)this.handItemsColor.getValue()).getBlue() / 255.0F, (float)((ColorSetting)this.handItemsColor.getValue()).getAlpha() / 255.0F);
      }

   }

   private static enum CMode {
      One,
      Two,
      Three;

      // $FF: synthetic method
      private static Chams.CMode[] $values() {
         return new Chams.CMode[]{One, Two, Three};
      }
   }
}
