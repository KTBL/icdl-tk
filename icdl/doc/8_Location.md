## Location - currently not used

```json
Location "hatchingHall" {
    last-modified = ""
    production = ""
    name = "Schlüpfbereich"
    description = "Bereich in dem die Küken schlüpfen."
}
```

**Stanza-Keyword:** `Location`

**Named-Stanza:** yes

**Required:** no

A *Location* object describes a location which is used in the production system and therefore where it is needed to assess indicators.

---

###  Elements

------

#### mod-time-stamp

**Required:** yes

**Value-Type:** `TimeStamp`

---

#### production

**Required:** yes

**Value-Type:** `ref ProductionSystem` 

---

#### name

**Required:** yes

**Value-Type:** `String`

Human readable name of the *Location*.

------

#### description

**Required:** no

**Value-Type:** `String`

Human readable description of the *Location*.

---

---

---



