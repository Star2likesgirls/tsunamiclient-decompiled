package tsunami.core.manager.client;

import java.io.File;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.FloatControl.Type;
import net.minecraft.class_2378;
import net.minecraft.class_2960;
import net.minecraft.class_3414;
import net.minecraft.class_3419;
import net.minecraft.class_7923;
import tsunami.core.Managers;
import tsunami.core.manager.IManager;
import tsunami.features.cmd.Command;
import tsunami.features.modules.client.ClientSettings;
import tsunami.features.modules.client.SoundFX;
import tsunami.features.modules.misc.ChatUtils;
import tsunami.utility.Timer;
import tsunami.utility.math.MathUtility;

public class SoundManager implements IManager {
   public final class_2960 KEYPRESS_SOUND = class_2960.method_60654("thunderhack:keypress");
   public class_3414 KEYPRESS_SOUNDEVENT;
   public final class_2960 KEYRELEASE_SOUND;
   public class_3414 KEYRELEASE_SOUNDEVENT;
   public final class_2960 UWU_SOUND;
   public class_3414 UWU_SOUNDEVENT;
   public final class_2960 ENABLE_SOUND;
   public class_3414 ENABLE_SOUNDEVENT;
   public final class_2960 DISABLE_SOUND;
   public class_3414 DISABLE_SOUNDEVENT;
   public final class_2960 MOAN1_SOUND;
   public class_3414 MOAN1_SOUNDEVENT;
   public final class_2960 MOAN2_SOUND;
   public class_3414 MOAN2_SOUNDEVENT;
   public final class_2960 MOAN3_SOUND;
   public class_3414 MOAN3_SOUNDEVENT;
   public final class_2960 MOAN4_SOUND;
   public class_3414 MOAN4_SOUNDEVENT;
   public final class_2960 SKEET_SOUND;
   public class_3414 SKEET_SOUNDEVENT;
   public final class_2960 ORTHODOX_SOUND;
   public class_3414 ORTHODOX_SOUNDEVENT;
   public final class_2960 BOOLEAN_SOUND;
   public class_3414 BOOLEAN_SOUNDEVENT;
   public final class_2960 SCROLL_SOUND;
   public class_3414 SCROLL_SOUNDEVENT;
   public final class_2960 SWIPEIN_SOUND;
   public class_3414 SWIPEIN_SOUNDEVENT;
   public final class_2960 SWIPEOUT_SOUND;
   public class_3414 SWIPEOUT_SOUNDEVENT;
   public final class_2960 ALERT_SOUND;
   public class_3414 ALERT_SOUNDEVENT;
   public final class_2960 PM_SOUND;
   public class_3414 PM_SOUNDEVENT;
   public final class_2960 RIFK_SOUND;
   public class_3414 RIFK_SOUNDEVENT;
   public final class_2960 CUTIE_SOUND;
   public class_3414 CUTIE_SOUNDEVENT;
   private final Timer scrollTimer;

   public SoundManager() {
      this.KEYPRESS_SOUNDEVENT = class_3414.method_47908(this.KEYPRESS_SOUND);
      this.KEYRELEASE_SOUND = class_2960.method_60654("thunderhack:keyrelease");
      this.KEYRELEASE_SOUNDEVENT = class_3414.method_47908(this.KEYRELEASE_SOUND);
      this.UWU_SOUND = class_2960.method_60654("thunderhack:uwu");
      this.UWU_SOUNDEVENT = class_3414.method_47908(this.UWU_SOUND);
      this.ENABLE_SOUND = class_2960.method_60654("thunderhack:enable");
      this.ENABLE_SOUNDEVENT = class_3414.method_47908(this.ENABLE_SOUND);
      this.DISABLE_SOUND = class_2960.method_60654("thunderhack:disable");
      this.DISABLE_SOUNDEVENT = class_3414.method_47908(this.DISABLE_SOUND);
      this.MOAN1_SOUND = class_2960.method_60654("thunderhack:moan1");
      this.MOAN1_SOUNDEVENT = class_3414.method_47908(this.MOAN1_SOUND);
      this.MOAN2_SOUND = class_2960.method_60654("thunderhack:moan2");
      this.MOAN2_SOUNDEVENT = class_3414.method_47908(this.MOAN2_SOUND);
      this.MOAN3_SOUND = class_2960.method_60654("thunderhack:moan3");
      this.MOAN3_SOUNDEVENT = class_3414.method_47908(this.MOAN3_SOUND);
      this.MOAN4_SOUND = class_2960.method_60654("thunderhack:moan4");
      this.MOAN4_SOUNDEVENT = class_3414.method_47908(this.MOAN4_SOUND);
      this.SKEET_SOUND = class_2960.method_60654("thunderhack:skeet");
      this.SKEET_SOUNDEVENT = class_3414.method_47908(this.SKEET_SOUND);
      this.ORTHODOX_SOUND = class_2960.method_60654("thunderhack:orthodox");
      this.ORTHODOX_SOUNDEVENT = class_3414.method_47908(this.ORTHODOX_SOUND);
      this.BOOLEAN_SOUND = class_2960.method_60654("thunderhack:boolean");
      this.BOOLEAN_SOUNDEVENT = class_3414.method_47908(this.BOOLEAN_SOUND);
      this.SCROLL_SOUND = class_2960.method_60654("thunderhack:scroll");
      this.SCROLL_SOUNDEVENT = class_3414.method_47908(this.SCROLL_SOUND);
      this.SWIPEIN_SOUND = class_2960.method_60654("thunderhack:swipein");
      this.SWIPEIN_SOUNDEVENT = class_3414.method_47908(this.SWIPEIN_SOUND);
      this.SWIPEOUT_SOUND = class_2960.method_60654("thunderhack:swipeout");
      this.SWIPEOUT_SOUNDEVENT = class_3414.method_47908(this.SWIPEOUT_SOUND);
      this.ALERT_SOUND = class_2960.method_60654("thunderhack:alert");
      this.ALERT_SOUNDEVENT = class_3414.method_47908(this.ALERT_SOUND);
      this.PM_SOUND = class_2960.method_60654("thunderhack:pmsound");
      this.PM_SOUNDEVENT = class_3414.method_47908(this.PM_SOUND);
      this.RIFK_SOUND = class_2960.method_60654("thunderhack:rifk");
      this.RIFK_SOUNDEVENT = class_3414.method_47908(this.RIFK_SOUND);
      this.CUTIE_SOUND = class_2960.method_60654("thunderhack:cutie");
      this.CUTIE_SOUNDEVENT = class_3414.method_47908(this.CUTIE_SOUND);
      this.scrollTimer = new Timer();
   }

   public void registerSounds() {
      class_2378.method_10230(class_7923.field_41172, this.KEYPRESS_SOUND, this.KEYPRESS_SOUNDEVENT);
      class_2378.method_10230(class_7923.field_41172, this.KEYRELEASE_SOUND, this.KEYRELEASE_SOUNDEVENT);
      class_2378.method_10230(class_7923.field_41172, this.ENABLE_SOUND, this.ENABLE_SOUNDEVENT);
      class_2378.method_10230(class_7923.field_41172, this.DISABLE_SOUND, this.DISABLE_SOUNDEVENT);
      class_2378.method_10230(class_7923.field_41172, this.MOAN1_SOUND, this.MOAN1_SOUNDEVENT);
      class_2378.method_10230(class_7923.field_41172, this.MOAN2_SOUND, this.MOAN2_SOUNDEVENT);
      class_2378.method_10230(class_7923.field_41172, this.MOAN3_SOUND, this.MOAN3_SOUNDEVENT);
      class_2378.method_10230(class_7923.field_41172, this.MOAN4_SOUND, this.MOAN4_SOUNDEVENT);
      class_2378.method_10230(class_7923.field_41172, this.UWU_SOUND, this.UWU_SOUNDEVENT);
      class_2378.method_10230(class_7923.field_41172, this.SKEET_SOUND, this.SKEET_SOUNDEVENT);
      class_2378.method_10230(class_7923.field_41172, this.ORTHODOX_SOUND, this.ORTHODOX_SOUNDEVENT);
      class_2378.method_10230(class_7923.field_41172, this.SCROLL_SOUND, this.SCROLL_SOUNDEVENT);
      class_2378.method_10230(class_7923.field_41172, this.BOOLEAN_SOUND, this.BOOLEAN_SOUNDEVENT);
      class_2378.method_10230(class_7923.field_41172, this.SWIPEIN_SOUND, this.SWIPEIN_SOUNDEVENT);
      class_2378.method_10230(class_7923.field_41172, this.SWIPEOUT_SOUND, this.SWIPEOUT_SOUNDEVENT);
      class_2378.method_10230(class_7923.field_41172, this.ALERT_SOUND, this.ALERT_SOUNDEVENT);
      class_2378.method_10230(class_7923.field_41172, this.PM_SOUND, this.PM_SOUNDEVENT);
      class_2378.method_10230(class_7923.field_41172, this.RIFK_SOUND, this.RIFK_SOUNDEVENT);
      class_2378.method_10230(class_7923.field_41172, this.CUTIE_SOUND, this.CUTIE_SOUNDEVENT);
   }

   public void playHitSound(SoundFX.HitSound value) {
      switch(value) {
      case UWU:
         this.playSound(this.UWU_SOUNDEVENT);
         break;
      case SKEET:
         this.playSound(this.SKEET_SOUNDEVENT);
         break;
      case KEYBOARD:
         this.playSound(this.KEYPRESS_SOUNDEVENT);
         break;
      case CUTIE:
         this.playSound(this.CUTIE_SOUNDEVENT);
         break;
      case MOAN:
         class_3414 var10000;
         switch((int)MathUtility.random(0.0F, 3.0F)) {
         case 0:
            var10000 = this.MOAN1_SOUNDEVENT;
            break;
         case 1:
            var10000 = this.MOAN2_SOUNDEVENT;
            break;
         case 2:
            var10000 = this.MOAN3_SOUNDEVENT;
            break;
         default:
            var10000 = this.MOAN4_SOUNDEVENT;
         }

         class_3414 sound = var10000;
         this.playSound(sound);
         break;
      case RIFK:
         this.playSound(this.RIFK_SOUNDEVENT);
         break;
      case CUSTOM:
         this.playSound("hit");
      }

   }

   public void playEnable() {
      if (ModuleManager.soundFX.enableMode.getValue() == SoundFX.OnOffSound.Inertia) {
         this.playSound(this.ENABLE_SOUNDEVENT);
      } else if (ModuleManager.soundFX.enableMode.getValue() == SoundFX.OnOffSound.Custom) {
         this.playSound("enable");
      }

   }

   public void playDisable() {
      if (ModuleManager.soundFX.disableMode.getValue() == SoundFX.OnOffSound.Inertia) {
         this.playSound(this.DISABLE_SOUNDEVENT);
      } else if (ModuleManager.soundFX.disableMode.getValue() == SoundFX.OnOffSound.Custom) {
         this.playSound("disable");
      }

   }

   public void playScroll() {
      if (this.scrollTimer.every(50L)) {
         if (ModuleManager.soundFX.scrollSound.getValue() == SoundFX.ScrollSound.KeyBoard) {
            this.playSound(this.KEYPRESS_SOUNDEVENT);
         } else if (ModuleManager.soundFX.scrollSound.getValue() == SoundFX.ScrollSound.Custom) {
            this.playSound("scroll");
         }
      }

   }

   public void playSound(class_3414 sound) {
      if (mc.field_1724 != null && mc.field_1687 != null) {
         mc.field_1687.method_8396(mc.field_1724, mc.field_1724.method_24515(), sound, class_3419.field_15245, (float)(Integer)ModuleManager.soundFX.volume.getValue() / 100.0F, 1.0F);
      }

   }

   public void playSound(String name) {
      try {
         Clip clip = AudioSystem.getClip();
         clip.open(AudioSystem.getAudioInputStream((new File(ConfigManager.SOUNDS_FOLDER, name + ".wav")).getAbsoluteFile()));
         FloatControl floatControl = (FloatControl)clip.getControl(Type.MASTER_GAIN);
         floatControl.setValue(floatControl.getMaximum() - floatControl.getMinimum() * ((float)(Integer)ModuleManager.soundFX.volume.getValue() / 100.0F) + floatControl.getMinimum());
         clip.start();
      } catch (Exception var4) {
         String var10000 = ClientSettings.isRu() ? "Ошибка воспроизведения звука! Проверь " : "Error with playing sound! Check ";
         Command.sendMessage(var10000 + (new File(ConfigManager.SOUNDS_FOLDER, name + ".wav")).getAbsolutePath());
      }

   }

   public void playSlider() {
      this.playSound(this.SCROLL_SOUNDEVENT);
   }

   public void playBoolean() {
      this.playSound(this.BOOLEAN_SOUNDEVENT);
   }

   public void playSwipeIn() {
      this.playSound(this.SWIPEIN_SOUNDEVENT);
   }

   public void playSwipeOut() {
      this.playSound(this.SWIPEOUT_SOUNDEVENT);
   }

   public void playPmSound(ChatUtils.PMSound sound) {
      if (sound == ChatUtils.PMSound.Default) {
         this.playSound(this.PM_SOUNDEVENT);
      } else {
         Managers.SOUND.playSound("pmsound");
      }

   }
}
