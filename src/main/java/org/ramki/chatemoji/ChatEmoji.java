package org.ramki.chatemoji;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.ramki.chatemoji.listeners.ChatListener;

public class ChatEmoji extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();

        Bukkit.getPluginManager().registerEvents(new ChatListener(this), this);
    }

    @Override
    public void onDisable() {

    }

}
