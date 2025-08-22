package tsunami.utility.player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.minecraft.class_1268;
import net.minecraft.class_1297;
import net.minecraft.class_1303;
import net.minecraft.class_1542;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_2848;
import net.minecraft.class_2879;
import net.minecraft.class_2885;
import net.minecraft.class_3532;
import net.minecraft.class_3959;
import net.minecraft.class_3965;
import net.minecraft.class_7202;
import net.minecraft.class_7204;
import net.minecraft.class_239.class_240;
import net.minecraft.class_2828.class_2830;
import net.minecraft.class_2828.class_2831;
import net.minecraft.class_2848.class_2849;
import net.minecraft.class_3959.class_242;
import net.minecraft.class_3959.class_3960;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tsunami.features.modules.Module;
import tsunami.injection.accesors.IClientWorldMixin;
import tsunami.utility.world.ExplosionUtility;

public final class InteractionUtility {
   private static final List<class_2248> SHIFT_BLOCKS;
   public static Map<class_2338, Long> awaiting;

   public static boolean canSee(class_243 vec) {
      return canSee(vec, vec);
   }

   public static boolean canSee(class_1297 entity) {
      class_243 entityEyes = getEyesPos(entity);
      class_243 entityPos = entity.method_19538();
      return canSee(entityEyes, entityPos);
   }

   public static boolean canSee(class_243 entityEyes, class_243 entityPos) {
      if (Module.mc.field_1724 != null && Module.mc.field_1687 != null) {
         class_243 playerEyes = getEyesPos(Module.mc.field_1724);
         if (ExplosionUtility.raycast(playerEyes, entityEyes, false) == class_240.field_1333) {
            return true;
         } else if (playerEyes.method_10214() > entityPos.method_10214()) {
            return ExplosionUtility.raycast(playerEyes, entityEyes, false) == class_240.field_1333;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public static class_243 getEyesPos(@NotNull class_1297 entity) {
      return entity.method_19538().method_1031(0.0D, (double)entity.method_18381(entity.method_18376()), 0.0D);
   }

   @NotNull
   public static float[] calculateAngle(class_243 to) {
      return calculateAngle(getEyesPos(Module.mc.field_1724), to);
   }

   @NotNull
   public static float[] calculateAngle(@NotNull class_243 from, @NotNull class_243 to) {
      double difX = to.field_1352 - from.field_1352;
      double difY = (to.field_1351 - from.field_1351) * -1.0D;
      double difZ = to.field_1350 - from.field_1350;
      double dist = (double)class_3532.method_15355((float)(difX * difX + difZ * difZ));
      float yD = (float)class_3532.method_15338(Math.toDegrees(Math.atan2(difZ, difX)) - 90.0D);
      float pD = (float)class_3532.method_15350(class_3532.method_15338(Math.toDegrees(Math.atan2(difY, dist))), -90.0D, 90.0D);
      return new float[]{yD, pD};
   }

   public static boolean placeBlock(class_2338 bp, InteractionUtility.Rotate rotate, InteractionUtility.Interact interact, InteractionUtility.PlaceMode mode, int slot, boolean returnSlot, boolean ignoreEntities) {
      int prevItem = Module.mc.field_1724.method_31548().field_7545;
      if (slot != -1) {
         InventoryUtility.switchTo(slot);
         boolean result = placeBlock(bp, rotate, interact, mode, ignoreEntities);
         if (returnSlot) {
            InventoryUtility.switchTo(prevItem);
         }

         return result;
      } else {
         return false;
      }
   }

   public static boolean placeBlock(class_2338 bp, InteractionUtility.Rotate rotate, InteractionUtility.Interact interact, InteractionUtility.PlaceMode mode, @NotNull SearchInvResult invResult, boolean returnSlot, boolean ignoreEntities) {
      int prevItem = Module.mc.field_1724.method_31548().field_7545;
      invResult.switchTo();
      boolean result = placeBlock(bp, rotate, interact, mode, ignoreEntities);
      if (returnSlot) {
         InventoryUtility.switchTo(prevItem);
      }

      return result;
   }

   public static boolean placeBlock(class_2338 bp, InteractionUtility.Rotate rotate, InteractionUtility.Interact interact, InteractionUtility.PlaceMode mode, boolean ignoreEntities) {
      class_3965 result = getPlaceResult(bp, interact, ignoreEntities);
      if (result != null && Module.mc.field_1687 != null && Module.mc.field_1761 != null && Module.mc.field_1724 != null) {
         boolean sprint = Module.mc.field_1724.method_5624();
         boolean sneak = needSneak(Module.mc.field_1687.method_8320(result.method_17777()).method_26204()) && !Module.mc.field_1724.method_5715();
         if (sprint) {
            Module.mc.field_1724.field_3944.method_52787(new class_2848(Module.mc.field_1724, class_2849.field_12985));
         }

         if (sneak) {
            Module.mc.field_1724.field_3944.method_52787(new class_2848(Module.mc.field_1724, class_2849.field_12979));
         }

         float[] angle = calculateAngle(result.method_17784());
         switch(rotate.ordinal()) {
         case 0:
         default:
            break;
         case 1:
            Module.mc.field_1724.field_3944.method_52787(new class_2831(angle[0], angle[1], Module.mc.field_1724.method_24828()));
            break;
         case 2:
            Module.mc.field_1724.field_3944.method_52787(new class_2830(Module.mc.field_1724.method_23317(), Module.mc.field_1724.method_23318(), Module.mc.field_1724.method_23321(), angle[0], angle[1], Module.mc.field_1724.method_24828()));
         }

         if (mode == InteractionUtility.PlaceMode.Normal) {
            Module.mc.field_1761.method_2896(Module.mc.field_1724, class_1268.field_5808, result);
         }

         if (mode == InteractionUtility.PlaceMode.Packet) {
            sendSequencedPacket((id) -> {
               return new class_2885(class_1268.field_5808, result, id);
            });
         }

         awaiting.put(bp, System.currentTimeMillis());
         if (rotate == InteractionUtility.Rotate.Grim) {
            Module.mc.field_1724.field_3944.method_52787(new class_2830(Module.mc.field_1724.method_23317(), Module.mc.field_1724.method_23318(), Module.mc.field_1724.method_23321(), Module.mc.field_1724.method_36454(), Module.mc.field_1724.method_36455(), Module.mc.field_1724.method_24828()));
         }

         if (sneak) {
            Module.mc.field_1724.field_3944.method_52787(new class_2848(Module.mc.field_1724, class_2849.field_12984));
         }

         if (sprint) {
            Module.mc.field_1724.field_3944.method_52787(new class_2848(Module.mc.field_1724, class_2849.field_12981));
         }

         Module.mc.field_1724.field_3944.method_52787(new class_2879(class_1268.field_5808));
         return true;
      } else {
         return false;
      }
   }

   public static boolean canPlaceBlock(@NotNull class_2338 bp, InteractionUtility.Interact interact, boolean ignoreEntities) {
      if (awaiting.containsKey(bp)) {
         return false;
      } else {
         return getPlaceResult(bp, interact, ignoreEntities) != null;
      }
   }

   @Nullable
   public static float[] getPlaceAngle(@NotNull class_2338 bp, InteractionUtility.Interact interact, boolean ignoreEntities) {
      class_3965 result = getPlaceResult(bp, interact, ignoreEntities);
      return result != null ? calculateAngle(result.method_17784()) : null;
   }

   public static void sendSequencedPacket(class_7204 packetCreator) {
      if (Module.mc.method_1562() != null && Module.mc.field_1687 != null) {
         class_7202 pendingUpdateManager = ((IClientWorldMixin)Module.mc.field_1687).getPendingUpdateManager().method_41937();

         try {
            int i = pendingUpdateManager.method_41942();
            Module.mc.method_1562().method_52787(packetCreator.predict(i));
         } catch (Throwable var5) {
            if (pendingUpdateManager != null) {
               try {
                  pendingUpdateManager.close();
               } catch (Throwable var4) {
                  var5.addSuppressed(var4);
               }
            }

            throw var5;
         }

         if (pendingUpdateManager != null) {
            pendingUpdateManager.close();
         }

      }
   }

   @Nullable
   public static class_3965 getPlaceResult(@NotNull class_2338 bp, InteractionUtility.Interact interact, boolean ignoreEntities) {
      if (!ignoreEntities) {
         Iterator var3 = (new ArrayList(Module.mc.field_1687.method_18467(class_1297.class, new class_238(bp)))).iterator();

         while(var3.hasNext()) {
            class_1297 entity = (class_1297)var3.next();
            if (!(entity instanceof class_1542) && !(entity instanceof class_1303)) {
               return null;
            }
         }
      }

      if (!Module.mc.field_1687.method_8320(bp).method_45474()) {
         return null;
      } else if (interact == InteractionUtility.Interact.AirPlace) {
         return ExplosionUtility.rayCastBlock(new class_3959(getEyesPos(Module.mc.field_1724), bp.method_46558(), class_3960.field_17558, class_242.field_1348, Module.mc.field_1724), bp);
      } else {
         ArrayList<InteractionUtility.BlockPosWithFacing> supports = getSupportBlocks(bp);
         Iterator var9 = supports.iterator();

         InteractionUtility.BlockPosWithFacing support;
         List dirs;
         do {
            if (!var9.hasNext()) {
               return null;
            }

            support = (InteractionUtility.BlockPosWithFacing)var9.next();
            if (interact == InteractionUtility.Interact.Vanilla) {
               break;
            }

            dirs = getStrictDirections(bp);
            if (dirs.isEmpty()) {
               return null;
            }
         } while(!dirs.contains(support.facing));

         class_3965 result = null;
         class_243 p;
         if (interact == InteractionUtility.Interact.Legit) {
            p = getVisibleDirectionPoint(support.facing, support.position, 0.0F, 6.0F);
            if (p != null) {
               return new class_3965(p, support.facing, support.position, false);
            }
         } else {
            p = new class_243((double)support.position.method_10263() + 0.5D + (double)support.facing.method_10163().method_10263() * 0.5D, (double)support.position.method_10264() + 0.5D + (double)support.facing.method_10163().method_10264() * 0.5D, (double)support.position.method_10260() + 0.5D + (double)support.facing.method_10163().method_10260() * 0.5D);
            result = new class_3965(p, support.facing, support.position, false);
         }

         return result;
      }
   }

   @NotNull
   public static ArrayList<InteractionUtility.BlockPosWithFacing> getSupportBlocks(@NotNull class_2338 bp) {
      ArrayList<InteractionUtility.BlockPosWithFacing> list = new ArrayList();
      if (Module.mc.field_1687.method_8320(bp.method_10069(0, -1, 0)).method_51367() || awaiting.containsKey(bp.method_10069(0, -1, 0))) {
         list.add(new InteractionUtility.BlockPosWithFacing(bp.method_10069(0, -1, 0), class_2350.field_11036));
      }

      if (Module.mc.field_1687.method_8320(bp.method_10069(0, 1, 0)).method_51367() || awaiting.containsKey(bp.method_10069(0, 1, 0))) {
         list.add(new InteractionUtility.BlockPosWithFacing(bp.method_10069(0, 1, 0), class_2350.field_11033));
      }

      if (Module.mc.field_1687.method_8320(bp.method_10069(-1, 0, 0)).method_51367() || awaiting.containsKey(bp.method_10069(-1, 0, 0))) {
         list.add(new InteractionUtility.BlockPosWithFacing(bp.method_10069(-1, 0, 0), class_2350.field_11034));
      }

      if (Module.mc.field_1687.method_8320(bp.method_10069(1, 0, 0)).method_51367() || awaiting.containsKey(bp.method_10069(1, 0, 0))) {
         list.add(new InteractionUtility.BlockPosWithFacing(bp.method_10069(1, 0, 0), class_2350.field_11039));
      }

      if (Module.mc.field_1687.method_8320(bp.method_10069(0, 0, 1)).method_51367() || awaiting.containsKey(bp.method_10069(0, 0, 1))) {
         list.add(new InteractionUtility.BlockPosWithFacing(bp.method_10069(0, 0, 1), class_2350.field_11043));
      }

      if (Module.mc.field_1687.method_8320(bp.method_10069(0, 0, -1)).method_51367() || awaiting.containsKey(bp.method_10069(0, 0, -1))) {
         list.add(new InteractionUtility.BlockPosWithFacing(bp.method_10069(0, 0, -1), class_2350.field_11035));
      }

      return list;
   }

   @Nullable
   public static InteractionUtility.BlockPosWithFacing checkNearBlocks(@NotNull class_2338 blockPos) {
      if (Module.mc.field_1687.method_8320(blockPos.method_10069(0, -1, 0)).method_51367()) {
         return new InteractionUtility.BlockPosWithFacing(blockPos.method_10069(0, -1, 0), class_2350.field_11036);
      } else if (Module.mc.field_1687.method_8320(blockPos.method_10069(-1, 0, 0)).method_51367()) {
         return new InteractionUtility.BlockPosWithFacing(blockPos.method_10069(-1, 0, 0), class_2350.field_11034);
      } else if (Module.mc.field_1687.method_8320(blockPos.method_10069(1, 0, 0)).method_51367()) {
         return new InteractionUtility.BlockPosWithFacing(blockPos.method_10069(1, 0, 0), class_2350.field_11039);
      } else if (Module.mc.field_1687.method_8320(blockPos.method_10069(0, 0, 1)).method_51367()) {
         return new InteractionUtility.BlockPosWithFacing(blockPos.method_10069(0, 0, 1), class_2350.field_11043);
      } else {
         return Module.mc.field_1687.method_8320(blockPos.method_10069(0, 0, -1)).method_51367() ? new InteractionUtility.BlockPosWithFacing(blockPos.method_10069(0, 0, -1), class_2350.field_11035) : null;
      }
   }

   public static float squaredDistanceFromEyes(@NotNull class_243 vec) {
      double d0 = vec.field_1352 - Module.mc.field_1724.method_23317();
      double d1 = vec.field_1350 - Module.mc.field_1724.method_23321();
      double d2 = vec.field_1351 - (Module.mc.field_1724.method_23318() + (double)Module.mc.field_1724.method_18381(Module.mc.field_1724.method_18376()));
      return (float)(d0 * d0 + d1 * d1 + d2 * d2);
   }

   public static float squaredDistanceFromEyes2d(@NotNull class_243 vec) {
      double d0 = vec.field_1352 - Module.mc.field_1724.method_23317();
      double d1 = vec.field_1350 - Module.mc.field_1724.method_23321();
      return (float)(d0 * d0 + d1 * d1);
   }

   @NotNull
   public static List<class_2350> getStrictDirections(@NotNull class_2338 bp) {
      List<class_2350> visibleSides = new ArrayList();
      class_243 positionVector = bp.method_46558();
      double westDelta = getEyesPos(Module.mc.field_1724).field_1352 - positionVector.method_1031(0.5D, 0.0D, 0.0D).field_1352;
      double eastDelta = getEyesPos(Module.mc.field_1724).field_1352 - positionVector.method_1031(-0.5D, 0.0D, 0.0D).field_1352;
      double northDelta = getEyesPos(Module.mc.field_1724).field_1350 - positionVector.method_1031(0.0D, 0.0D, 0.5D).field_1350;
      double southDelta = getEyesPos(Module.mc.field_1724).field_1350 - positionVector.method_1031(0.0D, 0.0D, -0.5D).field_1350;
      double upDelta = getEyesPos(Module.mc.field_1724).field_1351 - positionVector.method_1031(0.0D, 0.5D, 0.0D).field_1351;
      double downDelta = getEyesPos(Module.mc.field_1724).field_1351 - positionVector.method_1031(0.0D, -0.5D, 0.0D).field_1351;
      if (westDelta > 0.0D && isSolid(bp.method_10067())) {
         visibleSides.add(class_2350.field_11034);
      }

      if (westDelta < 0.0D && isSolid(bp.method_10078())) {
         visibleSides.add(class_2350.field_11039);
      }

      if (eastDelta < 0.0D && isSolid(bp.method_10078())) {
         visibleSides.add(class_2350.field_11039);
      }

      if (eastDelta > 0.0D && isSolid(bp.method_10067())) {
         visibleSides.add(class_2350.field_11034);
      }

      if (northDelta > 0.0D && isSolid(bp.method_10095())) {
         visibleSides.add(class_2350.field_11035);
      }

      if (northDelta < 0.0D && isSolid(bp.method_10072())) {
         visibleSides.add(class_2350.field_11043);
      }

      if (southDelta < 0.0D && isSolid(bp.method_10072())) {
         visibleSides.add(class_2350.field_11043);
      }

      if (southDelta > 0.0D && isSolid(bp.method_10095())) {
         visibleSides.add(class_2350.field_11035);
      }

      if (upDelta > 0.0D && isSolid(bp.method_10074())) {
         visibleSides.add(class_2350.field_11036);
      }

      if (upDelta < 0.0D && isSolid(bp.method_10084())) {
         visibleSides.add(class_2350.field_11033);
      }

      if (downDelta < 0.0D && isSolid(bp.method_10084())) {
         visibleSides.add(class_2350.field_11033);
      }

      if (downDelta > 0.0D && isSolid(bp.method_10074())) {
         visibleSides.add(class_2350.field_11036);
      }

      return visibleSides;
   }

   public static boolean isSolid(class_2338 bp) {
      return Module.mc.field_1687.method_8320(bp).method_51367() || awaiting.containsKey(bp);
   }

   @NotNull
   public static List<class_2350> getStrictBlockDirections(@NotNull class_2338 bp) {
      List<class_2350> visibleSides = new ArrayList();
      class_243 pV = bp.method_46558();
      double westDelta = getEyesPos(Module.mc.field_1724).field_1352 - pV.method_1031(0.5D, 0.0D, 0.0D).field_1352;
      double eastDelta = getEyesPos(Module.mc.field_1724).field_1352 - pV.method_1031(-0.5D, 0.0D, 0.0D).field_1352;
      double northDelta = getEyesPos(Module.mc.field_1724).field_1350 - pV.method_1031(0.0D, 0.0D, 0.5D).field_1350;
      double southDelta = getEyesPos(Module.mc.field_1724).field_1350 - pV.method_1031(0.0D, 0.0D, -0.5D).field_1350;
      double upDelta = getEyesPos(Module.mc.field_1724).field_1351 - pV.method_1031(0.0D, 0.5D, 0.0D).field_1351;
      double downDelta = getEyesPos(Module.mc.field_1724).field_1351 - pV.method_1031(0.0D, -0.5D, 0.0D).field_1351;
      if (westDelta > 0.0D && Module.mc.field_1687.method_8320(bp.method_10078()).method_45474()) {
         visibleSides.add(class_2350.field_11034);
      }

      if (eastDelta < 0.0D && Module.mc.field_1687.method_8320(bp.method_10067()).method_45474()) {
         visibleSides.add(class_2350.field_11039);
      }

      if (northDelta > 0.0D && Module.mc.field_1687.method_8320(bp.method_10072()).method_45474()) {
         visibleSides.add(class_2350.field_11035);
      }

      if (southDelta < 0.0D && Module.mc.field_1687.method_8320(bp.method_10095()).method_45474()) {
         visibleSides.add(class_2350.field_11043);
      }

      if (upDelta > 0.0D && Module.mc.field_1687.method_8320(bp.method_10084()).method_45474()) {
         visibleSides.add(class_2350.field_11036);
      }

      if (downDelta < 0.0D && Module.mc.field_1687.method_8320(bp.method_10074()).method_45474()) {
         visibleSides.add(class_2350.field_11033);
      }

      return visibleSides;
   }

   @Nullable
   public static InteractionUtility.BreakData getBreakData(class_2338 bp, InteractionUtility.Interact interact) {
      if (interact == InteractionUtility.Interact.Vanilla) {
         return new InteractionUtility.BreakData(class_2350.field_11036, bp.method_46558().method_1031(0.0D, 0.5D, 0.0D));
      } else {
         float bestDistance;
         if (interact == InteractionUtility.Interact.Strict) {
            bestDistance = 999.0F;
            class_2350 bestDirection = class_2350.field_11036;
            class_243 bestVector = null;
            class_2350[] var13 = class_2350.values();
            int var14 = var13.length;

            for(int var15 = 0; var15 < var14; ++var15) {
               class_2350 dir = var13[var15];
               class_243 directionVec = new class_243((double)bp.method_10263() + 0.5D + (double)dir.method_10163().method_10263() * 0.5D, (double)bp.method_10264() + 0.5D + (double)dir.method_10163().method_10264() * 0.5D, (double)bp.method_10260() + 0.5D + (double)dir.method_10163().method_10260() * 0.5D);
               float distance = squaredDistanceFromEyes(directionVec);
               if (bestDistance > distance) {
                  bestDirection = dir;
                  bestVector = directionVec;
                  bestDistance = distance;
               }
            }

            if (bestVector == null) {
               return null;
            } else {
               return new InteractionUtility.BreakData(bestDirection, bestVector);
            }
         } else if (interact != InteractionUtility.Interact.Legit) {
            return null;
         } else {
            bestDistance = 999.0F;
            InteractionUtility.BreakData bestData = null;

            for(float x = 0.0F; x <= 1.0F; x += 0.2F) {
               for(float y = 0.0F; y <= 1.0F; y += 0.2F) {
                  for(float z = 0.0F; z <= 1.0F; z += 0.2F) {
                     class_243 point = new class_243((double)((float)bp.method_10263() + x), (double)((float)bp.method_10264() + y), (double)((float)bp.method_10260() + z));
                     class_3965 wallCheck = Module.mc.field_1687.method_17742(new class_3959(getEyesPos(Module.mc.field_1724), point, class_3960.field_17558, class_242.field_1348, Module.mc.field_1724));
                     if (wallCheck == null || wallCheck.method_17783() != class_240.field_1332 || wallCheck.method_17777().equals(bp)) {
                        class_3965 result = ExplosionUtility.rayCastBlock(new class_3959(getEyesPos(Module.mc.field_1724), point, class_3960.field_17558, class_242.field_1348, Module.mc.field_1724), bp);
                        if (squaredDistanceFromEyes(point) < bestDistance && result != null && result.method_17783() == class_240.field_1332) {
                           bestData = new InteractionUtility.BreakData(result.method_17780(), result.method_17784());
                        }
                     }
                  }
               }
            }

            if (bestData == null) {
               return null;
            } else if (bestData.vector != null && bestData.dir != null) {
               return bestData;
            } else {
               return null;
            }
         }
      }
   }

   @Nullable
   public static class_243 getVisibleDirectionPoint(@NotNull class_2350 dir, @NotNull class_2338 bp, float wallRange, float range) {
      class_238 brutBox = getDirectionBox(dir);
      double x;
      double y;
      class_243 point;
      if (brutBox.field_1320 - brutBox.field_1323 == 0.0D) {
         for(x = brutBox.field_1322; x < brutBox.field_1325; x += 0.10000000149011612D) {
            for(y = brutBox.field_1321; y < brutBox.field_1324; y += 0.10000000149011612D) {
               point = new class_243((double)bp.method_10263() + brutBox.field_1323, (double)bp.method_10264() + x, (double)bp.method_10260() + y);
               if (!shouldSkipPoint(point, bp, dir, wallRange, range)) {
                  return point;
               }
            }
         }
      }

      if (brutBox.field_1325 - brutBox.field_1322 == 0.0D) {
         for(x = brutBox.field_1323; x < brutBox.field_1320; x += 0.10000000149011612D) {
            for(y = brutBox.field_1321; y < brutBox.field_1324; y += 0.10000000149011612D) {
               point = new class_243((double)bp.method_10263() + x, (double)bp.method_10264() + brutBox.field_1322, (double)bp.method_10260() + y);
               if (!shouldSkipPoint(point, bp, dir, wallRange, range)) {
                  return point;
               }
            }
         }
      }

      if (brutBox.field_1324 - brutBox.field_1321 == 0.0D) {
         for(x = brutBox.field_1323; x < brutBox.field_1320; x += 0.10000000149011612D) {
            for(y = brutBox.field_1322; y < brutBox.field_1325; y += 0.10000000149011612D) {
               point = new class_243((double)bp.method_10263() + x, (double)bp.method_10264() + y, (double)bp.method_10260() + brutBox.field_1321);
               if (!shouldSkipPoint(point, bp, dir, wallRange, range)) {
                  return point;
               }
            }
         }
      }

      return null;
   }

   @NotNull
   private static class_238 getDirectionBox(class_2350 dir) {
      class_238 var10000;
      switch(dir) {
      case field_11036:
         var10000 = new class_238(0.15000000596046448D, 1.0D, 0.15000000596046448D, 0.8500000238418579D, 1.0D, 0.8500000238418579D);
         break;
      case field_11033:
         var10000 = new class_238(0.15000000596046448D, 0.0D, 0.15000000596046448D, 0.8500000238418579D, 0.0D, 0.8500000238418579D);
         break;
      case field_11034:
         var10000 = new class_238(1.0D, 0.15000000596046448D, 0.15000000596046448D, 1.0D, 0.8500000238418579D, 0.8500000238418579D);
         break;
      case field_11039:
         var10000 = new class_238(0.0D, 0.15000000596046448D, 0.15000000596046448D, 0.0D, 0.8500000238418579D, 0.8500000238418579D);
         break;
      case field_11043:
         var10000 = new class_238(0.15000000596046448D, 0.15000000596046448D, 0.0D, 0.8500000238418579D, 0.8500000238418579D, 0.0D);
         break;
      case field_11035:
         var10000 = new class_238(0.15000000596046448D, 0.15000000596046448D, 1.0D, 0.8500000238418579D, 0.8500000238418579D, 1.0D);
         break;
      default:
         throw new MatchException((String)null, (Throwable)null);
      }

      return var10000;
   }

   private static boolean shouldSkipPoint(class_243 point, class_2338 bp, class_2350 dir, float wallRange, float range) {
      class_3959 context = new class_3959(getEyesPos(Module.mc.field_1724), point, class_3960.field_17558, class_242.field_1348, Module.mc.field_1724);
      class_3965 result = Module.mc.field_1687.method_17742(context);
      float dst = squaredDistanceFromEyes(point);
      if (result != null && result.method_17783() == class_240.field_1332 && !result.method_17777().equals(bp) && dst > wallRange * wallRange) {
         return true;
      } else {
         return dst > range * range;
      }
   }

   public static boolean needSneak(class_2248 in) {
      return SHIFT_BLOCKS.contains(in);
   }

   public static void lookAt(class_2338 bp) {
      if (bp != null) {
         float[] angle = calculateAngle(bp.method_46558());
         Module.mc.field_1724.method_36456(angle[0]);
         Module.mc.field_1724.method_36457(angle[1]);
      }

   }

   public static boolean isVecInFOV(class_243 pos, Integer fov) {
      double deltaX = pos.method_10216() - Module.mc.field_1724.method_23317();
      double deltaZ = pos.method_10215() - Module.mc.field_1724.method_23321();
      float yawDelta = class_3532.method_15393((float)class_3532.method_15338(Math.toDegrees(Math.atan2(deltaZ, deltaX)) - 90.0D) - class_3532.method_15393(Module.mc.field_1724.method_36454()));
      return Math.abs(yawDelta) <= (float)fov;
   }

   static {
      SHIFT_BLOCKS = Arrays.asList(class_2246.field_10443, class_2246.field_10034, class_2246.field_10380, class_2246.field_9980, class_2246.field_10486, class_2246.field_40285, class_2246.field_10246, class_2246.field_42740, class_2246.field_10535, class_2246.field_10333, class_2246.field_10312, class_2246.field_10228, class_2246.field_10200, class_2246.field_10608, class_2246.field_10485, class_2246.field_10199, class_2246.field_10407, class_2246.field_10063, class_2246.field_10203, class_2246.field_10600, class_2246.field_10275, class_2246.field_10051, class_2246.field_10140, class_2246.field_10532, class_2246.field_10268, class_2246.field_10605, class_2246.field_10373, class_2246.field_10055, class_2246.field_10068, class_2246.field_10371);
      awaiting = new HashMap();
   }

   public static enum Rotate {
      None,
      Default,
      Grim;

      // $FF: synthetic method
      private static InteractionUtility.Rotate[] $values() {
         return new InteractionUtility.Rotate[]{None, Default, Grim};
      }
   }

   public static enum Interact {
      Vanilla,
      Strict,
      Legit,
      AirPlace;

      // $FF: synthetic method
      private static InteractionUtility.Interact[] $values() {
         return new InteractionUtility.Interact[]{Vanilla, Strict, Legit, AirPlace};
      }
   }

   public static enum PlaceMode {
      Packet,
      Normal;

      // $FF: synthetic method
      private static InteractionUtility.PlaceMode[] $values() {
         return new InteractionUtility.PlaceMode[]{Packet, Normal};
      }
   }

   public static record BlockPosWithFacing(class_2338 position, class_2350 facing) {
      public BlockPosWithFacing(class_2338 position, class_2350 facing) {
         this.position = position;
         this.facing = facing;
      }

      public class_2338 position() {
         return this.position;
      }

      public class_2350 facing() {
         return this.facing;
      }
   }

   public static record BreakData(class_2350 dir, class_243 vector) {
      public BreakData(class_2350 dir, class_243 vector) {
         this.dir = dir;
         this.vector = vector;
      }

      public class_2350 dir() {
         return this.dir;
      }

      public class_243 vector() {
         return this.vector;
      }
   }
}
