package tsunami.features.modules;

import com.mojang.logging.LogUtils;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import net.minecraft.class_124;
import net.minecraft.class_1713;
import net.minecraft.class_2561;
import net.minecraft.class_2596;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_3675;
import net.minecraft.class_4587;
import net.minecraft.class_7202;
import net.minecraft.class_7204;
import net.minecraft.class_746;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tsunami.TsunamiClient;
import tsunami.core.Managers;
import tsunami.core.manager.client.CommandManager;
import tsunami.core.manager.client.ModuleManager;
import tsunami.features.modules.client.ClientSettings;
import tsunami.features.modules.client.UnHook;
import tsunami.features.modules.client.Windows;
import tsunami.gui.notification.Notification;
import tsunami.setting.Setting;
import tsunami.setting.impl.Bind;

public abstract class Module {
   private final Setting<Bind> bind = new Setting("Keybind", new Bind(-1, false, false));
   private final Setting<Boolean> drawn = new Setting("Drawn", true);
   private final Setting<Boolean> enabled = new Setting("Enabled", false);
   private final String description;
   private final Module.Category category;
   private final String displayName;
   private final List<String> ignoreSoundList = Arrays.asList("ClickGui", "ThunderGui", "HudEditor");
   public static final class_310 mc = class_310.method_1551();

   public Module(@NotNull String name, @NotNull Module.Category category) {
      this.displayName = name;
      String var10001 = category.getName().toLowerCase();
      this.description = "descriptions." + var10001 + "." + name.toLowerCase();
      this.category = category;
   }

   public void onEnable() {
   }

   public void onDisable() {
   }

   public void onLogin() {
   }

   public void onLogout() {
   }

   public void onUpdate() {
   }

   public void onRender2D(class_332 event) {
   }

   public void onRender3D(class_4587 event) {
   }

   public void onUnload() {
   }

   public boolean isToggleable() {
      return true;
   }

   protected void sendPacket(class_2596<?> packet) {
      if (mc.method_1562() != null) {
         mc.method_1562().method_52787(packet);
      }
   }

   protected void sendPacketSilent(class_2596<?> packet) {
      if (mc.method_1562() != null) {
         TsunamiClient.core.silentPackets.add(packet);
         mc.method_1562().method_52787(packet);
      }
   }

   protected void sendSequencedPacket(class_7204 packetCreator) {
      if (mc.method_1562() != null && mc.field_1687 != null) {
         class_7202 pendingUpdateManager = mc.field_1687.method_41925().method_41937();

         try {
            int i = pendingUpdateManager.method_41942();
            mc.method_1562().method_52787(packetCreator.predict(i));
         } catch (Throwable var6) {
            if (pendingUpdateManager != null) {
               try {
                  pendingUpdateManager.close();
               } catch (Throwable var5) {
                  var6.addSuppressed(var5);
               }
            }

            throw var6;
         }

         if (pendingUpdateManager != null) {
            pendingUpdateManager.close();
         }

      }
   }

   public String getDisplayInfo() {
      return null;
   }

   public boolean isOn() {
      return (Boolean)this.enabled.getValue();
   }

   public boolean isOff() {
      return !(Boolean)this.enabled.getValue();
   }

   public void setEnabled(boolean enabled) {
      this.enabled.setValue(enabled);
   }

   public void onThread() {
   }

   public void enable() {
      if (!(this instanceof UnHook)) {
         this.enabled.setValue(true);
      }

      if (!fullNullCheck() || this instanceof UnHook || this instanceof Windows) {
         this.onEnable();
      }

      if (this.isOn()) {
         TsunamiClient.EVENT_BUS.subscribe((Object)this);
      }

      if (!fullNullCheck()) {
         LogUtils.getLogger().info("[Tsunami] enabled " + this.getName());
         Managers.MODULE.sortModules();
         if (!this.ignoreSoundList.contains(this.getDisplayName())) {
            Managers.NOTIFICATION.publicity(this.getDisplayName(), ClientSettings.isRu() ? "Модуль включен!" : "Was Enabled!", 2, Notification.Type.ENABLED);
            Managers.SOUND.playEnable();
         }

      }
   }

   public void disable(String reason) {
      this.sendMessage(reason);
      this.disable();
   }

   public void disable() {
      try {
         TsunamiClient.EVENT_BUS.unsubscribe((Object)this);
      } catch (Exception var2) {
      }

      this.enabled.setValue(false);
      Managers.MODULE.sortModules();
      if (!fullNullCheck()) {
         this.onDisable();
         TsunamiClient.LOGGER.info("[Tsunami] disabled {}", this.getName());
         if (!this.ignoreSoundList.contains(this.getDisplayName())) {
            Managers.NOTIFICATION.publicity(this.getDisplayName(), ClientSettings.isRu() ? "Модуль выключен!" : "Was Disabled!", 2, Notification.Type.DISABLED);
            Managers.SOUND.playDisable();
         }

      }
   }

   public void toggle() {
      if ((Boolean)this.enabled.getValue()) {
         this.disable();
      } else {
         this.enable();
      }

   }

   public String getDisplayName() {
      return this.displayName;
   }

   public String getDescription() {
      return this.description;
   }

   public boolean isDrawn() {
      return (Boolean)this.drawn.getValue();
   }

   public void setDrawn(boolean d) {
      this.drawn.setValue(d);
   }

   public Module.Category getCategory() {
      return this.category;
   }

   public Bind getBind() {
      return (Bind)this.bind.getValue();
   }

   public void setBind(int key, boolean mouse, boolean hold) {
      this.setBind(new Bind(key, mouse, hold));
   }

   public void setBind(Bind b) {
      this.bind.setValue(b);
   }

   public boolean listening() {
      return this.isOn();
   }

   public String getFullArrayString() {
      String var10000 = this.getDisplayName();
      return var10000 + String.valueOf(class_124.field_1080) + (this.getDisplayInfo() != null ? " [" + String.valueOf(class_124.field_1068) + this.getDisplayInfo() + String.valueOf(class_124.field_1080) + "]" : "");
   }

   public static boolean fullNullCheck() {
      return mc.field_1724 == null || mc.field_1687 == null || ModuleManager.unHook.isEnabled();
   }

   public String getName() {
      return this.getDisplayName();
   }

   public List<Setting<?>> getSettings() {
      ArrayList<Setting<?>> settingList = new ArrayList();

      for(Class currentSuperclass = this.getClass(); currentSuperclass != null; currentSuperclass = currentSuperclass.getSuperclass()) {
         Field[] var3 = currentSuperclass.getDeclaredFields();
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            Field field = var3[var5];
            if (Setting.class.isAssignableFrom(field.getType())) {
               try {
                  field.setAccessible(true);
                  settingList.add((Setting)field.get(this));
               } catch (IllegalAccessException var8) {
                  TsunamiClient.LOGGER.warn(var8.getMessage());
               }
            }
         }
      }

      settingList.forEach((s) -> {
         s.setModule(this);
      });
      return settingList;
   }

   public boolean isEnabled() {
      return this.isOn();
   }

   public boolean isDisabled() {
      return !this.isEnabled();
   }

   public static void clickSlot(int id) {
      if (id != -1 && mc.field_1761 != null && mc.field_1724 != null) {
         mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, id, 0, class_1713.field_7790, mc.field_1724);
      }
   }

   public static void clickSlot(int id, class_1713 type) {
      if (id != -1 && mc.field_1761 != null && mc.field_1724 != null) {
         mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, id, 0, type, mc.field_1724);
      }
   }

   public static void clickSlot(int id, int button, class_1713 type) {
      if (id != -1 && mc.field_1761 != null && mc.field_1724 != null) {
         mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, id, button, type, mc.field_1724);
      }
   }

   public void sendMessage(String message) {
      if (!fullNullCheck() && (Boolean)ClientSettings.clientMessages.getValue() && !ModuleManager.unHook.isEnabled()) {
         if (mc.method_18854()) {
            class_746 var10000 = mc.field_1724;
            String var10001 = CommandManager.getClientMessage();
            var10000.method_43496(class_2561.method_30163(var10001 + " " + String.valueOf(class_124.field_1080) + "[" + String.valueOf(class_124.field_1064) + this.getDisplayName() + String.valueOf(class_124.field_1080) + "] " + message));
         } else {
            mc.method_40000(() -> {
               class_746 var10000 = mc.field_1724;
               String var10001 = CommandManager.getClientMessage();
               var10000.method_43496(class_2561.method_30163(var10001 + " " + String.valueOf(class_124.field_1080) + "[" + String.valueOf(class_124.field_1064) + this.getDisplayName() + String.valueOf(class_124.field_1080) + "] " + message));
            });
         }

      }
   }

   public void sendChatMessage(String message) {
      if (!fullNullCheck()) {
         mc.method_1562().method_45729(message);
      }
   }

   public void sendChatCommand(String command) {
      if (!fullNullCheck()) {
         mc.method_1562().method_45730(command);
      }
   }

   public void debug(String message) {
      if (!fullNullCheck() && (Boolean)ClientSettings.debug.getValue()) {
         if (mc.method_18854()) {
            class_746 var10000 = mc.field_1724;
            String var10001 = CommandManager.getClientMessage();
            var10000.method_43496(class_2561.method_30163(var10001 + " " + String.valueOf(class_124.field_1080) + "[" + String.valueOf(class_124.field_1064) + this.getDisplayName() + String.valueOf(class_124.field_1080) + "] [\ud83d\udd27] " + message));
         } else {
            mc.method_40000(() -> {
               class_746 var10000 = mc.field_1724;
               String var10001 = CommandManager.getClientMessage();
               var10000.method_43496(class_2561.method_30163(var10001 + " " + String.valueOf(class_124.field_1080) + "[" + String.valueOf(class_124.field_1064) + this.getDisplayName() + String.valueOf(class_124.field_1080) + "] [\ud83d\udd27] " + message));
            });
         }

      }
   }

   public boolean isKeyPressed(int button) {
      if (button != -1 && !ModuleManager.unHook.isEnabled()) {
         if (Managers.MODULE.activeMouseKeys.contains(button)) {
            Managers.MODULE.activeMouseKeys.clear();
            return true;
         } else {
            return button < 10 ? false : class_3675.method_15987(mc.method_22683().method_4490(), button);
         }
      } else {
         return false;
      }
   }

   public boolean isKeyPressed(Setting<Bind> bind) {
      return ((Bind)bind.getValue()).getKey() != -1 && !ModuleManager.unHook.isEnabled() ? this.isKeyPressed(((Bind)bind.getValue()).getKey()) : false;
   }

   @Nullable
   public Setting<?> getSettingByName(String name) {
      Iterator var2 = this.getSettings().iterator();

      Setting setting;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         setting = (Setting)var2.next();
      } while(!setting.getName().equalsIgnoreCase(name));

      return setting;
   }

   public static class Category {
      private final String name;
      private final boolean hidden;
      private static final Map<String, Module.Category> CATEGORIES = new LinkedHashMap();
      public static final Module.Category COMBAT = new Module.Category("Combat");
      public static final Module.Category MISC = new Module.Category("Misc");
      public static final Module.Category RENDER = new Module.Category("Render");
      public static final Module.Category MOVEMENT = new Module.Category("Movement");
      public static final Module.Category PLAYER = new Module.Category("Player");
      public static final Module.Category CLIENT = new Module.Category("Client");
      public static final Module.Category DONUT = new Module.Category("Donut");
      public static final Module.Category HUD = new Module.Category("HUD");
      public static final Module.Category NONE = new Module.Category("NONE", true);

      private Category(String name) {
         this.name = name;
         this.hidden = false;
      }

      private Category(String name, boolean hidden) {
         this.name = name;
         this.hidden = hidden;
      }

      public String getName() {
         return this.name;
      }

      public boolean isHidden() {
         return this.hidden;
      }

      public static Module.Category getCategory(String name) {
         return (Module.Category)CATEGORIES.computeIfAbsent(name, Module.Category::new);
      }

      public static Collection<Module.Category> getAllCategories() {
         return CATEGORIES.values();
      }

      public static Collection<Module.Category> values() {
         return (Collection)CATEGORIES.values().stream().filter((category) -> {
            return !category.isHidden();
         }).collect(ArrayList::new, (list, category) -> {
            list.add(category);
         }, ArrayList::addAll);
      }

      public static Collection<Module.Category> getDisplayCategories() {
         return values();
      }

      public static Collection<Module.Category> getVisibleCategories() {
         return values();
      }

      public static boolean isCustomCategory(Module.Category category) {
         Set<String> predefinedCategoryNames = Set.of("Combat", "Misc", "Render", "Movement", "Player", "Client", "Donut", "HUD", "NONE");
         return !predefinedCategoryNames.contains(category.getName());
      }

      public boolean equals(Object o) {
         if (this == o) {
            return true;
         } else if (o != null && this.getClass() == o.getClass()) {
            Module.Category category = (Module.Category)o;
            return Objects.equals(this.name, category.name);
         } else {
            return false;
         }
      }

      public int hashCode() {
         return Objects.hash(new Object[]{this.name});
      }

      static {
         CATEGORIES.put("Combat", COMBAT);
         CATEGORIES.put("Misc", MISC);
         CATEGORIES.put("Render", RENDER);
         CATEGORIES.put("Movement", MOVEMENT);
         CATEGORIES.put("Player", PLAYER);
         CATEGORIES.put("Client", CLIENT);
         CATEGORIES.put("Donut", DONUT);
         CATEGORIES.put("HUD", HUD);
         CATEGORIES.put("NONE", NONE);
      }
   }
}
