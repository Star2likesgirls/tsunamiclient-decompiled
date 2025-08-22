package tsunami.gui.mainmenu;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import net.minecraft.class_1074;
import net.minecraft.class_156;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_429;
import net.minecraft.class_437;
import net.minecraft.class_442;
import net.minecraft.class_500;
import net.minecraft.class_526;
import org.jetbrains.annotations.NotNull;
import tsunami.api.IAddon;
import tsunami.core.Managers;
import tsunami.core.manager.client.ModuleManager;
import tsunami.features.modules.Module;
import tsunami.gui.font.FontRenderers;
import tsunami.utility.render.Render2DEngine;
import tsunami.utility.render.TextureStorage;

public class MainMenuScreen extends class_437 {
   private final List<MainMenuButton> buttons = new ArrayList();
   public boolean confirm = false;
   public static int ticksActive;
   private static MainMenuScreen INSTANCE = new MainMenuScreen();

   protected MainMenuScreen() {
      super(class_2561.method_30163("TsMainMenuScreen"));
      INSTANCE = this;
      this.buttons.add(new MainMenuButton(-110.0F, -70.0F, class_1074.method_4662("menu.singleplayer", new Object[0]).toUpperCase(Locale.ROOT), () -> {
         Module.mc.method_1507(new class_526(this));
      }));
      this.buttons.add(new MainMenuButton(4.0F, -70.0F, class_1074.method_4662("menu.multiplayer", new Object[0]).toUpperCase(Locale.ROOT), () -> {
         Module.mc.method_1507(new class_500(this));
      }));
      this.buttons.add(new MainMenuButton(-110.0F, -29.0F, class_1074.method_4662("menu.options", new Object[0]).toUpperCase(Locale.ROOT).replace(".", ""), () -> {
         Module.mc.method_1507(new class_429(this, Module.mc.field_1690));
      }));
      this.buttons.add(new MainMenuButton(4.0F, -29.0F, "CLICKGUI", () -> {
         ModuleManager.clickGui.setGui();
      }));
      List var10000 = this.buttons;
      String var10005 = class_1074.method_4662("menu.quit", new Object[0]).toUpperCase(Locale.ROOT);
      class_310 var10006 = Module.mc;
      Objects.requireNonNull(var10006);
      var10000.add(new MainMenuButton(-110.0F, 12.0F, var10005, var10006::method_1592, true));
   }

   public static MainMenuScreen getInstance() {
      ticksActive = 0;
      if (INSTANCE == null) {
         INSTANCE = new MainMenuScreen();
      }

      return INSTANCE;
   }

   public void method_25393() {
      ++ticksActive;
      if (ticksActive > 400) {
         ticksActive = 0;
      }

   }

   public void method_25394(@NotNull class_332 context, int mouseX, int mouseY, float delta) {
      float halfOfWidth = (float)Module.mc.method_22683().method_4486() / 2.0F;
      float halfOfHeight = (float)Module.mc.method_22683().method_4502() / 2.0F;
      float mainX = halfOfWidth - 120.0F;
      float mainY = halfOfHeight - 80.0F;
      float mainWidth = 240.0F;
      float mainHeight = 140.0F;
      this.method_25420(context, mouseX, mouseY, delta);
      Render2DEngine.drawHudBase(context.method_51448(), mainX, mainY, mainWidth, mainHeight, 20.0F);
      this.buttons.forEach((b) -> {
         b.onRender(context, (float)mouseX, (float)mouseY);
      });
      boolean hoveredLogo = Render2DEngine.isHovered((double)mouseX, (double)mouseY, (double)((int)(halfOfWidth - 120.0F)), (double)((int)(halfOfHeight - 130.0F)), 210.0D, 50.0D);
      FontRenderers.thglitchBig.drawCenteredString(context.method_51448(), "Tsunami Client", (double)((int)halfOfWidth), (double)((int)(halfOfHeight - 120.0F)), (new Color(255, 255, 255, hoveredLogo ? 230 : 180)).getRGB());
      boolean hovered = Render2DEngine.isHovered((double)mouseX, (double)mouseY, (double)(halfOfWidth - 50.0F), (double)(halfOfHeight + 70.0F), 100.0D, 10.0D);
      FontRenderers.sf_medium.drawCenteredString(context.method_51448(), "<-- Back to default menu", (double)halfOfWidth, (double)(halfOfHeight + 70.0F), hovered ? -1 : Render2DEngine.applyOpacity(-1, 0.6F));
      Render2DEngine.drawHudBase(context.method_51448(), (float)(Module.mc.method_22683().method_4486() - 40), (float)(Module.mc.method_22683().method_4502() - 40), 30.0F, 30.0F, 5.0F, Render2DEngine.isHovered((double)mouseX, (double)mouseY, (double)(Module.mc.method_22683().method_4486() - 40), (double)(Module.mc.method_22683().method_4502() - 40), 30.0D, 30.0D) ? 0.7F : 1.0F);
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, Render2DEngine.isHovered((double)mouseX, (double)mouseY, (double)(Module.mc.method_22683().method_4486() - 40), (double)(Module.mc.method_22683().method_4502() - 40), 30.0D, 30.0D) ? 0.7F : 1.0F);
      context.method_25293(TextureStorage.thTeam, Module.mc.method_22683().method_4486() - 40, Module.mc.method_22683().method_4502() - 40, 30, 30, 0.0F, 0.0F, 30, 30, 30, 30);
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      int totalAddonsLoaded = Managers.ADDON.getTotalAddons();
      int screenWidth = Module.mc.method_22683().method_4486();
      int offset = 0;

      for(Iterator var16 = Managers.ADDON.getAddons().iterator(); var16.hasNext(); offset += 9) {
         IAddon addon = (IAddon)var16.next();
      }

   }

   public boolean method_25402(double mouseX, double mouseY, int button) {
      float halfOfWidth = (float)Module.mc.method_22683().method_4486() / 2.0F;
      float halfOfHeight = (float)Module.mc.method_22683().method_4502() / 2.0F;
      this.buttons.forEach((b) -> {
         b.onClick((int)mouseX, (int)mouseY);
      });
      if (Render2DEngine.isHovered(mouseX, mouseY, (double)(halfOfWidth - 50.0F), (double)(halfOfHeight + 70.0F), 100.0D, 10.0D)) {
         this.confirm = true;
         Module.mc.method_1507(new class_442());
         this.confirm = false;
      }

      if (Render2DEngine.isHovered(mouseX, mouseY, (double)(Module.mc.method_22683().method_4486() - 40), (double)(Module.mc.method_22683().method_4502() - 40), 40.0D, 40.0D)) {
         class_156.method_668().method_673(URI.create("https://discord.gg/Z2aEmDua9u"));
      }

      if (Render2DEngine.isHovered(mouseX, mouseY, (double)((int)(halfOfWidth - 157.0F)), (double)((int)(halfOfHeight - 140.0F)), 300.0D, 70.0D)) {
         class_156.method_668().method_673(URI.create("https://discord.gg/Z2aEmDua9u"));
      }

      return super.method_25402(mouseX, mouseY, button);
   }
}
