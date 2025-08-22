package tsunami.features.modules.render;

import com.google.common.collect.Ordering;
import com.mojang.blaze3d.platform.GlStateManager.class_4534;
import com.mojang.blaze3d.platform.GlStateManager.class_4535;
import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import net.minecraft.class_124;
import net.minecraft.class_1293;
import net.minecraft.class_1297;
import net.minecraft.class_1321;
import net.minecraft.class_1498;
import net.minecraft.class_1657;
import net.minecraft.class_1676;
import net.minecraft.class_1747;
import net.minecraft.class_1767;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1887;
import net.minecraft.class_1890;
import net.minecraft.class_1893;
import net.minecraft.class_1934;
import net.minecraft.class_243;
import net.minecraft.class_2480;
import net.minecraft.class_2561;
import net.minecraft.class_2586;
import net.minecraft.class_2636;
import net.minecraft.class_266;
import net.minecraft.class_2960;
import net.minecraft.class_308;
import net.minecraft.class_332;
import net.minecraft.class_3532;
import net.minecraft.class_5134;
import net.minecraft.class_5250;
import net.minecraft.class_5321;
import net.minecraft.class_640;
import net.minecraft.class_6880;
import net.minecraft.class_8646;
import net.minecraft.class_9013;
import net.minecraft.class_9025;
import net.minecraft.class_9288;
import net.minecraft.class_9304;
import net.minecraft.class_9334;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector4d;
import org.lwjgl.opengl.GL11;
import tsunami.core.Managers;
import tsunami.core.manager.client.ModuleManager;
import tsunami.core.manager.player.FriendManager;
import tsunami.features.hud.impl.PotionHud;
import tsunami.features.modules.Module;
import tsunami.features.modules.client.HudEditor;
import tsunami.features.modules.misc.NameProtect;
import tsunami.gui.font.FontRenderers;
import tsunami.setting.Setting;
import tsunami.setting.impl.ColorSetting;
import tsunami.utility.render.Render2DEngine;
import tsunami.utility.render.Render3DEngine;
import tsunami.utility.render.TextureStorage;

public class NameTags extends Module {
   private final Map<class_5321<class_1887>, String> encMap = new HashMap();
   private final Setting<Boolean> self = new Setting("Self", false);
   private final Setting<Float> scale = new Setting("Scale", 1.0F, 0.1F, 10.0F);
   private final Setting<Boolean> resize = new Setting("Resize", false);
   private final Setting<Float> height = new Setting("Height", 2.0F, 0.1F, 10.0F);
   private final Setting<Boolean> gamemode = new Setting("Gamemode", false);
   private final Setting<Boolean> spawners = new Setting("SpawnerNameTag", false);
   private final Setting<Boolean> entityOwner = new Setting("EntityOwner", false);
   private final Setting<Boolean> ping = new Setting("Ping", false);
   private final Setting<Boolean> hp = new Setting("HP", true);
   private final Setting<Boolean> distance = new Setting("Distance", true);
   private final Setting<Boolean> pops = new Setting("TotemPops", true);
   private final Setting<NameTags.OutlineColor> outline;
   private final Setting<NameTags.OutlineColor> friendOutline;
   private final Setting<ColorSetting> outlineColor;
   private final Setting<ColorSetting> friendOutlineColor;
   private final Setting<Boolean> enchantss;
   private final Setting<Boolean> onlyHands;
   private final Setting<Boolean> funtimeHp;
   private final Setting<Boolean> ignoreBots;
   private final Setting<Boolean> potions;
   private final Setting<Boolean> shulkers;
   private final Setting<ColorSetting> fillColorA;
   private final Setting<ColorSetting> fillColorF;
   private final Setting<NameTags.Font> font;
   private final Setting<NameTags.Armor> armorMode;
   private final Setting<NameTags.Health> health;

   public NameTags() {
      super("NameTags", Module.Category.RENDER);
      this.outline = new Setting("OutlineType", NameTags.OutlineColor.New);
      this.friendOutline = new Setting("FriendOutline", NameTags.OutlineColor.None);
      this.outlineColor = new Setting("OutlineColor", new ColorSetting(Integer.MIN_VALUE));
      this.friendOutlineColor = new Setting("FriendOutlineColor", new ColorSetting(Integer.MIN_VALUE));
      this.enchantss = new Setting("Enchants", true);
      this.onlyHands = new Setting("OnlyHands", false, (v) -> {
         return (Boolean)this.enchantss.getValue();
      });
      this.funtimeHp = new Setting("FunTimeHp", false);
      this.ignoreBots = new Setting("IgnoreBots", false);
      this.potions = new Setting("Potions", true);
      this.shulkers = new Setting("Shulkers", true);
      this.fillColorA = new Setting("Fill", new ColorSetting(Integer.MIN_VALUE));
      this.fillColorF = new Setting("FriendFill", new ColorSetting(Integer.MIN_VALUE));
      this.font = new Setting("FontMode", NameTags.Font.Fancy);
      this.armorMode = new Setting("ArmorMode", NameTags.Armor.Full);
      this.health = new Setting("Health", NameTags.Health.Number);
      this.encMap.put(class_1893.field_9107, "B");
      this.encMap.put(class_1893.field_9111, "P");
      this.encMap.put(class_1893.field_9118, "S");
      this.encMap.put(class_1893.field_9131, "E");
      this.encMap.put(class_1893.field_9119, "U");
      this.encMap.put(class_1893.field_9103, "PO");
      this.encMap.put(class_1893.field_9097, "T");
   }

   public void onRender2D(class_332 context) {
      if (!mc.field_1690.field_1842) {
         Iterator var2 = mc.field_1687.method_18456().iterator();

         while(true) {
            class_1657 ent;
            float scale;
            Vector4d position;
            String final_string;
            do {
               label174:
               do {
                  while(var2.hasNext()) {
                     ent = (class_1657)var2.next();
                     if (ent != mc.field_1724 || !mc.field_1690.method_31044().method_31034() && (Boolean)this.self.getValue()) {
                        continue label174;
                     }
                  }

                  if ((Boolean)this.spawners.getValue()) {
                     this.drawSpawnerNameTag(context);
                  }

                  if ((Boolean)this.entityOwner.getValue()) {
                     this.drawEntityOwner(context);
                  }

                  return;
               } while(getEntityPing(ent) <= 0 && (Boolean)this.ignoreBots.getValue());

               double x = ent.field_6014 + (ent.method_23317() - ent.field_6014) * (double)Render3DEngine.getTickDelta();
               double y = ent.field_6036 + (ent.method_23318() - ent.field_6036) * (double)Render3DEngine.getTickDelta();
               double z = ent.field_5969 + (ent.method_23321() - ent.field_5969) * (double)Render3DEngine.getTickDelta();
               scale = (Boolean)this.resize.getValue() ? (Float)this.scale.getValue() / mc.field_1724.method_5739(ent) : (Float)this.scale.getValue();
               class_243 vector = new class_243(x, y + (double)(Float)this.height.getValue(), z);
               position = null;
               vector = Render3DEngine.worldSpaceToScreenSpace(vector);
               if (vector.field_1350 > 0.0D && vector.field_1350 < 1.0D) {
                  position = new Vector4d(vector.field_1352, vector.field_1351, vector.field_1350, 0.0D);
                  position.x = Math.min(vector.field_1352, position.x);
                  position.y = Math.min(vector.field_1351, position.y);
                  position.z = Math.max(vector.field_1352, position.z);
               }

               final_string = "";
               if ((Boolean)this.ping.getValue()) {
                  final_string = final_string + this.getPingColor(getEntityPing(ent)) + getEntityPing(ent) + "ms " + String.valueOf(class_124.field_1068);
               }

               if ((Boolean)this.gamemode.getValue()) {
                  final_string = final_string + this.translateGamemode(getEntityGamemode(ent)) + " ";
               }

               if (FriendManager.friends.stream().anyMatch((ix) -> {
                  return ix.contains(ent.method_5476().getString());
               }) && (Boolean)NameProtect.hideFriends.getValue() && ModuleManager.nameProtect.isEnabled()) {
                  final_string = final_string + NameProtect.getCustomName() + " ";
               } else {
                  final_string = final_string + ent.method_5476().getString() + " ";
               }

               if ((Boolean)this.hp.getValue() && this.health.is(NameTags.Health.Number)) {
                  final_string = final_string + this.getHealthColor(this.getHealth(ent)) + round2(this.getHealth(ent)) + " ";
               }

               if ((Boolean)this.distance.getValue()) {
                  final_string = final_string + String.format("%.1f", mc.field_1724.method_5739(ent)) + "m ";
               }

               if ((Boolean)this.pops.getValue() && Managers.COMBAT.getPops(ent) != 0) {
                  final_string = final_string + String.valueOf(class_124.field_1070) + Managers.COMBAT.getPops(ent);
               }
            } while(position == null);

            double posX = position.x;
            double posY = position.y;
            double endPosX = position.z;
            double maxEnchantY = 0.0D;
            float diff = (float)(endPosX - posX) / 2.0F;
            float textWidth;
            if (this.font.getValue() == NameTags.Font.Fancy) {
               textWidth = FontRenderers.sf_bold.getStringWidth(final_string) * 1.0F;
            } else {
               textWidth = (float)mc.field_1772.method_1727(final_string);
            }

            float tagX = (float)((posX + (double)diff - (double)(textWidth / 2.0F)) * 1.0D);
            ArrayList<class_1799> stacks = new ArrayList();
            if (this.armorMode.getValue() != NameTags.Armor.Durability) {
               stacks.add(ent.method_6079());
            }

            stacks.add((class_1799)ent.method_31548().field_7548.get(0));
            stacks.add((class_1799)ent.method_31548().field_7548.get(1));
            stacks.add((class_1799)ent.method_31548().field_7548.get(2));
            stacks.add((class_1799)ent.method_31548().field_7548.get(3));
            if (this.armorMode.getValue() != NameTags.Armor.Durability) {
               stacks.add(ent.method_6047());
            }

            context.method_51448().method_22903();
            context.method_51448().method_46416(tagX - 2.0F + (textWidth + 4.0F) / 2.0F, (float)(posY - 13.0D) + 6.5F, 0.0F);
            context.method_51448().method_22905(scale, scale, 1.0F);
            context.method_51448().method_46416(-(tagX - 2.0F + (textWidth + 4.0F) / 2.0F), -((float)(posY - 13.0D + 6.5D)), 0.0F);
            float item_offset = 0.0F;
            if (this.armorMode.getValue() != NameTags.Armor.None) {
               for(Iterator var27 = stacks.iterator(); var27.hasNext(); item_offset += 18.0F) {
                  class_1799 armorComponent = (class_1799)var27.next();
                  if (!armorComponent.method_7960()) {
                     float enchantmentY;
                     if (this.armorMode.getValue() == NameTags.Armor.Full) {
                        context.method_51448().method_22903();
                        context.method_51448().method_22904(posX - 55.0D + (double)item_offset, (double)((float)(posY - 33.0D)), 0.0D);
                        context.method_51448().method_22905(1.1F, 1.1F, 1.1F);
                        class_308.method_24210();
                        context.method_51427(armorComponent, 0, 0);
                        context.method_51431(mc.field_1772, armorComponent, 0, 0);
                        context.method_51448().method_22909();
                     } else {
                        context.method_51448().method_22903();
                        context.method_51448().method_22904(posX - 35.0D + (double)item_offset, (double)((float)(posY - 20.0D)), 0.0D);
                        context.method_51448().method_22905(0.7F, 0.7F, 0.7F);
                        enchantmentY = (float)(armorComponent.method_7936() - armorComponent.method_7919());
                        int percent = (int)(enchantmentY / (float)armorComponent.method_7936() * 100.0F);
                        Color color;
                        if (percent < 33) {
                           color = Color.RED;
                        } else if (percent > 33 && percent < 66) {
                           color = Color.YELLOW;
                        } else {
                           color = Color.GREEN;
                        }

                        context.method_51433(mc.field_1772, percent + "%", 0, 0, color.getRGB(), false);
                        context.method_51448().method_22909();
                     }

                     enchantmentY = 0.0F;
                     class_9304 enchants = class_1890.method_57532(armorComponent);
                     if ((Boolean)this.enchantss.getValue() && (!(Boolean)this.onlyHands.getValue() || armorComponent == ent.method_6079() || armorComponent == ent.method_6047())) {
                        Iterator var44 = this.encMap.keySet().iterator();

                        while(var44.hasNext()) {
                           class_5321<class_1887> enchantment = (class_5321)var44.next();
                           if (enchants.method_57534().contains(mc.field_1687.method_30349().method_30530(class_1893.field_9111.method_58273()).method_40264(enchantment).get())) {
                              String id = (String)this.encMap.get(enchantment);
                              int level = enchants.method_57536((class_6880)mc.field_1687.method_30349().method_30530(class_1893.field_9111.method_58273()).method_40264(enchantment).get());
                              String encName = id + level;
                              if (this.font.getValue() == NameTags.Font.Fancy) {
                                 FontRenderers.sf_bold.drawString(context.method_51448(), encName, posX - 50.0D + (double)item_offset, (double)((float)posY - 45.0F + enchantmentY), -1);
                              } else {
                                 context.method_51448().method_22903();
                                 context.method_51448().method_22904(posX - 50.0D + (double)item_offset, posY - 45.0D + (double)enchantmentY, 0.0D);
                                 context.method_51433(mc.field_1772, encName, 0, 0, -1, false);
                                 context.method_51448().method_22909();
                              }

                              enchantmentY -= 8.0F;
                              if (maxEnchantY > (double)enchantmentY) {
                                 maxEnchantY = (double)enchantmentY;
                              }
                           }
                        }
                     }
                  }
               }
            }

            Color color = Managers.FRIEND.isFriend(ent) ? ((ColorSetting)this.fillColorF.getValue()).getColorObject() : ((ColorSetting)this.fillColorA.getValue()).getColorObject();
            NameTags.OutlineColor cl = Managers.FRIEND.isFriend(ent) ? (NameTags.OutlineColor)this.friendOutline.getValue() : (NameTags.OutlineColor)this.outline.getValue();
            if (cl == NameTags.OutlineColor.New) {
               Render2DEngine.drawRectWithOutline(context.method_51448(), tagX - 2.0F, (float)(posY - 13.0D), textWidth + 4.0F, 11.0F, color, ((ColorSetting)this.outlineColor.getValue()).getColorObject());
            } else {
               Render2DEngine.drawRect(context.method_51448(), tagX - 2.0F, (float)(posY - 13.0D), textWidth + 4.0F, 11.0F, color);
            }

            if (Managers.TELEMETRY.getOnlinePlayers().contains(ent.method_7334().getName())) {
               Render2DEngine.drawRect(context.method_51448(), tagX - 14.0F, (float)(posY - 13.0D), 12.0F, 11.0F, color.brighter().brighter());
               RenderSystem.enableBlend();
               RenderSystem.blendFunc(class_4535.SRC_ALPHA, class_4534.ONE);
               Color lColor = HudEditor.getColor(0);
               RenderSystem.setShaderColor((float)lColor.getRed() / 255.0F, (float)lColor.getGreen() / 255.0F, (float)lColor.getBlue() / 255.0F, 1.0F);
               RenderSystem.setShaderTexture(0, TextureStorage.miniLogo);
               Render2DEngine.renderTexture(context.method_51448(), (double)(tagX - 13.0F), (double)((float)(posY - 12.5D)), 10.0D, 10.0D, 0.0F, 0.0F, 256.0D, 256.0D, 256.0D, 256.0D);
               RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
               RenderSystem.disableBlend();
            }

            switch(cl.ordinal()) {
            case 0:
               Render2DEngine.drawRect(context.method_51448(), tagX - 3.0F, (float)(posY - 14.0D), textWidth + 6.0F, 1.0F, HudEditor.getColor(270));
               Render2DEngine.drawRect(context.method_51448(), tagX - 3.0F, (float)(posY - 3.0D), textWidth + 6.0F, 1.0F, HudEditor.getColor(0));
               Render2DEngine.drawRect(context.method_51448(), tagX - 3.0F, (float)(posY - 14.0D), 1.0F, 11.0F, HudEditor.getColor(180));
               Render2DEngine.drawRect(context.method_51448(), tagX + textWidth + 2.0F, (float)(posY - 14.0D), 1.0F, 11.0F, HudEditor.getColor(90));
               break;
            case 1:
               Render2DEngine.drawRect(context.method_51448(), tagX - 3.0F, (float)(posY - 14.0D), textWidth + 6.0F, 1.0F, Managers.FRIEND.isFriend(ent) ? ((ColorSetting)this.friendOutlineColor.getValue()).getColorObject() : ((ColorSetting)this.outlineColor.getValue()).getColorObject());
               Render2DEngine.drawRect(context.method_51448(), tagX - 3.0F, (float)(posY - 3.0D), textWidth + 6.0F, 1.0F, Managers.FRIEND.isFriend(ent) ? ((ColorSetting)this.friendOutlineColor.getValue()).getColorObject() : ((ColorSetting)this.outlineColor.getValue()).getColorObject());
               Render2DEngine.drawRect(context.method_51448(), tagX - 3.0F, (float)(posY - 14.0D), 1.0F, 11.0F, Managers.FRIEND.isFriend(ent) ? ((ColorSetting)this.friendOutlineColor.getValue()).getColorObject() : ((ColorSetting)this.outlineColor.getValue()).getColorObject());
               Render2DEngine.drawRect(context.method_51448(), tagX + textWidth + 2.0F, (float)(posY - 14.0D), 1.0F, 11.0F, Managers.FRIEND.isFriend(ent) ? ((ColorSetting)this.friendOutlineColor.getValue()).getColorObject() : ((ColorSetting)this.outlineColor.getValue()).getColorObject());
            case 2:
            case 3:
            }

            if (this.font.getValue() == NameTags.Font.Fancy) {
               FontRenderers.sf_bold.drawString(context.method_51448(), final_string, (double)tagX, (double)((float)posY - 10.0F), -1);
            } else {
               context.method_51448().method_22903();
               context.method_51448().method_46416(tagX, (float)posY - 11.0F, 0.0F);
               context.method_51433(mc.field_1772, final_string, 0, 0, -1, false);
               context.method_51448().method_22909();
            }

            if (!this.health.is(NameTags.Health.Number)) {
               int i = class_3532.method_15386(ent.method_6032());
               float f = (float)ent.method_45325(class_5134.field_23716);
               int p = class_3532.method_15386(ent.method_6067());
               context.method_51448().method_22903();
               context.method_51448().method_22904(posX - 44.0D, posY, 0.0D);
               context.method_51448().method_22905(1.1F, 1.1F, 1.0F);
               this.renderHealthBar(context, ent, f, i, p);
               context.method_51448().method_22909();
            }

            if ((Boolean)this.potions.getValue()) {
               this.renderStatusEffectOverlay(context, (float)posX, (float)(posY + maxEnchantY - 60.0D), ent);
            }

            class_1792 handItem = ent.method_6047().method_7909();
            if ((Boolean)this.shulkers.getValue()) {
               int var10002;
               int var10003;
               class_1799 var10004;
               label251: {
                  var10002 = (int)posX - 90;
                  var10003 = (int)posY - 120;
                  if (handItem instanceof class_1747) {
                     class_1747 bi = (class_1747)handItem;
                     if (bi.method_7711() instanceof class_2480) {
                        var10004 = ent.method_6047();
                        break label251;
                     }
                  }

                  var10004 = ent.method_6079();
               }

               this.renderShulkerToolTip(context, var10002, var10003, var10004);
            }

            context.method_51448().method_22909();
         }
      }
   }

   private void drawSpawnerNameTag(class_332 context) {
      Iterator var2 = StorageEsp.getBlockEntities().iterator();

      while(var2.hasNext()) {
         class_2586 blockEntity = (class_2586)var2.next();
         if (blockEntity instanceof class_2636) {
            class_2636 spawner = (class_2636)blockEntity;
            class_243 vector = new class_243((double)spawner.method_11016().method_10263() + 0.5D, (double)spawner.method_11016().method_10264() + 1.5D, (double)spawner.method_11016().method_10260() + 0.5D);
            Vector4d position = null;
            vector = Render3DEngine.worldSpaceToScreenSpace(new class_243(vector.field_1352, vector.field_1351, vector.field_1350));
            if (vector.field_1350 > 0.0D && vector.field_1350 < 1.0D) {
               position = new Vector4d(vector.field_1352, vector.field_1351, vector.field_1350, 0.0D);
               position.x = Math.min(vector.field_1352, position.x);
               position.y = Math.min(vector.field_1351, position.y);
               position.z = Math.max(vector.field_1352, position.z);
            }

            if (spawner.method_11390() != null && spawner.method_11390().method_8283(mc.field_1687, spawner.method_11016()) != null) {
               String var10000 = spawner.method_11390().method_8283(mc.field_1687, spawner.method_11016()).method_5477().getString();
               String final_string = var10000 + " " + String.format("%.1f", (float)spawner.method_11390().field_9154 / 20.0F) + "s";
               if (spawner.method_11390().method_8278() == spawner.method_11390().method_8279() && spawner.method_11390().method_8278() == 0.0D && (float)spawner.method_11390().field_9154 / 20.0F == 1.0F) {
                  class_2561 var17 = spawner.method_11390().method_8283(mc.field_1687, spawner.method_11016()).method_5477();
                  final_string = var17.getString() + " loot!";
               }

               if (position != null) {
                  double posX = position.x;
                  double posY = position.y;
                  double endPosX = position.z;
                  float diff = (float)(endPosX - posX) / 2.0F;
                  float textWidth = FontRenderers.sf_bold.getStringWidth(final_string) * 1.0F;
                  float tagX = (float)((posX + (double)diff - (double)(textWidth / 2.0F)) * 1.0D);
                  Render2DEngine.drawRect(context.method_51448(), tagX - 2.0F, (float)(posY - 13.0D), textWidth + 4.0F, 11.0F, ((ColorSetting)this.fillColorA.getValue()).getColorObject());
                  switch(((NameTags.OutlineColor)this.outline.getValue()).ordinal()) {
                  case 0:
                     Render2DEngine.drawRect(context.method_51448(), tagX - 3.0F, (float)(posY - 14.0D), textWidth + 6.0F, 1.0F, HudEditor.getColor(270));
                     Render2DEngine.drawRect(context.method_51448(), tagX - 3.0F, (float)(posY - 3.0D), textWidth + 6.0F, 1.0F, HudEditor.getColor(0));
                     Render2DEngine.drawRect(context.method_51448(), tagX - 3.0F, (float)(posY - 14.0D), 1.0F, 11.0F, HudEditor.getColor(180));
                     Render2DEngine.drawRect(context.method_51448(), tagX + textWidth + 2.0F, (float)(posY - 14.0D), 1.0F, 11.0F, HudEditor.getColor(90));
                     break;
                  case 1:
                     Render2DEngine.drawRect(context.method_51448(), tagX - 3.0F, (float)(posY - 14.0D), textWidth + 6.0F, 1.0F, ((ColorSetting)this.outlineColor.getValue()).getColorObject());
                     Render2DEngine.drawRect(context.method_51448(), tagX - 3.0F, (float)(posY - 3.0D), textWidth + 6.0F, 1.0F, ((ColorSetting)this.outlineColor.getValue()).getColorObject());
                     Render2DEngine.drawRect(context.method_51448(), tagX - 3.0F, (float)(posY - 14.0D), 1.0F, 11.0F, ((ColorSetting)this.outlineColor.getValue()).getColorObject());
                     Render2DEngine.drawRect(context.method_51448(), tagX + textWidth + 2.0F, (float)(posY - 14.0D), 1.0F, 11.0F, ((ColorSetting)this.outlineColor.getValue()).getColorObject());
                  case 2:
                  }

                  FontRenderers.sf_bold.drawString(context.method_51448(), final_string, (double)tagX, (double)((float)posY - 10.0F), -1);
               }
            }
         }
      }

   }

   public void drawEntityOwner(class_332 context) {
      Iterator var2 = mc.field_1687.method_18112().iterator();

      while(true) {
         class_1297 ent;
         String ownerName;
         while(true) {
            if (!var2.hasNext()) {
               return;
            }

            ent = (class_1297)var2.next();
            ownerName = "";
            if (ent instanceof class_1676) {
               class_1676 pe = (class_1676)ent;
               if (pe.method_24921() != null) {
                  ownerName = pe.method_24921().method_5476().getString();
               }
               break;
            }

            if (ent instanceof class_1498) {
               class_1498 he = (class_1498)ent;
               if (he.method_6139() != null) {
                  ownerName = he.method_6139().toString();
               }
               break;
            }

            if (ent instanceof class_1321) {
               class_1321 te = (class_1321)ent;
               if (te.method_6181() && te.method_35057() != null) {
                  ownerName = te.method_35057().method_5476().getString();
                  break;
               }
            }
         }

         String final_string = "Owned by " + ownerName;
         double x = ent.field_6014 + (ent.method_23317() - ent.field_6014) * (double)Render3DEngine.getTickDelta();
         double y = ent.field_6036 + (ent.method_23318() - ent.field_6036) * (double)Render3DEngine.getTickDelta();
         double z = ent.field_5969 + (ent.method_23321() - ent.field_5969) * (double)Render3DEngine.getTickDelta();
         class_243 vector = new class_243(x, y + 2.0D, z);
         Vector4d position = null;
         vector = Render3DEngine.worldSpaceToScreenSpace(new class_243(vector.field_1352, vector.field_1351, vector.field_1350));
         if (vector.field_1350 > 0.0D && vector.field_1350 < 1.0D) {
            position = new Vector4d(vector.field_1352, vector.field_1351, vector.field_1350, 0.0D);
            position.x = Math.min(vector.field_1352, position.x);
            position.y = Math.min(vector.field_1351, position.y);
            position.z = Math.max(vector.field_1352, position.z);
         }

         if (position != null) {
            double posX = position.x;
            double posY = position.y;
            double endPosX = position.z;
            float diff = (float)(endPosX - posX) / 2.0F;
            float textWidth = FontRenderers.sf_bold.getStringWidth(final_string) * 1.0F;
            float tagX = (float)((posX + (double)diff - (double)(textWidth / 2.0F)) * 1.0D);
            Render2DEngine.drawRect(context.method_51448(), tagX - 2.0F, (float)(posY - 13.0D), textWidth + 4.0F, 11.0F, ((ColorSetting)this.fillColorA.getValue()).getColorObject());
            switch(((NameTags.OutlineColor)this.outline.getValue()).ordinal()) {
            case 0:
               Render2DEngine.drawRect(context.method_51448(), tagX - 3.0F, (float)(posY - 14.0D), textWidth + 6.0F, 1.0F, HudEditor.getColor(270));
               Render2DEngine.drawRect(context.method_51448(), tagX - 3.0F, (float)(posY - 3.0D), textWidth + 6.0F, 1.0F, HudEditor.getColor(0));
               Render2DEngine.drawRect(context.method_51448(), tagX - 3.0F, (float)(posY - 14.0D), 1.0F, 11.0F, HudEditor.getColor(180));
               Render2DEngine.drawRect(context.method_51448(), tagX + textWidth + 2.0F, (float)(posY - 14.0D), 1.0F, 11.0F, HudEditor.getColor(90));
               break;
            case 1:
               Render2DEngine.drawRect(context.method_51448(), tagX - 3.0F, (float)(posY - 14.0D), textWidth + 6.0F, 1.0F, ((ColorSetting)this.outlineColor.getValue()).getColorObject());
               Render2DEngine.drawRect(context.method_51448(), tagX - 3.0F, (float)(posY - 3.0D), textWidth + 6.0F, 1.0F, ((ColorSetting)this.outlineColor.getValue()).getColorObject());
               Render2DEngine.drawRect(context.method_51448(), tagX - 3.0F, (float)(posY - 14.0D), 1.0F, 11.0F, ((ColorSetting)this.outlineColor.getValue()).getColorObject());
               Render2DEngine.drawRect(context.method_51448(), tagX + textWidth + 2.0F, (float)(posY - 14.0D), 1.0F, 11.0F, ((ColorSetting)this.outlineColor.getValue()).getColorObject());
            case 2:
            }

            FontRenderers.sf_bold.drawString(context.method_51448(), final_string, (double)tagX, (double)((float)posY - 10.0F), -1);
         }
      }
   }

   public static int getEntityPing(class_1657 entity) {
      if (mc.method_1562() == null) {
         return 0;
      } else {
         class_640 playerListEntry = mc.method_1562().method_2871(entity.method_5667());
         return playerListEntry == null ? 0 : playerListEntry.method_2959();
      }
   }

   public static class_1934 getEntityGamemode(class_1657 entity) {
      if (entity == null) {
         return null;
      } else {
         class_640 playerListEntry = mc.method_1562().method_2871(entity.method_5667());
         return playerListEntry == null ? null : playerListEntry.method_2958();
      }
   }

   private String translateGamemode(class_1934 gamemode) {
      if (gamemode == null) {
         return "[BOT]";
      } else {
         String var10000;
         switch(gamemode) {
         case field_9215:
            var10000 = "[S]";
            break;
         case field_9220:
            var10000 = "[C]";
            break;
         case field_9219:
            var10000 = "[SP]";
            break;
         case field_9216:
            var10000 = "[A]";
            break;
         default:
            throw new MatchException((String)null, (Throwable)null);
         }

         return var10000;
      }
   }

   public float getHealth(class_1657 ent) {
      if ((mc.method_1562() == null || mc.method_1562().method_45734() == null || !mc.method_1562().method_45734().field_3761.contains("funtime")) && !(Boolean)this.funtimeHp.getValue()) {
         return ent.method_6032() + ent.method_6067();
      } else {
         class_266 scoreBoard = null;
         String resolvedHp = "";
         if (ent.method_7327().method_1189(class_8646.field_45158) != null) {
            scoreBoard = ent.method_7327().method_1189(class_8646.field_45158);
            if (scoreBoard != null) {
               class_9013 readableScoreboardScore = ent.method_7327().method_55430(ent, scoreBoard);
               class_5250 text2 = class_9013.method_55398(readableScoreboardScore, scoreBoard.method_55380(class_9025.field_47566));
               resolvedHp = text2.getString();
            }
         }

         float numValue = 0.0F;

         try {
            numValue = Float.parseFloat(resolvedHp);
         } catch (NumberFormatException var6) {
         }

         return numValue;
      }
   }

   private void renderHealthBar(class_332 context, class_1657 player, float maxHealth, int lastHealth, int absorption) {
      int i = class_3532.method_15384((double)maxHealth / 2.0D);
      int j = class_3532.method_15384((double)absorption / 2.0D);
      int k = i * 2;
      int cont = 0;

      for(int l = i + j - 1; l >= 0; --l) {
         int n = l % 10;
         int o = n * 8;
         if (cont < 10) {
            this.drawHeart(context, NameTags.HeartType.CONTAINER, o, false, player);
            ++cont;
         }

         int q = l * 2;
         if (q < lastHealth) {
            this.drawHeart(context, NameTags.HeartType.NORMAL, o, q + 1 == lastHealth, player);
         }

         if (l >= i) {
            int r = q - k;
            if (q - k < absorption) {
               context.method_51448().method_22903();
               context.method_51448().method_46416(0.0F, 0.0F, 0.001F);
               this.drawHeart(context, NameTags.HeartType.ABSORBING, o, r + 1 == absorption, player);
               context.method_51448().method_22909();
            }
         }
      }

   }

   private void drawHeart(class_332 context, NameTags.HeartType type, int x, boolean half, class_1657 player) {
      if (this.health.is(NameTags.Health.Dots)) {
         Color color = Managers.FRIEND.isFriend(player) ? ((ColorSetting)this.fillColorF.getValue()).getColorObject() : ((ColorSetting)this.fillColorA.getValue()).getColorObject();
         if (type == NameTags.HeartType.CONTAINER) {
            Render2DEngine.drawRect(context.method_51448(), (float)x, 0.0F, 7.0F, 3.0F, color);
         } else if (type == NameTags.HeartType.NORMAL) {
            if (half) {
               Render2DEngine.drawRect(context.method_51448(), (float)x, 0.0F, 3.0F, 3.0F, this.getHealthColor2(player.method_6032() + player.method_6067()));
               Render2DEngine.drawRect(context.method_51448(), (float)(x + 3), 0.0F, 4.0F, 3.0F, color);
            } else {
               Render2DEngine.drawRect(context.method_51448(), (float)x, 0.0F, 7.0F, 3.0F, this.getHealthColor2(player.method_6032() + player.method_6067()));
            }
         }
      } else {
         context.method_52706(type.getTexture(half), x, 0, 9, 9);
      }

   }

   @NotNull
   public String getHealthColor(float health) {
      if (health <= 15.0F && health > 7.0F) {
         return String.valueOf(class_124.field_1054).makeConcatWithConstants<invokedynamic>(String.valueOf(class_124.field_1054));
      } else {
         return health > 15.0F ? String.valueOf(class_124.field_1060).makeConcatWithConstants<invokedynamic>(String.valueOf(class_124.field_1060)) : String.valueOf(class_124.field_1061).makeConcatWithConstants<invokedynamic>(String.valueOf(class_124.field_1061));
      }
   }

   @NotNull
   public String getPingColor(int ping) {
      if (ping <= 60) {
         return String.valueOf(class_124.field_1060).makeConcatWithConstants<invokedynamic>(String.valueOf(class_124.field_1060));
      } else {
         return ping > 60 && ping < 120 ? String.valueOf(class_124.field_1054).makeConcatWithConstants<invokedynamic>(String.valueOf(class_124.field_1054)) : String.valueOf(class_124.field_1061).makeConcatWithConstants<invokedynamic>(String.valueOf(class_124.field_1061));
      }
   }

   @NotNull
   private Color getHealthColor2(float health) {
      if (health <= 15.0F && health > 7.0F) {
         return Color.YELLOW;
      } else {
         return health > 15.0F ? Color.GREEN : Color.RED;
      }
   }

   public static float round2(float value) {
      if (!Float.isNaN(value) && !Float.isInfinite(value)) {
         BigDecimal bd = new BigDecimal((double)value);
         bd = bd.setScale(1, RoundingMode.HALF_UP);
         return bd.floatValue();
      } else {
         return 1.0F;
      }
   }

   private void renderStatusEffectOverlay(class_332 context, float x, float y, class_1657 player) {
      ArrayList<class_1293> effects = new ArrayList(player.method_6026());
      if (!effects.isEmpty()) {
         x += (float)effects.size() * 12.5F;
         RenderSystem.enableBlend();
         RenderSystem.defaultBlendFunc();
         Iterator var6 = Ordering.natural().reverse().sortedCopy(effects).iterator();

         while(var6.hasNext()) {
            class_1293 statusEffectInstance = (class_1293)var6.next();
            x -= 25.0F;
            String power = "";
            switch(statusEffectInstance.method_5578()) {
            case 0:
               power = "I";
               break;
            case 1:
               power = "II";
               break;
            case 2:
               power = "III";
               break;
            case 3:
               power = "IV";
               break;
            case 4:
               power = "V";
            }

            context.method_51448().method_22903();
            context.method_51448().method_46416(x, y, 0.0F);
            context.method_25298(0, 0, 0, 18, 18, mc.method_18505().method_18663(statusEffectInstance.method_5579()));
            FontRenderers.sf_bold_mini.drawCenteredString(context.method_51448(), PotionHud.getDuration(statusEffectInstance), 9.0D, -8.0D, -1);
            FontRenderers.categories.drawCenteredString(context.method_51448(), power, 9.0D, -16.0D, -1);
            context.method_51448().method_22909();
         }

         RenderSystem.disableBlend();
      }
   }

   public boolean renderShulkerToolTip(class_332 context, int offsetX, int offsetY, class_1799 stack) {
      try {
         class_9288 compoundTag = (class_9288)stack.method_57824(class_9334.field_49622);
         if (compoundTag == null) {
            return false;
         } else {
            float[] var10000 = new float[]{1.0F, 1.0F, 1.0F};
            class_1792 focusedItem = stack.method_7909();
            if (focusedItem instanceof class_1747) {
               class_1747 bi = (class_1747)focusedItem;
               if (bi.method_7711() instanceof class_2480) {
                  float[] colors;
                  try {
                     Color c = new Color(((class_1767)Objects.requireNonNull(class_2480.method_10527(stack.method_7909()))).method_7787());
                     colors = new float[]{(float)c.getRed() / 255.0F, (float)c.getGreen() / 255.0F, (float)c.getRed() / 255.0F, (float)c.getAlpha() / 255.0F};
                  } catch (NullPointerException var10) {
                     colors = new float[]{1.0F, 1.0F, 1.0F};
                  }

                  this.draw(context, compoundTag.method_57489().toList(), offsetX, offsetY, colors);
                  return true;
               }
            }

            return false;
         }
      } catch (Exception var11) {
         return false;
      }
   }

   private void draw(class_332 context, List<class_1799> itemStacks, int offsetX, int offsetY, float[] colors) {
      RenderSystem.disableDepthTest();
      GL11.glClear(256);
      offsetX += 8;
      offsetY -= 82;
      this.drawBackground(context, offsetX, offsetY, colors);
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      class_308.method_24211();
      int row = 0;
      int i = 0;
      Iterator var8 = itemStacks.iterator();

      while(var8.hasNext()) {
         class_1799 itemStack = (class_1799)var8.next();
         context.method_51427(itemStack, offsetX + 8 + i * 18, offsetY + 7 + row * 18);
         context.method_51431(mc.field_1772, itemStack, offsetX + 8 + i * 18, offsetY + 7 + row * 18);
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

   private static enum OutlineColor {
      Sync,
      Custom,
      None,
      New;

      // $FF: synthetic method
      private static NameTags.OutlineColor[] $values() {
         return new NameTags.OutlineColor[]{Sync, Custom, None, New};
      }
   }

   public static enum Font {
      Fancy,
      Fast;

      // $FF: synthetic method
      private static NameTags.Font[] $values() {
         return new NameTags.Font[]{Fancy, Fast};
      }
   }

   public static enum Armor {
      None,
      Full,
      Durability;

      // $FF: synthetic method
      private static NameTags.Armor[] $values() {
         return new NameTags.Armor[]{None, Full, Durability};
      }
   }

   public static enum Health {
      Number,
      Hearts,
      Dots;

      // $FF: synthetic method
      private static NameTags.Health[] $values() {
         return new NameTags.Health[]{Number, Hearts, Dots};
      }
   }

   private static enum HeartType {
      CONTAINER(class_2960.method_60654("hud/heart/container"), class_2960.method_60654("hud/heart/container")),
      NORMAL(class_2960.method_60654("hud/heart/full"), class_2960.method_60654("hud/heart/half")),
      ABSORBING(class_2960.method_60654("hud/heart/absorbing_full"), class_2960.method_60654("hud/heart/absorbing_half"));

      private final class_2960 fullTexture;
      private final class_2960 halfTexture;

      private HeartType(class_2960 fullTexture, class_2960 halfTexture) {
         this.fullTexture = fullTexture;
         this.halfTexture = halfTexture;
      }

      public class_2960 getTexture(boolean half) {
         return half ? this.halfTexture : this.fullTexture;
      }

      // $FF: synthetic method
      private static NameTags.HeartType[] $values() {
         return new NameTags.HeartType[]{CONTAINER, NORMAL, ABSORBING};
      }
   }
}
