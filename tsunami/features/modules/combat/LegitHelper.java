package tsunami.features.modules.combat;

import java.util.Iterator;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_1268;
import net.minecraft.class_1297;
import net.minecraft.class_1511;
import net.minecraft.class_1657;
import net.minecraft.class_1667;
import net.minecraft.class_1713;
import net.minecraft.class_1802;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_239;
import net.minecraft.class_243;
import net.minecraft.class_2596;
import net.minecraft.class_2824;
import net.minecraft.class_2846;
import net.minecraft.class_2868;
import net.minecraft.class_2885;
import net.minecraft.class_2886;
import net.minecraft.class_3532;
import net.minecraft.class_3959;
import net.minecraft.class_3965;
import net.minecraft.class_3966;
import net.minecraft.class_9278;
import net.minecraft.class_9334;
import net.minecraft.class_1297.class_5529;
import net.minecraft.class_239.class_240;
import net.minecraft.class_2846.class_2847;
import net.minecraft.class_3959.class_242;
import net.minecraft.class_3959.class_3960;
import org.jetbrains.annotations.NotNull;
import tsunami.core.Managers;
import tsunami.core.manager.client.AsyncManager;
import tsunami.core.manager.client.ModuleManager;
import tsunami.events.impl.EventEntitySpawn;
import tsunami.events.impl.EventSync;
import tsunami.events.impl.PacketEvent;
import tsunami.features.modules.Module;
import tsunami.injection.accesors.IMinecraftClient;
import tsunami.setting.Setting;
import tsunami.setting.impl.Bind;
import tsunami.setting.impl.BooleanSettingGroup;
import tsunami.utility.Timer;
import tsunami.utility.player.InteractionUtility;
import tsunami.utility.player.InventoryUtility;
import tsunami.utility.player.PlayerUtility;
import tsunami.utility.player.SearchInvResult;
import tsunami.utility.render.Render2DEngine;
import tsunami.utility.render.Render3DEngine;

public class LegitHelper extends Module {
   private final Setting<BooleanSettingGroup> minecarts = new Setting("Minecarts", new BooleanSettingGroup(true));
   private final Setting<Float> maxDistance;
   private final Setting<Boolean> refill;
   private final Setting<Integer> refillSlot;
   private final Setting<BooleanSettingGroup> anchors;
   private final Setting<Integer> anchorDelay;
   private final Setting<Bind> anchorBind;
   private final Setting<BooleanSettingGroup> crystals;
   private final Setting<Integer> crystalDelay;
   private final Setting<Bind> crystalBind;
   private final Setting<Boolean> changePitch;
   private final Setting<Boolean> crystalOptimizer;
   private final Setting<Boolean> switchBack;
   private final Setting<BooleanSettingGroup> shieldBreaker;
   private final Setting<Integer> breakerDelay;
   private final Setting<Boolean> swapBack;
   private final Setting<BooleanSettingGroup> windBoostJump;
   private final Setting<Bind> windBoostBind;
   private final Setting<BooleanSettingGroup> crossBow;
   private final Setting<Bind> crossBowBind;
   private final Setting<Boolean> cbswapBack;
   private Timer timer;
   private Timer cbtimer;
   private class_243 lastCrystalVec;
   private class_243 rotationVec;

   public LegitHelper() {
      super("LegitHelper", Module.Category.NONE);
      this.maxDistance = (new Setting("MaxDistance", 4.0F, 2.0F, 6.0F)).addToGroup(this.minecarts);
      this.refill = (new Setting("Refill", true)).addToGroup(this.minecarts);
      this.refillSlot = (new Setting("RefillSlot", 9, 1, 9, (v) -> {
         return (Boolean)this.refill.getValue();
      })).addToGroup(this.minecarts);
      this.anchors = new Setting("Anchors", new BooleanSettingGroup(true));
      this.anchorDelay = (new Setting("AnchorDelay", 50, 5, 250)).addToGroup(this.anchors);
      this.anchorBind = (new Setting("AnchorBind", new Bind(89, false, false))).addToGroup(this.anchors);
      this.crystals = new Setting("Crystals", new BooleanSettingGroup(true));
      this.crystalDelay = (new Setting("CrystalDelay", 50, 5, 250)).addToGroup(this.crystals);
      this.crystalBind = (new Setting("CrystalBind", new Bind(85, false, false))).addToGroup(this.crystals);
      this.changePitch = (new Setting("ChangePitch", false)).addToGroup(this.crystals);
      this.crystalOptimizer = (new Setting("CrystalOptimizer", false)).addToGroup(this.crystals);
      this.switchBack = (new Setting("SwitchBack", false)).addToGroup(this.crystals);
      this.shieldBreaker = new Setting("ShieldBreaker", new BooleanSettingGroup(false));
      this.breakerDelay = (new Setting("BreakerDelay", 50, 5, 250)).addToGroup(this.shieldBreaker);
      this.swapBack = (new Setting("SwapBack", true)).addToGroup(this.shieldBreaker);
      this.windBoostJump = new Setting("WindBoostJump", new BooleanSettingGroup(true));
      this.windBoostBind = (new Setting("WindBoostBind", new Bind(73, false, false))).addToGroup(this.windBoostJump);
      this.crossBow = new Setting("CrossBow", new BooleanSettingGroup(true));
      this.crossBowBind = (new Setting("CrossBowBind", new Bind(79, false, false))).addToGroup(this.crossBow);
      this.cbswapBack = (new Setting("CBSwapBack", true)).addToGroup(this.crossBow);
      this.timer = new Timer();
      this.cbtimer = new Timer();
      this.lastCrystalVec = class_243.field_1353;
      this.rotationVec = class_243.field_1353;
   }

   public void onUpdate() {
      int crystalSlot;
      if (((BooleanSettingGroup)this.anchors.getValue()).isEnabled() && this.isKeyPressed(this.anchorBind) && this.timer.every((long)(Integer)this.anchorDelay.getValue() * 5L + 100L)) {
         int glowSlot = InventoryUtility.findItemInHotBar(class_1802.field_8801).slot();
         int anchorSlot = InventoryUtility.findItemInHotBar(class_1802.field_23141).slot();
         if (glowSlot != -1 && anchorSlot != -1) {
            crystalSlot = mc.field_1724.method_31548().field_7545;
            Managers.ASYNC.run(() -> {
               mc.field_1724.method_31548().field_7545 = anchorSlot;
               mc.method_1562().method_52787(new class_2868(anchorSlot));
            });
            Managers.ASYNC.run(() -> {
               mc.method_40000(() -> {
                  ((IMinecraftClient)mc).idoItemUse();
               });
            }, (long)(Integer)this.anchorDelay.getValue());
            Managers.ASYNC.run(() -> {
               mc.field_1724.method_31548().field_7545 = glowSlot;
               mc.method_1562().method_52787(new class_2868(glowSlot));
            }, (long)((Integer)this.anchorDelay.getValue() * 2));
            Managers.ASYNC.run(() -> {
               mc.method_40000(() -> {
                  ((IMinecraftClient)mc).idoItemUse();
               });
            }, (long)(Integer)this.anchorDelay.getValue() * 3L);
            Managers.ASYNC.run(() -> {
               mc.field_1724.method_31548().field_7545 = crystalSlot;
               mc.method_1562().method_52787(new class_2868(crystalSlot));
            }, (long)((Integer)this.anchorDelay.getValue() * 4));
            Managers.ASYNC.run(() -> {
               mc.method_40000(() -> {
                  ((IMinecraftClient)mc).idoItemUse();
               });
            }, (long)(Integer)this.anchorDelay.getValue() * 5L);
         }
      } else {
         boolean var10000;
         label120: {
            class_239 var3 = mc.field_1765;
            if (var3 instanceof class_3966) {
               class_3966 ehr = (class_3966)var3;
               if (ehr.method_17782() instanceof class_1511) {
                  var10000 = true;
                  break label120;
               }
            }

            var10000 = false;
         }

         boolean crystalAtCrosshair;
         label115: {
            crystalAtCrosshair = var10000;
            class_239 var4 = mc.field_1765;
            if (var4 instanceof class_3965) {
               class_3965 bhr = (class_3965)var4;
               if (mc.field_1687.method_8320(bhr.method_17777()).method_26204() == class_2246.field_10540) {
                  var10000 = true;
                  break label115;
               }
            }

            var10000 = false;
         }

         boolean obbyAtCrosshair = var10000;
         int axeSlot;
         if (((BooleanSettingGroup)this.crystals.getValue()).isEnabled() && this.isKeyPressed(this.crystalBind) && this.timer.every((long)(Integer)this.crystalDelay.getValue() * (crystalAtCrosshair ? 1L : (obbyAtCrosshair ? 2L : 4L)))) {
            crystalSlot = InventoryUtility.findItemInHotBar(class_1802.field_8301).slot();
            int obbySlot = InventoryUtility.findBlockInHotBar(class_2246.field_10540).slot();
            if (obbySlot == -1 || crystalSlot == -1 || crystalSlot >= 9 || obbySlot >= 9) {
               return;
            }

            if (crystalAtCrosshair) {
               mc.field_1761.method_2918(mc.field_1724, ((class_3966)mc.field_1765).method_17782());
               mc.field_1724.method_6104(class_1268.field_5808);
               return;
            }

            axeSlot = mc.field_1724.method_31548().field_7545;
            if (!obbyAtCrosshair) {
               mc.field_1724.method_31548().field_7545 = obbySlot;
               mc.method_1562().method_52787(new class_2868(obbySlot));
               ((IMinecraftClient)mc).idoItemUse();
            }

            Managers.ASYNC.run(() -> {
               if (!obbyAtCrosshair) {
                  AsyncManager.sleep((Integer)this.crystalDelay.getValue());
               }

               mc.field_1724.method_31548().field_7545 = crystalSlot;
               mc.method_1562().method_52787(new class_2868(crystalSlot));
               AsyncManager.sleep((Integer)this.crystalDelay.getValue());
               ((IMinecraftClient)mc).idoItemUse();
               this.lastCrystalVec = mc.field_1765.method_17784();
               if ((Boolean)this.switchBack.getValue()) {
                  AsyncManager.sleep((Integer)this.crystalDelay.getValue());
                  mc.field_1724.method_31548().field_7545 = axeSlot;
                  mc.method_1562().method_52787(new class_2868(axeSlot));
               }

            });
         }

         if (((BooleanSettingGroup)this.shieldBreaker.getValue()).isEnabled()) {
            class_239 var16 = mc.field_1765;
            if (var16 instanceof class_3966) {
               class_3966 ehr = (class_3966)var16;
               class_1297 var17 = ehr.method_17782();
               if (var17 instanceof class_1657) {
                  class_1657 pl = (class_1657)var17;
                  if (!Managers.FRIEND.isFriend(pl) && (pl.method_6079().method_7909() == class_1802.field_8255 || pl.method_6047().method_7909() == class_1802.field_8255) && pl.method_6030().method_7909() == class_1802.field_8255 && this.timer.every(500L)) {
                     axeSlot = InventoryUtility.getAxeHotBar().slot();
                     if (axeSlot == -1) {
                        return;
                     }

                     int prevSlot = mc.field_1724.method_31548().field_7545;
                     Managers.ASYNC.run(() -> {
                        AsyncManager.sleep((Integer)this.breakerDelay.getValue());
                        mc.field_1724.method_31548().field_7545 = axeSlot;
                        mc.method_1562().method_52787(new class_2868(axeSlot));
                        AsyncManager.sleep((Integer)this.breakerDelay.getValue());
                        class_239 patt0$temp = mc.field_1765;
                        if (patt0$temp instanceof class_3966) {
                           class_3966 ehr2 = (class_3966)patt0$temp;
                           mc.field_1761.method_2918(mc.field_1724, ehr2.method_17782());
                        }

                        mc.field_1724.method_6104(class_1268.field_5808);
                        if ((Boolean)this.swapBack.getValue()) {
                           AsyncManager.sleep((Integer)this.breakerDelay.getValue());
                           mc.field_1724.method_31548().field_7545 = prevSlot;
                           mc.method_1562().method_52787(new class_2868(prevSlot));
                        }

                     });
                  }
               }
            }
         }

         SearchInvResult result;
         if (((BooleanSettingGroup)this.minecarts.getValue()).isEnabled() && (Boolean)this.refill.getValue() && mc.field_1724.method_31548().method_5438((Integer)this.refillSlot.getValue() - 1).method_7909() != class_1802.field_8069) {
            result = InventoryUtility.findItemInInventory(class_1802.field_8069);
            if (result.found()) {
               clickSlot(result.slot(), (Integer)this.refillSlot.getValue() - 1, class_1713.field_7791);
            }
         }

         if (((BooleanSettingGroup)this.crossBow.getValue()).isEnabled() && this.isKeyPressed(this.crossBowBind) && this.cbtimer.every(300L)) {
            result = InventoryUtility.findInHotBar((i) -> {
               return i.method_7909() == class_1802.field_8399 && i.method_57824(class_9334.field_49649) != null && !((class_9278)i.method_57824(class_9334.field_49649)).method_57442();
            });
            if (result.found()) {
               InventoryUtility.saveAndSwitchTo(result.slot());
               InteractionUtility.sendSequencedPacket((id) -> {
                  return new class_2886(class_1268.field_5808, id, mc.field_1724.method_36454(), mc.field_1724.method_36455());
               });
               if ((Boolean)this.cbswapBack.getValue()) {
                  InventoryUtility.returnSlot();
               }
            }
         }

      }
   }

   @EventHandler
   public void onEntitySpawn(EventEntitySpawn e) {
      class_1297 var3 = e.getEntity();
      if (var3 instanceof class_1511) {
         class_1511 cr = (class_1511)var3;
         if (e.getEntity().method_5707(this.lastCrystalVec) < 4.0D) {
            this.lastCrystalVec = class_243.field_1353;
            if ((Boolean)this.changePitch.getValue()) {
               float pitch = InteractionUtility.calculateAngle(cr.method_19538().method_1031(0.0D, 0.15D, 0.0D))[1];
               double gcdFix = Math.pow((Double)mc.field_1690.method_42495().method_41753() * 0.6D + 0.2D, 3.0D) * 1.2D;
               mc.field_1724.method_36457((float)((double)pitch - (double)(pitch - mc.field_1724.method_36455()) % gcdFix));
            }

            mc.field_1761.method_2918(mc.field_1724, e.getEntity());
            mc.field_1724.method_6104(class_1268.field_5808);
         }
      }

   }

   @EventHandler
   public void onPacketSend(@NotNull PacketEvent.Send event) {
      if ((Boolean)this.crystalOptimizer.getValue() && event.getPacket() instanceof class_2824 && Criticals.getInteractType((class_2824)event.getPacket()) == Criticals.InteractType.ATTACK) {
         class_1297 var3 = Criticals.getEntity((class_2824)event.getPacket());
         if (var3 instanceof class_1511) {
            class_1511 c = (class_1511)var3;
            if (!ModuleManager.autoCrystal.isEnabled()) {
               c.method_5768();
               c.method_31745(class_5529.field_26998);
               c.method_36209();
            }
         }
      }

   }

   @EventHandler
   public void onPacketSendPost(@NotNull PacketEvent.SendPost event) {
      class_2596 var3 = event.getPacket();
      if (var3 instanceof class_2846) {
         class_2846 action = (class_2846)var3;
         if (action.method_12363() == class_2847.field_12974 && ((BooleanSettingGroup)this.minecarts.getValue()).isEnabled() && mc.field_1724.method_6047().method_7909() == class_1802.field_8102) {
            class_2338 bp = this.calcTrajectory(mc.field_1724.method_36454());
            if (bp != null && PlayerUtility.squaredDistanceFromEyes(bp.method_46558()) <= this.maxDistance.getPow2Value() && PlayerUtility.squaredDistanceFromEyes(bp.method_46558()) > 3.0F) {
               SearchInvResult baseResult = InventoryUtility.findItemInHotBar(class_1802.field_8129, class_1802.field_8655, class_1802.field_8211, class_1802.field_8848);
               SearchInvResult cartResult = InventoryUtility.findItemInHotBar(class_1802.field_8069);
               if (baseResult.found() && cartResult.found()) {
                  InventoryUtility.saveSlot();
                  baseResult.switchTo();
                  this.sendSequencedPacket((s) -> {
                     return new class_2885(class_1268.field_5808, new class_3965(new class_243((double)bp.method_10263() + 0.5D, (double)bp.method_10084().method_10264(), (double)bp.method_10260() + 0.0D), class_2350.field_11036, bp, false), s);
                  });
                  this.rotationVec = bp.method_10084().method_46558();
                  cartResult.switchTo();
                  this.sendSequencedPacket((s) -> {
                     return new class_2885(class_1268.field_5808, new class_3965(new class_243((double)bp.method_10263() + 0.5D, (double)bp.method_10084().method_10264() + 0.125D, (double)bp.method_10260() + 0.5D), class_2350.field_11036, bp.method_10084(), false), s);
                  });
                  InventoryUtility.returnSlot();
               }
            }
         }
      }

   }

   @EventHandler
   public void onSync(EventSync e) {
      if (this.rotationVec != null) {
         float[] angle = InteractionUtility.calculateAngle(this.rotationVec);
         mc.field_1724.method_36456(angle[0]);
         mc.field_1724.method_36457(angle[1]);
         this.rotationVec = null;
      }

      if (this.isKeyPressed(this.windBoostBind) && mc.field_1724.method_24828()) {
         SearchInvResult result = InventoryUtility.findItemInHotBar(class_1802.field_49098);
         if (result.found()) {
            mc.field_1724.method_36457(90.0F);
            mc.field_1724.method_6043();
            InventoryUtility.saveAndSwitchTo(result.slot());
            mc.field_1761.method_2919(mc.field_1724, class_1268.field_5808);
            InventoryUtility.returnSlot();
         }
      }

   }

   private class_2338 calcTrajectory(float yaw) {
      double x = Render2DEngine.interpolate(mc.field_1724.field_6014, mc.field_1724.method_23317(), (double)Render3DEngine.getTickDelta());
      double y = Render2DEngine.interpolate(mc.field_1724.field_6036, mc.field_1724.method_23318(), (double)Render3DEngine.getTickDelta());
      double z = Render2DEngine.interpolate(mc.field_1724.field_5969, mc.field_1724.method_23321(), (double)Render3DEngine.getTickDelta());
      y = y + (double)mc.field_1724.method_18381(mc.field_1724.method_18376()) - 0.1000000014901161D;
      double motionX = (double)(-class_3532.method_15374(yaw / 180.0F * 3.1415927F) * class_3532.method_15362(mc.field_1724.method_36455() / 180.0F * 3.1415927F));
      double motionY = (double)(-class_3532.method_15374(mc.field_1724.method_36455() / 180.0F * 3.141593F));
      double motionZ = (double)(class_3532.method_15362(yaw / 180.0F * 3.1415927F) * class_3532.method_15362(mc.field_1724.method_36455() / 180.0F * 3.1415927F));
      float power = (float)mc.field_1724.method_6048() / 20.0F;
      power = (power * power + power * 2.0F) / 3.0F;
      if (power > 1.0F) {
         power = 1.0F;
      }

      float distance = class_3532.method_15355((float)(motionX * motionX + motionY * motionY + motionZ * motionZ));
      motionX /= (double)distance;
      motionY /= (double)distance;
      motionZ /= (double)distance;
      float pow = power * 3.0F;
      motionX *= (double)pow;
      motionY *= (double)pow;
      motionZ *= (double)pow;
      if (!mc.field_1724.method_24828()) {
         motionY += mc.field_1724.method_18798().method_10214();
      }

      for(int i = 0; i < 300; ++i) {
         class_243 lastPos = new class_243(x, y, z);
         x += motionX;
         y += motionY;
         z += motionZ;
         motionX *= 0.99D;
         motionY *= 0.99D;
         motionZ *= 0.99D;
         motionY -= 0.05000000074505806D;
         class_243 pos = new class_243(x, y, z);
         Iterator var20 = mc.field_1687.method_18112().iterator();

         while(var20.hasNext()) {
            class_1297 ent = (class_1297)var20.next();
            if (!(ent instanceof class_1667) && !ent.equals(mc.field_1724) && ent.method_5829().method_994(new class_238(x - 0.3D, y - 0.3D, z - 0.3D, x + 0.3D, y + 0.3D, z + 0.3D))) {
               return null;
            }
         }

         class_3965 bhr = mc.field_1687.method_17742(new class_3959(lastPos, pos, class_3960.field_17559, class_242.field_1348, mc.field_1724));
         if (bhr != null && bhr.method_17783() == class_240.field_1332) {
            return bhr.method_17777();
         }

         if (y <= -65.0D) {
            break;
         }
      }

      return null;
   }
}
