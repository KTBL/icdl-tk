## Structure of this Document

First we will describe the basic syntax of the *Indicator Description Language* (*ICDL*). Then we're going to list each part of an *Indicator*.  Hereby each part is follows the same pattern:

1. Keyword / Stanza Name
2. Value Type
3. Description
4. Example

---

---

## Basics of the syntax

The syntax used for the *ICDL* is called *HCL*. The *ICDL* is basically the *HCL* format with predefined possible key-value pairs and stanzas.

### Key-Value Pairs
A simple key-value looks like:
```json
key = value
```
Such a pair can be part of a stanza. A value can be either:
* `Numeric` - a number (e.g. `1`)
* `String` - a string (e.g. `"some text"`)
* `Boolean` - a boolean (`true` , `false`)
* `Maps`- `{"key" = "value", "key2" = "value2"}`
* `ImageId` - 
* `TimeStamp` - has the format: `yyyymmdd` (e.g. `20200504` for the 4th may 2020)
* `ref Stanza` - a reference to a named stanza
* `List[contentType]` - an array of the above mentioned types (e.g. `["A", "B", "C"]`, `[1, 2, 3]`). The `contentType` here describes what is within the list.
* `Formula` - a mathematical formula which can be parsed by http://mathparser.org

A key is always lower case written, and if it contains multiple words these words are concatenated using a dash ("-"). 

```json
test-key = "test" // valid!
Test-Key = "test" // invalid!
testKey = "test"  // invalid!
testkey = "test"  // invalid!

```



### Stanzas
An object, named or unnamed, is called a stanza. E.g.
```json
Unnamed {
    ...
}

Named "name" {
    ...
}
```

Each stanza starts with a capital letter.

```json
Indicator "testIndicator" { // This is valid
    ...
}

indicator "testIndicator" { // This is not valid
    ...
}
```

If stanza is named, it's name is only used within the *ICDL* like a variable name. Stanza-names are not allowed to contain any whitespaces. Use "_" instead.



### Multi-line-Text

Multi-line-Text can be used via [Heredoc-Syntax](https://de.wikipedia.org/wiki/Heredoc). An example:

```json
<<EOF
some text
over
multiple lines
EOF
```

`<<EOF` indicates the beginning of a multi-line text. Thereby `EOF` can basically be replaced with every other word, but, for readability reasons one should stick with `EOF` where every possible (the only case where it is not possible is a case where EOF appears as content in the multi-line text). To mark the end of the multi-line text write `EOF` (or its replaced) again at the end. Important: right behind `<<EOF` only a new line is allowed, no other stuff - not even white spaces.  The same applies before the end marker.

### Formulas

The expression parser used with the ICDL can be found at: http://mathparser.org

There are multiple variables which are predefined to be used within expressions:

- `herd_size`: amount of animals in context of the current herd
- `sample_size`:  sample size which is used for the current herd in the current indicator
- `stall_areal_size`:  size of the stall area in m²

An example to calculate a sample size by using the `herd_size` as variable:

```
floor(18.02 * ln(herd_size) - 32.204)
```

Functions predefined in `mathparser` can be found here:  http://mathparser.org/mxparser-tutorial/using-internal-help/