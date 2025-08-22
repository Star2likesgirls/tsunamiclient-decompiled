package tsunami.features.hud.impl;

import com.google.common.collect.Lists;
import java.awt.Color;
import java.util.Iterator;
import net.minecraft.class_1657;
import net.minecraft.class_332;
import net.minecraft.class_7833;
import tsunami.core.Managers;
import tsunami.core.manager.client.ModuleManager;
import tsunami.features.hud.HudElement;
import tsunami.setting.Setting;
import tsunami.setting.impl.BooleanSettingGroup;
import tsunami.setting.impl.ColorSetting;
import tsunami.utility.math.MathUtility;
import tsunami.utility.render.Render2DEngine;
import tsunami.utility.render.animation.AnimationUtility;

public class CrosshairArrows extends HudElement {
   public static Setting<Boolean> glow = new Setting("Glow", false);
   private final Setting<Float> width = new Setting("Height", 2.28F, 0.1F, 5.0F);
   private final Setting<BooleanSettingGroup> down = new Setting("Down", new BooleanSettingGroup(false));
   private final Setting<Float> downHeight;
   private final Setting<Float> tracerWidth;
   private final Setting<Integer> xOffset;
   private final Setting<Integer> pitchLock;
   private final Setting<CrosshairArrows.triangleModeEn> triangleMode;
   private final Setting<ColorSetting> colorf;
   private final Setting<ColorSetting> colors;
   private float smoothYaw;

   public CrosshairArrows() {
      super("CrosshairArrows", 0, 0);
      this.downHeight = (new Setting("DownHeight", 3.63F, 0.1F, 20.0F)).addToGroup(this.down);
      this.tracerWidth = new Setting("Width", 0.44F, 0.0F, 8.0F);
      this.xOffset = new Setting("TracerRadius", 68, 20, 100);
      this.pitchLock = new Setting("PitchLock", 42, 0, 90);
      this.triangleMode = new Setting("TracerCMode", CrosshairArrows.triangleModeEn.Astolfo);
      this.colorf = new Setting("Friend", new ColorSetting(new Color(59392)));
      this.colors = new Setting("Tracer", new ColorSetting(new Color(16776960)));
      this.smoothYaw = 0.0F;
   }

   public void onRender2D(class_332 context) {
      super.onRender2D(context);
      if (!fullNullCheck()) {
         float middleW = ModuleManager.crosshair.getAnimatedPosX();
         float middleH = ModuleManager.crosshair.getAnimatedPosY();
         int color = false;
         context.method_51448().method_22903();
         context.method_51448().method_46416(middleW, middleH, 0.0F);
         context.method_51448().method_22907(class_7833.field_40714.rotationDegrees(90.0F / Math.abs(90.0F / MathUtility.clamp(mc.field_1724.method_36455(), (float)(Integer)this.pitchLock.getValue(), 90.0F)) - 102.0F));
         context.method_51448().method_46416(-middleW, -middleH, 0.0F);
         this.smoothYaw = AnimationUtility.fast(this.smoothYaw, mc.field_1724.method_36454(), 13.0F);
         Iterator var5 = Lists.newArrayList(mc.field_1687.method_18456()).iterator();

         while(var5.hasNext()) {
            class_1657 e = (class_1657)var5.next();
            if (e != mc.field_1724) {
               context.method_51448().method_22903();
               float yaw = RadarRewrite.getRotations(e) - this.smoothYaw;
               context.method_51448().method_46416(middleW, middleH, 0.0F);
               context.method_51448().method_22907(class_7833.field_40718.rotationDegrees(yaw));
               context.method_51448().method_46416(-middleW, -middleH, 0.0F);
               int color;
               if (Managers.FRIEND.isFriend(e)) {
                  color = ((ColorSetting)this.colorf.getValue()).getColor();
               } else {
                  int var10000;
                  switch(((CrosshairArrows.triangleModeEn)this.triangleMode.getValue()).ordinal()) {
                  case 0:
                     var10000 = ((ColorSetting)this.colors.getValue()).getColor();
                     break;
                  case 1:
                     var10000 = Render2DEngine.astolfo(false, 1).getRGB();
                     break;
                  default:
                     throw new MatchException((String)null, (Throwable)null);
                  }

                  color = var10000;
               }

               Render2DEngine.drawTracerPointer(context.method_51448(), middleW, middleH - (float)(Integer)this.xOffset.getValue(), (Float)this.width.getValue() * 5.0F, (Float)this.tracerWidth.getValue(), (Float)this.downHeight.getValue(), ((BooleanSettingGroup)this.down.getValue()).isEnabled(), (Boolean)glow.getValue(), color);
               context.method_51448().method_46416(middleW, middleH, 0.0F);
               context.method_51448().method_22907(class_7833.field_40718.rotationDegrees(-yaw));
               context.method_51448().method_46416(-middleW, -middleH, 0.0F);
               context.method_51448().method_22909();
            }
         }

         context.method_51448().method_22909();
      }
   }

   public static enum triangleModeEn {
      Custom,
      Astolfo;

      // $FF: synthetic method
      private static CrosshairArrows.triangleModeEn[] $values() {
         return new CrosshairArrows.triangleModeEn[]{Custom, Astolfo};
      }
   }
}
