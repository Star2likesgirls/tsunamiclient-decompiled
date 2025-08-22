package tsunami.features.modules.misc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_1268;
import net.minecraft.class_1703;
import net.minecraft.class_1707;
import net.minecraft.class_1713;
import net.minecraft.class_1735;
import net.minecraft.class_1799;
import net.minecraft.class_2350;
import net.minecraft.class_2586;
import net.minecraft.class_2611;
import net.minecraft.class_3965;
import net.minecraft.class_4587;
import tsunami.events.impl.PlayerUpdateEvent;
import tsunami.features.modules.Module;
import tsunami.features.modules.render.StorageEsp;
import tsunami.setting.Setting;
import tsunami.setting.impl.ItemSelectSetting;
import tsunami.utility.Timer;
import tsunami.utility.math.MathUtility;

public class ChestStealer extends Module {
   public final Setting<ItemSelectSetting> items = new Setting("Items", new ItemSelectSetting(new ArrayList()));
   private final Setting<Integer> delay = new Setting("Delay", 100, 0, 1000);
   private final Setting<Boolean> random = new Setting("Random", false);
   private final Setting<Boolean> close = new Setting("Close", false);
   private final Setting<Boolean> autoMyst = new Setting("AutoMyst", false);
   private final Setting<ChestStealer.Sort> sort;
   private final Timer autoMystDelay;
   private final Timer timer;
   private final Random rnd;

   public ChestStealer() {
      super("ChestStealer", Module.Category.NONE);
      this.sort = new Setting("Sort", ChestStealer.Sort.None);
      this.autoMystDelay = new Timer();
      this.timer = new Timer();
      this.rnd = new Random();
   }

   public void onRender3D(class_4587 stack) {
      class_1703 var3 = mc.field_1724.field_7512;
      if (var3 instanceof class_1707) {
         class_1707 chest = (class_1707)var3;

         for(int i = 0; i < chest.method_7629().method_5439(); ++i) {
            class_1735 slot = chest.method_7611(i);
            if (slot.method_7681() && this.isAllowed(slot.method_7677()) && this.timer.every((long)((Integer)this.delay.getValue() + ((Boolean)this.random.getValue() && (Integer)this.delay.getValue() != 0 ? this.rnd.nextInt((Integer)this.delay.getValue()) : 0))) && !mc.field_1755.method_25440().getString().contains("Аукцион") && !mc.field_1755.method_25440().getString().contains("покупки")) {
               mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, i, 0, class_1713.field_7794, mc.field_1724);
               this.autoMystDelay.reset();
            }
         }

         if (this.isContainerEmpty(chest) && (Boolean)this.close.getValue()) {
            mc.field_1724.method_7346();
         }
      }

   }

   @EventHandler
   public void onPlayerUpdate(PlayerUpdateEvent event) {
      if ((Boolean)this.autoMyst.getValue() && mc.field_1755 == null && this.autoMystDelay.passedMs(3000L)) {
         Iterator var2 = StorageEsp.getBlockEntities().iterator();

         while(var2.hasNext()) {
            class_2586 be = (class_2586)var2.next();
            if (be instanceof class_2611 && !(mc.field_1724.method_5707(be.method_11016().method_46558()) > 39.0D)) {
               mc.field_1761.method_2896(mc.field_1724, class_1268.field_5808, new class_3965(be.method_11016().method_46558().method_1031(MathUtility.random(-0.4D, 0.4D), 0.375D, MathUtility.random(-0.4D, 0.4D)), class_2350.field_11036, be.method_11016(), false));
               mc.field_1724.method_6104(class_1268.field_5808);
               break;
            }
         }
      }

   }

   private boolean isAllowed(class_1799 stack) {
      boolean allowed = ((ItemSelectSetting)this.items.getValue()).contains(stack.method_7909().method_7876().replace("block.minecraft.", "").replace("item.minecraft.", ""));
      boolean var10000;
      switch(((ChestStealer.Sort)this.sort.getValue()).ordinal()) {
      case 0:
         var10000 = true;
         break;
      case 1:
         var10000 = allowed;
         break;
      default:
         var10000 = !allowed;
      }

      return var10000;
   }

   private boolean isContainerEmpty(class_1707 container) {
      for(int i = 0; i < (container.method_7629().method_5439() == 90 ? 54 : 27); ++i) {
         if (container.method_7611(i).method_7681()) {
            return false;
         }
      }

      return true;
   }

   private static enum Sort {
      None,
      WhiteList,
      BlackList;

      // $FF: synthetic method
      private static ChestStealer.Sort[] $values() {
         return new ChestStealer.Sort[]{None, WhiteList, BlackList};
      }
   }
}
