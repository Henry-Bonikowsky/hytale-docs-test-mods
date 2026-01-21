package com.example.events;

import com.hypixel.hytale.event.EventBus;
import com.hypixel.hytale.event.EventPriority;
import com.hypixel.hytale.event.player.PlayerJoinEvent;
import com.hypixel.hytale.event.player.PlayerQuitEvent;
import com.hypixel.hytale.event.player.PlayerChatEvent;
import com.hypixel.hytale.event.player.PlayerMoveEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

/**
 * Event listener class that handles various player events.
 *
 * This class demonstrates:
 * - Registering event listeners using EventBus
 * - Handling different types of player events
 * - Using event priorities (EARLY, NORMAL, LATE)
 * - Cancelling events
 * - Modifying event data
 *
 * Events are registered using Consumer lambdas with the EventBus.
 * The EventBus is obtained from the plugin via getEventRegistry().
 *
 * See: https://hytale-docs.dev/classes/com.hypixel.hytale.event.EventBus.html
 */
public class PlayerEventListener {

    private final EventExamplePlugin plugin;
    private final EventBus eventBus;

    public PlayerEventListener(EventExamplePlugin plugin) {
        this.plugin = plugin;
        // Get the event bus from the plugin's event registry
        // This is the main interface for registering event listeners
        this.eventBus = plugin.getEventRegistry();

        // Register all event listeners
        registerPlayerJoinListener();
        registerPlayerQuitListener();
        registerPlayerChatListener();
        registerPlayerMoveListener();
    }

    /**
     * Listen for players joining the server.
     *
     * This event fires when a player successfully joins the server.
     * You can modify the join message or perform initialization here.
     *
     * Priority: NORMAL (default)
     * See: https://hytale-docs.dev/classes/com.hypixel.hytale.event.player.PlayerJoinEvent.html
     */
    private void registerPlayerJoinListener() {
        eventBus.register(PlayerJoinEvent.class, event -> {
            var player = event.getPlayer();

            // Log to server console
            plugin.getLogger().info(player.getName() + " joined the server");

            // Customize the join message
            // Using Adventure Components for rich text formatting
            Component joinMessage = Component.text("Welcome, ")
                    .color(NamedTextColor.YELLOW)
                    .append(Component.text(player.getName())
                            .color(NamedTextColor.GOLD)
                            .decorate(TextDecoration.BOLD))
                    .append(Component.text("!")
                            .color(NamedTextColor.YELLOW));

            event.setJoinMessage(joinMessage);
        });
    }

    /**
     * Listen for players leaving the server.
     *
     * This event fires when a player disconnects from the server.
     * You can customize the quit message or perform cleanup here.
     *
     * Priority: NORMAL (default)
     * See: https://hytale-docs.dev/classes/com.hypixel.hytale.event.player.PlayerQuitEvent.html
     */
    private void registerPlayerQuitListener() {
        eventBus.register(PlayerQuitEvent.class, event -> {
            var player = event.getPlayer();

            plugin.getLogger().info(player.getName() + " left the server");

            // Customize the quit message
            Component quitMessage = Component.text(player.getName())
                    .color(NamedTextColor.GRAY)
                    .append(Component.text(" has left the game")
                            .color(NamedTextColor.DARK_GRAY));

            event.setQuitMessage(quitMessage);
        });
    }

    /**
     * Listen for player chat messages with EARLY priority.
     *
     * This event fires when a player sends a chat message.
     * You can modify the message, cancel it, or perform moderation here.
     *
     * Priority: EARLY - runs before other listeners
     * This is useful for filtering or moderation features
     *
     * See: https://hytale-docs.dev/classes/com.hypixel.hytale.event.player.PlayerChatEvent.html
     * See: https://hytale-docs.dev/classes/com.hypixel.hytale.event.EventPriority.html
     */
    private void registerPlayerChatListener() {
        eventBus.register(
            PlayerChatEvent.class,
            EventPriority.EARLY,  // Run before other listeners
            event -> {
                var player = event.getPlayer();
                String message = event.getMessage();

                // Log chat messages to server console
                plugin.getLogger().info("[CHAT] " + player.getName() + ": " + message);

                // Example: Block messages containing "badword"
                if (message.toLowerCase().contains("badword")) {
                    // Cancel the event to prevent the message from being sent
                    event.setCancelled(true);

                    // Notify the player
                    player.sendMessage(Component.text("Your message was blocked!")
                            .color(NamedTextColor.RED));

                    plugin.getLogger().warn("Blocked message from " + player.getName());
                }

                // Example: Add a prefix to messages starting with "!"
                if (message.startsWith("!")) {
                    String newMessage = "[ANNOUNCEMENT] " + message.substring(1);
                    event.setMessage(newMessage);
                }
            }
        );
    }

    /**
     * Listen for player movement with LATE priority.
     *
     * This event fires when a player moves to a different position.
     * Warning: This event fires very frequently! Use sparingly.
     *
     * Priority: LATE - runs after other listeners
     * Useful for final checks or cleanup operations
     *
     * See: https://hytale-docs.dev/classes/com.hypixel.hytale.event.player.PlayerMoveEvent.html
     */
    private void registerPlayerMoveListener() {
        eventBus.register(
            PlayerMoveEvent.class,
            EventPriority.LATE,  // Run after other listeners
            event -> {
                var player = event.getPlayer();
                var from = event.getFrom();
                var to = event.getTo();

                // Example: Prevent players from moving below Y=0
                if (to.getY() < 0) {
                    // Cancel the movement
                    event.setCancelled(true);

                    // Notify the player
                    player.sendMessage(Component.text("You cannot go below Y=0!")
                            .color(NamedTextColor.RED));

                    plugin.getLogger().info("Prevented " + player.getName() + " from going below Y=0");
                }

                // Example: Log when players move between chunks
                // Note: This is just for demonstration - in production you'd want
                // to track this more efficiently to avoid excessive logging
                int fromChunkX = (int) Math.floor(from.getX() / 16);
                int fromChunkZ = (int) Math.floor(from.getZ() / 16);
                int toChunkX = (int) Math.floor(to.getX() / 16);
                int toChunkZ = (int) Math.floor(to.getZ() / 16);

                if (fromChunkX != toChunkX || fromChunkZ != toChunkZ) {
                    plugin.getLogger().info(player.getName() + " moved to chunk [" +
                            toChunkX + ", " + toChunkZ + "]");
                }
            }
        );
    }
}
