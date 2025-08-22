package tsunami.features.modules.player;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.util.Iterator;
import net.minecraft.class_1657;
import net.minecraft.class_1738;
import net.minecraft.class_1799;
import net.minecraft.class_332;
import net.minecraft.class_634;
import tsunami.core.Managers;
import tsunami.features.modules.Module;
import tsunami.features.modules.client.ClientSettings;
import tsunami.gui.font.FontRenderers;
import tsunami.setting.Setting;
import tsunami.utility.Timer;
import tsunami.utility.render.TextureStorage;

public class DurabilityAlert extends Module {
   private final Setting<Boolean> friends = new Setting("Friend message", true);
   private final Setting<Integer> percent = new Setting("Percent", 20, 1, 100);
   private boolean need_alert = false;
   private final Timer timer = new Timer();

   public DurabilityAlert() {
      super("DurabilityAlert", Module.Category.NONE);
   }

   public void onUpdate() {
      if ((Boolean)this.friends.getValue()) {
         Iterator var1 = mc.field_1687.method_18456().iterator();

         label68:
         while(true) {
            class_1657 player;
            do {
               do {
                  if (!var1.hasNext()) {
                     break label68;
                  }

                  player = (class_1657)var1.next();
               } while(!Managers.FRIEND.isFriend(player));
            } while(player == mc.field_1724);

            Iterator var3 = player.method_31548().field_7548.iterator();

            while(var3.hasNext()) {
               class_1799 stack = (class_1799)var3.next();
               if (!stack.method_7960() && stack.method_7909() instanceof class_1738 && getDurability(stack) < (Integer)this.percent.getValue() && this.timer.passedMs(30000L)) {
                  class_634 var10000 = mc.field_1724.field_3944;
                  String var10001 = player.method_5477().getString();
                  var10000.method_45730("msg " + var10001 + (ClientSettings.isRu() ? " Срочно чини броню!" : " Fix your armor right now!"));
                  this.timer.reset();
               }
            }
         }
      }

      boolean flag = false;
      Iterator var6 = mc.field_1724.method_31548().field_7548.iterator();

      while(var6.hasNext()) {
         class_1799 stack = (class_1799)var6.next();
         if (!stack.method_7960() && stack.method_7909() instanceof class_1738 && getDurability(stack) < (Integer)this.percent.getValue()) {
            this.need_alert = true;
            flag = true;
         }
      }

      if (!flag && this.need_alert) {
         this.need_alert = false;
      }

   }

   public void onRender2D(class_332 context) {
      if (this.need_alert) {
         FontRenderers.sf_bold.drawCenteredString(context.method_51448(), ClientSettings.isRu() ? "Срочно чини броню!" : "Fix your armor right now!", (double)((float)mc.method_22683().method_4486() / 2.0F), (double)((float)mc.method_22683().method_4502() / 3.0F), (new Color(16768768)).getRGB());
         Color c1 = new Color(16768768);
         RenderSystem.setShaderColor((float)c1.getRed() / 255.0F, (float)c1.getGreen() / 255.0F, (float)c1.getBlue() / 255.0F, 1.0F);
         context.method_25293(TextureStorage.brokenShield, (int)((float)mc.method_22683().method_4486() / 2.0F - 40.0F), (int)((float)mc.method_22683().method_4502() / 3.0F - 120.0F), 80, 80, 0.0F, 0.0F, 80, 80, 80, 80);
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      }

   }

   public static int getDurability(class_1799 stack) {
      return (int)((double)(stack.method_7936() - stack.method_7919()) / Math.max(0.1D, (double)stack.method_7936()) * 100.0D);
   }
}
