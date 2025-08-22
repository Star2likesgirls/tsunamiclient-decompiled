package tsunami.features.modules.player;

import java.awt.Color;
import net.minecraft.class_1268;
import net.minecraft.class_1747;
import net.minecraft.class_2246;
import net.minecraft.class_238;
import net.minecraft.class_239;
import net.minecraft.class_2879;
import net.minecraft.class_3965;
import net.minecraft.class_4587;
import tsunami.core.manager.client.ModuleManager;
import tsunami.features.modules.Module;
import tsunami.setting.Setting;
import tsunami.setting.impl.ColorSetting;
import tsunami.setting.impl.SettingGroup;
import tsunami.utility.render.Render3DEngine;

public class AirPlace extends Module {
   private final Setting<Float> range = new Setting("Range", 5.0F, 0.0F, 6.0F);
   private final Setting<SettingGroup> renderGroup = new Setting("Render", new SettingGroup(false, 0));
   private final Setting<Boolean> swing;
   private final Setting<ColorSetting> fillColor;
   private final Setting<ColorSetting> lineColor;
   private final Setting<Integer> lineWidth;
   private class_3965 hit;
   private int cooldown;

   public AirPlace() {
      super("AirPlace", Module.Category.NONE);
      this.swing = (new Setting("Swing", true)).addToGroup(this.renderGroup);
      this.fillColor = (new Setting("Fill Color", new ColorSetting(new Color(100, 50, 255, 50)))).addToGroup(this.renderGroup);
      this.lineColor = (new Setting("Line Color", new ColorSetting(new Color(100, 50, 255, 150)))).addToGroup(this.renderGroup);
      this.lineWidth = (new Setting("Line Width", 2, 1, 5)).addToGroup(this.renderGroup);
   }

   public void onUpdate() {
      if (this.cooldown > 0) {
         --this.cooldown;
      }

      class_239 hitResult = mc.method_1560().method_5745((double)(Float)this.range.getValue(), 0.0F, false);
      if (hitResult instanceof class_3965) {
         class_3965 bhr = (class_3965)hitResult;
         this.hit = bhr;
         boolean main = mc.field_1724.method_6047().method_7909() instanceof class_1747;
         boolean off = mc.field_1724.method_6079().method_7909() instanceof class_1747;
         if (mc.field_1690.field_1904.method_1434() && (main || off) && this.cooldown <= 0) {
            mc.field_1761.method_2896(mc.field_1724, main ? class_1268.field_5808 : class_1268.field_5810, this.hit);
            if ((Boolean)this.swing.getValue()) {
               mc.field_1724.method_6104(main ? class_1268.field_5808 : class_1268.field_5810);
            } else {
               this.sendPacket(new class_2879(main ? class_1268.field_5808 : class_1268.field_5810));
            }

            this.cooldown = !ModuleManager.fastUse.isEnabled() || !(Boolean)ModuleManager.fastUse.blocks.getValue() && !(Boolean)ModuleManager.fastUse.all.getValue() ? 4 : 0;
         }

      }
   }

   public void onRender3D(class_4587 stack) {
      if (this.hit != null && mc.field_1687.method_8320(this.hit.method_17777()).method_26204().equals(class_2246.field_10124) && (mc.field_1724.method_6047().method_7909() instanceof class_1747 || mc.field_1724.method_6079().method_7909() instanceof class_1747)) {
         Render3DEngine.FILLED_QUEUE.add(new Render3DEngine.FillAction(new class_238(this.hit.method_17777()), ((ColorSetting)this.fillColor.getValue()).getColorObject()));
         Render3DEngine.OUTLINE_QUEUE.add(new Render3DEngine.OutlineAction(new class_238(this.hit.method_17777()), ((ColorSetting)this.lineColor.getValue()).getColorObject(), (float)(Integer)this.lineWidth.getValue()));
      }
   }
}
