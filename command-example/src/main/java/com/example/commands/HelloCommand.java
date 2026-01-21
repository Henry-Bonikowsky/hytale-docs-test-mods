package com.example.commands;

import com.hypixel.hytale.plugin.commands.AbstractCommand;
import com.hypixel.hytale.plugin.commands.CommandContext;
import com.hypixel.hytale.entity.player.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

/**
 * A simple command that sends a greeting message.
 *
 * Usage: /hello [player]
 * - If no player is specified, greets the command sender
 * - If a player name is provided, greets that player
 *
 * This demonstrates:
 * - Extending AbstractCommand for synchronous command execution
 * - Using optional arguments with withOptionalArg()
 * - Accessing the command sender via CommandContext
 * - Sending formatted messages using Adventure Components
 *
 * See: https://hytale-docs.dev/classes/com.hypixel.hytale.plugin.commands.AbstractCommand.html
 * See: https://hytale-docs.dev/classes/com.hypixel.hytale.plugin.commands.CommandContext.html
 */
public class HelloCommand extends AbstractCommand {

    private final CommandExamplePlugin plugin;

    public HelloCommand(CommandExamplePlugin plugin) {
        // Call parent constructor with command name and optional description
        super("hello", "Sends a friendly greeting");
        this.plugin = plugin;

        // Define an optional argument for target player name
        // The string "player" is the argument name shown in help/usage
        // See: https://hytale-docs.dev/classes/com.hypixel.hytale.plugin.commands.AbstractCommand.html#withOptionalArg
        withOptionalArg("player");
    }

    /**
     * Executed when the command is run by a player or console.
     *
     * @param context Contains information about the command execution:
     *                - getSender(): Who executed the command
     *                - getArgs(): Map of argument names to their values
     *                - getServer(): Access to the server instance
     */
    @Override
    public void execute(CommandContext context) {
        // Get the command sender (could be a Player or ConsoleCommandSender)
        var sender = context.getSender();

        // Check if an optional player argument was provided
        String targetPlayerName = context.getArgs().get("player");

        if (targetPlayerName != null && !targetPlayerName.isEmpty()) {
            // Greet the specified player
            // Using Adventure Component API for rich text formatting
            // See: https://docs.advntr.dev/text.html
            Component message = Component.text("Hello, " + targetPlayerName + "!")
                    .color(NamedTextColor.GREEN);
            sender.sendMessage(message);

            plugin.getLogger().info(sender.getName() + " used /hello to greet " + targetPlayerName);
        } else {
            // Greet the command sender
            Component message = Component.text("Hello, " + sender.getName() + "!")
                    .color(NamedTextColor.GREEN);
            sender.sendMessage(message);

            plugin.getLogger().info(sender.getName() + " used /hello");
        }
    }
}
