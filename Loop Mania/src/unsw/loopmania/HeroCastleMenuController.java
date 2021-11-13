package unsw.loopmania;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
//import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.io.File;

import javax.swing.Action;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;

public class HeroCastleMenuController {

    private MenuSwitcher gameSwitcher;
    private LoopManiaWorld world;
    private LoopManiaWorldController controller;
    private IntegerProperty goldInt;
    @FXML
    private GridPane inventory;
    private int potionBought = 0;
    private int armourBought = 0;

    public HeroCastleMenuController(LoopManiaWorld world, LoopManiaWorldController controller) {
        this.world = world;
        this.controller = controller;
        //Image swordImage = new Image((new File("src/images/basic_sword.png")).toURI().toString());
        
    }

    public void setGameSwitcher(MenuSwitcher gameSwitcher) {
        this.gameSwitcher = gameSwitcher;
    }
    
    @FXML
    private void switchToGame(){
        gameSwitcher.switchMenu();
    }
 
    @FXML
    private StackPane paneToSell;

    @FXML
    private Label gold;

    @FXML
    private Button exitButton;

    @FXML
    private Label swordPrice;

    @FXML
    private Button purchaseSword;

    @FXML
    private Label staffPrice;

    @FXML
    private Button purchaseStaff;

    @FXML
    private Label stakePrice;

    @FXML
    private Button purchaseStake;

    @FXML
    private Label armourPrice;

    @FXML
    private Button purchaseArmour;

    @FXML
    private Label helmetPrice;

    @FXML
    private Button purchaseHelmet;

    @FXML
    private Button purchaseShield;

    @FXML
    private Label potionPrice;

    @FXML
    private Button purchasePotion;

    @FXML
    private Button purchaseDoggie;

    @FXML
    private Label doggiePrice;

    @FXML
    private Button sell;

    @FXML
    private StackPane currentGold;

    @FXML
    private Label doggieCoinOwned;

    @FXML
    private Label shopMessage = new Label("");


    private int nextAvailableX = 0;
    private int nextAvailableY = 0;

    public void update() {
        initialiseInventory();
        //goldInt.set(world.getGolds());
        //sell.setText("Sell");
        for(ItemProperty item: world.getUnequippedInventoryItems()) {
            if (item != null) {

                ImageView view = item.onLoadItems();
                inventory.add(view, nextAvailableX, nextAvailableY);
                if (nextAvailableX == LoopManiaWorld.unequippedInventoryWidth - 1) {
                    nextAvailableX = 0;
                    nextAvailableY++;
                } else {
                    nextAvailableX++;
                }
    
                view.setOnMouseClicked(e->selected(item));
            }


        }
    
    }

    @FXML
    void updateInventory(ActionEvent event) {
        update();
    }

    @FXML
    void selectDoggie(MouseEvent event) {
        DoggieCoin coin = new DoggieCoin(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0), ItemType.DOGGIECOIN);
        selected(coin);
        coin.destroy();
    }

    
    void selected(ItemProperty item) {
        initialisePane();
        ImageView view = item.onLoadItems();
        view.setFitHeight(100);
        view.setFitWidth(100);
        paneToSell.getChildren().add(view);
        Label label = new Label(item.getType().name() + ":  $" + world.getItemPrice(item.getType()).get());
        paneToSell.getChildren().add(label);
        StackPane.setAlignment(label, Pos.BOTTOM_CENTER);
        StackPane.setAlignment(view, Pos.TOP_CENTER);
    }

    public void initialiseInventory() {
        inventory.getChildren().clear();
        this.nextAvailableX = 0;
        this.nextAvailableY = 0;
        Image inventorySlotImage = new Image((new File("src/images/empty_slot.png")).toURI().toString());
        for (int x = 0; x < LoopManiaWorld.unequippedInventoryWidth; x++) {
            for (int y = 0; y < LoopManiaWorld.unequippedInventoryHeight; y++) {
                ImageView emptySlotView = new ImageView(inventorySlotImage);
                inventory.add(emptySlotView, x, y);
            }
        }
    }

    @FXML
    void handleExitButton(ActionEvent event) {
        resetButtons();
        controller.getShopAudioPlayer().pause();
        if (!controller.getMute()) controller.getAudioPlayer().play();
        resetShop();
        switchToGame();
    }


    private void buyItem(ItemType itemType) {
        world.addGold(-1 * world.getItemPrice(itemType).get());
        //goldInt.set(world.getGolds());
        //gold.textProperty().bind(goldInt.asString());
        if (itemType == ItemType.HEALTHPOTION) {
            world.addPotion(1);
            
        } else if (itemType == ItemType.DOGGIECOIN) {
            world.addDoggieCoin(1);
        } else {
            ItemProperty item = world.addUnequippedItem(itemType);
            controller.onLoad(item);
        }
        
        update();
    }
    
    @FXML
    void handlePurchaseDoggie(ActionEvent event) {
        if (world.getGold().get() >= world.getItemPrice(ItemType.DOGGIECOIN).get()) {
            buyItem(ItemType.DOGGIECOIN);
            shopMessage.setText("Shop Message: 1 doggie coin was purchased");
        }
        else {
            shopMessage.setText("Shop Message: Not enough gold to buy doggie coin");
        }
    }

    @FXML
    void handlePurchaseArmour(ActionEvent event) {
        if (world.getMode() == ModeType.BERSERKER && armourBought != 0) {
            shopMessage.setText("Shop Message: Cannot buy more armour in Berserker mode");
        }
        else {
            if (world.getGold().get() >= world.getItemPrice(ItemType.ARMOUR).get()) {
                buyItem(ItemType.ARMOUR);
                armourBought++;
                shopMessage.setText("Shop Message: Armour was purchased");
            }
            else {
                shopMessage.setText("Shop Message: Not enough gold to buy armour");
            }
        }

        
    }

    @FXML
    void handlePurchaseHelmet(ActionEvent event) {
        if (world.getMode() == ModeType.BERSERKER && armourBought != 0) {
            shopMessage.setText("Shop Message: Cannot buy more armour in Berserker mode");
        }
        else {
            if (world.getGold().get() >= world.getItemPrice(ItemType.HELMET).get()) {
                buyItem(ItemType.HELMET);
                armourBought++;
                shopMessage.setText("Shop Message: Helmet was purchased");
            }
            else {
                shopMessage.setText("Shop Message: Not enough gold to buy helmet");
            }
        }


        
    }

    @FXML
    void handlePurchasePotion(ActionEvent event) {
        if (world.getMode() == ModeType.SURVIVAL && potionBought != 0) {
            shopMessage.setText("Shop Message: Cannot buy more potions in Survival mode");
        }
        else {
            if (world.getGold().get() >= world.getItemPrice(ItemType.HEALTHPOTION).get()) {
                buyItem(ItemType.HEALTHPOTION);
                potionBought++;
                shopMessage.setText("Shop Message: Potion was purchased");
            }
            else {
                shopMessage.setText("Shop Message: Not enough gold to buy potion");
            }
        }

        
    }

    @FXML
    void handlePurchaseShield(ActionEvent event) {
        if (world.getMode() == ModeType.BERSERKER && armourBought != 0) {
            shopMessage.setText("Shop Message: Cannot buy more armour in Berserker mode");
        }
        else {
            if (world.getGold().get() >= world.getItemPrice(ItemType.SHIELD).get()) {
                buyItem(ItemType.SHIELD);
                armourBought++;
                shopMessage.setText("Shop Message: Shield was purchased");
            }
            else {
                shopMessage.setText("Shop Message: Not enough gold to buy shield");
            }
        }

        
    }

    @FXML
    void handlePurchaseStaff(ActionEvent event) {
        if (world.getGold().get() >= world.getItemPrice(ItemType.STAFF).get()) {
            buyItem(ItemType.STAFF);
            shopMessage.setText("Shop Message: Staff was purchased");
        }
        else {
            shopMessage.setText("Shop Message: Not enough gold to buy staff");
        }
        
    }

    @FXML
    void handlePurchaseStake(ActionEvent event) {
        if (world.getGold().get() >= world.getItemPrice(ItemType.STAKE).get()) {
            buyItem(ItemType.STAKE);
            shopMessage.setText("Shop Message: Stake was purchased");
        }
        else {
            shopMessage.setText("Shop Message: Not enough gold to buy stake");
        }
        
    }

    @FXML
    void handlePurchaseSword(ActionEvent event) {
        if (world.getGold().get() >= world.getItemPrice(ItemType.SWORD).get()) {
            buyItem(ItemType.SWORD);
            shopMessage.setText("Shop message: Sword was purchased");
        }
        else {
            shopMessage.setText("Shop Message: Not enough gold to buy sword");
        }
        
    }


    @FXML
    public void initialize() {
        goldInt = world.getGold();
        gold = new Label(String.valueOf(goldInt.get()));
        gold.textProperty().bind(goldInt.asString());
        gold.setTextFill(Color.ORANGE);
        gold.setFont(new Font("Cambria", 40));
        //goldInt.set(world.getGolds());
        currentGold.getChildren().add(gold);
        StackPane.setAlignment(gold, Pos.CENTER_RIGHT);
        potionBought = 0;
        armourBought = 0;
        update();
        doggiePrice.setText("0");
        doggiePrice.textProperty().bind(DoggieCoinPrice.price.asString());

        doggieCoinOwned.setText(String.valueOf(world.getDoggieCoin().get()));
        //doggieCoinOwned.textProperty().bind(world.getDoggieCoin().asString());
    }

    @FXML
    public void addCoin(ActionEvent event) {
        String inital = doggieCoinOwned.getText();
        int now = Integer.parseInt(inital) + 1;
        if (now > world.getDoggieCoin().get()) return;
        doggieCoinOwned.setText(String.valueOf(now));
    }

    @FXML
    public void decreaseCoin(ActionEvent event) {
        String inital = doggieCoinOwned.getText();
        int now = Integer.parseInt(inital) - 1;
        if (now < 0) return;
        doggieCoinOwned.setText(String.valueOf(now));
    }

    @FXML
    public void sellItem(ActionEvent event) {
        if (paneToSell.getChildren().size() == 0) return;
        String text = "";
        for (Node each: paneToSell.getChildren()) {
            if (each instanceof Label) {
                Label label = (Label)each;
                text = label.getText().split(":")[0];
            }
        }
        
        removeItem(text);
        update();
        initialisePane();
        //goldInt.set(world.getGolds());
    }

    public void initialisePane() {
        paneToSell.getChildren().clear();
    }

    public void removeItem(String text) {
        if (text.equals("DOGGIECOIN")) {
            int number = Integer.parseInt(doggieCoinOwned.getText());
            goldInt.set(goldInt.get() + number * DoggieCoinPrice.price.get());
            world.addDoggieCoin(-1 * number);
            doggieCoinOwned.setText(String.valueOf(world.getDoggieCoin().get()));
            return;
        }
        for (ItemProperty item: world.getUnequippedInventoryItems()) {
            if(item.getType().name().equals(text)) {
                world.getUnequippedInventoryItems().remove(item);
                controller.unLoad(item);
                //world.addGold(item.getPrice());
                goldInt.set(goldInt.get() + world.getItemPrice(item.getType()).get());
                item.destroy();
                return;
            }
        }
    }
    public void resetButtons() {
        sell.setText("Sell");
        purchaseSword.setText("Buy");
    }

    public void resetShop() {
        potionBought = 0;
        armourBought = 0;
    }

}
