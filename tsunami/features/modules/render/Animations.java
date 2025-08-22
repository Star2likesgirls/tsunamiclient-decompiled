package tsunami.features.modules.render;

import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_1268;
import net.minecraft.class_1306;
import net.minecraft.class_1309;
import net.minecraft.class_1764;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_2879;
import net.minecraft.class_3532;
import net.minecraft.class_4587;
import net.minecraft.class_4597;
import net.minecraft.class_4608;
import net.minecraft.class_742;
import net.minecraft.class_7833;
import net.minecraft.class_811;
import org.jetbrains.annotations.NotNull;
import tsunami.TsunamiClient;
import tsunami.core.manager.client.ModuleManager;
import tsunami.events.impl.EventHeldItemRenderer;
import tsunami.events.impl.PacketEvent;
import tsunami.features.modules.Module;
import tsunami.features.modules.combat.Aura;
import tsunami.injection.accesors.IHeldItemRenderer;
import tsunami.setting.Setting;

public class Animations extends Module {
   private final Setting<Boolean> onlyaura = new Setting("OnlyAura", false);
   public Setting<Boolean> oldAnimationsM = new Setting("DisableSwapMain", true);
   public Setting<Boolean> oldAnimationsOff = new Setting("DisableSwapOff", true);
   private final Setting<Animations.Mode> mode;
   public static Setting<Boolean> slowAnimation = new Setting("SlowAnimation", true);
   public static Setting<Integer> slowAnimationVal = new Setting("SlowValue", 12, 1, 50);
   public boolean flip;

   public Animations() {
      super("Animations", Module.Category.RENDER);
      this.mode = new Setting("Mode", Animations.Mode.Default);
   }

   public boolean shouldAnimate() {
      return this.isEnabled() && (!(Boolean)this.onlyaura.getValue() || ModuleManager.aura.isEnabled() && Aura.target != null) && this.mode.getValue() != Animations.Mode.Normal;
   }

   public boolean shouldChangeAnimationDuration() {
      return this.isEnabled() && (!(Boolean)this.onlyaura.getValue() || ModuleManager.aura.isEnabled() && Aura.target != null);
   }

   public void onUpdate() {
      if (!fullNullCheck()) {
         if ((Boolean)this.oldAnimationsM.getValue() && ((IHeldItemRenderer)mc.method_1561().method_43336()).getEquippedProgressMainHand() <= 1.0F) {
            ((IHeldItemRenderer)mc.method_1561().method_43336()).setEquippedProgressMainHand(1.0F);
            ((IHeldItemRenderer)mc.method_1561().method_43336()).setItemStackMainHand(mc.field_1724.method_6047());
         }

         if ((Boolean)this.oldAnimationsOff.getValue() && ((IHeldItemRenderer)mc.method_1561().method_43336()).getEquippedProgressOffHand() <= 1.0F) {
            ((IHeldItemRenderer)mc.method_1561().method_43336()).setEquippedProgressOffHand(1.0F);
            ((IHeldItemRenderer)mc.method_1561().method_43336()).setItemStackOffHand(mc.field_1724.method_6079());
         }

      }
   }

   @EventHandler
   public void onPacketSend(PacketEvent.Send e) {
      if (e.getPacket() instanceof class_2879) {
         this.flip = !this.flip;
      }

   }

   private void renderSwordAnimation(class_4587 matrices, float f, float swingProgress, float equipProgress, class_1306 arm) {
      if (arm == class_1306.field_6182 && (this.mode.getValue() == Animations.Mode.Eleven || this.mode.getValue() == Animations.Mode.Ten || this.mode.getValue() == Animations.Mode.Nine || this.mode.getValue() == Animations.Mode.Three || this.mode.getValue() == Animations.Mode.Thirteen || this.mode.getValue() == Animations.Mode.Fourteen)) {
         this.applyEquipOffset(matrices, arm, equipProgress);
         matrices.method_46416(-(Float)ModuleManager.viewModel.positionMainX.getValue(), (Float)ModuleManager.viewModel.positionMainY.getValue(), (Float)ModuleManager.viewModel.positionMainZ.getValue());
         this.applySwingOffset(matrices, arm, swingProgress);
         matrices.method_46416((Float)ModuleManager.viewModel.positionMainX.getValue(), -(Float)ModuleManager.viewModel.positionMainY.getValue(), -(Float)ModuleManager.viewModel.positionMainZ.getValue());
      } else {
         float g;
         float m;
         float f1;
         switch(((Animations.Mode)this.mode.getValue()).ordinal()) {
         case 1:
            this.applyEquipOffset(matrices, arm, equipProgress);
            this.translateToViewModelOff(matrices);
            this.applySwingOffset(matrices, arm, swingProgress);
            this.translateBacklOff(matrices);
            break;
         case 2:
            g = -0.4F * class_3532.method_15374(class_3532.method_15355(swingProgress) * 3.1415927F);
            this.applyEquipOffset(matrices, arm, g);
            int i = arm == class_1306.field_6183 ? 1 : -1;
            this.translateToViewModel(matrices);
            f1 = class_3532.method_15374(swingProgress * swingProgress * 3.1415927F);
            matrices.method_22907(class_7833.field_40716.rotationDegrees((float)i * (45.0F + f1 * -20.0F)));
            float g = class_3532.method_15374(class_3532.method_15355(swingProgress) * 3.1415927F);
            matrices.method_22907(class_7833.field_40718.rotationDegrees((float)i * g * -20.0F));
            matrices.method_22907(class_7833.field_40714.rotationDegrees(g * 0.0F));
            matrices.method_22907(class_7833.field_40716.rotationDegrees((float)i * -45.0F));
            this.translateBack(matrices);
            break;
         case 3:
            this.applyEquipOffset(matrices, arm, 0.2F * class_3532.method_15374(class_3532.method_15355(swingProgress) * 6.2831855F));
            break;
         case 4:
            g = -0.4F * class_3532.method_15374(class_3532.method_15355(swingProgress) * 3.1415927F);
            m = class_3532.method_15374(class_3532.method_15355(swingProgress) * 3.1415927F);
            this.applyEquipOffset(matrices, arm, g);
            int i = arm == class_1306.field_6183 ? 1 : -1;
            this.translateToViewModel(matrices);
            matrices.method_22907(class_7833.field_40716.rotationDegrees((float)i * (45.0F + f * -20.0F)));
            matrices.method_22907(class_7833.field_40718.rotationDegrees((float)i * m * -70.0F));
            matrices.method_22907(class_7833.field_40714.rotationDegrees(-70.0F));
            matrices.method_22907(class_7833.field_40716.rotationDegrees((float)i * -45.0F));
            this.translateBack(matrices);
            break;
         case 5:
            this.applyEquipOffset(matrices, arm, 0.0F);
            this.translateToViewModel(matrices);
            matrices.method_22907(class_7833.field_40714.rotationDegrees(swingProgress > 0.0F ? -class_3532.method_15374(swingProgress * 13.0F) * 37.0F : 0.0F));
            this.translateBack(matrices);
            break;
         case 6:
            this.applyEquipOffset(matrices, arm, 0.0F);
            int i = arm == class_1306.field_6183 ? 1 : -1;
            m = class_3532.method_15374(class_3532.method_15355(swingProgress) * 3.1415927F);
            this.translateToViewModel(matrices);
            matrices.method_22907(class_7833.field_40718.rotationDegrees((float)i * m * -20.0F));
            this.translateBack(matrices);
            break;
         case 7:
            this.applyEquipOffset(matrices, arm, equipProgress);
            this.translateToViewModel(matrices);
            matrices.method_22907(class_7833.field_40714.rotationDegrees(swingProgress * (this.flip ? 360.0F : -360.0F)));
            this.translateBack(matrices);
            break;
         case 8:
            this.applyEquipOffset(matrices, arm, equipProgress);
            g = -class_3532.method_15374(swingProgress * 3.0F) / 2.0F + 1.0F;
            matrices.method_22905(g, g, g);
            break;
         case 9:
            this.applyEquipOffset(matrices, arm, equipProgress);
            this.translateToViewModel(matrices);
            matrices.method_22907(class_7833.field_40714.rotationDegrees(swingProgress * -360.0F));
            this.translateBack(matrices);
            break;
         case 10:
            g = class_3532.method_15374(class_3532.method_15355(swingProgress) * 3.1415927F);
            this.applyEquipOffset(matrices, arm, 0.0F);
            this.translateToViewModel(matrices);
            matrices.method_22907(class_7833.field_40714.rotationDegrees(50.0F));
            matrices.method_22907(class_7833.field_40716.rotationDegrees(-30.0F * (1.0F - g) - 30.0F));
            matrices.method_22907(class_7833.field_40718.rotationDegrees(110.0F));
            this.translateBack(matrices);
            break;
         case 11:
            g = class_3532.method_15374(class_3532.method_15355(swingProgress) * 3.1415927F);
            matrices.method_46416(0.0F, 0.0F, 0.0F);
            this.applyEquipOffset(matrices, arm, 0.0F);
            this.translateToViewModel(matrices);
            matrices.method_22907(class_7833.field_40714.rotationDegrees(50.0F));
            matrices.method_22907(class_7833.field_40716.rotationDegrees(-60.0F * g - 50.0F));
            matrices.method_22907(class_7833.field_40718.rotationDegrees(110.0F));
            this.translateBack(matrices);
            break;
         case 12:
            g = class_3532.method_15374(class_3532.method_15355(swingProgress) * 3.1415927F);
            this.applyEquipOffset(matrices, arm, 0.0F);
            this.translateToViewModel(matrices);
            matrices.method_22907(class_7833.field_40714.rotationDegrees(50.0F));
            matrices.method_22907(class_7833.field_40716.rotationDegrees(-60.0F));
            matrices.method_22907(class_7833.field_40718.rotationDegrees(110.0F + 20.0F * g));
            this.translateBack(matrices);
            break;
         case 13:
            g = class_3532.method_15374(class_3532.method_15355(swingProgress) * 3.1415927F);
            this.applyEquipOffset(matrices, arm, 0.0F);
            matrices.method_46416(0.0F, 0.0F, -g / 4.0F);
            this.translateToViewModel(matrices);
            matrices.method_22907(class_7833.field_40714.rotationDegrees(-120.0F));
            this.translateBack(matrices);
            break;
         case 14:
            g = class_3532.method_15374(class_3532.method_15355(swingProgress) * 3.1415927F);
            this.applyEquipOffset(matrices, arm, 0.0F);
            this.translateToViewModel(matrices);
            matrices.method_22907(class_7833.field_40714.rotationDegrees(-class_3532.method_15374(swingProgress * 3.0F) * 60.0F));
            matrices.method_22907(class_7833.field_40718.rotationDegrees(-60.0F * g));
            this.translateBack(matrices);
            break;
         case 15:
            if (swingProgress > 0.0F) {
               g = class_3532.method_15374(class_3532.method_15355(swingProgress) * 3.1415927F);
               matrices.method_46416(0.56F, equipProgress * -0.2F - 0.5F, -0.7F);
               this.translateToViewModel(matrices);
               matrices.method_22907(class_7833.field_40716.rotationDegrees(45.0F));
               matrices.method_22907(class_7833.field_40714.rotationDegrees(g * -85.0F));
               if (ModuleManager.viewModel.isEnabled()) {
                  matrices.method_46416(-0.1F * (Float)ModuleManager.viewModel.scaleMain.getValue(), 0.28F * (Float)ModuleManager.viewModel.scaleMain.getValue(), 0.2F * (Float)ModuleManager.viewModel.scaleMain.getValue());
               } else {
                  matrices.method_46416(-0.1F, 0.28F, 0.2F);
               }

               matrices.method_22907(class_7833.field_40714.rotationDegrees(-85.0F));
               this.translateBack(matrices);
            } else {
               g = -0.4F * class_3532.method_15374(class_3532.method_15355(swingProgress) * 3.1415927F);
               m = 0.2F * class_3532.method_15374(class_3532.method_15355(swingProgress) * 6.2831855F);
               f1 = -0.2F * class_3532.method_15374(swingProgress * 3.1415927F);
               matrices.method_46416(g, m, f1);
               this.applyEquipOffset(matrices, arm, equipProgress);
               this.applySwingOffset(matrices, arm, swingProgress);
            }
         }

      }
   }

   public void renderFirstPersonItemCustom(class_742 player, float tickDelta, float pitch, class_1268 hand, float swingProgress, class_1799 item, float equipProgress, class_4587 matrices, class_4597 vertexConsumers, int light) {
      if (!player.method_31550()) {
         boolean bl = hand == class_1268.field_5808;
         class_1306 arm = bl ? player.method_6068() : player.method_6068().method_5928();
         matrices.method_22903();
         float f = 0.0F;
         boolean bl2;
         float g;
         float h;
         float j;
         EventHeldItemRenderer event;
         if (item.method_31574(class_1802.field_8399)) {
            bl2 = class_1764.method_7781(item);
            boolean bl3 = arm == class_1306.field_6183;
            int i = bl3 ? 1 : -1;
            if (player.method_6115() && player.method_6014() > 0 && player.method_6058() == hand) {
               this.applyEquipOffset(matrices, arm, equipProgress);
               matrices.method_46416((float)i * -0.4785682F, -0.094387F, 0.05731531F);
               matrices.method_22907(class_7833.field_40714.rotationDegrees(-11.935F));
               matrices.method_22907(class_7833.field_40716.rotationDegrees((float)i * 65.3F));
               matrices.method_22907(class_7833.field_40718.rotationDegrees((float)i * -9.785F));
               f = (float)item.method_7935(mc.field_1724) - ((float)mc.field_1724.method_6014() - tickDelta + 1.0F);
               g = f / (float)class_1764.method_7775(item, mc.field_1724);
               if (g > 1.0F) {
                  g = 1.0F;
               }

               if (g > 0.1F) {
                  h = class_3532.method_15374((f - 0.1F) * 1.3F);
                  j = g - 0.1F;
                  float k = h * j;
                  matrices.method_46416(k * 0.0F, k * 0.004F, k * 0.0F);
               }

               matrices.method_46416(g * 0.0F, g * 0.0F, g * 0.04F);
               matrices.method_22905(1.0F, 1.0F, 1.0F + g * 0.2F);
               matrices.method_22907(class_7833.field_40715.rotationDegrees((float)i * 45.0F));
            } else {
               f = -0.4F * class_3532.method_15374(class_3532.method_15355(swingProgress) * 3.1415927F);
               g = 0.2F * class_3532.method_15374(class_3532.method_15355(swingProgress) * 6.2831855F);
               h = -0.2F * class_3532.method_15374(swingProgress * 3.1415927F);
               matrices.method_46416((float)i * f, g, h);
               this.applyEquipOffset(matrices, arm, equipProgress);
               this.applySwingOffset(matrices, arm, swingProgress);
               if (bl2 && swingProgress < 0.001F && bl) {
                  matrices.method_46416((float)i * -0.641864F, 0.0F, 0.0F);
                  matrices.method_22907(class_7833.field_40716.rotationDegrees((float)i * 10.0F));
               }
            }

            event = new EventHeldItemRenderer(hand, item, equipProgress, matrices);
            TsunamiClient.EVENT_BUS.post((Object)event);
            this.renderItem(player, item, bl3 ? class_811.field_4322 : class_811.field_4321, !bl3, matrices, vertexConsumers, light);
         } else {
            bl2 = arm == class_1306.field_6183;
            float m = 0.0F;
            int l;
            if (player.method_6115() && player.method_6014() > 0 && player.method_6058() == hand) {
               l = bl2 ? 1 : -1;
               switch(item.method_7976()) {
               case field_8952:
               case field_8949:
                  this.applyEquipOffset(matrices, arm, equipProgress);
                  break;
               case field_8950:
               case field_8946:
                  this.applyEatOrDrinkTransformationCustom(matrices, tickDelta, arm, item);
                  this.applyEquipOffset(matrices, arm, equipProgress);
                  break;
               case field_8953:
                  this.applyEquipOffset(matrices, arm, equipProgress);
                  matrices.method_46416((float)l * -0.2785682F, 0.18344387F, 0.15731531F);
                  matrices.method_22907(class_7833.field_40714.rotationDegrees(-13.935F));
                  matrices.method_22907(class_7833.field_40716.rotationDegrees((float)l * 35.3F));
                  matrices.method_22907(class_7833.field_40718.rotationDegrees((float)l * -9.785F));
                  m = (float)item.method_7935(mc.field_1724) - ((float)mc.field_1724.method_6014() - tickDelta + 1.0F);
                  f = m / 20.0F;
                  f = (f * f + f * 2.0F) / 3.0F;
                  if (f > 1.0F) {
                     f = 1.0F;
                  }

                  if (f > 0.1F) {
                     g = class_3532.method_15374((m - 0.1F) * 1.3F);
                     h = f - 0.1F;
                     j = g * h;
                     matrices.method_46416(j * 0.0F, j * 0.004F, j * 0.0F);
                  }

                  matrices.method_46416(f * 0.0F, f * 0.0F, f * 0.04F);
                  matrices.method_22905(1.0F, 1.0F, 1.0F + f * 0.2F);
                  matrices.method_22907(class_7833.field_40715.rotationDegrees((float)l * 45.0F));
                  break;
               case field_8951:
                  this.applyEquipOffset(matrices, arm, equipProgress);
                  matrices.method_46416((float)l * -0.5F, 0.7F, 0.1F);
                  matrices.method_22907(class_7833.field_40714.rotationDegrees(-55.0F));
                  matrices.method_22907(class_7833.field_40716.rotationDegrees((float)l * 35.3F));
                  matrices.method_22907(class_7833.field_40718.rotationDegrees((float)l * -9.785F));
                  m = (float)item.method_7935(mc.field_1724) - ((float)mc.field_1724.method_6014() - tickDelta + 1.0F);
                  f = m / 10.0F;
                  if (f > 1.0F) {
                     f = 1.0F;
                  }

                  if (f > 0.1F) {
                     g = class_3532.method_15374((m - 0.1F) * 1.3F);
                     h = f - 0.1F;
                     j = g * h;
                     matrices.method_46416(j * 0.0F, j * 0.004F, j * 0.0F);
                  }

                  matrices.method_46416(0.0F, 0.0F, f * 0.2F);
                  matrices.method_22905(1.0F, 1.0F, 1.0F + f * 0.2F);
                  matrices.method_22907(class_7833.field_40715.rotationDegrees((float)l * 45.0F));
                  break;
               case field_42717:
                  this.applyBrushTransformation(matrices, tickDelta, arm, item, equipProgress);
               }
            } else if (player.method_6123()) {
               this.applyEquipOffset(matrices, arm, equipProgress);
               l = bl2 ? 1 : -1;
               matrices.method_46416((float)l * -0.4F, 0.8F, 0.3F);
               matrices.method_22907(class_7833.field_40716.rotationDegrees((float)l * 65.0F));
               matrices.method_22907(class_7833.field_40718.rotationDegrees((float)l * -85.0F));
            } else {
               this.renderSwordAnimation(matrices, f, swingProgress, equipProgress, arm);
            }

            event = new EventHeldItemRenderer(hand, item, equipProgress, matrices);
            TsunamiClient.EVENT_BUS.post((Object)event);
            this.renderItem(player, item, bl2 ? class_811.field_4322 : class_811.field_4321, !bl2, matrices, vertexConsumers, light);
         }

         matrices.method_22909();
      }

   }

   private void applyBrushTransformation(class_4587 matrices, float tickDelta, class_1306 arm, @NotNull class_1799 stack, float equipProgress) {
      this.applyEquipOffset(matrices, arm, equipProgress);
      float f = (float)mc.field_1724.method_6014() - tickDelta + 1.0F;
      float g = 1.0F - f / (float)stack.method_7935(mc.field_1724);
      float m = -15.0F + 75.0F * class_3532.method_15362(g * 45.0F * 3.1415927F);
      if (arm != class_1306.field_6183) {
         matrices.method_22904(0.1D, 0.83D, 0.35D);
         matrices.method_22907(class_7833.field_40714.rotationDegrees(-80.0F));
         matrices.method_22907(class_7833.field_40716.rotationDegrees(-90.0F));
         matrices.method_22907(class_7833.field_40714.rotationDegrees(m));
         matrices.method_22904(-0.3D, 0.22D, 0.35D);
      } else {
         matrices.method_22904(-0.25D, 0.22D, 0.35D);
         matrices.method_22907(class_7833.field_40714.rotationDegrees(-80.0F));
         matrices.method_22907(class_7833.field_40716.rotationDegrees(90.0F));
         matrices.method_22907(class_7833.field_40718.rotationDegrees(0.0F));
         matrices.method_22907(class_7833.field_40714.rotationDegrees(m));
      }

   }

   private void applyEquipOffset(@NotNull class_4587 matrices, class_1306 arm, float equipProgress) {
      int i = arm == class_1306.field_6183 ? 1 : -1;
      matrices.method_46416((float)i * 0.56F, -0.52F + equipProgress * -0.6F, -0.72F);
   }

   private void applySwingOffset(@NotNull class_4587 matrices, class_1306 arm, float swingProgress) {
      int i = arm == class_1306.field_6183 ? 1 : -1;
      float f = class_3532.method_15374(swingProgress * swingProgress * 3.1415927F);
      matrices.method_22907(class_7833.field_40716.rotationDegrees((float)i * (45.0F + f * -20.0F)));
      float g = class_3532.method_15374(class_3532.method_15355(swingProgress) * 3.1415927F);
      matrices.method_22907(class_7833.field_40718.rotationDegrees((float)i * g * -20.0F));
      matrices.method_22907(class_7833.field_40714.rotationDegrees(g * -80.0F));
      matrices.method_22907(class_7833.field_40716.rotationDegrees((float)i * -45.0F));
   }

   public void renderItem(class_1309 entity, class_1799 stack, class_811 renderMode, boolean leftHanded, class_4587 matrices, class_4597 vertexConsumers, int light) {
      if (!stack.method_7960()) {
         mc.method_1480().method_23177(entity, stack, renderMode, leftHanded, matrices, vertexConsumers, entity.method_37908(), light, class_4608.field_21444, entity.method_5628() + renderMode.ordinal());
      }
   }

   private void applyEatOrDrinkTransformationCustom(class_4587 matrices, float tickDelta, class_1306 arm, @NotNull class_1799 stack) {
      float f = (float)mc.field_1724.method_6014() - tickDelta + 1.0F;
      float g = f / (float)stack.method_7935(mc.field_1724);
      float h;
      if (g < 0.8F) {
         h = class_3532.method_15379(class_3532.method_15362(f / 4.0F * 3.1415927F) * 0.005F);
         matrices.method_46416(0.0F, h, 0.0F);
      }

      h = 1.0F - (float)Math.pow((double)g, 27.0D);
      int i = arm == class_1306.field_6183 ? 1 : -1;
      matrices.method_46416(h * 0.6F * (float)i * (Float)ModuleManager.viewModel.eatX.getValue(), h * -0.5F * (Float)ModuleManager.viewModel.eatY.getValue(), h * 0.0F);
      matrices.method_22907(class_7833.field_40716.rotationDegrees((float)i * h * 90.0F));
      matrices.method_22907(class_7833.field_40714.rotationDegrees(h * 10.0F));
      matrices.method_22907(class_7833.field_40718.rotationDegrees((float)i * h * 30.0F));
   }

   private void translateToViewModel(class_4587 matrices) {
      if (ModuleManager.viewModel.isEnabled()) {
         matrices.method_46416((Float)ModuleManager.viewModel.positionMainX.getValue(), (Float)ModuleManager.viewModel.positionMainY.getValue(), (Float)ModuleManager.viewModel.positionMainZ.getValue());
      }

   }

   private void translateToViewModelOff(class_4587 matrices) {
      if (ModuleManager.viewModel.isEnabled()) {
         matrices.method_46416(-(Float)ModuleManager.viewModel.positionMainX.getValue(), (Float)ModuleManager.viewModel.positionMainY.getValue(), (Float)ModuleManager.viewModel.positionMainZ.getValue());
      }

   }

   private void translateBack(class_4587 matrices) {
      if (ModuleManager.viewModel.isEnabled()) {
         matrices.method_46416(-(Float)ModuleManager.viewModel.positionMainX.getValue(), -(Float)ModuleManager.viewModel.positionMainY.getValue(), -(Float)ModuleManager.viewModel.positionMainZ.getValue());
      }

   }

   private void translateBacklOff(class_4587 matrices) {
      if (ModuleManager.viewModel.isEnabled()) {
         matrices.method_46416((Float)ModuleManager.viewModel.positionMainX.getValue(), -(Float)ModuleManager.viewModel.positionMainY.getValue(), -(Float)ModuleManager.viewModel.positionMainZ.getValue());
      }

   }

   private static enum Mode {
      Normal,
      Default,
      One,
      Two,
      Three,
      Four,
      Five,
      Six,
      Seven,
      Eight,
      Nine,
      Ten,
      Eleven,
      Twelve,
      Thirteen,
      Fourteen;

      // $FF: synthetic method
      private static Animations.Mode[] $values() {
         return new Animations.Mode[]{Normal, Default, One, Two, Three, Four, Five, Six, Seven, Eight, Nine, Ten, Eleven, Twelve, Thirteen, Fourteen};
      }
   }
}
