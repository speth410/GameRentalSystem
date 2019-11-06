package GameRentalSystem;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;

public class CartController {
  @FXML private TableView<Game> tvCart;
  @FXML private TableColumn<Game, String> colTitle;
  ArrayList<Game> cartList = new ArrayList<>();

  public void initialize() {
    cartList = GameListController.getCartList();
    showCart();
  }

  private void showCart() {
    // Make the column and add it to the Table View
    colTitle.setCellValueFactory(new PropertyValueFactory<>("gameTitle"));
    colTitle.getStyleClass().add("gameTitleColumn");

    // Check to make sure the game doesn't already exist in the cart
    for (Game game : cartList) {
      if (!tvCart.getItems().contains(game)) {
        tvCart.getItems().add(game);
      }
    }
  }
}
