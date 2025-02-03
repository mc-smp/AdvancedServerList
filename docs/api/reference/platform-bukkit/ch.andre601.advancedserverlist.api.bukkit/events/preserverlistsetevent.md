---
api: true

constructors:
  - name: 'PreServerListSetEvent'
    parameters:
      - name: 'entry'
        type: ProfileEntry

methods:
  - name: 'getHandlerList'
    returns: 'Static `HandlerList` instance.'
    attributes:
      - static
    type:
      name: 'HandlerList'
      type: 'object'

inherits:
  'ch.andre601.advancedserverlist.api.events.GenericServerListEvent':
    link: '../../../api/ch.andre601.advancedserverlist.api/events/genericserverlistevent.md'
    list:
      - name: 'getEntry()'
        link: 'getentry'
      - name: 'setEntry(ProfileEntry)'
        link: 'setentry'
      - name: 'isCancelled()'
        link: 'iscancelled'
      - name: 'setCancelled(boolean)'
        link: 'setcancelled'
  'org.bukkit.event.Event':
    link: 'https://hub.spigotmc.org/javadocs/spigot/org/bukkit/event/Event.html'
    list:
      - name: 'getHandlers()'
        link: 'getHandlers()'
---

# <api__class></api__class> PreServerListSetEvent

Called **before** AdvancedServerList modifies the server list.  
The provided [`ProfileEntry`](../../../api/ch.andre601.advancedserverlist.api/events/genericserverlistevent.md#getentry) will be the one used for the server list.