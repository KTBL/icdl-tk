## Elements of an Indicator

An *Indicator* describes a method to collect specific information about animals. If enough data has been collected for an *Indicator* it is possible to evaluate these data. The evaluation result points out the health and wealth status of the checked animals regarding the captured *Indicator*.

An *Indicator* is a named stanza.

**Named-Stanza:** yes

**Keyword:** `Indicator`

**Required:** yes

**Example:** 

```json
Indicator "uncleanPig"{
	name = "Kotverschmutzung der Tiere"
    description = "Saubere Borsten sind [...]"
    last-modified = "20200405"
	animal-category = "sows"
    sorting = 10
    branches = ["sowsBranch1", "sowsBranche2"]
    what-and-why = "Was und Warum?"
	when-and-how-often = "Wann und wie oft?"
	which-and-how-many = "Welche und wie viele?"
	how = "Wie?"
	video-ref = {"title of video" = "link to video", "title of another video" = "link to another video"}
	Assessment {
		[.. see Assessment ..]
	}
	Evaluation{
		[.. see Evaluation ..]
	}
}
```

---

### Elements
The following elements are root-children within an *Indicator* declared. They're simple key-value pairs.

---

#### name

**Required:** yes

**Value-Type:**  `String`

Human readable name of the *Indicator*.

---

#### description

**Required:** no

**Value Type:** `String`

A short description of the Indicator. Typically this should contain information about what the *Indicator* measures and why this is important.

---

#### last-modified

**Required:** yes

**Value Type:** `TimeStamp`

Timestamp of the last change of the indicator.

---

#### animal-category

**Required:** yes

**Value Type:**  `ref AnimalCategory`

This field holds the basic species this *Indicator* is for.

**Example:**

```json
anima-category = "sows"
```

---

#### branches

**Required:** no

**Value-Type:** `List[ref Branch]`

List of `Branch` this *Indicator* is related to. This is basically only a grouping criterion for Indicators. There can only be one production-subtype for an indicator. Indicators that are the same for several production-subtypes have to be copied. The reason is, that sameness at the point in time, the indicators were created does not indicate general sameness. For example, the number of options in a classification might later be changed for only _one_ of the production-subtypes an indicator is valid for. Then the user/developer of the indicator description would have to remember copying later on and changing the lists in both indicators, which is prone to referential errors.

**Example:**

```json
branches = ["branchLabel1", "branchLabel2"]
```

---

#### what-and-why

**Required:** no

**Value-Type:** `String`

Explanation taken from the reference book.

---

#### when-and-how-often

**Required:** no

**Value-Type:** `String`

Explanation taken from the reference book.

---

#### which-and-how-many

**Required:** no

**Value-Type:** `String`

Explanation taken from the reference book.

---

#### how

**Required:** no

**Value-Type:** `String`

Explanation taken from the reference book.

---

#### group-with

**Required:** no

**Value-Type:** `List[ref Indicator]`

If the *Indicator* needs to be assessed together (in a row) with other *Indicator*s this can be marked by referencing these *Indicator*s in this list.

---

#### video-ref

**Required:** no

**Value-Type:** `map`

If there is a video explaining the `Indicator` the Hyperlink to the video can be placed here.

**Example:**

```json
video-ref = {"Title of Video 1" = "youtube link to video 1", "Title of Video 2" = "youtube link to video 2"}
```

---

#### sorting

**Required:** yes

**Value-Type:**  `Numeric`

Used to define a sorting. The numeric value thereby is a sorting weight. The application sorts indicators shown, for example in lists, sorted by this weight. Multiple indicators are allowed to have the same weight. In such a case the application has to decide how to sort further.

---

### Nested Elements

The following elements are root-children within an indicator declared. They are nested elements.

##### Assessment
Information about the assessment of this indicator. For a detailed description of the *Assessments*' content see *Elements of an Assessment*.

---

##### Evaluation
Information about the evaluation of this indicator. For a detailed description of the *Evaluations*' content see *Elements of an Evaluation*.

---
