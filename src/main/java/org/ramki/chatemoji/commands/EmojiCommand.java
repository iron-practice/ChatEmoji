package org.ramki.chatemoji.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.object.ObjectContents;
import net.kyori.adventure.text.object.PlayerHeadObjectContents;
import org.bukkit.Color;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.ramki.chatemoji.ChatEmoji;
import org.ramki.chatemoji.enums.Emojis;

import java.util.List;
import java.util.Set;

public class EmojiCommand implements CommandExecutor, TabCompleter {
    private static final Set<String> DEFAULT_STRING = Set.of("true", "default");
    private static final Set<String> APPLE_STRING = Set.of("false", "apple");

    private ChatEmoji chatEmoji;
    public EmojiCommand(ChatEmoji chatEmoji) { this.chatEmoji = chatEmoji; }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) return true;

        String toggle = chatEmoji.getConfig().getString("style");

        if (args.length == 1 && args[0].equalsIgnoreCase("reload") && player.hasPermission("chatemoji.admin")) {
            chatEmoji.reloadConfig();
            chatEmoji.getConfig();
            player.sendMessage(Component.text("config reloaded!", NamedTextColor.GREEN));
        }

        if (args.length <= 0) {
            if (toggle.equalsIgnoreCase("default")) {

                player.sendMessage(MiniMessage.miniMessage().deserialize(
                        "\n<green>Chat Emojis\n"
                ));

                for (Emojis emojis : Emojis.values()) {
                    String key = emojis.getKey();
                    String value = emojis.getValue();

                    PlayerHeadObjectContents builder = ObjectContents.playerHead()
                            .profileProperty(PlayerHeadObjectContents.property("textures", value))
                            .build();
                    Component heads = Component.object(builder).color(NamedTextColor.WHITE);

                     Component fortniteText = MiniMessage.miniMessage().deserialize(
                            "<yellow>:" + key + ": <gray>- <reset>"
                     );
                     Component fortnite = fortniteText.append(heads);

                     player.sendMessage(fortnite);
                }

                player.sendMessage("\n");

            } //Apple style emoji
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        return List.of();
    }
}
