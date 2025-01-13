---
icon: octicons/broadcast-24
---

AdvancedServerList provides some events that your plugin can listen to.  
These events are structurally the same across all platforms, yet still require the usage of Platform-specific APIs due to how some events are registered and handled on some platforms.

The plugin currently offers the following events:

<div class="grid cards" markdown>

- [PreServerListSetEvent](#preserverlistsetevent)
- [PostServerListSetEvent](#postserverlistsetevent)

</div>

## PreServerListSetEvent

This event is executed before AdvancedServerList tries to modify the player list.  

### Methods { #preserverlistsetevent-methods }

The following methods are available to use:

| Method                   | Description                                                                    |
|--------------------------|--------------------------------------------------------------------------------|
| `getEntry()`             | Returns the [`ProfileEntry`][profileentry] that AdvancedServerList should use. |
| `setEntry(ProfileEntry)` | Sets the [`ProfileEntry`][profileentry] that should be used.                   |
| `isCancelled()`          | Returns wether this event was cancelled or not.                                |
| `setCancelled(boolean)`  | Sets the event's cancelled state.                                              |

### Notes { #preserverlistsetevent-notes }

- Providing `null` as the `ProfileEntry` for `setEntry(ProfileEntry)` is not allowed. Also, a copy of the provided `ProfileEntry` is made and used.

## PostServerListSetEvent

This event is executed after AdvancedServerList completed the server list modifications.

### Methods { #postserverlistsetevent-methods }

| Method                   | Description                                                              |
|--------------------------|--------------------------------------------------------------------------|
| `getEntry()`             | Returns the [`ProfileEntry`][profileentry] that AdvancedServerList used. |

### Notes { #postserverlistsetevent-notes }

-   The returned `ProfileEntry` is not an actual representation of what is being displayed to the player and rather an instance of values the plugin received.
    The returned value may also be `null` should the Ping Event be cancelled due to things such as an invalid Protocol or the [`PreServerListSetEvent`](#preserverlistsetevent) being cancelled.

## Registering the Events

To register an event, please refer to our platforms documentation on how to do this.

## What is a ProfileEntry?

Both events provide and use a so called [`ProfileEntry`][profileentry].  
The `ProfileEntry` is a record, representing the values of a [Server List Profile](../profiles/index.md), meaning all values you can set in the profile configuration (Except condition and priority) will be available as a `ProfileEntry`.

It is important to note, that the `ProfileEntry` can be a representation of the [`profiles` option](../profiles/index.md#profiles), the global options or a mixture of both.  
If you want to create your own instance, you'll need to use the [`ProfileEntry.Builder` class](reference/api/ch.andre601.advancedserverlist.api/profiles/profileentry/builder.md) as the `ProfileEntry` is unmodifiable.

A convenience method in the form of `builder()` exists to create a Builder instance of the ProfileEntry with its values already pre-set.

[profileentry]: reference/api/ch.andre601.advancedserverlist.api/profiles/profileentry/index.md