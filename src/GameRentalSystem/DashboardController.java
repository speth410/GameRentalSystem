package GameRentalSystem;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.swing.*;
import javax.swing.text.LabelView;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DashboardController extends LoginController {
  @FXML private TilePane tpGames;
  @FXML private ScrollPane spGames;
  @FXML private HBox hBox;
  @FXML private TableView<CartItem> tvCart;
  @FXML private TableColumn<CartItem, String> colTitle;
  @FXML private TableColumn<?, ?> colRemove;
  @FXML private Button btnCheckout;
  @FXML private Label lblUsername;
  @FXML private Label topPanelTxt;
  @FXML private Button butttonAG;

  private Connection connection = null;
  private static String currentUser = null;
  private File selectedFile;
  private Stage dashboardStage = new Stage();
  private FileChooser fileChooser = new FileChooser();
  private Game game;
  public Text txtDisplayUserName;

  @FXML public BorderPane borderpane; // belongs to UserInterface

  @FXML public Text txtDisplayFirstName;
  @FXML public Text txtDisplayLastName;
  @FXML public Text txtDisplayAge;
  @FXML public Text txtDisplayGender;
  @FXML public Text txtDisplayEmail;

  private ArrayList<Game> games = new ArrayList<>();

  public DashboardController(String username) {
    // Save the current users username
    currentUser = username;

    // Create new stage
    Stage dashboardStage = new Stage();

    // Load the FXML file
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("UserInterface.fxml"));

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

  @FXML
  public void initialize() throws SQLException {

    // Get Connection from dbHandler
    connection = dbHandler.initializeDB();
    // Test to show that the dashboard knows who is logged in.
    System.out.println("Dashboard Controller -> Logged in as: " + currentUser);
    topPanelTxt.setText(currentUser);

    if (currentUser.equals("admin")){

    }else {
      butttonAG.setOpacity(0);
      butttonAG.setDisable(true);
    }

    //    getAccountInfo();

  }

  @FXML
  private void getGames() {

    // Remove all Game objects from the games ArrayList
    // Prevents duplicate entries
    games.clear();

    String sql = "SELECT * FROM GAMES";
    List<ImageView> imageList = new ArrayList<>();
    List<Label> labelList = new ArrayList<>();
    try {
      Statement stmt = connection.createStatement();
      ResultSet rs = stmt.executeQuery(sql);
      while (rs.next()) {

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

        // Get game title from database
        String gameTitle = rs.getString("GAME_TITLE");

        // Create a new ImageView and Label & store in an arrayList
        imageList.add(new ImageView(image));
        labelList.add(new Label(gameTitle));

        // Create a Game object for each game entry obtained from the database.
        if (!games.contains(new Game(gameTitle, image))) {
          games.add(new Game(gameTitle, image));
          System.out.println("Added: " + gameTitle);
        } else {
          System.out.println("Duplicate found");
        }
      }
      // Pass Labels and ImageViews to method to be drawn to the scene
      showGames(imageList, labelList);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void showGames(List<ImageView> imageList, List<Label> labelList) {

    tpGames.getChildren().clear();

    // For each element in the array (Both arrays should be the same size)
    for (int i = 0; i < imageList.size(); i++) {

      // Copy the ImageView in imageList, resize, and put it back.
      ImageView images = imageList.get(i);
      images.setFitHeight(100);
      images.setFitWidth(60);
      images.getStyleClass().add("gameImage");

      imageList.set(i, images);

      Label gameTitle = labelList.get(i);
      gameTitle.getStyleClass().add("gameTitle");

      // Create a VBox for each game to contain the ImageView and Label
      VBox vBox = new VBox();
      vBox.setAlignment(Pos.TOP_CENTER);
      vBox.getStyleClass().add("gameBox");

      // Add the ImageView and Label to the VBox
      vBox.getChildren().addAll(labelList.get(i), imageList.get(i));

      // Add and OnMouseClicked Event on each game listing
      vBox.setOnMouseClicked(
          e -> {
            tvCart.getColumns().clear();

            // Create temporary copy of the label contained within the VBox
            Label title = (Label) vBox.getChildren().get(0);

            CartItem item = new CartItem(title.getText());

            // Make the column and add it to the Table View
            colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
            colTitle.getStyleClass().add("gameTitleColumn");
            tvCart.getColumns().add(colTitle);

            // Check to make sure the game doesn't already exist in the cart
            if (!tvCart.getItems().contains(item)) {
              tvCart.getItems().add(item);
            } else {

              // Show a dialog informing the user that the item is already in their cart.
              Alert alert = new Alert(Alert.AlertType.INFORMATION);
              alert.setTitle("Duplicate item");
              alert.setHeaderText("That item is already in your cart!");
              alert.showAndWait();
            }
          });

      // Add the VBox containing the games image and title to the TilePane
      tpGames.getChildren().add(vBox);
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

          // Create a new Game object and add it to the games ArrayList.
          games.add(
              new Game(
                  txtTitle.getText(),
                  cbGenre.getValue(),
                  cbRating.getValue(),
                  txtPrice.getText(),
                  fileIn));

          // Insert new game into the database
          String sql =
              "INSERT INTO GAMES(GAME_TITLE, GAME_IMAGE, ESRB_RATING, GENRE, PRICE) VALUES (?, ?, ?, ?, ?)";
          PreparedStatement stmt = connection.prepareStatement(sql);
          stmt.setString(1, txtTitle.getText());
          stmt.setBinaryStream(2, fileIn);
          stmt.setString(3, cbRating.getValue().toString());
          stmt.setString(4, cbGenre.getValue().toString());
          // stmt.setInt(5, txtPrice.getText();

          stmt.executeUpdate();
          getGames();

          System.out.println("Size of games ArrayList: " + games.size());

          // Test to show all of the Game objects stored within the games ArrayList.
          for (Game game : games) {
            game.print();
          }
        } catch (Exception ex) {
          ex.printStackTrace();
        }
      }
    }
    else {
      butttonAG.setOpacity(0);
      butttonAG.setDisable(true);
    }
  }

  @FXML
  void loadGameList(MouseEvent event) {
    loadUI("GameList");
    System.out.println("GameList click");
  }

  @FXML
  void loadProfile(MouseEvent event) {
      loadUI("Profile");
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

  @FXML
  void handleRemoveClicked(MouseEvent event) {
    CartItem item = tvCart.getSelectionModel().getSelectedItem();
    tvCart.getItems().remove(item);
  }

  public void getAccountInfo() throws SQLException {
    String sql = "SELECT * FROM USERS WHERE USERNAME = ?";
    PreparedStatement stmt = connection.prepareStatement(sql);
    stmt.setString(1, currentUser);
    ResultSet resultSetGetAccountInfo = stmt.executeQuery();

    while (resultSetGetAccountInfo.next()) {
      txtDisplayUserName.setText(currentUser);
      txtDisplayFirstName.setText(resultSetGetAccountInfo.getString("FIRST_NAME"));
    }
  }

  public static String getCurrentUser() {
    return currentUser;
  }
}
// khgjhgjh
