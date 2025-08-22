package tsunami.setting;

import java.util.function.Predicate;
import tsunami.TsunamiClient;
import tsunami.events.impl.EventSetting;
import tsunami.features.modules.Module;
import tsunami.setting.impl.Bind;
import tsunami.setting.impl.BooleanSettingGroup;
import tsunami.setting.impl.EnumConverter;
import tsunami.setting.impl.ItemSelectSetting;
import tsunami.setting.impl.PositionSetting;
import tsunami.setting.impl.SettingGroup;

public class Setting<T> {
   private final String name;
   private final T defaultValue;
   private T value;
   private T plannedValue;
   private T min;
   private T max;
   public Setting<?> group = null;
   private boolean hasRestriction;
   private Predicate<T> visibility;
   private Module module;

   public Setting(String name, T defaultValue) {
      this.name = name;
      this.defaultValue = defaultValue;
      this.value = defaultValue;
      this.plannedValue = defaultValue;
   }

   public Setting(String name, T defaultValue, T min, T max) {
      this.name = name;
      this.defaultValue = defaultValue;
      this.value = defaultValue;
      this.min = min;
      this.max = max;
      this.plannedValue = defaultValue;
      this.hasRestriction = true;
   }

   public Setting(String name, T defaultValue, T min, T max, Predicate<T> visibility) {
      this.name = name;
      this.defaultValue = defaultValue;
      this.value = defaultValue;
      this.min = min;
      this.max = max;
      this.plannedValue = defaultValue;
      this.visibility = visibility;
      this.hasRestriction = true;
   }

   public Setting(String name, T defaultValue, Predicate<T> visibility) {
      this.name = name;
      this.defaultValue = defaultValue;
      this.value = defaultValue;
      this.visibility = visibility;
      this.plannedValue = defaultValue;
   }

   public static Enum get(Enum clazz) {
      int index = EnumConverter.currentEnum(clazz);

      for(int i = 0; i < ((Enum[])clazz.getClass().getEnumConstants()).length; ++i) {
         Enum e = ((Enum[])clazz.getClass().getEnumConstants())[i];
         if (i == index + 1) {
            return e;
         }
      }

      return ((Enum[])clazz.getClass().getEnumConstants())[0];
   }

   public String getName() {
      return this.name;
   }

   public T getValue() {
      return this.value;
   }

   public void setValue(T value) {
      this.setValueSilent(value);
      TsunamiClient.EVENT_BUS.post((Object)(new EventSetting(this)));
   }

   public void setValueSilent(T value) {
      this.setPlannedValue(value);
      if (this.hasRestriction) {
         if (((Number)this.min).floatValue() > ((Number)value).floatValue()) {
            this.setPlannedValue(this.min);
         }

         if (((Number)this.max).floatValue() < ((Number)value).floatValue()) {
            this.setPlannedValue(this.max);
         }
      }

      this.value = this.plannedValue;
   }

   public float getPow2Value() {
      if (this.value instanceof Float) {
         return (Float)this.value * (Float)this.value;
      } else {
         return this.value instanceof Integer ? (float)((Integer)this.value * (Integer)this.value) : 0.0F;
      }
   }

   public void setPlannedValue(T value) {
      this.plannedValue = value;
   }

   public T getMin() {
      return this.min;
   }

   public void setMin(T min) {
      this.min = min;
   }

   public T getMax() {
      return this.max;
   }

   public void setMax(T max) {
      this.max = max;
   }

   public Module getModule() {
      return this.module;
   }

   public void setModule(Module module) {
      this.module = module;
   }

   public String currentEnumName() {
      return EnumConverter.getProperName((Enum)this.value);
   }

   public String[] getModes() {
      return EnumConverter.getNames((Enum)this.value);
   }

   public void setEnum(Enum mod) {
      this.plannedValue = mod;
   }

   public void increaseEnum() {
      this.plannedValue = EnumConverter.increaseEnum((Enum)this.value);
      this.value = this.plannedValue;
      TsunamiClient.EVENT_BUS.post((Object)(new EventSetting(this)));
   }

   public void setEnumByNumber(int id) {
      this.plannedValue = EnumConverter.setEnumInt((Enum)this.value, id);
      this.value = this.plannedValue;
      TsunamiClient.EVENT_BUS.post((Object)(new EventSetting(this)));
   }

   public boolean isNumberSetting() {
      return this.value instanceof Double || this.value instanceof Integer || this.value instanceof Short || this.value instanceof Long || this.value instanceof Float;
   }

   public boolean isInteger() {
      return this.value instanceof Integer;
   }

   public boolean isFloat() {
      return this.value instanceof Float;
   }

   public boolean isEnumSetting() {
      return this.value.getClass().isEnum();
   }

   public boolean isBindSetting() {
      return this.value instanceof Bind;
   }

   public boolean isStringSetting() {
      return this.value instanceof String;
   }

   public boolean isItemSelectSetting() {
      return this.value instanceof ItemSelectSetting;
   }

   public boolean isPositionSetting() {
      return this.value instanceof PositionSetting;
   }

   public T getDefaultValue() {
      return this.defaultValue;
   }

   public boolean hasRestriction() {
      return this.hasRestriction;
   }

   public Setting<T> addToGroup(Setting<?> group) {
      this.group = group;
      return this;
   }

   public boolean isVisible() {
      if (this.group != null) {
         Object var2 = this.group.getValue();
         if (var2 instanceof BooleanSettingGroup) {
            BooleanSettingGroup bp = (BooleanSettingGroup)var2;
            if (!bp.isExtended()) {
               return false;
            }
         }

         var2 = this.group.getValue();
         if (var2 instanceof SettingGroup) {
            SettingGroup p = (SettingGroup)var2;
            if (!p.isExtended()) {
               return false;
            }
         }
      }

      return this.visibility == null ? true : this.visibility.test(this.getValue());
   }

   public boolean is(T v) {
      return this.value == v;
   }

   public boolean not(T v) {
      return this.value != v;
   }
}
