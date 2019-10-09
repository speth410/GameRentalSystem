package GameRentalSystem;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DashboardController {
  @FXML private TilePane tpGames;
  @FXML private ScrollPane spGames;
  @FXML private HBox hBox;
  @FXML private TableView<CartItem> tvCart;
  @FXML private TableColumn<CartItem, String> colTitle;
  @FXML private TableColumn<?, ?> colRemove;
  @FXML private Button btnCheckout;
  private Connection connection = null;
  private String currentUser = null;

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

  @FXML
  public void initialize() {

    // Get Connection from dbHandler
    connection = dbHandler.initializeDB();

    // Bind TilePanes dimensions to the ScrollPane that contains it
    tpGames.prefHeightProperty().bind(spGames.heightProperty());
    tpGames.prefWidthProperty().bind(spGames.widthProperty());

    // Retrieve and show games stored in the database
    getGames();

    // Test to show that the dashboard knows who is logged in.
    System.out.println("Logged in as: " + currentUser);
  }

  @FXML
  void handleRemoveClicked(MouseEvent event) {
    CartItem item = tvCart.getSelectionModel().getSelectedItem();
    tvCart.getItems().remove(item);
  }

  @FXML
  private void getGames() {
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
      }
      // Pass Labels and ImageViews to method to be drawn to the scene
      showGames(imageList, labelList);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void showGames(List<ImageView> imageList, List<Label> labelList) {

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
            tvCart.getColumns().add(colTitle);

            // Check to make sure the game doesn't already exist in the cart
            if (!tvCart.getItems().contains(item)) {
              tvCart.getItems().add(item);
            }
          });

      // Add the VBox containing the games image and title to the TilePane
      tpGames.getChildren().add(vBox);
    }
  }
}
