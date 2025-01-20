---
api: true

inherits:
  'ch.andre601.advancedserverlist.api.objects.GenericPlayer':
    link: '../../../api/ch.andre601.advancedserverlist.api/objects/genericplayer.md'
    list:
      - name: 'getName()'
        link: 'getname'
      - name: 'getProtocol()'
        link: 'getprotocol'
      - name: 'getUUID()'
        link: 'getuuid'
---

# <api__interface></api__interface> BungeePlayer

[`GenericPlayer` instance](../../../api/ch.andre601.advancedserverlist.api/objects/genericplayer.md) for the BungeeCord proxy implementation of AdvancedServerList.

To get an instance of this class from a GenericPlayer instance, simply cast it to a BungeePlayer (Granted that the GenericPlayer instance actually is a BungeePlayer instance).