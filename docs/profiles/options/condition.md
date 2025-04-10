# Condition

The `condition` option is a String that allows you to set a single or multiple requirements that need to be fulfilled to display the profile itself.

The String can contain [any valid expression](../expressions.md) that returns a boolean. Non-boolean values will be converted to one based on their value.

/// note
An empty condition String or no condition will always be treated as a true condition.
///

## Example

```yaml
priority: 1

#
# This condition returns true if the player's protocol version is
# less than 735.
#
# 735 is the protocol version for 1.16, which means this returns true
# if the player uses a version older than MC 1.16.
#
condition: '${player protocol} <= 735'
```