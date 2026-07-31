/*
 * Copyright (C) 2023-2026 ramenrrami
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

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
