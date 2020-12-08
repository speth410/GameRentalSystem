package GameRentalSystem;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GameListController {

  @FXML private TilePane tpGames;
  @FXML private ScrollPane spGames;
  @FXML private HBox hBox;
  @FXML private VBox vbGames;

  @FXML public BorderPane borderpane;
  @FXML private TableView<CartItem> tvCart;
  @FXML private TableColumn<CartItem, String> colTitle;
  @FXML private TableColumn<?, ?> colRemove;
  @FXML private TextArea consoleText;

  private Game game;
  private Connection connection = null;
  private File selectedFile;
  private Stage dashboardStage = new Stage();
  private FileChooser fileChooser = new FileChooser();

  private ArrayList<Game> gamesList = new ArrayList<>();
  private List<ImageView> imageList = new ArrayList<>();
  private List<Label> labelList = new ArrayList<>();
  private List<Label> priceList = new ArrayList<>();
  private static ArrayList<Game> cartList = new ArrayList<>();
  private User currentUser;

  @FXML
  public void initialize() throws SQLException {

    // Get the current user
    currentUser = DashboardController.getCurrentUser();

    // Get the users cartList.
    cartList = currentUser.getCartList();

    // Get Connection from dbHandler
    connection = dbHandler.initializeDB();

    // Bind TilePanes dimensions to the ScrollPane that contains it
    tpGames.prefHeightProperty().bind(spGames.heightProperty());
    tpGames.prefWidthProperty().bind(spGames.widthProperty());

    // Retrieve Games from the database
    getGames();

    // Dynamically create GUI elements to show the games stored in the database.
    showGames();

  }

  @FXML
  private void getGames() {

    // Remove all Game objects from the games ArrayList
    // Prevents duplicate entries
    gamesList.clear();

    System.out.println("Getting games.");

    String sql = "SELECT * FROM GAMES";
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

        // Get game price
        String gamePrice = rs.getString("PRICE");

        // Create a new ImageView and Label & store in an arrayList
        imageList.add(new ImageView(image));
        labelList.add(new Label(gameTitle));
        priceList.add(new Label(gamePrice));

        // Create a Game object for each game entry obtained from the database.
        if (!gamesList.contains(new Game(gameTitle, image))) {
          gamesList.add(new Game(gameTitle, image));
          System.out.println("Added: " + gameTitle);
        } else {
          System.out.println("Duplicate found");
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void showGames() {

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

      Label gamePrice = priceList.get(i);
      gamePrice.getStyleClass().add("gamePrice");

      // Create a VBox for each game to contain the ImageView and Label
      VBox vBox = new VBox();
      vBox.setAlignment(Pos.TOP_CENTER);
      vBox.getStyleClass().add("gameBox");

      // Add the ImageView and Label to the VBox
      vBox.getChildren().addAll(labelList.get(i), imageList.get(i), priceList.get(i));

      // Add and OnMouseClicked Event on each game listing
      vBox.setOnMouseClicked(
          e -> {
            // Create temporary copy of the label contained within the VBox
            Label title = (Label) vBox.getChildren().get(0);

            // Find the Game object of the selected game inside of the games ArrayList
            for (Game game : gamesList) {
              if (game.getGameTitle().equals(title.getText())) {
                cartList.add(game);
                System.out.println(game.getGameTitle() + " Added to cart.");
              }
            }
            currentUser.setCartList(cartList);
          });

      // Add the VBox containing the games image and title to the TilePane
      tpGames.getChildren().add(vBox);
    }
  }
  

  @FXML
  void handleAddGame(ActionEvent event) {

    Dialog<ButtonType> addGame = new Dialog<>();
    addGame.setTitle("Add a New Game");
    addGame.setHeaderText("Enter the necessary information to \nadd a new game to the system.");

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
        gamesList.add(
            new Game(
                txtTitle.getText(),
                cbGenre.getValue(),
                cbRating.getValue(),
                txtPrice.getText(),
                fileIn));

        // Insert new game into the database
        String sql = "INSERT INTO GAMES(GAME_TITLE, GAME_IMAGE) VALUES (?, ?)";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, txtTitle.getText());
        stmt.setBinaryStream(2, fileIn);
        stmt.executeUpdate();
        getGames();

        System.out.println("Size of games ArrayList: " + gamesList.size());

        // Test to show all of the Game objects stored within the games ArrayList.
        for (Game game : gamesList) {
          game.print();
          consoleText.setText("New game has been added Genre" + cbGenre.getValue());
          System.out.println("New game has been added Genre");
        }
      } catch (Exception ex) {
        ex.printStackTrace();
      }

    }

  }
}
