package com.hemmingfield.stables.command.type;

import org.bukkit.command.CommandSender;

public abstract class Console {
    public abstract String getName();

    public abstract String getDescription();

    public abstract String getSyntax();

    public abstract String getPermission();

    public abstract void perform(CommandSender paramCommandSender, String[] paramArrayOfString);
}



