package tsunami.features.modules.render;

import java.awt.Color;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.class_1297;
import net.minecraft.class_1299;
import net.minecraft.class_1309;
import net.minecraft.class_1538;
import net.minecraft.class_1657;
import net.minecraft.class_2398;
import net.minecraft.class_3419;
import net.minecraft.class_4587;
import tsunami.core.Managers;
import tsunami.features.modules.Module;
import tsunami.setting.Setting;
import tsunami.setting.impl.ColorSetting;
import tsunami.utility.render.Render3DEngine;

public class KillEffect extends Module {
   private final Setting<KillEffect.Mode> mode;
   private final Setting<Integer> speed;
   public final Setting<Integer> volume;
   private final Setting<Boolean> playSound;
   private final Setting<ColorSetting> color;
   private final Setting<Boolean> mobs;
   private final Map<class_1297, Long> renderEntities;
   private final Map<class_1297, Long> lightingEntities;

   public KillEffect() {
      super("KillEffect", Module.Category.RENDER);
      this.mode = new Setting("Mode", KillEffect.Mode.Orthodox);
      this.speed = new Setting("Y Speed", 0, -10, 10, (value) -> {
         return this.mode.getValue() == KillEffect.Mode.Orthodox;
      });
      this.volume = new Setting("Volume", 100, 0, 100);
      this.playSound = new Setting("Play Sound", true, (value) -> {
         return this.mode.getValue() == KillEffect.Mode.Orthodox;
      });
      this.color = new Setting("Color", new ColorSetting(new Color(255, 255, 0, 150)), (value) -> {
         return this.mode.getValue() == KillEffect.Mode.Orthodox;
      });
      this.mobs = new Setting("Mobs", false);
      this.renderEntities = new ConcurrentHashMap();
      this.lightingEntities = new ConcurrentHashMap();
   }

   public void onRender3D(class_4587 stack) {
      if (mc.field_1687 != null) {
         switch(((KillEffect.Mode)this.mode.getValue()).ordinal()) {
         case 0:
            this.renderEntities.forEach((entity, time) -> {
               if (System.currentTimeMillis() - time > 3000L) {
                  this.renderEntities.remove(entity);
               } else {
                  Render3DEngine.drawLine(entity.method_19538().method_1031(0.0D, this.calculateSpeed(), 0.0D), entity.method_19538().method_1031(0.0D, 3.0D + this.calculateSpeed(), 0.0D), ((ColorSetting)this.color.getValue()).getColorObject());
                  Render3DEngine.drawLine(entity.method_19538().method_1031(1.0D, 2.3D + this.calculateSpeed(), 0.0D), entity.method_19538().method_1031(-1.0D, 2.3D + this.calculateSpeed(), 0.0D), ((ColorSetting)this.color.getValue()).getColorObject());
                  Render3DEngine.drawLine(entity.method_19538().method_1031(0.5D, 1.2D + this.calculateSpeed(), 0.0D), entity.method_19538().method_1031(-0.5D, 0.8D + this.calculateSpeed(), 0.0D), ((ColorSetting)this.color.getValue()).getColorObject());
               }

            });
            break;
         case 1:
            this.renderEntities.keySet().forEach((entity) -> {
               for(int i = 0; (float)i < entity.method_17682() * 10.0F; ++i) {
                  for(int j = 0; (float)j < entity.method_17681() * 10.0F; ++j) {
                     for(int k = 0; (float)k < entity.method_17681() * 10.0F; ++k) {
                        mc.field_1687.method_8406(class_2398.field_18304, entity.method_23317() + (double)j * 0.1D, entity.method_23318() + (double)i * 0.1D, entity.method_23321() + (double)k * 0.1D, 0.0D, 0.0D, 0.0D);
                     }
                  }
               }

               this.renderEntities.remove(entity);
            });
            break;
         case 2:
            this.renderEntities.forEach((entity, time) -> {
               class_1538 lightningEntity = new class_1538(class_1299.field_6112, mc.field_1687);
               lightningEntity.method_24203(entity.method_23317(), entity.method_23318(), entity.method_23321());
               mc.field_1687.method_53875(lightningEntity);
               this.renderEntities.remove(entity);
               this.lightingEntities.put(entity, System.currentTimeMillis());
            });
         }

      }
   }

   public void onUpdate() {
      Managers.ASYNC.getAsyncEntities().forEach((entity) -> {
         if (entity instanceof class_1657 || (Boolean)this.mobs.getValue()) {
            if (entity instanceof class_1309) {
               class_1309 liv = (class_1309)entity;
               if (entity != mc.field_1724 && !this.renderEntities.containsKey(entity) && !this.lightingEntities.containsKey(entity)) {
                  if (!entity.method_5805() && liv.method_6032() == 0.0F) {
                     if ((Boolean)this.playSound.getValue() && this.mode.getValue() == KillEffect.Mode.Orthodox) {
                        mc.field_1687.method_8396(mc.field_1724, entity.method_24515(), Managers.SOUND.ORTHODOX_SOUNDEVENT, class_3419.field_15245, (float)(Integer)this.volume.getValue() / 100.0F, 1.0F);
                     }

                     this.renderEntities.put(entity, System.currentTimeMillis());
                  }
               }
            }
         }
      });
      if (!this.lightingEntities.isEmpty()) {
         this.lightingEntities.forEach((entity, time) -> {
            if (System.currentTimeMillis() - time > 5000L) {
               this.lightingEntities.remove(entity);
            }

         });
      }

   }

   private double calculateSpeed() {
      return (double)(Integer)this.speed.getValue() / 100.0D;
   }

   private static enum Mode {
      Orthodox,
      FallingLava,
      LightningBolt;

      // $FF: synthetic method
      private static KillEffect.Mode[] $values() {
         return new KillEffect.Mode[]{Orthodox, FallingLava, LightningBolt};
      }
   }
}
