---
icon: octicons/broadcast-24
---

AdvancedServerList provides some events that your plugin can listen to.  
These events are structurally the same across all platforms, yet still require the usage of Platform-specific APIs due to how some events are registered and handled on some platforms.

The plugin currently offers the following events:

/// html | div.grid.cards
- [PreServerListSetEvent](#preserverlistsetevent)
- [PostServerListSetEvent](#postserverlistsetevent)
///

## PreServerListSetEvent

This event is executed before AdvancedServerList tries to modify the player list.  

### Methods { #preserverlistsetevent-methods }

The following methods are available to use:

| Method                                  | Description                                                                    |
|-----------------------------------------|--------------------------------------------------------------------------------|
| [`getEntry()`][getentry]                | Returns the [`ProfileEntry`][profileentry] that AdvancedServerList should use. |
| [`setEntry(ProfileEntry)`][setentry]    | Sets the [`ProfileEntry`][profileentry] that should be used.                   |
| [`isCancelled()`][iscancelled]          | Returns wether this event was cancelled or not.                                |
| [`setCancelled(boolean)`][setcancelled] | Sets the event's cancelled state.                                              |

[setentry]: reference/api/ch.andre601.advancedserverlist.api/events/genericserverlistevent.md#setentry
[iscancelled]: reference/api/ch.andre601.advancedserverlist.api/events/genericserverlistevent.md#iscancelled
[setcancelled]: reference/api/ch.andre601.advancedserverlist.api/events/genericserverlistevent.md#setcancelled

### Notes { #preserverlistsetevent-notes }

- Providing `null` as the `ProfileEntry` for `setEntry(ProfileEntry)` is not allowed. Also, a copy of the provided `ProfileEntry` is made and used.

## PostServerListSetEvent

This event is executed after AdvancedServerList completed the server list modifications.

### Methods { #postserverlistsetevent-methods }

| Method                   | Description                                                              |
|--------------------------|--------------------------------------------------------------------------|
| [`getEntry()`][getentry] | Returns the [`ProfileEntry`][profileentry] that AdvancedServerList used. |

[getentry]: reference/api/ch.andre601.advancedserverlist.api/events/genericserverlistevent.md#getentry

### Notes { #postserverlistsetevent-notes }

-   The returned `ProfileEntry` is not an actual representation of what is being displayed to the player and rather an instance of values the plugin received.
    The returned value may also be `null` should the Ping Event be cancelled due to things such as an invalid Protocol or the [`PreServerListSetEvent`](#preserverlistsetevent) being cancelled.

## Registering the Events

/// tab | :fontawesome-solid-paper-plane: Paper
```java { title="MyPlugin.java" .annotate }
public class MyPlugin extends JavaPlugin {
    
    @Override
    public void onEnable() {
        // (1)
        Bukkit.getPluginManager().registerEvents(new ASLEventListener(), this);
    }
}
```

1. `getServer()` from the `JavaPlugin` class can be used here too instead of the `Bukkit` singleton.

```java { title="ASLEventListener.java" }
public class ASLEventListener implements Listener {
    
    @EventHandler
    public void onPreServerListSet(PreServerListSetEvent event) {
        System.out.println("PreServerListSetEvent fired!");
    }
    
    @EventHandler
    public void onPostServerListSetEvent(PostServerListSetEvent event) {
        System.out.println("PostServerListSetEvent fired!");
    }
}
```
///

/// tab | :simple-spigotmc: BungeeCord
```java { title="MyPlugin.java" }
public class MyPlugin extends Plugin {
    
    @Override
    public void onEnable() {
        getProxy().getPluginManager().registerListener(this, new ASLEventListener());
    }
}
```

```java { title="ASLEventListener.java" }
public class ASLEventListener implements Listener {
    
    @EventHandler
    public void onPreServerListSet(PreServerListSetEvent event) {
        System.out.println("PreServerListSetEvent fired!");
    }
    
    @EventHandler
    public void onPostServerListSetEvent(PostServerListSetEvent event) {
        System.out.println("PostServerListSetEvent fired!");
    }
}
```
///

/// tab | :simple-velocity: Velocity
```java { title="MyPlugin.java" }
@Plugin(
    id = "myplugin",
    name = "MyPlugin",
    version = "1.0.0",
    authors = {"author"},
    dependencies = {
        @Dependency(
            id = "advancedserverlist",
        )
    }
)
public class MyPlugin {
    
    private final ProxyServer proxy;
    
    @Inject
    public MyPlugin(ProxyServer server) {
        this.proxy = proxy;
    }
    
    @Subscribe
    public void init(ProxyInitializeEvent event) {
        proxy.getEventManager().register(this, new ASLEventListener());
    }
}
```

```java { title="ASLEventListener.java" }
public class ASLEventListener {
    
    @Subscribe
    public void onPreServerListSet(PreServerListSetEvent event) {
        System.out.println("PreServerListSetEvent fired!");
    }
    
    @Subscribe
    public void onPostServerListSetEvent(PostServerListSetEvent event) {
        System.out.println("PostServerListSetEvent fired!");
    }
}
```
///

## What is a ProfileEntry?

Both events provide and use a so called [`ProfileEntry`][profileentry].  
The `ProfileEntry` is a record, representing the values of a [Server List Profile](../profiles/index.md), meaning all values you can set in the profile configuration (Except condition and priority) will be available as a `ProfileEntry`.

It is important to note, that the `ProfileEntry` can be a representation of the [`profiles` option](../profiles/index.md#profiles), the global options or a mixture of both.  
If you want to create your own instance, you'll need to use the [`ProfileEntry.Builder` class](reference/api/ch.andre601.advancedserverlist.api/profiles/profileentry/builder.md) as the `ProfileEntry` is unmodifiable.

A convenience method in the form of [`builder()`][builder] exists to create a Builder instance of the ProfileEntry with its values already pre-set.

[profileentry]: reference/api/ch.andre601.advancedserverlist.api/profiles/profileentry/index.md
[builder]: reference/api/ch.andre601.advancedserverlist.api/profiles/profileentry/index.md#builder