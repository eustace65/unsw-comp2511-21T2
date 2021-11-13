package unsw.loopmania;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;


public class GameWinController {
    /**
     * facilitates switching to gameOver screen
     */
    private MenuSwitcher mainMenuSwitcher;

    public GameWinController() {
    }

    public void setMainMenuSwitcher(MenuSwitcher mainMenuSwitcher){
        this.mainMenuSwitcher = mainMenuSwitcher;
    }

    /**
     * button to switch back to main menu
     */

    @FXML
    private Button mainMenuButton;

    @FXML
    void handleMainMenuButton(ActionEvent event) {
        mainMenuSwitcher.switchMenu();
    }
}