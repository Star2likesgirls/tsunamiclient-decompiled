package tsunami.core.manager.client;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.mojang.logging.LogUtils;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import tsunami.TsunamiClient;
import tsunami.core.Managers;
import tsunami.core.manager.IManager;
import tsunami.features.cmd.Command;
import tsunami.features.modules.Module;
import tsunami.features.modules.client.ClientSettings;
import tsunami.setting.Setting;
import tsunami.setting.impl.Bind;
import tsunami.setting.impl.BooleanSettingGroup;
import tsunami.setting.impl.ColorSetting;
import tsunami.setting.impl.EnumConverter;
import tsunami.setting.impl.ItemSelectSetting;
import tsunami.setting.impl.PositionSetting;
import tsunami.setting.impl.SettingGroup;

public class ConfigManager implements IManager {
   public static final String CONFIG_FOLDER_NAME = "ThunderHackRecode";
   public static final File MAIN_FOLDER;
   public static final File CONFIGS_FOLDER;
   public static final File TEMP_FOLDER;
   public static final File MISC_FOLDER;
   public static final File SOUNDS_FOLDER;
   public static final File IMAGES_FOLDER;
   public static final File TABPARSER_FOLDER;
   public static final File STASHLOGGER_FOLDER;
   public File currentConfig = null;
   public static boolean firstLaunch;

   public ConfigManager() {
      firstLaunch = !MAIN_FOLDER.exists();
      this.createDirs(MAIN_FOLDER, CONFIGS_FOLDER, TEMP_FOLDER, MISC_FOLDER, SOUNDS_FOLDER, IMAGES_FOLDER, TABPARSER_FOLDER, STASHLOGGER_FOLDER);
   }

   private void createDirs(File... dirs) {
      File[] var2 = dirs;
      int var3 = dirs.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         File dir = var2[var4];
         if (!dir.exists()) {
            dir.mkdirs();
         }
      }

   }

   @NotNull
   public static String getConfigDate(String name) {
      File file = new File(CONFIGS_FOLDER, name + ".th");
      return !file.exists() ? "none" : (new SimpleDateFormat("dd MMM yyyy HH:mm")).format(new Date(file.lastModified()));
   }

   public void load(String name, String category) {
      File file = new File(CONFIGS_FOLDER, name + ".th");
      if (!file.exists()) {
         Command.sendMessage(ClientSettings.isRu() ? "Конфига " + name + " не существует!" : "Config " + name + " does not exist!");
      } else {
         if (this.currentConfig != null) {
            this.save(this.currentConfig);
         }

         Managers.MODULE.onUnload(category);
         this.load(file, category);
         Managers.MODULE.onLoad(category);
      }
   }

   public void loadBinds(String name) {
      File file = new File(CONFIGS_FOLDER, name + ".th");
      if (!file.exists()) {
         Command.sendMessage(ClientSettings.isRu() ? "Конфига " + name + " не существует!" : "Config " + name + " does not exist!");
      } else {
         if (this.currentConfig != null) {
            this.save(this.currentConfig);
         }

         this.loadBinds(file);
      }
   }

   private void loadBinds(@NotNull File config) {
      if (!config.exists()) {
         this.save(config);
      }

      try {
         FileReader reader = new FileReader(config, StandardCharsets.UTF_8);

         try {
            JsonObject modulesObject = JsonParser.parseReader(reader).getAsJsonArray().get(0).getAsJsonObject();
            JsonArray modules = modulesObject.getAsJsonArray("Modules");
            if (modules != null) {
               Iterator var5 = modules.iterator();

               while(var5.hasNext()) {
                  JsonElement element = (JsonElement)var5.next();
                  this.parseBinds(element.getAsJsonObject());
               }
            }

            Command.sendMessage(ClientSettings.isRu() ? "Загружены бинды с конфига: " + config.getName() : "Loaded bind from config: " + config.getName());
         } catch (Throwable var8) {
            try {
               reader.close();
            } catch (Throwable var7) {
               var8.addSuppressed(var7);
            }

            throw var8;
         }

         reader.close();
      } catch (IOException var9) {
         LogUtils.getLogger().warn(var9.getMessage());
      }

      this.saveCurrentConfig();
   }

   public void load(String name) {
      File file = new File(CONFIGS_FOLDER, name + ".th");
      if (!file.exists()) {
         Command.sendMessage(ClientSettings.isRu() ? "Конфига " + name + " не существует!" : "Config " + name + " does not exist!");
      } else {
         if (this.currentConfig != null) {
            this.save(this.currentConfig);
         }

         Managers.MODULE.onUnload("none");
         this.load(file);
         Managers.MODULE.onLoad("none");
      }
   }

   public void loadCloud(String name) {
      Command.sendMessage(ClientSettings.isRu() ? "Загружаю.." : "Downloading..");

      try {
         BufferedInputStream in = new BufferedInputStream((new URL("https://raw.githubusercontent.com/Pan4ur/THRecodeUtil/main/configs/" + name + ".th")).openStream());

         try {
            FileOutputStream fileOutputStream = new FileOutputStream(new File(CONFIGS_FOLDER, name + ".th"));

            try {
               byte[] dataBuffer = new byte[1024];

               int bytesRead;
               while((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                  fileOutputStream.write(dataBuffer, 0, bytesRead);
               }

               Command.sendMessage(ClientSettings.isRu() ? "Загрузил!" : "Downloaded!");
               this.load(name);
            } catch (Throwable var8) {
               try {
                  fileOutputStream.close();
               } catch (Throwable var7) {
                  var8.addSuppressed(var7);
               }

               throw var8;
            }

            fileOutputStream.close();
         } catch (Throwable var9) {
            try {
               in.close();
            } catch (Throwable var6) {
               var9.addSuppressed(var6);
            }

            throw var9;
         }

         in.close();
      } catch (Exception var10) {
         Command.sendMessage(ClientSettings.isRu() ? "Произошла ошибка при загрузке! Может название неправильное?" : "There was an error downloading! Maybe the name is wrong?");
      }

   }

   public void loadModuleOnly(String name, Module module) {
      File file = new File(CONFIGS_FOLDER, name + ".th");
      if (!file.exists()) {
         Command.sendMessage(ClientSettings.isRu() ? "Конфига " + name + " не существует!" : "Config " + name + " does not exist!");
      } else {
         if (module.isEnabled()) {
            TsunamiClient.EVENT_BUS.unsubscribe((Object)module);
            module.setEnabled(false);
         }

         this.loadModuleOnly(file, module);
         if (module.isEnabled()) {
            TsunamiClient.EVENT_BUS.subscribe((Object)module);
         }

      }
   }

   public void load(@NotNull File config) {
      this.load(config, "none");
   }

   private void load(@NotNull File config, String category) {
      if (!config.exists()) {
         this.save(config);
      }

      try {
         FileReader reader = new FileReader(config, StandardCharsets.UTF_8);
         JsonObject modulesObject = JsonParser.parseReader(reader).getAsJsonArray().get(0).getAsJsonObject();
         JsonArray modules = modulesObject.getAsJsonArray("Modules");
         if (modules != null) {
            Iterator var6 = modules.iterator();

            while(var6.hasNext()) {
               JsonElement element = (JsonElement)var6.next();
               this.parseModule(element.getAsJsonObject(), category);
            }
         }

         Command.sendMessage(ClientSettings.isRu() ? "Загружен конфиг " + config.getName() : "Loaded " + config.getName());
      } catch (Exception var8) {
         var8.printStackTrace();
      }

      if (Objects.equals(category, "none")) {
         this.currentConfig = config;
      }

      this.saveCurrentConfig();
   }

   public void loadModuleOnly(File config, Module module) {
      try {
         FileReader reader = new FileReader(config);

         try {
            JsonArray array = JsonParser.parseReader(reader).getAsJsonArray();
            JsonObject modulesObject = array.get(0).getAsJsonObject();
            JsonArray modules = modulesObject.getAsJsonArray("Modules");
            if (modules != null) {
               Iterator var7 = modules.iterator();

               while(var7.hasNext()) {
                  JsonElement element = (JsonElement)var7.next();
                  JsonObject moduleObject = element.getAsJsonObject();
                  Module loadedModule = (Module)Managers.MODULE.modules.stream().filter((m) -> {
                     return moduleObject.getAsJsonObject(m.getName()) != null;
                  }).findFirst().orElse((Object)null);
                  if (loadedModule != null && Objects.equals(module.getName(), loadedModule.getName())) {
                     this.parseModule(moduleObject, "none");
                  }
               }
            }

            Command.sendMessage(ClientSettings.isRu() ? "Загружен модуль " + module.getName() + " с конфига " + config.getName() : "Loaded " + module.getName() + " from " + config.getName());
         } catch (Throwable var12) {
            try {
               reader.close();
            } catch (Throwable var11) {
               var12.addSuppressed(var11);
            }

            throw var12;
         }

         reader.close();
      } catch (IOException var13) {
         LogUtils.getLogger().warn(var13.getMessage());
      }

   }

   public void save(String name) {
      File file = new File(CONFIGS_FOLDER, name + ".th");
      if (file.exists()) {
         Command.sendMessage(ClientSettings.isRu() ? "Перезаписываем " + name + "..." : "Overwriting " + name + "...");
         file.delete();
      } else {
         Command.sendMessage(ClientSettings.isRu() ? "Конфиг " + name + " успешно сохранен!" : "Config " + name + " successfully saved!");
      }

      this.save(file);
   }

   public void save(@NotNull File config) {
      try {
         if (!config.exists()) {
            config.createNewFile();
         }

         JsonArray array = new JsonArray();
         JsonObject modulesObj = new JsonObject();
         modulesObj.add("Modules", this.getModuleArray());
         array.add(modulesObj);
         FileWriter writer = new FileWriter(config, StandardCharsets.UTF_8);
         (new GsonBuilder()).setPrettyPrinting().create().toJson(array, writer);
         writer.close();
      } catch (IOException var5) {
         LogUtils.getLogger().warn(var5.getMessage());
      }

   }

   private void parseModule(JsonObject object, String category) throws NullPointerException {
      Module module = (Module)Managers.MODULE.modules.stream().filter((m) -> {
         return object.getAsJsonObject(m.getName()) != null;
      }).findFirst().orElse((Object)null);
      if (module != null) {
         if (Objects.equals(category, "none") || module.getCategory().getName().equalsIgnoreCase(category)) {
            JsonObject mobject = object.getAsJsonObject(module.getName());
            Iterator var5 = module.getSettings().iterator();

            while(var5.hasNext()) {
               Setting setting = (Setting)var5.next();

               try {
                  if (!(setting.getValue() instanceof SettingGroup)) {
                     if (setting.getValue() instanceof Boolean) {
                        setting.setValue(mobject.getAsJsonPrimitive(setting.getName()).getAsBoolean());
                     } else if (setting.getValue() instanceof Float) {
                        setting.setValue(mobject.getAsJsonPrimitive(setting.getName()).getAsFloat());
                     } else if (setting.getValue() instanceof Integer) {
                        setting.setValue(mobject.getAsJsonPrimitive(setting.getName()).getAsInt());
                     } else if (setting.getValue() instanceof String) {
                        setting.setValue(mobject.getAsJsonPrimitive(setting.getName()).getAsString().replace("%%", " ").replace("++", "/"));
                     } else {
                        JsonArray array;
                        if (setting.getValue() instanceof Bind) {
                           array = mobject.getAsJsonArray(setting.getName());
                           if (array.get(0).getAsString().contains("M")) {
                              setting.setValue(new Bind(Integer.parseInt(array.get(0).getAsString().replace("M", "")), true, array.get(1).getAsBoolean()));
                           } else {
                              setting.setValue(new Bind(Integer.parseInt(array.get(0).getAsString()), false, array.get(1).getAsBoolean()));
                           }
                        } else {
                           Object var11 = setting.getValue();
                           if (var11 instanceof ColorSetting) {
                              ColorSetting colorSetting = (ColorSetting)var11;
                              array = mobject.getAsJsonArray(setting.getName());
                              colorSetting.setColor(array.get(0).getAsInt());
                              colorSetting.setRainbow(array.get(1).getAsBoolean());
                           } else {
                              var11 = setting.getValue();
                              if (var11 instanceof PositionSetting) {
                                 PositionSetting posSetting = (PositionSetting)var11;
                                 array = mobject.getAsJsonArray(setting.getName());
                                 posSetting.setX(array.get(0).getAsFloat());
                                 posSetting.setY(array.get(1).getAsFloat());
                              } else {
                                 var11 = setting.getValue();
                                 if (var11 instanceof BooleanSettingGroup) {
                                    BooleanSettingGroup bGroup = (BooleanSettingGroup)var11;
                                    bGroup.setEnabled(mobject.getAsJsonPrimitive(setting.getName()).getAsBoolean());
                                 } else {
                                    var11 = setting.getValue();
                                    if (var11 instanceof ItemSelectSetting) {
                                       ItemSelectSetting iSetting = (ItemSelectSetting)var11;
                                       array = mobject.getAsJsonArray(setting.getName());

                                       for(int i = 0; i < array.size(); ++i) {
                                          if (!iSetting.getItemsById().contains(array.get(i).getAsString())) {
                                             iSetting.getItemsById().add(array.get(i).getAsString());
                                          }
                                       }
                                    } else if (setting.getValue().getClass().isEnum()) {
                                       Enum value = (new EnumConverter(((Enum)setting.getValue()).getClass())).doBackward((JsonElement)mobject.getAsJsonPrimitive(setting.getName()));
                                       setting.setValue(value == null ? setting.getDefaultValue() : value);
                                    }
                                 }
                              }
                           }
                        }
                     }
                  }
               } catch (Exception var13) {
                  Logger var10000 = LogUtils.getLogger();
                  String var10001 = module.getName();
                  var10000.warn("[Thunderhack] Module: " + var10001 + " Setting: " + setting.getName() + " Error: ");
                  var13.printStackTrace();
               }
            }

         }
      }
   }

   private void parseBinds(JsonObject object) throws NullPointerException {
      Module module = (Module)Managers.MODULE.modules.stream().filter((m) -> {
         return object.getAsJsonObject(m.getName()) != null;
      }).findFirst().orElse((Object)null);
      if (module != null) {
         JsonObject mobject = object.getAsJsonObject(module.getName());
         Iterator var4 = module.getSettings().iterator();

         while(var4.hasNext()) {
            Setting setting = (Setting)var4.next();

            try {
               if (setting.getValue() instanceof Bind) {
                  JsonArray array = mobject.getAsJsonArray(setting.getName());
                  if (array.get(0).getAsString().contains("M")) {
                     setting.setValue(new Bind(Integer.parseInt(array.get(0).getAsString().replace("M", "")), true, array.get(1).getAsBoolean()));
                  } else {
                     setting.setValue(new Bind(Integer.parseInt(array.get(0).getAsString()), false, array.get(1).getAsBoolean()));
                  }
               }
            } catch (Exception var7) {
               Logger var10000 = LogUtils.getLogger();
               String var10001 = module.getName();
               var10000.warn("[Thunderhack] Module: " + var10001 + " Setting: " + setting.getName() + " Error: ");
               var7.printStackTrace();
            }
         }

      }
   }

   @NotNull
   private JsonArray getModuleArray() {
      JsonArray modulesArray = new JsonArray();
      Iterator var2 = Managers.MODULE.modules.iterator();

      while(var2.hasNext()) {
         Module m = (Module)var2.next();
         modulesArray.add(this.getModuleObject(m));
      }

      return modulesArray;
   }

   public JsonObject getModuleObject(@NotNull Module m) {
      JsonObject attribs = new JsonObject();
      JsonParser jp = new JsonParser();
      Iterator var4 = m.getSettings().iterator();

      while(true) {
         while(var4.hasNext()) {
            Setting setting = (Setting)var4.next();
            Object var12 = setting.getValue();
            JsonArray array;
            if (var12 instanceof ColorSetting) {
               ColorSetting color = (ColorSetting)var12;
               array = new JsonArray();
               array.add(new JsonPrimitive(color.getRawColor()));
               array.add(new JsonPrimitive(color.isRainbow()));
               attribs.add(setting.getName(), array);
            } else {
               var12 = setting.getValue();
               if (var12 instanceof PositionSetting) {
                  PositionSetting pos = (PositionSetting)var12;
                  array = new JsonArray();
                  array.add(new JsonPrimitive(pos.getX()));
                  array.add(new JsonPrimitive(pos.getY()));
                  attribs.add(setting.getName(), array);
               } else {
                  var12 = setting.getValue();
                  if (var12 instanceof BooleanSettingGroup) {
                     BooleanSettingGroup bGroup = (BooleanSettingGroup)var12;
                     attribs.add(setting.getName(), jp.parse(String.valueOf(bGroup.isEnabled())));
                  } else {
                     var12 = setting.getValue();
                     if (var12 instanceof Bind) {
                        Bind b = (Bind)var12;
                        array = new JsonArray();
                        if (b.isMouse()) {
                           array.add(jp.parse(b.getBind()));
                        } else {
                           array.add(new JsonPrimitive(b.getKey()));
                        }

                        array.add(new JsonPrimitive(b.isHold()));
                        attribs.add(setting.getName(), array);
                     } else {
                        var12 = setting.getValue();
                        if (var12 instanceof String) {
                           String str = (String)var12;

                           try {
                              attribs.add(setting.getName(), jp.parse(str.replace(" ", "%%").replace("/", "++")));
                           } catch (Exception var16) {
                           }
                        } else {
                           var12 = setting.getValue();
                           if (!(var12 instanceof ItemSelectSetting)) {
                              if (setting.isEnumSetting()) {
                                 attribs.add(setting.getName(), (new EnumConverter(((Enum)setting.getValue()).getClass())).doForward((Enum)setting.getValue()));
                              } else {
                                 try {
                                    attribs.add(setting.getName(), jp.parse(setting.getValue().toString()));
                                 } catch (Exception var15) {
                                 }
                              }
                           } else {
                              ItemSelectSetting iSelect = (ItemSelectSetting)var12;
                              array = new JsonArray();
                              Iterator var13 = iSelect.getItemsById().iterator();

                              while(var13.hasNext()) {
                                 String id = (String)var13.next();
                                 array.add(new JsonPrimitive(id));
                              }

                              attribs.add(setting.getName(), array);
                           }
                        }
                     }
                  }
               }
            }
         }

         JsonObject moduleObject = new JsonObject();
         moduleObject.add(m.getName(), attribs);
         return moduleObject;
      }
   }

   public void delete(@NotNull File file) {
      file.delete();
   }

   public void delete(String name) {
      File file = new File(CONFIGS_FOLDER, name + ".th");
      if (file.exists()) {
         this.delete(file);
      }
   }

   public List<String> getConfigList() {
      if (MAIN_FOLDER.exists() && MAIN_FOLDER.listFiles() != null) {
         List<String> list = new ArrayList();
         if (CONFIGS_FOLDER.listFiles() != null) {
            Iterator var2 = Arrays.stream((File[])Objects.requireNonNull(CONFIGS_FOLDER.listFiles())).filter((f) -> {
               return f.getName().endsWith(".th");
            }).toList().iterator();

            while(var2.hasNext()) {
               File file = (File)var2.next();
               list.add(file.getName().replace(".th", ""));
            }
         }

         return list;
      } else {
         return null;
      }
   }

   public List<String> getCloudConfigs() {
      ArrayList list = new ArrayList();

      try {
         URL url = new URL("https://raw.githubusercontent.com/Pan4ur/THRecodeUtil/main/cloudConfigs.txt");
         BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

         String inputLine;
         while((inputLine = in.readLine()) != null) {
            list.add(inputLine.trim());
         }
      } catch (Exception var5) {
      }

      return list;
   }

   public void saveCurrentConfig() {
      File file = new File("ThunderHackRecode/misc/currentcfg.txt");

      try {
         FileWriter writer;
         if (file.exists()) {
            writer = new FileWriter(file);
            writer.write(this.currentConfig.getName().replace(".th", ""));
            writer.close();
         } else {
            file.createNewFile();
            writer = new FileWriter(file);
            writer.write(this.currentConfig.getName().replace(".th", ""));
            writer.close();
         }
      } catch (Exception var3) {
         LogUtils.getLogger().warn(var3.getMessage());
      }

   }

   public File getCurrentConfig() {
      File file = new File("ThunderHackRecode/misc/currentcfg.txt");
      String name = "config";

      try {
         if (file.exists()) {
            Scanner reader;
            for(reader = new Scanner(file); reader.hasNextLine(); name = reader.nextLine()) {
            }

            reader.close();
         }
      } catch (Exception var4) {
         LogUtils.getLogger().warn(var4.getMessage());
      }

      this.currentConfig = new File(CONFIGS_FOLDER, name + ".th");
      return this.currentConfig;
   }

   static {
      MAIN_FOLDER = new File(mc.field_1697, "ThunderHackRecode");
      CONFIGS_FOLDER = new File(MAIN_FOLDER, "configs");
      TEMP_FOLDER = new File(MAIN_FOLDER, "temp");
      MISC_FOLDER = new File(MAIN_FOLDER, "misc");
      SOUNDS_FOLDER = new File(MISC_FOLDER, "sounds");
      IMAGES_FOLDER = new File(MISC_FOLDER, "images");
      TABPARSER_FOLDER = new File(MISC_FOLDER, "tabparser");
      STASHLOGGER_FOLDER = new File(MISC_FOLDER, "stashlogger");
      firstLaunch = false;
   }
}
