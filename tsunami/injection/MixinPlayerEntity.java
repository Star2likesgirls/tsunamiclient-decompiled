package tsunami.injection;

import net.minecraft.class_1297;
import net.minecraft.class_1313;
import net.minecraft.class_1657;
import net.minecraft.class_1799;
import net.minecraft.class_1937;
import net.minecraft.class_243;
import net.minecraft.class_2561;
import net.minecraft.class_4174;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tsunami.TsunamiClient;
import tsunami.core.manager.client.ModuleManager;
import tsunami.events.impl.EventAttack;
import tsunami.events.impl.EventEatFood;
import tsunami.events.impl.EventPlayerJump;
import tsunami.events.impl.EventPlayerTravel;
import tsunami.features.modules.Module;
import tsunami.features.modules.client.Media;
import tsunami.features.modules.combat.Aura;
import tsunami.features.modules.movement.AutoSprint;
import tsunami.features.modules.movement.Speed;

@Mixin(
   value = {class_1657.class},
   priority = 800
)
public class MixinPlayerEntity {
   @Inject(
      method = {"getAttackCooldownProgressPerTick"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void getAttackCooldownProgressPerTickHook(CallbackInfoReturnable<Float> cir) {
      if (ModuleManager.aura.isEnabled() && ModuleManager.aura.switchMode.getValue() == Aura.Switch.Silent) {
         cir.setReturnValue(12.5F);
      }

   }

   @Inject(
      method = {"getDisplayName"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void getDisplayNameHook(CallbackInfoReturnable<class_2561> cir) {
      if (ModuleManager.media.isEnabled() && (Boolean)Media.nickProtect.getValue()) {
         cir.setReturnValue(class_2561.method_30163("Protected"));
      }

   }

   @Inject(
      method = {"attack"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/entity/player/PlayerEntity;setSprinting(Z)V",
   shift = Shift.AFTER
)}
   )
   public void attackAHook(CallbackInfo callbackInfo) {
      if (ModuleManager.autoSprint.isEnabled() && (Boolean)AutoSprint.sprint.getValue()) {
         float multiplier = 0.6F + 0.4F * (Float)AutoSprint.motion.getValue();
         Module.mc.field_1724.method_18800(Module.mc.field_1724.method_18798().field_1352 / 0.6D * (double)multiplier, Module.mc.field_1724.method_18798().field_1351, Module.mc.field_1724.method_18798().field_1350 / 0.6D * (double)multiplier);
         Module.mc.field_1724.method_5728(true);
      }

   }

   @Inject(
      method = {"getMovementSpeed"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void getMovementSpeedHook(CallbackInfoReturnable<Float> cir) {
      if (ModuleManager.speed.isEnabled() && ModuleManager.speed.mode.is(Speed.Mode.Vanilla)) {
         cir.setReturnValue((Float)ModuleManager.speed.boostFactor.getValue());
      }

   }

   @Inject(
      method = {"attack"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void attackAHook2(class_1297 target, CallbackInfo ci) {
      EventAttack event = new EventAttack(target, false);
      TsunamiClient.EVENT_BUS.post((Object)event);
      if (event.isCancelled()) {
         ci.cancel();
      }

   }

   @Inject(
      method = {"travel"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onTravelhookPre(class_243 movementInput, CallbackInfo ci) {
      if (Module.mc.field_1724 != null) {
         EventPlayerTravel event = new EventPlayerTravel(movementInput, true);
         TsunamiClient.EVENT_BUS.post((Object)event);
         if (event.isCancelled()) {
            Module.mc.field_1724.method_5784(class_1313.field_6308, Module.mc.field_1724.method_18798());
            ci.cancel();
         }

      }
   }

   @Inject(
      method = {"travel"},
      at = {@At("RETURN")},
      cancellable = true
   )
   private void onTravelhookPost(class_243 movementInput, CallbackInfo ci) {
      if (Module.mc.field_1724 != null) {
         EventPlayerTravel event = new EventPlayerTravel(movementInput, false);
         TsunamiClient.EVENT_BUS.post((Object)event);
         if (event.isCancelled()) {
            Module.mc.field_1724.method_5784(class_1313.field_6308, Module.mc.field_1724.method_18798());
            ci.cancel();
         }

      }
   }

   @Inject(
      method = {"jump"},
      at = {@At("HEAD")}
   )
   private void onJumpPre(CallbackInfo ci) {
      TsunamiClient.EVENT_BUS.post((Object)(new EventPlayerJump(true)));
   }

   @Inject(
      method = {"jump"},
      at = {@At("RETURN")}
   )
   private void onJumpPost(CallbackInfo ci) {
      TsunamiClient.EVENT_BUS.post((Object)(new EventPlayerJump(false)));
   }

   @Inject(
      method = {"eatFood"},
      at = {@At("RETURN")}
   )
   public void eatFoodHook(class_1937 world, class_1799 stack, class_4174 foodComponent, CallbackInfoReturnable<class_1799> cir) {
      TsunamiClient.EVENT_BUS.post((Object)(new EventEatFood((class_1799)cir.getReturnValue())));
   }

   @Inject(
      method = {"shouldDismount"},
      at = {@At("HEAD")},
      cancellable = true
   )
   protected void shouldDismountHook(CallbackInfoReturnable<Boolean> cir) {
      if (ModuleManager.boatFly.isEnabled() && (Boolean)ModuleManager.boatFly.allowShift.getValue()) {
         cir.setReturnValue(false);
      }

   }

   @Inject(
      method = {"getBlockInteractionRange"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void getBlockInteractionRangeHook(CallbackInfoReturnable<Double> cir) {
      if (ModuleManager.reach.isEnabled()) {
         if ((Boolean)ModuleManager.reach.Creative.getValue() && Module.mc.field_1724.method_7337()) {
            cir.setReturnValue((double)(Float)ModuleManager.reach.creativeBlocksRange.getValue());
         } else {
            cir.setReturnValue((double)(Float)ModuleManager.reach.blocksRange.getValue());
         }
      }

   }

   @Inject(
      method = {"getEntityInteractionRange"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void getEntityInteractionRangeHook(CallbackInfoReturnable<Double> cir) {
      if (ModuleManager.reach.isEnabled()) {
         if ((Boolean)ModuleManager.reach.Creative.getValue() && Module.mc.field_1724.method_7337()) {
            cir.setReturnValue((double)(Float)ModuleManager.reach.creativeEntityRange.getValue());
         } else {
            cir.setReturnValue((double)(Float)ModuleManager.reach.entityRange.getValue());
         }
      }

   }
}
