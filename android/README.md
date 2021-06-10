# EiKoTiger-Android

Information about the architecture can be found in `ARCHITECTURE.md`.

## Current State

**This app is a proof-of-concept for the possibility to realize an app which implements indicators of animal well being
of multiple animal categories. Because this is only a proof-of-concept, it only provides basics and is no fully developed app.**


## Translating ICDL to JSON
The app needs the ICDL translated into JSON. Therefore, a small python script is provided. It can be found at `INSERT PATH TO SCRIPT`.
The current state of indicators can be found at `INSERT PATH TO ICDL FILES`.

## Usage 101
1. **Install the app**
2. **When starting the first time, the app will boot into a splashscreen**
    1. Click 'Download Starten'
    2. Wait until it has finished
    3. Click the back button
3. **Now open the navigation drawer and choose 'Tierhaltung anlegen'**
    1. At the beginning, this view is empty
    2. Click the bottom right 'Add'-button to navigate to the next screen
4. **Create an Animal-Category instance**
    1. Here you see a list of all available 'Tierkategorien' 
    2. Select a 'Tierkategorie' you want to instantiate 
    3. Once selected, you will be transfered to the details screen
    4. Here you can change the name of the newly created instance
    5. Now you can click the back-button twice, to navigate to the 'Tierhaltung anlegen' screen (3)
5. **Overview of instances**
    1. Now you the newly created instance is shown
    2. By selecting the instance, one will be navigated to the detail screen
    3. Below the info text the animal branches are provided.
    4. For each branch at least one (!) animal group must be created
6. **Creating an animal group**
    1. Click 'Buchten anlegen' at the respective branch you want to add animal groups
    2. On the following screen click the 'Add'-button on the bottom right
    3. Insert a name and a amount of animal within this group
    4. Click back to navigate back to the group overview
    5. Optionally add more groups, starting at b)
    6. Click back to navigate back to the instance detail view
    7. **go back to 5d)**
7. **Now you are set up**
8. **Recording indicators**
    1. Choose 'Erfassung' from the navigation drawer
    2. Choose a 'Produktionsverfahren' as well as a 'Produktionsabschnitt'
    3. Click 'Weiter'
    4. Next one can choose between different recording processes. Implemented are 'Tierweise Erfassung' and 'Indikatorweise Erfassung'
9. **'Indikatorweise Erfassung'**  
    Once an observation has been inserted, the app will stay on the current indicator until another one is chosen explicitly
    - 'Übersicht':  
       Provides an overview of all available indicators. By choosing an indicator, one will be navigated to the input-view ('Eingabe')
        Via 'Tiergruppe wechseln' it is possible to switch between animal groups.
    - 'Indikator':  
        Provides the description and further information about the currently chosen indicator.
    - 'Eingabe':  
        Here, the user can insert any observation for the currently selected indicator.
10. **'Tierweise Erfassung'**  
    Similar to the 'Indikatorweise Erfassung', but once an observation has been inserted, the app will move forwards to
    the next indicator within the list. Still, the user can jump between indicators explictly.

