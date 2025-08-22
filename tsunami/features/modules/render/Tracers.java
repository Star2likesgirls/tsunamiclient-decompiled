package tsunami.features.modules.render;

import java.awt.Color;
import java.util.Iterator;
import net.minecraft.class_1657;
import net.minecraft.class_243;
import net.minecraft.class_4587;
import tsunami.core.Managers;
import tsunami.features.modules.Module;
import tsunami.setting.Setting;
import tsunami.setting.impl.ColorSetting;
import tsunami.utility.render.Render3DEngine;

public class Tracers extends Module {
   private final Setting<Float> height = new Setting("Height", 0.0F, 0.0F, 2.0F);
   private final Setting<ColorSetting> color = new Setting("Color", new ColorSetting(new Color(-1812004864, true)));
   private final Setting<ColorSetting> friendColor = new Setting("Friends", new ColorSetting(new Color(-1827152291, true)));

   public Tracers() {
      super("Tracers", Module.Category.RENDER);
   }

   public void onRender3D(class_4587 stack) {
      Iterator var2 = Managers.ASYNC.getAsyncPlayers().iterator();

      while(var2.hasNext()) {
         class_1657 player = (class_1657)var2.next();
         if (player != mc.field_1724) {
            Color color1 = ((ColorSetting)this.color.getValue()).getColorObject();
            if (Managers.FRIEND.isFriend(player)) {
               color1 = ((ColorSetting)this.friendColor.getValue()).getColorObject();
            }

            double x1 = mc.field_1724.field_6014 + (mc.field_1724.method_23317() - mc.field_1724.field_6014) * (double)Render3DEngine.getTickDelta();
            double y1 = (double)mc.field_1724.method_18381(mc.field_1724.method_18376()) + mc.field_1724.field_6036 + (mc.field_1724.method_23318() - mc.field_1724.field_6036) * (double)Render3DEngine.getTickDelta();
            double z1 = mc.field_1724.field_5969 + (mc.field_1724.method_23321() - mc.field_1724.field_5969) * (double)Render3DEngine.getTickDelta();
            class_243 vec2 = (new class_243(0.0D, 0.0D, 75.0D)).method_1037(-((float)Math.toRadians((double)mc.field_1773.method_19418().method_19329()))).method_1024(-((float)Math.toRadians((double)mc.field_1773.method_19418().method_19330()))).method_1031(x1, y1, z1);
            double x = player.field_6014 + (player.method_23317() - player.field_6014) * (double)Render3DEngine.getTickDelta();
            double y = player.field_6036 + (player.method_23318() - player.field_6036) * (double)Render3DEngine.getTickDelta();
            double z = player.field_5969 + (player.method_23321() - player.field_5969) * (double)Render3DEngine.getTickDelta();
            Render3DEngine.drawLineDebug(vec2, new class_243(x, y + (double)(Float)this.height.getValue(), z), color1);
         }
      }

   }
}
