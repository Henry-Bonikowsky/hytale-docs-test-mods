# Event Example Mod

This mod demonstrates how to listen to and handle events in Hytale using the event system.

## What This Demonstrates

- Registering event listeners using `EventBus`
- Handling player events (join, quit, chat, movement)
- Using event priorities (`EARLY`, `NORMAL`, `LATE`)
- Cancelling events with `setCancelled(true)`
- Modifying event data (messages, locations)
- Using Consumer lambdas for event handlers
- Best practices for event-driven programming

## Events Handled

### PlayerJoinEvent (NORMAL priority)
Fires when a player joins the server.
- Logs the join to server console
- Customizes the join message with formatted text

### PlayerQuitEvent (NORMAL priority)
Fires when a player leaves the server.
- Logs the disconnect to server console
- Customizes the quit message

### PlayerChatEvent (EARLY priority)
Fires when a player sends a chat message.
- Logs all chat messages to server console
- Blocks messages containing "badword"
- Adds "[ANNOUNCEMENT]" prefix to messages starting with "!"

### PlayerMoveEvent (LATE priority)
Fires when a player moves position.
- Prevents players from going below Y=0
- Logs when players move between chunks
- **Note**: This event fires very frequently, use sparingly!

## Building

```bash
cd event-example
mvn clean package
```

The compiled JAR will be in `target/EventExample.jar`.

## Installation

1. Copy `target/EventExample.jar` to your Hytale mods folder:
   - **Singleplayer**: `C:\Users\[username]\AppData\Roaming\Hytale\UserData\Saves\[WorldName]\mods\`
   - **Dedicated Server**: `C:\Users\[username]\AppData\Roaming\Hytale\install\release\package\game\latest\Server\mods\`

2. Enable the mod in your `config.json`:
```json
{
  "Mods": {
    "DocsExamples:EventExample": {
      "Enabled": true
    }
  }
}
```

3. Restart your world or server.

## Testing

1. Join your server - you should see a custom welcome message
2. Type "hello badword" in chat - it should be blocked
3. Type "!Server restart in 5 minutes" - it should have "[ANNOUNCEMENT]" prefix
4. Try to fall below Y=0 - you should be prevented
5. Check your server logs to see all the event logging

## Code Walkthrough

### EventExamplePlugin.java
The main plugin class creates and holds a reference to the `PlayerEventListener` instance.

### PlayerEventListener.java
The event listener class that demonstrates:

**EventBus Registration**:
```java
EventBus eventBus = plugin.getEventRegistry();
eventBus.register(EventClass.class, event -> {
    // Handle event
});
```

**Event Priorities**:
- `EARLY` - Runs first, good for filtering/blocking
- `NORMAL` - Default, runs in middle
- `LATE` - Runs last, good for final checks

Example:
```java
eventBus.register(
    PlayerChatEvent.class,
    EventPriority.EARLY,  // Specify priority
    event -> { ... }
);
```

**Cancelling Events**:
```java
if (shouldCancel) {
    event.setCancelled(true);  // Prevents the action
}
```

**Modifying Event Data**:
```java
event.setMessage("New message");      // Modify chat message
event.setJoinMessage(component);       // Modify join message
```

## Event Priority Flow

When multiple listeners are registered for the same event, they execute in this order:

1. **EARLY** listeners - Run first
2. **NORMAL** listeners - Run after EARLY
3. **LATE** listeners - Run last

If any listener cancels the event, the event is still passed to all listeners, but the game action may not occur (depending on the event type).

## Relevant API Documentation

- [EventBus](https://hytale-docs.dev/classes/com.hypixel.hytale.event.EventBus.html)
- [EventPriority](https://hytale-docs.dev/classes/com.hypixel.hytale.event.EventPriority.html)
- [PlayerJoinEvent](https://hytale-docs.dev/classes/com.hypixel.hytale.event.player.PlayerJoinEvent.html)
- [PlayerQuitEvent](https://hytale-docs.dev/classes/com.hypixel.hytale.event.player.PlayerQuitEvent.html)
- [PlayerChatEvent](https://hytale-docs.dev/classes/com.hypixel.hytale.event.player.PlayerChatEvent.html)
- [PlayerMoveEvent](https://hytale-docs.dev/classes/com.hypixel.hytale.event.player.PlayerMoveEvent.html)

## Extending This Example

Ideas for extending this mod:
- Add block break/place event listeners
- Implement an anti-cheat system using movement events
- Create a custom chat filter with configurable word list
- Add entity spawn listeners to control mob spawning
- Implement a player statistics tracker using various events
- Create a region protection system using block and movement events
- Add inventory interaction listeners for custom GUI systems
