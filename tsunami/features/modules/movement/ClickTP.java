package tsunami.features.modules.movement;

import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_2338;
import net.minecraft.class_238;
import net.minecraft.class_239;
import net.minecraft.class_243;
import net.minecraft.class_3965;
import net.minecraft.class_4587;
import net.minecraft.class_2828.class_2829;
import tsunami.events.impl.EventSync;
import tsunami.features.modules.Module;
import tsunami.features.modules.client.HudEditor;
import tsunami.setting.Setting;
import tsunami.utility.render.Render3DEngine;

public class ClickTP extends Module {
   private final Setting<Float> blockOffset = new Setting("BlockOffset", 1.0F, -1.0F, 1.0F);
   private final Setting<Integer> spoofs = new Setting("Spoofs", 0, 0, 40);
   private final Setting<Boolean> ground = new Setting("Ground", false);
   private int delay;

   public ClickTP() {
      super("ClickTP", Module.Category.NONE);
   }

   @EventHandler
   public void onSync(EventSync e) {
      if (this.delay >= 0) {
         --this.delay;
      }

      if (mc.field_1690.field_1871.method_1434() && this.delay < 0) {
         class_239 ray = mc.field_1724.method_5745(256.0D, Render3DEngine.getTickDelta(), false);
         if (ray instanceof class_3965) {
            class_3965 bhr = (class_3965)ray;
            if (!mc.field_1687.method_22347(bhr.method_17777())) {
               class_243 pos = bhr.method_17777().method_46558();

               for(int i = 0; i < (Integer)this.spoofs.getValue(); ++i) {
                  this.sendPacket(new class_2829(pos.method_10216(), pos.method_10214() + (double)(Float)this.blockOffset.getValue(), pos.method_10215(), (Boolean)this.ground.getValue()));
               }

               mc.field_1724.method_5814(pos.method_10216(), pos.method_10214() + (double)(Float)this.blockOffset.getValue(), pos.method_10215());
               this.delay = 5;
            }
         }
      }

   }

   public void onRender3D(class_4587 stack) {
      class_239 ray = mc.field_1724.method_5745(256.0D, Render3DEngine.getTickDelta(), false);
      if (ray instanceof class_3965) {
         class_3965 bhr = (class_3965)ray;
         if (!mc.field_1687.method_22347(bhr.method_17777())) {
            class_2338 pos = bhr.method_17777();
            Render3DEngine.OUTLINE_QUEUE.add(new Render3DEngine.OutlineAction(new class_238(pos), HudEditor.getColor(1), 1.0F));
         }
      }

   }
}
