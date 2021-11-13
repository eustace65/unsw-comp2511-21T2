Client requirements does not specify how battles will take place other than the requirement that there will be no human player interaction involved we will assume the following:

1. The player will always attack first once in the battle radius of the enemy.
2. The enemy will attack in order of strength: vampire first followed by zombie then slug
3. Any allies will attack after the player and the enemies have attacked.
4. Process repeats until either the player loses all health or the enemies are defeated.
5. Players will not be able to move to the next tile until all enemies in the current battle are defeated.

Potions can be used on any tile as long as the player is not engaged in battle with an enemy.

Client requirement does not specify how the enemies will move along the path, only referring to each enemy as having a different movement pattern. As such we will assume the following (assumptions for enemy movement will change as we plan out our design):

1. Slugs will move back and forth between their spawn tile and an adjacent tile.
2. Zombies will move 1 tile anticlockwise every turn.
3. Vampires will move 1 tile anticlockwise however they will reverse if their next turn will place them into a campfire battle radius.

Players will have 16 slots for equipment
Unequipped inventory is only for equippable items
Players will have 7 slots for cards

The shop at the Hero's Castle will offer six items every cycle at random. Can range from potions, weapons, armour, helmet, shield.

Character:
    -HP: 500
    -Damage: 100
    -Range: 5
    -Speed: 5
Item:
    -Sword
        -Damage:200
        -Price: 1000
    -Stake
        -Damage:150
        -Price:1500
    -Staff
        -Damage: 100
        -Price: 2000
    -Amour
        -Defense : (½) damage of enemy
        Price: 1000
    -Shield
        -Defense 200
        -Price: 2000
    -Helmet
        -Defense 150
        -Price:1500
    -Gold
        -Gold 200
    -HealthPotion
        -Price: 2000
        -Hp: 200

Enemy:
    Slug
        Hp :200
        Damage :20
        Gold: 100
        Support Range: 2
        Battle Range: 2
        Speed: 2
        Exp 100
    Zombie
        Hp :200
        Damage: 40
        Gold:  300
        Support Range: 4
        Battle Range: 2
        Speed: 1
        Exp 200
    Vampire
        Hp: 800
        Damage: 60
        Gold: 500
        Support Range: 5
        Battle Range: 2
        Speed: 3
        Exp 300
