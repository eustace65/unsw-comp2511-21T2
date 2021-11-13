package test;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import javafx.beans.property.SimpleIntegerProperty;
import org.javatuples.Pair;
import unsw.loopmania.Character;
import unsw.loopmania.HealthPotion;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.PathPosition;
import unsw.loopmania.Sword;
import unsw.loopmania.BasicItem;
import unsw.loopmania.Item;
import unsw.loopmania.ItemType;

public class EquipmentTest {
    @Test
    public void testEquipment() {
        List<Pair<Integer, Integer>> list = new ArrayList<Pair<Integer, Integer>>();
        Pair<Integer, Integer> pair1 = new Pair<>(1, 2);
        Pair<Integer, Integer> pair2 = new Pair<>(2, 2);
        list.add(pair1);
        list.add(pair2);
        LoopManiaWorld world = new LoopManiaWorld(5, 5, list);
        Item sword = world.addUnequippedItem(ItemType.SWORD);
    //    assertEquals(sword.getType(), ItemType.SWORD);
        assertEquals(sword.getX(), 0);
        assertEquals(sword.getY(), 0);
        //System.out.println(sword.getType());
        Item helmet = world.addUnequippedItem(ItemType.HELMET);
        assertEquals(helmet.getX(), 1);
        assertEquals(helmet.getY(), 0);
        assertEquals(helmet.getType(), ItemType.HELMET);
        Item sword1 = world.equipItemByCoordinates(0, 0);
        //System.out.println(sword1.getType());
        assertEquals(sword1.getX(), 0);
    }
    @Test
    public void testMismatchedSlots() {
        List<Pair<Integer, Integer>> list = new ArrayList<Pair<Integer, Integer>>();
        Pair<Integer, Integer> pair1 = new Pair<>(1, 2);
        Pair<Integer, Integer> pair2 = new Pair<>(2, 2);
        list.add(pair1);
        list.add(pair2);
        LoopManiaWorld world = new LoopManiaWorld(5, 5, list);
        Item helmet = world.addUnequippedItem(ItemType.HELMET);
        assertEquals(helmet.getX(), 0);
        assertEquals(helmet.getY(), 0);
        Item sword = world.addUnequippedItem(ItemType.SWORD);
        assertEquals(sword.getX(), 1);
        assertEquals(sword.getY(), 0);
        Item sword1 = world.equipItemByCoordinates(1, 0);
        assertEquals(sword1.getX(), 0);
        Item helmet1 = world.equipItemByCoordinates(0, 0);
        assertEquals(helmet1.getX(), 1);
        Item shield = world.addUnequippedItem(ItemType.SHIELD);
        assertEquals(shield.getX(), 0);
        Item shield1 = world.equipItemByCoordinates(0, 0);
        assertEquals(shield1.getX(), 2);

    }
    @Test
    public void testStake() {
        List<Pair<Integer, Integer>> list = new ArrayList<Pair<Integer, Integer>>();
        Pair<Integer, Integer> pair1 = new Pair<>(1, 2);
        Pair<Integer, Integer> pair2 = new Pair<>(2, 2);
        list.add(pair1);
        list.add(pair2);
        LoopManiaWorld world = new LoopManiaWorld(5, 5, list);
        Item stake = world.addUnequippedItem(ItemType.STAKE);
        assertEquals(stake.getType(), ItemType.STAKE);
        assertEquals(stake.getX(), 0);
        assertEquals(stake.getY(), 0);
    }
    @Test
    public void testStaff() {
        List<Pair<Integer, Integer>> list = new ArrayList<Pair<Integer, Integer>>();
        Pair<Integer, Integer> pair1 = new Pair<>(1, 2);
        Pair<Integer, Integer> pair2 = new Pair<>(2, 2);
        list.add(pair1);
        list.add(pair2);
        LoopManiaWorld world = new LoopManiaWorld(5, 5, list);
        Item staff = world.addUnequippedItem(ItemType.STAFF);
        assertEquals(staff.getType(), ItemType.STAFF);
        assertEquals(staff.getX(), 0);
        assertEquals(staff.getY(), 0);
        Item staff1 = world.equipItemByCoordinates(0, 0);
        assertEquals(staff1.getX(), 0);
    }
    @Test
    public void testSword() {
        List<Pair<Integer, Integer>> list = new ArrayList<Pair<Integer, Integer>>();
        Pair<Integer, Integer> pair1 = new Pair<>(1, 2);
        Pair<Integer, Integer> pair2 = new Pair<>(2, 2);
        list.add(pair1);
        list.add(pair2);
        LoopManiaWorld world = new LoopManiaWorld(5, 5, list);
        Item sword = world.addUnequippedItem(ItemType.SWORD);
        assertEquals(sword.getType(), ItemType.SWORD);
        assertEquals(sword.getX(), 0);
        assertEquals(sword.getY(), 0);
        Item sword1 = world.equipItemByCoordinates(0, 0);
        assertEquals(sword1.getX(), 0);
    }
    @Test
    public void testArmour() {
        List<Pair<Integer, Integer>> list = new ArrayList<Pair<Integer, Integer>>();
        Pair<Integer, Integer> pair1 = new Pair<>(1, 2);
        Pair<Integer, Integer> pair2 = new Pair<>(2, 2);
        list.add(pair1);
        list.add(pair2);
        LoopManiaWorld world = new LoopManiaWorld(5, 5, list);
        Item armour = world.addUnequippedItem(ItemType.ARMOUR);
        assertEquals(armour.getType(), ItemType.ARMOUR);
        assertEquals(armour.getX(), 0);
        assertEquals(armour.getY(), 0);
        Item armour1 = world.equipItemByCoordinates(0, 0);
        assertEquals(armour1.getX(), 3);
    }

    @Test
    public void testShield() {
        List<Pair<Integer, Integer>> list = new ArrayList<Pair<Integer, Integer>>();
        Pair<Integer, Integer> pair1 = new Pair<>(1, 2);
        Pair<Integer, Integer> pair2 = new Pair<>(2, 2);
        list.add(pair1);
        list.add(pair2);
        LoopManiaWorld world = new LoopManiaWorld(5, 5, list);
        Item shield = world.addUnequippedItem(ItemType.SHIELD);
        assertEquals(shield.getType(), ItemType.SHIELD);
        assertEquals(shield.getX(), 0);
        assertEquals(shield.getY(), 0);
        Item shield1 = world.equipItemByCoordinates(0, 0);
        assertEquals(shield1.getX(), 2);
    }

}
