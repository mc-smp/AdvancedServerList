---
api: true

constructors:
  - name: 'ProfileEntry'
    description: |
      Creates a new instance of a ProfileEntry with the given values.  
      It's recommended to use the [`Builder` class](builder.md) for a more convenient configuration of the Settings.
    parameters:
      - name: 'motd'
        description: 'The MOTD to use.'
        type: 'List<String>'
        attributes:
          - notnull
      - name: 'players'
        description: 'The players (Lines) to show in the hover.'
        type: 'List<String>'
        attributes:
          - notnull
      - name: 'playerCountText'
        description: 'The text to display instead of the player count.'
        type: String
        attributes:
          - nullable
      - name: 'favicon'
        description: 'The favicon to use.'
        type: String
        attributes:
          - nullable
      - name: 'hidePlayersEnabled'
        description: 'Whether player count should be hidden.'
        type: NullBool
        attributes:
          - notnull
      - name: 'extraPlayersEnabled'
        description: 'Whether the extra players option should be enabled.'
        type: NullBool
        attributes:
          - notnull
      - name: 'maxPlayersEnabled'
        description: 'Whether the max players option should be enabled.'
        type: NullBool
        attributes:
          - notnull
      - name: 'onlinePlayersCount'
        description: 'The number to set for the Online Players count.'
        type: String
        attributes:
          - nullable
      - name: 'hidePlayersHoverEnabled'
        description: 'Whether the online Players Hover should beenabled.'
        type: NullBool
        attributes:
          - notnull
      - name: 'extraPlayersCount'
        description: 'The number to add to the online Players for the extra Players.'
        type: String
        attributes:
          - nullable
      - name: 'maxPlayersCount'
        description: 'The number to set for the max Players count.'
        type: String
        attributes:
          - nullable
    seealso:
      - name: 'ProfileEntry.Builder'
        link: 'builder.md'

classes:
  - name: 'Builder'
    description: 'Builder class to create a new ProfileEntry instance.'
    type: 'static class'
    link: 'builder.md'

methods:
  - name: 'empty'
    description: |
      Creates an "empty" PlayerEntry with the following values set:  
      
      - [`motd`](#motd): Empty List
      - [`players`](#players): Empty List
      - [`playerCountText`](#playercounttext): Empty String
      - [`favicon`](#favicon): Empty String
      - [`hidePlayersEnabled`](#hideplayersenabled): [`NullBool.NOT_SET`](../../objects/nullbool.md#not_set)
      - [`extraPlayersEnabled`](#extraplayersenabled): [`NullBool.NOT_SET`](../../objects/nullbool.md#not_set)
      - [`maxPlayersEnabled`](#maxplayersenabled): [`NullBool.NOT_SET`](../../objects/nullbool.md#not_set)
      - [`onlinePlayersEnabled`](#onlineplayersenabled): [`NullBool.NOT_SET`](../../objects/nullbool.md#not_set)
      - [`hidePlayersHoverEnabled`](#hideplayershoverenabled): [`NullBool.NOT_SET`](../../objects/nullbool.md#not_set)
      - [`extraPlayersCount`](#extraplayerscount): `null`
      - [`maxPlayersCount`](#maxplayerscount): `null`
      - [`onlinePlayersCount`](#onlineplayerscount): `null`
    returns: 'New ProfileEntry instance with empty/null values defined'
    attributes:
      - static
    type:
      name: 'ProfileEntry'
      type: 'object'
  - name: 'copy'
    description: |
      Creates a copy of this ProfileEntry instance.
      
      This is a convenience method that avoids using [`builder()`](#builder) first followed by [`ProfileEntry.Builder.build()`](builder.md#build):
      ```java
      ProfileEntry entry = ProfileEntry.empty(); // Obtain ProfileEntry
      
      // Both do the same
      ProfileEntry newEntry1 = entry.builder().build();
      ProfileEntry newEntry2 = entry.copy();
      ```
    returns: 'A copy of this ProfileEntry instance.'
    seealso:
      - name: 'builder()'
        link: '#builder'
    type:
      name: 'ProfileEntry'
      type: 'object'
  - name: 'builder'
    description: |
      Creates a [`Builder` instance](builder.md) with the values from this ProfileEntry set.  
      Use this method if you would like to modify the ProfileEntry.
    returns: 'A new [`Builder` instance](builder.md) with the values of this ProfileEntry set.'
    type:
      name: 'Builder'
      type: 'object'
      link: 'builder.md'
  - name: 'motd'
    description: 'Gets the currently set MOTD of this ProfileEntry.'
    returns: 'The current MOTD used by this ProfileEntry.'
    type:
      name: 'List<String>'
      type: 'object'
  - name: 'players'
    description: 'Gets the currently set players of this ProfileEntry.'
    returns: 'The current list of players used by this ProfileEntry.'
    type:
      name: 'List<String>'
      type: 'object'
  - name: 'playerCountText'
    description: 'Gets the currently set player count text of this ProfileEntry.'
    returns: 'The current player count text used by this ProfileEntry.'
    type:
      name: 'String'
      type: 'object'
  - name: 'favicon'
    description: |
      Gets the currently set favicon of this ProfileEntry.  
      Note that the favicon usually is and supports one of the following options:
      
      - URL to a valid PNG file
      - File name (with `.png` extension) matching a file saved in the favicons folder of AdvancedServerList
      - `${player uuid}` to display the avatar of the player
    returns: 'The current favicon used by this ProfileEntry.'
    type:
      name: 'String'
      type: 'object'
  - name: 'hidePlayersEnabled'
    description: |
      Whether the player count should be hidden or not in this ProfileEntry.  
      To get the actual boolean value, append [`getOrDefault(boolean)`](../../objects/nullbool.md#getordefault).
    returns: 'Whether the player count should be hidden or not in this ProfileEntry.'
    type:
      name: 'NullBool'
      type: 'object'
      link: '../../objects/nullbool.md'
  - name: 'extraPlayersEnabled'
    description: |
      Whether the extra players feature should be used or not in this ProfileEntry.  
      To get the actual boolean value, append [`getOrDefault(boolean)`](../../objects/nullbool.md#getordefault).
    returns: 'Whether the extra players feature should be used or not in this ProfileEntry.'
    type:
      name: 'NullBool'
      type: 'object'
      link: '../../objects/nullbool.md'
  - name: 'maxPlayersEnabled'
    description: |
      Whether the max players feature should be used or not.  
      To get the actual boolean value, append [`getOrDefault(boolean)`](../../objects/nullbool.md#getordefault).
    returns: 'Whether the max players feature should be used or not.'
    type:
      name: 'NullBool'
      type: 'object'
      link: '../../objects/nullbool.md'
  - name: 'onlinePlayersEnabled'
    description: |
      Whether the online players feature should be used or not.  
      To get the actual boolean value, append [`getOrDefault(boolean)`](../../objects/nullbool.md#getordefault).
    returns: 'Whether the online players feature should be used or not.'
    type:
      name: 'NullBool'
      type: 'object'
      link: '../../objects/nullbool.md'
  - name: 'hidePlayersHoverEnabled'
    description: |
      Whether the Hover List of online players should be hidden or not.  
      To get the actual boolean value, append [`getOrDefault(boolean)`](../../objects/nullbool.md#getordefault).
      
      When true, this option will force the [`players()`](#players) list to be ignored.
    returns: 'Whether the Hover List of online players should be hidden or not.'
    type:
      name: 'NullBool'
      type: 'object'
      link: '../../objects/nullbool.md'
  - name: 'extraPlayersCount'
    description: 'Gets the currently set number of extra players of this ProfileEntry.'
    returns: 'The current number of extra players used by this ProfileEntry.'
    attributes:
      - nullable
    type:
      name: 'String'
      type: 'object'
  - name: 'maxPlayersCount'
    description: 'Gets the currently set number of max players of this ProfileEntry.'
    returns: 'The current number of max players used by this ProfileEntry.'
    attributes:
      - nullable
    type:
      name: 'String'
      type: 'object'
  - name: 'onlinePlayersCount'
    description: 'Gets the currently set number of online players of this ProfileEntry.'
    returns: 'The current number of online players used by this ProfileEntry.'
    attributes:
      - nullable
    type:
      name: 'String'
      type: 'object'
  - name: 'isInvalid'
    description: |
      Whether this ProfileEntry is considered invalid or not.  
      The ProfileEntry is considered invalid if all the following is true:
      
      - [`motd()`](#motd) is empty.
      - [`players()`](#players) is empty.
      - [`playerCountText()`](#playercounttext) is null/empty **and** [`hidePlayersEnabled()`](#hideplayersenabled) is `false`.
      - [`favicon()`](#favicon) is null/empty.
      
      As long as one of the above is **not** true is this ProfileEntry considered valid and `false` will be returned.
    returns: 'Whether this ProfileEntry is invalid or not.'
    type:
      name: 'boolean'
      type: 'primitive'
---

# <api__record></api__record> ProfileEntry

This record represents the content found in a server list profile YAML file.  
The content may come from either the "profiles" list, the options in the file itself (global options) or a mix of both.

This class is immutable. Use [`builder()`](#builder) to get a [`Builder` instance](builder.md) with the values of this class added.