package org.ramki.chatemoji.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.ramki.chatemoji.ChatEmoji;
import org.ramki.chatemoji.enums.AppleEmojis;
import org.ramki.chatemoji.enums.Emojis;

import java.util.List;

public class PlayerListener implements Listener {
    private ChatEmoji chatEmoji;
    public PlayerListener(ChatEmoji chatEmoji) { this.chatEmoji = chatEmoji; }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (chatEmoji.getConfig().getString("style").equalsIgnoreCase("default")) {
            for (Emojis emojis : Emojis.values()) {
                String key = emojis.getKey();

                e.getPlayer().addCustomChatCompletions(List.of(":" + key + ":"));
            }
        }

        if (chatEmoji.getConfig().getString("style").equalsIgnoreCase("apple")) {
            for (AppleEmojis appleEmojis : AppleEmojis.values()) {
                String key = appleEmojis.getKey();

                e.getPlayer().addCustomChatCompletions(List.of(":" + key + ":"));
            }
        }
    }

}
