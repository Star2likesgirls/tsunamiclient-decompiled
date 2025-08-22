package tsunami.gui.clickui;

import net.minecraft.class_332;
import tsunami.features.modules.Module;
import tsunami.utility.render.Render2DEngine;

public class AbstractCategory {
   private String name;
   public float animationY;
   protected float prevTargetX;
   protected float x;
   protected float y;
   protected float width;
   protected float height;
   protected float sx;
   protected float sy;
   private float prevX;
   private float prevY;
   protected boolean hovered;
   public boolean dragging;
   public float moduleOffset;
   private boolean open;

   public AbstractCategory(String name, float x, float y, float width, float height) {
      this.name = name;
      this.x = x;
      this.y = y;
      this.width = width;
      this.height = height;
      this.open = false;
   }

   public void init() {
   }

   public void render(class_332 context, int mouseX, int mouseY, float delta) {
      this.hovered = Render2DEngine.isHovered((double)mouseX, (double)mouseY, (double)this.x, (double)this.y, (double)this.width, (double)this.height);
      this.animationY = (float)interpolate((double)this.y, (double)this.animationY, 0.05D);
      if (this.dragging) {
         this.prevTargetX = this.x;
         this.x = this.prevX + (float)mouseX;
         this.y = this.prevY + (float)mouseY;
      } else {
         this.prevTargetX = this.x;
      }

   }

   public void mouseClicked(int mouseX, int mouseY, int button) {
      if (this.hovered && button == 0) {
         this.dragging = true;
         this.prevX = this.x - (float)mouseX;
         this.prevY = this.y - (float)mouseY;
      }

   }

   public static double interpolate(double current, double old, double scale) {
      return old + (current - old) * scale;
   }

   public void mouseReleased(int mouseX, int mouseY, int button) {
      if (button == 0) {
         this.dragging = false;
      }

   }

   public boolean keyTyped(int keyCode) {
      return true;
   }

   public void charTyped(char key, int modifier) {
   }

   public void onClose() {
   }

   public void setOpen(boolean open) {
      this.open = open;
   }

   public String getName() {
      return this.name;
   }

   public boolean isOpen() {
      return this.open;
   }

   public float getX() {
      return this.x;
   }

   public void setX(float x) {
      this.x = x;
   }

   public float getY() {
      return this.y;
   }

   public void setY(float y) {
      this.y = y;
   }

   public float getWidth() {
      return this.width;
   }

   public void setWidth(float width) {
      this.width = width;
   }

   public float getHeight() {
      return this.height;
   }

   public void setHeight(float height) {
      this.height = height;
   }

   public void setModuleOffset(float v, float mx, float my) {
      if (Render2DEngine.isHovered((double)mx, (double)my, (double)this.x, (double)this.y, (double)this.width, (double)(this.height + 1000.0F))) {
         this.moduleOffset += v;
      }

   }

   public void tick() {
   }

   public void hudClicked(Module module) {
   }

   public void savePos() {
      this.sx = this.x;
      this.sy = this.y;
   }

   public void restorePos() {
      this.x = this.sx;
      this.y = this.sy;
   }
}
