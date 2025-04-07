# MaxPlayers

The `maxPlayers` option contains additional options to enable and set the max players feature.  
The max players feature modifies the displayed max players count by setting the configured [`amount`](#amount) to it.

/// warning | Important
Using this option will affect the output of the `${server playersMax}` option, except when used within the [`condition`](../condition.md) option.  
This option is also ignored should the [`hidePlayers`](hideplayers.md) or [`extraPlayers`](extraplayers.md) option be enabled.
///

## Enabled

Sets the enabled state of this option.

## Amount

Sets the amount that should be displayed as the max players count!  
Placeholders that resolve into numbers can be used.

## Example

```yaml
priority: 0

motd:
  - '<grey>'
  - '<grey>'

playerCount:
  maxPlayers:
    enabled: true
    amount: -1
```
/// html | div.result
![maxplayers-example](../../../assets/images/examples/maxplayers-example.jpg){ loading="lazy" }
///