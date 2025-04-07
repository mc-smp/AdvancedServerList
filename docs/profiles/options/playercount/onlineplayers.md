# OnlinePlayers

The `onlinePlayers` option contains additional options to enable and set the online players feature.  
The online players feature modifies the displayed online players count by setting the configured [`amount`](#amount) to it.

/// warning | Important
This option affects the result of the [`extraPlayers`](extraplayers.md) option.
///

## Enabled

Sets the enabled state of this option.

## Amount

Sets the amount that should be displayed as the online players count!  
Placeholders that resolve into numbers can be used.

## Example

```yaml
priority: 0

motd:
  - '<grey>'
  - '<grey>'

playerCount:
  onlinePlayers:
    enabled: true
    amount: 10
```
/// html | div.result
![onlineplayers-example](../../../assets/images/examples/onlineplayers-example.jpg){ loading="lazy" }
///