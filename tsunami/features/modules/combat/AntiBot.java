package tsunami.features.modules.combat;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.UUID;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_745;
import net.minecraft.class_1297.class_5529;
import tsunami.events.impl.EventSync;
import tsunami.features.modules.Module;
import tsunami.features.modules.misc.FakePlayer;
import tsunami.features.modules.render.NameTags;
import tsunami.setting.Setting;
import tsunami.utility.Timer;

public final class AntiBot extends Module {
   public static ArrayList<class_1657> bots = new ArrayList();
   public Setting<Boolean> remove = new Setting("Remove", false);
   public Setting<Boolean> onlyAura = new Setting("OnlyAura", true);
   private final Setting<AntiBot.Mode> mode;
   public Setting<Integer> checkticks;
   private final Timer clearTimer;
   private int ticks;

   public AntiBot() {
      super("AntiBot", Module.Category.NONE);
      this.mode = new Setting("Mode", AntiBot.Mode.UUIDCheck);
      this.checkticks = new Setting("checkTicks", 3, 0, 10, (v) -> {
         return this.mode.getValue() == AntiBot.Mode.MotionCheck;
      });
      this.clearTimer = new Timer();
      this.ticks = 0;
   }

   @EventHandler
   public void onSync(EventSync e) {
      if (!(Boolean)this.onlyAura.getValue()) {
         mc.field_1687.method_18456().forEach(this::markAsBot);
      } else {
         class_1297 var3 = Aura.target;
         if (var3 instanceof class_1657) {
            class_1657 ent = (class_1657)var3;
            this.markAsBot(ent);
         }
      }

      if ((Boolean)this.remove.getValue()) {
         bots.forEach((b) -> {
            try {
               mc.field_1687.method_2945(b.method_5628(), class_5529.field_26998);
            } catch (Exception var2) {
            }

         });
      }

      if (this.clearTimer.passedMs(10000L)) {
         bots.clear();
         this.ticks = 0;
         this.clearTimer.reset();
      }

   }

   private void markAsBot(class_1657 ent) {
      if (!bots.contains(ent)) {
         switch(((AntiBot.Mode)this.mode.getValue()).ordinal()) {
         case 0:
            if (!ent.method_5667().equals(UUID.nameUUIDFromBytes(("OfflinePlayer:" + ent.method_5477().getString()).getBytes(StandardCharsets.UTF_8))) && ent instanceof class_745 && (FakePlayer.fakePlayer == null || ent.method_5628() != FakePlayer.fakePlayer.method_5628()) && !ent.method_5477().getString().contains("-")) {
               this.addBot(ent);
            }
            break;
         case 1:
            double diffX = ent.method_23317() - ent.field_6014;
            double diffZ = ent.method_23321() - ent.field_5969;
            if (diffX * diffX + diffZ * diffZ > 0.5D) {
               if (this.ticks >= (Integer)this.checkticks.getValue()) {
                  this.addBot(ent);
               }

               ++this.ticks;
            }
            break;
         case 2:
            if (NameTags.getEntityPing(ent) <= 0) {
               this.addBot(ent);
            }
         }

      }
   }

   private void addBot(class_1657 entity) {
      this.sendMessage(entity.method_5477().getString() + " is a bot!");
      bots.add(entity);
   }

   public String getDisplayInfo() {
      return String.valueOf(bots.size());
   }

   public static enum Mode {
      UUIDCheck,
      MotionCheck,
      ZeroPing;

      // $FF: synthetic method
      private static AntiBot.Mode[] $values() {
         return new AntiBot.Mode[]{UUIDCheck, MotionCheck, ZeroPing};
      }
   }
}
