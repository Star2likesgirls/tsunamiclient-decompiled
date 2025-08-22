package tsunami.utility.render.shaders.satin.api.managed;

import net.minecraft.class_276;

public interface ManagedFramebuffer {
   class_276 getFramebuffer();

   void beginWrite(boolean var1);

   void draw();

   void draw(int var1, int var2, boolean var3);

   void clear();

   void clear(boolean var1);
}
