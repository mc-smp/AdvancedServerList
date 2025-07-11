---
api: true

methods:
  - name: 'getVersion'
    description: 'Returns the [protocol version](../../../api/ch.andre601.advancedserverlist.api/objects/genericplayer.md#getprotocol) of the player in a readable MC version format (i.e. 1.19.3).'
    returns: 'The readable MC version the player uses.'
    type:
      name: 'String'
      type: 'object'

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

# <api__interface></api__interface> VelocityPlayer

[`GenericPlayer` instance](../../../api/ch.andre601.advancedserverlist.api/objects/genericplayer.md) for the Velocity proxy implementation of AdvancedServerList.  
Provides a [`getVersion()` method](#getversion) to get the MC version used as a readable String (i.e. 1.19.3) rather than just the protocol version.

To get an instance of this class from a GenericPlayer instance, simply cast it to a VelocityPlayer (Granted that the GenericPlayer instance actually is a VelocityPlayer instance).