package tsunami.utility.world;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2338;
import net.minecraft.class_2382;
import net.minecraft.class_243;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tsunami.features.modules.Module;

public final class HoleUtility {
   public static final class_2382[] VECTOR_PATTERN = new class_2382[]{new class_2382(0, 0, 1), new class_2382(0, 0, -1), new class_2382(1, 0, 0), new class_2382(-1, 0, 0)};

   @NotNull
   public static List<class_2338> getHolePoses(@NotNull class_243 from) {
      List<class_2338> positions = new ArrayList();
      double decimalX = from.method_10216() - Math.floor(from.method_10216());
      double decimalZ = from.method_10215() - Math.floor(from.method_10215());
      int offX = calcOffset(decimalX);
      int offZ = calcOffset(decimalZ);
      positions.add(getPos(from));

      for(int x = 0; x <= Math.abs(offX); ++x) {
         for(int z = 0; z <= Math.abs(offZ); ++z) {
            int properX = x * offX;
            int properZ = z * offZ;
            positions.add(((class_2338)Objects.requireNonNull(getPos(from))).method_10069(properX, 0, properZ));
         }
      }

      return positions;
   }

   @NotNull
   public static List<class_2338> getSurroundPoses(@NotNull class_243 from) {
      class_2338 fromPos = class_2338.method_49638(from);
      ArrayList<class_2338> tempOffsets = new ArrayList();
      double decimalX = Math.abs(from.method_10216()) - Math.floor(Math.abs(from.method_10216()));
      double decimalZ = Math.abs(from.method_10215()) - Math.floor(Math.abs(from.method_10215()));
      int lengthXPos = calcLength(decimalX, false);
      int lengthXNeg = calcLength(decimalX, true);
      int lengthZPos = calcLength(decimalZ, false);
      int lengthZNeg = calcLength(decimalZ, true);

      int z;
      for(z = 1; z < lengthXPos + 1; ++z) {
         tempOffsets.add(addToPlayer(fromPos, (double)z, 0.0D, (double)(1 + lengthZPos)));
         tempOffsets.add(addToPlayer(fromPos, (double)z, 0.0D, (double)(-(1 + lengthZNeg))));
      }

      for(z = 0; z <= lengthXNeg; ++z) {
         tempOffsets.add(addToPlayer(fromPos, (double)(-z), 0.0D, (double)(1 + lengthZPos)));
         tempOffsets.add(addToPlayer(fromPos, (double)(-z), 0.0D, (double)(-(1 + lengthZNeg))));
      }

      for(z = 1; z < lengthZPos + 1; ++z) {
         tempOffsets.add(addToPlayer(fromPos, (double)(1 + lengthXPos), 0.0D, (double)z));
         tempOffsets.add(addToPlayer(fromPos, (double)(-(1 + lengthXNeg)), 0.0D, (double)z));
      }

      for(z = 0; z <= lengthZNeg; ++z) {
         tempOffsets.add(addToPlayer(fromPos, (double)(1 + lengthXPos), 0.0D, (double)(-z)));
         tempOffsets.add(addToPlayer(fromPos, (double)(-(1 + lengthXNeg)), 0.0D, (double)(-z)));
      }

      return tempOffsets;
   }

   @NotNull
   private static class_2338 getPos(@NotNull class_243 from) {
      return class_2338.method_49637(from.method_10216(), from.method_10214() - Math.floor(from.method_10214()) > 0.8D ? Math.floor(from.method_10214()) + 1.0D : Math.floor(from.method_10214()), from.method_10215());
   }

   public static int calcOffset(double dec) {
      return dec >= 0.7D ? 1 : (dec <= 0.3D ? -1 : 0);
   }

   public static int calcLength(double decimal, boolean negative) {
      if (negative) {
         return decimal <= 0.3D ? 1 : 0;
      } else {
         return decimal >= 0.7D ? 1 : 0;
      }
   }

   public static class_2338 addToPlayer(@NotNull class_2338 playerPos, double x, double y, double z) {
      if (playerPos.method_10263() < 0) {
         x = -x;
      }

      if (playerPos.method_10264() < 0) {
         y = -y;
      }

      if (playerPos.method_10260() < 0) {
         z = -z;
      }

      return playerPos.method_10081(class_2338.method_49637(x, y, z));
   }

   public static boolean isHole(class_2338 pos) {
      return isSingleHole(pos) || validTwoBlockIndestructible(pos) || validTwoBlockBedrock(pos) || validQuadIndestructible(pos) || validQuadBedrock(pos);
   }

   public static boolean isSingleHole(class_2338 pos) {
      return validIndestructible(pos) || validBedrock(pos);
   }

   public static boolean validIndestructible(@NotNull class_2338 pos) {
      return !validBedrock(pos) && (isIndestructible(pos.method_10069(0, -1, 0)) || isBedrock(pos.method_10069(0, -1, 0))) && (isIndestructible(pos.method_10069(1, 0, 0)) || isBedrock(pos.method_10069(1, 0, 0))) && (isIndestructible(pos.method_10069(-1, 0, 0)) || isBedrock(pos.method_10069(-1, 0, 0))) && (isIndestructible(pos.method_10069(0, 0, 1)) || isBedrock(pos.method_10069(0, 0, 1))) && (isIndestructible(pos.method_10069(0, 0, -1)) || isBedrock(pos.method_10069(0, 0, -1))) && isReplaceable(pos) && isReplaceable(pos.method_10069(0, 1, 0)) && isReplaceable(pos.method_10069(0, 2, 0));
   }

   public static boolean validBedrock(@NotNull class_2338 pos) {
      return isBedrock(pos.method_10069(0, -1, 0)) && isBedrock(pos.method_10069(1, 0, 0)) && isBedrock(pos.method_10069(-1, 0, 0)) && isBedrock(pos.method_10069(0, 0, 1)) && isBedrock(pos.method_10069(0, 0, -1)) && isReplaceable(pos) && isReplaceable(pos.method_10069(0, 1, 0)) && isReplaceable(pos.method_10069(0, 2, 0));
   }

   public static boolean validTwoBlockBedrock(@NotNull class_2338 pos) {
      if (!isReplaceable(pos)) {
         return false;
      } else {
         class_2382 addVec = getTwoBlocksDirection(pos);
         if (addVec == null) {
            return false;
         } else {
            class_2338[] checkPoses = new class_2338[]{pos, pos.method_10081(addVec)};
            class_2338[] var3 = checkPoses;
            int var4 = checkPoses.length;

            for(int var5 = 0; var5 < var4; ++var5) {
               class_2338 checkPos = var3[var5];
               if (!isReplaceable(checkPos.method_10069(0, 1, 0)) || !isReplaceable(checkPos.method_10069(0, 2, 0))) {
                  return false;
               }

               class_2338 downPos = checkPos.method_10074();
               if (!isBedrock(downPos)) {
                  return false;
               }

               class_2382[] var8 = VECTOR_PATTERN;
               int var9 = var8.length;

               for(int var10 = 0; var10 < var9; ++var10) {
                  class_2382 vec = var8[var10];
                  class_2338 reducedPos = checkPos.method_10081(vec);
                  if (!isBedrock(reducedPos) && !reducedPos.equals(pos) && !reducedPos.equals(pos.method_10081(addVec))) {
                     return false;
                  }
               }
            }

            return true;
         }
      }
   }

   public static boolean validTwoBlockIndestructible(@NotNull class_2338 pos) {
      if (!isReplaceable(pos)) {
         return false;
      } else {
         class_2382 addVec = getTwoBlocksDirection(pos);
         if (addVec == null) {
            return false;
         } else {
            class_2338[] checkPoses = new class_2338[]{pos, pos.method_10081(addVec)};
            boolean wasIndestrictible = false;
            class_2338[] var4 = checkPoses;
            int var5 = checkPoses.length;

            for(int var6 = 0; var6 < var5; ++var6) {
               class_2338 checkPos = var4[var6];
               class_2338 downPos = checkPos.method_10074();
               if (isIndestructible(downPos)) {
                  wasIndestrictible = true;
               } else if (!isBedrock(downPos)) {
                  return false;
               }

               if (!isReplaceable(checkPos.method_10069(0, 1, 0)) || !isReplaceable(checkPos.method_10069(0, 2, 0))) {
                  return false;
               }

               class_2382[] var9 = VECTOR_PATTERN;
               int var10 = var9.length;

               for(int var11 = 0; var11 < var10; ++var11) {
                  class_2382 vec = var9[var11];
                  class_2338 reducedPos = checkPos.method_10081(vec);
                  if (isIndestructible(reducedPos)) {
                     wasIndestrictible = true;
                  } else if (!isBedrock(reducedPos) && !reducedPos.equals(pos) && !reducedPos.equals(pos.method_10081(addVec))) {
                     return false;
                  }
               }
            }

            return wasIndestrictible;
         }
      }
   }

   @Nullable
   private static class_2382 getTwoBlocksDirection(class_2338 pos) {
      class_2382[] var1 = VECTOR_PATTERN;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         class_2382 vec = var1[var3];
         if (isReplaceable(pos.method_10081(vec))) {
            return vec;
         }
      }

      return null;
   }

   public static boolean validQuadIndestructible(@NotNull class_2338 pos) {
      List<class_2338> checkPoses = getQuadDirection(pos);
      if (checkPoses == null) {
         return false;
      } else {
         boolean wasIndestrictible = false;
         Iterator var3 = checkPoses.iterator();

         while(var3.hasNext()) {
            class_2338 checkPos = (class_2338)var3.next();
            class_2338 downPos = checkPos.method_10074();
            if (isIndestructible(downPos)) {
               wasIndestrictible = true;
            } else if (!isBedrock(downPos)) {
               return false;
            }

            if (!isReplaceable(checkPos.method_10069(0, 1, 0)) || !isReplaceable(checkPos.method_10069(0, 2, 0))) {
               return false;
            }

            class_2382[] var6 = VECTOR_PATTERN;
            int var7 = var6.length;

            for(int var8 = 0; var8 < var7; ++var8) {
               class_2382 vec = var6[var8];
               class_2338 reducedPos = checkPos.method_10081(vec);
               if (isIndestructible(reducedPos)) {
                  wasIndestrictible = true;
               } else if (!isBedrock(reducedPos) && !checkPoses.contains(reducedPos)) {
                  return false;
               }
            }
         }

         return wasIndestrictible;
      }
   }

   public static boolean validQuadBedrock(@NotNull class_2338 pos) {
      List<class_2338> checkPoses = getQuadDirection(pos);
      if (checkPoses == null) {
         return false;
      } else {
         Iterator var2 = checkPoses.iterator();

         while(var2.hasNext()) {
            class_2338 checkPos = (class_2338)var2.next();
            class_2338 downPos = checkPos.method_10074();
            if (!isBedrock(downPos)) {
               return false;
            }

            if (!isReplaceable(checkPos.method_10069(0, 1, 0)) || !isReplaceable(checkPos.method_10069(0, 2, 0))) {
               return false;
            }

            class_2382[] var5 = VECTOR_PATTERN;
            int var6 = var5.length;

            for(int var7 = 0; var7 < var6; ++var7) {
               class_2382 vec = var5[var7];
               class_2338 reducedPos = checkPos.method_10081(vec);
               if (!isBedrock(reducedPos) && !checkPoses.contains(reducedPos)) {
                  return false;
               }
            }
         }

         return true;
      }
   }

   @Nullable
   private static List<class_2338> getQuadDirection(@NotNull class_2338 pos) {
      List<class_2338> dirList = new ArrayList();
      dirList.add(pos);
      if (!isReplaceable(pos)) {
         return null;
      } else {
         if (isReplaceable(pos.method_10069(1, 0, 0)) && isReplaceable(pos.method_10069(0, 0, 1)) && isReplaceable(pos.method_10069(1, 0, 1))) {
            dirList.add(pos.method_10069(1, 0, 0));
            dirList.add(pos.method_10069(0, 0, 1));
            dirList.add(pos.method_10069(1, 0, 1));
         }

         if (isReplaceable(pos.method_10069(-1, 0, 0)) && isReplaceable(pos.method_10069(0, 0, -1)) && isReplaceable(pos.method_10069(-1, 0, -1))) {
            dirList.add(pos.method_10069(-1, 0, 0));
            dirList.add(pos.method_10069(0, 0, -1));
            dirList.add(pos.method_10069(-1, 0, -1));
         }

         if (isReplaceable(pos.method_10069(1, 0, 0)) && isReplaceable(pos.method_10069(0, 0, -1)) && isReplaceable(pos.method_10069(1, 0, -1))) {
            dirList.add(pos.method_10069(1, 0, 0));
            dirList.add(pos.method_10069(0, 0, -1));
            dirList.add(pos.method_10069(1, 0, -1));
         }

         if (isReplaceable(pos.method_10069(-1, 0, 0)) && isReplaceable(pos.method_10069(0, 0, 1)) && isReplaceable(pos.method_10069(-1, 0, 1))) {
            dirList.add(pos.method_10069(-1, 0, 0));
            dirList.add(pos.method_10069(0, 0, 1));
            dirList.add(pos.method_10069(-1, 0, 1));
         }

         return dirList.size() != 4 ? null : dirList;
      }
   }

   private static boolean isIndestructible(class_2338 bp) {
      if (Module.mc.field_1687 == null) {
         return false;
      } else {
         class_2248 block = Module.mc.field_1687.method_8320(bp).method_26204();
         return block == class_2246.field_10540 || block == class_2246.field_22108 || block == class_2246.field_22423 || block == class_2246.field_23152;
      }
   }

   private static boolean isBedrock(class_2338 bp) {
      if (Module.mc.field_1687 == null) {
         return false;
      } else {
         return Module.mc.field_1687.method_8320(bp).method_26204() == class_2246.field_9987;
      }
   }

   private static boolean isReplaceable(class_2338 bp) {
      return Module.mc.field_1687 == null ? false : Module.mc.field_1687.method_8320(bp).method_45474();
   }
}
