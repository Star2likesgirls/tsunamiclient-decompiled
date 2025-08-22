package tsunami.features.modules.combat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_1657;
import net.minecraft.class_1747;
import net.minecraft.class_1792;
import net.minecraft.class_1802;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2292;
import net.minecraft.class_2338;
import net.minecraft.class_2440;
import tsunami.core.Managers;
import tsunami.core.manager.player.CombatManager;
import tsunami.events.impl.EventTick;
import tsunami.features.modules.Module;
import tsunami.features.modules.client.ClientSettings;
import tsunami.setting.Setting;
import tsunami.utility.player.InteractionUtility;
import tsunami.utility.player.InventoryUtility;
import tsunami.utility.player.SearchInvResult;
import tsunami.utility.world.HoleUtility;

public class AutoAnvil extends Module {
   private final Setting<Float> range = new Setting("Range", 5.0F, 1.0F, 7.0F);
   private final Setting<Boolean> once = new Setting("Once", false);
   private final Setting<Boolean> placePlates = new Setting("PlacePlates", false);
   private final Setting<InteractionUtility.PlaceMode> placeMode;
   private final Setting<InteractionUtility.Interact> interact;
   private final Setting<InteractionUtility.Rotate> rotate;
   private final Setting<CombatManager.TargetBy> targetBy;
   private final Setting<Boolean> sand;
   private final Setting<Boolean> gravel;
   private final Setting<Boolean> concrete;
   private final Setting<Boolean> anvils;
   private class_1657 target;

   public AutoAnvil() {
      super("AutoAnvil", Module.Category.NONE);
      this.placeMode = new Setting("Place Mode", InteractionUtility.PlaceMode.Normal);
      this.interact = new Setting("Interact Mode", InteractionUtility.Interact.Vanilla);
      this.rotate = new Setting("Rotate", InteractionUtility.Rotate.None);
      this.targetBy = new Setting("TargetBy", CombatManager.TargetBy.Distance);
      this.sand = new Setting("Sand", false);
      this.gravel = new Setting("Gravel", false);
      this.concrete = new Setting("Сoncrete", false);
      this.anvils = new Setting("Anvils", true);
   }

   @EventHandler
   private void onTick(EventTick event) {
      if (mc.field_1724 != null) {
         if (this.target != null && !this.target.method_29504()) {
            SearchInvResult result = this.getBlockResult();
            SearchInvResult plateResult = InventoryUtility.findItemInHotBar(class_1802.field_8667, class_1802.field_8779, class_1802.field_8592, class_1802.field_8721, class_1802.field_8391);
            class_2338 anvilPos = class_2338.method_49638(this.target.method_19538()).method_10086(2);
            if (result.found() && (plateResult.found() || !(Boolean)this.placePlates.getValue())) {
               class_2248 targetBlock = mc.field_1687.method_8320(class_2338.method_49638(this.target.method_19538())).method_26204();
               if (!(targetBlock instanceof class_2440) && targetBlock != class_2246.field_10582 && targetBlock != class_2246.field_10224 && (Boolean)this.placePlates.getValue()) {
                  InteractionUtility.placeBlock(class_2338.method_49638(this.target.method_19538()), (InteractionUtility.Rotate)this.rotate.getValue(), (InteractionUtility.Interact)this.interact.getValue(), (InteractionUtility.PlaceMode)this.placeMode.getValue(), plateResult, true, true);
               } else if (!InteractionUtility.canPlaceBlock(anvilPos, (InteractionUtility.Interact)this.interact.getValue(), false)) {
                  if (this.needObsidian(anvilPos)) {
                     Stream var10000 = (Stream)Arrays.stream(HoleUtility.VECTOR_PATTERN).parallel();
                     Objects.requireNonNull(anvilPos);
                     class_2338 obsidianPos = (class_2338)var10000.map(anvilPos::method_10081).filter((pos) -> {
                        return InteractionUtility.canPlaceBlock(pos, (InteractionUtility.Interact)this.interact.getValue(), false);
                     }).filter((pos) -> {
                        return pos.method_19770(mc.field_1724.method_19538()) <= (double)this.range.getPow2Value();
                     }).findFirst().orElse((Object)null);
                     SearchInvResult obbyResult = InventoryUtility.findBlockInHotBar(class_2246.field_10540);
                     if (obsidianPos != null && obbyResult.found()) {
                        InteractionUtility.placeBlock(obsidianPos, (InteractionUtility.Rotate)this.rotate.getValue(), (InteractionUtility.Interact)this.interact.getValue(), (InteractionUtility.PlaceMode)this.placeMode.getValue(), obbyResult, true, false);
                        if ((Boolean)this.once.getValue()) {
                           this.disable(ClientSettings.isRu() ? "Блок размещен" : "Done");
                        }
                     }
                  }

               } else {
                  InteractionUtility.placeBlock(anvilPos, (InteractionUtility.Rotate)this.rotate.getValue(), (InteractionUtility.Interact)this.interact.getValue(), (InteractionUtility.PlaceMode)this.placeMode.getValue(), result, true, false);
               }
            }
         } else {
            this.target = Managers.COMBAT.getTarget((Float)this.range.getValue(), (CombatManager.TargetBy)this.targetBy.getValue());
         }
      }
   }

   private boolean needObsidian(class_2338 anvilPos) {
      if (mc.field_1687 == null) {
         return false;
      } else {
         Stream var10000 = Arrays.stream(HoleUtility.VECTOR_PATTERN);
         Objects.requireNonNull(anvilPos);
         return var10000.map(anvilPos::method_10081).filter((pos) -> {
            return !mc.field_1687.method_8320(pos).method_45474();
         }).toList().isEmpty();
      }
   }

   protected SearchInvResult getBlockResult() {
      List<class_2248> canUseBlocks = new ArrayList();
      if (mc.field_1724 == null) {
         return SearchInvResult.notFound();
      } else {
         if ((Boolean)this.anvils.getValue()) {
            canUseBlocks.add(class_2246.field_10535);
         }

         if ((Boolean)this.sand.getValue()) {
            canUseBlocks.add(class_2246.field_10102);
         }

         if ((Boolean)this.gravel.getValue()) {
            canUseBlocks.add(class_2246.field_10255);
         }

         SearchInvResult defaultResult = InventoryUtility.findBlockInHotBar((List)canUseBlocks);
         SearchInvResult concreteResult = InventoryUtility.findInHotBar((i) -> {
            class_1792 patt0$temp = i.method_7909();
            boolean var10000;
            if (patt0$temp instanceof class_1747) {
               class_1747 bi = (class_1747)patt0$temp;
               if (bi.method_7711() instanceof class_2292) {
                  var10000 = true;
                  return var10000;
               }
            }

            var10000 = false;
            return var10000;
         });
         return (Boolean)this.concrete.getValue() && concreteResult.found() ? concreteResult : defaultResult;
      }
   }
}
