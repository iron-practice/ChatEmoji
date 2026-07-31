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

package org.ramki.chatemoji.emoji;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.ShadowColor;
import net.kyori.adventure.text.object.ObjectContents;
import net.kyori.adventure.text.object.PlayerHeadObjectContents;
import org.bukkit.configuration.ConfigurationSection;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public final class EmojiRegistry {

    private static final Pattern TOKEN_PATTERN = Pattern.compile(":([A-Za-z0-9_]{1,32}):");
    private static final Pattern NAME_PATTERN = Pattern.compile("[a-z0-9_]{1,32}");
    private static final Pattern TEXTURE_PATTERN = Pattern.compile("[A-Za-z0-9+/=]{1,4096}");

    private final Logger logger;
    private final TextReplacementConfig replacementConfig;
    private volatile Map<String, Component> emojis = Map.of();
    private volatile Component listMessage = Component.text("No emojis loaded.", NamedTextColor.RED);

    public EmojiRegistry(Logger logger) {
        this.logger = logger;
        this.replacementConfig = TextReplacementConfig.builder()
                .match(TOKEN_PATTERN)
                .replacement((matchResult, builder) -> {
                    Component emoji = this.emojis.get(matchResult.group(1).toLowerCase(Locale.ROOT));
                    return emoji != null ? emoji : builder;
                })
                .build();
    }

    public void load(ConfigurationSection section) {
        /*
         * LinkedHashMap keeps the config.yml order so the /emoji list shows
         * emojis exactly as the server owner arranged them.
         */
        Map<String, Component> loaded = new LinkedHashMap<>();
        if (section != null) {
            for (String key : section.getKeys(false)) {
                String name = key.toLowerCase(Locale.ROOT);
                String texture = section.getString(key);
                if (!isValid(name, texture)) {
                    this.logger.warning("Skipping invalid emoji definition in config.yml: " + key);
                    continue;
                }
                loaded.put(name, buildHead(texture));
            }
        }

        if (!loaded.isEmpty()) {
            TextComponent.Builder list = Component.text()
                    .append(Component.text("Emojis:", NamedTextColor.AQUA));
            for (Map.Entry<String, Component> entry : loaded.entrySet()) {
                list.appendNewline()
                        .append(Component.text(":" + entry.getKey() + ":", NamedTextColor.YELLOW))
                        .append(Component.text(" - ", NamedTextColor.GRAY))
                        .append(entry.getValue());
            }
            this.listMessage = list.build();
        }

        this.emojis = Map.copyOf(loaded);
        this.logger.info("Loaded " + loaded.size() + " chat emojis");
    }

    public boolean mayContain(String plainMessage) {
        return !this.emojis.isEmpty() && plainMessage.indexOf(':') >= 0;
    }

    public Component apply(Component message) {
        if (this.emojis.isEmpty()) return message;
        return message.replaceText(this.replacementConfig);
    }

    public Component listMessage() {
        return this.listMessage;
    }

    private static boolean isValid(String name, String texture) {
        return name != null && texture != null
                && NAME_PATTERN.matcher(name).matches()
                && TEXTURE_PATTERN.matcher(texture).matches();
    }

    /*
     * ShadowColor.none() because the default text shadow bleeds through the
     * transparent pixels of the head and looks like a dark outline.
     */
    private static Component buildHead(String textureValue) {
        return Component.object(ObjectContents.playerHead()
                        .profileProperty(PlayerHeadObjectContents.property("textures", textureValue))
                        .hat(true)
                        .build())
                .color(NamedTextColor.WHITE)
                .shadowColor(ShadowColor.none());
    }
}
