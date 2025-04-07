# Profiles

The `profiles` option is a List option that allows you to configure individual profiles to use.  
Each entry can use the following options:

- [`Motd`](motd.md)
- [`Favicon`](favicon.md)
- PlayerCount
    - [`HidePlayers`](playercount/hideplayers.md)
    - [`HidePlayersHover`](playercount/hideplayershover.md)
    - [`Hover`](playercount/hover.md)
    - [`Text`](playercount/text.md)
    - [`ExtraPlayers`](playercount/extraplayers.md)
    - [`MaxPlayers`](playercount/maxplayers.md)
    - [`OnlinePlayers`](playercount/onlineplayers.md)

The plugin will select an entry in the list at random. Should an option not be set will it check if it was set in the file itself to use that as a fallback.  
This allows you setups where you randomize only a specific option while keeping the other ones the same without having to copy-pasting it.

One downside is, that you cannot randomly pick a single option from the list. This means that if you have motd and favicon set for one entry will it always use said favicon and motd together.

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