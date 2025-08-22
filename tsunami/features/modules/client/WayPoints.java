package tsunami.features.modules.client;

import java.util.Iterator;
import net.minecraft.class_124;
import net.minecraft.class_243;
import net.minecraft.class_332;
import net.minecraft.class_3532;
import org.joml.Vector4d;
import tsunami.core.Managers;
import tsunami.core.manager.world.WayPointManager;
import tsunami.features.modules.Module;
import tsunami.gui.font.FontRenderers;
import tsunami.utility.render.Render3DEngine;
import tsunami.utility.render.TextureStorage;

public final class WayPoints extends Module {
   public WayPoints() {
      super("WayPoints", Module.Category.CLIENT);
   }

   public void onEnable() {
      this.sendMessage(Managers.COMMAND.getPrefix() + "waypoint add x y z name");
   }

   public void onRender2D(class_332 context) {
      if (!Managers.WAYPOINT.getWayPoints().isEmpty() && !fullNullCheck()) {
         Iterator var2 = Managers.WAYPOINT.getWayPoints().iterator();

         while(true) {
            WayPointManager.WayPoint wp;
            do {
               do {
                  do {
                     if (!var2.hasNext()) {
                        return;
                     }

                     wp = (WayPointManager.WayPoint)var2.next();
                  } while(wp.getName() == null);
               } while(mc.method_1542() && wp.getServer().equals("SinglePlayer"));
            } while(mc.method_1562().method_45734() != null && !mc.method_1562().method_45734().field_3761.contains(wp.getServer()));

            if (mc.field_1687.method_27983().method_29177().method_12832().equals(wp.getDimension())) {
               double difX = (double)wp.getX() - mc.field_1724.method_19538().field_1352;
               double difZ = (double)wp.getZ() - mc.field_1724.method_19538().field_1350;
               float yaw = (float)class_3532.method_15338(Math.toDegrees(Math.atan2(difZ, difX)) - 90.0D);
               double plYaw = (double)class_3532.method_15393(mc.field_1724.method_36454());
               if (!(Math.abs((double)yaw - plYaw) > 90.0D)) {
                  class_243 vector = new class_243((double)wp.getX(), (double)wp.getY(), (double)wp.getZ());
                  Vector4d position = null;
                  vector = Render3DEngine.worldSpaceToScreenSpace(new class_243(vector.field_1352, vector.field_1351, vector.field_1350));
                  position = new Vector4d(vector.field_1352, vector.field_1351, vector.field_1350, 0.0D);
                  position.x = Math.min(vector.field_1352, position.x);
                  position.y = Math.min(vector.field_1351, position.y);
                  position.z = Math.max(vector.field_1352, position.z);
                  double posX = position.x;
                  double posY = position.y;
                  double endPosX = position.z;
                  float diff = (float)(endPosX - posX) / 2.0F;
                  float tagX = (float)((posX + (double)diff - (double)(FontRenderers.sf_bold_mini.getStringWidth(wp.getName()) / 2.0F)) * 1.0D);
                  int var10000 = wp.getX();
                  String coords = var10000 + " " + wp.getZ();
                  float tagX2 = (float)((posX + (double)diff - (double)(FontRenderers.sf_bold_mini.getStringWidth(coords) / 2.0F)) * 1.0D);
                  Object[] var10001 = new Object[]{Math.sqrt(mc.field_1724.method_5649((double)wp.getX(), (double)wp.getY(), (double)wp.getZ()))};
                  String distance = String.format("%.0f", var10001) + "m";
                  float tagX3 = (float)((posX + (double)diff - (double)(FontRenderers.sf_bold_mini.getStringWidth(distance) / 2.0F)) * 1.0D);
                  context.method_51448().method_22903();
                  context.method_51448().method_22904(posX - 10.0D, posY - 35.0D, 0.0D);
                  context.method_25293(TextureStorage.waypoint, 0, 0, 20, 20, 0.0F, 0.0F, 20, 20, 20, 20);
                  context.method_51448().method_22909();
                  FontRenderers.sf_bold_mini.drawString(context.method_51448(), wp.getName(), (double)tagX, (double)((float)posY - 10.0F), -1);
                  FontRenderers.sf_bold_mini.drawString(context.method_51448(), String.valueOf(class_124.field_1080) + coords, (double)tagX2, (double)((float)posY - 2.0F), -1);
                  FontRenderers.sf_bold_mini.drawString(context.method_51448(), String.valueOf(class_124.field_1080) + distance, (double)tagX3, (double)((float)posY + 6.0F), -1);
               }
            }
         }
      }
   }
}
