package tsunami.features.hud.impl;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import net.minecraft.class_124;
import net.minecraft.class_1291;
import net.minecraft.class_1293;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1959;
import net.minecraft.class_2960;
import net.minecraft.class_332;
import net.minecraft.class_3545;
import net.minecraft.class_408;
import net.minecraft.class_7924;
import org.apache.commons.lang3.StringUtils;
import tsunami.TsunamiClient;
import tsunami.core.Managers;
import tsunami.core.manager.client.ModuleManager;
import tsunami.features.modules.Module;
import tsunami.gui.font.FontRenderer;
import tsunami.gui.font.FontRenderers;
import tsunami.setting.Setting;
import tsunami.setting.impl.ColorSetting;
import tsunami.utility.math.FrameRateCounter;
import tsunami.utility.math.MathUtility;

public class LegacyHud extends Module {
   private static final class_1799 totem;
   private final Setting<LegacyHud.Font> customFont;
   private final Setting<ColorSetting> colorSetting;
   private final Setting<Boolean> renderingUp;
   private final Setting<Boolean> waterMark;
   private final Setting<Boolean> arrayList;
   private final Setting<Boolean> coords;
   private final Setting<Boolean> direction;
   private final Setting<Boolean> armor;
   private final Setting<Boolean> totems;
   private final Setting<Boolean> greeter;
   private final Setting<Boolean> speed;
   private final Setting<Boolean> bps;
   public final Setting<Boolean> potions;
   private final Setting<Boolean> ping;
   private final Setting<Boolean> tps;
   private final Setting<Boolean> extraTps;
   private final Setting<Boolean> offhandDurability;
   private final Setting<Boolean> mainhandDurability;
   private final Setting<Boolean> fps;
   private final Setting<Boolean> chests;
   private final Setting<Boolean> worldTime;
   private final Setting<Boolean> biome;
   public Setting<Boolean> time;
   public Setting<Integer> waterMarkY;
   private int color;

   public LegacyHud() {
      super("LegacyHud", Module.Category.HUD);
      this.customFont = new Setting("Font", LegacyHud.Font.Minecraft);
      this.colorSetting = new Setting("Color", new ColorSetting(new Color(30719)));
      this.renderingUp = new Setting("RenderingUp", false);
      this.waterMark = new Setting("Watermark", false);
      this.arrayList = new Setting("ActiveModules", false);
      this.coords = new Setting("Coords", false);
      this.direction = new Setting("Direction", false);
      this.armor = new Setting("Armor", false);
      this.totems = new Setting("Totems", false);
      this.greeter = new Setting("Welcomer", false);
      this.speed = new Setting("Speed", false);
      this.bps = new Setting("BPS", false, (v) -> {
         return (Boolean)this.speed.getValue();
      });
      this.potions = new Setting("Potions", false);
      this.ping = new Setting("Ping", false);
      this.tps = new Setting("TPS", false);
      this.extraTps = new Setting("ExtraTPS", true, (v) -> {
         return (Boolean)this.tps.getValue();
      });
      this.offhandDurability = new Setting("OffhandDurability", false);
      this.mainhandDurability = new Setting("MainhandDurability", false);
      this.fps = new Setting("FPS", false);
      this.chests = new Setting("Chests", false);
      this.worldTime = new Setting("WorldTime", false);
      this.biome = new Setting("Biome", false);
      this.time = new Setting("Time", false);
      this.waterMarkY = new Setting("WatermarkPosY", 2, 0, 20, (v) -> {
         return (Boolean)this.waterMark.getValue();
      });
   }

   public void onRender2D(class_332 context) {
      if (!fullNullCheck()) {
         int width = mc.method_22683().method_4486();
         int height = mc.method_22683().method_4502();
         byte offset;
         switch(((LegacyHud.Font)this.customFont.getValue()).ordinal()) {
         case 0:
            offset = 10;
            break;
         case 2:
            offset = 9;
            break;
         default:
            offset = 8;
         }

         this.color = ((ColorSetting)this.colorSetting.getValue()).getColor();
         if ((Boolean)this.waterMark.getValue()) {
            this.drawText(context, "thunderhack v1.7b2407", 2, (Integer)this.waterMarkY.getValue());
         }

         int j = mc.field_1755 instanceof class_408 && !(Boolean)this.renderingUp.getValue() ? 14 : 0;
         String str;
         String var10000;
         if ((Boolean)this.arrayList.getValue()) {
            Iterator var6 = Managers.MODULE.getEnabledModules().stream().filter(Module::isDrawn).sorted(Comparator.comparing((modulex) -> {
               return this.getStringWidth(modulex.getFullArrayString()) * -1;
            })).toList().iterator();

            while(var6.hasNext()) {
               Module module = (Module)var6.next();
               if (module.isDrawn()) {
                  var10000 = module.getDisplayName();
                  str = var10000 + String.valueOf(class_124.field_1080) + (module.getDisplayInfo() != null ? " [" + String.valueOf(class_124.field_1068) + module.getDisplayInfo() + String.valueOf(class_124.field_1080) + "]" : "");
                  if ((Boolean)this.renderingUp.getValue()) {
                     this.drawText(context, str, width - 2 - this.getStringWidth(str), 2 + j * offset);
                     ++j;
                  } else {
                     j += offset;
                     this.drawText(context, str, width - 2 - this.getStringWidth(str), height - j);
                  }
               }
            }
         }

         int i = mc.field_1755 instanceof class_408 && (Boolean)this.renderingUp.getValue() ? 13 : ((Boolean)this.renderingUp.getValue() ? -2 : 0);
         if ((Boolean)this.potions.getValue()) {
            List<class_1293> effects = new ArrayList(mc.field_1724.method_6026());
            Iterator var19 = effects.iterator();

            while(var19.hasNext()) {
               class_1293 potionEffect = (class_1293)var19.next();
               class_1291 potion = (class_1291)potionEffect.method_5579().comp_349();
               String power = "";
               switch(potionEffect.method_5578()) {
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

               var10000 = potion.method_5560().getString();
               String s = var10000 + " " + power;
               String s2 = PotionHud.getDuration(potionEffect).makeConcatWithConstants<invokedynamic>(PotionHud.getDuration(potionEffect));
               Color c = new Color(((class_1291)potionEffect.method_5579().comp_349()).method_5556());
               if ((Boolean)this.renderingUp.getValue()) {
                  i += offset;
                  this.drawText(context, s + " " + s2, width - this.getStringWidth(s + " " + s2) - 2, height - 2 - i, c.getRGB());
               } else {
                  this.drawText(context, s + " " + s2, width - this.getStringWidth(s + " " + s2) - 2, 2 + i++ * offset, c.getRGB());
               }
            }
         }

         String fpsText;
         if ((Boolean)this.worldTime.getValue()) {
            var10000 = String.valueOf(class_124.field_1068);
            fpsText = "WorldTime: " + var10000 + mc.field_1687.method_8532() % 24000L;
            this.drawText(context, fpsText, width - this.getStringWidth(fpsText) - 2, (Boolean)this.renderingUp.getValue() ? height - 2 - (i += offset) : 2 + i++ * offset);
         }

         if ((Boolean)this.mainhandDurability.getValue()) {
            var10000 = String.valueOf(class_124.field_1068);
            fpsText = "MainHand" + var10000 + " [" + (mc.field_1724.method_6047().method_7936() - mc.field_1724.method_6047().method_7919()) + "]";
            this.drawText(context, fpsText, width - this.getStringWidth(fpsText) - 2, (Boolean)this.renderingUp.getValue() ? height - 2 - (i += offset) : 2 + i++ * offset);
         }

         if ((Boolean)this.tps.getValue()) {
            var10000 = String.valueOf(class_124.field_1068);
            fpsText = "TPS " + var10000 + Managers.SERVER.getTPS() + ((Boolean)this.extraTps.getValue() ? " [" + Managers.SERVER.getTPS2() + "]" : "");
            this.drawText(context, fpsText, width - this.getStringWidth(fpsText) - 2, (Boolean)this.renderingUp.getValue() ? height - 2 - (i += offset) : 2 + i++ * offset);
         }

         if ((Boolean)this.speed.getValue()) {
            var10000 = String.valueOf(class_124.field_1068);
            fpsText = "Speed " + var10000 + MathUtility.round(Managers.PLAYER.currentPlayerSpeed * ((Boolean)this.bps.getValue() ? 20.0F : 72.0F) * TsunamiClient.TICK_TIMER) + ((Boolean)this.bps.getValue() ? " b/s" : " km/h");
            this.drawText(context, fpsText, width - this.getStringWidth(fpsText) - 2, (Boolean)this.renderingUp.getValue() ? height - 2 - (i += offset) : 2 + i++ * offset);
         }

         if ((Boolean)this.chests.getValue()) {
            class_3545<Integer, Integer> chests = ModuleManager.chestCounter.getChestCount();
            var10000 = String.valueOf(class_124.field_1068);
            str = "Chests: " + var10000 + "S:" + String.valueOf(chests.method_15442()) + " D:" + String.valueOf(chests.method_15441());
            this.drawText(context, str, width - this.getStringWidth(str) - 2, (Boolean)this.renderingUp.getValue() ? height - 2 - (i += offset) : 2 + i++ * offset);
         }

         if ((Boolean)this.biome.getValue()) {
            var10000 = String.valueOf(class_124.field_1068);
            fpsText = "Biome: " + var10000 + biome();
            this.drawText(context, fpsText, width - this.getStringWidth(fpsText) - 2, (Boolean)this.renderingUp.getValue() ? height - 2 - (i += offset) : 2 + i++ * offset);
         }

         if ((Boolean)this.time.getValue()) {
            var10000 = String.valueOf(class_124.field_1068);
            fpsText = "Time " + var10000 + (new SimpleDateFormat("h:mm a")).format(new Date());
            this.drawText(context, fpsText, width - this.getStringWidth(fpsText) - 2, (Boolean)this.renderingUp.getValue() ? height - 2 - (i += offset) : 2 + i++ * offset);
         }

         if ((Boolean)this.offhandDurability.getValue()) {
            var10000 = String.valueOf(class_124.field_1068);
            fpsText = "OffHand" + var10000 + " [" + (mc.field_1724.method_6079().method_7936() - mc.field_1724.method_6079().method_7919()) + "]";
            this.drawText(context, fpsText, width - this.getStringWidth(fpsText) - 2, (Boolean)this.renderingUp.getValue() ? height - 2 - (i += offset) : 2 + i++ * offset);
         }

         if ((Boolean)this.ping.getValue()) {
            var10000 = String.valueOf(class_124.field_1068);
            fpsText = "Ping " + var10000 + Managers.SERVER.getPing();
            this.drawText(context, fpsText, width - this.getStringWidth(fpsText) - 2, (Boolean)this.renderingUp.getValue() ? height - 2 - (i += offset) : 2 + i++ * offset);
         }

         if ((Boolean)this.fps.getValue()) {
            var10000 = String.valueOf(class_124.field_1068);
            fpsText = "FPS " + var10000 + FrameRateCounter.INSTANCE.getFps();
            this.drawText(context, fpsText, width - this.getStringWidth(fpsText) - 2, (Boolean)this.renderingUp.getValue() ? height - 2 - (i + offset) : 2 + i++ * offset);
         }

         boolean inHell = Objects.equals(mc.field_1687.method_27983().method_29177().method_12832(), "the_nether");
         int posX = (int)mc.field_1724.method_23317();
         int posY = (int)mc.field_1724.method_23318();
         int posZ = (int)mc.field_1724.method_23321();
         float nether = !inHell ? 0.125F : 8.0F;
         int hposX = (int)(mc.field_1724.method_23317() * (double)nether);
         int hposZ = (int)(mc.field_1724.method_23321() * (double)nether);
         i = mc.field_1755 instanceof class_408 ? 14 : 0;
         var10000 = String.valueOf(class_124.field_1068);
         String coordinates = var10000 + "XYZ " + String.valueOf(class_124.field_1070) + (inHell ? posX + ", " + posY + ", " + posZ + String.valueOf(class_124.field_1068) + " [" + String.valueOf(class_124.field_1070) + hposX + ", " + hposZ + String.valueOf(class_124.field_1068) + "]" + String.valueOf(class_124.field_1070) : posX + ", " + posY + ", " + posZ + String.valueOf(class_124.field_1068) + " [" + String.valueOf(class_124.field_1070) + hposX + ", " + hposZ + String.valueOf(class_124.field_1068) + "]");
         String direction1 = "";
         i += offset;
         if ((Boolean)this.direction.getValue()) {
            switch(mc.field_1724.method_5735()) {
            case field_11034:
               direction1 = "East" + String.valueOf(class_124.field_1068) + " [+X]";
               break;
            case field_11039:
               direction1 = "West" + String.valueOf(class_124.field_1068) + " [-X]";
               break;
            case field_11043:
               direction1 = "North" + String.valueOf(class_124.field_1068) + " [-Z]";
               break;
            case field_11035:
               direction1 = "South" + String.valueOf(class_124.field_1068) + " [+Z]";
            }

            this.drawText(context, direction1, 2, height - i - 11);
         }

         if ((Boolean)this.coords.getValue()) {
            this.drawText(context, coordinates, 2, height - i);
         }

         if ((Boolean)this.armor.getValue()) {
            this.renderArmorHUD(true, context);
         }

         if ((Boolean)this.totems.getValue()) {
            this.renderTotemHUD(context);
         }

         if ((Boolean)this.greeter.getValue()) {
            this.renderGreeter(context);
         }

      }
   }

   private void drawText(class_332 context, String str, int x, int y, int color) {
      if (!((LegacyHud.Font)this.customFont.getValue()).equals(LegacyHud.Font.Minecraft)) {
         FontRenderer adapter;
         switch(((LegacyHud.Font)this.customFont.getValue()).ordinal()) {
         case 2:
            adapter = FontRenderers.monsterrat;
            break;
         case 3:
            adapter = FontRenderers.sf_medium;
            break;
         default:
            adapter = FontRenderers.modules;
         }

         adapter.drawString(context.method_51448(), str.replace(String.valueOf(class_124.field_1068).makeConcatWithConstants<invokedynamic>(String.valueOf(class_124.field_1068)), ""), (double)x + 0.5D, (double)y + 0.5D, Color.BLACK.getRGB());
         adapter.drawString(context.method_51448(), str, (double)x, (double)y, color);
      } else {
         context.method_51433(mc.field_1772, str, x, y, color, true);
      }
   }

   private void drawText(class_332 context, String str, int x, int y) {
      if (!((LegacyHud.Font)this.customFont.getValue()).equals(LegacyHud.Font.Minecraft)) {
         FontRenderer adapter;
         switch(((LegacyHud.Font)this.customFont.getValue()).ordinal()) {
         case 2:
            adapter = FontRenderers.monsterrat;
            break;
         case 3:
            adapter = FontRenderers.sf_medium;
            break;
         default:
            adapter = FontRenderers.modules;
         }

         adapter.drawString(context.method_51448(), str.replace(String.valueOf(class_124.field_1068).makeConcatWithConstants<invokedynamic>(String.valueOf(class_124.field_1068)), ""), (double)x + 0.5D, (double)y + 0.5D, Color.BLACK.getRGB());
         adapter.drawString(context.method_51448(), str, (double)x, (double)y, this.color);
      } else {
         context.method_51433(mc.field_1772, str, x, y, this.color, true);
      }
   }

   private int getStringWidth(String str) {
      switch(((LegacyHud.Font)this.customFont.getValue()).ordinal()) {
      case 0:
         return mc.field_1772.method_1727(str);
      case 1:
      default:
         return (int)FontRenderers.modules.getStringWidth(str);
      case 2:
         return (int)FontRenderers.monsterrat.getStringWidth(str);
      case 3:
         return (int)FontRenderers.sf_medium.getStringWidth(str);
      }
   }

   public void renderGreeter(class_332 context) {
      String var10000 = getTimeOfDay();
      String text = "Good " + var10000 + mc.field_1724.method_5477().getString();
      this.drawText(context, text, (int)((float)mc.method_22683().method_4486() / 2.0F - (float)this.getStringWidth(text) / 2.0F + 2.0F), 2);
   }

   public static String getTimeOfDay() {
      int timeOfDay = Calendar.getInstance().get(11);
      if (timeOfDay < 12) {
         return "Morning ";
      } else if (timeOfDay < 16) {
         return "Afternoon ";
      } else {
         return timeOfDay < 21 ? "Evening " : "Night ";
      }
   }

   public void renderTotemHUD(class_332 context) {
      int width = mc.method_22683().method_4486();
      int height = mc.method_22683().method_4502();
      int totems = mc.field_1724.method_31548().field_7547.stream().filter((itemStack) -> {
         return itemStack.method_7909() == class_1802.field_8288;
      }).mapToInt(class_1799::method_7947).sum();
      int u = mc.field_1724.method_5748();
      int v = Math.min(mc.field_1724.method_5669(), u);
      if (mc.field_1724.method_6079().method_7909() == class_1802.field_8288) {
         totems += mc.field_1724.method_6079().method_7947();
      }

      if (totems > 0) {
         int i = width / 2;
         int y = height - 55 - (!mc.field_1724.method_5869() && v >= u ? 0 : 10);
         int x = i - 189 + 180 + 2;
         context.method_51427(totem, x, y);
         context.method_51431(mc.field_1772, totem, x, y);
         this.drawText(context, totems.makeConcatWithConstants<invokedynamic>(totems), 8 + (int)((float)x - (float)this.getStringWidth(totems.makeConcatWithConstants<invokedynamic>(totems)) / 2.0F), y - 7, 16777215);
      }

   }

   private static String biome() {
      if (mc.field_1724 != null && mc.field_1687 != null) {
         class_2960 id = mc.field_1687.method_30349().method_30530(class_7924.field_41236).method_10221((class_1959)mc.field_1687.method_23753(mc.field_1724.method_24515()).comp_349());
         return id == null ? "Unknown" : (String)Arrays.stream(id.method_12832().split("_")).map(StringUtils::capitalize).collect(Collectors.joining(" "));
      } else {
         return null;
      }
   }

   public void renderArmorHUD(boolean percent, class_332 context) {
      int i = 0;
      int u = mc.field_1724.method_5748();
      int v = Math.min(mc.field_1724.method_5669(), u);
      int y = mc.method_22683().method_4502() - 55 - (!mc.field_1724.method_5869() && v >= u ? 0 : 10);
      Iterator var7 = mc.field_1724.method_31548().field_7548.iterator();

      while(var7.hasNext()) {
         class_1799 is = (class_1799)var7.next();
         ++i;
         if (!is.method_7960()) {
            int x = mc.method_22683().method_4486() / 2 - 90 + (9 - i) * 20 + 2;
            context.method_51427(is, x, y);
            context.method_51431(mc.field_1772, is, x, y);
            String s = is.method_7947() > 1 ? is.method_7947().makeConcatWithConstants<invokedynamic>(is.method_7947()) : "";
            this.drawText(context, s, x + 19 - 2 - this.getStringWidth(s), y + 9, 16777215);
            if (percent) {
               float green = (float)(is.method_7936() - is.method_7919()) / (float)is.method_7936();
               float red = 1.0F - green;
               int dmg = 100 - (int)(red * 100.0F);
               this.drawText(context, dmg.makeConcatWithConstants<invokedynamic>(dmg), x + 8 - this.getStringWidth(dmg.makeConcatWithConstants<invokedynamic>(dmg)) / 2, y - 11, (new Color((int)MathUtility.clamp(red * 255.0F, 0.0F, 255.0F), (int)MathUtility.clamp(green * 255.0F, 0.0F, 255.0F), 0)).getRGB());
            }
         }
      }

   }

   static {
      totem = new class_1799(class_1802.field_8288);
   }

   private static enum Font {
      Minecraft,
      Comfortaa,
      Monsterrat,
      SF;

      // $FF: synthetic method
      private static LegacyHud.Font[] $values() {
         return new LegacyHud.Font[]{Minecraft, Comfortaa, Monsterrat, SF};
      }
   }
}
