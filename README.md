# EiKoTiGer - Eigenkontrolle Tiergerechtheit

## Hintergrund 

### Der Grundgedanke
Woran können Tierhalterinnen und Tierhalter zuverlässig erkennen, wie es ihren Tieren geht? Um ihnen hierfür eine Hilfestellung zu geben, hat das KTBL in Fachgesprächen Tierschutzindikatoren für Rinder, Schweine und Geflügel ausgewählt und veröffentlicht

Weil sich das Tierwohl am besten direkt an den Tieren erkennen lässt, wurden überwiegend tierbezogene Indikatoren berücksichtigt.

Durch eine regelmäßige und systematische Erhebung dieser Indikatoren können Tierhalterinnen und Tierhalter relevante Tierschutzprobleme erkennen und frühzeitig Gegenmaßnahmen ergreifen. Langfristig können sie, sofern erforderlich, das Tierwohl so eigenverantwortlich schrittweise verbessern. Zudem kommen Tierhalterinnen und Tierhalter dadurch auch ihrer Pflicht zur „betrieblichen Eigenkontrolle“ gemäß Tierschutzgesetz § 11 (8) nach, die seit 2014 vorgeschrieben ist: Nutztierhalter sollen mit geeigneten tierbezogenen Merkmalen die Tiergerechtheit im Stall erheben und bewerten.

Projektseite: [ktbl/themen/eikotiger](https://www.ktbl.de/themen/eikotiger)

### Indicator Description Language
Die `Indicator Description Language` kurz `ICDL` ist eine auf `HCL` basierende Konfiguration-Definition. Diese wird genutzt um Produktionsrichtungen und dazugehörige Tierwohl-Indikatoren zu definieren. Die `ICDL` is unabhängig von Programmiersprachen und Frameworks.


## Inhalt dieses Repositories

### `android/`
Hier ist der Code einer Proof-of-Concept Android App zu finden welche die ICDL benutzt und erste Interaktionen auf Basis der definierten Produktionsrichtungen und Indikatoren ermöglicht. Weitere Informationen zu den technischen Details sind im `android/` Ordner zu finden.

### `icdl/`

Im Verzeichnis `icdl/data` sind die aktuellen Stände der `ICDL` für verschiedene Tierproduktionsrichtungen zu finden. Zu beachten ist hierbei, dass diese unvollständig sind und nicht alle `ICDL` Definitionen für die App übersetzt werden können.

Den aktuellen Stand der Dokumentation des `ICDL` Formats ist unter `icdl/doc` zu finden.

Die Android App nutzt das `ICDL` Format nicht direkt, sondern nutzt JSON als Zwischenformat. Das Verzeichnis `icdl/util` beinhaltet ein Python-Script zur Umwandlung der `ICDL` in das von der App genutzten JSON Format.


## Ansprechpartner
[Kontakt und Impressum](https://www.ktbl.de/service/impressum)

## Lizenz

Siehe [LICENSE](LICENSE)

