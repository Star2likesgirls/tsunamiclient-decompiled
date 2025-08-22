package tsunami.features.modules.combat;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_124;
import net.minecraft.class_1657;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_238;
import net.minecraft.class_2382;
import net.minecraft.class_2620;
import org.jetbrains.annotations.NotNull;
import tsunami.events.impl.EventAttackBlock;
import tsunami.events.impl.EventBreakBlock;
import tsunami.events.impl.EventPlaceBlock;
import tsunami.events.impl.EventPostSync;
import tsunami.events.impl.PacketEvent;
import tsunami.features.modules.Module;
import tsunami.features.modules.base.PlaceModule;
import tsunami.features.modules.client.ClientSettings;
import tsunami.setting.Setting;
import tsunami.setting.impl.SettingGroup;
import tsunami.utility.player.InteractionUtility;
import tsunami.utility.player.PlayerUtility;
import tsunami.utility.world.HoleUtility;

public final class Blocker extends PlaceModule {
   private final Setting<Integer> actionShift = new Setting("Place Per Tick", 1, 1, 5);
   private final Setting<Integer> actionInterval = new Setting("Delay", 0, 0, 5);
   private final Setting<SettingGroup> logic = new Setting("Logic", new SettingGroup(false, 0));
   private final Setting<Boolean> antiCev;
   private final Setting<Boolean> antiCiv;
   private final Setting<Boolean> expand;
   private final Setting<Boolean> antiTntAura;
   private final Setting<Boolean> antiAutoAnchor;
   private final Setting<SettingGroup> detect;
   private final Setting<Boolean> onPacket;
   private final Setting<Boolean> onAttackBlock;
   private final Setting<Boolean> onBreak;
   private final List<class_2338> placePositions;
   private int tickCounter;

   public Blocker() {
      super("Blocker", Module.Category.NONE);
      this.antiCev = (new Setting("Anti Cev", true)).addToGroup(this.logic);
      this.antiCiv = (new Setting("Anti Civ", true)).addToGroup(this.logic);
      this.expand = (new Setting("Expand", true)).addToGroup(this.logic);
      this.antiTntAura = (new Setting("Anti TNT", false)).addToGroup(this.logic);
      this.antiAutoAnchor = (new Setting("Anti Anchor", false)).addToGroup(this.logic);
      this.detect = (new Setting("Detect", new SettingGroup(false, 1))).addToGroup(this.logic);
      this.onPacket = (new Setting("On Break Packet", true)).addToGroup(this.detect);
      this.onAttackBlock = (new Setting("On Attack Block", false)).addToGroup(this.detect);
      this.onBreak = (new Setting("On Break", true)).addToGroup(this.detect);
      this.placePositions = new CopyOnWriteArrayList();
      this.tickCounter = 0;
   }

   public void onEnable() {
      this.tickCounter = 0;
      String var10001 = String.valueOf(class_124.field_1061);
      this.sendMessage(var10001 + (ClientSettings.isRu() ? "ВНИМАНИЕ!!! " + String.valueOf(class_124.field_1070) + "Использование блокера на серверах осуждается игроками, а в некоторых странах карается набутыливанием!" : "WARNING!!! " + String.valueOf(class_124.field_1070) + "The use of blocker on servers is condemned by players, and in some countries is punishable by jail!"));
   }

   @EventHandler
   public void onPostSync(EventPostSync event) {
      if (this.tickCounter < (Integer)this.actionInterval.getValue()) {
         ++this.tickCounter;
      } else {
         if (this.tickCounter >= (Integer)this.actionInterval.getValue()) {
            this.tickCounter = 0;
         }

         if (this.getBlockResult().found() && !this.placePositions.isEmpty()) {
            this.placePositions.removeIf((b) -> {
               return PlayerUtility.squaredDistanceFromEyes(b.method_46558()) > this.range.getPow2Value();
            });
            int blocksPlaced = 0;

            while(blocksPlaced < (Integer)this.actionShift.getValue()) {
               class_2338 pos = (class_2338)this.placePositions.stream().filter((p) -> {
                  return InteractionUtility.canPlaceBlock(p, (InteractionUtility.Interact)this.interact.getValue(), true);
               }).min(Comparator.comparing((p) -> {
                  return mc.field_1724.method_19538().method_1022(p.method_46558());
               })).orElse((Object)null);
               if (pos == null || !mc.field_1724.method_24828() || !this.placeBlock(pos)) {
                  break;
               }

               ++blocksPlaced;
               this.tickCounter = 0;
               this.placePositions.remove(pos);
               this.inactivityTimer.reset();
            }

         }
      }
   }

   @EventHandler
   private void onPacketReceive(@NotNull PacketEvent.Receive event) {
      if (event.getPacket() instanceof class_2620 && (Boolean)this.onPacket.getValue()) {
         class_2620 packet = (class_2620)event.getPacket();
         this.doLogic(packet.method_11277());
      }

   }

   @EventHandler
   private void onAttackBlock(EventAttackBlock event) {
      if ((Boolean)this.onAttackBlock.getValue()) {
         this.doLogic(event.getBlockPos());
      }
   }

   @EventHandler
   private void onBreak(EventBreakBlock event) {
      if ((Boolean)this.onBreak.getValue()) {
         this.doLogic(event.getPos());
      }
   }

   @EventHandler
   private void onPlaceBlock(@NotNull EventPlaceBlock event) {
      if (event.getBlockPos().equals(mc.field_1724.method_24515().method_10086(2)) && event.getBlock().equals(class_2246.field_10375) && (Boolean)this.antiTntAura.getValue()) {
         this.placePositions.add(event.getBlockPos());
      }

      if (event.getBlockPos().equals(mc.field_1724.method_24515().method_10086(2)) && event.getBlock().equals(class_2246.field_23152) && (Boolean)this.antiAutoAnchor.getValue()) {
         this.placePositions.add(event.getBlockPos());
      }

   }

   private void doLogic(class_2338 pos) {
      if (mc.field_1687 != null && mc.field_1724 != null && HoleUtility.isHole(mc.field_1724.method_24515())) {
         Iterator var2;
         class_2338 checkPos;
         if ((Boolean)this.antiCev.getValue()) {
            var2 = HoleUtility.getHolePoses(mc.field_1724.method_19538()).iterator();

            while(var2.hasNext()) {
               checkPos = (class_2338)var2.next();
               if (pos.equals(checkPos.method_10086(2))) {
                  this.placePositions.add(checkPos.method_10086(3));
                  return;
               }
            }
         }

         if (HoleUtility.getSurroundPoses(mc.field_1724.method_19538()).contains(pos)) {
            if (mc.field_1687.method_8320(pos).method_26204() != class_2246.field_9987 && !mc.field_1687.method_8320(pos).method_45474()) {
               this.placePositions.add(pos.method_10084());
               if ((Boolean)this.expand.getValue()) {
                  class_2382[] var7 = HoleUtility.VECTOR_PATTERN;
                  int var8 = var7.length;

                  for(int var4 = 0; var4 < var8; ++var4) {
                     class_2382 vec = var7[var4];
                     class_2338 checkPos = pos.method_10081(vec);
                     if (this.canPlaceBlock(checkPos, true) && mc.field_1687.method_18467(class_1657.class, new class_238(checkPos)).isEmpty()) {
                        this.placePositions.add(checkPos);
                     }
                  }
               }

            }
         } else {
            if ((Boolean)this.antiCiv.getValue()) {
               var2 = HoleUtility.getSurroundPoses(mc.field_1724.method_19538()).iterator();

               while(var2.hasNext()) {
                  checkPos = (class_2338)var2.next();
                  if (pos.equals(checkPos.method_10084())) {
                     this.placePositions.add(checkPos.method_10086(2));
                     return;
                  }
               }
            }

         }
      }
   }
}
