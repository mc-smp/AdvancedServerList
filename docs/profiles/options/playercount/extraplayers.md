# ExtraPlayers

The `extraPlayers` option contains additional options to enable and set the extra players feature.  
The extra players feature modifies the displayed max players count by taking the current online player count, adding the configured [`amount`](#amount) to it and using that as the number.

/// warning | Important
Using this option will affect the output of the `${server playersMax}` option, except when used within the [`condition`](../condition.md) option.  
This option is also ignored should the [`hidePlayers`](hideplayers.md) option be enabled.
///

## Enabled

Sets the enabled state of this option.

## Amount

Sets the amount that should be added to the online player count. Negative numbers are supported!  
Placeholders that resolve into numbers can be used.

## Example

```yaml
priority: 0

motd:
  - '<grey>'
  - '<grey>'

playerCount:
  extraPlayers:
    enabled: true
    amount: 1
```
/// html | div.result
![extraplayers-example](../../../assets/images/examples/extraplayers-example.jpg){ loading="lazy" }
///