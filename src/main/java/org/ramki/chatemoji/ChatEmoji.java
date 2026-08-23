package org.ramki.chatemoji;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.ramki.chatemoji.commands.EmojiCommand;
import org.ramki.chatemoji.listeners.ChatListener;

public class ChatEmoji extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getConfig();

        Bukkit.getPluginManager().registerEvents(new ChatListener(this), this);

        getCommand("emoji").setExecutor(new EmojiCommand(this));
        getCommand("emoji").setTabCompleter(new EmojiCommand(this));
    }

    @Override
    public void onDisable() {

    }

}
