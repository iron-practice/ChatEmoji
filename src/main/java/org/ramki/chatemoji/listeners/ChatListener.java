package org.ramki.chatemoji.listeners;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.object.ObjectContents;
import net.kyori.adventure.text.object.PlayerHeadObjectContents;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.ramki.chatemoji.ChatEmoji;
import org.ramki.chatemoji.enums.AppleEmojis;
import org.ramki.chatemoji.enums.Emojis;

import java.util.HashMap;

public class ChatListener implements Listener {
    private ChatEmoji chatEmoji;
    public ChatListener(ChatEmoji chatEmoji) { this.chatEmoji = chatEmoji; }

    private HashMap<String, String> heads = new HashMap<>();

    @EventHandler (priority = EventPriority.LOW)
    public void onChat(AsyncChatEvent e) {
        final Player player = e.getPlayer();
        Component message = e.message();

        if (chatEmoji.getConfig().getString("style").equalsIgnoreCase("Default")) {
            if (chatEmoji.getConfig().getBoolean("permission") == true && (!player.hasPermission("chatemoji.use"))) {
                return;
            }

            for (Emojis emojis : Emojis.values()) {
                String key = emojis.getKey();
                String value = emojis.getValue();
                heads.put(key, value);
                PlayerHeadObjectContents fortnite = ObjectContents.playerHead()
                        .profileProperty(PlayerHeadObjectContents.property("textures", heads.get(key)))
                        .build();
                Component emojiHead = Component.object(fortnite);
                // Keysetter -> headComponent -> textreplacement
                TextReplacementConfig idk = TextReplacementConfig.builder()
                        .matchLiteral(":" + key + ":")
                        .replacement(emojiHead)
                        .build();
                message = message.replaceText(idk);
            }
        }

        if (chatEmoji.getConfig().getString("style").equalsIgnoreCase("Apple")) {
            if (chatEmoji.getConfig().getBoolean("permission") == true && (!player.hasPermission("chatemoji.use"))) {
                return;
            }

            for (AppleEmojis appleEmojis : AppleEmojis.values()) {
                String keyApple = appleEmojis.getKey();
                String valueApple = appleEmojis.getValue();
                heads.put(keyApple, valueApple);
                PlayerHeadObjectContents fortnite = ObjectContents.playerHead()
                        .profileProperty(PlayerHeadObjectContents.property("textures", heads.get(keyApple)))
                        .build();
                Component emojiHead = Component.object(fortnite);
                // Keysetter -> headComponent -> textreplacement
                TextReplacementConfig idk = TextReplacementConfig.builder()
                        .matchLiteral(":" + keyApple + ":")
                        .replacement(emojiHead)
                        .build();
                message = message.replaceText(idk);
            }
        }

        e.message(message);
    }
}
