---
icon: octicons/eye-closed-24
---
# SayanVanish

The plugin [SayanVanish]{ target="_blank" rel="nofollow" } is hooking into AdvancedServerList to provide placeholders of its own to be used in conditions and text options.

/// note
Only the Velocity version supports AdvancedServerList!
///

## Placeholders

The below placeholders are based on the info retrieved from the latest [source code][source]{ target="_blank" rel="nofollow" } (State: 03rd of February 2025).

| Placeholder                      | Description                                                                            |
|----------------------------------|----------------------------------------------------------------------------------------|
| `${sayanvanish vanished}`        | Returns true or false based on if the player is vanished.                              |
| `${sayanvanish level}`           | Returns the vanish level of the player.                                                |
| `${sayanvanish count}`           | Returns the number of online players that are vanished.                                |
| `${sayanvanish online_total}`    | Returns the number of online players that aren't vanished.                             |
| `${sayanvanish online_<server>}` | Returns the number of online players that aren't vanished and on the server `<server>` |

[SayanVanish]: https://modrinth.com/plugin/sayanvanish
[source]: https://github.com/Syrent/SayanVanish/blob/585279bf5480542858a9d3d91cfc4d844982aa0f/sayanvanish-proxy/sayanvanish-proxy-velocity/src/main/kotlin/org/sayandev/sayanvanish/velocity/feature/features/hook/FeatureHookAdvancedServerList.kt