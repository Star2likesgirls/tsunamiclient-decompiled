package tsunami.features.modules.render;

import net.minecraft.class_1297;
import net.minecraft.class_1511;
import net.minecraft.class_1657;
import net.minecraft.class_4587;
import tsunami.core.Managers;
import tsunami.core.manager.client.ShaderManager;
import tsunami.features.modules.Module;
import tsunami.injection.accesors.IGameRenderer;
import tsunami.setting.Setting;
import tsunami.setting.impl.ColorSetting;
import tsunami.setting.impl.SettingGroup;
import tsunami.utility.render.Render3DEngine;

public class Shaders extends Module {
   private final Setting<SettingGroup> select = new Setting("Select", new SettingGroup(false, 0));
   private final Setting<Boolean> hands;
   private final Setting<Boolean> players;
   private final Setting<Boolean> self;
   private final Setting<Boolean> friends;
   private final Setting<Boolean> crystals;
   private final Setting<Boolean> creatures;
   private final Setting<Boolean> monsters;
   private final Setting<Boolean> ambients;
   private final Setting<Boolean> others;
   public Setting<ShaderManager.Shader> mode;
   public Setting<ShaderManager.Shader> handsMode;
   public final Setting<Integer> maxRange;
   public final Setting<Float> factor;
   public final Setting<Float> gradient;
   public final Setting<Integer> alpha2;
   public final Setting<Integer> lineWidth;
   public final Setting<Integer> quality;
   public final Setting<Integer> octaves;
   public final Setting<Integer> fillAlpha;
   public final Setting<Boolean> glow;
   private final Setting<SettingGroup> colors;
   public final Setting<ColorSetting> outlineColor;
   public final Setting<ColorSetting> outlineColor1;
   public final Setting<ColorSetting> outlineColor2;
   public final Setting<ColorSetting> fillColor1;
   public final Setting<ColorSetting> fillColor2;
   public final Setting<ColorSetting> fillColor3;

   public Shaders() {
      super("Shaders", Module.Category.NONE);
      this.hands = (new Setting("Hands", true)).addToGroup(this.select);
      this.players = (new Setting("Players", true)).addToGroup(this.select);
      this.self = (new Setting("Self", true, (v) -> {
         return (Boolean)this.players.getValue();
      })).addToGroup(this.select);
      this.friends = (new Setting("Friends", true)).addToGroup(this.select);
      this.crystals = (new Setting("Crystals", true)).addToGroup(this.select);
      this.creatures = (new Setting("Creatures", false)).addToGroup(this.select);
      this.monsters = (new Setting("Monsters", false)).addToGroup(this.select);
      this.ambients = (new Setting("Ambients", false)).addToGroup(this.select);
      this.others = (new Setting("Others", false)).addToGroup(this.select);
      this.mode = new Setting("Mode", ShaderManager.Shader.Default);
      this.handsMode = new Setting("HandsMode", ShaderManager.Shader.Default);
      this.maxRange = new Setting("MaxRange", 64, 16, 256, (v) -> {
         return (Boolean)this.players.getValue() || (Boolean)this.crystals.getValue() || (Boolean)this.friends.getValue() || (Boolean)this.creatures.getValue() || (Boolean)this.monsters.getValue() || (Boolean)this.ambients.getValue() || (Boolean)this.others.getValue();
      });
      this.factor = new Setting("GradientFactor", 2.0F, 0.0F, 20.0F, (v) -> {
         return this.mode.is(ShaderManager.Shader.Gradient) || this.handsMode.is(ShaderManager.Shader.Gradient);
      });
      this.gradient = new Setting("Gradient", 2.0F, 0.0F, 20.0F, (v) -> {
         return this.mode.is(ShaderManager.Shader.Gradient) || this.handsMode.is(ShaderManager.Shader.Gradient);
      });
      this.alpha2 = new Setting("GradientAlpha", 170, 0, 255, (v) -> {
         return this.mode.is(ShaderManager.Shader.Gradient) || this.handsMode.is(ShaderManager.Shader.Gradient);
      });
      this.lineWidth = new Setting("LineWidth", 2, 0, 500);
      this.quality = new Setting("Quality", 3, 0, 6);
      this.octaves = new Setting("SmokeOctaves", 10, 5, 30);
      this.fillAlpha = new Setting("FillAlpha", 170, 0, 255);
      this.glow = new Setting("SmokeGlow", true);
      this.colors = new Setting("Colors", new SettingGroup(false, 0));
      this.outlineColor = (new Setting("Outline", new ColorSetting(-2013200640))).addToGroup(this.colors);
      this.outlineColor1 = (new Setting("SmokeOutline", new ColorSetting(-2013200640), (v) -> {
         return this.mode.is(ShaderManager.Shader.Smoke) || this.handsMode.is(ShaderManager.Shader.Smoke);
      })).addToGroup(this.colors);
      this.outlineColor2 = (new Setting("SmokeOutline2", new ColorSetting(-2013200640), (v) -> {
         return this.mode.is(ShaderManager.Shader.Smoke) || this.handsMode.is(ShaderManager.Shader.Smoke);
      })).addToGroup(this.colors);
      this.fillColor1 = (new Setting("Fill", new ColorSetting(-2013200640))).addToGroup(this.colors);
      this.fillColor2 = (new Setting("SmokeFill", new ColorSetting(-2013200640))).addToGroup(this.colors);
      this.fillColor3 = (new Setting("SmokeFil2", new ColorSetting(-2013200640))).addToGroup(this.colors);
   }

   public boolean shouldRender(class_1297 entity) {
      if (entity == null) {
         return false;
      } else if (mc.field_1724 == null) {
         return false;
      } else if (mc.field_1724.method_5707(entity.method_19538()) > (double)this.maxRange.getPow2Value()) {
         return false;
      } else if (entity instanceof class_1657) {
         if (entity == mc.field_1724 && !(Boolean)this.self.getValue()) {
            return false;
         } else {
            return Managers.FRIEND.isFriend((class_1657)entity) ? (Boolean)this.friends.getValue() : (Boolean)this.players.getValue();
         }
      } else if (entity instanceof class_1511) {
         return (Boolean)this.crystals.getValue();
      } else {
         boolean var10000;
         switch(entity.method_5864().method_5891()) {
         case field_6294:
         case field_6300:
            var10000 = (Boolean)this.creatures.getValue();
            break;
         case field_6302:
            var10000 = (Boolean)this.monsters.getValue();
            break;
         case field_6303:
         case field_24460:
            var10000 = (Boolean)this.ambients.getValue();
            break;
         default:
            var10000 = (Boolean)this.others.getValue();
         }

         return var10000;
      }
   }

   public void onRender3D(class_4587 matrices) {
      if ((Boolean)this.hands.getValue()) {
         Managers.SHADER.renderShader(() -> {
            ((IGameRenderer)mc.field_1773).irenderHand(mc.field_1773.method_19418(), Render3DEngine.getTickDelta(), matrices.method_23760().method_23761());
         }, (ShaderManager.Shader)this.handsMode.getValue());
      }

   }

   public void onDisable() {
      Managers.SHADER.reloadShaders();
   }
}
