package GameRentalSystem;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TouchEvent;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;

public class DashboardController {

  @FXML private Button cartBtnBackground;
  @FXML private Button browseGameBtnBackground;
  @FXML private Button profilebtnbackground;
  @FXML private ImageView profilebtn;
  @FXML private ImageView browsegamebtn;
  @FXML private ImageView cartbtn;
  @FXML private ImageView addgamebtn;
  @FXML private ImageView logoutbtn;

  @FXML private TilePane tpGames;
  @FXML private ScrollPane spGames;
  @FXML private HBox hBox;
  @FXML private Label topPanelTxt;
  @FXML private Button btnAddGame;
  @FXML private Button butttonLogout;

  private Connection connection = null;
  private static User currentUser = null;
  private File selectedFile;
  private Stage dashboardStage = new Stage();
  private FileChooser fileChooser = new FileChooser();
  private ColorAdjust colorAdjust = new ColorAdjust();
  private ColorAdjust colorNormal = new ColorAdjust();
  private Blend hovereffect = new Blend();
  private Blend noHoverEffect = new Blend();

  @FXML public BorderPane borderpane;

  public DashboardController(User currentUser) {
    // Save the current users username
    // currentUser = username;
    DashboardController.currentUser = currentUser;

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
    System.out.println("Dashboard Controller -> Logged in as: " + currentUser.getUsername());
    topPanelTxt.setText(currentUser.getUsername());
    colorAdjust.setHue(0.7);
    colorNormal.setHue(0);
    hovereffect.setMode(BlendMode.ADD);
    hovereffect.setOpacity(0.2);
    noHoverEffect.setOpacity(0);
    loadUI("Featured");
    if (!currentUser.getUsername().equals("admin")) {
      btnAddGame.setOpacity(0);
      btnAddGame.setDisable(true);
    }
  }

  /**
   * logout
   *
   * @param event
   */
  @FXML
  void logoutReleased(MouseEvent event) throws InterruptedException {
    Thread.sleep(1000);
    try {
      Stage loginStage = new Stage();
      ArrayList<Game> cartList = new ArrayList<>();
      currentUser.setCartList(cartList);

      FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));

      // Load the scene
      loginStage.setScene(new Scene(loader.load()));

      // Setup the window/stage
      loginStage.setTitle("Game Rental System");
      loginStage.show();
      Stage stage = (Stage) butttonLogout.getScene().getWindow();
      stage.close();

    } catch (IOException e) {
      e.printStackTrace();
    }
    System.out.println("logout Clicked");
  }

  private void makeSlide() {
    TranslateTransition translate = new TranslateTransition();
    translate.setDuration(Duration.seconds(.35));
    translate.setFromX(borderpane.getWidth());
    translate.setToX(0);
    translate.setNode(borderpane.getCenter());
    translate.play();
  }

  @FXML
  void handleAddGame(ActionEvent event) {
    if (currentUser.getUsername().equals("admin")) {
      addgamebtn.setEffect(colorAdjust);
      profilebtn.setEffect(colorNormal);
      browsegamebtn.setEffect(colorNormal);
      cartbtn.setEffect(colorNormal);
      logoutbtn.setEffect(colorNormal);
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
    }
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
    makeSlide();
  }

  public static User getCurrentUser() {
    System.out.println(
        "Getting current user from DashboardController. " + currentUser.getUsername());
    return currentUser;
  }

  /** Button Click Effects and LoadUI */
  @FXML
  void loadProfile(MouseEvent event) {
    loadUI("Profile");
  }

  @FXML
  void loadGameList(MouseEvent event) {
    loadUI("GameList");
    System.out.println("GameList click");
  }

  @FXML
  void loadCart(MouseEvent event) {
    loadUI("Cart");
  }

  @FXML
  void logoutIn(MouseEvent event) {
  }

  /** Button Hover Effects */
  @FXML
  void profileHover(MouseEvent event) {
    profilebtnbackground.setEffect(hovereffect);
  }

  @FXML
  void profileHoverOff(MouseEvent event) {
    profilebtnbackground.setEffect(noHoverEffect);
  }

  @FXML
  void browseGameHover(MouseEvent event) {
    browseGameBtnBackground.setEffect(hovereffect);
  }

  @FXML
  void GameHoverOff(MouseEvent event) {
    browseGameBtnBackground.setEffect(noHoverEffect);
  }

  @FXML
  void cartHover(MouseEvent event) {
    cartBtnBackground.setEffect(hovereffect);
  }

  @FXML
  void cartHoverOff(MouseEvent event) {
    cartBtnBackground.setEffect(noHoverEffect);
  }

  @FXML
  void addGameBtnHover(MouseEvent event) {
    btnAddGame.setEffect(hovereffect);
  }

  @FXML
  void addGameBtnHoverOff(MouseEvent event) {
    btnAddGame.setEffect(noHoverEffect);
  }

  @FXML
  void logoutHover(MouseEvent event) {
    butttonLogout.setEffect(hovereffect);
  }

  @FXML
  void logoutHoverOff(MouseEvent event) {
    butttonLogout.setEffect(noHoverEffect);
  }
}
