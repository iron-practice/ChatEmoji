package org.ramki.chatemoji.config;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.permissions.Permissible;

public record EmojiSettings(boolean permissionRequired, String permissionNode, Component noPermissionMessage) {

    public static EmojiSettings load(FileConfiguration config) {
        String noPermission = config.getString("messages.no-permission",
                "<red>You do not have permission to do that.");
        return new EmojiSettings(
                config.getBoolean("permission.required", false),
                config.getString("permission.node", "chatemoji.use"),
                MiniMessage.miniMessage().deserialize(noPermission)
        );
    }

    public boolean allowedToUse(Permissible permissible) {
        return !this.permissionRequired || permissible.hasPermission(this.permissionNode);
    }
}
