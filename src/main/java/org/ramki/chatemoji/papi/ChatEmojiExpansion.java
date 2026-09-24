package org.ramki.chatemoji.papi;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.ramki.chatemoji.ChatEmoji;

public class ChatEmojiExpansion extends PlaceholderExpansion {

    private final ChatEmoji plugin;
    public ChatEmojiExpansion(ChatEmoji chatEmoji) {
        this.plugin = chatEmoji;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "chatemoji";
    }

    @Override
    public @NotNull String getAuthor() {
        return "raammi";
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
        if (params.equalsIgnoreCase("style")) {
            String style = plugin.getConfig().getString("style");
            if (style == null) return "disabled";
            return style.equalsIgnoreCase("default") || style.equalsIgnoreCase("apple") ? style : "disabled";
        }
        return null;
    }
}
