---
api: true

methods:
  - name: 'getEntry'
    description: 'Gets the [`ProfileEntry`](../profiles/profileentry/index.md) currently set.'
    returns: The currently used ProfileEntry.
    type:
      name: ProfileEntry
      type: object
      link: '../profiles/profileentry/index.md'
  - name: 'setEntry'
    description: |-
      Sets the new [`ProfileEntry`](../profiles/profileentry/index.md) to use.  
      This may not be `null`.
    parameters:
      - name: 'entry'
        description: 'The new [`ProfileEntry`](../profiles/profileentry/index.md) to use.'
        type: ProfileEntry
        attributes:
          - notnull
    throws:
      - name: 'IllegalArgumentException'
        description: 'When the provided ProfileEntry is `null`'
    type:
      name: 'void'
  - name: 'isCancelled'
    description: Returns whether this event has been cancelled or not.
    returns: Whether this event has been cancelled or not.
    type:
      name: boolean
  - name: 'setCancelled'
    description: Sets the event's cancel state
    parameters:
      - name: 'cancelled'
        description: 'Boolean to set the event''s cancelled state.'
        type: boolean
    type:
      name: 'void'
---

# <api__interface></api__interface> GenericServerListEvent

Interface used for the platform-specific PreServerListSetEvent instances.  
This allows the plugin to pull common info such as ProfileEntry or if the event has been cancelled by another plugin.