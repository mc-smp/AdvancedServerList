---
api: true

defaults:
  type:
    name: 'Builder'
    type: 'object'

constructors:
  - name: 'Builder'
    description: 'Empty Builder Constructor used to create a new instance of this class.'
  
methods:
  - name: 'extraPlayersCount'
    description: |
      Sets the value to resolve into a number by AdvancedServerList.  
      Strings not resolving into valid numbers will be treated as this option not being set.
      
      This option has no effect when [`extraPlayersEnabled()`](index.md#extraplayersenabled) is set to [`NullBool.FALSE`](../../objects/nullbool.md#false) or [`NullBool.NOT_SET`](../../objects/nullbool.md#not_set).
      
      Set this to `null` to not alter the max player count. Alternatively [disable extra Players](#extraplayersenabled).
    parameters:
      - name: 'extraPlayersCount'
        description: 'The number of extra players to add.'
        type: 'String'
        attributes:
          - 'nullable'
    returns: 'This Builder after the extra player count has been set. Useful for chaining.'
  - name: 'extraPlayersEnabled'
    description: |
      Sets whether the extra players feature should be enabled or not.
      
      Set to [`NullBool.NOT_SET`](../../objects/nullbool.md#not_set) to not set this.
    parameters:
      - name: 'extraPlayersEnabled'
        description: 'Whether the extra players feature should be enabled or not.'
        type: 'NullBool'
        attributes:
          - 'notnull'
    returns: 'This Builder after the NullBool has been set. Useful for chaining.'
    throws:
      - name: 'IllegalArgumentException'
        description: Thrown by the `CheckUtil` in case `null` has been provided as parameter.
  - name: 'favicon'
    description: |
      Sets the value to use for the favicon.  
      The following values are supported:
      
      - URL to a valid PNG file
      - File name (with `.png` extension) matching a file saved in the favicons folder of AdvancedServerList
      - `${player uuid}` to display the avatar of the player
      
      Set to an empty String or `null` to not alter the Favicon.
    parameters:
      - name: 'favicon'
        description: 'The favicon to set.'
        type: 'String'
        attributes:
          - 'nullable'
    returns: 'This Builder after the Favicon has been set. Useful for chaining.'
  - name: 'hidePlayersEnabled'
    description: |
      Sets whether the player count should be hidden or not.
      
      Set to [`NullBool.NOT_SET`](../../objects/nullbool.md#not_set) to not set this.
    parameters:
      - name: 'hidePlayersEnabled'
        description: 'Whether the player count should be hidden or not.'
        type: 'NullBool'
        attributes:
          - 'notnull'
    returns: 'This Builder after the NullBool has been set. Useful for chaining.'
    throws:
      - name: 'IllegalArgumentException'
        description: 'Thrown by the `CheckUtil` in case `null` has been provided as parameter.'
  - name: 'hidePlayersHoverEnabled'
    description: |
      Sets ehther the Hover List of online Players should be hidden or not.
      
      Set to [`NullBool.NOT_SET`](../../objects/nullbool.md#not_set) to not set this.
    parameters:
      - name: 'hidePlayersHoverEnabled'
        description: 'Whether the online players feature should be enabled or not.'
        type: 'NullBool'
        attributes:
          - 'notnull'
    returns: 'This Builder after the NullBool has been set. Useful for chaining.'
    throws:
      - name: 'IllegalArgumentException'
        description: 'Thrown by the `CheckUtil` in case `null` has been provided as parameter.'
  - name: 'maxPlayersCount'
    description: |
      Sets the value to resolve into a number by AdvancedServerList.  
      Strings not resolving into valid numbers will be treated as this option not being set.
      
      This option has no effect when [`maxPlayersEnabled(NullBool)`](#maxplayersenabled) is set to [`NullBool.FALSE`](../../objects/nullbool.md#false) or [`NullBool.NOT_SET`](../../objects/nullbool.md#not_set).
      
      Set this to `null` to not alter the max player count. Alternatively [disable max Players](#maxplayersenabled).
    parameters:
      - name: 'maxPlayersCount'
        description: 'The number of max players to set.'
        type: 'Integer'
        attributes:
          - 'nullable'
    returns: 'This Builder after the max player count has been set. Useful for chaining.'
  - name: 'maxPlayersEnabled'
    description: |
      Sets whether the max players feature should be enabled.
      
      Set to [`NullBool.NOT_SET`](../../objects/nullbool.md#not_set) to not set this.
      
      An `IllegalArgumentException` may be thrown by the `CheckUtil` should maxPlayersEnabled be null.
    parameters:
      - name: 'maxPlayersEnabled'
        description: 'Whether the extra players feature should be enabled or not.'
        type: 'NullBool'
        attributes:
          - 'notnull'
    returns: 'This Builder after the NullBool has been set. Useful for chaining.'
    throws:
      - name: 'IllegalArgumentException'
        description: 'Thrown by the `CheckUtil` in case `null` has been provided as parameter.'
  - name: 'motd'
    description: |
      Sets a new MOTD to use.
      
      Set to an empty list to not change the MOTD.  
      Only the first two entries of the list will be considered and any additional ones discarded.
      
      An `IllegalArgumentException` may be thrown by the `CheckUtil` should the provided motd list be null.
    parameters:
      - name: 'motd'
        description: 'The MOTD to use.'
        type: 'List<String>'
        attributes:
          - 'notnull'
    returns: 'This Builder after the motd has been set. Useful for chaining.'
    throws:
      - name: 'IllegalArgumentException'
        description: 'Thrown by the `CheckUtil` in case `null` has been provided as parameter.'
  - name: 'onlinePlayersCount'
    description: |
      Sets the value to resolve into a number by AdvancedServerList.  
      Strings not resolving into valid numbers will be treated as this option not being set.
      
      This option has no effect when [`onlinePlayersEnabled(NullBool)`](#onlineplayersenabled) is set to [`NullBool.FALSE`](../../objects/nullbool.md#false) or [`NullBool.NOT_SET`](../../objects/nullbool.md#not_set).
      
      Set this to `null` to not alter the online player count. Alternatively [disable online Players.](#onlineplayersenabled).
    parameters:
      - name: 'onlinePlayersCount'
        description: 'The number of online players to set.'
        type: 'String'
        attributes:
          - 'nullable'
    returns: 'This Builder after the max player count has been set. Useful for chaining.'
  - name: 'onlinePlayersEnabled'
    description: |
      Sets whether the online players feature should be enabled.
      
      Set to [`NullBool.NOT_SET`](../../objects/nullbool.md#not_set) to not set this.
      
      An `IllegalArgumentException` may be thrown by the `CheckUtil` should onlinePlayersEnabled be null.
    parameters:
      - name: 'onlinePlayersEnabled'
        description: 'Whether the extra players feature should be enabled or not.'
        type: 'NullBool'
        attributes:
          - 'notnull'
    returns: 'This Builder after the NullBool has been set. Useful for chaining.'
    throws:
      - name: 'IllegalArgumentException'
        description: 'Thrown by the `CheckUtil` in case `null` has been provided as parameter.'
  - name: 'playerCountText'
    description: |
      Sets the text to override the player count with.<br>
      <br>
      Set to an empty String or `null` to not alter the player count text.
    parameters:
      - name: 'playerCountText'
        description: 'The text to show in the player count.'
        type: 'String'
        attributes:
          - 'nullable'
    returns: 'This Builder after the player count text has been set. Useful for chaining.'
  - name: 'players'
    description: |
      Sets the players (lines) to use for the hover.
      
      Set to an empty list to not change the hover text.
      
      An `IllegalArgumentException` may be thrown by the `CheckUtil` should the provided okayersmotd list be null.
    parameters:
      - name: 'players'
        description: 'The lines to set for the hover.'
        type: 'List<String>'
        attributes:
          - 'notnull'
    returns: 'This Builder after the players have been set. Useful for chaining.'
    throws:
      - name: 'IllegalArgumentException'
        description: 'Thrown by the `CheckUtil` in case `null` has been provided as parameter.'
  - name: 'build'
    description: 'Creates a new [`ProfileEntry` instance](index.md) with the values set in this Builder.'
    returns: 'New [`ProfileEntry` instance](index.md).'
    type:
      name: 'ProfileEntry'
      type: 'object'
      link: 'index.md'
---

# <api__class></api__class> Builder

Builder class to create a new [`ProfileEntry` instance](index.md).