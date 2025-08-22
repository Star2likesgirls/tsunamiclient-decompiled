package tsunami.features.modules.render;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import net.minecraft.class_156;
import net.minecraft.class_2189;
import net.minecraft.class_2213;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2288;
import net.minecraft.class_2338;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_2680;
import net.minecraft.class_4587;
import org.jetbrains.annotations.NotNull;
import tsunami.features.modules.Module;
import tsunami.features.modules.client.ClientSettings;
import tsunami.setting.Setting;
import tsunami.setting.impl.BooleanSettingGroup;
import tsunami.setting.impl.ColorSetting;
import tsunami.setting.impl.ItemSelectSetting;
import tsunami.utility.Timer;
import tsunami.utility.player.PlayerUtility;
import tsunami.utility.render.Render3DEngine;

public class BlockESP extends Module {
   public final Setting<ItemSelectSetting> selectedBlocks = new Setting("SelectedBlocks", new ItemSelectSetting(new ArrayList()));
   public static ArrayList<BlockESP.BlockVec> blocks = new ArrayList();
   private final Setting<Integer> range = new Setting("Range", 100, 1, 128);
   private final Setting<BooleanSettingGroup> limit = new Setting("Limit", new BooleanSettingGroup(true));
   private final Setting<Integer> limitCount;
   private final Setting<ColorSetting> color;
   private final Setting<Boolean> illegals;
   private final Setting<Boolean> tracers;
   private final Setting<Boolean> fill;
   private final Setting<Boolean> outline;
   private final ExecutorService searchThread;
   private final Timer searchTimer;
   private long lastFrameTime;
   private boolean canContinue;

   public BlockESP() {
      super("BlockESP", Module.Category.DONUT);
      this.limitCount = (new Setting("LimitCount", 50, 1, 2048)).addToGroup(this.limit);
      this.color = new Setting("Color", new ColorSetting(-16711681));
      this.illegals = new Setting("Illegals", true);
      this.tracers = new Setting("Tracers", false);
      this.fill = new Setting("Fill", true);
      this.outline = new Setting("Outline", true);
      this.searchThread = Executors.newSingleThreadExecutor();
      this.searchTimer = new Timer();
   }

   public void onEnable() {
      blocks.clear();
      this.lastFrameTime = System.currentTimeMillis();
      this.canContinue = true;
   }

   public void onUpdate() {
      if (this.searchTimer.every(1000L) && this.canContinue) {
         CompletableFuture.supplyAsync(this::scan, this.searchThread).thenAcceptAsync(this::sync, class_156.method_18349());
         this.canContinue = false;
      }

   }

   private ArrayList<BlockESP.BlockVec> scan() {
      ArrayList<BlockESP.BlockVec> blocks = new ArrayList();
      int startX = (int)Math.floor(mc.field_1724.method_23317() - (double)(Integer)this.range.getValue());
      int endX = (int)Math.ceil(mc.field_1724.method_23317() + (double)(Integer)this.range.getValue());
      int startY = mc.field_1687.method_31607() + 1;
      int endY = mc.field_1687.method_31600();
      int startZ = (int)Math.floor(mc.field_1724.method_23321() - (double)(Integer)this.range.getValue());
      int endZ = (int)Math.ceil(mc.field_1724.method_23321() + (double)(Integer)this.range.getValue());

      for(int x = startX; x <= endX; ++x) {
         for(int y = startY; y <= endY; ++y) {
            for(int z = startZ; z <= endZ; ++z) {
               class_2338 pos = new class_2338(x, y, z);
               class_2680 bs = mc.field_1687.method_8320(pos);
               if (this.shouldAdd(bs.method_26204(), pos)) {
                  blocks.add(new BlockESP.BlockVec((double)pos.method_10263(), (double)pos.method_10264(), (double)pos.method_10260()));
               }
            }
         }
      }

      return blocks;
   }

   private void sync(ArrayList<BlockESP.BlockVec> b) {
      blocks = b;
      this.canContinue = true;
   }

   public void onRender3D(class_4587 stack) {
      if (!fullNullCheck() && !blocks.isEmpty()) {
         int count = 0;
         if (mc.method_47599() < 8 && mc.field_1724.field_6012 > 100) {
            this.disable(ClientSettings.isRu() ? "Спасаем твой ПК :)" : "Saving ur pc :)");
         } else {
            if ((Boolean)this.fill.getValue() || (Boolean)this.outline.getValue()) {
               Iterator var3 = Lists.newArrayList(blocks).iterator();

               label50:
               while(true) {
                  BlockESP.BlockVec vec;
                  do {
                     if (!var3.hasNext()) {
                        break label50;
                     }

                     vec = (BlockESP.BlockVec)var3.next();
                  } while(count > (Integer)this.limitCount.getValue() && ((BooleanSettingGroup)this.limit.getValue()).isEnabled());

                  if (vec.getDistance(mc.field_1724.method_19538()) > (double)this.range.getPow2Value()) {
                     blocks.remove(vec);
                  } else {
                     class_238 b = new class_238(vec.x, vec.y, vec.z, vec.x + 1.0D, vec.y + 1.0D, vec.z + 1.0D);
                     if ((Boolean)this.fill.getValue()) {
                        Render3DEngine.FILLED_QUEUE.add(new Render3DEngine.FillAction(b, ((ColorSetting)this.color.getValue()).getColorObject()));
                     }

                     if ((Boolean)this.outline.getValue()) {
                        Render3DEngine.OUTLINE_QUEUE.add(new Render3DEngine.OutlineAction(b, ((ColorSetting)this.color.getValue()).getColorObject(), 2.0F));
                     }

                     if ((Boolean)this.tracers.getValue()) {
                        class_243 vec2 = (new class_243(0.0D, 0.0D, 75.0D)).method_1037(-((float)Math.toRadians((double)mc.field_1773.method_19418().method_19329()))).method_1024(-((float)Math.toRadians((double)mc.field_1773.method_19418().method_19330()))).method_1019(mc.field_1719.method_33571());
                        Render3DEngine.drawLineDebug(vec2, vec.getVector(), ((ColorSetting)this.color.getValue()).getColorObject());
                     }

                     ++count;
                  }
               }
            }

            this.lastFrameTime = System.currentTimeMillis();
         }
      }
   }

   private boolean shouldAdd(class_2248 block, class_2338 pos) {
      if (block instanceof class_2189) {
         return false;
      } else if (((ItemSelectSetting)this.selectedBlocks.getValue()).contains(block)) {
         return true;
      } else {
         return (Boolean)this.illegals.getValue() ? this.isIllegal(block, pos) : false;
      }
   }

   private boolean isIllegal(class_2248 block, class_2338 pos) {
      if (!(block instanceof class_2288) && !(block instanceof class_2213)) {
         if (block == class_2246.field_9987) {
            if (!PlayerUtility.isInHell()) {
               return pos.method_10264() > 4;
            } else {
               return pos.method_10264() > 127 || pos.method_10264() < 123 && pos.method_10264() > 4;
            }
         } else {
            return false;
         }
      } else {
         return true;
      }
   }

   public static record BlockVec(double x, double y, double z) {
      public BlockVec(double x, double y, double z) {
         this.x = x;
         this.y = y;
         this.z = z;
      }

      public double getDistance(@NotNull class_243 v) {
         double dx = this.x - v.field_1352;
         double dy = this.y - v.field_1351;
         double dz = this.z - v.field_1350;
         return dx * dx + dy * dy + dz * dz;
      }

      public class_243 getVector() {
         return new class_243(this.x + 0.5D, this.y + 0.5D, this.z + 0.5D);
      }

      public double x() {
         return this.x;
      }

      public double y() {
         return this.y;
      }

      public double z() {
         return this.z;
      }
   }
}
