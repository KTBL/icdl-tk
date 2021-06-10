## Options 

Full example:

```json
[..]
Options {
    Group {
    	name = "Bonituren Gruppe 1"
        Var "class0" {
            name = "keine Veränderung" 
    		thumbnail = "<imageid>"
            short-description = ""
            images = []
            long-description = "...."
        }
        Var "class1" {
            name = "deutliche Rötung" 
            thumbnail = "<imageid>"
            short-description = "Deutlich sichtbare Rötung der betroffenen Stelle (Druckstelle), aber Haut nicht durchbrochen" 
            long-description = "..."
            images = ["ida", "idb"]
        }
        Var "class2" {
            name = "Sichtbare Wunde" 
            thumbnail = "<imageid>"
            short-description = "Deutlich sichtbare offene, frische oder verkrustete Wunde (Läsion; Haut durchbrochen)" 
            long-description = <<EOF
Eine sehr sehr lange Beschreibung was 'Sichtbare Wunde' bedeutet
EOF
            images = ["id1", "id2"]
        }
	}
	Group {
        name = "Bonituren Gruppe 2"
		Var "class3" {
            name = "keine Veränderung" 
            short-description = ""
			thumbnail = "<imageid>"
            images = []
            long-description = "...."
        }
        Var "class4" {
            name = "Sichtbare Wunde" 
			thumbnail = "<imageid>"
            short-description = "Deutlich sichtbare offene, frische oder verkrustete Wunde (Läsion; Haut durchbrochen)" 
            long-description = <<EOF
Eine sehr sehr lange Beschreibung was 'Sichtbare Wunde' bedeutet
EOF
            images = ["id1", "id2"]
        }
    }

	ComposedVar "varname1" {
        expression = "(class4 - class3) * 0.8"
    }

	ComposedVar "varname2" {
        expression = "class4 * 1.2"
    }
}
[..]
```

**Stanza-Keyword:** `Options`

**Named-Stanza:** no

**Required:** yes

An Options structs provides at least 2 possible options (called `Var`). Vars are grouped via the `Group` stanza. Grouped `Var`s should be grouped in the same way in the UI-representation. In case of a classification indicator, always just a single `Var` of each group can be chosen. There is always at least one group.

---

### ComposedVar

```json
ComposedVar "varname2" {
	expression = "A * B + 1"
}
```

**Stanza-Keyword:** `ComposedVar`

**Named-Stanza:** yes

**Required**: no

This stanza represents a composed input variable. This is to be used if `Var` elements the user has to provide are not directly to be used in evaluation. The `expression` provided to a `ComposedVar` is evaluated in the context of the input phase. For example: 

The user has to pass how many wounds an animal has. If an animal has at least two wounds it is marked as "generally wounded". To decide whether or not an animal is "generally wounded" a `ComposedVar` is needed. The `exepression` is then evaluated in context of the single animal.



Each `Options` stanza can host zero to many `ComposedVar` stanzas.

## Elements

#### expression

**Required:** yes

**Type:** `Formula`

The `ComposedVar` is not fulfilled in case the return value x of `expression` is: -1 < x < 1

The `ComposedVar`is fulfilled in case the return value x of `expression is: ` x <= -1 OR x >= 1



---

## Group

```json
Group {
    name = "Name der Gruppe"
    Var "A" {
    [...]
	}
	Var "B" {
    [...]
    }
}
```

**Stanza-Keyword:** `Group`

**Named-Stanza:** no

**Required:** yes

---

### Elements

---

#### name

**Required:** yes

**Value Type:** `String`

Name of the group used to identify the group on the UI. Thus the name needs to be understandable for a user.

---

## Var 

```json
Var "class0" {
    name = "keine Veränderung" 
    long-description = ""
    short-description = ""
    images = ["<image-id1>", "<image-id2>"]
}
```

**Stanza-Keyword:** `Var`

**Named-Stanza:** yes

**Required:** yes

The `Var` Stanza describes a variable to be used within another Stanza. For example to describe possible options within a *Classification* or *Counter* Indicator.

---

### Elements

---

#### name

**Required:** yes

**Value Type:** `String`

Name of this option. E.g. "Boniturstufe 1" , "Keine Verletzungen"

---

#### short-description

**Required:** yes

**Value Type:** `String`

Short description of this variable / options. This should not be longer than #tbd characters.

---

#### long-description

**Required:** yes

**Value Type:** `String`

Long description of this variable / options. This should not be longer than #tbd characters.

---

#### images

**Required:** no

**Value-Type:** `List[ImageId]`

List of `ImageId` which describe the variable.

---

#### thumbnail

**Required:** yes

**Value-Type:** `ImageID`

Thumbnail to be shown besides the short-description.

---

