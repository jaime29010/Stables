package com.hemmingfield.stables.command;

import com.hemmingfield.stables.command.commands.CreateStable;
import com.hemmingfield.stables.command.commands.SpawnMerchant;
import com.hemmingfield.stables.command.type.Console;
import com.hemmingfield.stables.command.type.Game;
import com.hemmingfield.stables.util.Language;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class CommandManager
        implements CommandExecutor {
    private ArrayList<Console> consoleCommands = new ArrayList<Console>();
    private ArrayList<Game> gameCommands = new ArrayList<Game>();

    public CommandManager() {
        this.gameCommands.add(new CreateStable());
        this.gameCommands.add(new SpawnMerchant());
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0) {
            ArrayList<String> args1 = new ArrayList<String>();
            for (String arg : args) {
                if (arg != args[0]) {
                    args1.add(arg);
                }
            }
            if ((sender instanceof Player)) {
                Player player = (Player) sender;
                for (Console command1 : this.consoleCommands) {
                    if (command1.getName().equalsIgnoreCase(args[0])) {
                        if (sender.hasPermission(command1.getPermission())) {
                            command1.perform(sender, args1.toArray(new String[args1.size()]));
                        } else {
                            Language.ERROR_INSUFFICIENT_PERMISSION.sendTo(sender);
                        }
                        return true;
                    }
                }
                for (Game command1 : this.gameCommands) {
                    if (command1.getName().equalsIgnoreCase(args[0])) {
                        if (player.hasPermission(command1.getPermission())) {
                            command1.perform(player, args1.toArray(new String[args1.size()]));
                        } else {
                            Language.ERROR_INSUFFICIENT_PERMISSION.sendTo(player);
                        }
                        return true;
                    }
                }
            }
        }
        displayUsage(sender);

        return true;
    }

    private void displayUsage(CommandSender sender) {
        for (Console command : this.consoleCommands) {
            Language.HELP_INFO.sendTo(sender, "%syntax%", command.getSyntax(), "%description%", command.getDescription(), "%permission%", command.getPermission());
        }
        for (Game command : this.gameCommands) {
            Language.HELP_INFO.sendTo(sender, "%syntax%", command.getSyntax(), "%description%", command.getDescription(), "%permission%", command.getPermission());
        }
    }
}



