package tsunami.utility.render.animation;

import com.mojang.blaze3d.platform.GlStateManager.class_4534;
import com.mojang.blaze3d.platform.GlStateManager.class_4535;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.class_1297;
import net.minecraft.class_286;
import net.minecraft.class_287;
import net.minecraft.class_289;
import net.minecraft.class_290;
import net.minecraft.class_4184;
import net.minecraft.class_4587;
import net.minecraft.class_757;
import net.minecraft.class_7833;
import net.minecraft.class_293.class_5596;
import org.joml.Matrix4f;
import tsunami.features.modules.Module;
import tsunami.features.modules.client.HudEditor;
import tsunami.utility.render.Render2DEngine;
import tsunami.utility.render.Render3DEngine;
import tsunami.utility.render.TextureStorage;

public class CaptureMark {
   private static float espValue = 1.0F;
   private static float prevEspValue;
   private static float espSpeed = 1.0F;
   private static boolean flipSpeed;

   public static void render(class_1297 target) {
      class_4184 camera = Module.mc.field_1773.method_19418();
      double tPosX = Render2DEngine.interpolate(target.field_6014, target.method_23317(), (double)Render3DEngine.getTickDelta()) - camera.method_19326().field_1352;
      double tPosY = Render2DEngine.interpolate(target.field_6036, target.method_23318(), (double)Render3DEngine.getTickDelta()) - camera.method_19326().field_1351;
      double tPosZ = Render2DEngine.interpolate(target.field_5969, target.method_23321(), (double)Render3DEngine.getTickDelta()) - camera.method_19326().field_1350;
      class_4587 matrices = new class_4587();
      RenderSystem.disableDepthTest();
      RenderSystem.disableCull();
      matrices.method_22907(class_7833.field_40714.rotationDegrees(camera.method_19329()));
      matrices.method_22907(class_7833.field_40716.rotationDegrees(camera.method_19330() + 180.0F));
      matrices.method_22904(tPosX, tPosY + (double)(target.method_18381(target.method_18376()) / 2.0F), tPosZ);
      matrices.method_22907(class_7833.field_40716.rotationDegrees(-camera.method_19330()));
      matrices.method_22907(class_7833.field_40714.rotationDegrees(camera.method_19329()));
      matrices.method_22907(class_7833.field_40718.rotationDegrees(Render2DEngine.interpolateFloat(prevEspValue, espValue, (double)Render3DEngine.getTickDelta())));
      RenderSystem.enableBlend();
      RenderSystem.blendFunc(class_4535.SRC_ALPHA, class_4534.ONE);
      RenderSystem.setShaderTexture(0, TextureStorage.capture);
      matrices.method_22904(-0.75D, -0.75D, -0.01D);
      Matrix4f matrix = matrices.method_23760().method_23761();
      RenderSystem.setShader(class_757::method_34543);
      class_287 buffer = class_289.method_1348().method_60827(class_5596.field_27382, class_290.field_1575);
      buffer.method_22918(matrix, 0.0F, 1.5F, 0.0F).method_22913(0.0F, 1.0F).method_39415(HudEditor.getColor(90).getRGB());
      buffer.method_22918(matrix, 1.5F, 1.5F, 0.0F).method_22913(1.0F, 1.0F).method_39415(HudEditor.getColor(0).getRGB());
      buffer.method_22918(matrix, 1.5F, 0.0F, 0.0F).method_22913(1.0F, 0.0F).method_39415(HudEditor.getColor(180).getRGB());
      buffer.method_22918(matrix, 0.0F, 0.0F, 0.0F).method_22913(0.0F, 0.0F).method_39415(HudEditor.getColor(270).getRGB());
      class_286.method_43433(buffer.method_60800());
      RenderSystem.enableCull();
      RenderSystem.enableDepthTest();
      RenderSystem.blendFunc(class_4535.SRC_ALPHA, class_4534.ONE_MINUS_SRC_ALPHA);
      RenderSystem.disableBlend();
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
   }

   public static void tick() {
      prevEspValue = espValue;
      espValue += espSpeed;
      if (espSpeed > 25.0F) {
         flipSpeed = true;
      }

      if (espSpeed < -25.0F) {
         flipSpeed = false;
      }

      espSpeed = flipSpeed ? espSpeed - 0.5F : espSpeed + 0.5F;
   }
}
