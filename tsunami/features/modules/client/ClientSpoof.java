package tsunami.features.modules.client;

import tsunami.features.modules.Module;
import tsunami.setting.Setting;

public class ClientSpoof extends Module {
   private final Setting<ClientSpoof.Mode> mode;
   private final Setting<String> custom;

   public ClientSpoof() {
      super("ClientSpoof", Module.Category.CLIENT);
      this.mode = new Setting("Mode", ClientSpoof.Mode.Vanilla);
      this.custom = new Setting("Client", "feather", (v) -> {
         return this.mode.getValue() == ClientSpoof.Mode.Custom;
      });
   }

   public String getClientName() {
      String var10000;
      switch(((ClientSpoof.Mode)this.mode.getValue()).ordinal()) {
      case 0:
         var10000 = "vanilla";
         break;
      case 1:
         var10000 = "lunarclient:1.20.4";
         break;
      case 2:
         var10000 = "lunarclient:1.20.1";
         break;
      case 3:
         var10000 = ((String)this.custom.getValue()).toString();
         break;
      default:
         var10000 = null;
      }

      return var10000;
   }

   public static enum Mode {
      Vanilla,
      Lunar1_20_4,
      Lunar1_20_1,
      Custom,
      Null;

      // $FF: synthetic method
      private static ClientSpoof.Mode[] $values() {
         return new ClientSpoof.Mode[]{Vanilla, Lunar1_20_4, Lunar1_20_1, Custom, Null};
      }
   }
}
