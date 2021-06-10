## AnimalCategory

```json
AnimalCategory "Sows" { 
    species = "Schwein"
	last-modified = ""
	name = "Sauen und Aufzuchtferkel"
	thumbnail = "Thumbnail-ID"
    assessment_duration = 7
    group_area_optional = false
	Branch "BranchLabel" {
		name = "Name des Branches"
		description = "Beschreibung des Branches"
	}
	Branch "BranchLabel2" {
		name = "Name des Branches"
		description = "Beschreibung des Branches"
	}
}
```

**Stanza-Keyword:** `AnimalCategory`

**Named-Stanza:** yes

**Required:** yes

---

### Elements

---

 #### species

**Required:** yes

**Value-Type**: `String`

Contains the species this `AnimalCategory` is part of.  It should be possible to group multiple `AnimalCategory`s by this property.

---

#### last-modified

**Required:** yes

**Value-Type:** `TimeStamp`

---

#### name

**Required:** yes

**Value-Type:** `String`

Human readable name of the `AnimalCategory`.

---

#### description

**Required:** no

**Value-Type:** `String` 

Human readable description of the `AnimalCategory`.

---

#### thumbnail

**Required:** no

**Value-Type:** `ImageID`

Thumbnail representing the `AnimalCategory`.

---

#### assessment_duration

**Required:** yes

**Value-Type:** `Numeric`

Amount of days until the sample-size of an ongoing assessment has to be reset. This is only used for usability reasons.

---

#### group_area_optional

**Required:** yes

**Value-Type:** `Boolean`

Whether or not the area property of a group configured is optional or not. E.g. for "MastRind" it is not optional, since it is used within some indicators.

---

#### images_dir

**Required:** yes

**Value-Type:** `String`

Contains the name of the directory holding images used for this `ProductionSystem`.

---

### Inner Stanzas

---

#### Branch

**Required:** no

**Value-Type:** `Branch`

Multiple *Branch*  Elements are possible.

---

---

## Branch

```json
Branch "branchLabel" {
		name = "Name des Branches"
		description = "Beschreibung des Branches"
}
```

**Stanza-Keyword:** `Branch`

**Named-Stanza:** yes

**Required:** no

This stanza is used to describe production branches of the parent `AnimalCategory`.

This can be used to filter *Indicator*s.

---

### Elements

---

#### name

**Required:** yes

**Value-Type:** `String`

Human readable name.

---

#### description

**Required:** no

**Value-Type:** `String`

Human readable description.