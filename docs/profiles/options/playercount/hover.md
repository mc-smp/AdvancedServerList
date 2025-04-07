# Hover

The `hover` option is a String list that overrides the Hover of the player count that usually displays the players currently on the server.  
This option only supports basic color and formatting options, meaning you can't use RGB colors here.

There can also be an issue with the hover showing "...and X more" at the bottom. This is an issue that cannot be fixed by the plugin itself unfortunately.

## Example

```yaml
priority: 0

motd:
  - '<grey>'
  - '<grey>'

playerCount:
  hover:
    - '<grey>Line 1'
    - '<aqua>Line 2'
    - '<gold>Line 3'
```
/// html | div.result
![hover-example](../../../assets/images/examples/hover-example.jpg){ loading="lazy" }
///