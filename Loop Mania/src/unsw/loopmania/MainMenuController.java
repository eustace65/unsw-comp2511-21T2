package unsw.loopmania;

import java.io.File;
import java.io.IOException;

import javax.swing.GroupLayout.Alignment;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.media.MediaPlayer;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

/**
 * controller for the main menu.
 * TODO = you could extend this, for example with a settings menu, or a menu to load particular maps.
 */
public class MainMenuController {
    /**
     * facilitates switching to main game
     */
    private MenuSwitcher gameSwitcher;
    private LoopManiaWorld world;
    private LoopManiaWorldController controller;
    
    

    public void setGameSwitcher(MenuSwitcher gameSwitcher){
        this.gameSwitcher = gameSwitcher;
    }
    
    public MainMenuController(LoopManiaWorld world, LoopManiaWorldController controller) {
        this.world = world;
        this.controller = controller;
    }
    @FXML
    private ImageView play;

    @FXML
    private StackPane music;

    /**
     * facilitates switching to main game upon button click
     * @throws IOException
     */
    @FXML
    private void switchToGame() throws IOException {
        world.setMode(ModeType.STANDARD);
        gameSwitcher.switchMenu();
    }



    @FXML
    private void switchToBerserkerGame() throws IOException {
        world.setMode(ModeType.BERSERKER);
        gameSwitcher.switchMenu();
    }

    @FXML
    private void switchToSurvivalGame() throws IOException {
        world.setMode(ModeType.SURVIVAL);
        gameSwitcher.switchMenu();

    }

    @FXML
    private void switchToConfusingGame() throws IOException {
        world.setMode(ModeType.CONFUSING);
        gameSwitcher.switchMenu();
    }

    @FXML
    private void mute(MouseEvent event) {
        controller.setMute(true);
        Image mute = new Image((new File("src/images/mute.png")).toURI().toString());
        ImageView view = new ImageView(mute);
        view.setFitHeight(30);
        view.setFitWidth(30);
        music.getChildren().remove(play);
        music.getChildren().add(view);
        StackPane.setAlignment(view, Pos.CENTER);
    }
}
