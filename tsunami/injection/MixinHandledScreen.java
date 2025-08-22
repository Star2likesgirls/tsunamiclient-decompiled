package tsunami.injection;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import net.minecraft.class_1277;
import net.minecraft.class_1703;
import net.minecraft.class_1713;
import net.minecraft.class_1733;
import net.minecraft.class_1735;
import net.minecraft.class_1747;
import net.minecraft.class_1767;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1806;
import net.minecraft.class_22;
import net.minecraft.class_2480;
import net.minecraft.class_2561;
import net.minecraft.class_308;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_3675;
import net.minecraft.class_3936;
import net.minecraft.class_437;
import net.minecraft.class_465;
import net.minecraft.class_9209;
import net.minecraft.class_9288;
import net.minecraft.class_9334;
import net.minecraft.class_4597.class_4598;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tsunami.core.Core;
import tsunami.core.manager.client.ModuleManager;
import tsunami.features.modules.Module;
import tsunami.features.modules.render.Tooltips;
import tsunami.gui.misc.PeekScreen;
import tsunami.utility.Timer;
import tsunami.utility.render.Render2DEngine;
import tsunami.utility.render.TextureStorage;

@Mixin({class_465.class})
public abstract class MixinHandledScreen<T extends class_1703> extends class_437 implements class_3936<T> {
   @Unique
   private final Timer delayTimer = new Timer();
   @Unique
   private Runnable postRender;
   @Shadow
   @Nullable
   protected class_1735 field_2787;
   @Shadow
   protected int field_2776;
   @Shadow
   protected int field_2800;
   private static final class_1799[] ITEMS = new class_1799[27];
   private Map<Render2DEngine.Rectangle, Integer> clickableRects = new HashMap();

   protected MixinHandledScreen(class_2561 title) {
      super(title);
   }

   @Shadow
   protected abstract boolean method_2387(class_1735 var1, double var2, double var4);

   @Shadow
   protected abstract void method_2383(class_1735 var1, int var2, int var3, class_1713 var4);

   @Inject(
      method = {"render"},
      at = {@At("HEAD")}
   )
   private void drawScreenHook(class_332 context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
      if (!Module.fullNullCheck()) {
         for(int i1 = 0; i1 < Module.mc.field_1724.field_7512.field_7761.size(); ++i1) {
            class_1735 slot = (class_1735)Module.mc.field_1724.field_7512.field_7761.get(i1);
            if (this.method_2387(slot, (double)mouseX, (double)mouseY) && slot.method_7682() && ModuleManager.itemScroller.isEnabled() && this.shit() && this.attack() && this.delayTimer.passedMs((long)(Integer)ModuleManager.itemScroller.delay.getValue())) {
               this.method_2383(slot, slot.field_7874, 0, class_1713.field_7794);
               this.delayTimer.reset();
            }
         }

      }
   }

   private boolean shit() {
      return class_3675.method_15987(class_310.method_1551().method_22683().method_4490(), 340) || class_3675.method_15987(class_310.method_1551().method_22683().method_4490(), 344);
   }

   private boolean attack() {
      return Core.hold_mouse0;
   }

   @Inject(
      method = {"render"},
      at = {@At("TAIL")}
   )
   private void onRender(class_332 context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
      if (!Module.fullNullCheck()) {
         if (this.field_2787 != null && !this.field_2787.method_7677().method_7960() && this.field_22787.field_1724.field_7498.method_34255().method_7960()) {
            if (Tooltips.hasItems(this.field_2787.method_7677()) && (Boolean)Tooltips.storage.getValue()) {
               this.renderShulkerToolTip(context, mouseX, mouseY, 0, 0, this.field_2787.method_7677());
            } else if (this.field_2787.method_7677().method_7909() == class_1802.field_8204 && (Boolean)Tooltips.maps.getValue()) {
               this.drawMapPreview(context, this.field_2787.method_7677(), mouseX, mouseY);
            }
         }

         int xOffset = 0;
         int yOffset = 20;
         int stage = false;
         if (ModuleManager.tooltips.isEnabled() && (Boolean)ModuleManager.tooltips.shulkerRegear.getValue()) {
            this.clickableRects.clear();

            for(int i1 = 0; i1 < Module.mc.field_1724.field_7512.field_7761.size(); ++i1) {
               class_1735 slot = (class_1735)Module.mc.field_1724.field_7512.field_7761.get(i1);
               if (!slot.method_7677().method_7960()) {
                  class_1792 var12 = slot.method_7677().method_7909();
                  if (var12 instanceof class_1747) {
                     class_1747 bi = (class_1747)var12;
                     if (bi.method_7711() instanceof class_2480 && this.renderShulkerToolTip(context, xOffset, yOffset + 67, mouseX, mouseY, slot.method_7677())) {
                        this.clickableRects.put(new Render2DEngine.Rectangle((float)xOffset, (float)yOffset, (float)(xOffset + 176), (float)(yOffset + 67)), slot.field_7874);
                        yOffset += 67;
                        if (!stage) {
                           if (yOffset + 67 >= Module.mc.method_22683().method_4502()) {
                              yOffset = 20;
                              xOffset = Module.mc.method_22683().method_4486() - 176;
                              stage = true;
                           }
                        } else if (stage) {
                           if (yOffset + 67 >= Module.mc.method_22683().method_4502()) {
                              yOffset = 20;
                              xOffset = 170;
                              stage = true;
                           }
                        } else if (yOffset + 67 >= Module.mc.method_22683().method_4502()) {
                           yOffset = 20;
                           xOffset = Module.mc.method_22683().method_4486() - 352;
                           stage = false;
                        }
                     }
                  }
               }
            }

            if (this.postRender != null) {
               this.postRender.run();
               this.postRender = null;
            }
         }

      }
   }

   @Inject(
      method = {"drawSlot(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/screen/slot/Slot;)V"},
      at = {@At("TAIL")}
   )
   protected void drawSlotHook(class_332 context, class_1735 slot, CallbackInfo ci) {
      if (ModuleManager.serverHelper.isEnabled() && (Boolean)ModuleManager.serverHelper.aucHelper.getValue()) {
         ModuleManager.serverHelper.onRenderChest(context, slot);
      }

   }

   public boolean renderShulkerToolTip(class_332 context, int offsetX, int offsetY, int mouseX, int mouseY, class_1799 stack) {
      try {
         class_9288 compoundTag = (class_9288)stack.method_57824(class_9334.field_49622);
         if (compoundTag == null) {
            return false;
         } else {
            float[] colors = new float[]{1.0F, 1.0F, 1.0F};
            class_1792 focusedItem = stack.method_7909();
            if (focusedItem instanceof class_1747) {
               class_1747 bi = (class_1747)focusedItem;
               if (bi.method_7711() instanceof class_2480) {
                  try {
                     Color c = new Color(((class_1767)Objects.requireNonNull(class_2480.method_10527(stack.method_7909()))).method_7787());
                     colors = new float[]{(float)c.getRed() / 255.0F, (float)c.getGreen() / 255.0F, (float)c.getRed() / 255.0F, (float)c.getAlpha() / 255.0F};
                  } catch (NullPointerException var12) {
                     colors = new float[]{1.0F, 1.0F, 1.0F};
                  }
               }
            }

            this.draw(context, compoundTag.method_57489().toList(), offsetX, offsetY, mouseX, mouseY, colors);
            return true;
         }
      } catch (Exception var13) {
         return false;
      }
   }

   @Inject(
      method = {"drawMouseoverTooltip"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onDrawMouseoverTooltip(class_332 context, int x, int y, CallbackInfo ci) {
      if (!Module.fullNullCheck()) {
         if (this.field_2787 != null && !this.field_2787.method_7677().method_7960() && this.field_22787.field_1724.field_7498.method_34255().method_7960() && this.field_2787.method_7677().method_7909() == class_1802.field_8204 && (Boolean)Tooltips.maps.getValue()) {
            ci.cancel();
         }

      }
   }

   @Unique
   private void draw(class_332 context, List<class_1799> itemStacks, int offsetX, int offsetY, int mouseX, int mouseY, float[] colors) {
      RenderSystem.disableDepthTest();
      GL11.glClear(256);
      offsetX += 8;
      offsetY -= 82;
      this.drawBackground(context, offsetX, offsetY, colors);
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      class_308.method_24211();
      int row = 0;
      int i = 0;
      Iterator var10 = itemStacks.iterator();

      while(var10.hasNext()) {
         class_1799 itemStack = (class_1799)var10.next();
         context.method_51427(itemStack, offsetX + 8 + i * 18, offsetY + 7 + row * 18);
         context.method_51431(Module.mc.field_1772, itemStack, offsetX + 8 + i * 18, offsetY + 7 + row * 18);
         if (mouseX > offsetX + 8 + i * 18 && mouseX < offsetX + 28 + i * 18 && mouseY > offsetY + 7 + row * 18 && mouseY < offsetY + 27 + row * 18) {
            this.postRender = () -> {
               context.method_51437(this.field_22793, method_25408(Module.mc, itemStack), itemStack.method_32347(), mouseX, mouseY);
            };
         }

         ++i;
         if (i >= 9) {
            i = 0;
            ++row;
         }
      }

      class_308.method_24210();
      RenderSystem.enableDepthTest();
   }

   private void drawBackground(class_332 context, int x, int y, float[] colors) {
      RenderSystem.disableBlend();
      RenderSystem.setShaderColor(colors[0], colors[1], colors[2], 1.0F);
      RenderSystem.texParameter(3553, 10240, 9729);
      RenderSystem.texParameter(3553, 10241, 9987);
      context.method_25290(TextureStorage.container, x, y, 0.0F, 0.0F, 176, 67, 176, 67);
      RenderSystem.enableBlend();
   }

   private void drawMapPreview(class_332 context, class_1799 stack, int x, int y) {
      RenderSystem.enableBlend();
      context.method_51448().method_22903();
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      int y1 = y - 12;
      int x1 = x + 8;
      int z = true;
      class_22 mapState = class_1806.method_8001(stack, this.field_22787.field_1687);
      if (mapState != null) {
         mapState.method_101(this.field_22787.field_1724);
         x1 += 8;
         y1 += 8;
         int z = 310;
         double scale = 0.65625D;
         context.method_51448().method_46416((float)x1, (float)y1, (float)z);
         context.method_51448().method_22905((float)scale, (float)scale, 0.0F);
         class_4598 consumer = this.field_22787.method_22940().method_23000();
         this.field_22787.field_1773.method_3194().method_1773(context.method_51448(), consumer, (class_9209)stack.method_57824(class_9334.field_49646), mapState, false, 15728880);
      }

      context.method_51448().method_22909();
   }

   @Inject(
      method = {"mouseClicked"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void mouseClicked(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
      if (!Module.fullNullCheck()) {
         if (button == 2 && this.field_2787 != null && !this.field_2787.method_7677().method_7960() && this.field_22787.field_1724.field_7498.method_34255().method_7960()) {
            class_1799 itemStack = this.field_2787.method_7677();
            if (Tooltips.hasItems(itemStack) && (Boolean)Tooltips.middleClickOpen.getValue()) {
               Arrays.fill(ITEMS, class_1799.field_8037);
               class_9288 nbt = (class_9288)itemStack.method_57824(class_9334.field_49622);
               if (nbt != null) {
                  List<class_1799> list = nbt.method_57489().toList();

                  for(int i = 0; i < list.size(); ++i) {
                     ITEMS[i] = (class_1799)list.get(i);
                  }
               }

               this.field_22787.method_1507(new PeekScreen(new class_1733(0, this.field_22787.field_1724.method_31548(), new class_1277(ITEMS)), this.field_22787.field_1724.method_31548(), this.field_2787.method_7677().method_7964(), ((class_1747)this.field_2787.method_7677().method_7909()).method_7711()));
               cir.setReturnValue(true);
            }
         }

         Iterator var11 = this.clickableRects.keySet().iterator();

         while(var11.hasNext()) {
            Render2DEngine.Rectangle rect = (Render2DEngine.Rectangle)var11.next();
            if (rect.contains(mouseX, mouseY)) {
               if ((Boolean)ModuleManager.tooltips.shulkerRegearShiftMode.getValue()) {
                  Module.mc.field_1761.method_2906(Module.mc.field_1724.field_7512.field_7763, (Integer)this.clickableRects.get(rect), 0, class_1713.field_7794, Module.mc.field_1724);
               } else {
                  Module.mc.field_1761.method_2906(Module.mc.field_1724.field_7512.field_7763, (Integer)this.clickableRects.get(rect), 0, class_1713.field_7790, Module.mc.field_1724);
               }
            }
         }

      }
   }
}
