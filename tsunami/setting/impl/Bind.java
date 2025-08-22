package tsunami.setting.impl;

import java.lang.reflect.Field;
import org.lwjgl.glfw.GLFW;

public class Bind {
   private final int key;
   private boolean hold;
   private final boolean mouse;

   public Bind(int key, boolean mouse, boolean hold) {
      this.key = key;
      this.mouse = mouse;
      this.hold = hold;
   }

   public int getKey() {
      return this.key;
   }

   public String getBind() {
      if (this.mouse) {
         return "M" + this.key;
      } else {
         String kn = this.key > 0 ? GLFW.glfwGetKeyName(this.key, 0) : "None";
         if (kn == null) {
            try {
               Field[] var2 = GLFW.class.getDeclaredFields();
               int var3 = var2.length;

               for(int var4 = 0; var4 < var3; ++var4) {
                  Field declaredField = var2[var4];
                  if (declaredField.getName().startsWith("GLFW_KEY_")) {
                     int a = (Integer)declaredField.get((Object)null);
                     if (a == this.key) {
                        String nb = declaredField.getName().substring("GLFW_KEY_".length());
                        String var10000 = nb.substring(0, 1).toUpperCase();
                        kn = var10000 + nb.substring(1).toLowerCase();
                     }
                  }
               }
            } catch (Exception var8) {
               kn = "unknown." + this.key;
            }
         }

         return this.key == -1 ? "None" : kn.makeConcatWithConstants<invokedynamic>(kn).toUpperCase();
      }
   }

   public boolean isHold() {
      return this.hold;
   }

   public boolean isMouse() {
      return this.mouse;
   }

   public void setHold(boolean hold) {
      this.hold = hold;
   }
}
