---
icon: octicons/pencil-24
---

# Formatting

AdvancedServerList utilizes the [MiniMessage] text formatting to allow colors, formatting codes, gradients, etc. for the text options `motd`, `playerCount -> hover` and `playerCount -> text`.  
You can find a collection of available tags on the library's [Format] page. Please make sure to see the [Unsupported Formatting](#unsupported-formatting) section below for tags with no support.

## Why no legacy colors (`&`/`§` codes) support?

### The tl;dr

MiniMessage tags are overall more readable while also allowing a lot more ways of formatting, with Legacy colors becoming an unreadable mess the more complex your formatting is.

### The long explanation

Legacy colors are a mess to read. This becomes especially evident with gradients using RGB color support.  
As an example is here the text `Hello World` made into a Gradient with starting color `#084CFB` and end color `#ADF3FD` both in Legacy Color (Using `&` character) and MiniMessage format:

/// tab | Legacy Color Codes
```
&#084CFBH&#195DFBe&#296DFBl&#3A7EFCl&#4A8FFCo &#6BB0FCW&#7CC1FCo&#8CD2FDr&#9DE2FDl&#ADF3FDd
```
///

/// tab | MiniMessage
```
<gradient:#084CFB:#ADF3FD>Hello World</gradient>
```
///

This becomes even more difficult to work with if you want to add text formatting to only parts of the text.  
Assuming you want to make `World` be bold, with legacy colors, you would need to add `&l` before every character of the word, as colors reset any formatting applied. In MiniMessage all you need to do is put `<bold>` before the word and `</bold>` after it.

Another benefit for gradients in particular is, that MiniMessage allows more than one color to be set for the gradient, i.e. you can set three colors for a start, middle and end color.

## Web Editor

The Adventure team offers an [Online Web tool][webviewer]{ target="_blank" rel="nofollow" } that allows you to create the right MiniMessage tags while also having a preview.  
Just visit the page and make sure to select `Server List` at the top to have a preview in a server list.

## YAML Formatting

It is recommended to always surround your text with single or double quotes. This avoids situations where the YAML parses sees a specific character and treats it as a special YAML feature.  
This is especially the case with MiniMessage tags, where `<` can be seen as the start of a so-called scolar value.

Should you be using single quotes inside the text itself - i.e. for words such as `you're` - will you either need to surround the text with double quotes, or change the single quote in the word to two single quotes (`you're` becomes `you''re`).

## Unsupported Formatting

### Non-functional Tags

While MiniMessage will parse most, if not all tags, will not all work.  
The following list of tags will do nothing, or only work under specific circumstances:

| Tag           | Note                                                                                                 |
|---------------|------------------------------------------------------------------------------------------------------|
| `<click>`     |                                                                                                      |
| `<hover>`     |                                                                                                      |
| `<insertion>` |                                                                                                      |
| `<font>`      | May work if either a default MC font is used, or the player has a resourcepack with the font loaded. |
| `<selector>`  |                                                                                                      |
| `<score>`     |                                                                                                      |

### Non-supported RGB

Only the `motd` option supports RGB colors. Every other option allowing MiniMessage format may only support the default colors provided by Minecraft (`<aqua>`, `<yellow>`, etc) with RGB colors being downsampled to their next supported option.

[MiniMessage]: https://docs.advntr.dev/minimessage/index.html
[Format]: https://docs.advntr.dev/minimessage/format.html
[webviewer]: https://webui.advntr.dev/