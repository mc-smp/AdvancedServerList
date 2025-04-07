---
api: true

constructors:
  - name: 'InvalidPlaceholderProviderException'
    description: Basic Constructor to create an instance of this Exception.
    parameters:
      - name: msg
        type: String
        description: The reason of the exception.
---

# <api__class></api__class> InvalidPlaceholderProviderException

RuntimeException thrown whenever an invalid [`PlaceholderProvider`](../placeholderprovider.md) has been given.