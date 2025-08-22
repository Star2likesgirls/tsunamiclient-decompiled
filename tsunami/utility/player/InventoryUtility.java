package tsunami.utility.player;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import net.minecraft.class_1280;
import net.minecraft.class_1293;
import net.minecraft.class_1294;
import net.minecraft.class_1657;
import net.minecraft.class_1743;
import net.minecraft.class_1748;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1810;
import net.minecraft.class_1821;
import net.minecraft.class_1829;
import net.minecraft.class_1890;
import net.minecraft.class_1893;
import net.minecraft.class_2248;
import net.minecraft.class_2868;
import net.minecraft.class_5134;
import net.minecraft.class_6880;
import net.minecraft.class_7923;
import net.minecraft.class_9334;
import org.jetbrains.annotations.NotNull;
import tsunami.core.Managers;
import tsunami.core.manager.client.ModuleManager;
import tsunami.features.modules.Module;
import tsunami.injection.accesors.IInteractionManager;

public final class InventoryUtility {
   private static int cachedSlot = -1;

   public static int getItemCount(class_1792 item) {
      if (Module.mc.field_1724 == null) {
         return 0;
      } else {
         int counter = 0;

         for(int i = 0; i <= 44; ++i) {
            class_1799 itemStack = Module.mc.field_1724.method_31548().method_5438(i);
            if (itemStack.method_7909() == item) {
               counter += itemStack.method_7947();
            }
         }

         return counter;
      }
   }

   public static SearchInvResult getAxe() {
      if (Module.mc.field_1724 == null) {
         return SearchInvResult.notFound();
      } else {
         int slot = -1;
         float f = 1.0F;

         for(int b1 = 9; b1 < 45; ++b1) {
            class_1799 itemStack = Module.mc.field_1724.method_31548().method_5438(b1 >= 36 ? b1 - 36 : b1);
            if (itemStack != null) {
               class_1792 var5 = itemStack.method_7909();
               if (var5 instanceof class_1743) {
                  class_1743 axe = (class_1743)var5;
                  float f1 = (float)(Integer)axe.method_57347().method_57829(class_9334.field_50072);
                  f1 += (float)class_1890.method_8225((class_6880)Module.mc.field_1687.method_30349().method_30530(class_1893.field_9118.method_58273()).method_40264(class_1893.field_9118).get(), itemStack);
                  if (f1 > f) {
                     f = f1;
                     slot = b1;
                  }
               }
            }
         }

         if (slot >= 36) {
            slot -= 36;
         }

         if (slot == -1) {
            return SearchInvResult.notFound();
         } else {
            return new SearchInvResult(slot, true, Module.mc.field_1724.method_31548().method_5438(slot));
         }
      }
   }

   public static SearchInvResult getPickAxeHotbar() {
      if (Module.mc.field_1724 == null) {
         return SearchInvResult.notFound();
      } else {
         int slot = -1;
         float f = 1.0F;

         for(int b1 = 0; b1 < 9; ++b1) {
            class_1799 itemStack = Module.mc.field_1724.method_31548().method_5438(b1);
            if (itemStack != null && itemStack.method_7909() instanceof class_1810) {
               float f1 = 0.0F;
               f1 += (float)class_1890.method_8225((class_6880)Module.mc.field_1687.method_30349().method_30530(class_1893.field_9131.method_58273()).method_40264(class_1893.field_9131).get(), itemStack);
               if (f1 > f) {
                  f = f1;
                  slot = b1;
               }
            }
         }

         if (slot == -1) {
            return SearchInvResult.notFound();
         } else {
            return new SearchInvResult(slot, true, Module.mc.field_1724.method_31548().method_5438(slot));
         }
      }
   }

   public static SearchInvResult getPickAxe() {
      if (Module.mc.field_1724 == null) {
         return SearchInvResult.notFound();
      } else {
         int slot = -1;
         float f = 1.0F;

         for(int b1 = 9; b1 < 45; ++b1) {
            class_1799 itemStack = Module.mc.field_1724.method_31548().method_5438(b1);
            if (itemStack != null && itemStack.method_7909() instanceof class_1810) {
               float f1 = 0.0F;
               f1 += (float)class_1890.method_8225((class_6880)Module.mc.field_1687.method_30349().method_30530(class_1893.field_9131.method_58273()).method_40264(class_1893.field_9131).get(), itemStack);
               if (f1 > f) {
                  f = f1;
                  slot = b1;
               }
            }
         }

         if (slot == -1) {
            return SearchInvResult.notFound();
         } else {
            return new SearchInvResult(slot, true, Module.mc.field_1724.method_31548().method_5438(slot));
         }
      }
   }

   public static SearchInvResult getPickAxeHotBar() {
      if (Module.mc.field_1724 == null) {
         return SearchInvResult.notFound();
      } else {
         int slot = -1;
         float f = 1.0F;

         for(int b1 = 0; b1 < 9; ++b1) {
            class_1799 itemStack = Module.mc.field_1724.method_31548().method_5438(b1);
            if (itemStack != null && itemStack.method_7909() instanceof class_1810) {
               float f1 = 0.0F;
               f1 += (float)class_1890.method_8225((class_6880)Module.mc.field_1687.method_30349().method_30530(class_1893.field_9131.method_58273()).method_40264(class_1893.field_9131).get(), itemStack);
               if (f1 > f) {
                  f = f1;
                  slot = b1;
               }
            }
         }

         if (slot == -1) {
            return SearchInvResult.notFound();
         } else {
            return new SearchInvResult(slot, true, Module.mc.field_1724.method_31548().method_5438(slot));
         }
      }
   }

   public static SearchInvResult getSkull() {
      if (Module.mc.field_1724 == null) {
         return SearchInvResult.notFound();
      } else {
         int slot = -1;

         for(int b1 = 0; b1 < 9; ++b1) {
            class_1799 itemStack = Module.mc.field_1724.method_31548().method_5438(b1);
            if (itemStack != null && (itemStack.method_7909().equals(class_1802.field_8398) || itemStack.method_7909().equals(class_1802.field_8791) || itemStack.method_7909().equals(class_1802.field_8681) || itemStack.method_7909().equals(class_1802.field_8575) || itemStack.method_7909().equals(class_1802.field_8470))) {
               slot = b1;
               break;
            }
         }

         return slot == -1 ? SearchInvResult.notFound() : new SearchInvResult(slot, true, Module.mc.field_1724.method_31548().method_5438(slot));
      }
   }

   public static SearchInvResult getSword() {
      if (Module.mc.field_1724 == null) {
         return SearchInvResult.notFound();
      } else {
         int slot = -1;
         float f = 1.0F;

         for(int b1 = 9; b1 < 45; ++b1) {
            class_1799 itemStack = Module.mc.field_1724.method_31548().method_5438(b1);
            if (itemStack != null) {
               class_1792 var5 = itemStack.method_7909();
               if (var5 instanceof class_1829) {
                  class_1829 sword = (class_1829)var5;
                  float f1 = (float)(Integer)sword.method_57347().method_57829(class_9334.field_50072);
                  f1 += (float)class_1890.method_8225((class_6880)Module.mc.field_1687.method_30349().method_30530(class_1893.field_9118.method_58273()).method_40264(class_1893.field_9118).get(), itemStack);
                  if (f1 > f) {
                     f = f1;
                     slot = b1;
                  }
               }
            }
         }

         if (slot == -1) {
            return SearchInvResult.notFound();
         } else {
            return new SearchInvResult(slot, true, Module.mc.field_1724.method_31548().method_5438(slot));
         }
      }
   }

   public static SearchInvResult getSwordHotBar() {
      if (Module.mc.field_1724 == null) {
         return SearchInvResult.notFound();
      } else {
         int slot = -1;
         float f = 1.0F;

         for(int b1 = 0; b1 < 9; ++b1) {
            class_1799 itemStack = Module.mc.field_1724.method_31548().method_5438(b1);
            if (itemStack != null) {
               class_1792 var5 = itemStack.method_7909();
               if (var5 instanceof class_1829) {
                  class_1829 sword = (class_1829)var5;
                  float f1 = (float)(Integer)sword.method_57347().method_57829(class_9334.field_50072);
                  f1 += (float)class_1890.method_8225((class_6880)Module.mc.field_1687.method_30349().method_30530(class_1893.field_9118.method_58273()).method_40264(class_1893.field_9118).get(), itemStack);
                  if (f1 > f) {
                     f = f1;
                     slot = b1;
                  }
               }
            }
         }

         if (slot == -1) {
            return SearchInvResult.notFound();
         } else {
            return new SearchInvResult(slot, true, Module.mc.field_1724.method_31548().method_5438(slot));
         }
      }
   }

   public static SearchInvResult getAxeHotBar() {
      if (Module.mc.field_1724 == null) {
         return SearchInvResult.notFound();
      } else {
         int slot = -1;
         float f = 1.0F;

         for(int b1 = 0; b1 < 9; ++b1) {
            class_1799 itemStack = Module.mc.field_1724.method_31548().method_5438(b1);
            if (itemStack != null) {
               class_1792 var5 = itemStack.method_7909();
               if (var5 instanceof class_1743) {
                  class_1743 axe = (class_1743)var5;
                  float f1 = (float)(Integer)axe.method_57347().method_57829(class_9334.field_50072);
                  f1 += (float)class_1890.method_8225((class_6880)Module.mc.field_1687.method_30349().method_30530(class_1893.field_9118.method_58273()).method_40264(class_1893.field_9118).get(), itemStack);
                  if (f1 > f) {
                     f = f1;
                     slot = b1;
                  }
               }
            }
         }

         if (slot == -1) {
            return SearchInvResult.notFound();
         } else {
            return new SearchInvResult(slot, true, Module.mc.field_1724.method_31548().method_5438(slot));
         }
      }
   }

   public static int getElytra() {
      Iterator var0 = Module.mc.field_1724.method_31548().field_7548.iterator();

      while(var0.hasNext()) {
         class_1799 stack = (class_1799)var0.next();
         if (stack.method_7909() == class_1802.field_8833 && stack.method_7919() < 430) {
            return -2;
         }
      }

      int slot = -1;

      for(int i = 0; i < 36; ++i) {
         class_1799 s = Module.mc.field_1724.method_31548().method_5438(i);
         if (s.method_7909() == class_1802.field_8833 && s.method_7919() < 430) {
            slot = i;
            break;
         }
      }

      if (slot < 9 && slot != -1) {
         slot += 36;
      }

      return slot;
   }

   public static SearchInvResult findInHotBar(InventoryUtility.Searcher searcher) {
      if (Module.mc.field_1724 != null) {
         for(int i = 0; i < 9; ++i) {
            class_1799 stack = Module.mc.field_1724.method_31548().method_5438(i);
            if (searcher.isValid(stack)) {
               return new SearchInvResult(i, true, stack);
            }
         }
      }

      return SearchInvResult.notFound();
   }

   public static SearchInvResult findItemInHotBar(List<class_1792> items) {
      return findInHotBar((stack) -> {
         return items.contains(stack.method_7909());
      });
   }

   public static SearchInvResult findItemInHotBar(class_1792... items) {
      return findItemInHotBar(Arrays.asList(items));
   }

   public static SearchInvResult findInInventory(InventoryUtility.Searcher searcher) {
      if (Module.mc.field_1724 != null) {
         for(int i = 36; i >= 0; --i) {
            class_1799 stack = Module.mc.field_1724.method_31548().method_5438(i);
            if (searcher.isValid(stack)) {
               if (i < 9) {
                  i += 36;
               }

               return new SearchInvResult(i, true, stack);
            }
         }
      }

      return SearchInvResult.notFound();
   }

   public static SearchInvResult findItemInInventory(List<class_1792> items) {
      return findInInventory((stack) -> {
         return items.contains(stack.method_7909());
      });
   }

   public static SearchInvResult findItemInInventory(class_1792... items) {
      return findItemInInventory(Arrays.asList(items));
   }

   public static SearchInvResult findBlockInHotBar(@NotNull List<class_2248> blocks) {
      return findItemInHotBar(blocks.stream().map(class_2248::method_8389).toList());
   }

   public static SearchInvResult findBlockInHotBar(class_2248... blocks) {
      return findItemInHotBar(Arrays.stream(blocks).map(class_2248::method_8389).toList());
   }

   public static SearchInvResult findBlockInInventory(@NotNull List<class_2248> blocks) {
      return findItemInInventory(blocks.stream().map(class_2248::method_8389).toList());
   }

   public static SearchInvResult findBlockInInventory(class_2248... blocks) {
      return findItemInInventory(Arrays.stream(blocks).map(class_2248::method_8389).toList());
   }

   public static void saveSlot() {
      cachedSlot = Module.mc.field_1724.method_31548().field_7545;
   }

   public static void returnSlot() {
      if (cachedSlot != -1) {
         switchTo(cachedSlot);
      }

      cachedSlot = -1;
   }

   public static void saveAndSwitchTo(int slot) {
      saveSlot();
      if (Module.mc.field_1724 != null && Module.mc.method_1562() != null) {
         if (Module.mc.field_1724.method_31548().field_7545 != slot || Managers.PLAYER.serverSideSlot != slot) {
            Module.mc.field_1724.method_31548().field_7545 = slot;
            ((IInteractionManager)Module.mc.field_1761).syncSlot();
         }
      }
   }

   public static void switchTo(int slot) {
      if (Module.mc.field_1724 != null && Module.mc.method_1562() != null) {
         if (Module.mc.field_1724.method_31548().field_7545 != slot || Managers.PLAYER.serverSideSlot != slot) {
            Module.mc.field_1724.method_31548().field_7545 = slot;
            ((IInteractionManager)Module.mc.field_1761).syncSlot();
         }
      }
   }

   public static void switchToSilent(int slot) {
      if (Module.mc.field_1724 != null && Module.mc.method_1562() != null) {
         Module.mc.method_1562().method_52787(new class_2868(slot));
      }
   }

   public static SearchInvResult getAntiWeaknessItem() {
      if (Module.mc.field_1724 == null) {
         return SearchInvResult.notFound();
      } else {
         class_1792 mainHand = Module.mc.field_1724.method_6047().method_7909();
         return !(mainHand instanceof class_1829) && !(mainHand instanceof class_1810) && !(mainHand instanceof class_1743) && !(mainHand instanceof class_1821) ? findInHotBar((itemStack) -> {
            return itemStack.method_7909() instanceof class_1829 || itemStack.method_7909() instanceof class_1810 || itemStack.method_7909() instanceof class_1743 || itemStack.method_7909() instanceof class_1821;
         }) : new SearchInvResult(Module.mc.field_1724.method_31548().field_7545, true, Module.mc.field_1724.method_6047());
      }
   }

   public static float getHitDamage(@NotNull class_1799 weapon, class_1657 ent) {
      if (Module.mc.field_1724 == null) {
         return 0.0F;
      } else {
         float baseDamage = 1.0F;
         class_1792 var4 = weapon.method_7909();
         if (var4 instanceof class_1829) {
            class_1829 swordItem = (class_1829)var4;
            baseDamage = 7.0F;
         }

         var4 = weapon.method_7909();
         if (var4 instanceof class_1743) {
            class_1743 axeItem = (class_1743)var4;
            baseDamage = 9.0F;
         }

         if (Module.mc.field_1724.field_6017 > 0.0F || ModuleManager.criticals.isEnabled()) {
            baseDamage += baseDamage / 2.0F;
         }

         if (Module.mc.field_1724.method_6059(class_1294.field_5910)) {
            int strength = ((class_1293)Objects.requireNonNull(Module.mc.field_1724.method_6112(class_1294.field_5910))).method_5578() + 1;
            baseDamage += (float)(3 * strength);
         }

         baseDamage = class_1280.method_5496(ent, baseDamage, Module.mc.field_1687.method_48963().method_48830(), (float)ent.method_6096(), (float)ent.method_5996(class_5134.field_23725).method_6194());
         return baseDamage;
      }
   }

   public static SearchInvResult findBedInHotBar() {
      if (Module.mc.field_1724 == null) {
         return SearchInvResult.notFound();
      } else {
         for(int b1 = 0; b1 < 9; ++b1) {
            class_1799 itemStack = Module.mc.field_1724.method_31548().method_5438(b1);
            if (itemStack != null && itemStack.method_7909() instanceof class_1748) {
               return new SearchInvResult(b1, true, Module.mc.field_1724.method_31548().method_5438(b1));
            }
         }

         return SearchInvResult.notFound();
      }
   }

   public static SearchInvResult findBed() {
      if (Module.mc.field_1724 == null) {
         return SearchInvResult.notFound();
      } else {
         for(int b1 = 9; b1 < 45; ++b1) {
            class_1799 itemStack = Module.mc.field_1724.method_31548().method_5438(b1 >= 36 ? b1 - 36 : b1);
            if (itemStack != null && itemStack.method_7909() instanceof class_1748) {
               return new SearchInvResult(b1, true, Module.mc.field_1724.method_31548().method_5438(b1));
            }
         }

         return SearchInvResult.notFound();
      }
   }

   public static class_1792 getItem(String Name) {
      if (Name == null) {
         return class_1802.field_8162;
      } else {
         Iterator var1 = class_7923.field_41175.iterator();

         class_2248 block;
         do {
            if (!var1.hasNext()) {
               var1 = class_7923.field_41178.iterator();

               class_1792 item;
               do {
                  if (!var1.hasNext()) {
                     return class_1802.field_8831;
                  }

                  item = (class_1792)var1.next();
               } while(!item.method_7876().replace("item.minecraft.", "").equals(Name.toLowerCase()));

               return item;
            }

            block = (class_2248)var1.next();
         } while(!block.method_9539().replace("block.minecraft.", "").equals(Name.toLowerCase()));

         return class_1792.method_7867(block);
      }
   }

   public static int getBedsCount() {
      if (Module.mc.field_1724 == null) {
         return 0;
      } else {
         int counter = 0;

         for(int i = 0; i <= 44; ++i) {
            class_1799 itemStack = Module.mc.field_1724.method_31548().method_5438(i);
            if (itemStack.method_7909() instanceof class_1748) {
               counter += itemStack.method_7947();
            }
         }

         return counter;
      }
   }

   public interface Searcher {
      boolean isValid(class_1799 var1);
   }
}
