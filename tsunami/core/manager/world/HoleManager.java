package tsunami.core.manager.world;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_2382;
import net.minecraft.class_243;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tsunami.core.manager.IManager;

public class HoleManager implements IManager {
   public static final class_2382[] VECTOR_PATTERN = new class_2382[]{new class_2382(0, 0, 1), new class_2382(0, 0, -1), new class_2382(1, 0, 0), new class_2382(-1, 0, 0)};

   @NotNull
   public List<class_2338> getHolePoses(@NotNull class_243 from) {
      List<class_2338> positions = new ArrayList();
      double decimalX = from.method_10216() - Math.floor(from.method_10216());
      double decimalZ = from.method_10215() - Math.floor(from.method_10215());
      int offX = this.calcOffset(decimalX);
      int offZ = this.calcOffset(decimalZ);
      positions.add(this.getPos(from));

      for(int x = 0; x <= Math.abs(offX); ++x) {
         for(int z = 0; z <= Math.abs(offZ); ++z) {
            int properX = x * offX;
            int properZ = z * offZ;
            positions.add(((class_2338)Objects.requireNonNull(this.getPos(from))).method_10069(properX, 0, properZ));
         }
      }

      return positions;
   }

   @NotNull
   public List<class_2338> getSurroundPoses(@NotNull class_243 from) {
      class_2338 fromPos = class_2338.method_49638(from);
      ArrayList<class_2338> tempOffsets = new ArrayList();
      double decimalX = Math.abs(from.method_10216()) - Math.floor(Math.abs(from.method_10216()));
      double decimalZ = Math.abs(from.method_10215()) - Math.floor(Math.abs(from.method_10215()));
      int lengthXPos = this.calcLength(decimalX, false);
      int lengthXNeg = this.calcLength(decimalX, true);
      int lengthZPos = this.calcLength(decimalZ, false);
      int lengthZNeg = this.calcLength(decimalZ, true);

      int z;
      for(z = 1; z < lengthXPos + 1; ++z) {
         tempOffsets.add(this.addToPlayer(fromPos, (double)z, 0.0D, (double)(1 + lengthZPos)));
         tempOffsets.add(this.addToPlayer(fromPos, (double)z, 0.0D, (double)(-(1 + lengthZNeg))));
      }

      for(z = 0; z <= lengthXNeg; ++z) {
         tempOffsets.add(this.addToPlayer(fromPos, (double)(-z), 0.0D, (double)(1 + lengthZPos)));
         tempOffsets.add(this.addToPlayer(fromPos, (double)(-z), 0.0D, (double)(-(1 + lengthZNeg))));
      }

      for(z = 1; z < lengthZPos + 1; ++z) {
         tempOffsets.add(this.addToPlayer(fromPos, (double)(1 + lengthXPos), 0.0D, (double)z));
         tempOffsets.add(this.addToPlayer(fromPos, (double)(-(1 + lengthXNeg)), 0.0D, (double)z));
      }

      for(z = 0; z <= lengthZNeg; ++z) {
         tempOffsets.add(this.addToPlayer(fromPos, (double)(1 + lengthXPos), 0.0D, (double)(-z)));
         tempOffsets.add(this.addToPlayer(fromPos, (double)(-(1 + lengthXNeg)), 0.0D, (double)(-z)));
      }

      return tempOffsets;
   }

   @NotNull
   private class_2338 getPos(@NotNull class_243 from) {
      return class_2338.method_49637(from.method_10216(), from.method_10214() - Math.floor(from.method_10214()) > 0.8D ? Math.floor(from.method_10214()) + 1.0D : Math.floor(from.method_10214()), from.method_10215());
   }

   public int calcOffset(double dec) {
      return dec >= 0.7D ? 1 : (dec <= 0.3D ? -1 : 0);
   }

   public int calcLength(double decimal, boolean negative) {
      if (negative) {
         return decimal <= 0.3D ? 1 : 0;
      } else {
         return decimal >= 0.7D ? 1 : 0;
      }
   }

   public class_2338 addToPlayer(@NotNull class_2338 playerPos, double x, double y, double z) {
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

   public boolean isHole(class_2338 pos) {
      return this.isSingleHole(pos) || this.validTwoBlockIndestructible(pos) || this.validTwoBlockBedrock(pos) || this.validQuadIndestructible(pos) || this.validQuadBedrock(pos);
   }

   public boolean isSingleHole(class_2338 pos) {
      return this.validIndestructible(pos) || this.validBedrock(pos);
   }

   public boolean validIndestructible(@NotNull class_2338 pos) {
      return !this.validBedrock(pos) && (this.isIndestructible(pos.method_10069(0, -1, 0)) || this.isBedrock(pos.method_10069(0, -1, 0))) && (this.isIndestructible(pos.method_10069(1, 0, 0)) || this.isBedrock(pos.method_10069(1, 0, 0))) && (this.isIndestructible(pos.method_10069(-1, 0, 0)) || this.isBedrock(pos.method_10069(-1, 0, 0))) && (this.isIndestructible(pos.method_10069(0, 0, 1)) || this.isBedrock(pos.method_10069(0, 0, 1))) && (this.isIndestructible(pos.method_10069(0, 0, -1)) || this.isBedrock(pos.method_10069(0, 0, -1))) && this.isReplaceable(pos) && this.isReplaceable(pos.method_10069(0, 1, 0)) && this.isReplaceable(pos.method_10069(0, 2, 0));
   }

   public boolean validBedrock(@NotNull class_2338 pos) {
      return this.isBedrock(pos.method_10069(0, -1, 0)) && this.isBedrock(pos.method_10069(1, 0, 0)) && this.isBedrock(pos.method_10069(-1, 0, 0)) && this.isBedrock(pos.method_10069(0, 0, 1)) && this.isBedrock(pos.method_10069(0, 0, -1)) && this.isReplaceable(pos) && this.isReplaceable(pos.method_10069(0, 1, 0)) && this.isReplaceable(pos.method_10069(0, 2, 0));
   }

   public boolean validTwoBlockBedrock(@NotNull class_2338 pos) {
      if (!this.isReplaceable(pos)) {
         return false;
      } else {
         class_2382 addVec = this.getTwoBlocksDirection(pos);
         if (addVec == null) {
            return false;
         } else {
            class_2338[] checkPoses = new class_2338[]{pos, pos.method_10081(addVec)};
            class_2338[] var4 = checkPoses;
            int var5 = checkPoses.length;

            for(int var6 = 0; var6 < var5; ++var6) {
               class_2338 checkPos = var4[var6];
               class_2338 downPos = checkPos.method_10074();
               if (!this.isBedrock(downPos)) {
                  return false;
               }

               class_2382[] var9 = VECTOR_PATTERN;
               int var10 = var9.length;

               for(int var11 = 0; var11 < var10; ++var11) {
                  class_2382 vec = var9[var11];
                  class_2338 reducedPos = checkPos.method_10081(vec);
                  if (!this.isBedrock(reducedPos) && !reducedPos.equals(pos) && !reducedPos.equals(pos.method_10081(addVec))) {
                     return false;
                  }
               }
            }

            return true;
         }
      }
   }

   public boolean validTwoBlockIndestructible(@NotNull class_2338 pos) {
      if (!this.isReplaceable(pos)) {
         return false;
      } else {
         class_2382 addVec = this.getTwoBlocksDirection(pos);
         if (addVec == null) {
            return false;
         } else {
            class_2338[] checkPoses = new class_2338[]{pos, pos.method_10081(addVec)};
            boolean wasIndestrictible = false;
            class_2338[] var5 = checkPoses;
            int var6 = checkPoses.length;

            for(int var7 = 0; var7 < var6; ++var7) {
               class_2338 checkPos = var5[var7];
               class_2338 downPos = checkPos.method_10074();
               if (this.isIndestructible(downPos)) {
                  wasIndestrictible = true;
               } else if (!this.isBedrock(downPos)) {
                  return false;
               }

               class_2382[] var10 = VECTOR_PATTERN;
               int var11 = var10.length;

               for(int var12 = 0; var12 < var11; ++var12) {
                  class_2382 vec = var10[var12];
                  class_2338 reducedPos = checkPos.method_10081(vec);
                  if (this.isIndestructible(reducedPos)) {
                     wasIndestrictible = true;
                  } else if (!this.isBedrock(reducedPos) && !reducedPos.equals(pos) && !reducedPos.equals(pos.method_10081(addVec))) {
                     return false;
                  }
               }
            }

            return wasIndestrictible;
         }
      }
   }

   @Nullable
   private class_2382 getTwoBlocksDirection(class_2338 pos) {
      class_2382[] var2 = VECTOR_PATTERN;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         class_2382 vec = var2[var4];
         if (this.isReplaceable(pos.method_10081(vec))) {
            return vec;
         }
      }

      return null;
   }

   public boolean validQuadIndestructible(@NotNull class_2338 pos) {
      List<class_2338> checkPoses = this.getQuadDirection(pos);
      if (checkPoses == null) {
         return false;
      } else {
         boolean wasIndestrictible = false;
         Iterator var4 = checkPoses.iterator();

         while(var4.hasNext()) {
            class_2338 checkPos = (class_2338)var4.next();
            class_2338 downPos = checkPos.method_10074();
            if (this.isIndestructible(downPos)) {
               wasIndestrictible = true;
            } else if (!this.isBedrock(downPos)) {
               return false;
            }

            class_2382[] var7 = VECTOR_PATTERN;
            int var8 = var7.length;

            for(int var9 = 0; var9 < var8; ++var9) {
               class_2382 vec = var7[var9];
               class_2338 reducedPos = checkPos.method_10081(vec);
               if (this.isIndestructible(reducedPos)) {
                  wasIndestrictible = true;
               } else if (!this.isBedrock(reducedPos) && !checkPoses.contains(reducedPos)) {
                  return false;
               }
            }
         }

         return wasIndestrictible;
      }
   }

   public boolean validQuadBedrock(@NotNull class_2338 pos) {
      List<class_2338> checkPoses = this.getQuadDirection(pos);
      if (checkPoses == null) {
         return false;
      } else {
         Iterator var3 = checkPoses.iterator();

         while(var3.hasNext()) {
            class_2338 checkPos = (class_2338)var3.next();
            class_2338 downPos = checkPos.method_10074();
            if (!this.isBedrock(downPos)) {
               return false;
            }

            class_2382[] var6 = VECTOR_PATTERN;
            int var7 = var6.length;

            for(int var8 = 0; var8 < var7; ++var8) {
               class_2382 vec = var6[var8];
               class_2338 reducedPos = checkPos.method_10081(vec);
               if (!this.isBedrock(reducedPos) && !checkPoses.contains(reducedPos)) {
                  return false;
               }
            }
         }

         return true;
      }
   }

   @Nullable
   private List<class_2338> getQuadDirection(@NotNull class_2338 pos) {
      List<class_2338> dirList = new ArrayList();
      dirList.add(pos);
      if (!this.isReplaceable(pos)) {
         return null;
      } else {
         if (this.isReplaceable(pos.method_10069(1, 0, 0)) && this.isReplaceable(pos.method_10069(0, 0, 1)) && this.isReplaceable(pos.method_10069(1, 0, 1))) {
            dirList.add(pos.method_10069(1, 0, 0));
            dirList.add(pos.method_10069(0, 0, 1));
            dirList.add(pos.method_10069(1, 0, 1));
         }

         if (this.isReplaceable(pos.method_10069(-1, 0, 0)) && this.isReplaceable(pos.method_10069(0, 0, -1)) && this.isReplaceable(pos.method_10069(-1, 0, -1))) {
            dirList.add(pos.method_10069(-1, 0, 0));
            dirList.add(pos.method_10069(0, 0, -1));
            dirList.add(pos.method_10069(-1, 0, -1));
         }

         if (this.isReplaceable(pos.method_10069(1, 0, 0)) && this.isReplaceable(pos.method_10069(0, 0, -1)) && this.isReplaceable(pos.method_10069(1, 0, -1))) {
            dirList.add(pos.method_10069(1, 0, 0));
            dirList.add(pos.method_10069(0, 0, -1));
            dirList.add(pos.method_10069(1, 0, -1));
         }

         if (this.isReplaceable(pos.method_10069(-1, 0, 0)) && this.isReplaceable(pos.method_10069(0, 0, 1)) && this.isReplaceable(pos.method_10069(-1, 0, 1))) {
            dirList.add(pos.method_10069(-1, 0, 0));
            dirList.add(pos.method_10069(0, 0, 1));
            dirList.add(pos.method_10069(-1, 0, 1));
         }

         return dirList.size() != 4 ? null : dirList;
      }
   }

   private boolean isIndestructible(class_2338 bp) {
      if (mc.field_1687 == null) {
         return false;
      } else {
         return mc.field_1687.method_8320(bp).method_26204() == class_2246.field_10540 || mc.field_1687.method_8320(bp).method_26204() == class_2246.field_22108 || mc.field_1687.method_8320(bp).method_26204() == class_2246.field_22423 || mc.field_1687.method_8320(bp).method_26204() == class_2246.field_23152;
      }
   }

   private boolean isBedrock(class_2338 bp) {
      if (mc.field_1687 == null) {
         return false;
      } else {
         return mc.field_1687.method_8320(bp).method_26204() == class_2246.field_9987;
      }
   }

   private boolean isReplaceable(class_2338 bp) {
      return mc.field_1687 == null ? false : mc.field_1687.method_8320(bp).method_45474();
   }
}
