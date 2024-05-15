# BanPlugins Addon

The BanPlugins Addon was made to provide placeholders for different punishment plugins to use in AdvancedServerList.

## Placeholders

/// tab | AdvancedBan
[Plugin Page](https://www.spigotmc.org/resources/8695/){ target="_blank" rel="nofollow" }

**Supported Platforms:**

- :fontawesome-solid-paper-plane: Paper
- :simple-spigotmc: BungeeCord

| Placeholder                     | Description                                                                              |
|---------------------------------|------------------------------------------------------------------------------------------|
| `${advancedban isMuted}`        | Whether the player is muted or not.                                                      |
| `${advancedban muteReason}`     | The reason for why the player was muted. Returns invalid placeholder if no mute was set. |
| `${advancedban muteDuration}`   | Relative time until the mute expires. Returns invalid placeholder if no mute was set.    |
| `${advancedban muteExpiration}` | Date on when the mute will expire-                                                       |
| `${advancedban isBanned}`       | Whether the player is banned or not.                                                     |
| `${advancedban banReason}`      | The reason for why the player was banned. Returns invalid placeholder if no ban was set. |
| `${advancedban banDuration}`    | Relative time until the ban expires. Returns invalid placeholder if no ban was set.      |
| `${advancedban banExpiration}`  | Date on when the ban will expire.                                                        |

//// note | Notes
-   `${advancedban muteDuration}` and `${advancedban banDuration}` accept an optional boolean to set, whether the mute/ban duration should be from the start.  
    Default is `false`.
-   `${advancedban muteExpiration}` and `${advancedban banExpiration}` accept an optional Date and Time pattern to format the time into.  
    Default is `dd, MMM yyyy HH:mm:ss`.
////

//// details | Example
    type: example

```yaml title="banned.yml"
priority: 1
condition: '${advancedban isBanned}'

motd:
  - '<red>You were <bold>BANNED</bold>!'
  - '<red>Expires: <grey>${advancedban banExpiration}'
```
////

///

/// tab | LibertyBans
[Plugin Page](https://modrinth.com/plugin/libertybans/){ target="_blank" rel="nofollow" }

**Supported Platforms:**

- :fontawesome-solid-paper-plane: Paper
- :simple-spigotmc: BungeeCord
- :fontawesome-solid-paper-plane: Velocity

| Placeholder                     | Description                                                                              |
|---------------------------------|------------------------------------------------------------------------------------------|
| `${libertybans isMuted}`        | Whether the player is muted or not.                                                      |
| `${libertybans muteReason}`     | The reason for why the player was muted. Returns invalid placeholder if no mute was set. |
| `${libertybans muteExpiration}` | Date on when the mute will expire.                                                       |
| `${libertybans isBanned}`       | Whether the player is banned or not.                                                     |
| `${libertybans banReason}`      | The reason for why the player was banned. Returns invalid placeholder if no ban was set. |
| `${libertybans banExpiration}`  | Date on when the ban will expire.                                                        |

//// note | Notes
- `${libertybans muteExpiration}` and `${libertybans banExpiration}` accept an optional Date and Time pattern to format the time into.  
  Default is `dd, MMM yyyy HH:mm:ss`.
////

//// details | Example
    type: example

```yaml title="banned.yml"
priority: 1
condition: '${libertybans isBanned}'

motd:
  - '<red>You were <bold>BANNED</bold>!'
  - '<red>Expires: <grey>${libertybans banExpiration}'
```
////

///

/// tab | LiteBans
[Plugin Page](https://www.spigotmc.org/resources/3715/){ target="_blank" rel="nofollow" }

**Supported Platforms:**

- :fontawesome-solid-paper-plane: Paper
- :simple-spigotmc: BungeeCord
- :fontawesome-solid-paper-plane: Velocity

| Placeholder                  | Description                                                                              |
|------------------------------|------------------------------------------------------------------------------------------|
| `${litebans isMuted}`        | Whether the player is muted or not.                                                      |
| `${litebans muteReason}`     | The reason for why the player was muted. Returns invalid placeholder if no mute was set. |
| `${litebans muteDuration}`   | Relative time until the mute expires. Returns invalid placeholder if no mute was set.    |
| `${litebans muteExpiration}` | Date on when the mute will expire-                                                       |
| `${litebans isBanned}`       | Whether the player is banned or not.                                                     |
| `${litebans banReason}`      | The reason for why the player was banned. Returns invalid placeholder if no ban was set. |
| `${litebans banDuration}`    | Relative time until the ban expires. Returns invalid placeholder if no ban was set.      |
| `${litebans banExpiration}`  | Date on when the ban will expire.                                                        |

//// note | Notes
-   `${litebans muteExpiration}` and `${litebans banExpiration}` accept an optional Date and Time pattern to format the time into.  
    Default is `dd, MMM yyyy HH:mm:ss`.
////

//// details | Example
    type: example

```yaml title="banned.yml"
priority: 1
condition: '${litebans isBanned}'

motd:
  - '<red>You were <bold>BANNED</bold>!'
  - '<red>Expires: <grey>${litebans banExpiration}'
```
////
///