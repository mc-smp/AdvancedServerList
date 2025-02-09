---
icon: octicons/arrow-right-24
---

# ServerListPlus

Version `v4.8.0-b1` added support for migrating your existing `ServerListPlus.yml` configuration to AdvancedServerList.

## Important notes

Migration requires that ServerListPlus is present and running on the server.  
This is due to AdvancedServerList using the internal API of ServerListPlus to retrieve the Configuration used.

## Conversions

Specific values within the ServerListPlus configuration will be converted into equivalents supported by AdvancedServerList.

### Placeholders

The following placeholders get converted:

| Placeholder             | Conversion                                                                        |
|-------------------------|-----------------------------------------------------------------------------------|
| `%player%`              | `${player name}`                                                                  |
| `%uuid%` and `%_uuid_%` | `${player uuid}`                                                                  |
| `%online%`              | `${server playersOnline}`                                                         |
| `%max%`                 | `${server playersMax}`                                                            |

### Colors/Formatting

The following color and formatting codes get converted[^1]:

| Color/Formatting code                      | Conversion                                                                    |
|--------------------------------------------|-------------------------------------------------------------------------------|
| `&<color/format>`                          | Corresponding color/formatting code in MiniMessage (i.e. `&1 -> <dark_blue>`) |
| `&#<hexcode>`                              | `<#<hexcode>>`                                                                |
| `%gradient#<start>#<end>%<text>%gradient%` | `<gradient:#<start>:#<end>><text></gradient>`                                 |

### Favicon options

The following Favicon options get converted into an equivalent one:

| Option  | Conversion                                                                      |
|---------|---------------------------------------------------------------------------------|
| `Heads` | `https://mc-heads.net/avatar/<entry>/64/nohelm`                                 |
| `Helms` | Returned as-is with [placeholders replaced](#placeholders).                     |
| `Files` | Only `.png` files will be included with [placeholders replaced](#placeholders). |

## How to Migrate

Execute the command [`/asl migrate serverlistplus`](../../commands/migrate.md) to start the migration process. Make sure that ServerListPlus is present and enabled.

## Example

Below can you find a full example of a ServerListPlus.yml file and the corresponding migrated files from AdvancedServerList:

/// tab | :octicons-file-code-24: ServerListPlus.yml
```yaml
--- !Status
Default:
  Description:
  - |-
    &aA Minecraft Server.
    &7Now with [&a&lPvP&7], [&a&lMinigames&7], and much more!
  - |-
    &aA Minecraft Server.
    &eNow running the latest &lMinecraft &eversion!
  - |-
    &a&#45bf55A Minecraft &#0288d1Server.
    &e%gradient#ffbe00#f4511e%Now with RGB colors. Nice!%gradient%
  Players:
    Hover:
    - |-
      &aWelcome to &lA Minecraft Server&a!
      &aCurrently &e&l%online%/%max% &aplayers are playing on our server!
Personalized:
  Description:
  - |-
    &aA Minecraft Server. &7|  &eHello, &l%player%!
    &7Now with [&a&lPvP&7], [&a&lMinigames&7], and much more!
  - |-
    &aA Minecraft Server. &7|  &eHello, &l%player%!
    &eNow running the latest &lMinecraft &eversion!
  - |-
    &a&#45bf55A Minecraft &#0288d1Server.
    &e%gradient#ffbe00#f4511e%Now with RGB colors. Hello %player%!%gradient%
  Players:
    Hover:
    - |-
      &aWelcome back, &l%player%&a!
      &aCurrently &e&l%online%/%max% &aplayers are playing on our server!
  Favicon:
    Files:
    - favicon1.png
    - favicon2.png
Banned:
  Description:
  - |-
    &cYou are &4&lBANNED!
    &cGo away!
  Players:
    Hidden: true
 
--- !Plugin
PlayerTracking:
  Enabled: true
  Storage: !JSONStorage
    Enabled: true
    SaveDelay: 5m
StripRGBIfOutdated: true
Unknown:
  PlayerName: player
  PlayerCount: ???
  Date: ???
  BanReason: some reason
  BanOperator: somebody
  BanExpirationDate: never
Favicon:
  Timeout: 10s
  RecursiveFolderSearch: true
  ResizeStrategy: SCALE

--- !Bukkit
ProtocolLib: DISABLE
```
///

/// tab | :octicons-file-code-24: slp_default.yml
```yaml
priority: 0
profiles:
- motd:
  - <green>A Minecraft Server.
  - <grey>Now with [<green><bold>PvP<grey>], [<green><bold>Minigames<grey>], and much
    more!
  playerCount:
    hover: []
    text: ''
    hidePlayers: false
    extraPlayers:
      enabled: false
      amount: 0
    maxPlayers:
      enabled: false
      amount: 0
  favicon: ''
- motd:
  - <green>A Minecraft Server.
  - <yellow>Now running the latest <bold>Minecraft <yellow>version!
  playerCount:
    hover: []
    text: ''
    hidePlayers: false
    extraPlayers:
      enabled: false
      amount: 0
    maxPlayers:
      enabled: false
      amount: 0
  favicon: ''
- motd:
  - <green><#45bf55>A Minecraft <#0288d1>Server.
  - <yellow><gradient:#ffbe00:#f4511e>Now with RGB colors. Nice!</gradient>
  playerCount:
    hover: []
    text: ''
    hidePlayers: false
    extraPlayers:
      enabled: false
      amount: 0
    maxPlayers:
      enabled: false
      amount: 0
  favicon: ''
motd: []
playerCount:
  hover:
  - <green>Welcome to <bold>A Minecraft Server<green>!
  - <green>Currently <yellow><bold>${server playersOnline}/${server playersMax} <green>players
    are playing on our server!
  text: ''
  hidePlayers: false
  extraPlayers:
    enabled: false
    amount: 0
  maxPlayers:
    enabled: false
    amount: 0
favicon: ''
```
///

/// tab | :octicons-file-code-24: slp_personalized.yml
```yaml
priority: 1
condition: ${player name} != "Someone"
profiles:
- motd:
  - <green>A Minecraft Server. <grey>|  <yellow>Hello, <bold>${player name}!
  - <grey>Now with [<green><bold>PvP<grey>], [<green><bold>Minigames<grey>], and much
    more!
  playerCount:
    hover: []
    text: ''
    hidePlayers: false
    extraPlayers:
      enabled: false
      amount: 0
    maxPlayers:
      enabled: false
      amount: 0
  favicon: favicon1.png
- motd:
  - <green>A Minecraft Server. <grey>|  <yellow>Hello, <bold>${player name}!
  - <yellow>Now running the latest <bold>Minecraft <yellow>version!
  playerCount:
    hover: []
    text: ''
    hidePlayers: false
    extraPlayers:
      enabled: false
      amount: 0
    maxPlayers:
      enabled: false
      amount: 0
  favicon: favicon2.png
- motd:
  - <green><#45bf55>A Minecraft <#0288d1>Server.
  - <yellow><gradient:#ffbe00:#f4511e>Now with RGB colors. Hello ${player name}!</gradient>
  playerCount:
    hover: []
    text: ''
    hidePlayers: false
    extraPlayers:
      enabled: false
      amount: 0
    maxPlayers:
      enabled: false
      amount: 0
  favicon: ''
motd: []
playerCount:
  hover:
  - <green>Welcome back, <bold>${player name}<green>!
  - <green>Currently <yellow><bold>${server playersOnline}/${server playersMax} <green>players
    are playing on our server!
  text: ''
  hidePlayers: false
  extraPlayers:
    enabled: false
    amount: 0
  maxPlayers:
    enabled: false
    amount: 0
favicon: ''
```
///

/// tab | :octicons-file-code-24: slp_banned.yml
```yaml
priority: 1
condition: ${player isBanned}
motd:
- <red>You are <dark_red><bold>BANNED!
- <red>Go away!
playerCount:
  hover: []
  text: ''
  hidePlayers: true
  extraPlayers:
    enabled: false
    amount: 0
  maxPlayers:
    enabled: false
    amount: 0
favicon: ''
```
///

[^1]:
    Converting of color and formatting codes is done lazely and without context.  
    This means that a text such as `&aHello &l%player%&a!` gets converted into `<aqua>Hello <bold>${player name}<aqua>!` when it actually should be `<aqua>Hello <bold>${player name}</bold>!`