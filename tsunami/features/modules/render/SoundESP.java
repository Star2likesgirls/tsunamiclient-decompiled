package tsunami.features.modules.render;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.class_243;
import net.minecraft.class_332;
import org.joml.Vector4d;
import tsunami.features.modules.Module;
import tsunami.gui.font.FontRenderers;
import tsunami.setting.Setting;
import tsunami.setting.impl.ColorSetting;
import tsunami.utility.render.Render2DEngine;
import tsunami.utility.render.Render3DEngine;

public class SoundESP extends Module {
   private final Setting<Float> scale = new Setting("Scale", 1.0F, 0.1F, 10.0F);
   private final Setting<ColorSetting> fillColorA = new Setting("Color", new ColorSetting(Integer.MIN_VALUE));
   private List<SoundESP.Sound> sounds = new ArrayList();

   public SoundESP() {
      super("SoundESP", Module.Category.RENDER);
   }

   public void add(double x, double y, double z, String name) {
      this.sounds.add(new SoundESP.Sound(this, x, y, z, name.replace("minecraft.block.", "").replace("minecraft.entity", "").replace(".", " ")));
   }

   public void onRender2D(class_332 context) {
      Iterator var2 = Lists.newArrayList(this.sounds).iterator();

      while(var2.hasNext()) {
         SoundESP.Sound s = (SoundESP.Sound)var2.next();
         class_243 vector = new class_243(s.x, s.y, s.z);
         Vector4d position = null;
         vector = Render3DEngine.worldSpaceToScreenSpace(vector);
         if (vector.field_1350 > 0.0D && vector.field_1350 < 1.0D) {
            position = new Vector4d(vector.field_1352, vector.field_1351, vector.field_1350, 0.0D);
            position.x = Math.min(vector.field_1352, position.x);
            position.y = Math.min(vector.field_1351, position.y);
            position.z = Math.max(vector.field_1352, position.z);
         }

         if (position != null) {
            double posX = position.x;
            double posY = position.y;
            double endPosX = position.z;
            float diff = (float)(endPosX - posX) / 2.0F;
            float textWidth = FontRenderers.sf_bold.getStringWidth(s.name) * 1.0F;
            float tagX = (float)((posX + (double)diff - (double)(textWidth / 2.0F)) * 1.0D);
            float alpha = (float)(1.0D - Math.pow((double)(1.0F - (float)s.ticks / 60.0F), 3.0D));
            context.method_51448().method_22903();
            context.method_51448().method_46416(tagX - 2.0F + (textWidth + 4.0F) / 2.0F, (float)(posY - 13.0D) + 6.5F, 0.0F);
            context.method_51448().method_22905((Float)this.scale.getValue(), (Float)this.scale.getValue(), 1.0F);
            context.method_51448().method_46416(-(tagX - 2.0F + (textWidth + 4.0F) / 2.0F), -((float)(posY - 13.0D + 6.5D)), 0.0F);
            Render2DEngine.drawRect(context.method_51448(), tagX - 2.0F, (float)(posY - 13.0D), textWidth + 4.0F, 11.0F, ((ColorSetting)this.fillColorA.getValue()).withAlpha((int)((float)((ColorSetting)this.fillColorA.getValue()).getAlpha() * alpha)).getColorObject());
            FontRenderers.sf_bold.drawString(context.method_51448(), s.name, (double)tagX, (double)((float)posY - 10.0F), Render2DEngine.applyOpacity(-1, alpha));
            context.method_51448().method_22909();
         }
      }

   }

   public void onUpdate() {
      this.sounds.removeIf(SoundESP.Sound::shouldRemove);
   }

   private class Sound {
      double x;
      double y;
      double z;
      String name;
      int ticks;

      public Sound(final SoundESP param1, double x, double y, double z, String name) {
         this.x = x;
         this.y = y;
         this.z = z;
         this.name = name;
         this.ticks = 60;
      }

      public boolean shouldRemove() {
         return this.ticks-- <= 0;
      }
   }
}
