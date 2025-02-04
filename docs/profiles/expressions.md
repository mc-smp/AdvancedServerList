---
icon: octicons/file-code-24
---

Expressions are part of a Server List Profile's condition system and allows you to define when a profile should and should not be displayed.

This page covers what the Expression system of AdvancedServerList offers and can do.

## Content

- [Types](#types)
    - [Literals](#literals)
        - [Numbers](#numbers)
        - [Strings](#strings)
        - [Booleans](#booleans)
    - [Placeholders](#placeholders)
    - [Binary Operators](#binary-operators)
    - [Parenthesis](#parenthesis)
    - [Negation](#negation)
- [Credits](#credits)

## Types

An expression contains different types of components that are evaluated to return a boolean output of either `true` or `false`.  
Depending on the type(s) will the output be differently understood and handled.

### Literals

The following cases are considered literal values:

#### Numbers

Any number is considered an expression, meaning the below examples are considered valid:

/// example | Examples
```ruby
0
1000
-7
47.2
```
///

#### Strings

A String in single or double quotes is a valid expression.

/// warning | Important
Specific keywords will be treated as [Binary Operators](#binary-operators) even if they are part of a word.  
As an example, `Sand` would cause the expression engine to see `S AND` due to parts of the word matching `and`.

To avoid this is it very important to always surround Strings with single or double quotes, as that tells the expression engine to treat as a String.
///

/// example | Examples
```ruby
"Hello World!"
""
```
///

#### Booleans

Strings `true` and `false` are treated as boolean literals.

### Placeholders

Any [placeholder](placeholders.md) is a valid expression.

/// example | Examples
```ruby
${player protocol}
${player name}

${server playersOnline}
${server host}
```
///

### Binary Operators

`<expression> <binary operator> <expression>` is a valid expression.

The following binary operators can be used for boolean evaluation:

| Operator      | True if                                                                       | Case Sensitive? |
|---------------|-------------------------------------------------------------------------------|-----------------|
| `and` / `&&`  | Both expressions return true.                                                 | No              |
| `or` / `\|\|` | Either expression returns true.                                               | No              |
| `==` / `=`    | Both expressions are equal (Case sensitive).                                  | Yes             |
| `=~` / `~`    | Both expressions are equal (Not Case sensitive).                              | No              |
| `!=`          | Both expressions are not equal (Case sensitive).                              | Yes             |
| `!~`          | Both expressions are not equal (Not case sensitive).                          | No              |
| `\|-`         | Left expression starts with right expression (Case sensitive).                | Yes             |
| `\|~`         | Left expression starts with right expression (Not case sensitive).            | No              |
| `-\|`         | Left expression ends with right expression (Case sensitive).                  | Yes             |
| `~\|`         | Left expression ends with right expression (Not case sensitive).              | No              |
| `<_`          | Left expression contains right expression (Case sensitive).                   | Yes             |
| `<~`          | Left expression contains right expression (Not case sensitive).               | No              |
| `<`           | Left expression is less than the right expression.^[1](#n1)^                  | No              |
| `<=`          | Left expression is less than, or equal to, the right expression.^[1](#n1)^    | No              |
| `>`           | Left expression is larger than the right expression.^[1](#n1)^                | No              |
| `>=`          | Left expression is larger than, or equal to, the right expression.^[1](#n1)^  | No              |

These additional binary operators can also be used to perform certain actions:

| Operator | Semantic                                     |
|----------|----------------------------------------------|
| `.`      | Concatenates (merges) two Strings            |
| `+`      | Adds two numbers.^[1](#n1)^                  |
| `-`      | Subtracts one number from another.^[1](#n1)^ |
| `*`      | Multiplies two numbers.^[1](#n1)^            |
| `/`      | Divides one number by another.^[1](#n1)^     |

/// example | Examples
```ruby
${player name} != "Anonymous"
763 > ${player protocol} > 758
${player hasPlayedBefore} and ${player isBanned} == "false"
```
///

<small>^1^{ #n1 } In case of text will the text length be used to compare against.</small>

### Parenthesis

`( <expression> )` is a valid expression.
Parenthesis can be used to prevent ambiguities.

### Negation

`!` can be used to negate boolean expressions.

/// example | Examples
```ruby
!${player isWhitelisted}
```
///

## Credits

This page, including some examples used, was adobted from [:octicons-book-24: CodeCrafter47/BungeeTabListPlus/wiki](https://github.com/CodeCrafter47/BungeeTabListPlus/wiki/Expressions){ target="_blank" rel="nofollow" }.