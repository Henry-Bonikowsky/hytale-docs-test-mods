package com.example.world;

import com.hypixel.hytale.plugin.commands.AbstractCommand;
import com.hypixel.hytale.plugin.commands.CommandContext;
import com.hypixel.hytale.entity.player.Player;
import com.hypixel.hytale.world.Location;
import com.hypixel.hytale.world.World;
import com.hypixel.hytale.world.chunk.WorldChunk;
import com.hypixel.hytale.world.block.BlockAccessor;
import com.hypixel.hytale.world.block.BlockType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

/**
 * A command that places a block at specified coordinates.
 *
 * Usage: /setblock <x> <y> <z> <blockType>
 * Example: /setblock 100 64 -50 stone
 *
 * This demonstrates:
 * - Accessing world chunks via World.getChunkAt()
 * - Using BlockAccessor to read and modify blocks
 * - Converting block type strings to BlockType enums
 * - Marking chunks as needing saving
 * - Error handling for world operations
 *
 * See: https://hytale-docs.dev/classes/com.hypixel.hytale.world.chunk.WorldChunk.html
 * See: https://hytale-docs.dev/classes/com.hypixel.hytale.world.block.BlockAccessor.html
 */
public class SetBlockCommand extends AbstractCommand {

    private final WorldExamplePlugin plugin;

    public SetBlockCommand(WorldExamplePlugin plugin) {
        super("setblock", "Places a block at the specified coordinates");
        this.plugin = plugin;

        // Define required arguments: X, Y, Z coordinates and block type
        withRequiredArg("x");
        withRequiredArg("y");
        withRequiredArg("z");
        withRequiredArg("blockType");
    }

    @Override
    public void execute(CommandContext context) {
        var sender = context.getSender();

        // This command requires a player to get the world context
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("This command can only be used by players!")
                    .color(NamedTextColor.RED));
            return;
        }

        try {
            // Parse coordinate arguments
            int x = Integer.parseInt(context.getArgs().get("x"));
            int y = Integer.parseInt(context.getArgs().get("y"));
            int z = Integer.parseInt(context.getArgs().get("z"));
            String blockTypeString = context.getArgs().get("blockType");

            // Get the player's current world
            // World contains methods for accessing chunks and world data
            // See: https://hytale-docs.dev/classes/com.hypixel.hytale.world.World.html
            World world = player.getWorld();

            // Validate Y coordinate (world height limits)
            if (y < 0 || y > 255) {
                sender.sendMessage(Component.text("Y coordinate must be between 0 and 255!")
                        .color(NamedTextColor.RED));
                return;
            }

            // Convert block type string to BlockType enum
            // BlockType represents all possible block types in the game
            // See: https://hytale-docs.dev/classes/com.hypixel.hytale.world.block.BlockType.html
            BlockType blockType;
            try {
                blockType = BlockType.valueOf(blockTypeString.toUpperCase());
            } catch (IllegalArgumentException e) {
                sender.sendMessage(Component.text("Unknown block type: " + blockTypeString)
                        .color(NamedTextColor.RED)
                        .append(Component.text("\nExamples: stone, dirt, grass_block, oak_log")
                                .color(NamedTextColor.GRAY)));
                return;
            }

            // Get the chunk containing this block
            // Chunks are 16x16 vertical columns of blocks
            // See: https://hytale-docs.dev/classes/com.hypixel.hytale.world.chunk.WorldChunk.html
            WorldChunk chunk = world.getChunkAt(x >> 4, z >> 4);  // Divide by 16 to get chunk coords

            if (chunk == null) {
                sender.sendMessage(Component.text("Chunk not loaded at those coordinates!")
                        .color(NamedTextColor.RED));
                return;
            }

            // Get the BlockAccessor for this chunk
            // BlockAccessor provides methods to read and modify blocks
            // See: https://hytale-docs.dev/classes/com.hypixel.hytale.world.block.BlockAccessor.html
            BlockAccessor accessor = chunk.getBlockAccessor();

            // Read the current block at this location
            BlockType currentBlock = accessor.getBlock(x, y, z);

            // Set the new block
            // This modifies the world immediately
            accessor.setBlock(x, y, z, blockType);

            // Mark the chunk as needing to be saved
            // This ensures changes are persisted to disk
            chunk.markNeedsSaving();

            // Send success message
            Component message = Component.text("Block at ")
                    .color(NamedTextColor.GREEN)
                    .append(Component.text(x + ", " + y + ", " + z)
                            .color(NamedTextColor.YELLOW))
                    .append(Component.text(" changed from ")
                            .color(NamedTextColor.GREEN))
                    .append(Component.text(currentBlock.name())
                            .color(NamedTextColor.AQUA))
                    .append(Component.text(" to ")
                            .color(NamedTextColor.GREEN))
                    .append(Component.text(blockType.name())
                            .color(NamedTextColor.AQUA));

            sender.sendMessage(message);

            plugin.getLogger().info(player.getName() + " set block at " +
                    x + ", " + y + ", " + z + " to " + blockType.name());

        } catch (NumberFormatException e) {
            sender.sendMessage(Component.text("Invalid coordinates! X, Y, Z must be integers.")
                    .color(NamedTextColor.RED));

        } catch (Exception e) {
            sender.sendMessage(Component.text("Failed to set block: " + e.getMessage())
                    .color(NamedTextColor.RED));
            plugin.getLogger().error("SetBlock command error", e);
        }
    }
}
