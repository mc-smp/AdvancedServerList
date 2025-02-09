---
icon: octicons/arrow-right-24
---

Version `4.10.0` added support for migrating your existing `main.conf` configuration from MiniMOTD to AdvancedServerList.

## Important Notes

- MiniMOTD does not need to be present on the server/proxy to migrate its files. Only the `main.conf` file needs to be available.
- The plugin will **only** migrate the `main.conf` file. No other files will be migrated.

## How to Migrate

Execute the command [`/asl migrate minimotd`](../../commands/migrate.md) to start the migration process. Make sure the `main.conf` file is present in `/plugins/MiniMOTD/` (`minimotd-velocity` on Velocity).

## Example

Below can you find a full example of a `main.conf` file and the corresponding migrated files from AdvancedServerList:

/// tab | :octicons-file-code-24: main.conf
```ruby
icon-enabled=true
motd-enabled=true
motds=[
    {
        icon=random
        line1="<rainbow>MiniMOTD Default"
        line2="MiniMessage <gradient:blue:red>Gradients"
    },
    {
        icon=random
        line1="<blue>Another <bold><red>MOTD"
        line2="<italic><underlined><gradient:red:green>much wow"
    }
]
player-count-settings {
    allow-exceeding-maximum=false
    disable-player-list-hover=false
    fake-players {
        fake-players="25%"
        fake-players-enabled=false
    }
    hide-player-count=false
    just-x-more-settings {
        just-x-more-enabled=false
        x-value=3
    }
    max-players=69
    max-players-enabled=true
    servers=[]
}
```
///

/// tab | :octicons-file-code-24: minimotd_migrated.yml
```yaml
priority: 0
profiles:
- motd:
  - <rainbow>MiniMOTD Default
  - MiniMessage <gradient:blue:red>Gradients
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
  - <blue>Another <bold><red>MOTD
  - <italic><underlined><gradient:red:green>much wow
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
  hover: []
  text: ''
  hidePlayers: false
  extraPlayers:
    enabled: false
    amount: 3
  maxPlayers:
    enabled: true
    amount: 69
favicon: ''
```
///