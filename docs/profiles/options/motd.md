# Motd

The `motd` option is a List of Strings that sets the "Message of the day", which refers to the 2 lines displayed in the server list.  
It is the only option in the profile that supports custom RGB colors (if server is using at least 1.16) as it is the only part of the server list that supports text components. Please see the [Formatting page](../formatting.md) for a list of known and supported formatting options.

/// tip | Centering the MOTD
Since version 5.2.0 can you center the MOTD lines. To do this, prefix each line with `<center>`.  
It is important that this placeholder is at the very start, even before any color or formatting tags!

Versions before 5.2.0 do not have this feature and need to manually center the text by prefixing the lines with spaces.
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