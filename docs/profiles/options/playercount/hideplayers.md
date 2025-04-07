# HidePlayers

The `hidePlayers` option is a boolean that sets wether the player count should be displayed or not.  
When set to `true` will the player counte be replaced with three question marks. This also disables the [`hover`](hover.md) option.

## Example

```yaml
priority: 0

motd:
  - '<grey>'
  - '<grey>'

playerCount:
  hidePlayers: true
```
/// html | div.result
![hideplayers-example](../../../assets/images/examples/hideplayers-example.jpg){ loading="lazy" }
///