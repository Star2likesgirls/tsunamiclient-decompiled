package tsunami.features.modules.render;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.class_1297;
import net.minecraft.class_1533;
import net.minecraft.class_1694;
import net.minecraft.class_1747;
import net.minecraft.class_1792;
import net.minecraft.class_238;
import net.minecraft.class_2480;
import net.minecraft.class_2586;
import net.minecraft.class_2591;
import net.minecraft.class_2595;
import net.minecraft.class_2601;
import net.minecraft.class_2609;
import net.minecraft.class_2611;
import net.minecraft.class_2614;
import net.minecraft.class_2627;
import net.minecraft.class_2646;
import net.minecraft.class_2818;
import net.minecraft.class_3719;
import net.minecraft.class_4587;
import org.jetbrains.annotations.Nullable;
import tsunami.core.Managers;
import tsunami.features.modules.Module;
import tsunami.setting.Setting;
import tsunami.setting.impl.ColorSetting;
import tsunami.utility.render.Render2DEngine;
import tsunami.utility.render.Render3DEngine;

public class StorageEsp extends Module {
   public final Setting<Boolean> outline = new Setting("Outline", true);
   public final Setting<Boolean> fill = new Setting("Fill", true);
   public final Setting<Boolean> chest = new Setting("Chest", true);
   public final Setting<Boolean> trappedChest = new Setting("Trapped Chest", true);
   public final Setting<Boolean> dispenser = new Setting("Dispenser", false);
   public final Setting<Boolean> shulker = new Setting("Shulker", true);
   public final Setting<Boolean> echest = new Setting("Ender Chest", true);
   public final Setting<Boolean> furnace = new Setting("Furnace", false);
   public final Setting<Boolean> hopper = new Setting("Hopper", false);
   public final Setting<Boolean> barrels = new Setting("Barrel", false);
   public final Setting<Boolean> cart = new Setting("Minecart", false);
   public final Setting<Boolean> frame = new Setting("ItemFrame", false);
   private final Setting<ColorSetting> chestColor = new Setting("ChestColor", new ColorSetting(-2013200640));
   private final Setting<ColorSetting> trappedChestColor = new Setting("TrappedChestColor", new ColorSetting(-2013200640));
   private final Setting<ColorSetting> shulkColor = new Setting("ShulkerColor", new ColorSetting(-2013200640));
   private final Setting<ColorSetting> echestColor = new Setting("EChestColor", new ColorSetting(-2013200640));
   private final Setting<ColorSetting> frameColor = new Setting("FrameColor", new ColorSetting(-2013200640));
   private final Setting<ColorSetting> shulkerframeColor = new Setting("ShulkFrameColor", new ColorSetting(-2013200640));
   private final Setting<ColorSetting> furnaceColor = new Setting("FurnaceColor", new ColorSetting(-2013200640));
   private final Setting<ColorSetting> hopperColor = new Setting("HopperColor", new ColorSetting(-2013200640));
   private final Setting<ColorSetting> dispenserColor = new Setting("DispenserColor", new ColorSetting(-2013200640));
   private final Setting<ColorSetting> barrelColor = new Setting("BarrelColor", new ColorSetting(-2013200640));
   private final Setting<ColorSetting> minecartColor = new Setting("MinecartColor", new ColorSetting(-2013200640));

   public StorageEsp() {
      super("StorageEsp", Module.Category.RENDER);
   }

   public void onRender3D(class_4587 stack) {
      if (!mc.field_1690.field_1842) {
         Iterator var2 = getBlockEntities().iterator();

         while(var2.hasNext()) {
            class_2586 blockEntity = (class_2586)var2.next();
            Color color = this.getColor(blockEntity);
            if (color != null) {
               class_238 chestbox = new class_238((double)blockEntity.method_11016().method_10263() + 0.06D, (double)blockEntity.method_11016().method_10264(), (double)blockEntity.method_11016().method_10260() + 0.06D, (double)blockEntity.method_11016().method_10263() + 0.94D, (double)blockEntity.method_11016().method_10264() - 0.125D + 1.0D, (double)blockEntity.method_11016().method_10260() + 0.94D);
               if ((Boolean)this.fill.getValue()) {
                  if (blockEntity instanceof class_2595) {
                     Render3DEngine.drawFilledBox(stack, chestbox, color);
                  } else if (blockEntity instanceof class_2611) {
                     Render3DEngine.drawFilledBox(stack, chestbox, color);
                  } else {
                     Render3DEngine.drawFilledBox(stack, new class_238(blockEntity.method_11016()), color);
                  }
               }

               if ((Boolean)this.outline.getValue()) {
                  if (blockEntity instanceof class_2595) {
                     Render3DEngine.drawBoxOutline(chestbox, Render2DEngine.injectAlpha(color, 255), 1.0F);
                  } else if (blockEntity instanceof class_2611) {
                     Render3DEngine.drawBoxOutline(chestbox, Render2DEngine.injectAlpha(color, 255), 1.0F);
                  } else {
                     Render3DEngine.drawBoxOutline(new class_238(blockEntity.method_11016()), Render2DEngine.injectAlpha(color, 255), 1.0F);
                  }
               }
            }
         }

         var2 = Managers.ASYNC.getAsyncEntities().iterator();

         while(var2.hasNext()) {
            class_1297 ent = (class_1297)var2.next();
            if (ent instanceof class_1533) {
               class_1533 iframe = (class_1533)ent;
               if ((Boolean)this.frame.getValue()) {
                  Color frameColor1 = ((ColorSetting)this.frameColor.getValue()).getColorObject();
                  class_1792 var7 = iframe.method_6940().method_7909();
                  if (var7 instanceof class_1747) {
                     class_1747 bitem = (class_1747)var7;
                     if (bitem.method_7711() instanceof class_2480) {
                        frameColor1 = ((ColorSetting)this.shulkerframeColor.getValue()).getColorObject();
                     }
                  }

                  if ((Boolean)this.fill.getValue()) {
                     Render3DEngine.drawFilledBox(stack, iframe.method_5829(), frameColor1);
                  }

                  if ((Boolean)this.outline.getValue()) {
                     Render3DEngine.drawBoxOutline(iframe.method_5829(), Render2DEngine.injectAlpha(frameColor1, 255), 1.0F);
                  }
               }
            }

            if (ent instanceof class_1694) {
               class_1694 mcart = (class_1694)ent;
               if ((Boolean)this.cart.getValue()) {
                  if ((Boolean)this.fill.getValue()) {
                     Render3DEngine.drawFilledBox(stack, mcart.method_5829(), ((ColorSetting)this.minecartColor.getValue()).getColorObject());
                  }

                  if ((Boolean)this.outline.getValue()) {
                     Render3DEngine.drawBoxOutline(mcart.method_5829(), Render2DEngine.injectAlpha(((ColorSetting)this.minecartColor.getValue()).getColorObject(), 255), 1.0F);
                  }
               }
            }
         }

      }
   }

   @Nullable
   private Color getColor(class_2586 bEnt) {
      Color color = null;
      if (bEnt instanceof class_2646 && (Boolean)this.trappedChest.getValue()) {
         color = ((ColorSetting)this.trappedChestColor.getValue()).getColorObject();
      } else if (bEnt instanceof class_2595 && (Boolean)this.chest.getValue() && bEnt.method_11017() != class_2591.field_11891) {
         color = ((ColorSetting)this.chestColor.getValue()).getColorObject();
      } else if (bEnt instanceof class_2611 && (Boolean)this.echest.getValue()) {
         color = ((ColorSetting)this.echestColor.getValue()).getColorObject();
      } else if (bEnt instanceof class_3719 && (Boolean)this.barrels.getValue()) {
         color = ((ColorSetting)this.barrelColor.getValue()).getColorObject();
      } else if (bEnt instanceof class_2627 && (Boolean)this.shulker.getValue()) {
         color = ((ColorSetting)this.shulkColor.getValue()).getColorObject();
      } else if (bEnt instanceof class_2609 && (Boolean)this.furnace.getValue()) {
         color = ((ColorSetting)this.furnaceColor.getValue()).getColorObject();
      } else if (bEnt instanceof class_2601 && (Boolean)this.dispenser.getValue()) {
         color = ((ColorSetting)this.dispenserColor.getValue()).getColorObject();
      } else if (bEnt instanceof class_2614 && (Boolean)this.hopper.getValue()) {
         color = ((ColorSetting)this.hopperColor.getValue()).getColorObject();
      }

      return color;
   }

   public static List<class_2586> getBlockEntities() {
      List<class_2586> list = new ArrayList();
      Iterator var1 = getLoadedChunks().iterator();

      while(var1.hasNext()) {
         class_2818 chunk = (class_2818)var1.next();
         list.addAll(chunk.method_12214().values());
      }

      return list;
   }

   public static List<class_2818> getLoadedChunks() {
      List<class_2818> chunks = new ArrayList();
      int viewDist = (Integer)mc.field_1690.method_42503().method_41753();

      for(int x = -viewDist; x <= viewDist; ++x) {
         for(int z = -viewDist; z <= viewDist; ++z) {
            class_2818 chunk = mc.field_1687.method_2935().method_21730((int)mc.field_1724.method_23317() / 16 + x, (int)mc.field_1724.method_23321() / 16 + z);
            if (chunk != null) {
               chunks.add(chunk);
            }
         }
      }

      return chunks;
   }
}
