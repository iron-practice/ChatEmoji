package org.ramki.chatemoji.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.ramki.chatemoji.config.EmojiSettings;
import org.ramki.chatemoji.emoji.EmojiRegistry;

public final class EmojiCommand {

    private final EmojiRegistry registry;
    private final EmojiSettings settings;

    public EmojiCommand(EmojiRegistry registry, EmojiSettings settings) {
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
