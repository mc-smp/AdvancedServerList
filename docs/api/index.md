---
icon: octicons/command-palette-24
---

# API

AdvancedServerList offers an API that other plugins can use to add their own placeholder to the plugin, listen for specific events or even modify Server List Profiles on the fly.

## Add dependency

Add the following to your `pom.xml`, `build.gradle` or `build.gradle.kts` depending on what you use:

/// tab | :simple-apachemaven: Maven 
```xml { data-md-component="api-version" title="pom.xml" }
<repositories>
  <repository>
    <id>codeberg</id>
    <url>https://codeberg.org/api/packages/Andre601/maven/</url>
  </repository>
</repositories>

<dependencies>
  <dependency>
    <groupId>ch.andre601.asl-api</groupId>
    <artifactId>api</artifactId>
    <version>{apiVersion}</version>
    <scope>provided</scope>
  </dependency>
  
  <!-- Optional platform dependencies -->
  <dependency>
    <groupId>ch.andre601.asl-api</groupId>
    <artifactId>platform-bukkit</artifactId>
    <version>{apiVersion}</version>
    <scope>provided</scope>
  </dependency>
  <dependency>
    <groupId>ch.andre601.asl-api</groupId>
    <artifactId>platform-bungeecord</artifactId>
    <version>{apiVersion}</version>
    <scope>provided</scope>
  </dependency>
  <dependency>
    <groupId>ch.andre601.asl-api</groupId>
    <artifactId>platform-velocity</artifactId>
    <version>{apiVersion}</version>
    <scope>provided</scope>
  </dependency>
</dependencies>
```
///

/// tab | :simple-gradle: Gradle
```groovy { data-md-component="api-version" title="build.gradle" }
repositorories {
    maven { url = 'https://codeberg.org/api/packages/Andre601/maven/' }
}

dependencies {
    compileOnly 'ch.andre601.asl-api:api:{apiVersion}'
    
    // Optional platform dependencies
    compileOnly 'ch.andre601.asl-api:platform-bukkit:{apiVersion}'
    compileOnly 'ch.andre601.asl-api:platform-bungeecord:{apiVersion}'
    compileOnly 'ch.andre601.asl-api:platform-velocity:{apiVersion}'
}
```
///

/// tab | :simple-gradle: Gradle (KTS)
```kotlin { data-md-component="api-version" title="build.gradle.kts" }
repositories {
    maven("https://codeberg.org/api/packages/Andre601/maven/")
}

dependencies {
    compileOnly("ch.andre601.asl-api:api:{apiVersion}")
    
    // Optional platform dependencies
    compileOnly("ch.andre601.asl-api:platform-bukkit:{apiVersion}")
    compileOnly("ch.andre601.asl-api:platform-bungeecord:{apiVersion}")
    compileOnly("ch.andre601.asl-api:platform-velocity:{apiVersion}")
}
```
///

## Tutorials

Dedicated pages to explain aspects of the API are available:

/// html | div.grid.cards
- [:octicons-plus-circle-24: Adding Placeholders](adding-placeholders.md)
- [:octicons-broadcast-24: Listening for Events](listening-for-events.md)
///

## Declare AdvancedServerList as (soft)depend

The final thing you should make sure is to define AdvancedServerList as a depend or soft-depend for your plugin, to make sure it loads after AdvancedServerList.

Below are example setups for Spigot, Paper, BungeeCord and Velocity:

/// tab | :fontawesome-solid-paper-plane: Paper
//// tab | Softdepend
```yaml title="paper-plugin.yml"
name: "MyPlugin"
author: "author"
version: "1.0.0"

main: "com.example.plugin.ExamplePlugin"

dependencies:
  server:
    AdvancedServerList:
      load: BEFORE
      required: false # Default, not required
```
////

//// tab | Depend
```yaml title="paper-plugin.yml"
name: "MyPlugin"
author: "author"
version: "1.0.0"

main: "com.example.plugin.ExamplePlugin"

dependencies:
  server:
    AdvancedServerList:
      load: BEFORE
      required: true
```
////
///

/// tab | :simple-spigotmc: BungeeCord
//// tab | Softdepend
```yaml title="bungee.yml"
name: "MyPlugin"
author: "author"
version: "1.0.0"

main: "com.example.plugin.ExamplePlugin"

softDepends:
  - AdvancedServerList
```
////

//// tab | Depend
```yaml title="bungee.yml"
name: "MyPlugin"
author: "author"
version: "1.0.0"

main: "com.example.plugin.ExamplePlugin"

depends:
  - AdvancedServerList
```
////
///

/// tab | :simple-velocity: Velocity
//// tab | Softdepend (File)
```json title="velocity-plugin.json"
{
  "id": "myplugin",
  "name": "MyPlugin",
  "version": "1.0.0",
  "authors": [
    "author"
  ],
  "main": "com.example.plugin.ExamplePlugin",
  "dependencies": [
    {
      "id": "advancedserverlist",
      "optional": true
    }
  ]
}
```
////

//// tab | Depend (File)
```json title="velocity-plugin.json"
{
  "id": "myplugin",
  "name": "MyPlugin",
  "version": "1.0.0",
  "authors": [
    "author"
  ],
  "main": "com.example.plugin.ExamplePlugin",
  "dependencies": [
    {
      "id": "advancedserverlist",
      "optional": false // Default, not required
    }
  ]
}
```
////

//// tab | Softdepend (Annotation)
```java title="MyPlugin.java"
@Plugin(
  id = "myplugin",
  name = "MyPlugin",
  version = "1.0.0",
  authors = {"author"},
  dependencies = {
    @Dependency(
      id = "advancedserverlist",
      optional = true
    )
  }
)
public class MyPlugin {
  
  // ...
  
}
```
////

//// tab | Depend (Annotation)
```java title="MyPlugin.java"
@Plugin(
  id = "myplugin",
  name = "MyPlugin",
  version = "1.0.0",
  authors = {"author"},
  dependencies = {
    @Dependency(
      id = "advancedserverlist",
      optional = false // Default, not required
    )
  }
)
public class MyPlugin {
  
  // ...
  
}
```
////
///

### 4. You're done!

Your plugin should now hook into AdvancedServerList and register its own custom placeholders to use.