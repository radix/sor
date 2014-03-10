# Secret of Rogues
Secret of Rogues is an action RPG with auto-generated content meant to be highly replayable.

I'm pretty much assuming that this game is set in the world of Mana, and so I will refer explicitly to elements of that world throughout this document, but at some point I will completely retheme the game to something else, in which case of course I will need to rename "Gaia's Navel", "Watts", etc.

#Multicharacter
Secret of Rogues will be a multi-character game, where you have a team of up to (2/3/4?) characters. These characters are playable by multiple human players at once, and if not controlled by a player, characters will revert to AI behavior.

There will be specific characters, with unique graphics. TBD is whether they will have different properties in combat, or fit into a "class" system. They will be namable by the players.

Character AI will be configurable by the player, on a scale of "aggressive" to "defensive".


# Combat
## Weapons
There are two styles of weapons: slow (charge) and fast (combo).

Slow weapons have a longish cool down between their use (1-1.5 second) and can be charged up for a stronger attack. Examples of **slow** weapons are **axe, longsword, whip, gun, longbow**.

Fast weapons can be "spammed" with no artificial cool down. For every hit landed, a counter is incremented, and when the counter reaches certain numbers, combo finishers are made available.  Examples of **fast** weapons are **fists, daggers**.


## Magic
Both SoM and SD3 had magic systems that required pausing gameplay every time the player wants to cast a spell. This is unfortunate both for single-player flow (though they optimized the amount of time spent in menus) and because it would interrupt other players' actions.

Legend of Mana had a system that allowed you to configure up to four spells to be accessible in real-time during combat. Holding down the trigger for one of the spells would charge up the spell, so that the longer you held it, the more powerful it would be or the larger range it would have.

Long, pause-the-game mechanics should be kept to a minimum, but are acceptable for rare occasions, like a very long charge-up or high-value combo finisher.

### Elements
Each spell will belong to a school of magic. A character's skill with a particular element can be improved by casting spells from that element.

TBD: will spell availability be determined by player class?

### Casting
Each character can have up to five spells castable in combat. Changing which spells are cartable will require leaving combat (by losing the attention of all enemies).

## Damage Mitigation
### Guard Impact
Consider a Soul Calibur-style "guard impact" mechanic, where certain enemy attacks may be accompanied by an audiovisual "tell" that the player has an opportunity to quickly react to, thus mitigating the damage from the attack.

### Dodging
It should be possible to dash in any direction. I doubt that dashing should be a pure immunity.

### Attributes
Other than the previously mentioned things, damage mitigation will be based on numbers such as armor value, agility, or "dodge".

# World
Auto-generated.

## Terrain types
Terrain types are made up of a graphical tileset and a terrain generation algorithm. For example,

- **forests** will have lots of trees with pathways through them
- **deserts** will be wide open

In addition, there will be terrain "features" which may cross boundaries between different terrain types, such as:

- **rivers**

## Special Locations
There will be certain kinds of locations that are stamped out in random places. They may contain random elements, but still have a distinct style or geographical set, or they may be completely standardized with no random elements.

### General location types

**Walrus Travel Centers** will be sprinkled throughout the world. When you find one and decide to travel, the camera zooms out to a map level, and you can click anywhere within a certain radius. You have a N% chance of landing where you clicked, and a Y% chance of landing within M meters. If you click on the location of another walrus travel center, you have a >N% chance of landing there. If you click on another walrus travel center (represented by a noticeable icon) when you're targeting your travel you will be able to select a successive travel destination which you will reach by bouncing off of the walrus.

**Inns** will likewise be sprinkled throughout the world, but less common. You will be able to sleep here to restore your party.

### Unique Locations

**Gaia's Navel**: A huge circular valley in the middle of a forested flatland, with a river running down to a lake at the base. At the lowest point of the valley you will be able to enter a cave system and eventually find Watts, the blacksmith, who can upgrade your equipment for you if you bring him certain rare artifacts.

##Map
The player will be able to call up a map of everything on the surface that has been explored at will.
