package tsunami.features.modules.combat;

import java.util.Iterator;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_1268;
import net.minecraft.class_1291;
import net.minecraft.class_1293;
import net.minecraft.class_1294;
import net.minecraft.class_1799;
import net.minecraft.class_1828;
import net.minecraft.class_1844;
import net.minecraft.class_2868;
import net.minecraft.class_2886;
import net.minecraft.class_6880;
import net.minecraft.class_9334;
import tsunami.core.manager.client.ModuleManager;
import tsunami.events.impl.EventAfterRotate;
import tsunami.events.impl.EventPostSync;
import tsunami.features.modules.Module;
import tsunami.setting.Setting;
import tsunami.setting.impl.BooleanSettingGroup;
import tsunami.utility.Timer;

public final class AutoBuff extends Module {
   private final Setting<Boolean> strength = new Setting("Strength", true);
   private final Setting<Boolean> speed = new Setting("Speed", true);
   private final Setting<Boolean> fire = new Setting("FireResistance", true);
   private final Setting<BooleanSettingGroup> heal = new Setting("InstantHealing", new BooleanSettingGroup(true));
   private final Setting<Integer> healthH;
   private final Setting<BooleanSettingGroup> regen;
   private final Setting<AutoBuff.TriggerOn> triggerOn;
   private final Setting<Integer> healthR;
   private final Setting<Boolean> onDaGround;
   private final Setting<Boolean> pauseAura;
   public Timer timer;
   private boolean spoofed;

   public AutoBuff() {
      super("AutoBuff", Module.Category.NONE);
      this.healthH = (new Setting("Health", 8, 0, 20)).addToGroup(this.heal);
      this.regen = new Setting("Regeneration", new BooleanSettingGroup(true));
      this.triggerOn = (new Setting("Trigger", AutoBuff.TriggerOn.LackOfRegen)).addToGroup(this.regen);
      this.healthR = (new Setting("HP", 8, 0, 20, (v) -> {
         return this.triggerOn.is(AutoBuff.TriggerOn.Health);
      })).addToGroup(this.regen);
      this.onDaGround = new Setting("OnlyOnGround", true);
      this.pauseAura = new Setting("PauseAura", false);
      this.timer = new Timer();
      this.spoofed = false;
   }

   public static int getPotionSlot(AutoBuff.Potions potion) {
      for(int i = 0; i < 9; ++i) {
         if (isStackPotion(mc.field_1724.method_31548().method_5438(i), potion)) {
            return i;
         }
      }

      return -1;
   }

   public static boolean isPotionOnHotBar(AutoBuff.Potions potions) {
      return getPotionSlot(potions) != -1;
   }

   public static boolean isStackPotion(class_1799 stack, AutoBuff.Potions potion) {
      if (stack == null) {
         return false;
      } else {
         if (stack.method_7909() instanceof class_1828) {
            class_1844 potionContentsComponent = (class_1844)stack.method_57825(class_9334.field_49651, class_1844.field_49274);
            class_6880<class_1291> id = null;
            switch(potion.ordinal()) {
            case 0:
               id = class_1294.field_5910;
               break;
            case 1:
               id = class_1294.field_5904;
               break;
            case 2:
               id = class_1294.field_5918;
               break;
            case 3:
               id = class_1294.field_5915;
               break;
            case 4:
               id = class_1294.field_5924;
            }

            Iterator var4 = potionContentsComponent.method_57397().iterator();

            while(var4.hasNext()) {
               class_1293 effect = (class_1293)var4.next();
               if (effect.method_5579() == id) {
                  return true;
               }
            }
         }

         return false;
      }
   }

   @EventHandler
   public void onPostRotationSet(EventAfterRotate event) {
      if (Aura.target == null || !(mc.field_1724.method_7261(1.0F) > 0.5F)) {
         if (mc.field_1724.field_6012 > 80 && this.shouldThrow()) {
            mc.field_1724.method_36457(90.0F);
            this.spoofed = true;
         }

      }
   }

   private boolean shouldThrow() {
      return !mc.field_1724.method_6059(class_1294.field_5904) && isPotionOnHotBar(AutoBuff.Potions.SPEED) && (Boolean)this.speed.getValue() || !mc.field_1724.method_6059(class_1294.field_5910) && isPotionOnHotBar(AutoBuff.Potions.STRENGTH) && (Boolean)this.strength.getValue() || !mc.field_1724.method_6059(class_1294.field_5918) && isPotionOnHotBar(AutoBuff.Potions.FIRERES) && (Boolean)this.fire.getValue() || mc.field_1724.method_6032() + mc.field_1724.method_6067() < (float)(Integer)this.healthH.getValue() && isPotionOnHotBar(AutoBuff.Potions.HEAL) && ((BooleanSettingGroup)this.heal.getValue()).isEnabled() || !mc.field_1724.method_6059(class_1294.field_5924) && this.triggerOn.is(AutoBuff.TriggerOn.LackOfRegen) && isPotionOnHotBar(AutoBuff.Potions.REGEN) && ((BooleanSettingGroup)this.regen.getValue()).isEnabled() || mc.field_1724.method_6032() + mc.field_1724.method_6067() < (float)(Integer)this.healthR.getValue() && this.triggerOn.is(AutoBuff.TriggerOn.Health) && isPotionOnHotBar(AutoBuff.Potions.REGEN) && ((BooleanSettingGroup)this.regen.getValue()).isEnabled();
   }

   @EventHandler
   public void onPostSync(EventPostSync e) {
      if (Aura.target == null || !(mc.field_1724.method_7261(1.0F) > 0.5F)) {
         if (!(Boolean)this.onDaGround.getValue() || mc.field_1724.method_24828()) {
            if (mc.field_1724.field_6012 > 80 && this.shouldThrow() && this.timer.passedMs(1000L) && this.spoofed) {
               if (!mc.field_1724.method_6059(class_1294.field_5904) && isPotionOnHotBar(AutoBuff.Potions.SPEED) && (Boolean)this.speed.getValue()) {
                  this.throwPotion(AutoBuff.Potions.SPEED);
               }

               if (!mc.field_1724.method_6059(class_1294.field_5910) && isPotionOnHotBar(AutoBuff.Potions.STRENGTH) && (Boolean)this.strength.getValue()) {
                  this.throwPotion(AutoBuff.Potions.STRENGTH);
               }

               if (!mc.field_1724.method_6059(class_1294.field_5918) && isPotionOnHotBar(AutoBuff.Potions.FIRERES) && (Boolean)this.fire.getValue()) {
                  this.throwPotion(AutoBuff.Potions.FIRERES);
               }

               if (mc.field_1724.method_6032() + mc.field_1724.method_6067() < (float)(Integer)this.healthH.getValue() && ((BooleanSettingGroup)this.heal.getValue()).isEnabled() && isPotionOnHotBar(AutoBuff.Potions.HEAL)) {
                  this.throwPotion(AutoBuff.Potions.HEAL);
               }

               if ((!mc.field_1724.method_6059(class_1294.field_5924) && this.triggerOn.is(AutoBuff.TriggerOn.LackOfRegen) || mc.field_1724.method_6032() + mc.field_1724.method_6067() < (float)(Integer)this.healthR.getValue() && this.triggerOn.is(AutoBuff.TriggerOn.Health)) && isPotionOnHotBar(AutoBuff.Potions.REGEN) && ((BooleanSettingGroup)this.regen.getValue()).isEnabled()) {
                  this.throwPotion(AutoBuff.Potions.REGEN);
               }

               this.sendPacket(new class_2868(mc.field_1724.method_31548().field_7545));
               this.timer.reset();
               this.spoofed = false;
            }

         }
      }
   }

   public void throwPotion(AutoBuff.Potions potion) {
      if ((Boolean)this.pauseAura.getValue()) {
         ModuleManager.aura.pause();
      }

      this.sendPacket(new class_2868(getPotionSlot(potion)));
      this.sendSequencedPacket((id) -> {
         return new class_2886(class_1268.field_5808, id, mc.field_1724.method_36454(), mc.field_1724.method_36455());
      });
   }

   public static enum TriggerOn {
      LackOfRegen,
      Health;

      // $FF: synthetic method
      private static AutoBuff.TriggerOn[] $values() {
         return new AutoBuff.TriggerOn[]{LackOfRegen, Health};
      }
   }

   public static enum Potions {
      STRENGTH,
      SPEED,
      FIRERES,
      HEAL,
      REGEN;

      // $FF: synthetic method
      private static AutoBuff.Potions[] $values() {
         return new AutoBuff.Potions[]{STRENGTH, SPEED, FIRERES, HEAL, REGEN};
      }
   }
}
