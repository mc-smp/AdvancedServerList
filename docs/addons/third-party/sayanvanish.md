---
icon: octicons/eye-closed-24
---
# SayanVanish

The plugin [SayanVanish]{ target="_blank" rel="nofollow" } is hooking into AdvancedServerList to provide placeholders of its own to be used in conditions and text options.

## Placeholders

/// tab | :fontawesome-solid-paper-plane: Paper
[:octicons-file-code-24: Source][source-paper]

| Placeholder                      | Description                                                                                |
|----------------------------------|--------------------------------------------------------------------------------------------|
| `${sayanvanish vanished}`        | Returns true or false based on if the player is vanished.                                  |
| `${sayanvanish level}`           | Returns the vanish level of the player.                                                    |
| `${sayanvanish count}`           | Returns the number of online players that are vanished.                                    |
| `${sayanvanish online_here}`     | Returns the number of online players on the same server that aren't vanished.              |
| `${sayanvanish online_total}`    | Returns the number of online players on the entire network that aren't vanished.^[1](#n1)^ |
| `${sayanvanish online_<server>}` | Returns the number of online players on `<server>` that aren't vanished.^[1](#n1)^         |
| `${sayanvanish vanish_prefix}`   | Returns the prefix for the player while they are vanished or an empty String.              |
| `${sayanvanish vanish_suffix}`   | Returns the suffix for the player while they are vanished or an empty String.              |

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
| `${sayanvanish vanish_prefix}`   | Returns the prefix for the player while they are vanished or an empty String.    |
| `${sayanvanish vanish_suffix}`   | Returns the suffix for the player while they are vanished or an empty String.    |
///

[SayanVanish]: https://modrinth.com/plugin/sayanvanish
[source-paper]: https://github.com/Syrent/SayanVanish/blob/6e03b874464a7f07c0baba233c94cdfefab5d3e2/sayanvanish-bukkit/src/main/kotlin/org/sayandev/sayanvanish/bukkit/feature/features/hook/FeatureHookAdvancedServerList.kt
[source-velocity]: https://github.com/Syrent/SayanVanish/blob/6e03b874464a7f07c0baba233c94cdfefab5d3e2/sayanvanish-proxy/sayanvanish-proxy-velocity/src/main/kotlin/org/sayandev/sayanvanish/velocity/feature/features/hook/FeatureHookAdvancedServerList.kt