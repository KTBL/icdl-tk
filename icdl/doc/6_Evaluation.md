## Evaluation 

Full Example:

```json
Evaluation {
    Formula {
    	name = "Anteil der Tiere mit X"
    	description = "Dieser Wert ist wichtig weil Apfelbaum und deswegen muss er drölf mal im Jahr erhoben und ausgewertet werden. Die Aussagekraft hängt hierbei stark davon ab wie viele schwarze Flecken eine Kuh hat."
    	expression = "floor(18.02 * ln(bonitur1) - 32.204)"
        Ranges {
    		Range {
    			branches = ["branchA", "branchB"]
                unit = "%|m²|kg|..."
                visual-lowerbound = 15.5
                visual-upperbound = 75.0
                Targetrange {
                    tag = "Zielbereich" 
                    description = "Kaum ein Tier hat xy."
                    lowerbound = 60 //Dezimalzahlen wären hier genauso möglich
                    upperbound = 100 
                }
                Alertrange {
                    tag = "Alarmbereich"
                    description = "Zu viele Tiere haben XY, sofortiger Handlungsbedarf ist nötig!"
                    lowerbound = 0
                    upperbound = 30
                }
                Warningrange {
                    tag = "Frühwarnbereich"
                    description = "Es läuft nicht optimal. Handlungsbedarf ist vorhanden"	
                }
            } 
    		Range {
    			branches = ["branchC"]
                unit = "%|m²|kg|..."
                visual-lowerbound = 15.5
                visual-upperbound = 75.0
                Targetrange {
                    tag = "Zielbereich" 
                    description = "Kaum ein Tier hat xy."
                    lowerbound = 40 //Dezimalzahlen wären hier genauso möglich
                    upperbound = 100 
                }
                Alertrange {
                    tag = "Alarmbereich"
                    description = "Zu viele Tiere haben XY, sofortiger Handlungsbedarf ist nötig!"
                    lowerbound = 0
                    upperbound = 20
                }
                Warningrange {
                    tag = "Frühwarnbereich"
                    description = "Es läuft nicht optimal. Handlungsbedarf ist vorhanden"	
                }
            } 
		}
	}

 	Formula {
    	name = "Anteil der Tiere mit X"
    	description = "Dieser Wert ist wichtig weil Apfelbaum und deswegen muss er drölf mal im Jahr erhoben und ausgewertet werden. Die Aussagekraft hängt hierbei stark davon ab wie viele schwarze Flecken eine Kuh hat."
    	expression = "floor(12 * ln(bonitur_2))"
    }
}
```
**Stanza-Keyword:**  `Evaluation`

**Named-Stanza:** no

**Required:** yes

The *Evaluation* holds every information needed to evaluate the recorded indicator data

---

### Formula

**Stanza-Keyword:** `Formula`

**Named-Stanza:** no

**Required:** yes

Each `Formula` holds all information needed to represent a single Indicator result which is calculated by a single formula.

---

#### Elements

---

#### name

**Required:** yes

**Value-Type:** `String`

Short name / description what this formula is about.

---

#### description

**Required:** yes

**Value-Type:** `String`

Longer description to explain what this formula is about and what the user has know to interpret it.

---

#### expression

**Required:** yes

**Value-Type:**`Formula`

The basics of Formulas can be found in the first part of this specification.

To use variables which identify the options defined previously, the option-stanza-name can be used.

For Example:

Let's say our defined options for this indicator are as following:

```yaml
Options {
    Group {
    	name = "Bonituren Gruppe 1"
        Var "class_0" {
            name = "keine Veränderung" 
    		thumbnail = "<imageid>"
            short-description = ""
            images = []
            long-description = "...."
        }
        Var "class_1" {
            name = "deutliche Rötung" 
            thumbnail = "<imageid>"
            short-description = "Deutlich sichtbare Rötung der betroffenen Stelle (Druckstelle), aber Haut nicht durchbrochen" 
            long-description = "..."
            images = ["ida", "idb"]
        }
        Var "class_2" {
            name = "Sichtbare Wunde" 
            thumbnail = "<imageid>"
            short-description = "Deutlich sichtbare offene, frische oder verkrustete Wunde (Läsion; Haut durchbrochen)" 
            long-description = <<EOF
Eine sehr sehr lange Beschreibung was 'Sichtbare Wunde' bedeutet
EOF
            images = ["id1", "id2"]
        }
	}
}
```

In this case the percentage of animals with `class_0` "keine Veränderungen" can be calculated using the following expression:

``` mathematica
(class_0 / (class_0 + class_1 + class 2)) * 100
```



---

### Range

```json
Ranges {
    unit = "%|m²|kg|..."
    visual-lowerbound = 15.5
    visual-upperbound = 75.0
    branches = ["branchA", "branchB"]
    Targetrange {
        tag = "Zielbereich" 
        description = "Kaum ein Tier hat xy."
        lowerbound = 60 //Dezimalzahlen wären hier genauso möglich
        upperbound = 100 
    }
    Alertrange {
        tag = "Alarmbereich"
        description = "Zu viele Tiere haben XY, sofortiger Handlungsbedarf ist nötig!"
        lowerbound = 0
        upperbound = 30
    }
    Warningrange {
        tag = "Frühwarnbereich"
        description = "Es läuft nicht optimal. Handlungsbedarf ist vorhanden"	
    }
} 
```

**Stanza-Keyword:**  `Ranges`

**Named-Stanza:** no

**Required:** no

This is not required. Some Formulas will not provide any Ranges. 

This Stanza, as well as the stanzas within, define the ranges used to interpret the indicator records. There are three ranges with different interpretation. 

Targetrange:

This range is the optimum. If a value lies within, the animal well being is fine

Alertrange: 

This range is the worst case. If a value lies within, the animal well being is terrible. 

Warningrange:

This range is implicitly between Alert- and Targetrange. If a value lies within, then it is still okay, but something should be done.

Since it can happen, that the overall value range goes for example from 0 to 100 percent, but the range of intereset is just 20 to 40 percent. In Such a case the user don't want to see the overall range, but just the 20 to 40 percent. To signal this case, the visual-lowerbound can define 20 and the visual-uppercase can define 40.

---

#### Elements

---

#### unit

**Required:** yes

**Value Type:** `String`

Unit to display alongside to the evaluation. Typically this will be `%`, `m²`or `kg`.

---

#### visual-lowerbound

**Required:** yes

**Value Type:** `Numeric`

This describes the lower bound to display in the evaluation diagram. 

---

#### visual-upperbound

**Required:** yes

**Value Type:** `String`

This describes the upper bound to display in the evaluation diagramm.

---

#### branches

**Required:** yes

**Value Type:** `List[ref Branch]`

List of Branches which should make use of this `Range`. Within a `Formula` each Branch should only make use of one `Range` (no doubles allowed!)

---

## Targetrange

```json
Targetrange {
    tag = "Zielbereich" 
    description = "Kaum ein Tier hat xy."
    lowerbound = 60 //Dezimalzahlen wären hier genauso möglich
    upperbound = 100 
}
```

**Stanza-Keyword:**  `Targetrange`

**Named-Stanza:** no

**Required:** yes

see documentation of `Ranges`

---

### Elements

---

#### tag

**Required:** yes

**Value Type:** `String`

Name of the range.

---

#### description

**Required:** yes

**Value Type:** `String`

This describes the range and how to interpret a value within this range.

---

#### upperbound

**Required:** yes

**Value Type:** `Numeric`

This describes the upper bound of this range.

---

#### lowerbound

**Required:** yes

**Value Type:** `Numeric`

This describes the lower bound of this range.

---

## Alertrange

```json
Alertrange {
    tag = "Alarmbereich"
    description = "Zu viele Tiere haben XY, sofortiger Handlungsbedarf ist nötig!"
    lowerbound = 0
    upperbound = 30
}
```

**Stanza-Keyword:**  `Alertrange`

**Named-Stanza:** no

**Required:** yes

see documentation of `Ranges`

Properties of this stanza are just like `Targetrange`

---

## Warningrange

```json
Warningrange {
    tag = "Frühwarnbereich"
    description = "Es läuft nicht optimal. Handlungsbedarf ist vorhanden"	
}
```

**Stanza-Keyword:**  `Warningrange`

**Named-Stanza:** no

**Required:** yes

see documentation of `Ranges`

Properties of this stanza are just like `Targetrange` but it has no explicit bounds.

