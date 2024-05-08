---
icon: octicons/arrow-right-24
---

# ServerListPlus

Version `v4.8.0-b1` added support for migrating your existing `ServerListPlus.yml` configuration to AdvancedServerList.

## Important notes

-   Migration requires that ServerListPlus is present and running, as AdvancedServerList will use its internal code to process the file.
-   The files `slp_default.yml`, `slp_personalized.yml` and `slp_banned.yml` will be created, depending on if the corresponding type (`Default`, `Personalized` and `Banned`) are present.  
    Should a file with the same name be found will it be skipped.
-   The plugin will convert placeholders and color/formatting codes in the following way:
    
    | Placeholder/Color/Format                   | Conversion                                                                        |
    |--------------------------------------------|-----------------------------------------------------------------------------------|
    | `%player%`                                 | `${player name}`                                                                  |
    | `%uuid%` and `%_uuid_%`                    | `${player uuid}`                                                                  |
    | `%online%`                                 | `${server playersOnline}`                                                         |
    | `%max%`                                    | `${server playersMax}`                                                            |
    | `&<color/format>`                          | Corresponding color/formatting code in MiniMessage (i.e. `&1 -> <dark_blue>`)[^1] |
    | `&#<hexcode>`                              | `<#<hexcode>>`                                                                    |
    | `%gradient#<start>#<end>%<text>%gradient%` | `<gradient:#<start>:#<end>><text></gradient>`                                     |

## How to Migrate

Execute the command [`/asl migrate serverlistplus`](../commands/index.md#migrate) to start the migration process. Make sure that ServerListPlus is present and enabled.

[^1]: The color/formatting code conversion is lazy and not context sensitive, meaning that a case like `&1Hello &l%player%&1!` will be converted to `<dark_blue>Hellow <bold>${player name}<dark_blue>` when it should be `<dark_blue>Hello <bold>${player name}</bold>!`. So keep that in mind.