---
api: true

constructors:
  - name: 'PostServerListSetEvent'
    description: 'Constructor for creating a new PostServerListSetEvent instance.'
    parameters:
      - name: 'entry'
        description: 'The [`ProfileEntry`](../../../api/ch.andre601.advancedserverlist.api/profiles/profileentry/index.md) used during the PingEvent.'
        type: ProfileEntry
        attribute:
          - nullable

methods:
  - name: 'getHandlerList'
    description: 'Static [`HandlerList`](https://hub.spigotmc.org/javadocs/spigot/org/bukkit/event/HandlerList.html){ target="_blank" rel="nofollow" } getter. Required by Paper.'
    returns: 'The static [`HandlerList` instance](https://hub.spigotmc.org/javadocs/spigot/org/bukkit/event/HandlerList.html){ target="_blank" rel="nofollow" } of this event.'
    attributes:
      - static
    type:
      name: 'HandlerList'
      type: 'object'
  - name: 'getEntry'
    description: |
      The [`ProfileEntry`](../../../api/ch.andre601.advancedserverlist.api/profiles/profileentry/index.md) that was used during the PingEvent handling.
      
      The returned value may be `null` should the PingEvent handling be cancelled at any point (i.e. due to an invalid protocol or because the [`PreServerListSetEvent`](preserverlistsetevent.md) has been cancelled).  
      The values within the ProfileEntry also do not represent what is actually used within the Server list itself, but only the values provided for the PingEvent handling.
    returns: 'Possibly-null ProfileEntry used for the PingEvent.'
    attributes:
      - nullable
    type:
      name: 'ProfileEntry'
      type: 'object'
      link: '../../../api/ch.andre601.advancedserverlist.api/profiles/profileentry/index.md'

inherits:
  'org.bukkit.event.Event':
    link: 'https://hub.spigotmc.org/javadocs/spigot/org/bukkit/event/Event.html'
    list:
      - 'getHandlers()'
---

# <api__class></api__class> PostServerListSetEvent

Called **after** the plugin has updated the Server List.

The provided [`ProfileEntry`](#getentry) may be null due to the PingEvent handling being cancelled (i.e. due to a invalid protocol or due to the [`PreServerListSetEvent`](preserverlistsetevent.md) being cancelled).  
The entry also only represents the values used during the PingEvent, not the actual content that is being displayed.