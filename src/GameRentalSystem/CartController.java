package GameRentalSystem;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.sql.*;
import java.util.ArrayList;

public class CartController {
  @FXML private TableView<Game> tvCart;
  @FXML private TableColumn<Game, String> colTitle;
  private ArrayList<Game> cartList = new ArrayList<>();
  private User currentUser;

  public void initialize() {
    currentUser = DashboardController.getCurrentUser();
    //cartList = GameListController.getCartList();
    cartList = currentUser.getCartList();


    showCart();
  }

  private void showCart() {
    tvCart.getItems().clear();

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

  @FXML
  void handleCheckout(MouseEvent event) {
    Connection conn = dbHandler.initializeDB();
    try {
      String sql = "INSERT INTO ORDERS(USER_ID, RENTAL_DATE, GAME_ID) VALUES (?,?,?)";
      PreparedStatement ps = conn.prepareStatement(sql);

      for (Game game : cartList) {
        ps.setInt(1, getUserId());
        ps.setDate(2, new Date(new java.util.Date().getTime()));
        ps.setInt(3, getGameId(game.getGameTitle()));

        ps.executeUpdate();
      }

      cartList.clear();
      currentUser.setCartList(cartList);
      dbHandler.close(ps);
      dbHandler.close(conn);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @FXML
  void handleRemove(MouseEvent event) {

    // Get the currently selected game in the cart.
    Game game = tvCart.getSelectionModel().getSelectedItem();

    // Remove the game from the TableView and the cartList.
    tvCart.getItems().remove(game);
    cartList.remove(game);

    // Pass the updated cartList back to the currentUser to reflect the changes.
    currentUser.setCartList(cartList);
  }

  @FXML
  void handleRemoveAll(MouseEvent event) {

    // Remove the game from the TableView and the cartList.
    tvCart.getItems().clear();
    cartList.clear();

    // Pass the updated cartList back to the currentUser to reflect the changes.
    currentUser.setCartList(cartList);
  }

  private int getUserId() {
    Connection conn = dbHandler.initializeDB();
    int id = 0;

    if (conn != null) {
      try {
        String sql = "SELECT * FROM USERS WHERE USERNAME = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, currentUser.getUsername());

        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
          id = rs.getInt("USER_ID");
        } else {
          System.out.println("error");
        }

        dbHandler.close(rs);
        dbHandler.close(ps);
        dbHandler.close(conn);
      } catch (SQLException e) {
        e.printStackTrace();
        return 0;
      }
    }
    return id;
  }

  private int getGameId(String gameTitle) {
    int id = 0;
    Connection conn = dbHandler.initializeDB();

    try {
      String sql = "SELECT * FROM GAMES WHERE GAME_TITLE = ?";
      PreparedStatement ps = conn.prepareStatement(sql);
      ps.setString(1, gameTitle);

      ResultSet rs = ps.executeQuery();
      if (rs.next()) {
        id = rs.getInt("GAME_ID");
      } else {
        System.out.println("error");
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return id;
  }
}
