package tsunami.core.manager.world;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import tsunami.core.manager.IManager;

public class WayPointManager implements IManager {
   private static CopyOnWriteArrayList<WayPointManager.WayPoint> wayPoints = new CopyOnWriteArrayList();

   public void addWayPoint(WayPointManager.WayPoint wp) {
      if (!wayPoints.contains(wp)) {
         wayPoints.add(wp);
      }

   }

   public void onLoad() {
      wayPoints = new CopyOnWriteArrayList();

      try {
         File file = new File("ThunderHackRecode/misc/waypoints.txt");
         if (file.exists()) {
            BufferedReader reader = new BufferedReader(new FileReader(file));

            try {
               while(reader.ready()) {
                  String[] line = reader.readLine().split(":");
                  String x = line[0];
                  String y = line[1];
                  String z = line[2];
                  String name = line[3];
                  String server = line[4];
                  String dimension = line.length == 6 ? line[5] : "overworld";
                  this.addWayPoint(new WayPointManager.WayPoint(Integer.parseInt(x), Integer.parseInt(y), Integer.parseInt(z), name, server, dimension));
               }
            } catch (Throwable var11) {
               try {
                  reader.close();
               } catch (Throwable var10) {
                  var11.addSuppressed(var10);
               }

               throw var11;
            }

            reader.close();
         }
      } catch (Exception var12) {
      }

   }

   public void saveWayPoints() {
      File file = new File("ThunderHackRecode/misc/waypoints.txt");

      try {
         (new File("ThunderHackRecode")).mkdirs();
         file.createNewFile();
      } catch (Exception var6) {
      }

      try {
         BufferedWriter writer = new BufferedWriter(new FileWriter(file));

         try {
            Iterator var3 = wayPoints.iterator();

            while(var3.hasNext()) {
               WayPointManager.WayPoint wayPoint = (WayPointManager.WayPoint)var3.next();
               writer.write(wayPoint.x + ":" + wayPoint.y + ":" + wayPoint.z + ":" + wayPoint.name + ":" + wayPoint.server + ":" + wayPoint.dimension + "\n");
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

   public void removeWayPoint(WayPointManager.WayPoint macro) {
      wayPoints.remove(macro);
   }

   public CopyOnWriteArrayList<WayPointManager.WayPoint> getWayPoints() {
      return wayPoints;
   }

   public WayPointManager.WayPoint getWayPointByName(String name) {
      Iterator var2 = this.getWayPoints().iterator();

      WayPointManager.WayPoint wayPoint;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         wayPoint = (WayPointManager.WayPoint)var2.next();
      } while(!wayPoint.name.equalsIgnoreCase(name));

      return wayPoint;
   }

   public static class WayPoint {
      private int x;
      private int y;
      private int z;
      private String name;
      private String server;
      private String dimension;

      public WayPoint(int x, int y, int z, String name, String server, String dimension) {
         this.x = x;
         this.y = y;
         this.z = z;
         this.name = name;
         this.server = server;
         this.dimension = dimension;
      }

      public int getX() {
         return this.x;
      }

      public void setX(int x) {
         this.x = x;
      }

      public int getY() {
         return this.y;
      }

      public void setY(int y) {
         this.y = y;
      }

      public int getZ() {
         return this.z;
      }

      public void setZ(int z) {
         this.z = z;
      }

      public String getName() {
         return this.name;
      }

      public void setName(String name) {
         this.name = name;
      }

      public String getServer() {
         return this.server;
      }

      public void setServer(String server) {
         this.server = server;
      }

      public String getDimension() {
         return this.dimension;
      }

      public void setDimension(String dimension) {
         this.dimension = dimension;
      }
   }
}
