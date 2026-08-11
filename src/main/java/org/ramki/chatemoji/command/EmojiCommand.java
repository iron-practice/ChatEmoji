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

package org.ramki.chatemoji.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.ramki.chatemoji.ChatEmoji;
import org.ramki.chatemoji.config.EmojiSettings;
import org.ramki.chatemoji.emoji.EmojiRegistry;

public final class EmojiCommand {

    private final EmojiRegistry registry;
    private final EmojiSettings settings;
    private final ChatEmoji chatEmoji;

    public EmojiCommand(EmojiRegistry registry, EmojiSettings settings, ChatEmoji chatEmoji) {
        this.chatEmoji = chatEmoji;
        this.registry = registry;
        this.settings = settings;
    }

    /*
     * The permission is checked in the executor instead of requires() so
     * players without it still see the configured deny message rather than
     * an unknown command error.
     */
    public LiteralCommandNode<CommandSourceStack> build() {
        return Commands.literal("emoji")
                .executes(context -> {
                    CommandSender sender = context.getSource().getSender();
                    if (!(sender instanceof Player player)) {
                        sender.sendMessage(Component.text("Only a player can run this command.", NamedTextColor.RED));
                        return Command.SINGLE_SUCCESS;
                    }
                    if (!this.settings.allowedToUse(player)) {
                        player.sendMessage(this.settings.noPermissionMessage());
                        return Command.SINGLE_SUCCESS;
                    }
                    player.sendMessage(this.registry.listMessage());
                    return Command.SINGLE_SUCCESS;
                })
                .build();
    }
}
