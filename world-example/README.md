# World Manipulation Example Mod

This mod demonstrates how to read and modify blocks and chunks in the Hytale world.

## What This Demonstrates

- Accessing world chunks via `World.getChunkAt()`
- Using `BlockAccessor` to read and write blocks
- Converting block type strings to `BlockType` enums
- Marking chunks as needing saving with `markNeedsSaving()`
- Batch block operations for efficiency
- Helper utilities for common world manipulation tasks
- World coordinate system and chunk calculations
- Safe location validation

## Commands

### `/setblock <x> <y> <z> <blockType>`
Places a block at the specified coordinates.
- **Required arguments**:
  - `x` - X coordinate (integer)
  - `y` - Y coordinate (integer, 0-255)
  - `z` - Z coordinate (integer)
  - `blockType` - Block type name (e.g., stone, dirt, oak_log)
- **Example**: `/setblock 100 64 -50 stone`
- **Example**: `/setblock 0 70 0 diamond_block`

## Building

```bash
cd world-example
mvn clean package
```

The compiled JAR will be in `target/WorldExample.jar`.

## Installation

1. Copy `target/WorldExample.jar` to your Hytale mods folder:
   - **Singleplayer**: `C:\Users\[username]\AppData\Roaming\Hytale\UserData\Saves\[WorldName]\mods\`
   - **Dedicated Server**: `C:\Users\[username]\AppData\Roaming\Hytale\install\release\package\game\latest\Server\mods\`

2. Enable the mod in your `config.json`:
```json
{
  "Mods": {
    "DocsExamples:WorldExample": {
      "Enabled": true
    }
  }
}
```

3. Restart your world or server.

## Testing

1. Join your server/world
2. Stand near coordinates you want to modify
3. Run `/setblock 100 64 -50 stone` to place a stone block
4. Try different block types: `grass_block`, `oak_log`, `diamond_ore`, etc.
5. The block should appear immediately at the specified location

## Code Walkthrough

### WorldExamplePlugin.java
The main plugin class that registers the `/setblock` command.

### SetBlockCommand.java
Demonstrates the complete workflow for modifying blocks:

**Get the world from a player**:
```java
World world = player.getWorld();
```

**Calculate chunk coordinates**:
```java
// Block coordinates to chunk coordinates (divide by 16)
WorldChunk chunk = world.getChunkAt(x >> 4, z >> 4);
```

**Get the BlockAccessor**:
```java
BlockAccessor accessor = chunk.getBlockAccessor();
```

**Read a block**:
```java
BlockType currentBlock = accessor.getBlock(x, y, z);
```

**Write a block**:
```java
accessor.setBlock(x, y, z, BlockType.STONE);
```

**Save changes**:
```java
chunk.markNeedsSaving();  // Persist to disk
```

### ChunkModifier.java
A helper class demonstrating advanced block manipulation patterns:

**fillCube()** - Fills a cubic region with blocks
- Shows efficient iteration through 3D space
- Demonstrates coordinate validation
- Single `markNeedsSaving()` call after all changes

**replaceBlocks()** - Finds and replaces block types
- Iterates through entire chunk (16x16x256)
- Conditional block replacement
- Only saves if changes were made

**createHollowCube()** - Creates hollow structures
- Edge detection logic
- Building outlines and frames

**clearColumn()** - Removes blocks in a vertical column
- Useful for making shafts or clearing space
- Only replaces non-air blocks for efficiency

**getHighestBlockAt()** - Finds surface level
- Top-down search for solid blocks
- Used in teleportation and spawning systems

**isSafeLocation()** - Validates spawn points
- Checks for solid ground below
- Ensures air space for player
- Prevents suffocation or falling

## Understanding Coordinates

**World Coordinates**: Absolute positions in the world (X, Y, Z)
- X: East/West
- Y: Up/Down (0 = bedrock, 255 = sky limit)
- Z: North/South

**Chunk Coordinates**: 16x16 vertical columns
- Convert block X to chunk X: `chunkX = blockX >> 4` (divide by 16)
- Convert block Z to chunk Z: `chunkZ = blockZ >> 4`
- Convert chunk to block: `blockX = chunkX * 16`

**Example**:
- Block at X=100 is in chunk X=6 (100 / 16 = 6.25, truncated to 6)
- Block at X=-32 is in chunk X=-2 (-32 / 16 = -2)

## Performance Considerations

**Batch Operations**: When modifying many blocks:
- Make all changes first
- Call `markNeedsSaving()` once at the end
- This is much more efficient than marking after each block

**Chunk Loading**: Always check if chunk is loaded before accessing:
```java
WorldChunk chunk = world.getChunkAt(chunkX, chunkZ);
if (chunk == null) {
    // Chunk not loaded, handle gracefully
}
```

**Large Operations**: For very large modifications:
- Consider spreading work across multiple server ticks
- Use async operations to avoid blocking the main thread
- Monitor performance impact on server TPS

## Relevant API Documentation

- [World](https://hytale-docs.dev/classes/com.hypixel.hytale.world.World.html)
- [WorldChunk](https://hytale-docs.dev/classes/com.hypixel.hytale.world.chunk.WorldChunk.html)
- [BlockAccessor](https://hytale-docs.dev/classes/com.hypixel.hytale.world.block.BlockAccessor.html)
- [BlockType](https://hytale-docs.dev/classes/com.hypixel.hytale.world.block.BlockType.html)
- [Location](https://hytale-docs.dev/classes/com.hypixel.hytale.world.Location.html)

## Extending This Example

Ideas for extending this mod:
- Add a `/fill` command that uses `ChunkModifier.fillCube()`
- Create a `/replace` command using `ChunkModifier.replaceBlocks()`
- Implement structure building (houses, towers, etc.)
- Add undo/redo functionality with change history
- Create a world editor with selection tools
- Build a terrain smoothing tool
- Implement custom structure generation
- Add schematic loading/saving
