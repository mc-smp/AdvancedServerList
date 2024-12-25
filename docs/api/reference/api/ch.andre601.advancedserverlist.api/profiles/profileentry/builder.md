---
template: api-doc.html

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
      Sets the value to resolve into a number by AdvancedServerList.<br>
      Strings not resolving into valid numbers will be treated as this option not being set.<br>
      <br>
      This option has no effect when <a href="../#extraplayersenabled()"><code>extraPlayersEnabled</code></a> is set to <a href="../../../objects/nullbool/#false"><code>NullBool.FALSE</code></a> or <a href="../../objects/nullbool/#not_set"><code>NullBool.NOT_SET</code></a>.<br>
      <br>
      Set this to <code>null</code> to not alter the max player count. Alternatively <a href="#setextraplayersenabled(nullbool)"><code>disable extra Players</code></a>.
    parameters:
      - name: 'extraPlayersCount'
        description: 'The number of extra players to add.'
        type: 'String'
        attribute:
          - 'nullable'
    returns: 'This Builder after the extra player count has been set. Useful for chaining.'
  - name: 'extraPlayersEnabled'
    description: |
      Sets whether the extra players feature should be enabled or not.<br>
      <br>
      Set to <a href="../../../objects/nullbool#not_set"><code>NullBool.NOT_SET</code></a> to not set this.
    parameters:
      - name: 'extraPlayersEnabled'
        description: 'Whether the extra players feature should be enabled or not.'
        type: 'NullBool'
        attribute:
          - 'notnull'
    returns: 'This Builder after the NullBool has been set. Useful for chaining.'
    throws:
      - name: 'IllegalArgumentException'
        description: Thrown by the <code>CheckUtil</code> in case <code>null</code> has been provided as parameter.
  - name: 'favicon'
    description: |
      Sets the value to use for the favicon.<br>
      The following values are supported:
      <ul>
      <li>URL to a valid PNG file.</li>
      <li>File name (with <code>.png</code> extension) matching a file saved in the favicons folder of AdvancedServerList.</li>
      <li><code>${player uuid}</code> to display the avatar of the player.
      </ul>
      Set to an empty String or <code>null</code> to not alter the Favicon.
    parameters:
      - name: 'favicon'
        description: 'The favicon to set.'
        type: 'String'
        attribute:
          - 'nullable'
    returns: 'This Builder after the Favicon has been set. Useful for chaining.'
  - name: 'hidePlayersEnabled'
    description: |
      Sets whether the player count should be hidden or not.<br>
      <br>
      Set to <a href="../../../objects/nullbool#not_set"><code>NullBool.NOT_SET</code></a> to not set this.
    parameters:
      - name: 'hidePlayersEnabled'
        description: 'Whether the player count should be hidden or not.'
        type: 'NullBool'
        attribute:
          - 'notnull'
    returns: 'This Builder after the NullBool has been set. Useful for chaining.'
    throws:
      - name: 'IllegalArgumentException'
        description: 'Thrown by the <code>CheckUtil</code> in case <code>null</code> has been provided as parameter.'
  - name: 'maxPlayersCount'
    description: |
      Sets the value to resolve into a number by AdvancedServerList.<br>
      Strings not resolving into valid numbers will be treated as this option not being set.<br>
      <br>
      This option has no effect when <a href="#maxplayersenabled(nullbool)">maxPlayersEnabled</a> is set to <a href="../../../objects/nullbool/#false"><code>NullBool.FALSE</code></a> or <a href="../../../objects/nullbool/#not_set"><code>NullBool.NOT_SET</code></a>.<br>
      <br>
      Set this to <code>null</code> to not alter the max player count. Alternatively <a href="#maxplayersenabled(nullbool)">disable max Players</a>.
    parameters:
      - name: 'maxPlayersCount'
        description: 'The number of max players to set.'
        type: 'Integer'
        attribute:
          - 'nullable'
    returns: 'This Builder after the max player count has been set. Useful for chaining.'
  - name: 'maxPlayersEnabled'
    description: |
      Sets whether the max players feature should be enabled.<br>
      <br>
      Set to <a href="../../../objects/nullbool/#not_set"><code>NullBool.NOT_SET</code></a> to not set this.<br>
      <br>
      An <code>IllegalArgumentException</code> may be thrown by the <code>CheckUtil</code> should maxPlayersEnabled be null.
    parameters:
      - name: 'maxPlayersEnabled'
        description: 'Whether the extra players feature should be enabled or not.'
        type: 'NullBool'
        attribute:
          - 'notnull'
    returns: 'This Builder after the NullBool has been set. Useful for chaining.'
    throws:
      - name: 'IllegalArgumentException'
        description: 'Thrown by the <code>CheckUtil</code> in case <code>null</code> has been provided as parameter.'
  - name: 'motd'
    description: |
      Sets a new MOTD to use.<br>
      <br>
      Set to an empty list to not change the MOTD.<br>
      Only the first two entries of the list will be considered and any additional ones discarded.<br>
      <br>
      An <code>IllegalArgumentException</code> may be thrown by the <code>CheckUtil</code> should the provided motd list be null.
    parameters:
      - name: 'motd'
        description: 'The MOTD to use.'
        type: 'List<String>'
        attribute:
          - 'notnull'
    returns: 'This Builder after the motd has been set. Useful for chaining.'
    throws:
      - name: 'IllegalArgumentException'
        description: 'Thrown by the <code>CheckUtil</code> in case <code>null</code> has been provided as parameter.'
  - name: 'onlinePlayersCount'
    description: |
      Sets the value to resolve into a number by AdvancedServerList.<br>
      Strings not resolving into valid numbers will be treated as this option not being set.<br>
      <br>
      This option has no effect when <a href="#onlineplayersenabled(nullbool)">maxPlayersEnabled</a> is set to <a href="../../../objects/nullbool/#false"><code>NullBool.FALSE</code></a> or <a href="../../../objects/nullbool/#not_set"><code>NullBool.NOT_SET</code></a>.<br>
      <br>
      Set this to <code>null</code> to not alter the online player count. Alternatively <a href="#onlineplayersenabled(nullbool)">disable online Players</a>.
    parameters:
      - name: 'onlinePlayersCount'
        description: 'The number of online players to set.'
        type: 'String'
        attribute:
          - 'nullable'
    returns: 'This Builder after the max player count has been set. Useful for chaining.'
  - name: 'onlinePlayersEnabled'
    description: |
      Sets whether the online players feature should be enabled.<br>
      <br>
      Set to <a href="../../../objects/nullbool/#not_set"><code>NullBool.NOT_SET</code></a> to not set this.<br>
      <br>
      An <code>IllegalArgumentException</code> may be thrown by the <code>CheckUtil</code> should onlinePlayersEnabled be null.
    parameters:
      - name: 'onlinePlayersEnabled'
        description: 'Whether the extra players feature should be enabled or not.'
        type: 'NullBool'
        attribute:
          - 'notnull'
    returns: 'This Builder after the NullBool has been set. Useful for chaining.'
    throws:
      - name: 'IllegalArgumentException'
        description: 'Thrown by the <code>CheckUtil</code> in case <code>null</code> has been provided as parameter.'
  - name: 'playerCountText'
    description: |
      Sets the text to override the player count with.<br>
      <br>
      Set to an empty String or <code>null</code> to not alter the player count text.
    parameters:
      - name: 'playerCountText'
        description: 'The text to show in the player count.'
        type: 'String'
        attribute:
          - 'nullable'
    returns: 'This Builder after the player count text has been set. Useful for chaining.'
  - name: 'players'
    description: |
      Sets the players (lines) to use for the hover.<br>
      <br>
      Set to an empty list to not change the hover text.
      <br>
      An <code>IllegalArgumentException</code> may be thrown by the <code>CheckUtil</code> should the provided okayersmotd list be null.
    parameters:
      - name: 'players'
        description: 'The lines to set for the hover.'
        type: 'List<String>'
        attribute:
          - 'notnull'
    returns: 'This Builder after the players have been set. Useful for chaining.'
    throws:
      - name: 'IllegalArgumentException'
        description: 'Thrown by the <code>CheckUtil</code> in case <code>null</code> has been provided as parameter.'
  - name: 'build'
    description: 'Creates a new <a href="./.."><code>ProfileEntry</code> instance</a> with the values set in this Builder.'
    returns: 'New <a href="./.."><code>ProfileEntry</code> instance</a>.'
    type:
      name: 'ProfileEntry'
      type: 'object'
      link: './..'
---

# <api__class></api__class> Builder

Builder class to create a new [`ProfileEntry` instance](index.md).