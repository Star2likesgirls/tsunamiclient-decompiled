package tsunami.features.modules.movement;

import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_1268;
import net.minecraft.class_1802;
import net.minecraft.class_2350;
import net.minecraft.class_2846;
import net.minecraft.class_2868;
import net.minecraft.class_2886;
import net.minecraft.class_744;
import net.minecraft.class_9334;
import net.minecraft.class_2846.class_2847;
import tsunami.events.impl.EventKeyboardInput;
import tsunami.features.modules.Module;
import tsunami.setting.Setting;
import tsunami.setting.impl.SettingGroup;

public class NoSlow extends Module {
   public final Setting<NoSlow.Mode> mode;
   private final Setting<Boolean> mainHand;
   private final Setting<SettingGroup> selection;
   private final Setting<Boolean> food;
   private final Setting<Boolean> projectiles;
   private final Setting<Boolean> shield;
   public final Setting<Boolean> soulSand;
   public final Setting<Boolean> honey;
   public final Setting<Boolean> slime;
   public final Setting<Boolean> ice;
   public final Setting<Boolean> sweetBerryBush;
   public final Setting<Boolean> sneak;
   public final Setting<Boolean> crawl;
   private boolean returnSneak;

   public NoSlow() {
      super("NoSlow", Module.Category.NONE);
      this.mode = new Setting("Mode", NoSlow.Mode.NCP);
      this.mainHand = new Setting("MainHand", true);
      this.selection = new Setting("Selection", new SettingGroup(false, 0));
      this.food = (new Setting("Food", true)).addToGroup(this.selection);
      this.projectiles = (new Setting("Projectiles", true)).addToGroup(this.selection);
      this.shield = (new Setting("Shield", true)).addToGroup(this.selection);
      this.soulSand = (new Setting("SoulSand", true)).addToGroup(this.selection);
      this.honey = (new Setting("Honey", true)).addToGroup(this.selection);
      this.slime = (new Setting("Slime", true)).addToGroup(this.selection);
      this.ice = (new Setting("Ice", true)).addToGroup(this.selection);
      this.sweetBerryBush = (new Setting("SweetBerryBush", true)).addToGroup(this.selection);
      this.sneak = (new Setting("Sneak", false)).addToGroup(this.selection);
      this.crawl = (new Setting("Crawl", false)).addToGroup(this.selection);
   }

   public void onUpdate() {
      if (this.returnSneak) {
         mc.field_1690.field_1832.method_23481(false);
         mc.field_1724.method_5728(true);
         this.returnSneak = false;
      }

      if (mc.field_1724.method_6115() && !mc.field_1724.method_3144() && !mc.field_1724.method_6128()) {
         switch(((NoSlow.Mode)this.mode.getValue()).ordinal()) {
         case 1:
            this.sendPacket(new class_2868(mc.field_1724.method_31548().field_7545));
            break;
         case 2:
            if (mc.field_1724.method_24828() && !mc.field_1690.field_1903.method_1434()) {
               mc.field_1724.method_18800(mc.field_1724.method_18798().field_1352 * 0.3D, mc.field_1724.method_18798().field_1351, mc.field_1724.method_18798().field_1350 * 0.3D);
            } else if (mc.field_1724.field_6017 > 0.2F) {
               mc.field_1724.method_18800(mc.field_1724.method_18798().field_1352 * 0.949999988079071D, mc.field_1724.method_18798().field_1351, mc.field_1724.method_18798().field_1350 * 0.949999988079071D);
            }
            break;
         case 3:
            if (mc.field_1724.method_6058() == class_1268.field_5810) {
               this.sendPacket(new class_2868(mc.field_1724.method_31548().field_7545 % 8 + 1));
               this.sendPacket(new class_2868(mc.field_1724.method_31548().field_7545 % 7 + 2));
               this.sendPacket(new class_2868(mc.field_1724.method_31548().field_7545));
            } else if ((Boolean)this.mainHand.getValue()) {
               this.sendSequencedPacket((id) -> {
                  return new class_2886(class_1268.field_5810, id, mc.field_1724.method_36454(), mc.field_1724.method_36455());
               });
            }
            break;
         case 4:
            if (mc.field_1724.method_24828() && mc.field_1690.field_1903.method_1434()) {
               mc.field_1690.field_1832.method_23481(true);
               this.returnSneak = true;
            }
            break;
         case 5:
            if (mc.field_1724.method_6058() == class_1268.field_5810) {
               this.sendPacket(new class_2868(mc.field_1724.method_31548().field_7545 % 8 + 1));
               this.sendPacket(new class_2868(mc.field_1724.method_31548().field_7545 % 7 + 2));
               this.sendPacket(new class_2868(mc.field_1724.method_31548().field_7545));
            } else if ((Boolean)this.mainHand.getValue() && (mc.field_1724.method_6048() <= 3 || mc.field_1724.field_6012 % 2 == 0)) {
               this.sendSequencedPacket((id) -> {
                  return new class_2886(class_1268.field_5810, id, mc.field_1724.method_36454(), mc.field_1724.method_36455());
               });
            }
            break;
         case 6:
            if (mc.field_1724.method_24828()) {
               if (mc.field_1724.field_6012 % 2 == 0) {
                  mc.field_1724.method_18800(mc.field_1724.method_18798().field_1352 * 0.5D, mc.field_1724.method_18798().field_1351, mc.field_1724.method_18798().field_1350 * 0.5D);
               } else {
                  mc.field_1724.method_18800(mc.field_1724.method_18798().field_1352 * 0.949999988079071D, mc.field_1724.method_18798().field_1351, mc.field_1724.method_18798().field_1350 * 0.949999988079071D);
               }
            }
            break;
         case 7:
            if (mc.field_1724.method_6048() <= 3) {
               this.sendSequencedPacket((id) -> {
                  return new class_2846(class_2847.field_12971, mc.field_1724.method_24515().method_10084(), class_2350.field_11043, id);
               });
            }
         }
      }

   }

   @EventHandler
   public void onKeyboardInput(EventKeyboardInput e) {
      if (this.mode.getValue() == NoSlow.Mode.Matrix3 && mc.field_1724.method_6115() && !mc.field_1724.method_6128()) {
         class_744 var10000 = mc.field_1724.field_3913;
         var10000.field_3905 *= 5.0F;
         var10000 = mc.field_1724.field_3913;
         var10000.field_3907 *= 5.0F;
         float mult = 1.0F;
         if (mc.field_1724.method_24828()) {
            if (mc.field_1724.field_3913.field_3905 != 0.0F && mc.field_1724.field_3913.field_3907 != 0.0F) {
               var10000 = mc.field_1724.field_3913;
               var10000.field_3905 *= 0.35F;
               var10000 = mc.field_1724.field_3913;
               var10000.field_3907 *= 0.35F;
            } else {
               var10000 = mc.field_1724.field_3913;
               var10000.field_3905 *= 0.5F;
               var10000 = mc.field_1724.field_3913;
               var10000.field_3907 *= 0.5F;
            }
         } else if (mc.field_1724.field_3913.field_3905 != 0.0F && mc.field_1724.field_3913.field_3907 != 0.0F) {
            mult = 0.47F;
         } else {
            mult = 0.67F;
         }

         var10000 = mc.field_1724.field_3913;
         var10000.field_3905 *= mult;
         var10000 = mc.field_1724.field_3913;
         var10000.field_3907 *= mult;
      }

   }

   public boolean canNoSlow() {
      if (this.mode.getValue() == NoSlow.Mode.Matrix3) {
         return false;
      } else if (!(Boolean)this.food.getValue() && mc.field_1724.method_6030().method_57353().method_57832(class_9334.field_50075)) {
         return false;
      } else if (!(Boolean)this.shield.getValue() && mc.field_1724.method_6030().method_7909() == class_1802.field_8255) {
         return false;
      } else if (!(Boolean)this.projectiles.getValue() && (mc.field_1724.method_6030().method_7909() == class_1802.field_8399 || mc.field_1724.method_6030().method_7909() == class_1802.field_8102 || mc.field_1724.method_6030().method_7909() == class_1802.field_8547)) {
         return false;
      } else if (this.mode.getValue() == NoSlow.Mode.MusteryGrief && mc.field_1724.method_24828() && !mc.field_1690.field_1903.method_1434()) {
         return false;
      } else if (!(Boolean)this.mainHand.getValue() && mc.field_1724.method_6058() == class_1268.field_5808) {
         return false;
      } else {
         return !mc.field_1724.method_6079().method_57353().method_57832(class_9334.field_50075) && mc.field_1724.method_6079().method_7909() != class_1802.field_8255 || this.mode.getValue() != NoSlow.Mode.GrimNew && this.mode.getValue() != NoSlow.Mode.Grim || mc.field_1724.method_6058() != class_1268.field_5808;
      }
   }

   public static enum Mode {
      NCP,
      StrictNCP,
      Matrix,
      Grim,
      MusteryGrief,
      GrimNew,
      Matrix2,
      LFCraft,
      Matrix3;

      // $FF: synthetic method
      private static NoSlow.Mode[] $values() {
         return new NoSlow.Mode[]{NCP, StrictNCP, Matrix, Grim, MusteryGrief, GrimNew, Matrix2, LFCraft, Matrix3};
      }
   }
}
