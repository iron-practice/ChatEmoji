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

package org.ramki.chatemoji;

import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.plugin.java.JavaPlugin;
import org.ramki.chatemoji.command.EmojiCommand;
import org.ramki.chatemoji.config.EmojiSettings;
import org.ramki.chatemoji.emoji.EmojiRegistry;
import org.ramki.chatemoji.listener.EmojiChatListener;

public final class ChatEmoji extends JavaPlugin {

    @Override
    public void onEnable() {
        /*
         * Player head chat components only exist since 1.21.9. Failing here with
         * a clear message beats a NoClassDefFoundError mid-chat on older servers.
         */
        try {
            Class.forName("net.kyori.adventure.text.object.ObjectContents");
        } catch (ClassNotFoundException ex) {
            getLogger().severe("ChatEmoji requires Minecraft 1.21.9 or newer, disabling.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        saveDefaultConfig();

        EmojiSettings settings = EmojiSettings.load(getConfig());
        EmojiRegistry registry = new EmojiRegistry(getLogger());
        registry.load(getConfig().getConfigurationSection("emojis"));

        getServer().getPluginManager().registerEvents(new EmojiChatListener(registry, settings), this);

        EmojiCommand emojiCommand = new EmojiCommand(registry, settings);
        getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands ->
                commands.registrar().register(emojiCommand.build(), "Shows the list of chat emojis"));
    }
}
