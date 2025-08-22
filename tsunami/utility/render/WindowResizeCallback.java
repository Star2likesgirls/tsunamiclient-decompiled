package tsunami.utility.render;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.class_1041;
import net.minecraft.class_310;

public interface WindowResizeCallback {
   Event<WindowResizeCallback> EVENT = EventFactory.createArrayBacked(WindowResizeCallback.class, (callbacks) -> {
      return (client, window) -> {
         WindowResizeCallback[] var3 = callbacks;
         int var4 = callbacks.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            WindowResizeCallback callback = var3[var5];
            callback.onResized(client, window);
         }

      };
   });

   void onResized(class_310 var1, class_1041 var2);
}
