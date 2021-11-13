package test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.ArrayList;
import java.util.List;

import unsw.loopmania.Card;

import org.junit.jupiter.api.Test;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.Ally;
import unsw.loopmania.Armour;
import unsw.loopmania.Barracks;
import unsw.loopmania.BarracksCard;
import unsw.loopmania.BasicEnemy;
import unsw.loopmania.BasicItem;
import unsw.loopmania.Campfire;
import unsw.loopmania.CampfireCard;
import unsw.loopmania.Character;
import unsw.loopmania.ElanMuske;
import unsw.loopmania.EnemyProperty;
import unsw.loopmania.GoalCompsite;
import unsw.loopmania.GoalLogic;
import unsw.loopmania.GoalOrLogic;
import unsw.loopmania.Gold;
import unsw.loopmania.HealthPotion;
import unsw.loopmania.Helmet;
import unsw.loopmania.HeroCastle;
import unsw.loopmania.ItemType;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.LoopManiaWorldController;
import unsw.loopmania.ModeType;
import unsw.loopmania.PathPosition;
import unsw.loopmania.Shield;
import unsw.loopmania.Slug;
import unsw.loopmania.Staff;
import unsw.loopmania.Stake;
import unsw.loopmania.SubGoalGold;
import unsw.loopmania.Sword;
import unsw.loopmania.TheOneRing;
import unsw.loopmania.Tower;
import unsw.loopmania.TowerCard;
import unsw.loopmania.Trap;
import unsw.loopmania.TrapCard;
import unsw.loopmania.Vampire;
import unsw.loopmania.VampireCastleBuilding;
import unsw.loopmania.VampireCastleCard;
import unsw.loopmania.Village;
import unsw.loopmania.VillageCard;
import unsw.loopmania.Zombie;
import unsw.loopmania.ZombiePit;
import unsw.loopmania.ZombiePitCard;

import org.javatuples.Pair;

public class integrationTest {
    @Test
    public void Test() {
        List<Pair<Integer, Integer>> list = new ArrayList<Pair<Integer, Integer>>();
        Pair<Integer, Integer> pair1 = new Pair<>(1, 2);
        Pair<Integer, Integer> pair2 = new Pair<>(2, 2);
        list.add(pair1);
        list.add(pair2);
        LoopManiaWorld world = new LoopManiaWorld(5, 5, list);
        List<Pair<Integer, Integer>> orderedPath = world.getOrderedPath();
        int index = orderedPath.indexOf(pair1);
        PathPosition position = new PathPosition(index, orderedPath);
        Ally ally = new Ally(position);
        Character character = new Character(position);
        //
        Vampire vampire = new Vampire(position);
        Zombie zombie = new Zombie(position);
        SimpleIntegerProperty x = new SimpleIntegerProperty(1);
        SimpleIntegerProperty y = new SimpleIntegerProperty(2);
        Armour armour = new Armour(x, y);
        Barracks barrack = new Barracks(x, y);
        BarracksCard barracksCard = new BarracksCard("Barracks", x, y);
        Campfire campfire = new Campfire(x, y);
        CampfireCard campfireCard = new CampfireCard("Campfire", x, y);
        Gold gold = new Gold(x, y);
        HealthPotion healthPotion = new HealthPotion(x, y);
        Helmet helmet = new Helmet(x, y);
        HeroCastle heroCastle = new HeroCastle(x, y);
        Shield shield = new Shield(x, y);
        Slug slug = new Slug(position);
        Staff staff = new Staff(x, y);
        Stake stake = new Stake(x, y);
        Sword sword = new Sword(x, y);
        TheOneRing theOneRing = new TheOneRing(x, y);
        Tower tower = new Tower(x, y);
        TowerCard towerCard = new TowerCard("Tower", x, y);
        Trap trap = new Trap(x, y);
        TrapCard trapCard = new TrapCard("Trap", x, y);
        VampireCastleBuilding vampireCastleBuilding = new VampireCastleBuilding(x, y);
        VampireCastleCard vampireCastleCard = new VampireCastleCard("VampireCastleBuilding", x, y);
        Village village = new Village(x, y);
        VillageCard villageCard = new VillageCard("Village", x, y);
        ZombiePit zombiePit = new ZombiePit(x, y);
        ZombiePitCard zombiePitCard = new ZombiePitCard("ZombiePit", x, y);
        world.setCharacter(character);
        for (int i = 0; i < 20 * orderedPath.size(); i++) {
            // TODO = handle more aspects of the behaviour required by the specification
            //System.out.println("starting timer");

            // trigger adding code to process main game logic to queue. JavaFX will target
            // framerate of 0.3 seconds

            world.runTickMoves();


        }
        assertEquals(0, world.getAllies().size());
        assertEquals(0, world.getEnemy().size());
        assertEquals(0, world.getCampfire().size());
        assertNotEquals(null, world.getOrderedPath());
        assertEquals(0, world.getUnpickedItems().size());
        world.addAlly(ally);
        assertEquals(1, world.getAllies().size());
        assertEquals(5, world.getWidth());
        assertEquals(5, world.getHeight());
        world.setCharacter(character);
        world.addEntity(character);
        world.getEnemy().add(slug);
        world.getEnemy().add(vampire);
        world.getEnemy().add(zombie);

        assertEquals(2, world.possiblySpawnItems().size());
        world.killEnemy(vampire);
        //world.killEnemy(zombie);


        List<BasicEnemy> enemies = new ArrayList<>();
        while (true) {
            if (world.getEnemy().size() == 0)
                break;
            if (character.getHp().get() <= 0) {
                break;
            }
            enemies.addAll(world.runBattles());
        }
        assertNotEquals(3, enemies.size());
        world.killAlly(ally);
        assertEquals(0, world.getAllies().size());
        assertNotEquals(null, world.generateItem());

        assertEquals(ItemType.SWORD, world.addUnequippedItem(ItemType.SWORD).getType());
        world.equipItemByCoordinates(0, 0);
        Ally ally22 = new Ally(position);
        Ally ally33 = new Ally(position);
        world.getAllies().add(ally22);
        world.getAllies().add(ally33);
        for (int i = 0; i < orderedPath.size(); i++) {
            world.runTickMoves();
        }
        assertNotEquals(0, world.getGold());
        assertEquals(1, world.getPotions());
        character.setHp(100);
        world.spendPotions();
        assertEquals(500, character.getHp().get());
        assertEquals(0, world.getPotions());
        assertNotEquals(0, world.getGold());
        //DoubleProperty gold = world.getGold();
        world.addGold(100);
        //assertEquals(gold + 100, world.getGold());

        //assertEquals(gold, world.getGold());
        world.addExperience(100);

        EnemyProperty slugb = new Slug(position);
        //world.generateTrophy(slugb);
        assertEquals(0, world.getBuildings().size());
        world.createbuilding("VampireCastle", x, y);
        //System.err.println(world.getBuildings());
        //assertEquals(1, world.getBuildings().size());
        world.loadVampireCard();
        world.loadCampfireCard();
        world.loadTowerCard();
        world.loadTrapCard();
        world.loadBarracksCard();
        world.loadVillageCard();
        world.loadZombiePitCard();



        world.getHealthPotionNum();
        world.getExpInt();
        world.addExperience(3);
        world.getHpInt();
        world.getAllyNum();
        world.isGameOver();
        world.getExp();
        world.getHp();
        world.getCylceNum();
        world.isShopTime();
        world.updatePathCycle();


        world.checkAdjacentToPathTile(x, y);
        world.supportMove(slugb);
        world.getAwayFromCampfire(slugb);
        world.createbuilding("Trap", x, y);
        world.charactersStepOnBuilding();
        world.moveBasicEnemies();
        world.createbuilding("Village", x, y);
        world.createbuilding("Barracks", x, y);
        world.createbuilding("Tower", new SimpleIntegerProperty(1), new SimpleIntegerProperty(2));
        world.createbuilding("VampireCastleBuilding", new SimpleIntegerProperty(1), new SimpleIntegerProperty(2));
        world.createbuilding("ZombiePit", new SimpleIntegerProperty(1), new SimpleIntegerProperty(2));
        world.createbuilding("Campfire", new SimpleIntegerProperty(1), new SimpleIntegerProperty(2));
        world.getShortestCampfire(slugb);
        assertEquals(true, world.checkBuildingAlrdyExisted(x, y));
        world.enemyStepOnBuilding();
        Ally ally1 = new Ally(position);
        Ally ally2 = new Ally(position);
        Ally ally3 = new Ally(position);
        world.addAlly(ally1);
        world.addAlly(ally2);
        world.addAlly(ally3);
        world.runTickMoves();
        world.getMode();
        world.getCardEntities();
        world.getUnequippedInventoryItems();
        world.getRing();
        world.getHealthPotion();
        world.getDoggieCoin();
        world.getGoal();
        world.getSuperPowerProgress();
        world.getHpValue();
        world.setMode(ModeType.STANDARD);

        GoalCompsite goal = new SubGoalGold(world, 300);
        GoalLogic logic = new GoalOrLogic();
        logic.addGoal(goal);
        world.setGoal(logic);
        world.isGameWin();
        world.addUnequippedInventory(new Sword(x, y));
        world.removeUnequippedInventoryItem(new Sword(x, y));
        world.addDoggieCoin(3);
        world.removeUnequippedInventoryItemByCoordinates(x.get(), y.get());
        world.addUnequippedInventory(new Sword(x, y));
        world.equipItemByCoordinates(x.get(), y.get());
        world.removeItemByPositionInUnequippedInventoryItems(0);
        world.removeCard(0);
        world.shiftCardsDownFromXCoordinate(0);
        world.getBattleMuskNum();
        world.getBattleSlugNum();
        world.getBattleDoggieNum();
        world.getBattleVampireNum();
        world.getBattleZombieNum();
        world.getItemPrice(ItemType.STAFF);
        world.getItemPrice(ItemType.ARMOUR);
        world.getItemPrice(ItemType.HELMET);
        world.getItemPrice(ItemType.SHIELD);
        world.getItemPrice(ItemType.STAKE);
        world.getItemPrice(ItemType.SWORD);
        world.getItemPrice(ItemType.HEALTHPOTION);
        world.getItemPrice(ItemType.DOGGIECOIN);
        world.getCardEntities().add(new VampireCastleCard("VampireCastleCard", x, y));
        world.convertCardToBuildingByCoordinates(x.get(), y.get(), x.get(), y.get());
        world.possiblyGetBasicEnemySpawnPosition();
        world.possiblySpawnEnemies();
        world.getBuildings().add(campfire);
        world.getShortestCampfire(slugb);
        world.getCharacter().setHp(0);
        world.isGameOver();
        ElanMuske musk = new ElanMuske(position);
        world.getEnemy().add(musk);
        world.isBossAlive();
        world.getBuildings().add(new Campfire(x, y));
        world.updatePathCycle();
        world.useSuperPower();
        Zombie zombie2 = new Zombie(position);
        Campfire campfire2 = new Campfire(x, y);
        world.getCampfire().add(campfire2);
        world.getShortestCampfire(zombie2);
        world.getRing().set(2);
        world.isGameOver();
        Zombie zombie3 = new Zombie(position);
        Zombie zombie4 = new Zombie(position);
        world.getEnemy().add(zombie3);
        world.getEnemy().add(zombie4);
        world.moveBasicEnemies();
    }
}
