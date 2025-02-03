---
api: true

enums:
  - name: 'true'
    description: 'Boolean value `true`'
  - name: 'false'
    description: 'Boolean value `false`'
  - name: 'not_set'
    description: 'Boolean value `null`'

methods:
  - name: 'resolve'
    description: |
      Returns a NullBool instance based on the provided `Boolean` value.  
      In case of `null` being provided will [`NullBool.NOT_SET`](#not_set) be returned, otherwise will the corresponding NullBool instance matching the Boolean value be returned.
    returns: 'NullBool instance based on the provided `Boolean` value.'
    parameters:
      - name: 'bool'
        description: 'The `Boolean` value to receive a NullBool instance for.'
        type: Boolean
        attributes:
          - nullable
    attributes:
      - static
    type:
      name: 'NullBool'
      type: 'object'
  - name: 'isNotSet'
    description: 'Returns whether the NullBool is [`NullBool.NOT_SET`](#not_set)'
    returns: 'True if the instance is [`NullBool.NOT_SET`](#not_set), otherwise false.'
    type:
      name: 'boolean'
      type: 'primitive'
  - name: 'getOrDefault'
    description: |
      Gets the corresponding boolean value associated with the NullBool instance.  
      In the case of NullBool [not being set](#isnotset) will the provided default value be returned.
    returns: 'True or false depending on the NullBool instance and the provided default value.'
    parameters:
      - name: 'def'
        description: 'The default boolean value to return should the NullBool instance be [`NullBool.NOT_SET`](#not_set).'
        type: boolean
    type:
      name: 'boolean'
      type: 'primitive'
---

# <api__enum></api__enum> NullBool

Enum used to return a `Boolean` value with a default one should no value be set.