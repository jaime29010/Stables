package com.hemmingfield.stables.command.type;

import org.bukkit.entity.Player;

public abstract class Game {
    public abstract String getName();

    public abstract String getDescription();

    public abstract String getSyntax();

    public abstract String getPermission();

    public abstract void perform(Player paramPlayer, String[] paramArrayOfString);
}



