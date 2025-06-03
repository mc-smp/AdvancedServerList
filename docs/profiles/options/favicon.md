# Favicon

/// info | Favicon Merging (New since 5.6.0)
You can define multiple entries to be merged into a single Favicon by separating them with a semicolon.  
Example: `background.png;${player uuid};foreground.png`
///

The `favicon` option is a String that sets the image that should be displayed in the server list.  
The final image itself is 64x64 pixels and any images used with a differing size will be automatically resized to fit.

The option supports specific values:

- A URL with `https://` at the start that points to an image.
- A filename with `.png` extension that matches an existing file in the `favicons` folder of the plugin.
    - You can use `random` to have the plugin randomly select an image from the folder.
- A player name or UUID that will be resolved into a Player head image using https://mc-heads.net{ target="_blank" rel="nofollow" }.
    - Placeholders that get resolved into a player name or UUID (i.e. `${player name}` or `${player uuid}`) can be used.

/// details | Why is the favicon from URL/Player name/Player UUID/Placeholder not loading?
    type: question

Servers and Proxies don't use the image file directly, but have their own specific Favicon instance.  
To set the Favicon is AdvancedServerList required to create such a Favicon instance from the provided URL. This process takes a lot of time, so to not slow down the server/proxy more then necessary is this done asynchronously.  
A downside of this is, that the server/proxy won't wait for the plugin to be done with creating the favicon, which results in the ping event being completed before the plugin could set the Favicon, resulting in none being displayed.

The created favicon is being cached and *should* display on the next ping, if done so within the configured `faviconCacheTime` (Default: 1 minute).

This issue only exists for Favicons created from a URL, player name/uuid or Placeholder and doesn't exist for local favicons located in the `favicons` folder as they are converted into Favicon instances during the plugin's startup.
///

## Example

```yaml
priority: 0

motd:
  - '<grey>'
  - '<grey>'

#
# In this example is the player Andre_601 watching the server
# list, so ${player uuid} is being resolved into their UUID,
# resulting in the head of their skin being displayed.
#
favicon: '${player uuid}'
```
/// html | div.result
![favicon-example](../../assets/images/examples/favicon-example.jpg){ loading="lazy" }
///