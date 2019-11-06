package GameRentalSystem;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.sql.*;
import java.util.Optional;

public class DashboardController {
  @FXML private TilePane tpGames;
  @FXML private ScrollPane spGames;
  @FXML private HBox hBox;
  @FXML private Label topPanelTxt;
  @FXML private Button butttonAG;

  private Connection connection = null;
  private static String currentUser = null;
  private File selectedFile;
  private Stage dashboardStage = new Stage();
  private FileChooser fileChooser = new FileChooser();

  @FXML public BorderPane borderpane;

  public DashboardController(String username) {
    // Save the current users username
    currentUser = username;

    // Create new stage
    Stage dashboardStage = new Stage();

    // Load the FXML file
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("Dashboard.fxml"));

      // Set this class as the controller
      loader.setController(this);

      // Load the scene
      dashboardStage.setScene(new Scene(loader.load()));

      // Setup the window/stage
      dashboardStage.setTitle("Dashboard");
      dashboardStage.show();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  //
  @FXML
  public void initialize() throws SQLException {

    // Test to show that the dashboard knows who is logged in.
    System.out.println("Dashboard Controller -> Logged in as: " + currentUser);
    topPanelTxt.setText(currentUser);

    if (currentUser.equals("admin")) {

    } else {
      butttonAG.setOpacity(0);
      butttonAG.setDisable(true);
    }
  }

  @FXML
  void handleAddGame(ActionEvent event) {
    if (currentUser.equals("admin")) {
      Dialog<ButtonType> addGame = new Dialog<>();
      addGame.setTitle("Add a New Game");
      addGame.setHeaderText("Enter the necessary information to add a new game to the system.");

      Label lblTitle = new Label("Game Title: ");
      Label lblGenre = new Label("Genre: ");
      Label lblRating = new Label("ESRB Rating: ");
      Label lblPrice = new Label("Price: ");
      Label lblGameImage = new Label("Game Image: ");

      TextField txtTitle = new TextField();

      ChoiceBox<GameGenre> cbGenre = new ChoiceBox<>();
      cbGenre.getItems().addAll(GameGenre.values());

      ChoiceBox<GameRating> cbRating = new ChoiceBox<>();
      cbRating.getItems().addAll(GameRating.values());

      TextField txtPrice = new TextField();
      TextField txtGameImage = new TextField();

      // Create a GridPane and add the elements to it.
      GridPane grid = new GridPane();
      grid.add(lblTitle, 1, 1);
      grid.add(txtTitle, 2, 1);
      grid.add(lblGenre, 1, 2);
      grid.add(cbGenre, 2, 2);
      grid.add(lblRating, 1, 3);
      grid.add(cbRating, 2, 3);
      grid.add(lblPrice, 1, 4);
      grid.add(txtPrice, 2, 4);
      grid.add(lblGameImage, 1, 5);
      grid.add(txtGameImage, 2, 5);

      addGame.getDialogPane().setContent(grid);

      ButtonType btnSubmit = new ButtonType("Submit");

      addGame.getDialogPane().getButtonTypes().add(btnSubmit);
      addGame
          .getDialogPane()
          .getButtonTypes()
          .add(new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE));

      // When the user clicks the TextField for the Game Image.
      txtGameImage.setOnMouseClicked(
          e -> {
            // Show a file chooser
            selectedFile = fileChooser.showOpenDialog(dashboardStage);

            // Set the text of the text area to the filename of the selected file.
            txtGameImage.setText(selectedFile.getName());
          });

      // Show the dialog and set event handler on submit button
      Optional<ButtonType> result = addGame.showAndWait();
      if (result.isPresent() && result.get() == btnSubmit) {
        try {

          // Initialize fileIn as null to avoid a NullPointerException if the user doesn't add an
          // image.
          FileInputStream fileIn = null;
          if (selectedFile != null) {
            fileIn = new FileInputStream(selectedFile);
          }

          // Create a new Game object.
          new Game(
              txtTitle.getText(),
              cbGenre.getValue(),
              cbRating.getValue(),
              txtPrice.getText(),
              fileIn);

          // Insert new game into the database
          connection = dbHandler.initializeDB();

          String sql =
              "INSERT INTO GAMES(GAME_TITLE, GAME_IMAGE, ESRB_RATING, GENRE, PRICE) VALUES (?, ?, ?, ?, ?)";
          PreparedStatement ps = connection.prepareStatement(sql);
          ps.setString(1, txtTitle.getText());
          ps.setBinaryStream(2, fileIn);
          ps.setString(3, cbRating.getValue().toString());
          ps.setString(4, cbGenre.getValue().toString());
          ps.setString(5, txtPrice.getText());

          ps.executeUpdate();

          dbHandler.close(ps);
          dbHandler.close(connection);
        } catch (Exception ex) {
          ex.printStackTrace();
        }
      }
    } else {
      butttonAG.setOpacity(0);
      butttonAG.setDisable(true);
    }
  }

  // Show the GameList.fxml
  @FXML
  void loadGameList(MouseEvent event) {
    loadUI("GameList");
    System.out.println("GameList click");
  }

  // Show the Profile.fxml
  @FXML
  void loadProfile(MouseEvent event) {
    loadUI("Profile");
  }

  // Show the Cart
  @FXML
  void loadCart(MouseEvent event) {
    loadUI("Cart");
  }

  @FXML
  void loadReturn(MouseEvent event) {
    borderpane.setCenter(null);
  }

  private void loadUI(String ui) {
    Parent root = null;

    try {
      root = FXMLLoader.load(getClass().getResource(ui + ".fxml"));
    } catch (IOException e) {
      e.printStackTrace();
    }
    borderpane.setCenter(root);
  }

  public static String getCurrentUser() {
    return currentUser;
  }
}
