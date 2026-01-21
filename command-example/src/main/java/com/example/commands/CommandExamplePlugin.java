package com.example.commands;

import com.hypixel.hytale.plugin.PluginBase;
import com.hypixel.hytale.plugin.commands.CommandRegistry;

/**
 * Main plugin class for Command Example mod.
 *
 * This mod demonstrates how to register and implement custom commands in Hytale.
 * Commands are registered in the setup() method using the CommandRegistry.
 *
 * See: https://hytale-docs.dev/classes/com.hypixel.hytale.plugin.commands.CommandRegistry.html
 */
public class CommandExamplePlugin extends PluginBase {

    /**
     * Called when the plugin is loaded and enabled.
     * This is where you register commands, event listeners, and perform initialization.
     */
    @Override
    public void setup() {
        // Get the command registry - this is used to register all commands
        CommandRegistry commandRegistry = this.commandRegistry;

        // Register the /hello command
        // This demonstrates a simple command with an optional player argument
        commandRegistry.registerCommand(new HelloCommand(this));

        // Register the /teleport command
        // This demonstrates a command with required numeric arguments
        commandRegistry.registerCommand(new TeleportCommand(this));

        getLogger().info("Command Example mod loaded! Available commands: /hello, /teleport");
    }

    /**
     * Called when the plugin is being disabled/unloaded.
     * Use this to clean up resources, save data, etc.
     */
    @Override
    public void teardown() {
        getLogger().info("Command Example mod unloading...");
    }
}
