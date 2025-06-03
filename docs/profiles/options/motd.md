# Motd

The `motd` option is a List of Strings that sets the "Message of the day", referring to the two lines of text shown in the Multiplayer Server List.  
This is the only option that supports RGB colors, if the server and client use at least version 1.16 of Minecraft.

/// tip | Centering the MOTD
Centering the MOTD can be done automatically by prefixing the lines with `<center>` (Even before color and formatting text), but only since version 5.2.0.  
Before this version, you would need to manually center the text by adding spaces yourself.
///

## Example

```yaml
priority: 0

#
# Creates a MOTD with a smooth Rainbow gradient.
#
motd:
  - '<rainbow:2>Rainbow Gradients</rainbow>'
  - '<rainbow!2>|||||||||||||||||||||||||||||||||||||</rainbow>'
```
/// html | div.result
![motd-example](../../assets/images/examples/motd-example.jpg)
///