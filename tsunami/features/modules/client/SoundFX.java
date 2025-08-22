package tsunami.features.modules.client;

import meteordevelopment.orbit.EventHandler;
import org.jetbrains.annotations.NotNull;
import tsunami.core.Managers;
import tsunami.events.impl.EventAttack;
import tsunami.events.impl.EventDeath;
import tsunami.features.modules.Module;
import tsunami.features.modules.combat.Aura;
import tsunami.features.modules.combat.AutoCrystal;
import tsunami.setting.Setting;

public final class SoundFX extends Module {
   public final Setting<Integer> volume = new Setting("Volume", 100, 0, 100);
   public final Setting<SoundFX.OnOffSound> enableMode;
   public final Setting<SoundFX.OnOffSound> disableMode;
   public final Setting<SoundFX.HitSound> hitSound;
   public final Setting<SoundFX.KillSound> killSound;
   public final Setting<SoundFX.ScrollSound> scrollSound;

   public SoundFX() {
      super("SoundFX", Module.Category.CLIENT);
      this.enableMode = new Setting("EnableMode", SoundFX.OnOffSound.Inertia);
      this.disableMode = new Setting("DisableMode", SoundFX.OnOffSound.Inertia);
      this.hitSound = new Setting("HitSound", SoundFX.HitSound.OFF);
      this.killSound = new Setting("KillSound", SoundFX.KillSound.OFF);
      this.scrollSound = new Setting("ScrollSound", SoundFX.ScrollSound.KeyBoard);
   }

   @EventHandler
   public void onAttack(@NotNull EventAttack event) {
      if (!event.isPre()) {
         Managers.SOUND.playHitSound((SoundFX.HitSound)this.hitSound.getValue());
      }

   }

   @EventHandler
   public void onDeath(EventDeath e) {
      if (Aura.target != null && Aura.target == e.getPlayer() && this.killSound.is(SoundFX.KillSound.Custom)) {
         Managers.SOUND.playSound("kill");
      } else {
         if (AutoCrystal.target != null && AutoCrystal.target == e.getPlayer() && this.killSound.is(SoundFX.KillSound.Custom)) {
            Managers.SOUND.playSound("kill");
         }

      }
   }

   public static enum OnOffSound {
      Custom,
      Inertia,
      OFF;

      // $FF: synthetic method
      private static SoundFX.OnOffSound[] $values() {
         return new SoundFX.OnOffSound[]{Custom, Inertia, OFF};
      }
   }

   public static enum HitSound {
      UWU,
      MOAN,
      SKEET,
      RIFK,
      KEYBOARD,
      CUTIE,
      CUSTOM,
      OFF;

      // $FF: synthetic method
      private static SoundFX.HitSound[] $values() {
         return new SoundFX.HitSound[]{UWU, MOAN, SKEET, RIFK, KEYBOARD, CUTIE, CUSTOM, OFF};
      }
   }

   public static enum KillSound {
      Custom,
      OFF;

      // $FF: synthetic method
      private static SoundFX.KillSound[] $values() {
         return new SoundFX.KillSound[]{Custom, OFF};
      }
   }

   public static enum ScrollSound {
      Custom,
      OFF,
      KeyBoard;

      // $FF: synthetic method
      private static SoundFX.ScrollSound[] $values() {
         return new SoundFX.ScrollSound[]{Custom, OFF, KeyBoard};
      }
   }
}
