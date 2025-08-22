package tsunami.features.modules.render;

import java.awt.Color;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_2761;
import tsunami.events.impl.PacketEvent;
import tsunami.features.modules.Module;
import tsunami.setting.Setting;
import tsunami.setting.impl.BooleanSettingGroup;
import tsunami.setting.impl.ColorSetting;

public class WorldTweaks extends Module {
   public static final Setting<BooleanSettingGroup> fogModify = new Setting("FogModify", new BooleanSettingGroup(true));
   public static final Setting<Integer> fogStart;
   public static final Setting<Integer> fogEnd;
   public static final Setting<ColorSetting> fogColor;
   public final Setting<Boolean> ctime = new Setting("ChangeTime", false);
   public final Setting<Integer> ctimeVal = new Setting("Time", 21, 0, 23);
   long oldTime;

   public WorldTweaks() {
      super("WorldTweaks", Module.Category.NONE);
   }

   public void onEnable() {
      this.oldTime = mc.field_1687.method_8510();
   }

   public void onDisable() {
      mc.field_1687.method_8435(this.oldTime);
   }

   @EventHandler
   private void onPacketReceive(PacketEvent.Receive event) {
      if (event.getPacket() instanceof class_2761 && (Boolean)this.ctime.getValue()) {
         this.oldTime = ((class_2761)event.getPacket()).method_11871();
         event.cancel();
      }

   }

   public void onUpdate() {
      if ((Boolean)this.ctime.getValue()) {
         mc.field_1687.method_8435((long)((Integer)this.ctimeVal.getValue() * 1000));
      }

   }

   static {
      fogStart = (new Setting("FogStart", 0, 0, 256)).addToGroup(fogModify);
      fogEnd = (new Setting("FogEnd", 64, 10, 256)).addToGroup(fogModify);
      fogColor = (new Setting("FogColor", new ColorSetting(new Color(11075839)))).addToGroup(fogModify);
   }
}
