package org.ramki.chatemoji.listener;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.ramki.chatemoji.config.EmojiSettings;
import org.ramki.chatemoji.emoji.EmojiRegistry;

public final class EmojiChatListener implements Listener {

    private final EmojiRegistry registry;
    private final EmojiSettings settings;

    public EmojiChatListener(EmojiRegistry registry, EmojiSettings settings) {
        this.registry = registry;
        this.settings = settings;
    }

    /*
     * LOW so chat format plugins running at NORMAL and above already see the
     * message with emoji components applied instead of the raw tokens.
     */
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onChat(AsyncChatEvent event) {
        if (!this.settings.allowedToUse(event.getPlayer())) return;

        String plain = PlainTextComponentSerializer.plainText().serialize(event.message());
        if (!this.registry.mayContain(plain)) return;

        event.message(this.registry.apply(event.message()));
    }
}
