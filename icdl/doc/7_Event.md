## Events - currently not used

```json
Event "chickenHatch" {
    last-modified = ""
    production = "chicken"
    name = "Schlüpfen der Hühner"
    description = "Sobald die Hühner schlüpfen..."
}
```

**Stanza-Keyword:** `Event`

**Named:** yes

**Required:** no

---

### Elements

---

#### last-modified

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

Human readable name of the *Event*.

---

#### description

**Required:** yes

**Value-Type:** `String`

Human readable description of the *Event*.

---

---

---

