package tsunami.features.hud.impl;

import java.awt.Color;
import java.util.Iterator;
import net.minecraft.class_124;
import net.minecraft.class_1657;
import net.minecraft.class_332;
import tsunami.core.Managers;
import tsunami.core.manager.client.ModuleManager;
import tsunami.features.hud.HudElement;
import tsunami.features.modules.client.HudEditor;
import tsunami.features.modules.render.NameTags;
import tsunami.gui.font.FontRenderers;
import tsunami.setting.Setting;
import tsunami.setting.impl.ColorSetting;
import tsunami.utility.render.Render2DEngine;
import tsunami.utility.render.Render3DEngine;

public class Radar extends HudElement {
   private final Setting<Radar.Mode> mode;
   private final Setting<Radar.ColorMode> colorMode;
   private final Setting<Integer> size;
   private final Setting<ColorSetting> color2;
   private final Setting<ColorSetting> color3;
   private final Setting<Radar.Component> c1;
   private final Setting<Radar.Component> c2;
   private final Setting<Radar.Component> c3;
   private final Setting<Radar.Component> c4;
   private final Setting<Radar.Component> c5;
   private final Setting<class_124> c12;
   private final Setting<class_124> c22;
   private final Setting<class_124> c32;
   private final Setting<class_124> c42;

   public Radar() {
      super("Radar", 100, 100);
      this.mode = new Setting("Mode", Radar.Mode.Rect);
      this.colorMode = new Setting("ColorMode", Radar.ColorMode.Sync);
      this.size = new Setting("Size", 80, 20, 300);
      this.color2 = new Setting("Color", new ColorSetting(-15724528));
      this.color3 = new Setting("PlayerColor", new ColorSetting(-979657829));
      this.c1 = new Setting("Component1", Radar.Component.Name, (v) -> {
         return this.mode.is(Radar.Mode.Text);
      });
      this.c2 = new Setting("Component2", Radar.Component.Hp, (v) -> {
         return this.mode.is(Radar.Mode.Text);
      });
      this.c3 = new Setting("Component3", Radar.Component.Ping, (v) -> {
         return this.mode.is(Radar.Mode.Text);
      });
      this.c4 = new Setting("Component4", Radar.Component.None, (v) -> {
         return this.mode.is(Radar.Mode.Text);
      });
      this.c5 = new Setting("Component5", Radar.Component.None, (v) -> {
         return this.mode.is(Radar.Mode.Text);
      });
      this.c12 = new Setting("Color1", class_124.field_1068, (v) -> {
         return this.mode.is(Radar.Mode.Text);
      });
      this.c22 = new Setting("Color2", class_124.field_1068, (v) -> {
         return this.mode.is(Radar.Mode.Text);
      });
      this.c32 = new Setting("Color3", class_124.field_1068, (v) -> {
         return this.mode.is(Radar.Mode.Text);
      });
      this.c42 = new Setting("Color4", class_124.field_1068, (v) -> {
         return this.mode.is(Radar.Mode.Text);
      });
   }

   public void onRender2D(class_332 context) {
      super.onRender2D(context);
      if (this.mode.getValue() == Radar.Mode.Rect) {
         Render2DEngine.drawHudBase(context.method_51448(), this.getPosX(), this.getPosY(), (float)(Integer)this.size.getValue(), (float)(Integer)this.size.getValue(), (Float)HudEditor.hudRound.getValue());
         if (HudEditor.hudStyle.is(HudEditor.HudStyle.Blurry)) {
            Render2DEngine.drawRectDumbWay(context.method_51448(), this.getPosX(), this.getPosY() + (float)(Integer)this.size.getValue() / 2.0F + 0.25F, this.getPosX() + (float)(Integer)this.size.getValue(), this.getPosY() + (float)(Integer)this.size.getValue() / 2.0F - 0.25F, new Color(1426063359, true));
            Render2DEngine.drawRectDumbWay(context.method_51448(), this.getPosX() + ((float)(Integer)this.size.getValue() / 2.0F - 0.25F), this.getPosY() - 0.5F, this.getPosX() + (float)(Integer)this.size.getValue() / 2.0F + 0.25F, this.getPosY() + (float)(Integer)this.size.getValue() - 1.0F, new Color(1426063359, true));
         } else {
            Render2DEngine.draw2DGradientRect(context.method_51448(), (float)((double)this.getPosX() + ((double)((float)(Integer)this.size.getValue() / 2.0F) - 0.5D)), (float)((double)this.getPosY() + 3.5D), (float)((double)this.getPosX() + (double)((float)(Integer)this.size.getValue() / 2.0F) + 0.2D), (float)((double)(this.getPosY() + (float)(Integer)this.size.getValue()) - 3.5D), ((ColorSetting)this.color2.getValue()).getColorObject(), ((ColorSetting)this.color2.getValue()).getColorObject(), ((ColorSetting)this.color2.getValue()).getColorObject(), ((ColorSetting)this.color2.getValue()).getColorObject());
            Render2DEngine.draw2DGradientRect(context.method_51448(), this.getPosX() + 3.5F, this.getPosY() + ((float)(Integer)this.size.getValue() / 2.0F - 0.2F), this.getPosX() + (float)(Integer)this.size.getValue() - 3.5F, this.getPosY() + (float)(Integer)this.size.getValue() / 2.0F + 0.5F, ((ColorSetting)this.color2.getValue()).getColorObject(), ((ColorSetting)this.color2.getValue()).getColorObject(), ((ColorSetting)this.color2.getValue()).getColorObject(), ((ColorSetting)this.color2.getValue()).getColorObject());
         }

         Iterator var2 = Managers.ASYNC.getAsyncPlayers().iterator();

         while(var2.hasNext()) {
            class_1657 entityPlayer = (class_1657)var2.next();
            if (entityPlayer != mc.field_1724) {
               float posX = (float)(entityPlayer.field_6014 + (entityPlayer.field_6014 - entityPlayer.method_23317()) * (double)Render3DEngine.getTickDelta() - mc.field_1724.method_23317()) * 2.0F;
               float posZ = (float)(entityPlayer.field_5969 + (entityPlayer.field_5969 - entityPlayer.method_23321()) * (double)Render3DEngine.getTickDelta() - mc.field_1724.method_23321()) * 2.0F;
               float cos = (float)Math.cos((double)mc.field_1724.method_5705(Render3DEngine.getTickDelta()) * 0.017453292D);
               float sin = (float)Math.sin((double)mc.field_1724.method_5705(Render3DEngine.getTickDelta()) * 0.017453292D);
               float rotY = -(posZ * cos - posX * sin);
               float rotX = -(posX * cos + posZ * sin);
               if (rotY > (float)(Integer)this.size.getValue() / 2.0F - 6.0F) {
                  rotY = (float)(Integer)this.size.getValue() / 2.0F - 6.0F;
               } else if (rotY < -((float)(Integer)this.size.getValue() / 2.0F - 8.0F)) {
                  rotY = -((float)(Integer)this.size.getValue() / 2.0F - 8.0F);
               }

               if (rotX > (float)(Integer)this.size.getValue() / 2.0F - 5.0F) {
                  rotX = (float)(Integer)this.size.getValue() / 2.0F - 5.0F;
               } else if (rotX < -((float)(Integer)this.size.getValue() / 2.0F - 5.0F)) {
                  rotX = -((float)(Integer)this.size.getValue() / 2.0F - 5.0F);
               }

               Render2DEngine.drawRound(context.method_51448(), this.getPosX() + (float)(Integer)this.size.getValue() / 2.0F + rotX - 2.0F, this.getPosY() + (float)(Integer)this.size.getValue() / 2.0F + rotY - 2.0F, 4.0F, 4.0F, 2.0F, ((ColorSetting)this.color3.getValue()).getColorObject());
            }
         }
      }

      if (this.mode.getValue() == Radar.Mode.Text) {
         float offset_y = 0.0F;
         Iterator var11 = Managers.ASYNC.getAsyncPlayers().iterator();

         while(var11.hasNext()) {
            class_1657 entityPlayer = (class_1657)var11.next();
            if (entityPlayer != mc.field_1724) {
               String str = String.format("%s %s %s %s %s", this.getText(this.c1, entityPlayer), this.getText(this.c2, entityPlayer), this.getText(this.c3, entityPlayer), this.getText(this.c4, entityPlayer), this.getText(this.c5, entityPlayer));
               if (this.colorMode.getValue() == Radar.ColorMode.Sync) {
                  FontRenderers.sf_bold.drawString(context.method_51448(), str, (double)this.getPosX(), (double)(this.getPosY() + offset_y), HudEditor.getColor((int)(offset_y * 2.0F)).getRGB());
               } else {
                  FontRenderers.sf_bold.drawString(context.method_51448(), str, (double)this.getPosX(), (double)(this.getPosY() + offset_y), ((ColorSetting)this.color2.getValue()).getColor());
               }

               offset_y += FontRenderers.sf_bold.getFontHeight(str);
            }
         }
      }

      this.setBounds(this.getPosX(), this.getPosY(), (float)(Integer)this.size.getValue(), (float)(Integer)this.size.getValue());
   }

   public String getText(Setting<Radar.Component> c, class_1657 player) {
      String var10000;
      switch(((Radar.Component)c.getValue()).ordinal()) {
      case 0:
         var10000 = String.valueOf(this.c12.getValue());
         return var10000 + player.method_5477().getString() + String.valueOf(class_124.field_1070);
      case 1:
         int health = (int)Math.ceil((double)(player.method_6032() + player.method_6067()));
         var10000 = ModuleManager.nameTags.getHealthColor((float)health);
         return var10000 + health + String.valueOf(class_124.field_1070);
      case 2:
         var10000 = String.valueOf(this.c32.getValue());
         return var10000 + (int)Math.ceil((double)mc.field_1724.method_5739(player)) + "m" + String.valueOf(class_124.field_1070);
      case 3:
         var10000 = String.valueOf(this.c22.getValue());
         return var10000 + NameTags.getEntityPing(player) + "ms" + String.valueOf(class_124.field_1070);
      case 4:
         var10000 = String.valueOf(this.c42.getValue());
         return var10000 + Managers.COMBAT.getPops(player) + String.valueOf(class_124.field_1070);
      default:
         return "";
      }
   }

   private static enum Mode {
      Rect,
      Text;

      // $FF: synthetic method
      private static Radar.Mode[] $values() {
         return new Radar.Mode[]{Rect, Text};
      }
   }

   public static enum ColorMode {
      Sync,
      Custom;

      // $FF: synthetic method
      private static Radar.ColorMode[] $values() {
         return new Radar.ColorMode[]{Sync, Custom};
      }
   }

   public static enum Component {
      Name,
      Hp,
      Distance,
      Ping,
      TotemPops,
      None;

      // $FF: synthetic method
      private static Radar.Component[] $values() {
         return new Radar.Component[]{Name, Hp, Distance, Ping, TotemPops, None};
      }
   }
}
