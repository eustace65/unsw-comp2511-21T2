package unsw.loopmania;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;


public class GameOverController {
    /**
     * facilitates switching to gameOver screen
     */
    private MenuSwitcher mainMenuSwitcher;

    public GameOverController() {
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
