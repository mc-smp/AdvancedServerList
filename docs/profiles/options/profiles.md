# Profiles

The `profiles` option is a List option that allows you to define multiple entries for a single file to use, providing a way of randomly displaying an MOTD, Favicon, etc.

Each list entry can contain any combination of the following options:

- [`motd`](motd.md)
- [`favicon`](favicon.md)
- `playerCount`
    - [`hidePlayers`](playercount/hideplayers.md)
    - [`hidePlayersHover`](playercount/hideplayershover.md)
    - [`hover`](playercount/hover.md)
    - [`text`](playercount/text.md)
    - [`extraPlayers`](playercount/extraplayers.md)
    - [`maxPlayers`](playercount/maxplayers.md)
    - [`onlinePlayers`](playercount/onlineplayers.md)

When present with at least one entry will the plugin select an entry at random, using the provided option values to display.  
Should an entry not have an option present will the plugin check the file itself for the option to use. As an example, not setting `favicon` in the list will have AdvancedServerList check the file itself for a `favicon` option to use.  
This allows you to only randomize a specific option of a profile while keeping the other ones static (i.e. have a randomized MOTD while the Favicon remains the same).

## Example

```yaml
priority: 0

#
# This example has two profiles where both change the 'playerCount -> text' option
# but only the first entry also changes the motd, resulting in the second one
# using the motd option in the file itself.
#
profiles:
  - motd: ['<rainbow>Line 1</rainbow>', '<rainbow:!>Line 2</rainbow>']
    playerCount:
      text: '<green><bold>Awesome!'
  - playerCount:
      text: '<yellow><bold>Also Awesome!'

#
# This MOTD is only used should the second profiles option be picked as it does not
# have a motd option set.
#
motd:
  - '<rainbow:2>Line A</rainbow>'
  - '<rainbow:!2>Line B</rainbow>'
```
/// html | div.result
![profiles-example-1](../../assets/images/examples/profiles-example-1.jpg){ loading="lazy" }
![profiles-example-2](../../assets/images/examples/profiles-example-2.jpg){ loading="lazy" }
///