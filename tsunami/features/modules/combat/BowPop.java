package tsunami.features.modules.combat;

import java.util.Random;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_1268;
import net.minecraft.class_1802;
import net.minecraft.class_2846;
import net.minecraft.class_2848;
import net.minecraft.class_2886;
import net.minecraft.class_2828.class_2829;
import net.minecraft.class_2828.class_2830;
import net.minecraft.class_2846.class_2847;
import net.minecraft.class_2848.class_2849;
import tsunami.events.impl.PacketEvent;
import tsunami.features.modules.Module;
import tsunami.setting.Setting;
import tsunami.setting.impl.SettingGroup;
import tsunami.utility.Timer;

public final class BowPop extends Module {
   public Setting<Boolean> rotation = new Setting("Rotation", false);
   public Setting<BowPop.ModeEn> Mode;
   public Setting<Float> factor;
   public Setting<BowPop.exploitEn> exploit;
   public Setting<Float> scale;
   public Setting<Boolean> minimize;
   public Setting<Float> delay;
   public final Setting<SettingGroup> selection;
   public final Setting<Boolean> bow;
   public final Setting<Boolean> pearls;
   public final Setting<Boolean> xp;
   public final Setting<Boolean> eggs;
   public final Setting<Boolean> potions;
   public final Setting<Boolean> snowballs;
   public static Timer delayTimer = new Timer();
   private final Random rnd;

   public BowPop() {
      super("BowPop", Module.Category.NONE);
      this.Mode = new Setting("Mode", BowPop.ModeEn.Maximum);
      this.factor = new Setting("Factor", 1.0F, 1.0F, 20.0F);
      this.exploit = new Setting("Exploit", BowPop.exploitEn.Strong);
      this.scale = new Setting("Scale", 0.01F, 0.01F, 0.4F);
      this.minimize = new Setting("Minimize", false);
      this.delay = new Setting("Delay", 5.0F, 0.0F, 10.0F);
      this.selection = new Setting("Selection", new SettingGroup(false, 0));
      this.bow = (new Setting("Bows", true)).addToGroup(this.selection);
      this.pearls = (new Setting("EPearls", true)).addToGroup(this.selection);
      this.xp = (new Setting("XP", true)).addToGroup(this.selection);
      this.eggs = (new Setting("Eggs", true)).addToGroup(this.selection);
      this.potions = (new Setting("SplashPotions", true)).addToGroup(this.selection);
      this.snowballs = (new Setting("Snowballs", true)).addToGroup(this.selection);
      this.rnd = new Random();
   }

   @EventHandler
   private void onPacketSend(PacketEvent.Send event) {
      if (!fullNullCheck() && delayTimer.passedMs((long)((Float)this.delay.getValue() * 1000.0F))) {
         if (event.getPacket() instanceof class_2846 && ((class_2846)event.getPacket()).method_12363() == class_2847.field_12974 && mc.field_1724.method_6030().method_7909() == class_1802.field_8102 && (Boolean)this.bow.getValue() || event.getPacket() instanceof class_2886 && ((class_2886)event.getPacket()).method_12551() == class_1268.field_5808 && (mc.field_1724.method_6047().method_7909() == class_1802.field_8634 && (Boolean)this.pearls.getValue() || mc.field_1724.method_6047().method_7909() == class_1802.field_8287 && (Boolean)this.xp.getValue() || mc.field_1724.method_6047().method_7909() == class_1802.field_8803 && (Boolean)this.eggs.getValue() || mc.field_1724.method_6047().method_7909() == class_1802.field_8436 && (Boolean)this.potions.getValue() || mc.field_1724.method_6047().method_7909() == class_1802.field_8543 && (Boolean)this.snowballs.getValue())) {
            mc.field_1724.field_3944.method_52787(new class_2848(mc.field_1724, class_2849.field_12981));
            double[] strict_direction = new double[]{100.0D * -Math.sin(Math.toRadians((double)mc.field_1724.method_36454())), 100.0D * Math.cos(Math.toRadians((double)mc.field_1724.method_36454()))};
            int i;
            if (this.exploit.getValue() == BowPop.exploitEn.Fast) {
               for(i = 0; i < this.getRuns(); ++i) {
                  this.spoof(mc.field_1724.method_23317(), (Boolean)this.minimize.getValue() ? mc.field_1724.method_23318() : mc.field_1724.method_23318() - 1.0E-10D, mc.field_1724.method_23321(), true);
                  this.spoof(mc.field_1724.method_23317(), mc.field_1724.method_23318() + 1.0E-10D, mc.field_1724.method_23321(), false);
               }
            }

            if (this.exploit.getValue() == BowPop.exploitEn.Strong) {
               for(i = 0; i < this.getRuns(); ++i) {
                  this.spoof(mc.field_1724.method_23317(), mc.field_1724.method_23318() + 1.0E-10D, mc.field_1724.method_23321(), false);
                  this.spoof(mc.field_1724.method_23317(), (Boolean)this.minimize.getValue() ? mc.field_1724.method_23318() : mc.field_1724.method_23318() - 1.0E-10D, mc.field_1724.method_23321(), true);
               }
            }

            if (this.exploit.getValue() == BowPop.exploitEn.Phobos) {
               for(i = 0; i < this.getRuns(); ++i) {
                  this.spoof(mc.field_1724.method_23317(), mc.field_1724.method_23318() + 1.3E-13D, mc.field_1724.method_23321(), true);
                  this.spoof(mc.field_1724.method_23317(), mc.field_1724.method_23318() + 2.7E-13D, mc.field_1724.method_23321(), false);
               }
            }

            if (this.exploit.getValue() == BowPop.exploitEn.Strict) {
               for(i = 0; i < this.getRuns(); ++i) {
                  if (this.rnd.nextBoolean()) {
                     this.spoof(mc.field_1724.method_23317() - strict_direction[0], mc.field_1724.method_23318(), mc.field_1724.method_23321() - strict_direction[1], false);
                  } else {
                     this.spoof(mc.field_1724.method_23317() + strict_direction[0], mc.field_1724.method_23318(), mc.field_1724.method_23321() + strict_direction[1], true);
                  }
               }
            }

            if (this.exploit.getValue() == BowPop.exploitEn.WB) {
               for(i = 0; i < this.getRuns(); ++i) {
                  this.spoof(mc.field_1724.method_23317() + (double)this.getWorldBorderRnd(), mc.field_1724.method_23318(), mc.field_1724.method_23321() + (double)this.getWorldBorderRnd(), false);
               }
            }

            delayTimer.reset();
         }

      }
   }

   private void spoof(double x, double y, double z, boolean ground) {
      if ((Boolean)this.rotation.getValue()) {
         this.sendPacket(new class_2830(x, y, z, mc.field_1724.method_36454(), mc.field_1724.method_36455(), ground));
      } else {
         this.sendPacket(new class_2829(x, y, z, ground));
      }

   }

   private int getRuns() {
      int var10000;
      switch(((BowPop.ModeEn)this.Mode.getValue()).ordinal()) {
      case 0:
         var10000 = (int)Math.floor((double)(Float)this.factor.getValue());
         break;
      case 1:
         var10000 = (int)(30.0F * (Float)this.factor.getValue());
         break;
      case 2:
         var10000 = 10 + (int)((Float)this.factor.getValue() - 1.0F);
         break;
      default:
         throw new MatchException((String)null, (Throwable)null);
      }

      return var10000;
   }

   private int getWorldBorderRnd() {
      if (mc.method_1542()) {
         return 1;
      } else {
         int n = this.rnd.nextInt(29000000);
         return this.rnd.nextBoolean() ? n : -n;
      }
   }

   private static enum ModeEn {
      Normal,
      Maximum,
      Factorised;

      // $FF: synthetic method
      private static BowPop.ModeEn[] $values() {
         return new BowPop.ModeEn[]{Normal, Maximum, Factorised};
      }
   }

   private static enum exploitEn {
      Strong,
      Fast,
      Strict,
      Phobos,
      WB;

      // $FF: synthetic method
      private static BowPop.exploitEn[] $values() {
         return new BowPop.exploitEn[]{Strong, Fast, Strict, Phobos, WB};
      }
   }
}
