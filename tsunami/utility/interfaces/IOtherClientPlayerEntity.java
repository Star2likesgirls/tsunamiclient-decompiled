package tsunami.utility.interfaces;

import tsunami.features.modules.combat.Aura;

public interface IOtherClientPlayerEntity {
   void resolve(Aura.Resolver var1);

   void releaseResolver();
}
