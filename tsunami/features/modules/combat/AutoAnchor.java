package tsunami.features.modules.combat;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_1268;
import net.minecraft.class_1657;
import net.minecraft.class_1713;
import net.minecraft.class_1747;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_2680;
import net.minecraft.class_2815;
import net.minecraft.class_2848;
import net.minecraft.class_2885;
import net.minecraft.class_3532;
import net.minecraft.class_3675;
import net.minecraft.class_3959;
import net.minecraft.class_3965;
import net.minecraft.class_4587;
import net.minecraft.class_4969;
import net.minecraft.class_239.class_240;
import net.minecraft.class_2828.class_2830;
import net.minecraft.class_2848.class_2849;
import net.minecraft.class_3959.class_242;
import net.minecraft.class_3959.class_3960;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tsunami.core.Managers;
import tsunami.core.manager.client.ModuleManager;
import tsunami.core.manager.player.CombatManager;
import tsunami.events.impl.EventPostSync;
import tsunami.events.impl.EventSetBlockState;
import tsunami.events.impl.EventSync;
import tsunami.events.impl.EventTick;
import tsunami.events.impl.PlayerUpdateEvent;
import tsunami.features.modules.Module;
import tsunami.features.modules.client.HudEditor;
import tsunami.setting.Setting;
import tsunami.setting.impl.Bind;
import tsunami.setting.impl.BooleanSettingGroup;
import tsunami.setting.impl.ColorSetting;
import tsunami.utility.Timer;
import tsunami.utility.math.MathUtility;
import tsunami.utility.player.InteractionUtility;
import tsunami.utility.player.InventoryUtility;
import tsunami.utility.player.PlayerUtility;
import tsunami.utility.player.SearchInvResult;
import tsunami.utility.render.Render2DEngine;
import tsunami.utility.render.Render3DEngine;
import tsunami.utility.world.ExplosionUtility;

public final class AutoAnchor extends Module {
   private static final Setting<AutoAnchor.Pages> page;
   private final Setting<AutoAnchor.AK47> ak47;
   private final Setting<AutoAnchor.Timing> timing;
   private final Setting<Boolean> rotate;
   private final Setting<BooleanSettingGroup> yawStep;
   private final Setting<Float> yawAngle;
   private final Setting<CombatManager.TargetBy> targetLogic;
   private final Setting<Float> targetRange;
   public static final Setting<Integer> selfPredictTicks;
   public final Setting<Boolean> useOptimizedCalc;
   private final Setting<InteractionUtility.Interact> interact;
   private final Setting<Integer> placeDelay;
   private final Setting<Integer> lowPlaceDelay;
   private final Setting<Float> placeRange;
   private final Setting<Float> placeWallRange;
   public static final Setting<Integer> predictTicks;
   private final Setting<Integer> explodeDelay;
   private final Setting<Integer> lowExplodeDelay;
   private final Setting<Boolean> mining;
   private final Setting<Boolean> eating;
   private final Setting<Boolean> aura;
   private final Setting<Boolean> pistonAura;
   private final Setting<Boolean> surround;
   private final Setting<Boolean> middleClick;
   private final Setting<Float> pauseHP;
   private final Setting<BooleanSettingGroup> switchPause;
   private final Setting<Integer> switchDelay;
   public final Setting<AutoAnchor.Sort> sort;
   public final Setting<Float> minDamage;
   public final Setting<Float> maxSelfDamage;
   private final Setting<AutoAnchor.Safety> safety;
   private final Setting<Float> safetyBalance;
   public final Setting<Boolean> protectFriends;
   private final Setting<Boolean> overrideSelfDamage;
   private final Setting<Float> lethalMultiplier;
   private final Setting<BooleanSettingGroup> armorBreaker;
   private final Setting<Float> armorScale;
   private final Setting<Float> facePlaceHp;
   private final Setting<Bind> facePlaceButton;
   private final Setting<AutoAnchor.Switch> autoSwitch;
   private final Setting<AutoAnchor.Switch> antiWeakness;
   private final Setting<Boolean> render;
   private final Setting<AutoAnchor.Render> renderMode;
   private final Setting<Boolean> rselfDamage;
   private final Setting<Boolean> drawDamage;
   private final Setting<ColorSetting> fillColor;
   private final Setting<ColorSetting> lineColor;
   private final Setting<Integer> lineWidth;
   private final Setting<Integer> slideDelay;
   private final Setting<ColorSetting> textColor;
   private final Setting<Boolean> targetName;
   private final Setting<Boolean> currentSide;
   private final Setting<Boolean> speed;
   private final Setting<Boolean> confirmInfo;
   private final Setting<Boolean> calcInfo;
   public static class_1657 target;
   private AutoAnchor.PlaceData bestPosition;
   private class_3965 bestAnchor;
   private final Timer placeTimer;
   private final Timer breakTimer;
   private final Timer pauseTimer;
   private final Map<class_2338, Long> placedAnchors;
   public float renderDamage;
   public float renderSelfDamage;
   public float rotationYaw;
   public float rotationPitch;
   private int prevAnchorAmmount;
   private int anchorSpeed;
   private int invTimer;
   private boolean rotated;
   private boolean facePlacing;
   private long confirmTime;
   private long calcTime;
   private class_2338 renderPos;
   private class_2338 prevRenderPos;
   private long renderMultiplier;
   private final Map<class_2338, Long> renderPositions;

   public AutoAnchor() {
      super("AutoAnchor", Module.Category.NONE);
      this.ak47 = new Setting("AK47", AutoAnchor.AK47.ON, (v) -> {
         return page.getValue() == AutoAnchor.Pages.Main;
      });
      this.timing = new Setting("Timing", AutoAnchor.Timing.NORMAL, (v) -> {
         return page.getValue() == AutoAnchor.Pages.Main;
      });
      this.rotate = new Setting("Rotate", true, (v) -> {
         return page.getValue() == AutoAnchor.Pages.Main;
      });
      this.yawStep = new Setting("YawStep", new BooleanSettingGroup(false), (v) -> {
         return (Boolean)this.rotate.getValue() && page.getValue() == AutoAnchor.Pages.Main;
      });
      this.yawAngle = (new Setting("YawAngle", 180.0F, 1.0F, 180.0F, (v) -> {
         return (Boolean)this.rotate.getValue() && page.getValue() == AutoAnchor.Pages.Main;
      })).addToGroup(this.yawStep);
      this.targetLogic = new Setting("TargetLogic", CombatManager.TargetBy.Distance, (v) -> {
         return page.getValue() == AutoAnchor.Pages.Main;
      });
      this.targetRange = new Setting("TargetRange", 10.0F, 1.0F, 15.0F, (v) -> {
         return page.getValue() == AutoAnchor.Pages.Main;
      });
      this.useOptimizedCalc = new Setting("UseOptimizedCalc", true, (v) -> {
         return page.getValue() == AutoAnchor.Pages.Main;
      });
      this.interact = new Setting("Interact", InteractionUtility.Interact.Vanilla, (v) -> {
         return page.getValue() == AutoAnchor.Pages.Place;
      });
      this.placeDelay = new Setting("PlaceDelay", 0, 0, 1000, (v) -> {
         return page.getValue() == AutoAnchor.Pages.Place;
      });
      this.lowPlaceDelay = new Setting("LowPlaceDelay", 550, 0, 1000, (v) -> {
         return page.getValue() == AutoAnchor.Pages.Place;
      });
      this.placeRange = new Setting("PlaceRange", 5.0F, 1.0F, 6.0F, (v) -> {
         return page.getValue() == AutoAnchor.Pages.Place;
      });
      this.placeWallRange = new Setting("PlaceWallRange", 3.5F, 1.0F, 6.0F, (v) -> {
         return page.getValue() == AutoAnchor.Pages.Place;
      });
      this.explodeDelay = new Setting("ExplodeDelay", 0, 0, 1000, (v) -> {
         return page.getValue() == AutoAnchor.Pages.Break;
      });
      this.lowExplodeDelay = new Setting("LowExplodeDelay", 550, 0, 1000, (v) -> {
         return page.getValue() == AutoAnchor.Pages.Break;
      });
      this.mining = new Setting("Mining", true, (v) -> {
         return page.getValue() == AutoAnchor.Pages.Pause;
      });
      this.eating = new Setting("Eating", true, (v) -> {
         return page.getValue() == AutoAnchor.Pages.Pause;
      });
      this.aura = new Setting("Aura", false, (v) -> {
         return page.getValue() == AutoAnchor.Pages.Pause;
      });
      this.pistonAura = new Setting("PistonAura", true, (v) -> {
         return page.getValue() == AutoAnchor.Pages.Pause;
      });
      this.surround = new Setting("Surround", true, (v) -> {
         return page.getValue() == AutoAnchor.Pages.Pause;
      });
      this.middleClick = new Setting("MiddleClick", true, (v) -> {
         return page.getValue() == AutoAnchor.Pages.Pause;
      });
      this.pauseHP = new Setting("HP", 8.0F, 2.0F, 10.0F, (v) -> {
         return page.getValue() == AutoAnchor.Pages.Pause;
      });
      this.switchPause = new Setting("SwitchPause", new BooleanSettingGroup(true), (v) -> {
         return page.getValue() == AutoAnchor.Pages.Pause;
      });
      this.switchDelay = (new Setting("SwitchDelay", 100, 0, 1000, (v) -> {
         return page.getValue() == AutoAnchor.Pages.Pause;
      })).addToGroup(this.switchPause);
      this.sort = new Setting("Sort", AutoAnchor.Sort.DAMAGE, (v) -> {
         return page.getValue() == AutoAnchor.Pages.Damages;
      });
      this.minDamage = new Setting("MinDamage", 6.0F, 2.0F, 20.0F, (v) -> {
         return page.getValue() == AutoAnchor.Pages.Damages;
      });
      this.maxSelfDamage = new Setting("MaxSelfDamage", 10.0F, 2.0F, 20.0F, (v) -> {
         return page.getValue() == AutoAnchor.Pages.Damages;
      });
      this.safety = new Setting("Safety", AutoAnchor.Safety.NONE, (v) -> {
         return page.getValue() == AutoAnchor.Pages.Damages;
      });
      this.safetyBalance = new Setting("SafetyBalance", 1.1F, 0.1F, 3.0F, (v) -> {
         return page.getValue() == AutoAnchor.Pages.Damages && this.safety.getValue() == AutoAnchor.Safety.BALANCE;
      });
      this.protectFriends = new Setting("ProtectFriends", true, (v) -> {
         return page.getValue() == AutoAnchor.Pages.Damages;
      });
      this.overrideSelfDamage = new Setting("OverrideSelfDamage", true, (v) -> {
         return page.getValue() == AutoAnchor.Pages.Damages;
      });
      this.lethalMultiplier = new Setting("LethalMultiplier", 1.0F, 0.0F, 5.0F, (v) -> {
         return page.getValue() == AutoAnchor.Pages.Damages;
      });
      this.armorBreaker = new Setting("ArmorBreaker", new BooleanSettingGroup(true), (v) -> {
         return page.getValue() == AutoAnchor.Pages.Damages;
      });
      this.armorScale = (new Setting("Armor %", 5.0F, 0.0F, 40.0F, (v) -> {
         return page.getValue() == AutoAnchor.Pages.Damages;
      })).addToGroup(this.armorBreaker);
      this.facePlaceHp = new Setting("FacePlaceHp", 5.0F, 0.0F, 20.0F, (v) -> {
         return page.getValue() == AutoAnchor.Pages.Damages;
      });
      this.facePlaceButton = new Setting("FacePlaceBtn", new Bind(340, false, false), (v) -> {
         return page.getValue() == AutoAnchor.Pages.Damages;
      });
      this.autoSwitch = new Setting("Switch", AutoAnchor.Switch.NORMAL, (v) -> {
         return page.getValue() == AutoAnchor.Pages.Switch;
      });
      this.antiWeakness = new Setting("AntiWeakness", AutoAnchor.Switch.SILENT, (v) -> {
         return page.getValue() == AutoAnchor.Pages.Switch;
      });
      this.render = new Setting("Render", true, (v) -> {
         return page.getValue() == AutoAnchor.Pages.Render;
      });
      this.renderMode = new Setting("RenderMode", AutoAnchor.Render.Fade, (v) -> {
         return page.getValue() == AutoAnchor.Pages.Render;
      });
      this.rselfDamage = new Setting("SelfDamage", true, (v) -> {
         return page.getValue() == AutoAnchor.Pages.Render;
      });
      this.drawDamage = new Setting("RenderDamage", true, (v) -> {
         return page.getValue() == AutoAnchor.Pages.Render;
      });
      this.fillColor = new Setting("Block Fill Color", new ColorSetting(HudEditor.getColor(0)), (v) -> {
         return page.getValue() == AutoAnchor.Pages.Render;
      });
      this.lineColor = new Setting("Block Line Color", new ColorSetting(HudEditor.getColor(0)), (v) -> {
         return page.getValue() == AutoAnchor.Pages.Render;
      });
      this.lineWidth = new Setting("Block Line Width", 2, 1, 10, (v) -> {
         return page.getValue() == AutoAnchor.Pages.Render;
      });
      this.slideDelay = new Setting("Slide Delay", 200, 1, 1000, (v) -> {
         return page.getValue() == AutoAnchor.Pages.Render;
      });
      this.textColor = new Setting("Text Color", new ColorSetting(Color.WHITE), (v) -> {
         return page.getValue() == AutoAnchor.Pages.Render;
      });
      this.targetName = new Setting("TargetName", false, (v) -> {
         return page.getValue() == AutoAnchor.Pages.Info;
      });
      this.currentSide = new Setting("CurrentSide", true, (v) -> {
         return page.getValue() == AutoAnchor.Pages.Info;
      });
      this.speed = new Setting("Speed", true, (v) -> {
         return page.getValue() == AutoAnchor.Pages.Info;
      });
      this.confirmInfo = new Setting("ConfirmTime", true, (v) -> {
         return page.getValue() == AutoAnchor.Pages.Info;
      });
      this.calcInfo = new Setting("CalcInfo", false, (v) -> {
         return page.getValue() == AutoAnchor.Pages.Info;
      });
      this.placeTimer = new Timer();
      this.breakTimer = new Timer();
      this.pauseTimer = new Timer();
      this.placedAnchors = new HashMap();
      this.renderPositions = new ConcurrentHashMap();
   }

   public void onEnable() {
      this.resetVars();
   }

   public void onDisable() {
      this.resetVars();
   }

   private void resetVars() {
      this.facePlacing = false;
      this.rotated = false;
      this.renderDamage = 0.0F;
      this.renderSelfDamage = 0.0F;
      this.renderMultiplier = 0L;
      this.confirmTime = 0L;
      this.renderPositions.clear();
      this.placedAnchors.clear();
      this.breakTimer.reset();
      this.placeTimer.reset();
      this.bestAnchor = null;
      this.bestPosition = null;
      this.renderPos = null;
      this.prevRenderPos = null;
      target = null;
   }

   @EventHandler(
      priority = 200
   )
   public void onTick(EventTick e) {
      if (mc.field_1724 != null && mc.field_1687 != null) {
         long currentTime = System.currentTimeMillis();
         List<AutoAnchor.PlaceData> blocks = this.getPossibleBlocks(target, mc.field_1724.method_19538(), (Float)this.placeRange.getValue());
         this.calcPosition(blocks);
         this.getAnchorToExplode(blocks);
         this.calcTime = System.currentTimeMillis() - currentTime;
         if (this.timing.is(AutoAnchor.Timing.NORMAL)) {
            if (this.bestPosition != null && this.placeTimer.passedMs((long)this.facePlacing ? (Integer)this.lowPlaceDelay.getValue() : (Integer)this.placeDelay.getValue())) {
               this.placeAnchor(this.bestPosition, false);
            }

            if (this.bestAnchor != null && this.breakTimer.passedMs((long)this.facePlacing ? (Integer)this.lowExplodeDelay.getValue() : (Integer)this.explodeDelay.getValue())) {
               this.explodeAnchor(this.bestAnchor);
            }
         }

      }
   }

   @EventHandler
   public void onPlayerUpdate(PlayerUpdateEvent event) {
      if ((Boolean)this.rotate.getValue()) {
         this.calcRotations();
      }

   }

   @EventHandler
   public void onSync(EventSync e) {
      if (mc.field_1724 != null && mc.field_1687 != null) {
         target = Managers.COMBAT.getTarget((Float)this.targetRange.getValue(), (CombatManager.TargetBy)this.targetLogic.getValue());
         if (target == null || !target.method_29504() && !(target.method_6032() < 0.0F)) {
            if (this.invTimer++ >= 20) {
               this.anchorSpeed = MathUtility.clamp(this.prevAnchorAmmount - InventoryUtility.getItemCount(class_1802.field_23141), 0, 255);
               this.prevAnchorAmmount = InventoryUtility.getItemCount(class_1802.field_23141);
               this.invTimer = 0;
            }

            if ((Boolean)this.rotate.getValue() && mc.field_1724 != null && this.rotationYaw != mc.field_1724.method_36454() && this.rotationPitch != mc.field_1724.method_36455()) {
               mc.field_1724.method_36456(this.rotationYaw);
               mc.field_1724.method_36457(this.rotationPitch);
            }

         } else {
            target = null;
         }
      }
   }

   @EventHandler
   public void onPostSync(EventPostSync e) {
      if (this.timing.is(AutoAnchor.Timing.SEQUENTIAL)) {
         if (this.bestPosition != null && this.placeTimer.passedMs((long)this.facePlacing ? (Integer)this.lowPlaceDelay.getValue() : (Integer)this.placeDelay.getValue())) {
            this.placeAnchor(this.bestPosition, false);
         }

         if (this.bestAnchor != null && this.breakTimer.passedMs((long)this.facePlacing ? (Integer)this.lowExplodeDelay.getValue() : (Integer)this.explodeDelay.getValue())) {
            this.explodeAnchor(this.bestAnchor);
         }
      }

   }

   public String getDisplayInfo() {
      StringBuilder info = new StringBuilder();
      class_2350 side = this.bestPosition == null ? null : this.bestPosition.bhr().method_17780();
      if (side != null) {
         if ((Boolean)this.targetName.getValue() && target != null) {
            info.append(target.method_5477().getString()).append(" | ");
         }

         if ((Boolean)this.speed.getValue()) {
            info.append(this.anchorSpeed).append(" c/s").append(" | ");
         }

         if ((Boolean)this.currentSide.getValue()) {
            info.append(side.toString().toUpperCase()).append(" | ");
         }

         if ((Boolean)this.confirmInfo.getValue()) {
            info.append("c: ").append(this.confirmTime).append(" | ");
         }

         if ((Boolean)this.calcInfo.getValue()) {
            info.append("calc: ").append(this.calcTime).append(" | ");
         }
      }

      return info.length() < 4 ? info.toString() : info.substring(0, info.length() - 3);
   }

   @EventHandler
   public void onBlockDestruct(EventSetBlockState e) {
      if (mc.field_1724 != null && mc.field_1687 != null) {
         if (e.getPrevState() != null && e.getState() != null) {
            if (target != null && target.method_5707(e.getPos().method_46558()) <= 4.0D && e.getState().method_26204() instanceof class_4969 && e.getPrevState().method_45474()) {
               this.debug("Detected change of state " + String.valueOf(e.getPos()) + ", exploding...");
            }

         }
      }
   }

   public void calcRotations() {
      if ((Boolean)this.rotate.getValue() && !this.shouldPause() && (this.bestPosition != null || this.bestAnchor != null) && mc.field_1724 != null) {
         class_243 vec = this.bestPosition == null ? this.bestAnchor.method_17784() : this.bestPosition.bhr().method_17784();
         float yawDelta = class_3532.method_15393((float)class_3532.method_15338(Math.toDegrees(Math.atan2(vec.field_1350 - mc.field_1724.method_23321(), vec.field_1352 - mc.field_1724.method_23317())) - 90.0D) - this.rotationYaw);
         float pitchDelta = (float)(-Math.toDegrees(Math.atan2(vec.field_1351 - (mc.field_1724.method_19538().field_1351 + (double)mc.field_1724.method_18381(mc.field_1724.method_18376())), Math.sqrt(Math.pow(vec.field_1352 - mc.field_1724.method_23317(), 2.0D) + Math.pow(vec.field_1350 - mc.field_1724.method_23321(), 2.0D))))) - this.rotationPitch;
         yawDelta = (float)((double)yawDelta + Render2DEngine.interpolate(-1.2000000476837158D, 1.2000000476837158D, Math.sin((double)(mc.field_1724.field_6012 % 80))) + (double)MathUtility.random(-1.2F, 1.2F));
         pitchDelta += MathUtility.random(-0.8F, 0.8F);
         if (yawDelta > 180.0F) {
            yawDelta -= 180.0F;
         }

         float yawStepVal = 180.0F;
         if (((BooleanSettingGroup)this.yawStep.getValue()).isEnabled()) {
            yawStepVal = (Float)this.yawAngle.getValue();
         }

         float clampedYawDelta = class_3532.method_15363(class_3532.method_15379(yawDelta), -yawStepVal, yawStepVal);
         float clampedPitchDelta = class_3532.method_15363(pitchDelta, -45.0F, 45.0F);
         this.rotated = class_3532.method_15379(yawDelta) <= yawStepVal || !((BooleanSettingGroup)this.yawStep.getValue()).isEnabled();
         float newYaw = this.rotationYaw + (yawDelta > 0.0F ? clampedYawDelta : -clampedYawDelta);
         float newPitch = class_3532.method_15363(this.rotationPitch + clampedPitchDelta, -90.0F, 90.0F);
         double gcdFix = Math.pow((Double)mc.field_1690.method_42495().method_41753() * 0.6D + 0.2D, 3.0D) * 1.2D;
         this.rotationYaw = (float)((double)newYaw - (double)(newYaw - this.rotationYaw) % gcdFix);
         this.rotationPitch = (float)((double)newPitch - (double)(newPitch - this.rotationPitch) % gcdFix);
         ModuleManager.rotations.fixRotation = this.rotationYaw;
      } else {
         this.rotationYaw = mc.field_1724.method_36454();
         this.rotationPitch = mc.field_1724.method_36455();
      }

   }

   public void onRender3D(class_4587 stack) {
      if ((Boolean)this.render.getValue()) {
         Object2ObjectMap<class_2338, Long> cache = new Object2ObjectOpenHashMap(this.renderPositions);
         cache.forEach((pos, time) -> {
            if (System.currentTimeMillis() - time > 500L) {
               this.renderPositions.remove(pos);
            }

         });
         float var10000 = MathUtility.round2((double)this.renderDamage);
         String dmg = var10000 + ((Boolean)this.rselfDamage.getValue() ? " / " + MathUtility.round2((double)this.renderSelfDamage) : "");
         if (this.renderMode.getValue() == AutoAnchor.Render.Fade) {
            cache.forEach((pos, time) -> {
               if (System.currentTimeMillis() - time < 500L) {
                  int alpha = (int)(100.0F * (1.0F - (float)(System.currentTimeMillis() - time) / 500.0F));
                  Render3DEngine.FILLED_QUEUE.add(new Render3DEngine.FillAction(new class_238(pos), Render2DEngine.injectAlpha(((ColorSetting)this.fillColor.getValue()).getColorObject(), alpha)));
                  Render3DEngine.OUTLINE_QUEUE.add(new Render3DEngine.OutlineAction(new class_238(pos), Render2DEngine.injectAlpha(((ColorSetting)this.lineColor.getValue()).getColorObject(), alpha), (float)(Integer)this.lineWidth.getValue()));
                  if ((Boolean)this.drawDamage.getValue()) {
                     Render3DEngine.drawTextIn3D(dmg, pos.method_46558(), 0.0D, 0.1D, 0.0D, Render2DEngine.applyOpacity(((ColorSetting)this.textColor.getValue()).getColorObject(), (float)alpha / 100.0F));
                  }
               }

            });
         } else if (this.renderMode.getValue() == AutoAnchor.Render.Slide && this.renderPos != null) {
            if (this.prevRenderPos == null) {
               this.prevRenderPos = this.renderPos;
            }

            if (this.renderPositions.isEmpty()) {
               return;
            }

            float mult = MathUtility.clamp((float)(System.currentTimeMillis() - this.renderMultiplier) / (float)(Integer)this.slideDelay.getValue(), 0.0F, 1.0F);
            class_238 interpolatedBox = Render3DEngine.interpolateBox(new class_238(this.prevRenderPos), new class_238(this.renderPos), mult);
            this.renderBox(dmg, interpolatedBox);
         } else if (this.renderPos != null) {
            class_238 box = new class_238(this.renderPos);
            if (this.renderPositions.isEmpty()) {
               return;
            }

            this.renderBox(dmg, box);
         }
      }

   }

   private void renderBox(String dmg, class_238 box) {
      Render3DEngine.FILLED_QUEUE.add(new Render3DEngine.FillAction(box, ((ColorSetting)this.fillColor.getValue()).getColorObject()));
      Render3DEngine.OUTLINE_QUEUE.add(new Render3DEngine.OutlineAction(box, ((ColorSetting)this.lineColor.getValue()).getColorObject(), (float)(Integer)this.lineWidth.getValue()));
      if ((Boolean)this.drawDamage.getValue()) {
         Render3DEngine.drawTextIn3D(dmg, box.method_1005(), 0.0D, 0.1D, 0.0D, ((ColorSetting)this.textColor.getValue()).getColorObject());
      }

   }

   public boolean shouldPause() {
      if (mc.field_1724 != null && mc.field_1687 != null && mc.field_1761 != null) {
         boolean offhand = mc.field_1724.method_6079().method_7909() == class_1802.field_23141;
         boolean mainHand = mc.field_1724.method_6047().method_7909() == class_1802.field_23141;
         boolean offhandGlow = mc.field_1724.method_6079().method_7909() == class_1802.field_8801;
         boolean mainHandGlow = mc.field_1724.method_6047().method_7909() == class_1802.field_8801;
         if (!this.pauseTimer.passedMs(1000L)) {
            return true;
         } else if (mc.field_1761.method_2923() && !offhand && (Boolean)this.mining.getValue()) {
            return true;
         } else if (this.autoSwitch.is(AutoAnchor.Switch.NONE) && !offhand && !mainHand) {
            return true;
         } else if ((this.autoSwitch.is(AutoAnchor.Switch.SILENT) || this.autoSwitch.is(AutoAnchor.Switch.NORMAL)) && !InventoryUtility.findItemInHotBar(class_1802.field_23141).found() && !offhand) {
            return true;
         } else if (this.autoSwitch.is(AutoAnchor.Switch.INVENTORY) && !InventoryUtility.findItemInInventory(class_1802.field_23141).found() && !offhand) {
            return true;
         } else if (this.autoSwitch.is(AutoAnchor.Switch.NONE) && !offhandGlow && !mainHandGlow) {
            return true;
         } else if ((this.autoSwitch.is(AutoAnchor.Switch.SILENT) || this.autoSwitch.is(AutoAnchor.Switch.NORMAL)) && !InventoryUtility.findItemInHotBar(class_1802.field_8801).found() && !offhandGlow) {
            return true;
         } else if (this.autoSwitch.is(AutoAnchor.Switch.INVENTORY) && !InventoryUtility.findItemInInventory(class_1802.field_8801).found() && !offhandGlow) {
            return true;
         } else if (mc.field_1724.method_6115() && (Boolean)this.eating.getValue()) {
            return true;
         } else if (this.rotationMarkedDirty()) {
            return true;
         } else if (mc.field_1724.method_6032() + mc.field_1724.method_6067() < (Float)this.pauseHP.getValue()) {
            return true;
         } else {
            boolean silentWeakness = this.antiWeakness.getValue() == AutoAnchor.Switch.SILENT || this.antiWeakness.getValue() == AutoAnchor.Switch.INVENTORY;
            boolean silent = this.autoSwitch.getValue() == AutoAnchor.Switch.SILENT || this.autoSwitch.getValue() == AutoAnchor.Switch.INVENTORY;
            return ((BooleanSettingGroup)this.switchPause.getValue()).isEnabled() && !Managers.PLAYER.switchTimer.passedMs((long)(Integer)this.switchDelay.getValue()) && !silent && !silentWeakness;
         }
      } else {
         return true;
      }
   }

   public boolean rotationMarkedDirty() {
      if (ModuleManager.surround.isEnabled() && !ModuleManager.surround.inactivityTimer.passedMs(500L) && (Boolean)this.surround.getValue()) {
         return true;
      } else if (ModuleManager.middleClick.isEnabled() && mc.field_1690.field_1871.method_1434() && (Boolean)this.middleClick.getValue()) {
         return true;
      } else if (ModuleManager.autoTrap.isEnabled() && !ModuleManager.surround.inactivityTimer.passedMs(500L)) {
         return true;
      } else if (ModuleManager.blocker.isEnabled() && !ModuleManager.surround.inactivityTimer.passedMs(500L)) {
         return true;
      } else if (ModuleManager.holeFill.isEnabled() && !HoleFill.inactivityTimer.passedMs(500L)) {
         return true;
      } else if (ModuleManager.aura.isEnabled() && (Boolean)this.aura.getValue()) {
         return true;
      } else {
         return ModuleManager.pistonAura.isEnabled() && (Boolean)this.pistonAura.getValue();
      }
   }

   public void explodeAnchor(class_3965 bhr) {
      if (!this.shouldPause() && mc.field_1724 != null) {
         int prevSlot = -1;
         SearchInvResult glowResult = InventoryUtility.findItemInHotBar(class_1802.field_8801);
         SearchInvResult anchorResult = InventoryUtility.findInHotBar((stack) -> {
            return !(stack.method_7909() instanceof class_1747);
         });
         SearchInvResult glowInvResult = InventoryUtility.findItemInInventory(class_1802.field_8801);
         boolean offhand = mc.field_1724.method_6079().method_7909() == class_1802.field_8801;
         boolean holdingAnchor = mc.field_1724.method_6047().method_7909() == class_1802.field_8801 || offhand;
         if (!(Boolean)this.rotate.getValue() || this.rotated) {
            if (this.autoSwitch.getValue() != AutoAnchor.Switch.NONE && !holdingAnchor) {
               prevSlot = this.switchTo(glowResult, glowInvResult, this.autoSwitch);
            }

            if (mc.field_1724.method_6047().method_7909() == class_1802.field_8801 || offhand || this.autoSwitch.getValue() == AutoAnchor.Switch.SILENT) {
               if (this.ak47.is(AutoAnchor.AK47.OFF)) {
                  if (mc.field_1724.method_5715()) {
                     this.sendPacket(new class_2848(mc.field_1724, class_2849.field_12984));
                  }

                  if ((Integer)mc.field_1687.method_8320(bhr.method_17777()).method_11654(class_4969.field_23153) == 0) {
                     this.sendSequencedPacket((id) -> {
                        return new class_2885(offhand ? class_1268.field_5810 : class_1268.field_5808, bhr, id);
                     });
                     mc.field_1724.method_6104(offhand ? class_1268.field_5810 : class_1268.field_5808);
                  } else {
                     anchorResult.switchTo();
                     this.sendSequencedPacket((id) -> {
                        return new class_2885(class_1268.field_5808, bhr, id);
                     });
                     mc.field_1724.method_6104(class_1268.field_5808);
                  }
               } else {
                  if (mc.field_1724.method_5715()) {
                     this.sendPacket(new class_2848(mc.field_1724, class_2849.field_12984));
                  }

                  this.sendSequencedPacket((id) -> {
                     return new class_2885(offhand ? class_1268.field_5810 : class_1268.field_5808, bhr, id);
                  });
                  mc.field_1724.method_6104(offhand ? class_1268.field_5810 : class_1268.field_5808);
                  anchorResult.switchTo();
                  this.sendSequencedPacket((id) -> {
                     return new class_2885(class_1268.field_5808, bhr, id);
                  });
                  mc.field_1724.method_6104(class_1268.field_5808);
               }

               this.breakTimer.reset();
               this.postPlaceSwitch(prevSlot);
            }
         }
      }
   }

   private int switchTo(SearchInvResult result, SearchInvResult resultInv, @NotNull Setting<AutoAnchor.Switch> switchMode) {
      if (mc.field_1724 != null && mc.field_1687 != null && mc.field_1761 != null) {
         int prevSlot = mc.field_1724.method_31548().field_7545;
         switch(((AutoAnchor.Switch)switchMode.getValue()).ordinal()) {
         case 0:
         default:
            break;
         case 1:
            result.switchTo();
            break;
         case 2:
            result.switchToSilent();
            break;
         case 3:
            if (resultInv.found()) {
               prevSlot = resultInv.slot();
               mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, prevSlot, mc.field_1724.method_31548().field_7545, class_1713.field_7791, mc.field_1724);
               this.sendPacket(new class_2815(mc.field_1724.field_7512.field_7763));
            }
         }

         return prevSlot;
      } else {
         return -1;
      }
   }

   public void placeAnchor(AutoAnchor.PlaceData data, boolean instant) {
      if (!this.shouldPause() && mc.field_1724 != null && !data.anchor()) {
         int prevSlot = -1;
         SearchInvResult anchorResult = InventoryUtility.findItemInHotBar(class_1802.field_23141);
         SearchInvResult anchorInvResult = InventoryUtility.findItemInInventory(class_1802.field_23141);
         boolean offhand = mc.field_1724.method_6079().method_7909() == class_1802.field_23141;
         boolean holdingAnchor = mc.field_1724.method_6047().method_7909() == class_1802.field_23141 || offhand;
         if ((Boolean)this.rotate.getValue()) {
            if (instant) {
               float[] angle = InteractionUtility.calculateAngle(data.bhr().method_17784());
               this.sendPacket(new class_2830(mc.field_1724.method_23317(), mc.field_1724.method_23318(), mc.field_1724.method_23321(), angle[0], angle[1], mc.field_1724.method_24828()));
            } else if (!this.rotated) {
               return;
            }
         }

         if (this.autoSwitch.getValue() != AutoAnchor.Switch.NONE && !holdingAnchor) {
            prevSlot = this.switchTo(anchorResult, anchorInvResult, this.autoSwitch);
         }

         if (mc.field_1724.method_6047().method_7909() == class_1802.field_23141 || offhand || this.autoSwitch.getValue() == AutoAnchor.Switch.SILENT) {
            this.sendSequencedPacket((id) -> {
               return new class_2885(offhand ? class_1268.field_5810 : class_1268.field_5808, data.bhr(), id);
            });
            mc.field_1724.method_6104(offhand ? class_1268.field_5810 : class_1268.field_5808);
            this.placeTimer.reset();
            if (!data.bp().equals(this.renderPos)) {
               this.renderMultiplier = System.currentTimeMillis();
               this.prevRenderPos = this.renderPos;
               this.renderPos = data.bp();
            }

            this.renderPositions.put(data.bp(), System.currentTimeMillis());
            if (this.ak47.is(AutoAnchor.AK47.ON)) {
               class_3965 placeResult = this.getInteractResult(data.bp);
               if (placeResult != null) {
                  this.explodeAnchor(placeResult);
               }
            }

            this.postPlaceSwitch(prevSlot);
         }
      }
   }

   private void postPlaceSwitch(int slot) {
      if (mc.field_1724 != null && mc.field_1687 != null && mc.field_1761 != null) {
         if (this.autoSwitch.getValue() == AutoAnchor.Switch.SILENT) {
            InventoryUtility.switchTo(slot);
         }

         if (this.autoSwitch.getValue() == AutoAnchor.Switch.INVENTORY && slot != -1) {
            mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, slot, mc.field_1724.method_31548().field_7545, class_1713.field_7791, mc.field_1724);
            this.sendPacket(new class_2815(mc.field_1724.field_7512.field_7763));
         }

      }
   }

   public void calcPosition(List<AutoAnchor.PlaceData> blocks) {
      if (mc.field_1724 != null && mc.field_1687 != null) {
         if (target == null) {
            this.renderPos = null;
            this.prevRenderPos = null;
            this.bestPosition = null;
         } else {
            List<AutoAnchor.PlaceData> list = blocks.stream().filter((data) -> {
               return this.isSafe(data.damage, data.selfDamage, data.overrideDamage);
            }).toList();
            this.bestPosition = list.isEmpty() ? null : this.filterPositions(list);
         }
      }
   }

   @NotNull
   private List<AutoAnchor.PlaceData> getPossibleBlocks(class_1657 target, class_243 center, float range) {
      List<AutoAnchor.PlaceData> blocks = new ArrayList();
      class_2338 playerPos = class_2338.method_49638(center);

      for(int x = (int)Math.floor((double)((float)playerPos.method_10263() - range)); (double)x <= Math.ceil((double)((float)playerPos.method_10263() + range)); ++x) {
         for(int y = (int)Math.floor((double)((float)playerPos.method_10264() - range)); (double)y <= Math.ceil((double)((float)playerPos.method_10264() + range)); ++y) {
            for(int z = (int)Math.floor((double)((float)playerPos.method_10260() - range)); (double)z <= Math.ceil((double)((float)playerPos.method_10260() + range)); ++z) {
               AutoAnchor.PlaceData data = this.getPlaceData(new class_2338(x, y, z), target);
               if (data != null) {
                  blocks.add(data);
               }
            }
         }
      }

      return blocks;
   }

   public void getAnchorToExplode(List<AutoAnchor.PlaceData> blocks) {
      if (target == null) {
         this.bestAnchor = null;
      } else {
         List<AutoAnchor.PlaceData> list = blocks.stream().filter((data) -> {
            return this.isSafe(data.damage, data.selfDamage, data.overrideDamage) && data.anchor();
         }).toList();
         this.bestAnchor = list.isEmpty() ? null : this.filterAnchors(list);
      }
   }

   public boolean isSafe(float damage, float selfDamage, boolean overrideDamage) {
      if (mc.field_1724 != null && mc.field_1687 != null) {
         if (overrideDamage) {
            return true;
         } else if ((double)selfDamage + 0.5D > (double)(mc.field_1724.method_6032() + mc.field_1724.method_6067())) {
            return false;
         } else if (this.safety.getValue() == AutoAnchor.Safety.STABLE) {
            return damage - selfDamage > 0.0F;
         } else if (this.safety.getValue() == AutoAnchor.Safety.BALANCE) {
            return damage * (Float)this.safetyBalance.getValue() - selfDamage > 0.0F;
         } else {
            return true;
         }
      } else {
         return false;
      }
   }

   @Nullable
   private AutoAnchor.PlaceData filterPositions(@NotNull List<AutoAnchor.PlaceData> clearedList) {
      AutoAnchor.PlaceData bestData = null;
      float bestVal = 0.0F;
      Iterator var4 = clearedList.iterator();

      while(true) {
         AutoAnchor.PlaceData data;
         do {
            if (!var4.hasNext()) {
               if (bestData == null) {
                  return null;
               }

               this.facePlacing = bestData.damage < (Float)this.minDamage.getValue();
               this.renderDamage = bestData.damage;
               this.renderSelfDamage = bestData.selfDamage;
               return bestData;
            }

            data = (AutoAnchor.PlaceData)var4.next();
         } while(!this.shouldOverride(data.damage) && !(data.damage > (Float)this.minDamage.getValue()));

         if (this.sort.getValue() == AutoAnchor.Sort.DAMAGE) {
            if (bestVal < data.damage) {
               bestData = data;
               bestVal = data.damage;
            }
         } else if (bestVal < data.damage / data.selfDamage) {
            bestData = data;
            bestVal = data.damage / data.selfDamage;
         }
      }
   }

   public boolean shouldOverride(float damage) {
      if (target == null) {
         return false;
      } else {
         boolean override = target.method_6032() + target.method_6067() <= (Float)this.facePlaceHp.getValue();
         if (((BooleanSettingGroup)this.armorBreaker.getValue()).isEnabled()) {
            Iterator var3 = target.method_5661().iterator();

            while(var3.hasNext()) {
               class_1799 armor = (class_1799)var3.next();
               if (armor != null && !armor.method_7909().equals(class_1802.field_8162) && (float)(armor.method_7936() - armor.method_7919()) / (float)armor.method_7936() * 100.0F < (Float)this.armorScale.getValue()) {
                  override = true;
                  break;
               }
            }
         }

         if (((Bind)this.facePlaceButton.getValue()).getKey() != -1 && class_3675.method_15987(mc.method_22683().method_4490(), ((Bind)this.facePlaceButton.getValue()).getKey())) {
            override = true;
         }

         if ((double)(target.method_6032() + target.method_6067() - damage * (Float)this.lethalMultiplier.getValue()) < 0.5D) {
            override = true;
         }

         return override;
      }
   }

   @Nullable
   private class_3965 filterAnchors(@NotNull List<AutoAnchor.PlaceData> clearedList) {
      AutoAnchor.PlaceData bestData = null;
      float bestVal = 0.0F;
      Iterator var4 = clearedList.iterator();

      while(true) {
         AutoAnchor.PlaceData data;
         do {
            if (!var4.hasNext()) {
               if (bestData == null) {
                  return null;
               }

               this.renderDamage = bestData.damage;
               this.renderSelfDamage = bestData.selfDamage;
               return bestData.bhr;
            }

            data = (AutoAnchor.PlaceData)var4.next();
         } while(!this.shouldOverride(data.damage) && !(data.damage > (Float)this.minDamage.getValue()));

         if (this.sort.getValue() == AutoAnchor.Sort.DAMAGE) {
            if (bestVal < data.damage) {
               bestData = data;
               bestVal = data.damage;
            }
         } else if (bestVal < data.damage / data.selfDamage) {
            bestData = data;
            bestVal = data.damage / data.selfDamage;
         }
      }
   }

   @Nullable
   public AutoAnchor.PlaceData getPlaceData(class_2338 bp, class_1657 target) {
      if (mc.field_1724 != null && mc.field_1687 != null) {
         if (target != null && target.method_19538().method_1025(bp.method_46558()) > 144.0D) {
            return null;
         } else {
            class_2680 state = mc.field_1687.method_8320(bp);
            boolean isAnchor = state.method_26204() instanceof class_4969;
            if (!state.method_45474() && !isAnchor) {
               return null;
            } else {
               if (isAnchor) {
                  mc.field_1687.method_8501(bp, class_2246.field_10124.method_9564());
               }

               class_3965 placeResult = InteractionUtility.getPlaceResult(bp, (InteractionUtility.Interact)this.interact.getValue(), false);
               if (placeResult == null) {
                  if (isAnchor) {
                     mc.field_1687.method_8501(bp, state);
                  }

                  return null;
               } else {
                  float damage = target == null ? 10.0F : ExplosionUtility.getAutoCrystalDamage(bp.method_46558(), target, (Integer)predictTicks.getValue(), (Boolean)this.useOptimizedCalc.getValue());
                  if (damage < 1.5F) {
                     if (isAnchor) {
                        mc.field_1687.method_8501(bp, state);
                     }

                     return null;
                  } else {
                     float selfDamage = ExplosionUtility.getAutoCrystalDamage(bp.method_46558(), mc.field_1724, (Integer)selfPredictTicks.getValue(), (Boolean)this.useOptimizedCalc.getValue());
                     boolean overrideDamage = this.shouldOverrideDamage(damage, selfDamage);
                     if ((Boolean)this.protectFriends.getValue()) {
                        List<class_1657> players = Lists.newArrayList(mc.field_1687.method_18456());
                        Iterator var10 = players.iterator();

                        while(var10.hasNext()) {
                           class_1657 pl = (class_1657)var10.next();
                           if (Managers.FRIEND.isFriend(pl)) {
                              float fdamage = ExplosionUtility.getAutoCrystalDamage(bp.method_46558(), pl, (Integer)selfPredictTicks.getValue(), (Boolean)this.useOptimizedCalc.getValue());
                              if (fdamage > selfDamage) {
                                 selfDamage = fdamage;
                              }
                           }
                        }
                     }

                     if (isAnchor) {
                        mc.field_1687.method_8501(bp, state);
                        placeResult = this.getInteractResult(bp);
                     }

                     if (placeResult == null) {
                        return null;
                     } else {
                        return selfDamage > (Float)this.maxSelfDamage.getValue() && !overrideDamage ? null : new AutoAnchor.PlaceData(placeResult, bp, damage, selfDamage, overrideDamage, isAnchor);
                     }
                  }
               }
            }
         }
      } else {
         return null;
      }
   }

   public boolean shouldOverrideDamage(float damage, float selfDamage) {
      if ((Boolean)this.overrideSelfDamage.getValue() && target != null) {
         if (mc.field_1724 != null && mc.field_1687 != null) {
            boolean targetSafe = target.method_6079().method_7909() == class_1802.field_8288 || target.method_6047().method_7909() == class_1802.field_8288;
            boolean playerSafe = mc.field_1724.method_6079().method_7909() == class_1802.field_8288 || mc.field_1724.method_6047().method_7909() == class_1802.field_8288;
            float targetHp = target.method_6032() + target.method_6067() - 1.0F;
            float playerHp = mc.field_1724.method_6032() + mc.field_1724.method_6067() - 1.0F;
            boolean canPop = damage > targetHp && targetSafe;
            boolean canKill = damage > targetHp && !targetSafe;
            boolean canPopSelf = selfDamage > playerHp && playerSafe;
            boolean canKillSelf = selfDamage > playerHp && !playerSafe;
            if (canPopSelf && canKill) {
               return true;
            } else {
               return selfDamage > (Float)this.maxSelfDamage.getValue() && (canPop || canKill) && !canKillSelf && !canPopSelf;
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public class_3965 getInteractResult(class_2338 bp) {
      class_3965 interactResult = null;
      switch((InteractionUtility.Interact)this.interact.getValue()) {
      case Vanilla:
      case AirPlace:
         interactResult = this.getDefaultInteract(bp);
         break;
      case Strict:
         interactResult = this.getStrictInteract(bp);
         break;
      case Legit:
         interactResult = this.getLegitInteract(bp);
      }

      return interactResult;
   }

   @Nullable
   private class_3965 getDefaultInteract(class_2338 bp) {
      if (mc.field_1724 != null && mc.field_1687 != null) {
         class_243 vec = bp.method_46558().method_1031(0.0D, -0.5D, 0.0D);
         if (PlayerUtility.squaredDistanceFromEyes(vec) > this.placeRange.getPow2Value()) {
            return null;
         } else {
            class_3965 wallCheck = mc.field_1687.method_17742(new class_3959(InteractionUtility.getEyesPos(mc.field_1724), vec, class_3960.field_17558, class_242.field_1348, mc.field_1724));
            return wallCheck != null && wallCheck.method_17783() == class_240.field_1332 && wallCheck.method_17777() != bp && PlayerUtility.squaredDistanceFromEyes(vec) > this.placeWallRange.getPow2Value() ? null : new class_3965(vec, mc.field_1687.method_24794(bp.method_10084()) ? class_2350.field_11036 : class_2350.field_11033, bp, false);
         }
      } else {
         return null;
      }
   }

   public class_3965 getStrictInteract(@NotNull class_2338 bp) {
      if (mc.field_1724 != null && mc.field_1687 != null) {
         float bestDistance = Float.MAX_VALUE;
         class_2350 bestDirection = null;
         class_243 bestVector = null;
         float upPoint = (float)bp.method_10084().method_10264();
         if (mc.field_1724.method_33571().method_10214() > (double)upPoint) {
            bestDirection = class_2350.field_11036;
            bestVector = new class_243((double)bp.method_10263() + 0.5D, (double)(bp.method_10264() + 1), (double)bp.method_10260() + 0.5D);
         } else if (mc.field_1724.method_33571().method_10214() < (double)bp.method_10264() && mc.field_1687.method_22347(bp.method_10074())) {
            bestDirection = class_2350.field_11033;
            bestVector = new class_243((double)bp.method_10263() + 0.5D, (double)bp.method_10264(), (double)bp.method_10260() + 0.5D);
         } else {
            Iterator var6 = InteractionUtility.getStrictBlockDirections(bp).iterator();

            while(var6.hasNext()) {
               class_2350 dir = (class_2350)var6.next();
               if (dir != class_2350.field_11036 && dir != class_2350.field_11033) {
                  class_243 directionVec = new class_243((double)bp.method_10263() + 0.5D + (double)dir.method_10163().method_10263() * 0.5D, (double)bp.method_10264() + 0.9D, (double)bp.method_10260() + 0.5D + (double)dir.method_10163().method_10260() * 0.5D);
                  if (mc.field_1687.method_22347(bp.method_10093(dir))) {
                     float distance = PlayerUtility.squaredDistanceFromEyes(directionVec);
                     if (bestDistance > distance) {
                        bestDirection = dir;
                        bestVector = directionVec;
                        bestDistance = distance;
                     }
                  }
               }
            }
         }

         if (bestVector == null) {
            return null;
         } else if (PlayerUtility.squaredDistanceFromEyes(bestVector) > this.placeRange.getPow2Value()) {
            return null;
         } else {
            class_3965 wallCheck = mc.field_1687.method_17742(new class_3959(InteractionUtility.getEyesPos(mc.field_1724), bestVector, class_3960.field_17558, class_242.field_1348, mc.field_1724));
            return wallCheck != null && wallCheck.method_17783() == class_240.field_1332 && wallCheck.method_17777() != bp && PlayerUtility.squaredDistanceFromEyes(bestVector) > this.placeWallRange.getPow2Value() ? null : new class_3965(bestVector, bestDirection, bp, false);
         }
      } else {
         return null;
      }
   }

   public class_3965 getLegitInteract(class_2338 bp) {
      if (mc.field_1724 != null && mc.field_1687 != null) {
         float bestDistance = Float.MAX_VALUE;
         class_3965 bestResult = null;

         for(float x = 0.0F; x <= 1.0F; x += 0.2F) {
            for(float y = 0.0F; y <= 1.0F; y += 0.2F) {
               for(float z = 0.0F; z <= 1.0F; z += 0.2F) {
                  class_243 point = new class_243((double)((float)bp.method_10263() + x), (double)((float)bp.method_10264() + y), (double)((float)bp.method_10260() + z));
                  float distance = PlayerUtility.squaredDistanceFromEyes(point);
                  class_3965 wallCheck = mc.field_1687.method_17742(new class_3959(InteractionUtility.getEyesPos(mc.field_1724), point, class_3960.field_17558, class_242.field_1348, mc.field_1724));
                  if (wallCheck == null || wallCheck.method_17783() != class_240.field_1332 || wallCheck.method_17777() == bp || !(distance > this.placeWallRange.getPow2Value())) {
                     class_3965 result = ExplosionUtility.rayCastBlock(new class_3959(InteractionUtility.getEyesPos(mc.field_1724), point, class_3960.field_17558, class_242.field_1348, mc.field_1724), bp);
                     if (!(distance > this.placeRange.getPow2Value()) && distance < bestDistance && result != null && result.method_17783() == class_240.field_1332) {
                        bestResult = result;
                        bestDistance = distance;
                     }
                  }
               }
            }
         }

         return bestResult;
      } else {
         return null;
      }
   }

   public void pause() {
      this.pauseTimer.reset();
   }

   static {
      page = new Setting("Page", AutoAnchor.Pages.Main);
      selfPredictTicks = new Setting("SelfPredictTicks", 3, 0, 20, (v) -> {
         return page.getValue() == AutoAnchor.Pages.Main;
      });
      predictTicks = new Setting("PredictTicks", 3, 0, 20, (v) -> {
         return page.getValue() == AutoAnchor.Pages.Place;
      });
   }

   public static enum AK47 {
      OFF,
      ON,
      SEMI;

      // $FF: synthetic method
      private static AutoAnchor.AK47[] $values() {
         return new AutoAnchor.AK47[]{OFF, ON, SEMI};
      }
   }

   private static enum Timing {
      NORMAL,
      SEQUENTIAL;

      // $FF: synthetic method
      private static AutoAnchor.Timing[] $values() {
         return new AutoAnchor.Timing[]{NORMAL, SEQUENTIAL};
      }
   }

   public static enum Sort {
      SAFE,
      DAMAGE;

      // $FF: synthetic method
      private static AutoAnchor.Sort[] $values() {
         return new AutoAnchor.Sort[]{SAFE, DAMAGE};
      }
   }

   public static enum Safety {
      BALANCE,
      STABLE,
      NONE;

      // $FF: synthetic method
      private static AutoAnchor.Safety[] $values() {
         return new AutoAnchor.Safety[]{BALANCE, STABLE, NONE};
      }
   }

   private static enum Switch {
      NONE,
      NORMAL,
      SILENT,
      INVENTORY;

      // $FF: synthetic method
      private static AutoAnchor.Switch[] $values() {
         return new AutoAnchor.Switch[]{NONE, NORMAL, SILENT, INVENTORY};
      }
   }

   public static enum Render {
      Fade,
      Slide,
      Default;

      // $FF: synthetic method
      private static AutoAnchor.Render[] $values() {
         return new AutoAnchor.Render[]{Fade, Slide, Default};
      }
   }

   public static record PlaceData(class_3965 bhr, class_2338 bp, float damage, float selfDamage, boolean overrideDamage, boolean anchor) {
      public PlaceData(class_3965 bhr, class_2338 bp, float damage, float selfDamage, boolean overrideDamage, boolean anchor) {
         this.bhr = bhr;
         this.bp = bp;
         this.damage = damage;
         this.selfDamage = selfDamage;
         this.overrideDamage = overrideDamage;
         this.anchor = anchor;
      }

      public class_3965 bhr() {
         return this.bhr;
      }

      public class_2338 bp() {
         return this.bp;
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

      public boolean anchor() {
         return this.anchor;
      }
   }

   private static enum Pages {
      Place,
      Break,
      Pause,
      Render,
      Damages,
      Main,
      Switch,
      Info;

      // $FF: synthetic method
      private static AutoAnchor.Pages[] $values() {
         return new AutoAnchor.Pages[]{Place, Break, Pause, Render, Damages, Main, Switch, Info};
      }
   }
}
