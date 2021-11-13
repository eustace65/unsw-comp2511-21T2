package unsw.loopmania;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.codefx.libfx.listener.handle.ListenerHandle;
import org.codefx.libfx.listener.handle.ListenerHandles;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.util.Duration;
import java.util.EnumMap;

import java.io.File;
import java.io.IOException;

/**
 * the draggable types. If you add more draggable types, add an enum value here.
 * This is so we can see what type is being dragged.
 */
enum DRAGGABLE_TYPE {
    CARD, ITEM
}

/**
 * A JavaFX controller for the world.
 *
 * All event handlers and the timeline in JavaFX run on the JavaFX application
 * thread:
 * https://examples.javacodegeeks.com/desktop-java/javafx/javafx-concurrency-example/
 * Note in
 * https://openjfx.io/javadoc/11/javafx.graphics/javafx/application/Application.html
 * under heading "Threading", it specifies animation timelines are run in the
 * application thread. This means that the starter code does not need locks
 * (mutexes) for resources shared between the timeline KeyFrame, and all of the
 * event handlers (including between different event handlers). This will make
 * the game easier for you to implement. However, if you add time-consuming
 * processes to this, the game may lag or become choppy.
 *
 * If you need to implement time-consuming processes, we recommend: using Task
 * https://openjfx.io/javadoc/11/javafx.graphics/javafx/concurrent/Task.html by
 * itself or within a Service
 * https://openjfx.io/javadoc/11/javafx.graphics/javafx/concurrent/Service.html
 *
 * Tasks ensure that any changes to public properties, change notifications for
 * errors or cancellation, event handlers, and states occur on the JavaFX
 * Application thread, so is a better alternative to using a basic Java Thread:
 * https://docs.oracle.com/javafx/2/threads/jfxpub-threads.htm The Service class
 * is used for executing/reusing tasks. You can run tasks without Service,
 * however, if you don't need to reuse it.
 *
 * If you implement time-consuming processes in a Task or thread, you may need
 * to implement locks on resources shared with the application thread (i.e.
 * Timeline KeyFrame and drag Event handlers). You can check whether code is
 * running on the JavaFX application thread by running the helper method
 * printThreadingNotes in this class.
 *
 * NOTE: http://tutorials.jenkov.com/javafx/concurrency.html and
 * https://www.developer.com/design/multithreading-in-javafx/#:~:text=JavaFX%20has%20a%20unique%20set,in%20the%20JavaFX%20Application%20Thread.
 *
 * If you need to delay some code but it is not long-running, consider using
 * Platform.runLater
 * https://openjfx.io/javadoc/11/javafx.graphics/javafx/application/Platform.html#runLater(java.lang.Runnable)
 * This is run on the JavaFX application thread when it has enough time.
 */
public class LoopManiaWorldController {

    /**
     * squares gridpane includes path images, enemies, character, empty grass,
     * buildings
     */
    @FXML
    private GridPane squares;

    /**
     * cards gridpane includes cards and the ground underneath the cards
     */
    @FXML
    private GridPane cards;

    /**
     * anchorPaneRoot is the "background". It is useful since anchorPaneRoot
     * stretches over the entire game world, so we can detect dragging of
     * cards/items over this and accordingly update DragIcon coordinates
     */
    @FXML
    private AnchorPane anchorPaneRoot;

    /**
     * equippedItems gridpane is for equipped items (e.g. swords, shield, axe)
     */
    @FXML
    private GridPane equippedItems;

    @FXML
    private GridPane unequippedInventory;

    @FXML
    private ProgressBar hpProgress;

    @FXML
    private ProgressBar expProgress;

    @FXML
    private ProgressBar superProgressBar;

    @FXML
    private Label powerLabel;

    @FXML
    private Label hpNum;

    @FXML
    private Label cycleNum;

    @FXML
    private Label BattleSlugNumImage;

    @FXML
    private Label BattleZombieNumImage;

    @FXML
    private Label BattleVampireNumImage;

    @FXML
    private Label BattleDoggieNumImage;

    @FXML
    private Label BattleMuskNumImage;

    @FXML
    private Label BattleSlugNum;

    @FXML
    private Label BattleZombieNum;

    @FXML
    private Label BattleVampireNum;

    @FXML
    private Label BattleDoggieNum;

    @FXML
    private Label BattleMuskNum;

    @FXML
    private Label ringNum;

    @FXML
    private Label expNum;

    @FXML
    private Label cycleImage;

    @FXML
    private StackPane layout;

    @FXML
    private StackPane layout2;

    @FXML
    private StackPane layout3;

    @FXML
    private StackPane layout4;

    @FXML
    private StackPane layout5;

    @FXML StackPane superStackPane;

    // all image views including tiles, character, enemies, cards... even though
    // cards in separate gridpane...
    private List<ImageView> entityImages;

    /**
     * when we drag a card/item, the picture for whatever we're dragging is set here
     * and we actually drag this node
     */
    private DragIcon draggedEntity;

    private boolean isPaused;
    private LoopManiaWorld world;

    /**
     * runs the periodic game logic - second-by-second moving of character through
     * maze, as well as enemies, and running of battles
     */
    private Timeline timeline;
    private File PVZaudio;
    private File shopMusic;
    private File battleMusicAutumn;
    private File battleMusicSpring;
    private File battleMusicSummer;
    private File battleMusicWinter;
    private File mainMenuMusic;
    MediaPlayer mainMenuAudioPlayer;
    MediaPlayer shopAudioPlayer;
    MediaPlayer audioPlayer;
    MediaPlayer battleAutumnAudioPlayer;
    MediaPlayer battleWinterAudioPlayer;
    MediaPlayer battleSpringAudioPlayer;
    MediaPlayer battleSummerAudioPlayer;

    private boolean muteMusic;
    
    private Image brilliantBlueNewImage;
    private Image goldImage;
    private Image expImage;
    private Image heartImage;
    private Image swordImage;

    private Image theOneRingImage;

    // private Image basicEnemyImage;
    // private Image vampireImage;
    // private Image zombieImage;

    private Image heroCastleImage;
    private Image vampireCastleCardImage;
    private Image barracksCardImage;
    private Image campfireCardImage;
    private Image towerCardImage;
    private Image trapCard;
    private Image villageCard;
    private Image zombiePitCardImage;

    private Image allyImage;

    @FXML
    private StackPane stackPane;

    @FXML
    private Label goldNum;
    @FXML
    private Label allyNum;
    @FXML
    private Label healthPotionNum;

    @FXML
    private Label superLabel;
    private IntegerProperty allyInNum;
    private IntegerProperty healthPotionInNum;
    private IntegerProperty ringInNum;
    private IntegerProperty battleSlugInNum;
    private IntegerProperty cycleInNum;
    private IntegerProperty battleZombieInNum;
    private IntegerProperty battleVampireInNum;
    private IntegerProperty battleDoggieInNum;
    private IntegerProperty battleMuskInNum;
    private DoubleProperty superProgress;
    private IntegerProperty supercharged;
    // private Experience gold;

    private IntegerProperty hpInNum;

    private IntegerProperty expInNum;
    private IntegerProperty goldInt;
    // private DoubleProperty goldInWorld;
    private DoubleProperty hpInWorld;

    private DoubleProperty expInWorld;
    /**
     * the image currently being dragged, if there is one, otherwise null. Holding
     * the ImageView being dragged allows us to spawn it again in the drop location
     * if appropriate.
     */
    // TODO = it would be a good idea for you to instead replace this with the
    // building/item which should be dropped
    private ImageView currentlyDraggedImage;

    /**
     * null if nothing being dragged, or the type of item being dragged
     */
    private DRAGGABLE_TYPE currentlyDraggedType;

    /**
     * mapping from draggable type enum CARD/TYPE to the event handler triggered
     * when the draggable type is dropped over its appropriate gridpane
     */
    private EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>> gridPaneSetOnDragDropped;
    /**
     * mapping from draggable type enum CARD/TYPE to the event handler triggered
     * when the draggable type is dragged over the background
     */
    private EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>> anchorPaneRootSetOnDragOver;
    /**
     * mapping from draggable type enum CARD/TYPE to the event handler triggered
     * when the draggable type is dropped in the background
     */
    private EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>> anchorPaneRootSetOnDragDropped;
    /**
     * mapping from draggable type enum CARD/TYPE to the event handler triggered
     * when the draggable type is dragged into the boundaries of its appropriate
     * gridpane
     */
    private EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>> gridPaneNodeSetOnDragEntered;
    /**
     * mapping from draggable type enum CARD/TYPE to the event handler triggered
     * when the draggable type is dragged outside of the boundaries of its
     * appropriate gridpane
     */
    private EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>> gridPaneNodeSetOnDragExited;

    /**
     * object handling switching to the main menu
     */
    private MenuSwitcher mainMenuSwitcher;

    private MenuSwitcher gameOverSwitcher;
    private MenuSwitcher gameWinSwitcher;

    private MenuSwitcher heroCastleMenuSwitcher;

    /**
     * @param world           world object loaded from file
     * @param initialEntities the initial JavaFX nodes (ImageViews) which should be
     *                        loaded into the GUI
     */
    public LoopManiaWorldController(LoopManiaWorld world, List<ImageView> initialEntities) {
        this.world = world;
        entityImages = new ArrayList<>(initialEntities);

        vampireCastleCardImage = new Image((new File("src/images/vampire_castle_card.png")).toURI().toString());
        barracksCardImage = new Image((new File("src/images/barracks_card.png")).toURI().toString());
        campfireCardImage = new Image((new File("src/images/campfire_card.png")).toURI().toString());
        towerCardImage = new Image((new File("src/images/tower_card.png")).toURI().toString());
        trapCard = new Image((new File("src/images/trap_card.png")).toURI().toString());
        villageCard = new Image((new File("src/images/village_card.png")).toURI().toString());
        zombiePitCardImage = new Image((new File("src/images/zombie_pit_card.png")).toURI().toString());

        /*
         * basicEnemyImage = new Image((new
         * File("src/images/slug.png")).toURI().toString()); vampireImage = new
         * Image((new File("src/images/vampire.png")).toURI().toString()); zombieImage =
         * new Image((new File("src/images/zombie.png")).toURI().toString());
         */
        heroCastleImage = new Image((new File("src/images/heros_castle.png")).toURI().toString());

        swordImage = new Image((new File("src/images/basic_sword.png")).toURI().toString());

        brilliantBlueNewImage = new Image((new File("src/images/brilliant_blue_new.png")).toURI().toString());
        heartImage = new Image((new File("src/images/heart.png")).toURI().toString());
        theOneRingImage = new Image((new File("src/images/the_one_ring.png")).toURI().toString());
        expImage = new Image((new File("src/images/exp.png")).toURI().toString());
        goldImage = new Image((new File("src/images/gold_pile.png")).toURI().toString());
        allyImage = new Image((new File("src/images/deep_elf_master_archer.png")).toURI().toString());

        currentlyDraggedImage = null;
        currentlyDraggedType = null;

        // initialize them all...
        gridPaneSetOnDragDropped = new EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>>(DRAGGABLE_TYPE.class);
        anchorPaneRootSetOnDragOver = new EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>>(DRAGGABLE_TYPE.class);
        anchorPaneRootSetOnDragDropped = new EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>>(DRAGGABLE_TYPE.class);
        gridPaneNodeSetOnDragEntered = new EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>>(DRAGGABLE_TYPE.class);
        gridPaneNodeSetOnDragExited = new EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>>(DRAGGABLE_TYPE.class);
    }

    @FXML
    public void initialize() {
        // TODO = load more images/entities during initialization
        battleMusicAutumn = new File("src/images/BATTLE_AUTUMN.mp3"); 
        Media audio = new Media(battleMusicAutumn.toURI().toString());
        battleAutumnAudioPlayer = new MediaPlayer(audio);
        battleAutumnAudioPlayer.setVolume(0.2);

        battleMusicWinter = new File("src/images/BATTLE_WINTER.mp3"); 
        audio = new Media(battleMusicWinter.toURI().toString());
        battleWinterAudioPlayer = new MediaPlayer(audio);
        battleWinterAudioPlayer.setVolume(0.2);

        battleMusicSpring = new File("src/images/BATTLE_SPRING.mp3"); 
        audio = new Media(battleMusicSpring.toURI().toString());
        battleSpringAudioPlayer = new MediaPlayer(audio);
        battleSpringAudioPlayer.setVolume(0.2);

        battleMusicSummer = new File("src/images/BATTLE_SUMMER.mp3"); 
        audio = new Media(battleMusicSummer.toURI().toString());
        battleSummerAudioPlayer = new MediaPlayer(audio);
        battleSummerAudioPlayer.setVolume(0.2);

        mainMenuMusic = new File("src/images/mainMenuMusic.mp3"); 
        audio = new Media(mainMenuMusic.toURI().toString());
        mainMenuAudioPlayer = new MediaPlayer(audio);
        if (!getMute()) mainMenuAudioPlayer.setAutoPlay(true);
        mainMenuAudioPlayer.setVolume(0.2);
        if (!getMute())mainMenuAudioPlayer.setAutoPlay(true);
        mainMenuAudioPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        if (!getMute()) mainMenuAudioPlayer.play();
        
        Image pathTilesImage = new Image((new File("src/images/32x32GrassAndDirtPath.png")).toURI().toString());
        Image inventorySlotImage = new Image((new File("src/images/empty_slot.png")).toURI().toString());
        Rectangle2D imagePart = new Rectangle2D(0, 0, 32, 32);


        // Add the ground first so it is below all other entities (inculding all the
        // twists and turns)
        for (int x = 0; x < world.getWidth(); x++) {
            for (int y = 0; y < world.getHeight(); y++) {
                ImageView groundView = new ImageView(pathTilesImage);
                groundView.setViewport(imagePart);
                squares.add(groundView, x, y);
            }
        }

        // load entities loaded from the file in the loader into the squares gridpane
        for (ImageView entity : entityImages) {
            squares.getChildren().add(entity);
        }

        // add the ground underneath the cards
        for (int x = 0; x < world.getWidth(); x++) {
            ImageView groundView = new ImageView(pathTilesImage);
            groundView.setViewport(imagePart);
            cards.add(groundView, x, 0);
        }

        // add the empty slot images for the unequipped inventory
        for (int x = 0; x < LoopManiaWorld.unequippedInventoryWidth; x++) {
            for (int y = 0; y < LoopManiaWorld.unequippedInventoryHeight; y++) {
                ImageView emptySlotView = new ImageView(inventorySlotImage);
                unequippedInventory.add(emptySlotView, x, y);
            }
        }

        // create the draggable icon
        draggedEntity = new DragIcon();
        draggedEntity.setVisible(false);
        draggedEntity.setOpacity(0.7);
        anchorPaneRoot.getChildren().add(draggedEntity);

        BuildingProperty heroCastle = new HeroCastle(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        onLoad((HeroCastle) heroCastle);

        ImageView view = new ImageView(goldImage);

        // Label gold = new Label("Gold: $");

        goldNum = new Label("0");

        goldInt = world.getGold();

        goldNum.textProperty().bind(goldInt.asString());
        goldNum.setTextFill(Color.ORANGE);
        goldNum.setFont(new Font("Cambria", 40));

        layout.getChildren().add(view);
        StackPane.setAlignment(view, Pos.CENTER_LEFT);
        layout.getChildren().add(goldNum);
        StackPane.setAlignment(goldNum, Pos.CENTER_RIGHT);

        ImageView allyView = new ImageView(allyImage);
        allyInNum = world.getAllyNum();
        allyNum = new Label("0");
        allyNum.textProperty().bind(allyInNum.asString());
        allyNum.setTextFill(Color.GRAY);
        allyNum.setFont(new Font("Cambria", 40));

        layout.getChildren().add(allyView);
        StackPane.setAlignment(allyView, Pos.TOP_LEFT);
        layout.getChildren().add(allyNum);
        StackPane.setAlignment(allyNum, Pos.TOP_RIGHT);

        ImageView healthPotionView = new ImageView(brilliantBlueNewImage);
        healthPotionNum = new Label("0");
        healthPotionInNum = world.getHealthPotionNum();
        healthPotionNum.textProperty().bind(healthPotionInNum.asString());
        healthPotionNum.setTextFill(Color.BLUE);

        healthPotionNum.setFont(new Font("Cambria", 40));
        layout2.getChildren().add(healthPotionView);
        StackPane.setAlignment(healthPotionView, Pos.TOP_LEFT);
        layout2.getChildren().add(healthPotionNum);
        StackPane.setAlignment(healthPotionNum, Pos.TOP_RIGHT);

        ImageView ringView = new ImageView(theOneRingImage);
        ringNum = new Label("0");
        ringInNum = world.getRing();
        ringNum.textProperty().bind(ringInNum.asString());
        ringNum.setTextFill(Color.GREEN);
        ringNum.setFont(new Font("Cambria", 40));
        layout3.getChildren().add(ringView);
        StackPane.setAlignment(ringView, Pos.TOP_LEFT);
        layout3.getChildren().add(ringNum);
        StackPane.setAlignment(ringNum, Pos.TOP_RIGHT);

        cycleImage = new Label("Cycle");
        cycleNum = new Label("0");
        cycleInNum = world.getCylceNum();
        cycleNum.textProperty().bind(cycleInNum.asString());
        cycleNum.setTextFill(Color.PURPLE);
        cycleNum.setFont(new Font("Cambria", 40));

        cycleImage.setFont(new Font("Cambria", 30));
        cycleImage.setTextFill(Color.BLACK);
        layout2.getChildren().add(cycleImage);
        StackPane.setAlignment(cycleImage, Pos.BOTTOM_LEFT);
        layout2.getChildren().add(cycleNum);
        StackPane.setAlignment(cycleNum, Pos.BOTTOM_RIGHT);

        BattleSlugNumImage = new Label("Slug");
        BattleSlugNum = new Label("0");
        battleSlugInNum = world.getBattleSlugNum();
        BattleSlugNum.textProperty().bind(battleSlugInNum.asString());
        BattleSlugNum.setTextFill(Color.PURPLE);
        BattleSlugNum.setFont(new Font("Cambria", 40));

        BattleSlugNumImage.setFont(new Font("Cambria", 30));
        BattleSlugNumImage.setTextFill(Color.BLACK);
        layout4.getChildren().add(BattleSlugNumImage);
        StackPane.setAlignment(BattleSlugNumImage, Pos.TOP_LEFT);
        layout4.getChildren().add(BattleSlugNum);
        StackPane.setAlignment(BattleSlugNum, Pos.TOP_RIGHT);


        BattleZombieNumImage = new Label("Zombie");
        BattleZombieNum = new Label("0");
        battleZombieInNum = world.getBattleZombieNum();
        BattleZombieNum.textProperty().bind(battleZombieInNum.asString());
        BattleZombieNum.setTextFill(Color.PURPLE);
        BattleZombieNum.setFont(new Font("Cambria", 40));

        BattleZombieNumImage.setFont(new Font("Cambria", 30));
        BattleZombieNumImage.setTextFill(Color.BLACK);
        layout4.getChildren().add(BattleZombieNumImage);
        StackPane.setAlignment(BattleZombieNumImage, Pos.CENTER_LEFT);
        layout4.getChildren().add(BattleZombieNum);
        StackPane.setAlignment(BattleZombieNum, Pos.CENTER_RIGHT);


        BattleVampireNumImage = new Label("Vampire");
        BattleVampireNum = new Label("0");
        battleVampireInNum = world.getBattleVampireNum();
        BattleVampireNum.textProperty().bind(battleVampireInNum.asString());
        BattleVampireNum.setTextFill(Color.PURPLE);
        BattleVampireNum.setFont(new Font("Cambria", 40));

        BattleVampireNumImage.setFont(new Font("Cambria", 30));
        BattleVampireNumImage.setTextFill(Color.BLACK);
        layout4.getChildren().add(BattleVampireNumImage);
        StackPane.setAlignment(BattleVampireNumImage, Pos.BOTTOM_LEFT);
        layout4.getChildren().add(BattleVampireNum);
        StackPane.setAlignment(BattleVampireNum, Pos.BOTTOM_RIGHT);


        BattleDoggieNumImage = new Label("Doggie");
        BattleDoggieNum = new Label("0");
        battleDoggieInNum = world.getBattleDoggieNum();
        BattleDoggieNum.textProperty().bind(battleDoggieInNum.asString());
        BattleDoggieNum.setTextFill(Color.PURPLE);
        BattleDoggieNum.setFont(new Font("Cambria", 40));

        BattleDoggieNumImage.setFont(new Font("Cambria", 30));
        BattleDoggieNumImage.setTextFill(Color.BLACK);
        layout5.getChildren().add(BattleDoggieNumImage);
        StackPane.setAlignment(BattleDoggieNumImage, Pos.TOP_LEFT);
        layout5.getChildren().add(BattleDoggieNum);
        StackPane.setAlignment(BattleDoggieNum, Pos.TOP_RIGHT);



        BattleMuskNumImage = new Label("Musk");
        BattleMuskNum = new Label("0");
        battleMuskInNum = world.getBattleMuskNum();
        BattleMuskNum.textProperty().bind(battleMuskInNum.asString());
        BattleMuskNum.setTextFill(Color.PURPLE);
        BattleMuskNum.setFont(new Font("Cambria", 40));

        BattleMuskNumImage.setFont(new Font("Cambria", 30));
        BattleMuskNumImage.setTextFill(Color.BLACK);
        layout5.getChildren().add(BattleMuskNumImage);
        StackPane.setAlignment(BattleMuskNumImage, Pos.CENTER_LEFT);
        layout5.getChildren().add(BattleMuskNum);
        StackPane.setAlignment(BattleMuskNum, Pos.CENTER_RIGHT);


        ImageView heartView = new ImageView(heartImage);
        hpNum = new Label("0");
        hpInNum = world.getHpInt();
        hpNum.textProperty().bind(hpInNum.asString());
        hpNum.setTextFill(Color.RED);
        
        hpProgress = new ProgressBar();
        hpInWorld = world.getHp();
        hpProgress.progressProperty().bind(hpInWorld);


        layout.getChildren().add(hpProgress);
        StackPane.setAlignment(hpProgress, Pos.BOTTOM_RIGHT);
        layout.getChildren().add(heartView);
        StackPane.setAlignment(heartView, Pos.BOTTOM_LEFT);
        layout.getChildren().add(hpNum);
        StackPane.setAlignment(hpNum, Pos.BOTTOM_RIGHT);

        ImageView expView = new ImageView(expImage);
        expProgress = new ProgressBar();
        expInWorld = world.getExp();
        expProgress.progressProperty().bind(expInWorld);

        expNum = new Label("0");
        expInNum = world.getExpInt();
        expNum.textProperty().bind(expInNum.asString());
        expNum.setTextFill(Color.GOLD);

        layout2.getChildren().add(expProgress);
        StackPane.setAlignment(expProgress, Pos.CENTER_RIGHT);
        layout2.getChildren().add(expView);
        StackPane.setAlignment(expView, Pos.CENTER_LEFT);

        layout2.getChildren().add(expNum);
        StackPane.setAlignment(expNum, Pos.CENTER_RIGHT);

        
        superProgressBar = new ProgressBar();
        superProgress = world.getSuperPowerProgress();
        superProgressBar.progressProperty().bind(superProgress);
        superStackPane.getChildren().add(superProgressBar);
        StackPane.setAlignment(superProgressBar, Pos.BOTTOM_CENTER);
        
        powerLabel.setTextFill(Color.RED);
        powerLabel.setFont(new Font("Cambria", 30));
        


        

    }

    public GridPane getUnequippedInventory() {
        return this.unequippedInventory;
    }

    public void setMute(boolean muteMusic) {
        this.muteMusic = muteMusic;
    }

    public boolean getMute() {
        return this.muteMusic;
    }


    /**
     * create and run the timer
     */
    public void startTimer() {
        // TODO = handle more aspects of the behaviour required by the specification
        System.out.println("starting timer");
        mainMenuAudioPlayer.pause();


        PVZaudio = new File("src/images/audio.mp3");
        Media audio = new Media(PVZaudio.toURI().toString());
        audioPlayer = new MediaPlayer(audio);
        if (!getMute()) audioPlayer.setAutoPlay(true);
        audioPlayer.setVolume(0.2);
        audioPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        // trigger adding code to process main game logic to queue. JavaFX will target
        // framerate of 0.3 seconds
        timeline = new Timeline(new KeyFrame(Duration.seconds(0.3), event -> {

            world.runTickMoves();
            int result = world.getCycle() % 4;
            if (result == 0) {
                if (world.getCharacter().getInBattle()) {
                    audioPlayer.pause();

                    if (!getMute())battleAutumnAudioPlayer.setAutoPlay(true);
                    if (!getMute())battleAutumnAudioPlayer.setAutoPlay(true);
                    battleAutumnAudioPlayer.setCycleCount(MediaPlayer.INDEFINITE);
                    if (!getMute()) battleAutumnAudioPlayer.play();
                } else {
                    battleAutumnAudioPlayer.stop();
                    if (!getMute()) audioPlayer.play();
                    battleWinterAudioPlayer.stop();
                    battleSpringAudioPlayer.stop();
                    battleSummerAudioPlayer.stop();
                    if (!getMute())audioPlayer.play();
                }
            } else if (result == 1) {
                if (world.getCharacter().getInBattle()) {
                    audioPlayer.pause();

                    if (!getMute()) battleWinterAudioPlayer.setAutoPlay(true);
                    if (!getMute())battleWinterAudioPlayer.setAutoPlay(true);
                    battleWinterAudioPlayer.setCycleCount(MediaPlayer.INDEFINITE);
                    if (!getMute()) battleWinterAudioPlayer.play();
                } else {
                    battleAutumnAudioPlayer.stop();
                    battleWinterAudioPlayer.stop();
                    if (!getMute()) audioPlayer.play();
                    battleSpringAudioPlayer.stop();
                    battleSummerAudioPlayer.stop();
                    if (!getMute())audioPlayer.play();
                }
            } else if (result == 2) {
                if (world.getCharacter().getInBattle()) {
                    audioPlayer.pause();
                    
                    if (!getMute()) battleSpringAudioPlayer.setAutoPlay(true);
                    battleSpringAudioPlayer.setCycleCount(MediaPlayer.INDEFINITE);
                    if (!getMute()) battleSpringAudioPlayer.play();
                } else {
                    battleAutumnAudioPlayer.stop();
                    battleWinterAudioPlayer.stop();
                    battleSpringAudioPlayer.stop();
                    if (!getMute()) audioPlayer.play();
                    battleSummerAudioPlayer.stop();
                    if (!getMute())audioPlayer.play();
                }
            } else {
                if (world.getCharacter().getInBattle()) {
                    audioPlayer.pause();
                    
                    if (!getMute()) battleSummerAudioPlayer.setAutoPlay(true);
                    battleSummerAudioPlayer.setCycleCount(MediaPlayer.INDEFINITE);
                    if (!getMute()) battleSummerAudioPlayer.play();
                } else {
                    battleAutumnAudioPlayer.stop();
                    battleWinterAudioPlayer.stop();
                    battleSpringAudioPlayer.stop();
                    battleSummerAudioPlayer.stop();
                    if (!getMute()) audioPlayer.play();
                }
            }
            
            if (world.isShopTime()) {
                try {
                    
                    switchToShop();
                    audioPlayer.pause();
                    
                    shopMusic = new File("src/images/shopMusic.mp3");
                    Media stopAudio = new Media(shopMusic.toURI().toString());
                    shopAudioPlayer = new MediaPlayer(stopAudio);
                    if (!getMute()) shopAudioPlayer.setAutoPlay(true);
                    shopAudioPlayer.setVolume(0.1);
                    
                    shopAudioPlayer.setCycleCount(MediaPlayer.INDEFINITE);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //allyInNum.set(world.getAllies().size());
            healthPotionInNum.set(world.getHealthPotion());
            cycleInNum.set(world.getCycle());
            battleSlugInNum.set(world.getBattleSlugNum().get());
            battleZombieInNum.set(world.getBattleZombieNum().get());
            battleVampireInNum.set(world.getBattleVampireNum().get());
            battleDoggieInNum.set(world.getBattleDoggieNum().get());
            battleMuskInNum.set(world.getBattleMuskNum().get());
            List<ItemProperty> items = world.possiblySpawnItems();
            for (ItemProperty item : items) {
                onLoad(item);
            }
            List<EnemyProperty> defeatedEnemies = world.runBattles();
            for (EnemyProperty e : defeatedEnemies) {
                reactToEnemyDefeat(e);
            }
            List<EnemyProperty> newEnemies = world.possiblySpawnEnemies();
            for (EnemyProperty newEnemy : newEnemies) {
                onLoad(newEnemy);
            }
            printThreadingNotes("HANDLED TIMER");
            checkGameState();
            System.out.println(
                    world.getExpInt().get() + " gold:" + world.getGold().get() + " cycle:" + world.getCylceNum().get());
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    /**
     * pause the execution of the game loop the human player can still drag and drop
     * items during the game pause
     */
    public void pause() {
        isPaused = true;
        System.out.println("pausing");
        timeline.stop();
    }

    public void terminate() {
        pause();
    }

    private void checkGameState() {
        if (world.isGameOver()) {
            System.out.println("oops dead");
            terminate();
            gameOverSwitcher.switchMenu();
        }
        if (world.isGameWin()) {
            System.out.println("you won");
            terminate();
            gameWinSwitcher.switchMenu();
        }
    }

    /**
     * pair the entity an view so that the view copies the movements of the entity.
     * add view to list of entity images
     *
     * @param entity backend entity to be paired with view
     * @param view   frontend imageview to be paired with backend entity
     */
    private void addEntity(Entity entity, ImageView view) {
        trackPosition(entity, view);
        entityImages.add(view);
    }

    /**
     * load a card from the world, and pair it with an image in the GUI
     */
    private void loadVampireCard() {
        // TODO = load more types of card
        checkCardEntity();
        VampireCastleCard vampireCastleCard = world.loadVampireCard();
        onLoad(vampireCastleCard);
    }

    private void loadCampfireCard() {
        // TODO = load more types of card
        checkCardEntity();
        CampfireCard campfireCard = world.loadCampfireCard();
        onLoad(campfireCard);
    }

    private void loadTowerCard() {
        checkCardEntity();
        // TODO = load more types of card
        TowerCard towerCard = world.loadTowerCard();
        onLoad(towerCard);
    }

    private void loadTrapCard() {
        // TODO = load more types of card
        checkCardEntity();
        TrapCard trapCard = world.loadTrapCard();
        onLoad(trapCard);
    }

    private void loadVillageCard() {
        // TODO = load more types of card
        checkCardEntity();
        VillageCard villageCard = world.loadVillageCard();
        onLoad(villageCard);
    }

    private void loadBarracksCard() {
        // TODO = load more types of card
        checkCardEntity();
        BarracksCard barracksCard = world.loadBarracksCard();
        onLoad(barracksCard);
    }

    public void loadZombiePitCard() {
        checkCardEntity();
        ZombiePitCard zombiePitCard = world.loadZombiePitCard();
        onLoad(zombiePitCard);
    }

    public void loadAnduril() {
        Anduril anduril = (Anduril) world.addUnequippedItem(ItemType.ANDURIL);
        onLoad(anduril);
    }

    public void loadTreeStump() {
        TreeStump treeStump = (TreeStump) world.addUnequippedItem(ItemType.TREESTUMP);
        onLoad(treeStump);
    }

    /**
     * load an item into the world and pair it with an image in the GUI
     */
    private void loadSword() {
        // TODO = load more types of weapon
        // start by getting first available coordinates
        Sword sword = (Sword) world.addUnequippedItem(ItemType.SWORD);
        //world.addRareItem(sword);
        onLoad(sword);
    } 

    private void loadShield() {
        Shield shield = (Shield) world.addUnequippedItem(ItemType.SHIELD);
        onLoad(shield);
    }

    private void loadHelmet() {
        Helmet helmet = (Helmet) world.addUnequippedItem(ItemType.HELMET);
        onLoad(helmet);
    }

    private void loadStaff() {
        Staff staff = (Staff) world.addUnequippedItem(ItemType.STAFF);
        onLoad(staff);
    }

    private void loadStake() {
        Stake stake = (Stake) world.addUnequippedItem(ItemType.STAKE);
        onLoad(stake);
    }

    private void loadArmour() {
        Armour armour = (Armour)world.addUnequippedItem(ItemType.ARMOUR);
        onLoad(armour);
    }

    /**
     * run GUI events after an enemy is defeated, such as spawning
     * items/experience/gold
     *
     * @param enemy defeated enemy for which we should react to the death of
     */
    private void reactToEnemyDefeat(EnemyProperty enemy) {
        // react to character defeating an enemy
        // in starter code, spawning extra card/weapon...
        // TODO = provide different benefits to defeating the enemy based on the type of
        // enemy

        Random rand = new Random();
        int result = rand.nextInt(2);
        world.addGold(enemy.getGold());
        if (enemy.getType().equals("Doggie")) {
            world.addDoggieCoin(rand.nextInt(5));
        }
        switch(result) {
            case 0:
                generateCard();
                break;
            case 1:
                generateItem();
                break;
            default:
                break;
        }

    }

    /**
     * generates a random card from the available cards
     */
    public void generateCard() {
        int totalCards = 7;
        Random rand = new Random();
        int result = rand.nextInt(1000) % totalCards;

        switch (result) {
            case 0:
                loadVampireCard();
                break;
            case 1:
                loadCampfireCard();
                break;
            case 2:
                loadTowerCard();
                break;
            case 3:
                loadTrapCard();
                break;
            case 4:
                loadBarracksCard();
                break;
            case 5:
                loadVillageCard();
                break;
            case 6:
                loadZombiePitCard();
                break;
            default:
                return;
        }

    }

    /**
     * generates a random item from the available rewards
     */
    public void generateItem() {
        int totalRewards = 7;
        Random rand = new Random();
        int result = rand.nextInt(1000) % totalRewards;
        
        switch (result) {
            case 0:
                loadSword();
                break;
            case 1:
                loadArmour();
                break;
            case 2:
                loadShield();
                break;
            case 3:
                loadHelmet();
                break;
            case 4:
                loadStaff();
                break;
            case 5:
                loadStake();
                break;
            case 6:
                result = rand.nextInt(3);
                if (result == 0) loadAnduril();
                else if (result == 1) loadTreeStump();
                break;
            default:
                return;
        }
    }

    /**
     * load a vampire castle card into the GUI. Particularly, we must connect to the
     * drag detection event handler, and load the image into the cards GridPane.
     *
     * @param vampireCastleCard
     */
    private void onLoad(VampireCastleCard vampireCastleCard) {
        ImageView view = new ImageView(vampireCastleCardImage);

        // FROM
        // https://stackoverflow.com/questions/41088095/javafx-drag-and-drop-to-gridpane
        // note target setOnDragOver and setOnDragEntered defined in initialize method
        addDragEventHandlers(view, DRAGGABLE_TYPE.CARD, cards, squares);

        addEntity(vampireCastleCard, view);
        cards.getChildren().add(view);
    }

    private void onLoad(CampfireCard campfireCard) {
        ImageView view = new ImageView(campfireCardImage);

        // FROM
        // https://stackoverflow.com/questions/41088095/javafx-drag-and-drop-to-gridpane
        // note target setOnDragOver and setOnDragEntered defined in initialize method
        addDragEventHandlers(view, DRAGGABLE_TYPE.CARD, cards, squares);

        addEntity(campfireCard, view);
        cards.getChildren().add(view);
    }

    private void onLoad(VillageCard villageCardIm) {
        ImageView view = new ImageView(villageCard);

        // FROM
        // https://stackoverflow.com/questions/41088095/javafx-drag-and-drop-to-gridpane
        // note target setOnDragOver and setOnDragEntered defined in initialize method
        addDragEventHandlers(view, DRAGGABLE_TYPE.CARD, cards, squares);

        addEntity(villageCardIm, view);
        cards.getChildren().add(view);
    }

    private void onLoad(TowerCard towerCard) {
        ImageView view = new ImageView(towerCardImage);

        // FROM
        // https://stackoverflow.com/questions/41088095/javafx-drag-and-drop-to-gridpane
        // note target setOnDragOver and setOnDragEntered defined in initialize method
        addDragEventHandlers(view, DRAGGABLE_TYPE.CARD, cards, squares);

        addEntity(towerCard, view);
        cards.getChildren().add(view);
    }

    private void onLoad(TrapCard trapCardIm) {
        ImageView view = new ImageView(trapCard);

        // FROM
        // https://stackoverflow.com/questions/41088095/javafx-drag-and-drop-to-gridpane
        // note target setOnDragOver and setOnDragEntered defined in initialize method
        addDragEventHandlers(view, DRAGGABLE_TYPE.CARD, cards, squares);

        addEntity(trapCardIm, view);
        cards.getChildren().add(view);
    }

    private void onLoad(BarracksCard barracksCard) {
        ImageView view = new ImageView(barracksCardImage);

        // FROM
        // https://stackoverflow.com/questions/41088095/javafx-drag-and-drop-to-gridpane
        // note target setOnDragOver and setOnDragEntered defined in initialize method
        addDragEventHandlers(view, DRAGGABLE_TYPE.CARD, cards, squares);

        addEntity(barracksCard, view);
        cards.getChildren().add(view);
    }

    private void onLoad(ZombiePitCard zombiePitCard) {
        ImageView view = new ImageView(zombiePitCardImage);

        // FROM
        // https://stackoverflow.com/questions/41088095/javafx-drag-and-drop-to-gridpane
        // note target setOnDragOver and setOnDragEntered defined in initialize method
        addDragEventHandlers(view, DRAGGABLE_TYPE.CARD, cards, squares);

        addEntity(zombiePitCard, view);
        cards.getChildren().add(view);
    }


    /**
     * loads an item into the GUI depending on the ItemType given connects the item
     * to the drag detection event handler and places it in unequippedInventory if
     * it is an equippable item. Otherwise item will be loaded onto the map due to
     * our implementation.
     *
     * @param item
     */
    public void onLoad(ItemProperty item) {
        ImageView view = item.onLoadItems();

        if (item.getType() == ItemType.OTHER || item.getType() == ItemType.HEALTHPOTION) {
            squares.getChildren().add(view);
            addEntity(item, view);
        }
        else {
            addDragEventHandlers(view, DRAGGABLE_TYPE.ITEM, unequippedInventory, equippedItems);
            addEntity(item, view);
            unequippedInventory.getChildren().add(view);
        }
    }

    public void unLoad(ItemProperty item) {
        ImageView view = item.onLoadItems();
        unequippedInventory.getChildren().remove(view);
        
    }

    /**
     * load an enemy into the GUI
     *
     * @param enemy
     */
    private void onLoad(EnemyProperty enemy) {
        ImageView view;
        view = enemy.onLoadEnemy();
        // ImageView view = new ImageView(basicEnemyImage);
        addEntity(enemy, view);
        squares.getChildren().add(view);
    }

    /**
     * load a building into the GUI
     *
     * @param building
     */

    private void onLoad(HeroCastle building) {
        ImageView view = new ImageView(heroCastleImage);
        addEntity(building, view);
        squares.getChildren().add(view);
    }

    /**
     * add drag event handlers for dropping into gridpanes, dragging over the
     * background, dropping over the background. These are not attached to invidual
     * items such as swords/cards.
     *
     * @param draggableType  the type being dragged - card or item
     * @param sourceGridPane the gridpane being dragged from
     * @param targetGridPane the gridpane the human player should be dragging to
     *                       (but we of course cannot guarantee they will do so)
     */
    private void buildNonEntityDragHandlers(DRAGGABLE_TYPE draggableType, GridPane sourceGridPane,
            GridPane targetGridPane) {
        // TODO = be more selective about where something can be dropped
        // for example, in the specification, villages can only be dropped on path,
        // whilst vampire castles cannot go on the path

        gridPaneSetOnDragDropped.put(draggableType, new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                // TODO = for being more selective about where something can be dropped,
                // consider applying additional if-statement logic
                /*
                 * you might want to design the application so dropping at an invalid location
                 * drops at the most recent valid location hovered over, or simply allow the
                 * card/item to return to its slot (the latter is easier, as you won't have to
                 * store the last valid drop location!)
                 */
                if (currentlyDraggedType == draggableType) {
                    // problem = event is drop completed is false when should be true...
                    // https://bugs.openjdk.java.net/browse/JDK-8117019
                    // putting drop completed at start not making complete on VLAB...

                    // Data dropped
                    // If there is an image on the dragboard, read it and use it
                    Dragboard db = event.getDragboard();
                    Node node = event.getPickResult().getIntersectedNode();
                    if (node != targetGridPane && db.hasImage()) {

                        Integer cIndex = GridPane.getColumnIndex(node);
                        Integer rIndex = GridPane.getRowIndex(node);
                        int x = cIndex == null ? 0 : cIndex;
                        int y = rIndex == null ? 0 : rIndex;
                        // Places at 0,0 - will need to take coordinates once that is implemented
                        ImageView image = new ImageView(db.getImage());

                        int nodeX = GridPane.getColumnIndex(currentlyDraggedImage);
                        int nodeY = GridPane.getRowIndex(currentlyDraggedImage);
                        switch (draggableType) {
                            case CARD:
                                removeDraggableDragEventHandlers(draggableType, targetGridPane);
                                // TODO = spawn a building here of different types

                                BuildingProperty b = world.convertCardToBuildingByCoordinates(nodeX, nodeY, x, y);
                                /*
                                 * if (b instanceof VampireCastleBuilding) onLoad((VampireCastleBuilding)b); if
                                 * (b instanceof Campfire) onLoad((Campfire)b); if (b instanceof Tower)
                                 * onLoad((Tower)b); if (b instanceof Trap) onLoad((Trap)b); if (b instanceof
                                 * Village) onLoad((Village)b); if (b instanceof ZombiePit)
                                 * onLoad((ZombiePit)b); if (b instanceof Barracks) onLoad((Barracks)b);
                                 */
                                if (b != null) {

                                    ImageView view = b.onLoadBuilding();
                                    addEntity(b, view);
                                    squares.getChildren().add(view);
                                }
                                // VampireCastleBuilding newBuilding =
                                // (VampireCastleBuilding)world.convertCardToBuildingByCoordinates(nodeX, nodeY,
                                // x, y);
                                // Campfire campfire = (Campfire)world.convertCardToBuildingByCoordinates(nodeX,
                                // nodeY, x, y);
                                // onLoad(newBuilding);
                                // onLoad(campfire);
                                break;
                            case ITEM:
                                removeDraggableDragEventHandlers(draggableType, targetGridPane);
                                // TODO = spawn an item in the new location. The above code for spawning a
                                // building will help, it is very similar
                                // world.equipItem()
                                // TODO = fix for more item types/slots
                                ItemProperty item = (ItemProperty) world.equipItemByCoordinates(nodeX, nodeY);
                                // Helmet helmet = (Helmet)world.equipItemByCoordinates(x, y);
                                targetGridPane.add(image, item.getSlot(), y, 1, 1);
                                // onLoad(item);
                                // onLoad(helmet);
                                // removeItemByCoordinates(nodeX, nodeY);
                                // Helmet helmet = (Helmet)world.equipItemByCoordinates(x, y);

                                // targetGridPane.add(image, x, y, 1, 1);
                                break;
                            default:
                                break;
                        }

                        draggedEntity.setVisible(false);
                        draggedEntity.setMouseTransparent(false);
                        // remove drag event handlers before setting currently dragged image to null
                        currentlyDraggedImage = null;
                        currentlyDraggedType = null;
                        printThreadingNotes("DRAG DROPPED ON GRIDPANE HANDLED");
                    }
                }
                event.setDropCompleted(true);
                // consuming prevents the propagation of the event to the anchorPaneRoot (as a
                // sub-node of anchorPaneRoot, GridPane is prioritized)
                // https://openjfx.io/javadoc/11/javafx.base/javafx/event/Event.html#consume()
                // to understand this in full detail, ask your tutor or read
                // https://docs.oracle.com/javase/8/javafx/events-tutorial/processing.htm
                event.consume();
            }
        });

        // this doesn't fire when we drag over GridPane because in the event handler for
        // dragging over GridPanes, we consume the event
        anchorPaneRootSetOnDragOver.put(draggableType, new EventHandler<DragEvent>() {
            // https://github.com/joelgraff/java_fx_node_link_demo/blob/master/Draggable_Node/DraggableNodeDemo/src/application/RootLayout.java#L110
            @Override
            public void handle(DragEvent event) {
                if (currentlyDraggedType == draggableType) {
                    if (event.getGestureSource() != anchorPaneRoot && event.getDragboard().hasImage()) {
                        event.acceptTransferModes(TransferMode.MOVE);
                    }
                }
                if (currentlyDraggedType != null) {
                    draggedEntity.relocateToPoint(new Point2D(event.getSceneX(), event.getSceneY()));
                }
                event.consume();
            }
        });

        // this doesn't fire when we drop over GridPane because in the event handler for
        // dropping over GridPanes, we consume the event
        anchorPaneRootSetOnDragDropped.put(draggableType, new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                if (currentlyDraggedType == draggableType) {
                    // Data dropped
                    // If there is an image on the dragboard, read it and use it
                    Dragboard db = event.getDragboard();
                    Node node = event.getPickResult().getIntersectedNode();
                    if (node != anchorPaneRoot && db.hasImage()) {
                        // Places at 0,0 - will need to take coordinates once that is implemented
                        currentlyDraggedImage.setVisible(true);
                        draggedEntity.setVisible(false);
                        draggedEntity.setMouseTransparent(false);
                        // remove drag event handlers before setting currently dragged image to null
                        removeDraggableDragEventHandlers(draggableType, targetGridPane);

                        currentlyDraggedImage = null;
                        currentlyDraggedType = null;
                    }
                }
                // let the source know whether the image was successfully transferred and used
                event.setDropCompleted(true);
                event.consume();
            }
        });
    }

    /**
     * remove the card from the world, and spawn and return a building instead where
     * the card was dropped
     *
     * @param cardNodeX     the x coordinate of the card which was dragged, from 0
     *                      to width-1
     * @param cardNodeY     the y coordinate of the card which was dragged (in
     *                      starter code this is 0 as only 1 row of cards)
     * @param buildingNodeX the x coordinate of the drop location for the card,
     *                      where the building will spawn, from 0 to width-1
     * @param buildingNodeY the y coordinate of the drop location for the card,
     *                      where the building will spawn, from 0 to height-1
     * @return building entity returned from the world
     */
    /*
     * private VampireCastleBuilding convertCardToBuildingByCoordinates(int
     * cardNodeX, int cardNodeY, int buildingNodeX, int buildingNodeY) { return
     * (VampireCastleBuilding)world.convertCardToBuildingByCoordinates(cardNodeX,
     * cardNodeY, buildingNodeX, buildingNodeY); }
     */

    /**
     * remove an item from the unequipped inventory by its x and y coordinates in
     * the unequipped inventory gridpane
     *
     * @param nodeX x coordinate from 0 to unequippedInventoryWidth-1
     * @param nodeY y coordinate from 0 to unequippedInventoryHeight-1
     */
    private void removeItemByCoordinates(int nodeX, int nodeY) {
        world.removeUnequippedInventoryItemByCoordinates(nodeX, nodeY);
    }

    /**
     * add drag event handlers to an ImageView
     *
     * @param view           the view to attach drag event handlers to
     * @param draggableType  the type of item being dragged - card or item
     * @param sourceGridPane the relevant gridpane from which the entity would be
     *                       dragged
     * @param targetGridPane the relevant gridpane to which the entity would be
     *                       dragged to
     */
    private void addDragEventHandlers(ImageView view, DRAGGABLE_TYPE draggableType, GridPane sourceGridPane,
            GridPane targetGridPane) {
        view.setOnDragDetected(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                currentlyDraggedImage = view; // set image currently being dragged, so squares setOnDragEntered can
                                              // detect it...
                currentlyDraggedType = draggableType;
                // Drag was detected, start drap-and-drop gesture
                // Allow any transfer node
                Dragboard db = view.startDragAndDrop(TransferMode.MOVE);

                // Put ImageView on dragboard
                ClipboardContent cbContent = new ClipboardContent();
                cbContent.putImage(view.getImage());
                db.setContent(cbContent);
                view.setVisible(false);

                buildNonEntityDragHandlers(draggableType, sourceGridPane, targetGridPane);

                draggedEntity.relocateToPoint(new Point2D(event.getSceneX(), event.getSceneY()));
                switch (draggableType) {
                    case CARD:
                        draggedEntity.setImage(view.getImage());
                        break;
                    case ITEM:
                        draggedEntity.setImage(view.getImage());
                        break;
                    default:
                        break;
                }

                draggedEntity.setVisible(true);
                draggedEntity.setMouseTransparent(true);
                draggedEntity.toFront();

                // IMPORTANT!!!
                // to be able to remove event handlers, need to use addEventHandler
                // https://stackoverflow.com/a/67283792
                targetGridPane.addEventHandler(DragEvent.DRAG_DROPPED, gridPaneSetOnDragDropped.get(draggableType));
                anchorPaneRoot.addEventHandler(DragEvent.DRAG_OVER, anchorPaneRootSetOnDragOver.get(draggableType));
                anchorPaneRoot.addEventHandler(DragEvent.DRAG_DROPPED,
                        anchorPaneRootSetOnDragDropped.get(draggableType));

                for (Node n : targetGridPane.getChildren()) {
                    // events for entering and exiting are attached to squares children because that
                    // impacts opacity change
                    // these do not affect visibility of original image...
                    // https://stackoverflow.com/questions/41088095/javafx-drag-and-drop-to-gridpane
                    gridPaneNodeSetOnDragEntered.put(draggableType, new EventHandler<DragEvent>() {
                        // TODO = be more selective about whether highlighting changes - if it cannot be
                        // dropped in the location, the location shouldn't be highlighted!
                        public void handle(DragEvent event) {
                            if (currentlyDraggedType == draggableType) {
                                // The drag-and-drop gesture entered the target
                                // show the user that it is an actual gesture target
                                if (event.getGestureSource() != n && event.getDragboard().hasImage()) {
                                    n.setOpacity(0.7);
                                }
                            }
                            event.consume();
                        }
                    });
                    gridPaneNodeSetOnDragExited.put(draggableType, new EventHandler<DragEvent>() {
                        // TODO = since being more selective about whether highlighting changes, you
                        // could program the game so if the new highlight location is invalid the
                        // highlighting doesn't change, or leave this as-is
                        public void handle(DragEvent event) {
                            if (currentlyDraggedType == draggableType) {
                                n.setOpacity(1);
                            }

                            event.consume();
                        }
                    });
                    n.addEventHandler(DragEvent.DRAG_ENTERED, gridPaneNodeSetOnDragEntered.get(draggableType));
                    n.addEventHandler(DragEvent.DRAG_EXITED, gridPaneNodeSetOnDragExited.get(draggableType));
                }
                event.consume();
            }

        });
    }

    /**
     * remove drag event handlers so that we don't process redundant events this is
     * particularly important for slower machines such as over VLAB.
     *
     * @param draggableType  either cards, or items in unequipped inventory
     * @param targetGridPane the gridpane to remove the drag event handlers from
     */
    private void removeDraggableDragEventHandlers(DRAGGABLE_TYPE draggableType, GridPane targetGridPane) {
        // remove event handlers from nodes in children squares, from anchorPaneRoot,
        // and squares
        targetGridPane.removeEventHandler(DragEvent.DRAG_DROPPED, gridPaneSetOnDragDropped.get(draggableType));

        anchorPaneRoot.removeEventHandler(DragEvent.DRAG_OVER, anchorPaneRootSetOnDragOver.get(draggableType));
        anchorPaneRoot.removeEventHandler(DragEvent.DRAG_DROPPED, anchorPaneRootSetOnDragDropped.get(draggableType));

        for (Node n : targetGridPane.getChildren()) {
            n.removeEventHandler(DragEvent.DRAG_ENTERED, gridPaneNodeSetOnDragEntered.get(draggableType));
            n.removeEventHandler(DragEvent.DRAG_EXITED, gridPaneNodeSetOnDragExited.get(draggableType));
        }
    }

    /**
     * handle the pressing of keyboard keys. Specifically, we should pause when
     * pressing SPACE
     *
     * @param event some keyboard key press
     */
    @FXML
    public void handleKeyPress(KeyEvent event) {
        // TODO = handle additional key presses, e.g. for consuming a health potion
        switch (event.getCode()) {
            case SPACE:
                if (isPaused) {
                    startTimer();
                } else {
                    pause();
                }
                break;
            case E:
                world.spendPotions();
                break;
            case R:
                world.useSuperPower();
                break;
            default:
                break;
        }
    }

    public void setMainMenuSwitcher(MenuSwitcher mainMenuSwitcher) {
        // TODO = possibly set other menu switchers
        this.mainMenuSwitcher = mainMenuSwitcher;
    }

    public void setGameOverSwitcher(MenuSwitcher gameOverSwitcher) {
        this.gameOverSwitcher = gameOverSwitcher;
    }

    public void setGameWinSwitcher(MenuSwitcher gameWinSwitcher) {
        this.gameWinSwitcher = gameWinSwitcher;
    }

    public void setHeroCastleMenuSwitcher(MenuSwitcher heroCastleMenuSwitcher) {
        this.heroCastleMenuSwitcher = heroCastleMenuSwitcher;
    }

    /**
     * this method is triggered when click button to go to main menu in FXML
     *
     * @throws IOException
     */
    @FXML
    private void switchToMainMenu() throws IOException {
        // TODO = possibly set other menu switchers
        pause();
        mainMenuSwitcher.switchMenu();
    }

    @FXML
    private void switchToShop() throws IOException {
        pause();
        heroCastleMenuSwitcher.switchMenu();
    }

    /**
     * Set a node in a GridPane to have its position track the position of an entity
     * in the world.
     *
     * By connecting the model with the view in this way, the model requires no
     * knowledge of the view and changes to the position of entities in the model
     * will automatically be reflected in the view.
     *
     * note that this is put in the controller rather than the loader because we
     * need to track positions of spawned entities such as enemy or items which
     * might need to be removed should be tracked here
     *
     * NOTE teardown functions setup here also remove nodes from their GridPane. So
     * it is vital this is handled in this Controller class
     *
     * @param entity
     * @param node
     */
    private void trackPosition(Entity entity, Node node) {
        // TODO = tweak this slightly to remove items from the equipped inventory?
        GridPane.setColumnIndex(node, entity.getX());
        GridPane.setRowIndex(node, entity.getY());

        ChangeListener<Number> xListener = new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                GridPane.setColumnIndex(node, newValue.intValue());
            }
        };
        ChangeListener<Number> yListener = new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                GridPane.setRowIndex(node, newValue.intValue());
            }
        };

        // if need to remove items from the equipped inventory, add code to remove from
        // equipped inventory gridpane in the .onDetach part
        ListenerHandle handleX = ListenerHandles.createFor(entity.x(), node)
                .onAttach((o, l) -> o.addListener(xListener)).onDetach((o, l) -> {
                    o.removeListener(xListener);
                    entityImages.remove(node);
                    squares.getChildren().remove(node);
                    cards.getChildren().remove(node);
                    equippedItems.getChildren().remove(node);
                    unequippedInventory.getChildren().remove(node);
                }).buildAttached();
        ListenerHandle handleY = ListenerHandles.createFor(entity.y(), node)
                .onAttach((o, l) -> o.addListener(yListener)).onDetach((o, l) -> {
                    o.removeListener(yListener);
                    entityImages.remove(node);
                    squares.getChildren().remove(node);
                    cards.getChildren().remove(node);
                    equippedItems.getChildren().remove(node);
                    unequippedInventory.getChildren().remove(node);
                }).buildAttached();
        handleX.attach();
        handleY.attach();

        // this means that if we change boolean property in an entity tracked from here,
        // position will stop being tracked
        // this wont work on character/path entities loaded from loader classes
        entity.shouldExist().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> obervable, Boolean oldValue, Boolean newValue) {
                handleX.detach();
                handleY.detach();
            }
        });
    }

    /**
     * we added this method to help with debugging so you could check your code is
     * running on the application thread. By running everything on the application
     * thread, you will not need to worry about implementing locks, which is outside
     * the scope of the course. Always writing code running on the application
     * thread will make the project easier, as long as you are not running
     * time-consuming tasks. We recommend only running code on the application
     * thread, by using Timelines when you want to run multiple processes at once.
     * EventHandlers will run on the application thread.
     */
    private void printThreadingNotes(String currentMethodLabel) {
        System.out.println("\n###########################################");
        System.out.println("current method = " + currentMethodLabel);
        System.out.println("In application thread? = " + Platform.isFxApplicationThread());
        System.out.println("Current system time = " + java.time.LocalDateTime.now().toString().replace('T', ' '));
    }


    public void checkCardEntity() {
        if (world.getCardEntities().size() >= world.getWidth()){
            // give some cash/experience/item rewards for the discarding of the oldest card
            Random rand = new Random();
            int result = rand.nextInt(10) % 3;
            switch (result) {
                case 0:
                    world.addGold(rand.nextInt(5));
                    break;
                case 1:
                    world.addExperience(rand.nextInt(5));
                    break;
                case 2:
                    world.addUnequippedItem(world.generateItem());
                    break;
            }

            world.removeCard(0);
        }
    }

    public MediaPlayer getAudioPlayer() {
        return audioPlayer;
    }

    public MediaPlayer getShopAudioPlayer() {
        return shopAudioPlayer;
    }
}
