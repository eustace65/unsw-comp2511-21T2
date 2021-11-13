package unsw.loopmania;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.text.html.HTMLDocument.Iterator;

import java.lang.Math;
import org.javatuples.Pair;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.effect.BlurType;

import unsw.loopmania.ModeType;
/**
 * A backend world.
 *
 * A world can contain many entities, each occupy a square. More than one entity
 * can occupy the same square.
 */
public class LoopManiaWorld {
    // TODO = add additional backend functionality

    public static final int unequippedInventoryWidth = 4;
    public static final int unequippedInventoryHeight = 4;

    public static final int equippedInventoryWidth = 4;
    public static final int equippedInventoryHeight = 1;

    /**
     * width of the world in GridPane cells
     */
    private int width;
    private int shopTimes;
    /**
     * height of the world in GridPane cells
     */
    private int height;

    private int pathCycle = 0;

    private DoggieCoinPrice doggieCoinPrice;

    /**
     * generic entitites - i.e. those which don't have dedicated fields
     */
    private List<Entity> nonSpecifiedEntities;

    private Character character;
    private HeroCastle startCastle;

    // TODO = add more lists for other entities, for equipped inventory items,
    // etc...

    // TODO = expand the range of enemies
    private List<EnemyProperty> enemies;
    List<EnemyProperty> transferZombies;
    // TODO = expand the range of cards
    private List<Card> cardEntities;

    // TODO = expand the range of items
    // private Inventory unequippedInventoryItems;
    private List<ItemProperty> unequippedInventoryItems;
    private List<ItemProperty> unPickedItem;
    private Equipment equippedItems;

    // TODO = expand the range of buildings
    private List<VampireCastleBuilding> buildingEntities;
    private List<BuildingProperty> campfires;
    private List<BuildingProperty> buildings;

    private List<Ally> allies;

    private int potionsOwned;

    private SimpleIntegerProperty alliesOwned;
    private IntegerProperty doggieCoinOwned;

    private IntegerProperty experience;
    private DoubleProperty expDouble;
    private IntegerProperty ringOwned;

    private boolean shouldSpawnDoggie;
    private boolean shouldSpawnMuske;
    public BooleanProperty hasSpawnMuske;
    public BooleanProperty hasKilledMuske;
    private IntegerProperty gold;
    private GoalLogic totaGoal;
    private DoggieCoinMarket doggieCoinMarket;
    private ModeType mode;
    // private List<ItemProperty> rareItems
    /**
     * list of x,y coordinate pairs in the order by which moving entities traverse
     * them
     */
    private List<Pair<Integer, Integer>> orderedPath;



    /**
     * create the world (constructor)
     *
     * @param width       width of world in number of cells
     * @param height      height of world in number of cells
     * @param orderedPath ordered list of x, y coordinate pairs representing
     *                    position of path cells in world
     */
    public LoopManiaWorld(int width, int height, List<Pair<Integer, Integer>> orderedPath) {
        this.width = width;
        this.height = height;
        nonSpecifiedEntities = new ArrayList<>();
        character = null;
        enemies = new ArrayList<>();
        cardEntities = new ArrayList<>();
        unequippedInventoryItems = new ArrayList<>();
        equippedItems = new Equipment(unequippedInventoryItems);
        this.orderedPath = orderedPath;
        buildingEntities = new ArrayList<>();
        gold = new SimpleIntegerProperty(0);
        potionsOwned = 0;
        transferZombies = new ArrayList<EnemyProperty>();

        experience = new SimpleIntegerProperty(0);
        expDouble = new SimpleDoubleProperty((double) 0 / 123456.00);
        ringOwned = new SimpleIntegerProperty(0);
        alliesOwned = new SimpleIntegerProperty(0);
        buildings = new ArrayList<>();
        allies = new ArrayList<>();
        campfires = new ArrayList<>();
        unPickedItem = new ArrayList<>();
        startCastle = new HeroCastle(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        shouldSpawnDoggie = false;
        shouldSpawnMuske = false;
        hasSpawnMuske = new SimpleBooleanProperty(false);
        hasKilledMuske = new SimpleBooleanProperty(false);
        doggieCoinOwned = new SimpleIntegerProperty(0);

        doggieCoinMarket = new DoggieCoinMarket(this);
        shopTimes = 0;
        doggieCoinPrice = new DoggieCoinPrice();
        doggieCoinMarket.registerObserver(doggieCoinPrice);
    }



    public GoalLogic getGoal() {
        return totaGoal;
    }

    public void setGoal(GoalLogic goal) {
        this.totaGoal = goal;
    }

    public List<Ally> getAllies() {
        return this.allies;
    }

    public List<EnemyProperty> getEnemy() {
        return this.enemies;
    }

    public List<BuildingProperty> getCampfire() {
        return this.campfires;
    }

    public List<Pair<Integer, Integer>> getOrderedPath() {
        return this.orderedPath;
    }

    public List<ItemProperty> getUnpickedItems() {
        return this.unPickedItem;
    }

    public void addAlly(Ally ally) {
        allies.add(ally);
        alliesOwned.set(alliesOwned.get() + 1);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    /**
     * set the character. This is necessary because it is loaded as a special entity
     * out of the file
     *
     * @param character the character
     */
    public void setCharacter(Character character) {
        this.character = character;
    }

    /**
     * add a generic entity (without it's own dedicated method for adding to the
     * world)
     *
     * @param entity
     */
    public void addEntity(Entity entity) {
        // for adding non-specific entities (ones without another dedicated list)
        // TODO = if more specialised types being added from main menu, add more methods
        // like this with specific input types...
        nonSpecifiedEntities.add(entity);
    }
    public IntegerProperty getDoggieCoin() {
        return this.doggieCoinOwned;
    }
    public IntegerProperty getHpValue() {
        return character.getHp();
    }

    public int getHealthPotion() {
        return potionsOwned;
    }

    public IntegerProperty getRing() {
        return ringOwned;
    }

    public int getCycle() {
        return this.pathCycle / orderedPath.size();
    }

    public void setCycle(int n) {
        this.pathCycle = n * orderedPath.size();
    }

    public List<ItemProperty> getUnequippedInventoryItems() {
        return this.unequippedInventoryItems;
    }

    /**
     * spawns enemies if the conditions warrant it, adds to world
     *
     * @return list of the enemies to be displayed on screen
     */
    public List<EnemyProperty> possiblySpawnEnemies() {
        // TODO = expand this very basic version
        Pair<Integer, Integer> pos;
        List<EnemyProperty> spawningEnemies = new ArrayList<>();
        if (shouldSpawnDoggie) {
            pos = possiblyGetBasicEnemySpawnPosition();
            if (pos != null) {
                int indexInPath = orderedPath.indexOf(pos);

                EnemyProperty enemy = new Doggie(new PathPosition(indexInPath, orderedPath));
                enemies.add(enemy);
                spawningEnemies.add(enemy);
            }
            shouldSpawnDoggie = false;
        }

        if (shouldSpawnMuske) {
            pos = possiblyGetBasicEnemySpawnPosition();
            if (pos != null) {
                int indexInPath = orderedPath.indexOf(pos);

                EnemyProperty enemy = new ElanMuske(new PathPosition(indexInPath, orderedPath));
                enemies.add(enemy);
                spawningEnemies.add(enemy);
                shouldSpawnMuske = false;
                hasSpawnMuske.set(true);
            }
        }


        pos = possiblyGetBasicEnemySpawnPosition();
        if (pos != null) {
            int indexInPath = orderedPath.indexOf(pos);

            EnemyProperty enemy = new Slug(new PathPosition(indexInPath, orderedPath));
            enemies.add(enemy);
            spawningEnemies.add(enemy);
        }

        // Spawn vampires from vampire castle
        for (BuildingProperty b : this.buildings) {
            b.spawnEnemy(this, spawningEnemies);
        }

        for (EnemyProperty e : transferZombies) {
            spawningEnemies.add(e);
        }


        transferZombies.clear();
        return spawningEnemies;
    }

    public int getPotions() {
        return this.potionsOwned;
    }

    /**
     * spawns items if the conditions warrant it, adds to world
     *
     * @return list of the gold to be displayed on screen
     */
    public List<ItemProperty> possiblySpawnItems() {
        List<ItemProperty> spawningItems = new ArrayList<>();
        if (unPickedItem.size() < 2) {
            boolean goldExist = false;
            boolean healthPotionExist = false;
            for (ItemProperty item : unPickedItem) {
                if (item.getType() == ItemType.OTHER) {
                    goldExist = true;
                }
                if (item.getType() == ItemType.HEALTHPOTION) {
                    healthPotionExist = true;
                }
            }
            Random rand = new Random();
            if (goldExist == false && this.pathCycle % (1 * orderedPath.size()) == 0) {
                int indexInOrderedPath = rand.nextInt(orderedPath.size() - 1);
                Pair<Integer, Integer> newPos = orderedPath.get(indexInOrderedPath);

                ItemProperty gold = new Gold(new SimpleIntegerProperty(newPos.getValue0()),
                        new SimpleIntegerProperty(newPos.getValue1()));
                unPickedItem.add(gold);
                spawningItems.add(gold);
            }

            if (healthPotionExist == false && this.pathCycle % (4 * orderedPath.size()) == 0) {
                int indexInOrderedPath = rand.nextInt(orderedPath.size() - 1);
                Pair<Integer, Integer> newPos = orderedPath.get(indexInOrderedPath);
                ItemProperty healthPotion = new HealthPotion(new SimpleIntegerProperty(newPos.getValue0()),
                        new SimpleIntegerProperty(newPos.getValue1()));
                unPickedItem.add(healthPotion);
                spawningItems.add(healthPotion);
            }
        }
        return spawningItems;
    }


    /**
     * kill an enemy
     *
     * @param enemy enemy to be killed
     */
    public void killEnemy(EnemyProperty enemy) {
        enemy.destroy();
        enemies.remove(enemy);
    }

    public void killAlly(Ally ally) {
        ally.destroy();
        allies.remove(ally);
        alliesOwned.set(alliesOwned.get() - 1);
        if (alliesOwned.get() < 0) alliesOwned.set(0);
    }

    /**
     * run the expected battles in the world, based on current world state
     *
     * @return list of enemies which have been killed
     */
    public List<EnemyProperty> runBattles() {
        // without any damage!
        List<EnemyProperty> defeatedEnemies = new ArrayList<EnemyProperty>();
        List<Ally> defeatedAllies = new ArrayList<Ally>();

        boolean inBattle = false;
        character.setDamageBack();
        if (character.getSuperPowerDuration() > 0) {
            character.setDamage(3 * character.getDamage());
        }
        for (EnemyProperty e : enemies) {
            // Pythagoras: a^2+b^2 < radius^2 to see if within radius
            // influence radii and battle radii
            // boolean hasAttacked = false;
            e.setAllPropertyBack();
            if (e.attack(this, defeatedAllies, transferZombies, inBattle, getEquipItems())) {
                inBattle = true;
                e.setInBattle(true);
                character.setInBattle(true);
            }

        }
        for (EnemyProperty enemy : transferZombies) {
            enemies.add(enemy);

        }

        for (Ally ally : allies) {
            for (EnemyProperty e : enemies) {
                if (e.getHP() <= 0) {
                    continue;
                }
                if (e.getInBattle()){
                    //inBattle = true;
                    ally.attack(e);
                    if (e.getHP() <= 0) {
                        defeatedEnemies.add(e);
                    }
                    break;
                }
            }
        }

        for (EnemyProperty e : enemies) {
            if (e.getHP() <= 0) {
                continue;
            }
            // add character attacked
            if (e.getInBattle()) {
                //inBattle = true;
                //e.setInBattle(true);
                character.attack(e, equippedItems.getEquipment(), getMode());
                if (e.getHP() <= 0) {
                    defeatedEnemies.add(e);
                }
                break;
            }
        }
        for (EnemyProperty e : defeatedEnemies) {
            // IMPORTANT = we kill enemies here, because killEnemy removes the enemy from
            // the enemies list
            // if we killEnemy in prior loop, we get
            // java.util.ConcurrentModificationException
            // due to mutating list we're iterating over
            if (e.getType().equals("ElanMuske")) {
                hasKilledMuske.set(true);
            }
            addExperience(e.getExp());
            killEnemy(e);
        }

        for (Ally ally : defeatedAllies) {
            killAlly(ally);
        }
        if (!inBattle) {

            character.setInBattle(false);
        }

        return defeatedEnemies;
    }

    public ItemType generateItem() {
        int totalRewards = 7;
        Random rand = new Random();
        int result = rand.nextInt(1000) % totalRewards;
        switch (result) {
            case 1:
                return ItemType.ARMOUR;
            case 2:
                return ItemType.HEALTHPOTION;

            case 3:
                return ItemType.HELMET;
            case 4:
                return ItemType.SHIELD;
            case 5:
                return ItemType.STAFF;
            case 6:
                return ItemType.STAKE;
            case 0:
                return ItemType.SWORD;
            default:
                return null;
        }
    }



    /**
     * spawn an item in the world and return the item entity
     *
     * @param type of item to be added
     * @return a item to be spawned in the controller as a JavaFX node
     */

    public ItemProperty addUnequippedItem(ItemType type) {
        
        Random rand = new Random();
        int result = rand.nextInt(30);
        if (result == 0) {
            ringOwned.set(ringOwned.get() + 1);
        }
        Pair<Integer, Integer> firstAvailableSlot = getFirstAvailableSlotForItem();
        if (firstAvailableSlot == null) {
            // eject the oldest unequipped item and replace it... oldest item is that at
            // beginning of items
            removeItemByPositionInUnequippedInventoryItems(0);
            firstAvailableSlot = getFirstAvailableSlotForItem();
            // gives random amount of cash/experience reward for discarding oldest item
            result = rand.nextInt(10) % 2;
            switch (result) {
                case 0:
                    addExperience(rand.nextInt(10));
                    break;
                case 1:
                    addGold(rand.nextInt(10));
                    break;
                default:
                    break;
            }
        }
        if (firstAvailableSlot == null)
            return null;
        SimpleIntegerProperty x = new SimpleIntegerProperty(firstAvailableSlot.getValue0());
        SimpleIntegerProperty y = new SimpleIntegerProperty(firstAvailableSlot.getValue1());
        // insert new item as it is now we know we have a slot available
        ItemProperty item;
        switch (type) {
            case SWORD:
                item = new Sword(x, y);
                break;

            case STAKE:
                item = new Stake(x, y);
                break;
            case STAFF:
                item = new Staff(x, y);
                break;
            case HELMET:
                item = new Helmet(x, y);
                break;
            case ARMOUR:
                item = new Armour(x, y);
                break;
            case SHIELD:
                item = new Shield(x, y);
                break;
            case HEALTHPOTION:
                // addPotion(1);
                potionsOwned += 1;
                item = null;
                break;
            case ANDURIL:
                item = new Anduril(x, y);
                break;
            case TREESTUMP:
                item = new TreeStump(x, y);
                break;
            default:

                item = null;

        }
        if (item == null) return null;
        unequippedInventoryItems.add(item);
        return item;
    }

    /**
     * removes the item from equippedItems list and adds it back to
     * unequippedInventory
     *
     * @param slot of item to be removed
     * @return
     */
    public boolean unEquipItem(int slot) {
        // TODO = spawn the item back into the inventory
        equippedItems.unEquip(slot);
        return equippedItems.unEquip(slot);
    }

    /**
     * moves an item from unequippedInventory to equippedItems. Deletes the item and
     * creates a new copy of it inside equippedItems
     *
     * @param nodeX of the item in unequippedInventory
     * @param nodeY of the item in unequippedInventory
     * @return item to be spawned inside the equippedItems gridpane
     */
    public ItemProperty equipItemByCoordinates(int nodeX, int nodeY) {
        ItemProperty item = getUnequippedInventoryItemEntityByCoordinates(nodeX, nodeY);
        equippedItems.equip(item);
        ItemProperty equippedItem = equippedItems.spawnEquippedItem(item.getType().getIndex(), item.getType());
        item.destroy();
        unequippedInventoryItems.remove(item);
        return equippedItem;
    }

    /**
     * remove an item by x,y coordinates
     *
     * @param x x coordinate from 0 to width-1
     * @param y y coordinate from 0 to height-1
     */
    public void removeUnequippedInventoryItemByCoordinates(int x, int y) {
        Item item = getUnequippedInventoryItemEntityByCoordinates(x, y);
        removeUnequippedInventoryItem(item);
    }

    /**
     * run moves which occur with every tick without needing to spawn anything
     * immediately
     */
    public void runTickMoves() {
        if (character.getSuperPowerDuration() > 0) {
            character.setSuperPowerDuration(character.getSuperPowerDuration() - 1);
        }
        doggieCoinMarket.notifyObservers();
        if (!character.getInBattle()) {
            character.moveDownPath();
            updatePathCycle();
        }
        if (pathCycle % orderedPath.size() == 0 && getCycle() >= 20 && getCycle() % 5 == 0) {
            shouldSpawnDoggie = true;
        }

        if (getCycle() >= 40 && experience.get() >= 10000 && !hasKilledMuske.get() && !hasSpawnMuske.get()) {
            shouldSpawnMuske = true;
        }
        moveBasicEnemies();
        charactersStepOnBuilding();
        enemyStepOnBuilding();
        // turn ally back to enemy
        boolean battleEnd = true;
        for (EnemyProperty enemy : enemies) {
            if (enemy.getInBattle()) {
                battleEnd = false;
            }
        }
        if (battleEnd) {
            // kill all tranced allies
            for (Ally ally : allies) {
                if (!ally.getOriginalType().isEmpty()) {
                    killAlly(ally);
                }
            }
        } else {
            // Tranced ally turns back to enemy
            for (Ally ally : allies) {
                PathPosition position = ally.getPathPosition();
                if (ally.getOriginalType() != null) {
                    if (ally.getRound() - 1 == 0) {
                        String type = ally.getOriginalType();
                        EnemyProperty enemy;
                        switch (type) {
                            case "Vampire":
                                enemy = new Vampire(position);
                                break;
                            case "Slug":
                                enemy = new Slug(position);
                                break;
                            default:
                                enemy = new Zombie(position);
                                break;
                        }
                        enemies.add(enemy);

                        killAlly(ally);
                    } else {
                        ally.setRound(ally.getRound() - 1);
                    }
                }
            }
        }

        // Pick up gold or health potion
        List<ItemProperty> toRemoveGold = new ArrayList<>();
        List<ItemProperty> toRemoveHealthPotion = new ArrayList<>();
        for (ItemProperty item : unPickedItem) {

            item.characterStepOn(this, toRemoveGold, toRemoveHealthPotion);
        }

        for (ItemProperty item : toRemoveGold) {
            unPickedItem.remove(item);
            item.destroy();
            gold.set(gold.get() + (Gold.value.get()));
        }
        toRemoveGold.clear();

        for (ItemProperty item : toRemoveHealthPotion) {
            unPickedItem.remove(item);
            item.destroy();
            addPotion(1);
            // character.setHp(500);
        }
        toRemoveHealthPotion.clear();
    }

    public void updatePathCycle() {
        this.pathCycle += 1;
        for (BuildingProperty b : this.buildings) {
            b.setPathCycle(b.getPathCycle() + 1);
        }
    }

    /**
     * remove an item from the unequipped inventory
     *
     * @param item item to be removed
     */
    public void removeUnequippedInventoryItem(Entity item) {
        item.destroy();
        unequippedInventoryItems.remove(item);
    }

    /**
     * return an unequipped inventory item by x and y coordinates assumes that no 2
     * unequipped inventory items share x and y coordinates
     *
     * @param x x index from 0 to width-1
     * @param y y index from 0 to height-1
     * @return unequipped inventory item at the input position
     */
    private ItemProperty getUnequippedInventoryItemEntityByCoordinates(int x, int y) {
        for (ItemProperty e : unequippedInventoryItems) {
            if ((e.getX() == x) && (e.getY() == y)) {
                return e;
            }
        }
        return null;
    }

    /**
     * remove item at a particular index in the unequipped inventory items list
     * (this is ordered based on age in the starter code)
     *
     * @param index index from 0 to length-1
     */
    public void removeItemByPositionInUnequippedInventoryItems(int index) {
        Entity item = unequippedInventoryItems.get(index);
        item.destroy();
        unequippedInventoryItems.remove(index);
    }

    /**
     * get the first pair of x,y coordinates which don't have any items in it in the
     * unequipped inventory
     *
     * @return x,y coordinate pair
     */
    private Pair<Integer, Integer> getFirstAvailableSlotForItem() {
        // first available slot for an item...
        // IMPORTANT - have to check by y then x, since trying to find first available
        // slot defined by looking row by row
        for (int y = 0; y < unequippedInventoryHeight; y++) {
            for (int x = 0; x < unequippedInventoryWidth; x++) {
                if (getUnequippedInventoryItemEntityByCoordinates(x, y) == null) {
                    return new Pair<Integer, Integer>(x, y);
                }
            }
        }
        return null;
    }

    /**
     * shift card coordinates down starting from x coordinate
     *
     * @param x x coordinate which can range from 0 to width-1
     */
    public void shiftCardsDownFromXCoordinate(int x) {
        for (Card c : cardEntities) {
            if (c.getX() >= x) {
                c.x().set(c.getX() - 1);
            }
        }
    }

    /**
     * move all enemies
     */
    public void moveBasicEnemies() {
        // TODO = expand to more types of enemy

        for (EnemyProperty e : enemies) {
            for (int i = 0; i < e.getSpeed(); i++) {
                BuildingProperty nearestCamp = this.getShortestCampfire(e);
                if (e.getType().equals("Vampire") && nearestCamp != null) {
                    getAwayFromCampfire(e);
                }

                if (character.getInBattle()
                        && e.getDistance(character.getX(), character.getY()) <= e.getSupportRadius()) {
                    supportMove(e);
                } else {
                    e.move();
                }
                enemyStepOnBuilding();
                // supportMove(e);
            }
        }
    }

    /**
     * get a randomly generated position which could be used to spawn an enemy
     *
     * @return null if random choice is that wont be spawning an enemy or it isn't
     *         possible, or random coordinate pair if should go ahead
     */
    public Pair<Integer, Integer> possiblyGetBasicEnemySpawnPosition() {
        // TODO = modify this

        // has a chance spawning a basic enemy on a tile the character isn't on or
        // immediately before or after (currently space required = 2)...
        Random rand = new Random();
        int choice = rand.nextInt(2); // TODO = change based on spec... currently low value for dev purposes...
        // TODO = change based on spec
        int slugNum = 0;
        for ( EnemyProperty enemy : enemies) {
            if (enemy.getType().equals("Slug")) {
                slugNum++;
            }
        }
        if (shouldSpawnDoggie || shouldSpawnMuske || ((choice == 0) && (slugNum < 2))){
            List<Pair<Integer, Integer>> orderedPathSpawnCandidates = new ArrayList<>();
            int indexPosition = orderedPath.indexOf(new Pair<Integer, Integer>(character.getX(), character.getY()));
            // inclusive start and exclusive end of range of positions not allowed
            int startNotAllowed = (indexPosition - 2 + orderedPath.size()) % orderedPath.size();
            int endNotAllowed = (indexPosition + 3) % orderedPath.size();
            // note terminating condition has to be != rather than < since wrap around...
            for (int i = endNotAllowed; i != startNotAllowed; i = (i + 1) % orderedPath.size()) {
                orderedPathSpawnCandidates.add(orderedPath.get(i));
            }

            // choose random choice
            Pair<Integer, Integer> spawnPosition = orderedPathSpawnCandidates
                    .get(rand.nextInt(orderedPathSpawnCandidates.size()));

            return spawnPosition;
        }
        return null;
    }

    public void addPotion(int numGained) {
        potionsOwned += numGained;
    }

    public void spendPotions() {

        if (potionsOwned > 0) {
            character.setHp(500);
            addPotion(-1);
            ;
        }
    }

    public DoubleProperty getExp() {
        return expDouble;
    }

    public IntegerProperty getExpInt() {
        return experience;
    }

    public IntegerProperty getCylceNum() {
        return new SimpleIntegerProperty(this.pathCycle / orderedPath.size());
    }


    public IntegerProperty getAllyNum() {
        return alliesOwned;
    }

    public IntegerProperty getHealthPotionNum() {
        return new SimpleIntegerProperty(potionsOwned);
    }

    public DoubleProperty getHp() {
        return character.getHpProgress();
    }

    public IntegerProperty getHpInt() {
        return character.getHp();
    }

    public void addGold(int num) {
        gold.set(gold.get() + num);
    }
    public void addDoggieCoin(int num) {
        this.doggieCoinOwned.set(this.getDoggieCoin().get() + num);
    }
    /*
    public void spendGold(int numLost) {
        this.goldOwned -= numLost;
    }
    */
    /*
    public int getExperience() {
        return this.experience;
    }
    */
    public void addExperience(int numGained) {
        this.experience.set(experience.get() + numGained);
        this.expDouble.set((double) (experience.get() + numGained) / 123456.00);
    }

    public BuildingProperty getShortestCampfire(EnemyProperty e) {
        if (this.getCampfire().isEmpty())
            return null;
        int shortest = 1000;
        BuildingProperty tmp = new Campfire(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        for (BuildingProperty b : this.getCampfire()) {
            int currDist = e.getDistance(b.getX(), b.getY());
            if (currDist < shortest) {
                tmp = b;
                shortest = currDist;
            }
        }
        return tmp;
    }

    public void getAwayFromCampfire(EnemyProperty e) {
        int shortest = 1000;
        int enemyPos = 0;
        int placeToGo = 0;
        int i = 0;
        boolean flag = false;
        for (Pair<Integer, Integer> pos : orderedPath) {
            boolean isAvailable = true;
            for (BuildingProperty building : campfires) {
                Campfire campfire = (Campfire) building;
                if (campfire.getDistance(pos.getValue0(), pos.getValue1()) <= campfire.getcampRadius()) {
                    isAvailable = false;
                    break;
                }
            }
            if (isAvailable) {
                if (e.getDistance(pos.getValue0(), pos.getValue1()) < shortest) {
                    placeToGo = i;
                    shortest = e.getDistance(pos.getValue0(), pos.getValue1());
                }
            }
            i++;
            if (pos.getValue0() == e.getX() && pos.getValue1() == e.getY()) {
                flag = true;
                enemyPos++;
            }
            if (!flag) {
                enemyPos++;
            }
        }
        if (shortest == 1000) {
            e.move();
            return;
        } else {
            int len = orderedPath.size() / 2;
            if (enemyPos - placeToGo < len && enemyPos - placeToGo > 0) {
                e.moveUpPath();
            } else {
                e.moveDownPath();
            }
        }
    }

    public void addUnequippedInventory(ItemProperty item) {

        this.unequippedInventoryItems.add(item);
    }

    public void supportMove(EnemyProperty e) {

        int enemyX = e.getX();
        int enemyY = e.getY();
        int characterX = character.getX();
        int characterY = character.getY();

        int len = orderedPath.size() / 2;
        int start = 0;
        boolean isStart = false;
        boolean isEnd = false;
        int end = 0;
        for (Pair<Integer, Integer> pair : orderedPath) {
            int tmpX = pair.getValue0();
            int tmpY = pair.getValue1();
            if (!isStart) {
                start += 1;
            }
            if (!isEnd) {
                end += 1;
            }
            if (tmpX == enemyX && tmpY == enemyY) {
                start += 1;
                isStart = true;
            }
            if (tmpX == characterX && tmpY == characterY) {
                end += 1;
                isEnd = true;
            }
            if (isStart && isEnd) {
                break;
            }
        }

        if (start - end < len && start - end > 0) {
            e.moveUpPath();
        } else {
            e.moveDownPath();
        }
    }

    public BuildingProperty createbuilding(String type, SimpleIntegerProperty x, SimpleIntegerProperty y) {
        BuildingProperty newBuilding = null;
        if (!checkBuildingAlrdyExisted(x, y)) {
            switch (type) {
                case "Village":
                    if (checkPathTile(x, y))
                        newBuilding = new Village(x, y);
                    break;
                case "Barracks":
                    if (checkPathTile(x, y))
                        newBuilding = new Barracks(x, y);
                    break;
                case "Tower":
                    if (!checkPathTile(x, y) && checkAdjacentToPathTile(x, y))
                        newBuilding = new Tower(x, y);
                    break;
                case "Trap":
                    if (checkPathTile(x, y))
                        newBuilding = new Trap(x, y);
                    break;
                case "VampireCastleBuilding":
                    if (!checkPathTile(x, y) && checkAdjacentToPathTile(x, y))
                        newBuilding = new VampireCastleBuilding(x, y);
                    break;
                case "ZombiePit":
                    if (!checkPathTile(x, y) && checkAdjacentToPathTile(x, y))
                        newBuilding = new ZombiePit(x, y);
                    break;
                case "Campfire":
                    if (!checkPathTile(x, y))
                        newBuilding = new Campfire(x, y);
                    break;
            }
        }

        if (newBuilding != null)
            this.buildings.add(newBuilding);
        return newBuilding;
    }

    public boolean checkBuildingAlrdyExisted(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        for (BuildingProperty b : this.buildings) {
            if (x.get() == b.getX() && y.get() == b.getY()) {
                return true;
            }
        }
        return false;
    }

    public void charactersStepOnBuilding() {
        for (BuildingProperty b : this.buildings) {
            int destX = b.getX();
            int destY = b.getY();
            int srcX = this.character.getPathPosition().getX().get();
            int srcY = this.character.getPathPosition().getY().get();

            if (srcX == destX && srcY == destY) {
                b.characterStepOn(this);
            }

        }
    }

    public void enemyStepOnBuilding() {
        List<BuildingProperty> toRemove = new ArrayList<BuildingProperty>();
        for (BuildingProperty b : this.buildings) {
            b.enemyStepOn(this, toRemove);
        }

        for (Building b : toRemove) {
            this.buildings.remove(b);
            b.destroy();
        }
        toRemove.clear();

    }

    /**
     * spawn a card in the world and return the card entity
     *
     * @return a card to be spawned in the controller as a JavaFX node
     */
    /*
     * public Card loadCard(String type) { Card newCard = null; checkCardEntity();
     * switch (type) { case "ZombiePit": newCard = new ZombiePitCard("ZombiePit",
     * new SimpleIntegerProperty(cardEntities.size()), new
     * SimpleIntegerProperty(0)); break; case "Village": newCard = new
     * VillageCard("Village", new SimpleIntegerProperty(cardEntities.size()), new
     * SimpleIntegerProperty(0)); break; case "Trap": newCard = new TrapCard("Trap",
     * new SimpleIntegerProperty(cardEntities.size()), new
     * SimpleIntegerProperty(0)); break; case "Tower": newCard = new
     * TowerCard("Tower", new SimpleIntegerProperty(cardEntities.size()), new
     * SimpleIntegerProperty(0)); break; case "Barracks": newCard = new
     * BarracksCard("Barracks", new SimpleIntegerProperty(cardEntities.size()), new
     * SimpleIntegerProperty(0)); break; case "Campfire": newCard = new
     * CampfireCard("Campfire", new SimpleIntegerProperty(cardEntities.size()), new
     * SimpleIntegerProperty(0)); break; case "VampireCastle": newCard = new
     * VampireCastleCard("VampireCastleBuilding", new
     * SimpleIntegerProperty(cardEntities.size()), new SimpleIntegerProperty(0));
     * default: return null; } cardEntities.add(newCard); return newCard; }
     */

    public VampireCastleCard loadVampireCard() {
        // if adding more cards than have, remove the first card...
        VampireCastleCard vampireCastleCard = new VampireCastleCard("VampireCastleBuilding", new SimpleIntegerProperty(cardEntities.size()), new SimpleIntegerProperty(0));
        cardEntities.add(vampireCastleCard);
        return vampireCastleCard;
    }

    public CampfireCard loadCampfireCard() {
        CampfireCard campfireCard = new CampfireCard("Campfire", new SimpleIntegerProperty(cardEntities.size()),
                new SimpleIntegerProperty(0));
        cardEntities.add(campfireCard);
        return campfireCard;
    }

    public TowerCard loadTowerCard() {
        TowerCard towerCard = new TowerCard("Tower", new SimpleIntegerProperty(cardEntities.size()),
                new SimpleIntegerProperty(0));
        cardEntities.add(towerCard);
        return towerCard;
    }

    public TrapCard loadTrapCard() {
        TrapCard trapCard = new TrapCard("Trap", new SimpleIntegerProperty(cardEntities.size()),
                new SimpleIntegerProperty(0));
        cardEntities.add(trapCard);
        return trapCard;
    }

    public BarracksCard loadBarracksCard() {
        BarracksCard barracksCard = new BarracksCard("Barracks", new SimpleIntegerProperty(cardEntities.size()),
                new SimpleIntegerProperty(0));
        cardEntities.add(barracksCard);
        return barracksCard;
    }

    public VillageCard loadVillageCard() {
        VillageCard villageCard = new VillageCard("Village", new SimpleIntegerProperty(cardEntities.size()),
                new SimpleIntegerProperty(0));
        cardEntities.add(villageCard);
        return villageCard;
    }

    public ZombiePitCard loadZombiePitCard() {
        ZombiePitCard zombiePitCard = new ZombiePitCard("ZombiePit", new SimpleIntegerProperty(cardEntities.size()),
                new SimpleIntegerProperty(0));
        cardEntities.add(zombiePitCard);
        return zombiePitCard;
    }






    public boolean checkPathTile(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        Pair<Integer, Integer> position = new Pair<>(x.get(), y.get());
        for (Pair<Integer, Integer> pos : orderedPath) {
            if (pos.equals(position))
                return true;
        }
        return false;
    }

    public boolean checkAdjacentToPathTile(SimpleIntegerProperty x, SimpleIntegerProperty y) {

        Pair<Integer, Integer> up = new Pair<>(x.get() - 1, y.get());
        Pair<Integer, Integer> down = new Pair<>(x.get() + 1, y.get());
        Pair<Integer, Integer> left = new Pair<>(x.get(), y.get() - 1);
        Pair<Integer, Integer> right = new Pair<>(x.get(), y.get() + 1);

        for (Pair<Integer, Integer> pos : orderedPath) {
            if (pos.equals(up) || pos.equals(down) || pos.equals(left) || pos.equals(right))
                return true;
        }

        return false;
    }

    /**
     * remove card at a particular index of cards (position in gridpane of unplayed
     * cards)
     *
     * @param index the index of the card, from 0 to length-1
     */
    public void removeCard(int index) {
        Card c = cardEntities.get(index);
        int x = c.getX();
        c.destroy();
        cardEntities.remove(index);
        shiftCardsDownFromXCoordinate(x);
    }

    /**
     * remove a card by its x, y coordinates
     *
     * @param cardNodeX     x index from 0 to width-1 of card to be removed
     * @param cardNodeY     y index from 0 to height-1 of card to be removed
     * @param buildingNodeX x index from 0 to width-1 of building to be added
     * @param buildingNodeY y index from 0 to height-1 of building to be added
     */
    public BuildingProperty convertCardToBuildingByCoordinates(int cardNodeX, int cardNodeY, int buildingNodeX,
            int buildingNodeY) {
        // start by getting card
        Card card = null;
        for (Card c : cardEntities) {
            if ((c.getX() == cardNodeX) && (c.getY() == cardNodeY)) {
                card = c;
                break;
            }
        }
        // if (card == null) return null;
        String type = card.getType();

        // now spawn building
        BuildingProperty newBuilding = createbuilding(type, new SimpleIntegerProperty(buildingNodeX),
                new SimpleIntegerProperty(buildingNodeY));

        // destroy the card
        card.destroy();
        cardEntities.remove(card);
        shiftCardsDownFromXCoordinate(cardNodeX);

        return newBuilding;
    }

    public boolean isShopTime() {
        if (character.getX() == startCastle.getX() && character.getY() == startCastle.getY() && shopTimes < getCycle()) {
            shopTimes++;
            return true;
        }
        return false;
    }

    public boolean isGameOver() {
        if (character.getHp().get() <= 0) {
            /*if (getMode() == ModeType.CONFUSING) {

            } */
            if (ringOwned.get() > 0) {
                ringOwned.set(ringOwned.get() - 1);;
                character.setHp(500);
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    public boolean isGameWin() {
        /*if (goal.goalComplete(getGold().get(), getExpInt().get(), getCylceNum().get())) {
            return true;
        } else {
            return false;
        }*/
        return totaGoal.goalReached();
    }

    public Character getCharacter() {
        return this.character;
    }

    public List<BuildingProperty> getBuildings() {
        return this.buildings;
    }

    public ItemProperty[] getEquipItems() {
        return equippedItems.getEquipment();
    }

    public boolean isBossAlive() {
        for (EnemyProperty e : enemies) {
            if (e.isBoss()) return true;
        }
        return !hasSpawnMuske.get();
    }

    public IntegerProperty getItemPrice(ItemType itemType) {
        if (itemType == ItemType.ARMOUR) {
            return Armour.price;
        }
        else if (itemType == ItemType.HELMET) {
            return Helmet.price;
        }
        else if (itemType == ItemType.SHIELD) {
            return Shield.price;
        }
        else if (itemType == ItemType.STAFF) {
            return Staff.price;
        }
        else if (itemType == ItemType.STAKE) {
            return Stake.price;
        }
        else if (itemType == ItemType.SWORD) {
            return Sword.price;
        }
        else if (itemType == ItemType.HEALTHPOTION) {
            return HealthPotion.price;
        }
        else if (itemType == ItemType.DOGGIECOIN) {
            return DoggieCoinPrice.price;
        }
        return new SimpleIntegerProperty(0);
    }

    public IntegerProperty getBattleSlugNum() {
        int num = 0;
        for (EnemyProperty enemy : enemies) {
            System.err.println(enemy.getInBattle());
            if (enemy.getType().equals("Slug") && enemy.getInBattle())
                num++;
        }
        return new SimpleIntegerProperty(num);
    }

    public IntegerProperty getBattleVampireNum() {
        int num = 0;
        for (EnemyProperty enemy : enemies) {
            if (enemy.getType().equals("Vampire") && enemy.getInBattle())
                num++;
        }
        return new SimpleIntegerProperty(num);
    }

    public IntegerProperty getBattleZombieNum() {
        int num = 0;
        for (EnemyProperty enemy : enemies) {
            if (enemy.getType().equals("Zombie") && enemy.getInBattle())
                num++;
        }
        return new SimpleIntegerProperty(num);
    }

    public IntegerProperty getBattleDoggieNum() {
        int num = 0;
        for (EnemyProperty enemy : enemies) {
            if (enemy.getType().equals("Doggie") && enemy.getInBattle())
                num++;
        }
        return new SimpleIntegerProperty(num);
    }

    public IntegerProperty getBattleMuskNum() {

        int num = 0;
        for (EnemyProperty enemy : enemies) {
            if (enemy.getType().equals("ElanMuske") && enemy.getInBattle())
                num++;
        }
        return new SimpleIntegerProperty(num);
    }
    public List<Card> getCardEntities() {
        return this.cardEntities;
    }

    public IntegerProperty getGold() {
        return this.gold;
    }

    public ModeType getMode() {
        return mode;
    }

    public void setMode(ModeType mode) {
        this.mode = mode;
    }

    public DoubleProperty getSuperPowerProgress() {
        return character.getSuperPowerProgress();
    }

    public void useSuperPower() {
        character.useSuperPower();
    }

}
