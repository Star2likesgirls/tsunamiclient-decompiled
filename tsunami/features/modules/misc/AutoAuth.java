package tsunami.features.modules.misc;

import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_124;
import net.minecraft.class_2596;
import net.minecraft.class_7439;
import org.apache.commons.lang3.RandomStringUtils;
import org.jetbrains.annotations.NotNull;
import tsunami.core.Managers;
import tsunami.events.impl.PacketEvent;
import tsunami.features.modules.Module;
import tsunami.features.modules.client.ClientSettings;
import tsunami.gui.notification.Notification;
import tsunami.setting.Setting;

public final class AutoAuth extends Module {
   private final Setting<AutoAuth.Mode> mode;
   private final Setting<String> cpass;
   private final Setting<Boolean> show;

   public AutoAuth() {
      super("AutoAuth", Module.Category.NONE);
      this.mode = new Setting("Mode", AutoAuth.Mode.Custom);
      this.cpass = new Setting("Password", "babidjon777", (v) -> {
         return this.mode.getValue() == AutoAuth.Mode.Custom;
      });
      this.show = new Setting("ShowPassword", true);
   }

   public void onEnable() {
      String warningMsg = ClientSettings.isRu() ? String.valueOf(class_124.field_1061) + "Внимание! " + String.valueOf(class_124.field_1070) + "Пароль сохраняется в конфиге, перед передачей конфига " + String.valueOf(class_124.field_1061) + " ВЫКЛЮЧИ МОДУЛЬ!" : String.valueOf(class_124.field_1061) + "Attention! " + String.valueOf(class_124.field_1070) + "The passwords are stored in the config, so before sharing your configs " + String.valueOf(class_124.field_1061) + " TOGGLE OFF THE MODULE!";
      this.sendMessage(warningMsg);
   }

   public void onDisable() {
      this.sendMessage("Resetting password...");
      this.cpass.setValue("none");
   }

   @EventHandler
   public void onPacketReceive(@NotNull PacketEvent.Receive event) {
      class_2596 var3 = event.getPacket();
      if (var3 instanceof class_7439) {
         class_7439 pac = (class_7439)var3;
         if (mc.method_1562() != null) {
            String password = "";
            String var10001;
            switch(((AutoAuth.Mode)this.mode.getValue()).ordinal()) {
            case 0:
               password = (String)this.cpass.getValue();
               if (password.isEmpty()) {
                  var10001 = String.valueOf(class_124.field_1061);
                  this.sendMessage(var10001 + (ClientSettings.isRu() ? "Ошибка регистрации: Пароль пуст!" : "Registration error: Password is empty!"));
                  return;
               }
               break;
            case 1:
               String var10000 = RandomStringUtils.randomAlphabetic(5);
               password = var10000 + RandomStringUtils.randomPrint(5);
               break;
            case 2:
               password = "qwerty123";
            }

            String m = pac.comp_763().getString().toLowerCase();
            if (!m.contains("/reg") && !m.contains("/register") && !m.contains("зарегистрируйтесь")) {
               if (m.contains("авторизуйтесь") || m.contains("/l")) {
                  mc.method_1562().method_45730("login " + password);
                  Managers.NOTIFICATION.publicity("AutoAuth", ClientSettings.isRu() ? "Выполнен вход!" : "Logged in!", 4, Notification.Type.SUCCESS);
               }
            } else {
               mc.method_1562().method_45730("reg " + password + " " + password);
               if ((Boolean)this.show.getValue()) {
                  var10001 = ClientSettings.isRu() ? "Твой пароль: " : "Your password: ";
                  this.sendMessage(var10001 + String.valueOf(class_124.field_1061) + password);
               }

               Managers.NOTIFICATION.publicity("AutoAuth", ClientSettings.isRu() ? "Выполнена регистрация!" : "Registration completed!", 4, Notification.Type.SUCCESS);
            }
         }
      }

   }

   private static enum Mode {
      Custom,
      Random,
      Qwerty;

      // $FF: synthetic method
      private static AutoAuth.Mode[] $values() {
         return new AutoAuth.Mode[]{Custom, Random, Qwerty};
      }
   }
}
