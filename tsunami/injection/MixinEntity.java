package tsunami.injection;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_2338;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_3532;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
import tsunami.TsunamiClient;
import tsunami.core.manager.client.ModuleManager;
import tsunami.events.impl.EventFixVelocity;
import tsunami.features.modules.Module;
import tsunami.features.modules.combat.HitBox;
import tsunami.features.modules.render.Shaders;
import tsunami.features.modules.render.Trails;
import tsunami.utility.interfaces.IEntity;

@Mixin({class_1297.class})
public abstract class MixinEntity implements IEntity {
   @Shadow
   private class_238 field_6005;
   @Unique
   public List<Trails.Trail> trails = new ArrayList();

   @Shadow
   protected abstract class_2338 method_23314();

   public List<Trails.Trail> getTrails() {
      return this.trails;
   }

   public class_2338 thunderHack_Recode$getVelocityBP() {
      return this.method_23314();
   }

   @ModifyArgs(
      method = {"pushAwayFrom"},
      at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/entity/Entity;addVelocity(DDD)V"
)
   )
   public void pushAwayFromHook(Args args) {
      if (this == Module.mc.field_1724 && ModuleManager.noPush.isEnabled() && (Boolean)ModuleManager.noPush.players.getValue()) {
         args.set(0, 0.0D);
         args.set(1, 0.0D);
         args.set(2, 0.0D);
      }

   }

   @Inject(
      method = {"updateVelocity"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void updateVelocityHook(float speed, class_243 movementInput, CallbackInfo ci) {
      if (!Module.fullNullCheck()) {
         if (this == Module.mc.field_1724) {
            ci.cancel();
            EventFixVelocity event = new EventFixVelocity(movementInput, speed, Module.mc.field_1724.method_36454(), movementInputToVelocityC(movementInput, speed, Module.mc.field_1724.method_36454()));
            TsunamiClient.EVENT_BUS.post((Object)event);
            Module.mc.field_1724.method_18799(Module.mc.field_1724.method_18798().method_1019(event.getVelocity()));
         }

      }
   }

   @Unique
   private static class_243 movementInputToVelocityC(class_243 movementInput, float speed, float yaw) {
      double d = movementInput.method_1027();
      if (d < 1.0E-7D) {
         return class_243.field_1353;
      } else {
         class_243 vec3d = (d > 1.0D ? movementInput.method_1029() : movementInput).method_1021((double)speed);
         float f = class_3532.method_15374(yaw * 0.017453292F);
         float g = class_3532.method_15362(yaw * 0.017453292F);
         return new class_243(vec3d.field_1352 * (double)g - vec3d.field_1350 * (double)f, vec3d.field_1351, vec3d.field_1350 * (double)g + vec3d.field_1352 * (double)f);
      }
   }

   @Inject(
      method = {"getBoundingBox"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public final void getBoundingBox(CallbackInfoReturnable<class_238> cir) {
      if (ModuleManager.hitBox.isEnabled() && Module.mc != null && Module.mc.field_1724 != null && ((class_1297)this).method_5628() != Module.mc.field_1724.method_5628() && (ModuleManager.aura.isDisabled() || (Boolean)HitBox.affectToAura.getValue())) {
         cir.setReturnValue(new class_238(this.field_6005.field_1323 - (double)((Float)HitBox.XZExpand.getValue() / 2.0F), this.field_6005.field_1322 - (double)((Float)HitBox.YExpand.getValue() / 2.0F), this.field_6005.field_1321 - (double)((Float)HitBox.XZExpand.getValue() / 2.0F), this.field_6005.field_1320 + (double)((Float)HitBox.XZExpand.getValue() / 2.0F), this.field_6005.field_1325 + (double)((Float)HitBox.YExpand.getValue() / 2.0F), this.field_6005.field_1324 + (double)((Float)HitBox.XZExpand.getValue() / 2.0F)));
      }

   }

   @Inject(
      method = {"isGlowing"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void isGlowingHook(CallbackInfoReturnable<Boolean> cir) {
      Shaders shaders = ModuleManager.shaders;
      if (shaders.isEnabled()) {
         cir.setReturnValue(shaders.shouldRender((class_1297)this));
      }

   }

   @Inject(
      method = {"isOnFire"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void isOnFireHook(CallbackInfoReturnable<Boolean> cir) {
      if (ModuleManager.noRender.isEnabled() && (Boolean)ModuleManager.noRender.fireEntity.getValue()) {
         cir.setReturnValue(false);
      }

   }

   @Inject(
      method = {"isInvisibleTo"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void isInvisibleToHook(class_1657 player, CallbackInfoReturnable<Boolean> cir) {
      if (ModuleManager.serverHelper.isEnabled() && (Boolean)ModuleManager.serverHelper.trueSight.getValue()) {
         cir.setReturnValue(false);
      }

   }

   @Inject(
      method = {"isInLava"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void isInLavaHook(CallbackInfoReturnable<Boolean> cir) {
      if ((ModuleManager.jesus.isEnabled() || ModuleManager.noWaterCollision.isEnabled()) && Module.mc.field_1724 != null && ((class_1297)this).method_5628() == Module.mc.field_1724.method_5628()) {
         cir.setReturnValue(false);
      }

   }

   @Inject(
      method = {"isTouchingWater"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void isTouchingWaterHook(CallbackInfoReturnable<Boolean> cir) {
      if ((ModuleManager.jesus.isEnabled() || ModuleManager.noWaterCollision.isEnabled()) && Module.mc.field_1724 != null && ((class_1297)this).method_5628() == Module.mc.field_1724.method_5628()) {
         cir.setReturnValue(false);
      }

   }

   @Inject(
      method = {"setSwimming"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void setSwimmingHook(boolean swimming, CallbackInfo ci) {
      if ((ModuleManager.jesus.isEnabled() || ModuleManager.noWaterCollision.isEnabled()) && swimming && Module.mc.field_1724 != null && ((class_1297)this).method_5628() == Module.mc.field_1724.method_5628()) {
         ci.cancel();
      }

   }

   @ModifyVariable(
      method = {"changeLookDirection"},
      at = @At("HEAD"),
      ordinal = 0,
      argsOnly = true
   )
   private double changeLookDirectionHook0(double value) {
      return ModuleManager.viewLock.isEnabled() && (Boolean)ModuleManager.viewLock.yaw.getValue() ? 0.0D : value;
   }

   @ModifyVariable(
      method = {"changeLookDirection"},
      at = @At("HEAD"),
      ordinal = 1,
      argsOnly = true
   )
   private double changeLookDirectionHook1(double value) {
      return ModuleManager.viewLock.isEnabled() && (Boolean)ModuleManager.viewLock.pitch.getValue() ? 0.0D : value;
   }
}
