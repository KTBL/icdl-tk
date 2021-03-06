## Architecture

The App currently mixes Kotlin and Java. It is planned to completely translate it to kotlin.

This app makes use of two Activities: Settings and Main. Additionally a third activity "Info 
Activity" exists but is currently not used. We make use of Dependency Injection using Dagger and 
AndroidX components.

### UI

Views are created using Fragments, data is bound using ViewModels. ViewModels use _Data Access 
Interfaces_ (DAI) 
to define which data they need to query/add/update. These Interfaces are then implemented by the
database layer using Room with Repositories. Creation and implementation of the _DAI_ is work in progress.

The _DAI_ are located at: `de.ktbl.eikotiger.view.datainterface`

```
-----------------------------
| UI                        |
| ------------------------- |
| |        XML            | |
| ------------------------- |    -------------------------    ------------------------- 
|           ^ v             | <> |       ViewModel       | <> | Data Access Interface | 
| ------------------------- |    -------------------------    ------------------------- 
| |       Fragment        | |                                           ^ v
| ------------------------- |                                 -------------------------   
-----------------------------                                 |   Room Repositories   |
                                                              ------------------------- 
```

Most views are used in more than one position and functionality. Therefore View/Fragments are defined
in combination with base-Viewmodels which are implemented per use case. While this enables to reuse 
Views and Fragments, this enforces, that these Views/Fragments are all used within the same Activity.
Using Views/Fragments in multiple Activities leads to problems when using Android's _Navigation Component_, 
since it generates code. If multiple Activities use the same View _Navigation Component_ cannot map
the View to the respective Activity.

Viewmodels are also used to decide where to navigate next. Since the ViewModel should not hold 
references to Context-specific objects a `LiveEventHandler` has been created which is used together
with `INavigationRequester` and `NavigationCommand`. This way the Viewmodel is able to pass navigation
queries to a Fragment or Activity, which then processes and executes the requested navigation action.

see:  
`de.ktbl.android.sharedlibrary.livedata.LiveEventhandler`  
`de.ktbl.android.sharedlibrary.navigation.*`

Since many views are reused, our packages are structure by technical function ("adapter", "viewmodel", 
etc.) instead of feature.

#### Activities:

##### Settings Activity
This Activity currently holds a setting fragment to read/write user specific data like name, address and phone number.

##### Main Activity
This Activity holds the main parts of the app. It consists of three navigation Branches:
 1. Tierkategorien --- View, add, modify, delete any create Instances and their data.
 2. Erfassung --- Start a recording session to log Indicators of a chosen Instance.
 3. Auswertung --- Evaluation of previously logged Indicators.  

### Database / Data-Layer
The ICDL defines data and information which is used by the app to provide its functionality. Building
upon the ICDL the user is able to create Instances of the ICDL.

The Database layer consists of three parts:
- ICDL (JSON) --- holding the ICDL data in an JSON format
- ICDL (Room) --- parsed ICDL JSON data inserted into a SQLite Database using Room.
- Userdefined data --- data generated by the user, mostly associated with ICDL (Room) 

#### ICDL (JSON)

The ICDL is an own describtion language. Since this language is hardly handled within the app, the 
original description is transformed into an intermediate JSON format. These JSON files are provided
in the app as Assets. Each JSON files contains an AnimalCategory and Indicators associated with this
AnimalCategory. 

The JSON files are parsed at the first start and translated to In-App models. Models used for parsing
are different models. Parsing models and code can be found at: `de.ktbl.eikotiger.data.json.*`
The JSON-files can be found in the Asset folder `icdl-new` (important: `icdl` still exists but holds 
files using an older JSON format).

As JSON parser Moshi is used.

#### ICDL-Room ("App-Stammdaten" / "Metadaten")

ICDL in-App data is generated based on the previously described JSON data. The model classes used 
in-App are defined in `de.ktbl.eikotiger.data.icdl`. Currently these classes are restructured. The
Entities and Relations are oriented on the original ICDL (as it can be seen in the JSON files and 
the according JSON model classes).

#### User-defined Data

Data models for user defined data are found in: `de.ktbl.eikotiger.data.inputmodel`.

The central class here is `Instance`. An Instance is the user-side instantiation of an AnimalCategory.
All other operations are always based on data/information bound to an instance. E.g. Branch, Indicator-logs.
These classes are subject of rework/refactoring as well. The basic will be kept. 

## Some Words About Words
Vocabulary used within this project has changed over time. Therefore names in this repository are _a bit_ 
inconsistent. Here are some synonyms (the last one is always the word to be used currently:

- Production direction : Direction ("Dir") : Animal Category
- Subsection : Production Section : Section : Branch


## External Libraries used:

### Android-SharedLibrary

As library `Android-SharedLibrary` is used. This lib is currently only used within EiKoTiGer and 
is included as Submodule. The repository of the library can be found here: https://github.com/KTBL/Android-SharedLibrary

### Timber
https://github.com/JakeWharton/timber

Logging-Library unter der Apache-2.0. Wird genutzt, weil Logcat geringere M??glichkeiten im Produktiv-Betrieb bietet.
Besonders wichtig ist hier das Logging-Verhalten im Produktiv-Betrieb.

### Sticky Timeline
https://github.com/sangcomz/StickyTimeLine

Used as Recyclerview with grouping to display instance / icdl data

### Moshi
https://github.com/square/moshi

Used for parsing the JSON-ICDL representation.

## External Icons:
### Font-Awesome
**License:** https://fontawesome.com/license/free

https://fontawesome.com/icons/calculator?style=solid