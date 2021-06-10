## Assessment

```json
Assessment {
		default_type = "By_Indicator|By_Animal|Report|Environment"
    	type_changeable = true | false
		sample = "0.8 * herd_size"
		presentation = "classification|counter"
		presentation_changeable = true | false
		Options { 
            [..]
		}
		Evaluation {
            [..]
        }
	}
```

**Stanza-Keyword:**  `Assessment`

**Named-Stanza:** yes

**Required:** yes

The *Assessment* hold information about how to collect data for an *Indicator*. This object is nested into *Indicator*s.

---

### Elements

---

#### default_type

**Required:** yes

**Value-Type:** `Enum String`

**Enum Value:**

- `By_Animal`

  Used if this indicator should be recorded by moving from animal to animal.

- `By_Indicator`

  Used if this indicator should be recorded by taking a look at multiple animals at once.

- `Report`

  Used if this indicator is recorded by reviewing reports and documents (alias office-indicator).

- `Environment` 

  Used if this indicator is used to record parts of the environment (like water bowls).

`Report` and `Environment` Indicators are by default not allowed to be used otherwise. Whether or not an `By_Animal` indicator my be used as `By_Indicator` indicator, and vice  versa, or not is indicated via the field `type_changeable`

---

#### type_changeable

**Required:** no

**Value-Type:** `boolean`

Shows whether or not is is allowed to change the type (indicated by `default_type`) from `By_Animal` to `By_Indicator` or respectively from `By_Indicator` to `By_Animal`. `Report` and `Environment` are by default not changeable.

---

#### timing

*currently not used* 

**Required:** yes

**Value-Type:** `List[Enum String]`

**Enum Value:**

 - `halfyear`

 - `quarteryear`

 - `year`

 - `monthly`

 - `weekly`

 - `summer`

 - `winter`

 - `onEvent` - 

   
---

#### on-event

*currently not used* 

**Required:** only if timing contains `onEvent`

**Value-Type:** `List[ref Event]`



---

#### location

*currently not used* 

**Required:** no

**Value-Type:** `List[ref Location]`



---

#### presentation

**Required:** yes

**Value-Type:** `Enum String`

**Enum Value:** 

- `classification` 

  If a symbolic classification has to be done. For example:

  *Which of the following color has the cow in front of you?*

  - *Red?*
  - *Blue?*
  - *Yellow?*

- `counter`

  If multiple symbolic classifications should be done at once. For example:

  *How many cows with each of the following colors do you see in front of you?*

  - *Red? 2 cows*
  - *Blue? 5 cows*
  - *Yellow? 3 cows*

Defines the type of the *Indicator* and therefore it's representation and evaluation scheme.

Whether or not this can be changed to one another is indicated via the field `assessment_changeable`

---

#### presentation_changeable

**Required:** yes

**Value-Type:** `boolean`

Indicates whether an indicator which has set `presentation = "classification"` is also allowed to be assessed using `counter` and vice versa.

---

#### sample

**Required:** yes

**Value-Type:** `Formula`

This represents the formula to calculate the sample size which should be recorded. Most of the time the sample size is based on `herd_size`.



---

### Nested Elements

---

#### Sample

**Stanza-Type:** `Sample`

---

#### Options

**Stanza-Type: ** `Options`

---

#### Evaluation

**Stanza-Type:** `Evaluation`



