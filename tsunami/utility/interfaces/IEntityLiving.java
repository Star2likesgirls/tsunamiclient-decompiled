package tsunami.utility.interfaces;

import java.util.List;
import tsunami.features.modules.combat.Aura;

public interface IEntityLiving {
   double getPrevServerX();

   double getPrevServerY();

   double getPrevServerZ();

   List<Aura.Position> getPositionHistory();
}
