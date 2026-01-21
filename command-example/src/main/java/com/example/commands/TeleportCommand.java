package com.example.commands;

import com.hypixel.hytale.plugin.commands.AbstractCommand;
import com.hypixel.hytale.plugin.commands.CommandContext;
import com.hypixel.hytale.entity.player.Player;
import com.hypixel.hytale.world.Location;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

/**
 * A command that teleports the player to specific coordinates.
 *
 * Usage: /teleport <x> <y> <z>
 * - Requires three numeric arguments for coordinates
 * - Only works when executed by a player (not console)
 *
 * This demonstrates:
 * - Using required arguments with withRequiredArg()
 * - Parsing numeric arguments
 * - Validating the command sender type
 * - Manipulating player location
 * - Error handling and user feedback
 *
 * See: https://hytale-docs.dev/classes/com.hypixel.hytale.plugin.commands.AbstractCommand.html
 * See: https://hytale-docs.dev/classes/com.hypixel.hytale.world.Location.html
 */
public class TeleportCommand extends AbstractCommand {

    private final CommandExamplePlugin plugin;

    public TeleportCommand(CommandExamplePlugin plugin) {
        super("teleport", "Teleports you to the specified coordinates");
        this.plugin = plugin;

        // Define three required arguments for X, Y, Z coordinates
        // Required arguments must be provided or the command will show usage help
        // See: https://hytale-docs.dev/classes/com.hypixel.hytale.plugin.commands.AbstractCommand.html#withRequiredArg
        withRequiredArg("x");
        withRequiredArg("y");
        withRequiredArg("z");
    }

    @Override
    public void execute(CommandContext context) {
        var sender = context.getSender();

        // Check if the sender is a player (console can't be teleported)
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("This command can only be used by players!")
                    .color(NamedTextColor.RED));
            return;
        }

        try {
            // Parse the coordinate arguments
            // getArgs() returns a Map<String, String> of argument names to values
            double x = Double.parseDouble(context.getArgs().get("x"));
            double y = Double.parseDouble(context.getArgs().get("y"));
            double z = Double.parseDouble(context.getArgs().get("z"));

            // Get the player's current world and create a new location
            // Location represents a position in the world with X, Y, Z coordinates
            // See: https://hytale-docs.dev/classes/com.hypixel.hytale.world.Location.html
            Location currentLocation = player.getLocation();
            Location newLocation = new Location(currentLocation.getWorld(), x, y, z);

            // Teleport the player to the new location
            // See: https://hytale-docs.dev/classes/com.hypixel.hytale.entity.player.Player.html#teleport
            player.teleport(newLocation);

            // Send success message
            Component message = Component.text("Teleported to ")
                    .color(NamedTextColor.GREEN)
                    .append(Component.text(String.format("%.1f, %.1f, %.1f", x, y, z))
                            .color(NamedTextColor.YELLOW));
            player.sendMessage(message);

            plugin.getLogger().info(player.getName() + " teleported to " + x + ", " + y + ", " + z);

        } catch (NumberFormatException e) {
            // Handle invalid number format
            sender.sendMessage(Component.text("Invalid coordinates! Please provide numeric values.")
                    .color(NamedTextColor.RED));

        } catch (Exception e) {
            // Handle any other errors during teleportation
            sender.sendMessage(Component.text("Failed to teleport: " + e.getMessage())
                    .color(NamedTextColor.RED));
            plugin.getLogger().error("Teleport command error", e);
        }
    }
}
