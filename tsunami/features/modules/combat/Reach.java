package tsunami.features.modules.combat;

import tsunami.features.modules.Module;
import tsunami.setting.Setting;

public class Reach extends Module {
   public final Setting<Float> blocksRange = new Setting("BlocksRange", 3.0F, 0.1F, 10.0F);
   public final Setting<Float> entityRange = new Setting("EntityRange", 3.0F, 0.1F, 10.0F);
   public final Setting<Boolean> Creative = new Setting("Creative", false);
   public final Setting<Float> creativeBlocksRange = new Setting("CBlocksRange", 5.0F, 0.1F, 10.0F, (v) -> {
      return (Boolean)this.Creative.getValue();
   });
   public final Setting<Float> creativeEntityRange = new Setting("CEntityRange", 5.0F, 0.1F, 10.0F, (v) -> {
      return (Boolean)this.Creative.getValue();
   });

   public Reach() {
      super("Reach", Module.Category.NONE);
   }

   public String getDisplayInfo() {
      String var10000 = String.valueOf(this.blocksRange.getValue());
      return "B: " + var10000 + " E:" + String.valueOf(this.entityRange.getValue());
   }
}
