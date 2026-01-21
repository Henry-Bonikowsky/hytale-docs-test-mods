# Command Example Mod

This mod demonstrates how to create and register custom commands in Hytale using the command API.

## What This Demonstrates

- Extending `AbstractCommand` for synchronous command execution
- Registering commands via `CommandRegistry`
- Using required and optional command arguments
- Parsing and validating command arguments
- Sending formatted messages using Adventure Components
- Player-only command restrictions
- Error handling in commands

## Commands

### `/hello [player]`
Sends a friendly greeting message.
- **Optional argument**: `player` - Name of player to greet
- **Example**: `/hello` greets yourself
- **Example**: `/hello Steve` greets Steve

### `/teleport <x> <y> <z>`
Teleports you to the specified coordinates.
- **Required arguments**: `x`, `y`, `z` - Numeric coordinates
- **Example**: `/teleport 100 64 -200`
- **Note**: Player-only command (cannot be used from console)

## Building

```bash
cd command-example
mvn clean package
```

The compiled JAR will be in `target/CommandExample.jar`.

## Installation

1. Copy `target/CommandExample.jar` to your Hytale mods folder:
   - **Singleplayer**: `C:\Users\[username]\AppData\Roaming\Hytale\UserData\Saves\[WorldName]\mods\`
   - **Dedicated Server**: `C:\Users\[username]\AppData\Roaming\Hytale\install\release\package\game\latest\Server\mods\`

2. Enable the mod in your `config.json`:
```json
{
  "Mods": {
    "DocsExamples:CommandExample": {
      "Enabled": true
    }
  }
}
```

3. Restart your world or server.

## Code Walkthrough

### CommandExamplePlugin.java
The main plugin class that extends `PluginBase`. Commands are registered in the `setup()` method:
```java
commandRegistry.registerCommand(new HelloCommand(this));
commandRegistry.registerCommand(new TeleportCommand(this));
```

### HelloCommand.java
A simple command demonstrating:
- **Optional arguments**: Using `withOptionalArg("player")` to accept an optional parameter
- **CommandContext**: Accessing sender and arguments via `context.getSender()` and `context.getArgs()`
- **Adventure Components**: Formatting text with colors using `Component.text(...).color(NamedTextColor.GREEN)`

### TeleportCommand.java
A more complex command demonstrating:
- **Required arguments**: Using `withRequiredArg()` for mandatory parameters
- **Type validation**: Checking if sender is a Player with `instanceof Player`
- **Argument parsing**: Converting string arguments to doubles with `Double.parseDouble()`
- **Player manipulation**: Teleporting players using `player.teleport(location)`
- **Error handling**: Try-catch blocks for parsing errors and failed operations

## Relevant API Documentation

- [AbstractCommand](https://hytale-docs.dev/classes/com.hypixel.hytale.plugin.commands.AbstractCommand.html)
- [CommandRegistry](https://hytale-docs.dev/classes/com.hypixel.hytale.plugin.commands.CommandRegistry.html)
- [CommandContext](https://hytale-docs.dev/classes/com.hypixel.hytale.plugin.commands.CommandContext.html)
- [Player](https://hytale-docs.dev/classes/com.hypixel.hytale.entity.player.Player.html)
- [Location](https://hytale-docs.dev/classes/com.hypixel.hytale.world.Location.html)

## Extending This Example

Ideas for extending this mod:
- Add a `/spawn` command to teleport to world spawn
- Create a `/tpa <player>` command to request teleportation to another player
- Add a `/home` command with saved locations
- Implement command cooldowns to prevent spam
- Add permission checks for admin-only commands
- Create a `/broadcast` command with formatted messages
