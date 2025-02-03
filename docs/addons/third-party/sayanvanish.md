---
icon: octicons/eye-closed-24
---
# SayanVanish

The plugin [SayanVanish]{ target="_blank" rel="nofollow" } is hooking into AdvancedServerList to provide placeholders of its own to be used in conditions and text options.

## Placeholders

/// tab | :fontawesome-solid-paper-plane: Paper
[:octicons-file-code-24: Source][source-paper]

| Placeholder                      | Description                                                                               |
|----------------------------------|-------------------------------------------------------------------------------------------|
| `${sayanvanish vanished}`        | Returns true or false based on if the player is vanished.                                 |
| `${sayanvanish level}`           | Returns the vanish level of the player.                                                   |
| `${sayanvanish count}`           | Returns the number of online players that are vanished.                                   |
| `${sayanvanish online_here}`     | Returns the number of online players on the same server that aren't vanished.             |
| `${sayanvanish online_total}`    | Returns the number of online players on the entire network that aren't vanished.^[1](#1)^ |
| `${sayanvanish online_<server>}` | Returns the number of online players on `<server>` that aren't vanished.^[1](#1)^         |

<small>^1^{ #n1 } Only works when Proxy mode is enabled and will otherwise return `PROXY_MODE IS NOT ENABLED!`</small>
///

/// tab | :fontawesome-solid-paper-plane: Velocity
[:octicons-file-code-24: Source][source-velocity]

| Placeholder                      | Description                                                                      |
|----------------------------------|----------------------------------------------------------------------------------|
| `${sayanvanish vanished}`        | Returns true or false based on if the player is vanished.                        |
| `${sayanvanish level}`           | Returns the vanish level of the player.                                          |
| `${sayanvanish count}`           | Returns the number of online players that are vanished.                          |
| `${sayanvanish online_total}`    | Returns the number of online players on the entire network that aren't vanished. |
| `${sayanvanish online_<server>}` | Returns the number of online players on `<server>` that aren't vanished.         |
///

[SayanVanish]: https://modrinth.com/plugin/sayanvanish
[source-paper]: 
[source-velocity]: https://github.com/Syrent/SayanVanish/blob/585279bf5480542858a9d3d91cfc4d844982aa0f/sayanvanish-proxy/sayanvanish-proxy-velocity/src/main/kotlin/org/sayandev/sayanvanish/velocity/feature/features/hook/FeatureHookAdvancedServerList.kt