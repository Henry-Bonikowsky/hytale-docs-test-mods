package com.example.world;

import com.hypixel.hytale.plugin.PluginBase;
import com.hypixel.hytale.plugin.commands.CommandRegistry;

/**
 * Main plugin class for World Manipulation Example mod.
 *
 * This mod demonstrates how to read and modify blocks and chunks in the world.
 * World manipulation is essential for many mod types including:
 * - Custom structures and buildings
 * - Terrain modification tools
 * - Mining and excavation systems
 * - Custom world generation
 *
 * See: https://hytale-docs.dev/classes/com.hypixel.hytale.world.chunk.WorldChunk.html
 * See: https://hytale-docs.dev/classes/com.hypixel.hytale.world.block.BlockAccessor.html
 */
public class WorldExamplePlugin extends PluginBase {

    @Override
    public void setup() {
        getLogger().info("World Example mod is loading...");

        // Register the /setblock command
        CommandRegistry commandRegistry = this.commandRegistry;
        commandRegistry.registerCommand(new SetBlockCommand(this));

        getLogger().info("World Example mod loaded! Available commands: /setblock");
    }

    @Override
    public void teardown() {
        getLogger().info("World Example mod unloading...");
    }
}
