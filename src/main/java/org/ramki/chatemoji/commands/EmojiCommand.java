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
import org.ramki.chatemoji.enums.AppleEmojis;
import org.ramki.chatemoji.enums.Emojis;

import javax.inject.Named;
import java.util.List;
import java.util.Set;

public class EmojiCommand implements CommandExecutor, TabCompleter {
    private static final Set<String> DEFAULT_STRING = Set.of("default");
    private static final Set<String> APPLE_STRING = Set.of("apple");

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

                //all send messages

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

            }

            if (chatEmoji.getConfig().getString("style").equalsIgnoreCase("apple")) {

                player.sendMessage(MiniMessage.miniMessage().deserialize(
                        "\n<green>Chat Emojis\n"
                ));

                for (AppleEmojis emojis : AppleEmojis.values()) {
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

            }
        }

        if (args.length == 2 && player.hasPermission("chatemoji.admin") && args[0].equalsIgnoreCase("style")) {
            String str = args[1].toLowerCase();
            if (DEFAULT_STRING.contains(str)) {
                chatEmoji.getConfig().set("style", "Default");
                chatEmoji.saveConfig();
                chatEmoji.reloadConfig();
                chatEmoji.getConfig();
                player.sendMessage(Component.text("style changed to default!", NamedTextColor.GREEN));
                return true;
            } else if (APPLE_STRING.contains(str)) {
                chatEmoji.getConfig().set("style", "Apple");
                chatEmoji.saveConfig();
                chatEmoji.reloadConfig();
                chatEmoji.getConfig();
                player.sendMessage(Component.text("style changed to apple!", NamedTextColor.GREEN));
                return true;
            }
        }

        if (!player.hasPermission("chatemoji.admin")) {
            sender.sendMessage(Component.text("You do not have permission to do that!", NamedTextColor.RED));
            return true;
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length == 1) {
            String arg = args[0].toLowerCase();
            return List.of("reload", "style").stream()
                    .filter(s -> s.startsWith(arg))
                    .toList();
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("style")) {
            String arg1 = args[1].toLowerCase();
            return List.of("default", "apple").stream()
                    .filter(s -> s.startsWith(arg1))
                    .toList();
        }
        return List.of();
    }
}
