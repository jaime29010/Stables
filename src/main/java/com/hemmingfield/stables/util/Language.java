package com.hemmingfield.stables.util;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.regex.Pattern;

public enum Language {
    ERROR_INCORRECT_SYNTAX("error-incorrect-syntax", "&cError: the syntax which you have used is incorrect, use %syntax% for correct usage."), ERROR_INSUFFICIENT_FUNDS("error-insufficient-fudns", "&cError: you do not have enough money to make that purchase."), ERROR_INSUFFICIENT_PERMISSION("error-insufficient-permission", "&cError: you are not permitted to perform this command."), ERROR_INVALID_ENTITY_TYPE("error-invalid-entity-type", "&cError: the entity type you have specified is invalid."), ERROR_INVALID_REGION("error-invalid-region", "&cError: A region with that name does not exist."), ERROR_NO_HORSE("error-no-horse", "&cError: you do not own a horse."), ERROR_NO_SELECTION("error-no-selection", "&cError: You must select a region using a diamond pickaxe before executing this command."), ERROR_OUTSIDE_STABLE("error-outside-stable", "&cError: a merchant can only be spawned inside a stable."), HELP_INFO("help-info", "&6%syntax%", "  &e&o%description%", "  &c%permission%"), HORSE_LOADED("horse-loaded", "&aSuccess: Your horse, with a speed of %speed% has been loaded."), HORSE_STORED("horse-stored", "&aSuccess: Your horse has been stored."), HORSE_INACTIVE_STORED("horse-inactive-stored", "&aSuccess: Your horse has been stored because you were 75 or more blocks away from it."), HORSE_NOT_OWN("horse-not-own", "&cError: that horse does not belong to you!"), MERCHANT_SPAWNED("merchant-spawned", "&aSuccess: a merchant has been spawned at your location."), SELECTION_ONE("selection-one", "&aYour first selection has been selected."), SELECTION_TWO("selection-two", "&aYour second selection has been selected."), STABLE_CREATED("stable-created", "&aSuccess: a stable has been created as your region selection.");

    private final String path;
    private String[] messages;

    Language(String path, String... messages) {
        this.path = path;
        setMessages(messages);
    }

    public static String replace(String message, String... args) {
        for (int i = 0; i + 2 <= args.length; i += 2) {
            message = message.replaceAll("(?i)" + Pattern.quote(args[i]), args[(i + 1)].replace("$", "\\$"));
        }
        return message;
    }

    public static boolean loadMessages(Plugin instance) {
        YamlConfiguration config = getMessageConfig(instance);
        if (config == null) {
            return false;
        }
        for (Language message : values()) {
            if (config.contains(message.getPath())) {
                List<String> configMessage = config.getStringList(message.getPath());
                String[] configMessageArray = new String[configMessage.size()];

                message.setMessages((String[]) configMessage.toArray(configMessageArray));
            }
        }
        saveMessages(instance);

        return true;
    }

    public static boolean saveMessages(Plugin instance) {
        YamlConfiguration config = getMessageConfig(instance);
        if (config == null) {
            return false;
        }
        for (Language message : values()) {
            config.set(message.getPath(), message.getMessages());
        }
        File file = new File(instance.getDataFolder(), "messages.yml");
        try {
            config.save(file);

            instance.getLogger().log(Level.INFO, "Saved the message config!");
        } catch (IOException ex) {
            instance.getLogger().log(Level.WARNING, "Could not save the message config!");
            instance.getLogger().log(Level.INFO, "Reason: " + ex.getLocalizedMessage());

            return false;
        }
        return true;
    }

    private static YamlConfiguration getMessageConfig(Plugin instance) {
        File file = new File(instance.getDataFolder(), "messages.yml");
        if (!file.exists()) {
            try {
                YamlConfiguration yc = new YamlConfiguration();
                for (Language message : values()) {
                    String[] messages = message.getMessages();
                    for (int i = 0; i < messages.length; i++) {
                        messages[i] = messages[i].replace("§", "&");
                    }
                    yc.set(message.getPath(), Arrays.asList(messages));
                }
                yc.save(file);

                instance.getLogger().log(Level.INFO, "Generated the message config!");
            } catch (IOException ex) {
                instance.getLogger().log(Level.WARNING, "Could not generate the message config!");
                instance.getLogger().log(Level.INFO, "Reason: " + ex.getLocalizedMessage());

                return null;
            }
        }
        return YamlConfiguration.loadConfiguration(file);
    }

    public String getPath() {
        return this.path;
    }

    public String getMessage() {
        return this.messages[0];
    }

    public String getMessage(String... args) {
        return replace(this.messages[0], args);
    }

    public String[] getMessages() {
        return this.messages;
    }

    public void setMessages(String... messages) {
        for (int i = 0; i < messages.length; i++) {
            messages[i] = ChatColor.translateAlternateColorCodes('&', messages[i]);
        }
        this.messages = messages;
    }

    public String[] getMessages(String... args) {
        String[] messages = this.messages;
        for (int i = 0; i < messages.length; i++) {
            messages[i] = replace(messages[i], args);
        }
        return messages;
    }

    public String toString() {
        return this.messages[0];
    }

    public void sendTo(CommandSender reciver) {
        if (reciver == null) {
            return;
        }
        for (String message : this.messages) {
            reciver.sendMessage(message);
        }
    }

    public void sendTo(CommandSender reciver, String... args) {
        if (reciver == null) {
            return;
        }
        for (String message : this.messages) {
            reciver.sendMessage(replace(message, args));
        }
    }

    public void sendTo(Player target, String... args) {
        if (target == null) {
            return;
        }
        for (String message : this.messages) {
            target.sendMessage(replace(message, args));
        }
    }
}



