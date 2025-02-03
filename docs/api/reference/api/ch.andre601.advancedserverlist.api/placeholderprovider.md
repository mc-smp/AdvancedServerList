---
api: true

constructors:
  - name: 'PlaceholderProvider'
    description: 'Constructor used to set the identifier for the class extending the PlaceholderProvider class itself.'
    parameters:
      - name: 'identifier'
        description: 'The identifier to use for the placeholder. Cannot be empty.'
        type: String
        attributes:
          - notnull

methods:
  - name: 'parsePlaceholder'
    description: |
      Method called by AdvancedServerList's StringReplacer class to replace any appearances of `${<identifier> <placeholder> with whatever value a matching PlaceholderProvider may return.
      
      Returning `null` will be treated as an invalid placeholder by the plugin, making it return the placeholder as-is without any changes.
    parameters:
      - name: 'placeholder'
        type: String
        description: 'The part of the Placeholder that comes after the identifier and before the closing curly bracket.'
      - name: 'player'
        type: GenericPlayer
        description: 'The [`GenericPlayer` instance](objects/genericplayer.md) used.'
      - name: 'server'
        type: GenericServer
        description: 'The [`GenericServer` instance](objects/genericserver.md) used.'
    returns: 'Possibly-parsed or possibly-null String to replace the placeholder with.'
    attributes:
      - nullable
      - abstract
    type:
      name: 'String'
      type: 'object'
  - name: 'getIdentifier'
    description: 'Returns the identifier used by this PlaceholderProvider instance.'
    returns: 'String representing the identifier of this PlaceholderProvider instance.'
    type:
      name: 'String'
      type: 'object'
---

# <api__abstract></api__abstract> <api__class></api__class> PlaceholderProvider

Abstract class that is used to provide your own Placeholder patterns for AdvancedServerList to parse.

In order for your class to be considered a valid PlaceholderProvider will you need to set the `identifier` to a non-null, non-empty value without having any spaces in it.  
Once set, use [`AdvancedServerListAPI#addPlaceholderProvider(PlaceholderProvider)`](advancedserverlistapi.md#addplaceholderprovider) to register your class for AdvancedServerList to use.