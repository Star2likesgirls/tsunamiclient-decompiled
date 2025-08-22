package tsunami.features.modules.render;

import com.mojang.blaze3d.platform.GlStateManager.class_4534;
import com.mojang.blaze3d.platform.GlStateManager.class_4535;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.class_1657;
import net.minecraft.class_243;
import net.minecraft.class_287;
import net.minecraft.class_289;
import net.minecraft.class_290;
import net.minecraft.class_2960;
import net.minecraft.class_4587;
import net.minecraft.class_757;
import net.minecraft.class_7833;
import net.minecraft.class_293.class_5596;
import org.joml.Matrix4f;
import tsunami.features.modules.Module;
import tsunami.features.modules.client.HudEditor;
import tsunami.setting.Setting;
import tsunami.utility.ThunderUtility;
import tsunami.utility.Timer;
import tsunami.utility.render.Render2DEngine;
import tsunami.utility.render.TextureStorage;

public class JumpCircle extends Module {
   private final Setting<JumpCircle.Mode> mode;
   private final Setting<Boolean> easeOut;
   private final Setting<Float> rotateSpeed;
   private final Setting<Float> circleScale;
   private final Setting<Boolean> onlySelf;
   private final List<JumpCircle.Circle> circles;
   private final List<class_1657> cache;
   private class_2960 custom;

   public JumpCircle() {
      super("JumpCircle", Module.Category.NONE);
      this.mode = new Setting("Mode", JumpCircle.Mode.Default);
      this.easeOut = new Setting("EaseOut", true);
      this.rotateSpeed = new Setting("RotateSpeed", 2.0F, 0.5F, 5.0F);
      this.circleScale = new Setting("CircleScale", 1.0F, 0.5F, 5.0F);
      this.onlySelf = new Setting("OnlySelf", false);
      this.circles = new ArrayList();
      this.cache = new CopyOnWriteArrayList();
   }

   public void onEnable() {
      try {
         this.custom = ThunderUtility.getCustomImg("circle");
      } catch (Exception var2) {
         this.sendMessage(var2.getMessage());
      }

   }

   public void onUpdate() {
      if (this.mode.is(JumpCircle.Mode.Custom) && this.custom == null) {
         try {
            this.custom = ThunderUtility.getCustomImg("circle");
         } catch (Exception var3) {
            this.sendMessage(".minecraft -> ThunderHackRecode -> misc -> images -> circle.png");
         }
      }

      Iterator var1 = mc.field_1687.method_18456().iterator();

      while(true) {
         class_1657 pl;
         do {
            do {
               do {
                  if (!var1.hasNext()) {
                     this.cache.forEach((plx) -> {
                        if (plx != null && !plx.method_24828()) {
                           this.circles.add(new JumpCircle.Circle(new class_243(plx.method_23317(), (double)((float)((int)Math.floor(plx.method_23318())) + 0.001F), plx.method_23321()), new Timer()));
                           this.cache.remove(plx);
                        }

                     });
                     this.circles.removeIf((c) -> {
                        return c.timer.passedMs((Boolean)this.easeOut.getValue() ? 5000L : 6000L);
                     });
                     return;
                  }

                  pl = (class_1657)var1.next();
               } while(this.cache.contains(pl));
            } while(!pl.method_24828());
         } while(mc.field_1724 != pl && (Boolean)this.onlySelf.getValue());

         this.cache.add(pl);
      }
   }

   public void onRender3D(class_4587 stack) {
      Collections.reverse(this.circles);
      RenderSystem.disableDepthTest();
      RenderSystem.enableBlend();
      RenderSystem.blendFunc(class_4535.SRC_ALPHA, class_4534.ONE);
      switch(((JumpCircle.Mode)this.mode.getValue()).ordinal()) {
      case 0:
         RenderSystem.setShaderTexture(0, TextureStorage.default_circle);
         break;
      case 1:
         RenderSystem.setShaderTexture(0, TextureStorage.bubble);
         break;
      case 2:
         RenderSystem.setShaderTexture(0, (class_2960)Objects.requireNonNullElse(this.custom, TextureStorage.default_circle));
      }

      RenderSystem.setShader(class_757::method_34543);
      class_287 buffer = class_289.method_1348().method_60827(class_5596.field_27382, class_290.field_1575);
      Iterator var3 = this.circles.iterator();

      while(var3.hasNext()) {
         JumpCircle.Circle c = (JumpCircle.Circle)var3.next();
         float colorAnim = (float)c.timer.getPassedTimeMs() / 6000.0F;
         float sizeAnim = (Float)this.circleScale.getValue() - (float)Math.pow((double)(1.0F - (float)c.timer.getPassedTimeMs() * ((Boolean)this.easeOut.getValue() ? 2.0F : 1.0F) / 5000.0F), 4.0D);
         stack.method_22903();
         stack.method_22904(c.pos().field_1352 - mc.method_1561().field_4686.method_19326().method_10216(), c.pos().field_1351 - mc.method_1561().field_4686.method_19326().method_10214(), c.pos().field_1350 - mc.method_1561().field_4686.method_19326().method_10215());
         stack.method_22907(class_7833.field_40714.rotationDegrees(90.0F));
         stack.method_22907(class_7833.field_40718.rotationDegrees(sizeAnim * (Float)this.rotateSpeed.getValue() * 1000.0F));
         float scale = sizeAnim * 2.0F;
         Matrix4f matrix = stack.method_23760().method_23761();
         buffer.method_22918(matrix, -sizeAnim, -sizeAnim + scale, 0.0F).method_22913(0.0F, 1.0F).method_39415(Render2DEngine.applyOpacity(HudEditor.getColor(270), 1.0F - colorAnim).getRGB());
         buffer.method_22918(matrix, -sizeAnim + scale, -sizeAnim + scale, 0.0F).method_22913(1.0F, 1.0F).method_39415(Render2DEngine.applyOpacity(HudEditor.getColor(0), 1.0F - colorAnim).getRGB());
         buffer.method_22918(matrix, -sizeAnim + scale, -sizeAnim, 0.0F).method_22913(1.0F, 0.0F).method_39415(Render2DEngine.applyOpacity(HudEditor.getColor(180), 1.0F - colorAnim).getRGB());
         buffer.method_22918(matrix, -sizeAnim, -sizeAnim, 0.0F).method_22913(0.0F, 0.0F).method_39415(Render2DEngine.applyOpacity(HudEditor.getColor(90), 1.0F - colorAnim).getRGB());
         stack.method_22909();
      }

      Render2DEngine.endBuilding(buffer);
      RenderSystem.disableBlend();
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      RenderSystem.enableDepthTest();
      Collections.reverse(this.circles);
   }

   public static enum Mode {
      Default,
      Portal,
      Custom;

      // $FF: synthetic method
      private static JumpCircle.Mode[] $values() {
         return new JumpCircle.Mode[]{Default, Portal, Custom};
      }
   }

   public static record Circle(class_243 pos, Timer timer) {
      public Circle(class_243 pos, Timer timer) {
         this.pos = pos;
         this.timer = timer;
      }

      public class_243 pos() {
         return this.pos;
      }

      public Timer timer() {
         return this.timer;
      }
   }
}
