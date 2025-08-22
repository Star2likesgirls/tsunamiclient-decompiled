package tsunami.features.modules.render;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.platform.GlStateManager.class_4534;
import com.mojang.blaze3d.platform.GlStateManager.class_4535;
import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_1309;
import net.minecraft.class_1657;
import net.minecraft.class_243;
import net.minecraft.class_2596;
import net.minecraft.class_2703;
import net.minecraft.class_287;
import net.minecraft.class_289;
import net.minecraft.class_290;
import net.minecraft.class_2960;
import net.minecraft.class_332;
import net.minecraft.class_4587;
import net.minecraft.class_5602;
import net.minecraft.class_591;
import net.minecraft.class_745;
import net.minecraft.class_757;
import net.minecraft.class_7828;
import net.minecraft.class_7833;
import net.minecraft.class_2703.class_2705;
import net.minecraft.class_2703.class_5893;
import net.minecraft.class_293.class_5596;
import net.minecraft.class_5617.class_5618;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import org.joml.Vector4d;
import tsunami.events.impl.PacketEvent;
import tsunami.features.modules.Module;
import tsunami.features.modules.misc.FakePlayer;
import tsunami.gui.font.FontRenderers;
import tsunami.injection.accesors.IEntity;
import tsunami.setting.Setting;
import tsunami.setting.impl.ColorSetting;
import tsunami.utility.math.MathUtility;
import tsunami.utility.render.Render2DEngine;
import tsunami.utility.render.Render3DEngine;

public class LogoutSpots extends Module {
   private final Setting<LogoutSpots.RenderMode> renderMode;
   private final Setting<ColorSetting> color;
   private final Setting<Boolean> notifications;
   private final Setting<Boolean> ignoreBots;
   private final Map<UUID, class_1657> playerCache;
   private final Map<UUID, class_1657> logoutCache;

   public LogoutSpots() {
      super("LogoutSpots", Module.Category.NONE);
      this.renderMode = new Setting("RenderMode", LogoutSpots.RenderMode.TexturedChams);
      this.color = new Setting("Color", new ColorSetting(-2013200640));
      this.notifications = new Setting("Notifications", true);
      this.ignoreBots = new Setting("IgnoreBots", true);
      this.playerCache = Maps.newConcurrentMap();
      this.logoutCache = Maps.newConcurrentMap();
   }

   @EventHandler
   public void onPacketReceive(PacketEvent.Receive e) {
      class_2596 var3 = e.getPacket();
      String var10001;
      Iterator var5;
      UUID uuid;
      class_1657 pl;
      Iterator var9;
      if (var3 instanceof class_2703) {
         class_2703 pac = (class_2703)var3;
         if (pac.method_46327().contains(class_5893.field_29136)) {
            var9 = pac.method_46330().iterator();

            label88:
            while(var9.hasNext()) {
               class_2705 ple = (class_2705)var9.next();
               var5 = this.logoutCache.keySet().iterator();

               while(true) {
                  do {
                     do {
                        if (!var5.hasNext()) {
                           continue label88;
                        }

                        uuid = (UUID)var5.next();
                     } while(!uuid.equals(ple.comp_1107().getId()));

                     pl = (class_1657)this.logoutCache.get(uuid);
                  } while((Boolean)this.ignoreBots.getValue() && this.isABot(pl));

                  if ((Boolean)this.notifications.getValue()) {
                     var10001 = pl.method_5477().getString();
                     this.sendMessage(var10001 + " logged back at  X: " + (int)pl.method_23317() + " Y: " + (int)pl.method_23318() + " Z: " + (int)pl.method_23321());
                  }

                  this.logoutCache.remove(uuid);
               }
            }
         }

         this.playerCache.clear();
      }

      var3 = e.getPacket();
      if (var3 instanceof class_7828) {
         class_7828 pac = (class_7828)var3;
         var9 = pac.comp_1105.iterator();

         label63:
         while(var9.hasNext()) {
            UUID uuid2 = (UUID)var9.next();
            var5 = this.playerCache.keySet().iterator();

            while(true) {
               do {
                  do {
                     if (!var5.hasNext()) {
                        continue label63;
                     }

                     uuid = (UUID)var5.next();
                  } while(!uuid.equals(uuid2));

                  pl = (class_1657)this.playerCache.get(uuid);
               } while((Boolean)this.ignoreBots.getValue() && this.isABot(pl));

               if (pl != null) {
                  if ((Boolean)this.notifications.getValue()) {
                     var10001 = pl.method_5477().getString();
                     this.sendMessage(var10001 + " logged out at  X: " + (int)pl.method_23317() + " Y: " + (int)pl.method_23318() + " Z: " + (int)pl.method_23321());
                  }

                  if (!this.logoutCache.containsKey(uuid)) {
                     this.logoutCache.put(uuid, pl);
                  }
               }
            }
         }

         this.playerCache.clear();
      }

   }

   public void onEnable() {
      this.playerCache.clear();
      this.logoutCache.clear();
   }

   public void onUpdate() {
      Iterator var1 = mc.field_1687.method_18456().iterator();

      while(var1.hasNext()) {
         class_1657 player = (class_1657)var1.next();
         if (player != null && !player.equals(mc.field_1724)) {
            this.playerCache.put(player.method_7334().getId(), player);
         }
      }

   }

   public void onRender3D(class_4587 s) {
      RenderSystem.enableBlend();
      RenderSystem.disableDepthTest();
      if (this.renderMode.is(LogoutSpots.RenderMode.Box)) {
         RenderSystem.defaultBlendFunc();
      } else {
         RenderSystem.blendFunc(class_4535.SRC_ALPHA, class_4534.ONE);
      }

      Iterator var2 = this.logoutCache.keySet().iterator();

      while(var2.hasNext()) {
         UUID uuid = (UUID)var2.next();
         class_1657 data = (class_1657)this.logoutCache.get(uuid);
         if (data != null) {
            if (this.renderMode.is(LogoutSpots.RenderMode.Box)) {
               Render3DEngine.drawBoxOutline(data.method_5829(), ((ColorSetting)this.color.getValue()).getColorObject(), 2.0F);
            } else {
               class_591<class_1657> modelPlayer = new class_591((new class_5618(mc.method_1561(), mc.method_1480(), mc.method_1541(), mc.method_1561().method_43336(), mc.method_1478(), mc.method_31974(), mc.field_1772)).method_32167(class_5602.field_27577), false);
               modelPlayer.method_2838().method_41924(new Vector3f(-0.3F, -0.3F, -0.3F));
               this.renderEntity(s, data, modelPlayer, ((class_745)data).method_52814().comp_1626(), ((ColorSetting)this.color.getValue()).getAlpha());
            }
         }
      }

      RenderSystem.enableDepthTest();
      RenderSystem.disableBlend();
   }

   public void onRender2D(class_332 context) {
      Iterator var2 = this.logoutCache.keySet().iterator();

      while(var2.hasNext()) {
         UUID uuid = (UUID)var2.next();
         class_1657 data = (class_1657)this.logoutCache.get(uuid);
         if (data != null) {
            class_243 vector = new class_243(data.method_23317(), data.method_23318() + 2.0D, data.method_23321());
            Vector4d position = null;
            vector = Render3DEngine.worldSpaceToScreenSpace(new class_243(vector.field_1352, vector.field_1351, vector.field_1350));
            if (vector.field_1350 > 0.0D && vector.field_1350 < 1.0D) {
               position = new Vector4d(vector.field_1352, vector.field_1351, vector.field_1350, 0.0D);
               position.x = Math.min(vector.field_1352, position.x);
               position.y = Math.min(vector.field_1351, position.y);
               position.z = Math.max(vector.field_1352, position.z);
            }

            String var10000 = data.method_5477().getString();
            String string = var10000 + " " + String.format("%.1f", data.method_6032() + data.method_6067()) + " X: " + (int)data.method_23317() + "  Z: " + (int)data.method_23321();
            if (position != null) {
               float diff = (float)(position.z - position.x) / 2.0F;
               float textWidth = FontRenderers.sf_bold.getStringWidth(string) * 1.0F;
               float tagX = (float)((position.x + (double)diff - (double)(textWidth / 2.0F)) * 1.0D);
               Render2DEngine.drawRect(context.method_51448(), tagX - 2.0F, (float)(position.y - 13.0D), textWidth + 4.0F, 11.0F, new Color(-1728053247, true));
               FontRenderers.sf_bold.drawString(context.method_51448(), string, (double)tagX, (double)((float)position.y - 10.0F), -1);
            }
         }
      }

   }

   private void renderEntity(@NotNull class_4587 matrices, @NotNull class_1309 entity, @NotNull class_591<class_1657> modelBase, class_2960 texture, int alpha) {
      modelBase.field_3482.field_3665 = true;
      modelBase.field_3479.field_3665 = true;
      modelBase.field_3484.field_3665 = true;
      modelBase.field_3486.field_3665 = true;
      modelBase.field_3483.field_3665 = true;
      modelBase.field_3394.field_3665 = true;
      double x = entity.method_23317() - mc.method_1561().field_4686.method_19326().method_10216();
      double y = entity.method_23318() - mc.method_1561().field_4686.method_19326().method_10214();
      double z = entity.method_23321() - mc.method_1561().field_4686.method_19326().method_10215();
      ((IEntity)entity).setPos(entity.method_19538());
      matrices.method_22903();
      matrices.method_46416((float)x, (float)y, (float)z);
      matrices.method_22907(class_7833.field_40716.rotation(MathUtility.rad(180.0F - entity.field_6283)));
      prepareScale(matrices);
      modelBase.method_17086((class_1657)entity, entity.field_42108.method_48569(), entity.field_42108.method_48566(), Render3DEngine.getTickDelta());
      float limbSpeed = Math.min(entity.field_42108.method_48566(), 1.0F);
      modelBase.method_17087((class_1657)entity, entity.field_42108.method_48569(), limbSpeed, (float)entity.field_6012, entity.field_6241 - entity.field_6283, entity.method_36455());
      class_287 buffer;
      if (this.renderMode.is(LogoutSpots.RenderMode.TexturedChams)) {
         RenderSystem.setShaderTexture(0, texture);
         RenderSystem.setShader(class_757::method_34542);
         buffer = class_289.method_1348().method_60827(class_5596.field_27382, class_290.field_1585);
      } else {
         RenderSystem.setShader(class_757::method_34539);
         buffer = class_289.method_1348().method_60827(class_5596.field_27382, class_290.field_1592);
      }

      RenderSystem.setShaderColor(((ColorSetting)this.color.getValue()).getGlRed(), ((ColorSetting)this.color.getValue()).getGlGreen(), ((ColorSetting)this.color.getValue()).getGlBlue(), (float)alpha / 255.0F);
      modelBase.method_60879(matrices, buffer, 10, 0);
      Render2DEngine.endBuilding(buffer);
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      matrices.method_22909();
   }

   private static void prepareScale(@NotNull class_4587 matrixStack) {
      matrixStack.method_22905(-1.0F, -1.0F, 1.0F);
      matrixStack.method_22905(1.6F, 1.8F, 1.6F);
      matrixStack.method_46416(0.0F, -1.501F, 0.0F);
   }

   private boolean isABot(class_1657 ent) {
      return !ent.method_5667().equals(UUID.nameUUIDFromBytes(("OfflinePlayer:" + ent.method_5477().getString()).getBytes(StandardCharsets.UTF_8))) && ent instanceof class_745 && (FakePlayer.fakePlayer == null || ent.method_5628() != FakePlayer.fakePlayer.method_5628()) && !ent.method_5477().getString().contains("-");
   }

   private static enum RenderMode {
      Chams,
      TexturedChams,
      Box;

      // $FF: synthetic method
      private static LogoutSpots.RenderMode[] $values() {
         return new LogoutSpots.RenderMode[]{Chams, TexturedChams, Box};
      }
   }
}
