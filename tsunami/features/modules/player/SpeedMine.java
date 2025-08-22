package tsunami.features.modules.player;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_1268;
import net.minecraft.class_1293;
import net.minecraft.class_1294;
import net.minecraft.class_1297;
import net.minecraft.class_1542;
import net.minecraft.class_1657;
import net.minecraft.class_1713;
import net.minecraft.class_1799;
import net.minecraft.class_1890;
import net.minecraft.class_1893;
import net.minecraft.class_2189;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_2680;
import net.minecraft.class_2815;
import net.minecraft.class_2846;
import net.minecraft.class_2868;
import net.minecraft.class_4587;
import net.minecraft.class_5134;
import net.minecraft.class_6880;
import net.minecraft.class_2846.class_2847;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tsunami.core.Managers;
import tsunami.core.manager.client.ModuleManager;
import tsunami.core.manager.player.PlayerManager;
import tsunami.events.impl.EventAttackBlock;
import tsunami.events.impl.EventSync;
import tsunami.events.impl.PacketEvent;
import tsunami.features.modules.Module;
import tsunami.features.modules.combat.AutoCrystal;
import tsunami.injection.accesors.IInteractionManager;
import tsunami.setting.Setting;
import tsunami.setting.impl.BooleanSettingGroup;
import tsunami.setting.impl.ColorSetting;
import tsunami.setting.impl.SettingGroup;
import tsunami.utility.Timer;
import tsunami.utility.math.MathUtility;
import tsunami.utility.player.InteractionUtility;
import tsunami.utility.player.InventoryUtility;
import tsunami.utility.player.PlayerUtility;
import tsunami.utility.render.Render2DEngine;
import tsunami.utility.render.Render3DEngine;
import tsunami.utility.world.ExplosionUtility;

public final class SpeedMine extends Module {
   public final Setting<SpeedMine.Mode> mode;
   public final Setting<Boolean> doubleMine;
   private final Setting<SpeedMine.StartMode> startMode;
   private final Setting<SpeedMine.SwitchMode> switchMode;
   private final Setting<Integer> swapDelay;
   private final Setting<Float> factor;
   private final Setting<Float> speed;
   public final Setting<Float> range;
   private final Setting<Boolean> rotate;
   private final Setting<Boolean> placeCrystal;
   private final Setting<Boolean> resetOnSwitch;
   private final Setting<Integer> breakAttempts;
   private final Setting<Boolean> pauseEat;
   private final Setting<Boolean> clientRemove;
   private final Setting<SettingGroup> packets;
   private final Setting<Boolean> stop;
   private final Setting<Boolean> abort;
   private final Setting<Boolean> start;
   private final Setting<Boolean> stop2;
   private final Setting<BooleanSettingGroup> render;
   private final Setting<Boolean> smooth;
   private final Setting<SpeedMine.RenderMode> renderMode;
   private final Setting<ColorSetting> startLineColor;
   private final Setting<ColorSetting> endLineColor;
   private final Setting<Integer> lineWidth;
   private final Setting<ColorSetting> startFillColor;
   private final Setting<ColorSetting> endFillColor;
   public ArrayList<SpeedMine.MineAction> actions;

   public SpeedMine() {
      super("SpeedMine", Module.Category.NONE);
      this.mode = new Setting("Mode", SpeedMine.Mode.Packet);
      this.doubleMine = new Setting("DoubleMine", false);
      this.startMode = new Setting("StartMode", SpeedMine.StartMode.StartAbort, (v) -> {
         return this.mode.is(SpeedMine.Mode.Packet) && !(Boolean)this.doubleMine.getValue();
      });
      this.switchMode = new Setting("SwitchMode", SpeedMine.SwitchMode.Alternative, (v) -> {
         return this.mode.not(SpeedMine.Mode.Damage);
      });
      this.swapDelay = new Setting("SwapDelay", 50, 0, 1000, (v) -> {
         return this.mode.getValue() != SpeedMine.Mode.Damage;
      });
      this.factor = new Setting("Factor", 1.0F, 0.5F, 2.0F, (v) -> {
         return this.mode.getValue() != SpeedMine.Mode.Damage;
      });
      this.speed = new Setting("Speed", 0.5F, 0.0F, 1.0F, (v) -> {
         return this.mode.getValue() == SpeedMine.Mode.Damage;
      });
      this.range = new Setting("Range", 4.2F, 3.0F, 10.0F, (v) -> {
         return this.mode.getValue() != SpeedMine.Mode.Damage;
      });
      this.rotate = new Setting("Rotate", false, (v) -> {
         return this.mode.getValue() != SpeedMine.Mode.Damage;
      });
      this.placeCrystal = new Setting("PlaceCrystal", true);
      this.resetOnSwitch = new Setting("ResetOnSwitch", true, (v) -> {
         return this.mode.getValue() != SpeedMine.Mode.Damage;
      });
      this.breakAttempts = new Setting("BreakAttempts", 10, 1, 50, (v) -> {
         return this.mode.getValue() == SpeedMine.Mode.Packet;
      });
      this.pauseEat = new Setting("Pause On Eat", false);
      this.clientRemove = new Setting("ClientRemove", true);
      this.packets = new Setting("Packets", new SettingGroup(false, 0), (v) -> {
         return this.mode.is(SpeedMine.Mode.Packet) && !(Boolean)this.doubleMine.getValue();
      });
      this.stop = (new Setting("Stop", true, (v) -> {
         return this.mode.is(SpeedMine.Mode.Packet) && !(Boolean)this.doubleMine.getValue();
      })).addToGroup(this.packets);
      this.abort = (new Setting("Abort", true, (v) -> {
         return this.mode.is(SpeedMine.Mode.Packet) && !(Boolean)this.doubleMine.getValue();
      })).addToGroup(this.packets);
      this.start = (new Setting("Start", true, (v) -> {
         return this.mode.is(SpeedMine.Mode.Packet) && !(Boolean)this.doubleMine.getValue();
      })).addToGroup(this.packets);
      this.stop2 = (new Setting("Stop2", true, (v) -> {
         return this.mode.is(SpeedMine.Mode.Packet) && !(Boolean)this.doubleMine.getValue();
      })).addToGroup(this.packets);
      this.render = new Setting("Render", new BooleanSettingGroup(false), (v) -> {
         return this.mode.getValue() != SpeedMine.Mode.Damage;
      });
      this.smooth = (new Setting("Smooth", true, (v) -> {
         return this.mode.getValue() != SpeedMine.Mode.Damage;
      })).addToGroup(this.render);
      this.renderMode = (new Setting("Render Mode", SpeedMine.RenderMode.Shrink, (v) -> {
         return this.mode.getValue() != SpeedMine.Mode.Damage;
      })).addToGroup(this.render);
      this.startLineColor = (new Setting("Start Line Color", new ColorSetting(new Color(255, 0, 0, 200)), (v) -> {
         return this.mode.getValue() != SpeedMine.Mode.Damage;
      })).addToGroup(this.render);
      this.endLineColor = (new Setting("End Line Color", new ColorSetting(new Color(47, 255, 0, 200)), (v) -> {
         return this.mode.getValue() != SpeedMine.Mode.Damage;
      })).addToGroup(this.render);
      this.lineWidth = (new Setting("Line Width", 2, 1, 10, (v) -> {
         return this.mode.getValue() != SpeedMine.Mode.Damage;
      })).addToGroup(this.render);
      this.startFillColor = (new Setting("Start Fill Color", new ColorSetting(new Color(255, 0, 0, 120)), (v) -> {
         return this.mode.getValue() != SpeedMine.Mode.Damage;
      })).addToGroup(this.render);
      this.endFillColor = (new Setting("End Fill Color", new ColorSetting(new Color(47, 255, 0, 120)), (v) -> {
         return this.mode.getValue() != SpeedMine.Mode.Damage;
      })).addToGroup(this.render);
      this.actions = new ArrayList();
   }

   public void onDisable() {
      this.actions.forEach(SpeedMine.MineAction::reset);
      this.actions.clear();
   }

   public void onEnable() {
      this.actions.forEach(SpeedMine.MineAction::reset);
      this.actions.clear();
   }

   public void onUpdate() {
      if (!fullNullCheck() && !mc.field_1724.method_31549().field_7477) {
         if (!PlayerUtility.isEating() || !(Boolean)this.pauseEat.getValue()) {
            if (this.mode.getValue() == SpeedMine.Mode.Damage && ((IInteractionManager)mc.field_1761).getCurBlockDamageMP() < (Float)this.speed.getValue()) {
               ((IInteractionManager)mc.field_1761).setCurBlockDamageMP((Float)this.speed.getValue());
            }

            this.actions.removeIf(SpeedMine.MineAction::update);
         }
      }
   }

   public void onRender3D(class_4587 stack) {
      if (!this.mode.is(SpeedMine.Mode.Damage) && mc.field_1687 != null) {
         this.actions.forEach((a) -> {
            if (!mc.field_1687.method_22347(a.getPos())) {
               float noom = (float)MathUtility.clamp(Render2DEngine.interpolate((double)a.getPrevProgress(), (double)a.getProgress(), (double)Render3DEngine.getTickDelta()), 0.0D, 1.0D);
               class_238 var10000;
               switch(((SpeedMine.RenderMode)this.renderMode.getValue()).ordinal()) {
               case 0:
                  var10000 = new class_238(a.getPos());
                  break;
               case 1:
                  var10000 = (new class_238((double)a.getPos().method_10263(), (double)a.getPos().method_10264(), (double)a.getPos().method_10260(), (double)a.getPos().method_10263(), (double)a.getPos().method_10264(), (double)a.getPos().method_10260())).method_1002((double)noom, (double)noom, (double)noom).method_989(0.5D + (double)noom * 0.5D, 0.5D + (double)noom * 0.5D, 0.5D + (double)noom * 0.5D);
                  break;
               case 2:
                  var10000 = new class_238((double)a.getPos().method_10263(), (double)a.getPos().method_10264(), (double)a.getPos().method_10260(), (double)(a.getPos().method_10263() + 1), (double)((float)a.getPos().method_10264() + noom), (double)(a.getPos().method_10260() + 1));
                  break;
               default:
                  throw new MatchException((String)null, (Throwable)null);
               }

               class_238 renderBox = var10000;
               Render3DEngine.FILLED_QUEUE.add(new Render3DEngine.FillAction(renderBox, Render2DEngine.getColor(((ColorSetting)this.startFillColor.getValue()).getColorObject(), ((ColorSetting)this.endFillColor.getValue()).getColorObject(), a.getProgress(), (Boolean)this.smooth.getValue())));
               Render3DEngine.OUTLINE_QUEUE.add(new Render3DEngine.OutlineAction(renderBox, Render2DEngine.getColor(((ColorSetting)this.startLineColor.getValue()).getColorObject(), ((ColorSetting)this.endLineColor.getValue()).getColorObject(), a.getProgress(), (Boolean)this.smooth.getValue()), (float)(Integer)this.lineWidth.getValue()));
            }

         });
      }
   }

   @EventHandler
   public void onAttackBlock(@NotNull EventAttackBlock event) {
      if (!fullNullCheck() && this.canBreak(event.getBlockPos()) && !mc.field_1724.method_31549().field_7477 && !this.mode.is(SpeedMine.Mode.Damage)) {
         if (!this.alreadyActing(event.getBlockPos())) {
            if ((!(Boolean)this.doubleMine.getValue() || this.actions.size() >= 2) && !this.actions.isEmpty()) {
               ((SpeedMine.MineAction)this.actions.removeFirst()).cancel();
            }

            this.actions.add(new SpeedMine.MineAction(event.getBlockPos(), event.getEnumFacing()));
         }

         event.cancel();
      }
   }

   public boolean alreadyActing(class_2338 blockPos) {
      return this.actions.stream().anyMatch((a) -> {
         return a.pos.equals(blockPos);
      });
   }

   @EventHandler(
      priority = -100
   )
   private void onSync(EventSync event) {
      this.actions.forEach(SpeedMine.MineAction::onSync);
   }

   @EventHandler
   private void onPacketSend(@NotNull PacketEvent.SendPost e) {
      if (e.getPacket() instanceof class_2868 && (Boolean)this.resetOnSwitch.getValue() && !this.switchMode.is(SpeedMine.SwitchMode.Silent) && !this.mode.is(SpeedMine.Mode.GrimInstant)) {
         this.actions.forEach(SpeedMine.MineAction::reset);
      }

   }

   private void closeScreen() {
      if (mc.field_1724 != null) {
         this.sendPacket(new class_2815(mc.field_1724.field_7512.field_7763));
      }
   }

   public float getBlockStrength(@NotNull class_2680 state, class_2338 position) {
      if (state == class_2246.field_10124.method_9564()) {
         return 0.02F;
      } else {
         float hardness = state.method_26214(mc.field_1687, position);
         return hardness < 0.0F ? 0.0F : this.getDigSpeed(state, position) / hardness / (this.canBreak(position) ? 30.0F : 100.0F);
      }
   }

   private float getDestroySpeed(class_2338 position, class_2680 state) {
      float destroySpeed = 1.0F;
      int slot = this.getTool(position);
      if (mc.field_1724 == null) {
         return 0.0F;
      } else {
         if (slot != -1 && mc.field_1724.method_31548().method_5438(slot) != null && !mc.field_1724.method_31548().method_5438(slot).method_7960()) {
            destroySpeed *= mc.field_1724.method_31548().method_5438(slot).method_7924(state);
         }

         return destroySpeed;
      }
   }

   public float getDigSpeed(class_2680 state, class_2338 position) {
      if (mc.field_1724 == null) {
         return 0.0F;
      } else {
         float digSpeed = this.getDestroySpeed(position, state);
         if (digSpeed > 1.0F) {
            int slot = this.getTool(position);
            if (slot != -1) {
               class_1799 itemstack = mc.field_1724.method_31548().method_5438(slot);
               int efficiencyModifier = class_1890.method_8225((class_6880)mc.field_1687.method_30349().method_30530(class_1893.field_9131.method_58273()).method_40264(class_1893.field_9131).get(), itemstack);
               if (efficiencyModifier > 0 && !itemstack.method_7960()) {
                  digSpeed += (float)(StrictMath.pow((double)efficiencyModifier, 2.0D) + 1.0D);
               }
            }
         }

         if (mc.field_1724.method_6059(class_1294.field_5917)) {
            digSpeed *= 1.0F + (float)(((class_1293)Objects.requireNonNull(mc.field_1724.method_6112(class_1294.field_5917))).method_5578() + 1) * 0.2F;
         }

         if (mc.field_1724.method_6059(class_1294.field_5901)) {
            digSpeed *= (float)Math.pow(0.30000001192092896D, (double)(((class_1293)Objects.requireNonNull(mc.field_1724.method_6112(class_1294.field_5901))).method_5578() + 1));
         }

         if (mc.field_1724.method_5869()) {
            digSpeed *= (float)mc.field_1724.method_5996(class_5134.field_51576).method_6194();
         }

         if (!mc.field_1724.method_24828() && ModuleManager.freeCam.isDisabled()) {
            digSpeed /= 5.0F;
         }

         return digSpeed < 0.0F ? 0.0F : digSpeed * (Float)this.factor.getValue();
      }
   }

   public int getTool(class_2338 pos) {
      int index = -1;
      float currentFastest = 1.0F;
      if (mc.field_1687 != null && mc.field_1724 != null && !(mc.field_1687.method_8320(pos).method_26204() instanceof class_2189)) {
         for(int i = 9; i < 45; ++i) {
            class_1799 stack = mc.field_1724.method_31548().method_5438(i >= 36 ? i - 36 : i);
            if (stack != class_1799.field_8037 && stack.method_7936() - stack.method_7919() > 10) {
               float digSpeed = (float)class_1890.method_8225((class_6880)mc.field_1687.method_30349().method_30530(class_1893.field_9111.method_58273()).method_40264(class_1893.field_9131).get(), stack);
               float destroySpeed = stack.method_7924(mc.field_1687.method_8320(pos));
               if (digSpeed + destroySpeed > currentFastest) {
                  currentFastest = digSpeed + destroySpeed;
                  index = i;
               }
            }
         }

         return index >= 36 ? index - 36 : index;
      } else {
         return -1;
      }
   }

   private boolean canBreak(class_2338 pos) {
      if (mc.field_1687 != null && !(PlayerUtility.squaredDistanceFromEyes(pos.method_46558()) > this.range.getPow2Value())) {
         class_2680 blockState = mc.field_1687.method_8320(pos);
         class_2248 block = blockState.method_26204();
         return block.method_36555() != -1.0F;
      } else {
         return false;
      }
   }

   public void placeCrystal() {
      if (AutoCrystal.target != null) {
         AutoCrystal.PlaceData data = this.getCevData();
         if (data == null) {
            data = this.getBestData();
         }

         if (data != null) {
            ModuleManager.autoCrystal.placeCrystal(data.bhr(), true, false);
            this.debug("placing..");
            ModuleManager.autoTrap.pause();
            ModuleManager.breaker.pause();
         }

      }
   }

   @Nullable
   public AutoCrystal.PlaceData getCevData() {
      Iterator var1 = this.actions.iterator();

      SpeedMine.MineAction action;
      do {
         if (!var1.hasNext()) {
            return null;
         }

         action = (SpeedMine.MineAction)var1.next();
      } while(!mc.field_1687.method_22347(action.getPos().method_10074()));

      if (ExplosionUtility.getSelfExplosionDamage(action.getPos().method_46558().method_1031(0.0D, 0.5D, 0.0D), 0, false) > (Float)ModuleManager.autoCrystal.maxSelfDamage.getValue()) {
         return null;
      } else {
         return ModuleManager.autoCrystal.getPlaceData(action.getPos(), (class_1657)null, mc.field_1724.method_19538());
      }
   }

   @Nullable
   public AutoCrystal.PlaceData getBestData() {
      Iterator var1 = this.actions.iterator();

      float selfDmg;
      AutoCrystal.PlaceData autoMineData;
      do {
         if (!var1.hasNext()) {
            return null;
         }

         SpeedMine.MineAction action = (SpeedMine.MineAction)var1.next();
         class_2680 prevState = mc.field_1687.method_8320(action.getPos());
         mc.field_1687.method_8501(action.getPos(), class_2246.field_10124.method_9564());
         class_2350[] var4 = class_2350.values();
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            class_2350 dir = var4[var6];
            if (dir != class_2350.field_11036 && dir != class_2350.field_11033 && !(ExplosionUtility.getSelfExplosionDamage(action.getPos().method_10074().method_10093(dir).method_46558().method_1031(0.0D, 0.5D, 0.0D), 0, false) > (Float)ModuleManager.autoCrystal.maxSelfDamage.getValue())) {
               AutoCrystal.PlaceData autoMineData = ModuleManager.autoCrystal.getPlaceData(action.getPos().method_10074().method_10093(dir), (class_1657)null, mc.field_1724.method_19538());
               if (autoMineData != null) {
                  mc.field_1687.method_8501(action.getPos(), prevState);
                  return autoMineData;
               }
            }
         }

         selfDmg = ExplosionUtility.getSelfExplosionDamage(action.getPos().method_46558().method_1031(0.0D, 0.5D, 0.0D), 0, false);
         mc.field_1687.method_8501(action.getPos(), prevState);
         autoMineData = ModuleManager.autoCrystal.getPlaceData(action.getPos(), (class_1657)null, mc.field_1724.method_19538());
      } while(selfDmg > (Float)ModuleManager.autoCrystal.maxSelfDamage.getValue());

      return autoMineData;
   }

   public boolean isBlockDrop(class_1297 ent) {
      if (ent instanceof class_1542 && this.isOn() && ent.field_6012 < 3) {
         Iterator var2 = this.actions.iterator();

         while(var2.hasNext()) {
            SpeedMine.MineAction a = (SpeedMine.MineAction)var2.next();
            if (a.getPos().method_46558().method_1025(ent.method_19538()) <= 1.0D) {
               return true;
            }
         }
      }

      return false;
   }

   public static enum Mode {
      Packet,
      GrimInstant,
      Damage;

      // $FF: synthetic method
      private static SpeedMine.Mode[] $values() {
         return new SpeedMine.Mode[]{Packet, GrimInstant, Damage};
      }
   }

   public static enum StartMode {
      StartAbort,
      StartStop;

      // $FF: synthetic method
      private static SpeedMine.StartMode[] $values() {
         return new SpeedMine.StartMode[]{StartAbort, StartStop};
      }
   }

   public static enum SwitchMode {
      Silent,
      Normal,
      Alternative;

      // $FF: synthetic method
      private static SpeedMine.SwitchMode[] $values() {
         return new SpeedMine.SwitchMode[]{Silent, Normal, Alternative};
      }
   }

   public static enum RenderMode {
      Block,
      Shrink,
      Grow;

      // $FF: synthetic method
      private static SpeedMine.RenderMode[] $values() {
         return new SpeedMine.RenderMode[]{Block, Shrink, Grow};
      }
   }

   public class MineAction {
      @NotNull
      private final class_2338 pos;
      private float progress;
      private float prevProgress;
      private int mineBreaks;
      private final Timer attackTimer = new Timer();

      public MineAction(@NotNull class_2338 pos, class_2350 direction) {
         this.pos = pos;
         this.progress = 0.0F;
         this.mineBreaks = 0;
         this.start(direction);
      }

      public void start(class_2350 direction) {
         class_2350 startDirection = direction == null ? Module.mc.field_1724.method_5735() : direction;
         if (startDirection != null) {
            if ((Boolean)SpeedMine.this.doubleMine.getValue()) {
               SpeedMine.this.sendPacket(new class_2846(class_2847.field_12973, this.pos, startDirection));
               SpeedMine.this.sendPacket(new class_2846(class_2847.field_12968, this.pos, startDirection));
               SpeedMine.this.sendPacket(new class_2846(class_2847.field_12973, this.pos, startDirection));
            } else {
               SpeedMine.this.sendPacket(new class_2846(class_2847.field_12968, this.pos, startDirection));
               SpeedMine.this.sendPacket(new class_2846(SpeedMine.this.startMode.getValue() == SpeedMine.StartMode.StartAbort ? class_2847.field_12971 : class_2847.field_12973, this.pos, startDirection));
            }
         }

      }

      public boolean update() {
         class_2350 dir = (class_2350)InteractionUtility.getStrictDirections(this.pos).stream().findFirst().orElse(Module.mc.field_1724.method_5735());
         if (this.mineBreaks >= (Integer)SpeedMine.this.breakAttempts.getValue() && SpeedMine.this.mode.not(SpeedMine.Mode.GrimInstant)) {
            return true;
         } else if (PlayerUtility.squaredDistanceFromEyes(this.pos.method_46558()) > SpeedMine.this.range.getPow2Value()) {
            this.cancel();
            return true;
         } else if (Module.mc.field_1687.method_22347(this.pos)) {
            this.progress = 0.0F;
            this.prevProgress = -1.0F;
            return false;
         } else {
            if (this.progress == 0.0F && this.prevProgress == -1.0F && SpeedMine.this.mode.is(SpeedMine.Mode.Packet) && this.attackTimer.every(800L)) {
               this.start(dir);
               Module.mc.field_1724.method_6104(class_1268.field_5808);
            }

            int pickSlot = SpeedMine.this.getTool(this.pos);
            int prevSlot = Module.mc.field_1724.method_31548().field_7545;
            if (pickSlot == -1) {
               return false;
            } else {
               boolean instant = this.mineBreaks > 0 && SpeedMine.this.mode.is(SpeedMine.Mode.GrimInstant);
               if (!(this.progress >= 1.0F) && !instant) {
                  this.prevProgress = this.progress;
                  this.progress += SpeedMine.this.getBlockStrength(Module.mc.field_1687.method_8320(this.pos), this.pos);
               } else {
                  if ((Boolean)SpeedMine.this.placeCrystal.getValue()) {
                     SpeedMine.this.placeCrystal();
                  }

                  this.switchTo(pickSlot, -1);
                  if (SpeedMine.this.mode.getValue() != SpeedMine.Mode.GrimInstant && !(Boolean)SpeedMine.this.doubleMine.getValue()) {
                     if ((Boolean)SpeedMine.this.stop.getValue()) {
                        SpeedMine.this.sendPacket(new class_2846(class_2847.field_12973, this.pos, dir));
                     }

                     if ((Boolean)SpeedMine.this.abort.getValue()) {
                        SpeedMine.this.sendPacket(new class_2846(class_2847.field_12971, this.pos, dir));
                     }

                     if ((Boolean)SpeedMine.this.start.getValue()) {
                        SpeedMine.this.sendPacket(new class_2846(class_2847.field_12968, this.pos, dir));
                     }

                     if ((Boolean)SpeedMine.this.stop2.getValue()) {
                        SpeedMine.this.sendPacket(new class_2846(class_2847.field_12973, this.pos, dir));
                     }
                  } else {
                     SpeedMine.this.sendPacket(new class_2846(class_2847.field_12973, this.pos, dir));
                  }

                  if ((Boolean)SpeedMine.this.clientRemove.getValue()) {
                     Module.mc.field_1761.method_2899(this.pos);
                  }

                  int delay = (Boolean)SpeedMine.this.doubleMine.getValue() ? 100 : (Integer)SpeedMine.this.swapDelay.getValue();
                  if (delay != 0) {
                     Managers.ASYNC.run(() -> {
                        this.switchTo(prevSlot, pickSlot);
                     }, (long)delay);
                  } else {
                     this.switchTo(prevSlot, pickSlot);
                  }

                  ++this.mineBreaks;
                  this.progress = this.prevProgress = 0.0F;
                  if ((Boolean)SpeedMine.this.doubleMine.getValue() && SpeedMine.this.mode.is(SpeedMine.Mode.GrimInstant) && SpeedMine.this.actions.size() >= 2) {
                     return true;
                  }
               }

               this.fixMovement();
               return false;
            }
         }
      }

      private void switchTo(int slot, int from) {
         if (SpeedMine.this.switchMode.getValue() != SpeedMine.SwitchMode.Alternative && slot < 9) {
            if (SpeedMine.this.switchMode.is(SpeedMine.SwitchMode.Silent)) {
               InventoryUtility.switchToSilent(slot);
            } else {
               InventoryUtility.switchTo(slot);
            }
         } else {
            if (from == -1) {
               Module.clickSlot(slot < 9 ? slot + 36 : slot, Module.mc.field_1724.method_31548().field_7545, class_1713.field_7791);
            } else {
               Module.clickSlot(from < 9 ? from + 36 : from, Module.mc.field_1724.method_31548().field_7545, class_1713.field_7791);
            }

            SpeedMine.this.closeScreen();
         }

      }

      public void fixMovement() {
         if ((Boolean)SpeedMine.this.rotate.getValue() && (double)this.progress > 0.95D) {
            ModuleManager.rotations.fixRotation = PlayerManager.calcAngle(Module.mc.field_1724.method_33571(), this.pos.method_46558())[0];
         }

      }

      public class_2338 getPos() {
         return this.pos;
      }

      public float getPrevProgress() {
         return this.prevProgress;
      }

      public float getProgress() {
         return this.progress;
      }

      public void onSync() {
         if ((Boolean)SpeedMine.this.rotate.getValue() && (double)this.progress > 0.95D) {
            float[] angle = PlayerManager.calcAngle(Module.mc.field_1724.method_33571(), this.pos.method_46558().method_1031(0.0D, -0.25D, 0.0D));
            Module.mc.field_1724.method_36456(angle[0]);
            Module.mc.field_1724.method_36457(angle[1]);
         }

      }

      public void reset() {
         if (this.progress != 0.0F) {
            this.prevProgress = this.progress = 0.0F;
            class_2350 dir = (class_2350)InteractionUtility.getStrictDirections(this.pos).stream().findFirst().orElse(Module.mc.field_1724.method_5735());
            SpeedMine.this.sendPacket(new class_2846(class_2847.field_12971, this.pos, dir));
            this.start(dir);
         }
      }

      public void cancel() {
         if (this.progress != 0.0F) {
            SpeedMine.this.sendPacket(new class_2846(class_2847.field_12971, this.pos, class_2350.field_11033));
         }

      }

      public boolean instantBreaking() {
         return this.mineBreaks > 0 && SpeedMine.this.mode.is(SpeedMine.Mode.GrimInstant);
      }
   }
}
