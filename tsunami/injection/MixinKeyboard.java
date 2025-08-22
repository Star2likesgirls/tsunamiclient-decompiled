package tsunami.injection;

import net.minecraft.class_309;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tsunami.TsunamiClient;
import tsunami.core.Managers;
import tsunami.events.impl.EventKeyPress;
import tsunami.events.impl.EventKeyRelease;
import tsunami.features.modules.Module;
import tsunami.gui.clickui.ClickGUI;
import tsunami.gui.hud.HudEditorGui;

@Mixin({class_309.class})
public class MixinKeyboard {
   @Inject(
      method = {"onKey"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onKey(long windowPointer, int key, int scanCode, int action, int modifiers, CallbackInfo ci) {
      if (!Module.fullNullCheck()) {
         boolean whitelist = Module.mc.field_1755 == null || Module.mc.field_1755 instanceof ClickGUI || Module.mc.field_1755 instanceof HudEditorGui;
         if (whitelist) {
            if (action == 0) {
               Managers.MODULE.onKeyReleased(key);
            }

            if (action == 1) {
               Managers.MODULE.onKeyPressed(key);
            }

            if (action == 2) {
               action = 1;
            }

            switch(action) {
            case 0:
               EventKeyRelease event = new EventKeyRelease(key, scanCode);
               TsunamiClient.EVENT_BUS.post((Object)event);
               if (event.isCancelled()) {
                  ci.cancel();
               }
               break;
            case 1:
               EventKeyPress event = new EventKeyPress(key, scanCode);
               TsunamiClient.EVENT_BUS.post((Object)event);
               if (event.isCancelled()) {
                  ci.cancel();
               }
            }

         }
      }
   }
}
