package test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import unsw.loopmania.Ally;
import unsw.loopmania.BasicEnemy;
import unsw.loopmania.EnemyProperty;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.PathPosition;
import unsw.loopmania.Zombie;
import org.javatuples.Pair;

public class AllyTest {
    @Test
    public void testAlly()  {
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
        assertEquals("Ally", ally.getType());
        assertEquals(100, ally.getHp());
        ally.setHp(200);
        assertEquals(200, ally.getHp());
        assertEquals(200, ally.getDamage());
        assertEquals(0, ally.getRound());
        ally.setRound(5);
        assertEquals(5, ally.getRound());
        ally.setOriginalType("Vampire");
        assertEquals("Vampire", ally.getOriginalType());
        EnemyProperty e = new Zombie(position);
        ally.attack(e);
        assertEquals(600, e.getHP());
    }
}
