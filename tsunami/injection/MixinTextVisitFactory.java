package tsunami.injection;

import net.minecraft.class_5223;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import tsunami.core.manager.client.ModuleManager;
import tsunami.core.manager.player.FriendManager;
import tsunami.features.modules.Module;
import tsunami.features.modules.misc.NameProtect;

@Mixin({class_5223.class})
public class MixinTextVisitFactory {
   @ModifyArg(
      at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/text/TextVisitFactory;visitFormatted(Ljava/lang/String;ILnet/minecraft/text/Style;Lnet/minecraft/text/Style;Lnet/minecraft/text/CharacterVisitor;)Z",
   ordinal = 0
),
      method = {"visitFormatted(Ljava/lang/String;ILnet/minecraft/text/Style;Lnet/minecraft/text/CharacterVisitor;)Z"},
      index = 0
   )
   private static String adjustText(String text) {
      return protect(text);
   }

   private static String protect(String string) {
      if (ModuleManager.nameProtect.isEnabled() && Module.mc.field_1724 != null) {
         String me = Module.mc.method_1548().method_1676();
         return !string.contains(me) && (!FriendManager.friends.stream().anyMatch((i) -> {
            return i.contains(string);
         }) || !(Boolean)NameProtect.hideFriends.getValue()) ? string : string.replace(me, NameProtect.getCustomName());
      } else {
         return string;
      }
   }
}
