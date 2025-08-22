package tsunami.features.hud.impl;

import com.mojang.blaze3d.platform.GlStateManager.class_4534;
import com.mojang.blaze3d.platform.GlStateManager.class_4535;
import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Date;
import net.minecraft.class_124;
import net.minecraft.class_332;
import net.minecraft.class_7833;
import tsunami.core.Managers;
import tsunami.core.manager.client.ModuleManager;
import tsunami.features.hud.HudElement;
import tsunami.features.modules.client.HudEditor;
import tsunami.features.modules.client.Media;
import tsunami.features.modules.misc.NameProtect;
import tsunami.gui.font.FontRenderers;
import tsunami.setting.Setting;
import tsunami.setting.impl.ColorSetting;
import tsunami.utility.render.Render2DEngine;
import tsunami.utility.render.Render3DEngine;
import tsunami.utility.render.TextUtil;
import tsunami.utility.render.TextureStorage;

public class WaterMark extends HudElement {
   public static final Setting<WaterMark.Mode> mode;
   private final Setting<Boolean> ru = new Setting("RU", false);
   private final TextUtil textUtil = new TextUtil(new String[]{"SunamiTOP", "TsunamiBlea"});

   public WaterMark() {
      super("WaterMark", 100, 35);
   }

   public void onRender2D(class_332 context) {
      super.onRender2D(context);
      String username = (!ModuleManager.media.isEnabled() || !(Boolean)Media.nickProtect.getValue()) && !ModuleManager.nameProtect.isEnabled() ? mc.method_1548().method_1676() : (ModuleManager.nameProtect.isEnabled() ? NameProtect.getCustomName() : "Protected");
      if (mode.getValue() == WaterMark.Mode.Big) {
         Render2DEngine.drawHudBase(context.method_51448(), this.getPosX(), this.getPosY(), 106.0F, 30.0F, (Float)HudEditor.hudRound.getValue());
         FontRenderers.thglitch.drawString(context.method_51448(), "TsunamiClient ", (double)this.getPosX() + 5.5D, (double)(this.getPosY() + 5.0F), -1);
         FontRenderers.monsterrat.drawGradientString(context.method_51448(), "New", this.getPosX() + 35.5F, this.getPosY() + 21.0F, 1);
         this.setBounds(this.getPosX(), this.getPosY(), 106.0F, 30.0F);
      } else {
         String var10000;
         if (mode.getValue() == WaterMark.Mode.Small) {
            float offset2;
            if (HudEditor.hudStyle.is(HudEditor.HudStyle.Blurry)) {
               float offset1 = FontRenderers.sf_bold.getStringWidth(username) + 72.0F;
               offset2 = FontRenderers.sf_bold.getStringWidth(mc.method_1542() ? "SinglePlayer" : mc.method_1562().method_45734().field_3761);
               float offset3 = Managers.PROXY.isActive() ? FontRenderers.sf_bold.getStringWidth(Managers.PROXY.getActiveProxy().getName()) + 11.0F : 0.0F;
               Render2DEngine.drawRoundedBlur(context.method_51448(), this.getPosX(), this.getPosY(), 50.0F, 15.0F, 3.0F, ((ColorSetting)HudEditor.blurColor.getValue()).getColorObject());
               Render2DEngine.drawRoundedBlur(context.method_51448(), this.getPosX() + 55.0F, this.getPosY(), offset1 + offset2 - 36.0F + offset3, 15.0F, 3.0F, ((ColorSetting)HudEditor.blurColor.getValue()).getColorObject());
               Render2DEngine.setupRender();
               Render2DEngine.drawRect(context.method_51448(), this.getPosX() + 13.0F, this.getPosY() + 1.5F, 0.5F, 11.0F, new Color(1157627903, true));
               FontRenderers.sf_bold.drawGradientString(context.method_51448(), "New", this.getPosX() + 18.0F, this.getPosY() + 5.0F, 20);
               RenderSystem.blendFunc(class_4535.SRC_ALPHA, class_4534.ONE);
               RenderSystem.setShaderTexture(0, TextureStorage.miniLogo);
               Render2DEngine.renderGradientTexture(context.method_51448(), (double)(this.getPosX() + 1.0F), (double)(this.getPosY() + 2.0F), 11.0D, 11.0D, 0.0F, 0.0F, 128.0D, 128.0D, 128.0D, 128.0D, HudEditor.getColor(270), HudEditor.getColor(0), HudEditor.getColor(180), HudEditor.getColor(90));
               RenderSystem.setShaderTexture(0, TextureStorage.playerIcon);
               Render2DEngine.renderGradientTexture(context.method_51448(), (double)(this.getPosX() + 58.0F), (double)(this.getPosY() + 3.0F), 8.0D, 8.0D, 0.0F, 0.0F, 128.0D, 128.0D, 128.0D, 128.0D, HudEditor.getColor(270), HudEditor.getColor(0), HudEditor.getColor(180), HudEditor.getColor(90));
               RenderSystem.setShaderTexture(0, TextureStorage.serverIcon);
               Render2DEngine.renderGradientTexture(context.method_51448(), (double)(this.getPosX() + offset1), (double)(this.getPosY() + 2.0F), 10.0D, 10.0D, 0.0F, 0.0F, 128.0D, 128.0D, 128.0D, 128.0D, HudEditor.getColor(270), HudEditor.getColor(0), HudEditor.getColor(180), HudEditor.getColor(90));
               if (Managers.PROXY.isActive()) {
                  RenderSystem.setShaderTexture(0, TextureStorage.proxyIcon);
                  Render2DEngine.renderGradientTexture(context.method_51448(), (double)(this.getPosX() + offset1 + offset2 + 16.0F), (double)(this.getPosY() + 2.0F), 10.0D, 10.0D, 0.0F, 0.0F, 128.0D, 128.0D, 128.0D, 128.0D, HudEditor.getColor(270), HudEditor.getColor(0), HudEditor.getColor(180), HudEditor.getColor(90));
                  FontRenderers.sf_bold.drawString(context.method_51448(), Managers.PROXY.getActiveProxy().getName(), (double)(this.getPosX() + offset1 + offset2 + 28.0F), (double)(this.getPosY() + 5.0F), -1);
               }

               Render2DEngine.endRender();
               Render2DEngine.setupRender();
               RenderSystem.defaultBlendFunc();
               FontRenderers.sf_bold.drawString(context.method_51448(), username, (double)(this.getPosX() + 68.0F), (double)(this.getPosY() + 4.5F), ((ColorSetting)HudEditor.textColor.getValue()).getColor());
               FontRenderers.sf_bold.drawString(context.method_51448(), mc.method_1542() ? "SinglePlayer" : mc.method_1562().method_45734().field_3761, (double)(this.getPosX() + offset1 + 13.0F), (double)(this.getPosY() + 4.5F), ((ColorSetting)HudEditor.textColor.getValue()).getColor());
               Render2DEngine.endRender();
               this.setBounds(this.getPosX(), this.getPosY(), 100.0F, 15.0F);
            } else {
               var10000 = String.valueOf(class_124.field_1063);
               String info = var10000 + "| " + String.valueOf(class_124.field_1070) + username + String.valueOf(class_124.field_1063) + " | " + String.valueOf(class_124.field_1070) + Managers.SERVER.getPing() + " ms" + String.valueOf(class_124.field_1063) + " | " + String.valueOf(class_124.field_1070) + (mc.method_1542() ? "SinglePlayer" : mc.method_1562().method_45734().field_3761);
               offset2 = FontRenderers.sf_bold.getStringWidth("TsunamiClient " + info) + 5.0F;
               Render2DEngine.drawHudBase(context.method_51448(), this.getPosX(), this.getPosY(), offset2, 10.0F, 3.0F);
               FontRenderers.sf_bold.drawGradientString(context.method_51448(), (Boolean)this.ru.getValue() ? String.valueOf(this.textUtil) + " " : "TsunamiClient ", this.getPosX() + 2.0F, this.getPosY() + 2.5F, 10);
               FontRenderers.sf_bold.drawString(context.method_51448(), info, (double)(this.getPosX() + 2.0F + FontRenderers.sf_bold.getStringWidth("TsunamiClient ")), (double)(this.getPosY() + 2.5F), ((ColorSetting)HudEditor.textColor.getValue()).getColor());
               this.setBounds(this.getPosX(), this.getPosY(), offset2, 10.0F);
            }
         } else if (mode.getValue() == WaterMark.Mode.BaltikaClient) {
            Render2DEngine.drawHudBase(context.method_51448(), this.getPosX(), this.getPosY(), 100.0F, 64.0F, (Float)HudEditor.hudRound.getValue());
            Render2DEngine.addWindow(context.method_51448(), this.getPosX(), this.getPosY(), this.getPosX() + 100.0F, this.getPosY() + 64.0F, 1.0D);
            context.method_51448().method_22903();
            context.method_51448().method_46416(this.getPosX() + 10.0F, this.getPosY() + 32.0F, 0.0F);
            context.method_51448().method_22907(class_7833.field_40718.rotation((float)Math.toRadians((double)((float)(mc.field_1724.field_6012 * 3) + Render3DEngine.getTickDelta()))));
            context.method_51448().method_46416(-(this.getPosX() + 10.0F), -(this.getPosY() + 32.0F), 0.0F);
            context.method_25290(TextureStorage.baltika, (int)this.getPosX() - 10, (int)this.getPosY() + 2, 0.0F, 0.0F, 40, 64, 40, 64);
            context.method_51448().method_22909();
            Render2DEngine.popWindow();
            FontRenderers.thglitch.drawString(context.method_51448(), "BALTIKA", (double)(this.getPosX() + 43.0F), (double)this.getPosY() + 41.5D, -1);
            this.setBounds(this.getPosX(), this.getPosY(), 100.0F, 64.0F);
         } else if (mode.is(WaterMark.Mode.Rifk)) {
            Date date = new Date(System.currentTimeMillis());
            SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
            var10000 = String.valueOf(class_124.field_1060);
            String info = var10000 + String.format("Tsunami | build: 2025  | rate: %d | %s", Math.round(Managers.SERVER.getTPS()), format.format(date));
            float width = FontRenderers.profont.getStringWidth(info) + 5.0F;
            Render2DEngine.drawRectWithOutline(context.method_51448(), this.getPosX(), this.getPosY(), width, 8.0F, Color.decode("#192A1A"), Color.decode("#833B7B"));
            Render2DEngine.drawGradientBlurredShadow1(context.method_51448(), this.getPosX(), this.getPosY(), width, 8.0F, 10, Color.decode("#161A1E"), Color.decode("#161A1E"), Color.decode("#382E37"), Color.decode("#382E37"));
            FontRenderers.profont.drawString(context.method_51448(), info, (double)this.getPosX() + 2.7D, (double)this.getPosY() + 2.953D, ((ColorSetting)HudEditor.textColor.getValue()).getColor());
            this.setBounds(this.getPosX(), this.getPosY(), width, 8.0F);
         } else {
            FontRenderers.monsterrat.drawGradientString(context.method_51448(), "TsunamiClient 1.7b2407", this.getPosX() + 5.5F, this.getPosY() + 5.0F, 10);
            this.setBounds(this.getPosX(), this.getPosY(), 100.0F, 3.0F);
         }
      }

   }

   public void onUpdate() {
      this.textUtil.tick();
   }

   static {
      mode = new Setting("Mode", WaterMark.Mode.Big);
   }

   private static enum Mode {
      Big,
      Small,
      Classic,
      BaltikaClient,
      Rifk;

      // $FF: synthetic method
      private static WaterMark.Mode[] $values() {
         return new WaterMark.Mode[]{Big, Small, Classic, BaltikaClient, Rifk};
      }
   }
}
