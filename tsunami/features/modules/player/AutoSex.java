package tsunami.features.modules.player;

import java.util.Arrays;
import java.util.List;
import net.minecraft.class_1657;
import net.minecraft.class_634;
import tsunami.core.Managers;
import tsunami.features.modules.Module;
import tsunami.setting.Setting;
import tsunami.utility.Timer;
import tsunami.utility.math.MathUtility;

public class AutoSex extends Module {
   private final Setting<Integer> targetRange = new Setting("Target Range", 5, 1, 10);
   private final Setting<AutoSex.SexMode> mode;
   private final Setting<Integer> msgDelay;
   private static final String[] PASSIVE_MESSAGES = new String[]{"It's so Biiiiiiig", "Be careful daddy <3", "Oh, I feel it inside me!"};
   private static final String[] ACTIVE_MESSAGES = new String[]{"Oh, I'm cumming!", "Oh, ur pussy is so nice!", "Yeah, yeah", "I feel u!", "Oh, im inside u"};
   private class_1657 target;
   private final Timer messageTimer;
   private final Timer sneakTimer;

   public AutoSex() {
      super("AutoSex", Module.Category.NONE);
      this.mode = new Setting("Sex Mode", AutoSex.SexMode.Active);
      this.msgDelay = new Setting("Message Delay", 1, 0, 50);
      this.messageTimer = new Timer();
      this.sneakTimer = new Timer();
   }

   public void onUpdate() {
      if (!fullNullCheck()) {
         if (this.target == null) {
            this.target = Managers.COMBAT.getNearestTarget((float)(Integer)this.targetRange.getValue());
         } else if (this.target.method_19538().method_1025(mc.field_1724.method_19538()) >= (double)this.targetRange.getPow2Value()) {
            this.target = null;
         } else {
            switch(((AutoSex.SexMode)this.mode.getValue()).ordinal()) {
            case 0:
               if (this.sneakTimer.passedMs((long)MathUtility.random(200.0F, 1200.0F))) {
                  mc.field_1690.field_1832.method_23481(!mc.field_1690.field_1832.method_1434());
                  this.sneakTimer.reset();
               }
               break;
            case 1:
               if (!mc.field_1690.field_1832.method_1434()) {
                  mc.field_1690.field_1832.method_23481(true);
               }
            }

            if (this.messageTimer.passedMs((long)((Integer)this.msgDelay.getValue() * 1000)) && mc.method_1562() != null) {
               List<String> messages = Arrays.stream(this.mode.getValue() == AutoSex.SexMode.Active ? ACTIVE_MESSAGES : PASSIVE_MESSAGES).toList();
               class_634 var10000 = mc.method_1562();
               String var10001 = this.target.method_5477().getString();
               var10000.method_45730("msg " + var10001 + " " + (String)messages.get((int)(Math.random() * (double)messages.size())));
               this.messageTimer.reset();
            }

         }
      }
   }

   private static enum SexMode {
      Active,
      Passive;

      // $FF: synthetic method
      private static AutoSex.SexMode[] $values() {
         return new AutoSex.SexMode[]{Active, Passive};
      }
   }
}
