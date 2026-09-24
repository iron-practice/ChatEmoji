package org.ramki.chatemoji;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.ramki.chatemoji.commands.EmojiCommand;
import org.ramki.chatemoji.listeners.ChatListener;
import org.ramki.chatemoji.listeners.PlayerListener;
import org.ramki.chatemoji.papi.ChatEmojiExpansion;

public class ChatEmoji extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getConfig();

        Bukkit.getPluginManager().registerEvents(new ChatListener(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerListener(this), this);

        getCommand("emoji").setExecutor(new EmojiCommand(this));
        getCommand("emoji").setTabCompleter(new EmojiCommand(this));

        //PlaceholderAPI expansion register
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI"))
            new ChatEmojiExpansion(this).register();
    }

    @Override
    public void onDisable() {

    }

}
