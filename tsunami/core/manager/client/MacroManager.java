package tsunami.core.manager.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import tsunami.core.manager.IManager;

public class MacroManager implements IManager {
   private static CopyOnWriteArrayList<MacroManager.Macro> macros = new CopyOnWriteArrayList();

   public static void addMacro(MacroManager.Macro macro) {
      if (!macros.contains(macro)) {
         macros.add(macro);
      }

   }

   public void onLoad() {
      macros = new CopyOnWriteArrayList();

      try {
         File file = new File("ThunderHackRecode/misc/macro.txt");
         if (file.exists()) {
            BufferedReader reader = new BufferedReader(new FileReader(file));

            try {
               while(reader.ready()) {
                  String[] nameKey = reader.readLine().split(":");
                  String name = nameKey[0];
                  String key = nameKey[1];
                  String command = nameKey[2];
                  addMacro(new MacroManager.Macro(name, command, Integer.parseInt(key)));
               }
            } catch (Throwable var8) {
               try {
                  reader.close();
               } catch (Throwable var7) {
                  var8.addSuppressed(var7);
               }

               throw var8;
            }

            reader.close();
         }
      } catch (Exception var9) {
      }

   }

   public void saveMacro() {
      File file = new File("ThunderHackRecode/misc/macro.txt");

      try {
         if ((new File("ThunderHackRecode")).mkdirs()) {
            file.createNewFile();
         }
      } catch (Exception var6) {
      }

      try {
         BufferedWriter writer = new BufferedWriter(new FileWriter(file));

         try {
            Iterator var3 = macros.iterator();

            while(var3.hasNext()) {
               MacroManager.Macro macro = (MacroManager.Macro)var3.next();
               writer.write(macro.name + ":" + macro.bind + ":" + macro.text + "\n");
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

   public void removeMacro(MacroManager.Macro macro) {
      macros.remove(macro);
   }

   public CopyOnWriteArrayList<MacroManager.Macro> getMacros() {
      return macros;
   }

   public MacroManager.Macro getMacroByName(String n) {
      Iterator var2 = this.getMacros().iterator();

      MacroManager.Macro m;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         m = (MacroManager.Macro)var2.next();
      } while(!m.name.equalsIgnoreCase(n));

      return m;
   }

   public static class Macro {
      private String name;
      private String text;
      private int bind;

      public Macro(String name, String text, int bind) {
         this.name = name;
         this.text = text;
         this.bind = bind;
      }

      public String getName() {
         return this.name;
      }

      public void setName(String name) {
         this.name = name;
      }

      public String getText() {
         return this.text;
      }

      public void setText(String text) {
         this.text = text;
      }

      public int getBind() {
         return this.bind;
      }

      public void setBind(int bind) {
         this.bind = bind;
      }

      public void runMacro() {
         if (IManager.mc.field_1724 != null) {
            if (this.text.contains("/")) {
               IManager.mc.field_1724.field_3944.method_45730(this.text.replace("/", ""));
            } else {
               IManager.mc.field_1724.field_3944.method_45729(this.text);
            }

         }
      }
   }
}
