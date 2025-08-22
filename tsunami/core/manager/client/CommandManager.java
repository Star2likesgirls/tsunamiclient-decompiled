package tsunami.core.manager.client;

import com.mojang.brigadier.CommandDispatcher;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.class_124;
import net.minecraft.class_2172;
import net.minecraft.class_310;
import net.minecraft.class_634;
import net.minecraft.class_637;
import org.jetbrains.annotations.NotNull;
import tsunami.core.manager.IManager;
import tsunami.features.cmd.Command;
import tsunami.features.cmd.impl.AddonsCommand;
import tsunami.features.cmd.impl.BenchMarkCommand;
import tsunami.features.cmd.impl.BindCommand;
import tsunami.features.cmd.impl.BlockESPCommand;
import tsunami.features.cmd.impl.CfgCommand;
import tsunami.features.cmd.impl.ChestStealerCommand;
import tsunami.features.cmd.impl.DrawCommand;
import tsunami.features.cmd.impl.DropAllCommand;
import tsunami.features.cmd.impl.EClipCommand;
import tsunami.features.cmd.impl.FriendCommand;
import tsunami.features.cmd.impl.GamemodeCommand;
import tsunami.features.cmd.impl.GarbageCleanerCommand;
import tsunami.features.cmd.impl.GetNbtCommand;
import tsunami.features.cmd.impl.GotoWaypointCommand;
import tsunami.features.cmd.impl.GpsCommand;
import tsunami.features.cmd.impl.HClipCommand;
import tsunami.features.cmd.impl.HelpCommand;
import tsunami.features.cmd.impl.HorseSpeedCommand;
import tsunami.features.cmd.impl.InvCleanerCommand;
import tsunami.features.cmd.impl.KitCommand;
import tsunami.features.cmd.impl.LoginCommand;
import tsunami.features.cmd.impl.MacroCommand;
import tsunami.features.cmd.impl.ModuleCommand;
import tsunami.features.cmd.impl.NukerCommand;
import tsunami.features.cmd.impl.OpenFolderCommand;
import tsunami.features.cmd.impl.PrefixCommand;
import tsunami.features.cmd.impl.RctCommand;
import tsunami.features.cmd.impl.ResetBindsCommand;
import tsunami.features.cmd.impl.RpcCommand;
import tsunami.features.cmd.impl.StaffCommand;
import tsunami.features.cmd.impl.TabParseCommand;
import tsunami.features.cmd.impl.TrackerCommand;
import tsunami.features.cmd.impl.TreasureCommand;
import tsunami.features.cmd.impl.VClipCommand;
import tsunami.features.cmd.impl.WayPointCommand;

public class CommandManager implements IManager {
   private String prefix = "@";
   private final CommandDispatcher<class_2172> dispatcher = new CommandDispatcher();
   private final class_2172 source = new class_637((class_634)null, class_310.method_1551());
   private final List<Command> commands = new ArrayList();

   public CommandManager() {
      this.add(new RpcCommand());
      this.add(new KitCommand());
      this.add(new GpsCommand());
      this.add(new CfgCommand());
      this.add(new RctCommand());
      this.add(new BindCommand());
      this.add(new DrawCommand());
      this.add(new HelpCommand());
      this.add(new NukerCommand());
      this.add(new EClipCommand());
      this.add(new HClipCommand());
      this.add(new LoginCommand());
      this.add(new MacroCommand());
      this.add(new StaffCommand());
      this.add(new VClipCommand());
      this.add(new AddonsCommand());
      this.add(new GetNbtCommand());
      this.add(new FriendCommand());
      this.add(new ModuleCommand());
      this.add(new PrefixCommand());
      this.add(new TrackerCommand());
      this.add(new GamemodeCommand());
      this.add(new DropAllCommand());
      this.add(new TreasureCommand());
      this.add(new WayPointCommand());
      this.add(new TabParseCommand());
      this.add(new BlockESPCommand());
      this.add(new BenchMarkCommand());
      this.add(new HorseSpeedCommand());
      this.add(new OpenFolderCommand());
      this.add(new ResetBindsCommand());
      this.add(new InvCleanerCommand());
      this.add(new GotoWaypointCommand());
      this.add(new ChestStealerCommand());
      this.add(new GarbageCleanerCommand());
   }

   private void add(@NotNull Command command) {
      command.register(this.dispatcher);
      this.commands.add(command);
   }

   public String getPrefix() {
      return this.prefix;
   }

   public void setPrefix(String prefix) {
      this.prefix = prefix;
   }

   public Command get(Class<? extends Command> commandClass) {
      Iterator var2 = this.commands.iterator();

      Command command;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         command = (Command)var2.next();
      } while(!command.getClass().equals(commandClass));

      return command;
   }

   @NotNull
   public static String getClientMessage() {
      String var10000 = String.valueOf(class_124.field_1068);
      return var10000 + "⌊" + String.valueOf(class_124.field_1065) + "⚡" + String.valueOf(class_124.field_1068) + "⌉" + String.valueOf(class_124.field_1070);
   }

   public List<Command> getCommands() {
      return this.commands;
   }

   public class_2172 getSource() {
      return this.source;
   }

   public CommandDispatcher<class_2172> getDispatcher() {
      return this.dispatcher;
   }

   public void registerCommand(Command command) {
      if (command != null) {
         command.register(this.dispatcher);
         this.commands.add(command);
      }
   }
}
