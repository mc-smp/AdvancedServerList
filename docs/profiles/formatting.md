---
icon: octicons/pencil-24
---

# Formatting

This page explains specific points of formatting in AdvancedServerList, be it [text formatting](#text-formatting) or special quirks of [YAML formatting](#yaml-formatting).  
If you have questions, don't hesitate to join the [M.O.S.S. Discord][discord] and ask in the `#asl-support` channel.

## Text Formatting

AdvancedServerList uses the [MiniMessage Text Formatting][minimessage] for all formatting of text options ([`motd`](options/motd.md), [`playerCount -> hover`](options/playercount/hover.md) and [`playerCount -> text`](options/playercount/text.md)).  
It supports all available [formatting tags][tags] with a [few exceptions and limitations](#exceptions-and-limitations).

/// note
Please note that the available tags in MiniMessage depend heavily on the version used on the Server or Proxy.  
While AdvancedServerList downloads MiniMessage for BungeeCord, is it using the built-in version on Paper and Velocity. Due to this could the version used be older than the one the plugin is compiled against.
///

[minimessage]: https://docs.advntr.dev/minimessage/index.html
[tags]: https://docs.advntr.dev/minimessage/format.html

### Why MiniMessage and not `&`/`§`? { #why-minimessage }

The reason to only support MiniMessage and not the so-called legacy color and formatting codes can be boiled down to 3 specific reasons:

1.  **MiniMessage tags are more clear on what they do.**  
    `<aqua>` makes it clear that it colors the text while `&b` is not as clear on that matter.
2.  **MiniMessage allows easier combination of color and formatting codes.**  
    MiniMessage "remembers" what colors and formatting you applied, meaning if you close a tag and a previous one was set before it, that will continue.
    
    /// details | Example
        type: example
    
    Below is a comparison of the same text made in MiniMessage and Color codes.
    
    //// tab | MiniMessage
    ```mm
    <aqua>Hello <green><bold>World! <red>How</bold> are</red> you?
    ```
    ////
    
    //// tab | Color Codes
    ```
    # We add &c after "How" to avoid a "bold space"
    
    &bHello &a&lWorld! &c&lHow&c are &ayou?
    ```
    ////
    ///
    
3.  **MiniMessage adds easier support for advanced features (RGB colors, gradients, etc.)**  
    With MiniMessage is it significantly more easy to use more advanced features such as gradients, rgb colors and similar.  
    While legacy colors have a form of RGB support is the format weird to look at and gradients become a mess. This is even worse when including bold for specific characters.
    
    /// details | Example
        type: example
    
    Below is an example of a Gradient starting at `#084CFB` (:octicons-dot-fill-24:{ style="color: #084CFB;" }) and ending at `#ADF3FD` (:octicons-dot-fill-24:{ style="color: #ADF3FD;" }).
    
    //// tab | MiniMessage
    ```mm
    <gradient:#084CFB:#ADF3FD>Hello World!</gradient>
    ```
    ////
    
    //// tab | Color Codes
    ```
    &#084CFBH&#195DFBe&#296DFBl&#3A7EFCl&#4A8FFCo &#6BB0FCW&#7CC1FCo&#8CD2FDr&#9DE2FDl&#ADF3FDd
    ```
    ////
    ///

In general is MiniMessage a lot easier to manage and read as legacy color codes, as their appearance is more distinct from normal text.

### Exceptions and Limitations

Due to the situation MiniMessage is used in are not all of its features available to use.  
Below is a list of all features that cannot be used by AdvancedServerList, or can only be used by it under specific conditions.

| Feature       | Tag                          | Supported?                                                                                                                                  |
|---------------|------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------|
| Hex Colors    | `#!mm <#rrggbb>`                  | Only works properly on `motd` and will be downsampled to the closest named color for the other options.                                     |
| Hex Gradients | `#!mm <gradient:#rrggbb:#rrggbb>` | Only works properly on `motd` and will be downsampled to the closest named color for the other options.                                     |
| Click Actions | `#!mm <click:_type_:_value_>`     | Tag will be rendered, but clicking the text won't do anything.                                                                              |
| Hover Actions | `#!mm <hover:_type_:_value_>`     | Tag will be rendered, but hovering the text won't show anything.                                                                            |
| Insertions    | `#!mm <insertion:_value_>`        | Tag will be rendered, but clicking the text won't do anything.                                                                              |
| Font          | `#!mm <font:_font_>`              | May work with the default fonts (`default`, `alt` and `uniform`) and with custom ones should the client already have it.                    |
| Selector      | `#!mm <selector:_sel_>`           | Tag will be rendered, but clicking the text won't do anything.                                                                              |
| Score         | `#!mm <score:_name_:_objective_>` | Will not render due to requiring the player to be on the server.                                                                            |
| Pride         | `#!mm <pride[:_flag_]>`           | Only works if the server (Paper) or Proxy (Velocity) use `v4.18.0` of MiniMessage. BungeeCord is unaffected due to downloading the library. |

## Yaml Formatting

YAML has some special quirks that require attention when editing the file.  
Such quirks include features such as...

- Reading `~` and `null` as literal null values.
- Reading `>` and `|` as multi-line indicators.

To avoid this should you always surround text values with single (`'`) or double quotes (`"`) to force the YAML parser to see those as Strings.

/// note | Note on Single quotes inside text
Using single quotes, the YAML parser may get confused when the text itself also contains a single quote.  
As an example `'You're banned'` would mess up the value because the `'` in `You're` is understood as the end of the String.

To avoid this issue, do one of the following:

- Surround the text with Double quotes instead of Single quotes (`"You're banned"`)
- Use two single quotes (`''`) inside the text every time you want to display a single quote character (`'You''re banned'`)
///