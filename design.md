# Secret of Rogues

Secret of Rogues is an action RPG with auto-generated content meant to be highly replayable.

I'm pretty much assuming that this game is set in the world of Mana, and so I will refer explicitly to elements of that world throughout this document, but I may decide to completely retheme the game to something else, in which case of course I will need to rename "Gaia's Navel", "Watts", "Cannon Travel Center" etc.

# Combat

## Weapons

Some weapons are SoM-style, some are SD3-style. General idea is that light, quick weapons are SD3-style, since that allows you to spam.

SoM weapons: Axe, longsword, whip, gun, longbow
SD3 weapons: fists, nunchaku, shuriken

I think for SoM-style I want to get rid of the ability to attack before the cooldown is up, because it's pretty much always the wrong thing to do, and becomes an additional player education problem.

I like charging up.

## Magic
Both SoM and SD3 had magic systems that required pausing gameplay every time the player wants to cast a spell. This is unfortunate both for single-player flow (though they optimized the amount of time spent in menus) and because it would interrupt other players' actions.

Legend of Mana had a system that allowed you to configure up to four spells to be accessible in real-time during combat. Holding down the trigger for one of the spells would charge up the spell, so that the longer you held it, the more powerful it would be or the larger range it would have.

Long, pause-the-game mechanics should be kept to a minimum, but are acceptable for rare occasions, like a very long charge-up or high-value combo finisher.

## Damage Mitigation

### Guard Impact
There will not be a general blocking mechanic, but I am considering adding a Soul Calibur-style "guard impact" mechanic, where certain enemy attacks may be accompanied by an audiovisual "tell" that the player has an opportunity to quickly react to, thus mitigating the damage from the attack.

### Dodging
It should be possible to dash in any direction. I doubt that dashing should be a pure immunity.

### Attributes
Other than the previously mentioned things, damage mitigation will be based on numbers such as armor value.

# World

auto-generated, with "terrain types" and "location types".

## Terrain types

forest: lots of pathways
desert: wide open
rivers: with bridges

## Location types

There will be certain kinds of locations that are stamped out in random places. Some of them will be unique, with only one showing up in any particular instance of the game.

### General location types

Cannon Travel Centers: These need to be sprinkled throughout the world. When you decide to travel, the camera zooms out to a map level, and you can click anywhere within a certain radius. You have a N% chance of landing where you clicked, and a Y% chance of landing within M meters. If you click on the location of another cannon travel center, you have a >N% chance of landing there. Next to every cannon is a trampoline, and if you click the trampoline icon when you're targeting your travel you will be able to select a successive travel destination which you will reach by bouncing off of the trampoline.

### Unique Locations

Gaia's Navel
