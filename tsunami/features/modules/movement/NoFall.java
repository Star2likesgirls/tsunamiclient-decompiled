package tsunami.features.modules.movement;

import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_1268;
import net.minecraft.class_1802;
import net.minecraft.class_2338;
import net.minecraft.class_2596;
import net.minecraft.class_2828;
import net.minecraft.class_2828.class_2830;
import net.minecraft.class_2828.class_5911;
import tsunami.events.impl.EventSync;
import tsunami.events.impl.EventTick;
import tsunami.events.impl.PacketEvent;
import tsunami.features.modules.Module;
import tsunami.injection.accesors.IPlayerMoveC2SPacket;
import tsunami.setting.Setting;
import tsunami.utility.player.InteractionUtility;
import tsunami.utility.player.InventoryUtility;
import tsunami.utility.player.SearchInvResult;

public class NoFall extends Module {
   public final Setting<NoFall.Mode> mode;
   public final Setting<NoFall.FallDistance> fallDistance;
   public final Setting<Integer> fallDistanceValue;
   private final Setting<Boolean> powderSnowBucket;
   private final Setting<Boolean> waterBucket;
   private final Setting<Boolean> retrieve;
   private final Setting<Boolean> enderPearl;
   private final Setting<Boolean> cobweb;
   private final Setting<Boolean> twistingVines;
   private tsunami.utility.Timer pearlCooldown;
   private boolean retrieveFlag;
   private boolean cancelGround;

   public NoFall() {
      super("NoFall", Module.Category.NONE);
      this.mode = new Setting("Mode", NoFall.Mode.Rubberband);
      this.fallDistance = new Setting("FallDistance", NoFall.FallDistance.Calc);
      this.fallDistanceValue = new Setting("FallDistanceVal", 10, 2, 100, (v) -> {
         return this.fallDistance.getValue() == NoFall.FallDistance.Custom;
      });
      this.powderSnowBucket = new Setting("PowderSnowBucket", true, (v) -> {
         return this.mode.getValue() == NoFall.Mode.Items;
      });
      this.waterBucket = new Setting("WaterBucket", true, (v) -> {
         return this.mode.getValue() == NoFall.Mode.Items;
      });
      this.retrieve = new Setting("Retrieve", true, (v) -> {
         return this.mode.getValue() == NoFall.Mode.Items && (Boolean)this.waterBucket.getValue();
      });
      this.enderPearl = new Setting("EnderPearl", true, (v) -> {
         return this.mode.getValue() == NoFall.Mode.Items;
      });
      this.cobweb = new Setting("Cobweb", true, (v) -> {
         return this.mode.getValue() == NoFall.Mode.Items;
      });
      this.twistingVines = new Setting("TwistingVines", true, (v) -> {
         return this.mode.getValue() == NoFall.Mode.Items;
      });
      this.pearlCooldown = new tsunami.utility.Timer();
      this.cancelGround = false;
   }

   @EventHandler
   public void onSync(EventSync e) {
      if (!fullNullCheck()) {
         if (this.isFalling()) {
            switch(((NoFall.Mode)this.mode.getValue()).ordinal()) {
            case 0:
               this.sendPacket(new class_5911(true));
               break;
            case 1:
               class_2338 playerPos = class_2338.method_49638(mc.field_1724.method_19538());
               SearchInvResult snowResult = InventoryUtility.findItemInHotBar(class_1802.field_27876);
               SearchInvResult pearlResult = InventoryUtility.findItemInHotBar(class_1802.field_8634);
               SearchInvResult webResult = InventoryUtility.findItemInHotBar(class_1802.field_8786);
               SearchInvResult vinesResult = InventoryUtility.findItemInHotBar(class_1802.field_23070);
               SearchInvResult waterResult = InventoryUtility.findItemInHotBar(class_1802.field_8705);
               if (waterResult.found() && (Boolean)this.waterBucket.getValue()) {
                  mc.field_1724.method_36457(90.0F);
                  this.doWaterDrop(waterResult, playerPos);
               } else if (pearlResult.found() && (Boolean)this.enderPearl.getValue()) {
                  mc.field_1724.method_36457(90.0F);
                  this.doPearlDrop(pearlResult);
               } else if (webResult.found() && (Boolean)this.cobweb.getValue()) {
                  mc.field_1724.method_36457(90.0F);
                  this.doWebDrop(webResult, playerPos);
               } else if (vinesResult.found() && (Boolean)this.twistingVines.getValue()) {
                  mc.field_1724.method_36457(90.0F);
                  this.doVinesDrop(vinesResult, playerPos);
               } else if (snowResult.found() && (Boolean)this.powderSnowBucket.getValue()) {
                  mc.field_1724.method_36457(90.0F);
                  this.doSnowDrop(snowResult, playerPos);
               }
               break;
            case 2:
            case 3:
               this.cancelGround = true;
            }
         } else if (this.retrieveFlag) {
            InventoryUtility.saveSlot();
            SearchInvResult waterResult = InventoryUtility.findItemInHotBar(class_1802.field_8550);
            waterResult.switchTo();
            mc.field_1724.method_36457(90.0F);
            mc.field_1761.method_2919(mc.field_1724, class_1268.field_5808);
            mc.field_1724.method_6104(class_1268.field_5808);
            InventoryUtility.returnSlot();
            this.retrieveFlag = false;
         }

      }
   }

   @EventHandler
   public void onTick(EventTick e) {
      if (this.mode.is(NoFall.Mode.Grim2b2t) && this.isFalling()) {
         this.sendPacket(new class_2830(mc.field_1724.method_23317(), mc.field_1724.method_23318() + 1.0E-9D, mc.field_1724.method_23321(), mc.field_1724.method_36454(), mc.field_1724.method_36455(), false));
         mc.field_1724.method_38785();
      }

   }

   private void doWaterDrop(SearchInvResult waterResult, class_2338 playerPos) {
      if (mc.field_1687.method_8320(playerPos.method_10074()).method_51367() || mc.field_1687.method_8320(playerPos.method_10074().method_10074()).method_51367()) {
         InventoryUtility.saveSlot();
         waterResult.switchTo();
         mc.field_1761.method_2919(mc.field_1724, class_1268.field_5808);
         mc.field_1724.method_6104(class_1268.field_5808);
         InventoryUtility.returnSlot();
         this.retrieveFlag = (Boolean)this.retrieve.getValue();
      }

   }

   private void doPearlDrop(SearchInvResult pearlResult) {
      if (this.pearlCooldown.passedMs(5000L)) {
         InventoryUtility.saveSlot();
         pearlResult.switchTo();
         mc.field_1761.method_2919(mc.field_1724, class_1268.field_5808);
         mc.field_1724.method_6104(class_1268.field_5808);
         InventoryUtility.returnSlot();
         this.pearlCooldown.reset();
      }

   }

   private void doWebDrop(SearchInvResult webResult, class_2338 playerPos) {
      if (mc.field_1687.method_8320(playerPos.method_10074()).method_51367() || mc.field_1687.method_8320(playerPos.method_10074().method_10074()).method_51367()) {
         InventoryUtility.saveSlot();
         if (mc.field_1687.method_8320(playerPos.method_10074()).method_51367()) {
            InteractionUtility.placeBlock(playerPos, InteractionUtility.Rotate.None, InteractionUtility.Interact.Vanilla, InteractionUtility.PlaceMode.Normal, webResult.slot(), false, true);
         } else {
            InteractionUtility.placeBlock(playerPos.method_10074(), InteractionUtility.Rotate.None, InteractionUtility.Interact.Vanilla, InteractionUtility.PlaceMode.Normal, webResult.slot(), false, true);
         }

         mc.field_1724.method_6104(class_1268.field_5808);
         InventoryUtility.returnSlot();
      }

   }

   private void doVinesDrop(SearchInvResult vinesResult, class_2338 playerPos) {
      if (mc.field_1687.method_8320(playerPos.method_10074()).method_51367() || mc.field_1687.method_8320(playerPos.method_10074().method_10074()).method_51367()) {
         InventoryUtility.saveSlot();
         if (mc.field_1687.method_8320(playerPos.method_10074()).method_51367()) {
            InteractionUtility.placeBlock(playerPos, InteractionUtility.Rotate.None, InteractionUtility.Interact.Vanilla, InteractionUtility.PlaceMode.Normal, vinesResult.slot(), false, true);
         } else {
            InteractionUtility.placeBlock(playerPos.method_10074(), InteractionUtility.Rotate.None, InteractionUtility.Interact.Vanilla, InteractionUtility.PlaceMode.Normal, vinesResult.slot(), false, true);
         }

         mc.field_1724.method_6104(class_1268.field_5808);
         InventoryUtility.returnSlot();
      }

   }

   private void doSnowDrop(SearchInvResult snowResult, class_2338 playerPos) {
      if (mc.field_1687.method_8320(playerPos.method_10074()).method_51367() || mc.field_1687.method_8320(playerPos.method_10074().method_10074()).method_51367()) {
         InventoryUtility.saveSlot();
         if (mc.field_1687.method_8320(playerPos.method_10074()).method_51367()) {
            InteractionUtility.placeBlock(playerPos, InteractionUtility.Rotate.None, InteractionUtility.Interact.Vanilla, InteractionUtility.PlaceMode.Normal, snowResult.slot(), false, true);
         } else {
            InteractionUtility.placeBlock(playerPos.method_10074(), InteractionUtility.Rotate.None, InteractionUtility.Interact.Vanilla, InteractionUtility.PlaceMode.Normal, snowResult.slot(), false, true);
         }

         mc.field_1724.method_6104(class_1268.field_5808);
         InventoryUtility.returnSlot();
      }

   }

   public boolean isFalling() {
      if (mc != null && mc.field_1724 != null && mc.field_1687 != null) {
         if (mc.field_1724.method_6128()) {
            return false;
         } else if (this.mode.is(NoFall.Mode.Grim2b2t)) {
            return mc.field_1724.field_6017 > 3.0F;
         } else {
            switch(((NoFall.FallDistance)this.fallDistance.getValue()).ordinal()) {
            case 0:
               return (mc.field_1724.field_6017 - 3.0F) / 2.0F + 3.5F > mc.field_1724.method_6032() / 3.0F;
            case 1:
               return mc.field_1724.field_6017 > (float)(Integer)this.fallDistanceValue.getValue();
            default:
               return false;
            }
         }
      } else {
         return false;
      }
   }

   public String getDisplayInfo() {
      String var10000 = ((NoFall.Mode)this.mode.getValue()).toString();
      return var10000 + " " + (this.isFalling() ? "Ready" : "");
   }

   @EventHandler
   public void onPacketSend(PacketEvent.Send e) {
      class_2596 var3 = e.getPacket();
      if (var3 instanceof class_2828) {
         class_2828 pac = (class_2828)var3;
         if (this.cancelGround) {
            ((IPlayerMoveC2SPacket)pac).setOnGround(false);
         }
      }

   }

   public void onEnable() {
      this.cancelGround = false;
   }

   private static enum Mode {
      Rubberband,
      Items,
      MatrixOffGround,
      Vanilla,
      Grim2b2t;

      // $FF: synthetic method
      private static NoFall.Mode[] $values() {
         return new NoFall.Mode[]{Rubberband, Items, MatrixOffGround, Vanilla, Grim2b2t};
      }
   }

   private static enum FallDistance {
      Calc,
      Custom;

      // $FF: synthetic method
      private static NoFall.FallDistance[] $values() {
         return new NoFall.FallDistance[]{Calc, Custom};
      }
   }
}
