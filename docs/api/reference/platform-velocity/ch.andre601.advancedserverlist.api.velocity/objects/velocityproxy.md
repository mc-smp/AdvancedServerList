---
api: true

methods:
  - name: 'getServers'
    description: 'Returns a <code>Map&lt;String, RegisteredServer&gt;</code> where the key is the name of the server and the value the RegisteredServer of the proxy.'
    returns: 'Possibly-empty Map containing pairs of Strings and RegisteredServers.'
    type:
      name: 'Map<String, RegisteredServer>'
      type: 'object'

inherits:
  'ch.andre601.advancedserverlist.api.objects.GenericServer':
    link: '../../../api/ch.andre601.advancedserverlist.api/objects/genericserver.md'
    list:
      - name: 'getPlayersOnline()'
        link: 'getplayersonline'
      - name: 'getPlayersMax()'
        link: 'getplayersmax'
      - name: 'getHost()'
        link: 'gethost'
---

# <api__interface></api__interface> VelocityProxy