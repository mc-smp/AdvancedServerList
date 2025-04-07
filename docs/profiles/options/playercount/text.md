# Text

The `text` option is a String that sets the text usually displaying the player count.  
This option only supports basic color and formatting options. In addition will your server be displayed as "outdated" (The ping icon will appear crossed out). This is due to the plugin manipulating the "outdated server/client" text usually displayed if your client and server version do not match. This is an issue that cannot be fixed by the plugin and is something other plugins offering this feature also suffer from.

## Example

```yaml
priority: 0

motd:
  - '<grey>'
  - '<grey>'

playerCount:
  text: '<yellow><bold>Cool text!'
```
/// html | div.result
![text-example](../../../assets/images/examples/text-example.jpg){ loading="lazy" }
///