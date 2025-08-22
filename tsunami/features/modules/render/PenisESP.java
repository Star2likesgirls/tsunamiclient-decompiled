package tsunami.features.modules.render;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_243;
import net.minecraft.class_332;
import net.minecraft.class_4587;
import tsunami.core.Managers;
import tsunami.features.modules.Module;
import tsunami.setting.Setting;
import tsunami.setting.impl.ColorSetting;
import tsunami.utility.render.Render3DEngine;

public class PenisESP extends Module {
   private final Setting<Boolean> onlyOwn = new Setting("OnlyOwn", false);
   private final Setting<Float> ballSize = new Setting("BallSize", 0.1F, 0.1F, 0.5F);
   private final Setting<Float> penisSize = new Setting("PenisSize", 1.5F, 0.1F, 3.0F);
   private final Setting<Float> friendSize = new Setting("FriendSize", 1.5F, 0.1F, 3.0F);
   private final Setting<Float> enemySize = new Setting("EnemySize", 0.5F, 0.1F, 3.0F);
   private final Setting<Integer> gradation = new Setting("Gradation", 30, 20, 100);
   private final Setting<ColorSetting> penisColor = new Setting("PenisColor", new ColorSetting(new Color(231, 180, 122, 255)));
   private final Setting<ColorSetting> headColor = new Setting("HeadColor", new ColorSetting(new Color(240, 50, 180, 255)));

   public PenisESP() {
      super("PenisESP", Module.Category.RENDER);
   }

   public void onRender2D(class_332 event) {
      Iterator var2 = mc.field_1687.method_18456().iterator();

      while(true) {
         class_1657 player;
         do {
            if (!var2.hasNext()) {
               return;
            }

            player = (class_1657)var2.next();
         } while((Boolean)this.onlyOwn.getValue() && player != mc.field_1724);

         double size = (double)Managers.FRIEND.isFriend(player) ? (Float)this.friendSize.getValue() : (player != mc.field_1724 ? (Float)this.enemySize.getValue() : (Float)this.penisSize.getValue());
         class_243 base = this.getBase(player);
         class_243 forward = base.method_1031(0.0D, (double)player.method_17682() / 2.4D, 0.0D).method_1019(class_243.method_1030(0.0F, player.method_36454()).method_1021(0.1D));
         class_243 left = forward.method_1019(class_243.method_1030(0.0F, player.method_36454() - 90.0F).method_1021((double)(Float)this.ballSize.getValue()));
         class_243 right = forward.method_1019(class_243.method_1030(0.0F, player.method_36454() + 90.0F).method_1021((double)(Float)this.ballSize.getValue()));
         this.drawBall(player, (double)(Float)this.ballSize.getValue(), (Integer)this.gradation.getValue(), left, ((ColorSetting)this.penisColor.getValue()).getColorObject(), 0);
         this.drawBall(player, (double)(Float)this.ballSize.getValue(), (Integer)this.gradation.getValue(), right, ((ColorSetting)this.penisColor.getValue()).getColorObject(), 0);
         this.drawPenis(player, event.method_51448(), size, forward);
      }
   }

   public class_243 getBase(class_1297 entity) {
      double x = entity.field_6014 + (entity.method_23317() - entity.field_6014) * (double)Render3DEngine.getTickDelta();
      double y = entity.field_6036 + (entity.method_23318() - entity.field_6036) * (double)Render3DEngine.getTickDelta();
      double z = entity.field_5969 + (entity.method_23321() - entity.field_5969) * (double)Render3DEngine.getTickDelta();
      return new class_243(x, y, z);
   }

   public void drawBall(class_1657 player, double radius, int gradation, class_243 pos, Color color, int stage) {
      for(float alpha = 0.0F; (double)alpha < 3.141592653589793D; alpha = (float)((double)alpha + 3.141592653589793D / (double)gradation)) {
         for(float beta = 0.0F; (double)beta < 6.283185307179586D; beta = (float)((double)beta + 3.141592653589793D / (double)gradation)) {
            double x1 = (double)((float)(pos.method_10216() + radius * Math.cos((double)beta) * Math.sin((double)alpha)));
            double y1 = (double)((float)(pos.method_10214() + radius * Math.sin((double)beta) * Math.sin((double)alpha)));
            double z1 = (double)((float)(pos.method_10215() + radius * Math.cos((double)alpha)));
            double sin = Math.sin((double)alpha + 3.141592653589793D / (double)gradation);
            double x2 = (double)((float)(pos.method_10216() + radius * Math.cos((double)beta) * sin));
            double y2 = (double)((float)(pos.method_10214() + radius * Math.sin((double)beta) * sin));
            double z2 = (double)((float)(pos.method_10215() + radius * Math.cos((double)alpha + 3.141592653589793D / (double)gradation)));
            class_243 base = this.getBase(player);
            class_243 forward = base.method_1031(0.0D, (double)player.method_17682() / 2.4D, 0.0D).method_1019(class_243.method_1030(0.0F, player.method_36454()).method_1021(0.1D));
            class_243 vec3d = new class_243(x1, y1, z1);
            switch(stage) {
            case 1:
               if (!vec3d.method_24802(forward, 0.145D)) {
                  continue;
               }
               break;
            case 2:
               double size = (double)Managers.FRIEND.isFriend(player) ? (Float)this.friendSize.getValue() : (player != mc.field_1724 ? (Float)this.enemySize.getValue() : (Float)this.penisSize.getValue());
               if (vec3d.method_24802(forward, size + 0.095D)) {
                  continue;
               }
            }

            Render3DEngine.drawLine(vec3d, new class_243(x2, y2, z2), color);
         }
      }

   }

   public void drawPenis(class_1657 player, class_4587 event, double size, class_243 start) {
      class_243 copy = start;
      start = start.method_1019(class_243.method_1030(0.0F, player.method_36454()).method_1021(0.1D));
      class_243 end = start.method_1019(class_243.method_1030(0.0F, player.method_36454()).method_1021(size));
      List<class_243> vecs = this.getVec3ds(start, 0.1D);
      vecs.forEach((vec3d) -> {
         if (vec3d.method_24802(copy, 0.145D)) {
            if (!vec3d.method_24802(copy, 0.135D)) {
               class_243 pos = vec3d.method_1019(class_243.method_1030(0.0F, player.method_36454()).method_1021(size));
               Render3DEngine.drawLine(vec3d, pos, ((ColorSetting)this.penisColor.getValue()).getColorObject());
            }
         }
      });
      this.drawBall(player, 0.1D, (Integer)this.gradation.getValue(), start, ((ColorSetting)this.penisColor.getValue()).getColorObject(), 1);
      this.drawBall(player, 0.1D, (Integer)this.gradation.getValue(), end, ((ColorSetting)this.headColor.getValue()).getColorObject(), 2);
   }

   public List<class_243> getVec3ds(class_243 vec3d, double radius) {
      List<class_243> vec3ds = new ArrayList();

      for(float alpha = 0.0F; (double)alpha < 3.141592653589793D; alpha = (float)((double)alpha + 3.141592653589793D / (double)(Integer)this.gradation.getValue())) {
         for(float beta = 0.0F; (double)beta < 6.314601203754922D; beta = (float)((double)beta + 3.141592653589793D / (double)(Integer)this.gradation.getValue())) {
            double x1 = (double)((float)(vec3d.method_10216() + radius * Math.cos((double)beta) * Math.sin((double)alpha)));
            double y1 = (double)((float)(vec3d.method_10214() + radius * Math.sin((double)beta) * Math.sin((double)alpha)));
            double z1 = (double)((float)(vec3d.method_10215() + radius * Math.cos((double)alpha)));
            class_243 vec = new class_243(x1, y1, z1);
            vec3ds.add(vec);
         }
      }

      return vec3ds;
   }
}
