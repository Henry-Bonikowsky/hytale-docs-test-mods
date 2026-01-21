package com.example.world;

import com.hypixel.hytale.world.chunk.WorldChunk;
import com.hypixel.hytale.world.block.BlockAccessor;
import com.hypixel.hytale.world.block.BlockType;
import com.hypixel.hytale.world.Location;

/**
 * Helper class demonstrating batch block operations on chunks.
 *
 * This class shows how to efficiently modify multiple blocks in a chunk,
 * which is useful for:
 * - Building structures
 * - Terrain generation
 * - Area effects
 * - World editing tools
 *
 * Best practices demonstrated:
 * - Batch operations for efficiency
 * - Marking chunks dirty only once after all changes
 * - Validation before modification
 * - Clear method documentation
 *
 * See: https://hytale-docs.dev/classes/com.hypixel.hytale.world.chunk.WorldChunk.html
 */
public class ChunkModifier {

    /**
     * Fills a cubic area with a specific block type.
     *
     * @param chunk The chunk to modify
     * @param x1 First corner X coordinate (world coords)
     * @param y1 First corner Y coordinate
     * @param z1 First corner Z coordinate (world coords)
     * @param x2 Second corner X coordinate (world coords)
     * @param y2 Second corner Y coordinate
     * @param z2 Second corner Z coordinate (world coords)
     * @param blockType The block type to fill with
     * @return The number of blocks changed
     */
    public static int fillCube(WorldChunk chunk, int x1, int y1, int z1,
                               int x2, int y2, int z2, BlockType blockType) {
        // Ensure coordinates are in correct order (min to max)
        int minX = Math.min(x1, x2);
        int maxX = Math.max(x1, x2);
        int minY = Math.min(y1, y2);
        int maxY = Math.max(y1, y2);
        int minZ = Math.min(z1, z2);
        int maxZ = Math.max(z1, z2);

        // Validate Y coordinates (world height limits)
        minY = Math.max(0, minY);
        maxY = Math.min(255, maxY);

        BlockAccessor accessor = chunk.getBlockAccessor();
        int blocksChanged = 0;

        // Iterate through all blocks in the cube
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    // Set the block
                    accessor.setBlock(x, y, z, blockType);
                    blocksChanged++;
                }
            }
        }

        // Mark chunk as needing saving (only once after all changes)
        chunk.markNeedsSaving();

        return blocksChanged;
    }

    /**
     * Replaces all blocks of one type with another in a chunk.
     *
     * @param chunk The chunk to modify
     * @param fromType The block type to replace
     * @param toType The block type to replace with
     * @return The number of blocks changed
     */
    public static int replaceBlocks(WorldChunk chunk, BlockType fromType, BlockType toType) {
        BlockAccessor accessor = chunk.getBlockAccessor();
        int blocksChanged = 0;

        // Get chunk bounds (chunks are 16x16 horizontally, full world height vertically)
        int chunkX = chunk.getChunkX() * 16;
        int chunkZ = chunk.getChunkZ() * 16;

        // Iterate through all blocks in the chunk
        for (int x = chunkX; x < chunkX + 16; x++) {
            for (int z = chunkZ; z < chunkZ + 16; z++) {
                for (int y = 0; y < 256; y++) {
                    // Check if this block matches the type we're replacing
                    BlockType currentBlock = accessor.getBlock(x, y, z);
                    if (currentBlock == fromType) {
                        accessor.setBlock(x, y, z, toType);
                        blocksChanged++;
                    }
                }
            }
        }

        if (blocksChanged > 0) {
            chunk.markNeedsSaving();
        }

        return blocksChanged;
    }

    /**
     * Creates a hollow cube outline with a specific block type.
     *
     * @param chunk The chunk to modify
     * @param x1 First corner X coordinate (world coords)
     * @param y1 First corner Y coordinate
     * @param z1 First corner Z coordinate (world coords)
     * @param x2 Second corner X coordinate (world coords)
     * @param y2 Second corner Y coordinate
     * @param z2 Second corner Z coordinate (world coords)
     * @param blockType The block type for the outline
     * @return The number of blocks changed
     */
    public static int createHollowCube(WorldChunk chunk, int x1, int y1, int z1,
                                       int x2, int y2, int z2, BlockType blockType) {
        int minX = Math.min(x1, x2);
        int maxX = Math.max(x1, x2);
        int minY = Math.max(0, Math.min(y1, y2));
        int maxY = Math.min(255, Math.max(y1, y2));
        int minZ = Math.min(z1, z2);
        int maxZ = Math.max(z1, z2);

        BlockAccessor accessor = chunk.getBlockAccessor();
        int blocksChanged = 0;

        // Only set blocks on the edges of the cube
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    // Check if this position is on an edge
                    boolean isEdge = (x == minX || x == maxX) ||
                                    (y == minY || y == maxY) ||
                                    (z == minZ || z == maxZ);

                    if (isEdge) {
                        accessor.setBlock(x, y, z, blockType);
                        blocksChanged++;
                    }
                }
            }
        }

        chunk.markNeedsSaving();
        return blocksChanged;
    }

    /**
     * Clears all blocks in a vertical column (from bedrock to sky).
     *
     * @param chunk The chunk to modify
     * @param x X coordinate (world coords)
     * @param z Z coordinate (world coords)
     * @return The number of blocks changed
     */
    public static int clearColumn(WorldChunk chunk, int x, int z) {
        BlockAccessor accessor = chunk.getBlockAccessor();
        int blocksChanged = 0;

        // Replace all blocks in this column with air
        for (int y = 0; y < 256; y++) {
            BlockType currentBlock = accessor.getBlock(x, y, z);
            if (currentBlock != BlockType.AIR) {
                accessor.setBlock(x, y, z, BlockType.AIR);
                blocksChanged++;
            }
        }

        if (blocksChanged > 0) {
            chunk.markNeedsSaving();
        }

        return blocksChanged;
    }

    /**
     * Gets the highest non-air block at the given X, Z coordinates.
     *
     * @param chunk The chunk to search
     * @param x X coordinate (world coords)
     * @param z Z coordinate (world coords)
     * @return The Y coordinate of the highest non-air block, or -1 if none found
     */
    public static int getHighestBlockAt(WorldChunk chunk, int x, int z) {
        BlockAccessor accessor = chunk.getBlockAccessor();

        // Start from the top and work down
        for (int y = 255; y >= 0; y--) {
            BlockType block = accessor.getBlock(x, y, z);
            if (block != BlockType.AIR) {
                return y;
            }
        }

        return -1;  // No solid blocks found
    }

    /**
     * Checks if a block position is safe for a player to teleport to.
     * Safe means: solid block below, two air blocks above.
     *
     * @param chunk The chunk to check
     * @param x X coordinate (world coords)
     * @param y Y coordinate (player feet position)
     * @param z Z coordinate (world coords)
     * @return true if the position is safe
     */
    public static boolean isSafeLocation(WorldChunk chunk, int x, int y, int z) {
        if (y < 1 || y > 253) {
            return false;  // Too close to world boundaries
        }

        BlockAccessor accessor = chunk.getBlockAccessor();

        // Check for solid block below
        BlockType below = accessor.getBlock(x, y - 1, z);
        if (below == BlockType.AIR) {
            return false;
        }

        // Check for air at feet and head level
        BlockType feet = accessor.getBlock(x, y, z);
        BlockType head = accessor.getBlock(x, y + 1, z);

        return feet == BlockType.AIR && head == BlockType.AIR;
    }
}
