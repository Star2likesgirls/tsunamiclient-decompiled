package tsunami.injection;

import com.mojang.authlib.GameProfile;
import java.util.Iterator;
import net.minecraft.class_243;
import net.minecraft.class_638;
import net.minecraft.class_742;
import net.minecraft.class_745;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import tsunami.core.manager.client.ModuleManager;
import tsunami.features.modules.Module;
import tsunami.features.modules.combat.Aura;
import tsunami.features.modules.misc.FakePlayer;
import tsunami.utility.interfaces.IEntityLiving;
import tsunami.utility.interfaces.IOtherClientPlayerEntity;

@Mixin({class_745.class})
public class MixinOtherClientPlayerEntity extends class_742 implements IOtherClientPlayerEntity {
   @Unique
   private double backUpX;
   @Unique
   private double backUpY;
   @Unique
   private double backUpZ;

   public MixinOtherClientPlayerEntity(class_638 world, GameProfile profile) {
      super(world, profile);
   }

   public void resolve(Aura.Resolver mode) {
      if (this == FakePlayer.fakePlayer) {
         this.backUpY = -999.0D;
      } else {
         this.backUpX = this.method_23317();
         this.backUpY = this.method_23318();
         this.backUpZ = this.method_23321();
         if (mode == Aura.Resolver.BackTrack) {
            double minDst = 999.0D;
            Aura.Position bestPos = null;
            Iterator var5 = ((IEntityLiving)this).getPositionHistory().iterator();

            while(var5.hasNext()) {
               Aura.Position p = (Aura.Position)var5.next();
               double dst = Module.mc.field_1724.method_5649(p.getX(), p.getY(), p.getZ());
               if (dst < minDst) {
                  minDst = dst;
                  bestPos = p;
               }
            }

            if (bestPos != null) {
               this.method_5814(bestPos.getX(), bestPos.getY(), bestPos.getZ());
               if (Aura.target == this) {
                  ModuleManager.aura.resolvedBox = this.method_5829();
               }
            }

         } else {
            class_243 from = new class_243(((IEntityLiving)this).getPrevServerX(), ((IEntityLiving)this).getPrevServerY(), ((IEntityLiving)this).getPrevServerZ());
            class_243 to = new class_243(this.field_6224, this.field_6245, this.field_6263);
            if (mode == Aura.Resolver.Advantage) {
               if (Module.mc.field_1724.method_5707(from) > Module.mc.field_1724.method_5707(to)) {
                  this.method_5814(to.field_1352, to.field_1351, to.field_1350);
               } else {
                  this.method_5814(from.field_1352, from.field_1351, from.field_1350);
               }
            } else {
               this.method_5814(to.field_1352, to.field_1351, to.field_1350);
            }

            if (Aura.target == this) {
               ModuleManager.aura.resolvedBox = this.method_5829();
            }

         }
      }
   }

   public void releaseResolver() {
      if (this.backUpY != -999.0D) {
         this.method_5814(this.backUpX, this.backUpY, this.backUpZ);
         this.backUpY = -999.0D;
      }

   }
}
