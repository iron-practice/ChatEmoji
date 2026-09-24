package org.raammi.chatemoji;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.raammi.chatemoji.api.ChatEmojiAPI;
import org.raammi.chatemoji.commands.EmojiCommand;
import org.raammi.chatemoji.listeners.ChatListener;
import org.raammi.chatemoji.listeners.PlayerListener;
import org.raammi.chatemoji.papi.ChatEmojiExpansion;

public class ChatEmoji extends JavaPlugin {

    private static ChatEmoji instance;

    public ChatEmoji() {
        ChatEmoji.instance = this;
    }

    @Override
    public void onEnable() {
        ChatEmojiAPI.init(this);
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
        ChatEmojiAPI.disable(this);
    }

}
