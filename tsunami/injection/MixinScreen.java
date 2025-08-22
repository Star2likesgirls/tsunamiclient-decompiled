package tsunami.injection;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.logging.LogUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import net.minecraft.class_2583;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_437;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tsunami.core.Managers;
import tsunami.core.manager.client.CommandManager;
import tsunami.core.manager.client.ModuleManager;
import tsunami.core.manager.client.ProxyManager;
import tsunami.events.impl.ClientClickEvent;
import tsunami.features.modules.Module;
import tsunami.features.modules.client.ClientSettings;
import tsunami.gui.misc.DialogScreen;
import tsunami.utility.math.MathUtility;
import tsunami.utility.render.Render2DEngine;
import tsunami.utility.render.TextureStorage;

@Mixin({class_437.class})
public abstract class MixinScreen {
   @Shadow
   public abstract void method_25423(class_310 var1, int var2, int var3);

   @Inject(
      method = {"handleTextClick"},
      at = {@At(
   value = "INVOKE",
   target = "Lorg/slf4j/Logger;error(Ljava/lang/String;Ljava/lang/Object;)V",
   ordinal = 1,
   remap = false
)},
      cancellable = true
   )
   private void onRunCommand(class_2583 style, CallbackInfoReturnable<Boolean> cir) {
      Object var4 = Objects.requireNonNull(style.method_10970());
      if (var4 instanceof ClientClickEvent) {
         ClientClickEvent clientClickEvent = (ClientClickEvent)var4;
         if (clientClickEvent.method_10844().startsWith(Managers.COMMAND.getPrefix())) {
            try {
               CommandManager manager = Managers.COMMAND;
               manager.getDispatcher().execute(style.method_10970().method_10844().substring(Managers.COMMAND.getPrefix().length()), manager.getSource());
               cir.setReturnValue(true);
            } catch (CommandSyntaxException var5) {
            }
         }
      }

   }

   @Inject(
      method = {"filesDragged"},
      at = {@At("HEAD")}
   )
   public void filesDragged(List<Path> paths, CallbackInfo ci) {
      String configPath = ((Path)paths.get(0)).toString();
      File cfgFile = new File(configPath);
      String fileName = cfgFile.getName();
      DialogScreen dialogScreen2;
      if (fileName.contains(".th")) {
         dialogScreen2 = new DialogScreen(TextureStorage.questionPic, ClientSettings.isRu() ? "Обнаружен конфиг!" : "Config detected!", ClientSettings.isRu() ? "Ты действительно хочешь загрузить " + fileName + "?" : "Are you sure you want to load " + fileName + "?", ClientSettings.isRu() ? "Да" : "Yes", ClientSettings.isRu() ? "Нет" : "No", () -> {
            Managers.MODULE.onUnload("none");
            Managers.CONFIG.load(cfgFile);
            Managers.MODULE.onLoad("none");
            Module.mc.method_1507((class_437)null);
         }, () -> {
            Module.mc.method_1507((class_437)null);
         });
         Module.mc.method_1507(dialogScreen2);
      } else if (fileName.contains(".txt")) {
         dialogScreen2 = new DialogScreen(TextureStorage.questionPic, ClientSettings.isRu() ? "Обнаружен текстовый файл!" : "Text file detected!", ClientSettings.isRu() ? "Импортировать файл " + fileName + " как" : "Import file " + fileName + " as", ClientSettings.isRu() ? "Прокси" : "Proxies", ClientSettings.isRu() ? "Забить" : "Cancel", () -> {
            try {
               BufferedReader reader = new BufferedReader(new FileReader(cfgFile));

               String ip;
               String login;
               String password;
               int p;
               try {
                  for(; reader.ready(); Managers.PROXY.addProxy(new ProxyManager.ThProxy("Proxy" + (int)MathUtility.random(0.0F, 10000.0F), ip, p, login, password))) {
                     String[] line = reader.readLine().split(":");
                     ip = line[0];
                     String port = line[1];
                     login = line[2];
                     password = line[3];
                     p = 80;

                     try {
                        p = Integer.parseInt(port);
                     } catch (Exception var10) {
                        LogUtils.getLogger().warn(var10.getMessage());
                     }
                  }
               } catch (Throwable var11) {
                  try {
                     reader.close();
                  } catch (Throwable var9) {
                     var11.addSuppressed(var9);
                  }

                  throw var11;
               }

               reader.close();
            } catch (Exception var12) {
            }

            Module.mc.method_1507((class_437)null);
         }, () -> {
            Module.mc.method_1507((class_437)null);
         });
         Module.mc.method_1507(dialogScreen2);
      }

   }

   @Inject(
      method = {"renderPanoramaBackground"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void renderPanoramaBackgroundHook(class_332 context, float delta, CallbackInfo ci) {
      if ((Boolean)ClientSettings.customPanorama.getValue() && Module.mc.field_1687 == null) {
         ci.cancel();
         Render2DEngine.drawMainMenuShader(context.method_51448(), 0.0F, 0.0F, (float)Module.mc.method_22683().method_4486(), (float)Module.mc.method_22683().method_4502());
      }

   }

   @Inject(
      method = {"renderInGameBackground"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void renderInGameBackground(CallbackInfo info) {
      if (ModuleManager.noRender.isEnabled() && (Boolean)ModuleManager.noRender.disableGuiBackGround.getValue()) {
         info.cancel();
      }

   }

   @Inject(
      method = {"renderBackground(Lnet/minecraft/client/gui/DrawContext;IIF)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void onRenderBackground(class_332 context, int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
      if (ModuleManager.noRender.isEnabled() && (Boolean)ModuleManager.noRender.disableGuiBackGround.getValue() && Module.mc.field_1687 != null) {
         ci.cancel();
      }

   }
}
