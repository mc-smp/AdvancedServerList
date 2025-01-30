---
icon: octicons/rel-file-path-24
---

# Commands

The plugin adds commands to use for various things related to the plugin itself.  
The main command is `/advancedserverlist` but an alias called `/asl` is also registered to use.

## Permissions

The main permission is `advancedserverlist.admin` for all commands, but you can also grant access to specific commands using `advancedserverlist.command.<subcommand>` instead (i.e. `advancedserverlist.command.help` to grant access to [`/asl help [query]`](#help)).

## Subcommands

The following subcommands are available (Subcommands are case-insensitive):

- [`help [query]`](#help)
- [`reload`](#reload)
- [`clearCache`](#clearcache)
- [`migrate <plugin>`](#migrate)
- [`profiles {add <name> | copy <profile> <name> | info <profile> | list | set <profile> <option> [value]}`](#profiles)

### `help [query]` { #help }

**Permission:** `advancedserverlist.command.help`  
**Arguments:**

- `[query]` - Optional search query to receive help info from.

Shows a list of all available [subcommands](#subcommands) for AdvancedServerList.

----

### `reload`

**Permission:** `advancedserverlist.command.reload`

Reloads the plugin's `config.yml` and all available YAML files in the `profiles` folder.

----

### `clearcache`

**Permission:** `advancedserverlist.command.clearcache`

Clears the currently cached favicons and players.

----

### `migrate <plugin>` { #migrate }

**Permission:** `advancedserverlist.command.migrate`  
**Arguments:**

- `<plugin>` - The plugin to migrate from.

Migrates files from one plugin over to AdvancedServerList.  
Check the [Migration Page](../migration/index.md) for a list of supported plugins.

----

### `profiles {add <profile> | copy <profile> <name> | info <profile> | list | set <profile> <option> [value]}` { #profiles }

**Permission:** `advancedserverlist.command.profiles`  
**Arguments:**

- `add <profile>` - Creates a new profile using the default profile values.
    - `<profile>` - Name of the profile to use.
- `copy <profile> <name>` - Creates a copy of an existing profile.
    - `<profile>` - Name of the profile to copy from.
    - `<name>` - Name of the copy to have.
- `info <profile>` - Returns information of a profile. Only executable by players.
    - `<profile>` - Name of the profile to fetch info from.
- `list` - Lists all loaded profiles. The entries have hover text for extra info.
- `set <profile> <option> [value]` - Edits values of a profile. Requires [`/asl reload`](#reload) to apply changes.
    - `<profile>` - Name of the profile to edit.
    - `<option>` - The option to edit.
    - `[value]` - The new value to set. Leave empty to reset to the default.

Creates, copies or shows profiles in AdvancedServerList.