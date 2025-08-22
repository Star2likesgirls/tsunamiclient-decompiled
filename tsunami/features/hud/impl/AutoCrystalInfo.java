package tsunami.features.hud.impl;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Iterator;
import net.minecraft.class_124;
import net.minecraft.class_287;
import net.minecraft.class_289;
import net.minecraft.class_290;
import net.minecraft.class_332;
import net.minecraft.class_4587;
import net.minecraft.class_757;
import net.minecraft.class_293.class_5596;
import tsunami.core.manager.client.ModuleManager;
import tsunami.features.hud.HudElement;
import tsunami.features.modules.client.HudEditor;
import tsunami.features.modules.combat.AutoCrystal;
import tsunami.gui.font.FontRenderer;
import tsunami.gui.font.FontRenderers;
import tsunami.setting.impl.ColorSetting;
import tsunami.utility.math.MathUtility;
import tsunami.utility.render.Render2DEngine;

public class AutoCrystalInfo extends HudElement {
   private final ArrayDeque<Integer> speeds = new ArrayDeque(20);
   private int max;
   private int min;
   private long time;

   public AutoCrystalInfo() {
      super("AutoCrystalInfo", 175, 80);
   }

   public void onRender2D(class_332 context) {
      super.onRender2D(context);
      Render2DEngine.drawHudBase(context.method_51448(), this.getPosX(), this.getPosY(), this.getWidth(), this.getHeight(), (Float)HudEditor.hudRound.getValue());
      Color c1 = HudEditor.getColor(0).darker().darker().darker();
      Color c2 = HudEditor.getColor(0);
      Render2DEngine.drawRect(context.method_51448(), this.getPosX() + 2.0F, this.getPosY() + 14.0F, 96.0F, 64.0F, (Float)HudEditor.hudRound.getValue(), 0.4F, c1, c1, c1, c1);
      FontRenderers.sf_bold.drawGradientString(context.method_51448(), "AutoCrystal Info", this.getPosX() + 2.0F, this.getPosY() + 4.0F, 10);
      RenderSystem.setShader(class_757::method_34540);
      class_287 bufferBuilder = class_289.method_1348().method_60827(class_5596.field_29345, class_290.field_1576);
      float offset = 0.0F;

      for(Iterator var6 = this.speeds.iterator(); var6.hasNext(); offset += 4.8F) {
         Integer speed = (Integer)var6.next();
         bufferBuilder.method_22912(this.getPosX() + 2.0F + offset, this.getPosY() + 80.0F - 55.0F * ((float)speed / (float)this.max), 0.0F).method_39415(c2.getRGB());
      }

      Render2DEngine.endBuilding(bufferBuilder);
      class_287 bufferBuilder2 = class_289.method_1348().method_60827(class_5596.field_27380, class_290.field_1576);
      offset = 0.0F;

      for(Iterator var11 = this.speeds.iterator(); var11.hasNext(); offset += 4.8F) {
         Integer speed = (Integer)var11.next();
         bufferBuilder2.method_22912(this.getPosX() + 2.0F + offset, this.getPosY() + 80.0F - 55.0F * ((float)speed / (float)this.max), 0.0F).method_39415(Render2DEngine.applyOpacity(c2.getRGB(), 0.3F));
         bufferBuilder2.method_22912(this.getPosX() + 2.0F + offset, this.getPosY() + 80.0F, 0.0F).method_39415(Render2DEngine.applyOpacity(c2.darker().darker().getRGB(), 0.0F));
      }

      Render2DEngine.endBuilding(bufferBuilder2);
      FontRenderers.sf_bold_mini.drawString(context.method_51448(), this.max.makeConcatWithConstants<invokedynamic>(this.max), (double)(this.getPosX() + 100.0F), (double)(this.getPosY() + 16.0F), ((ColorSetting)HudEditor.textColor.getValue()).getRawColor());
      if (!this.speeds.isEmpty()) {
         FontRenderers.sf_bold_mini.drawString(context.method_51448(), String.valueOf(this.speeds.getLast()).makeConcatWithConstants<invokedynamic>(String.valueOf(this.speeds.getLast())), (double)(this.getPosX() + 100.0F), (double)(this.getPosY() + 80.0F - 55.0F * ((float)(Integer)this.speeds.getLast() / (float)this.max)), ((ColorSetting)HudEditor.textColor.getValue()).getRawColor());
      }

      FontRenderer var10000 = FontRenderers.sf_bold_mini;
      class_4587 var10001 = context.method_51448();
      int var10002 = this.min;
      var10000.drawString(var10001, var10002.makeConcatWithConstants<invokedynamic>(var10002), (double)(this.getPosX() + 100.0F), (double)(this.getPosY() + 72.0F), ((ColorSetting)HudEditor.textColor.getValue()).getRawColor());
      boolean isNull = ModuleManager.autoCrystal.getCurrentData() == null;
      var10000 = FontRenderers.sf_bold_mini;
      var10001 = context.method_51448();
      String var14 = String.valueOf(class_124.field_1080);
      var10000.drawString(var10001, "Target: " + var14 + (AutoCrystal.target == null ? "null" : AutoCrystal.target.method_5477().getString()), (double)(this.getPosX() + 113.0F), (double)(this.getPosY() + 16.0F), ((ColorSetting)HudEditor.textColor.getValue()).getRawColor());
      int calc = (int)ModuleManager.autoCrystal.getCalcTime();
      float efficiency = isNull ? 0.0F : MathUtility.round2((double)(ModuleManager.autoCrystal.getCurrentData().damage() / ModuleManager.autoCrystal.getCurrentData().selfDamage()));
      var10000 = FontRenderers.sf_bold_mini;
      var10001 = context.method_51448();
      var14 = String.valueOf(this.getCalcColor((float)calc));
      var10000.drawString(var10001, "Calc delay: " + var14 + calc + "ms", (double)(this.getPosX() + 113.0F), (double)(this.getPosY() + 24.0F), ((ColorSetting)HudEditor.textColor.getValue()).getRawColor());
      var10000 = FontRenderers.sf_bold_mini;
      var10001 = context.method_51448();
      var14 = String.valueOf(class_124.field_1080);
      var10000.drawString(var10001, "Side: " + var14 + String.valueOf(isNull ? "null" : ModuleManager.autoCrystal.getCurrentData().bhr().method_17780()), (double)(this.getPosX() + 113.0F), (double)(this.getPosY() + 32.0F), ((ColorSetting)HudEditor.textColor.getValue()).getRawColor());
      var10000 = FontRenderers.sf_bold_mini;
      var10001 = context.method_51448();
      var14 = String.valueOf(class_124.field_1080);
      var10000.drawString(var10001, "Damage: " + var14 + String.valueOf(isNull ? "null" : MathUtility.round2((double)ModuleManager.autoCrystal.getCurrentData().damage())), (double)(this.getPosX() + 113.0F), (double)(this.getPosY() + 40.0F), ((ColorSetting)HudEditor.textColor.getValue()).getRawColor());
      var10000 = FontRenderers.sf_bold_mini;
      var10001 = context.method_51448();
      var14 = String.valueOf(class_124.field_1080);
      var10000.drawString(var10001, "Self: " + var14 + String.valueOf(isNull ? "null" : MathUtility.round2((double)ModuleManager.autoCrystal.getCurrentData().selfDamage())), (double)(this.getPosX() + 113.0F), (double)(this.getPosY() + 48.0F), ((ColorSetting)HudEditor.textColor.getValue()).getRawColor());
      FontRenderers.sf_bold_mini.drawString(context.method_51448(), "Overr. dmg: " + String.valueOf(class_124.field_1080) + String.valueOf(isNull ? "null" : ModuleManager.autoCrystal.getCurrentData().overrideDamage()), (double)(this.getPosX() + 113.0F), (double)(this.getPosY() + 56.0F), ((ColorSetting)HudEditor.textColor.getValue()).getRawColor());
      FontRenderers.sf_bold_mini.drawString(context.method_51448(), "Efficiency: " + String.valueOf(this.getEfficiencyColor(efficiency)) + efficiency, (double)(this.getPosX() + 113.0F), (double)(this.getPosY() + 64.0F), ((ColorSetting)HudEditor.textColor.getValue()).getRawColor());
      FontRenderers.sf_bold_mini.drawString(context.method_51448(), "Pause: " + ModuleManager.autoCrystal.getPauseState(), (double)(this.getPosX() + 113.0F), (double)(this.getPosY() + 72.0F), ((ColorSetting)HudEditor.textColor.getValue()).getRawColor());
      Render2DEngine.drawRect(context.method_51448(), this.getPosX() + 110.5F, this.getPosY() + 12.0F, 0.5F, 65.0F, new Color(1157627903, true));
      this.setBounds(this.getPosX(), this.getPosY(), this.getWidth(), this.getHeight());
   }

   public class_124 getCalcColor(float val) {
      if (val > 20.0F) {
         return class_124.field_1061;
      } else {
         return val > 10.0F ? class_124.field_1054 : class_124.field_1060;
      }
   }

   public class_124 getEfficiencyColor(float val) {
      if (val > 6.0F) {
         return class_124.field_1060;
      } else {
         return val < 1.0F ? class_124.field_1061 : class_124.field_1054;
      }
   }

   public void onSpawn() {
      if (this.time != 0L) {
         if (this.speeds.size() > 20) {
            this.speeds.poll();
         }

         this.speeds.add((int)(1000.0F / (float)(System.currentTimeMillis() - this.time)));
         this.max = (Integer)Collections.max(this.speeds);
         this.min = (Integer)Collections.min(this.speeds);
      }

      this.time = System.currentTimeMillis();
   }
}
