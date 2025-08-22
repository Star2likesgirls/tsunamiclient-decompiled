package tsunami.injection;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_1309;
import net.minecraft.class_1313;
import net.minecraft.class_2338;
import net.minecraft.class_2404;
import net.minecraft.class_243;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tsunami.TsunamiClient;
import tsunami.core.manager.client.ModuleManager;
import tsunami.events.impl.EventTravel;
import tsunami.features.modules.Module;
import tsunami.features.modules.combat.Aura;
import tsunami.features.modules.movement.WaterSpeed;
import tsunami.features.modules.render.Animations;
import tsunami.utility.interfaces.IEntityLiving;

@Mixin({class_1309.class})
public class MixinEntityLiving implements IEntityLiving {
   @Shadow
   protected double field_6224;
   @Shadow
   protected double field_6245;
   @Shadow
   protected double field_6263;
   @Unique
   double prevServerX;
   @Unique
   double prevServerY;
   @Unique
   double prevServerZ;
   @Unique
   public List<Aura.Position> positonHistory = new ArrayList();
   @Unique
   private boolean prevFlying = false;

   public List<Aura.Position> getPositionHistory() {
      return this.positonHistory;
   }

   @Inject(
      method = {"getHandSwingDuration"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void getArmSwingAnimationEnd(CallbackInfoReturnable<Integer> info) {
      if (!(Boolean)ModuleManager.noRender.noSwing.getValue() && ModuleManager.animations.shouldChangeAnimationDuration() && (Boolean)Animations.slowAnimation.getValue()) {
         info.setReturnValue((Integer)Animations.slowAnimationVal.getValue());
      }

   }

   @Inject(
      method = {"updateTrackedPositionAndAngles"},
      at = {@At("HEAD")}
   )
   private void updateTrackedPositionAndAnglesHook(double x, double y, double z, float yaw, float pitch, int interpolationSteps, CallbackInfo ci) {
      if (!Module.fullNullCheck()) {
         this.prevServerX = this.field_6224;
         this.prevServerY = this.field_6245;
         this.prevServerZ = this.field_6263;
         this.positonHistory.add(new Aura.Position(this.field_6224, this.field_6245, this.field_6263));
         this.positonHistory.removeIf(Aura.Position::shouldRemove);
      }
   }

   public double getPrevServerX() {
      return this.prevServerX;
   }

   public double getPrevServerY() {
      return this.prevServerY;
   }

   public double getPrevServerZ() {
      return this.prevServerZ;
   }

   @Inject(
      method = {"isFallFlying"},
      at = {@At("TAIL")},
      cancellable = true
   )
   public void isFallFlyingHook(CallbackInfoReturnable<Boolean> cir) {
      if (ModuleManager.elytraRecast.isEnabled()) {
         boolean elytra = (Boolean)cir.getReturnValue();
         if (this.prevFlying && !(Boolean)cir.getReturnValue()) {
            cir.setReturnValue(ModuleManager.elytraRecast.castElytra());
         }

         this.prevFlying = elytra;
      }

   }

   @Inject(
      method = {"travel"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void travelHook(class_243 movementInput, CallbackInfo ci) {
      if (!Module.fullNullCheck()) {
         if ((class_1309)this == Module.mc.field_1724) {
            EventTravel event = new EventTravel(Module.mc.field_1724.method_18798(), true);
            TsunamiClient.EVENT_BUS.post((Object)event);
            if (event.isCancelled()) {
               Module.mc.field_1724.method_5784(class_1313.field_6308, event.getmVec());
               ci.cancel();
            }

         }
      }
   }

   @Inject(
      method = {"travel"},
      at = {@At("RETURN")},
      cancellable = true
   )
   public void travelPostHook(class_243 movementInput, CallbackInfo ci) {
      if (!Module.fullNullCheck()) {
         if ((class_1309)this == Module.mc.field_1724) {
            EventTravel event = new EventTravel(movementInput, false);
            TsunamiClient.EVENT_BUS.post((Object)event);
            if (event.isCancelled()) {
               Module.mc.field_1724.method_5784(class_1313.field_6308, Module.mc.field_1724.method_18798());
               ci.cancel();
            }

         }
      }
   }

   @ModifyVariable(
      method = {"setSprinting"},
      at = @At("HEAD"),
      ordinal = 0,
      argsOnly = true
   )
   private boolean setSprintingHook(boolean sprinting) {
      return Module.mc.field_1724 == null || Module.mc.field_1687 == null || !ModuleManager.waterSpeed.isEnabled() || !ModuleManager.waterSpeed.mode.is(WaterSpeed.Mode.CancelResurface) || !Module.mc.field_1724.method_5799() && !(Module.mc.field_1687.method_8320(class_2338.method_49638(Module.mc.field_1724.method_19538().method_1031(0.0D, -0.5D, 0.0D))).method_26204() instanceof class_2404) ? sprinting : true;
   }

   @Inject(
      method = {"getHandSwingDuration"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onGetHandSwingDuration(CallbackInfoReturnable<Integer> cir) {
      if (ModuleManager.noRender.isEnabled() && (Boolean)ModuleManager.noRender.noSwing.getValue()) {
         cir.setReturnValue(0);
         cir.cancel();
      }

   }
}
