package org.ramki.chatemoji.api;

import org.ramki.chatemoji.ChatEmoji;

public class ChatEmojiAPI {
    private static ChatEmojiAPI instance;

    private final ChatEmoji plugin;

    public ChatEmojiAPI(ChatEmoji plugin) {
        this.plugin = plugin;
    }

    /**
     * Inits the API, MUST be called once on onEnable
     *
     * @param plugin instance of the plugin
     */
    public static void init(ChatEmoji plugin) {
        if (instance != null)
            throw new IllegalStateException("ChatEmoji has already been initialized.");
        instance = new ChatEmojiAPI(plugin);
    }

    /**
     * Shutsdown the API, Should be called on onDisable
     *
     * @param plugin instance of the plugin
     */
    public static void disable(ChatEmoji plugin) {
        instance =null;
    }

    /**
     * Checks if the API is available
     *
     * @return returns the instance
     */
    public static boolean isAvailable() {
        return instance != null;
    }

    /**
     * Gets the singleton instance of the API
     *
     * @return returns the instance
     */
    public static ChatEmojiAPI getInstance() {
        if (instance == null) {
            throw new IllegalStateException("ChatEmoji has not been initialized yet.");
        }
        return instance;
    }

    /**
     * Sets the style
     *
     * @param isEnabled if enabled style set to "default", otherwise to "apple"
     */
    public void setStyle(boolean isEnabled) {
        String style = isEnabled ? "default" : "apple";
        plugin.getConfig().set("style", style);
        plugin.saveConfig(); plugin.reloadConfig();
    }

    /**
     * Checks if the current style is set to "default"
     *
     * @return true if "default", false otherwise
     */
    public boolean isDefaultStyle() {
        String style = plugin.getConfig().getString("style");
        if (style == null) return false;

        return style.equalsIgnoreCase("default");
    }

    /**
     * Toggles between styles
     *
     * @return if style was "default" it switches to apple, otherwise "default"
     */
    public boolean toggleStyle() {
        boolean appleStyle = isDefaultStyle();
        String newStyle = appleStyle ? "apple" : "default";
        plugin.getConfig().set("style", newStyle);

        plugin.saveConfig(); plugin.reloadConfig();

        return !appleStyle;
    }
}
