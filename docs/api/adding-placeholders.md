---
icon: octicons/plus-circle-24
---

AdvancedServerList allows you to add your own Placeholders to be useable through its `${identifier value}` format.  
The benefit of this method over alternatives such as using PlaceholderAPI placeholders is availability and the fact that these placeholders can be used within the [`condition` option](../profiles/options/condition.md) of the plugin.

Adding your own placeholder is relatively easy and only requires the main API module to be used in your dependencies, unless you need platform-specific values (Explained later on).

## Create your PlaceholderProvider class

AdvancedServerList's placeholder system uses a dedicated [`PlaceholderProvider`](reference/api/ch.andre601.advancedserverlist.api/placeholderprovider.md) class where the placeholder gets defined and its values parsed.  
In order to add your own, you'll need to first create a new class and make it extend the `PlaceholderProvider` class:

```java title="MyPlaceholder.java"
public class MyPlaceholder extends PlaceholderProvider {
    
}
```

Your IDE should now report issues, namely a missing method to override and a missing Constructor to match the extended class' one. Your IDE should allow you to easily implement those, if not, then manually add the constructor with a `String` argument and the `public String parsePlaceholder` method with `String`, [`GenericPlayer`](reference/api/ch.andre601.advancedserverlist.api/objects/genericplayer.md) and [`GenericServer`](reference/api/ch.andre601.advancedserverlist.api/objects/genericserver.md) arguments:

```java { title="MyPlaceholder.java" .annotate }
public class MyPlaceholder extends PlaceholderProvider {
    
    public MyPlaceholder(String identifier) {
        super(identifier); // (1)
    }
    
    @Override
    public String parsePlaceholder(String placeholder, GenericPlayer player, GenericServer server) {
        return null;
    }
}
```

1.  It is recommended to replace the Constructor with a No-Args Constructor and set the placeholder name directly in the `super` call:  
    ```java
    public MyPlaceholder() {
        super("myplaceholder");
    }
    ```

## Parsing placeholders

To now parse your placeholders, all you need to do is edit the `parsePlaceholder` method.  
AdvancedServerList calls this method whenever it finds a matching identifier of a PlaceholderProvider, with the `String` argument being the actual value after the identifier and the `GenericPlayer` and `GenericServer` arguments holding the Player and server values respectively.

As an example, assuming you simply want to return `Hello` for the placeholder `${myplaceholder sayHello}`, this would be the code for it:

```java { title="MyPlaceholder.java" .annotate }
public class MyPlaceholder extends PlaceholderProvider {
    
    public MyPlaceholder() {
        super("myplaceholder");
    }
    
    @Override
    public String parsePlaceholder(String placeholder, GenericPlayer player, GenericServer server) {
        if (placeholder.equals("sayHello")) {
            return "Hello";
        }
        
        return null; // (1)
    }
}
```

1. Returning `null` tells AdvancedServerList that the placeholder was invalid, making it return the placeholder unchanged, while also reporting an error, if used in `condition`.

## Registering the Placeholder

The final step now would be to register your placeholder for AdvancedServerList to use.  
To do this, obtain an instance of the [`AdvancedServerListAPI`](reference/api/ch.andre601.advancedserverlist.api/advancedserverlistapi.md) through its static `get()` method and then call `addPlaceholderProvider(PlaceholderProvider)` through it:

/// tab | :fontawesome-solid-paper-plane: Paper Example
```java title="MyPlugin.java"
public class MyPlugin extends JavaPlugin {
    
    @Override
    public void onEnable() {
        AdvancedServerListAPI api = AdvancedServerListAPI.get();
        api.addPlaceholderProvider(new MyPlaceholder());
        getLogger().info("Hooked into AdvancedServerList!");
    }
}
```
///

/// tab | :octicons-git-merge-24: BungeeCord Example
```java title="MyPlugin.java"
public class MyPlugin extends Plugin {
    
    @Override
    public void onEnable() {
        AdvancedServerListAPI api = AdvancedServerListAPI.get();
        api.addPlaceholderProvider(new MyPlaceholder());
        getLogger().info("Hooked into AdvancedServerList!");
    }
}
```
///

/// tab | :simple-velocity: Velocity Example
```java title="MyPlugin.java"
@Plugin(
    id = "myplugin",
    version = "1.0.0",
    authors = {"You?"},
    dependencies = {
        @Dependency(id = "advancedserverlist")
    }
)
public class MyPlugin {
    
    private final ProxyServer proxy;
    private final Logger logger;
    
    @Inject
    public MyPlugin(ProxyServer proxy, Logger logger) {
        this.proxy = proxy;
        this.logger = logger;
    }
    
    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        AdvancedServerListAPI api = AdvancedServerListAPI.get();
        api.addPlaceholderProvider(new MyPlaceholder());
        logger.info("Hooked into AdvancedServerList!");
    }
}
```
///

## You're done

Your Placeholders should now be registered successfully if they check the following:

- The provided identifier is not `null` nor empty.
- The provided identifier does not contain spaces.
- The provided identifier is not already registered in the API.

## Note on `GenericPlayer` and `GenericServer`

The different platform APIs of AdvancedServerList provide extended versions of the `GenericPlayer` and `GenericServer` instances used, providing additional methods otherwise not available.  
Should you want to use these specific instances will you need to add the platform API as an additional dependency to your project and then cast the `GenericPlayer` and/or `GenericServer` instance to the platform specific version.

Below is an example using the `platform-bukkit` API (This assumes your plugin is one running on Paper):

```java { title="MyPlaceholder.java" .annotate }
public class MyPlaceholder extends PlaceholderProvider {
    
    public MyPlaceholder() {
        super("myplaceholder");
    }
    
    @Override
    public String parsePlaceholder(String placeholder, GenericPlayer player, GenericServer server) {
        if (!(player instanceof BukkitPlayer bukkitPlayer)) {
            return null; // (1)
        }
        
        return switch (placeholder) {
            case "playedBefore" -> String.valueOf(bukkitPlayer.hasPlayedBefore());
            case "whitelisted" -> String.valueOf(bukkitPlayer.isWhitelisted());
            case "banned" -> String.valueOf(bukkitPlayer.isBanned());
            default -> null;
        };
    }
}
```

1.  Depending on the Java version you use will you need to alter the code:  
    ```java
    if (!(player instanceof BukkitPlayer)) {
        return null;
    }
    
    // You need to cast it separately.
    BukkitPlayer bukkitPlayer = (BukkitPlayer) player;
    
    // ...use bukkitPlayer here.
    ```