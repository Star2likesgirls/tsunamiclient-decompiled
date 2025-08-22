package tsunami.features.modules.render;

import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.platform.GlStateManager.class_4534;
import com.mojang.blaze3d.platform.GlStateManager.class_4535;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.concurrent.CopyOnWriteArrayList;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_1309;
import net.minecraft.class_1657;
import net.minecraft.class_2338;
import net.minecraft.class_287;
import net.minecraft.class_289;
import net.minecraft.class_290;
import net.minecraft.class_2960;
import net.minecraft.class_4587;
import net.minecraft.class_5602;
import net.minecraft.class_591;
import net.minecraft.class_742;
import net.minecraft.class_757;
import net.minecraft.class_7833;
import net.minecraft.class_1297.class_5529;
import net.minecraft.class_293.class_5596;
import net.minecraft.class_5617.class_5618;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import tsunami.events.impl.TotemPopEvent;
import tsunami.features.modules.Module;
import tsunami.injection.accesors.IEntity;
import tsunami.setting.Setting;
import tsunami.setting.impl.ColorSetting;
import tsunami.utility.math.MathUtility;
import tsunami.utility.render.Render2DEngine;
import tsunami.utility.render.Render3DEngine;

public final class PopChams extends Module {
   private final Setting<PopChams.Mode> mode;
   private final Setting<Boolean> secondLayer;
   private final Setting<ColorSetting> color;
   private final Setting<Integer> ySpeed;
   private final Setting<Integer> aSpeed;
   private final Setting<Float> rotSpeed;
   private final CopyOnWriteArrayList<PopChams.Person> popList;

   public PopChams() {
      super("PopChams", Module.Category.NONE);
      this.mode = new Setting("Mode", PopChams.Mode.Textured);
      this.secondLayer = new Setting("SecondLayer", true);
      this.color = new Setting("Color", new ColorSetting(-2013200640));
      this.ySpeed = new Setting("YSpeed", 0, -10, 10);
      this.aSpeed = new Setting("AlphaSpeed", 5, 1, 100);
      this.rotSpeed = new Setting("RotationSpeed", 0.25F, 0.0F, 6.0F);
      this.popList = new CopyOnWriteArrayList();
   }

   public void onUpdate() {
      this.popList.forEach((person) -> {
         person.update(this.popList);
      });
   }

   public void onRender3D(class_4587 stack) {
      RenderSystem.enableBlend();
      RenderSystem.disableDepthTest();
      if (this.mode.is(PopChams.Mode.Simple)) {
         RenderSystem.defaultBlendFunc();
      } else {
         RenderSystem.blendFunc(class_4535.SRC_ALPHA, class_4534.ONE);
      }

      this.popList.forEach((person) -> {
         this.renderEntity(stack, person.player, person.modelPlayer, person.getTexture(), person.getAlpha());
      });
      RenderSystem.enableDepthTest();
      RenderSystem.disableBlend();
   }

   @EventHandler
   private void onTotemPop(@NotNull TotemPopEvent e) {
      if (!e.getEntity().equals(mc.field_1724) && mc.field_1687 != null) {
         class_1657 entity = new class_1657(this, mc.field_1687, class_2338.field_10980, e.getEntity().field_6283, new GameProfile(e.getEntity().method_5667(), e.getEntity().method_5477().getString())) {
            public boolean method_7325() {
               return false;
            }

            public boolean method_7337() {
               return false;
            }
         };
         entity.method_5719(e.getEntity());
         entity.field_6283 = e.getEntity().field_6283;
         entity.field_6241 = e.getEntity().field_6241;
         entity.field_6251 = e.getEntity().field_6251;
         entity.field_6279 = e.getEntity().field_6279;
         entity.method_5660(e.getEntity().method_5715());
         entity.field_42108.method_48567(e.getEntity().field_42108.method_48566());
         entity.field_42108.field_42111 = e.getEntity().field_42108.method_48569();
         this.popList.add(new PopChams.Person(entity, ((class_742)e.getEntity()).method_52814().comp_1626()));
      }
   }

   private void renderEntity(@NotNull class_4587 matrices, @NotNull class_1309 entity, @NotNull class_591<class_1657> modelBase, class_2960 texture, int alpha) {
      modelBase.field_3482.field_3665 = (Boolean)this.secondLayer.getValue();
      modelBase.field_3479.field_3665 = (Boolean)this.secondLayer.getValue();
      modelBase.field_3484.field_3665 = (Boolean)this.secondLayer.getValue();
      modelBase.field_3486.field_3665 = (Boolean)this.secondLayer.getValue();
      modelBase.field_3483.field_3665 = (Boolean)this.secondLayer.getValue();
      modelBase.field_3394.field_3665 = (Boolean)this.secondLayer.getValue();
      double x = entity.method_23317() - mc.method_1561().field_4686.method_19326().method_10216();
      double y = entity.method_23318() - mc.method_1561().field_4686.method_19326().method_10214();
      double z = entity.method_23321() - mc.method_1561().field_4686.method_19326().method_10215();
      ((IEntity)entity).setPos(entity.method_19538().method_1031(0.0D, (double)(Integer)this.ySpeed.getValue() / 50.0D, 0.0D));
      matrices.method_22903();
      matrices.method_46416((float)x, (float)y, (float)z);
      float yRotYaw = (float)alpha / 255.0F * 360.0F * (Float)this.rotSpeed.getValue();
      yRotYaw = yRotYaw == 0.0F ? 0.0F : Render2DEngine.interpolateFloat(yRotYaw, yRotYaw - (float)(Integer)this.aSpeed.getValue() / 255.0F * 360.0F * (Float)this.rotSpeed.getValue(), (double)Render3DEngine.getTickDelta());
      matrices.method_22907(class_7833.field_40716.rotation(MathUtility.rad(180.0F - entity.field_6283 + yRotYaw)));
      prepareScale(matrices);
      modelBase.method_17086((class_1657)entity, entity.field_42108.method_48569(), entity.field_42108.method_48566(), Render3DEngine.getTickDelta());
      float limbSpeed = Math.min(entity.field_42108.method_48566(), 1.0F);
      modelBase.method_17087((class_1657)entity, entity.field_42108.method_48569(), limbSpeed, (float)entity.field_6012, entity.field_6241 - entity.field_6283, entity.method_36455());
      class_287 buffer;
      if (this.mode.is(PopChams.Mode.Textured)) {
         RenderSystem.setShaderTexture(0, texture);
         RenderSystem.setShader(class_757::method_34542);
         buffer = class_289.method_1348().method_60827(class_5596.field_27382, class_290.field_1585);
      } else {
         RenderSystem.setShader(class_757::method_34539);
         buffer = class_289.method_1348().method_60827(class_5596.field_27382, class_290.field_1592);
      }

      RenderSystem.setShaderColor(((ColorSetting)this.color.getValue()).getGlRed(), ((ColorSetting)this.color.getValue()).getGlGreen(), ((ColorSetting)this.color.getValue()).getGlBlue(), (float)alpha / 255.0F);
      modelBase.method_60879(matrices, buffer, 10, 0);
      Render2DEngine.endBuilding(buffer);
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      matrices.method_22909();
   }

   private static void prepareScale(@NotNull class_4587 matrixStack) {
      matrixStack.method_22905(-1.0F, -1.0F, 1.0F);
      matrixStack.method_22905(1.6F, 1.8F, 1.6F);
      matrixStack.method_46416(0.0F, -1.501F, 0.0F);
   }

   private static enum Mode {
      Simple,
      Textured;

      // $FF: synthetic method
      private static PopChams.Mode[] $values() {
         return new PopChams.Mode[]{Simple, Textured};
      }
   }

   private class Person {
      private final class_1657 player;
      private final class_591<class_1657> modelPlayer;
      private class_2960 texture;
      private int alpha;

      public Person(class_1657 player, class_2960 texture) {
         this.player = player;
         this.modelPlayer = new class_591((new class_5618(Module.mc.method_1561(), Module.mc.method_1480(), Module.mc.method_1541(), Module.mc.method_1561().method_43336(), Module.mc.method_1478(), Module.mc.method_31974(), Module.mc.field_1772)).method_32167(class_5602.field_27577), false);
         this.modelPlayer.method_2838().method_41924(new Vector3f(-0.3F, -0.3F, -0.3F));
         this.alpha = ((ColorSetting)PopChams.this.color.getValue()).getAlpha();
         this.texture = texture;
      }

      public void update(CopyOnWriteArrayList<PopChams.Person> arrayList) {
         if (this.alpha <= 0) {
            arrayList.remove(this);
            this.player.method_5768();
            this.player.method_5650(class_5529.field_26998);
            this.player.method_36209();
         } else {
            this.alpha -= (Integer)PopChams.this.aSpeed.getValue();
         }
      }

      public int getAlpha() {
         return MathUtility.clamp(this.alpha, 0, 255);
      }

      public class_2960 getTexture() {
         return this.texture;
      }
   }
}
