package tsunami.core.manager.player;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.class_1657;
import net.minecraft.class_742;
import org.jetbrains.annotations.NotNull;
import tsunami.core.manager.IManager;

public class FriendManager implements IManager {
   public static List<String> friends = new ArrayList();

   public boolean isFriend(String name) {
      return friends.stream().anyMatch((friend) -> {
         return friend.equalsIgnoreCase(name);
      });
   }

   public boolean isFriend(@NotNull class_1657 player) {
      return this.isFriend(player.method_5477().getString());
   }

   public void removeFriend(String name) {
      friends.remove(name);
   }

   public void addFriend(String friend) {
      friends.add(friend);
   }

   public List<String> getFriends() {
      return friends;
   }

   public void clear() {
      friends.clear();
   }

   public List<class_742> getNearFriends() {
      return (List)(mc.field_1687 == null ? new ArrayList() : mc.field_1687.method_18456().stream().filter((player) -> {
         return friends.contains(player.method_5477().getString());
      }).toList());
   }

   public void saveFriends() {
      File file = new File("ThunderHackRecode/misc/friends.txt");

      try {
         file.createNewFile();
      } catch (Exception var6) {
      }

      try {
         BufferedWriter writer = new BufferedWriter(new FileWriter(file));

         try {
            Iterator var3 = friends.iterator();

            while(var3.hasNext()) {
               String friend = (String)var3.next();
               writer.write(friend + "\n");
            }
         } catch (Throwable var7) {
            try {
               writer.close();
            } catch (Throwable var5) {
               var7.addSuppressed(var5);
            }

            throw var7;
         }

         writer.close();
      } catch (Exception var8) {
      }

   }

   public void loadFriends() {
      try {
         File file = new File("ThunderHackRecode/misc/friends.txt");
         if (file.exists()) {
            BufferedReader reader = new BufferedReader(new FileReader(file));

            try {
               while(reader.ready()) {
                  friends.add(reader.readLine());
               }
            } catch (Throwable var6) {
               try {
                  reader.close();
               } catch (Throwable var5) {
                  var6.addSuppressed(var5);
               }

               throw var6;
            }

            reader.close();
         }
      } catch (Exception var7) {
      }

   }
}
