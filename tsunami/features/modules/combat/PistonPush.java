package tsunami.features.modules.combat;

import java.awt.Color;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_1268;
import net.minecraft.class_1657;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_238;
import net.minecraft.class_2868;
import net.minecraft.class_4587;
import net.minecraft.class_2828.class_2831;
import tsunami.core.Managers;
import tsunami.events.impl.EventPostSync;
import tsunami.events.impl.EventSync;
import tsunami.features.modules.Module;
import tsunami.injection.accesors.IClientPlayerEntity;
import tsunami.setting.Setting;
import tsunami.setting.impl.ColorSetting;
import tsunami.setting.impl.SettingGroup;
import tsunami.utility.player.InteractionUtility;
import tsunami.utility.player.InventoryUtility;
import tsunami.utility.player.SearchInvResult;
import tsunami.utility.render.Render2DEngine;
import tsunami.utility.render.Render3DEngine;
import tsunami.utility.world.HoleUtility;

public final class PistonPush extends Module {
   private final Setting<Float> range = new Setting("Target Range", 5.0F, 1.5F, 7.0F);
   private final Setting<Integer> blocksPerTick = new Setting("Blocks/Tick", 2, 1, 2);
   private final Setting<Integer> delayPerPlace = new Setting("Delay/Place", 0, 0, 5);
   private final Setting<InteractionUtility.PlaceMode> placeMode;
   private final Setting<InteractionUtility.Interact> interact;
   private final Setting<Boolean> rotate;
   private final Setting<PistonPush.ChargeType> chargeType;
   private final Setting<PistonPush.PistonType> pistonType;
   private final Setting<Boolean> autoSwap;
   private final Setting<Boolean> swing;
   private final Setting<SettingGroup> render;
   private final Setting<ColorSetting> fillColor;
   private final Setting<ColorSetting> lineColor;
   private final Setting<Integer> lineWidth;
   private class_1657 target;
   private class_2338 pistonPos;
   private class_2338 chargePos;
   private boolean firstPlace;
   private int delay;
   private final ConcurrentHashMap<class_2338, Long> renderPoses;
   private Runnable placeRunnable;

   public PistonPush() {
      super("PistonPush", Module.Category.NONE);
      this.placeMode = new Setting("Place Mode", InteractionUtility.PlaceMode.Normal);
      this.interact = new Setting("Interact", InteractionUtility.Interact.Strict);
      this.rotate = new Setting("Rotate", false);
      this.chargeType = new Setting("Charge Type", PistonPush.ChargeType.All);
      this.pistonType = new Setting("Piston Type", PistonPush.PistonType.All);
      this.autoSwap = new Setting("Auto Swap", true);
      this.swing = new Setting("Swing", true);
      this.render = new Setting("Render", new SettingGroup(false, 0));
      this.fillColor = (new Setting("Fill Color", new ColorSetting(new Color(255, 0, 0, 50)))).addToGroup(this.render);
      this.lineColor = (new Setting("Line Color", new ColorSetting(new Color(255, 0, 0, 200)))).addToGroup(this.render);
      this.lineWidth = (new Setting("Line Width", 2, 1, 5)).addToGroup(this.render);
      this.renderPoses = new ConcurrentHashMap();
      this.placeRunnable = null;
   }

   public void onEnable() {
      this.target = null;
      this.pistonPos = null;
      this.chargePos = null;
      this.placeRunnable = null;
      this.delay = 0;
      this.firstPlace = true;
   }

   @EventHandler
   public void onSync(EventSync event) {
      if (!this.isPlayerTargetCorrect(this.target)) {
         this.findTarget();
      } else if (this.pistonPos != null && this.chargePos != null) {
         if (this.delay < (Integer)this.delayPerPlace.getValue()) {
            ++this.delay;
         } else {
            this.handlePistonPush(false);
         }
      } else {
         this.findPlacePoses();
      }
   }

   @EventHandler
   public void onPostSync(EventPostSync event) {
      for(int blocksPlaced = 0; blocksPlaced < (Integer)this.blocksPerTick.getValue(); ++blocksPlaced) {
         this.handlePistonPush(true);
         if (this.placeRunnable == null) {
            return;
         }

         this.placeRunnable.run();
         this.placeRunnable = null;
      }

      this.delay = 0;
   }

   public void handlePistonPush(boolean onSync) {
      if (this.firstPlace) {
         this.placePiston(onSync);
      } else {
         this.placeCharge(onSync);
      }

   }

   public void onRender3D(class_4587 stack) {
      this.renderPoses.forEach((pos, time) -> {
         if (System.currentTimeMillis() - time > 500L) {
            this.renderPoses.remove(pos);
         } else {
            Render3DEngine.FILLED_QUEUE.add(new Render3DEngine.FillAction(new class_238(pos), Render2DEngine.injectAlpha(((ColorSetting)this.fillColor.getValue()).getColorObject(), (int)((float)((ColorSetting)this.fillColor.getValue()).getAlpha() * (1.0F - (float)(System.currentTimeMillis() - time) / 500.0F)))));
            Render3DEngine.OUTLINE_QUEUE.add(new Render3DEngine.OutlineAction(new class_238(pos), ((ColorSetting)this.lineColor.getValue()).getColorObject(), (float)(Integer)this.lineWidth.getValue()));
         }

      });
   }

   private void placeCharge(boolean onSync) {
      if (this.getChargeSlot().found() && ((Boolean)this.autoSwap.getValue() || this.getChargeSlot().isHolding())) {
         if (this.chargePos != null) {
            if ((Boolean)this.rotate.getValue()) {
               float[] angle = InteractionUtility.getPlaceAngle(this.chargePos, (InteractionUtility.Interact)this.interact.getValue(), false);
               if (angle == null) {
                  return;
               }

               if (onSync) {
                  this.sendPacket(new class_2831(angle[0], angle[1], mc.field_1724.method_24828()));
               } else {
                  mc.field_1724.method_36456(angle[0]);
                  mc.field_1724.method_36457(angle[1]);
               }
            }

            this.placeRunnable = () -> {
               int prevSlot = mc.field_1724.method_31548().field_7545;
               InteractionUtility.placeBlock(this.chargePos, InteractionUtility.Rotate.None, (InteractionUtility.Interact)this.interact.getValue(), (InteractionUtility.PlaceMode)this.placeMode.getValue(), this.getChargeSlot(), true, false);
               this.sendPacket(new class_2868(prevSlot));
               mc.field_1724.method_31548().field_7545 = prevSlot;
               this.firstPlace = true;
               if ((Boolean)this.swing.getValue()) {
                  mc.field_1724.method_6104(class_1268.field_5808);
               }

               this.renderPoses.put(this.chargePos, System.currentTimeMillis());
            };
         }
      }
   }

   private void placePiston(boolean extra) {
      if (this.pistonPos != null) {
         if (this.getPistonSlot().found() && ((Boolean)this.autoSwap.getValue() || this.getPistonSlot().isHolding())) {
            if ((Boolean)this.rotate.getValue()) {
               float[] angle = InteractionUtility.getPlaceAngle(this.pistonPos, (InteractionUtility.Interact)this.interact.getValue(), false);
               if (angle == null) {
                  return;
               }

               if (extra) {
                  this.sendPacket(new class_2831(angle[0], angle[1], mc.field_1724.method_24828()));
               } else {
                  mc.field_1724.method_36456(angle[0]);
                  mc.field_1724.method_36457(angle[1]);
               }
            }

            this.placeRunnable = () -> {
               float angle = InteractionUtility.calculateAngle(this.target.method_33571(), this.pistonPos.method_46558())[0];
               this.sendPacket(new class_2831(angle, 0.0F, mc.field_1724.method_24828()));
               float prevYaw = mc.field_1724.method_36454();
               mc.field_1724.method_36456(angle);
               mc.field_1724.field_5982 = angle;
               ((IClientPlayerEntity)mc.field_1724).setLastYaw(angle);
               int prevSlot = mc.field_1724.method_31548().field_7545;
               InteractionUtility.placeBlock(this.pistonPos, InteractionUtility.Rotate.None, (InteractionUtility.Interact)this.interact.getValue(), (InteractionUtility.PlaceMode)this.placeMode.getValue(), this.getPistonSlot(), true, false);
               this.sendPacket(new class_2868(prevSlot));
               mc.field_1724.method_31548().field_7545 = prevSlot;
               mc.field_1724.method_36456(prevYaw);
               this.firstPlace = false;
               if ((Boolean)this.swing.getValue()) {
                  mc.field_1724.method_6104(class_1268.field_5808);
               }

               this.renderPoses.put(this.pistonPos, System.currentTimeMillis());
            };
         }
      }
   }

   private SearchInvResult getPistonSlot() {
      SearchInvResult stickyPistonSlot = InventoryUtility.findBlockInHotBar(class_2246.field_10615);
      SearchInvResult pistonSlot = InventoryUtility.findBlockInHotBar(class_2246.field_10560);
      SearchInvResult finalResult = null;
      switch(((PistonPush.PistonType)this.pistonType.getValue()).ordinal()) {
      case 0:
         finalResult = stickyPistonSlot;
         break;
      case 1:
         finalResult = pistonSlot;
         break;
      case 2:
         finalResult = pistonSlot.found() ? pistonSlot : stickyPistonSlot;
      }

      return finalResult;
   }

   private SearchInvResult getChargeSlot() {
      SearchInvResult redstoneTorchSlot = InventoryUtility.findBlockInHotBar(class_2246.field_10523);
      SearchInvResult redstoneBlockSlot = InventoryUtility.findBlockInHotBar(class_2246.field_10002);
      SearchInvResult finalResult = null;
      switch(((PistonPush.ChargeType)this.chargeType.getValue()).ordinal()) {
      case 0:
         finalResult = redstoneBlockSlot;
         break;
      case 1:
         finalResult = redstoneTorchSlot;
         break;
      case 2:
         finalResult = redstoneTorchSlot.found() ? redstoneTorchSlot : redstoneBlockSlot;
      }

      return finalResult;
   }

   private void findPlacePoses() {
      class_2338 targetBP = class_2338.method_49638(this.target.method_19538());
      class_2338[] surroundPoses = new class_2338[]{targetBP.method_10069(1, 1, 0), targetBP.method_10069(-1, 1, 0), targetBP.method_10069(0, 1, 1), targetBP.method_10069(0, 1, -1)};
      class_2338[] var3 = surroundPoses;
      int var4 = surroundPoses.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         class_2338 pos = var3[var5];
         if (InteractionUtility.canPlaceBlock(pos, (InteractionUtility.Interact)this.interact.getValue(), false)) {
            class_2338[] chargePoses = new class_2338[]{pos.method_10069(0, 1, 0), pos.method_10069(0, -1, 0), pos.method_10069(1, 0, 0), pos.method_10069(-1, 0, 0), pos.method_10069(0, 0, 1), pos.method_10069(0, 0, -1)};
            class_2338[] var8 = chargePoses;
            int var9 = chargePoses.length;

            for(int var10 = 0; var10 < var9; ++var10) {
               class_2338 chPos = var8[var10];
               if (chPos != targetBP && mc.field_1687.method_8320(chPos).method_45474() && (this.chargeType.getValue() != PistonPush.ChargeType.Torch || !chPos.equals(pos.method_10084())) && InteractionUtility.canPlaceBlock(chPos, (InteractionUtility.Interact)this.interact.getValue(), false)) {
                  this.chargePos = chPos;
                  break;
               }
            }

            this.pistonPos = pos;
         }
      }

   }

   private boolean isPlayerTargetCorrect(class_1657 player) {
      if (player == null) {
         return false;
      } else {
         return !Managers.FRIEND.isFriend(player) && player != mc.field_1724 && player.method_5739(mc.field_1724) <= (Float)this.range.getValue() && !player.method_29504() && player.method_6032() + player.method_6067() > 0.0F && HoleUtility.isHole(player.method_24515());
      }
   }

   private void findTarget() {
      Iterator var1 = Managers.ASYNC.getAsyncPlayers().iterator();

      while(var1.hasNext()) {
         class_1657 player = (class_1657)var1.next();
         if (this.isPlayerTargetCorrect(player)) {
            this.target = player;
            break;
         }
      }

   }

   private static enum ChargeType {
      Block,
      Torch,
      All;

      // $FF: synthetic method
      private static PistonPush.ChargeType[] $values() {
         return new PistonPush.ChargeType[]{Block, Torch, All};
      }
   }

   private static enum PistonType {
      Sticky,
      Normal,
      All;

      // $FF: synthetic method
      private static PistonPush.PistonType[] $values() {
         return new PistonPush.PistonType[]{Sticky, Normal, All};
      }
   }
}
