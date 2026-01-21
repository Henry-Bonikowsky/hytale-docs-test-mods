package com.example.events;

import com.hypixel.hytale.plugin.PluginBase;

/**
 * Main plugin class for Event Example mod.
 *
 * This mod demonstrates how to listen to and handle events in Hytale.
 * Events allow your mod to respond to game actions like player joins,
 * block breaks, entity spawns, and more.
 *
 * See: https://hytale-docs.dev/classes/com.hypixel.hytale.event.EventBus.html
 */
public class EventExamplePlugin extends PluginBase {

    private PlayerEventListener playerEventListener;

    @Override
    public void setup() {
        getLogger().info("Event Example mod is loading...");

        // Create and register our event listener
        // The listener class contains all the event handler methods
        playerEventListener = new PlayerEventListener(this);

        getLogger().info("Event Example mod loaded! Listening for player events.");
    }

    @Override
    public void teardown() {
        getLogger().info("Event Example mod unloading...");

        // Clean up - event listeners are automatically unregistered
        // when the plugin is disabled, but explicit cleanup is good practice
        playerEventListener = null;
    }
}
