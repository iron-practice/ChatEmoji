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
