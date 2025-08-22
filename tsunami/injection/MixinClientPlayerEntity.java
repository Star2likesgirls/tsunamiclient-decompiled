package tsunami.injection;

import com.mojang.authlib.GameProfile;
import net.minecraft.class_1313;
import net.minecraft.class_243;
import net.minecraft.class_2848;
import net.minecraft.class_638;
import net.minecraft.class_742;
import net.minecraft.class_746;
import net.minecraft.class_2848.class_2849;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tsunami.TsunamiClient;
import tsunami.core.Core;
import tsunami.core.manager.client.ModuleManager;
import tsunami.events.impl.EventAfterRotate;
import tsunami.events.impl.EventMove;
import tsunami.events.impl.EventPostSync;
import tsunami.events.impl.EventSprint;
import tsunami.events.impl.EventSync;
import tsunami.events.impl.PlayerUpdateEvent;
import tsunami.events.impl.PostPlayerUpdateEvent;
import tsunami.features.modules.Module;

@Mixin(
   value = {class_746.class},
   priority = 800
)
public abstract class MixinClientPlayerEntity extends class_742 {
   @Unique
   boolean pre_sprint_state = false;
   @Unique
   private boolean updateLock = false;
   @Unique
   private Runnable postAction;

   @Shadow
   public abstract float method_5695(float var1);

   @Shadow
   protected abstract void method_3136();

   public MixinClientPlayerEntity(class_638 world, GameProfile profile) {
      super(world, profile);
   }

   @Inject(
      method = {"tick"},
      at = {@At("HEAD")}
   )
   public void tickHook(CallbackInfo info) {
      if (!Module.fullNullCheck()) {
         TsunamiClient.EVENT_BUS.post((Object)(new PlayerUpdateEvent()));
      }
   }

   @Redirect(
      method = {"tickMovement"},
      at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/client/network/ClientPlayerEntity;isUsingItem()Z"
),
      require = 0
   )
   private boolean tickMovementHook(class_746 player) {
      return ModuleManager.noSlow.isEnabled() && ModuleManager.noSlow.canNoSlow() ? false : player.method_6115();
   }

   @Inject(
      method = {"shouldSlowDown"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void shouldSlowDownHook(CallbackInfoReturnable<Boolean> cir) {
      if (ModuleManager.noSlow.isEnabled()) {
         if (this.method_20448()) {
            if ((Boolean)ModuleManager.noSlow.crawl.getValue()) {
               cir.setReturnValue(false);
            }
         } else if ((Boolean)ModuleManager.noSlow.sneak.getValue()) {
            cir.setReturnValue(false);
         }
      }

   }

   @Inject(
      method = {"move"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;move(Lnet/minecraft/entity/MovementType;Lnet/minecraft/util/math/Vec3d;)V"
)},
      cancellable = true
   )
   public void onMoveHook(class_1313 movementType, class_243 movement, CallbackInfo ci) {
      if (!Module.fullNullCheck()) {
         EventMove event = new EventMove(movement.field_1352, movement.field_1351, movement.field_1350);
         TsunamiClient.EVENT_BUS.post((Object)event);
         if (event.isCancelled()) {
            super.method_5784(movementType, new class_243(event.getX(), event.getY(), event.getZ()));
            ci.cancel();
         }

      }
   }

   @Inject(
      method = {"sendMovementPackets"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void sendMovementPacketsHook(CallbackInfo info) {
      if (!Module.fullNullCheck()) {
         EventSync event = new EventSync(this.method_36454(), this.method_36455());
         TsunamiClient.EVENT_BUS.post((Object)event);
         this.postAction = event.getPostAction();
         EventSprint e = new EventSprint(this.method_5624());
         TsunamiClient.EVENT_BUS.post((Object)e);
         TsunamiClient.EVENT_BUS.post((Object)(new EventAfterRotate()));
         if (e.getSprintState() != Module.mc.field_1724.field_3919) {
            if (e.getSprintState()) {
               Module.mc.field_1724.field_3944.method_52787(new class_2848(this, class_2849.field_12981));
            } else {
               Module.mc.field_1724.field_3944.method_52787(new class_2848(this, class_2849.field_12985));
            }

            Module.mc.field_1724.field_3919 = e.getSprintState();
         }

         this.pre_sprint_state = Module.mc.field_1724.field_3919;
         Core.lockSprint = true;
         if (event.isCancelled()) {
            info.cancel();
         }

      }
   }

   @Inject(
      method = {"sendMovementPackets"},
      at = {@At("RETURN")},
      cancellable = true
   )
   private void sendMovementPacketsPostHook(CallbackInfo info) {
      if (!Module.fullNullCheck()) {
         Module.mc.field_1724.field_3919 = this.pre_sprint_state;
         Core.lockSprint = false;
         EventPostSync event = new EventPostSync();
         TsunamiClient.EVENT_BUS.post((Object)event);
         if (this.postAction != null) {
            this.postAction.run();
            this.postAction = null;
         }

         if (event.isCancelled()) {
            info.cancel();
         }

      }
   }

   @Inject(
      method = {"tick"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/client/network/ClientPlayerEntity;sendMovementPackets()V",
   ordinal = 0,
   shift = Shift.AFTER
)},
      cancellable = true
   )
   private void PostUpdateHook(CallbackInfo info) {
      if (!Module.fullNullCheck()) {
         if (!this.updateLock) {
            PostPlayerUpdateEvent playerUpdateEvent = new PostPlayerUpdateEvent();
            TsunamiClient.EVENT_BUS.post((Object)playerUpdateEvent);
            if (playerUpdateEvent.isCancelled()) {
               info.cancel();
               if (playerUpdateEvent.getIterations() > 0) {
                  for(int i = 0; i < playerUpdateEvent.getIterations(); ++i) {
                     this.updateLock = true;
                     this.method_5773();
                     this.updateLock = false;
                     this.method_3136();
                  }
               }
            }

         }
      }
   }

   @Inject(
      method = {"pushOutOfBlocks"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onPushOutOfBlocksHook(double x, double d, CallbackInfo info) {
      if (ModuleManager.noPush.isEnabled() && (Boolean)ModuleManager.noPush.blocks.getValue()) {
         info.cancel();
      }

   }

   @Inject(
      method = {"tickNausea"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void updateNauseaHook(CallbackInfo ci) {
      if (ModuleManager.portalInventory.isEnabled()) {
         ci.cancel();
      }

   }
}
