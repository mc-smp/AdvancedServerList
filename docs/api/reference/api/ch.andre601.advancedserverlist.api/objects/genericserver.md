---
api: true

methods:
  - name: 'getPlayersOnline'
    description: 'Returns the number of players currently online on the server.'
    returns: 'Number of players online on the server.'
    type:
      name: int
  - name: 'getPlayersMax'
    description: 'Returns the number of total players that can join the server.'
    returns: 'Number of total players that can join the server.'
    type:
      name: int
  - name: 'getHost'
    description: 'Returns the IP/Domain that got pinged by the player.'
    returns: 'Possibly-null String containing the IP/Domain that got pinged by the player'
    attributes:
      - nullable
    type:
      name: 'String'
      type: 'object'
---

# <api__interface></api__interface> GenericServer

Interface used to share common information across all platforms.

Platform-specific interfaces extending this interface are available and may offer additional features in addition to what this interface is offering.  
This interface always offers data for the following:

- [Players currently online on the Proxy/Server](#getplayersonline)
- [Max Players allowed to join the Proxy/Server](#getplayersmax)
- [IP/Domain pinged by the Player](#gethost)