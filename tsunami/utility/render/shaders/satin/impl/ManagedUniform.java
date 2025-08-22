package tsunami.utility.render.shaders.satin.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.class_283;
import net.minecraft.class_284;
import net.minecraft.class_5944;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import tsunami.utility.render.shaders.satin.api.managed.uniform.Uniform1f;
import tsunami.utility.render.shaders.satin.api.managed.uniform.Uniform1i;
import tsunami.utility.render.shaders.satin.api.managed.uniform.Uniform2f;
import tsunami.utility.render.shaders.satin.api.managed.uniform.Uniform2i;
import tsunami.utility.render.shaders.satin.api.managed.uniform.Uniform3f;
import tsunami.utility.render.shaders.satin.api.managed.uniform.Uniform3i;
import tsunami.utility.render.shaders.satin.api.managed.uniform.Uniform4f;
import tsunami.utility.render.shaders.satin.api.managed.uniform.Uniform4i;
import tsunami.utility.render.shaders.satin.api.managed.uniform.UniformMat4;

public final class ManagedUniform extends ManagedUniformBase implements Uniform1i, Uniform2i, Uniform3i, Uniform4i, Uniform1f, Uniform2f, Uniform3f, Uniform4f, UniformMat4 {
   private static final class_284[] NO_TARGETS = new class_284[0];
   private final int count;
   private class_284[] targets;
   private int i0;
   private int i1;
   private int i2;
   private int i3;
   private float f0;
   private float f1;
   private float f2;
   private float f3;
   private boolean firstUpload;

   public ManagedUniform(String name, int count) {
      super(name);
      this.targets = NO_TARGETS;
      this.firstUpload = true;
      this.count = count;
   }

   public boolean findUniformTargets(List<class_283> shaders) {
      List<class_284> list = new ArrayList();
      Iterator var3 = shaders.iterator();

      while(var3.hasNext()) {
         class_283 shader = (class_283)var3.next();
         class_284 uniform = shader.method_1295().method_1271(this.name);
         if (uniform != null) {
            if (uniform.method_35661() != this.count) {
               int var10002 = this.count;
               throw new IllegalStateException("Mismatched number of values, expected " + var10002 + " but JSON definition declares " + uniform.method_35661());
            }

            list.add(uniform);
         }
      }

      if (list.size() > 0) {
         this.targets = (class_284[])list.toArray(new class_284[0]);
         this.syncCurrentValues();
         return true;
      } else {
         this.targets = NO_TARGETS;
         return false;
      }
   }

   public boolean findUniformTarget(class_5944 shader) {
      class_284 uniform = shader.method_34582(this.name);
      if (uniform != null) {
         this.targets = new class_284[]{uniform};
         this.syncCurrentValues();
         return true;
      } else {
         this.targets = NO_TARGETS;
         return false;
      }
   }

   private void syncCurrentValues() {
      if (!this.firstUpload) {
         class_284[] var1 = this.targets;
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            class_284 target = var1[var3];
            if (target.method_35663() != null) {
               target.method_1248(this.i0, this.i1, this.i2, this.i3);
            } else {
               assert target.method_35664() != null;

               target.method_1252(this.f0, this.f1, this.f2, this.f3);
            }
         }
      }

   }

   public void set(int value) {
      class_284[] targets = this.targets;
      int nbTargets = targets.length;
      if (nbTargets > 0 && (this.firstUpload || this.i0 != value)) {
         class_284[] var4 = targets;
         int var5 = targets.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            class_284 target = var4[var6];
            target.method_35649(value);
         }

         this.i0 = value;
         this.firstUpload = false;
      }

   }

   public void set(int value0, int value1) {
      class_284[] targets = this.targets;
      int nbTargets = targets.length;
      if (nbTargets > 0 && (this.firstUpload || this.i0 != value0 || this.i1 != value1)) {
         class_284[] var5 = targets;
         int var6 = targets.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            class_284 target = var5[var7];
            target.method_35650(value0, value1);
         }

         this.i0 = value0;
         this.i1 = value1;
         this.firstUpload = false;
      }

   }

   public void set(int value0, int value1, int value2) {
      class_284[] targets = this.targets;
      int nbTargets = targets.length;
      if (nbTargets > 0 && (this.firstUpload || this.i0 != value0 || this.i1 != value1 || this.i2 != value2)) {
         class_284[] var6 = targets;
         int var7 = targets.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            class_284 target = var6[var8];
            target.method_35651(value0, value1, value2);
         }

         this.i0 = value0;
         this.i1 = value1;
         this.i2 = value2;
         this.firstUpload = false;
      }

   }

   public void set(int value0, int value1, int value2, int value3) {
      class_284[] targets = this.targets;
      int nbTargets = targets.length;
      if (nbTargets > 0 && (this.firstUpload || this.i0 != value0 || this.i1 != value1 || this.i2 != value2 || this.i3 != value3)) {
         class_284[] var7 = targets;
         int var8 = targets.length;

         for(int var9 = 0; var9 < var8; ++var9) {
            class_284 target = var7[var9];
            target.method_35656(value0, value1, value2, value3);
         }

         this.i0 = value0;
         this.i1 = value1;
         this.i2 = value2;
         this.i3 = value3;
         this.firstUpload = false;
      }

   }

   public void set(float value) {
      class_284[] targets = this.targets;
      int nbTargets = targets.length;
      if (nbTargets > 0 && (this.firstUpload || this.f0 != value)) {
         class_284[] var4 = targets;
         int var5 = targets.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            class_284 target = var4[var6];
            target.method_1251(value);
         }

         this.f0 = value;
         this.firstUpload = false;
      }

   }

   public void set(float value0, float value1) {
      class_284[] targets = this.targets;
      int nbTargets = targets.length;
      if (nbTargets > 0 && (this.firstUpload || this.f0 != value0 || this.f1 != value1)) {
         class_284[] var5 = targets;
         int var6 = targets.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            class_284 target = var5[var7];
            target.method_1255(value0, value1);
         }

         this.f0 = value0;
         this.f1 = value1;
         this.firstUpload = false;
      }

   }

   public void set(Vector2f value) {
      this.set(value.x(), value.y());
   }

   public void set(float value0, float value1, float value2) {
      class_284[] targets = this.targets;
      int nbTargets = targets.length;
      if (nbTargets > 0 && (this.firstUpload || this.f0 != value0 || this.f1 != value1 || this.f2 != value2)) {
         class_284[] var6 = targets;
         int var7 = targets.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            class_284 target = var6[var8];
            target.method_1249(value0, value1, value2);
         }

         this.f0 = value0;
         this.f1 = value1;
         this.f2 = value2;
         this.firstUpload = false;
      }

   }

   public void set(Vector3f value) {
      this.set(value.x(), value.y(), value.z());
   }

   public void set(float value0, float value1, float value2, float value3) {
      class_284[] targets = this.targets;
      int nbTargets = targets.length;
      if (nbTargets > 0 && (this.firstUpload || this.f0 != value0 || this.f1 != value1 || this.f2 != value2 || this.f3 != value3)) {
         class_284[] var7 = targets;
         int var8 = targets.length;

         for(int var9 = 0; var9 < var8; ++var9) {
            class_284 target = var7[var9];
            target.method_35657(value0, value1, value2, value3);
         }

         this.f0 = value0;
         this.f1 = value1;
         this.f2 = value2;
         this.f3 = value3;
         this.firstUpload = false;
      }

   }

   public void set(Vector4f value) {
      this.set(value.x(), value.y(), value.z(), value.w());
   }

   public void set(Matrix4f value) {
      class_284[] targets = this.targets;
      int nbTargets = targets.length;
      if (nbTargets > 0) {
         class_284[] var4 = targets;
         int var5 = targets.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            class_284 target = var4[var6];
            target.method_1250(value);
         }
      }

   }

   public void setFromArray(float[] values) {
      if (this.count != values.length) {
         throw new IllegalArgumentException("Mismatched values size, expected " + this.count + " but got " + values.length);
      } else {
         class_284[] targets = this.targets;
         int nbTargets = targets.length;
         if (nbTargets > 0) {
            class_284[] var4 = targets;
            int var5 = targets.length;

            for(int var6 = 0; var6 < var5; ++var6) {
               class_284 target = var4[var6];
               target.method_1253(values);
            }
         }

      }
   }
}
