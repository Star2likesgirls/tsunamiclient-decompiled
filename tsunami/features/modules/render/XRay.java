package tsunami.features.modules.render;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ThreadLocalRandom;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_1268;
import net.minecraft.class_1810;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2338;
import net.minecraft.class_238;
import net.minecraft.class_2596;
import net.minecraft.class_2626;
import net.minecraft.class_2879;
import net.minecraft.class_332;
import net.minecraft.class_3532;
import net.minecraft.class_4587;
import org.jetbrains.annotations.NotNull;
import tsunami.events.impl.EventMove;
import tsunami.events.impl.EventSetting;
import tsunami.events.impl.PacketEvent;
import tsunami.features.modules.Module;
import tsunami.features.modules.client.ClientSettings;
import tsunami.features.modules.client.HudEditor;
import tsunami.gui.font.FontRenderers;
import tsunami.setting.Setting;
import tsunami.utility.Timer;
import tsunami.utility.math.FrameRateCounter;
import tsunami.utility.math.MathUtility;
import tsunami.utility.player.MovementUtility;
import tsunami.utility.render.Render2DEngine;
import tsunami.utility.render.Render3DEngine;

public class XRay extends Module {
   private final Setting<XRay.Plugin> plugin;
   public final Setting<Boolean> wallHack;
   private final Setting<Boolean> brutForce;
   private final Setting<Boolean> fast;
   private final Setting<Integer> delay;
   private final Setting<Integer> radius;
   private final Setting<Integer> up;
   private final Setting<Integer> down;
   private static final Setting<Boolean> netherite = new Setting("Netherite", false);
   private static final Setting<Boolean> diamond = new Setting("Diamond ", false);
   private static final Setting<Boolean> gold = new Setting("Gold", false);
   private static final Setting<Boolean> iron = new Setting("Iron", false);
   private static final Setting<Boolean> emerald = new Setting("Emerald", false);
   private static final Setting<Boolean> redstone = new Setting("Redstone", false);
   private static final Setting<Boolean> lapis = new Setting("Lapis", false);
   private static final Setting<Boolean> coal = new Setting("Coal", false);
   private static final Setting<Boolean> quartz = new Setting("Quartz", false);
   private static final Setting<Boolean> water = new Setting("Water", false);
   private static final Setting<Boolean> lava = new Setting("Lava", false);
   private final Timer delayTimer;
   private final ArrayList<class_2338> ores;
   private final ArrayList<class_2338> toCheck;
   private final ArrayList<XRay.BlockMemory> checked;
   private class_2338 displayBlock;
   private int done;
   private int all;
   private class_238 area;

   public XRay() {
      super("XRay", Module.Category.MISC);
      this.plugin = new Setting("Plugin", XRay.Plugin.New);
      this.wallHack = new Setting("WallHack", false);
      this.brutForce = new Setting("OreDeobf", false);
      this.fast = new Setting("Fast", false, (v) -> {
         return (Boolean)this.brutForce.getValue();
      });
      this.delay = new Setting("Delay", 25, 1, 100, (v) -> {
         return (Boolean)this.brutForce.getValue();
      });
      this.radius = new Setting("Radius", 5, 1, 64, (v) -> {
         return (Boolean)this.brutForce.getValue();
      });
      this.up = new Setting("Up", 5, 1, 32, (v) -> {
         return (Boolean)this.brutForce.getValue();
      });
      this.down = new Setting("Down", 5, 1, 32, (v) -> {
         return (Boolean)this.brutForce.getValue();
      });
      this.delayTimer = new Timer();
      this.ores = new ArrayList();
      this.toCheck = new ArrayList();
      this.checked = new ArrayList();
      this.area = new class_238(class_2338.field_10980);
   }

   public void onEnable() {
      this.ores.clear();
      this.toCheck.clear();
      this.checked.clear();
      this.toCheck.addAll(this.getBlocks());
      this.all = this.toCheck.size();
      this.done = 0;
      mc.field_1730 = false;
      mc.field_1769.method_3279();
      this.area = this.getArea();
   }

   public void onDisable() {
      mc.field_1769.method_3279();
      mc.field_1730 = true;
   }

   @EventHandler
   public void onReceivePacket(PacketEvent.Receive e) {
      class_2596 var3 = e.getPacket();
      if (var3 instanceof class_2626) {
         class_2626 pac = (class_2626)var3;
         String var10001 = ((class_2626)e.getPacket()).method_11308().method_26204().method_9518().getString();
         this.debug(var10001 + " " + pac.method_11309().toString());
         if (isCheckableOre(pac.method_11308().method_26204()) && !this.ores.contains(pac.method_11309())) {
            this.ores.add(pac.method_11309());
         }
      }

   }

   @EventHandler
   public void onSettingChange(EventSetting e) {
      if (e.getSetting() == this.wallHack) {
         mc.field_1769.method_3279();
      }

   }

   @EventHandler
   public void onMove(EventMove e) {
      if ((Boolean)this.brutForce.getValue()) {
         if (this.all != this.done) {
            e.setZ(0.0D);
            e.setX(0.0D);
            e.cancel();
            if (mc.field_1724.field_6012 % 8 == 0 && MovementUtility.isMoving()) {
               this.sendMessage(ClientSettings.isRu() ? "Не двигайся пока идет деобфускация!" : "Don't move while deobf!");
            }
         } else {
            class_238 newArea = this.getArea();
            if (!newArea.method_994(this.area)) {
               this.area = newArea;
               this.toCheck.clear();
               this.toCheck.addAll(this.getBlocks());
               this.checked.clear();
               this.all = this.toCheck.size();
               this.done = 0;
            }
         }
      }

   }

   @NotNull
   private class_238 getArea() {
      int radius_ = this.plugin.is(XRay.Plugin.New) ? Math.min(4, (Integer)this.radius.getValue()) : (Integer)this.radius.getValue();
      int down_ = this.plugin.is(XRay.Plugin.New) ? Math.min(3, (Integer)this.down.getValue()) : (Integer)this.down.getValue();
      int up_ = this.plugin.is(XRay.Plugin.New) ? Math.min(4, (Integer)this.up.getValue()) : (Integer)this.up.getValue();
      return new class_238(mc.field_1724.method_23317() - (double)radius_, mc.field_1724.method_23318() - (double)down_, mc.field_1724.method_23321() - (double)radius_, mc.field_1724.method_23317() + (double)radius_, mc.field_1724.method_23318() + (double)up_, mc.field_1724.method_23321() + (double)radius_);
   }

   public void onUpdate() {
      if (this.plugin.is(XRay.Plugin.New)) {
         this.checked.forEach((blockMemory) -> {
            if (blockMemory.isDelayed() && !this.ores.contains(blockMemory.bp)) {
               this.ores.add(blockMemory.bp);
            }

         });
      }

   }

   public void onRender3D(class_4587 stack) {
      Iterator var2 = this.ores.iterator();

      while(var2.hasNext()) {
         class_2338 pos = (class_2338)var2.next();
         class_2248 block = mc.field_1687.method_8320(pos).method_26204();
         if ((block == class_2246.field_10442 || block == class_2246.field_29029) && (Boolean)diamond.getValue()) {
            this.draw(pos, 0, 255, 255);
         }

         if ((block == class_2246.field_10571 || block == class_2246.field_29026) && (Boolean)gold.getValue()) {
            this.draw(pos, 255, 215, 0);
         }

         if (block == class_2246.field_23077 && (Boolean)gold.getValue()) {
            this.draw(pos, 255, 215, 0);
         }

         if ((block == class_2246.field_10212 || block == class_2246.field_29027) && (Boolean)iron.getValue()) {
            this.draw(pos, 213, 213, 213);
         }

         if ((block == class_2246.field_10013 || block == class_2246.field_29220) && (Boolean)emerald.getValue()) {
            this.draw(pos, 0, 255, 77);
         }

         if ((block == class_2246.field_10080 || block == class_2246.field_29030) && (Boolean)redstone.getValue()) {
            this.draw(pos, 255, 0, 0);
         }

         if (block == class_2246.field_10418 && (Boolean)coal.getValue()) {
            this.draw(pos, 0, 0, 0);
         }

         if ((block == class_2246.field_10090 || block == class_2246.field_29028) && (Boolean)lapis.getValue()) {
            this.draw(pos, 38, 97, 156);
         }

         if (block == class_2246.field_22109 && (Boolean)netherite.getValue()) {
            this.draw(pos, 255, 255, 255);
         }

         if (block == class_2246.field_10213 && (Boolean)quartz.getValue()) {
            this.draw(pos, 170, 170, 170);
         }
      }

      if (this.displayBlock != null && this.done != this.all) {
         this.draw(this.displayBlock, 255, 0, 60);
      }

      if ((Boolean)this.brutForce.getValue()) {
         Render3DEngine.OUTLINE_QUEUE.add(new Render3DEngine.OutlineAction(this.area, HudEditor.getColor(1), 2.0F));
      }

      if (!this.toCheck.isEmpty() && (Boolean)this.brutForce.getValue()) {
         if (mc.method_1542()) {
            this.disable(ClientSettings.isRu() ? "Братан, ты в синглплеере" : "Bro, you're in singleplayer");
         } else if (mc.field_1724.method_6047().method_7909() instanceof class_1810) {
            if (mc.field_1724.field_6012 % 8 == 0) {
               this.disable(ClientSettings.isRu() ? "Убери кирку из руки!" : "Remove pickaxe from ur hand!");
            }

         } else {
            if (this.delayTimer.every((long)(Integer)this.delay.getValue())) {
               class_2338 pos = (class_2338)this.toCheck.remove(this.toCheck.size() - 1 <= 1 ? 0 : ThreadLocalRandom.current().nextInt(0, this.toCheck.size() - 1));
               mc.field_1761.method_2910(this.displayBlock = pos, mc.field_1724.method_5735());
               mc.field_1761.method_2925();
               this.sendPacket(new class_2879(class_1268.field_5808));
               this.checked.add(new XRay.BlockMemory(this, pos));
               ++this.done;
            }

         }
      }
   }

   private void draw(class_2338 pos, int r, int g, int b) {
      Render3DEngine.FILLED_QUEUE.add(new Render3DEngine.FillAction(new class_238(pos), new Color(r, g, b, 100)));
      Render3DEngine.OUTLINE_QUEUE.add(new Render3DEngine.OutlineAction(new class_238(pos), new Color(r, g, b, 200), 2.0F));
   }

   public void onRender2D(class_332 context) {
      if ((Boolean)this.brutForce.getValue()) {
         float posX = (float)mc.method_22683().method_4486() / 2.0F - 68.0F;
         float posY = (float)mc.method_22683().method_4502() / 2.0F + 68.0F;
         Render2DEngine.drawGradientBlurredShadow(context.method_51448(), posX + 2.0F, posY + 2.0F, 133.0F, 44.0F, 14, HudEditor.getColor(270), HudEditor.getColor(0), HudEditor.getColor(180), HudEditor.getColor(90));
         Render2DEngine.renderRoundedGradientRect(context.method_51448(), HudEditor.getColor(270), HudEditor.getColor(0), HudEditor.getColor(180), HudEditor.getColor(90), posX, posY, 137.0F, 47.5F, 9.0F);
         Render2DEngine.drawRound(context.method_51448(), posX + 0.5F, posY + 0.5F, 136.0F, 46.0F, 9.0F, Render2DEngine.injectAlpha(Color.BLACK, 220));
         Render2DEngine.drawGradientRound(context.method_51448(), posX + 4.0F, posY + 32.0F, 129.0F, 11.0F, 4.0F, HudEditor.getColor(0).darker().darker(), HudEditor.getColor(0).darker().darker().darker().darker(), HudEditor.getColor(0).darker().darker().darker().darker(), HudEditor.getColor(0).darker().darker().darker().darker());
         Render2DEngine.renderRoundedGradientRect(context.method_51448(), HudEditor.getColor(270), HudEditor.getColor(0), HudEditor.getColor(0), HudEditor.getColor(270), posX + 4.0F, posY + 32.0F, (float)((int)class_3532.method_15363(129.0F * ((float)this.done / Math.max((float)this.all, 1.0F)), 8.0F, 129.0F)), 11.0F, 4.0F);
         FontRenderers.sf_bold.drawCenteredString(context.method_51448(), (int)((float)this.done / (float)this.all * 100.0F) + "%", (double)(posX + 68.0F), (double)(posY + 35.0F), -1);
         FontRenderers.sf_bold.drawCenteredString(context.method_51448(), "XRay", (double)(posX + 68.0F), (double)(posY + 7.0F), -1);
         double time = 0.0D;

         try {
            time = MathUtility.round((double)(this.all - this.done) * ((1000.0D / (double)FrameRateCounter.INSTANCE.getFps() + (double)(Integer)this.delay.getValue()) / 1000.0D), 1);
         } catch (NumberFormatException var7) {
         }

         FontRenderers.sf_bold.drawCenteredString(context.method_51448(), this.done + " / " + this.all + (ClientSettings.isRu() ? " Осталось: " : " Estimated time: ") + time + "s", (double)(posX + 68.0F), (double)(posY + 18.0F), -1);
      }

   }

   public static boolean isCheckableOre(class_2248 block) {
      if (!(Boolean)diamond.getValue() || block != class_2246.field_10442 && block != class_2246.field_29029) {
         if (!(Boolean)gold.getValue() || block != class_2246.field_10571 && block != class_2246.field_29026 && block != class_2246.field_23077) {
            if ((Boolean)iron.getValue() && (block == class_2246.field_10212 || block == class_2246.field_29027)) {
               return true;
            } else if (!(Boolean)emerald.getValue() || block != class_2246.field_10013 && block != class_2246.field_29220) {
               if (!(Boolean)redstone.getValue() || block != class_2246.field_10080 && block != class_2246.field_29030) {
                  if (!(Boolean)coal.getValue() || block != class_2246.field_10418 && block != class_2246.field_29219) {
                     if ((Boolean)netherite.getValue() && block == class_2246.field_22109) {
                        return true;
                     } else if ((Boolean)water.getValue() && block == class_2246.field_10382) {
                        return true;
                     } else if ((Boolean)lava.getValue() && block == class_2246.field_10164) {
                        return true;
                     } else if ((Boolean)quartz.getValue() && block == class_2246.field_10213) {
                        return true;
                     } else if ((Boolean)lapis.getValue() && (block == class_2246.field_10090 || block == class_2246.field_29028)) {
                        return true;
                     } else {
                        return (Boolean)lapis.getValue() && (block == class_2246.field_10090 || block == class_2246.field_29028);
                     }
                  } else {
                     return true;
                  }
               } else {
                  return true;
               }
            } else {
               return true;
            }
         } else {
            return true;
         }
      } else {
         return true;
      }
   }

   private ArrayList<class_2338> getBlocks() {
      int radius_ = this.plugin.is(XRay.Plugin.New) ? Math.min(4, (Integer)this.radius.getValue()) : (Integer)this.radius.getValue();
      int down_ = this.plugin.is(XRay.Plugin.New) ? Math.min(3, (Integer)this.down.getValue()) : (Integer)this.down.getValue();
      int up_ = this.plugin.is(XRay.Plugin.New) ? Math.min(4, (Integer)this.up.getValue()) : (Integer)this.up.getValue();
      ArrayList<class_2338> positions = new ArrayList();

      for(int x = (int)(mc.field_1724.method_23317() - (double)radius_); (double)x < mc.field_1724.method_23317() + (double)radius_; ++x) {
         for(int y = (int)(mc.field_1724.method_23318() - (double)down_); (double)y < mc.field_1724.method_23318() + (double)up_; ++y) {
            for(int z = (int)(mc.field_1724.method_23321() - (double)radius_); (double)z < mc.field_1724.method_23321() + (double)radius_; ++z) {
               class_2338 pos = new class_2338(x, y, z);
               if (!mc.field_1687.method_22347(pos) && (!(Boolean)this.fast.getValue() || !this.plugin.is(XRay.Plugin.Old) || x % 2 != 0 && y % 2 != 0 && z % 2 != 0)) {
                  positions.add(pos);
               }
            }
         }
      }

      return positions;
   }

   private static enum Plugin {
      Old,
      New;

      // $FF: synthetic method
      private static XRay.Plugin[] $values() {
         return new XRay.Plugin[]{Old, New};
      }
   }

   public class BlockMemory {
      private final class_2338 bp;
      private long time = 0L;

      public BlockMemory(final XRay this$0, class_2338 bp) {
         this.bp = bp;
      }

      private boolean isDelayed() {
         return this.time++ > 10L;
      }
   }
}
