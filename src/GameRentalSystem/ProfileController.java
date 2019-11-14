package GameRentalSystem;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import sun.java2d.cmm.Profile;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public class ProfileController {

  @FXML public BorderPane borderpane; // belongs to UserInterface
  @FXML public Text txtDisplayEmail;
  @FXML private ScrollPane spRentals;
  @FXML private TilePane tpRentals;
  @FXML private Label lblUsername;
  @FXML private Label lblFirstName;
  @FXML private Label lblLastName;
  @FXML private Label lblAge;
  @FXML private Label lblGender;
  @FXML private Label lblEmail;
  @FXML private HBox hBoxRentals;

  private User currentUser = null;
  private Connection connection = null;
  private ArrayList<Game> cartList = new ArrayList<>();

  @FXML
  public void initialize() throws SQLException {
    currentUser = DashboardController.getCurrentUser();

    // Initialize the database and store the connection for later use.
    connection = dbHandler.initializeDB();
    cartList = currentUser.getCartList();

    getAccountInfo();

    hBoxRentals.prefHeightProperty().bind(spRentals.heightProperty());

    ArrayList<Integer> gameIds = getOrders();
    ArrayList<Game> games = getGames(gameIds);

    spRentals.setMaxHeight(140);
    spRentals.setPrefHeight(140);

    showRentals(games);
  }

  public void showRentals(ArrayList<Game> games) {
    hBoxRentals.getChildren().clear();

    for (Game game : games) {

      // Copy the ImageView in imageList, resize, and put it back.//
      ImageView image = new ImageView(game.getGameImage());
      image.setFitHeight(100);
      image.setFitWidth(60);
      image.getStyleClass().add("gameImage");

      Label gameTitle = new Label(game.getGameTitle());
      gameTitle.getStyleClass().add("gameTitle");

      // Create a VBox for each game to contain the ImageView and Label
      VBox vBox = new VBox();
      vBox.setMaxWidth(150);
      vBox.setPrefWidth(150);
      vBox.setAlignment(Pos.TOP_CENTER);
      vBox.getStyleClass().add("gameBox");

      // Add the ImageView and Label to the VBox
      vBox.getChildren().addAll(gameTitle, image);

      hBoxRentals.getChildren().add(vBox);
    }
  }

  private ArrayList<Game> getGames(ArrayList<Integer> gameId) {
    Connection conn = dbHandler.initializeDB();
    ArrayList<Game> game = new ArrayList<>();

    try {
      for (Integer integer : gameId) {
        String sql = "SELECT * FROM GAMES WHERE GAME_ID = ?";
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setInt(1, integer);

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
          String gameTitle = rs.getString("GAME_TITLE");

          // Get Image from database
          InputStream input = rs.getBinaryStream("GAME_IMAGE");
          InputStreamReader inputReader = new InputStreamReader(input);

          // Save image as tempFile.jpg
          File tempFile = new File("res/img/tempFile.jpg");
          FileOutputStream fos = new FileOutputStream(tempFile);

          if (inputReader.ready()) {
            byte[] buffer = new byte[1024];
            while (input.read(buffer) > 0) {
              fos.write(buffer);
            }
          }

          // Load tempFile as Image
          Image image = new Image(tempFile.toURI().toURL().toString());
          game.add(new Game(gameTitle, image));
        }
      }
    } catch (SQLException | IOException e) {
      e.printStackTrace();
    }
    return game;
  }

  public ArrayList<Integer> getOrders() {
    Connection conn = dbHandler.initializeDB();
    ArrayList<Integer> gameId = new ArrayList<>();

    try {
      String sql = "SELECT GAME_ID FROM ORDERS WHERE USER_ID = ?";
      PreparedStatement ps = conn.prepareStatement(sql);
      ps.setInt(1, getUserId());

      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        gameId.add(rs.getInt("GAME_ID"));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return gameId;
  }

  private int getUserId() {
    Connection conn = dbHandler.initializeDB();
    int id = 0;

    try {
      String sql = "SELECT USER_ID FROM USERS WHERE USERNAME = ?";
      PreparedStatement ps = conn.prepareStatement(sql);
      ps.setString(1, currentUser.getUsername());

      ResultSet rs = ps.executeQuery();

      if (rs.next()) {
        id = rs.getInt("USER_ID");
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return id;
  }

  public void getAccountInfo() {

    lblUsername.setText(currentUser.getUsername());
    lblFirstName.setText(currentUser.getFirstName());
    lblLastName.setText(currentUser.getLastName());
    lblAge.setText(Integer.toString(currentUser.getAge()));
    lblGender.setText(currentUser.getGender());
    lblEmail.setText(currentUser.getEmail());
  }

  @FXML
  void changeEmail(MouseEvent event) {
    Dialog<ButtonType> emailChange = new Dialog<>();
    emailChange.setTitle("Change Email");
    emailChange.setHeaderText("Enter a new email");

    // labels to instruct the user to enter information
    Label lblOldEmail = new Label("Old Email: ");
    Label lblNewEmail = new Label("New Email: ");

    // text fields to obtain the user input for each
    TextField txtOldEmail = new TextField(currentUser.getEmail());
    TextField txtNewEmail = new TextField();

    GridPane grid = new GridPane();
    grid.add(lblOldEmail, 1, 1);
    grid.add(txtOldEmail, 2, 1);
    grid.add(lblNewEmail, 1, 2);
    grid.add(txtNewEmail, 2, 2);

    // attaching the grid pane to the dialog pane
    emailChange.getDialogPane().setContent(grid);

    // creates the submit button
    ButtonType btnSubmit = new ButtonType("Submit");

    emailChange.getDialogPane().getButtonTypes().add(btnSubmit);
    emailChange
        .getDialogPane()
        .getButtonTypes()
        .add(new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE));

    Optional<ButtonType> result = emailChange.showAndWait();
    if (result.isPresent() && result.get() == btnSubmit) {
      Connection conn = dbHandler.initializeDB();
      if (conn != null) {
        try {
          String sql = "UPDATE USERS SET EMAIL = ? WHERE USERNAME = ?";
          PreparedStatement ps = conn.prepareStatement(sql);
          ps.setString(1, txtNewEmail.getText());
          ps.setString(2, currentUser.getUsername());

          ps.executeUpdate();
          currentUser.setEmail(txtNewEmail.getText());

          initialize();
          dbHandler.close(ps);
        } catch (SQLException e) {
          e.printStackTrace();
        }
        dbHandler.close(conn);
      }
    }
  }
}
