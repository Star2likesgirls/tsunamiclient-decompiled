package tsunami.features.modules.combat;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_124;
import net.minecraft.class_1268;
import net.minecraft.class_1657;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_243;
import net.minecraft.class_2879;
import net.minecraft.class_3965;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tsunami.core.Managers;
import tsunami.core.manager.client.ModuleManager;
import tsunami.events.impl.EventPostSync;
import tsunami.events.impl.EventSync;
import tsunami.events.impl.EventTick;
import tsunami.features.modules.Module;
import tsunami.features.modules.client.ClientSettings;
import tsunami.features.modules.client.HudEditor;
import tsunami.gui.notification.Notification;
import tsunami.setting.Setting;
import tsunami.setting.impl.BooleanSettingGroup;
import tsunami.setting.impl.ColorSetting;
import tsunami.utility.Timer;
import tsunami.utility.math.MathUtility;
import tsunami.utility.player.InteractionUtility;
import tsunami.utility.player.InventoryUtility;
import tsunami.utility.player.SearchInvResult;
import tsunami.utility.render.BlockAnimationUtility;
import tsunami.utility.world.ExplosionUtility;

public class AutoCrystalBase extends Module {
   private final Setting<AutoCrystalBase.TargetLogic> targetLogic;
   private final Setting<Integer> range;
   private final Setting<Float> minDamageDelta;
   private final Setting<Integer> placeDelay;
   private final Setting<Integer> calcDelay;
   private final Setting<InteractionUtility.Interact> interact;
   private final Setting<Boolean> rotate;
   private final Setting<Boolean> notification;
   private final Setting<Boolean> disableNoObby;
   private final Setting<BooleanSettingGroup> render;
   private final Setting<BlockAnimationUtility.BlockRenderMode> renderMode;
   private final Setting<BlockAnimationUtility.BlockAnimationMode> animationMode;
   private final Setting<ColorSetting> renderFillColor;
   private final Setting<ColorSetting> renderLineColor;
   private final Setting<Integer> renderLineWidth;
   private class_1657 target;
   private AutoCrystalBase.ObbyData bestData;
   private final Timer placeTimer;
   private final Timer calcTimer;

   public AutoCrystalBase() {
      super("AutoCrystalBase", Module.Category.NONE);
      this.targetLogic = new Setting("TargetLogic", AutoCrystalBase.TargetLogic.Distance);
      this.range = new Setting("Range", 5, 1, 7);
      this.minDamageDelta = new Setting("MinDamageDelta", 5.0F, 1.0F, 20.0F);
      this.placeDelay = new Setting("PlaceDelay", 300, 0, 3000);
      this.calcDelay = new Setting("CalcDelay", 150, 0, 3000);
      this.interact = new Setting("Interact", InteractionUtility.Interact.Strict);
      this.rotate = new Setting("Rotate", true);
      this.notification = new Setting("Notification", true);
      this.disableNoObby = new Setting("DisableNoObby", false);
      this.render = new Setting("Render", new BooleanSettingGroup(true));
      this.renderMode = (new Setting("RenderMode", BlockAnimationUtility.BlockRenderMode.All)).addToGroup(this.render);
      this.animationMode = (new Setting("AnimationMode", BlockAnimationUtility.BlockAnimationMode.Fade)).addToGroup(this.render);
      this.renderFillColor = (new Setting("RenderFillColor", new ColorSetting(HudEditor.getColor(0)))).addToGroup(this.render);
      this.renderLineColor = (new Setting("RenderLineColor", new ColorSetting(HudEditor.getColor(0)))).addToGroup(this.render);
      this.renderLineWidth = (new Setting("RenderLineWidth", 2, 1, 5)).addToGroup(this.render);
      this.placeTimer = new Timer();
      this.calcTimer = new Timer();
   }

   @EventHandler
   public void onTick(EventTick e) {
      if (mc.field_1724 != null && mc.field_1687 != null) {
         if (!InventoryUtility.findBlockInHotBar(class_2246.field_10540).found() && (Boolean)this.disableNoObby.getValue()) {
            this.disable(ClientSettings.isRu() ? "Нет обсидиана!" : "No obsidian!");
         } else {
            switch(((AutoCrystalBase.TargetLogic)this.targetLogic.getValue()).ordinal()) {
            case 0:
               this.target = Managers.COMBAT.getNearestTarget(15.0F);
               break;
            case 1:
               this.target = Managers.COMBAT.getTargetByHealth(15.0F);
               break;
            case 2:
               this.target = Managers.COMBAT.getTargetByFOV(15.0F);
            }

            if (this.target == null || !this.target.method_29504() && !(this.target.method_6032() < 0.0F)) {
               if (this.calcTimer.every((long)(Integer)this.calcDelay.getValue())) {
                  Managers.ASYNC.run(() -> {
                     this.calcPosition((float)(Integer)this.range.getValue(), mc.field_1724.method_19538());
                  });
               }

            } else {
               this.target = null;
            }
         }
      }
   }

   @EventHandler
   public void onSync(EventSync e) {
      if ((Boolean)this.rotate.getValue() && this.bestData != null && this.isWorth()) {
         float[] angle = InteractionUtility.calculateAngle(this.bestData.bhr().method_17784());
         mc.field_1724.method_36456(angle[0]);
         mc.field_1724.method_36457(angle[1]);
      }

   }

   @EventHandler
   public void onPostSync(EventPostSync e) {
      SearchInvResult obbyResult = InventoryUtility.findBlockInHotBar(class_2246.field_10540);
      if (this.placeTimer.every((long)(Integer)this.placeDelay.getValue()) && this.bestData != null && obbyResult.found() && this.isWorth()) {
         InventoryUtility.saveSlot();
         obbyResult.switchTo();
         mc.field_1761.method_2896(mc.field_1724, class_1268.field_5808, this.bestData.bhr());
         this.sendPacket(new class_2879(class_1268.field_5808));
         if (((BooleanSettingGroup)this.render.getValue()).isEnabled()) {
            BlockAnimationUtility.renderBlock(this.bestData.position(), ((ColorSetting)this.renderLineColor.getValue()).getColorObject(), (Integer)this.renderLineWidth.getValue(), ((ColorSetting)this.renderFillColor.getValue()).getColorObject(), (BlockAnimationUtility.BlockAnimationMode)this.animationMode.getValue(), (BlockAnimationUtility.BlockRenderMode)this.renderMode.getValue());
         }

         if ((Boolean)this.notification.getValue()) {
            String var10000;
            String content;
            if (ClientSettings.isRu()) {
               var10000 = String.valueOf(class_124.field_1080);
               content = "Ставлю на" + var10000 + " X:" + this.bestData.position().method_10263() + " Y:" + this.bestData.position().method_10264() + " Z:" + this.bestData.position().method_10260() + String.valueOf(class_124.field_1068) + " урон возрастет на " + String.valueOf(class_124.field_1061) + MathUtility.round2((double)(this.bestData.damage - ModuleManager.autoCrystal.renderDamage));
            } else {
               var10000 = String.valueOf(class_124.field_1080);
               content = "Placing obby on" + var10000 + " X:" + this.bestData.position().method_10263() + " Y:" + this.bestData.position().method_10264() + " Z:" + this.bestData.position().method_10260() + String.valueOf(class_124.field_1068) + " damage will increase by " + String.valueOf(class_124.field_1061) + MathUtility.round2((double)(this.bestData.damage - ModuleManager.autoCrystal.renderDamage));
            }

            Managers.NOTIFICATION.publicity("AutoCrystalBase", content, 2, Notification.Type.INFO);
         }

         InventoryUtility.returnSlot();
      }

   }

   public boolean isWorth() {
      return ModuleManager.autoCrystal.isEnabled() && this.bestData != null && ModuleManager.autoCrystal.renderDamage + (Float)this.minDamageDelta.getValue() < this.bestData.damage;
   }

   public void calcPosition(float range, class_243 center) {
      if (mc.field_1724 != null && mc.field_1687 != null) {
         if (this.target == null) {
            this.bestData = null;
         } else {
            List<AutoCrystalBase.ObbyData> list = this.getPossibleBlocks(this.target, center, range).stream().filter((data) -> {
               return ModuleManager.autoCrystal.isSafe(data.damage, data.selfDamage, data.overrideDamage);
            }).toList();
            if (list.isEmpty()) {
               this.bestData = null;
            } else {
               this.bestData = this.filterPositions(list);
            }

         }
      }
   }

   @NotNull
   private List<AutoCrystalBase.ObbyData> getPossibleBlocks(class_1657 target, class_243 center, float range) {
      List<AutoCrystalBase.ObbyData> blocks = new ArrayList();
      class_2338 playerPos = class_2338.method_49638(center);

      for(int x = (int)Math.floor((double)((float)playerPos.method_10263() - range)); (double)x <= Math.ceil((double)((float)playerPos.method_10263() + range)); ++x) {
         for(int y = (int)Math.floor((double)((float)playerPos.method_10264() - range)); (double)y <= Math.ceil((double)((float)playerPos.method_10264() + range)); ++y) {
            for(int z = (int)Math.floor((double)((float)playerPos.method_10260() - range)); (double)z <= Math.ceil((double)((float)playerPos.method_10260() + range)); ++z) {
               class_2338 bp = new class_2338(x, y, z);
               if (mc.field_1687.method_22347(bp)) {
                  class_3965 placeResult = InteractionUtility.getPlaceResult(bp, (InteractionUtility.Interact)this.interact.getValue(), false);
                  if (placeResult != null) {
                     AutoCrystal.PlaceData data = this.getPlaceData(bp, target);
                     if (data != null) {
                        blocks.add(new AutoCrystalBase.ObbyData(bp, placeResult, data.damage(), data.selfDamage(), data.overrideDamage()));
                     }
                  }
               }
            }
         }
      }

      return blocks;
   }

   private AutoCrystalBase.ObbyData filterPositions(@NotNull List<AutoCrystalBase.ObbyData> clearedList) {
      AutoCrystalBase.ObbyData bestData = null;
      float bestVal = 0.0F;
      Iterator var4 = clearedList.iterator();

      while(true) {
         while(true) {
            AutoCrystalBase.ObbyData data;
            do {
               if (!var4.hasNext()) {
                  return bestData;
               }

               data = (AutoCrystalBase.ObbyData)var4.next();
            } while(!ModuleManager.autoCrystal.shouldOverrideMinDmg(data.damage) && !(data.damage > (Float)ModuleManager.autoCrystal.minDamage.getValue()));

            if (bestData != null && Math.abs(bestData.damage - data.damage) < 1.0F) {
               if (bestData.selfDamage >= data.selfDamage) {
                  bestData = data;
                  bestVal = data.damage;
               }
            } else if (bestVal < data.damage) {
               bestData = data;
               bestVal = data.damage;
            }
         }
      }
   }

   @Nullable
   public AutoCrystal.PlaceData getPlaceData(class_2338 bp, class_1657 target) {
      if (mc.field_1724 != null && mc.field_1687 != null) {
         if (!mc.field_1687.method_22347(bp.method_10084())) {
            return null;
         } else if (ModuleManager.autoCrystal.isPositionBlockedByEntity(bp, true)) {
            return null;
         } else {
            class_243 crystalVec = new class_243((double)(0.5F + (float)bp.method_10263()), (double)(1.0F + (float)bp.method_10264()), (double)(0.5F + (float)bp.method_10260()));
            float damage = target == null ? 10.0F : ExplosionUtility.getDamageOfGhostBlock(crystalVec, target, bp);
            float selfDamage = ExplosionUtility.getDamageOfGhostBlock(crystalVec, mc.field_1724, bp);
            boolean overrideDamage = ModuleManager.autoCrystal.shouldOverrideMaxSelfDmg(damage, selfDamage);
            if ((Boolean)ModuleManager.autoCrystal.protectFriends.getValue()) {
               List<class_1657> players = Lists.newArrayList(mc.field_1687.method_18456());
               Iterator var8 = players.iterator();

               while(var8.hasNext()) {
                  class_1657 pl = (class_1657)var8.next();
                  if (Managers.FRIEND.isFriend(pl)) {
                     float fdamage = ExplosionUtility.getDamageOfGhostBlock(crystalVec, target, bp);
                     if (fdamage > selfDamage) {
                        selfDamage = fdamage;
                     }
                  }
               }
            }

            if (damage < 1.5F) {
               return null;
            } else if (selfDamage > (Float)ModuleManager.autoCrystal.maxSelfDamage.getValue() && !overrideDamage) {
               return null;
            } else {
               class_3965 interactResult = ModuleManager.autoCrystal.getInteractResult(bp, crystalVec);
               return interactResult == null ? null : new AutoCrystal.PlaceData(interactResult, damage, selfDamage, overrideDamage);
            }
         }
      } else {
         return null;
      }
   }

   private static enum TargetLogic {
      Distance,
      HP,
      FOV;

      // $FF: synthetic method
      private static AutoCrystalBase.TargetLogic[] $values() {
         return new AutoCrystalBase.TargetLogic[]{Distance, HP, FOV};
      }
   }

   private static record ObbyData(class_2338 position, class_3965 bhr, float damage, float selfDamage, boolean overrideDamage) {
      private ObbyData(class_2338 position, class_3965 bhr, float damage, float selfDamage, boolean overrideDamage) {
         this.position = position;
         this.bhr = bhr;
         this.damage = damage;
         this.selfDamage = selfDamage;
         this.overrideDamage = overrideDamage;
      }

      public class_2338 position() {
         return this.position;
      }

      public class_3965 bhr() {
         return this.bhr;
      }

      public float damage() {
         return this.damage;
      }

      public float selfDamage() {
         return this.selfDamage;
      }

      public boolean overrideDamage() {
         return this.overrideDamage;
      }
   }
}
