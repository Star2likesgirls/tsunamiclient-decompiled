package tsunami.setting.impl;

import java.util.List;
import net.minecraft.class_1792;
import net.minecraft.class_2248;

public class ItemSelectSetting {
   private List<String> itemsById;

   public ItemSelectSetting(List<String> itemsById) {
      this.itemsById = itemsById;
   }

   public List<String> getItemsById() {
      return this.itemsById;
   }

   public void add(String s) {
      this.itemsById.add(s);
   }

   public void remove(String s) {
      this.itemsById.remove(s);
   }

   public boolean contains(String s) {
      return this.itemsById.contains(s);
   }

   public void add(class_2248 b) {
      this.add(b.method_9539().replace("block.minecraft.", ""));
   }

   public void add(class_1792 i) {
      this.add(i.method_7876().replace("item.minecraft.", ""));
   }

   public void remove(class_2248 b) {
      this.remove(b.method_9539().replace("block.minecraft.", ""));
   }

   public void remove(class_1792 i) {
      this.remove(i.method_7876().replace("item.minecraft.", ""));
   }

   public boolean contains(class_2248 b) {
      return this.contains(b.method_9539().replace("block.minecraft.", ""));
   }

   public boolean contains(class_1792 i) {
      return this.contains(i.method_7876().replace("item.minecraft.", ""));
   }

   public void clear() {
      this.itemsById.clear();
   }
}
