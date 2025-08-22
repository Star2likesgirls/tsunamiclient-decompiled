package tsunami.features.modules.render;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import net.minecraft.class_1923;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_2465;
import net.minecraft.class_2561;
import net.minecraft.class_2680;
import net.minecraft.class_2818;
import net.minecraft.class_3414;
import net.minecraft.class_3417;
import net.minecraft.class_4587;
import net.minecraft.class_2350.class_2351;
import tsunami.features.modules.Module;
import tsunami.features.modules.client.ClientSettings;
import tsunami.setting.Setting;
import tsunami.setting.impl.ColorSetting;
import tsunami.utility.render.Render2DEngine;
import tsunami.utility.render.Render3DEngine;

public class ChunkFinder extends Module {
   private final Setting<ColorSetting> color = new Setting("Color", new ColorSetting(-16711936));
   private final Setting<Boolean> tracers = new Setting("Tracers", true);
   private final Setting<Boolean> fill = new Setting("Fill", true);
   private final Setting<Boolean> outline = new Setting("Outline", true);
   private final Setting<Integer> minY = new Setting("MinY", 8, -64, 320);
   private final Setting<Integer> maxUndergroundY = new Setting("MaxUndergroundY", 60, -64, 320);
   private final Setting<Integer> highlightY = new Setting("HighlightY", 30, -64, 320);
   private final Setting<Boolean> playSound = new Setting("PlaySound", true);
   private final Setting<Boolean> chatNotification = new Setting("ChatNotification", true);
   private final Setting<Boolean> findRotatedDeepslate = new Setting("FindRotatedDeepslate", true);
   private final Set<class_1923> flaggedChunks = ConcurrentHashMap.newKeySet();
   private final Set<class_1923> scannedChunks = ConcurrentHashMap.newKeySet();
   private final Set<class_1923> notifiedChunks = ConcurrentHashMap.newKeySet();
   private ExecutorService scannerThread;
   private Future<?> currentScanTask;
   private volatile boolean shouldStop = false;
   private long lastScanTime = 0L;
   private static final long SCAN_COOLDOWN = 500L;
   private long lastCleanupTime = 0L;
   private static final long CLEANUP_INTERVAL = 5000L;

   public ChunkFinder() {
      super("ChunkFinder", Module.Category.DONUT);
   }

   public void onEnable() {
      this.flaggedChunks.clear();
      this.scannedChunks.clear();
      this.notifiedChunks.clear();
      this.shouldStop = false;
      this.scannerThread = Executors.newSingleThreadExecutor((r) -> {
         Thread t = new Thread(r, "ChunkFinder-Scanner");
         t.setDaemon(true);
         t.setPriority(1);
         return t;
      });
      this.scheduleChunkScan();
   }

   public void onDisable() {
      this.shouldStop = true;
      if (this.currentScanTask != null && !this.currentScanTask.isDone()) {
         this.currentScanTask.cancel(true);
      }

      if (this.scannerThread != null && !this.scannerThread.isShutdown()) {
         this.scannerThread.shutdownNow();
      }

      this.flaggedChunks.clear();
      this.scannedChunks.clear();
      this.notifiedChunks.clear();
   }

   public void onUpdate() {
      if (mc.field_1687 != null && mc.field_1724 != null) {
         long currentTime = System.currentTimeMillis();
         if (currentTime - this.lastScanTime > 500L) {
            this.scheduleChunkScan();
            this.lastScanTime = currentTime;
         }

         if (currentTime - this.lastCleanupTime > 5000L) {
            this.cleanupDistantChunks();
            this.lastCleanupTime = currentTime;
         }

      }
   }

   private void scheduleChunkScan() {
      if (!this.shouldStop && this.scannerThread != null && !this.scannerThread.isShutdown()) {
         if (this.currentScanTask != null && !this.currentScanTask.isDone()) {
            this.currentScanTask.cancel(false);
         }

         this.currentScanTask = this.scannerThread.submit(this::scanChunksBackground);
      }
   }

   private void scanChunksBackground() {
      if (!this.shouldStop && mc.field_1687 != null && mc.field_1724 != null) {
         try {
            List<class_2818> loadedChunks = getLoadedChunks();
            Iterator var2 = loadedChunks.iterator();

            while(var2.hasNext()) {
               class_2818 chunk = (class_2818)var2.next();
               if (!this.shouldStop && chunk != null && !chunk.method_12223()) {
                  class_1923 chunkPos = chunk.method_12004();
                  if (!this.scannedChunks.contains(chunkPos)) {
                     boolean wasAlreadyFlagged = this.flaggedChunks.contains(chunkPos);
                     boolean shouldFlag = this.scanChunkForCoveredOres(chunk);
                     if (shouldFlag) {
                        this.flaggedChunks.add(chunkPos);
                        if (!wasAlreadyFlagged && !this.notifiedChunks.contains(chunkPos)) {
                           this.notifyChunkFound(chunkPos);
                           this.notifiedChunks.add(chunkPos);
                        }
                     } else {
                        this.flaggedChunks.remove(chunkPos);
                        this.notifiedChunks.remove(chunkPos);
                     }

                     this.scannedChunks.add(chunkPos);
                     Thread.sleep(5L);
                  }
               }
            }
         } catch (InterruptedException var7) {
            Thread.currentThread().interrupt();
         } catch (Exception var8) {
            System.err.println("Error in ChunkFinder background scanning: " + var8.getMessage());
         }

      }
   }

   private void notifyChunkFound(class_1923 chunkPos) {
      mc.execute(() -> {
         try {
            if ((Boolean)this.chatNotification.getValue() && mc.field_1724 != null) {
               String message = ClientSettings.isRu() ? "§a\ud83d\udd0d [ChunkFinder] §fНайден подземный чанк: \ud83d\udccd" + chunkPos.field_9181 + ", " + chunkPos.field_9180 : "§b\ud83d\udd0d [ChunkFinder] ✨ Sus Chunk Found: \ud83d\udccd " + chunkPos.field_9181 + ", " + chunkPos.field_9180;
               mc.field_1724.method_7353(class_2561.method_43470(message), false);
            }

            if ((Boolean)this.playSound.getValue() && mc.field_1724 != null) {
               mc.field_1724.method_5783((class_3414)class_3417.field_14793.comp_349(), 1.0F, 1.0F);
            }
         } catch (Exception var3) {
         }

      });
   }

   private boolean scanChunkForCoveredOres(class_2818 chunk) {
      if (!this.shouldStop && chunk != null && !chunk.method_12223()) {
         class_1923 cpos = chunk.method_12004();
         int xStart = cpos.method_8326();
         int zStart = cpos.method_8328();
         int yMin = Math.max(chunk.method_31607(), (Integer)this.minY.getValue());
         int yMax = Math.min(chunk.method_31607() + chunk.method_31605(), (Integer)this.maxUndergroundY.getValue());

         for(int x = xStart; x < xStart + 16; ++x) {
            for(int z = zStart; z < zStart + 16; ++z) {
               for(int y = yMin; y < yMax; ++y) {
                  if (this.shouldStop) {
                     return false;
                  }

                  class_2338 pos = new class_2338(x, y, z);

                  try {
                     class_2680 state = chunk.method_8320(pos);
                     if (this.isTargetBlock(state) && y >= (Integer)this.minY.getValue() && y <= (Integer)this.maxUndergroundY.getValue() && this.isBlockCovered(chunk, pos) && this.isPositionUnderground(pos)) {
                        return true;
                     }

                     if ((Boolean)this.findRotatedDeepslate.getValue() && this.isRotatedDeepslate(state) && this.isBlockCovered(chunk, pos) && this.isPositionUnderground(pos)) {
                        return true;
                     }
                  } catch (Exception var12) {
                  }
               }
            }
         }

         return false;
      } else {
         return false;
      }
   }

   private boolean isPositionUnderground(class_2338 pos) {
      if (mc.field_1687 == null) {
         return false;
      } else {
         int checkHeight = Math.min(pos.method_10264() + 50, (Integer)this.maxUndergroundY.getValue() + 20);
         int solidBlocksAbove = 0;

         for(int y = pos.method_10264() + 1; y <= checkHeight; ++y) {
            class_2338 checkPos = new class_2338(pos.method_10263(), y, pos.method_10260());
            class_2680 state = mc.field_1687.method_8320(checkPos);
            if (!state.method_26215() && state.method_26212(mc.field_1687, checkPos)) {
               ++solidBlocksAbove;
            }
         }

         return solidBlocksAbove >= 3;
      }
   }

   private boolean isTargetBlock(class_2680 state) {
      if (state != null && !state.method_26215()) {
         class_2248 block = state.method_26204();
         if (block == class_2246.field_28888) {
            return !this.isRotatedDeepslate(state);
         } else {
            return block == class_2246.field_27165;
         }
      } else {
         return false;
      }
   }

   private boolean isRotatedDeepslate(class_2680 state) {
      if (state != null && !state.method_26215()) {
         class_2248 block = state.method_26204();
         if (block == class_2246.field_28888 && state.method_28498(class_2465.field_11459)) {
            class_2351 axis = (class_2351)state.method_11654(class_2465.field_11459);
            return axis == class_2351.field_11048 || axis == class_2351.field_11051;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private boolean isBlockCovered(class_2818 chunk, class_2338 pos) {
      class_2350[] directions = new class_2350[]{class_2350.field_11036, class_2350.field_11033, class_2350.field_11043, class_2350.field_11035, class_2350.field_11034, class_2350.field_11039};
      class_2350[] var4 = directions;
      int var5 = directions.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         class_2350 dir = var4[var6];
         class_2338 adjacentPos = pos.method_10093(dir);

         try {
            class_2680 adjacentState = null;
            if (mc.field_1687 != null) {
               adjacentState = mc.field_1687.method_8320(adjacentPos);
               if (!adjacentState.method_26215() && !this.isTransparentBlock(adjacentState)) {
                  if (adjacentState.method_26212(mc.field_1687, adjacentPos)) {
                     continue;
                  }

                  return false;
               }

               return false;
            }

            return false;
         } catch (Exception var10) {
            return false;
         }
      }

      return true;
   }

   private boolean isTransparentBlock(class_2680 state) {
      class_2248 block = state.method_26204();
      if (block != class_2246.field_10033 && block != class_2246.field_10382 && block != class_2246.field_10164 && block != class_2246.field_10295 && block != class_2246.field_10225 && block != class_2246.field_10384) {
         if (block != class_2246.field_10087 && block != class_2246.field_10227 && block != class_2246.field_10574 && block != class_2246.field_10271 && block != class_2246.field_10049 && block != class_2246.field_10157 && block != class_2246.field_10317 && block != class_2246.field_10555 && block != class_2246.field_9996 && block != class_2246.field_10248 && block != class_2246.field_10399 && block != class_2246.field_10060 && block != class_2246.field_10073 && block != class_2246.field_10357 && block != class_2246.field_10272 && block != class_2246.field_9997 && block != class_2246.field_27115) {
            return block == class_2246.field_10503 || block == class_2246.field_9988 || block == class_2246.field_10539 || block == class_2246.field_10335 || block == class_2246.field_10098 || block == class_2246.field_10035 || block == class_2246.field_37551 || block == class_2246.field_42731;
         } else {
            return true;
         }
      } else {
         return true;
      }
   }

   private void cleanupDistantChunks() {
      if (mc.field_1724 != null) {
         int viewDist = (Integer)mc.field_1690.method_42503().method_41753();
         int playerChunkX = (int)mc.field_1724.method_23317() / 16;
         int playerChunkZ = (int)mc.field_1724.method_23321() / 16;
         this.flaggedChunks.removeIf((chunkPos) -> {
            int dx = Math.abs(chunkPos.field_9181 - playerChunkX);
            int dz = Math.abs(chunkPos.field_9180 - playerChunkZ);
            return dx > viewDist || dz > viewDist;
         });
         this.scannedChunks.removeIf((chunkPos) -> {
            int dx = Math.abs(chunkPos.field_9181 - playerChunkX);
            int dz = Math.abs(chunkPos.field_9180 - playerChunkZ);
            return dx > viewDist || dz > viewDist;
         });
         this.notifiedChunks.removeIf((chunkPos) -> {
            int dx = Math.abs(chunkPos.field_9181 - playerChunkX);
            int dz = Math.abs(chunkPos.field_9180 - playerChunkZ);
            return dx > viewDist || dz > viewDist;
         });
      }
   }

   public void onRender3D(class_4587 stack) {
      if (!fullNullCheck() && !this.flaggedChunks.isEmpty()) {
         if (mc.method_47599() < 8 && mc.field_1724.field_6012 > 100) {
            this.disable(ClientSettings.isRu() ? "Спасаем твой ПК :)" : "Saving ur pc :)");
         } else {
            Iterator var2 = this.flaggedChunks.iterator();

            while(var2.hasNext()) {
               class_1923 chunkPos = (class_1923)var2.next();
               this.renderChunkHighlight(stack, chunkPos);
            }

         }
      }
   }

   private void renderChunkHighlight(class_4587 stack, class_1923 chunkPos) {
      int startX = chunkPos.field_9181 * 16;
      int startZ = chunkPos.field_9180 * 16;
      int endX = startX + 16;
      int endZ = startZ + 16;
      double y = (double)(Integer)this.highlightY.getValue();
      double height = 0.10000000149011612D;
      class_238 chunkBox = new class_238((double)startX, y, (double)startZ, (double)endX, y + height, (double)endZ);
      Color renderColor = ((ColorSetting)this.color.getValue()).getColorObject();
      if ((Boolean)this.fill.getValue()) {
         Render3DEngine.drawFilledBox(stack, chunkBox, renderColor);
      }

      if ((Boolean)this.outline.getValue()) {
         Render3DEngine.drawBoxOutline(chunkBox, Render2DEngine.injectAlpha(renderColor, 255), 2.0F);
      }

      if ((Boolean)this.tracers.getValue()) {
         class_243 tracerStart = (new class_243(0.0D, 0.0D, 75.0D)).method_1037(-((float)Math.toRadians((double)mc.field_1773.method_19418().method_19329()))).method_1024(-((float)Math.toRadians((double)mc.field_1773.method_19418().method_19330()))).method_1019(mc.field_1719.method_33571());
         class_243 chunkCenter = new class_243((double)(startX + 8), y + height / 2.0D, (double)(startZ + 8));
         Render3DEngine.drawLineDebug(tracerStart, chunkCenter, renderColor);
      }

   }

   public static List<class_2818> getLoadedChunks() {
      List<class_2818> chunks = new ArrayList();
      int viewDist = (Integer)mc.field_1690.method_42503().method_41753();

      for(int x = -viewDist; x <= viewDist; ++x) {
         for(int z = -viewDist; z <= viewDist; ++z) {
            class_2818 chunk = mc.field_1687.method_2935().method_21730((int)mc.field_1724.method_23317() / 16 + x, (int)mc.field_1724.method_23321() / 16 + z);
            if (chunk != null) {
               chunks.add(chunk);
            }
         }
      }

      return chunks;
   }

   public void onBlockUpdate(class_2338 pos, class_2680 newState) {
      if (mc.field_1687 != null && mc.field_1724 != null) {
         class_1923 chunkPos = new class_1923(pos);
         this.scannedChunks.remove(chunkPos);
         if (newState.method_26215() && this.flaggedChunks.contains(chunkPos)) {
            this.flaggedChunks.remove(chunkPos);
            this.notifiedChunks.remove(chunkPos);
         }

      }
   }

   public void onChunkLoad(class_1923 chunkPos) {
      this.scannedChunks.remove(chunkPos);
   }

   public void onChunkUnload(class_1923 chunkPos) {
      this.flaggedChunks.remove(chunkPos);
      this.scannedChunks.remove(chunkPos);
      this.notifiedChunks.remove(chunkPos);
   }
}
