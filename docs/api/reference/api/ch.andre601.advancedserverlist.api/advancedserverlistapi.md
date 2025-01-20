---
api: true

methods:
  - name: 'get'
    description: |
      Retrieve the instance used of this API.  
      If no instance has been made so far will a new one be created.
    returns: 'Instance of this API.'
    attributes:
      - static
    type:
      name: 'AdvancedServerListAPI'
      type: 'object'
  - name: 'addPlaceholderProvider'
    description: |
      Adds the provided [`PlaceholderAPIProvider`](placeholderprovider.md) to the list, if it passes the following checks:
      
      - Provided PlaceholderProvider instance is not null.
      - The identifier is not null nor empty.
      - The identifier does not contain any spaces.
      - A PlaceholderProvider with the same identifier doesn't exist already.
      
      Not passing any of the above checks results in a [`InvalidPlaceholderProviderException`](exceptions/invalidplaceholderproviderexception.md) being thrown.
    parameters:
      - name: 'placeholderProvider'
        description: 'The [`PlaceholderProvider`](placeholderprovider.md) to add.'
        type: PlaceholderProvider
        attributes:
          - notnull
    throws:
      - name: 'InvalidPlaceholderProviderException'
        description: 'When the provided [`PlaceholderProvider`](placeholderprovider.md) is null, has a null or empty identifier, the identifier contains spaces, or another PlaceholderProvider with the same identifier is already in use.'
    type:
      name: 'void'
  - name: 'retrievePlaceholderProvider'
    description: 'Retrieves the [`PlaceholderProvider`](placeholderprovider.md) associated with the provided identifier, or `null` should no such entry exist.'
    parameters:
      - name: 'identifier'
        description: 'The identifier to find a matching [`PlaceholderProvider`](placeholderprovider.md) for.'
        type: String
    returns: 'Possibly-null [`PlaceholderProvider` instance](placeholderprovider.md).'
    attributes:
      - nullable
    type:
      name: PlaceholderProvider
      type: object
      link: 'placeholderprovider.md'
---

# <api__class></api__class> AdvancedServerListAPI

Core class of the API for AdvancedServerList.  
Use [`get()`](#get) to retrieve the instance currently used.