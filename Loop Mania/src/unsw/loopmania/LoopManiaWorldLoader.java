package unsw.loopmania;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

import org.javatuples.Pair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import javafx.beans.property.SimpleIntegerProperty;

import java.util.List;

/**
 * Loads a world from a .json file.
 * 
 * By extending this class, a subclass can hook into entity creation.
 * This is useful for creating UI elements with corresponding entities.
 * 
 * this class is used to load the world.
 * it loads non-spawning entities from the configuration files.
 * spawning of enemies/cards must be handled by the controller.
 */
public abstract class LoopManiaWorldLoader {
    private JSONObject json;
    //List<GoalCompsite> goals = new ArrayList<>();
    private GoalLogic totalGoal;

    public LoopManiaWorldLoader(String filename) throws FileNotFoundException {
        json = new JSONObject(new JSONTokener(new FileReader("worlds/" + filename)));
        totalGoal = new GoalAndLogic();
    }

    /**
     * Parses the JSON to create a world.
     */
    public LoopManiaWorld load() {
        int width = json.getInt("width");
        int height = json.getInt("height");

        // path variable is collection of coordinates with directions of path taken...
        List<Pair<Integer, Integer>> orderedPath = loadPathTiles(json.getJSONObject("path"), width, height);

        LoopManiaWorld world = new LoopManiaWorld(width, height, orderedPath);

        JSONArray jsonEntities = json.getJSONArray("entities");

        // load non-path entities later so that they're shown on-top
        for (int i = 0; i < jsonEntities.length(); i++) {
            loadEntity(world, jsonEntities.getJSONObject(i), orderedPath);
        }

        // Extract goals from Json file
        JSONObject goal = json.getJSONObject("goal-condition");
        loadGoal(world, goal);
        return world;
    }

    /**
     * load an entity into the world
     * @param world backend world object
     * @param json a JSON object to parse (different from the )
     * @param orderedPath list of pairs of x, y cell coordinates representing game path
     */
    private void loadEntity(LoopManiaWorld world, JSONObject currentJson, List<Pair<Integer, Integer>> orderedPath) {
        String type = currentJson.getString("type");
        int x = currentJson.getInt("x");
        int y = currentJson.getInt("y");
        int indexInPath = orderedPath.indexOf(new Pair<Integer, Integer>(x, y));
        Entity entity = null;
        //assert indexInPath != -1;
        if (indexInPath != -1) {

            // TODO = load more entity types from the file
            switch (type) {
            case "hero_castle":
                Character character = new Character(new PathPosition(indexInPath, orderedPath));
                world.setCharacter(character);
                onLoad(character);
                entity = character;
                break;
            case "path_tile":
                throw new RuntimeException("path_tile's aren't valid entities, define the path externally.");
            // TODO Handle other possible entities
            case "barracks":
                Barracks barracks = new Barracks(new SimpleIntegerProperty(x), new SimpleIntegerProperty(y));
                world.getBuildings().add(barracks);
                onLoad(barracks);
                entity = barracks;
                break;
            /*case "gold":
                Gold gold = new Gold(new SimpleIntegerProperty(x), new SimpleIntegerProperty(y));
                world.getUnpickedItems().add(gold);
                onLoad(gold);
                entity = gold;
                break;*/
                //TODO: gold unable to be picked
            }
        } else {
            switch(type) {
            case "vampire_castle":
                VampireCastleBuilding vampireCastle = new VampireCastleBuilding(new SimpleIntegerProperty(x), new SimpleIntegerProperty(y));
                world.getBuildings().add(vampireCastle);
                onLoad(vampireCastle);
                entity = vampireCastle;
                break;
            case "campfire":
                Campfire campfire  = new Campfire(new SimpleIntegerProperty(x), new SimpleIntegerProperty(y));
                world.getBuildings().add(campfire);
                onLoad(campfire);
                entity = campfire;
                break;
            case "tower":
                Tower tower  = new Tower(new SimpleIntegerProperty(x), new SimpleIntegerProperty(y));
                world.getBuildings().add(tower);
                onLoad(tower);
                entity = tower;
                break;
            case "zombiePit":
                ZombiePit zombiePit = new ZombiePit(new SimpleIntegerProperty(x), new SimpleIntegerProperty(y));
                world.getBuildings().add(zombiePit);
                onLoad(zombiePit);
                entity = zombiePit;
                break;
            }
            

        }
        if (entity != null) world.addEntity(entity);
    }

    /**
     * load path tiles
     * @param path json data loaded from file containing path information
     * @param width width in number of cells
     * @param height height in number of cells
     * @return list of x, y cell coordinate pairs representing game path
     */
    private List<Pair<Integer, Integer>> loadPathTiles(JSONObject path, int width, int height) {
        if (!path.getString("type").equals("path_tile")) {
            // ... possible extension
            throw new RuntimeException(
                    "Path object requires path_tile type.  No other path types supported at this moment.");
        }
        PathTile starting = new PathTile(new SimpleIntegerProperty(path.getInt("x")), new SimpleIntegerProperty(path.getInt("y")));
        if (starting.getY() >= height || starting.getY() < 0 || starting.getX() >= width || starting.getX() < 0) {
            throw new IllegalArgumentException("Starting point of path is out of bounds");
        }
        // load connected path tiles
        List<PathTile.Direction> connections = new ArrayList<>();
        for (Object dir: path.getJSONArray("path").toList()){
            connections.add(Enum.valueOf(PathTile.Direction.class, dir.toString()));
        }

        if (connections.size() == 0) {
            throw new IllegalArgumentException(
                "This path just consists of a single tile, it needs to consist of multiple to form a loop.");
        }

        // load the first position into the orderedPath
        PathTile.Direction first = connections.get(0);
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        orderedPath.add(Pair.with(starting.getX(), starting.getY()));

        int x = starting.getX() + first.getXOffset();
        int y = starting.getY() + first.getYOffset();

        // add all coordinates of the path into the orderedPath
        for (int i = 1; i < connections.size(); i++) {
            orderedPath.add(Pair.with(x, y));
            
            if (y >= height || y < 0 || x >= width || x < 0) {
                throw new IllegalArgumentException("Path goes out of bounds at direction index " + (i - 1) + " (" + connections.get(i - 1) + ")");
            }
            
            PathTile.Direction dir = connections.get(i);
            PathTile tile = new PathTile(new SimpleIntegerProperty(x), new SimpleIntegerProperty(y));
            x += dir.getXOffset();
            y += dir.getYOffset();
            if (orderedPath.contains(Pair.with(x, y)) && !(x == starting.getX() && y == starting.getY())) {
                throw new IllegalArgumentException("Path crosses itself at direction index " + i + " (" + dir + ")");
            }
            onLoad(tile, connections.get(i - 1), dir);
        }
        // we should connect back to the starting point
        if (x != starting.getX() || y != starting.getY()) {
            throw new IllegalArgumentException(String.format(
                    "Path must loop back around on itself, this path doesn't finish where it began, it finishes at %d, %d.",
                    x, y));
        }
        onLoad(starting, connections.get(connections.size() - 1), connections.get(0));
        return orderedPath;
    }

    public void loadGoal(LoopManiaWorld world, JSONObject goal) {
        storeGoals(this.totalGoal, world, goal);
        world.setGoal(totalGoal);
    }

    public void storeGoals(GoalLogic totalGoal, LoopManiaWorld world, JSONObject goal) {
        int value = 0;
        GoalCompsite subgoal = null;
        if (goal.getString("goal").equals("experience")) {
            value = goal.getInt("quantity");
            subgoal = new SubGoalExp(world, value);
            totalGoal.addGoal(subgoal);
        } else if (goal.getString("goal").equals("cycles")) {
            value = goal.getInt("quantity");
            subgoal = new SubGoalCycle(world, value);
            totalGoal.addGoal(subgoal);
        } else if (goal.getString("goal").equals("gold")) {
            value = goal.getInt("quantity");
            subgoal = new SubGoalGold(world, value);
            totalGoal.addGoal(subgoal);
        } else if (goal.getString("goal").equals("bosses")) {
            subgoal = new SubGoalBoss(world);
            totalGoal.addGoal(subgoal);
        } else if (goal.getString("goal").equals("AND")) {
            JSONArray subgoals = goal.getJSONArray("subgoals");
            GoalLogic logic = new GoalAndLogic();
            for (int i = 0; i < subgoals.length(); i++) {
                storeGoals(logic, world, subgoals.getJSONObject(i));
            }
            totalGoal.addGoal(logic);
        } else if (goal.getString("goal").equals("OR")) {
            JSONArray subgoals = goal.getJSONArray("subgoals");
            GoalLogic logic = new GoalOrLogic();
            for (int i = 0; i < subgoals.length(); i++) {
                storeGoals(logic, world, subgoals.getJSONObject(i));
            }
            totalGoal.addGoal(logic);
        }
    }

    public abstract void onLoad(Character character);
    public abstract void onLoad(PathTile pathTile, PathTile.Direction into, PathTile.Direction out);
    public abstract void onLoad(BuildingProperty building);
    public abstract void onLoad(ItemProperty item);

    // TODO Create additional abstract methods for the other entities

}
