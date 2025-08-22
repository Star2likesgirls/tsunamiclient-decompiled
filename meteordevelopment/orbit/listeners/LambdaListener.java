package meteordevelopment.orbit.listeners;

import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.function.Consumer;
import meteordevelopment.orbit.EventHandler;

public class LambdaListener implements IListener {
   private static boolean isJava1dot8;
   private static Constructor<Lookup> lookupConstructor;
   private static Method privateLookupInMethod;
   private final Class<?> target;
   private final boolean isStatic;
   private final int priority;
   private Consumer<Object> executor;

   public LambdaListener(LambdaListener.Factory factory, Class<?> klass, Object object, Method method) {
      this.target = method.getParameters()[0].getType();
      this.isStatic = Modifier.isStatic(method.getModifiers());
      this.priority = ((EventHandler)method.getAnnotation(EventHandler.class)).priority();

      try {
         String name = method.getName();
         Lookup lookup;
         if (isJava1dot8) {
            boolean a = lookupConstructor.isAccessible();
            lookupConstructor.setAccessible(true);
            lookup = (Lookup)lookupConstructor.newInstance(klass);
            lookupConstructor.setAccessible(a);
         } else {
            lookup = factory.create(privateLookupInMethod, klass);
         }

         MethodType methodType = MethodType.methodType(Void.TYPE, method.getParameters()[0].getType());
         MethodHandle methodHandle;
         MethodType invokedType;
         if (this.isStatic) {
            methodHandle = lookup.findStatic(klass, name, methodType);
            invokedType = MethodType.methodType(Consumer.class);
         } else {
            methodHandle = lookup.findVirtual(klass, name, methodType);
            invokedType = MethodType.methodType(Consumer.class, klass);
         }

         MethodHandle lambdaFactory = LambdaMetafactory.metafactory(lookup, "accept", invokedType, MethodType.methodType(Void.TYPE, Object.class), methodHandle, methodType).getTarget();
         if (this.isStatic) {
            this.executor = lambdaFactory.invoke();
         } else {
            this.executor = lambdaFactory.invoke(object);
         }
      } catch (Throwable var11) {
         var11.printStackTrace();
      }

   }

   public void call(Object event) {
      this.executor.accept(event);
   }

   public Class<?> getTarget() {
      return this.target;
   }

   public int getPriority() {
      return this.priority;
   }

   public boolean isStatic() {
      return this.isStatic;
   }

   static {
      try {
         isJava1dot8 = System.getProperty("java.version").startsWith("1.8");
         if (isJava1dot8) {
            lookupConstructor = Lookup.class.getDeclaredConstructor(Class.class);
         } else {
            privateLookupInMethod = MethodHandles.class.getDeclaredMethod("privateLookupIn", Class.class, Lookup.class);
         }
      } catch (NoSuchMethodException var1) {
         var1.printStackTrace();
      }

   }

   public interface Factory {
      Lookup create(Method var1, Class<?> var2) throws InvocationTargetException, IllegalAccessException;
   }
}
