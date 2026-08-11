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
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.ramki.chatemoji.ChatEmoji;
import org.ramki.chatemoji.config.EmojiSettings;
import org.ramki.chatemoji.emoji.EmojiRegistry;

import java.util.Set;

public final class EmojiCommand {
    private static final Set<String> APPLE_STRING = Set.of("true", "apple");
    private static final Set<String> DEFAULT = Set.of("false", "default");

    private final EmojiRegistry registry;
    private final EmojiSettings settings;
    private final ChatEmoji chatEmoji;

    public EmojiCommand(EmojiRegistry registry, EmojiSettings settings, ChatEmoji chatEmoji) {
        this.chatEmoji = chatEmoji;
        this.registry = registry;
        this.settings = settings;
    }

    boolean toggle;

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

                .then(Commands.literal("style")
                        .requires(sender -> sender.getSender().isOp() || sender.getSender().hasPermission("chatemoji.admin"))
                        .then(Commands.argument("name", StringArgumentType.word())
                                .suggests((context, builder) -> {
                                    builder.suggest("apple");
                                    builder.suggest("default");
                                    return builder.buildFuture();
                                })
                                .executes(context -> {
                                    CommandSender sender = context.getSource().getSender();
                                    final String name = StringArgumentType.getString(context, "name");
                                    if (name.equalsIgnoreCase("apple")) {
                                        this.registry.load(this.chatEmoji.getConfig().getConfigurationSection("apple"));
                                        this.chatEmoji.getConfig().set("emoji-style", "apple");
                                        this.chatEmoji.saveConfig();
                                        sender.sendRichMessage("<green>updated emoji style to: <yellow>apple");
                                    } else if (name.equalsIgnoreCase("default")) {
                                        this.registry.load(this.chatEmoji.getConfig().getConfigurationSection("emojis"));
                                        this.chatEmoji.getConfig().set("emoji-style", "emojis");
                                        this.chatEmoji.saveConfig();
                                        sender.sendRichMessage("<green>updated emoji style to: <yellow>default");
                                    } else {
                                        sender.sendRichMessage("<red>Invalid style option, use: <yellow>default/apple");
                                    }
                                    return Command.SINGLE_SUCCESS;
                                })))
                .build();
    }
}
