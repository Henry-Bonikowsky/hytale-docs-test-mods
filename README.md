# Hytale Documentation Example Mods

> **⚠️ DISCLAIMER**
>
> These are **unofficial example mods** created for educational purposes based on documented and reverse-engineered Hytale server APIs. They are **not affiliated with or endorsed by Hypixel Studios**.
>
> - These examples demonstrate modding patterns and API usage based on current documentation
> - The Hytale API is subject to change and these examples may require updates for compatibility
> - These mods are intended as learning resources and may not be production-ready
> - Always refer to official Hypixel Studios documentation and resources when available

Example mods demonstrating the Hytale modding API. These mods accompany the [Hytale API Documentation](https://hytale-docs.dev) and showcase common modding patterns and best practices.

## 📚 Mods Included

### [Command Example](./command-example/)
Demonstrates command registration and implementation.
- Creating commands with required and optional arguments
- Argument parsing and validation
- Player-only command restrictions
- Error handling and user feedback
- **Commands**: `/hello [player]`, `/teleport <x> <y> <z>`

### [Event Example](./event-example/)
Demonstrates event listening and handling.
- Registering event listeners via EventBus
- Using event priorities (EARLY, NORMAL, LATE)
- Cancelling and modifying events
- Handling player events (join, quit, chat, movement)
- **Events**: PlayerJoinEvent, PlayerQuitEvent, PlayerChatEvent, PlayerMoveEvent

### [World Example](./world-example/)
Demonstrates world and block manipulation.
- Accessing chunks via World API
- Reading and writing blocks with BlockAccessor
- Batch block operations
- Chunk coordinate calculations
- Helper utilities for common tasks
- **Commands**: `/setblock <x> <y> <z> <blockType>`

## 🚀 Quick Start

### Requirements

- Java 21 or higher
- Maven 3.6 or higher
- Hytale (version 2026.01.15 or compatible)

### Before Building

These mods require the Hytale server JAR as a compile-time dependency. You need to install it to your local Maven repository first:

```bash
mvn install:install-file \
  -Dfile="C:/Users/[username]/AppData/Roaming/Hytale/install/release/package/game/latest/Server/HytaleServer.jar" \
  -DgroupId=com.hypixel.hytale \
  -DartifactId=hytale-server \
  -Dversion=2026.01.15 \
  -Dpackaging=jar
```

**Windows (PowerShell):**
```powershell
mvn install:install-file `
  -Dfile="$env:APPDATA\Hytale\install\release\package\game\latest\Server\HytaleServer.jar" `
  -DgroupId=com.hypixel.hytale `
  -DartifactId=hytale-server `
  -Dversion=2026.01.15 `
  -Dpackaging=jar
```

This only needs to be done once. The JAR will be cached in your local Maven repository (`~/.m2/repository/`).

### Building All Mods

From the repository root:

```bash
mvn clean package
```

This will build all three mods and place the JAR files in each module's `target/` directory:
- `command-example/target/CommandExample.jar`
- `event-example/target/EventExample.jar`
- `world-example/target/WorldExample.jar`

### Building Individual Mods

```bash
cd command-example
mvn clean package
```

Replace `command-example` with `event-example` or `world-example` as needed.

## 📦 Installation

### Singleplayer Worlds (Most Common)

1. Copy the mod JAR file to your world's mods folder:
   ```
   C:\Users\[username]\AppData\Roaming\Hytale\UserData\Saves\[WorldName]\mods\
   ```

2. Edit your world's `config.json` to enable the mod:
   ```
   C:\Users\[username]\AppData\Roaming\Hytale\UserData\Saves\[WorldName]\config.json
   ```

3. Add the mod to the configuration:
   ```json
   {
     "Mods": {
       "DocsExamples:CommandExample": {
         "Enabled": true
       },
       "DocsExamples:EventExample": {
         "Enabled": true
       },
       "DocsExamples:WorldExample": {
         "Enabled": true
       }
     }
   }
   ```

4. Reload or restart your world.

### Dedicated Server

1. Copy the mod JAR file to the server mods folder:
   ```
   C:\Users\[username]\AppData\Roaming\Hytale\install\release\package\game\latest\Server\mods\
   ```

2. Edit the server's `config.json` following the same format as above.

3. Restart the server.

### Verifying Installation

Check your server logs for messages like:
```
[PluginManager] Enabled plugin DocsExamples:CommandExample
[PluginManager] Enabled plugin DocsExamples:EventExample
[PluginManager] Enabled plugin DocsExamples:WorldExample
```

Or run `/help` in-game to see the custom commands.

## 📖 Learning Path

We recommend exploring the mods in this order:

1. **Command Example** - Start here to understand the plugin lifecycle and basic command structure
2. **Event Example** - Learn how to respond to game events
3. **World Example** - Explore world manipulation and block operations

Each mod's README contains:
- Detailed code walkthrough
- API usage examples
- Links to relevant documentation
- Ideas for extending the mod

## 🏗️ Project Structure

```
hytale-docs-test-mods/
├── command-example/          # Command registration and usage
│   ├── src/main/java/com/example/commands/
│   │   ├── CommandExamplePlugin.java
│   │   ├── HelloCommand.java
│   │   └── TeleportCommand.java
│   ├── src/main/resources/
│   │   └── manifest.json
│   ├── pom.xml
│   └── README.md
├── event-example/            # Event listening and handling
│   ├── src/main/java/com/example/events/
│   │   ├── EventExamplePlugin.java
│   │   └── PlayerEventListener.java
│   ├── src/main/resources/
│   │   └── manifest.json
│   ├── pom.xml
│   └── README.md
├── world-example/            # World and block manipulation
│   ├── src/main/java/com/example/world/
│   │   ├── WorldExamplePlugin.java
│   │   ├── SetBlockCommand.java
│   │   └── ChunkModifier.java
│   ├── src/main/resources/
│   │   └── manifest.json
│   ├── pom.xml
│   └── README.md
├── pom.xml                   # Parent POM with shared config
├── .gitignore
└── README.md                 # This file
```

## 🔗 API Documentation

Full API documentation is available at: **[https://hytale-docs.dev](https://hytale-docs.dev)**

Key API classes used in these examples:
- [PluginBase](https://hytale-docs.dev/classes/com.hypixel.hytale.plugin.PluginBase.html)
- [AbstractCommand](https://hytale-docs.dev/classes/com.hypixel.hytale.plugin.commands.AbstractCommand.html)
- [EventBus](https://hytale-docs.dev/classes/com.hypixel.hytale.event.EventBus.html)
- [WorldChunk](https://hytale-docs.dev/classes/com.hypixel.hytale.world.chunk.WorldChunk.html)
- [BlockAccessor](https://hytale-docs.dev/classes/com.hypixel.hytale.world.block.BlockAccessor.html)

## 💡 Common Patterns

### Plugin Lifecycle

All mods extend `PluginBase` and override:
- `setup()` - Called when plugin is enabled (register commands, events, etc.)
- `teardown()` - Called when plugin is disabled (cleanup)

### Command Registration

```java
@Override
public void setup() {
    commandRegistry.registerCommand(new MyCommand(this));
}
```

### Event Listening

```java
EventBus eventBus = plugin.getEventRegistry();
eventBus.register(PlayerJoinEvent.class, event -> {
    // Handle event
});
```

### World Manipulation

```java
World world = player.getWorld();
WorldChunk chunk = world.getChunkAt(x >> 4, z >> 4);
BlockAccessor accessor = chunk.getBlockAccessor();
accessor.setBlock(x, y, z, BlockType.STONE);
chunk.markNeedsSaving();
```

## 🤝 Contributing

These examples are maintained to stay current with the Hytale API. If you find issues or have suggestions:

1. Check the [Hytale Documentation](https://hytale-docs.dev) for API updates
2. Open an issue describing the problem or suggestion
3. Submit a pull request with improvements

## 📝 License

These example mods are provided as educational resources and may be freely used and modified.

## 🔍 Troubleshooting

### Mod not loading
- Check that the JAR is in the correct mods folder
- Verify the mod is enabled in `config.json`
- Look for errors in the server logs

### Commands not working
- Ensure the mod is loaded (check logs)
- Verify you have permission to run commands
- Check command syntax in each mod's README

### Build failures
- Ensure you have Java 21 installed: `java -version`
- Ensure you have Maven installed: `mvn -version`
- Try cleaning first: `mvn clean` then `mvn package`

### API version mismatch
- Check the parent `pom.xml` for the Hytale version
- Update `<hytale.version>` to match your server version
- Rebuild all mods

## 📧 Support

For issues with these examples:
- Open an issue on this repository

For general Hytale modding questions:
- Visit the [Hytale Documentation](https://hytale-docs.dev)
- Check the official Hytale modding resources

---

**Happy Modding!** 🎮
