---
api: true

methods:
  - name: 'getName'
    description: |
      Returns the name of the player.  
      On BungeeCord and Velocity is the returned value always what AdvancedServerList has cached, while on paper the value may differ, should the plugin be able to obtain a OfflinePlayer instance from the Server.
      
      Name may also be whatever has been defined in AdvancedServerList's `unknownPlayer -> name` config.yml Option.
    returns: 'String representing the player''s name'
    type:
      name: String
      type: object
  - name: 'getProtocol'
    description: Returns the protocol ID the player is using. The protocol ID is a unique positive integer that is used in Minecraft to determine the Version used by the Client and Server.
    returns: 'Integer representing the protocol version of this player.'
    type:
      name: int
  - name: 'getUUID'
    description: |
      Returns the unique ID associated with this player.
      
      UUID may also be whatever has been defined in AdvancedServerList's `unknownPlayer -> uuid` config.yml Option.
    returns: 'UUID of the player'
    type:
      name: UUID
      type: object
---

# <api__interface></api__interface> GenericPlayer

Interface used by the API to share common information across all platforms.

Platform-specific interfaces extending this interface are available and may offer additional features in addition to what this interface is offering.  
This interface always offers data for the following:

- [Name of the Player](#getname)
- [Protocol version of the Player](#getprotocol)
- [UUID of the Player](#getuuid)