package tsunami.features.modules.player;

import net.minecraft.class_124;
import net.minecraft.class_418;
import net.minecraft.class_437;
import tsunami.core.Managers;
import tsunami.core.manager.world.WayPointManager;
import tsunami.features.modules.Module;
import tsunami.setting.Setting;
import tsunami.utility.Timer;

public class AutoRespawn extends Module {
   private final Setting<Boolean> deathcoords = new Setting("deathcoords", true);
   private final Setting<Boolean> autokit = new Setting("Auto Kit", false);
   private final Setting<String> kit = new Setting("kit name", "kitname", (v) -> {
      return (Boolean)this.autokit.getValue();
   });
   private final Setting<Boolean> autohome = new Setting("Auto Home", false);
   private final Setting<Boolean> autowaypoint = new Setting("Auto Waypoint", false);
   private boolean flag;
   private int waypointCount = 0;
   private final Timer timer = new Timer();

   public AutoRespawn() {
      super("AutoRespawn", Module.Category.NONE);
   }

   public void onUpdate() {
      if (!fullNullCheck()) {
         if (this.timer.passedMs(2100L)) {
            this.timer.reset();
         }

         if (mc.field_1755 instanceof class_418) {
            if (this.flag) {
               ++this.waypointCount;
               if ((Boolean)this.deathcoords.getValue()) {
                  String var10001 = String.valueOf(class_124.field_1065);
                  this.sendMessage(var10001 + "[PlayerDeath] " + String.valueOf(class_124.field_1054) + (int)mc.field_1724.method_23317() + " " + (int)mc.field_1724.method_23318() + " " + (int)mc.field_1724.method_23321());
               }

               if ((Boolean)this.autowaypoint.getValue()) {
                  WayPointManager.WayPoint wp = new WayPointManager.WayPoint((int)mc.field_1724.method_23317(), (int)mc.field_1724.method_23318(), (int)mc.field_1724.method_23321(), "Death №" + this.waypointCount, mc.method_1542() ? "SinglePlayer" : mc.method_1562().method_45734().field_3761, mc.field_1687.method_27983().method_29177().method_12832());
                  Managers.WAYPOINT.addWayPoint(wp);
               }

               mc.field_1724.method_7331();
               mc.method_1507((class_437)null);
               Managers.ASYNC.run(() -> {
                  if ((Boolean)this.autokit.getValue() && mc.field_1724 != null) {
                     mc.field_1724.field_3944.method_45730("kit " + (String)this.kit.getValue());
                  }

                  if ((Boolean)this.autohome.getValue() && mc.field_1724 != null) {
                     mc.field_1724.field_3944.method_45730("home");
                  }

               }, 1000L);
               this.flag = false;
            }
         } else {
            this.flag = true;
         }

      }
   }
}
