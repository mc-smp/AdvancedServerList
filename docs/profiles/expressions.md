---
icon: octicons/file-code-24
---

The condition of a Server List Profile consists of one or multiple expressions.  
The complete expression should return a boolean. If not, will the returned value be converted based on the following rules:

- Numbers will be converted to true, if not `0`
- Strings will be converted to true, if equal to `"true"`

The content below lists all supported forms of expressions that can be used to form a condition:

## Literals

The following values are treated as literal and can form a valid expression.

### Numbers

Any postive or negative number with or without decimal points can form a valid expression.

/// example | Examples
```ruby
0
100
-1.0
10.2
```
///

### Strings

Any quoted String can form a valid expression.

/// note
Always surround Strings with single or double quotes to avoid ambiguities.  
The parser may understand specific characters as [binary operands](#binary-operators) should they match an existing text causing issues. Wrapping a String in single or double quotes forces it to treat the value as a String literal.
///

/// example | Examples
```ruby
"Hello World!"
''
```
///

### Booleans

Words `true` and `false` (case insensitive) are treated as literal boolean values - unless quoted - and can form valid expressions.

/// example | Examples
```ruby
true
false
```
///

## Placeholders

[Placeholders](placeholders.md) are resolved into corresponding [Literals](#literals) and can therefore be valid expressions.

/// example | Examples
```ruby
${player name}
${player protocol}

${server host}
```
///

## Binary Operators

Binary operators can be used to combine multiple expressions to form a new one.

### Boolean Binary Operators

The following binary operators can be used for boolean evaluation:

| Operator      | True if                                                                      | Case Sensitive? |
|---------------|------------------------------------------------------------------------------|-----------------|
| `and` / `&&`  | Both expressions return true.                                                | No              |
| `or` / `\|\|` | Either expression returns true.                                              | No              |
| `==` / `=`    | Both expressions are equal.                                                  | Yes             |
| `=~` / `~`    | Both expressions are equal.                                                  | No              |
| `!=`          | Both expressions are not equal.                                              | Yes             |
| `!~`          | Both expressions are not equal.                                              | No              |
| `\|-`         | Left expression starts with right expression.                                | Yes             |
| `\|~`         | Left expression starts with right expression.                                | No              |
| `-\|`         | Left expression ends with right expression.                                  | Yes             |
| `~\|`         | Left expression ends with right expression.                                  | No              |
| `<_`          | Left expression contains right expression.                                   | Yes             |
| `<~`          | Left expression contains right expression.                                   | No              |
| `<`           | Left expression is less than the right expression.^[1](#n1)^                 | No              |
| `<=`          | Left expression is less than, or equal to, the right expression.^[1](#n1)^   | No              |
| `>`           | Left expression is larger than the right expression.^[1](#n1)^               | No              |
| `>=`          | Left expression is larger than, or equal to, the right expression.^[1](#n1)^ | No              |

/// example | Examples
```ruby
${player name} != "Anonymous"
763 > ${player protocol} > 758
${player hasPlayedBefore} and ${player isBanned} == "false"
```
///

### Other Binary Operators

These additional binary operators can also be used to perform certain actions:

| Operator | Semantic                                     |
|----------|----------------------------------------------|
| `.`      | Concatenates (merges) two Strings            |
| `+`      | Adds two numbers.^[1](#n1)^                  |
| `-`      | Subtracts one number from another.^[1](#n1)^ |
| `*`      | Multiplies two numbers.^[1](#n1)^            |
| `/`      | Divides one number by another.^[1](#n1)^     |

<small>^1^{ #n1 } In case of text will the text length be used to compare against.</small>

/// example | Examples
```ruby
${player protocol} + 10
"<green>" . ${player name}
```
///

## Parenthesis

Parenthesis can be used to enclose one or multiple expressions to create a new expression and avoid ambiguities.

/// example | Example
```ruby
(${player protocol} + 10) / 100
```
///

## Negation

`!` can be used to negate boolean expressions.

/// example | Examples
```ruby
!${player isWhitelisted}
```
///

## Credits

This page, including some examples used, was adobted from [:octicons-book-24: CodeCrafter47/BungeeTabListPlus/wiki](https://github.com/CodeCrafter47/BungeeTabListPlus/wiki/Expressions){ target="_blank" rel="nofollow" }.