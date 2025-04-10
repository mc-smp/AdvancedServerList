# Priority

The `priority` option is a number that determines the order in which AdvancedServerList should go through the individual files, should there be multiple.  
The plugin will go from highest number to lowest number and select the first profile whos [`condition`](condition.md) option returns true.

## Example

```yaml
priority: 0
```