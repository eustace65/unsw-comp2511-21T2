package test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.javatuples.Pair;

import unsw.loopmania.Ally;
import unsw.loopmania.Character;
import unsw.loopmania.Doggie;
import unsw.loopmania.ElanMuske;
import unsw.loopmania.EnemyProperty;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.PathPosition;
import unsw.loopmania.Slug;
import unsw.loopmania.Vampire;
import unsw.loopmania.Zombie;

public class BasicEnemyTest {
    @Test
    public void testSlug() {
        List<Pair<Integer, Integer>> list = new ArrayList<Pair<Integer, Integer>>();
        Pair<Integer, Integer> pair1 = new Pair<>(1, 2);
        Pair<Integer, Integer> pair2 = new Pair<>(2, 2);
        list.add(pair1);
        list.add(pair2);
        LoopManiaWorld world = new LoopManiaWorld(5, 5, list);
        List<Pair<Integer, Integer>> orderedPath = world.getOrderedPath();
        int index = orderedPath.indexOf(pair1);
        PathPosition position = new PathPosition(index, orderedPath);
        position.moveUpPath();
        position.moveDownPath();
        Slug slug = new Slug(position);
        assertEquals("Slug", slug.getType());
        assertEquals(600, slug.getHP());
        assertEquals(100, slug.getExp());
        assertEquals(10, slug.getDamage());
        assertEquals(1, slug.getFightRadius());
        assertEquals(1, slug.getSupportRadius());
        assertFalse(slug.getIsWeak());
        assertEquals(100, slug.getGold());
        assertEquals(2, slug.getSpeed());
        assertFalse(slug.getInBattle());
        assertEquals("Up", slug.getLastDirec());
        slug.setInBattle(true);
        assertTrue(slug.getInBattle());
        slug.setLastDirec("Down");
        assertEquals("Down", slug.getLastDirec());
        Ally ally = new Ally(position);
        slug.attack_ally(ally);
        assertEquals(90, ally.getHp());
        assertEquals(2, slug.getDistance(3, 4));
        Character character = new Character(position);
        world.setCharacter(character);
        slug.attack_character(character);
        slug.setAllPropertyBack();
        assertTrue(slug.attack(world, new ArrayList<>(), new ArrayList<>(), true, world.getEquipItems()));
        assertFalse(slug.isBoss());
    }

    @Test
    public void testVampire() {
        List<Pair<Integer, Integer>> list = new ArrayList<Pair<Integer, Integer>>();
        Pair<Integer, Integer> pair1 = new Pair<>(1, 2);
        Pair<Integer, Integer> pair2 = new Pair<>(2, 2);
        list.add(pair1);
        list.add(pair2);
        LoopManiaWorld world = new LoopManiaWorld(5, 5, list);
        List<Pair<Integer, Integer>> orderedPath = world.getOrderedPath();
        int index = orderedPath.indexOf(pair1);
        PathPosition position = new PathPosition(index, orderedPath);
        Vampire vampire = new Vampire(position);
        Ally ally = new Ally(position);
        vampire.attack_ally(ally);
        vampire.attack_ally(ally);
        vampire.attack_ally(ally);
        vampire.attack_ally(ally);
        vampire.attack_ally(ally);
        vampire.attack_ally(ally);
        vampire.attack_ally(ally);
        vampire.attack_ally(ally);
        vampire.attack_ally(ally);
        assertNotEquals(300, ally.getHp());
        Character character = new Character(position);
        vampire.attack_character(character);
        vampire.attack_character(character);
        vampire.attack_character(character);
        vampire.attack_character(character);
        vampire.attack_character(character);
        vampire.attack_character(character);
        vampire.attack_character(character);
        vampire.attack_character(character);
        vampire.attack_character(character);
        assertNotEquals(500, character.getHp());
        vampire.setCriticalPoss(100);
        assertEquals(100, vampire.getCriticalPoss());
        assertFalse(vampire.isBoss());
        assertEquals(100, vampire.getCriticalPoss());
        vampire.move();
        vampire.setAllPropertyBack();
        world.setCharacter(character);
        assertTrue(vampire.attack(world, new ArrayList<>(), new ArrayList<>(), true, world.getEquipItems()));
    }

    @Test
    public void testZombie() {
        List<Pair<Integer, Integer>> list = new ArrayList<Pair<Integer, Integer>>();
        Pair<Integer, Integer> pair1 = new Pair<>(1, 2);
        Pair<Integer, Integer> pair2 = new Pair<>(2, 2);
        list.add(pair1);
        list.add(pair2);
        LoopManiaWorld world = new LoopManiaWorld(5, 5, list);
        List<Pair<Integer, Integer>> orderedPath = world.getOrderedPath();
        int index = orderedPath.indexOf(pair1);
        PathPosition position = new PathPosition(index, orderedPath);
        Zombie zombie = new Zombie(position);
        Ally ally = new Ally(position);
        zombie.attack_ally(ally);

        Character character = new Character(position);
        zombie.attack_character(character);
        zombie.setAllPropertyBack();
        world.setCharacter(character);
        assertTrue(zombie.attack(world, new ArrayList<>(), new ArrayList<>(), true, world.getEquipItems()));
        assertFalse(zombie.isBoss());
    }

    @Test
    public void testDoggie() {
        List<Pair<Integer, Integer>> list = new ArrayList<Pair<Integer, Integer>>();
        Pair<Integer, Integer> pair1 = new Pair<>(1, 2);
        Pair<Integer, Integer> pair2 = new Pair<>(2, 2);
        list.add(pair1);
        list.add(pair2);
        LoopManiaWorld world = new LoopManiaWorld(5, 5, list);
        List<Pair<Integer, Integer>> orderedPath = world.getOrderedPath();
        int index = orderedPath.indexOf(pair1);
        PathPosition position = new PathPosition(index, orderedPath);
        Doggie zombie = new Doggie(position);
        Ally ally = new Ally(position);
        zombie.attack_ally(ally);
        Character character = new Character(position);
        zombie.attack_character(character);
        zombie.setAllPropertyBack();
        world.setCharacter(character);
        assertTrue(zombie.attack(world, new ArrayList<>(), new ArrayList<>(), true, world.getEquipItems()));
        assertTrue(zombie.isBoss());
    }


    @Test
    public void testMusk() {
        List<Pair<Integer, Integer>> list = new ArrayList<Pair<Integer, Integer>>();
        Pair<Integer, Integer> pair1 = new Pair<>(1, 2);
        Pair<Integer, Integer> pair2 = new Pair<>(2, 2);
        list.add(pair1);
        list.add(pair2);
        LoopManiaWorld world = new LoopManiaWorld(5, 5, list);
        List<Pair<Integer, Integer>> orderedPath = world.getOrderedPath();
        int index = orderedPath.indexOf(pair1);
        PathPosition position = new PathPosition(index, orderedPath);
        ElanMuske zombie = new ElanMuske(position);
        Ally ally = new Ally(position);
        zombie.attack_ally(ally);
        Character character = new Character(position);
        zombie.attack_character(character);
        zombie.setAllPropertyBack();
        world.setCharacter(character);
        world.getAllies().add(ally);
        world.setCycle(6);
        assertTrue(zombie.attack(world, new ArrayList<>(), new ArrayList<>(), true, world.getEquipItems()));
        assertTrue(zombie.isBoss());
        Zombie zombie2 = new Zombie(position);
        zombie2.setHP(100);
        List<EnemyProperty> list2 = new ArrayList<>();
        list2.add(zombie2);
        zombie.recoverEnemies(list2);
        assertEquals(zombie2.getHP(), 110);

    }



}
