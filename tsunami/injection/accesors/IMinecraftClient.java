package tsunami.injection.accesors;

import com.mojang.authlib.minecraft.UserApiService;
import net.minecraft.class_310;
import net.minecraft.class_320;
import net.minecraft.class_5520;
import net.minecraft.class_7574;
import net.minecraft.class_7853;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin({class_310.class})
public interface IMinecraftClient {
   @Accessor("itemUseCooldown")
   int getUseCooldown();

   @Accessor("itemUseCooldown")
   void setUseCooldown(int var1);

   @Invoker("doItemUse")
   void idoItemUse();

   @Invoker("doAttack")
   boolean idoAttack();

   @Mutable
   @Accessor("profileKeys")
   void setProfileKeys(class_7853 var1);

   @Mutable
   @Accessor("session")
   void setSessionT(class_320 var1);

   @Mutable
   @Accessor
   void setUserApiService(UserApiService var1);

   @Mutable
   @Accessor("socialInteractionsManager")
   void setSocialInteractionsManagerT(class_5520 var1);

   @Mutable
   @Accessor("abuseReportContext")
   void setAbuseReportContextT(class_7574 var1);
}
