package tsunami.utility.render.shaders;

import com.mojang.blaze3d.systems.RenderSystem;
import java.util.Objects;
import net.minecraft.class_290;
import net.minecraft.class_2960;
import tsunami.features.modules.Module;
import tsunami.utility.render.animation.AnimationUtility;
import tsunami.utility.render.shaders.satin.api.managed.ManagedCoreShader;
import tsunami.utility.render.shaders.satin.api.managed.ShaderEffectManager;
import tsunami.utility.render.shaders.satin.api.managed.uniform.Uniform1f;
import tsunami.utility.render.shaders.satin.api.managed.uniform.Uniform2f;
import tsunami.utility.render.shaders.satin.api.managed.uniform.Uniform4f;

public class MainMenuProgram {
   private Uniform1f Time;
   private Uniform2f uSize;
   private Uniform4f color;
   public static float time_ = 10000.0F;
   public static final ManagedCoreShader MAIN_MENU;

   public MainMenuProgram() {
      this.setup();
   }

   public void setParameters(float x, float y, float width, float height) {
      float i = (float)Module.mc.method_22683().method_4495();
      this.uSize.set(width * i, height * i);
      time_ += (float)(0.55D * (double)AnimationUtility.deltaTime());
      this.Time.set(time_);
   }

   public void use() {
      ManagedCoreShader var10000 = MAIN_MENU;
      Objects.requireNonNull(var10000);
      RenderSystem.setShader(var10000::getProgram);
   }

   protected void setup() {
      this.uSize = MAIN_MENU.findUniform2f("uSize");
      this.Time = MAIN_MENU.findUniform1f("Time");
      this.color = MAIN_MENU.findUniform4f("color");
   }

   static {
      MAIN_MENU = ShaderEffectManager.getInstance().manageCoreShader(class_2960.method_60655("thunderhack", "mainmenu"), class_290.field_1592);
   }
}
